package com.urrecliner.merge2048.GameObject;

import com.urrecliner.merge2048.GameInfo;

public class Cell {

    public int index;
    public GameInfo .STATE state;
    long timeStamp;

    public Cell(int index, GameInfo.STATE state) {
        this.index = index;
        this.state = state;
        timeStamp = System.currentTimeMillis();
    }

}