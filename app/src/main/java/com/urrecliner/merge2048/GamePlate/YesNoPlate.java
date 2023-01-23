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
    private final int blockIconSize, blockOutSize;
    private final Paint yesNoPaint;
    private boolean yesNo;
    private long yesNoWaitTime;

    public YesNoPlate(GInfo gInfo, Context context) {
        this.gInfo = gInfo;
        this.blockIconSize = gInfo.blockIconSize;
        this.blockOutSize = gInfo.blockOutSize;
        xYesPos = gInfo.xYesPos;
        yYesPos = gInfo.yYesPos;
        xNopPos = gInfo.xNopPos;
        yNopPos = gInfo.yNopPos;
        yesMap = new ScaleMap().build(context, R.drawable.a_yes, blockOutSize);
        yesSmallMap = new ScaleMap().blink(yesMap, blockOutSize);
        noMap = new ScaleMap().build(context, R.drawable.a_no, blockOutSize);
        noSmallMap = new ScaleMap().blink(noMap, blockOutSize);
        yesNoPaint = new Paint();
        yesNoPaint.setColor(Color.WHITE);
        yesNoPaint.setStrokeWidth(6);
        yesNoPaint.setStyle(Paint.Style.STROKE);

    }

    public void draw(Canvas canvas) {

        if (gInfo.newGamePressed || gInfo.quitGamePressed || (gInfo.isGameOver && gInfo.is2048)) {
            canvas.drawRoundRect(xYesPos - 2, yYesPos - 2,
                    xNopPos + blockOutSize + 2, yNopPos + blockOutSize + 2,
                    4, 4, yesNoPaint);
            canvas.drawBitmap((yesNo) ? yesMap : yesSmallMap, xYesPos, yYesPos, null);
            canvas.drawBitmap((yesNo) ? noMap : noSmallMap, xNopPos, yNopPos, null);
            if (yesNoWaitTime < System.currentTimeMillis()) {
                yesNoWaitTime = System.currentTimeMillis() + 456;
                yesNo = !yesNo;
            }
        }
    }
}