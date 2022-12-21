package com.urrecliner.merge2048.GamePlate;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.urrecliner.merge2048.GameInfo;
import com.urrecliner.merge2048.R;

public class BackPlate {

    Context context;
    GameInfo gameInfo;
    Paint backPaint, vLinePaint, nextPaint;
    int xBlockCnt, yBlockCnt, xBlockOutSize, yBlockOutSize, xOffset, yUpOffset, yDownOffset;

    public BackPlate(GameInfo gameInfo, Context context) {
        this.gameInfo = gameInfo;
        this.context = context;

        backPaint = new Paint();
        backPaint.setColor(ContextCompat.getColor(context, R.color.c00000));
        xBlockCnt = gameInfo.xBlockCnt;
        yBlockCnt = gameInfo.yBlockCnt;
        xBlockOutSize = gameInfo.blockOutSize;
        yBlockOutSize = gameInfo.blockOutSize;
        xOffset = gameInfo.xOffset;
        yDownOffset = gameInfo.yDownOffset;
        vLinePaint = new Paint();
        vLinePaint.setStrokeWidth(6);
        vLinePaint.setColor(ContextCompat.getColor(context, R.color.ver_line));

    }

    public void draw(Canvas canvas) {

        canvas.drawRect(0, 0, gameInfo.screenXSize, gameInfo.screenYSize, backPaint);
        for (int x = 0; x <= xBlockCnt; x++) {
            canvas.drawLine(xOffset + xBlockOutSize * x - 4, yUpOffset,
                    xOffset + xBlockOutSize * x,
                    yUpOffset + yBlockOutSize * yBlockCnt, vLinePaint);
        }
        canvas.drawLine(xOffset - 4, yUpOffset - 4,
                xOffset + xBlockOutSize * xBlockCnt,
                yUpOffset -4, vLinePaint);
        canvas.drawLine(xOffset - 4, yUpOffset + yBlockOutSize * yBlockCnt + 4,
                xOffset + xBlockOutSize * xBlockCnt ,
                yUpOffset + yBlockOutSize * yBlockCnt + 4, vLinePaint);

    }

}