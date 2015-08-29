package io;

import sprites.Fill;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

/**
 * Reads and parses the blocks definitions file.
 */
public class BlocksDefinitionReader {
    private BlocksFromSymbolsFactory blocksFactory =
            new BlocksFromSymbolsFactory();

    /**
     * Creates the blocks factory from file reader.
     *
     * @param reader the file reader
     * @return the prepared factory
     * @throws FormatException wrong file format
     * @throws IOException problem with file
     */
    public BlocksFromSymbolsFactory fromReader(java.io.Reader reader)
            throws FormatException, IOException {
        BufferedReader inputStream;
        inputStream = new BufferedReader(reader);

        // Read a line of data and parse it
        String line;
        while ((line = inputStream.readLine()) != null) {
            // Skip the line if needed
            if (line.startsWith("#") || line.trim().isEmpty()) {
                continue;
            }
            parseDefinitionsLine(line);
        }
        return blocksFactory;
    }

    /**
     * Parses one line of definitions - block, spacer or defaults.
     *
     * @param definitionsLine a line of definitions
     * @throws FormatException wrong format
     * @throws IOException problem with file
     */
    private void parseDefinitionsLine(String definitionsLine)
            throws FormatException, IOException {
        FormatException formatException = new FormatException();
        // Split the line to tag and definitions array
        String[] lineSplit = definitionsLine.split(" ", 2);
        if (lineSplit.length < 2) {
            throw formatException;
        }
        String tag = lineSplit[0];
        String[] params = lineSplit[1].split(" ");
        if (params.length == 0) {
            throw formatException;
        }

        // Default definitions
        if (tag.contentEquals("default")) {
            parseDefaults(params);

        // Block definitions
        } else if (tag.contentEquals("bdef")) {
            parseBlockDef(params);


        // Spacers definitions
        } else if (tag.contentEquals("sdef")) {
            parseSpacer(params);
        }
    }

    /**
     * Parses a block definitions array of strings.
     *
     * @param blockParams block parameters as strings to parse
     * @throws FormatException wrong format
     * @throws IOException problem with file
     */
    private void parseBlockDef(String[] blockParams)
            throws FormatException, IOException {
        FormatException formatException = new FormatException();
        Map<Integer, Fill> fillsMap = new TreeMap<Integer, Fill>();
        Map<String, Object> blockDefMap = new TreeMap<String, Object>();

        // Get spacer data
        for (String param : blockParams) {
            parseBlockParameter(fillsMap, blockDefMap, param);
        }

        // Check if we have the symbol itself
        if (!blockDefMap.containsKey("symbol")) {
            throw formatException;
        }

        // Save fills only if we have them
        if (!fillsMap.isEmpty()) {
            blockDefMap.put("fill-k", fillsMap);
        }

        blocksFactory.addBlockCreator(blockDefMap);
    }

    /**
     * Parses a block parameter and adds it to mapping.
     *
     * @param fillsMap    mapping with different fills
     * @param blockDefMap mapping to add parameter to
     * @param param       the parameter as a string
     * @throws FormatException wrong format
     * @throws IOException problems with file
     */
    private void parseBlockParameter(Map<Integer, Fill> fillsMap,
                                     Map<String, Object> blockDefMap,
                                     String param)
            throws FormatException, IOException {
        FormatException formatException = new FormatException();

        // Split tag from value. Make sure we have both of them
        String[] paramsSplit = param.split(":");
        if (paramsSplit.length != 2) {
            throw formatException;
        }
        String tag = paramsSplit[0];
        String value = paramsSplit[1];

        // Parse the parameter according to it's tag

        if (tag.contentEquals("symbol")) {
            // If legal symbol - save
            if (value.length() != 1) {
                throw formatException;
            }
            blockDefMap.put(tag, value);

        } else if (tag.contentEquals("height")
                || tag.contentEquals("width")
                || tag.contentEquals("hit_points")) {
            // If legal number - save
            if (!value.matches("[0-9]+")) {
                throw formatException;
            }
            blockDefMap.put(tag, Integer.decode(value));

        } else if (tag.contentEquals("fill")) {
            blockDefMap.put("fill", FillParser.fillFromString(value));

        } else if (tag.startsWith("fill-")) {
            // If legal fill - save
            if (tag.contentEquals("fill-")) {
                throw formatException;
            }
            Integer fillPoint = Integer.decode(tag.split("-")[1]);
            Fill fillK = FillParser.fillFromString(value);
            fillsMap.put(fillPoint, fillK);

        } else if (tag.contentEquals("stroke")) {
            blockDefMap.put(tag,
                    ColorsParser.colorFromString(value));

        } else {
            throw formatException;
        }
    }

    /**
     * Parses spacer data and adds it to factory.
     *
     * @param spacerParams the spacer parameters
     * @throws FormatException wrong format
     */
    private void parseSpacer(String[] spacerParams) throws FormatException {
        FormatException formatException = new FormatException();
        String symbol = null;
        Integer spacerWidth = null;

        // Get spacer data
        for (String param : spacerParams) {
            String[] paramsSplit = param.split(":");
            if (paramsSplit.length != 2) {
                throw formatException;
            }

            String paramTag = paramsSplit[0];
            String paramValue = paramsSplit[1];

            if (paramTag.contentEquals("symbol")) {
                symbol = paramValue;

            } else if (paramTag.contentEquals("width")) {
                spacerWidth = Integer.decode(paramValue);

            } else {
                throw formatException;
            }
        }

        // Check if we have all the needed data
        if (symbol == null || spacerWidth == null) {
            throw formatException;
        }

        blocksFactory.addSpacer(symbol, spacerWidth);
    }

    /**
     * Parses the defaults line and updates the blocks factory.
     *
     * @param defs defaults string
     * @throws FormatException wrong format
     * @throws IOException problems with file
     */
    private void parseDefaults(String[] defs)
            throws FormatException, IOException {
        FormatException formatException = new FormatException();

        // Go over the default parameters we have
        for (String def : defs) {
            // Get the type and the data of the parameter and add to factory
            String[] defParams = def.split(":");
            if (defParams.length != 2) {
                throw formatException;
            }
            addDefaultParameter(defParams[0], defParams[1]);
        }
    }

    /**
     * Gets the default type and data and adds it to block factory.
     *
     * @param type default type
     * @param data default data
     * @throws FormatException wrong format
     * @throws IOException problems with file
     */
    private void addDefaultParameter(String type, String data)
            throws FormatException, IOException {
        // Update the relevant default parameter in the blocks factory
        if (type.contentEquals("height")) {
            Integer height = Integer.decode(data);
            blocksFactory.setDefHeight(height);

        } else if (type.contentEquals("width")) {
            Integer width = Integer.decode(data);
            blocksFactory.setDefWidth(width);

        } else if (type.contentEquals("stroke")) {
            blocksFactory.setDefStrokeColor(ColorsParser
                    .colorFromString(data));

        } else if (type.contentEquals("hit_points")) {
            blocksFactory.setDefHitPoints(Integer.decode(data));

        } else if (type.contentEquals("fill")) {
            Fill defFill = FillParser.fillFromString(data);
            blocksFactory.setDefFill(defFill);

        } else if (type.startsWith("fill-")) {
            // Get the hit points for this fill
            String[] fillKSplit = type.split("-", 2);
            if (fillKSplit.length != 2) {
                throw new FormatException();
            }
            Integer fillPoints = Integer.decode(fillKSplit[1]);
            blocksFactory.addDefSpecialFill(fillPoints,
                    FillParser.fillFromString(data));
        }
    }
}




