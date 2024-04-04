package nuparu.sevendaystomine.util;

import java.util.ArrayList;

public class ColorDetector {

	public ArrayList<ColorEntry> colorList = new ArrayList<ColorEntry>();

	public static final ColorDetector INSTANCE = new ColorDetector();

	public ColorDetector() {
		colorList.add(new ColorEntry(255, 0, 0, "color.red"));
		colorList.add(new ColorEntry(0, 255, 0, "color.green"));
		colorList.add(new ColorEntry(0, 0, 255, "color.blue"));
		colorList.add(new ColorEntry(255, 255, 255, "color.white"));
		colorList.add(new ColorEntry(0, 0, 0, "color.black"));
		colorList.add(new ColorEntry(128, 0, 128, "color.purple"));
		colorList.add(new ColorEntry(255, 0, 255, "color.magenta"));
		colorList.add(new ColorEntry(255, 192, 203, "color.pink"));
		colorList.add(new ColorEntry(173, 216, 230, "color.light_blue"));
		colorList.add(new ColorEntry(0, 255, 255, "color.cyan"));
		colorList.add(new ColorEntry(0, 201, 87, "color.emerald_green"));
		colorList.add(new ColorEntry(50, 205, 50, "color.lime_green"));
		colorList.add(new ColorEntry(100, 0, 0, "color.dark_red"));
		colorList.add(new ColorEntry(0, 100, 0, "color.dark_green"));
		colorList.add(new ColorEntry(0, 0, 100, "color.dark_blue"));
		colorList.add(new ColorEntry(255, 255, 0, "color.yellow"));
		colorList.add(new ColorEntry(255, 215, 0, "color.gold"));
		colorList.add(new ColorEntry(255, 165, 0, "color.orange"));
		colorList.add(new ColorEntry(20, 20, 20, "color.gray"));

	}

	public String getColorMatch(float r, float g, float b) {
		double lastMatch = Double.MAX_VALUE;
		String color = null;
		for (int i = 0; i < colorList.size(); i++) {
			ColorEntry entry = colorList.get(i);
			double match = entry.colorDifference(r, g, b);
			if (match < lastMatch) {
				lastMatch = match;
				color = entry.name;
			}
		}
		return color;
	}

	public static class ColorEntry {
		float r = 0;
		float g = 0;
		float b = 0;
		String name;

		public ColorEntry(float r, float g, float b, String name) {
			this.r = r;
			this.g = g;
			this.b = b;
			this.name = name;
		}

		public double colorDifference(float inR, float inG, float inB) {
			long rmean = ((long) r + (long) inR) / 2;
			long outR = (long) r - (long) inR;
			long outG = (long) g - (long) inG;
			long outB = (long) b - (long) inB;
			return Math.sqrt(
					(((512 + rmean) * outR * outR) >> 8) + 4 * outG * outG + (((767 - rmean) * outB * outB) >> 8));
		}
	}

}
