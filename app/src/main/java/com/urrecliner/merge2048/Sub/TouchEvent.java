package com.urrecliner.merge2048.Sub;

import android.view.MotionEvent;

import com.urrecliner.merge2048.GInfo;

public class TouchEvent {

    final GInfo gInfo;
    private final int xBlockCnt;
    private int xTouchPos, yTouchPos;
    private final int xOffset;
    private final int yNextBottom;
    private final int blockOutSize, blockInSize;
    private final int xNewPosS, yNewPosS, xNewPosE, yNewPosE;
    private final int xNextNextPosS, yNextNextPosS, xNextNextPosE, yNextNextPosE;
    private final int xQuitPosS, yQuitPosS, xQuitPosE, yQuitPosE;
    private final int xSwingPosS, ySwingPosS, xSwingPosE, ySwingPosE;
    private final int xSwapPosS, ySwapPosS, xSwapPosE, ySwapPosE;
    private final int xHighPosS, xHighPosE, yHighPosS, yHighPosE;
    private final int xYesPosS, xYesPosE, xNopPosS, xNopPosE;
    private final int yYesPosS, yYesPosE, yNopPosS, yNopPosE;
    private final int xUndoPosS, xUndoPosE, yUndoPosS, yUndoPosE;

    public TouchEvent (GInfo gInfo) {
        this.gInfo = gInfo;
        int blockIconSize = gInfo.blockIconSize;

        xOffset = gInfo.xOffset;
        blockOutSize = gInfo.blockOutSize;
        blockInSize = gInfo.blockInSize;
        xBlockCnt = gInfo.X_BLOCK_CNT;

        yNextBottom = gInfo.yNextPos + blockOutSize + 4;

        xNewPosS = gInfo.xNewPos;       yNewPosS = gInfo.yNewPos;
        xNewPosE = xNewPosS + gInfo.blockIconSize;
        yNewPosE = yNewPosS+ gInfo.blockIconSize;

        xQuitPosS = gInfo.xQuitPos;     yQuitPosS = gInfo.yQuitPos;
        xQuitPosE = xQuitPosS + gInfo.blockIconSize;
        yQuitPosE = yQuitPosS + gInfo.blockIconSize;

        xNextNextPosS = gInfo.xNextNextPos;
        yNextNextPosS = gInfo.yNextNextPos;
        xNextNextPosE = xNextNextPosS + blockIconSize /2;
        yNextNextPosE = yNextNextPosS + blockIconSize /2;

        xSwingPosS = gInfo.xNewPos;
        ySwingPosS = gInfo.yNewPos + blockIconSize;
        xSwingPosE = xSwingPosS + blockIconSize;
        ySwingPosE = ySwingPosS + blockIconSize;

        xSwapPosS = gInfo.xSwapPos;     ySwapPosS = gInfo.ySwapPos;
        xSwapPosE = xSwapPosS + blockIconSize;
        ySwapPosE = ySwapPosS + blockIconSize;

        xUndoPosS = gInfo.xUndoPos;     xUndoPosE = xUndoPosS + blockIconSize;
        yUndoPosS = gInfo.yUndoPos;     yUndoPosE = yUndoPosS + blockIconSize;

        xHighPosS = gInfo.xHighPosS;    xHighPosE = gInfo.xHighPosE;
        yHighPosS = gInfo.yHighPosS;    yHighPosE = gInfo.yHighPosE;

        xYesPosS = gInfo.xYesPos;       xYesPosE = xYesPosS + blockInSize + blockInSize;
        yYesPosS = gInfo.yYesPos;       yYesPosE = yYesPosS + blockInSize + blockInSize;

        xNopPosS = gInfo.xNopPos;         xNopPosE = xNopPosS + blockInSize + blockInSize;
        yNopPosS = gInfo.yNopPos;         yNopPosE = yNopPosS + blockInSize + blockInSize;

    }
    
