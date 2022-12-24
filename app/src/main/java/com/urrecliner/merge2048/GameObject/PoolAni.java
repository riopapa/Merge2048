package com.urrecliner.merge2048.GameObject;

import com.urrecliner.merge2048.GameInfo;

public class PoolAni {

    GameInfo.STATE state;
    int xS, yS, xF, yF;   // move Start to Finish cell
    int xInc, yInc, count;     // ani increments stack

    long delay = 20;   // redraw at every 100 milli sec
    long timeStamp = 0;

    // state MOVING, GREAT
    public PoolAni(GameInfo.STATE state, int xS, int yS, int xF, int yF, int xInc, int yInc) {

        this.state = state;
        this.xS = xS;
        this.yS = yS;
        this.xF = xF;
        this.yF = yF;
        this.xInc = xInc;
        this.yInc = yInc;
        count = 0;
        delay = 20;
    }

    // state EXPLODE
    public PoolAni(GameInfo.STATE state, int xS, int yS, int xInc, int yInc) {
        this.state = state;
        this.xS = xS;
        this.yS = yS;
        this.xInc = xInc;
        this.yInc = yInc;
        delay = 15;
        count = 0;
    }

    // state MERGE
    public PoolAni(GameInfo.STATE state, int xS, int yS, int index) {
        this.state = state;
        this.xS = xS;
        this.yS = yS;
        this.xF = index;
        delay = 25; // a little more than explode
        count = 0;
    }
}