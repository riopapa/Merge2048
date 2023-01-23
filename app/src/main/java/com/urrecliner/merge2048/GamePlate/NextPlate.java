package com.urrecliner.merge2048.GamePlate;

/*
 * draw Next, NextNext block, also in swap icon area
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
    final int blockOutSize, blockInSize, bOffset;
    final int xNextNextPos, yNextNextPos, xSwapPos, ySwapPos;
    final int swapSize;

    final Bitmap nextHideMap, swapMap;

    Paint nextPaint;

    public int nextIndex, nextNextIndex;

    public int swingXInc, swingXMaxLeft, swingXMaxRight;
    long swingTime, swingDelay;

    public NextPlate(GInfo gInfo, Context context, List<BlockImage> blockImages) {
        this.gInfo = gInfo;
        this.blockImages = blockImages;
        this.blockInSize = gInfo.blockInSize;
        this.blockOutSize = gInfo.blockOutSize;
        swapSize = gInfo.blockIconSize*2/3;
        this.bOffset = gInfo.blockIconSize - swapSize;

        nextPaint = new Paint();
        nextPaint.setColor(Color.WHITE);
        nextPaint.setStyle(Paint.Style.STROKE);
        nextPaint.setStrokeWidth(8);

        nextIndex = new Random().nextInt(gInfo.gameDifficulty) + 1;
        nextNextIndex = new Random().nextInt(gInfo.gameDifficulty) + 1;

        swingXInc = blockOutSize / 5;   // 48
        swingXMaxLeft = gInfo.xOffset - 32;   // 78
        swingXMaxRight = gInfo.screenXSize - gInfo.xOffset -blockOutSize + 32;  // 1118

        xNextNextPos = gInfo.xNextPos + gInfo.blockOutSize / 4;
        yNextNextPos = gInfo.yNewPos;
        xSwapPos = gInfo.xSwapPos;
        ySwapPos = gInfo.ySwapPos;

        nextHideMap = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(context.getResources(),
                R.drawable.a_next_hide), blockInSize/2, blockInSize/2, false);
        swapMap = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.a_swap), gInfo.blockIconSize, gInfo.blockIconSize, false);
    }

    public void setNextBlock() {
        nextIndex = nextNextIndex;
        nextNextIndex = new Random().nextInt(gInfo.gameDifficulty) + 1;
        if (new Random().nextInt(10) == 0)
            nextNextIndex = new Random().nextInt(gInfo.gameDifficulty-1) + 2;
    }

    public void swapNextBlock() {
        int sv = nextIndex;
        nextIndex = nextNextIndex;
        nextNextIndex = sv;
    }

    public void draw(Canvas canvas) {

        if (!gInfo.swing)    // if on swing, no outer box
            canvas.drawRoundRect(gInfo.xNextPos, gInfo.yNextPos,
                    gInfo.xNextPos + blockInSize, gInfo.yNextPos + blockInSize,
                blockInSize/8f,blockInSize/8f, nextPaint);

        // draw Next Next //
        canvas.drawRoundRect(xNextNextPos, yNextNextPos,
                xNextNextPos +blockInSize/2f, yNextNextPos +blockInSize/2f,
                blockInSize/16f,blockInSize/16f, nextPaint);

        if (nextIndex == -1)
            return;
        if (gInfo.swing)
            updateSwing();

        canvas.drawBitmap(blockImages.get(nextIndex).bitmap, gInfo.xNextPos, gInfo.yNextPos, null);

        if (gInfo.swapCount > 0)
            canvas.drawBitmap(blockImages.get(nextIndex).halfMap, xSwapPos, ySwapPos, null);

        if (gInfo.showNext) {
            canvas.drawBitmap(blockImages.get(nextNextIndex).halfMap, xNextNextPos, yNextNextPos, null);
            if (gInfo.swapCount > 0)
                canvas.drawBitmap(blockImages.get(nextNextIndex).halfMap, xSwapPos+bOffset, ySwapPos+bOffset, null);
        } else {
            canvas.drawBitmap(nextHideMap, xNextNextPos, yNextNextPos, null);
            if (gInfo.swapCount > 0)
                canvas.drawBitmap(nextHideMap, xSwapPos+bOffset, ySwapPos+bOffset, null);
        }

        canvas.drawBitmap(swapMap, xSwapPos, ySwapPos,null);
    }


    void updateSwing() {
        if (System.currentTimeMillis() > swingTime) {   // start swing left, right
            gInfo.xNextPos += swingXInc;
            if (gInfo.xNextPos > swingXMaxRight) {
                gInfo.xNextPos = swingXMaxRight;
                swingXInc = -swingXInc;
            } else if (gInfo.xNextPos < swingXMaxLeft) {
                gInfo.xNextPos = swingXMaxLeft;
                swingXInc = -swingXInc;
            }
            swingTime = System.currentTimeMillis() + swingDelay;
        }
    }
    public void resetSwing() {

        gInfo.xNextPos = gInfo.xNextPosFixed;
        swingDelay = 300 / (gInfo.gameDifficulty+2);
        swingXInc = blockOutSize / 6;
        swingTime = System.currentTimeMillis() + swingDelay;
    }

}