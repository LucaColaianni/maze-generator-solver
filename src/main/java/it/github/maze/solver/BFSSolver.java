package it.github.maze.solver;

import it.github.maze.interfaces.StepSolver;
import it.github.maze.model.Cell;
import it.github.maze.model.MazeGrid;

import java.util.*;


public class BFSSolver implements StepSolver {
    private final MazeGrid grid;
    private final Queue<Cell> queue = new ArrayDeque<>();
    private final Set<Cell> visited = new HashSet<>();
    private final Cell start, goal;
    private Cell current;
    private boolean found = false;

    public BFSSolver(MazeGrid grid, Cell start, Cell goal) {
        this.grid = grid;
        this.start = start;
        this.goal = goal;
        queue.add(start);
        visited.add(start);
    }

    public Cell getCurrent() { return current; }

    @Override
    public boolean hasNextStep() { return !queue.isEmpty() && !found; }

    @Override
    public void nextStep() {
        current = queue.poll();
        if (current.equals(goal)) { found = true; return; }
        for (Cell n : getAccessibleNeighbors(current)) {
            if (!visited.contains(n)) {
                visited.add(n);
                n.setParent(current);
                queue.add(n);
            }
        }
    }

    private List<Cell> getAccessibleNeighbors(Cell c) {
        List<Cell> list = new ArrayList<>();
        int r = c.getRow(), col = c.getCol();
        if (!c.hasTopWall() && r>0)              list.add(grid.getCell(r-1,col));
        if (!c.hasRightWall() && col<grid.getCols()-1) list.add(grid.getCell(r,col+1));
        if (!c.hasBottomWall() && r<grid.getRows()-1) list.add(grid.getCell(r+1,col));
        if (!c.hasLeftWall() && col>0)            list.add(grid.getCell(r,col-1));
        return list;
    }

    @Override
    public List<Cell> getPath() {
        List<Cell> path = new ArrayList<>();
        if (!found) return path;
        Cell cur = goal;
        while (cur != null) {
            path.add(cur);
            cur = cur.getParent();
        }
        Collections.reverse(path);
        return path;
    }
}
