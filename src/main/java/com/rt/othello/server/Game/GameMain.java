package com.rt.othello.server.Game;

import java.util.ArrayList;
import java.util.List;

public class GameMain {

    private List<Step> stepList = new ArrayList<>(0);
    private List<ChessBoard> chessBoardList = new ArrayList<>(0);
    private int[][] currentChessBoard = new int[8][8];
    private int[][] currentChessBoardWithTips = new int[8][8];
    private int currentPlayer;
    private int stepNumber;
    private int blackScore;
    private int whiteScore;
    private boolean end;

    public GameMain(){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                currentChessBoard[i][j] = 0;
            }
        }
        currentChessBoard[3][3] = -1;
        currentChessBoard[3][4] = 1;
        currentChessBoard[4][3] = 1;
        currentChessBoard[4][4] = -1;
        countScore();
        stepNumber = 0;
        currentPlayer = -1;
        end = false;
        showTips();
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public int[][] getCurrentChessBoard() {
        return currentChessBoard;
    }

    public int[][] getCurrentChessBoardWithTips() {
        return currentChessBoardWithTips;
    }

    public String getChessBoardData(){
        String chessBoardData = "";
        for (int i = 0; i < 8; i++) {
            chessBoardData = chessBoardData.concat(String.format("%d %d %d %d %d %d %d %d\n",currentChessBoardWithTips[i][0],currentChessBoardWithTips[i][1],currentChessBoardWithTips[i][2],currentChessBoardWithTips[i][3],currentChessBoardWithTips[i][4],currentChessBoardWithTips[i][5],currentChessBoardWithTips[i][6],currentChessBoardWithTips[i][7]));
        }
        return chessBoardData;
    }

    public int place(int row, int col){
        currentChessBoard[row][col] = currentPlayer;
        flip(row, col);
        countScore();
        save(row, col);
        swapPlayer();
        if (showTips()) {
            stepNumber++;
            return 0;
        }
        else {
            swapPlayer();
            if (showTips()) {
                stepNumber++;
                return 1;
            }
            else {
                stepNumber++;
                end = true;
                return 2;
            }
        }
    }

    public void swapPlayer(){
        if (currentPlayer == -1){
            currentPlayer = 1;
        }
        else {
            currentPlayer = -1;
        }
    }

    private void countScore() {
        blackScore = 0;
        whiteScore = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (currentChessBoard[i][j] == -1) {
                    blackScore++;
                }
                else if(currentChessBoard[i][j] == 1) {
                    whiteScore++;
                }
            }
        }
    }

    public int chooseWinner() {
        if (blackScore > whiteScore) {
            return -1;
        }
        else if (blackScore == whiteScore) {
            return 0;
        }
        else {
            return 1;
        }
    }

    public void flip(int row, int col){
        int[][] directions = new int[8][];
        directions[0] = new int[]{0,1};//right
        directions[1] = new int[]{0,-1};//left
        directions[2] = new int[]{1,0};//down
        directions[3] = new int[]{-1,0};//up
        directions[4] = new int[]{1,1};//down and right
        directions[5] = new int[]{-1,-1};//up and left
        directions[6] = new int[]{1,-1};//down and left
        directions[7] = new int[]{-1,1};//up and right
        int[] direction_save;
        int[] weizhi_save;
        for (int i = 0; i < 8; i++) { //test 8 directions
            int[] weizhi = new int[]{row,col};
            while ((weizhi[0]<8)&&(weizhi[0]>=0)&&(weizhi[1]<8)&&(weizhi[1]>=0)){
                weizhi[0] += directions[i][0];
                weizhi[1] += directions[i][1];
                if((weizhi[0]>=8)||(weizhi[0]<0)||(weizhi[1]>=8)||(weizhi[1]<0)){
                    break;
                }
                else if(currentChessBoard[weizhi[0]][weizhi[1]] == 0){
                    break;
                }
                else if((currentChessBoard[weizhi[0]][weizhi[1]] != 0)&&(currentChessBoard[weizhi[0]][weizhi[1]] != currentPlayer)){
                    continue;
                }
                else if((currentChessBoard[weizhi[0]][weizhi[1]] == currentPlayer)&&((weizhi[0]!=(row+directions[i][0]))||(weizhi[1]!=(col+directions[i][1])))){
                    direction_save = new int[]{directions[i][0],directions[i][1]};
                    weizhi_save = new int[]{weizhi[0],weizhi[1]};
                    int[] weizhi1 = new int[]{row+direction_save[0],col+direction_save[1]};
                    while(!((weizhi1[0] == weizhi_save[0])&&(weizhi1[1] == weizhi_save[1]))){
                        this.currentChessBoard[weizhi1[0]][weizhi1[1]] = currentPlayer;
                        weizhi1[0] += direction_save[0];
                        weizhi1[1] += direction_save[1];
                    }
                    break;
                }
                else if((currentChessBoard[weizhi[0]][weizhi[1]] == currentPlayer)&&(weizhi[0]==(row+directions[i][0]))&&(weizhi[1]==(col+directions[i][1]))){
                    break;
                }
            }
        }
    }

    public boolean showTips(){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                currentChessBoardWithTips[i][j] = currentChessBoard[i][j];
            }
        }
        boolean canShow = false;
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
                if(currentChessBoard[i][j] == 0){
                    for (int k = 0; k < 8; k++) { //test 8 directions
                        int[] weizhi = new int[]{i,j};
                        while ((weizhi[0]<8)&&(weizhi[0]>=0)&&(weizhi[1]<8)&&(weizhi[1]>=0)){
                            weizhi[0] += directions[k][0];
                            weizhi[1] += directions[k][1];
                            if((weizhi[0]>=8)||(weizhi[0]<0)||(weizhi[1]>=8)||(weizhi[1]<0)){
                                break;
                            }
                            else if(currentChessBoard[weizhi[0]][weizhi[1]] == 0){
                                break;
                            }
                            else if((currentChessBoard[weizhi[0]][weizhi[1]] != 0)&&(currentChessBoard[weizhi[0]][weizhi[1]] != currentPlayer)){
                                continue;
                            }
                            else if((currentChessBoard[weizhi[0]][weizhi[1]] == currentPlayer)&&((weizhi[0]!=(i+directions[k][0]))||(weizhi[1]!=(j+directions[k][1])))){
                                if(currentChessBoardWithTips[i][j] == 0){
                                    currentChessBoardWithTips[i][j] = 2;
                                    if (canShow == false) {
                                        canShow = true;
                                    }
                                    System.out.println("tip"+i+","+j);
                                }
                                break;
                            }
                            else if((currentChessBoard[weizhi[0]][weizhi[1]] == currentPlayer)&&(weizhi[0]==(i+directions[k][0]))&&(weizhi[1]==(j+directions[k][1]))){
                                break;
                            }
                        }
                    }
                }
            }
        }
        return canShow;
    }

    public void save(int row, int col){
        Step step = new Step(stepNumber, row, col, currentPlayer, 0);
        ChessBoard chessBoard = new ChessBoard(stepNumber, currentChessBoard);
        stepList.add(step);
        chessBoardList.add(chessBoard);
    }

    public int getBlackScore() {
        return blackScore;
    }

    public int getWhiteScore() {
        return whiteScore;
    }
}