package nuparu.sevendaystomine.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import nuparu.sevendaystomine.world.level.horde.Horde;
import nuparu.sevendaystomine.world.level.horde.HordeManager;

import java.util.Optional;

public class ExtendedEntity implements IExtendedEntity{

    private Horde horde;
    private Entity owner;

    @Override
    public Horde getHorde() {
        return horde;
    }

    @Override
    public void setHorde(Horde horde) {
        this.horde = horde;
    }

    @Override
    public boolean hasHorde() {
        return horde != null;
    }

    @Override
    public void readNBT(CompoundTag nbt) {
        if(owner == null || owner.getLevel().isClientSide()) return;

        if(nbt.contains("HordeId",Tag.TAG_INT)){
            int hordeId = nbt.getInt("HordeId");
            MinecraftServer server = owner.getServer();
            HordeManager hordeManager = HordeManager.getOrCreate(server);
            Optional<Horde> hordeOptional = hordeManager.getHorde(hordeId);
            if(hordeOptional.isPresent()){
                Horde horde = hordeOptional.get();
                horde.addEntity(owner);
                horde.updateBossbar();
            }
            else{
                setHorde(null);
            }
        }
    }

    @Override
    public CompoundTag writeNBT(CompoundTag nbt) {
        if(hasHorde()) {
            nbt.putInt("HordeId",getHorde().getId());
        }
        return nbt;
    }

    @Override
    public void copy(IExtendedEntity iee) {
        readNBT(iee.writeNBT(new CompoundTag()));
    }

    @Override
    public Entity getOwner() {
        return owner;
    }

    @Override
    public void setOwner(Entity owner) {
        this.owner = owner;
    }
}
