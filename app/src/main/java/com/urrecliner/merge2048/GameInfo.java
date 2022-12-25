package com.urrecliner.merge2048;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

import com.urrecliner.merge2048.GameObject.HighMember;

import java.util.List;

public class GameInfo {

    public final int screenXSize, screenYSize;
    public final int blockInSize, blockOutSize, blockIconSize, blockFlyingGap, explodeGap;
    public final int xBlockCnt = 5, yBlockCnt = 6;
    public final int xOffset, yUpOffset, yDownOffset, xNextPosCenter;
    public long scoreNow;
    public int score2Add;
    public List<HighMember> highMembers;

    public int xNextPos, yNextPos, xNewPos, yNewPosS, xNextNextPos, yNextNextPos;

    public int greatIdx, greatStacked = 0;
    public final int greatCount = 12;
    public boolean isGameOver = false, quitPressed = false, quitGame = false;
    public boolean newGamePressed = false, startNewGame = false;
    public boolean showNext = true;

    public boolean swingPressed = false, swing = false;
    public int swingXInc, swingXPosLeft, swingXPosRight;
    public long swingTime, swingDelay;

    public boolean dumpCellClicked = false;
    public boolean blockClicked = false;   // clicked means user clicked
    public int touchIndex;               // user selected x Index (0 ~ xBlockCnt)
    public int poolAniSize = 0;
    public int gameDifficulty = 6;

    public enum STATE {
        PAUSED, MOVING, STOP, CHECK, MERGE, EXPLODE, EXPLODED
    }

    public GameInfo(Context context) {

        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        // note 20 ultra    1440 x 2819
        // A32              1080 x 2194

        // setUp final values
        screenXSize = metrics.widthPixels;
        screenYSize = metrics.heightPixels;
        yUpOffset = 32;
        blockOutSize = screenXSize * 90 / 100 / xBlockCnt;
        blockInSize = blockOutSize - blockOutSize / 36;
        blockFlyingGap = (blockOutSize * 120/100 - blockOutSize)/2;
        xOffset = (screenXSize - xBlockCnt * blockOutSize) / 2;
        blockIconSize = (screenXSize - blockOutSize) / 3 / 2;
        explodeGap = blockOutSize / 5;
        xNextPosCenter = (screenXSize - blockOutSize) / 2;

        swingXInc = blockOutSize / 5;
        swingXPosLeft = xOffset - 32;
        swingXPosRight = xOffset + blockOutSize * (xBlockCnt - 1) + 32;

        Log.w("GameInfo", "screen= " + screenXSize + " x " + screenYSize);
        Log.w("GameInfo", "out=" + blockOutSize + " in=" + blockInSize + " icon=" + blockIconSize);

        yDownOffset = yUpOffset + yBlockCnt * blockOutSize + 12;
        resetValues();
    }

    public void resetValues() {
        xNextPos = xNextPosCenter;
        yNextPos = yDownOffset + 16;
        xNextNextPos = xNextPos + (blockOutSize / 4);
        yNextNextPos = yNextPos + blockOutSize + 8;

        xNewPos = xNextPos + blockOutSize - 8;
        yNewPosS = yNextPos + blockOutSize + 16;
        scoreNow = 0;
        gameDifficulty = 4;
        greatIdx = 0;
        greatStacked = 0;
        isGameOver = false;
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