    public void check(MotionEvent event) {

        // Handle user get touch event actions
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if (gInfo.aniStacks.size() != 0) // if animation not completed
                    return;              // ignore touch Up
                xTouchPos = (int) event.getX();
                yTouchPos = (int) event.getY();
                if (yTouchPos < 300 && xTouchPos < 300) {
                    gInfo.dumpClicked = true;
                    gInfo.dumpCount++;
                }
                if (yTouchPos < yYesPosS)
                    return;
                if (gInfo.newGamePressed) {
                    if (gInfo.isGameOver || isYesPressed()) {
                        gInfo.startNewGameYes = true;
                        gInfo.newGamePressed = false;
                    } else if (isNoPressed()) {
                        gInfo.startNewGameYes = false;
                        gInfo.newGamePressed = false;
                    }

                } else if (gInfo.isGameOver && gInfo.is2048) {
                    if (isYesPressed()) {
                        gInfo.continueYes = true;
                    } else if (isNoPressed()) {
                        gInfo.continueYes = false;
                        gInfo.is2048 = false;
                        gInfo.startNewGameYes = true;
                    }
                } else if (isNewGamePressed()) {
                    gInfo.newGamePressed = true;

                } else if (isNextPressed()) {
                    gInfo.showNextPressed = true;

                } else if (isSwapPressed()) {
                    gInfo.swapPressed = true;

                } else if (gInfo.quitGamePressed) {
                    if (isYesPressed()) {
                        gInfo.quitGamePressed = false;
                        gInfo.quitGame = true;
                    } else if (isNoPressed()) {
                        gInfo.quitGamePressed = false;
                        gInfo.quitGame = false;
                    }
                } else if (isQuitGamePressed()) {
                    gInfo.quitGamePressed = true;

                } else if (isHighPressed()) {
                    gInfo.highTouchPressed = true;

                } else if (isShootPressed()) {
                    if (gInfo.swing) {
                        if (xTouchPos >= gInfo.xNextPos &&
                                xTouchPos < gInfo.xNextPos + blockOutSize) {
                            calcTouchIdx();
                        }
                    } else {
                        calcTouchIdx();
                    }
                } else if (isSwingPressed()) {
                    gInfo.swingPressed = true;
                } else if (isUndoPressed()) {
                    gInfo.undoPressed = true;
                }
                break;

            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                break;
        }
    }

    private void calcTouchIdx() {
        xTouchPos -= xOffset;
        int shootIdx = xTouchPos / blockOutSize;
        if (shootIdx < 0)
            shootIdx = 0;
        if (shootIdx >= xBlockCnt)
            shootIdx = xBlockCnt - 1;
        gInfo.shoutClicked = true;
        gInfo.shootIndex = shootIdx;
    }

    boolean isNewGamePressed() {
        return (xTouchPos >= xNewPosS && xTouchPos <= xNewPosE &&
                yTouchPos >= yNewPosS && yTouchPos <= yNewPosE);
    }

    boolean isQuitGamePressed() {
        return (xTouchPos >= xQuitPosS && xTouchPos <= xQuitPosE &&
                yTouchPos >= yQuitPosS && yTouchPos <= yQuitPosE);
    }

    boolean isHighPressed() {
        return  (xTouchPos >= xHighPosS && xTouchPos <= xHighPosE &&
                yTouchPos >= yHighPosS && yTouchPos <= yHighPosE);
    }

    boolean isYesPressed() {
        return  (xTouchPos >= xYesPosS && xTouchPos <= xYesPosE &&
                yTouchPos >= yYesPosS && yTouchPos <= yYesPosE);
    }

    boolean isNoPressed() {
        return  (xTouchPos >= xNopPosS && xTouchPos <= xNopPosE &&
                yTouchPos >= yNopPosS && yTouchPos <= yNopPosE);
    }

    boolean isSwingPressed() {
        return  (!gInfo.isGameOver) &&
                (xTouchPos >= xSwingPosS  && xTouchPos <= xSwingPosE &&
                        yTouchPos >= ySwingPosS && yTouchPos <= ySwingPosE);
    }

    boolean isSwapPressed() {
        return  (!gInfo.isGameOver) &&
                (xTouchPos >= xSwapPosS  && xTouchPos <= xSwapPosE &&
                        yTouchPos >= ySwapPosS && yTouchPos <= ySwapPosE);
    }

    boolean isUndoPressed() {
        return  (!gInfo.isGameOver) &&
                (xTouchPos >= xUndoPosS  && xTouchPos <= xUndoPosE &&
                        yTouchPos >= yUndoPosS && yTouchPos <= yUndoPosE);
    }

    boolean isShootPressed() {
        return  yTouchPos <= yNextBottom && yTouchPos > yNextBottom - blockOutSize;
    }

    boolean isNextPressed() {
        return (xTouchPos >= xNextNextPosS && xTouchPos <= xNextNextPosE &&
                yTouchPos >= yNextNextPosS && yTouchPos <= yNextNextPosE);
    }

}