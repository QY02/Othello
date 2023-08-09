package com.rt.othello.net;

import com.rt.othello.controller.onlineMode.OnlineGameController;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class Send{

    private volatile Socket socket;
    private volatile OutputStream outStream;
    private volatile PrintWriter out;
    private OnlineGameController onlineGameController;

    public Send(Socket socket, OnlineGameController onlineGameController) {
        this.socket = socket;
        this.onlineGameController = onlineGameController;
        try {
            outStream = socket.getOutputStream();
            out = new PrintWriter(new OutputStreamWriter(outStream, "UTF-8"), true);
            out.println(-1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(int data){
        out.println(data);
    }

    public void resetSocket(Socket socket, boolean reconnect) {
        this.socket = socket;
        try {
            outStream = socket.getOutputStream();
            out = new PrintWriter(new OutputStreamWriter(outStream, "UTF-8"), true);
            if (reconnect) {
                out.println(onlineGameController.getPlayerType());
                out.println(1);
            }
            else {
                out.println(-1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
