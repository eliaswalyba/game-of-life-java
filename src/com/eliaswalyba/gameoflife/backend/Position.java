package com.eliaswalyba.gameoflife.backend;

public class Position
{
    /*
     * -------------------------------------------------------------------------------------------------------------
     * @className: Position
     * @description: Represents a position. We use this class to track cells in this game.
     * @version: 0.1
     * @author: Elias W. BA (eliaswalyba@gmail.com)
     * @date: february 2017
     * -------------------------------------------------------------------------------------------------------------
     */

    /**
     * the coordinates of the position in the plan
     */
    private int x, y;

    /**
     * Allows to create a Position object with the coordinates.
     *
     * @param x the abscissa
     * @param y the ordinate
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Allows to access the abscissa
     *
     * @return int x the abscissa of the position
     */
    public int getX() {
        return x;
    }

    /**
     * Allows to access the ordinate
     *
     * @return int y the ordinate of the position
     */
    public int getY() {
        return y;
    }

}
