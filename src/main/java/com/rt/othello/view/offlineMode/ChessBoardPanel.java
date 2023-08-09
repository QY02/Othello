package com.rt.othello.view.offlineMode;

import com.rt.othello.ai.searcher.Minimax;
import com.rt.othello.components.offlineMode.ChessGridComponent;
import com.rt.othello.model.ChessPiece;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ChessBoardPanel extends JPanel {
    private final int CHESS_COUNT = 8;
    private ChessGridComponent[][] chessGrids;
    private int length;
    private boolean cheat = false;
    private OfflineModeFrame offlineModeFrame;
    private int blackPlayerType;
    private int whitePlayerType;
    private Minimax blackPlayerAi;
    private Minimax whitePlayerAi;

    public ChessBoardPanel(OfflineModeFrame offlineModeFrame, int width, int height, int blackPlayerType, int whitePlayerType, Minimax blackPlayerAi, Minimax whitePlayerAi) {
        this.offlineModeFrame = offlineModeFrame;
        this.setVisible(true);
        this.setFocusable(true);
        this.setLayout(null);
        this.setBackground(Color.BLACK);
        this.length = Math.min(width, height);
        this.setSize(length, length);
        ChessGridComponent.gridSize = length / CHESS_COUNT;
        ChessGridComponent.chessSize = (int) (ChessGridComponent.gridSize * 0.9);
        System.out.printf("width = %d height = %d gridSize = %d chessSize = %d\n",
                width, height, ChessGridComponent.gridSize, ChessGridComponent.chessSize);

        this.blackPlayerType = blackPlayerType;
        this.whitePlayerType = whitePlayerType;
        this.blackPlayerAi = blackPlayerAi;
        this.whitePlayerAi = whitePlayerAi;

        initialChessGrids();//return empty chessboard
        initialGame();//add initial four chess
        showTips(ChessPiece.BLACK);

        repaint();
    }

    public void updateSize(){
        this.length = this.getWidth();
        ChessGridComponent.gridSize = this.length / CHESS_COUNT;
        ChessGridComponent.chessSize = (int) (ChessGridComponent.gridSize * 0.9);
        for (int i = 0; i < CHESS_COUNT; i++) {
            for (int j = 0; j < CHESS_COUNT; j++) {
                chessGrids[i][j].updateSize();
                chessGrids[i][j].setLocation((this.length - 8 * ChessGridComponent.gridSize)/2 + j * (ChessGridComponent.gridSize), (this.length - 8 * ChessGridComponent.gridSize)/2 + i * (ChessGridComponent.gridSize));
            }
        }
    }

    /**
     * set an empty chessboard
     */
    public void initialChessGrids() {
        chessGrids = new ChessGridComponent[CHESS_COUNT][CHESS_COUNT];

        //draw all chess grids
        for (int i = 0; i < CHESS_COUNT; i++) {
            for (int j = 0; j < CHESS_COUNT; j++) {
                ChessGridComponent gridComponent = new ChessGridComponent(this, i, j);
                gridComponent.setLocation((length - 8 * ChessGridComponent.gridSize)/2 + j * (ChessGridComponent.gridSize), (length - 8 * ChessGridComponent.gridSize)/2 + i * (ChessGridComponent.gridSize));
                chessGrids[i][j] = gridComponent;
                this.add(chessGrids[i][j]);
            }
        }
    }

    /**
     * initial origin four chess
     */
    public void initialGame() {
        chessGrids[3][3].setChessPiece(ChessPiece.BLACK);
        chessGrids[3][4].setChessPiece(ChessPiece.WHITE);
        chessGrids[4][3].setChessPiece(ChessPiece.WHITE);
        chessGrids[4][4].setChessPiece(ChessPiece.BLACK);
    }

    public synchronized void aiMove() {
        while (((OfflineModeFrame.controller.getCurrentPlayer() == ChessPiece.BLACK) && (this.blackPlayerType != 0)) || ((OfflineModeFrame.controller.getCurrentPlayer() == ChessPiece.WHITE) && (this.whitePlayerType != 0))) {
            if (OfflineModeFrame.controller.getCurrentPlayer() == ChessPiece.BLACK) {
                int[] position = this.blackPlayerAi.search(getChessBoardData(), -1, -1, Integer.MIN_VALUE, Integer.MAX_VALUE);
                chessGrids[position[0]][position[1]].move();
            }
            else {
                int[] position = this.whitePlayerAi.search(getChessBoardData(), 1, -1, Integer.MIN_VALUE, Integer.MAX_VALUE);
                chessGrids[position[0]][position[1]].move();
            }
        }
    }

    public int[][] getChessBoardData() {
        int[][] chessBoardData = new int[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (this.chessGrids[i][j].getChessPiece() == ChessPiece.BLACK) {
                    chessBoardData[i][j] = -1;
                }
                else if (this.chessGrids[i][j].getChessPiece() == ChessPiece.WHITE) {
                    chessBoardData[i][j] = 1;
                }
                else {
                    chessBoardData[i][j] = 0;
                }
            }
        }
        return chessBoardData;
    }

    public void restart(){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if((!((i==3)&&(j==3)))&&(!((i==3)&&(j==4)))&&(!((i==4)&&(j==3)))&&(!((i==4)&&(j==4)))){
                    this.setChess(i,j,null);
                }
                else if (((i==3)&&(j==3))||((i==4)&&(j==4))){
                    this.setChess(i,j, ChessPiece.BLACK);
                }
                else if (((i==3)&&(j==4))||((i==4)&&(j==3))){
                    this.setChess(i,j, ChessPiece.WHITE);
                }
            }
        }
        showTips(ChessPiece.BLACK);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
    }

    public boolean  canClickGrid(int row, int col) {
        boolean can = false;
        if(this.cheat == false){
            if(chessGrids[row][col].getChessPiece() == ChessPiece.TIP ){
                can = true;
            }
        }
        else if(this.cheat == true){
            if((chessGrids[row][col].getChessPiece() == ChessPiece.TIP)||(chessGrids[row][col].getChessPiece() == null)){
                can = true;
            }
        }
        return can;
    }

    public void changeCheatMode(){
        if(this.cheat == false){
            this.cheat = true;
        }
        else if (this.cheat == true){
            this.cheat = false;
        }
    }

    public boolean isCheat() {
        return cheat;
    }

    public int getIntCheat(){
        int intCheat = 0;
        if(cheat == false){
            intCheat = 0;
        }
        else if(cheat == true){
            intCheat = 1;
        }
        return intCheat;
    }

    public boolean showTips(ChessPiece currentPlayer){
        boolean canShow = true;
        if(currentPlayer != null){
            ArrayList<int[]> tips = new ArrayList<>(0);
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    int[][] directions = new int[8][];
                    directions[0] = new int[]{0,1};//right
                    directions[1] = new int[]{0,-1};//left
                    directions[2] = new int[]{1,0};//down
                    directions[3] = new int[]{-1,0};//up
                    directions[4] = new int[]{1,1};//down and right
                    directions[5] = new int[]{-1,-1};//up and left
                    directions[6] = new int[]{1,-1};//down and left
                    directions[7] = new int[]{-1,1};//up and right
                    if(chessGrids[i][j].getChessPiece() == null ){
                        for (int k = 0; k < 8; k++) { //test 8 directions
                            int[] weizhi = new int[]{i,j};
                            while ((weizhi[0]<8)&&(weizhi[0]>=0)&&(weizhi[1]<8)&&(weizhi[1]>=0)){
                                weizhi[0] += directions[k][0];
                                weizhi[1] += directions[k][1];
                                if((weizhi[0]>=8)||(weizhi[0]<0)||(weizhi[1]>=8)||(weizhi[1]<0)){
                                    break;
                                }
                                else if(chessGrids[weizhi[0]][weizhi[1]].getChessPiece() == null){
                                    break;
                                }
                                else if((chessGrids[weizhi[0]][weizhi[1]].getChessPiece() != null)&&(chessGrids[weizhi[0]][weizhi[1]].getChessPiece() != currentPlayer)){
                                    continue;
                                }
                                else if((chessGrids[weizhi[0]][weizhi[1]].getChessPiece() == currentPlayer)&&((weizhi[0]!=(i+directions[k][0]))||(weizhi[1]!=(j+directions[k][1])))){
                                    if(tips.size() != 0){
                                        if((tips.get(tips.size()-1)[0] != i)||(tips.get(tips.size()-1)[1] != j)){
                                            tips.add(new int[]{i,j});
                                            System.out.println("tip"+i+","+j);
                                        }
                                    }
                                    else if(tips.size() == 0){
                                        tips.add(new int[]{i,j});
                                        System.out.println("tip"+i+","+j);
                                    }
                                    break;
                                }
                                else if((chessGrids[weizhi[0]][weizhi[1]].getChessPiece() == currentPlayer)&&(weizhi[0]==(i+directions[k][0]))&&(weizhi[1]==(j+directions[k][1]))){
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            for (int i = 0; i < tips.size(); i++) {
                if(this.cheat == false){
                    chessGrids[tips.get(i)[0]][tips.get(i)[1]].setChessPiece(ChessPiece.TIP);
                }
            }
            repaint();

            if(tips.size() == 0){
                canShow = false;
            }
            if(tips.size() > 0){
                canShow = true;
            }
        }
        return canShow;
    }

    public void removeTips(){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(chessGrids[i][j].getChessPiece() == ChessPiece.TIP){
                    chessGrids[i][j].setChessPiece(null);
                }
            }
        }
        repaint();
    }

    public void setChess(int row,int col,ChessPiece chessPiece) {
        this.chessGrids[row][col].setChessPiece(chessPiece);
        repaint();
    }

    public void flip(int row,int col,ChessPiece currentPlayer){
        int[][] directions = new int[8][];
        directions[0] = new int[]{0,1};//right
        directions[1] = new int[]{0,-1};//left
        directions[2] = new int[]{1,0};//down
        directions[3] = new int[]{-1,0};//up
        directions[4] = new int[]{1,1};//down and right
        directions[5] = new int[]{-1,-1};//up and left
        directions[6] = new int[]{1,-1};//down and left
        directions[7] = new int[]{-1,1};//up and right
        int[] direction_save = new int[2];
        int[] weizhi_save = new int[2];
        for (int i = 0; i < 8; i++) { //test 8 directions
            int[] weizhi = new int[]{row,col};
            while ((weizhi[0]<8)&&(weizhi[0]>=0)&&(weizhi[1]<8)&&(weizhi[1]>=0)){
                weizhi[0] += directions[i][0];
                weizhi[1] += directions[i][1];
                if((weizhi[0]>=8)||(weizhi[0]<0)||(weizhi[1]>=8)||(weizhi[1]<0)){
                    break;
                }
                else if(chessGrids[weizhi[0]][weizhi[1]].getChessPiece() == null){
                    break;
                }
                else if((chessGrids[weizhi[0]][weizhi[1]].getChessPiece() != null)&&(chessGrids[weizhi[0]][weizhi[1]].getChessPiece() != currentPlayer)){
                    continue;
                }
                else if((chessGrids[weizhi[0]][weizhi[1]].getChessPiece() == currentPlayer)&&((weizhi[0]!=(row+directions[i][0]))||(weizhi[1]!=(col+directions[i][1])))){
                    direction_save = new int[]{directions[i][0],directions[i][1]};
                    weizhi_save = new int[]{weizhi[0],weizhi[1]};
                    int[] weizhi1 = new int[]{row+direction_save[0],col+direction_save[1]};
                    while(!((weizhi1[0] == weizhi_save[0])&&(weizhi1[1] == weizhi_save[1]))){
                        this.chessGrids[weizhi1[0]][weizhi1[1]].setChessPiece(currentPlayer);
                        weizhi1[0] += direction_save[0];
                        weizhi1[1] += direction_save[1];
                    }
                    break;
                }
                else if((chessGrids[weizhi[0]][weizhi[1]].getChessPiece() == currentPlayer)&&(weizhi[0]==(row+directions[i][0]))&&(weizhi[1]==(col+directions[i][1]))){
                    break;
                }
            }
        }
        repaint();
    }

    public int[] countScore(){
        int[] scores = new int[2];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(this.chessGrids[i][j].getChessPiece() == ChessPiece.BLACK){
                    scores[0]++;
                }
                else if(this.chessGrids[i][j].getChessPiece() == ChessPiece.WHITE){
                    scores[1]++;
                }
            }
        }
        return scores;
    }

    public ChessGridComponent[][] getChessGrids() {
        return chessGrids;
    }

    public void randomColor(){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                this.chessGrids[i][j].randomColor();
            }
        }
        repaint();
    }

    public void defaultColor(){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                this.chessGrids[i][j].defaultColor();
            }
        }
        repaint();
    }

    public OfflineModeFrame getOfflineModeFrame() {
        return offlineModeFrame;
    }
}
