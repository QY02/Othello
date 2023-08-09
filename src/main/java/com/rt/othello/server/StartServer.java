package com.rt.othello.server;

import com.rt.othello.server.Game.GameMain;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Scanner;

public class StartServer implements Runnable {

    private volatile boolean isStop = false;

    private ArrayList<ConnectedPlayer> players = new ArrayList<>();

    @Override
    public void run() {
        GameMain game = new GameMain();
        try (ServerSocket s = new ServerSocket(25566)) {
//            players = new ArrayList<>();
            s.setSoTimeout(1000);
            int i = 0;
            while (!isStop) {
                System.out.println("Server started");
                try {
                    Socket incoming = s.accept();
                    InputStream inStream = incoming.getInputStream();
                    OutputStream outStream = incoming.getOutputStream();
                    Scanner in = new Scanner(inStream, "UTF-8");
                    PrintWriter out = new PrintWriter(new OutputStreamWriter(outStream, "UTF-8"), true);
                    int connectionType = in.nextInt();
                    if (connectionType == -1) {
                        ConnectedPlayer connectedPlayer = new ConnectedPlayer(incoming, inStream, outStream, in, out, i, players, game);
                        players.add(connectedPlayer);
                        Thread connectedPlayerThread = new Thread(connectedPlayer);
                        connectedPlayerThread.start();
                        i++;
                    } else {
                        players.get(connectionType).resetConnection(incoming, inStream, outStream, in, out);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            for (int j = 0; j < players.size(); j++) {
                System.out.println("Stop" + j);
                players.get(j).stop();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            for (int j = 0; j < players.size(); j++) {
                players.get(j).stop();
            }
        }
    }

    public void stop() {
        this.isStop = true;
    }
}
