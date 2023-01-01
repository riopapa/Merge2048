package com.urrecliner.merge2048.GamePlate;

/*
 * Draw a message (heading, line) for a while
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.urrecliner.merge2048.GameInfo;
import com.urrecliner.merge2048.R;

public class MessagePlate {

    Context context;
    GameInfo gameInfo;
    Paint msgBoxPaint, msgHeadPaint, msgLinePaint;
    final Bitmap msgMap;
    final int msgMapSize;
    final int xMapPos, xBoxPos;
    int yBoxPos;

    public MessagePlate(GameInfo gameInfo, Context context){
        this.gameInfo = gameInfo;
        this.context = context;

        msgBoxPaint = new Paint();
        msgBoxPaint.setColor(ContextCompat.getColor(context, R.color.msg_background));

        msgHeadPaint = new Paint();
        msgHeadPaint.setTextSize(100);
        msgHeadPaint.setTextAlign(Paint.Align.CENTER);
        msgHeadPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        msgLinePaint = new Paint();
        msgLinePaint.setTextSize(80);
        msgLinePaint.setTextAlign(Paint.Align.CENTER);
        msgLinePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        msgMapSize = gameInfo.screenXSize/2;
        msgMap = Bitmap.createBitmap(msgMapSize, msgMapSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(msgMap);
        canvas.drawRoundRect(0, 0, msgMapSize, msgMapSize, msgMapSize/12f, msgMapSize/12f, msgBoxPaint);

        xMapPos = (gameInfo.screenXSize- msgMapSize)/2;
        xBoxPos = gameInfo.screenXSize/2;
    }

    public void draw(Canvas canvas) {
        if (gameInfo.msgHead.length() == 0)
            return;

        if (gameInfo.msgLine2.equals("")) {
            yBoxPos = gameInfo.yDownOffset/2;
        } else {
            yBoxPos = gameInfo.yDownOffset/2 + 50;
        }
        canvas.drawBitmap(msgMap, xMapPos, yBoxPos-msgMapSize/2f, null);
        msgHeadPaint.setStrokeWidth(16);
        msgHeadPaint.setColor(Color.BLUE);
        canvas.drawText(gameInfo.msgHead, xBoxPos, yBoxPos - 160, msgHeadPaint);
        msgHeadPaint.setStrokeWidth(0);
        msgHeadPaint.setColor(Color.RED);
        canvas.drawText(gameInfo.msgHead, xBoxPos, yBoxPos - 160, msgHeadPaint);

        if (gameInfo.msgLine2.equals(""))
            yBoxPos += 100;
        msgLinePaint.setStrokeWidth(8);
        msgLinePaint.setColor(Color.BLUE);
        canvas.drawText(gameInfo.msgLine1, xBoxPos, yBoxPos, msgLinePaint);
        msgLinePaint.setStrokeWidth(0);
        msgLinePaint.setColor(Color.CYAN);
        canvas.drawText(gameInfo.msgLine1, xBoxPos, yBoxPos, msgLinePaint);

        if (!gameInfo.msgLine2.equals("")) {
            msgLinePaint.setStrokeWidth(8);
            msgLinePaint.setColor(Color.BLUE);
            canvas.drawText(gameInfo.msgLine2, xBoxPos, yBoxPos+120, msgLinePaint);
            msgLinePaint.setStrokeWidth(0);
            msgLinePaint.setColor(Color.CYAN);
            canvas.drawText(gameInfo.msgLine2, xBoxPos, yBoxPos+120, msgLinePaint);
        }
        if (gameInfo.msgTime < System.currentTimeMillis())
            gameInfo.msgHead = "";

    }

}