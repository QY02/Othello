package server;

import server.Game.GameMain;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.Thread.sleep;

public class ConnectedPlayer implements Runnable {
    private volatile Socket incoming;
    private volatile InputStream inStream;
    private volatile OutputStream outStream;
    private volatile Scanner in;
    private volatile PrintWriter out;
    private int id;
    private ArrayList<ConnectedPlayer> players;
    private Send sd;

    private String name;

    private boolean enable = true;
    private int color;

    private GameMain game;

    private volatile boolean isStop = false;

    public ConnectedPlayer(Socket incomingSocket, InputStream inStream, OutputStream outStream, Scanner in, PrintWriter out, int id, ArrayList<ConnectedPlayer> players, GameMain game) {
        incoming = incomingSocket;
//        try {
//            incoming.setSoTimeout(10000);
//        } catch (SocketException e) {
//            e.printStackTrace();
//        }
        this.inStream = inStream;
        this.outStream = outStream;
        this.in = in;
        this.out = out;
        this.players = players;
        this.id = id;
        if (this.id == 0){
            color = -1;
        }
        else if (this.id == 1){
            color = 1;
        }
        this.game = game;
        sd = new Send(players);
    }

    @Override
    public void run() {
        System.out.println(id + "加入了游戏");
        if (id == 0) {
            out.println(0);
            out.println(-1);
        }
        else if (id == 1) {
            out.println(0);
            out.println(1);
        }
        out.println("1");
        out.println(game.getChessBoardData());
        out.println(game.getBlackScore());
        out.println(game.getWhiteScore());
        enable = false;
        out.println(0);
        out.println(0);
        if (id == 0) {
            out.println(0);
            out.println(2);
        }
        else if (id == 1) {
            players.get(0).send(0);
            players.get(0).send(3);
            players.get(0).toResume();
            players.get(0).send(1);
            players.get(0).send(game.getChessBoardData());
            players.get(0).send(game.getBlackScore());
            players.get(0).send(game.getWhiteScore());
        }
        while(!isStop){
            if (color != game.getCurrentPlayer()){
                enable = false;
            }
            while (!isStop){
                if (in.hasNextInt()) {
                    int command = in.nextInt();
                    if (command == -1) {
                        out.println(-1);
                        continue;
                    }
                    else if (command == 1) {
                        if (enable == true) {
                            out.println(2);
                            out.println(1);
                        }
                        else {
                            out.println(2);
                            out.println(0);
                        }
                        out.println("1");
                        out.println(game.getChessBoardData());
                        out.println(game.getBlackScore());
                        out.println(game.getWhiteScore());
                    }
                    if (enable == true) {
                        if (command == 0) {
                            int row = in.nextInt();
                            int col = in.nextInt();
                            if (game.getCurrentChessBoardWithTips()[row][col] != 2) {
                                out.println(3);
                                out.println(0);
                                continue;
                            }
                            int result = game.place(row, col);
                            if (result == 0) {
                                out.println(2);
                                out.println(0);
                                out.println("1");
                                out.println(game.getChessBoardData());
                                out.println(game.getBlackScore());
                                out.println(game.getWhiteScore());
                                out.println(3);
                                out.println(1);
                                for (int i = 0; i < 2; i++) {
                                    if (i != id) {
                                        players.get(i).send(2);
                                        players.get(i).send(1);
                                        players.get(i).send("1");
                                        players.get(i).send(game.getChessBoardData());
                                        players.get(i).send(game.getBlackScore());
                                        players.get(i).send(game.getWhiteScore());
                                        players.get(i).send(3);
                                        players.get(i).send(1);
                                        players.get(i).toResume();
                                    }
                                }
                                enable = false;
                            }
                            else if (result == 1) {
                                for (int i = 0; i < 2; i++) {
                                    players.get(i).send(1);
                                    players.get(i).send(game.getChessBoardData());
                                    players.get(i).send(game.getBlackScore());
                                    players.get(i).send(game.getWhiteScore());
                                    players.get(i).send(3);
                                    players.get(i).send(2);
                                }
                            }
                            else if (result == 2) {
                                for (int i = 0; i < 2; i++) {
                                    players.get(i).send(2);
                                    players.get(i).send(0);
                                    players.get(i).send(1);
                                    players.get(i).send(game.getChessBoardData());
                                    players.get(i).send(game.getBlackScore());
                                    players.get(i).send(game.getWhiteScore());
                                    players.get(i).send(3);
                                    players.get(i).send(1);
                                    players.get(i).send(3);
                                    players.get(i).send(3);
                                    players.get(i).send(4);
                                    players.get(i).send(game.chooseWinner());
                                }
                            }
                            break;
                        }
                    }
                }
                else {
                    System.out.println("scanner reset");
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        try {
            incoming.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(id + "stop");
    }

    public void stop() {
        this.isStop = true;
    }

    public void send(String data) {
        out.println(data);
    }
    public void send(int command) {
        out.println(command);
    }

    public synchronized void toResume(){
        //notify();//当前等待的线程继续执行
        enable = true;
    }

    public void resetConnection(Socket incomingSocket, InputStream inStream, OutputStream outStream, Scanner in, PrintWriter out) {
        try {
//            System.out.println(isInterrupted());
//            System.out.println(interrupted());
//            System.out.println(isInterrupted());
            //this.incoming.shutdownInput();
            this.incoming.close();
            this.inStream.close();
            this.outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.incoming = incomingSocket;
        this.inStream = inStream;
        this.outStream = outStream;
        this.in = in;
        this.out = out;
        System.out.println("reconnect!");
    }
}
