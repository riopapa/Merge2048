package com.urrecliner.merge2048.GameObject;

import com.urrecliner.merge2048.GInfo;

public class AniStack {

    public GInfo.STATE state;
    public int xS, yS, xF, yF; // move Start to Finish cell
    public int xInc, yInc;
    public int count,maxCount, index;
    public long delay;   // redraw at every delay
    public long timeStamp;

    // state MOVING, PULL
     public AniStack(GInfo.STATE state, int xS, int yS, int xF, int yF,
                    int xInc, int yInc, int maxCount, long timeStamp, int index) {

        this.state = state;
        this.xS = xS;
        this.yS = yS;
        this.xF = xF;
        this.yF = yF;
        this.xInc = xInc;
        this.yInc = yInc;
        this.maxCount = maxCount;
        this.timeStamp = timeStamp;
        this.index = index;
        count = 0;
        delay = (state == GInfo.STATE.MOVING) ? 0: 80;  // move with no delay
    }

    // state EXPLODE
    public AniStack(GInfo.STATE state, int xS, int yS, int xInc, int yInc, long timeStamp, int index) {
        this.state = state;
        this.xS = xS;
        this.yS = yS;
        this.xInc = xInc;
        this.yInc = yInc;
        this.timeStamp = timeStamp;
        this.index = index;
        delay = 0;
        count = 0;
        maxCount = 5;   // refer to BlockImage explode[n]
    }

    // state DESTROY
    public AniStack(GInfo.STATE state, int xS, int yS, long timeStamp, int index) {
        this.state = state;
        this.xS = xS;
        this.yS = yS;
        this.timeStamp = timeStamp;
        this.index = index;
        delay = 30;
        count = 0;
        maxCount = 12;
    }

    // state MERGE
    public AniStack(GInfo.STATE state, int xS, int yS, int index, long timeStamp) {
        this.state = state;
        this.xS = xS;
        this.yS = yS;
        this.index = index;
        this.timeStamp = timeStamp;
        delay = 20; // a little more than explode
        count = 0;
        maxCount = 5;
    }
}