package com.urrecliner.merge2048.GamePlate;

/*
 * Purpose of this Ani module is to draw, animate Game Plate
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.urrecliner.merge2048.GameInfo;

public class GameOverPlate {

    Context context;
    GameInfo gameInfo;
    Paint overPaint;
    boolean showSwich = false;
    int waitCount;
    final int xOverPos, yOverPosTop, yOverPosBottom;
    int  yOverPos, yOverInc;

    public GameOverPlate(GameInfo gameInfo, Context context){
        this.gameInfo = gameInfo;
        this.context = context;

        overPaint = new Paint();
        overPaint.setTextSize(200);
        overPaint.setColor(Color.RED);
        overPaint.setTextAlign(Paint.Align.CENTER);
        overPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        overPaint.setStrokeWidth(32);
        waitCount = 100;

        xOverPos = gameInfo.screenXSize/2;
        yOverPos = gameInfo.yDownOffset/2;;
        yOverPosTop = gameInfo.blockIconSize * 2;
        yOverPosBottom = gameInfo.blockOutSize * (gameInfo.yBlockCnt -1);
        yOverInc = gameInfo.blockInSize / 32;
    }

    public void draw(Canvas canvas) {
        if (!gameInfo.isGameOver)
            return;
        waitCount++;
        if (waitCount > 10) {
            waitCount = 0;
            showSwich = !showSwich;
        }
        yOverPos += yOverInc;
        if (yOverPos > yOverPosBottom || yOverPos < yOverPosTop) {
            yOverInc = - yOverInc;
        }

        overPaint.setStrokeWidth(64);
        overPaint.setColor((showSwich) ? Color.BLUE : Color.RED);
        canvas.drawText("Game Over", xOverPos, yOverPos, overPaint);
        overPaint.setColor((showSwich) ? Color.RED : Color.BLUE);
        overPaint.setStrokeWidth(0);
        canvas.drawText("Game Over", xOverPos, yOverPos, overPaint);

    }

}