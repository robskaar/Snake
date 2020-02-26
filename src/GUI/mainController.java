package GUI;

import Domain.Blocks;
import Domain.Direction;
import Domain.Snake;
import javafx.animation.*;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;


public class mainController implements Initializable {

    @FXML
    private Label countDown;
    @FXML
    private Label score;

    @FXML
    private AnchorPane gamePane;

    @FXML
    private AnchorPane menuPane;
    @FXML
    private AnchorPane countDownPane;
    @FXML
    private TextField userNameField;
    @FXML
    private AnchorPane overlayPane;
    volatile StringProperty countDownNo = new SimpleStringProperty("");
    Timeline FPStimeline = new Timeline();
    Timeline CollisionTimeline = new Timeline();
    static Snake snake;

    private String currentUser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        snake = new Snake();                          // Create new snake

        for (Blocks block : snake.getSnakeArray()) {  // Add all blocks to gamePane
            gamePane.getChildren().add(block);
        }

        overlayPane.setFocusTraversable(true);       // Make key input possible on overlay pane
        initFPSTimeline();                           // Timeline to move snake
        initCollisionTimeline();                     // Timeline to detect collision


        userNameField.setOnMouseClicked(e->{
            userNameField.setEditable(true);
            userNameField.setStyle("-fx-background-color: white");
        });userNameField.setOnAction(e->{
            userNameField.setStyle("-fx-background-color: transparent");
            userNameField.setEditable(false);
        });


    }


    public void initFPSTimeline() {

        FPStimeline.setAutoReverse(false);
        FPStimeline.setCycleCount(Animation.INDEFINITE);
        FPStimeline.getKeyFrames().add(new KeyFrame(Duration.millis(500), ActionEvent -> {

            snake.moveSnake();

        }));

        FPStimeline.play();
    }

    public void initCollisionTimeline() {

        CollisionTimeline.setAutoReverse(false);
        CollisionTimeline.setCycleCount(Animation.INDEFINITE);
        CollisionTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(1), ActionEvent -> {

            // Check if snake collides with anything
            boolean hasCollision = snake.checkCollision();

            // End game if collision is detected
            if (hasCollision) {
                snake.getSnakeArray().get(0).toFront();
                endGame();
            }

        }));

        CollisionTimeline.play();
    }

    public void getKeyInput(KeyEvent ke) throws InterruptedException {

        KeyCode key = ke.getCode();

        // Get movement input
        Direction snakeDirection = snake.getSnakeDirection();

        if (key == KeyCode.DOWN && snakeDirection != Direction.UP) {
            snake.setSnakeDirection(Direction.DOWN);
        }
        else if (key == KeyCode.LEFT && snakeDirection != Direction.RIGHT) {
            snake.setSnakeDirection(Direction.LEFT);
        }
        else if (key == KeyCode.RIGHT && snakeDirection != Direction.LEFT) {
            snake.setSnakeDirection(Direction.RIGHT);
        }
        else if (key == KeyCode.UP && snakeDirection != Direction.DOWN) {
            snake.setSnakeDirection(Direction.UP);
        }

        // Other input

        if (key == KeyCode.SPACE) {
            if (FPStimeline.getStatus().equals(Animation.Status.PAUSED)){
                // you cant pause if the game is paused.
            }else
            showMenu();
        }
    }

    public void showMenu() {
        FPStimeline.pause();
        CollisionTimeline.pause();
        menuPane.setVisible(true);
    }

    public void resumeGame() {
        if (isUserNameSupplied()) {
            KeyFrame countOne = new KeyFrame(Duration.seconds(0), event -> {
                FPStimeline.pause();
                CollisionTimeline.pause();
                menuPane.setVisible(false);
                countDownPane.setVisible(true);
                countDown.setText("3");
                Timeline timeline = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(countDown.textFillProperty(), Color.rgb(0, 0, 0))),
                        new KeyFrame(Duration.seconds(1), new KeyValue(countDown.textFillProperty(), Color.rgb(255, 0, 25))),
                        new KeyFrame(Duration.seconds(1), new KeyValue(countDown.textFillProperty(), Color.rgb(0, 0, 0))),
                        new KeyFrame(Duration.seconds(2), new KeyValue(countDown.textFillProperty(), Color.rgb(255, 255, 0))),
                        new KeyFrame(Duration.seconds(2), new KeyValue(countDown.textFillProperty(), Color.rgb(0, 0, 0))),
                        new KeyFrame(Duration.seconds(3), new KeyValue(countDown.textFillProperty(), Color.rgb(0, 255, 0)))
                );
                timeline.play();
            });
            KeyFrame countTwo = new KeyFrame(Duration.seconds(1), event -> {
                countDown.setText("2");
            });
            KeyFrame countThree = new KeyFrame(Duration.seconds(2), event -> {
                countDown.setText("1");
            });
            KeyFrame resume = new KeyFrame(Duration.seconds(3), event -> {
                countDown.setText("");
                countDownPane.setVisible(false);
                FPStimeline.play();
                CollisionTimeline.play();
            });

            Timeline countDowns = new Timeline(
                    countOne, countTwo, countThree, resume
            );
            countDowns.setCycleCount(1);
            countDowns.play();
        }
    }

    public boolean isUserNameSupplied() {
        if (userNameField.getText().isEmpty()) {
            userNameField.setEditable(true);
            userNameField.requestFocus();
            userNameField.setStyle("-fx-background-color: white");
            userNameField.setTooltip(new Tooltip("YOU MUST PROVIDE A USERNAME"));
            return false;
        }
        else {
            userNameField.setStyle("-fx-background-color: transparent");
            return true;
        }

    }

    public void newGame() {
        if (isUserNameSupplied()) {
            userNameField.setTooltip(null);
            gamePane.getChildren().clear();
            snake = new Snake();                          // Create new snake

            for (Blocks block : snake.getSnakeArray()) {  // Add all blocks to gamePane
                gamePane.getChildren().add(block);
            }
            resumeGame();
        }
    }

    public void quitGame() {
        Platform.exit();
    }

    public void endGame() {
        FPStimeline.stop();       // Stop moving the snake..
        CollisionTimeline.stop();
        score.setText("Game ended. u ded bot");
        showMenu();
    }

    // Temp method for button
    public void addToSnakeBody() {
        snake.addSnakeBody(gamePane);
    }
}
