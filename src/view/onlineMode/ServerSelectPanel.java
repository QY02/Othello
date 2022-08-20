package view.onlineMode;

import view.MainFrame;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

public class ServerSelectPanel extends JScrollPane {
    private JPanel contentPanel = new JPanel();

    private MainFrame mainFrame;

    private FileReader fileReader;
    private BufferedReader bufferedReader;
    private FileWriter fileWriter;
    private BufferedWriter bufferedWriter;

    private int serverTextHeight;

    private ArrayList<String> servers = new ArrayList<>(0);
    private ArrayList<ServerLabel> serverLabels = new ArrayList<>(0);

    private Border blackLineBorder = BorderFactory.createLineBorder(Color.BLACK);

    private int width;
    private int height;

    private int selected = -1;

    public ServerSelectPanel(int width, int height, MainFrame mainFrame){
        super(VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.mainFrame = mainFrame;
        this.width = width;
        this.height = height;
        serverTextHeight = height / 8;
        try {
            fileReader = new FileReader("servers.txt");
            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("file is not exist");
            File file = new File("servers.txt");
            try {
                file.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        refreshServerList();
        this.getVerticalScrollBar().setUnitIncrement(10);
        this.getVerticalScrollBar().setPreferredSize(new Dimension((int)(width * 0.03), height));
        this.setSize(width - (int)(width * 0.024), height);
        //this.setVisible(true);
    }

    public void addServer(String serverAddress) {
        try {
            fileWriter = new FileWriter("servers.txt", true);
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(serverAddress + "\n");
            bufferedWriter.close();
            fileWriter.close();
            refreshServerList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteServer() {
        if (selected != -1) {
            try {
                fileWriter = new FileWriter("servers.txt", false);
                bufferedWriter = new BufferedWriter(fileWriter);
                for (int i = 0; i < servers.size(); i++) {
                    if (i != selected) {
                        bufferedWriter.write(servers.get(i) + "\n");
                    }
                }
                bufferedWriter.close();
                fileWriter.close();
                refreshServerList();
                selected = -1;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void refreshServerList() {
        servers.clear();
        serverLabels.clear();
        contentPanel.removeAll();
        try {
            fileReader = new FileReader("servers.txt");
            bufferedReader = new BufferedReader(fileReader);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String serverAddress = null;
        do {
            try {
                serverAddress = bufferedReader.readLine();
                if (serverAddress != null) {
                    servers.add(serverAddress);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (serverAddress != null);
        try {
            bufferedReader.close();
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        contentPanel.setPreferredSize(new Dimension((int)(width * 0.9), serverTextHeight * servers.size()));
        contentPanel.setLayout(null);
        for (int i = 0; i < servers.size(); i++) {
            ServerLabel serverLabel = new ServerLabel(i, servers.get(i));
            serverLabel.setSize((int)(width * 0.8), (int)(serverTextHeight * 0.8));
            serverLabel.setLocation((int)(width * 0.1), (int)(serverTextHeight * (0.1 + i)));
            serverLabel.setText(servers.get(i));
            System.out.println(servers.get(i));
            serverLabel.setFont(new Font("Calibri", Font.BOLD, Math.min((int)(serverLabel.getHeight()), (int)(serverLabel.getWidth() / 14))));
            serverLabel.setForeground(Color.BLACK);
            serverLabel.setHorizontalAlignment(JLabel.CENTER);
            serverLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    //super.mousePressed(e);
                    int clickCount = e.getClickCount();
                    if((clickCount == 1) && (e.getButton() == e.BUTTON1)){
                        if (serverLabel.getBorder() == null) {
                            serverLabel.setBorder(blackLineBorder);
                            selected = serverLabel.getId();
                            for (int j = 0; j < serverLabels.size(); j++) {
                                if (serverLabels.get(j) != serverLabel) {
                                    serverLabels.get(j).setBorder(null);
                                }
                            }
                        }
                        else {
                            serverLabel.setBorder(null);
                            selected = -1;
                        }
                    }
                    else if (clickCount == 2) {
                        System.out.println("connect to the server");
                        //ImageIcon serverIcon = new ImageIcon(this.getClass().getResource("/res/loadIcon.png"));
                        //serverIcon.setImage(serverIcon.getImage().getScaledInstance((int)(mainFrame.getWidth() * 0.114),(int)(mainFrame.getWidth() * 0.1),Image.SCALE_DEFAULT));
                        String serverAddress = serverLabel.getText();
                        if ((serverAddress != null) && (!serverAddress.equals(""))){
                            String[] temp = serverAddress.split(":");
                            if ((temp.length != 1) && (temp.length != 2)){
                                System.out.println("Invalid address");
                                JOptionPane.showMessageDialog(mainFrame, "Invalid address", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                            else {
                                System.out.println("Address is: " + temp[0]);
                                int port;
                                if (temp.length == 1){
                                    port = 25566;
                                    mainFrame.openOnlineModeFrame(temp[0], port);
                                }
                                else {
                                    try {
                                        port = Integer.parseInt(temp[1]);
                                        System.out.println("Port is: " + port);
                                        mainFrame.openOnlineModeFrame(temp[0], port);
                                    } catch (NumberFormatException f) {
                                        System.out.println("Invalid address");
                                        f.printStackTrace();
                                        JOptionPane.showMessageDialog(mainFrame, "Invalid address", "Error", JOptionPane.ERROR_MESSAGE);
                                    }
                                }
                            }
                        }
                        else if (serverAddress == null) {
                            JOptionPane.showMessageDialog(mainFrame, "Invalid address", "Error", JOptionPane.ERROR_MESSAGE);
                            System.out.println("Invalid address");
                        }
                        else {
                            JOptionPane.showMessageDialog(mainFrame, "Invalid address", "Error", JOptionPane.ERROR_MESSAGE);
                            System.out.println("Invalid address");
                        }
                    }
                }
            });
            serverLabels.add(serverLabel);
            contentPanel.add(serverLabel);
        }
        contentPanel.setVisible(true);
        this.setViewportView(contentPanel);
    }
}
