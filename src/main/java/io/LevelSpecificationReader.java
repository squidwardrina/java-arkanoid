package io;

import gameplay.LevelInformation;
import sprites.Background;
import sprites.Block;
import sprites.Sprite;
import sprites.ball.Velocity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Can read level specs file and create levels.
 */
public class LevelSpecificationReader {
    /**
     * Reads the level specs file and makes a list of levels informations.
     *
     * @param reader the level specs file reader
     * @return list of level informations
     * @throws FormatException wrong specification format
     * @throws IOException     problem with the file
     */
    public List<LevelInformation> fromReader(java.io.Reader reader)
            throws FormatException, IOException {
        List<LevelInformation> levels = new ArrayList<LevelInformation>();
        BufferedReader stream = new BufferedReader(reader);

        // Go over level sections and make levels
        String line = stream.readLine();
        while (line != null) {
            if (isToSkip(line)) {
                line = stream.readLine();
                continue;
            }
            if (line.contentEquals("START_LEVEL")) {
                levels.add(parseLevelInfo(stream));
            } else {
                throw new FormatException();
            }
            line = stream.readLine();
        }

        // Finished all the levels. Return the prepared list
        return levels;
    }

    /**
     * Checks if the line is to be skipped.
     *
     * @param specLine line to check
     * @return true/false
     */
    private boolean isToSkip(String specLine) {
        return specLine.startsWith("#") || specLine.trim().isEmpty();
    }

    /**
     * Reads a level from file and creates the level information object.
     *
     * @param stream a reader to get level from
     * @return the prepared level information
     * @throws IOException     problem reading file
     * @throws FormatException wrong format
     */
    private LevelInformation parseLevelInfo(BufferedReader stream) throws
            FormatException, IOException {
        FormatException wrongFormat = new FormatException();
        String levelName = null;
        List<Velocity> velocities = null;
        Sprite background = null;
        Integer paddleSpeed = null;
        Integer paddleWidth = null;
        Integer blocksStartX = null;
        Integer blocksStartY = null;
        Integer rowHeight = null;
        Integer numBlocks = null;
        String blocksFile = null;

        // Read lines until blocks section
        String line = stream.readLine();
        while (!line.contentEquals("START_BLOCKS")) {
            if (line == null) {
                throw wrongFormat;
            }
            if (isToSkip(line)) {
                continue;
            }

            // Separate the tag and the data
            String[] paramSplit = splitParameter(line);
            String tag = paramSplit[0];
            String value = paramSplit[1];

            // Update data according to the tag
            if (tag.contentEquals("level_name")) {
                levelName = value;
            } else if (tag.contentEquals("ball_velocities")) {
                velocities = parseVelocities(value);
            } else if (tag.contentEquals("background")) {
                background = parseBackground(value);
            } else if (tag.contentEquals("paddle_speed")) {
                paddleSpeed = Integer.decode(value);
            } else if (tag.contentEquals("paddle_width")) {
                paddleWidth = Integer.decode(value);
            } else if (tag.contentEquals("block_definitions")) {
                blocksFile = value;
            } else if (tag.contentEquals("blocks_start_x")) {
                blocksStartX = Integer.decode(value);
            } else if (tag.contentEquals("blocks_start_y")) {
                blocksStartY = Integer.decode(value);
            } else if (tag.contentEquals("row_height")) {
                rowHeight = Integer.decode(value);
            } else if (tag.contentEquals("num_blocks")) {
                numBlocks = Integer.decode(value);
            } else {
                throw wrongFormat;
            }
            line = stream.readLine();
        }

        // Create list of blocks
        List<Block> blocks = getBlocks(
                stream, blocksFile, blocksStartX, blocksStartY, rowHeight);

        // Verify we have all the needed fields
        if (levelName == null || velocities == null
                || background == null || paddleSpeed == null
                || paddleWidth == null || blocksFile == null
                || blocksStartX == null || blocksStartY == null
                || rowHeight == null || numBlocks == null) {
            throw new FormatException();
        }

        // Read lines until end of level
        line = stream.readLine();
        while (!line.contentEquals("END_LEVEL")) {
            if (!isToSkip(line)) {
                throw wrongFormat; // lines after the end of BLOCKS section
            }
            line = stream.readLine();
        }

        // Create the new level from parsed data and return it
        return createNewLevel(velocities, paddleSpeed, paddleWidth,
                levelName, background, blocks, numBlocks);
    }

    /**
     * Creates a level information object with the specified params.
     *
     * @param velocities  class parameter
     * @param paddleSpeed class parameter
     * @param paddleWidth class parameter
     * @param levelName   class parameter
     * @param background  class parameter
     * @param blocks      class parameter
     * @param numBlocks   class parameter
     * @return new level information object
     */
    private LevelInformation createNewLevel(final List<Velocity> velocities,
                                            final int paddleSpeed,
                                            final int paddleWidth,
                                            final String levelName,
                                            final Sprite background,
                                            final List<Block> blocks,
                                            final int numBlocks) {
        return new LevelInformation() {
            public int numberOfBalls() {
                return initialBallVelocities().size();
            }

            public List<Velocity> initialBallVelocities() {
                return velocities;
            }

            public int paddleSpeed() {
                return paddleSpeed;
            }

            public int paddleWidth() {
                return paddleWidth;
            }

            public String levelName() {
                return levelName;
            }

            public Sprite getBackground() {
                return background;
            }

            public List<Block> blocks() {
                return blocks;
            }

            public int numberOfBlocksToRemove() {
                return numBlocks;
            }
        };
    }

