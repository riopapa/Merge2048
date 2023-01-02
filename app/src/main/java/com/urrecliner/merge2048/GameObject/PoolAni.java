package com.urrecliner.merge2048.GameObject;

import com.urrecliner.merge2048.GInfo;

public class PoolAni {

    GInfo.STATE state;
    int xS, yS, xF, yF;   // move Start to Finish cell
    int xInc, yInc, count, maxCount;     // ani increments stack

    long delay = 20;   // redraw at every 100 milli sec
    long timeStamp = 0;

    // state MOVING
    public PoolAni(GInfo.STATE state, int xS, int yS, int xF, int yF,
                   int xInc, int yInc, int maxCount) {

        this.state = state;
        this.xS = xS;
        this.yS = yS;
        this.xF = xF;
        this.yF = yF;
        this.xInc = xInc;
        this.yInc = yInc;
        this.maxCount = maxCount;
        count = 0;
        delay = 15;
    }

    // state EXPLODE
    public PoolAni(GInfo.STATE state, int xS, int yS, int xInc, int yInc) {
        this.state = state;
        this.xS = xS;
        this.yS = yS;
        this.xInc = xInc;
        this.yInc = yInc;
        delay = 15;
        count = 0;
    }

    // state MERGE
    public PoolAni(GInfo.STATE state, int xS, int yS, int index) {
        this.state = state;
        this.xS = xS;
        this.yS = yS;
        this.xF = index;
        delay = 25; // a little more than explode
        count = 0;
    }
}