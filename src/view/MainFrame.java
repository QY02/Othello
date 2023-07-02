package view;

import Sound.ThreadedSound;
import server.StartServer;
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
    private boolean serverIsRunning = false;
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

    private JButton newGameBtn;
    private JButton addGameBtn;
    private JButton onlineModeTypeBackBtn;
    private StartServer server;
    private JLabel blackLabel;
    private JLabel whiteLabel;
    private JButton offlineChoosePlayerTypeBtn1;
    private JButton offlineChoosePlayerTypeBtn2;
    private JButton offlinePlayerChooseComponentsBackBtn;
    private JButton startOfflineModeBtn;
    private JButton playerBtn;
    private JButton aiNormalBtn;
    private JButton aiReversedBtn;
    private int offlineBlackPlayerType = 0;
    private int offlineWhitePlayerType = 0;
    private int offlineCurrentSetPlayer = -1;

    public MainFrame(int width, int height, ScreenSize screenSize) {
        initializeMainFrame(width, height);
        socket = new Socket();
        addOfflineModeButton();
        addOfflinePlayerChooseComponents();
        addOnlineModeButton();
        addOnlineModeTypeComponents();
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
            if (offlineModeFrameIsOpen == false) {
                this.offlineModeBtn.setVisible(false);
                this.onlineModeBtn.setVisible(false);
                this.blackLabel.setVisible(true);
                this.whiteLabel.setVisible(true);
                this.offlineChoosePlayerTypeBtn1.setVisible(true);
                this.offlineChoosePlayerTypeBtn2.setVisible(true);
                this.offlinePlayerChooseComponentsBackBtn.setVisible(true);
                this.startOfflineModeBtn.setVisible(true);
            } else {
                bgm.pause();
                offlineModeFrame.setVisible(true);
                offlineModeFrame.playBgm();
                this.setVisible(false);
            }
        });
    }

    public void addOfflinePlayerChooseComponents() {
        offlineChoosePlayerTypeBtn1 = new JButton("Player");
        offlineChoosePlayerTypeBtn1.setMargin(new Insets(0, 0, 0, 0));
        offlineChoosePlayerTypeBtn1.setVisible(false);
        offlineChoosePlayerTypeBtn1.addActionListener(e -> {
            System.out.println("click offlineChoosePlayerType1 Btn");
            this.offlineCurrentSetPlayer = -1;
            this.blackLabel.setVisible(false);
            this.whiteLabel.setVisible(false);
            this.offlineChoosePlayerTypeBtn1.setVisible(false);
            this.offlineChoosePlayerTypeBtn2.setVisible(false);
            this.offlinePlayerChooseComponentsBackBtn.setVisible(false);
            this.startOfflineModeBtn.setVisible(false);
            this.playerBtn.setVisible(true);
            this.aiNormalBtn.setVisible(true);
            this.aiReversedBtn.setVisible(true);
        });
        add(offlineChoosePlayerTypeBtn1);

        offlineChoosePlayerTypeBtn2 = new JButton("Player");
        offlineChoosePlayerTypeBtn2.setMargin(new Insets(0, 0, 0, 0));
        offlineChoosePlayerTypeBtn2.setVisible(false);
        offlineChoosePlayerTypeBtn2.addActionListener(e -> {
            System.out.println("click offlineChoosePlayerType2 Btn");
            this.offlineCurrentSetPlayer = 1;
            this.blackLabel.setVisible(false);
            this.whiteLabel.setVisible(false);
            this.offlineChoosePlayerTypeBtn1.setVisible(false);
            this.offlineChoosePlayerTypeBtn2.setVisible(false);
            this.offlinePlayerChooseComponentsBackBtn.setVisible(false);
            this.startOfflineModeBtn.setVisible(false);
            this.playerBtn.setVisible(true);
            this.aiNormalBtn.setVisible(true);
            this.aiReversedBtn.setVisible(true);
        });
        add(offlineChoosePlayerTypeBtn2);

        this.blackLabel = new JLabel();
        this.blackLabel.setForeground(Color.BLACK);
        this.blackLabel.setHorizontalAlignment(JLabel.CENTER);
        this.blackLabel.setText("Black");
        this.blackLabel.setVisible(false);
        add(blackLabel);

        this.whiteLabel = new JLabel();
        this.whiteLabel.setForeground(Color.BLACK);
        this.whiteLabel.setHorizontalAlignment(JLabel.CENTER);
        this.whiteLabel.setText("White");
        this.whiteLabel.setVisible(false);
        add(whiteLabel);

        offlinePlayerChooseComponentsBackBtn = new JButton("Back");
        offlinePlayerChooseComponentsBackBtn.setMargin(new Insets(0, 0, 0, 0));
        offlinePlayerChooseComponentsBackBtn.setVisible(false);
        offlinePlayerChooseComponentsBackBtn.addActionListener(e -> {
            System.out.println("click offlinePlayerChooseComponentsBack Btn");
            this.blackLabel.setVisible(false);
            this.whiteLabel.setVisible(false);
            this.offlineChoosePlayerTypeBtn1.setVisible(false);
            this.offlineChoosePlayerTypeBtn2.setVisible(false);
            this.offlinePlayerChooseComponentsBackBtn.setVisible(false);
            this.startOfflineModeBtn.setVisible(false);
            this.offlineModeBtn.setVisible(true);
            this.onlineModeBtn.setVisible(true);
        });
        add(offlinePlayerChooseComponentsBackBtn);

        startOfflineModeBtn = new JButton("Start");
        startOfflineModeBtn.setMargin(new Insets(0, 0, 0, 0));
        startOfflineModeBtn.setVisible(false);
        startOfflineModeBtn.addActionListener(e -> {
            System.out.println("click startOfflineModeBtn Btn");
            bgm.pause();
            offlineModeFrame = new OfflineModeFrame(this.getWidth(), this.getHeight(), this, this.offlineBlackPlayerType, this.offlineWhitePlayerType);
            offlineModeFrameIsOpen = true;
            this.setVisible(false);
        });
        add(startOfflineModeBtn);

        playerBtn = new JButton("Player");
        playerBtn.setMargin(new Insets(0, 0, 0, 0));
        playerBtn.setVisible(false);
        playerBtn.addActionListener(e -> {
            System.out.println("click Player Btn");
            if (this.offlineCurrentSetPlayer == -1) {
                this.offlineBlackPlayerType = 0;
                this.offlineChoosePlayerTypeBtn1.setText("Player");
            } else {
                this.offlineWhitePlayerType = 0;
                this.offlineChoosePlayerTypeBtn2.setText("Player");
            }
            this.playerBtn.setVisible(false);
            this.aiNormalBtn.setVisible(false);
            this.aiReversedBtn.setVisible(false);
            this.blackLabel.setVisible(true);
            this.whiteLabel.setVisible(true);
            this.offlineChoosePlayerTypeBtn1.setVisible(true);
            this.offlineChoosePlayerTypeBtn2.setVisible(true);
            this.offlinePlayerChooseComponentsBackBtn.setVisible(true);
            this.startOfflineModeBtn.setVisible(true);
        });
        add(playerBtn);

        aiNormalBtn = new JButton("AI-Normal");
        aiNormalBtn.setMargin(new Insets(0, 0, 0, 0));
        aiNormalBtn.setVisible(false);
        aiNormalBtn.addActionListener(e -> {
            System.out.println("click AINormal Btn");
            if (this.offlineCurrentSetPlayer == -1) {
                this.offlineBlackPlayerType = 1;
                this.offlineChoosePlayerTypeBtn1.setText("AI-Normal");
            } else {
                this.offlineWhitePlayerType = 1;
                this.offlineChoosePlayerTypeBtn2.setText("AI-Normal");
            }
            this.playerBtn.setVisible(false);
            this.aiNormalBtn.setVisible(false);
            this.aiReversedBtn.setVisible(false);
            this.blackLabel.setVisible(true);
            this.whiteLabel.setVisible(true);
            this.offlineChoosePlayerTypeBtn1.setVisible(true);
            this.offlineChoosePlayerTypeBtn2.setVisible(true);
            this.offlinePlayerChooseComponentsBackBtn.setVisible(true);
            this.startOfflineModeBtn.setVisible(true);
        });
        add(aiNormalBtn);
        
        aiReversedBtn = new JButton("AI-Reversed");
        aiReversedBtn.setMargin(new Insets(0, 0, 0, 0));
        aiReversedBtn.setVisible(false);
        aiReversedBtn.addActionListener(e -> {
            System.out.println("click AIReversed Btn");
            if (this.offlineCurrentSetPlayer == -1) {
                this.offlineBlackPlayerType = 2;
                this.offlineChoosePlayerTypeBtn1.setText("AI-Reversed");
            } else {
                this.offlineWhitePlayerType = 2;
                this.offlineChoosePlayerTypeBtn2.setText("AI-Reversed");
            }
            this.playerBtn.setVisible(false);
            this.aiNormalBtn.setVisible(false);
            this.aiReversedBtn.setVisible(false);
            this.blackLabel.setVisible(true);
            this.whiteLabel.setVisible(true);
            this.offlineChoosePlayerTypeBtn1.setVisible(true);
            this.offlineChoosePlayerTypeBtn2.setVisible(true);
            this.offlinePlayerChooseComponentsBackBtn.setVisible(true);
            this.startOfflineModeBtn.setVisible(true);
        });
        add(aiReversedBtn);
        
        setSizeOfflinePlayerChooseComponents();
    }

    public void setSizeOfflinePlayerChooseComponents() {
        this.blackLabel.setSize((int) (contentWidth * 0.3), (int) (contentHeight * 0.08));
        this.whiteLabel.setSize((int) (contentWidth * 0.3), (int) (contentHeight * 0.08));
        offlineChoosePlayerTypeBtn1.setSize((int) (contentWidth * 0.3), (int) (contentHeight * 0.08));
        offlineChoosePlayerTypeBtn2.setSize((int) (contentWidth * 0.3), (int) (contentHeight * 0.08));
        offlinePlayerChooseComponentsBackBtn.setSize((int) (contentWidth * 0.3), (int) (contentHeight * 0.08));
        startOfflineModeBtn.setSize((int) (contentWidth * 0.3), (int) (contentHeight * 0.08));
        playerBtn.setSize((int) (contentWidth * 0.3), (int) (contentHeight * 0.08));
        aiNormalBtn.setSize((int) (contentWidth * 0.3), (int) (contentHeight * 0.08));
        aiReversedBtn.setSize((int) (contentWidth * 0.3), (int) (contentHeight * 0.08));

        this.blackLabel.setLocation((int)((contentWidth * 0.8 - this.blackLabel.getWidth() - this.offlineChoosePlayerTypeBtn1.getWidth()) / 2), (int) ((contentHeight * 0.6 - this.blackLabel.getHeight() * 2 - this.offlinePlayerChooseComponentsBackBtn.getHeight()) / 2));
        this.blackLabel.setFont(new Font("Calibri", Font.BOLD, Math.min((int) (this.blackLabel.getHeight() * 1.25), (int) (this.blackLabel.getWidth() / 7))));

        this.whiteLabel.setLocation((int)((contentWidth * 0.8 - this.blackLabel.getWidth() - this.offlineChoosePlayerTypeBtn1.getWidth()) / 2), (int) (this.blackLabel.getY() + this.blackLabel.getHeight() + contentHeight * 0.2));
        this.whiteLabel.setFont(new Font("Calibri", Font.BOLD, Math.min((int) (this.whiteLabel.getHeight() * 1.25), (int) (this.whiteLabel.getWidth() / 7))));

        offlineChoosePlayerTypeBtn1.setLocation((int) (this.blackLabel.getX() + this.blackLabel.getWidth() + contentWidth * 0.2), (int) ((contentHeight * 0.6 - this.blackLabel.getHeight() * 2 - this.offlinePlayerChooseComponentsBackBtn.getHeight()) / 2));
        offlineChoosePlayerTypeBtn1.setFont(new Font("Calibri", Font.BOLD, Math.min((int) (offlineChoosePlayerTypeBtn1.getHeight() * 0.5), (int) (offlineChoosePlayerTypeBtn1.getWidth() / 6))));

        offlineChoosePlayerTypeBtn2.setLocation((int) (this.blackLabel.getX() + this.blackLabel.getWidth() + contentWidth * 0.2), (int) (this.blackLabel.getY() + this.blackLabel.getHeight() + contentHeight * 0.2));
        offlineChoosePlayerTypeBtn2.setFont(new Font("Calibri", Font.BOLD, Math.min((int) (offlineChoosePlayerTypeBtn2.getHeight() * 0.5), (int) (offlineChoosePlayerTypeBtn2.getWidth() / 6))));

        offlinePlayerChooseComponentsBackBtn.setLocation((int) ((contentWidth * 0.8 - this.offlinePlayerChooseComponentsBackBtn.getWidth() - this.startOfflineModeBtn.getWidth()) / 2), (int) (this.whiteLabel.getY() + this.whiteLabel.getHeight() + contentHeight * 0.2));
        offlinePlayerChooseComponentsBackBtn.setFont(new Font("Calibri", Font.BOLD, Math.min((int) (offlinePlayerChooseComponentsBackBtn.getHeight() * 0.5), (int) (offlinePlayerChooseComponentsBackBtn.getWidth() / 6))));

        startOfflineModeBtn.setLocation((int) (this.offlinePlayerChooseComponentsBackBtn.getX() + this.offlinePlayerChooseComponentsBackBtn.getWidth() + contentWidth * 0.2), (int) (this.whiteLabel.getY() + this.whiteLabel.getHeight() + contentHeight * 0.2));
        startOfflineModeBtn.setFont(new Font("Calibri", Font.BOLD, Math.min((int) (startOfflineModeBtn.getHeight() * 0.5), (int) (startOfflineModeBtn.getWidth() / 6))));
        
        playerBtn.setLocation((int) ((contentWidth - playerBtn.getWidth()) / 2), (int) ((contentHeight * 0.8 - playerBtn.getHeight() - aiNormalBtn.getHeight() - aiReversedBtn.getHeight()) / 2));
        playerBtn.setFont(new Font("Calibri", Font.BOLD, Math.min((int) (playerBtn.getHeight() * 0.5), (int) (playerBtn.getWidth() / 6))));
        
        aiNormalBtn.setLocation((int) ((contentWidth - aiNormalBtn.getWidth()) / 2), (int) (this.playerBtn.getY() + this.playerBtn.getHeight() + contentHeight * 0.1));
        aiNormalBtn.setFont(new Font("Calibri", Font.BOLD, Math.min((int) (aiNormalBtn.getHeight() * 0.5), (int) (aiNormalBtn.getWidth() / 6))));
        
        aiReversedBtn.setLocation((int) ((contentWidth - aiReversedBtn.getWidth()) / 2), (int) (this.aiNormalBtn.getY() + this.aiNormalBtn.getHeight() + contentHeight * 0.1));
        aiReversedBtn.setFont(new Font("Calibri", Font.BOLD, Math.min((int) (aiReversedBtn.getHeight() * 0.5), (int) (aiReversedBtn.getWidth() / 6))));
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
            offlineModeBtn.setVisible(false);
            onlineModeBtn.setVisible(false);
            newGameBtn.setVisible(true);
            addGameBtn.setVisible(true);
            onlineModeTypeBackBtn.setVisible(true);
//            if (onlineModeFrameIsOpen == false) {
//                offlineModeBtn.setVisible(false);
//                onlineModeBtn.setVisible(false);
////                serverSelectPanel.setVisible(true);
////                backBtn.setVisible(true);
////                addBtn.setVisible(true);
////                deleteBtn.setVisible(true);
//                newGameBtn.setVisible(true);
//                addGameBtn.setVisible(true);
//                onlineModeTypeBackBtn.setVisible(true);
//            } else {
//                bgm.pause();
//                onlineModeFrame.setVisible(true);
//                this.setVisible(false);
//            }
        });
    }

    public void addOnlineModeTypeComponents() {
        newGameBtn = new JButton("New Game");
        newGameBtn.setMargin(new Insets(0, 0, 0, 0));
        newGameBtn.setVisible(false);
        newGameBtn.addActionListener(e -> {
            System.out.println("click New Game Btn");
//            onlineModeTypeBackBtn.setVisible(false);
//            newGameBtn.setVisible(false);
//            addGameBtn.setVisible(false);
            server = new StartServer();
            Thread serverThread = new Thread(server);
            serverThread.start();
            this.serverIsRunning = true;
            openOnlineModeFrame("localhost", 25566);
//            if (!onlineModeFrameIsOpen) {
//                openOnlineModeFrame("localhost", 25566);
//            }
//            else {
//                while (true) {
//                    try {
//                        socket.connect(new InetSocketAddress("localhost", 25566), 1000);
////                        onlineModeFrame.resetSocket(socket);
////                        onlineModeFrame.resetSocket(socket, false);
//                        onlineModeFrame.restart(socket, "localhost", 25566);
//                        thisFrame.setVisible(false);
//                        onlineModeFrame.setVisible(true);
//                        break;
//                    } catch (IOException ex) {
//                        ex.printStackTrace();
//                        try {
//                            socket.close();
//                        } catch (IOException exc) {
//                            exc.printStackTrace();
//                        }
//                        socket = new Socket();
//                        try {
//                            Thread.sleep(1000);
//                        } catch (InterruptedException exc) {
//                            exc.printStackTrace();
//                        }
//                    }
//                }
//            }
        });
        add(newGameBtn);

        addGameBtn = new JButton("Add Game");
        addGameBtn.setMargin(new Insets(0, 0, 0, 0));
        addGameBtn.setVisible(false);
        addGameBtn.addActionListener(e -> {
            System.out.println("click Add Game Btn");
            onlineModeTypeBackBtn.setVisible(false);
            newGameBtn.setVisible(false);
            addGameBtn.setVisible(false);
            serverSelectPanel.setVisible(true);
            backBtn.setVisible(true);
            addBtn.setVisible(true);
            deleteBtn.setVisible(true);
        });
        add(addGameBtn);

        onlineModeTypeBackBtn = new JButton("Back");
        onlineModeTypeBackBtn.setMargin(new Insets(0, 0, 0, 0));
        onlineModeTypeBackBtn.setVisible(false);
        onlineModeTypeBackBtn.addActionListener(e -> {
            System.out.println("click onlineModeTypeBack Btn");
            onlineModeTypeBackBtn.setVisible(false);
            newGameBtn.setVisible(false);
            addGameBtn.setVisible(false);
            offlineModeBtn.setVisible(true);
            onlineModeBtn.setVisible(true);
        });
        add(onlineModeTypeBackBtn);

        setSizeOnlineModeTypeComponents();
    }

    public void serverSelectComponents() {
        serverSelectPanel = new ServerSelectPanel(this.getWidth(), (int) (this.getHeight() * 0.7), thisFrame);
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
//            serverSelectPanel.setVisible(false);
//            backBtn.setVisible(false);
//            addBtn.setVisible(false);
//            deleteBtn.setVisible(false);
//            offlineModeBtn.setVisible(true);
//            onlineModeBtn.setVisible(true);
            serverSelectComponentsBack();
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
            addServerIcon.setImage(addServerIcon.getImage().getScaledInstance((int) (this.getWidth() * 0.1), (int) (this.getWidth() * 0.1), Image.SCALE_DEFAULT));
            String serverAddress = (String) JOptionPane.showInputDialog(this, "input the address here", "Add server", JOptionPane.QUESTION_MESSAGE, addServerIcon, null, null);
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
                Object[] options = {"狠心离开", "再玩一会"};
                int confirm = JOptionPane.showOptionDialog(MainFrame.this, "官人，再玩一会吧^_^", "提示", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (confirm == 0) {
                    System.exit(0);
                }
            }
        });

        this.addComponentListener(new ComponentAdapter() {//拖动窗口监听
            public void componentResized(ComponentEvent e) {
                setSizesMainFrame();
                setSizeOfflinePlayerChooseComponents();
                setSizesServerSelectComponents();
                setSizeOnlineModeTypeComponents();
            }
        });
    }

    public void setSizesMainFrame() {
        contentHeight = thisFrame.getHeight() - inset.top - inset.bottom;
        contentWidth = thisFrame.getWidth() - inset.left - inset.right;
        offlineModeBtn.setSize((int) (thisFrame.getWidth() * 0.5), (int) (thisFrame.getHeight() * 0.1));
        offlineModeBtn.setLocation((int) ((contentWidth - offlineModeBtn.getWidth()) / 2), (int) ((contentHeight * 0.95 - offlineModeBtn.getHeight() * 2) / 2));
        offlineModeBtn.setFont(new Font("Calibri", Font.BOLD, Math.min((int) (offlineModeBtn.getHeight() * 0.5), (int) (offlineModeBtn.getWidth() / 6))));
        onlineModeBtn.setSize((int) (thisFrame.getWidth() * 0.5), (int) (thisFrame.getHeight() * 0.1));
        onlineModeBtn.setLocation(offlineModeBtn.getX(), offlineModeBtn.getY() + offlineModeBtn.getHeight() + (int) (contentHeight * 0.05));
        onlineModeBtn.setFont(new Font("Calibri", Font.BOLD, Math.min((int) (onlineModeBtn.getHeight() * 0.5), (int) (onlineModeBtn.getWidth() / 6))));
    }

    public void setSizeOnlineModeTypeComponents() {
        newGameBtn.setSize((int) (this.getWidth() * 0.3), (int) (this.getHeight() * 0.08));
        newGameBtn.setLocation((int) ((contentWidth * 0.8 - newGameBtn.getWidth() * 2) / 2), (int) ((contentHeight * 0.8 - newGameBtn.getHeight() - onlineModeTypeBackBtn.getHeight()) / 2));
        newGameBtn.setFont(new Font("Calibri", Font.BOLD, Math.min((int) (newGameBtn.getHeight() * 0.5), (int) (newGameBtn.getWidth() / 6))));

        addGameBtn.setSize((int) (this.getWidth() * 0.3), (int) (this.getHeight() * 0.08));
        addGameBtn.setLocation((int) (newGameBtn.getX() + newGameBtn.getWidth() + contentWidth * 0.2), (int) ((contentHeight * 0.8 - newGameBtn.getHeight() - onlineModeTypeBackBtn.getHeight()) / 2));
        addGameBtn.setFont(new Font("Calibri", Font.BOLD, Math.min((int) (addGameBtn.getHeight() * 0.5), (int) (addGameBtn.getWidth() / 6))));

        onlineModeTypeBackBtn.setSize((int) (this.getWidth() * 0.3), (int) (this.getHeight() * 0.08));
        onlineModeTypeBackBtn.setLocation((int) ((contentWidth - onlineModeTypeBackBtn.getWidth()) / 2), (int) (newGameBtn.getY() + newGameBtn.getHeight() + contentHeight * 0.2));
        onlineModeTypeBackBtn.setFont(new Font("Calibri", Font.BOLD, Math.min((int) (onlineModeTypeBackBtn.getHeight() * 0.5), (int) (onlineModeTypeBackBtn.getWidth() / 6))));
    }

    public void setSizesServerSelectComponents() {
        serverSelectPanel.reSize(this.getWidth(), (int) (this.getHeight() * 0.7));

        backBtn.setSize((int) (this.getWidth() * 0.2), (int) (this.getHeight() * 0.08));
        backBtn.setLocation((int) (contentWidth * 0.1), serverSelectPanel.getY() + serverSelectPanel.getHeight() + (int) (contentHeight * 0.08));
        backBtn.setFont(new Font("Calibri", Font.BOLD, Math.min((int) (backBtn.getHeight() * 0.5), (int) (backBtn.getWidth() / 6))));

        addBtn.setSize((int) (this.getWidth() * 0.2), (int) (this.getHeight() * 0.08));
        addBtn.setLocation(backBtn.getX() + backBtn.getWidth() + (int) (contentWidth * 0.1), serverSelectPanel.getY() + serverSelectPanel.getHeight() + (int) (contentHeight * 0.08));
        addBtn.setFont(new Font("Calibri", Font.BOLD, Math.min((int) (addBtn.getHeight() * 0.5), (int) (addBtn.getWidth() / 6))));

        deleteBtn.setSize((int) (this.getWidth() * 0.2), (int) (this.getHeight() * 0.08));
        deleteBtn.setLocation(addBtn.getX() + addBtn.getWidth() + (int) (contentWidth * 0.1), serverSelectPanel.getY() + serverSelectPanel.getHeight() + (int) (contentHeight * 0.08));
        deleteBtn.setFont(new Font("Calibri", Font.BOLD, Math.min((int) (deleteBtn.getHeight() * 0.5), (int) (deleteBtn.getWidth() / 6))));
    }

    public void openOnlineModeFrame(String address, int port) {
        if (!onlineModeFrameIsOpen) {
            while (true) {
                try {
                    socket.connect(new InetSocketAddress(address, port), 1000);
                    bgm.pause();
                    onlineModeFrame = new OnlineModeFrame(this.getWidth(), this.getHeight(), socket, this, address, port);
                    onlineModeFrameIsOpen = true;
                    this.setVisible(false);
                    break;
//                serverSelectComponentsBack();
                } catch (IOException f) {
                    f.printStackTrace();
                    try {
                        socket.close();
                    } catch (IOException g) {
                        g.printStackTrace();
                    }
                    socket = new Socket();
                    if (serverIsRunning) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Connect failed", "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                    }
                }
            }
        } else {
            reopenOnlineModeFrame(address, port);
        }
    }

    public void reopenOnlineModeFrame(String address, int port) {
        while (true) {
            try {
                socket.connect(new InetSocketAddress(address, port), 1000);
//                        onlineModeFrame.resetSocket(socket);
//                        onlineModeFrame.resetSocket(socket, false);
                onlineModeFrame.restart(socket, address, port);
                thisFrame.setVisible(false);
                onlineModeFrame.setVisible(true);
                break;
            } catch (IOException ex) {
                ex.printStackTrace();
                try {
                    socket.close();
                } catch (IOException exc) {
                    exc.printStackTrace();
                }
                socket = new Socket();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException exc) {
                    exc.printStackTrace();
                }
            }
        }
    }

    public void serverSelectComponentsBack() {
        serverSelectPanel.setVisible(false);
        backBtn.setVisible(false);
        addBtn.setVisible(false);
        deleteBtn.setVisible(false);
//        offlineModeBtn.setVisible(true);
//        onlineModeBtn.setVisible(true);
        onlineModeTypeBackBtn.setVisible(true);
        newGameBtn.setVisible(true);
        addGameBtn.setVisible(true);
    }

    public void stopServer() {
        if (serverIsRunning) {
            this.server.stop();
            this.serverIsRunning = false;
        }
    }

    public void closeClientSocket() {
        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.socket = new Socket();
    }

    public boolean isServerIsRunning() {
        return this.serverIsRunning;
    }
}
