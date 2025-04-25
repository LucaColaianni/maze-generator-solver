module it.github.maze.mazegeneratorsolver {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens it.github.maze to javafx.fxml;
    exports it.github.maze;
    exports it.github.maze.model;
    opens it.github.maze.model to javafx.fxml;
}