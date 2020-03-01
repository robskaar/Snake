package GUI;

import Domain.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
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
import java.security.Key;
import java.util.Random;
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
    private AnchorPane userNamePane;
    @FXML
    private AnchorPane settingsPane;
    @FXML
    private AnchorPane menuPane;
    @FXML
    private AnchorPane countDownPane;
    @FXML
    private AnchorPane highScorePane;
    @FXML
    private AnchorPane gameUnderlayPane;
    @FXML
    private TextField userNameField;
    @FXML
    private AnchorPane overlayPane;

    static Food yumyum;
    static Snake snake;
    static String difficulty;
    static Score currentScore;
    ;
    volatile StringProperty countDownNo = new SimpleStringProperty(""); // used to countdown when resuming / starting a game
    Timeline FPStimeline = new Timeline();
    Timeline CollisionTimeline = new Timeline();
    AudioPlayer menuSound = new AudioPlayer("src/Resources/Sound/MenuSound.wav", 0.1);
    AudioPlayer gameSound = new AudioPlayer("src/Resources/Sound/GameSound.wav", 0.1);
    AudioPlayer foodSlurp = new AudioPlayer("src/Resources/Sound/Slurping+1.wav");
    AudioPlayer foodBite = new AudioPlayer("src/Resources/Sound/bite.wav");
    AudioPlayer foodChomp = new AudioPlayer("src/Resources/Sound/Chomp+1.wav");
    AudioPlayer gameOver = new AudioPlayer("src/Resources/Sound/GameOver2.wav");
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
        overlayPane.setVisible(false);               // Hide overlay
        highScorePane.setVisible(false);

        AnimationUtilities.drawGameGrid(gameUnderlayPane);


        userNameField.setOnAction(e -> {
            if (userNameField.getText().isEmpty()) {
                userNameField.requestFocus();
                userNameField.setTooltip(new Tooltip("Please provide a username"));
            } else {
                userNamePane.setVisible(false);
                userNameField.clear();
                playMenuSound(false);
                playGameSound(true);
                newGame();
            }
        });
        resumeButton.setDisable(true); // initial disables resume game button - no game to resume at startup
        resumeButton.setOnAction(e -> { // set on action for resume button
            playMenuSound(false);
            playGameSound(true);
            resumeGame();
        });

        newGameButton.setOnAction(e -> { // set on action for new game button
            setUserName();
        });


    }

    public void setUserName() {
        menuPane.setVisible(false);
        userNamePane.setVisible(true);
    }

    public void setMode() {

        easyDifficultyButton.setToggleGroup(levelDifficulty);
        normalDifficultyButton.setToggleGroup(levelDifficulty);
        hardDifficultyButton.setToggleGroup(levelDifficulty);

        if (easyDifficultyButton.isArmed()) {
            FPStimeline.setRate(2);
            difficulty = "Easy";
        } else if (hardDifficultyButton.isArmed()) {
            FPStimeline.setRate(6);
            difficulty = "Hard";
        } else if (normalDifficultyButton.isArmed()) {
            FPStimeline.setRate(4);
            difficulty = "Normal";
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
            boolean hasSelfCollision, hasBorderCollision, hasFoodCollision;
            hasSelfCollision = snake.checkSelfCollision();
            hasBorderCollision = snake.checkBorderCollision();
            hasFoodCollision = snake.checkFoodCollision();


            // End game if collision with self or border is detected
            if (hasSelfCollision || hasBorderCollision) {
                snake.getSnakeArray().get(0).toFront();
                gameOver.play(0);
                endGame();
            }

            // expand snake if collision with food is detected

            if (hasFoodCollision) {
                currentScore.setScore(currentScore.getScore() + 1);
                score.setText(Integer.toString(currentScore.getScore()));
                snake.addSnakeBody(gamePane);
                generateFood();
                playRandomFoodSound();
                System.out.println(difficulty + " current rate" + FPStimeline.getCurrentRate());
                if (difficulty.contains("Hard")) {
                    hardModeSpeedBoost();
                }
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
        } else if (key == KeyCode.LEFT && snakeDirection != Direction.RIGHT) {
            snake.setSnakeDirection(Direction.LEFT);
        } else if (key == KeyCode.RIGHT && snakeDirection != Direction.LEFT) {
            snake.setSnakeDirection(Direction.RIGHT);
        } else if (key == KeyCode.UP && snakeDirection != Direction.DOWN) {
            snake.setSnakeDirection(Direction.UP);
        }

        // Other input

        if (key == KeyCode.SPACE) {              // pause game
            if (FPStimeline.getStatus().equals(Animation.Status.PAUSED)) {
                // you cant pause if the game is paused.
            } else {
                playGameSound(false);
                playMenuSound(true);
                showMenu();
            }
        }


        if (key == KeyCode.P) {          // Plays victory animation
            FPStimeline.stop();
            CollisionTimeline.stop();
            menuSound.stop();
            gameSound.stop();
            overlayPane.setVisible(false);
            gamePane.getChildren().clear();
            AnimationUtilities animationUtilities = new AnimationUtilities(gamePane);
            animationUtilities.play();
        }
    }

    public void showMenu() {

        FPStimeline.pause();
        CollisionTimeline.pause();
        userNamePane.setVisible(false);
        settingsPane.setVisible(false);
        menuPane.setVisible(true);
        overlayPane.setVisible(false);
        highScorePane.setVisible(false);
    }

    public void showSettings() {
        menuPane.setVisible(false);
        settingsPane.setVisible(true);
    }

    public void resumeGame() {

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

        overlayPane.setVisible(true);
    }

    public boolean isUserNameSupplied() {
        if (userNameField.getText().isEmpty()) {
            userNameField.requestFocus();
            userNameField.setTooltip(new Tooltip("YOU MUST PROVIDE A USERNAME"));
            return false;
        } else {
            return true;
        }

    }

    public void newGame() {

        overlayPane.setVisible(true);
        resumeButton.setDisable(false);
        highScorePane.setVisible(false);
        userNameField.setTooltip(null);
        gamePane.getChildren().clear();

        // Create new score and bind value to label
        currentScore = new Score(userNameField.getText(), 0, difficulty);

        snake = new Snake();                          // Create new snake
        generateFood();

        for (Blocks block : snake.getSnakeArray()) {  // Add all blocks to gamePane
            gamePane.getChildren().add(block);
        }
        resumeGame();
    }

    public void quitGame() {
        Platform.exit();
    }

    public void playGameSound(Boolean play) {
        if (play) {
            gameSound.play(-1);
        } else {
            gameSound.stop();
        }

    }

    public void playMenuSound(Boolean play) {
        if (play) {
            menuSound.play(-1);
        } else {
            menuSound.stop();

        }

    }

    public void endGame() {
        FPStimeline.stop();       // Stop moving the snake..
        CollisionTimeline.stop();
        gamePane.getChildren().clear();  // Clear gamepane
        resumeButton.setDisable(true); // initial disables resume game button - no game to resume at startup
        showHighScoreOnEnd();
        currentScore = null;
    }

    public void showHighScoreOnEnd() {
        FPStimeline.pause();
        CollisionTimeline.pause();
        settingsPane.setVisible(false);
        menuPane.setVisible(false);
        overlayPane.setVisible(false);
        highScorePane.setVisible(true);

        gameSound.stop();
        menuSound.play(-1);

        if (currentScore == null) {
            new Score().showHighScores(highScorePane);
        } else {
            currentScore.addToObservableList();
            currentScore.showHighScores(highScorePane);   // Show highscores
            currentScore.writeCSV();                      // Add current score to csv
        }

    }

    // generates a new food token at a random location on the playfield
    public void generateFood() {

        gamePane.getChildren().remove(yumyum);
        boolean foodIsUnderSnake;

        do {
            foodIsUnderSnake = false;

            double rndX, rndY;
            rndX = Math.random() * 600;
            if (rndX >= 600) rndX = rndX - 20;
            rndY = Math.random() * 600;
            if (rndY >= 600) rndY = rndY - 20;
            rndX = Math.round(rndX);
            rndY = Math.round(rndY);

            rndX = rndX - (rndX % 20);
            rndY = rndY - (rndY % 20);

            yumyum = new Food();
            yumyum.setX(rndX);
            yumyum.setY(rndY);

            for (Blocks block : snake.getSnakeArray()) {
                if (yumyum.getHashValue() == block.getHashValue()) {
                    System.out.println("FOOD IS UNDER SNAKE!");
                    foodIsUnderSnake = true;
                    break;
                }
            }

        } while (foodIsUnderSnake);

        gamePane.getChildren().add(yumyum);
    }

    public static Food getFood() {
        return yumyum;
    }

    private void playRandomFoodSound() {
        Random r = new Random();
        switch (r.nextInt(3)) {
            case 0:
                foodBite.play(0);
                break;
            case 1:
                foodChomp.play(0);
                break;
            case 2:
                foodSlurp.play(0);
                break;
        }
    }

    private void hardModeSpeedBoost() {
        double currentSpeed = FPStimeline.getCurrentRate();
        KeyFrame speedStart = new KeyFrame(Duration.seconds(0), event -> {
            FPStimeline.setRate(10);
        });
        KeyFrame speedEnd = new KeyFrame(Duration.seconds(1), event -> {
            FPStimeline.setRate(currentSpeed);
        });
        Timeline speedBuffTime = new Timeline(
                speedStart, speedEnd
        );
        speedBuffTime.setCycleCount(1);
        speedBuffTime.play();
    }
}
