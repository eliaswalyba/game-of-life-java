package com.eliaswalyba.gameoflife.backend;

public class Controller
{
    /*
     * -----------------------------------------------------------------------------------------------------------------
     * @className: Controller
     * @description: Handles all the algorithms behind scene that allows the game to work correctly
     * @version: 0.1
     * @author: Elias W. BA (eliaswalyba@gmail.com)
     * @date: february 2017
     * -----------------------------------------------------------------------------------------------------------------
     *
     * This class handles all the logic of the game.
     * It uses a matrix of binaries (1 and 0) to represent the graphical grid.
     * The value of a cell in the matrix depends on the state (ALIVE or DEAD) of it's corresponding
     * cell in the graphical grid. When the cell G[i][j] in the graphical grid is ALIVE we set the
     * value of 1 in it's corresponding cell M[i][j] in the matrix and when it's DEAD we set it to 0
     * At the beginning all cells are DEAD by default (When we launch the UI the grid has no black
     * cell) and whenever the user clicks on a cell we update the matrix.
     *------------------------------------------------------------------------------------------------------------------
     */

    /**
     * A 2D array of integers to store the states of the cells in the grid
     */
    private int [][]cells;

    /**
     * A bunch of integers class constants to normalize some aspects and concepts
     */
    private static final int

            /* The state of a cell: ALIVE or DEAD */
            ALIVE            = 1,
            DEAD             = 0,

            /* The 4 corners in the grid */
            TOP_LEFT         = 0,
            TOP_RIGHT        = 1,
            BOTTOM_RIGHT     = 2,
            BOTTOM_LEFT      = 3,

            /* The 4 borders in the grid */
            BORDER_TOP       = 0,
            BORDER_RIGHT     = 1,
            BORDER_BOTTOM    = 2,
            BORDER_LEFT      = 3,

            /* The 3 different number of neighbors possible in the grid */
            CORNER_NEIGHBORS = 3,
            BORDER_NEIGHBORS = 5,
            CENTER_NEIGHBORS = 8;

    /**
     * The lowest and highest indexes of the matrix
     */
    private final int
            MIN_BOUND,
            MAX_BOUND;

    /**
     * The constructor of the class
     *
     * @param cells [][]int the grids state to populate in the matrix
     */
    public Controller(int [][]cells)
    {
        this.cells = cells;
        this.MIN_BOUND = 0;
        this.MAX_BOUND = this.cells.length - 1;
    }

    /**
     * Allows to access the matrix of cells
     *
     * @return [][]int the matrix of cells
     */
    public int[][] getCells() {
        return cells;
    }

    /**
     * Allows to mutate the matrix of cells
     *
     * @param cells int[][] the new matrix of cells
     */
    public void setCells(int[][] cells) {
        this.cells = cells;
    }

