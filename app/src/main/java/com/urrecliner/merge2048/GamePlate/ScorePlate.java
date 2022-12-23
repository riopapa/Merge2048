package com.urrecliner.merge2048.GamePlate;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.urrecliner.merge2048.GameInfo;
import com.urrecliner.merge2048.GameObject.HighMember;
import com.urrecliner.merge2048.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ScorePlate {
    Context context;
    Paint textPaint, scorePaint, hTextPaint, hScorePaint, board1Paint, board2Paint;
    public int sXPos, sYPos, tXPos, tYPos, hXPos, hYPos;
    GameInfo gameInfo;
    final String scoreStr = "Score: ";
    List<HighMember> highMembers;
    final SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm", Locale.US);

    public ScorePlate(GameInfo gameInfo, Context context) {
        this.gameInfo = gameInfo;
        this.context = context;
        this.highMembers = gameInfo.highMembers;

        tXPos = 48;
        tYPos = gameInfo.yNextPos + gameInfo.blockOutSize + 112;
        textPaint = new Paint();
        textPaint.setTypeface(ResourcesCompat.getFont(context, R.font.ticking_regular));
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(48);
        textPaint.setTextAlign(Paint.Align.LEFT);

        sXPos = tXPos + 8 + (int) textPaint.measureText(scoreStr);
        sYPos = tYPos + 16;
        scorePaint = new Paint();
        scorePaint.setTypeface(ResourcesCompat.getFont(context, R.font.ticking_regular));
        scorePaint.setColor(Color.MAGENTA);
        scorePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        scorePaint.setTextSize(90);
        scorePaint.setTextAlign(Paint.Align.LEFT);

        board1Paint = new Paint();
        board1Paint.setColor(ContextCompat.getColor(context, R.color.ver_line));
        board2Paint = new Paint();
        board2Paint.setColor(ContextCompat.getColor(context, R.color.ver_line) & 0xffF0F0F0);
        board2Paint.setStrokeWidth(4);

        hXPos = tXPos; hYPos = tYPos + 100;
        hTextPaint = new Paint();
        hTextPaint.setTypeface(ResourcesCompat.getFont(context, R.font.steelfish_rg));
        hTextPaint.setColor(Color.WHITE);
        hTextPaint.setTextSize(64);

        hScorePaint = new Paint();
        hScorePaint.setTypeface(ResourcesCompat.getFont(context, R.font.ticking_regular));
        hScorePaint.setTextSize(64);
        hScorePaint.setColor(Color.WHITE);
        hScorePaint.setTextAlign(Paint.Align.LEFT);

    }

    public void draw(Canvas canvas) {
        canvas.drawText(scoreStr, tXPos, tYPos, textPaint);
        scorePaint.setColor(Color.BLUE);
        scorePaint.setStrokeWidth(4);
        canvas.drawText(""+ gameInfo.scoreNow, sXPos, sYPos, scorePaint);
        scorePaint.setColor(Color.RED);
        scorePaint.setStrokeWidth(0);
        canvas.drawText(""+ gameInfo.scoreNow, sXPos-2, sYPos-2, scorePaint);

        for (int i = 0; i < highMembers.size(); i++) {
            int y = hYPos + i*90;
            canvas.drawRoundRect(hXPos-8, y-8, 792, y+84, 16,16, board1Paint);
            canvas.drawRoundRect(hXPos-8, y-4, 792, y+78, 16,16, board2Paint);
            HighMember highMember = highMembers.get(i);
            String when = sdf.format(highMember.when);
            hTextPaint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(highMember.who, hXPos+160, y+56, hTextPaint);
            hTextPaint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText(when, hXPos+ 180, y+56, hTextPaint);
            canvas.drawText(""+highMember.score, hXPos+ 440, y+58, hScorePaint);
        }

    }
}