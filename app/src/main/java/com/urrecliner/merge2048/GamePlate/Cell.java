package com.urrecliner.merge2048.GamePlate;

public class Cell {

    int index, number;
    boolean active = true;
    long delay = 100;   // redraw at every 100 mili sec
    int percent = 10;
    long timeStamp;

    public Cell(int index) {
        this.index = index;
        number = 1;
        while (index-- > 0)
            number = number + number;
        active = true;
        timeStamp = System.currentTimeMillis();
    }



}