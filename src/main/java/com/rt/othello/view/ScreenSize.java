package com.rt.othello.view;

import java.awt.*;

public class ScreenSize {

    private  static int width;
    private  static int height;

    public ScreenSize() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.width = (int)screenSize.getWidth();
        this.height = (int)screenSize.getHeight();
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }
}
