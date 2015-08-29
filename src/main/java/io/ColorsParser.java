package io;

import java.awt.Color;
import java.util.Map;
import java.util.TreeMap;

/**
 * Color parsing class. Used for parsing formatted string to a color object.
 */
public class ColorsParser {
    /**
     * Get color from string.
     *
     * @param strColorDef a string representing the background color
     * @return parsed color
     * @throws FormatException problem with file format
     */
    public static Color colorFromString(String strColorDef)
            throws FormatException {
        FormatException formatException = new FormatException();
        // If by RGB - create the color
        if (strColorDef.startsWith("RGB")) {
            String rgbValue = strColorDef.split("\\(")[1];
            String[] rgbStrings = rgbValue.split(",");
            if (rgbStrings.length != 3) {
                throw formatException; // must have 3 fields
            }

            // Check if format is ok and split
            int red, green, blue;
            try {
                red = Integer.decode(rgbStrings[0]);
                green = Integer.decode(rgbStrings[1]);
                blue = Integer.decode(rgbStrings[2]);
            } catch (NumberFormatException e) {
                throw formatException;
            }
            return new Color(red, green, blue);
        }

        // If by color name - get the color from mapping and return it
        return mapColors().get(strColorDef);
    }

    /**
     * Maps color strings to color objects.
     *
     * @return color mapping
     */
    private static Map<String, Color> mapColors() {
        Map<String, Color> colorMap = new TreeMap<String, Color>();
        colorMap.put("black", Color.BLACK);
        colorMap.put("blue", Color.BLUE);
        colorMap.put("cyan", Color.CYAN);
        colorMap.put("gray", Color.GRAY);
        colorMap.put("lightGray", Color.LIGHT_GRAY);
        colorMap.put("green", Color.GREEN);
        colorMap.put("orange", Color.ORANGE);
        colorMap.put("pink", Color.PINK);
        colorMap.put("red", Color.RED);
        colorMap.put("white", Color.WHITE);
        colorMap.put("yellow", Color.YELLOW);
        return colorMap;
    }
}
