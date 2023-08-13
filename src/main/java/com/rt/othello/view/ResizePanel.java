package com.rt.othello.view;

import javax.swing.*;
import java.awt.*;

public class ResizePanel extends JPanel {
    private int width;
    private int height;
    private double relativeMainFrameWidth;
    private double relativeMainFrameHeight;
    private MainFrame mainFrame;
    private JLabel relativeWidthLabel;
    private JLabel relativeHeightLabel;
    private JTextField widthInputBox;
    private JTextField heightInputBox;
    private JButton confirmBtn;
    private JButton cancelBtn;

    public ResizePanel(MainFrame mainFrame, int width, int height) {
        this.mainFrame = mainFrame;
        this.width = width;
        this.height = height;
        this.setSize(this.width, this.height);
        this.setLayout(null);
        this.setVisible(false);
        initializeResizePanel();
    }

    public void initializeResizePanel() {
        this.relativeWidthLabel = new JLabel();
        this.relativeWidthLabel.setForeground(Color.BLACK);
        this.relativeWidthLabel.setHorizontalAlignment(JLabel.CENTER);
        this.relativeWidthLabel.setText("Relative width:");
        add(this.relativeWidthLabel);

        this.relativeHeightLabel = new JLabel();
        this.relativeHeightLabel.setForeground(Color.BLACK);
        this.relativeHeightLabel.setHorizontalAlignment(JLabel.CENTER);
        this.relativeHeightLabel.setText("Relative height:");
        add(this.relativeHeightLabel);

        this.widthInputBox = new JTextField();
        this.add(widthInputBox);

        this.heightInputBox = new JTextField();
        this.add(heightInputBox);

        this.confirmBtn = new JButton("Confirm");
        this.confirmBtn.setMargin(new Insets(0, 0, 0, 0));
        this.confirmBtn.addActionListener(e -> {
            boolean isSuccess = getValuesFromInputBoxes();
            if (isSuccess) {
                this.mainFrame.setSize((int) (ScreenSize.getWidth() * this.relativeMainFrameWidth), (int) (ScreenSize.getHeight() * this.relativeMainFrameHeight));
                this.mainFrame.setLocation((int) ((ScreenSize.getWidth() - this.mainFrame.getWidth()) / 2), (int) ((ScreenSize.getHeight() - this.mainFrame.getHeight()) / 2));
                this.mainFrame.resizeThisFrame();
                back();
            }
        });
        this.add(confirmBtn);

        this.cancelBtn = new JButton("Cancel");
        this.cancelBtn.setMargin(new Insets(0, 0, 0, 0));
        this.cancelBtn.addActionListener(e -> {
            back();
        });
        this.add(cancelBtn);

        resize();
    }

