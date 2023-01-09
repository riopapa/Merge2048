package com.urrecliner.merge2048;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

import com.urrecliner.merge2048.GameObject.AniStack;
import com.urrecliner.merge2048.GameObject.Cell;
import com.urrecliner.merge2048.GameObject.HighMember;

import java.util.ArrayList;
import java.util.List;

public class GInfo {

    public final int screenXSize, screenYSize;
    public final int xBlockCnt = 5, yBlockCnt = 6;
    public final int blockOutSize;  // block Size with gaps
    public final int blockInSize;   // block size itself;
    public final int blockIconSize; // new, yes, no, ... icon size
    public final int blockFlyingGap;    // bigger size while moving
    public final int explodeGap;        // explode is a little larger
    public final int xOffset, yUpOffset, yDownOffset, xNextPosFixed;
    public final int xNewPos, yNewPos, xQuitPos, yQuitPos, yNextPos;
    public final int xNextNextPos, yNextNextPos, xSwingPos, ySwingPos, xSwapPos, ySwapPos;
    public final int swingXPosLeft, swingXPosRight;
    public final int bonusLoopCount = 12;
    public final int piece; // small pixel size for calculation sizes
    public final Cell[][] cells;

    public long scoreNow;
    public int score2Add;
    public List<HighMember> highMembers;
    public long highLowScore;

    public int xNextPos;
    public int bonusCount, bonusStacked = 0;
    public boolean isGameOver = false, quitGamePressed = false, quitGame = false;
    public boolean newGamePressed = false, startNewGameYes = false;
    public boolean showNextPressed = false, showNext = true;
    public boolean swingPressed = false, swing = false;
    public boolean swapPressed = false, swap = false;
    public int swingXInc;
    public long swingTime, swingDelay;

    public boolean shoutClicked = false;   // clicked means user clicked
    public int shootIndex;               // user selected x Index (0 ~ xBlockCnt)
    public int gameDifficulty = 5;
    public int swapCount;

    public String msgHead = "";
    public String msgLine1 = "", msgLine2 = "";
    public long msgStartTime, msgFinishTime = 0;

    public List<AniStack> aniStacks = new ArrayList<>();

    public int dumpCount = 0;
    public boolean dumpClicked = false;

    public enum STATE {
        PAUSED, MOVING, STOP, GO_UP, MERGE, MERGED, EXPLODE, EXPLODED
    }

    public GInfo(Context context) {

        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);

        screenXSize = metrics.widthPixels;      // note20  1440 x 2819
        screenYSize = metrics.heightPixels;     // A32     1080 x 2194

        blockOutSize = screenXSize * 85 / 100 / xBlockCnt;  // note20 = 244
        blockInSize = blockOutSize - blockOutSize / 36;     // note20 = 238

        blockFlyingGap = (blockOutSize * 110/100 - blockInSize)/2;  // note20 15
        xOffset = (screenXSize - xBlockCnt * blockOutSize) / 2; // note20 110
        yUpOffset = screenYSize / 80;   // ~= 35;
        yDownOffset = yUpOffset + yBlockCnt * blockOutSize + yUpOffset/3;  // next block top 1510
        blockIconSize = (screenXSize - blockOutSize) / 3 / 2;   // note20 199
        explodeGap = blockOutSize / 5;  // note20 48
        piece = screenXSize / 12;   // 120

        xNextPosFixed = (screenXSize - blockOutSize) / 2;  // note20 598
        yNextPos = yDownOffset + 16;    // 1526

        xNextNextPos = xNextPosFixed + (blockOutSize / 4);
        yNextNextPos = yNextPos + blockOutSize + 8;

        xNewPos = xNextPosFixed + blockOutSize - 8;    //  note20 834
        yNewPos = yNextPos + blockOutSize + 16;

        xSwingPos = xNewPos;
        ySwingPos = yNewPos + blockIconSize;
        swingXInc = blockOutSize / 5;   // 48
        swingXPosLeft = xOffset - 32;   // 78
        swingXPosRight = screenXSize - xOffset -blockOutSize + 32;  // 1118

        xQuitPos = screenXSize-xOffset-blockIconSize;
        yQuitPos = yNewPos;

        xSwapPos = screenXSize-xOffset-blockIconSize;
        ySwapPos = ySwingPos;

        cells = new Cell[xBlockCnt][yBlockCnt];
        resetValues();
    }

    public void resetValues() {
        aniStacks = new ArrayList<>();
        scoreNow = 0;
        gameDifficulty = 5;
        bonusCount = 0;
        bonusStacked = 0;
        isGameOver = false;
        dumpCount = 0;
        swapCount = 3;
        xNextPos = xNextPosFixed;
    }

    public void resetSwing() {

        xNextPos = xNextPosFixed;
        swingDelay = 300 / (gameDifficulty+2);
        swingXInc = blockOutSize / 6;
        swingTime = System.currentTimeMillis() + swingDelay;
    }

    public void updateSwing() {
        if (System.currentTimeMillis() > swingTime) {   // start swing left, right
            xNextPos += swingXInc;
            if (xNextPos > swingXPosRight) {
                xNextPos = swingXPosRight;
                swingXInc = -swingXInc;
            } else if (xNextPos < swingXPosLeft) {
                xNextPos = swingXPosLeft;
                swingXInc = -swingXInc;
            }
            swingTime = System.currentTimeMillis() + swingDelay;
        }
    }
}