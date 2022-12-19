package com.urrecliner.merge2048.GamePlate;

public class Cell {

    public int index, number;
    public Ani.STATE state;
    public long delay = 300;   // redraw at every 100 mili sec
    public int percent = 10;
    long timeStamp;

    public Cell(int index) {
        this.index = index;
        if (index != 0) {
            number = 1;
            while (index-- > 0)
                number = number + number;
        } else
            number = 0;
        state = Ani.STATE.PAUSED;
        timeStamp = System.currentTimeMillis();
    }

    public Cell(int index, Ani.STATE state) {
        this.index = index;
        if (index != 0) {
            number = 1;
            while (index-- > 0)
                number = number + number;
        } else
            number = 0;
        this.state = state;
        timeStamp = System.currentTimeMillis();
    }


}