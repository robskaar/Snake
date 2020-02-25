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
}
