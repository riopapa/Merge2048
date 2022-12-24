package com.urrecliner.merge2048.GameObject;

public class Great {
    public int xS, yS, idx, loopCount,xInc, yInc, count;
    public long delay;
    public long timeStamp = 0;

    // GREAT
    public Great(int xS, int yS, int idx, int loopCount, int xInc, int yInc) {

        this.xS = xS;
        this.yS = yS;
        this.idx = idx;
        this.loopCount = loopCount;   // great count
        this.xInc = xInc;
        this.yInc = yInc;
        count = 0;
        delay = 30;
        timeStamp = System.currentTimeMillis();
    }

}