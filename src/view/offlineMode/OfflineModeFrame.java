package view.offlineMode;


import SaveAndLoad.Save;
import Sound.ThreadedSound;
import controller.offlineMode.GameController;
import view.BackGroundPanel;
import view.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class OfflineModeFrame extends JFrame {
    public static GameController controller;
    public static Save save;
    private ChessBoardPanel chessBoardPanel;
    private StatusPanel statusPanel;
    private BackGroundPanel backGroundPanel;

    private OfflineModeFrame thisFrame = this;

    private ThreadedSound bgm;

    private int contentHeight;
    private int contentWidth;

    private int minSize;

    private int chessBoardPanelSize;

    private int menubarHeight;

    public OfflineModeFrame(int width, int height, MainFrame mainFrame) {

        this.setTitle("Othello - OfflineMode");
        ImageIcon icon = new ImageIcon(this.getClass().getResource("/res/icon.png"));
        this.setIconImage(icon.getImage());
        this.setLayout(null);

        this.setSize(width, height);

        this.setLocationRelativeTo(null);

        bgm = new ThreadedSound("/res/bgm1.wav", true, 1000);
        addMenuBar();

        this.setVisible(true);
        Insets inset = this.getInsets();
        menubarHeight = this.getJMenuBar().getHeight();
        contentHeight = this.getHeight() - inset.top - inset.bottom - menubarHeight;
        contentWidth = this.getWidth() - inset.left - inset.right;
        minSize = contentWidth < contentHeight ? contentWidth:contentHeight;
        int temp = (int)(minSize * 0.75);
        chessBoardPanelSize = (temp % 2) == 0 ? temp : (temp + 1);

        chessBoardPanel = new ChessBoardPanel(chessBoardPanelSize, chessBoardPanelSize);
        chessBoardPanel.setLocation((contentWidth - chessBoardPanel.getWidth()) / 2, (contentHeight - chessBoardPanel.getHeight()) / 3);

        statusPanel = new StatusPanel(this.chessBoardPanel.getWidth(), this.chessBoardPanel.getY());
        statusPanel.setLocation(this.chessBoardPanel.getX(), 0);
        save = new Save(chessBoardPanel);
        controller = new GameController(chessBoardPanel, statusPanel, save);
        backGroundPanel = new BackGroundPanel(contentWidth,contentHeight);

        this.add(chessBoardPanel);
        this.add(statusPanel);

        JButton restartBtn = new JButton("Restart");
        restartBtn.setSize((int)(contentWidth * 0.15), (int)(contentHeight * 0.06));
        restartBtn.setLocation((contentWidth - 4 * restartBtn.getWidth() - (int)(3 * contentWidth * 0.08)) / 2, (contentHeight + chessBoardPanel.getHeight()) / 2);
        restartBtn.setFont(new Font("Calibri", Font.BOLD, Math.min((int)(restartBtn.getHeight() * 0.5), (int)(restartBtn.getWidth() / 6))));
        restartBtn.setMargin(new Insets(0, 0, 0, 0));
        add(restartBtn);
        restartBtn.addActionListener(e -> {
            System.out.println("click restart Btn");
            int confirm = JOptionPane.showConfirmDialog(this,"是否重新开始？","restart",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
            if(confirm == 0){
                this.chessBoardPanel.restart();
                this.controller.restart();
                this.save.restart();
            }
        });

        JButton loadGameBtn = new JButton("Load");
        loadGameBtn.setSize((int)(contentWidth * 0.15), (int)(contentHeight * 0.06));
        loadGameBtn.setLocation(restartBtn.getX()+restartBtn.getWidth()+(int)(contentWidth * 0.08), restartBtn.getY());
        loadGameBtn.setFont(new Font("Calibri", Font.BOLD, Math.min((int)(loadGameBtn.getHeight() * 0.5), (int)(loadGameBtn.getWidth() / 6))));
        loadGameBtn.setMargin(new Insets(0, 0, 0, 0));
        add(loadGameBtn);
        loadGameBtn.addActionListener(e -> {
            System.out.println("clicked Load Btn");
            ImageIcon loadIcon = new ImageIcon(this.getClass().getResource("/res/loadIcon.png"));
            loadIcon.setImage(loadIcon.getImage().getScaledInstance((int)(this.getWidth() * 0.114),(int)(this.getWidth() * 0.1),Image.SCALE_DEFAULT));
            String filePath = (String) JOptionPane.showInputDialog(this, "input the path here","Load",JOptionPane.QUESTION_MESSAGE,loadIcon,null,null);
            if(filePath != null){
                controller.readFileData(filePath);
            }
        });

        JButton saveGameBtn = new JButton("Save");
        saveGameBtn.setSize((int)(contentWidth * 0.15), (int)(contentHeight * 0.06));
        saveGameBtn.setLocation(loadGameBtn.getX()+restartBtn.getWidth()+(int)(contentWidth * 0.08), restartBtn.getY());
        saveGameBtn.setFont(new Font("Calibri", Font.BOLD, Math.min((int)(saveGameBtn.getHeight() * 0.5), (int)(saveGameBtn.getWidth() / 6))));
        saveGameBtn.setMargin(new Insets(0, 0, 0, 0));
        add(saveGameBtn);
        saveGameBtn.addActionListener(e -> {
            System.out.println("clicked Save Btn");
            ImageIcon saveIcon = new ImageIcon(this.getClass().getResource("/res/saveIcon.png"));
            saveIcon.setImage(saveIcon.getImage().getScaledInstance((int)(this.getWidth() * 0.1),(int)(this.getWidth() * 0.1),Image.SCALE_DEFAULT));
            String filePath = (String) JOptionPane.showInputDialog(this, "input the path here","Save",JOptionPane.QUESTION_MESSAGE,saveIcon,null,null);
            if(filePath != null){
                controller.writeDataToFile(filePath);
            }
        });

        JButton cheatBtn = new JButton("cheat:off");
        cheatBtn.setSize((int)(contentWidth * 0.15), (int)(contentHeight * 0.06));
        cheatBtn.setLocation(saveGameBtn.getX()+restartBtn.getWidth()+(int)(contentWidth * 0.08), restartBtn.getY());
        cheatBtn.setFont(new Font("Calibri", Font.BOLD, Math.min((int)(cheatBtn.getHeight() * 0.5), (int)(cheatBtn.getWidth() / 6))));
        cheatBtn.setMargin(new Insets(0, 0, 0, 0));
        add(cheatBtn);
        cheatBtn.addActionListener(e -> {
            System.out.println("change cheat mode");
            chessBoardPanel.changeCheatMode();
            if(chessBoardPanel.isCheat() == true){
                cheatBtn.setText("cheat:on");
                chessBoardPanel.removeTips();
            }
            else if(chessBoardPanel.isCheat() == false){
                cheatBtn.setText("cheat:off");
                chessBoardPanel.showTips(controller.getCurrentPlayer());
            }
        });



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
                bgm.pause();
                thisFrame.setVisible(false);
                mainFrame.setVisible(true);
                mainFrame.playBgm();
            }
        });

        this.addComponentListener(new ComponentAdapter() {//拖动窗口监听
            public void componentResized(ComponentEvent e) {
                contentHeight = thisFrame.getHeight() - inset.top - inset.bottom - menubarHeight;
                contentWidth = thisFrame.getWidth() - inset.left - inset.right;
                minSize = contentWidth < contentHeight ? contentWidth:contentHeight;
                int temp = (int)(minSize * 0.75);
                chessBoardPanelSize = (temp % 2) == 0 ? temp : (temp + 1);
                chessBoardPanel.setSize(chessBoardPanelSize, chessBoardPanelSize);
                chessBoardPanel.setLocation((contentWidth - chessBoardPanel.getWidth()) / 2, (contentHeight - chessBoardPanel.getHeight()) / 3);
                chessBoardPanel.updateSize();
                statusPanel.setSize(chessBoardPanel.getWidth(), chessBoardPanel.getY());
                statusPanel.setLocation(chessBoardPanel.getX(), 0);
                statusPanel.updateSize();
                backGroundPanel.setSize(contentWidth,contentHeight);
                restartBtn.setSize((int)(contentWidth * 0.15), (int)(contentHeight * 0.06));
                restartBtn.setLocation((contentWidth - 4 * restartBtn.getWidth() - (int)(3 * contentWidth * 0.08)) / 2, (contentHeight + chessBoardPanel.getHeight()) / 2);
                restartBtn.setFont(new Font("Calibri", Font.BOLD, Math.min((int)(restartBtn.getHeight() * 0.5), (int)(restartBtn.getWidth() / 6))));
                loadGameBtn.setSize((int)(contentWidth * 0.15), (int)(contentHeight * 0.06));
                loadGameBtn.setLocation(restartBtn.getX()+restartBtn.getWidth()+(int)(contentWidth * 0.08), restartBtn.getY());
                loadGameBtn.setFont(new Font("Calibri", Font.BOLD, Math.min((int)(loadGameBtn.getHeight() * 0.5), (int)(loadGameBtn.getWidth() / 6))));
                saveGameBtn.setSize((int)(contentWidth * 0.15), (int)(contentHeight * 0.06));
                saveGameBtn.setLocation(loadGameBtn.getX()+restartBtn.getWidth()+(int)(contentWidth * 0.08), restartBtn.getY());
                saveGameBtn.setFont(new Font("Calibri", Font.BOLD, Math.min((int)(saveGameBtn.getHeight() * 0.5), (int)(saveGameBtn.getWidth() / 6))));
                cheatBtn.setSize((int)(contentWidth * 0.15), (int)(contentHeight * 0.06));
                cheatBtn.setLocation(saveGameBtn.getX()+restartBtn.getWidth()+(int)(contentWidth * 0.08), restartBtn.getY());
                cheatBtn.setFont(new Font("Calibri", Font.BOLD, Math.min((int)(cheatBtn.getHeight() * 0.5), (int)(cheatBtn.getWidth() / 6))));
            }

        });

    }

    public void addMenuBar(){
        ImageIcon undoIcon = new ImageIcon(this.getClass().getResource("/res/undo.png"));
        undoIcon.setImage(undoIcon.getImage().getScaledInstance((int)(this.getWidth() * 0.06), (int)(this.getWidth() * 0.045),Image.SCALE_DEFAULT));
        JMenuItem undo = new JMenuItem(undoIcon){
            @Override
            public Dimension getMaximumSize() {
                Dimension d1 = super.getPreferredSize();
                Dimension d2 = super.getMaximumSize();
                d2.width = d1.width;
                return d2;
            }
        };

        undo.addActionListener(e -> {
            System.out.println("clicked Undo Btn");
            if(controller.getId() > 1){
                this.controller.undo();
                this.save.undo();
            }
        });


        JMenuBar menuBar=new JMenuBar();
        JMenu menu=new JMenu("菜单");
        JMenuItem profile=new JMenuItem("简介");
        JMenuItem instruction=new JMenuItem("说明");
        JMenuItem play=new JMenuItem("开始播放");
        JMenuItem stop=new JMenuItem("停止播放");
        JMenu color = new JMenu("颜色");
        JMenuItem randomColor=new JMenuItem("随机颜色");
        JMenuItem mr=new JMenuItem("默认");
        this.setJMenuBar(menuBar);

        menuBar.add(menu);
        menuBar.add(color);
        menu.add(profile);
        menu.add(instruction);
        menu.add(play);
        menu.add(stop);
        menuBar.add(undo);
        menuBar.setVisible(true);
        color.add(randomColor);
        color.add(mr);
        ImageIcon info = new ImageIcon(this.getClass().getResource("/res/info.png"));
        info.setImage(info.getImage().getScaledInstance((int)(this.getWidth() * 0.5), (int)(this.getWidth() * 0.3),Image.SCALE_DEFAULT));
        instruction.addActionListener(e -> {

            System.out.println("弹出指南");
            JOptionPane.showMessageDialog(this, "    如果玩家在棋盘上没有地方可以下子，则该玩家对手可以连下。双方都没有棋子可以下时棋局结束，以棋子数目来计算胜负，棋子多的一方获胜。\n" +
                    "在棋盘还没有下满时，如果一方的棋子已经被对方吃光，则棋局也结束。将对手棋子吃光的一方获胜。\n" +
                    "翻转棋类似于棋盘游戏“奥赛罗 (Othello)”，是一种得分会戏剧性变化并且需要长时间思考的策略性游戏。\n" +
                    "翻转棋的棋盘上有 64 个可以放置黑白棋子的方格（类似于国际象棋和跳棋）。游戏的目标是使棋盘上自己颜色的棋子数超过对手的棋子数。\n" +
                    "该游戏非常复杂，其名称就暗示着结果的好坏可能会迅速变化。\n" +
                    "当游戏双方都不能再按规则落子时，游戏就结束了。通常，游戏结束时棋盘上会摆满了棋子。结束时谁的棋子最多谁就是赢家。","操作说明",JOptionPane.WARNING_MESSAGE,info);
        });
        profile.addActionListener(e -> {

            System.out.println("弹出简介");

            JOptionPane.showMessageDialog(this, "    黑白棋，又叫翻转棋（Reversi）、奥赛罗棋（Othello）、苹果棋或正反棋（Anti reversi）。\n黑白棋在西方和日本很流行。它的游戏规则简单，因此上手很容易，但是它的变化又非常复杂。\n"
                    +"    黑白棋是19世纪末英国人发明的。直到上个世纪70年代日本人长谷川五郎将其进行发展和推广，"
                    +"\n借用莎士比亚名剧奥赛罗（othello)为这个游戏重新命名(日语“オセロ”），也就是大家玩的黑白棋。"
                    +"\n为何借用莎士比亚名剧呢?是因为奥赛罗是莎士比亚一个名剧的男主角。他是一个黑人，"
                    +"\n妻子是白人，因受小人挑拨，怀疑妻子不忠一直情海翻波，最终亲手把妻子杀死。后来真相大白，"
                    +"\n奥赛罗懊悔不已，自杀而死。黑白棋就是借用这个黑人白人斗争的故事而命名。","简介",JOptionPane.WARNING_MESSAGE,info);
        });
        stop.addActionListener(e -> {

            System.out.println("stop");
            bgm.pause();
        });
        play.addActionListener(e -> {

            System.out.println("play");
            bgm.play();
        });

        randomColor.addActionListener(e -> {
            System.out.println("random color");
            this.chessBoardPanel.randomColor();
        });

        mr.addActionListener(e -> {
            System.out.println("default color");
            this.chessBoardPanel.defaultColor();
        });
    }

    public void playBgm() {
        bgm.play();
    }
}
