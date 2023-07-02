package net;

import controller.onlineMode.OnlineGameController;
import view.onlineMode.OnlineChessBoardPanel;
import view.onlineMode.OnlineStatusPanel;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Scanner;

import static java.lang.Thread.sleep;

public class Receive implements Runnable{

    private volatile Socket socket;
    private volatile InputStream inStream;
    private volatile Scanner in;

    private volatile TestConnection testConnection;
    private volatile boolean isStop = false;

    private OnlineChessBoardPanel onlineChessBoardPanel;
    private OnlineStatusPanel onlineStatusPanel;
    private OnlineGameController onlineGameController;

    public Receive(Socket socket, OnlineChessBoardPanel onlineChessBoardPanel, OnlineStatusPanel onlineStatusPanel, OnlineGameController onlineGameController, TestConnection testConnection){
        this.socket = socket;
        this.onlineChessBoardPanel = onlineChessBoardPanel;
        this.onlineStatusPanel = onlineStatusPanel;
        this.onlineGameController = onlineGameController;
        this.testConnection = testConnection;
        try {
            inStream = this.socket.getInputStream();
            in = new Scanner(inStream, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            if (in.hasNextInt()) {
                int command = in.nextInt();
                if (command == -1) {
                    testConnection.isConnected();
                }
                else if (command == 1){
                    int[][] chessBoardData = new int[8][8];
                    for (int i = 0; i < 8; i++) {
                        for (int j = 0; j < 8; j++) {
                            chessBoardData[i][j] = in.nextInt();
                            System.out.println(chessBoardData[i][j]);
                        }
                    }
                    onlineChessBoardPanel.updateChessBoard(chessBoardData);
                    onlineStatusPanel.setScoreText(in.nextInt(), in.nextInt());
                }
                else if (command == 0) {
                    int content = in.nextInt();
                    if (content == -1) {
                        onlineStatusPanel.setPlayerText("Black");
                        onlineStatusPanel.setPlayerColor(false);
                        onlineGameController.setPlayerType(0);
                    }
                    else if (content == 1) {
                        onlineStatusPanel.setPlayerText("White");
                        onlineStatusPanel.setPlayerColor(true);
                        onlineGameController.setPlayerType(1);
                    }
                    else if (content == 0) {
                        onlineGameController.disable();
                        onlineChessBoardPanel.removeTips();
                        onlineStatusPanel.setNull();
                    }
                    else if (content == 2) {
//                        onlineGameController.disable();
//                        onlineChessBoardPanel.removeTips();
                        onlineStatusPanel.setWait();
                    }
                    else if (content == 3) {
                        onlineGameController.enable();
                        onlineStatusPanel.setNull();
                    }
                }
                else if (command == 2) {
                    int enable = in.nextInt();
                    if (enable == 0) {
                        onlineGameController.disable();
                    }
                    else if (enable == 1) {
                        onlineGameController.enable();
                    }
                }
                else if (command == 3) {
                    int content = in.nextInt();
                    if (content == 0) {
                        onlineStatusPanel.setInValid();
                    }
                    else if (content == 1) {
                        onlineStatusPanel.setNull();
                    }
                    else if (content == 2) {
                        onlineStatusPanel.playerNotChange();
                    }
                    else if (content == 3) {
                        onlineStatusPanel.setEnd();
                    }
                }
                else if (command == 4) {
                    int winner = in.nextInt();
                    onlineGameController.showWinner(winner);
                }
            }
            else {
                System.out.println("scanner reset!");
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            while (isStop) {
                System.out.println("Receive stop");
                synchronized (this) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("Receive restart");
            }
        }
    }

    public void stop() {
        this.isStop = true;
    }

    public void restart() {
        this.isStop = false;
    }

    public void resetSocket(Socket socket) {
        try {
            this.socket.close();
            this.inStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.socket = socket;
        try {
            inStream = this.socket.getInputStream();
            in = new Scanner(inStream, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
