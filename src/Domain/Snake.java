package Domain;

import java.util.ArrayList;

public class Snake {

    private ArrayList<Blocks> snakeArray = new ArrayList<>();
    private Direction snakeDirection = Direction.RIGHT;

    public Snake(){
        snakeArray.add(new SnakeHead());       // Add snake head
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

    public ArrayList<Blocks> getSnakeArray() {
        return snakeArray;
    }

    public void setSnakeArray(ArrayList<Blocks> snakeArray) {
        this.snakeArray = snakeArray;
    }

    public Direction getSnakeDirection() {
        return snakeDirection;
    }

    public void setSnakeDirection(Direction snakeDirection) {
        this.snakeDirection = snakeDirection;
    }
}
