package it.github.maze.model;


public class MazeGrid {
    private final int rows, cols;
    private final Cell[][] cells;

    public MazeGrid(int rows, int cols) {
        this.rows = rows; this.cols = cols;
        cells = new Cell[rows][cols];
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                cells[r][c] = new Cell(r, c);
    }

    public int getRows() { return rows; }
    public int getCols() { return cols; }
    public Cell getCell(int row, int col) { return cells[row][col]; }
    public Cell getStart() { return getCell(0,0); }
    public Cell getEnd() { return getCell(rows-1, cols-1); }
}
