package com.urrecliner.merge2048.GamePlate;

/*
 *
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.urrecliner.merge2048.GInfo;
import com.urrecliner.merge2048.R;

import java.util.Random;

public class NextPlate {

    Context context;
    final GInfo gInfo;
    Paint nextPaint;
    int blockOutSize, blockInSize;
    int xNextNextPos, yNextNextPos;
    public int nextIndex, nextNextIndex;
    final Bitmap nextNoMap;

    public NextPlate(GInfo gInfo, Context context) {
        this.gInfo = gInfo;
        this.context = context;
        this.blockInSize = gInfo.blockInSize;
        this.blockOutSize = gInfo.blockOutSize;

        nextPaint = new Paint();
        nextPaint.setColor(Color.WHITE);
        nextPaint.setStyle(Paint.Style.STROKE);
        nextPaint.setStrokeWidth(4);

        nextIndex = new Random().nextInt(gInfo.gameDifficulty) + 1;
        nextNextIndex = new Random().nextInt(gInfo.gameDifficulty) + 1;

        xNextNextPos = gInfo.xNextPos + gInfo.blockOutSize / 4;
        yNextNextPos = gInfo.yNewPosS;

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.a_next);
        nextNoMap = Bitmap.createScaledBitmap(bitmap, blockInSize/2, blockInSize/2, false);

    }

    public void generateNextBlock() {
        nextIndex = nextNextIndex;
        nextNextIndex = new Random().nextInt(5) + 1;
        if (new Random().nextInt(10) < 3)
            return;
        nextNextIndex = new Random().nextInt(gInfo.gameDifficulty) + 1;
        if (new Random().nextInt(10) == 0) {
            nextNextIndex = new Random().nextInt(gInfo.gameDifficulty + 1) + 1;
            if (new Random().nextInt(10) == 0)
                nextNextIndex = new Random().nextInt(gInfo.gameDifficulty + 2) + 2;
        }
    }

    public void draw(Canvas canvas, Bitmap nextBlockMap, Bitmap nextNextHalfMap) {

        if (!gInfo.swing)    // if on swing, no outer box
            canvas.drawRoundRect(gInfo.xNextPos, gInfo.yNextPos,
                    gInfo.xNextPos + blockInSize, gInfo.yNextPos + blockInSize,
                blockInSize/8f,blockInSize/8f, nextPaint);
        if (nextBlockMap != null)
            canvas.drawBitmap(nextBlockMap, gInfo.xNextPos, gInfo.yNextPos,null);

        // draw Next Next //
        canvas.drawRoundRect(xNextNextPos, yNextNextPos,
                xNextNextPos +blockInSize/2f, yNextNextPos +blockInSize/2f,
                blockInSize/16f,blockInSize/16f, nextPaint);
        if (gInfo.showNext)
            canvas.drawBitmap(nextNextHalfMap, xNextNextPos, yNextNextPos,null);
        else
            canvas.drawBitmap(nextNoMap, xNextNextPos, yNextNextPos,null);
    }

}