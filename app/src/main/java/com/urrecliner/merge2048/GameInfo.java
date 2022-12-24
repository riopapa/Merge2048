package com.urrecliner.merge2048;

import android.util.Log;

import com.urrecliner.merge2048.GameObject.HighMember;

import java.util.List;

public class GameInfo {

    public int screenXSize, screenYSize;
    public int blockInSize, blockOutSize, blockIconSize, explodeGap;
    public int xBlockCnt = 5, yBlockCnt = 6;   // screen Size
    public int xOffset,yUpOffset, yDownOffset;
    public long scoreNow;
    public List<HighMember> highMembers;
    public int xNextPos, yNextPos, xNewPos, yNewPosS, xNextNextPos, yNextNextPos;
    public int greatIdx, greatStacked = 0, greatCount = 12;
    public boolean isGameOver = false, quitPressed = false, quitGame = false;
    public boolean newGamePressed = false, newGameStart = false, showNext = true;
    public boolean swingPressed = false, swing = false;
    public int swingXPosition = 0;
    public boolean dumpCellClicked = false;
    public boolean blockClicked = false;   // clicked means user clicked
    public int touchIndex;               // user selected x Index (0 ~ xBlockCnt)
    public boolean isGameGone = false;
    public int poolAniSize = 0;
    public int gameDifficulty = 6;

    public GameInfo (int screenXSize, int screenYSize) {
        this.screenXSize = screenXSize;
        this.screenYSize = screenYSize;
        yUpOffset = 32;
        blockOutSize = screenXSize* 90/100 / xBlockCnt;
        blockInSize = blockOutSize - blockOutSize/24;
        xOffset = (screenXSize - xBlockCnt * blockOutSize) / 2;
        blockIconSize = (blockOutSize* 2 + xOffset) / 3;
        explodeGap = blockOutSize / 5;

        Log.w("GameInfo", "screen= "+screenXSize +" x "+ screenYSize);
        Log.w("GameInfo", "out="+blockOutSize +" in="+ blockInSize+" icon="+blockIconSize);

        yDownOffset = yUpOffset + yBlockCnt * blockOutSize + 12;

        xNextPos = (screenXSize - blockOutSize) / 2;
        yNextPos = yDownOffset + 16;

        xNewPos = xNextPos + blockOutSize - 8;
        yNewPosS = yNextPos + blockOutSize + 16;
    }

}