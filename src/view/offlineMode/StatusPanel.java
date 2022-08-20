package view.offlineMode;

import model.ChessPiece;

import javax.swing.*;
import java.awt.*;

public class StatusPanel extends JPanel {
    private JLabel playerLabel;
    private JLabel scoreLabel;
    private static JLabel tip;

    public StatusPanel(int width, int height) {
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
        this.playerLabel.setText(playerText + "'s turn");
    }

    public void setEnd(){
        this.playerLabel.setText("End");
    }

    public static void setValid(){
        tip.setText(null);
    }

    public static void setInValid(){
        tip.setText("无效走棋！");
    }

    public static void playerNotChange(ChessPiece chessPiece){
        if(chessPiece == ChessPiece.BLACK){
            tip.setText("白方无处可走，黑方继续！");
        }
        else if(chessPiece == ChessPiece.WHITE){
            tip.setText("黑方无处可走，白方继续！");
        }
    }
}
