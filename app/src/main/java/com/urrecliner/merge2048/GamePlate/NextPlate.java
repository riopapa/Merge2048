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
import com.urrecliner.merge2048.GameImage.BlockImage;
import com.urrecliner.merge2048.R;

import java.util.List;
import java.util.Random;

public class NextPlate {

    final GInfo gInfo;
    final List<BlockImage> blockImages;
    final int blockOutSize, blockInSize;
    final int xNextNextPos, yNextNextPos;

    final Bitmap nextNoMap;

    Paint nextPaint;

    public int nextIndex, nextNextIndex;

    public NextPlate(GInfo gInfo, Context context, List<BlockImage> blockImages) {
        this.gInfo = gInfo;
        this.blockImages = blockImages;
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

        nextNoMap = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(context.getResources(),
                R.drawable.a_next), blockInSize/2, blockInSize/2, false);
    }

    public void generateNextBlock() {
        nextIndex = nextNextIndex;
        nextNextIndex = new Random().nextInt(gInfo.gameDifficulty-1) + 1;
        if (new Random().nextInt(10) < 4)
            return;
        nextNextIndex = new Random().nextInt(gInfo.gameDifficulty) + 1;
        if (new Random().nextInt(10) == 0) {
            nextNextIndex = new Random().nextInt(gInfo.gameDifficulty + 1) + 1;
        }
    }

    public void draw(Canvas canvas) {

        if (!gInfo.swing)    // if on swing, no outer box
            canvas.drawRoundRect(gInfo.xNextPos, gInfo.yNextPos,
                    gInfo.xNextPos + blockInSize, gInfo.yNextPos + blockInSize,
                blockInSize/8f,blockInSize/8f, nextPaint);
        if (nextIndex != -1)
            canvas.drawBitmap(blockImages.get(nextIndex).bitmap, gInfo.xNextPos, gInfo.yNextPos,null);

        // draw Next Next //
        canvas.drawRoundRect(xNextNextPos, yNextNextPos,
                xNextNextPos +blockInSize/2f, yNextNextPos +blockInSize/2f,
                blockInSize/16f,blockInSize/16f, nextPaint);
        if (gInfo.showNext)
            canvas.drawBitmap(blockImages.get(nextNextIndex).halfMap, xNextNextPos, yNextNextPos,null);
        else
            canvas.drawBitmap(nextNoMap, xNextNextPos, yNextNextPos,null);
    }

}