    /**
     * Parses string data into background sprite.
     *
     * @param fieldData the background string
     * @return the new sprite
     * @throws FormatException format problem
     * @throws IOException problem with file
     */
    private Sprite parseBackground(String fieldData)
            throws FormatException, IOException {
        return new Background(FillParser.fillFromString(fieldData));
    }

    /**
     * Parses the velocities data as one string to a list of velocities.
     *
     * @param stringToParse the velocities as a whole string
     * @return list of velocities
     * @throws FormatException wrong format
     */
    private List<Velocity> parseVelocities(String stringToParse)
            throws FormatException {
        List<Velocity> ballVelocities = new ArrayList<Velocity>();

        // Get array of strings, each one representing one velocity
        String[] strVelocities = stringToParse.split(" ");

        // For each velocity - parse and add to list
        for (String strVelocity : strVelocities) {
            ballVelocities.add(parseVelocity(strVelocity));
        }

        return ballVelocities;
    }

    /**
     * Parses a single velocity string to velocity object.
     *
     * @param strVelocity string representing velocity: (angle, speed)
     * @return new velocity
     * @throws FormatException wrong format
     */
    private Velocity parseVelocity(String strVelocity) throws FormatException {
        String[] velParams = strVelocity.split(",", 2); // get the params
        if (velParams.length < 2) {
            throw new FormatException();
        }

        double angle, speed;
        try {
            angle = Double.parseDouble(velParams[0]);
            speed = Double.parseDouble(velParams[1]);
        } catch (NumberFormatException e) {
            throw new FormatException();
        }
        return Velocity.fromAngleAndSpeed(angle, speed);
    }

    /**
     * Splits a single parameter string to strings array.
     *
     * @param parameter a parameter to split
     * @return array of 2 strings - parameter name and value
     * @throws FormatException if not 2 subsrings
     */
    private String[] splitParameter(String parameter) throws FormatException {
        String[] paramSplit = parameter.split(":");
        if (paramSplit.length != 2) {
            throw new FormatException();
        }
        return paramSplit;
    }

    /**
     * Creates a list of blocks of the level.
     *
     * @param stream       the file to create blocks from
     * @param blocksFile   the file of blocks definitions
     * @param blocksStartX start x position
     * @param blocksStartY start y position
     * @param rowHeight    height of one block row
     * @return the list of blocks
     * @throws FormatException wrong format
     * @throws IOException     problem reading from file
     */
    private List<Block> getBlocks(BufferedReader stream, String blocksFile,
                                  int blocksStartX, int blocksStartY,
                                  int rowHeight)
            throws FormatException, IOException {
        FormatException wrongFormat = new FormatException();
        List<Block> blocks = new ArrayList<Block>();
        String line;
        int xPos = blocksStartX;
        int yPos = blocksStartY;

        // Create the factory to create blocks
        BlocksFromSymbolsFactory blocksFactory = getBlocksFactory(blocksFile);

        // Read lines until end of blocks section
        line = stream.readLine();
        while (!line.contentEquals("END_BLOCKS")) {
            if (line == null) {
                throw wrongFormat;
            }
            if (isToSkip(line)) {
                continue;
            }

            createBlocksRow(
                    blocks, xPos, yPos, blocksFactory, line.toCharArray());

            // Next block row
            xPos = blocksStartX;
            yPos += rowHeight;
            line = stream.readLine();
        }

        return blocks;
    }

    /**
     * Creates the blocks factory according to the definitions file.
     *
     * @param blocksFile blocks definitions file
     * @return the new block factory
     * @throws FormatException wrong format
     * @throws IOException problem with file
     */
    private BlocksFromSymbolsFactory getBlocksFactory(String blocksFile)
            throws FormatException, IOException {
        BlocksDefinitionReader blocksReader = new BlocksDefinitionReader();
        InputStreamReader stream = null;
        BlocksFromSymbolsFactory blocksFactory = null;

        // Try to open the block definitions file and read it
        try {
            stream = new InputStreamReader(ClassLoader.getSystemClassLoader()
                    .getResourceAsStream(blocksFile));
            blocksFactory = blocksReader.fromReader(stream);

        } finally {
            if (stream != null) {
                stream.close();
            }
        }

        return blocksFactory;
    }

    /**
     * Creates one row of blocks.
     *
     * @param blocks        the list of blocks to update
     * @param xPos          the starting x position
     * @param yPos          the starting y position
     * @param blocksFactory factory of blocks by symbols
     * @param symbols       the symbols array representing line of blocks
     * @throws FormatException wrong format
     */
    private void createBlocksRow(List<Block> blocks, int xPos, int yPos,
                                 BlocksFromSymbolsFactory blocksFactory,
                                 char[] symbols) throws FormatException {
        // Go over all the symbols and create blocks
        for (char charSymbol : symbols) {
            String symbol = String.valueOf(charSymbol);
            if (blocksFactory.isBlockSymbol(symbol)) {
                // Block - create the new block
                Block newBlock = blocksFactory.getBlock(symbol, xPos, yPos);
                xPos += newBlock.getWidth(); // move the x position
                blocks.add(newBlock);
            } else if (blocksFactory.isSpaceSymbol(symbol)) {
                // Spacer - move the x position
                xPos += blocksFactory.getSpaceWidth(symbol);
            } else {
                throw new FormatException(); // wrong symbol
            }
        }
    }
}
