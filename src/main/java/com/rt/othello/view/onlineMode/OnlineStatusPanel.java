package com.rt.othello.view.onlineMode;

import com.rt.othello.controller.onlineMode.OnlineGameController;
import com.rt.othello.model.ChessPiece;

import javax.swing.*;
import java.awt.*;

public class OnlineStatusPanel extends JPanel {

    private OnlineGameController onlineGameController;

    private JLabel playerLabel;
    private JLabel scoreLabel;
    private JLabel tip;

    private ChessPiece chessPiece;

    public OnlineStatusPanel(int width, int height, OnlineGameController onlineGameController) {
        this.onlineGameController = onlineGameController;
        this.setSize(width, height);
        this.setLayout(null);
        this.setVisible(true);
        this.setOpaque(false);

        this.playerLabel = new JLabel();
        this.playerLabel.setBounds(0, (int)(height * 0.05), (int) (width * 0.5), (int)(height * 0.4));
        this.playerLabel.setFont(new Font("Calibri", Font.BOLD, Math.min((int)(this.playerLabel.getHeight() * 1.25), (int)(this.playerLabel.getWidth() / 7))));
        this.playerLabel.setForeground(Color.BLACK);
        this.playerLabel.setHorizontalAlignment(JLabel.CENTER);
        this.setPlayerText(ChessPiece.BLACK.name());
        add(playerLabel);

        this.scoreLabel = new JLabel();
        this.scoreLabel.setBounds((int) (width * 0.5), (int)(height * 0.05), (int) (width * 0.5), (int)(height * 0.4));
        this.scoreLabel.setFont(new Font("Calibri", Font.ITALIC, Math.min((int)(this.scoreLabel.getHeight() * 1.25), (int)(this.scoreLabel.getWidth() / 9))));
        this.scoreLabel.setForeground(Color.BLACK);
        this.scoreLabel.setHorizontalAlignment(JLabel.CENTER);
        this.setScoreText(2,2);
        add(scoreLabel);

        this.tip = new JLabel();
        this.tip.setBounds(0, (int)(height * 0.55), width, (int)(height * 0.4));
        this.tip.setFont(new Font("MS Song", Font.BOLD, Math.min((int)(this.tip.getHeight()), (int)(this.tip.getWidth() / 14))));
        this.tip.setForeground(Color.RED);
        this.tip.setHorizontalAlignment(JLabel.CENTER);
        this.tip.setText(null);
        add(tip);
    }

    public void updateSize(){
        this.playerLabel.setBounds(0, (int)(this.getHeight() * 0.05), (int) (this.getWidth() * 0.5), (int)(this.getHeight() * 0.4));
        this.playerLabel.setFont(new Font("Calibri", Font.BOLD, Math.min((int)(this.playerLabel.getHeight() * 1.25), (int)(this.playerLabel.getWidth() / 7))));
        this.scoreLabel.setBounds((int) (this.getWidth() * 0.5), (int)(this.getHeight() * 0.05), (int) (this.getWidth() * 0.5), (int)(this.getHeight() * 0.4));
        this.scoreLabel.setFont(new Font("Calibri", Font.ITALIC, Math.min((int)(this.scoreLabel.getHeight() * 1.25), (int)(this.scoreLabel.getWidth() / 9))));
        this.tip.setBounds(0, (int)(this.getHeight() * 0.55), this.getWidth(), (int)(this.getHeight() * 0.4));
        this.tip.setFont(new Font("MS Song", Font.BOLD, Math.min((int)(this.tip.getHeight()), (int)(this.tip.getWidth() / 14))));
    }

    public void setScoreText(int black, int white) {
        this.scoreLabel.setText(String.format("BLACK: %d\tWHITE: %d", black, white));
    }

    public void setPlayerText(String playerText) {
        this.playerLabel.setText(playerText);
    }

    public void setPlayerColor(boolean color) {
        if (color == false) {
            chessPiece = ChessPiece.BLACK;
        }
        else {
            chessPiece = ChessPiece.WHITE;
        }
    }

    public void setEnd(){
        this.tip.setText("Game End!");
    }

    public void setNull(){
        tip.setText(null);
    }

    public void setInValid(){
        tip.setText("无效走棋！");
    }

    public void setWait(){
        tip.setText("等待对手加入......");
    }

    public void setConnecting(){
        tip.setText("Connecting......");
    }

    public void playerNotChange(){
        if (onlineGameController.isEnable()) {
            if(chessPiece == ChessPiece.BLACK){
                tip.setText("白方无处可走，黑方继续！");
            }
            else if(chessPiece == ChessPiece.WHITE){
                tip.setText("黑方无处可走，白方继续！");
            }
        }
        else {
            if(chessPiece == ChessPiece.BLACK){
                tip.setText("黑方无处可走，白方继续！");
            }
            else if(chessPiece == ChessPiece.WHITE){
                tip.setText("白方无处可走，黑方继续！");
            }
        }
    }
}
