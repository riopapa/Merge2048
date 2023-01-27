package com.urrecliner.merge2048.GamePlate;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.urrecliner.merge2048.GInfo;
import com.urrecliner.merge2048.R;
import com.urrecliner.merge2048.Sub.ScaleMap;

public class YesNoPlate {

    private final GInfo gInfo;
    private final int xYesPos, xNopPos, yYesPos, yNopPos;
    private final Bitmap yesMap, yesSmallMap, noMap, noSmallMap;
    private final int blockSize;
    private final Paint yesNoPaint;
    private boolean yesNo;
    private long yesNoWaitTime;

    public YesNoPlate(GInfo gInfo, Context context) {
        this.gInfo = gInfo;
        this.blockSize = gInfo.blockInSize + gInfo.blockInSize;
        xYesPos = gInfo.xYesPos;
        yYesPos = gInfo.yYesPos;
        xNopPos = gInfo.xNopPos;
        yNopPos = gInfo.yNopPos;
        yesMap = new ScaleMap().build(context, R.drawable.a_yes, blockSize);
        yesSmallMap = new ScaleMap().blink(yesMap, blockSize);
        noMap = new ScaleMap().build(context, R.drawable.a_no, blockSize);
        noSmallMap = new ScaleMap().blink(noMap, blockSize);
        yesNoPaint = new Paint();
        yesNoPaint.setColor(Color.WHITE);
        yesNoPaint.setStrokeWidth(12);
        yesNoPaint.setStyle(Paint.Style.STROKE);
    }

    public void draw(Canvas canvas) {

        if (gInfo.newGamePressed || gInfo.quitGamePressed || (gInfo.isGameOver && gInfo.is2048)) {
            canvas.drawRoundRect(xYesPos - 8, yYesPos - 8,
                    xNopPos + blockSize + 8, yNopPos + blockSize + 8,
                    64, 64, yesNoPaint);
            canvas.drawBitmap((yesNo) ? yesMap : yesSmallMap, xYesPos, yYesPos, null);
            canvas.drawBitmap((yesNo) ? noMap : noSmallMap, xNopPos, yNopPos, null);
            if (yesNoWaitTime < System.currentTimeMillis()) {
                yesNoWaitTime = System.currentTimeMillis() + 678;
                yesNo = !yesNo;
            }
        }
    }
}