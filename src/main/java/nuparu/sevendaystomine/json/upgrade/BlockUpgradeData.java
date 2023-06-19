package nuparu.sevendaystomine.json.upgrade;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public record BlockUpgradeData(Block block, HashMap<String, String> properties) {

    public boolean test(BlockState state) {
        if (state.getBlock() != block) return false;

        for (Map.Entry<String, String> propertyEntry : properties.entrySet()) {
            String propertyName = propertyEntry.getKey();
            String propertyValue = propertyEntry.getValue();
            boolean hasProperty = false;
            for (Property<?> property : state.getProperties()) {
                if (property.getName().equals(propertyName)) {
                    hasProperty = true;
                    String value = state.getValue(property).toString();
                    System.out.println("Property " + propertyName + " value is " + value);
                    if (!value.equals(propertyValue)) {
                        return false;
                    }
                    break;
                }
            }
            if (!hasProperty) {
                return false;
            }
        }

        return true;
    }

    public  <T extends Comparable<T>> BlockState asBlockState() {
        BlockState state = block.defaultBlockState();

        for (Map.Entry<String, String> propertyEntry : properties.entrySet()) {
            String propertyName = propertyEntry.getKey();
            String propertyValue = propertyEntry.getValue();
            for (Property<?> property : state.getProperties()) {
                if (property.getName().equals(propertyName)) {
                    Optional<T> value = ((Property<T>) property).getValue(propertyValue);
                    if (value.isPresent()) {
                        state = state.setValue(((Property<T>) property), value.get());
                    } else {
                        System.out.println("Property is missing");
                    }
                }
            }
        }

        return state;
    }

    public <T extends Comparable<T>> BlockState asBlockState(UpgradeEntry entry, BlockState from) {
        BlockState state = asBlockState();

        for (String toCopy : entry.copy()) {
            Object valueFrom = null;
            for (Property<?> property : from.getProperties()) {
                if (property.getName().equals(toCopy)) {
                    valueFrom = from.getValue(property);
                }
            }

            if (valueFrom != null) {
                System.out.println("Property " + toCopy + " value is " + valueFrom);
                for (Property<?> property : state.getProperties()) {
                    if (property.getName().equals(toCopy)) {
                        state = state.setValue(((Property<T>) property), (T) valueFrom);
                    }
                }
            } else {
                System.out.println("Property is missing");
            }
        }


        return state;
    }
}
