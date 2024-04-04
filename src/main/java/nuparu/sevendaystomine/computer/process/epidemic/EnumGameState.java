package nuparu.sevendaystomine.computer.process.epidemic;

import net.minecraft.util.ResourceLocation;
import nuparu.sevendaystomine.SevenDaysToMine;

public enum EnumGameState {
	MENU("menu", new ResourceLocation(SevenDaysToMine.MODID, "textures/backgrounds/epidemic_menu.png")),
	CREATE("create", new ResourceLocation(SevenDaysToMine.MODID, "textures/backgrounds/epidemic_menu.png")),
	GAME("game", new ResourceLocation(SevenDaysToMine.MODID, "textures/backgrounds/epidemic_game.png")),
	SELECT_START("select_start", new ResourceLocation(SevenDaysToMine.MODID, "textures/backgrounds/epidemic_game.png")),
	WORLD("world", new ResourceLocation(SevenDaysToMine.MODID, "textures/backgrounds/epidemic_menu.png")),
	DISEASE("disease", new ResourceLocation(SevenDaysToMine.MODID, "textures/backgrounds/epidemic_menu.png")),
	INFECTED_MAP("infected_map", new ResourceLocation(SevenDaysToMine.MODID, "textures/backgrounds/epidemic_game.png")),
	END("end", new ResourceLocation(SevenDaysToMine.MODID, "textures/backgrounds/epidemic_menu.png"));

	String name;
	public final ResourceLocation background;

	EnumGameState(String name, ResourceLocation background) {
		this.name = name;
		this.background = background;
	}

	public static EnumGameState getByName(String name) {
		for (EnumGameState state : EnumGameState.values()) {
			if (state.name.equals(name))
				return state;
		}
		return MENU;
	}

	public String getName() {
		return name;
	}
}