    /**
     * This method computes the next generation according to the state of each cell and the number of alive cells
     * around it in the current generation.
     * The rules are pretty simple:
     *      - if a cell is DEAD
     *              if it is surrounded by 3 alive cells it rebirths
     *              else it stays dead
     *      - else
     *              if it is surrounded by 2 or 3 alive cells it stays alive
     *              else if it is surrounded by less than 2 alive cells it died by underpopulation
     *              else it dies by overpopulation
     */
    public void nextGeneration() {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells.length; j++) {
                Position position = new Position(i, j);
                int cpt = 0;
                Neighborhood neighborhood = extractNeighborhood(position);
                for (int k = 0; k < (neighborhood != null ? neighborhood.neighbors.length : 0); k++) {
                    for (int l = 0; l < neighborhood.neighbors.length; l++) {
                        Position p = neighborhood.target;
                        if (!(p.getX() == k && p.getY() == l)) {
                            if (neighborhood.neighbors[k][l] == ALIVE) {
                                cpt++;
                            }
                        }
                    }
                }
                this.changeState(position, cpt);
            }
        }
    }

    /**
     * Allows to change the status of cell.
     *
     * @param p position of the cell to change it's status
     * @param n the number of alive neighbors around that cell
     */
    private void changeState(Position p, int n)
    {
        if (isAlive(cells[p.getX()][p.getY()]) && (n < 2 || n > 3))
            this.cells[p.getX()][p.getY()] = DEAD;
        else if ((!isAlive(cells[p.getX()][p.getY()]) && n == 3))
            this.cells[p.getX()][p.getY()] = ALIVE;
    }

    /**
     * Allows to extract a neighborhood according to the given position.
     * This methods switches the 3 different possible values of the return value of evaluateNumberOfNeighbors(p) method.
     * For each case it applies a specific treatment to find out the neighborhood to return.
     *
     * @param p the position of the cell to find its neighborhood
     * @return Neighborhood an object of type Neighborhood.
     */
    private Neighborhood extractNeighborhood(Position p)
    {
        int neighbors[][] = new int[3][3], i = p.getX(), j = p.getY();
        Neighborhood neighborhood = new Neighborhood();
        int []A;
        int []B;
        int n = cells.length - 1;
        switch (evaluateNumberOfNeighbors(p)) {
            case CORNER_NEIGHBORS:
                switch (whichCorner(p)) {
                    case TOP_LEFT:
                        A = new int[]{n, 0, 1};
                        neighborhood.target = new Position(0, 0);
                        for (int k = 0, a = 0; k < neighbors.length && a < A.length; k++, a++) {
                            for (int l = 0, b = 0; l < neighbors.length && b < A.length; l++, b++) {
                                neighbors[k][l] = cells[A[a]][A[b]];
                            }
                        }
                        break;
                    case TOP_RIGHT:
                        A = new int[]{n, 0, 1};
                        B = new int[]{n-1, n, 0};
                        neighborhood.target = new Position(0, 1);
                        for (int k = 0, a = 0; k < neighbors.length && a < A.length; k++, a++) {
                            for (int l = 0, b = 0; l < neighbors.length && b < B.length; l++, b++) {
                                neighbors[k][l] = cells[A[a]][B[b]];
                            }
                        }
                        break;
                    case BOTTOM_RIGHT:
                        A = new int[]{n-1, n, 0};
                        neighborhood.target = new Position(1, 1);
                        for (int k = 0, a = 0; k < neighbors.length && a < A.length; k++, a++) {
                            for (int l = 0, b = 0; l < neighbors.length && b < A.length; l++, b++) {
                                neighbors[k][l] = cells[A[a]][A[b]];
                            }
                        }
                        break;
                    case BOTTOM_LEFT:
                        A = new int[]{n-1, n, 0};
                        B = new int[]{n, 0, 1};
                        neighborhood.target = new Position(1, 0);
                        for (int k = 0, a = 0; k < neighbors.length && a < A.length; k++, a++) {
                            for (int l = 0, b = 0; l < neighbors.length && b < B.length; l++, b++) {
                                neighbors[k][l] = cells[A[a]][B[b]];
                            }
                        }
                        break;
                    default: return null;
                }
                break;
            case BORDER_NEIGHBORS:
                switch (whichBorder(p)) {
                    case BORDER_TOP:
                        A = new int[]{n, 0, 1};
                        B = new int[]{j-1, j, j+1};
                        neighborhood.target = new Position(0, 1);
                        for (int k = 0, a = 0; k < neighbors.length && a < A.length; k++, a++) {
                            for (int l = 0, b = 0; l < neighbors.length && b < B.length; l++, b++) {
                                neighbors[k][l] = cells[A[a]][B[b]];
                            }
                        }
                        break;
                    case BORDER_RIGHT:
                        A = new int[]{i-1, i, i+1};
                        B = new int[]{n-1, n, 0};
                        neighborhood.target = new Position(1, 1);
                        for (int k = 0, a = 0; k < neighbors.length && a < A.length; k++, a++) {
                            for (int l = 0, b = 0; l < neighbors.length && b < B.length; l++, b++) {
                                neighbors[k][l] = cells[A[a]][B[b]];
                            }
                        }
                        break;
                    case BORDER_BOTTOM:
                        A = new int[]{n-1, n, 0};
                        B = new int[]{j-1, j, j+1};
                        neighborhood.target = new Position(1, 1);
                        for (int k = 0, a = 0; k < neighbors.length && a < A.length; k++, a++) {
                            for (int l = 0, b = 0; l < neighbors.length && b < B.length; l++, b++) {
                                neighbors[k][l] = cells[A[a]][B[b]];
                            }
                        }
                        break;
                    case BORDER_LEFT:
                        B = new int[]{n, 0, 1};
                        A = new int[]{i-1, i, i+1};
                        neighborhood.target = new Position(1, 0);
                        for (int k = 0, a = 0; k < neighbors.length && a < A.length; k++, a++) {
                            for (int l = 0, b = 0; l < neighbors.length && b < B.length; l++, b++) {
                                neighbors[k][l] = cells[A[a]][B[b]];
                            }
                        }
                        break;
                    default: return null;
                }
                break;
            case CENTER_NEIGHBORS:
                neighborhood.target = new Position(1, 1);
                for (int k = 0; k < neighbors.length; k++) for (int l = 0; l < neighbors.length; l++)
                        neighbors[k][l] = cells[i-(1-k)][j-(1-l)];
                break;
            default: return null;
        }
        neighborhood.neighbors = neighbors;
        return neighborhood;
    }

    /**
     * Allows to check whether a given position in the grid is in corners or not
     *
     * @param p the position of the cell
     * @return boolean true if it's in a corner and false if not
     */
    private boolean isInCorner(Position p)
    {
        int i = p.getX(), j = p.getY();
        return (i == MIN_BOUND && j == MIN_BOUND) || (i == MIN_BOUND && j == MAX_BOUND)
                        || (i == MAX_BOUND && j == MIN_BOUND) || (i == MAX_BOUND && j == MAX_BOUND);
    }

    /**
     * Allows to know exactly in which corner is located a given position
     *
     * @param p the position
     * @return int an integer representing the corner
     */
    private int whichCorner(Position p)
    {
        int i = p.getX(), j = p.getY();
        if (i == MIN_BOUND && j == MIN_BOUND) return TOP_LEFT;
        else if (i == MIN_BOUND && j == MAX_BOUND) return TOP_RIGHT;
        else if (i == MAX_BOUND && j == MAX_BOUND) return BOTTOM_RIGHT;
        else return BOTTOM_LEFT;
    }

    /**
     * Allows to check whether a given position in the grid is in a border or not
     *
     * @param p the position of the cell
     * @return boolean true if it's in a border and false if not
     */
    private boolean isInBorder(Position p)
    {
        int i = p.getX(), j = p.getY();
        return (
                (i == MIN_BOUND || i == MAX_BOUND) && (j > MIN_BOUND && j < MAX_BOUND)
        ) || (
                (j == MIN_BOUND || j == MAX_BOUND) && (i > MIN_BOUND && i < MAX_BOUND)
        );
    }

    /**
     * Allows to know exactly in which border is located a given position
     *
     * @param p the position
     * @return int an integer representing the border
     */
    private int whichBorder(Position p)
    {
        int i = p.getX(), j = p.getY();
        if (i == MIN_BOUND && (j > MIN_BOUND && j < MAX_BOUND)) return BORDER_TOP;
        else if ((i > MIN_BOUND && i < MAX_BOUND) && j == MAX_BOUND) return BORDER_RIGHT;
        else if (i == MAX_BOUND && (j > MIN_BOUND && j < MAX_BOUND)) return BORDER_BOTTOM;
        else return BORDER_LEFT;
    }

    /**
     * Allows to calculate the number of neighbors a given position in the cell has.
     *
     * @param p the position
     * @return int the number of neighbors
     */
    private int evaluateNumberOfNeighbors(Position p)
    {
        if (isInCorner(p)) return CORNER_NEIGHBORS;
        else if (isInBorder(p)) return BORDER_NEIGHBORS;
        else return CENTER_NEIGHBORS;
    }

    /**
     * Allows to test if a cell is alive or not
     *
     * @param e the value of the cell
     * @return boolean true if alive and false if not
     */
    private boolean isAlive(int e)
    {
        return e == ALIVE;
    }

    private class Neighborhood
    {
        /*
         * -------------------------------------------------------------------------------------------------------------
         * @className: Neighborhood
         * @description: Represents a neighborhood in the matrix.
         * @version: 0.1
         * @author: Elias W. BA (eliaswalyba@gmail.com)
         * @date: february 2017
         * -------------------------------------------------------------------------------------------------------------
         */

        /**
         * An object of type position to represent the position of the cell we wanna compute it's neighborhood.
         */
        private Position target;

        /**
         * A 2D array of integers to store the states of the neighbors of the cell the position in target
         */
        private int[][] neighbors;


        /**
         * The default constructor. This method does nothing but creating an object of type Neighborhood.
         */
        public Neighborhood(){}

    }

}
