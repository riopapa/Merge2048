package com.urrecliner.merge2048.GamePlate;

public class Cell {

    public int index;
    public Ani.STATE state;
    long timeStamp;

    public Cell(int index, Ani.STATE state) {
        this.index = index;
        this.state = state;
        timeStamp = System.currentTimeMillis();
    }

}