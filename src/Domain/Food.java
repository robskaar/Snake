package Domain;

import javafx.scene.image.Image;

import java.util.Random;

public class Food extends Blocks {

    public Food() {
        super(new Image(randomFood()));  // Set width, height and image

        this.setScaleX(1.5);
        this.setScaleY(1.5);
    }

    public Food(int x, int y){
        this();

        this.setX(x);
        this.setY(y);
    }

    private static String randomFood() {
        Random r = new Random();
        String foodString;
        switch (r.nextInt(3)) {
            case 0:
                foodString = "Resources/Images/Strawberry.png";
                break;
            case 1:
                foodString = "Resources/Images/Apple.png";
                break;
            case 2:
                foodString = "Resources/Images/orange.png";
                break;
            default:
                foodString = "Resources/Images/snakeFood.png";
        }
        return foodString;
    }

}
