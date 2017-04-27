package com.eliaswalyba.gameoflife.frontend;

import com.eliaswalyba.gameoflife.backend.Position;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Grid extends JPanel
{
    /*
     * -----------------------------------------------------------------------------------------------------------------
     * @className: Grid
     * @description: Draws a nice grid to graphically represents the cells.
     * @version: 0.1
     * @author: Elias W. BA (eliaswalyba@gmail.com)
     * @date: february 2017
     * -----------------------------------------------------------------------------------------------------------------
     * In this class we handle all the UI of the game. We use the Java Swing Graphics and Graphics 2D objects to draw
     * a very nice and well shaped grid for the user.
     *------------------------------------------------------------------------------------------------------------------
     */

    /**
     * The number of cells in the grid.
     */
    private static final int MAX = 25;

    /**
     * The states of the cells in the grid.
     */
    private int[][] states = new int[MAX][MAX];

    /**
     * The cells in the grid.
     */
    private Cell[][] cells = new Cell[MAX][MAX];

    private class Cell
    {
        /*
         * -------------------------------------------------------------------------------------------------------------
         * @className: Cell
         * @description: Represents a cell in the GUI.
         * @version: 0.1
         * @author: Elias W. BA (eliaswalyba@gmail.com)
         * @date: february 2017
         * -------------------------------------------------------------------------------------------------------------
         */

        /**
         * The shape of the cell
         */
        private Shape shape;

        /**
         * The color of the cell in the grid
         */
        private Color color;

        /**
         * The position of the cell in the grid
         */
        private Position position;

        /**
         * Allows to create a shape.
         *
         * @param shape the shape to create
         * @param color the color of the shape to create
         * @param i the abscissa of the shape in the grid
         * @param j the ordinate of the shape in the grid
         */
        Cell(Shape shape, Color color, int i, int j) {
            this.shape = shape;
            this.color = color;
            this.position = new Position(i, j);
        }

    }


    /**
     * Allows to create the panel and to draw the grid in it.
     */
    public Grid()
    {
        this.populateGrid(cells);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent)
            {
                super.mouseClicked(mouseEvent);
                for (Cell line[] : cells) for (Cell c : line)
                    if (c.shape.contains(mouseEvent.getPoint()))
                    {
                        int x = c.position.getX(), y = c.position.getY();
                        c.color = c.color == Color.BLACK ? Color.WHITE : Color.BLACK;
                        states[x][y] = (c.color == Color.BLACK) ? 0 : 1;
                    }
                repaint();
            }
        });
    }

    /**
     * This method allows to populate the grid the first time we launch the application.
     * It fills all the cells with a white color because initially all cells are considered dead.
     *
     * @param cells the matrix of cell to populate
     */
    private void populateGrid(Cell[][] cells)
    {
        for (int k = 0, i = 0; k < MAX && i < cells.length * MAX; k++, i += (MAX + 1))
            for (int l = 0, j = 0; l < MAX && j < cells.length * MAX; l++, j += (MAX + 1)) {
                Cell c = new Cell(new Rectangle(j, i, MAX, MAX), Color.WHITE, k, l);
                cells[k][l] = c;
            }
    }

    /**
     * We redefine the paintComponent method to draw our super designed grid.
     *
     * @param g the graphics
     */
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        for (Cell[] line : cells) for (Cell c : line) {
                g2.setColor(c.color);
                g2.fill(c.shape);
                int x = c.position.getX(), y = c.position.getY();
                if (c.color == Color.BLACK) this.states[x][y] = 1;
                else this.states[c.position.getX()][c.position.getY()] = 0;
        }
        g2.dispose();
    }

    /**
     * We call this method any time the user presses the <<Next Generation>> button.
     * It injects in the panel the new cells resulting from the nextGeneration method in the Controller class.
     *
     * @param grid the new grid to inject
     */
    public void updateGrid(int[][] grid)
    {
        this.states = grid;
        for (Cell line[] : cells) for (Cell c : line) {
                int x = c.position.getX(), y = c.position.getY();
                c.color = states[x][y] == 1 ? Color.BLACK : Color.WHITE;
        }
        repaint();
    }

    /**
     * Allows to access the states of the cells.
     *
     * @return int[][] a matrix containing all the states of the cells.
     */
    public int[][] getStates()
    {
        return states;
    }

}
