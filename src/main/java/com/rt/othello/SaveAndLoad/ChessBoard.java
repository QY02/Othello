package com.rt.othello.SaveAndLoad;

import java.util.Arrays;

public class ChessBoard {
    private int cbid;
    private int[][] chessBoardData;

    public ChessBoard(int cbid, int[][] chessBoardData) {
        this.cbid = cbid;
        this.chessBoardData = chessBoardData;
    }

    public int getCbid() {
        return cbid;
    }

    public void setCbid(int cbid) {
        this.cbid = cbid;
    }

    public int[][] getChessBoardData() {
        return chessBoardData;
    }

    public void setChessBoardData(int[][] chessBoardData) {
        this.chessBoardData = chessBoardData;
    }

    public String write(){
        String write = cbid +"\n";
        for (int i = 0; i < 8; i++) {
            write = write.concat(String.format("%d %d %d %d %d %d %d %d\n",chessBoardData[i][0],chessBoardData[i][1],chessBoardData[i][2],chessBoardData[i][3],chessBoardData[i][4],chessBoardData[i][5],chessBoardData[i][6],chessBoardData[i][7]));
        }
        return write;
    }

    @Override
    public String toString() {
        return "ChessBoard{" + "cbid=" + cbid + ", chessBoardData=" + Arrays.deepToString(chessBoardData) + '}';
    }
}
