package it.github.maze.generator;

import it.github.maze.interfaces.StepGenerator;
import it.github.maze.model.MazeGrid;
import it.github.maze.model.Cell;

import java.util.*;

public class DFSGenerator implements StepGenerator {
    private final MazeGrid grid;
    private final Deque<Cell> stack = new ArrayDeque<>();
    private final Random rand = new Random();

    public DFSGenerator(MazeGrid grid) {
        this.grid = grid;
        Cell start = grid.getStart();
        start.setVisited(true);
        stack.push(start);
    }

    @Override
    public boolean hasNextStep() { return !stack.isEmpty(); }

    @Override
    public void nextStep() {
        Cell curr = stack.peek();
        List<Cell> neighbors = getUnvisitedNeighbors(curr);
        if (!neighbors.isEmpty()) {
            Cell next = neighbors.get(rand.nextInt(neighbors.size()));
            curr.removeWallWith(next);
            next.setVisited(true);
            stack.push(next);
        } else {
            stack.pop();
        }
    }

    private List<Cell> getUnvisitedNeighbors(Cell c) {
        List<Cell> list = new ArrayList<>();
        int r = c.getRow(), col = c.getCol();
        if (r>0)              addIfUnvisited(grid.getCell(r-1,col), list);
        if (r<grid.getRows()-1) addIfUnvisited(grid.getCell(r+1,col), list);
        if (col>0)            addIfUnvisited(grid.getCell(r,col-1), list);
        if (col<grid.getCols()-1) addIfUnvisited(grid.getCell(r,col+1), list);
        return list;
    }

    private void addIfUnvisited(Cell c, List<Cell> list) {
        if (!c.isVisited()) list.add(c);
    }
}
