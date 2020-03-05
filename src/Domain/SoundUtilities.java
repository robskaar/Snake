package Domain;

import GUI.mainController;

import java.util.Random;


public abstract class SoundUtilities {

    private static final AudioPlayer FOOD_SLURP = new AudioPlayer("src/Resources/Sound/Slurping+1.wav");
    private static final AudioPlayer FOOD_BITE = new AudioPlayer("src/Resources/Sound/bite.wav");
    private static final AudioPlayer FOOD_CHOMP = new AudioPlayer("src/Resources/Sound/Chomp+1.wav");
    private static final AudioPlayer MENU_SOUND = new AudioPlayer("src/Resources/Sound/MenuSound.wav", 0.05);
    private static final AudioPlayer GAME_SOUND = new AudioPlayer("src/Resources/Sound/GameSound.wav", 0.05);
    private static final AudioPlayer GAME_OVER = new AudioPlayer("src/Resources/Sound/GameOver2.wav");
    private static final AudioPlayer VICTORY = new AudioPlayer("src/Resources/Sound/victoryFanfareCut1.wav");
    private static final AudioPlayer WUHUU = new AudioPlayer("src/Resources/Sound/Wuhu.wav");
    private static final AudioPlayer SCREAM = new AudioPlayer("src/Resources/Sound/Scream.wav", 1);
    private static final AudioPlayer BUTTON_HOVER = new AudioPlayer("src/Resources/Sound/buttonHover.wav",0.5);
    private static final AudioPlayer HEAD_GROW = new AudioPlayer("src/Resources/Sound/GrowHead.wav");
    private static double currentMusicLvl;
    private static double currentSoundLvl;

    public static void muteStatus(Boolean mute) {
        if (mute) {
            FOOD_SLURP.setVolume(0.0);
            FOOD_BITE.setVolume(0.0);
            FOOD_CHOMP.setVolume(0.0);
            MENU_SOUND.setVolume(0.0);
            GAME_SOUND.setVolume(0.0);
            GAME_OVER.setVolume(0.0);
            VICTORY.setVolume(0.0);
            WUHUU.setVolume(0.0);
            SCREAM.setVolume(0.0);
            BUTTON_HOVER.setVolume(0.0);
        }
        else {
            FOOD_SLURP.setVolume(currentSoundLvl);
            FOOD_BITE.setVolume(currentSoundLvl);
            FOOD_CHOMP.setVolume(currentSoundLvl);
            MENU_SOUND.setVolume(currentMusicLvl);
            GAME_SOUND.setVolume(currentMusicLvl);
            GAME_OVER.setVolume(currentSoundLvl);
            VICTORY.setVolume(currentSoundLvl);
            WUHUU.setVolume(currentSoundLvl);
            SCREAM.setVolume(currentSoundLvl);
            BUTTON_HOVER.setVolume(currentSoundLvl);
        }
    }

    public static void controlSoundLevel(double sliderVal) {
        System.out.println(sliderVal);
        FOOD_SLURP.setVolume(sliderVal);
        FOOD_BITE.setVolume(sliderVal);
        FOOD_CHOMP.setVolume(sliderVal);
        GAME_OVER.setVolume(sliderVal);
        VICTORY.setVolume(sliderVal);
        WUHUU.setVolume(sliderVal);
        SCREAM.setVolume(sliderVal);
        BUTTON_HOVER.setVolume(sliderVal);
        currentSoundLvl = sliderVal;
    }
    public static void controlMusicLevel(double sliderVal) {
        System.out.println(sliderVal);

        MENU_SOUND.setVolume(sliderVal);
        GAME_SOUND.setVolume(sliderVal);
        currentMusicLvl = sliderVal;
    }

    public static void playGameSound(Boolean play) {
        if (play) {
            GAME_SOUND.play(-1);
        }
        else {
            GAME_SOUND.stop();
        }

    }

    public static void playGrowHead(Boolean play) {
        if (play) {
            HEAD_GROW.play(0);
        }
        else {
            HEAD_GROW.stop();
        }

    }

    public static void playHoverSound(Boolean play) {
        if (play) {
            BUTTON_HOVER.play(0);
        }
        else {
            BUTTON_HOVER.stop();
        }
    }

    public static void playMenuSound(Boolean play) {
        if (play) {
            MENU_SOUND.play(-1);
        }
        else {
            MENU_SOUND.stop();
        }
    }

    public static void playGameOverSound(Boolean play) {
        if (play) {
            GAME_OVER.play(0);
        }
        else {
            GAME_OVER.stop();
        }
    }

    public static void playVictorySound(Boolean play) {
        if (play) {
            VICTORY.play(3);
        }
        else {
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
        }
        else {
            WUHUU.stop();
        }
    }

    public static void playRotatePane(Boolean play) {
        if (play) {
            SCREAM.play(0);

        }
        else {
            SCREAM.stop();
        }
    }
}
