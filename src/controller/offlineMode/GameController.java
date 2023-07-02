package controller.offlineMode;

import SaveAndLoad.Load;
import SaveAndLoad.Save;
import model.ChessPiece;
import view.offlineMode.ChessBoardPanel;
import view.offlineMode.OfflineModeFrame;
import view.offlineMode.StatusPanel;

import java.io.*;


public class GameController {


    private ChessBoardPanel gamePanel;
    private StatusPanel statusPanel;
    private ChessPiece currentPlayer;
    private Save save;
    private int blackScore;
    private int whiteScore;
    private int id;
    private boolean end = false;
    private int blackPlayerType;
    private int whitePlayerType;

    public GameController(ChessBoardPanel gamePanel, StatusPanel statusPanel,Save save, int blackPlayerType, int whitePlayerType) {
        this.gamePanel = gamePanel;
        this.statusPanel = statusPanel;
        this.currentPlayer = ChessPiece.BLACK;
        this.save = save;

        this.blackPlayerType = blackPlayerType;
        this.whitePlayerType = whitePlayerType;

        blackScore = 2;
        whiteScore = 2;
        id = 1;
    }

    public void swapPlayer() {
        countScore();
        currentPlayer = (currentPlayer == ChessPiece.BLACK) ? ChessPiece.WHITE : ChessPiece.BLACK;
        statusPanel.setPlayerText(currentPlayer.name());
        statusPanel.setScoreText(blackScore, whiteScore);
    }


    public void countScore() {
        //todo: modify the countScore method
        this.blackScore = this.gamePanel.countScore()[0];
        this.whiteScore = this.gamePanel.countScore()[1];
    }


    public ChessPiece getCurrentPlayer() {
        return currentPlayer;
    }

    public ChessBoardPanel getGamePanel() {
        return gamePanel;
    }


    public void setGamePanel(ChessBoardPanel gamePanel) {
        this.gamePanel = gamePanel;
    }


