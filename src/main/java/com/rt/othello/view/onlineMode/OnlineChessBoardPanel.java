package com.rt.othello.view.onlineMode;

import com.rt.othello.components.onlineMode.OnlineChessGridComponent;
import com.rt.othello.controller.onlineMode.OnlineGameController;
import com.rt.othello.model.ChessPiece;
import com.rt.othello.net.Send;

import javax.swing.*;
import java.awt.*;

public class OnlineChessBoardPanel extends JPanel {
    private final int CHESS_COUNT = 8;
    private OnlineChessGridComponent[][] onlineChessGrids;
    private int length;

    private OnlineGameController onlineGameController;
    private Send send;

    public OnlineChessBoardPanel(int width, int height, Send send, OnlineGameController onlineGameController) {
        this.onlineGameController = onlineGameController;
        this.send = send;
        this.setVisible(true);
        this.setFocusable(true);
        this.setLayout(null);
        this.setBackground(Color.BLACK);
        this.length = Math.min(width, height);
        this.setSize(length, length);
        OnlineChessGridComponent.gridSize = length / CHESS_COUNT;
        OnlineChessGridComponent.chessSize = (int) (OnlineChessGridComponent.gridSize * 0.9);
        System.out.printf("width = %d height = %d gridSize = %d chessSize = %d\n",
                width, height, OnlineChessGridComponent.gridSize, OnlineChessGridComponent.chessSize);

        initialChessGrids();//return empty chessboard
        initialGame();//add initial four chess

        repaint();
    }

    public void updateSize(){
        this.length = this.getWidth();
        OnlineChessGridComponent.gridSize = this.length / CHESS_COUNT;
        OnlineChessGridComponent.chessSize = (int) (OnlineChessGridComponent.gridSize * 0.9);
        for (int i = 0; i < CHESS_COUNT; i++) {
            for (int j = 0; j < CHESS_COUNT; j++) {
                onlineChessGrids[i][j].updateSize();
                onlineChessGrids[i][j].setLocation((this.length - 8 * OnlineChessGridComponent.gridSize)/2 + j * (OnlineChessGridComponent.gridSize), (this.length - 8 * OnlineChessGridComponent.gridSize)/2 + i * (OnlineChessGridComponent.gridSize));
            }
        }
    }

    /**
     * set an empty chessboard
     */
    public void initialChessGrids() {
        onlineChessGrids = new OnlineChessGridComponent[CHESS_COUNT][CHESS_COUNT];

        //draw all chess grids
        for (int i = 0; i < CHESS_COUNT; i++) {
            for (int j = 0; j < CHESS_COUNT; j++) {
                OnlineChessGridComponent onlineGridComponent = new OnlineChessGridComponent(i, j, this.send, this.onlineGameController);
                onlineGridComponent.setLocation((length - 8 * OnlineChessGridComponent.gridSize)/2 + j * (OnlineChessGridComponent.gridSize), (length - 8 * OnlineChessGridComponent.gridSize)/2 + i * (OnlineChessGridComponent.gridSize));
                onlineChessGrids[i][j] = onlineGridComponent;
                this.add(onlineChessGrids[i][j]);
            }
        }
    }

    /**
     * initial origin four chess
     */
    public void initialGame() {
        onlineChessGrids[3][3].setChessPiece(ChessPiece.BLACK);
        onlineChessGrids[3][4].setChessPiece(ChessPiece.WHITE);
        onlineChessGrids[4][3].setChessPiece(ChessPiece.WHITE);
        onlineChessGrids[4][4].setChessPiece(ChessPiece.BLACK);
    }

    public void updateChessBoard(int[][] chessBoardData) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((chessBoardData[i][j] == 0) || ((chessBoardData[i][j] == 2) && (!this.onlineGameController.isEnable()))){
                    onlineChessGrids[i][j].setChessPiece(null);
                }
                else if (chessBoardData[i][j] == -1){
                    onlineChessGrids[i][j].setChessPiece(ChessPiece.BLACK);
                }
                else if (chessBoardData[i][j] == 1){
                    onlineChessGrids[i][j].setChessPiece(ChessPiece.WHITE);
                }
                else if ((chessBoardData[i][j] == 2) && (this.onlineGameController.isEnable())){
                    onlineChessGrids[i][j].setChessPiece(ChessPiece.TIP);
                }
            }
        }
        repaint();
    }

    public void removeTips() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (onlineChessGrids[i][j].getChessPiece() == ChessPiece.TIP) {
                    onlineChessGrids[i][j].setChessPiece(null);
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
    }

    public void setChess(int row,int col,ChessPiece chessPiece) {
        this.onlineChessGrids[row][col].setChessPiece(chessPiece);
        repaint();
    }

    public void randomColor(){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                this.onlineChessGrids[i][j].randomColor();
            }
        }
        repaint();
    }

    public void defaultColor(){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                this.onlineChessGrids[i][j].defaultColor();
            }
        }
        repaint();
    }
}
