package com.urrecliner.merge2048.GameObject;

import com.urrecliner.merge2048.GInfo;

public class AniStack {

    public GInfo.STATE state;
    public int xS, yS, xF, yF; // move Start to Finish cell
    public int xInc, yInc;
    public int count,maxCount, block;
    public long delay;   // redraw at every delay
    public long timeStamp;

    // state MOVING
    public AniStack(GInfo.STATE state, int xS, int yS, int xF, int yF,
                    int xInc, int yInc, int maxCount, long timeStamp, int block) {

        this.state = state;
        this.xS = xS;
        this.yS = yS;
        this.xF = xF;
        this.yF = yF;
        this.xInc = xInc;
        this.yInc = yInc;
        this.maxCount = maxCount;
        this.timeStamp = timeStamp;
        this.block = block;
        count = 0;
        delay = 8;
    }

    // state EXPLODE
    public AniStack(GInfo.STATE state, int xS, int yS, int xInc, int yInc) {
        this.state = state;
        this.xS = xS;
        this.yS = yS;
        this.xInc = xInc;
        this.yInc = yInc;
        delay = 8;
        count = 0;
    }

    // state MERGE
    public AniStack(GInfo.STATE state, int xS, int yS, int index) {
        this.state = state;
        this.xS = xS;
        this.yS = yS;
        this.xF = index;
        delay = 15; // a little more than explode
        count = 0;
    }
}