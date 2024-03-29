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

import static Domain.Direction.DOWN;
import static Domain.Direction.UP;

public class Snake {

    private ArrayList<Blocks> snakeArray = new ArrayList<>();
    private Direction snakeDirection = Direction.RIGHT;

    public enum Headstate {NORMAL, BIG};
    private Headstate head = Headstate.NORMAL;

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
        } else if (snakeDirection == DOWN) {
            snakeArray.get(0).setY(snakeHeadY + snakeSize);
            snakeArray.get(0).setRotate(90);
        } else if (snakeDirection == UP) {
            snakeArray.get(0).setY(snakeHeadY - snakeSize);
            snakeArray.get(0).setRotate(-90);
        } else if (snakeDirection == Direction.LEFT) {
            snakeArray.get(0).setX(snakeHeadX - snakeSize);
            snakeArray.get(0).setRotate(-180);
        }
    }

    public boolean checkFoodCollision(Food food, Headstate headSize) {
        boolean result = false;

        if(headSize == Headstate.NORMAL){
            int headHash = snakeArray.get(0).getHashValue();
            int foodHash = food.getHashValue();
            if(headHash == foodHash){
                result = true;
            }
        } else {
            int foodHash = food.getHashValue();

            int x = (int) snakeArray.get(0).getX();
            int y = (int) snakeArray.get(0).getY();

            ArrayList<Integer> hashes = new ArrayList<Integer>();

            hashes.add(calcUnboundHash(x-20,y-20));
            hashes.add(calcUnboundHash(x,y-20));
            hashes.add(calcUnboundHash(x+20,y-20));

            hashes.add(calcUnboundHash(x-20,y));
            hashes.add(calcUnboundHash(x,y));
            hashes.add(calcUnboundHash(x+20,y));

            hashes.add(calcUnboundHash(x-20,y+20));
            hashes.add(calcUnboundHash(x,y+20));
            hashes.add(calcUnboundHash(x+20,y+20));

            for(int hash : hashes){
                if(foodHash==hash)result=true;
            }
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

    public void changeBodyColor(Pane pane){

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
        timeline.setOnFinished(ActionEvent -> {
            this.addSnakeBody(pane);
        });

    }

    // Getter and Setters

    public ArrayList<Blocks> getSnakeArray() {
        return snakeArray;
    }

    public Direction getSnakeDirection() {
        return snakeDirection;
    }

    public void setSnakeDirection(Direction snakeDirection) {
        this.snakeDirection = snakeDirection;
    }

    public void setHeadstate (Headstate size){
        this.head=size;
    }

    public Headstate getHeadstate(){
        return this.head;
    }

    public int calcUnboundHash(int x, int y){
        return (((x + y) * (x + y + 1)) / 2) + y;
    }
}