    public boolean getValuesFromInputBoxes() {
        try {
            double relativeWidthTemp = Double.parseDouble(this.widthInputBox.getText());
            double relativeHeightTemp = Double.parseDouble(this.heightInputBox.getText());
            if ((relativeWidthTemp < 0) || (relativeHeightTemp < 0)) {
                throw new NumberFormatException();
            }
            if ((relativeWidthTemp == this.relativeMainFrameWidth) && (relativeHeightTemp == this.relativeMainFrameHeight)) {
                back();
                return false;
            }
            this.relativeMainFrameWidth = relativeWidthTemp;
            this.relativeMainFrameHeight = relativeHeightTemp;
            return true;
        }
        catch (NullPointerException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this.mainFrame, "Invalid input!", "Warning", JOptionPane.WARNING_MESSAGE, null);
            return false;
        }
    }

    public void back() {
        this.setVisible(false);
        this.mainFrame.getOfflineModeBtn().setVisible(true);
        this.mainFrame.getOnlineModeBtn().setVisible(true);
        this.mainFrame.getResizeBtn().setVisible(true);
    }

    public void resize() {
        this.relativeWidthLabel.setSize((int)(this.width * 0.4), (int)(this.height * 0.2));
        this.relativeWidthLabel.setLocation((int)((this.width * 0.95 - this.relativeWidthLabel.getWidth() * 2) / 2), (int)((this.height * 0.8 - this.relativeWidthLabel.getHeight() * 3) / 2));
        this.relativeWidthLabel.setFont(new Font("Calibri", Font.BOLD, Math.min((int) (this.relativeWidthLabel.getHeight() * 0.5), (int) (this.relativeWidthLabel.getWidth() / 6))));

        this.relativeHeightLabel.setSize((int)(this.width * 0.4), (int)(this.height * 0.2));
        this.relativeHeightLabel.setLocation(this.relativeWidthLabel.getX(), (int)(this.relativeWidthLabel.getY() + this.relativeWidthLabel.getHeight() + this.height * 0.1));
        this.relativeHeightLabel.setFont(new Font("Calibri", Font.BOLD, Math.min((int) (this.relativeHeightLabel.getHeight() * 0.5), (int) (this.relativeHeightLabel.getWidth() / 6))));

        this.widthInputBox.setSize((int)(this.width * 0.4), (int)(this.height * 0.2));
        this.widthInputBox.setLocation((int)(this.relativeWidthLabel.getX() + this.relativeWidthLabel.getWidth() + this.width * 0.05), (int)((this.height * 0.8 - this.widthInputBox.getHeight() * 3) / 2));
        this.widthInputBox.setFont(new Font("Calibri", Font.BOLD, Math.min((int) (this.widthInputBox.getHeight() * 0.5), (int) (this.widthInputBox.getWidth() / 6))));

        this.heightInputBox.setSize((int)(this.width * 0.4), (int)(this.height * 0.2));
        this.heightInputBox.setLocation((int)(this.relativeHeightLabel.getX() + this.relativeHeightLabel.getWidth() + this.width * 0.05), (int)(this.widthInputBox.getY() + this.widthInputBox.getHeight() + this.height * 0.1));
        this.heightInputBox.setFont(new Font("Calibri", Font.BOLD, Math.min((int) (this.heightInputBox.getHeight() * 0.5), (int) (this.heightInputBox.getWidth() / 6))));

        this.confirmBtn.setSize((int)(this.width * 0.4), (int)(this.height * 0.2));
        this.confirmBtn.setLocation((int)((this.width * 0.95 - this.confirmBtn.getWidth() * 2) / 2), (int)(this.heightInputBox.getY() + this.heightInputBox.getHeight() + this.height * 0.1));
        this.confirmBtn.setFont(new Font("Calibri", Font.BOLD, Math.min((int) (this.confirmBtn.getHeight() * 0.5), (int) (this.confirmBtn.getWidth() / 6))));

        this.cancelBtn.setSize((int)(this.width * 0.4), (int)(this.height * 0.2));
        this.cancelBtn.setLocation((int)(this.confirmBtn.getX() + this.confirmBtn.getWidth() + this.width * 0.05), (int)(this.heightInputBox.getY() + this.heightInputBox.getHeight() + this.height * 0.1));
        this.cancelBtn.setFont(new Font("Calibri", Font.BOLD, Math.min((int) (this.cancelBtn.getHeight() * 0.5), (int) (this.cancelBtn.getWidth() / 6))));
    }

    public void resizeThisPanel(int width, int height) {
        this.width = width;
        this.height = height;
        this.setSize(this.width, this.height);
        resize();
    }

    public void setRelativeSizeDisplayedInInputBoxes(double relativeWidth, double relativeHeight) {
        this.relativeMainFrameWidth = relativeWidth;
        this.relativeMainFrameHeight = relativeHeight;
        this.widthInputBox.setText(String.valueOf(this.relativeMainFrameWidth));
        this.widthInputBox.setCaretPosition(0);
        this.heightInputBox.setText(String.valueOf(this.relativeMainFrameHeight));
        this.heightInputBox.setCaretPosition(0);
    }
}
