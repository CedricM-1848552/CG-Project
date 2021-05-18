package window;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.BufferedInputStream;
import java.io.FileInputStream;

public class Sound {
    private final String path;

    public Sound(String path) {
        this.path = path;
    }

    public synchronized void play() {
        new Thread(() -> {
            try {
                Clip clip = AudioSystem.getClip();
                FileInputStream file = new FileInputStream(path);
                AudioInputStream inputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(file));
                clip.open(inputStream);
                clip.start();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }).start();
    }
}
