package Domain;


import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class Snake {

    private ArrayList<Blocks> snakeArray = new ArrayList<>();
    private Direction snakeDirection = Direction.RIGHT;

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
        } else if (snakeDirection == Direction.DOWN) {
            snakeArray.get(0).setY(snakeHeadY + snakeSize);
        } else if (snakeDirection == Direction.UP) {
            snakeArray.get(0).setY(snakeHeadY - snakeSize);
        } else if (snakeDirection == Direction.LEFT) {
            snakeArray.get(0).setX(snakeHeadX - snakeSize);
        }
    }

    public boolean checkCollision() {

        // Check collision with snake it self

        for (int i = 1; i <= snakeArray.size() - 1 ; i++) {

            int headHash = snakeArray.get(0).getHashValue();
            int checkHash = snakeArray.get(i).getHashValue();

            if(headHash == checkHash){
                return true;
            }
        }

        // Get position of the snake head
        double snakeX = snakeArray.get(0).getX();
        double snakeY = snakeArray.get(0).getY();

        // Check collision with borders (Pane is 600x600)
        if (snakeX >= 600 || snakeX < 0 || snakeY >= 600 || snakeY < 0) {
            return true;
        }

        // On no collision return false
        return false;

    }

    public void addSnakeBody(Pane pane) {

        // Copy info from last part of the snake
        Blocks lastBlock = snakeArray.get(snakeArray.size() - 1);
        double posX = lastBlock.getX();
        double posY = lastBlock.getY();

        snakeArray.add(new SnakeBody(posX, posY));
        pane.getChildren().add(snakeArray.get(snakeArray.size() - 1));

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
