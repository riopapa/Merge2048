package com.urrecliner.merge2048.GamePlate;

import static com.urrecliner.merge2048.GamePlate.Ani.MOVE_SMOOTH;

import com.urrecliner.merge2048.GameInfo;

public class AniPool {

    Ani.STATE state;
    int xS, yS, xF, yF;   // move Start to Finish cell
    int xInc, yInc, count;     // ani increments stack

    long delay = 20;   // redraw at every 100 mili sec
    long timeStamp = 0;

    // state MOVING
    public AniPool(Ani.STATE state, int xS, int yS, int xF, int yF, int xI, int yI) {
        this.state = state;
        this.xS = xS;
        this.yS = yS;
        this.xF = xF;
        this.yF = yF;
        this.xInc = xI;
        this.yInc = yI;

        count = 0;
    }

    // state EXPLODE
    public AniPool(Ani.STATE state, int xS, int yS, int xInc, int yInc) {
        this.state = state;
        this.xS = xS;
        this.yS = yS;
        this.xInc = xInc;
        this.yInc = yInc;
        delay = 10;
        count = 0;
    }

    // state MERGE
    public AniPool(Ani.STATE state, int xS, int yS) {
        this.state = state;
        this.xS = xS;
        this.yS = yS;
        delay = 10;
        count = 0;
    }
}