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
    public final int xOffset, yUpOffset, yDownOffset, xNextPosCenter;
    public final int xNewPos, yNextPos;
    public final int greatLoopCount = 12;
    public final int pxcl;
    public final Cell[][] cells;

    public long scoreNow;
    public int score2Add;
    public List<HighMember> highMembers;
    public long highLowScore;

    public int xNextPos, yNewPosS, xNextNextPos, yNextNextPos;
    public int greatIdx, greatStacked = 0;
    public boolean isGameOver = false, quitGamePressed = false, quitGame = false;
    public boolean newGamePressed = false, startNewGameYes = false;
    public boolean showNext = true;
    public boolean swingPressed = false, swing = false;
    public int swingXInc, swingXPosLeft, swingXPosRight;
    public long swingTime, swingDelay;

    public boolean blockClicked = false;   // clicked means user clicked
    public int touchIndex;               // user selected x Index (0 ~ xBlockCnt)
    public int gameDifficulty = 5;

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


        // setUp final values
        screenXSize = metrics.widthPixels;      // note20  1440 x 2819
        screenYSize = metrics.heightPixels;     // A32     1080 x 2194

        blockOutSize = screenXSize * 90 / 100 / xBlockCnt;  // note20 = 259
        blockInSize = blockOutSize - blockOutSize / 36;     // note20 = 252

        blockFlyingGap = (blockOutSize * 110/100 - blockOutSize)/2;
        xOffset = (screenXSize - xBlockCnt * blockOutSize) / 2;
        yUpOffset = 32;
        yDownOffset = yUpOffset + yBlockCnt * blockOutSize + 12;
        blockIconSize = (screenXSize - blockOutSize) / 3 / 2;   // note20 196
        explodeGap = blockOutSize / 5;
        xNextPosCenter = (screenXSize - blockOutSize) / 2;
        xNewPos = xNextPosCenter + blockOutSize - 8;
        yNextPos = yDownOffset + 16;
        pxcl = screenXSize / 12;
        swingXInc = blockOutSize / 5;
        swingXPosLeft = xOffset - 32;
        swingXPosRight = xOffset + blockOutSize * (xBlockCnt - 1) + 32;
        cells = new Cell[xBlockCnt][yBlockCnt];

//        Log.w("GInfo", "screen= " + screenXSize + " x " + screenYSize);
//        Log.w("GInfo", "blockOutSize=" + blockOutSize + " blockInSize=" + blockInSize + " blockIconSize=" + blockIconSize);

        resetValues();
    }

    public void resetValues() {
        xNextPos = xNextPosCenter;
        xNextNextPos = xNextPos + (blockOutSize / 4);
        yNextNextPos = yNextPos + blockOutSize + 8;
        yNewPosS = yNextPos + blockOutSize + 16;

        aniStacks = new ArrayList<>();
        scoreNow = 0;
        gameDifficulty = 5;
        greatIdx = 0;
        greatStacked = 0;
        isGameOver = false;
        dumpCount = 0;
    }

    public void resetSwing() {
        swing = !swing;
        xNextPos = (screenXSize - blockOutSize) / 2;
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