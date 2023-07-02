package controller.onlineMode;

import SaveAndLoad.Load;
import SaveAndLoad.Save;
import model.ChessPiece;
import view.ThreadedJOptionalPane;
import view.offlineMode.ChessBoardPanel;
import view.offlineMode.OfflineModeFrame;
import view.offlineMode.StatusPanel;
import view.onlineMode.OnlineModeFrame;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public class OnlineGameController {

    private OnlineModeFrame onlineModeFrame;
    private ThreadedJOptionalPane threadedJOptionalPane;
    private boolean enable = false;
    private int playerType;

    public OnlineGameController(OnlineModeFrame onlineModeFrame) {
        this.onlineModeFrame = onlineModeFrame;
        threadedJOptionalPane = new ThreadedJOptionalPane();
        threadedJOptionalPane.start();
    }

    public boolean isEnable() {
        return enable;
    }

    public void enable() {
        enable = true;
    }

    public void disable() {
        enable = false;
    }

    public void setPlayerType(int playerType) {
        this.playerType = playerType;
    }

    public int getPlayerType() {
        return playerType;
    }

    public void showWinner(int winner) {
        if (winner == -1) {
            threadedJOptionalPane.show(onlineModeFrame, "Black win!", "Game end!", JOptionPane.INFORMATION_MESSAGE);
            //JOptionPane.showMessageDialog(onlineModeFrame, "Black win!", "Game end!", JOptionPane.INFORMATION_MESSAGE);
        }
        else if (winner == 0) {
            threadedJOptionalPane.show(onlineModeFrame, "Draw!", "Game end!", JOptionPane.INFORMATION_MESSAGE);
            //JOptionPane.showMessageDialog(onlineModeFrame, "Draw!", "Game end!", JOptionPane.INFORMATION_MESSAGE);
        }
        else if (winner == 1) {
            threadedJOptionalPane.show(onlineModeFrame, "White win!", "Game end!", JOptionPane.INFORMATION_MESSAGE);
            //JOptionPane.showMessageDialog(onlineModeFrame, "White win!", "Game end!", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
