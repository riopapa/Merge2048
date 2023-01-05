package com.urrecliner.merge2048.GameObject;

import com.urrecliner.merge2048.GInfo;

public class Cell {

    public int index, count;
    public GInfo.STATE state;
    public boolean xor;

    public Cell(int index, GInfo.STATE state) {
        this.index = index;
        this.state = state;
    }

}