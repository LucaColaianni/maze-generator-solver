package it.github.maze.interfaces;


import java.util.List;
import it.github.maze.model.Cell;

public interface StepSolver {
    boolean hasNextStep();
    void nextStep();
    List<Cell> getPath();
}