package com.urrecliner.merge2048;

import android.util.Log;

import com.urrecliner.merge2048.GameObject.HighMember;

import java.util.List;

public class GameInfo {

    public int screenXSize, screenYSize;
    public int blockInSize, blockOutSize;
    public int xBlockCnt = 5, yBlockCnt = 6;   // screen Size
    public int xOffset,yUpOffset, yDownOffset;
    public long scoreNow;
    public List<HighMember> highMembers;
    public int xNextPos, yNextPos, xNewPos, yNewPosS, xNextNextPos, yNextNextPos;
    public int greatIdx, greatStacked = 0, greatCount = 12;
    public boolean isGameOver = false, quitPressed = false, quitGame = false;
    public boolean newGamePressed = false, newGameStart = false, showNext = true;
    public boolean dumpCellClicked = false;
    public boolean blockClicked = false;   // clicked means user clicked
    public int touchIndex;               // user selected x Index (0 ~ xBlockCnt)
    public boolean isGameGone = false;
    public int poolAniSize = 0;

    public GameInfo (int screenXSize, int screenYSize) {
        this.screenXSize = screenXSize;
        this.screenYSize = screenYSize;
        yUpOffset = 32;
        blockOutSize = (screenXSize-180) / xBlockCnt;
        blockInSize = blockOutSize - blockOutSize/24;

        xOffset = (screenXSize - xBlockCnt * blockOutSize) / 2;
        yDownOffset = yUpOffset + yBlockCnt * blockOutSize + 24;

        xNextPos = (screenXSize - blockOutSize) / 2;
        yNextPos = yDownOffset + 12;

        xNewPos = xNextPos + blockOutSize - 16;
        yNewPosS = yNextPos + blockOutSize + 32;
        Log.w("blockSize", "out="+blockOutSize +" in="+ blockInSize);
    }

}