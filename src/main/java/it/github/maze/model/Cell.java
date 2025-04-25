package it.github.maze.model;

public class Cell {
    private final int row, col;
    private boolean topWall = true, rightWall = true, bottomWall = true, leftWall = true;
    private boolean visited;
    private Cell parent;

    public Cell(int row, int col) { this.row = row; this.col = col; }

    public int getRow()   { return row; }
    public int getCol()   { return col; }

    public boolean hasTopWall()    { return topWall; }
    public boolean hasRightWall()  { return rightWall; }
    public boolean hasBottomWall() { return bottomWall; }
    public boolean hasLeftWall()   { return leftWall; }
    public boolean isVisited()     { return visited; }

    public void setVisited(boolean v) { visited = v; }
    public Cell getParent()            { return parent; }
    public void setParent(Cell p)      { parent = p; }

    public void removeWallWith(Cell other) {
        if (row == other.row) {
            if (col < other.col) { rightWall = false; other.leftWall = false; }
            else                 { leftWall = false;  other.rightWall = false; }
        } else {
            if (row < other.row) { bottomWall = false; other.topWall = false; }
            else                 { topWall = false;    other.bottomWall = false; }
        }
    }
}