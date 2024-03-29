package GUI;

import Domain.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.jar.Attributes;

import static Domain.Direction.*;


public class mainController implements Initializable {
    @FXML
    private ToggleButton blackBackgroundButton;
    @FXML
    private ToggleButton normalBackgroundButton;
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
    private Button highScoreButton;
    @FXML
    private Button quitButton;
    @FXML
    private Button settingsButton;
    @FXML
    private AnchorPane gamePane;
    @FXML
    private AnchorPane userNamePane;
    @FXML
    private AnchorPane settingsPane;
    @FXML
    private AnchorPane menuPane;
    @FXML
    private AnchorPane winnerPane;
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
    @FXML
    private Button soundButton3;
    @FXML
    private Button soundButton2;
    @FXML
    private Button soundButton1;
    @FXML
    private Slider soundSlider;
    @FXML
    private Slider musicSlider;

    public static boolean muteStatus = false;
    static boolean winnerAnimationActive;
    static Food yumYum;
    static Snake snake;
    static String difficulty;

    static String extraBackground;
    static Score currentScore;
    AnimationUtilities animationUtilities;
    Timeline foodTimeLime = new Timeline();
    Timeline FPSTimeline = new Timeline();
    Timeline CollisionTimeline = new Timeline();
    ToggleGroup levelDifficulty = new ToggleGroup(); // toggle group for level difficulty
    ToggleGroup levelExtraBackground = new ToggleGroup();
    static Direction pressedDirection = RIGHT;

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        musicSlider.setOnMouseDragged(e -> {
            SoundUtilities.controlMusicLevel(musicSlider.getValue());
        });
        musicSlider.setOnMouseClicked(e -> {
            SoundUtilities.controlMusicLevel(musicSlider.getValue());
        });
        soundSlider.setOnMouseDragged(e -> {
            SoundUtilities.controlSoundLevel(soundSlider.getValue());
        });
        soundSlider.setOnMouseClicked(e -> {
            SoundUtilities.controlSoundLevel(soundSlider.getValue());
        });

        highScoreButton.setOnMouseEntered(e -> {
            SoundUtilities.playHoverSound(true);
        });
        newGameButton.setOnMouseEntered(e -> {
            SoundUtilities.playHoverSound(true);
        });
        resumeButton.setOnMouseEntered(e -> {
            SoundUtilities.playHoverSound(true);
        });
        quitButton.setOnMouseEntered(e -> {
            SoundUtilities.playHoverSound(true);
        });
        settingsButton.setOnMouseEntered(e -> {
            SoundUtilities.playHoverSound(true);
        });

        blackBackgroundButton.setSelected(false);
        normalBackgroundButton.setSelected(true);
        SoundUtilities.playMenuSound(true);                        // start the menu sound
        normalDifficultyButton.setSelected(true);   // sets initial difficulty to normal
        normalDifficultyButton.arm();               // sets initial difficulty to normal
        setDifficulty();                                  // load the initial difficulty level
        setBackground();
        overlayPane.setFocusTraversable(true);       // Make key input possible on overlay pane
        initFPSTimeline();                           // Timeline to move snake
        initCollisionTimeline();                     // Timeline to detect collision
        initFoodTimeLine();                         // time to spawn and despawn food
        Score.initHighScores(highScorePane);
        FPSTimeline.pause();                        // pauses timeline
        CollisionTimeline.pause();                  // pauses timeline
        foodTimeLime.pause();                       //pauses timelime
        menuPane.setVisible(true);                   //initially shows main menu
        overlayPane.setVisible(false);               // Hide overlay
        highScorePane.setVisible(false);
        winnerPane.setVisible(false);

        AnimationUtilities.drawGameGrid(gameUnderlayPane); // Draw grass and grid


        userNameField.setOnAction(e -> {
            if (userNameField.getText().isEmpty()) {
                userNameField.requestFocus();
                userNameField.setTooltip(new Tooltip("Please provide a username"));
            } else {
                userNamePane.setVisible(false);
                SoundUtilities.playMenuSound(false);
                SoundUtilities.playGameSound(true);
                newGame();
            }
        });
        resumeButton.setDisable(true); // initial disables resume game button - no game to resume at startup
        resumeButton.setOnAction(e -> { // set on action for resume button
            SoundUtilities.playMenuSound(false);
            SoundUtilities.playGameSound(true);
            resumeGame();
        });

