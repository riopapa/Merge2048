package com.urrecliner.merge2048;

import android.util.Log;
import android.view.MotionEvent;

public class TouchEvent {

    GameInfo gameInfo;
    private final int xBlockCnt;
    int xTouchPos, yTouchPos;
    private final int xOffset, yDownOffset, yNextBottom, blockOutSize;
    private final int xNewPosS, yNewPosS, xNewPosE, yNewPosE;
    private final int xNextNextPosS, yNextNextPosS, xNextNextPosE, yNextNextPosE;
    public int touchIndex;

    public TouchEvent (GameInfo gameInfo) {
        this.gameInfo = gameInfo;
        xOffset = gameInfo.xOffset; yDownOffset = gameInfo.yDownOffset;
        blockOutSize = gameInfo.blockOutSize;
        xBlockCnt = gameInfo.xBlockCnt;

        yNextBottom = gameInfo.yNextPos + blockOutSize + 4;

        xNewPosS = gameInfo.xNewPos;
        xNewPosE = xNewPosS + gameInfo.blockOutSize*2/3;
        yNewPosS = gameInfo.yNewPosS;
        yNewPosE = yNewPosS+ gameInfo.blockOutSize*2/3;

        xNextNextPosS = gameInfo.xNextNextPos;
        yNextNextPosS = gameInfo.yNextNextPos;
        xNextNextPosE = xNextNextPosS + blockOutSize/2;
        yNextNextPosE = yNextNextPosS + blockOutSize/2;

    }
    
    public void check(MotionEvent event) {
        Log.w("touch check", event.getActionMasked()+"  "+event.getX()+" x "+event.getY());
        // Handle user input touch event actions
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if (gameInfo.poolAniSize != 0) // if animation not completed
                    return;              // ignore touch Up
                xTouchPos = (int) event.getX();
                yTouchPos = (int) event.getY();
                if (yTouchPos < yDownOffset)
                    return;
                if (gameInfo.newGamePressed) {
                    if (gameInfo.isGameOver || isYesPressed()) {
                        gameInfo.newGamePressed = false;
                        gameInfo.newGameStart = true;
                    } else if (isNoPressed()) {
                        gameInfo.newGamePressed = false;
                        gameInfo.newGameStart = false;
                    }

                } else if (isNextPressed()) {
                    gameInfo.showNext = !gameInfo.showNext;

                } else if (isNewGamePressed()) {
                    gameInfo.newGamePressed = true;

                } else if (gameInfo.quitPressed) {
                    if (isYesPressed()) {
                        gameInfo.quitPressed = false;
                        gameInfo.quitGame = true;
                    } else if (isNoPressed()) {
                        gameInfo.quitPressed = false;
                        gameInfo.quitGame = false;
                    }
                } else if (isShootPressed()) {
                    xTouchPos -= xOffset;
                    if (xTouchPos > 0) {
                        touchIndex = xTouchPos / blockOutSize;
                        if (touchIndex < xBlockCnt) {
                            gameInfo.blockClicked = true;
                            gameInfo.touchIndex = touchIndex;
                        }
                    }

                } else if (yTouchPos > yNextBottom + 200){
                    gameInfo.dumpCellClicked = true;
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

    boolean isYesPressed() {
        return  (!gameInfo.isGameOver) &&
                (xTouchPos >= xNewPosE &&
                xTouchPos <= xNewPosE + gameInfo.blockOutSize*4/5 &&
                yTouchPos >= yNewPosS && yTouchPos <= yNewPosE);
    }

    boolean isNoPressed() {
        return  (!gameInfo.isGameOver) &&
                (xTouchPos >= xNewPosE + gameInfo.blockOutSize*4/5 &&
                xTouchPos <= xNewPosE + gameInfo.blockOutSize*8/5 &&
                yTouchPos >= yNewPosS && yTouchPos <= yNewPosE);

    }

    boolean isShootPressed() {
        return  (!gameInfo.isGameOver) && (yTouchPos <= yNextBottom);
    }


    boolean isNextPressed() {
        return (xTouchPos >= xNextNextPosS && xTouchPos <= xNextNextPosE &&
                yTouchPos >= yNextNextPosS && yTouchPos <= yNextNextPosE);
    }

}