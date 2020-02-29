package Domain;

import javafx.scene.image.Image;

public class SnakeHead extends Blocks {

    // Position of snakeHead when game starts
    private final int POSITION_X = 300;
    private final int POSITION_Y = 300;

    public SnakeHead() {
        super(new Image("Resources/Images/snakeHeadAnimated.gif"));  // Set width, height and image
        this.setX(POSITION_X);
        this.setY(POSITION_Y);

        this.setScaleX(1.5);
        this.setScaleY(1.5);
    }
}
