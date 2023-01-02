package com.urrecliner.merge2048.GamePlate;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.urrecliner.merge2048.GInfo;
import com.urrecliner.merge2048.GameObject.HighMember;
import com.urrecliner.merge2048.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ScorePlate {
    Context context;
    long scoreTimeStamp = 0;
    Paint scoreOPaint, scoreIPaint, hTextPaint, hScoreOPaint, hScoreIPaint, board1Paint, board2Paint;
    final int gameScoreXPos, gameScoreYPos;
    final int xBoardPosLeft, xBoardPosRight, yBoardPosTop, yBoardSize, xBoardPosWho, xBoardPosTime, xBoardPosScore;
    GInfo gInfo;
    List<HighMember> highMembers;
    final SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm", Locale.US);

    public ScorePlate(GInfo gInfo, Context context) {
        this.gInfo = gInfo;
        this.context = context;
        this.highMembers = gInfo.highMembers;

        gameScoreXPos = gInfo.xNextPos/2 + gInfo.xOffset;
        gameScoreYPos = gInfo.yNewPosS + gInfo.blockInSize/2;
        scoreOPaint = new Paint();
        scoreOPaint.setTypeface(ResourcesCompat.getFont(context, R.font.old_english));
        scoreOPaint.setTextAlign(Paint.Align.CENTER);
        scoreOPaint.setTextSize(gInfo.pxcl);
        scoreOPaint.setStrokeWidth(12);
        scoreOPaint.setLetterSpacing(0.1f);
        scoreOPaint.setColor(Color.BLUE);
        scoreOPaint.setStyle(Paint.Style.STROKE);

        scoreIPaint = new Paint();
        scoreIPaint.setTypeface(ResourcesCompat.getFont(context, R.font.old_english));
        scoreIPaint.setTextAlign(Paint.Align.CENTER);
        scoreIPaint.setTextSize(gInfo.pxcl);
        scoreIPaint.setStrokeWidth(0);
        scoreIPaint.setLetterSpacing(0.1f);
        scoreIPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        scoreIPaint.setColor(ContextCompat.getColor(context, R.color.score));

        xBoardPosLeft = calcPixel(48);
        xBoardPosRight = gInfo.xNewPos - 8;

        yBoardPosTop = gInfo.yNewPosS + gInfo.blockInSize*4/5;

        xBoardPosWho = gInfo.xOffset + calcPixel(80);

        xBoardPosTime = xBoardPosWho + 32;

        board1Paint = new Paint();
        board1Paint.setColor(ContextCompat.getColor(context, R.color.board0));
        board2Paint = new Paint();
        board2Paint.setColor(ContextCompat.getColor(context, R.color.board1));
        board2Paint.setStrokeWidth(4);

        int height, texSize = gInfo.pxcl;
        hTextPaint = new Paint();
        hTextPaint.setTypeface(ResourcesCompat.getFont(context, R.font.steelfish_rg));
        hTextPaint.setColor(Color.WHITE);
        while (true) {
            hTextPaint.setTextSize(texSize);
            Rect rect = new Rect();
            hTextPaint.getTextBounds("Me 09", 0, 5, rect);
            height = rect.height();
            if (height > calcPixel(48)) {
                texSize -=2;
            } else
                break;
        }
        yBoardSize = height + 32;

        xBoardPosScore = xBoardPosRight - (xBoardPosRight - xBoardPosTime - (int) hTextPaint.measureText("22-12-31 24:31"))/2;

        hScoreOPaint = new Paint();
        hScoreOPaint.setTypeface(ResourcesCompat.getFont(context, R.font.ticking_regular));
        hScoreOPaint.setTextSize(texSize);
        hScoreOPaint.setStyle(Paint.Style.STROKE);
        hScoreOPaint.setLetterSpacing(0.05f);
        hScoreOPaint.setColor(Color.BLUE);
        hScoreOPaint.setStrokeWidth(6);
        hScoreOPaint.setTextAlign(Paint.Align.CENTER);

        hScoreIPaint = new Paint();
        hScoreIPaint.setTypeface(ResourcesCompat.getFont(context, R.font.ticking_regular));
        hScoreIPaint.setTextSize(texSize);
        hScoreIPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        hScoreIPaint.setLetterSpacing(0.05f);
        hScoreIPaint.setColor(ContextCompat.getColor(context, R.color.score));
        hScoreIPaint.setTextAlign(Paint.Align.CENTER);

    }

    private int calcPixel(int milliPercent) {
        return gInfo.screenXSize * milliPercent / 1000;
    }

    public void draw(Canvas canvas) {

        canvas.drawText(""+ gInfo.scoreNow, gameScoreXPos, gameScoreYPos, scoreOPaint);
        canvas.drawText(""+ gInfo.scoreNow, gameScoreXPos, gameScoreYPos, scoreIPaint);

        if (gInfo.score2Add > 0 && scoreTimeStamp < System.currentTimeMillis()) {
            scoreTimeStamp = System.currentTimeMillis() + 40;
            int score = gInfo.score2Add / 6;
            if (score < 2)
                score = 2;
            else
                score = (score / 2) * 2;
            gInfo.score2Add -= score;
            gInfo.scoreNow += score;
        }

        for (int i = 0; i < highMembers.size(); i++) {
            int y = yBoardPosTop + i*yBoardSize;
            canvas.drawRoundRect(xBoardPosLeft-8, y,
                    xBoardPosRight, y+yBoardSize-4, 16,16, board2Paint);
            canvas.drawRoundRect(xBoardPosLeft-4, y+4,
                    xBoardPosRight-8, y+yBoardSize-12, 16,16, board1Paint);
            HighMember highMember = highMembers.get(i);
            String when = sdf.format(highMember.when);
            hTextPaint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(highMember.who, xBoardPosWho, y+yBoardSize-24, hTextPaint);
            hTextPaint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText(when, xBoardPosTime, y+yBoardSize-24, hTextPaint);
            canvas.drawText(""+highMember.score, xBoardPosScore, y+yBoardSize-28, hScoreOPaint);
            canvas.drawText(when, xBoardPosTime, y+yBoardSize-24, hTextPaint);
            canvas.drawText(""+highMember.score, xBoardPosScore, y+yBoardSize-28, hScoreIPaint);
        }

    }
}