    public void readFileData(String fileName) {
        Load saveData = new Load();
        int error = 0;
        if(fileName.length() < 4){
            error = 4;
            System.out.println("读取错误104");
            saveData.showError(error);
        }
        else if(fileName.substring(fileName.length()-3).equals("txt")==false){
            error = 4;
            System.out.println("读取错误104");
            saveData.showError(error);
        }
        else{
            try {
                error = saveData.loadFile(fileName);
                if (error != 0){
                    saveData.showError(error);
                }
                else if(error == 0){
                    saveData.loadGame();
                }
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }
    }

    public void writeDataToFile(String path) {
        try {
            FileWriter fileWriter = new FileWriter(path);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            writer.write("#CurrentPlayer\n");
            if(end == false){
                if(currentPlayer == ChessPiece.BLACK){
                    writer.write("-1\n");
                }
                if(currentPlayer == ChessPiece.WHITE){
                    writer.write("1\n");
                }
            }
            else if(end == true){
                writer.write("0\n");
            }
            writer.write("#StepNumber\n");
            writer.write((id-1)+"\n");
            writer.write("#StepList\n");
            for (int i = 0; i < save.getStepList().size(); i++) {
                writer.write(save.getStepList().get(i).write()+"\n");
            }
            if(id == 1){
                writer.write("#ChessBoardList");
            }
            else if(id > 1){
                writer.write("#ChessBoardList\n");
            }
            for (int i = 0; i < save.getChessBoardList().size(); i++) {
                if(i != save.getChessBoardList().size()-1){
                    writer.write(save.getChessBoardList().get(i).write());
                }
                else if(i == save.getChessBoardList().size()-1){
                    writer.write(save.getChessBoardList().get(i).write().substring(0,save.getChessBoardList().get(i).write().length()-1));
                }
            }
            writer.close();
            fileWriter.close();
        } catch (IOException e) {
            //e.printStackTrace();
        }
        //todo: write data into file
    }

    public boolean canClick(int row, int col) {
        return gamePanel.canClickGrid(row, col);
    }

    public void flip(int row,int col,ChessPiece currentPlayer){
        gamePanel.flip(row,col,currentPlayer);
    }

    public boolean showTip(ChessPiece currentPlayer){
        return gamePanel.showTips(currentPlayer);
    }

    public void removeTips(){
        gamePanel.removeTips();
    }

    public void idIncrease(){
        this.id++;
        System.out.println("current id:"+this.id);
    }

    public int getId(){
        return this.id;
    }

    public void restart(){
        this.blackScore = 2;
        this.whiteScore = 2;
        this.currentPlayer = ChessPiece.BLACK;
        this.id = 1;
        this.end = false;
        statusPanel.setPlayerText(currentPlayer.name());
        statusPanel.setScoreText(blackScore, whiteScore);
        statusPanel.setValid();
    }

    public void undo(){
        id--;
        if(this.end == true){
            this.end = false;
        }
        statusPanel.setValid();
        if(OfflineModeFrame.save.getStepList().get(OfflineModeFrame.save.getStepList().size()-1).getColor()==-1){
            this.currentPlayer = ChessPiece.BLACK;
        }
        else if(OfflineModeFrame.save.getStepList().get(OfflineModeFrame.save.getStepList().size()-1).getColor()==1){
            this.currentPlayer = ChessPiece.WHITE;
        }
        statusPanel.setPlayerText(currentPlayer.name());
        if(OfflineModeFrame.save.getChessBoardList().size() >= 2){
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    ChessPiece chessPiece = null;
                    int chessPieceData = OfflineModeFrame.save.getChessBoardList().get(OfflineModeFrame.save.getChessBoardList().size()-2).getChessBoardData()[i][j];
                    if(chessPieceData == 0){
                        chessPiece = null;
                    }
                    else if(chessPieceData == -1){
                        chessPiece = ChessPiece.BLACK;
                    }
                    else if (chessPieceData == 1){
                        chessPiece = ChessPiece.WHITE;
                    }
                    gamePanel.setChess(i,j,chessPiece);
                }
            }
        }
        else if(OfflineModeFrame.save.getChessBoardList().size() == 1){
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if((!((i==3)&&(j==3)))&&(!((i==3)&&(j==4)))&&(!((i==4)&&(j==3)))&&(!((i==4)&&(j==4)))){
                        gamePanel.setChess(i,j,null);
                    }
                    else if (((i==3)&&(j==3))||((i==4)&&(j==4))){
                        gamePanel.setChess(i,j, ChessPiece.BLACK);
                    }
                    else if (((i==3)&&(j==4))||((i==4)&&(j==3))){
                        gamePanel.setChess(i,j, ChessPiece.WHITE);
                    }
                }
            }
        }
        countScore();
        statusPanel.setScoreText(blackScore, whiteScore);
        showTip(currentPlayer);
    }

    public void restartChessBoard(){
        gamePanel.restart();
    }

    public int getBlackScore() {
        return blackScore;
    }

    public int getWhiteScore() {
        return whiteScore;
    }

    public void loadId(int stepNumber){
        this.id = stepNumber + 1;
        System.out.println("current id:"+this.id);
    }

    public void loadChessBoard(int[][] chessBoardData){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPiece chessPiece = null;
                if(chessBoardData[i][j] == 0){
                    chessPiece = null;
                }
                else if(chessBoardData[i][j] == -1){
                    chessPiece = ChessPiece.BLACK;
                }
                else if(chessBoardData[i][j] == 1){
                    chessPiece = ChessPiece.WHITE;
                }
                gamePanel.setChess(i,j,chessPiece);
            }
        }
    }

    public void loadScore(){
        countScore();
        statusPanel.setScoreText(blackScore, whiteScore);
    }

    public void loadCurrentPlayer(int currentPlayer) {
        if(currentPlayer == -1){
            end = false;
            this.currentPlayer = ChessPiece.BLACK;
            statusPanel.setPlayerText(this.currentPlayer.name());
        }
        else if(currentPlayer == 1){
            end = false;
            this.currentPlayer = ChessPiece.WHITE;
            statusPanel.setPlayerText(this.currentPlayer.name());
        }
        else if(currentPlayer == 0){
            end();
        }
    }

    public void loadTips(){
        gamePanel.showTips(this.currentPlayer);
    }

    public void end(){
        this.end = true;
        this.currentPlayer = null;
        statusPanel.setEnd();
    }

    public int getBlackPlayerType() {
        return blackPlayerType;
    }

    public int getWhitePlayerType() {
        return whitePlayerType;
    }
}
