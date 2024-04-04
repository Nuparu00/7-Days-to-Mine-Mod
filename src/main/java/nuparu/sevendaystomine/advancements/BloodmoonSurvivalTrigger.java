package nuparu.sevendaystomine.advancements;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.stats.StatList;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IExtendedPlayer;
import nuparu.sevendaystomine.config.ModConfig;

public class BloodmoonSurvivalTrigger implements ICriterionTrigger {
	private final ResourceLocation RL;
	private final Map listeners = Maps.newHashMap();

	public BloodmoonSurvivalTrigger(String parString) {
		super();
		RL = new ResourceLocation(parString);
	}

	public BloodmoonSurvivalTrigger(ResourceLocation parRL) {
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
		BloodmoonSurvivalTrigger.Listeners myCustomTrigger$listeners = (Listeners) listeners.get(playerAdvancementsIn);

		if (myCustomTrigger$listeners == null) {
			myCustomTrigger$listeners = new BloodmoonSurvivalTrigger.Listeners(playerAdvancementsIn);
			listeners.put(playerAdvancementsIn, myCustomTrigger$listeners);
		}

		myCustomTrigger$listeners.add(listener);
	}

	@Override
	public void removeListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener listener) {
		BloodmoonSurvivalTrigger.Listeners tameanimaltrigger$listeners = (Listeners) listeners
				.get(playerAdvancementsIn);

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
	public BloodmoonSurvivalTrigger.Instance deserializeInstance(JsonObject json, JsonDeserializationContext context) {
		int bloodmoon = -1;

		if (json.has("bloodmoon")) {
			bloodmoon = JsonUtils.getInt(json, "bloodmoon");
		} else {
			throw new JsonSyntaxException("Expected property <bloodmoon> for " + getId().toString());
		}

		return new BloodmoonSurvivalTrigger.Instance(bloodmoon, getId());
	}

	/**
	 * Trigger.
	 *
	 * @param parPlayer the player
	 */
	public void trigger(EntityPlayerMP parPlayer) {
		BloodmoonSurvivalTrigger.Listeners tameanimaltrigger$listeners = (Listeners) listeners
				.get(parPlayer.getAdvancements());

		if (tameanimaltrigger$listeners != null) {
			tameanimaltrigger$listeners.trigger(parPlayer);
		}
	}

	public static class Instance extends AbstractCriterionInstance {

		private final int bloodmoon;

		/**
		 * Instantiates a new instance.
		 */
		public Instance(int bloodmoon, ResourceLocation parRL) {
			super(parRL);
			this.bloodmoon = bloodmoon;
		}

		/**
		 * Test.
		 *
		 * @return true, if successful
		 */
		public boolean test(EntityPlayerMP player) {
			World world = player.world;
			int lastDeath = player.getStatFile().readStat(StatList.TIME_SINCE_DEATH);
			if(lastDeath < 10000) return false;
			IExtendedPlayer iep = CapabilityHelper.getExtendedPlayer(player);
			int survived = iep.getSurvivedBloodmons()+1;
			iep.setSurvivedBloodmoons(survived);
			return (ModConfig.world.bloodmoonFrequency > 0 && survived >= bloodmoon);

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
		public void trigger(EntityPlayerMP player) {
			ArrayList<ICriterionTrigger.Listener> list = null;

			for (ICriterionTrigger.Listener listener : listeners) {
				if (((Instance) (listener.getCriterionInstance())).test(player)) {
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