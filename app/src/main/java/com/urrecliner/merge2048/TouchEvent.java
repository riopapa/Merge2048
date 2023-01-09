package com.urrecliner.merge2048;

import android.view.MotionEvent;

public class TouchEvent {

    final GInfo gInfo;
    private final int xBlockCnt;
    private int xTouchPos, yTouchPos;
    private final int xOffset, yDownOffset, yNextBottom, blockOutSize;
    private final int xNewPosS, yNewPosS, xNewPosE, yNewPosE;
    private final int xNextNextPosS, yNextNextPosS, xNextNextPosE, yNextNextPosE;
    private final int xQuitPosS, yQuitPosS, xQuitPosE, yQuitPosE;
    private final int xSwingPosS, ySwingPosS, xSwingPosE, ySwingPosE;
    private final int xSwapPosS, ySwapPosS, xSwapPosE, ySwapPosE;

    public TouchEvent (GInfo gInfo) {
        this.gInfo = gInfo;
        xOffset = gInfo.xOffset; yDownOffset = gInfo.yDownOffset;
        blockOutSize = gInfo.blockOutSize;
        xBlockCnt = gInfo.xBlockCnt;

        yNextBottom = gInfo.yNextPos + blockOutSize + 4;

        xNewPosS = gInfo.xNewPos;
        yNewPosS = gInfo.yNewPos;
        xNewPosE = xNewPosS + gInfo.blockIconSize;
        yNewPosE = yNewPosS+ gInfo.blockIconSize;

        xQuitPosS = gInfo.xQuitPos;
        yQuitPosS = gInfo.yQuitPos;
        xQuitPosE = xQuitPosS + gInfo.blockIconSize;
        yQuitPosE = yQuitPosS + gInfo.blockIconSize;

        xNextNextPosS = gInfo.xNextNextPos;
        yNextNextPosS = gInfo.yNextNextPos;
        xNextNextPosE = xNextNextPosS + gInfo.blockIconSize/2;
        yNextNextPosE = yNextNextPosS + gInfo.blockIconSize/2;

        xSwingPosS = gInfo.xNewPos;
        ySwingPosS = gInfo.yNewPos + gInfo.blockIconSize;
        xSwingPosE = xSwingPosS + gInfo.blockIconSize;
        ySwingPosE = ySwingPosS + gInfo.blockIconSize;

        xSwapPosS = gInfo.xSwapPos;
        ySwapPosS = gInfo.ySwapPos;
        xSwapPosE = xSwapPosS + gInfo.blockIconSize;
        ySwapPosE = ySwapPosS + gInfo.blockIconSize;

    }
    
    public void check(MotionEvent event) {

        // Handle user input touch event actions
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
                if (yTouchPos < yDownOffset)
                    return;
                if (gInfo.newGamePressed) {
                    if (gInfo.isGameOver || isYesPressed()) {
                        gInfo.startNewGameYes = true;
                        gInfo.newGamePressed = false;
                    } else if (isNoPressed()) {
                        gInfo.startNewGameYes = false;
                        gInfo.newGamePressed = false;
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
        int touchIndex = xTouchPos / blockOutSize;
        if (touchIndex < 0)
            touchIndex = 0;
        if (touchIndex >= xBlockCnt)
            touchIndex = xBlockCnt - 1;
        gInfo.shoutClicked = true;
        gInfo.touchIndex = touchIndex;
    }

    boolean isNewGamePressed() {
        return (xTouchPos >= xNewPosS && xTouchPos <= xNewPosE &&
                yTouchPos >= yNewPosS && yTouchPos <= yNewPosE);
    }

    boolean isQuitGamePressed() {
        return (xTouchPos >= xQuitPosS && xTouchPos <= xQuitPosE &&
                yTouchPos >= yQuitPosS && yTouchPos <= yQuitPosE);
    }

    boolean isYesPressed() {
        return  (xTouchPos >= xNewPosE &&
                xTouchPos <= xNewPosE + gInfo.blockOutSize*4/5 &&
                yTouchPos >= yNewPosS && yTouchPos <= yNewPosE);
    }

    boolean isNoPressed() {
        return  (xTouchPos >= xNewPosE + gInfo.blockOutSize*4/5 &&
                xTouchPos <= xNewPosE + gInfo.blockOutSize*8/5 &&
                yTouchPos >= yNewPosS && yTouchPos <= yNewPosE);
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

    boolean isShootPressed() {
        return  yTouchPos <= yNextBottom;
    }

    boolean isNextPressed() {
        return (xTouchPos >= xNextNextPosS && xTouchPos <= xNextNextPosE &&
                yTouchPos >= yNextNextPosS && yTouchPos <= yNextNextPosE);
    }

}