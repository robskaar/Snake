package Domain;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class Blocks extends Rectangle {

    public final int BLOCK_SIZE = 20;

    public Blocks(Image img) {
        this.setWidth(BLOCK_SIZE);
        this.setHeight(BLOCK_SIZE);
        this.setFill(new ImagePattern(img));
    }

    public int getBLOCK_SIZE() {
        return BLOCK_SIZE;
    }

    public int getHashValue() {

        // Calculating an unique value with the Cantor Pairing hash function

        int x = (int) this.getX();
        int y = (int) this.getY();

        return (((x + y) * (x + y + 1)) / 2) + y;
    }
}
