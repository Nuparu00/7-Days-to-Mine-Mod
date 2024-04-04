package nuparu.sevendaystomine.item;

import net.minecraft.util.text.TextFormatting;

public enum EnumQuality {
	NONE(Integer.MIN_VALUE, 0, TextFormatting.RESET),
	FAULTY(1, 99, TextFormatting.GRAY),
	POOR(100, 199,TextFormatting.GOLD),
	GOOD(200, 299, TextFormatting.YELLOW),
	FINE(300, 399, TextFormatting.GREEN),
	GREAT(400, 499, TextFormatting.BLUE),
	FLAWLESS(500, Integer.MAX_VALUE, TextFormatting.DARK_PURPLE);

	int minimumQuality;
	int maximumQuality;

	TextFormatting color;

	EnumQuality(int min, int max, TextFormatting color) {
		this.minimumQuality = min;
		this.maximumQuality = max;
		this.color = color;
	}

	public static EnumQuality getFromQuality(int i) {
		for (EnumQuality quality : EnumQuality.values()) {
			if (quality.minimumQuality <= i && i <= quality.maximumQuality) {
				return quality;
			}
		}
		return EnumQuality.NONE;
	}
	
	public static TextFormatting getColorFromInt(int i) {
		for(EnumQuality quality : EnumQuality.values()) {
			if (quality.minimumQuality <= i && i <= quality.maximumQuality) {
				return quality.color;
			}
		}
		return null;
	}
	
	public TextFormatting getColor() {
		return this.color;
	}

}