package com.urrecliner.merge2048.GamePlate;

/*
 * Purpose of this Animation module is to draw, animate Game Over
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.urrecliner.merge2048.GInfo;
import com.urrecliner.merge2048.R;

public class GameOverPlate {

    final GInfo gInfo;
    Paint gOverPaint;
    boolean showSwitch = false;
    int waitCount;
    final int xOverPos, yOverPosTop, yOverPosBottom;
    int  yOverPos, yOverInc;
    final int overColor0, overColor1;

    public GameOverPlate(GInfo gInfo, Context context){
        this.gInfo = gInfo;
        gOverPaint = new Paint();
        gOverPaint.setTextSize(gInfo.piece +gInfo.piece);
        gOverPaint.setColor(Color.RED);
        gOverPaint.setTextAlign(Paint.Align.CENTER);
        gOverPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        gOverPaint.setStrokeWidth(32);
        overColor0 = ContextCompat.getColor(context, R.color.game_over0);
        overColor1 = ContextCompat.getColor(context, R.color.game_over1);
        waitCount = 100;

        xOverPos = gInfo.screenXSize/2;
        yOverPos = gInfo.yDownOffset/2;
        yOverPosTop = gInfo.blockIconSize * 2;
        yOverPosBottom = gInfo.blockOutSize * (gInfo.yBlockCnt -1);
        yOverInc = gInfo.blockInSize / 32;
    }

    public void draw(Canvas canvas) {
        if (!gInfo.isGameOver)
            return;
        waitCount++;
        if (waitCount > 12) {
            waitCount = 0;
            showSwitch = !showSwitch;
        }
        yOverPos += yOverInc;
        if (yOverPos > yOverPosBottom || yOverPos < yOverPosTop) {
            yOverInc = - yOverInc;
        }

        gOverPaint.setStrokeWidth(60);
        gOverPaint.setColor((showSwitch) ? overColor0 : overColor1);
        canvas.drawText("Game Over", xOverPos, yOverPos, gOverPaint);
        gOverPaint.setColor((showSwitch) ? overColor1 : overColor0);
        gOverPaint.setStrokeWidth(0);
        canvas.drawText("Game Over", xOverPos, yOverPos, gOverPaint);

    }

}