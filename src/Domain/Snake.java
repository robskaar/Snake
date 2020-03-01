package Domain;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.layout.Pane;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import GUI.mainController;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class Snake {

    private ArrayList<Blocks> snakeArray = new ArrayList<>();
    private Direction snakeDirection = Direction.RIGHT;
    private Food yumyum;

    public Snake() {
        snakeArray.add(new SnakeHead());                    // Add snake head
        snakeArray.add(new SnakeBody(280,300));  // Add body part
        snakeArray.add(new SnakeBody(260,300));  // Add body part
    }

    public void moveSnake() {

        //Move snake body parts
        //Moves to the position of the part in front of it
        for (int i = snakeArray.size(); i > 1; i--) {
            snakeArray.get(i - 1).setX(snakeArray.get(i - 2).getX());
            snakeArray.get(i - 1).setY(snakeArray.get(i - 2).getY());
        }

        // Move snake head

        double snakeHeadX = snakeArray.get(0).getX();
        double snakeHeadY = snakeArray.get(0).getY();
        int snakeSize = snakeArray.get(0).getBLOCK_SIZE();

        if (snakeDirection == Direction.RIGHT) {
            snakeArray.get(0).setX(snakeHeadX + snakeSize);
            snakeArray.get(0).setRotate(360);
        } else if (snakeDirection == Direction.DOWN) {
            snakeArray.get(0).setY(snakeHeadY + snakeSize);
            snakeArray.get(0).setRotate(90);
        } else if (snakeDirection == Direction.UP) {
            snakeArray.get(0).setY(snakeHeadY - snakeSize);
            snakeArray.get(0).setRotate(-90);
        } else if (snakeDirection == Direction.LEFT) {
            snakeArray.get(0).setX(snakeHeadX - snakeSize);
            snakeArray.get(0).setRotate(-180);
        }
    }

    public boolean checkFoodCollision() {
        boolean result = false;

        int headHash = snakeArray.get(0).getHashValue();
        int foodHash = mainController.getFood().getHashValue();

        if(headHash == foodHash){
            result = true;
        }

        return result;
    }

    public boolean checkSelfCollision() {

        // Check collision with snake itself

        boolean result = false;

        for (int i = 1; i <= snakeArray.size() - 1 ; i++) {

            int headHash = snakeArray.get(0).getHashValue();
            int checkHash = snakeArray.get(i).getHashValue();

            if(headHash == checkHash){
                result = true;
            }
        }

        return result;
    }

    public boolean checkBorderCollision(){

        // checking if snake is out of bounds of playing fields

        boolean result = false;

        // Get position of the snake head
        double snakeX = snakeArray.get(0).getX();
        double snakeY = snakeArray.get(0).getY();

        // Check collision with borders (Pane is 600x600)
        if (snakeX >= 600 || snakeX < 0 || snakeY >= 600 || snakeY < 0) {
            result =  true;
        }

        return result;
    }

    public void addSnakeBody(Pane pane) {

        // Copy info from last part of the snake
        Blocks lastBlock = snakeArray.get(snakeArray.size() - 1);
        double posX = lastBlock.getX();
        double posY = lastBlock.getY();

        snakeArray.add(new SnakeBody(posX, posY));
        pane.getChildren().add(snakeArray.get(snakeArray.size() - 1));

    }

    public void changeBodyColor(){

        Queue<Integer> queue = new LinkedList<>();
        for (int i = 1; i < this.getSnakeArray().size() ; i++) {
            queue.add(i);
        }

        Timeline timeline = new Timeline();
        timeline.setAutoReverse(false);
        timeline.setCycleCount(queue.size());
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(150), ActionEvent -> {

            if(!queue.isEmpty()){
                AnimationUtilities.shapeColorShow(this.getSnakeArray().get(queue.poll()),true,Duration.millis(100),2);
            }

        }));
        timeline.play();

    }

    public ArrayList<Blocks> getSnakeArray() {
        return snakeArray;
    }

    public Direction getSnakeDirection() {
        return snakeDirection;
    }

    public void setSnakeDirection(Direction snakeDirection) {
        this.snakeDirection = snakeDirection;
    }


}
