package com.urrecliner.merge2048;

import android.graphics.Paint;

public class GameInfo {

    public int screenXSize, screenYSize;
    public int xBlockInSize, xBlockOutSize;
    public int yBlockInSize, yBlockOutSize;
    public int iConSize;
    public int xBlockCnt, yBlockCnt;
    public int xOffset,yUpOffset = 400, yDownOffset = 300;
    public int scoreNow;
    public int xNextPos, yNextPos;

    public GameInfo (int screenXSize, int screenYSize, int xBlockCnt, int yBlockCnt) {
        this.screenXSize = screenXSize;
        this.screenYSize = screenYSize;
        this.xBlockCnt = xBlockCnt;
        this.yBlockCnt = yBlockCnt;
        yUpOffset = 40;
        yDownOffset = 700;
        xBlockOutSize = (screenXSize-120) / xBlockCnt;
        yBlockOutSize = (screenYSize - yUpOffset - yDownOffset) / (yBlockCnt+1);
        int xBlockGap = xBlockOutSize / 48;
        int yBlockGap = yBlockOutSize / 48;
        xBlockInSize = xBlockOutSize - xBlockGap - xBlockGap;
        yBlockInSize = yBlockOutSize - yBlockGap - yBlockGap;
        iConSize = Math.min(xBlockInSize, yBlockInSize);

        xOffset = (screenXSize - xBlockCnt * xBlockOutSize) / 2;
        yDownOffset = yUpOffset + yBlockCnt * yBlockOutSize + 20;

        xNextPos = (screenXSize - xBlockOutSize) / 2;
        yNextPos = yDownOffset + 16;

//        Log.w("blockSize", xBlockInSize +" x "+ yBlockInSize +" i="+iConSize);
    }

    public boolean outOfCanvas(int pos, int maxSize) {
        return (pos > maxSize - iConSize || pos < 0);
    }


}