package view;

import javax.swing.*;

public class ThreadedJOptionalPane extends Thread{
    private JFrame parentComponent;
    private String message;
    private String title;
    private int messageType;

    public ThreadedJOptionalPane() {
    }

    @Override
    public void run() {
        JOptionPane.showMessageDialog(parentComponent, message, title, messageType);
    }

    public void show(JFrame parentComponent, String message, String title, int messageType) {
        this.parentComponent = parentComponent;
        this.message = message;
        this.title = title;
        this.messageType = messageType;
        start();
    }
}
