package components.offlineMode;

import Sound.ThreadedSound;
import components.BasicComponent;
import model.*;
import view.offlineMode.ChessBoardPanel;
import view.offlineMode.OfflineModeFrame;
//import view.SoundEffect;
import view.offlineMode.StatusPanel;

import javax.swing.*;
import java.awt.*;

public class ChessGridComponent extends BasicComponent {
    public static int chessSize;
    public static int gridSize;
    public static Color gridColor = new Color(255, 150, 50);

    private ChessPiece chessPiece;
    private ChessBoardPanel chessBoardPanel;
    private int row;
    private int col;

    public ChessGridComponent(ChessBoardPanel chessBoardPanel, int row, int col) {
        this.setSize(gridSize, gridSize);

        this.chessBoardPanel = chessBoardPanel;
        this.row = row;
        this.col = col;
    }

    public void updateSize(){
        this.setSize(gridSize, gridSize);
    }

    @Override
    public void onMouseClicked() {
        System.out.printf("%s clicked (%d, %d)\n", OfflineModeFrame.controller.getCurrentPlayer(), row, col);
        //todo: complete mouse click method
        if (((OfflineModeFrame.controller.getCurrentPlayer() == ChessPiece.BLACK) && (OfflineModeFrame.controller.getBlackPlayerType() == 0)) || ((OfflineModeFrame.controller.getCurrentPlayer() == ChessPiece.WHITE) && (OfflineModeFrame.controller.getWhitePlayerType() == 0))) {
            if ((OfflineModeFrame.controller.canClick(row, col))&&(OfflineModeFrame.controller.getCurrentPlayer()!=null)) {
                System.out.println("有效走棋！");
                move();
                synchronized (this.chessBoardPanel.getOfflineModeFrame().getAiMain()) {
                    this.chessBoardPanel.getOfflineModeFrame().getAiMain().notifyAll();
                }
            }
            else {
                System.out.println("无效走棋！");
                ThreadedSound invalid = new ThreadedSound("/res/invalid.wav", false);
                StatusPanel.setInValid();
            }
        }
        else {
            System.out.println("Player not turn!");
        }
    }

    public synchronized void move() {
        ThreadedSound luozi = new ThreadedSound("/res/luozi.wav", false);
        StatusPanel.setValid();
        OfflineModeFrame.controller.removeTips();
        OfflineModeFrame.save.addStep(OfflineModeFrame.controller.getId(),row,col, OfflineModeFrame.controller.getCurrentPlayer());
        this.chessPiece = OfflineModeFrame.controller.getCurrentPlayer();
        OfflineModeFrame.controller.flip(row,col, OfflineModeFrame.controller.getCurrentPlayer());
        OfflineModeFrame.save.addChessBoard(OfflineModeFrame.controller.getId());
        OfflineModeFrame.controller.swapPlayer();
        boolean canShow = OfflineModeFrame.controller.showTip(OfflineModeFrame.controller.getCurrentPlayer());
        if(canShow == false){
            OfflineModeFrame.controller.swapPlayer();
            boolean canAlsoShow = OfflineModeFrame.controller.showTip(OfflineModeFrame.controller.getCurrentPlayer());
            if(canAlsoShow == false){
                if (OfflineModeFrame.controller.getBlackScore() == OfflineModeFrame.controller.getWhiteScore()) {
                    OfflineModeFrame.controller.end();
                    System.out.println("平局");
                    Object[] options = {"重新开始", "返回"};
                    int confirm = JOptionPane.showOptionDialog(null, "平局", "Finish", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                    if (confirm == 0) {
                        OfflineModeFrame.controller.restart();
                        OfflineModeFrame.save.restart();
                        OfflineModeFrame.controller.restartChessBoard();
                    }
                    else if(confirm == 1){
                        OfflineModeFrame.controller.idIncrease();
                    }
                }
                if (OfflineModeFrame.controller.getBlackScore() > OfflineModeFrame.controller.getWhiteScore()) {
                    OfflineModeFrame.controller.end();
                    System.out.println("黑方胜利");
                    ThreadedSound victory = new ThreadedSound("/res/victory.wav", false);
                    Object[] options = {"重新开始", "返回"};
                    int confirm = JOptionPane.showOptionDialog(null, "恭喜黑方胜利", "Celebration", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                    if (confirm == 0) {
                        OfflineModeFrame.controller.restart();
                        OfflineModeFrame.save.restart();
                        OfflineModeFrame.controller.restartChessBoard();
                    }
                    else if(confirm == 1){
                        OfflineModeFrame.controller.idIncrease();
                    }
                }
                if (OfflineModeFrame.controller.getBlackScore() < OfflineModeFrame.controller.getWhiteScore()) {
                    OfflineModeFrame.controller.end();
                    System.out.println("白方胜利");
                    ThreadedSound victory = new ThreadedSound("/res/victory.wav", false);
                    Object[] options = {"重新开始", "返回"};
                    int confirm = JOptionPane.showOptionDialog(null, "恭喜白方胜利", "Celebration", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                    if (confirm == 0) {
                        OfflineModeFrame.controller.restart();
                        OfflineModeFrame.save.restart();
                        OfflineModeFrame.controller.restartChessBoard();
                    }
                    else if(confirm == 1){
                        OfflineModeFrame.controller.idIncrease();
                    }
                }
            }
            else if(canAlsoShow == true){
                StatusPanel.playerNotChange(OfflineModeFrame.controller.getCurrentPlayer());
                OfflineModeFrame.controller.idIncrease();
            }
        }
        else if(canShow == true){
            OfflineModeFrame.controller.idIncrease();
        }
        repaint();
    }


    public ChessPiece getChessPiece() {
        return chessPiece;
    }

    public void setChessPiece(ChessPiece chessPiece) {
        this.chessPiece = chessPiece;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void randomColor(){
        int r = (int)(Math.random()*255);
        int g = (int)(Math.random()*255);
        int b = (int)(Math.random()*255);
        this.gridColor = new Color(r, g, b);
    }

    public void defaultColor(){
        this.gridColor = new Color(255, 150, 50);
    }

    public void drawPiece(Graphics g) {
        g.setColor(gridColor);
        g.fillRect(1, 1, this.getWidth() - 2, this.getHeight() - 2);
        if (this.chessPiece != null) {
            ImageIcon black = new ImageIcon(this.getClass().getResource("/res/black.png"));
            ImageIcon white = new ImageIcon(this.getClass().getResource("/res/white.png"));
            ImageIcon tip = new ImageIcon(this.getClass().getResource("/res/tip.png"));
            if(chessPiece.getColor() == Color.BLACK){
                g.drawImage(black.getImage(),(gridSize - chessSize) / 2, (gridSize - chessSize) / 2,chessSize, chessSize,this);
            }
            else if (chessPiece.getColor() == Color.WHITE){
                g.drawImage(white.getImage(),(gridSize - chessSize) / 2, (gridSize - chessSize) / 2,chessSize, chessSize,this);
            }
            else if(chessPiece.getColor() == Color.RED){
                g.drawImage(tip.getImage(),(gridSize - chessSize) / 2, (gridSize - chessSize) / 2,chessSize, chessSize,this);
            }
        }
    }


    @Override
    public void paintComponent(Graphics g) {
        super.printComponents(g);
        drawPiece(g);
    }


}
