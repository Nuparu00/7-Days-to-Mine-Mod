package nuparu.sevendaystomine.capability;

import net.minecraft.nbt.CompoundTag;

import java.io.Serial;
import java.io.Serializable;

public class BreakData implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    long lastChange;
    float state;

    public BreakData(long lastChange, float state) {
        this.lastChange = lastChange;
        this.state = state;
    }

    public float getState(){
        return this.state;
    }
    public long getLastChange(){
        return this.lastChange;
    }

    public BreakData setState(float state) {
        this.state = state;
        return this;
    }

    public BreakData addState(float state) {
        this.state += state;
        return this;
    }

    public BreakData setLastChange(long lastChange) {
        this.lastChange = lastChange;
        return this;
    }

    public CompoundTag save(CompoundTag nbt) {
        nbt.putLong("lastChange", lastChange);
        nbt.putFloat("state", state);
        return nbt;
    }

    public BreakData read(CompoundTag nbt) {
        lastChange = nbt.getLong("lastChange");
        state = nbt.getFloat("state");
        return this;
    }

    public static BreakData of(CompoundTag nbt) {
        return new BreakData(nbt.getLong("lastChange"),nbt.getFloat("state"));
    }
}
