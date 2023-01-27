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
    public final int X_BLOCK_CNT = 5, Y_BLOCK_CNT = 6;
    public final int blockOutSize;  // block Size with gaps
    public final int blockInSize;   // block size itself;
    public final int blockIconSize; // new, yes, no, ... icon size
    public final int blockFlyingGap;    // bigger size while moving
    public final int explodeGap;        // explode is a little larger
    public final int xOffset, yUpOffset, yDownOffset, xNextPosFixed;
    public final int xNewPos, yNewPos, xUndoPos, yUndoPos, xYesPos, yYesPos, xNopPos, yNopPos;
    public final int xSwingPos, ySwingPos, xQuitPos, yQuitPos;
    public final int xNextNextPos, yNextNextPos;
    public final int xSwapPos, ySwapPos;

    public final int bonusLoopCount = 12;
    public final int piece; // small pixel size for calculation sizes
    public Cell[][] cells;
    public List<String> svCells;
    public List<Integer> svNext;
    public List<Integer> svNextNext;

    public final int CONTINUE_INDEX = 10;  // 10 if achieved this index game can be continued

    public long scoreNow;
    public int score2Add;
    public List<HighMember> highMembers;
    public String userName = "";
    public long highLowScore;
    public int undoCount;
    public int xNextPos, yNextPos;
    public int bonusCount, bonusStacked = 0;
    public boolean isGameOver = false, quitGamePressed = false, quitGame = false;
    public boolean newGamePressed = false, startNewGameYes = false;
    public boolean swingPressed = false, swing = false;
    public boolean swapPressed = false, swap = false, undoPressed = false;
    public boolean is2048 = false, continueYes = false, userUpdated = false, isRanked = false;

    public boolean showNextPressed = false, showNext = true;
    public int showCount;

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

    public int xHighPosS, xHighPosE, yHighPosS, yHighPosE;
    public int highTouchCount;
    public boolean highTouchPressed;


    public enum STATE {
        PAUSED, MOVING, STOP, GO_UP, MERGE, MERGED, EXPLODE, EXPLODED, DESTROY, DESTROYED, PULL
    }

    public GInfo(Context context) {

        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);

        screenXSize = metrics.widthPixels;      // note20  1440 x 2819
        screenYSize = metrics.heightPixels;     // A32     1080 x 2194

        blockOutSize = screenXSize * 85 / 100 / X_BLOCK_CNT;  // note20 = 244
        blockInSize = blockOutSize - blockOutSize / 36;     // note20 = 238

        blockFlyingGap = (blockOutSize * 110/100 - blockInSize)/2;  // note20 15
        xOffset = (screenXSize - X_BLOCK_CNT * blockOutSize) / 2; // note20 110
        yUpOffset = screenYSize / 80;   // ~= 35;
        yDownOffset = yUpOffset + Y_BLOCK_CNT * blockOutSize + yUpOffset/3;  // next block top 1510
        blockIconSize = blockOutSize * 3 / 4;   // note20 183
        explodeGap = blockInSize / 8;  // note20 48
        piece = screenXSize / 12;   // 120

        xNextPosFixed = (screenXSize - blockOutSize) / 2;  // note20 598
        yNextPos = yDownOffset + 16;    // 1526

        xNextNextPos = xNextPosFixed + (blockOutSize / 4);
        yNextNextPos = yNextPos + blockOutSize + 8;

        xNewPos = xNextPosFixed + blockOutSize - 8;    //  note20 834
        yNewPos = yNextPos + blockOutSize + 16;
        xQuitPos = xNewPos + blockIconSize;         yQuitPos = yNewPos;
        xUndoPos = xQuitPos + blockIconSize;        yUndoPos = yNewPos;
        xSwingPos = xNewPos;                        ySwingPos = yNewPos + blockIconSize;
        xSwapPos = xNewPos + blockIconSize;         ySwapPos = yNewPos + blockIconSize;
        xYesPos = screenXSize/2 - blockInSize - blockInSize;
        xNopPos = screenXSize/2;
        yYesPos = yDownOffset - blockOutSize*3;
        yNopPos = yYesPos;
        cells = new Cell[X_BLOCK_CNT][Y_BLOCK_CNT];
        resetValues();
    }

    public void resetValues() {
        isGameOver = false;
        aniStacks = new ArrayList<>();
        svCells = new ArrayList<>();
        svNext = new ArrayList<>();
        svNextNext = new ArrayList<>();
        scoreNow = 0;
        gameDifficulty = 5;
        xNextPos = xNextPosFixed;
        bonusCount = 0;
        dumpCount = 0;
        swapCount = 3;
        showCount = 6;
        undoCount = 4;
        highTouchCount = 0;
        bonusStacked = 0;
        is2048 = false;
        isRanked = false;
        continueYes = false;
        userUpdated = false;
    }
}