package components.onlineMode;

import components.BasicComponent;
import controller.onlineMode.OnlineGameController;
import model.ChessPiece;
import net.Send;
import view.SoundEffect;
import view.offlineMode.OfflineModeFrame;
import view.offlineMode.StatusPanel;

import javax.swing.*;
import java.awt.*;

public class OnlineChessGridComponent extends BasicComponent {
    public static int chessSize;
    public static int gridSize;
    public static Color gridColor = new Color(255, 150, 50);

    private ChessPiece chessPiece;

    private OnlineGameController onlineGameController;
    private Send send;

    private int row;
    private int col;

    public OnlineChessGridComponent(int row, int col, Send send, OnlineGameController onlineGameController) {
        this.onlineGameController = onlineGameController;
        this.send = send;
        this.setSize(gridSize, gridSize);

        this.row = row;
        this.col = col;
    }

    public void updateSize(){
        this.setSize(gridSize, gridSize);
    }

    @Override
    public void onMouseClicked() {
        if (onlineGameController.isEnable()) {
            send.send(0);
            send.send(row);
            send.send(col);
        }
        //todo: complete mouse click method
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
