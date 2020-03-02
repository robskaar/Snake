package Domain;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;

public class SnakeBody extends Blocks {

    public SnakeBody(double posX, double posY) {
        super(new Image("Resources/Images/transparantBody.png"));  // Set width, height and image
        this.setX(posX);
        this.setY(posY);
        this.setStrokeType(StrokeType.INSIDE);
        this.setStroke(Color.BLACK);
        this.setFill(Color.BLACK);
    }

}
