package com.urrecliner.merge2048.GamePlate;

/*
 * Purpose of this Animation module is to draw, animate Game Plate
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.urrecliner.merge2048.GInfo;

public class GameOverPlate {

    final Context context;
    final GInfo gInfo;
    Paint overPaint;
    boolean showSwitch = false;
    int waitCount;
    final int xOverPos, yOverPosTop, yOverPosBottom;
    int  yOverPos, yOverInc;

    public GameOverPlate(GInfo gInfo, Context context){
        this.gInfo = gInfo;
        this.context = context;

        overPaint = new Paint();
        overPaint.setTextSize(gInfo.pxcl+gInfo.pxcl);
        overPaint.setColor(Color.RED);
        overPaint.setTextAlign(Paint.Align.CENTER);
        overPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        overPaint.setStrokeWidth(32);
        waitCount = 100;

        xOverPos = gInfo.screenXSize/2;
        yOverPos = gInfo.yDownOffset/2;;
        yOverPosTop = gInfo.blockIconSize * 2;
        yOverPosBottom = gInfo.blockOutSize * (gInfo.yBlockCnt -1);
        yOverInc = gInfo.blockInSize / 32;
    }

    public void draw(Canvas canvas) {
        if (!gInfo.isGameOver)
            return;
        waitCount++;
        if (waitCount > 10) {
            waitCount = 0;
            showSwitch = !showSwitch;
        }
        yOverPos += yOverInc;
        if (yOverPos > yOverPosBottom || yOverPos < yOverPosTop) {
            yOverInc = - yOverInc;
        }

        overPaint.setStrokeWidth(64);
        overPaint.setColor((showSwitch) ? Color.BLUE : Color.RED);
        canvas.drawText("Game Over", xOverPos, yOverPos, overPaint);
        overPaint.setColor((showSwitch) ? Color.RED : Color.BLUE);
        overPaint.setStrokeWidth(0);
        canvas.drawText("Game Over", xOverPos, yOverPos, overPaint);

    }

}