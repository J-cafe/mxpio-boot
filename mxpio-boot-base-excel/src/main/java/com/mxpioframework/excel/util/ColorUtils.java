package com.mxpioframework.excel.util;

import java.awt.Color;

public class ColorUtils {

	public static int[] parse2RGB(String c) {
		Color convertedColor = Color.ORANGE;
		if (c.startsWith("#")) {
			c = c.substring(1);
		} else if (c.startsWith("0x")) {
			c = c.substring(2);
		}
		try {
			convertedColor = new Color(Integer.parseInt(c, 16));
		} catch (NumberFormatException ignored) {
		}
        return new int[] { convertedColor.getRed(), convertedColor.getGreen(), convertedColor.getBlue() };
	}

}
