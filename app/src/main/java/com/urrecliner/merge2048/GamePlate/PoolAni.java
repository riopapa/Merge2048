package com.urrecliner.merge2048.GamePlate;

public class PoolAni {

    Ani.STATE state;
    int xS, yS, xF, yF;   // move Start to Finish cell
    int xInc, yInc, count;     // ani increments stack

    long delay = 20;   // redraw at every 100 mili sec
    long timeStamp = 0;

    // state MOVING
    public PoolAni(Ani.STATE state, int xS, int yS, int xF, int yF, int xI, int yI) {
        this.state = state;
        this.xS = xS;
        this.yS = yS;
        this.xF = xF;
        this.yF = yF;
        this.xInc = xI;
        this.yInc = yI;

        count = 0;
    }

    // state GREAT
    public PoolAni(Ani.STATE state, int xS, int yS, int xInc, int yInc, int countIdx) {
        this.state = state;
        this.xS = xS;
        this.yS = yS;
        this.xInc = xInc;
        this.yInc = yInc;
        this.xF = countIdx;
        delay = 20;
        count = 0;
    }

    // state EXPLODE
    public PoolAni(Ani.STATE state, int xS, int yS, int xInc, int yInc) {
        this.state = state;
        this.xS = xS;
        this.yS = yS;
        this.xInc = xInc;
        this.yInc = yInc;
        delay = 10;
        count = 0;
    }

    // state MERGE
    public PoolAni(Ani.STATE state, int xS, int yS, int index) {
        this.state = state;
        this.xS = xS;
        this.yS = yS;
        this.xF = index;
        delay = 10;
        count = 0;
    }
}