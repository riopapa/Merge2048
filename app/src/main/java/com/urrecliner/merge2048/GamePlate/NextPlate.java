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
    int xNextNextPos, yNextNextPos;
    public int nextIndex, nextNextIndex;
    Bitmap nextNoMap;

    public NextPlate(GameInfo gameInfo, Context context) {
        this.gameInfo = gameInfo;
        this.context = context;
        this.blockInSize = gameInfo.blockInSize;
        this.blockOutSize = gameInfo.blockOutSize;

        nextPaint = new Paint();
        nextPaint.setColor(Color.WHITE);
        nextPaint.setStyle(Paint.Style.STROKE);
        nextPaint.setStrokeWidth(4);

        nextIndex = new Random().nextInt(gameInfo.gameDifficulty) + 1;
        nextNextIndex = new Random().nextInt(gameInfo.gameDifficulty) + 1;

        xNextNextPos = gameInfo.xNextPos + gameInfo.blockOutSize / 4;
        yNextNextPos = gameInfo.yNewPosS;

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.a_next);
        nextNoMap = Bitmap.createScaledBitmap(bitmap, blockInSize/2, blockInSize/2, false);

    }

    public void generateNextBlock() {
        nextIndex = nextNextIndex;
        nextNextIndex = new Random().nextInt(gameInfo.gameDifficulty) + 1;
        if (new Random().nextInt(8) == 0)
            nextNextIndex = new Random().nextInt(gameInfo.gameDifficulty+ 1) + 1;
        if (new Random().nextInt(12) == 0)
            nextNextIndex = new Random().nextInt(gameInfo.gameDifficulty+ 3) + 1;
    }

    public void draw(Canvas canvas, Bitmap nextBlockMap, Bitmap nextNextHalfMap) {

        if (!gameInfo.swing)    // if on swing, no outer box
            canvas.drawRoundRect(gameInfo.xNextPos, gameInfo.yNextPos,
                    gameInfo.xNextPos + blockInSize, gameInfo.yNextPos + blockInSize,
                blockInSize/8f,blockInSize/8f, nextPaint);
        if (nextBlockMap != null)
            canvas.drawBitmap(nextBlockMap, gameInfo.xNextPos, gameInfo.yNextPos,null);

        // draw Next Next //
        canvas.drawRoundRect(xNextNextPos, yNextNextPos,
                xNextNextPos +blockInSize/2f, yNextNextPos +blockInSize/2f,
                blockInSize/16f,blockInSize/16f, nextPaint);
        if (gameInfo.showNext)
            canvas.drawBitmap(nextNextHalfMap, xNextNextPos, yNextNextPos,null);
        else
            canvas.drawBitmap(nextNoMap, xNextNextPos, yNextNextPos,null);
    }

}