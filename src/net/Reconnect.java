package net;

import view.onlineMode.OnlineModeFrame;

import javax.swing.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import static java.lang.Thread.sleep;

public class Reconnect {
    private OnlineModeFrame onlineModeFrame;
    private Socket socket;

    public Reconnect(OnlineModeFrame onlineModeFrame) {
        this.onlineModeFrame = onlineModeFrame;
    }

    public void reconnect() {
        while (true) {
            try {
                sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            socket = new Socket();
            try {
                socket.connect(new InetSocketAddress(onlineModeFrame.getAddress(), onlineModeFrame.getPort()),1000);
                onlineModeFrame.resetSocket(socket);
                System.out.println("reconnect!");
                break;
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    socket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
