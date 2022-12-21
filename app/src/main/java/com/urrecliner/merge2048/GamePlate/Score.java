package com.urrecliner.merge2048.GamePlate;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.core.content.res.ResourcesCompat;

import com.urrecliner.merge2048.GameInfo;
import com.urrecliner.merge2048.R;

public class Score {
    Context context;
    Paint textPaint, scorePaint;
    public int sXPos, sYPos, tYPos;
    GameInfo gameInfo;

    public Score (GameInfo gameInfo, Context context) {
        this.gameInfo = gameInfo;
        this.context = context;

        sXPos = gameInfo.screenXSize - 32;
        sYPos = gameInfo.yDownOffset + 94;
        scorePaint = new Paint();
        scorePaint.setTypeface(ResourcesCompat.getFont(context, R.font.radioland_regular));
        scorePaint.setColor(Color.MAGENTA);
        scorePaint.setTextSize(90);
        scorePaint.setTextAlign(Paint.Align.RIGHT);

        tYPos = gameInfo.yDownOffset +76;
        textPaint = new Paint();
        textPaint.setTypeface(ResourcesCompat.getFont(context, R.font.radioland_regular));
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(60);
        textPaint.setTextAlign(Paint.Align.RIGHT);
    }

    public void draw(Canvas canvas) {
        canvas.drawText(""+ gameInfo.scoreNow, sXPos, sYPos, scorePaint);
        float scoreWidth = scorePaint.measureText(""+gameInfo.scoreNow);
        canvas.drawText("Score:", sXPos-scoreWidth-12, tYPos, textPaint);
    }
}