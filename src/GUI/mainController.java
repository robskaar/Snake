package GUI;

import Domain.Blocks;
import Domain.Direction;
import Domain.Snake;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class mainController implements Initializable {

    @FXML
    private Label score;

    @FXML
    private AnchorPane gamePane;

    @FXML
    private AnchorPane overlayPane;

    Timeline FPStimeline = new Timeline();
    Timeline CollisionTimeline = new Timeline();
    static Snake snake;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        snake = new Snake();                          // Create new snake

        for (Blocks block : snake.getSnakeArray()) {  // Add all blocks to gamePane
            gamePane.getChildren().add(block);
        }

        overlayPane.setFocusTraversable(true);       // Make key input possible on overlay pane
        initFPSTimeline();                           // Timeline to move snake
        initCollisionTimeline();                     // Timeline to detect collision

    }

    public void initFPSTimeline() {

        FPStimeline.setAutoReverse(false);
        FPStimeline.setCycleCount(Animation.INDEFINITE);
        FPStimeline.getKeyFrames().add(new KeyFrame(Duration.millis(300), ActionEvent -> {
            snake.moveSnake();
        }));

        FPStimeline.play();
    }

    public void initCollisionTimeline() {

        CollisionTimeline.setAutoReverse(false);
        CollisionTimeline.setCycleCount(Animation.INDEFINITE);
        CollisionTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(500), ActionEvent -> {

            // Check if snake collides with anything
            boolean hasCollision = snake.checkCollision();

            // End game if collision is detected
            if (hasCollision) endGame();

        }));

        CollisionTimeline.play();
    }

    public void getKeyInput(KeyEvent ke) {

        KeyCode key = ke.getCode();

        // Get movement input
        Direction snakeDirection = snake.getSnakeDirection();

        if (key == KeyCode.DOWN && snakeDirection != Direction.UP) {
            snake.setSnakeDirection(Direction.DOWN);
        } else if (key == KeyCode.LEFT && snakeDirection != Direction.RIGHT) {
            snake.setSnakeDirection(Direction.LEFT);
        } else if (key == KeyCode.RIGHT && snakeDirection != Direction.LEFT) {
            snake.setSnakeDirection(Direction.RIGHT);
        } else if (key == KeyCode.UP && snakeDirection != Direction.DOWN) {
            snake.setSnakeDirection(Direction.UP);
        }

        // Other input

        if (key == KeyCode.SPACE) {
            // Pause game
        }
    }

    public void endGame() {
        FPStimeline.stop();       // Stop moving the snake..
        CollisionTimeline.stop();
        score.setText("Game ended. u ded bot");
    }

    // Temp method for button
    public void addToSnakeBody() {
        snake.addSnakeBody(gamePane);
    }
}
