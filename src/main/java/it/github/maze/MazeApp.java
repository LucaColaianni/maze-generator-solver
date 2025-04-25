package it.github.maze;

import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

import it.github.maze.model.MazeGrid;
import it.github.maze.model.Cell;
import it.github.maze.generator.DFSGenerator;
import it.github.maze.solver.BFSSolver;
import it.github.maze.interfaces.StepGenerator;
import it.github.maze.interfaces.StepSolver;

public class MazeApp extends Application {
    private static final int COLS = 30;
    private static final int ROWS = 20;
    private static final int CELL_SIZE = 20;

    private MazeGrid grid;
    private StepGenerator generator;
    private StepSolver solver;
    private Canvas canvas;
    private Timeline timeline;
    private boolean generating;
    private List<Cell> solverTrail; // traccia del solver

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        ComboBox<String> comboGen = new ComboBox<>();
        comboGen.getItems().addAll("DFS");
        comboGen.setValue("DFS");

        ComboBox<String> comboSol = new ComboBox<>();
        comboSol.getItems().addAll("BFS");
        comboSol.setValue("BFS");

        Button btnStart = new Button("Start");
        HBox controls = new HBox(10, comboGen, comboSol, btnStart);
        canvas = new Canvas(COLS * CELL_SIZE, ROWS * CELL_SIZE);

        BorderPane root = new BorderPane(canvas, controls, null, null, null);
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Maze Generator & Solver");
        primaryStage.show();

        btnStart.setOnAction(e -> startAnimation(comboGen.getValue(), comboSol.getValue()));
    }

    private void startAnimation(String genType, String solType) {
        generating = true;
        grid = new MazeGrid(ROWS, COLS);
        generator = switch (genType) {
            case "DFS" -> new DFSGenerator(grid);
            default    -> throw new IllegalArgumentException("Generator non implementato");
        };
        solver = null;
        if (timeline != null) {
            timeline.stop();
        }

        timeline = new Timeline(new KeyFrame(Duration.millis(50), e -> {
            if (generating) {
                if (generator.hasNextStep()) {
                    generator.nextStep();
                    drawGrid();
                } else {
                    generating = false;
                    solver = new BFSSolver(grid, grid.getStart(), grid.getEnd());
                    solverTrail = new ArrayList<>();
                }
            } else {
                if (solver.hasNextStep()) {
                    solver.nextStep();
                    if (solver instanceof BFSSolver) {
                        Cell cur = ((BFSSolver) solver).getCurrent();
                        if (cur != null) solverTrail.add(cur);
                    }
                    drawGrid();
                    drawSolverTrail();
                    drawSolverCursor();
                } else {
                    drawPath();
                    timeline.stop();
                }
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void drawGrid() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, COLS * CELL_SIZE, ROWS * CELL_SIZE);
        gc.setStroke(Color.LIME);
        gc.setLineWidth(2);
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                Cell cell = grid.getCell(r, c);
                int x = c * CELL_SIZE;
                int y = r * CELL_SIZE;
                if (cell.hasTopWall())    gc.strokeLine(x, y, x + CELL_SIZE, y);
                if (cell.hasRightWall())  gc.strokeLine(x + CELL_SIZE, y, x + CELL_SIZE, y + CELL_SIZE);
                if (cell.hasBottomWall()) gc.strokeLine(x, y + CELL_SIZE, x + CELL_SIZE, y + CELL_SIZE);
                if (cell.hasLeftWall())   gc.strokeLine(x, y, x, y + CELL_SIZE);
                if (generating && cell.isVisited()) {
                    gc.fillRect(x + 1, y + 1, CELL_SIZE - 2, CELL_SIZE - 2);
                }
            }
        }
    }

    private void drawSolverTrail() {
        if (solverTrail == null) return;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.rgb(255, 0, 0, 0.3));
        for (Cell cell : solverTrail) {
            int x = cell.getCol() * CELL_SIZE;
            int y = cell.getRow() * CELL_SIZE;
            gc.fillRect(x + 4, y + 4, CELL_SIZE - 8, CELL_SIZE - 8);
        }
    }

    private void drawSolverCursor() {
        if (!(solver instanceof BFSSolver)) return;
        Cell cur = ((BFSSolver) solver).getCurrent();
        if (cur == null) return;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.RED);
        int x = cur.getCol() * CELL_SIZE;
        int y = cur.getRow() * CELL_SIZE;
        gc.fillRect(x + 2, y + 2, CELL_SIZE - 4, CELL_SIZE - 4);
    }

    private void drawPath() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.RED);
        for (Cell cell : solver.getPath()) {
            int x = cell.getCol() * CELL_SIZE;
            int y = cell.getRow() * CELL_SIZE;
            gc.fillOval(x + CELL_SIZE/4, y + CELL_SIZE/4, CELL_SIZE/2, CELL_SIZE/2);
        }
    }
}
