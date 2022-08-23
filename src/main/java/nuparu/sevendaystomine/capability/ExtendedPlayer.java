package nuparu.sevendaystomine.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import nuparu.sevendaystomine.config.ServerConfig;
import nuparu.sevendaystomine.init.ModDamageSources;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.messages.ExtendedPlayerSyncMessage;

import java.util.ArrayList;
import java.util.List;

public class ExtendedPlayer implements IExtendedPlayer{

    private List<String> recipes = new ArrayList<>();
    private int waterLevel = 20;
    private float saturationLevel;
    private float exhaustionLevel;
    private int tickTimer;
    private int lastWaterLevel = 20;
    int drinkCounter;



    int infectionTime = -1;

    private Player owner;
    @Override
    public void setWaterLevel(int level) {
        this.waterLevel = level;
        onDataChanged();
    }

    @Override
    public void consumeWaterLevel(int level) {
        this.waterLevel =  Math.min(Math.max(waterLevel-level,0),20);
        onDataChanged();
    }

    @Override
    public void addWaterLevel(int level) {
        this.waterLevel = Math.min(Math.max(waterLevel+level,0),20);
        this.saturationLevel = Math.min(Math.max(waterLevel+level,0),20)/2f;
        onDataChanged();
    }

    @Override
    public int getWaterLevel() {
        return waterLevel;
    }

    @Override
    public int getMaxWaterLevel() {
        return 20;
    }

    @Override
    public void causeExhaustion(float delta) {
        if (!owner.getAbilities().invulnerable) {
            if (!owner.level.isClientSide) {
                this.exhaustionLevel = Math.min(this.exhaustionLevel + delta, 40.0F);
            }

        }
    }

    public float getSaturationLevel() {
        return this.saturationLevel;
    }

    @Override
    public void unlockRecipe(String recipe) {
        if (hasRecipe(recipe))
            return;
        recipes.add(recipe);
        onDataChanged();
    }

    @Override
    public void forgetRecipe(String recipe) {
        if (!hasRecipe(recipe))
            return;
        recipes.remove(recipe);
        onDataChanged();
    }

    @Override
    public boolean hasRecipe(String recipe) {
        return recipes.contains(recipe);
    }

    @Override
    public List<String> getRecipes() {
        return recipes;
    }

    @Override
    public IExtendedPlayer setOwner(Player player) {
        this.owner = player;
        return this;
    }

    @Override
    public Player getOwner() {
        return owner;
    }

    @Override
    public void readNBT(CompoundTag nbt) {
        if (nbt.contains("waterLevel", 99)) {
            this.waterLevel = nbt.getInt("waterLevel");
            this.tickTimer = nbt.getInt("waterTickTimer");
            this.saturationLevel = nbt.getFloat("waterSaturationLevel");
            this.exhaustionLevel = nbt.getFloat("waterExhaustionLevel");
        }
        infectionTime = nbt.getInt("infectionTime");

        if (nbt.contains("recipes", Tag.TAG_LIST)) {
            recipes.clear();
            ListTag list = nbt.getList("recipes", Tag.TAG_STRING);
            for (int i = 0; i < list.size(); i++) {
                recipes.add(list.getString(i));
            }
        }
    }

    @Override
    public CompoundTag writeNBT(CompoundTag nbt) {
        nbt.putInt("waterLevel", this.waterLevel);
        nbt.putInt("waterTickTimer", this.tickTimer);
        nbt.putFloat("waterSaturationLevel", this.saturationLevel);
        nbt.putFloat("waterExhaustionLevel", this.exhaustionLevel);
        nbt.putInt("infectionTime", infectionTime);

        ListTag list = new ListTag();
        for (String rec : recipes) {
            list.add(StringTag.valueOf(rec));
        }
        nbt.put("recipes", list);
        return nbt;
    }

    @Override
    public void copy(IExtendedPlayer iep) {
        readNBT(iep.writeNBT(new CompoundTag()));
    }

    @Override
    public void onDataChanged() {
        if(!owner.level.isClientSide()){
            ExtendedPlayerSyncMessage message = new ExtendedPlayerSyncMessage(this, owner);

            PacketManager.sendToTrackingEntity(PacketManager.extendedPlayerSync, message, () -> owner);
            PacketManager.sendTo(PacketManager.extendedPlayerSync, message, (ServerPlayer) owner);
        }
    }

    @Override
    public void onStartedTracking(Player tracker) {
        if (!owner.level.isClientSide()) {
            ExtendedPlayerSyncMessage message = new ExtendedPlayerSyncMessage(this, owner);
            PacketManager.sendTo(PacketManager.extendedPlayerSync, message, (ServerPlayer) tracker);
        }
    }

    @Override
    public void tick(Player player){
        boolean dirty = false;

        if(getDrinkCounter() > 0){
            setDrinkCounter(getDrinkCounter()-1);
        }

        if(ServerConfig.thirst.get()) {
            Difficulty difficulty = player.level.getDifficulty();
            this.lastWaterLevel = this.waterLevel;
            if (this.exhaustionLevel > 4.0F) {
                this.exhaustionLevel -= 4.0F;
                if (this.saturationLevel > 0.0F) {
                    this.saturationLevel = Math.max(this.saturationLevel - 1.0F, 0.0F);
                } else if (difficulty != Difficulty.PEACEFUL) {
                    this.waterLevel = Math.max(this.waterLevel - 1, 0);
                }
                dirty = true;
            }
            if (this.waterLevel <= 0) {
                ++this.tickTimer;
                if (this.tickTimer >= 80) {
                    if (player.getHealth() > 10.0F || difficulty == Difficulty.HARD || player.getHealth() > 1.0F && difficulty == Difficulty.NORMAL) {
                        player.hurt(ModDamageSources.thirst, 1.0F);
                    }

                    this.tickTimer = 0;
                }
            } else {
                this.tickTimer = 0;
            }

            if (player.level.getDifficulty() == Difficulty.PEACEFUL && player.level.getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION) && this.tickTimer % 10 == 0 && waterLevel < getMaxWaterLevel()) {
                waterLevel += 1;
                dirty = true;
            }
        }

        if(dirty){
            onDataChanged();
        }
    }

    public void setDrinkCounter(int value){
        drinkCounter = value;
    }

    public int getDrinkCounter() {
        return drinkCounter;
    }

    @Override
    public boolean isInfected() {
        return infectionTime != -1;
    }

    @Override
    public int getInfectionTime() {
        return this.infectionTime;
    }

    @Override
    public void setInfectionTime(int time) {
        this.infectionTime = time;
        onDataChanged();
    }
}
