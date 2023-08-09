package com.rt.othello.SaveAndLoad;

public class Step {
    private int sid;
    private int row;
    private int col;
    private int color;
    private int cheat;

    public Step(int sid, int row, int col, int color,int cheat ) {
        this.sid = sid;
        this.row = row;
        this.col = col;
        this.color = color;
        this.cheat = cheat;

    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getCheat() {
        return cheat;
    }

    public void setCheat(int cheat) {
        this.cheat = cheat;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String write(){
        return String.format("%d %d %d %d %d",sid,row,col,color,cheat);
    }

    @Override
    public String toString() {
        return "Step{" + "sid=" + sid + ", row=" + row + ", col=" + col + ", color=" + color + ", cheat=" + cheat + '}';
    }
}
