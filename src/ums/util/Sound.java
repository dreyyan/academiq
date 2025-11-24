package ums.util;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Sound {
    private static final String SFX_DIR = "sfx/";

    // [METHOD] Play Sound Effects
    public static void playSFX(String fileName) {
        new Thread(() -> play(fileName, 0)).start();
    }

    // [METHOD] Play Sound Effects in Loop
    public static void playSFXLoop(String fileName, int loopCount) {
        new Thread(() -> play(fileName, loopCount)).start();
    }

    // [METHOD] Core Sound Playing Logic
    private static void play(String fileName, int loopCount) {
        try {
            File soundFile = new File(SFX_DIR + fileName);
            if (!soundFile.exists()) {
                System.err.println("Sound file not found: " + soundFile.getPath());
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);

            if (loopCount > 0) {
                clip.loop(loopCount);
            } else {
                clip.start();
            }

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error playing sound: " + e.getMessage());
        }
    }
}