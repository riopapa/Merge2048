package com.urrecliner.merge2048.GamePlate;

/*
 * Draw a message (heading, line) for a while
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.urrecliner.merge2048.GInfo;
import com.urrecliner.merge2048.R;

public class MessagePlate {

    Context context;
    final GInfo gInfo;
    final Bitmap msgMap;
    final int msgMapSize;
    final int xMapPos, xBoxPos;
    final int pxcl;
    Paint msgBoxPaint, msgHeadPaint, msgLinePaint;
    int yBoxPos, yTopPos, yBottomPos;
    int yInc, delay;
    long nextTime;

    public MessagePlate(GInfo gInfo, Context context){
        this.gInfo = gInfo;
        this.context = context;
        this.pxcl = gInfo.pxcl;

        msgBoxPaint = new Paint();
        msgBoxPaint.setColor(ContextCompat.getColor(context, R.color.msg_background));

        msgHeadPaint = new Paint();
        msgHeadPaint.setTextSize(gInfo.pxcl);
        msgHeadPaint.setTextAlign(Paint.Align.CENTER);
        msgHeadPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        msgLinePaint = new Paint();
        msgLinePaint.setTextSize(gInfo.pxcl * 8 / 9);
        msgLinePaint.setTextAlign(Paint.Align.CENTER);
        msgLinePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        msgMapSize = gInfo.screenXSize*3/5;
        msgMap = Bitmap.createBitmap(msgMapSize, msgMapSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(msgMap);
        canvas.drawRoundRect(0, 0, msgMapSize, msgMapSize, msgMapSize/10f, msgMapSize/10f, msgBoxPaint);

        xMapPos = (gInfo.screenXSize- msgMapSize)/2;
        xBoxPos = gInfo.screenXSize/2;
        yTopPos = gInfo.yDownOffset/2 - pxcl - pxcl;
        yBottomPos = gInfo.yDownOffset/2 + pxcl + pxcl;
    }

    public void draw(Canvas canvas) {
        long nowTime = System.currentTimeMillis();
        if (gInfo.msgHead.length() == 0 && gInfo.msgStartTime < nowTime)
            return;
        if (nowTime < nextTime)
            return;
        nextTime += delay;
        yBoxPos += yInc;
        if (yBoxPos > yBottomPos || yBoxPos < yTopPos) {
            yInc = - yInc;
        }

        canvas.drawBitmap(msgMap, xMapPos, yBoxPos-msgMapSize/2f, null);
        msgHeadPaint.setStrokeWidth(16);
        msgHeadPaint.setColor(ContextCompat.getColor(context, R.color.msg_header_out));
        canvas.drawText(gInfo.msgHead, xBoxPos, yBoxPos - pxcl - pxcl, msgHeadPaint);
        msgHeadPaint.setStrokeWidth(0);
        msgHeadPaint.setColor(ContextCompat.getColor(context, R.color.msg_header));
        canvas.drawText(gInfo.msgHead, xBoxPos, yBoxPos - pxcl - pxcl, msgHeadPaint);

        int yPos = yBoxPos;
        if (gInfo.msgLine2.equals(""))
            yPos += pxcl;
        msgLinePaint.setStrokeWidth(8);
        msgLinePaint.setColor(ContextCompat.getColor(context, R.color.msg_line_out));
        canvas.drawText(gInfo.msgLine1, xBoxPos, yPos, msgLinePaint);
        msgLinePaint.setStrokeWidth(0);
        msgLinePaint.setColor(ContextCompat.getColor(context, R.color.msg_line));
        canvas.drawText(gInfo.msgLine1, xBoxPos, yPos, msgLinePaint);

        if (!gInfo.msgLine2.equals("")) {
            msgLinePaint.setStrokeWidth(8);
            msgLinePaint.setColor(ContextCompat.getColor(context, R.color.msg_line_out));
            canvas.drawText(gInfo.msgLine2, xBoxPos, yPos+pxcl+pxcl, msgLinePaint);
            msgLinePaint.setStrokeWidth(0);
            msgLinePaint.setColor(ContextCompat.getColor(context, R.color.msg_line));
            canvas.drawText(gInfo.msgLine2, xBoxPos, yPos+pxcl+pxcl, msgLinePaint);
        }
        if (gInfo.msgFinishTime < System.currentTimeMillis())
            gInfo.msgHead = "";
    }

    public void set(String head, String line1, String line2, long startTime, int keep) {
        gInfo.msgHead = head;
        gInfo.msgLine1 = line1; gInfo.msgLine2 = line2;
        gInfo.msgStartTime = startTime;
        gInfo.msgFinishTime = gInfo.msgStartTime + keep;
        yInc = 4;
        delay = 30;
        nextTime = startTime;
        yBoxPos = (yTopPos + yBottomPos) / 2;
    }
}