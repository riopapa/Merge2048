package com.urrecliner.merge2048.GamePlate;

/*
 * Purpose of this Animation module is to draw, animate Game Over
 */

import android.content.Context;
import android.graphics.Canvas;
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
        gOverPaint.setTextSize(gInfo.piece + gInfo.piece);
        gOverPaint.setTextAlign(Paint.Align.CENTER);
        gOverPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        gOverPaint.setStrokeWidth(32);
        overColor0 = ContextCompat.getColor(context, R.color.game_over0);
        overColor1 = ContextCompat.getColor(context, R.color.game_over1);
        waitCount = 100;

        xOverPos = gInfo.screenXSize/2;
        yOverPos = gInfo.yDownOffset/2;
        yOverPosTop = gInfo.blockIconSize * 2;
        yOverPosBottom = gInfo.blockOutSize * (gInfo.Y_BLOCK_CNT -1);
        yOverInc = gInfo.blockInSize / 32;
    }

    public void draw(Canvas canvas) {
        if (!gInfo.isGameOver)
            return;
        if (gInfo.aniStacks.size() > 0)
            return;
        waitCount++;
        if (waitCount > 30) {
            waitCount = 0;
            showSwitch = !showSwitch;
        }
        yOverPos += yOverInc;
        if (yOverPos > yOverPosBottom || yOverPos < yOverPosTop) {
            yOverInc = - yOverInc;
        }

        gOverPaint.setStrokeWidth(40);
        gOverPaint.setTextSize(gInfo.piece + gInfo.piece/3f);
        gOverPaint.setColor((showSwitch) ? overColor0 : overColor1);
        canvas.drawText("Game Over", xOverPos, yOverPos, gOverPaint);
        gOverPaint.setColor((showSwitch) ? overColor1 : overColor0);
        gOverPaint.setStrokeWidth(12);
        canvas.drawText("Game Over", xOverPos, yOverPos, gOverPaint);

        if (gInfo.is2048) {
            gOverPaint.setTextSize(gInfo.piece*8/10f);
            gOverPaint.setStrokeWidth(20);
            gOverPaint.setColor((showSwitch) ? overColor0 : overColor1);
            canvas.drawText("2048 이상을 지우고", xOverPos, yOverPos+208, gOverPaint);
            gOverPaint.setColor((showSwitch) ? overColor1 : overColor0);
            gOverPaint.setStrokeWidth(2);
            canvas.drawText("2048 이상을 지우고", xOverPos, yOverPos+208, gOverPaint);
            gOverPaint.setTextSize(gInfo.piece*8/10f);
            gOverPaint.setStrokeWidth(20);
            gOverPaint.setColor((showSwitch) ? overColor0 : overColor1);
            canvas.drawText("계속할까요?", xOverPos, yOverPos+408, gOverPaint);
            gOverPaint.setColor((showSwitch) ? overColor1 : overColor0);
            gOverPaint.setStrokeWidth(2);
            canvas.drawText("계속할까요?", xOverPos, yOverPos+408, gOverPaint);
        }
    }

}