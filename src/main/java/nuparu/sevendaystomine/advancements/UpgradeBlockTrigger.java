package nuparu.sevendaystomine.advancements;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public class UpgradeBlockTrigger implements ICriterionTrigger {
	private final ResourceLocation RL;
	private final Map listeners = Maps.newHashMap();

	public UpgradeBlockTrigger(String parString) {
		super();
		RL = new ResourceLocation(parString);
	}

	public UpgradeBlockTrigger(ResourceLocation parRL) {
		super();
		RL = parRL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.minecraft.advancements.ICriterionTrigger#getId()
	 */
	@Override
	public ResourceLocation getId() {
		return RL;
	}

	@Override
	public void addListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener listener) {
		UpgradeBlockTrigger.Listeners myCustomTrigger$listeners = (Listeners) listeners.get(playerAdvancementsIn);

		if (myCustomTrigger$listeners == null) {
			myCustomTrigger$listeners = new UpgradeBlockTrigger.Listeners(playerAdvancementsIn);
			listeners.put(playerAdvancementsIn, myCustomTrigger$listeners);
		}

		myCustomTrigger$listeners.add(listener);
	}

	@Override
	public void removeListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener listener) {
		UpgradeBlockTrigger.Listeners tameanimaltrigger$listeners = (Listeners) listeners.get(playerAdvancementsIn);

		if (tameanimaltrigger$listeners != null) {
			tameanimaltrigger$listeners.remove(listener);

			if (tameanimaltrigger$listeners.isEmpty()) {
				listeners.remove(playerAdvancementsIn);
			}
		}
	}

	@Override
	public void removeAllListeners(PlayerAdvancements playerAdvancementsIn) {
		listeners.remove(playerAdvancementsIn);
	}

	/**
	 * Deserialize a ICriterionInstance of this trigger from the data in the JSON.
	 *
	 * @param json    the json
	 * @param context the context
	 * @return the tame bird trigger. instance
	 */
	@Override
	public UpgradeBlockTrigger.Instance deserializeInstance(JsonObject json, JsonDeserializationContext context) {
		Block block = null;

        if (json.has("block"))
        {
            ResourceLocation resourcelocation = new ResourceLocation(JsonUtils.getString(json, "block"));

            if (!Block.REGISTRY.containsKey(resourcelocation))
            {
                throw new JsonSyntaxException("Unknown block type '" + resourcelocation + "'");
            }

            block = Block.REGISTRY.getObject(resourcelocation);
        }
        Map < IProperty<?>, Object > map = null;

        if (json.has("state"))
        {
            if (block == null)
            {
                throw new JsonSyntaxException("Can't define block state without a specific block type");
            }

            BlockStateContainer blockstatecontainer = block.getBlockState();

            for (Entry<String, JsonElement> entry : JsonUtils.getJsonObject(json, "state").entrySet())
            {
                IProperty<?> iproperty = blockstatecontainer.getProperty(entry.getKey());

                if (iproperty == null)
                {
                    throw new JsonSyntaxException("Unknown block state property '" + (String)entry.getKey() + "' for block '" + Block.REGISTRY.getNameForObject(block) + "'");
                }

                String s = JsonUtils.getString(entry.getValue(), entry.getKey());
                Optional<?> optional = iproperty.parseValue(s);

                if (!optional.isPresent())
                {
                    throw new JsonSyntaxException("Invalid block state value '" + s + "' for property '" + (String)entry.getKey() + "' on block '" + Block.REGISTRY.getNameForObject(block) + "'");
                }

                if (map == null)
                {
                    map = Maps. < IProperty<?>, Object > newHashMap();
                }

                map.put(iproperty, optional.get());
            }
        }
        
		return new UpgradeBlockTrigger.Instance(block,map,getId());
	}

	/**
	 * Trigger.
	 *
	 * @param parPlayer the player
	 */
	public void trigger(EntityPlayerMP parPlayer, IBlockState state) {
		UpgradeBlockTrigger.Listeners tameanimaltrigger$listeners = (Listeners) listeners.get(parPlayer.getAdvancements());

		if (tameanimaltrigger$listeners != null) {
			tameanimaltrigger$listeners.trigger(parPlayer,state);
		}
	}

	public static class Instance extends AbstractCriterionInstance {

		 private final Block block;
         private final Map < IProperty<?>, Object > properties;
		/**
		 * Instantiates a new instance.
		 */
		public Instance(@Nullable Block block, @Nullable Map < IProperty<?>, Object > propertiesIn, ResourceLocation parRL) {
			super(parRL);
			   this.block = block;
               this.properties = propertiesIn;
		}

		/**
		 * Test.
		 *
		 * @return true, if successful
		 */
		public boolean test(IBlockState state)
        {
            if (this.block != null && state.getBlock() != this.block)
            {
                return false;
            }
            else
            {
                if (this.properties != null)
                {
                    for (Entry < IProperty<?>, Object > entry : this.properties.entrySet())
                    {
                        if (state.getValue(entry.getKey()) != entry.getValue())
                        {
                            return false;
                        }
                    }
                }
            }
            return true;
        }
	}

	static class Listeners {
		private final PlayerAdvancements playerAdvancements;

		private final Set<Listener> listeners = Sets.newHashSet();

		/**
		 * Instantiates a new listeners.
		 *
		 * @param playerAdvancementsIn the player advancements in
		 */
		public Listeners(PlayerAdvancements playerAdvancementsIn) {
			playerAdvancements = playerAdvancementsIn;
		}

		/**
		 * Checks if is empty.
		 *
		 * @return true, if is empty
		 */
		public boolean isEmpty() {
			return listeners.isEmpty();
		}

		/**
		 * Adds the listener.
		 *
		 * @param listener the listener
		 */
		public void add(ICriterionTrigger.Listener listener) {
			listeners.add(listener);
		}

		/**
		 * Removes the listener.
		 *
		 * @param listener the listener
		 */
		public void remove(ICriterionTrigger.Listener listener) {
			listeners.remove(listener);
		}

		/**
		 * Trigger.
		 *
		 * @param player the player
		 */
		public void trigger(EntityPlayerMP player, IBlockState state) {
			ArrayList<ICriterionTrigger.Listener> list = null;

			for (ICriterionTrigger.Listener listener : listeners) {
				if (((Instance) (listener.getCriterionInstance())).test(state)) {
					if (list == null) {
						list = Lists.newArrayList();
					}

					list.add(listener);
				}
			}

			if (list != null) {
				for (ICriterionTrigger.Listener listener1 : list) {
					listener1.grantCriterion(playerAdvancements);
				}
			}
		}
	}
}