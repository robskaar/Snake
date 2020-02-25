package Domain;

import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

public class Blocks extends Rectangle {

    private final int BLOCK_SIZE = 20;

    public Blocks(){
        this.setWidth(BLOCK_SIZE);
        this.setHeight(BLOCK_SIZE);
    }

    public int getBLOCK_SIZE() {
        return BLOCK_SIZE;
    }
}
