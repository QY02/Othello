package com.rt.othello;

import com.rt.othello.view.MainFrame;
import com.rt.othello.view.ScreenSize;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            ScreenSize screenSize = new ScreenSize();
//            OfflineModeFrame offlineModeFrame = new OfflineModeFrame((int)(screenSize.getWidth()*0.45),(int)(screenSize.getHeight()*1));
//            offlineModeFrame.setVisible(true);
            MainFrame mainFrame = new MainFrame((int)(screenSize.getWidth()*1),(int)(screenSize.getHeight()*1), screenSize);
//            MainFrame mainFrame = new MainFrame((int)(screenSize.getWidth()*0.45),(int)(screenSize.getHeight()*1), screenSize);
            mainFrame.setVisible(true);
        });
    }
}
