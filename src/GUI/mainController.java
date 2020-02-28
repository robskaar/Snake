package GUI;

import Domain.AudioPlayer;
import Domain.Blocks;
import Domain.Direction;
import Domain.Snake;
import javafx.animation.*;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;


public class mainController implements Initializable {

    @FXML
    private ToggleButton easyDifficultyButton;
    @FXML
    private ToggleButton normalDifficultyButton;
    @FXML
    private ToggleButton hardDifficultyButton;
    @FXML
    private Label countDown;
    @FXML
    private Label score;
    @FXML
    private Button newGameButton;
    @FXML
    private Button resumeButton;
    @FXML
    private AnchorPane gamePane;
    @FXML
    private AnchorPane settingsPane;
    @FXML
    private AnchorPane menuPane;
    @FXML
    private AnchorPane countDownPane;
    @FXML
    private TextField userNameField;
    @FXML
    private AnchorPane overlayPane;
    volatile StringProperty countDownNo = new SimpleStringProperty(""); // used to countdown when resuming / starting a game
    Timeline FPStimeline = new Timeline();
    Timeline CollisionTimeline = new Timeline();
    static Snake snake;
    AudioPlayer menuSound = new AudioPlayer("src/Resources/Sound/MenuSound.wav");
    AudioPlayer gameSound = new AudioPlayer("src/Resources/Sound/GameSound.wav");
    ToggleGroup levelDifficulty = new ToggleGroup(); // toggle group for level difficulty

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        playMenuSound(true);                        // start the menu sound
        normalDifficultyButton.setSelected(true);   // sets initial difficulty to normal
        normalDifficultyButton.arm();               // sets initial difficulty to normal
        setMode();                                  // load the initial difficulty level

        overlayPane.setFocusTraversable(true);       // Make key input possible on overlay pane
        initFPSTimeline();                           // Timeline to move snake
        initCollisionTimeline();                     // Timeline to detect collision
        FPStimeline.pause();                        // pauses timeline
        CollisionTimeline.pause();                   // pauses timeline
        menuPane.setVisible(true);                   //initially shows main menu

        userNameField.setOnMouseClicked(e -> { // set on mouse click actions for username field
            userNameField.setEditable(true);
            userNameField.setStyle("-fx-background-color: white");
        });
        userNameField.setOnAction(e -> { // set on action for username field - when space is pressed.
            userNameField.setStyle("-fx-background-color: transparent");
            userNameField.setEditable(false);
        });

        resumeButton.setDisable(true); // initial disables resume game button - no game to resume at startup
        resumeButton.setOnAction(e -> { // set on action for resume button
            playMenuSound(false);
            playGameSound(true);
            resumeGame();
        });

        newGameButton.setOnAction(e -> { // set on action for new game button
            playMenuSound(false);
            playGameSound(true);
            newGame();
        });


    }

    public void setMode(){

        easyDifficultyButton.setToggleGroup(levelDifficulty);
        normalDifficultyButton.setToggleGroup(levelDifficulty);
        hardDifficultyButton.setToggleGroup(levelDifficulty);

        if (easyDifficultyButton.isArmed()){
            FPStimeline.setRate(2);

        }
        else if(normalDifficultyButton.isArmed()){
            FPStimeline.setRate(4);

        }
        else if (hardDifficultyButton.isArmed()){
            FPStimeline.setRate(6);

        }

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
            if (FPStimeline.getStatus().equals(Animation.Status.PAUSED)) {
                // you cant pause if the game is paused.
            }
            else {
                playGameSound(false);
                playMenuSound(true);
                showMenu();
            }
        }
    }

    public void showMenu() {

        FPStimeline.pause();
        CollisionTimeline.pause();
        settingsPane.setVisible(false);
        menuPane.setVisible(true);
    }

    public void showSettings() {
        menuPane.setVisible(false);
        settingsPane.setVisible(true);
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
            resumeButton.setDisable(false);
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


    public void playGameSound(Boolean play) {
        if (play) {
            gameSound.play(-1);
        }
        else {
            gameSound.stop();
        }

    }


    public void playMenuSound(Boolean play) {
        if (play) {
            menuSound.play(-1);
        }
        else {
            menuSound.stop();

        }

    }

    public void endGame() {
        FPStimeline.stop();       // Stop moving the snake..
        CollisionTimeline.stop();
        score.setText("Game ended. u ded bot");
        resumeButton.setDisable(true); // initial disables resume game button - no game to resume at startup
        showMenu();

    }

    // Temp method for button
    public void addToSnakeBody() {
        snake.addSnakeBody(gamePane);
    }
}
