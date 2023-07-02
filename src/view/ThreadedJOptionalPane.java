package view;

import javax.swing.*;

public class ThreadedJOptionalPane extends Thread{
    private volatile JFrame parentComponent;
    private volatile String message;
    private volatile String title;
    private volatile int messageType;

    public ThreadedJOptionalPane() {
    }

    @Override
    public void run() {
        while (true) {
            synchronized (this) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            JOptionPane.showMessageDialog(parentComponent, message, title, messageType);
        }
    }

    public void show(JFrame parentComponent, String message, String title, int messageType) {
        this.parentComponent = parentComponent;
        this.message = message;
        this.title = title;
        this.messageType = messageType;
        synchronized (this) {
            notifyAll();
        }
    }
}
