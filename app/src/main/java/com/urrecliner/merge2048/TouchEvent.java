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

    public TouchEvent (GInfo gInfo) {
        this.gInfo = gInfo;
        xOffset = gInfo.xOffset; yDownOffset = gInfo.yDownOffset;
        blockOutSize = gInfo.blockOutSize;
        xBlockCnt = gInfo.xBlockCnt;

        yNextBottom = gInfo.yNextPos + blockOutSize + 4;

        xNewPosS = gInfo.xNewPos;
        xNewPosE = xNewPosS + gInfo.blockOutSize*2/3;
        yNewPosS = gInfo.yNewPosS;
        yNewPosE = yNewPosS+ gInfo.blockOutSize*2/3;

        xQuitPosE = gInfo.screenXSize - gInfo.xOffset;
        xQuitPosS = xQuitPosE - gInfo.blockIconSize;
        yQuitPosS = gInfo.yNewPosS;
        yQuitPosE = yNewPosS+ gInfo.blockOutSize*2/3;

        xNextNextPosS = gInfo.xNextNextPos;
        yNextNextPosS = gInfo.yNextNextPos;
        xNextNextPosE = xNextNextPosS + blockOutSize/2;
        yNextNextPosE = yNextNextPosS + blockOutSize/2;

        xSwingPosS = gInfo.xNewPos; ySwingPosS = gInfo.yNewPosS + gInfo.blockIconSize;
        xSwingPosE = xSwingPosS + gInfo.blockIconSize; ySwingPosE = ySwingPosS + gInfo.blockIconSize;
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
                if (yTouchPos < 400 && xTouchPos < 400) {
                    gInfo.dumpClicked = true;
                    gInfo.dumpCount++;
//                    if (gInfo.dumpCount > 2) {
//                        gInfo.msgHead = "참고";
//                        gInfo.msgLine1 = "Dump Start..";
//                        gInfo.msgLine2 = "Cell Arrays";
//                        gInfo.msgTime = System.currentTimeMillis() + 1500;
//                    }
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

                } else if (isNextPressed()) {
                    gInfo.showNextPressed = true;

                } else if (isNewGamePressed()) {
                    gInfo.newGamePressed = true;

                } else if (isQuitGamePressed()) {
                    gInfo.quitGamePressed = true;

                } else if (gInfo.quitGamePressed) {
                    if (isYesPressed()) {
                        gInfo.quitGamePressed = false;
                        gInfo.quitGame = true;
                    } else if (isNoPressed()) {
                        gInfo.quitGamePressed = false;
                        gInfo.quitGame = false;
                    }
                } else if (isShootPressed()) {
                    xTouchPos -= xOffset;
                    if (xTouchPos >= 0) {
                        int touchIndex = xTouchPos / blockOutSize;
                        if (touchIndex >= 0 && touchIndex < xBlockCnt) {
                            if (gInfo.swing) {
                                if (xTouchPos >= gInfo.xNextPos &&
                                    xTouchPos < gInfo.xNextPos + blockOutSize) {
                                    gInfo.blockClicked = true;
                                    gInfo.touchIndex = touchIndex;
                                }
                            } else {
                                gInfo.blockClicked = true;
                                gInfo.touchIndex = touchIndex;
                            }
                        }
                    }
                } else if (isSwingPressed()) {
                    gInfo.swingPressed = true;
//
//                } else if (yTouchPos > yNextBottom + 200){
//                    gInfo.dumpCellClicked = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                break;
        }
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
        return  (!gInfo.isGameOver) &&
                (xTouchPos >= xNewPosE &&
                xTouchPos <= xNewPosE + gInfo.blockOutSize*4/5 &&
                yTouchPos >= yNewPosS && yTouchPos <= yNewPosE);
    }

    boolean isNoPressed() {
        return  (!gInfo.isGameOver) &&
                (xTouchPos >= xNewPosE + gInfo.blockOutSize*4/5 &&
                xTouchPos <= xNewPosE + gInfo.blockOutSize*8/5 &&
                yTouchPos >= yNewPosS && yTouchPos <= yNewPosE);
    }

    boolean isSwingPressed() {
        return  (!gInfo.isGameOver) &&
                (xTouchPos >= xSwingPosS  && xTouchPos <= xSwingPosE &&
                        yTouchPos >= ySwingPosS && yTouchPos <= ySwingPosE);
    }

    boolean isShootPressed() {
        return  yTouchPos <= yNextBottom;
    }

    boolean isNextPressed() {
        return (xTouchPos >= xNextNextPosS && xTouchPos <= xNextNextPosE &&
                yTouchPos >= yNextNextPosS && yTouchPos <= yNextNextPosE);
    }


}