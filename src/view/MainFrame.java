package view;

import Sound.ThreadedSound;
import view.offlineMode.OfflineModeFrame;
import view.onlineMode.OnlineModeFrame;
import view.onlineMode.ServerSelectPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class MainFrame extends JFrame {

    private OfflineModeFrame offlineModeFrame;
    private OnlineModeFrame onlineModeFrame;

    private boolean offlineModeFrameIsOpen = false;
    private boolean onlineModeFrameIsOpen = false;
    private MainFrame thisFrame = this;

    private ThreadedSound bgm;

    private Insets inset;
    private int contentHeight;
    private int contentWidth;

    private Socket socket;

    private JButton offlineModeBtn;
    private JButton onlineModeBtn;
    private ServerSelectPanel serverSelectPanel;
    private JButton backBtn;
    private JButton addBtn;
    private JButton deleteBtn;

    public MainFrame(int width, int height, ScreenSize screenSize){
        initializeMainFrame(width, height);
        socket = new Socket();
        addOfflineModeButton();
        addOnlineModeButton();
        serverSelectComponents();
        setSizesMainFrame();
        addListeners();
    }

    public void playBgm() {
        bgm.play();
    }

    public void pauseBgm() {
        bgm.pause();
    }

    public void initializeMainFrame(int width, int height) {
        this.setTitle("Othello");
        ImageIcon icon = new ImageIcon(this.getClass().getResource("/res/icon.png"));
        this.setIconImage(icon.getImage());
        this.setLayout(null);

        this.setSize(width, height);

        this.setLocationRelativeTo(null);

        this.bgm = new ThreadedSound("/res/bgm00.wav", true);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        this.setVisible(true);
        inset = this.getInsets();
        contentHeight = this.getHeight() - inset.top - inset.bottom;
        contentWidth = this.getWidth() - inset.left - inset.right;
    }

    public void addOfflineModeButton() {
        offlineModeBtn = new JButton("OfflineMode");
//        offlineModeBtn.setSize((int)(this.getWidth() * 0.5), (int)(this.getHeight() * 0.1));
//        offlineModeBtn.setLocation((int)((contentWidth - offlineModeBtn.getWidth()) / 2),(int)((contentHeight * 0.95 - offlineModeBtn.getHeight() * 2) / 2));
//        offlineModeBtn.setFont(new Font("Calibri", Font.BOLD, Math.min((int)(offlineModeBtn.getHeight() * 0.5), (int)(offlineModeBtn.getWidth() / 6))));
        offlineModeBtn.setMargin(new Insets(0, 0, 0, 0));
        this.add(offlineModeBtn);
        offlineModeBtn.addActionListener(e -> {
            System.out.println("click OfflineMode Btn");
            if (offlineModeFrameIsOpen == false){
                bgm.pause();
                offlineModeFrame = new OfflineModeFrame(this.getWidth(),this.getHeight(), this);
                offlineModeFrameIsOpen = true;
            }
            else {
                bgm.pause();
                offlineModeFrame.setVisible(true);
                offlineModeFrame.playBgm();
            }
            this.setVisible(false);
        });
    }

    public void addOnlineModeButton() {
        onlineModeBtn = new JButton("OnlineMode");
//        onlineModeBtn.setSize((int)(this.getWidth() * 0.5), (int)(this.getHeight() * 0.1));
//        onlineModeBtn.setLocation(offlineModeBtn.getX(),offlineModeBtn.getY() + offlineModeBtn.getHeight() + (int)(contentHeight * 0.05));
//        onlineModeBtn.setFont(new Font("Calibri", Font.BOLD, Math.min((int)(onlineModeBtn.getHeight() * 0.5), (int)(onlineModeBtn.getWidth() / 6))));
        onlineModeBtn.setMargin(new Insets(0, 0, 0, 0));
        add(onlineModeBtn);
        onlineModeBtn.addActionListener(e -> {
            System.out.println("click OnlineMode Btn");
            if (onlineModeFrameIsOpen == false) {
                offlineModeBtn.setVisible(false);
                onlineModeBtn.setVisible(false);
                serverSelectPanel.setVisible(true);
                backBtn.setVisible(true);
                addBtn.setVisible(true);
                deleteBtn.setVisible(true);
            }
            else {
                bgm.pause();
                onlineModeFrame.setVisible(true);
                this.setVisible(false);
            }
        });
    }

    public void serverSelectComponents() {
        serverSelectPanel = new ServerSelectPanel(this.getWidth(), (int)(this.getHeight() * 0.7), thisFrame);
        serverSelectPanel.setVisible(false);
        this.add(serverSelectPanel);

        backBtn = new JButton("Back");
//        backBtn.setSize((int)(this.getWidth() * 0.2), (int)(this.getHeight() * 0.08));
//        backBtn.setLocation((int)(contentWidth * 0.1),serverSelectPanel.getY() + serverSelectPanel.getHeight() + (int)(contentHeight * 0.08));
//        backBtn.setFont(new Font("Calibri", Font.BOLD, Math.min((int)(backBtn.getHeight() * 0.5), (int)(backBtn.getWidth() / 6))));
        backBtn.setMargin(new Insets(0, 0, 0, 0));
        backBtn.setVisible(false);
        backBtn.addActionListener(e -> {
            System.out.println("click Back Btn");
            serverSelectPanel.setVisible(false);
            backBtn.setVisible(false);
            addBtn.setVisible(false);
            deleteBtn.setVisible(false);
            offlineModeBtn.setVisible(true);
            onlineModeBtn.setVisible(true);
        });
        add(backBtn);

        addBtn = new JButton("Add");
//        addBtn.setSize((int)(this.getWidth() * 0.2), (int)(this.getHeight() * 0.08));
//        addBtn.setLocation(backBtn.getX() + backBtn.getWidth() + (int)(contentWidth * 0.1),serverSelectPanel.getY() + serverSelectPanel.getHeight() + (int)(contentHeight * 0.08));
//        addBtn.setFont(new Font("Calibri", Font.BOLD, Math.min((int)(addBtn.getHeight() * 0.5), (int)(addBtn.getWidth() / 6))));
        addBtn.setMargin(new Insets(0, 0, 0, 0));
        addBtn.setVisible(false);
        addBtn.addActionListener(e -> {
            System.out.println("click Add Btn");
            ImageIcon addServerIcon = new ImageIcon(this.getClass().getResource("/res/saveIcon.png"));
            addServerIcon.setImage(addServerIcon.getImage().getScaledInstance((int)(this.getWidth() * 0.1),(int)(this.getWidth() * 0.1),Image.SCALE_DEFAULT));
            String serverAddress = (String) JOptionPane.showInputDialog(this, "input the address here","Add server",JOptionPane.QUESTION_MESSAGE,addServerIcon,null,null);
            if ((serverAddress != null) && (!serverAddress.equals(""))) {
                serverSelectPanel.addServer(serverAddress);
            }
        });
        add(addBtn);

        deleteBtn = new JButton("Delete");
//        deleteBtn.setSize((int)(this.getWidth() * 0.2), (int)(this.getHeight() * 0.08));
//        deleteBtn.setLocation(addBtn.getX() + addBtn.getWidth() + (int)(contentWidth * 0.1),serverSelectPanel.getY() + serverSelectPanel.getHeight() + (int)(contentHeight * 0.08));
//        deleteBtn.setFont(new Font("Calibri", Font.BOLD, Math.min((int)(deleteBtn.getHeight() * 0.5), (int)(deleteBtn.getWidth() / 6))));
        deleteBtn.setMargin(new Insets(0, 0, 0, 0));
        deleteBtn.setVisible(false);
        deleteBtn.addActionListener(e -> {
            System.out.println("click Delete Btn");
            serverSelectPanel.deleteServer();
        });
        add(deleteBtn);
        setSizesServerSelectComponents();
    }

    public void addListeners() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Object[] options = {"狠心离开","再玩一会"};
                int confirm = JOptionPane.showOptionDialog(MainFrame.this,"官人，再玩一会吧^_^","提示",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
                if(confirm == 0){
                    System.exit(0);
                }
            }
        });

        this.addComponentListener(new ComponentAdapter() {//拖动窗口监听
            public void componentResized(ComponentEvent e) {
                setSizesMainFrame();
                setSizesServerSelectComponents();
            }
        });
    }

    public void setSizesMainFrame() {
        contentHeight = thisFrame.getHeight() - inset.top - inset.bottom;
        contentWidth = thisFrame.getWidth() - inset.left - inset.right;
        offlineModeBtn.setSize((int)(thisFrame.getWidth() * 0.5), (int)(thisFrame.getHeight() * 0.1));
        offlineModeBtn.setLocation((int)((contentWidth - offlineModeBtn.getWidth()) / 2),(int)((contentHeight * 0.95 - offlineModeBtn.getHeight() * 2) / 2));
        offlineModeBtn.setFont(new Font("Calibri", Font.BOLD, Math.min((int)(offlineModeBtn.getHeight() * 0.5), (int)(offlineModeBtn.getWidth() / 6))));
        onlineModeBtn.setSize((int)(thisFrame.getWidth() * 0.5), (int)(thisFrame.getHeight() * 0.1));
        onlineModeBtn.setLocation(offlineModeBtn.getX(),offlineModeBtn.getY() + offlineModeBtn.getHeight() + (int)(contentHeight * 0.05));
        onlineModeBtn.setFont(new Font("Calibri", Font.BOLD, Math.min((int)(onlineModeBtn.getHeight() * 0.5), (int)(onlineModeBtn.getWidth() / 6))));
    }

    public void setSizesServerSelectComponents() {
        serverSelectPanel.reSize(this.getWidth(), (int)(this.getHeight() * 0.7));

        backBtn.setSize((int)(this.getWidth() * 0.2), (int)(this.getHeight() * 0.08));
        backBtn.setLocation((int)(contentWidth * 0.1),serverSelectPanel.getY() + serverSelectPanel.getHeight() + (int)(contentHeight * 0.08));
        backBtn.setFont(new Font("Calibri", Font.BOLD, Math.min((int)(backBtn.getHeight() * 0.5), (int)(backBtn.getWidth() / 6))));

        addBtn.setSize((int)(this.getWidth() * 0.2), (int)(this.getHeight() * 0.08));
        addBtn.setLocation(backBtn.getX() + backBtn.getWidth() + (int)(contentWidth * 0.1),serverSelectPanel.getY() + serverSelectPanel.getHeight() + (int)(contentHeight * 0.08));
        addBtn.setFont(new Font("Calibri", Font.BOLD, Math.min((int)(addBtn.getHeight() * 0.5), (int)(addBtn.getWidth() / 6))));

        deleteBtn.setSize((int)(this.getWidth() * 0.2), (int)(this.getHeight() * 0.08));
        deleteBtn.setLocation(addBtn.getX() + addBtn.getWidth() + (int)(contentWidth * 0.1),serverSelectPanel.getY() + serverSelectPanel.getHeight() + (int)(contentHeight * 0.08));
        deleteBtn.setFont(new Font("Calibri", Font.BOLD, Math.min((int)(deleteBtn.getHeight() * 0.5), (int)(deleteBtn.getWidth() / 6))));
    }

    public void openOnlineModeFrame(String address, int port) {
        try {
            socket.connect(new InetSocketAddress(address, port),1000);
            bgm.pause();
            onlineModeFrame = new OnlineModeFrame(this.getWidth(),this.getHeight(), socket, this, address, port);
            onlineModeFrameIsOpen = true;
            this.setVisible(false);
            back();
        } catch (IOException f) {
            f.printStackTrace();
            try {
                socket.close();
            } catch (IOException g) {
                g.printStackTrace();
            }
            socket = new Socket();
            JOptionPane.showMessageDialog(this, "Connect failed", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    public void back() {
        serverSelectPanel.setVisible(false);
        backBtn.setVisible(false);
        addBtn.setVisible(false);
        deleteBtn.setVisible(false);
        offlineModeBtn.setVisible(true);
        onlineModeBtn.setVisible(true);
    }
}
