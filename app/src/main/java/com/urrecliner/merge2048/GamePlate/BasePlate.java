package com.urrecliner.merge2048.GamePlate;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.urrecliner.merge2048.GInfo;
import com.urrecliner.merge2048.R;

public class BasePlate {

    Context context;
    final GInfo gInfo;
    final Paint backPaint, vPath1Paint, vPath2Paint, yesNoPaint, horizonPaint;
    private final int xBlockCnt, yBlockCnt;
    private final int blockOutSize, blockIconSize;
    private final int xOffset, yUpOffset, xRight, yLinePos;
    private final int xLeft, yNextBottom;
    private final int xNewPos, yNewPos, xQuitPos, yQuitPos, xYesPos, xNopPos, xSwingPos, ySwingPos;
    private final Bitmap newMap, yesMap, yesSmallMap, noMap, noSmallMap, swingOnMap, swingOffMap, quitMap;

    private boolean yesNo;
    private long yesNoTime;

    public BasePlate(GInfo gInfo, Context context) {
        this.gInfo = gInfo;
        this.context = context;

        backPaint = new Paint();
        backPaint.setColor(ContextCompat.getColor(context, R.color.game_background));
        xBlockCnt = gInfo.X_BLOCK_CNT;
        yBlockCnt = gInfo.Y_BLOCK_CNT;
        blockOutSize = gInfo.blockOutSize;
        blockIconSize = gInfo.blockIconSize;

        xOffset = gInfo.xOffset;
        yUpOffset = gInfo.yUpOffset;
        vPath1Paint = new Paint();
        vPath1Paint.setColor(ContextCompat.getColor(context, R.color.board0));
        vPath1Paint.setAlpha(230);
        vPath2Paint = new Paint();
        vPath2Paint.setColor(ContextCompat.getColor(context, R.color.board1));
        vPath2Paint.setAlpha(230);

        xLeft = xOffset-4;
        yNextBottom = gInfo.yNextPos + blockOutSize + 4;
        xRight = gInfo.screenXSize - xOffset;
        xNewPos = gInfo.xNewPos;
        yNewPos = gInfo.yNewPos;
        xQuitPos = gInfo.screenXSize-xOffset-blockIconSize; yQuitPos = gInfo.yNewPos;
        xYesPos = xNewPos + blockIconSize;
        xNopPos = xNewPos + blockIconSize * 2;
        xSwingPos = xNewPos; ySwingPos = yNewPos + blockIconSize;

        horizonPaint = new Paint();
        horizonPaint.setColor(ContextCompat.getColor(context, R.color.horizon_line));
        horizonPaint.setStrokeWidth(8);
        horizonPaint.setPathEffect(new DashPathEffect(new float[] {40, 8}, 0));
        yLinePos = (gInfo.yDownOffset + gInfo.yUpOffset + blockOutSize*yBlockCnt) / 2;

        yesNoPaint = new Paint();
        yesNoPaint.setColor(Color.BLUE);
        yesNoPaint.setStrokeWidth(6);
        yesNoPaint.setStyle(Paint.Style.STROKE);

        newMap = buildMap (R.drawable.a_new);
        yesMap = buildMap (R.drawable.a_yes);
        yesSmallMap = yesNoSmall(yesMap);
        noMap = buildMap (R.drawable.a_no);
        noSmallMap = yesNoSmall(noMap);
        quitMap = buildMap(R.drawable.a_quit);

        swingOnMap = buildMap(R.drawable.a_swing_on);
        swingOffMap = buildMap(R.drawable.a_swing_off);
    }

    Bitmap buildMap(int resId) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
        return Bitmap.createScaledBitmap(bitmap, blockIconSize-4, blockIconSize-4, false);
    }

    Bitmap yesNoSmall(Bitmap bitmap) {
        Bitmap bMap = Bitmap.createBitmap(blockIconSize, blockIconSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bMap);
        Bitmap sMap = Bitmap.createScaledBitmap(bitmap, blockIconSize-8, blockIconSize-8, false);
        canvas.drawBitmap(sMap, 4, 4, null);
        return bMap;
    }

    public void draw(Canvas canvas) {

        // back ground display
        canvas.drawRect(0, 0, gInfo.screenXSize, gInfo.screenYSize, backPaint);

        for (int x = 0; x <= xBlockCnt - 1; x++) {
            for (int y = 0; y < yBlockCnt; y++) {
                canvas.drawRect(xLeft + blockOutSize * x + 16,
                        yUpOffset + y*blockOutSize+16,
                        xLeft + blockOutSize * (x + 1) - 16,
                        yUpOffset + (y+1)*blockOutSize-16,
                        ((x+y)%2 == 0) ? vPath1Paint:vPath2Paint);
            }
            canvas.drawRect(xLeft + blockOutSize * x + 16, gInfo.yNextPos,
                    xOffset + blockOutSize * (x + 1) - 16, yNextBottom,
                    (x%2 == 0) ? vPath1Paint:vPath2Paint);
        }

        // horizon line at shoot
        canvas.drawLine(xLeft, yLinePos, xRight, yLinePos, horizonPaint);

        // new Icon
        canvas.drawBitmap(newMap, xNewPos, yNewPos,null);
        // quit Icon
        canvas.drawBitmap(quitMap, xQuitPos, yQuitPos,null);

        // yes, no
        if (gInfo.newGamePressed || gInfo.quitGamePressed || (gInfo.isGameOver && gInfo.is2048)) {
            canvas.drawRoundRect(xYesPos-2, yNewPos-2,
                    xNewPos+ blockIconSize*3, yNewPos+ blockIconSize,
                        4,4, yesNoPaint);
            canvas.drawBitmap((yesNo) ? yesMap : yesSmallMap, xYesPos, yNewPos,null);
            canvas.drawBitmap((yesNo) ? noMap : noSmallMap, xNopPos, yNewPos,null);
            if (yesNoTime <System.currentTimeMillis()) {
                yesNoTime = System.currentTimeMillis() + 222;
                yesNo = !yesNo;
            }
        }
        canvas.drawBitmap((gInfo.swing)? swingOnMap:swingOffMap, xSwingPos, ySwingPos,null);

    }

}