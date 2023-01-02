package com.urrecliner.merge2048.GameObject;

import com.urrecliner.merge2048.GInfo;

public class Cell {

    public int index;
    public GInfo.STATE state;
    long timeStamp;

    public Cell(int index, GInfo.STATE state) {
        this.index = index;
        this.state = state;
        timeStamp = System.currentTimeMillis();
    }

}