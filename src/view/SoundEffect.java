package view;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import java.io.IOException;
import java.io.InputStream;

public class SoundEffect {
    public SoundEffect(InputStream inputStream) {
        AudioStream sound = null;
        try {
            sound = new AudioStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        AudioPlayer.player.start(sound);
    }
}
