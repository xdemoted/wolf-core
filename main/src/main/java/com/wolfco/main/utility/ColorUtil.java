package com.wolfco.main.utility;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.ChatColor;

public class ColorUtil {

	final static Map<ChatColor, ColorSet> colorMap = new HashMap<>();

	static {
		colorMap.put(ChatColor.BLACK, new ColorSet(0, 0, 0));
		colorMap.put(ChatColor.DARK_BLUE, new ColorSet(0, 0, 170));
		colorMap.put(ChatColor.DARK_GREEN, new ColorSet(0, 170, 0));
		colorMap.put(ChatColor.DARK_AQUA, new ColorSet(0, 170, 170));
		colorMap.put(ChatColor.DARK_RED, new ColorSet(170, 0, 0));
		colorMap.put(ChatColor.DARK_PURPLE, new ColorSet(170, 0, 170));
		colorMap.put(ChatColor.GOLD, new ColorSet(255, 170, 0));
		colorMap.put(ChatColor.GRAY, new ColorSet(170, 170, 170));
		colorMap.put(ChatColor.DARK_GRAY, new ColorSet(85, 85, 85));
		colorMap.put(ChatColor.BLUE, new ColorSet(85, 85, 255));
		colorMap.put(ChatColor.GREEN, new ColorSet(85, 255, 85));
		colorMap.put(ChatColor.AQUA, new ColorSet(85, 255, 255));
		colorMap.put(ChatColor.RED, new ColorSet(255, 85, 85));
		colorMap.put(ChatColor.LIGHT_PURPLE, new ColorSet(255, 85, 255));
		colorMap.put(ChatColor.YELLOW, new ColorSet(255, 255, 85));
		colorMap.put(ChatColor.WHITE, new ColorSet(255, 255, 255));
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

	public static ChatColor fromRGB(int r, int g, int b) {
		TreeMap<Integer, ChatColor> closest = new TreeMap<>();
		colorMap.forEach((color, set) -> {
			int red = Math.abs(r - set.getRed());
			int green = Math.abs(g - set.getGreen());
			int blue = Math.abs(b - set.getBlue());
			closest.put(red + green + blue, color);
		});
		return closest.firstEntry().getValue();
	}

	public static ChatColor parseCode(String colorCode) {
		return parseCode(colorCode, "§");
	}

	public static ChatColor parseCode(String colorCode, String colorChar) {
		String reducedMessage = colorCode.replaceAll(colorChar, ""); // x000000 | 0 | empty

		if (reducedMessage.length() == 1) {
			return ChatColor.getByChar(reducedMessage.charAt(0));
		} else if (reducedMessage.length() == 7 && reducedMessage.startsWith("x")) {
			int r = Integer.parseInt(reducedMessage.substring(1, 3), 16);
			int g = Integer.parseInt(reducedMessage.substring(3, 5), 16);
			int b = Integer.parseInt(reducedMessage.substring(5, 7), 16);
			return fromRGB(r, g, b);
		} else {
			return ChatColor.WHITE;
		}
	}

}