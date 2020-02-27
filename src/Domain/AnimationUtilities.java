package Domain;

import GUI.Main;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class AnimationUtilities {

    private Pane pane;

    public AnimationUtilities(Pane pane) {
        this.pane = pane;
    }

    public void play() {
        winnerSnake();
    }

    private void winnerSnake() {

        final int BLOCK_SIZE = 30;
        final int PANE_SIZE = 600;
        final int ANIMATION_TIME_MS = 3000;

        // Creates queue with blocks that covers whole pane
        Queue<Rectangle> queue = new LinkedList<>();
        for (int i = 0; i < PANE_SIZE; i += BLOCK_SIZE * 2 ) {
            for (int j = 0; j < PANE_SIZE; j += BLOCK_SIZE) {
                queue.add(new Rectangle(j, i, BLOCK_SIZE, BLOCK_SIZE));
            }
            for (int j = PANE_SIZE - BLOCK_SIZE; j >= 0; j -= BLOCK_SIZE) {
                queue.add(new Rectangle(j, i + BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE));
            }
        }

        Timeline timeline = new Timeline();
        timeline.setAutoReverse(false);
        timeline.setCycleCount(queue.size());
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(ANIMATION_TIME_MS / (Math.pow(PANE_SIZE/BLOCK_SIZE,2))), ActionEvent -> {

            Rectangle rectangle = queue.poll();

            // Set black outline on blocks
            rectangle.setStrokeType(StrokeType.INSIDE);
            rectangle.setStrokeWidth(2);
            rectangle.setStroke(Color.BLACK);

            pane.getChildren().add(rectangle);

            // Makes every block blink in random colors
            shapeColorShow(rectangle,true,Duration.millis(20),300);

        }));

        timeline.play();

        timeline.setOnFinished(ActionEvent -> {
            winnerLabel();
        });

    }

    private void confettiCanon() {

        Random ran = new Random();
        int circleSpawnX = 410;
        int circleSpawnY = 240;

        Timeline victoryTimeline = new Timeline();
        victoryTimeline.setAutoReverse(false);
        victoryTimeline.setCycleCount(1000);
        victoryTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(10), ActionEvent -> {

            Circle circle = new Circle(ran.nextInt(15) + 2);
            circle.setLayoutX(circleSpawnX);
            circle.setLayoutY(circleSpawnY);
            circle.setStrokeType(StrokeType.INSIDE);
            circle.setStroke(Color.BLACK);

            shapeColorShow(circle, true , Duration.millis(250) , 50);

            pane.getChildren().add(circle);

            int x = ran.nextInt(600) - circleSpawnX;
            int y = ran.nextInt(600) - circleSpawnY;

            Path path = new Path();
            path.getElements().add(new MoveTo(0, 0));   // Where movement will start
            path.getElements().add(new LineTo(x, y));         // Move to this x,y in straight line

            PathTransition pathTransition = new PathTransition();
            pathTransition.setNode(circle);
            pathTransition.setDuration(Duration.seconds(1));
            pathTransition.setPath(path);
            pathTransition.setCycleCount(1);
            pathTransition.play();

        }));

        victoryTimeline.play();

    }

    private void fire() {

        final Duration DURATION = Duration.millis(3000);

        ImageView imageView = new ImageView();
        imageView.setImage(new Image("Resources/Images/fire.gif"));
        imageView.setFitHeight(40);
        imageView.setFitWidth(40);
        imageView.setLayoutX(387);
        imageView.setLayoutY(215);

        pane.getChildren().add(imageView);

        // Scale fire
        ScaleTransition scaleTransition = new ScaleTransition(DURATION, imageView);
        scaleTransition.setToX(3);
        scaleTransition.setToY(3);
        scaleTransition.setOnFinished(ActionEvent -> {
            confettiCanon();
        });

        // Move fire a little up to compensate for scaling
        TranslateTransition translateTransition = new TranslateTransition(DURATION, imageView);
        translateTransition.setToY(-30);

        scaleTransition.play();
        translateTransition.play();

        nodeToFront(imageView);

    }

    private void winnerLabel() {

        Text text = new Text("You Win");
        text.setStyle("-fx-font-size: 10");
        text.setFont(Main.getWinnerFont());
        text.setFill(Color.BLACK);
        text.setStrokeType(StrokeType.INSIDE);
        text.setStrokeWidth(1);
        text.setStroke(Color.WHITE);
        text.setLayoutX(270);
        text.setLayoutY(300);

        pane.getChildren().add(text);

        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(1000), text);
        scaleTransition.setToX(8);
        scaleTransition.setToY(8);
        scaleTransition.setDelay(Duration.millis(3000));
        scaleTransition.setOnFinished(ActionEvent -> {
            fire();
        });
        scaleTransition.play();

        nodeToFront(text);
    }

    private void nodeToFront(Node node) {

        Timeline timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.setAutoReverse(false);
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(1), ActionEvent -> {
            node.toFront();
        }));
        timeline.play();
    }

    private void shapeColorShow(Shape shape, Boolean endOnBlack, Duration duration , int cycleCount) {

        Timeline timeline = new Timeline();
        timeline.setAutoReverse(false);
        timeline.setCycleCount(cycleCount);
        timeline.getKeyFrames().add(new KeyFrame(duration, ActionEvent -> {

            shape.setFill(Color.color(Math.random(), Math.random(), Math.random()));

        }));

        timeline.setOnFinished(ActionEvent -> {
            if (endOnBlack) {
                shape.setFill(Color.BLACK);
            }
        });

        timeline.play();
    }
}
