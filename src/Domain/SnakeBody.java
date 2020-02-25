package Domain;

import javafx.scene.image.Image;

public class SnakeBody extends Blocks {

    public SnakeBody(double posX, double posY) {
        super(new Image("Resources/Images/snakeBody.png"));  // Set width, height and image
        this.setX(posX);
        this.setY(posY);
    }

}
