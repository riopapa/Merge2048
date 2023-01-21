package com.urrecliner.merge2048.GamePlate;

/*
 * Draw a message (heading, line) for a while
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import androidx.core.content.ContextCompat;

import com.urrecliner.merge2048.GInfo;
import com.urrecliner.merge2048.R;

public class MessagePlate {

    final GInfo gInfo;
    final Bitmap msgBoxMap;
    final int msgMapSize;
    final int xMapPos, xBoxPos;
    final int piece;
    Paint msgBox0Paint, msgHeadPaint, msgLinePaint;
    int yBoxPos, yTopPos, yBottomPos;
    int yInc, delay;
    long nextTime;
    final int head_colorOut, head_colorIn, line_colorOut, line_colorIn;
    public MessagePlate(GInfo gInfo, Context context){
        this.gInfo = gInfo;
        this.piece = gInfo.piece;

        msgBox0Paint = new Paint();
        msgBox0Paint.setColor(ContextCompat.getColor(context, R.color.msg_background0));
        msgBox0Paint.setAlpha(200);

        msgHeadPaint = new Paint();
        msgHeadPaint.setTextSize(gInfo.piece*7f/9);
        msgHeadPaint.setTextAlign(Paint.Align.CENTER);
        msgHeadPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        msgLinePaint = new Paint();
        msgLinePaint.setTextSize(gInfo.piece * 6f / 9);
        msgLinePaint.setTextAlign(Paint.Align.CENTER);
        msgLinePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        msgMapSize = gInfo.screenXSize*3/5;
//        canvas0.drawRoundRect(0, 0, msgMapSize, msgMapSize, msgMapSize/10f, msgMapSize/10f, msgBox0Paint);

        msgBoxMap = get_Merge_2048(context);

        xMapPos = (gInfo.screenXSize- msgMapSize)/2;
        xBoxPos = gInfo.screenXSize/2;
        yTopPos = gInfo.yDownOffset/2 - piece;
        yBottomPos = gInfo.yDownOffset/2 + piece;
        head_colorOut = ContextCompat.getColor(context, R.color.msg_header_out);
        head_colorIn = ContextCompat.getColor(context, R.color.msg_header_in);
        line_colorOut = ContextCompat.getColor(context, R.color.msg_line_out);
        line_colorIn = ContextCompat.getColor(context, R.color.msg_line_in);
    }

    private Bitmap get_Merge_2048(Context context) {

        Bitmap map = Bitmap.createBitmap(msgMapSize, msgMapSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(map);
        Paint p = new Paint();
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
        p.setAlpha(120);
        Bitmap img = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(context.getResources(), R.mipmap.merge_2048, null), msgMapSize, msgMapSize, false);
        canvas.drawBitmap(img, 0, 0, p);
        return map;
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

        canvas.drawBitmap(msgBoxMap, xMapPos, yBoxPos-msgMapSize/2f, null);
        msgHeadPaint.setStrokeWidth(16);
        msgHeadPaint.setColor(head_colorOut);
        canvas.drawText(gInfo.msgHead, xBoxPos, yBoxPos - piece - piece, msgHeadPaint);
        msgHeadPaint.setStrokeWidth(0);
        msgHeadPaint.setColor(head_colorIn);
        canvas.drawText(gInfo.msgHead, xBoxPos, yBoxPos - piece - piece, msgHeadPaint);

        int yPos = yBoxPos;
        if (gInfo.msgLine2.equals(""))
            yPos += piece;
        msgLinePaint.setStrokeWidth(8);
        msgLinePaint.setColor(line_colorOut);
        canvas.drawText(gInfo.msgLine1, xBoxPos, yPos, msgLinePaint);
        msgLinePaint.setStrokeWidth(0);
        msgLinePaint.setColor(line_colorIn);
        canvas.drawText(gInfo.msgLine1, xBoxPos, yPos, msgLinePaint);

        if (!gInfo.msgLine2.equals("")) {
            msgLinePaint.setStrokeWidth(8);
            msgLinePaint.setColor(line_colorOut);
            canvas.drawText(gInfo.msgLine2, xBoxPos, yPos+ piece + piece, msgLinePaint);
            msgLinePaint.setStrokeWidth(0);
            msgLinePaint.setColor(line_colorIn);
            canvas.drawText(gInfo.msgLine2, xBoxPos, yPos+ piece + piece, msgLinePaint);
        }
        if (gInfo.msgFinishTime < System.currentTimeMillis())
            gInfo.msgHead = "";
    }

    public void set(String head, String line1, String line2, long startTime, int keep) {
        gInfo.msgHead = head;
        gInfo.msgLine1 = line1; gInfo.msgLine2 = line2;
        gInfo.msgStartTime = startTime;
        gInfo.msgFinishTime = gInfo.msgStartTime + keep;
        yInc = 3;
        delay = 30;
        nextTime = startTime;
        yBoxPos = (yTopPos + yBottomPos) / 2;
    }
}