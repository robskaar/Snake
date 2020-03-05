package Domain;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioPlayer {

    private AudioInputStream audioInputStream = null;
    private Clip clip = null;

    public AudioPlayer(String filePath) {

        try {
            audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());

            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            e.printStackTrace();
        }

    }


    public AudioPlayer(String filePath, double volume) {

        try {
            audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());

            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            e.printStackTrace();
        }

        setVolume(volume);

    }

    /**
     * plays the clip with defined amount of loops
     *
     * @param numberOfLoops
     */
    public void play(int numberOfLoops) {
        clip.setMicrosecondPosition(0);
        clip.loop(numberOfLoops);
    }

    /**
     * stops the clip
     */
    public void stop() {

        clip.stop();

    }

    /**
     * get's the volume converted from dB to percentage
     *
     * @return
     */
    public float getVolume() {
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        return (float) Math.pow(10f, gainControl.getValue() / 20f);
    }

    /**
     * Sets the volume by converting the float represented as percentage to dB
     *
     * @param volume
     */
    public void setVolume(double volume) {
        double gain;
        float dB;
        FloatControl gainControl;

        if (volume < 0f || volume > 1f)
            throw new IllegalArgumentException("Volume not valid: " + volume);

        gain = volume; // number between 0 and 1 (loudest)
        dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
        gainControl = (FloatControl) clip
                .getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(dB);
    }

}
