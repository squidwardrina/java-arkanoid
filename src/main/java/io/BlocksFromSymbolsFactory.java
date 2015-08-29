package io;

import graphics.Point;
import sprites.Block;
import sprites.Fill;

import java.awt.Color;
import java.util.Map;
import java.util.TreeMap;

/**
 * Factory class, creates blocks by symbol specifications.
 */
public class BlocksFromSymbolsFactory {
    private Map<String, Integer> spacerWidths = new TreeMap<String, Integer>();
    private Map<String, BlockCreator> blockCreators
            = new TreeMap<String, BlockCreator>();
    private Integer defHitPoints = null;
    private Integer defHeight = null;
    private Integer defWidth = null;
    private Color defStrokeColor = null;
    private Fill defFill = null;
    private Map<Integer, Fill> defFills = new TreeMap<Integer, Fill>();

    /**
     * Adds a new spacer to the mapping.
     *
     * @param symbol the spacer symbol
     * @param width  width of the spacer in pixels
     */
    public void addSpacer(String symbol, Integer width) {
        spacerWidths.put(symbol, width);
    }

    /**
     * Adds a block creator that would create blocks with the given properties.
     *
     * @param blockDefMap block definition properties by names
     */

    public void addBlockCreator(final Map<String, Object> blockDefMap) {
        String symbol = (String) blockDefMap.get("symbol");
        BlockCreator creator = new BlockCreator() {
            public Block create(int xpos, int ypos) throws FormatException {
                Integer width = defWidth;
                Integer height = defHeight;
                Integer hitPoints = defHitPoints;
                Fill fill = defFill;
                Map<Integer, Fill> fills = defFills;
                Color stroke = defStrokeColor;

                if (blockDefMap.containsKey("width")) {
                    width = (Integer) blockDefMap.get("width");
                }
                if (blockDefMap.containsKey("height")) {
                    height = (Integer) blockDefMap.get("height");
                }
                if (blockDefMap.containsKey("hit_points")) {
                    hitPoints = (Integer) blockDefMap.get("hit_points");
                }
                if (blockDefMap.containsKey("fill")) {
                    fill = (Fill) blockDefMap.get("fill");
                }
                if (blockDefMap.containsKey("fill-k")) {
                    // Add the fills to default fills. Override old ones
                    TreeMap newFills = (TreeMap) blockDefMap.get("fill-k");
                    for (Object s : newFills.keySet()) {
                        fills.put((Integer) s, (Fill) newFills.get(s));
                    }
                }
                if (blockDefMap.containsKey("stroke")) {
                    stroke = (Color) blockDefMap.get("stroke");
                }

                verifyFields(width, height, hitPoints, fill, fills);

                Point upperLeft = new Point(xpos, ypos);
                return new Block(upperLeft, width, height, fill,
                        fills, stroke, hitPoints);
            }

            /**
             * Throws exception if there is not enough data to create block.
             *
             * @param width obligatory field
             * @param height obligatory field
             * @param hitPoints obligatory field
             * @param fill default fill
             * @param fills special fills
             * @throws FormatException
             */
            private void verifyFields(Integer width, Integer height,
                                      Integer hitPoints,
                                      Fill fill, Map<Integer, Fill> fills)
                    throws FormatException {
                // Check for necessary fields
                if (width == null || height == null || hitPoints == null) {
                    throw new FormatException();
                }

                // If no default fill - check there is fill-k for every state
                if (fill == null) {
                    for (int i = 1; i <= hitPoints; i++) {
                        if (!fills.containsKey(i)) {
                            throw new FormatException();
                        }
                    }
                }
            }
        };

        blockCreators.put(symbol, creator);
    }

    /**
     * Checks if the string is a spacer symbol.
     *
     * @param symbol string to check
     * @return true/false
     */
    public boolean isSpaceSymbol(String symbol) {
        return this.spacerWidths.containsKey(symbol);
    }

    /**
     * Checks if the string is a block symbol.
     *
     * @param symbol string to check
     * @return true/false
     */
    public boolean isBlockSymbol(String symbol) {
        return this.blockCreators.containsKey(symbol);
    }

    /**
     * Creates a block according to the definitions associated with the symbol.
     *
     * @param symbol the symbol of the block
     * @param xpos   x coordinate
     * @param ypos   y coordinate
     * @return the new block
     * @throws FormatException wrong format
     */
    public Block getBlock(String symbol, int xpos, int ypos)
            throws FormatException {
        BlockCreator creator = this.blockCreators.get(symbol);
        return creator.create(xpos, ypos);
    }

    /**
     * Returns the width in pixels associated with the given spacer-symbol.
     *
     * @param symbol the spacer symbol
     * @return width of the space
     */
    public int getSpaceWidth(String symbol) {
        return this.spacerWidths.get(symbol);
    }

    /**
     * Adds default special filling.
     *
     * @param hitPoints hit points for the default special fill
     * @param fill      the default special filling
     */
    public void addDefSpecialFill(Integer hitPoints, Fill fill) {
        this.defFills.put(hitPoints, fill);
    }

    /**
     * Sets default hit points.
     *
     * @param hitPoints default hit points
     */
    public void setDefHitPoints(Integer hitPoints) {
        this.defHitPoints = hitPoints;
    }

    /**
     * Sets default height.
     *
     * @param height default height
     */
    public void setDefHeight(Integer height) {
        this.defHeight = height;
    }

    /**
     * Sets default width.
     *
     * @param width default width
     */
    public void setDefWidth(Integer width) {
        this.defWidth = width;
    }

    /**
     * Sets default stroke color.
     *
     * @param color default stroke color
     */
    public void setDefStrokeColor(Color color) {
        this.defStrokeColor = color;
    }

    /**
     * Sets default fill.
     *
     * @param fill default fill
     */
    public void setDefFill(Fill fill) {
        this.defFill = fill;
    }
}