        newGameButton.setOnAction(e -> { // set on action for new game button
            setUserName();
        });


    }

    public void mute() {

        if (muteStatus) {
            soundButton1.setStyle("-fx-background-image: url('/Resources/Images/sound.png');");
            soundButton2.setStyle("-fx-background-image: url('/Resources/Images/sound.png');");
            soundButton3.setStyle("-fx-background-image: url('/Resources/Images/sound.png');");
            soundSlider.getValue();
            muteStatus = false;
        } else {
            soundButton1.setStyle("-fx-background-image: url('/Resources/Images/mute.png');");
            soundButton2.setStyle("-fx-background-image: url('/Resources/Images/mute.png');");
            soundButton3.setStyle("-fx-background-image: url('/Resources/Images/mute.png');");
            muteStatus = true;
        }
        SoundUtilities.muteStatus(muteStatus);
    }

    public void setUserName() {
        menuPane.setVisible(false);
        userNamePane.setVisible(true);
        userNameField.requestFocus();
    }

    public void setDifficulty() {

        easyDifficultyButton.setToggleGroup(levelDifficulty);
        normalDifficultyButton.setToggleGroup(levelDifficulty);
        hardDifficultyButton.setToggleGroup(levelDifficulty);

        if (easyDifficultyButton.isArmed()) {
            FPSTimeline.setRate(2);
            difficulty = "Easy";
        } else if (hardDifficultyButton.isArmed()) {
            FPSTimeline.setRate(6);
            difficulty = "Hard";
        } else if (normalDifficultyButton.isArmed()) {
            FPSTimeline.setRate(4);
            difficulty = "Normal";
        }

    }

    public void setBackground() {

        normalBackgroundButton.setToggleGroup(levelExtraBackground);
        blackBackgroundButton.setToggleGroup(levelExtraBackground);

        if (blackBackgroundButton.isArmed()) {
            AnimationUtilities.setBackground(true);
        }
        else{
            AnimationUtilities.setBackground(false);
        }

    }



    public void initFoodTimeLine() {
        foodTimeLime.setAutoReverse(false);
        foodTimeLime.setCycleCount(Animation.INDEFINITE);

        if (difficulty.contains("Hard")) {

            foodTimeLime.getKeyFrames().add(new KeyFrame(Duration.seconds(6), ActionEvent -> {

                generateFood();

            }));

        }
        foodTimeLime.play();
    }

    public void initFPSTimeline() {

        FPSTimeline.setAutoReverse(false);
        FPSTimeline.setCycleCount(Animation.INDEFINITE);
        FPSTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(500), ActionEvent -> {

            snake.setSnakeDirection(pressedDirection);
            snake.moveSnake();

        }));

        FPSTimeline.play();
    }

    public void initCollisionTimeline() {

        CollisionTimeline.setAutoReverse(false);
        CollisionTimeline.setCycleCount(Animation.INDEFINITE);
        CollisionTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(1), ActionEvent -> {

            // Check if snake collides with anything
            boolean hasSelfCollision, hasBorderCollision, hasFoodCollision;
            hasSelfCollision = snake.checkSelfCollision();
            hasBorderCollision = snake.checkBorderCollision();
            hasFoodCollision = snake.checkFoodCollision(yumYum, snake.getHeadstate());

            // End game if collision with self or border is detected
            if (hasSelfCollision || hasBorderCollision) {
                snake.getSnakeArray().get(0).toFront();
                SoundUtilities.playGameOverSound(true);
                endGame();
            }

            // expand snake if collision with food is detected
            if (hasFoodCollision) {
                currentScore.setScore(currentScore.getScore() + 1);
                score.setText(Integer.toString(currentScore.getScore()));
                generateFood();
                SoundUtilities.playRandomFoodSound();
                foodTimeLime.playFromStart();

                if (difficulty.contains("Hard")) {

                    if (rollTheDice(25)) {
                        SoundUtilities.playSpeedBoost(true);
                        hardModeSpeedBoost();
                    }
                    if (rollTheDice(20)) {
                        SoundUtilities.playGrowHead(true);
                        hardModeBigHead();
                    }
                    if (rollTheDice(10)) {
                        SoundUtilities.playRotatePane(true);
                        AnimationUtilities.rotatePane(5, gamePane);
                    }

                }

                snake.changeBodyColor(gamePane);

            }

        }));

        CollisionTimeline.play();
    }

    public void getGameInput(KeyEvent ke) {

        KeyCode key = ke.getCode();
        Timer t = new java.util.Timer();
        if (key == KeyCode.DOWN && snake.getSnakeDirection() != Direction.UP) {
            if(difficulty.equals("Hard") && snake.getSnakeDirection() == DOWN){
                t.schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                FPSTimeline.setRate(FPSTimeline.getRate()*0.5);
                                t.cancel();
                            }
                        },
                        3000
                );
                FPSTimeline.setRate(FPSTimeline.getRate()*2);
            }
            pressedDirection = DOWN;
        } else if (key == KeyCode.LEFT && snake.getSnakeDirection() != RIGHT) {
            if(difficulty.equals("Hard") && snake.getSnakeDirection() == LEFT){
                t.schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                FPSTimeline.setRate(FPSTimeline.getRate()*0.5);
                                t.cancel();
                            }
                        },
                        3000
                );
                FPSTimeline.setRate(FPSTimeline.getRate()*2);
            }
            pressedDirection = LEFT;
        } else if (key == KeyCode.RIGHT && snake.getSnakeDirection() != LEFT) {
            if(difficulty.equals("Hard") && snake.getSnakeDirection() == RIGHT){
                t.schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                FPSTimeline.setRate(FPSTimeline.getRate()*0.5);
                                t.cancel();
                            }
                        },
                        3000
                );
                FPSTimeline.setRate(FPSTimeline.getRate()*2);
            }
            pressedDirection = RIGHT;
        } else if (key == KeyCode.UP && snake.getSnakeDirection() != DOWN) {
            if(difficulty.equals("Hard") && snake.getSnakeDirection() == UP){
                t.schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                FPSTimeline.setRate(FPSTimeline.getRate()*0.5);
                                t.cancel();
                            }
                        },
                        3000
                );
                FPSTimeline.setRate(FPSTimeline.getRate()*2);
            }
            pressedDirection = UP;
        }

        // Other input

        if(key == KeyCode.ENTER){
            if(menuPane.isVisible()) newGame();
            else if(highScorePane.isVisible()) showMenu();
        }

        if (key == KeyCode.SPACE) {              // pause game
            if (!FPSTimeline.getStatus().equals(Animation.Status.PAUSED)) {
                SoundUtilities.playGameSound(false);
                SoundUtilities.playMenuSound(true);
                showMenu();
            }
        }

        if (key == KeyCode.P) {          // Plays victory animation

            if (winnerAnimationActive) {
                animationUtilities.stopAnimation();
                winnerPane.getChildren().clear();
                winnerPane.setVisible(false);
                endGame();
            } else {
                animationUtilities = new AnimationUtilities(winnerPane);
                winnerPane.setVisible(true);
                winnerAnimationActive = true;
                FPSTimeline.stop();
                CollisionTimeline.stop();
                foodTimeLime.stop();
                SoundUtilities.playGameSound(false);
                SoundUtilities.playMenuSound(false);
                score.setStyle("-fx-text-fill: transparent");
                animationUtilities.playVictoryAnimation();
            }

        }
    }

    public void getMenuInput(KeyEvent ke){

        KeyCode key = ke.getCode();

        if(key == KeyCode.ENTER){
            if(menuPane.isVisible()) setUserName();
            else if(highScorePane.isVisible()) showMenu();
        }
    }

    public void showMenu() {
        FPSTimeline.pause();
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
            FPSTimeline.pause();
            CollisionTimeline.pause();
            foodTimeLime.pause();
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
            FPSTimeline.play();
            CollisionTimeline.play();
            foodTimeLime.play();
        });

        Timeline countDowns = new Timeline(
                countOne, countTwo, countThree, resume
        );
        countDowns.setCycleCount(1);
        countDowns.play();

        overlayPane.setVisible(true);
    }

    public void newGame() {
        winnerAnimationActive = false;
        overlayPane.setVisible(true);
        resumeButton.setDisable(false);
        highScorePane.setVisible(false);
        userNameField.setTooltip(null);
        gamePane.getChildren().clear();

        // Create new score and bind value to label
        currentScore = new Score(userNameField.getText(), 0, difficulty);

        snake = new Snake();                          // Create new snake
        generateFood();
        snake.setHeadstate(Snake.Headstate.NORMAL);
        for (Blocks block : snake.getSnakeArray()) {  // Add all blocks to gamePane
            gamePane.getChildren().add(block);
        }
        resumeGame();
    }

    public void quitGame() {
        Platform.exit();
    }

    public void endGame() {
        FPSTimeline.stop();       // Stop moving the snake..
        CollisionTimeline.stop();
        foodTimeLime.stop();
        pressedDirection = RIGHT;
        gamePane.getChildren().clear();  // Clear gamepane
        resumeButton.setDisable(true); // initial disables resume game button - no game to resume at startup
        showHighScores();
        currentScore = null;
        score.setText("0");
    }

    public void showHighScores() {
        FPSTimeline.pause();
        CollisionTimeline.pause();
        settingsPane.setVisible(false);
        menuPane.setVisible(false);
        overlayPane.setVisible(false);
        highScorePane.setVisible(true);

        SoundUtilities.playGameSound(false);
        SoundUtilities.playMenuSound(true);

        if (currentScore == null) {
            new Score().showHighScores();
        } else {
            currentScore.addToObservableList();
            currentScore.showHighScores();   // Show highscores
            currentScore.writeCSV();                      // Add current score to csv
        }


    }

    public void generateFood() {
        gamePane.getChildren().remove(yumYum);
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

            yumYum = new Food();
            yumYum.setX(rndX);
            yumYum.setY(rndY);

            for (Blocks block : snake.getSnakeArray()) {
                if (yumYum.getHashValue() == block.getHashValue()) {
                    System.out.println("FOOD IS UNDER SNAKE!");
                    foodIsUnderSnake = true;
                    break;
                }
            }

        } while (foodIsUnderSnake);

        System.out.println("x " + yumYum.getX() + " y " + yumYum.getY());

        gamePane.getChildren().add(yumYum);
    }

    private void hardModeSpeedBoost() {
        KeyFrame speedStart = new KeyFrame(Duration.seconds(0), event -> {
            FPSTimeline.setRate(10);
        });
        KeyFrame speedEnd = new KeyFrame(Duration.seconds(1), event -> {
            FPSTimeline.setRate(4);
        });
        Timeline speedBuffTime = new Timeline(
                speedStart, speedEnd
        );
        speedBuffTime.setCycleCount(1);
        speedBuffTime.play();
    }

    private void hardModeBigHead() {
        KeyFrame bigHeadTimer = new KeyFrame(Duration.seconds(0), event -> {
            snake.getSnakeArray().get(0).setScaleX(3);
            snake.getSnakeArray().get(0).setScaleY(3);
            snake.setHeadstate(Snake.Headstate.BIG);
        });
        KeyFrame bigHeadTimerEnd = new KeyFrame(Duration.seconds(5), event -> {
            snake.getSnakeArray().get(0).setScaleX(1.5);
            snake.getSnakeArray().get(0).setScaleY(1.5);
            snake.setHeadstate(Snake.Headstate.NORMAL);
        });
        Timeline headBuffTimer = new Timeline(
                bigHeadTimer, bigHeadTimerEnd
        );
        headBuffTimer.setCycleCount(1);
        headBuffTimer.play();
    }

    private boolean rollTheDice(int percentChance) {

        if (percentChance > 100 || percentChance < 0) {
            throw new IllegalArgumentException("Percent can only be between 0-100");
        }

        Random ran = new Random();
        int randomNumber = ran.nextInt(100) + 1;

        return randomNumber < percentChance + 1;

    }
}