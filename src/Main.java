import view.MainFrame;
import view.ScreenSize;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            ScreenSize screenSize = new ScreenSize();
//            OfflineModeFrame offlineModeFrame = new OfflineModeFrame((int)(screenSize.getWidth()*0.45),(int)(screenSize.getHeight()*1));
//            offlineModeFrame.setVisible(true);
            MainFrame mainFrame = new MainFrame((int)(screenSize.getWidth()*0.45),(int)(screenSize.getHeight()*1), screenSize);
            mainFrame.setVisible(true);
        });
    }
}
