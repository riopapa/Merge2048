package com.urrecliner.merge2048.GamePlate;

/*
 * Purpose of this Ani module is to draw, animate Game Plate
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.urrecliner.merge2048.GameInfo;

import java.util.Random;

public class NextBlocks {

    Context context;
    GameInfo gameInfo;
    Paint nextPaint;
    int xBlockOutSize, yBlockOutSize;
    int nextXPos, nextYPos, nextNXPos, nextNYPos;
    public int nextIndex, nNextIndex;

    public NextBlocks(GameInfo gameInfo, Context context) {
        this.gameInfo = gameInfo;
        this.context = context;

        nextPaint = new Paint();
        nextPaint.setColor(Color.WHITE);
        nextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        nextPaint.setStrokeWidth(4);

        nextXPos = gameInfo.xNextPos-4;
        nextYPos = gameInfo.yNextPos-4;
        nextNXPos = nextXPos + (gameInfo.xBlockOutSize/4);
        nextNYPos = nextYPos + gameInfo.yBlockOutSize;

        nextIndex = new Random().nextInt(4) + 1;
        nNextIndex = new Random().nextInt(4) + 1;
    }

    public void generateNextBlock() {
        nextIndex = nNextIndex;
        nNextIndex = new Random().nextInt(6) + 1;
    }

    public void draw(Canvas canvas, Bitmap blockMap, Bitmap halfMap) {

        canvas.drawRoundRect(nextXPos, nextYPos,
                nextXPos + xBlockOutSize, nextYPos + yBlockOutSize,
                xBlockOutSize/8f,yBlockOutSize/8f, nextPaint);

        canvas.drawBitmap(blockMap, nextXPos+4, nextYPos+4,null);

        // draw Next Next //
        canvas.drawRoundRect(nextNXPos, nextNYPos,
                nextNXPos+xBlockOutSize/2f, nextNYPos+yBlockOutSize/2f,
                xBlockOutSize/16f,yBlockOutSize/16f, nextPaint);

        canvas.drawBitmap(halfMap, nextNXPos+2, nextNYPos+2,null);
    }

}