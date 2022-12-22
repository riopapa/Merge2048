package com.urrecliner.merge2048.GamePlate;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.core.content.res.ResourcesCompat;

import com.urrecliner.merge2048.GameInfo;
import com.urrecliner.merge2048.R;

public class ScorePlate {
    Context context;
    Paint textPaint, scorePaint;
    public int sXPos, sYPos, tXPos, tYPos;
    GameInfo gameInfo;
    final String scoreStr = "ScorePlate:";

    public ScorePlate(GameInfo gameInfo, Context context) {
        this.gameInfo = gameInfo;
        this.context = context;

        tXPos = 48;
        tYPos = gameInfo.yNextPos + gameInfo.blockOutSize + 112;
        textPaint = new Paint();
        textPaint.setTypeface(ResourcesCompat.getFont(context, R.font.radioland_regular));
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(48);
        textPaint.setTextAlign(Paint.Align.LEFT);

        sXPos = tXPos + 16 + (int) textPaint.measureText(scoreStr);
        sYPos = tYPos + 16;
        scorePaint = new Paint();
        scorePaint.setTypeface(ResourcesCompat.getFont(context, R.font.radioland_regular));
        scorePaint.setColor(Color.MAGENTA);
        scorePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        scorePaint.setTextSize(90);
        scorePaint.setTextAlign(Paint.Align.LEFT);

    }

    public void draw(Canvas canvas) {
        canvas.drawText(scoreStr, tXPos, tYPos, textPaint);
        scorePaint.setColor(Color.BLUE);
        scorePaint.setStrokeWidth(4);
        canvas.drawText(""+ gameInfo.scoreNow, sXPos, sYPos, scorePaint);
        scorePaint.setColor(Color.RED);
        scorePaint.setStrokeWidth(0);
        canvas.drawText(""+ gameInfo.scoreNow, sXPos-2, sYPos-2, scorePaint);
    }
}