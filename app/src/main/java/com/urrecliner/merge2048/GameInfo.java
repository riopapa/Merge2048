package com.urrecliner.merge2048;

import android.util.Log;

public class GameInfo {

    public int screenXSize, screenYSize;
    public int blockInSize, blockOutSize;
    public int xBlockCnt, yBlockCnt;
    public int xOffset,yUpOffset, yDownOffset;
    public int scoreNow;
    public int xNextPos, yNextPos, xNewPos, yNewPosS;
    public boolean newGamePressed = false, newGameStart = false;

    public GameInfo (int screenXSize, int screenYSize, int xBlockCnt, int yBlockCnt) {
        this.screenXSize = screenXSize;
        this.screenYSize = screenYSize;
        this.xBlockCnt = xBlockCnt;
        this.yBlockCnt = yBlockCnt;
        yUpOffset = 32;
        blockOutSize = (screenXSize-180) / xBlockCnt;
        blockInSize = blockOutSize - blockOutSize/24;

        xOffset = (screenXSize - xBlockCnt * blockOutSize) / 2;
        yDownOffset = yUpOffset + yBlockCnt * blockOutSize;

        xNextPos = (screenXSize - blockOutSize) / 2;
        yNextPos = yDownOffset + 32;

        xNewPos = xNextPos + blockOutSize - 16;
        yNewPosS = yNextPos + blockOutSize + 16;
        Log.w("blockSize", "out="+blockOutSize +" in="+ blockInSize);
    }

}