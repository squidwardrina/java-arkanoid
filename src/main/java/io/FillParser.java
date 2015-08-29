package io;

import sprites.Fill;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;

/**
 * A parser for fill/background strings.
 */
public class FillParser {

    /**
     * Gets a fill string and returnes the new fill object.
     *
     * @param fillStr the fill / background string
     * @return new fill object
     * @throws FormatException wrong format
     * @throws IOException problem with file
     */
    public static Fill fillFromString(String fillStr)
            throws FormatException, IOException {
        FormatException formatException = new FormatException();
        String[] backgSpecs = fillStr.split("\\(", 2);
        if (backgSpecs.length < 2) {
            throw formatException;
        }
        String backgType = backgSpecs[0];
        String backgData = backgSpecs[1].replace(")", ""); // remove ")"

        // Check which type of fill we have
        if (backgType.contentEquals("color")) {
            return new Fill(ColorsParser.colorFromString(backgData));

        } else if (backgType.contentEquals("image")) {
            return createImageFill(backgData);

        } else { // wrong format
            throw formatException;
        }
    }

    /**
     * Creates a fill from image file specified.
     *
     * @param file the name of the file with the image
     * @return the new fill
     * @throws FormatException wrong format
     * @throws IOException problem with file
     */
    private static Fill createImageFill(String file) throws
            FormatException, IOException {
        Image backgImage;

        FormatException formatException = new FormatException(
                "Can't parse information - wrong file format " + file);

        // Read the image
        InputStream stream =
                ClassLoader.getSystemClassLoader().getResourceAsStream(file);
        if (stream == null) {
            throw formatException;
        }
        backgImage = ImageIO.read(stream);

        // If succeeded reading the image - return the new fill
        if (backgImage != null) {
            return new Fill(backgImage);
        } else {
            throw formatException;
        }
    }
}
