package com.urrecliner.merge2048.GamePlate;

/*
 * Purpose of this Ani module is to draw, animate Game Plate
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.urrecliner.merge2048.GameInfo;

public class GameOver {

    Context context;
    GameInfo gameInfo;
    Paint overPaint;
    int overXPos, overYPos;

    public GameOver(GameInfo gameInfo, Context context){
        this.gameInfo = gameInfo;
        this.context = context;

        overPaint = new Paint();
        overPaint.setTextSize(200);
        overPaint.setColor(Color.RED);
        overPaint.setTextAlign(Paint.Align.CENTER);
        overPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        overPaint.setStrokeWidth(32);
        overXPos = gameInfo.screenXSize/2;
        overYPos = gameInfo.yDownOffset/2;

    }

    public void draw(Canvas canvas) {
        overPaint.setStrokeWidth(64);
        overPaint.setColor(Color.BLUE);
        canvas.drawText("Game Over", overXPos, overYPos, overPaint);
        overPaint.setColor(Color.RED);
        overPaint.setStrokeWidth(0);
        canvas.drawText("Game Over", overXPos, overYPos, overPaint);
    }

}