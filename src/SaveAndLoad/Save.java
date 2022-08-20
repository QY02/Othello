package SaveAndLoad;

import components.offlineMode.ChessGridComponent;
import model.ChessPiece;
import view.offlineMode.ChessBoardPanel;

import java.util.ArrayList;
import java.util.List;

public class Save {

    private ChessBoardPanel gamePanel;
    private List<Step> stepList = new ArrayList<>(0);
    private List<ChessBoard> chessBoardList = new ArrayList<>(0);

    public Save(ChessBoardPanel chessBoardPanel) {
        this.gamePanel = chessBoardPanel;
    }

    public void addStep(int sid, int row, int col, ChessPiece chessPiece){
        Step step = null;
        if(chessPiece == ChessPiece.BLACK){
            step = new Step(sid,row,col,-1,gamePanel.getIntCheat());
        }
        else if (chessPiece == ChessPiece.WHITE){
            step = new Step(sid,row,col,1,gamePanel.getIntCheat());
        }
        stepList.add(step);
        System.out.println(stepList.toString());
    }

    public void addChessBoard(int cbid){
        ChessGridComponent[][] chessGridComponents = gamePanel.getChessGrids();
        int[][] chessBoardData = new int[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(chessGridComponents[i][j].getChessPiece() == ChessPiece.BLACK){
                    chessBoardData[i][j] = -1;
                }
                if(chessGridComponents[i][j].getChessPiece() == ChessPiece.WHITE){
                    chessBoardData[i][j] = 1;
                }
            }
        }
        ChessBoard chessBoard = new ChessBoard(cbid, chessBoardData);
        this.chessBoardList.add(chessBoard);
        System.out.println(chessBoardList.toString());
    }

    public List<Step> getStepList() {
        return stepList;
    }

    public List<ChessBoard> getChessBoardList() {
        return chessBoardList;
    }

    public void restart(){
        stepList.clear();
        chessBoardList.clear();
    }

    public void undo(){
        stepList.remove(stepList.size()-1);
        chessBoardList.remove(chessBoardList.size()-1);
    }

    public void setStepList(List<Step> stepList) {
        this.stepList = stepList;
        System.out.println(this.stepList.toString());
    }

    public void setChessBoardList(List<ChessBoard> chessBoardList) {
        this.chessBoardList = chessBoardList;
        System.out.println(this.chessBoardList.toString());
    }
}
