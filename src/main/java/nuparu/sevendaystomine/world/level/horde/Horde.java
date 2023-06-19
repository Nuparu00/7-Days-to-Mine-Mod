package nuparu.sevendaystomine.world.level.horde;

import com.google.common.collect.Sets;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.util.INBTSerializable;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IExtendedEntity;
import nuparu.sevendaystomine.json.horde.HordeDataManager;
import nuparu.sevendaystomine.json.horde.HordeEntry;
import nuparu.sevendaystomine.json.horde.pool.HordePool;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public class Horde implements INBTSerializable<CompoundTag> {
    private static final Component HORDE_NAME_COMPONENT = Component.translatable("event.sevendaystomine.horde");
    private final ServerBossEvent bossEvent = new ServerBossEvent(HORDE_NAME_COMPONENT, BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.NOTCHED_10);
    private final RandomSource random = RandomSource.create();

    private int id;


    private HordeEntry hordeEntry;
    private ServerLevel level;

    private List<Entity> entities = new ArrayList<>();
    private BlockPos center;
    private int wave;
    private int maxWaves;
    private int waveTimer;
    private int age;
    private int originalPlayersCount;
    private float totalHealth;

    private boolean active = false;

    public Horde(HordeEntry hordeEntry, int id, ServerLevel level, BlockPos center, int originalPlayersCount) {
        this.hordeEntry = hordeEntry;
        this.id = id;
        this.level = level;
        this.center = center;
        this.originalPlayersCount = originalPlayersCount;
        this.maxWaves = hordeEntry.waves().get(level,random).intValue();
        this.bossEvent.setProgress(0.0F);
    }

    public Horde(ServerLevel level, CompoundTag tag) {
        this.level = level;
        deserializeNBT(tag);
        this.bossEvent.setProgress(0.0F);
    }

    /**
     * Called on the death of an entity, that is part of the horde
     * @param entity entity
     */
    public void onEntityDeath(Entity entity){

    }

    /**
     * Starts the horde
     */
    public void start(){
        active = true;
        SoundEvent startSound = getHordeEntry().trigger().sound();
        if(startSound != null){
            level.playSound(null,getCenter().getX(),getCenter().getY(),getCenter().getZ(),startSound, SoundSource.AMBIENT,1,1);
        }
        startWaveTimer();
    }

    /**
     * Starts the wave timer
     */
    public void startWaveTimer(){
        waveTimer = getHordeEntry().wavesDelay();
    }

    /**
     * Actually starts the wave
     */
    public void startWave(){
        Optional<HordePool> pool;
        if(getWave() == 0){
            pool = getHordeEntry().getStartPool(level,random);
        } else if(getWave() == getMaxWaves() -1){
            pool = getHordeEntry().getEndPool(level,random);
        }
        else{
            pool = getHordeEntry().getNormalPool(level,random);
        }
        if(pool.isPresent()) {
            summonPool(pool.get());
        }
        else{
            SevenDaysToMine.LOGGER.error("No pool found for wave " + getWave()+ "/" + getMaxWaves() + " of " + getHordeEntry().path().toString());
        }
        wave++;
    }

    /**
     * Summons the entities inside the pool
     * @param pool pool to get entities from
     */
    private void summonPool(HordePool pool){
        SoundEvent soundEvent = pool.soundEvent();
        if(soundEvent != null){
            level.playSound(null,getCenter().getX(),getCenter().getY(),getCenter().getZ(),soundEvent, SoundSource.AMBIENT,1,1);
        }
        int rolls = pool.rolls().get(level,random).intValue() * originalPlayersCount;
        for(int i = 0; i < rolls; i++){
            Optional<HordePool.Entry> entry = pool.getEntry(level, random);
            if(entry.isPresent()) {
                summonEntity(entry.get());
            }
            else{
                SevenDaysToMine.LOGGER.error("No entry found for pool of " + getHordeEntry().path().toString());
            }
        }
        this.totalHealth = getHealthOfLivingRaiders();
        updateBossbar();
    }

    /**
     * Summons a single entity from the horde pool entry
     * @param entry entry
     */
    private void summonEntity(HordePool.Entry entry) {
        if (entry.type().canSummon()) {
            Entity entity = entry.type().spawn(level, entry.nbt(), null, null, getSpawnPosFor(entry.type()), MobSpawnType.NATURAL, true, true);
            if(entity != null) {
                addEntity(entity);
            }
        } else {
            SevenDaysToMine.LOGGER.error("Cannot summon " + entry.type() + " for " + getHordeEntry().path().toString());
        }
    }

    /**
     * Makes an entity part of the horde
     * @param entity entity
     */
    public void addEntity(Entity entity){
        IExtendedEntity iee = CapabilityHelper.getExtendedEntity(entity);
        if(iee != null) {
            iee.setHorde(this);
            entities.add(entity);
        }
    }

    /**
     * Calculates the spawn position for the given entity type
     * @param type type
     * @return position to spawn at
     */
    private BlockPos getSpawnPosFor(EntityType<?> type){
        return getCenter();
    }

    @Nullable
    private BlockPos findRandomSpawnPos(int p_37708_, int p_37709_) {
        int i = p_37708_ == 0 ? 2 : 2 - p_37708_;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for(int i1 = 0; i1 < p_37709_; ++i1) {
            float f = this.level.random.nextFloat() * ((float)Math.PI * 2F);
            int j = this.center.getX() + Mth.floor(Mth.cos(f) * 32.0F * (float)i) + this.level.random.nextInt(5);
            int l = this.center.getZ() + Mth.floor(Mth.sin(f) * 32.0F * (float)i) + this.level.random.nextInt(5);
            int k = this.level.getHeight(Heightmap.Types.WORLD_SURFACE, j, l);
            blockpos$mutableblockpos.set(j, k, l);
            if (!this.level.isVillage(blockpos$mutableblockpos) || p_37708_ >= 2) {
                int j1 = 10;
                if (this.level.hasChunksAt(blockpos$mutableblockpos.getX() - 10, blockpos$mutableblockpos.getZ() - 10, blockpos$mutableblockpos.getX() + 10, blockpos$mutableblockpos.getZ() + 10) && this.level.isPositionEntityTicking(blockpos$mutableblockpos) && (NaturalSpawner.isSpawnPositionOk(SpawnPlacements.Type.ON_GROUND, this.level, blockpos$mutableblockpos, EntityType.RAVAGER) || this.level.getBlockState(blockpos$mutableblockpos.below()).is(Blocks.SNOW) && this.level.getBlockState(blockpos$mutableblockpos).isAir())) {
                    return blockpos$mutableblockpos;
                }
            }
        }

        return null;
    }

    /**
     * Removes entity from this horde
     * @param entity entity to remove
     */
    public void removeEntityFromHorde(Entity entity){
        entities.remove(entity);
        IExtendedEntity iee = CapabilityHelper.getExtendedEntity(entity);
        if(iee != null && iee.getHorde() == this) {
            iee.setHorde(null);
        }
        this.updateBossbar();
    }

    /**
     * End the horde, prevents new waves from starting (if there are any left)
     * and makes all surviving entities not part of the horde (however, they won't be killed)
     */
    public void end(){
        clearEntities();
        rewardPlayers();
        bossEvent.removeAllPlayers();
        HordeManager.getOrCreate(getLevel().getServer()).removeHorde(this);
    }

    /**
     * Rewards all players (if there are any rewards defined) who have not died during the horde
     */
    private void rewardPlayers() {
        if (hordeEntry.rewards() != null) {
            for (ServerPlayer player : bossEvent.getPlayers()) {
                int timeSinceDeath = player.getStats().getValue(Stats.CUSTOM.get(Stats.TIME_SINCE_DEATH));
                if (timeSinceDeath >= age) {
                    hordeEntry.rewards().grant(player);
                }
            }
        }
    }

    /**
     * Makes all the entities not part of the horde
     */
    public void clearEntities(){
        boolean kill = false;

        if(this.getHordeEntry().trigger() != null){
            kill = this.getHordeEntry().trigger().despawnEntities();
        }

        for(Entity entity : entities){
            IExtendedEntity iee = CapabilityHelper.getExtendedEntity(entity);
            if(iee != null && iee.getHorde() == this) {
                iee.setHorde(null);
            }
            if(kill){
                entity.remove(Entity.RemovalReason.KILLED);
            }
        }
        entities.clear();
    }

    /**
     * Updates the horde state
     */
    public void update(){
        if(!active) return;

        if (this.level.getDifficulty() == Difficulty.PEACEFUL) {
            this.end();
            return;
        }

        if(this.getHordeEntry().trigger() != null){
            HordeEntry.Trigger trigger = this.getHordeEntry().trigger();
            if(age > trigger.lifespan()){
                this.end();
            }
        }

        age++;

        if(waveTimer > 0){
            waveTimer--;
            if(getHordeEntry().wavesDelay() > 0) {
                this.bossEvent.setProgress(Mth.clamp((float) (getHordeEntry().wavesDelay() - this.waveTimer) / getHordeEntry().wavesDelay(), 0.0F, 1.0F));
            }
        }

        if(age % 20 == 0){
            this.updatePlayers();
        }

        if(waveTimer == 0 && wave < maxWaves){
            startWave();
            waveTimer = -1;
        } else if (waveTimer == -1 && age % 10 == 0 && getHealthOfLivingRaiders() == 0){
            onWaveEnd();
        }
    }

    /**
     * Called when a horde wave ends
     */
    public void onWaveEnd(){
        if(wave >= maxWaves){
            end();
            return;
        }
        clearEntities();
        startWaveTimer();
    }

    public void onPlayerStartTracking(ServerPlayer player, Entity entity){

    }

    public void onPlayerStopTracking(){

    }

    private Predicate<ServerPlayer> validPlayer() {
        return (p_37723_) -> {
            BlockPos blockpos = p_37723_.blockPosition();
            return p_37723_.isAlive() && blockpos.distSqr(center) <= 6400; //80 blocks = 5 chunks
        };
    }

    /**
     * Updates what players are considered to be affected by the horde
     */
    private void updatePlayers() {
        Set<ServerPlayer> set = Sets.newHashSet(this.bossEvent.getPlayers());
        List<ServerPlayer> list = this.level.getPlayers(this.validPlayer());

        for(ServerPlayer serverplayer : list) {
            if (!set.contains(serverplayer)) {
                this.bossEvent.addPlayer(serverplayer);
            }
        }

        for(ServerPlayer serverplayer1 : set) {
            if (!list.contains(serverplayer1)) {
                this.bossEvent.removePlayer(serverplayer1);
            }
        }
    }

    public void updateBossbar() {
        this.bossEvent.setProgress(Mth.clamp(totalHealth == 0 ? 0 : this.getHealthOfLivingRaiders() / this.totalHealth, 0.0F, 1.0F));
    }

    public float getHealthOfLivingRaiders() {
        float f = 0.0F;

        for(Entity entity : entities){
            if(entity instanceof LivingEntity livingEntity){
                f += livingEntity.getHealth();
            }
            else if (entity.isAlive()){
                f += 1;
            }
        }

        return f;
    }

    private void sendSoundToPlayer(ServerPlayer player, SoundEvent soundEvent, SoundSource soundSource, double x, double y, double z, float volume, float pitch, long seed){
        player.connection.send(new ClientboundSoundPacket(soundEvent, soundSource, x, y, z, volume, pitch, seed));
    }


    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putString("Horde",hordeEntry.path().toString());
        tag.putInt("Wave",wave);
        tag.putInt("MaxWaves",maxWaves);
        tag.putInt("WaveTimer",waveTimer);
        tag.putBoolean("Active",active);
        tag.putInt("Age",age);
        tag.putFloat("TotalHealth",totalHealth);
        tag.putInt("Id",id);
        tag.putLong("Center",getCenter().asLong());

        return tag;
    }


    public void deserializeNBT(CompoundTag nbt) {
        if(!nbt.contains("Horde", Tag.TAG_STRING) || !nbt.contains("Id", Tag.TAG_INT) || !nbt.contains("Center", Tag.TAG_LONG)){
            end();
            return;
        }
        ResourceLocation resourceLocation = new ResourceLocation(nbt.getString("Horde"));
        Optional<HordeEntry> optionalHorde = HordeDataManager.INSTANCE.getByRes(resourceLocation);
        if(optionalHorde.isEmpty()){
            end();
            return;
        }
        this.hordeEntry = optionalHorde.get();
        this.id = nbt.getInt("Id");
        this.setCenter(BlockPos.of(nbt.getLong("Center")));

        if(nbt.contains("Wave", Tag.TAG_INT)){
            this.wave = nbt.getInt("Wave");
        }

        if(nbt.contains("MaxWaves", Tag.TAG_INT)){
            this.maxWaves = nbt.getInt("MaxWaves");
        }

        if(nbt.contains("WaveTimer", Tag.TAG_INT)){
            this.waveTimer = nbt.getInt("WaveTimer");
        }

        if(nbt.contains("Active", Tag.TAG_BYTE)){
            this.active = nbt.getBoolean("Active");
        }

        if(nbt.contains("Age", Tag.TAG_INT)){
            this.age = nbt.getInt("Age");
        }

        if(nbt.contains("TotalHealth", Tag.TAG_FLOAT)){
            this.totalHealth = nbt.getFloat("TotalHealth");
        }
    }


    public HordeEntry getHordeEntry() {
        return hordeEntry;
    }

    public ServerLevel getLevel() {
        return level;
    }

    public BlockPos getCenter() {
        return center;
    }

    public int getWave() {
        return wave;
    }

    public int getMaxWaves() {
        return maxWaves;
    }

    public int getWaveTimer() {
        return waveTimer;
    }

    public void setCenter(BlockPos center) {
        this.center = center;
    }

    public void setWave(int wave) {
        this.wave = wave;
    }

    public void setWaveTimer(int waveTimer) {
        this.waveTimer = waveTimer;
    }

    public float getTotalHealth() {
        return this.totalHealth;
    }

    public int getId(){
        return this.id;
    }
}
