package view;

import javax.swing.*;
import java.awt.*;

public class BackGroundPanel extends JPanel {

    public BackGroundPanel(int width, int height){
        this.setVisible(true);
        this.setFocusable(true);
        this.setLayout(null);
        this.setSize(width,height);
    }

    public void paintComponent(Graphics g){
        ImageIcon black = new ImageIcon(this.getClass().getResource("/res/background.png"));
        g.drawImage(black.getImage(),0,0,this.getWidth(),this.getHeight(),this);
    }
}
