package com.rt.othello.SaveAndLoad;

import com.rt.othello.view.offlineMode.OfflineModeFrame;
import com.rt.othello.view.offlineMode.StatusPanel;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Load {

    private List<Step> stepList = new ArrayList<>(0);
    private List<ChessBoard> chessBoardList = new ArrayList<>(0);
    private int[][] initialChessBoard = new int[8][8];
    private int currentPlayer;
    private int stepNumber;

    public Load(){
        initialChessBoard[3][3] = -1;
        initialChessBoard[4][4] = -1;
        initialChessBoard[3][4] = 1;
        initialChessBoard[4][3] = 1;
    }

    public int loadFile(String fileName) throws IOException {
        int error = 0;
        FileReader fileReader = null;
        fileReader = new FileReader(fileName);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String check1 = bufferedReader.readLine();
        if (check1.equals("#CurrentPlayer") == false){
            error = 6;
            System.out.println("读取错误106");
        }
        if(error == 0){
            String currentPlayer = bufferedReader.readLine();
            if((currentPlayer.equals("1")==false)&&(currentPlayer.equals("-1")==false)&&(currentPlayer.equals("0")==false)){
                error = 3;
                System.out.println("读取错误103");
            }
            else {
                this.currentPlayer = Integer.parseInt(currentPlayer);
                System.out.println("currentPlayer is:"+this.currentPlayer);
            }
        }
        if(error == 0){
            String check2 = bufferedReader.readLine();
            if (check2.equals("#StepNumber") == false){
                error = 6;
                System.out.println("读取错误106");
            }
        }
        if(error == 0){
            String stepNumber = bufferedReader.readLine();
            if(isNumber(stepNumber)==false){
                error = 6;
                System.out.println("读取错误106");
            }
            else if(Integer.parseInt(stepNumber)>60){
                error = 6;
                System.out.println("读取错误106");
            }
            else{
                this.stepNumber = Integer.parseInt(stepNumber);
                System.out.println("stepNumber is:"+this.stepNumber);
            }
        }
        if(error == 0){
            String check3 = bufferedReader.readLine();
            if (check3.equals("#StepList") == false){
                error = 6;
                System.out.println("读取错误106");
            }
        }
        if(error == 0){
            for (int i = 0; i < this.stepNumber; i++) {
                String[] stepData = bufferedReader.readLine().split("\\s+");
                if(stepData.length!=5){
                    error = 6;
                    System.out.println("读取错误106");
                    break;
                }
                else if(checkStep(i,stepData) == false){
                    error = 6;
                    System.out.println("读取错误106");
                    break;
                }
                else if((Integer.parseInt(stepData[1])<0)||(Integer.parseInt(stepData[1])>=8)||(Integer.parseInt(stepData[2])<0)||(Integer.parseInt(stepData[2])>=8)){
                    error = 5;
                    System.out.println("读取错误105");
                    break;
                }
                Step step = new Step(Integer.parseInt(stepData[0]),Integer.parseInt(stepData[1]),Integer.parseInt(stepData[2]),Integer.parseInt(stepData[3]),Integer.parseInt(stepData[4]));
                stepList.add(step);
                System.out.println(stepList.toString());
            }
        }
        if(error == 0){
            String check4 = bufferedReader.readLine();
            if (check4.equals("#ChessBoardList") == false){
                error = 6;
                System.out.println("读取错误106");
            }
        }
        if(error == 0){
            for (int i = 0; i < this.stepNumber; i++) {
                String id = bufferedReader.readLine();
                if(isNumber(id) == false){
                    error = 1;
                    System.out.println("读取错误101");
                    break;
                }
                else if(Integer.parseInt(id)!=(i+1)){
                    error = 1;
                    System.out.println("读取错误101");
                    break;
                }
                String[] chessBoardData = new String[8];
                for (int j = 0; j < 8; j++) {
                    chessBoardData[j] = bufferedReader.readLine();
                }
                if(checkChessBoard(chessBoardData) == false){
                    error = 1;
                    System.out.println("读取错误101");
                    break;
                }
                if(checkChess(i,chessBoardData) == false){
                    error = 2;
                    System.out.println("读取错误102");
                    break;
                }
                ChessBoard chessBoard = new ChessBoard(Integer.parseInt(id),transformChessBoardData(chessBoardData));
                chessBoardList.add(chessBoard);
                System.out.println(chessBoardList.toString());
            }
        }
        if(error == 0){
            if(checkMove()==false){
                error = 5;
                System.out.println("读取错误105");
            }
        }
        return error;
    }

    public boolean isNumber(String str){
        boolean is = true;
        if(str.isEmpty() == true){
            is = false;
        }
        else if(str.charAt(0) == '-'){
            if(str.length() == 1){
                is = false;
            }
            else if(str.length() > 1){
                for (int i = 1; i < str.length(); i++) {
                    if(Character.isDigit(str.charAt(i)) == false){
                        is = false;
                    }
                }
            }
        }
        else if(str.charAt(0) != '-'){
            for (int i = 0; i < str.length(); i++) {
                if(Character.isDigit(str.charAt(i)) == false){
                    is = false;
                }
            }
        }
        return is;
    }

    public boolean checkStep(int count,String[] stepData){
        boolean check = true;
        if((isNumber(stepData[0]) == false)||isNumber(stepData[1]) == false||isNumber(stepData[2]) == false){
            check = false;
        }
        else if(Integer.parseInt(stepData[0])!=(count+1)){
            check = false;
        }
        if((stepData[3].equals("1")==false)&&(stepData[3].equals("-1")==false)){
            check = false;
        }
        if((stepData[4].equals("0")==false)&&(stepData[4].equals("1")==false)){
            check = false;
        }
        return check;
    }

    public boolean checkChessBoard(String[] chessBoardData){
        boolean check = true;
        for (int i = 0; i < 8; i++) {
            String[] chessData = chessBoardData[i].split("\\s+");
            if(chessData.length!=8){
                check = false;
            }
        }
        return check;
    }

    public boolean checkChess(int count,String[] chessBoardData){
        boolean check = true;
        String[] chessPiece = new String[3];
        for (int i = 0; i < 8; i++) {
            String[] chessData = chessBoardData[i].split("\\s+");
            for (int j = 0; j < 8; j++) {
                if((chessData[j].equals("0")==false)&&(chessData[j].equals("-1")==false)&&(chessData[j].equals("1")==false)){
                    check = false;
                }
                if(chessData[j].equals("0")){
                    chessPiece[0] = "0";
                }
                else if(chessData[j].equals("-1")){
                    chessPiece[1] = "-1";
                }
                else if(chessData[j].equals("1")){
                    chessPiece[2] = "1";
                }
            }
        }
        if(((chessPiece[0]==null)||(chessPiece[1]==null)||(chessPiece[2]==null))&&((count != (this.stepNumber-1))||(this.currentPlayer != 0))){
            check = false;
        }
        return check;
    }

    public int[][] transformChessBoardData(String[] chessBoardData){
        int[][] chessBoard = new int[8][8];
        for (int i = 0; i < 8; i++) {
            String[] chessData = chessBoardData[i].split("\\s+");
            for (int j = 0; j < 8; j++) {
                chessBoard[i][j] = Integer.parseInt(chessData[j]);
            }
        }
        return chessBoard;
    }

    public boolean checkMove(){
        boolean check = true;
        for (int i = 0; i < stepNumber; i++) {
            boolean check1 = false;
            if(this.stepList.get(i).getCheat()==0){
                if(i == 0){
                    ArrayList<int[]> canMoveGrids = canMove(initialChessBoard,this.stepList.get(i).getColor());
                    for (int j = 0; j < canMoveGrids.size(); j++) {
                        if((canMoveGrids.get(j)[0] == this.stepList.get(i).getRow())&&(canMoveGrids.get(j)[1] == this.stepList.get(i).getCol())){
                            check1 = true;
                        }
                    }
                }
                else if(i > 0){
                    ArrayList<int[]> canMoveGrids = canMove(this.chessBoardList.get(i-1).getChessBoardData(),this.stepList.get(i).getColor());
                    for (int j = 0; j < canMoveGrids.size(); j++) {
                        if((canMoveGrids.get(j)[0] == this.stepList.get(i).getRow())&&(canMoveGrids.get(j)[1] == this.stepList.get(i).getCol())){
                            check1 = true;
                        }
                    }
                }
            }
            else if(this.stepList.get(i).getCheat()==1){
                if(i == 0){
                    if(initialChessBoard[this.stepList.get(i).getRow()][this.stepList.get(i).getCol()]==0){
                        check1 = true;
                    }
                }
                else if (i > 0){
                    if(this.chessBoardList.get(i-1).getChessBoardData()[this.stepList.get(i).getRow()][this.stepList.get(i).getCol()]==0){
                        check1 = true;
                    }
                }
            }
            if(check1 == false){
                check = false;
                break;
            }
        }
        return check;
    }

    public ArrayList<int[]> canMove(int[][] chessBoardData,int currentPlayer){
        ArrayList<int[]> canMoveGrids = new ArrayList<>(0);
        int[][] directions = new int[8][];
        directions[0] = new int[]{0,1};//right
        directions[1] = new int[]{0,-1};//left
        directions[2] = new int[]{1,0};//down
        directions[3] = new int[]{-1,0};//up
        directions[4] = new int[]{1,1};//down and right
        directions[5] = new int[]{-1,-1};//up and left
        directions[6] = new int[]{1,-1};//down and left
        directions[7] = new int[]{-1,1};//up and right
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(chessBoardData[i][j] == 0 ){
                    for (int k = 0; k < 8; k++) { //test 8 directions
                        int[] weizhi = new int[]{i,j};
                        while ((weizhi[0]<8)&&(weizhi[0]>=0)&&(weizhi[1]<8)&&(weizhi[1]>=0)){
                            weizhi[0] += directions[k][0];
                            weizhi[1] += directions[k][1];
                            if((weizhi[0]>=8)||(weizhi[0]<0)||(weizhi[1]>=8)||(weizhi[1]<0)){
                                break;
                            }
                            else if(chessBoardData[weizhi[0]][weizhi[1]] == 0){
                                break;
                            }
                            else if((chessBoardData[weizhi[0]][weizhi[1]] != 0)&&(chessBoardData[weizhi[0]][weizhi[1]] != currentPlayer)){
                                continue;
                            }
                            else if((chessBoardData[weizhi[0]][weizhi[1]] == currentPlayer)&&((weizhi[0]!=(i+directions[k][0]))||(weizhi[1]!=(j+directions[k][1])))){
                                if(canMoveGrids.size() != 0){
                                    if((canMoveGrids.get(canMoveGrids.size()-1)[0] != i)||(canMoveGrids.get(canMoveGrids.size()-1)[1] != j)){
                                        canMoveGrids.add(new int[]{i,j});
                                        System.out.println("canMove"+i+","+j);
                                    }
                                }
                                else if(canMoveGrids.size() == 0){
                                    canMoveGrids.add(new int[]{i,j});
                                    System.out.println("canMove"+i+","+j);
                                }
                                break;
                            }
                            else if((chessBoardData[weizhi[0]][weizhi[1]] == currentPlayer)&&(weizhi[0]==(i+directions[k][0]))&&(weizhi[1]==(j+directions[k][1]))){
                                break;
                            }
                        }
                    }
                }
            }
        }
        System.out.println();
        return canMoveGrids;
    }

    public void showError(int error){
        if(error == 1){
            JOptionPane.showMessageDialog(null, "棋盘错误 错误代码101","读取错误",JOptionPane.ERROR_MESSAGE);
        }
        else if(error == 2){
            JOptionPane.showMessageDialog(null, "棋子错误 错误代码102","读取错误",JOptionPane.ERROR_MESSAGE);
        }
        else if(error == 3){
            JOptionPane.showMessageDialog(null, "缺少行棋方 错误代码103","读取错误",JOptionPane.ERROR_MESSAGE);
        }
        else if(error == 4){
            JOptionPane.showMessageDialog(null, "文件格式错误 错误代码104","读取错误",JOptionPane.ERROR_MESSAGE);
        }
        else if(error == 5){
            JOptionPane.showMessageDialog(null, "非法落子 错误代码105","读取错误",JOptionPane.ERROR_MESSAGE);
        }
        else if(error == 6){
            JOptionPane.showMessageDialog(null, "其他错误 错误代码106","读取错误",JOptionPane.ERROR_MESSAGE);
        }
    }

    public void loadGame(){
        OfflineModeFrame.save.setStepList(this.stepList);
        OfflineModeFrame.save.setChessBoardList(this.chessBoardList);
        OfflineModeFrame.controller.loadId(this.stepNumber);
        OfflineModeFrame.controller.loadChessBoard(chessBoardList.get(chessBoardList.size()-1).getChessBoardData());
        OfflineModeFrame.controller.loadScore();
        StatusPanel.setValid();
        OfflineModeFrame.controller.loadCurrentPlayer(currentPlayer);
        OfflineModeFrame.controller.loadTips();
    }
}
