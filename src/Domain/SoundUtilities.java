package Domain;

import javax.swing.*;
import java.util.Random;

public abstract class SoundUtilities {

    private static final AudioPlayer FOOD_SLURP = new AudioPlayer("src/Resources/Sound/Slurping+1.wav");
    private static final AudioPlayer FOOD_BITE = new AudioPlayer("src/Resources/Sound/bite.wav");
    private static final AudioPlayer FOOD_CHOMP = new AudioPlayer("src/Resources/Sound/Chomp+1.wav");
    private static final AudioPlayer MENU_SOUND = new AudioPlayer("src/Resources/Sound/MenuSound.wav", 0.5);
    private static final AudioPlayer GAME_SOUND = new AudioPlayer("src/Resources/Sound/GameSound.wav", 0.5);
    private static final AudioPlayer GAME_OVER = new AudioPlayer("src/Resources/Sound/GameOver2.wav");
    private static final AudioPlayer VICTORY = new AudioPlayer("src/Resources/Sound/victoryFanfareCut1.wav");
    private static final AudioPlayer WUHUU = new AudioPlayer("src/Resources/Sound/Wuhu.wav");
    private static final AudioPlayer SCREAM = new AudioPlayer("src/Resources/Sound/Scream.wav",1);

    public static void muteStatus(Boolean mute){
        if (mute){
            GAME_SOUND.setVolume(0.0);
            MENU_SOUND.setVolume(0.0);
        }
        else{
            GAME_SOUND.setVolume(0.05);
            MENU_SOUND.setVolume(0.05);
        }
    }


    public static void playGameSound(Boolean play) {
        if (play) {
            GAME_SOUND.play(-1);
        } else {
            GAME_SOUND.stop();
        }

    }

    public static void playMenuSound(Boolean play) {
        if (play) {
            MENU_SOUND.play(-1);
        } else {
            MENU_SOUND.stop();
        }
    }

    public static void playGameOverSound(Boolean play) {
        if (play) {
            GAME_OVER.play(0);
        } else {
            GAME_OVER.stop();
        }
    }

    public static void playVictorySound(Boolean play) {
        if (play) {
            VICTORY.play(3);
        } else {
            VICTORY.stop();
        }
    }

    public static void playRandomFoodSound() {
        Random r = new Random();
        switch (r.nextInt(3)) {
            case 0:
                FOOD_BITE.play(0);
                break;
            case 1:
                FOOD_CHOMP.play(0);
                break;
            case 2:
                FOOD_SLURP.play(0);
                break;
        }
    }

    public static void playSpeedBoost(Boolean play) {
        if (play) {
            WUHUU.play(0);
        } else {
            WUHUU.stop();
        }
    }

    public static void playRotatePane(Boolean play) {
        if (play) {
            SCREAM.play(0);

        } else {
            SCREAM.stop();
        }
    }
}
