package view.onlineMode;

import controller.onlineMode.OnlineGameController;
import net.Receive;
import net.Reconnect;
import net.Send;
import net.TestConnection;
import view.BackGroundPanel;
import view.MainFrame;
import view.offlineMode.ChessBoardPanel;
import view.offlineMode.StatusPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.Socket;

public class OnlineModeFrame extends JFrame {

    private String address;
    private int port;
    private Socket socket;
    private Send send;
    private Receive receive;
    private TestConnection testConnection;
    private Reconnect reconnect;

    private MainFrame mainFrame;
    private OnlineModeFrame thisFrame = this;

    private OnlineGameController onlineGameController;
    private OnlineChessBoardPanel onlineChessBoardPanel;
    private OnlineStatusPanel onlineStatusPanel;
    private BackGroundPanel backGroundPanel;

    private int minSize;
    private int chessBoardPanelSize;
    private int contentHeight;
    private int contentWidth;

    public OnlineModeFrame(int width, int height, Socket socket, MainFrame mainFrame, String address, int port){
        this.address = address;
        this.port = port;
        this.socket = socket;
        onlineGameController = new OnlineGameController(this);
        this.send = new Send(this.socket, onlineGameController);
        this.reconnect = new Reconnect(this);
        this.testConnection = new TestConnection(send, reconnect, this);
        this.mainFrame = mainFrame;

        this.setTitle("Othello - OnlineMode");
        ImageIcon icon = new ImageIcon(this.getClass().getResource("/res/icon.png"));
        this.setIconImage(icon.getImage());
        this.setSize(width, height);

        this.setLayout(null);
        this.setLocationRelativeTo(null);

        this.setVisible(true);

        Insets inset = this.getInsets();
        contentHeight = this.getHeight() - inset.top - inset.bottom;
        contentWidth = this.getWidth() - inset.left - inset.right;
        minSize = contentWidth < contentHeight ? contentWidth:contentHeight;
        int temp = (int)(minSize * 0.75);
        chessBoardPanelSize = (temp % 2) == 0 ? temp : (temp + 1);

        onlineChessBoardPanel = new OnlineChessBoardPanel(chessBoardPanelSize, chessBoardPanelSize, send, onlineGameController);
        onlineChessBoardPanel.setLocation((contentWidth - onlineChessBoardPanel.getWidth()) / 2, (contentHeight - onlineChessBoardPanel.getHeight()) / 3);
        this.add(onlineChessBoardPanel);
        onlineStatusPanel = new OnlineStatusPanel(this.onlineChessBoardPanel.getWidth(), this.onlineChessBoardPanel.getY(), onlineGameController);
        onlineStatusPanel.setLocation(this.onlineChessBoardPanel.getX(), 0);
        this.add(onlineStatusPanel);

        this.receive = new Receive(this.socket, onlineChessBoardPanel, onlineStatusPanel, onlineGameController, testConnection);
        Thread receiveThread = new Thread(receive);
        receiveThread.start();
        testConnection.start();

        backGroundPanel = new BackGroundPanel(contentWidth,contentHeight);
        this.add(backGroundPanel);

        //this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
//                Object[] options = {"狠心离开","再玩一会"};
//                int confirm = JOptionPane.showOptionDialog(OfflineModeFrame.this,"官人，再玩一会吧^_^","提示",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
//                if(confirm == 0){
//                    System.exit(0);
//                }
                thisFrame.setVisible(false);
                mainFrame.setVisible(true);
            }
        });

        this.addComponentListener(new ComponentAdapter() {//拖动窗口监听
            public void componentResized(ComponentEvent e) {
                contentHeight = thisFrame.getHeight() - inset.top - inset.bottom;
                contentWidth = thisFrame.getWidth() - inset.left - inset.right;
                minSize = contentWidth < contentHeight ? contentWidth:contentHeight;
                int temp = (int)(minSize * 0.75);
                chessBoardPanelSize = (temp % 2) == 0 ? temp : (temp + 1);
                onlineChessBoardPanel.setSize(chessBoardPanelSize, chessBoardPanelSize);
                onlineChessBoardPanel.setLocation((contentWidth - onlineChessBoardPanel.getWidth()) / 2, (contentHeight - onlineChessBoardPanel.getHeight()) / 3);
                onlineChessBoardPanel.updateSize();
                onlineStatusPanel.setSize(onlineChessBoardPanel.getWidth(), onlineChessBoardPanel.getY());
                onlineStatusPanel.setLocation(onlineChessBoardPanel.getX(), 0);
                onlineStatusPanel.updateSize();
                backGroundPanel.setSize(contentWidth,contentHeight);
            }
        });
    }

    public void resetSocket(Socket socket) {
        this.socket = socket;
        receive.resetSocket(this.socket);
        send.resetSocket(this.socket);
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public OnlineStatusPanel getOnlineStatusPanel() {
        return onlineStatusPanel;
    }
}
