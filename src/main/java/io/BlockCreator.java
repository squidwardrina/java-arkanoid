package io;

import sprites.Block;

/**
 * Reprsents a block creator.
 */
public interface BlockCreator {
    /**
     * Create the block at the specified location.
     *
     * @param xpos x coordinate
     * @param ypos y coordinate
     * @return new block
     * @throws FormatException wrong file format
     */
    Block create(int xpos, int ypos) throws FormatException;
}