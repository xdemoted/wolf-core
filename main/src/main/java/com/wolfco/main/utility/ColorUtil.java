package com.wolfco.main.utility;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import net.kyori.adventure.text.format.NamedTextColor;

public class ColorUtil {

	final static Map<NamedTextColor, ColorSet> colorMap = new HashMap<>();

	static {
		colorMap.put(NamedTextColor.BLACK, new ColorSet(0, 0, 0));
		colorMap.put(NamedTextColor.DARK_BLUE, new ColorSet(0, 0, 170));
		colorMap.put(NamedTextColor.DARK_GREEN, new ColorSet(0, 170, 0));
		colorMap.put(NamedTextColor.DARK_AQUA, new ColorSet(0, 170, 170));
		colorMap.put(NamedTextColor.DARK_RED, new ColorSet(170, 0, 0));
		colorMap.put(NamedTextColor.DARK_PURPLE, new ColorSet(170, 0, 170));
		colorMap.put(NamedTextColor.GOLD, new ColorSet(255, 170, 0));
		colorMap.put(NamedTextColor.GRAY, new ColorSet(170, 170, 170));
		colorMap.put(NamedTextColor.DARK_GRAY, new ColorSet(85, 85, 85));
		colorMap.put(NamedTextColor.BLUE, new ColorSet(85, 85, 255));
		colorMap.put(NamedTextColor.GREEN, new ColorSet(85, 255, 85));
		colorMap.put(NamedTextColor.AQUA, new ColorSet(85, 255, 255));
		colorMap.put(NamedTextColor.RED, new ColorSet(255, 85, 85));
		colorMap.put(NamedTextColor.LIGHT_PURPLE, new ColorSet(255, 85, 255));
		colorMap.put(NamedTextColor.YELLOW, new ColorSet(255, 255, 85));
		colorMap.put(NamedTextColor.WHITE, new ColorSet(255, 255, 255));
	}

	private static class ColorSet {
		Integer red, green, blue;

		ColorSet(Integer red, Integer green, Integer blue) {
			this.red = red;
			this.green = green;
			this.blue = blue;
		}

		public Integer getRed() {
			return red;
		}

		public Integer getGreen() {
			return green;
		}

		public Integer getBlue() {
			return blue;
		}

	}

	public static NamedTextColor fromRGB(int r, int g, int b) {
		TreeMap<Integer, NamedTextColor> closest = new TreeMap<>();
		colorMap.forEach((color, set) -> {
			int red = Math.abs(r - set.getRed());
			int green = Math.abs(g - set.getGreen());
			int blue = Math.abs(b - set.getBlue());
			closest.put(red + green + blue, color);
		});
		return closest.firstEntry().getValue();
	}
}