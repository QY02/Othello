package Sound;

import javax.sound.sampled.*;
import java.io.IOException;

public class ThreadedSound extends Thread{

    private String location;

    private AudioInputStream audioInputStream;
    private AudioFormat audioFormat;
    private SourceDataLine sourceDataLine;

    private boolean loop;
    private volatile boolean pause;
    private long sleep = 0;

    public ThreadedSound(String location, boolean loop){
        this.location = location;
        this.loop = loop;
        start();
    }

    public ThreadedSound(String location, boolean loop, long sleep){
        this.location = location;
        this.loop = loop;
        this.sleep = sleep;
        start();
    }

    @Override
    public void run() {
        try {
            if (sleep > 0){
                sleep(sleep);
            }
            do {
                audioInputStream = AudioSystem.getAudioInputStream(this.getClass().getResource(location));
                audioFormat = audioInputStream.getFormat();
                sourceDataLine = AudioSystem.getSourceDataLine(audioFormat);
                sourceDataLine.open();
                sourceDataLine.start();
                int totalByteRead = 0;
                byte[] read = new byte[1024];
                while (totalByteRead != -1) {
                    synchronized (this) {
                        if (pause){
                            try {
                                wait();//让线程进入等待状态
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    totalByteRead = audioInputStream.read(read, 0, read.length);
                    if (totalByteRead > 0) {
                        sourceDataLine.write(read, 0, read.length);
                    }
                }
                sourceDataLine.drain();
                sourceDataLine.close();
                audioInputStream.close();
            } while (loop);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void play() {
        notify();
        pause = false;
    }

    public synchronized void pause() {
        pause = true;
    }
}
