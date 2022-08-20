package net;

import view.MainFrame;
import view.onlineMode.OnlineModeFrame;

import javax.swing.*;

public class TestConnection extends Thread{

    private Send send;
    private Reconnect reconnect;
    private OnlineModeFrame onlineModeFrame;

    private volatile boolean isConnected = false;

    public TestConnection(Send send, Reconnect reconnect, OnlineModeFrame onlineModeFrame) {
        this.send = send;
        this.reconnect = reconnect;
        this.onlineModeFrame = onlineModeFrame;
    }

    @Override
    public void run() {
        try {
            sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (true) {
            send.send(-1);
            System.out.println("send-1");
            try {
                sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (isConnected == false) {
                System.out.println("Disconnected!");
                Object[] options = {"reconnect"};
                JOptionPane.showOptionDialog(onlineModeFrame,"Disconnect from the server","Connection lost",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE,null,options,options[0]);
                onlineModeFrame.getOnlineStatusPanel().setConnecting();
                reconnect.reconnect();
                onlineModeFrame.getOnlineStatusPanel().setNull();
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else {
                isConnected = false;
                System.out.println("connected");
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void isConnected() {
        this.isConnected = true;
    }
}
