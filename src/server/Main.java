package server;

import server.Game.GameMain;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        GameMain game = new GameMain();
        try (ServerSocket s = new ServerSocket(25566)){
            ArrayList<ConnectedPlayer> players = new ArrayList<>();
            int i = 0;
            while (true){
                Socket incoming = s.accept();
                try {
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
                    }
                    else {
                        players.get(connectionType).resetConnection(incoming, inStream, outStream, in, out);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}