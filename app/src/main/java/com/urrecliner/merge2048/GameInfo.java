package com.urrecliner.merge2048;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;

public class GameInfo {

    public int screenXSize, screenYSize;
    public int xBlockInSize, xBlockOutSize;
    public int yBlockInSize, yBlockOutSize;
    public int iConSize;
    public int xBlockCnt, yBlockCnt;
    public int yUpOffset = 300, yDownOffset = 300;
    public Paint backPaint;
    private final int xBlockGap, yBlockGap;

    public GameInfo (int screenXSize, int screenYSize, int xBlockCnt, int yBlockCnt, Paint backPaint) {
        this.screenXSize = screenXSize;
        this.screenYSize = screenYSize;
        this.xBlockCnt = xBlockCnt;
        this.yBlockCnt = yBlockCnt;
        this.backPaint = backPaint;
        xBlockOutSize = screenXSize / (xBlockCnt+1);
        yBlockOutSize = (screenYSize - yUpOffset - yDownOffset) / (yBlockCnt+1);
        xBlockGap = xBlockOutSize / 16;
        yBlockGap = yBlockOutSize / 16;
        xBlockInSize = xBlockOutSize - xBlockGap - xBlockGap;
        yBlockInSize = yBlockOutSize - yBlockGap - yBlockGap;

        iConSize = (xBlockInSize < yBlockInSize) ? xBlockInSize : yBlockInSize;
        Log.w("blockSize", xBlockInSize +" x "+ yBlockInSize +" i="+iConSize);
    }

    public boolean outOfCanvas(int pos, int maxSize) {
        return (pos > maxSize - iConSize || pos < 0);
    }


}