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
import java.util.Comparator;
import java.util.Locale;

public class ScorePlate {

    final GInfo gInfo;
    final int gameScoreXPos, gameScoreYPos;
    final int xBoardPosLeft, xBoardPosRight, yBoardPosTop, yBoardSize, xBoardPosWho, xBoardPosTime, xBoardPosScore;
    final int board_color0, board_color1;
    final SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm", Locale.US);

    long scoreTimeStamp = 0;
    Paint scoreOPaint, scoreIPaint, hTextPaint, hScoreOPaint, hScoreIPaint, board0Paint, board1Paint;
    boolean xor;
    int xorCount, delay;
    final String highHeart = "♥♥";

    public ScorePlate(GInfo gInfo, Context context) {
        this.gInfo = gInfo;

        gameScoreXPos = gInfo.xNextPosFixed /2 + gInfo.xOffset;
        gameScoreYPos = gInfo.yNewPos + gInfo.blockIconSize/2;
        scoreOPaint = new Paint();
//        scoreOPaint.setTypeface(ResourcesCompat.getFont(context, R.font.old_english));
        scoreOPaint.setTypeface(ResourcesCompat.getFont(context, R.font.ticking_regular));
        scoreOPaint.setTextAlign(Paint.Align.CENTER);
        scoreOPaint.setTextSize(gInfo.piece *8f/10);
        scoreOPaint.setStrokeWidth(6);
        scoreOPaint.setLetterSpacing(0.1f);
        scoreOPaint.setColor(Color.BLUE);
        scoreOPaint.setStyle(Paint.Style.STROKE);

        scoreIPaint = new Paint();
//        scoreIPaint.setTypeface(ResourcesCompat.getFont(context, R.font.old_english));
        scoreIPaint.setTypeface(ResourcesCompat.getFont(context, R.font.ticking_regular));
        scoreIPaint.setTextAlign(Paint.Align.CENTER);
        scoreIPaint.setTextSize(gInfo.piece *8f/10);
        scoreIPaint.setStrokeWidth(0);
        scoreIPaint.setLetterSpacing(0.1f);
        scoreIPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        scoreIPaint.setColor(ContextCompat.getColor(context, R.color.score));

        xBoardPosLeft = 32;
        xBoardPosRight = gInfo.xNextPosFixed + gInfo.blockOutSize - gInfo.piece / 8;
        yBoardPosTop = gInfo.yNewPos + gInfo.blockInSize*3/5;

        board0Paint = new Paint();
        board0Paint.setStrokeWidth(3);
        board1Paint = new Paint();
        board1Paint.setStrokeWidth(3);

        int width, height, scoreSize = gInfo.piece + gInfo.piece;
        hTextPaint = new Paint();
        hTextPaint.setTypeface(ResourcesCompat.getFont(context, R.font.steelfish_rg));
        hTextPaint.setColor(Color.WHITE);
        while (true) {
            hTextPaint.setTextSize(scoreSize);
            Rect rect = new Rect();
            hTextPaint.getTextBounds("RioPaPa", 0, 5, rect);
            height = rect.height();
            width = rect.width();
            if (height > calcPercentPixel(40)) {
                scoreSize -=2;
            } else
                break;
        }
//        xBoardPosWho = gInfo.xOffset + calcPercentPixel(80);
        xBoardPosWho = gInfo.xOffset + width + 36;
        xBoardPosTime = xBoardPosWho + 16;
        yBoardSize = height + 48;

        xBoardPosScore = xBoardPosRight - (xBoardPosRight - xBoardPosTime - (int) hTextPaint.measureText("22-12-31 24:31"))/2;

        hScoreOPaint = new Paint();
        hScoreOPaint.setTypeface(ResourcesCompat.getFont(context, R.font.ticking_regular));
        hScoreOPaint.setTextSize(scoreSize+6);
        hScoreOPaint.setStyle(Paint.Style.STROKE);
        hScoreOPaint.setLetterSpacing(0.05f);
        hScoreOPaint.setColor(ContextCompat.getColor(context, R.color.hi_score));
        hScoreOPaint.setStrokeWidth(4);
        hScoreOPaint.setTextAlign(Paint.Align.CENTER);

        hScoreIPaint = new Paint();
        hScoreIPaint.setTypeface(ResourcesCompat.getFont(context, R.font.ticking_regular));
        hScoreIPaint.setTextSize(scoreSize+6);
        hScoreIPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        hScoreIPaint.setLetterSpacing(0.05f);
        hScoreIPaint.setColor(Color.WHITE);
        hScoreIPaint.setTextAlign(Paint.Align.CENTER);

        board_color0 = ContextCompat.getColor(context, R.color.hi_board0);
        board_color1 = ContextCompat.getColor(context, R.color.hi_board1);
    }

    private int calcPercentPixel(int milliPercent) {
        return gInfo.screenXSize * milliPercent / 1000;
    }

    public void draw(Canvas canvas) {

        canvas.drawText(""+ gInfo.scoreNow, gameScoreXPos, gameScoreYPos, scoreOPaint);
        canvas.drawText(""+ gInfo.scoreNow, gameScoreXPos, gameScoreYPos, scoreIPaint);

        if (gInfo.score2Add > 0 && scoreTimeStamp < System.currentTimeMillis()) {
            int score = (int) Math.sqrt(gInfo.score2Add + 200);
            delay += 12;
            if (score > gInfo.score2Add)
                score = gInfo.score2Add;
            else if (score < 2)
                score = 2;
            scoreTimeStamp = System.currentTimeMillis() + delay;
            gInfo.score2Add -= score;
            gInfo.scoreNow += score;
            if (gInfo.score2Add == 0 && gInfo.scoreNow > gInfo.highLowScore) {
                boolean updated = false;
                for (int i = 0; i < gInfo.highMembers.size(); i++) {
                    HighMember hm = gInfo.highMembers.get(i);
                    if (hm.who.equals(highHeart)) {
                        hm.score = gInfo.scoreNow;
                        gInfo.highMembers.set(i, hm);
                        gInfo.highMembers.sort(Comparator.comparingLong(HighMember::getScore).reversed());
                        updated = true;
                    }
                }
                if (!updated) {
                    gInfo.highMembers.add(new HighMember(gInfo.scoreNow, highHeart));
                    gInfo.highMembers.sort(Comparator.comparingLong(HighMember::getScore).reversed());
                    gInfo.highMembers.remove(3);
                }
            }
        } else {
            delay = 30;
        }

        for (int i = 0; i < gInfo.highMembers.size(); i++) {
            HighMember highMember = gInfo.highMembers.get(i);
            if (gInfo.scoreNow == highMember.score) {
                xorCount++;
                if (xor && xorCount > 60) {
                    xorCount = 0;
                    board0Paint.setColor(board_color0);
                    board1Paint.setColor(board_color1);
                    xor = !xor;
                } else if (!xor && xorCount > 20) {
                    board0Paint.setColor(board_color1);
                    board1Paint.setColor(board_color0);
                    xor = !xor;
                }
            } else {
                board0Paint.setColor(board_color1);
                board1Paint.setColor(board_color0);
            }
            int y = yBoardPosTop + i*yBoardSize;
            canvas.drawRoundRect(xBoardPosLeft, y,
                    xBoardPosRight, y+yBoardSize-4, 16,16, board1Paint);
            canvas.drawRoundRect(xBoardPosLeft+4, y+4,
                    xBoardPosRight-8, y+yBoardSize-12, 16,16, board0Paint);
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