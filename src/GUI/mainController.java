package GUI;

import Domain.Blocks;
import Domain.Direction;
import Domain.Snake;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
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
        initCollisionTimeline();

    }

    public void initFPSTimeline() {

        // Setup game frame updates
        FPStimeline.setAutoReverse(false);
        FPStimeline.setCycleCount(Animation.INDEFINITE);
        FPStimeline.getKeyFrames().add(new KeyFrame(Duration.millis(500), ActionEvent -> {

            snake.moveSnake();

        }));

        FPStimeline.play();
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

        if(key == KeyCode.SPACE){
            // Pause game
        }
    }

    public void initCollisionTimeline() {

        CollisionTimeline.setAutoReverse(false);
        CollisionTimeline.setCycleCount(Animation.INDEFINITE);
        CollisionTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(500), ActionEvent -> {

            double snakeX = snake.getSnakeArray().get(0).getX();
            double snakeY = snake.getSnakeArray().get(0).getY();

            // Check collision with snake itself
            for (int i = 1; i < snake.getSnakeArray().size() - 1; i++) {

                Blocks currentCheck = snake.getSnakeArray().get(i);

                if (snakeX == currentCheck.getX() && snakeY == currentCheck.getY()) {
                    endGame();
                }
            }

            double paneSize = gamePane.getPrefWidth();
            System.out.println(paneSize);
            // Check collision with borders
            if(snakeX >= paneSize || snakeX < 0 || snakeY >= paneSize || snakeY < 0){
                endGame();
            }
        }));

        CollisionTimeline.play();
    }

    public void endGame(){
        FPStimeline.stop();       // Stop moving the snake..
        CollisionTimeline.stop();
        System.out.println("Game ended..... you dieeeed");
    }
}
