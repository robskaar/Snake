package Domain;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioPlayer {

    private AudioInputStream audioInputStream =  null;
    private Clip clip = null;

    public AudioPlayer(String filePath) {

        try{
            audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());

            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        }
        catch (IOException | UnsupportedAudioFileException | LineUnavailableException e){
            e.printStackTrace();
        }

    }

    public void play(int numberOfLoops) {
        clip.loop(numberOfLoops);
    }

    public void stop(){

            clip.stop();

    }
}
