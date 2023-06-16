package view;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import java.io.IOException;


public class ContinuousMusic extends Thread{

    private volatile boolean play = true;

    public ContinuousMusic(){
    }

    @Override
    public void run() {
        while(true) {
            if(play == true){
                AudioStream music = null;
                try {
                    music = new AudioStream(this.getClass().getResourceAsStream("/res/bgm.wav"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                AudioPlayer.player.start(music);
                while(true) {
                    try {
                        if (!(music.available() > 0)) break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(play == false){
                        AudioPlayer.player.stop(music);
                        break;
                    }
                }
            }
        }
    }

    public void Play(){
        this.play = true;
    }

    public void Stop(){
        this.play = false;
    }

}
