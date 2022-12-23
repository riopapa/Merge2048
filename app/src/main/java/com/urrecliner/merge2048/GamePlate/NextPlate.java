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

import com.urrecliner.merge2048.GameInfo;
import com.urrecliner.merge2048.R;

import java.util.Random;

public class NextPlate {

    Context context;
    GameInfo gameInfo;
    Paint nextPaint;
    int blockOutSize, blockInSize;
    int nextXPos, nextYPos, xNextNextPos, yNextNextPos;
    public int nextIndex, nNextIndex;
    Bitmap nextNoMap;

    public NextPlate(GameInfo gameInfo, Context context) {
        this.gameInfo = gameInfo;
        this.context = context;
        this.blockInSize = gameInfo.blockInSize;
        this.blockOutSize = gameInfo.blockOutSize;

        nextPaint = new Paint();
        nextPaint.setColor(Color.WHITE);
        nextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        nextPaint.setStrokeWidth(4);

        nextXPos = gameInfo.xNextPos-2;
        nextYPos = gameInfo.yNextPos-2;
        xNextNextPos = nextXPos + (blockOutSize /4);
        yNextNextPos = nextYPos + blockOutSize + 24;
        gameInfo.xNextNextPos = xNextNextPos;
        gameInfo.yNextNextPos = yNextNextPos;
        nextIndex = new Random().nextInt(3) + 1;
        nNextIndex = new Random().nextInt(4) + 1;

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.i_next);
        nextNoMap = Bitmap.createScaledBitmap(bitmap, blockInSize/2, blockInSize/2, false);

    }

    public void generateNextBlock() {
        nextIndex = nNextIndex;
        nNextIndex = new Random().nextInt(5) + 1;
        if (new Random().nextInt(5) > 3)
            nNextIndex = new Random().nextInt(6) + 2;
    }

    public void draw(Canvas canvas, Bitmap blockMap, Bitmap halfMap) {

        canvas.drawRoundRect(nextXPos, nextYPos,
                nextXPos + blockInSize, nextYPos + blockInSize,
                blockInSize/8f,blockInSize/8f, nextPaint);

        canvas.drawBitmap(blockMap, nextXPos, nextYPos,null);

        // draw Next Next //
        canvas.drawRoundRect(xNextNextPos, yNextNextPos,
                xNextNextPos +blockInSize/2f, yNextNextPos +blockInSize/2f,
                blockInSize/16f,blockInSize/16f, nextPaint);
        if (gameInfo.showNext)
            canvas.drawBitmap(halfMap, xNextNextPos, yNextNextPos,null);
        else
            canvas.drawBitmap(nextNoMap, xNextNextPos, yNextNextPos,null);
    }

}