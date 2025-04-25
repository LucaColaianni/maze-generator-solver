module it.github.maze.mazegeneratorsolver {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens it.github.maze.mazegeneratorsolver to javafx.fxml;
    exports it.github.maze.mazegeneratorsolver;
}