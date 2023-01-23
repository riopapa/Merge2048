package com.urrecliner.merge2048.GamePlate;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.urrecliner.merge2048.GInfo;
import com.urrecliner.merge2048.R;
import com.urrecliner.merge2048.Sub.ScaleMap;

public class BasePlate {

    Context context;
    final GInfo gInfo;
    final Paint backPaint, vPath1Paint, vPath2Paint, vPathAPaint, vPathBPaint;
    private final int xBlockCnt, yBlockCnt;
    private final int blockOutSize, blockIconSize;
    private final int xOffset, yUpOffset;
    private final int xLeft, yNextBottom;
    private final int xNewPos, yNewPos, xQuitPos, yQuitPos;
    private final int xUndoPos, yUndoPos, xSwingPos, ySwingPos;
    private final Bitmap newMap, newSmallMap, swingOnMap, swingOffMap, quitMap, quitSmallMap, undoMap;

    private boolean newNo, quitNo;
    private long newWaitTime, quitWaitTime;

    public BasePlate(GInfo gInfo, Context context) {
        this.gInfo = gInfo;
        this.context = context;

        backPaint = new Paint();
        backPaint.setColor(ContextCompat.getColor(context, R.color.game_background));
        xBlockCnt = gInfo.X_BLOCK_CNT;
        yBlockCnt = gInfo.Y_BLOCK_CNT;
        blockOutSize = gInfo.blockOutSize;
        blockIconSize = gInfo.blockIconSize;

        xOffset = gInfo.xOffset;
        yUpOffset = gInfo.yUpOffset;
        vPath1Paint = new Paint();
        vPath1Paint.setColor(ContextCompat.getColor(context, R.color.board0));
        vPath1Paint.setAlpha(230);
        vPath2Paint = new Paint();
        vPath2Paint.setColor(ContextCompat.getColor(context, R.color.board1));
        vPath2Paint.setAlpha(230);
        vPathAPaint = new Paint();
        vPathAPaint.setColor(ContextCompat.getColor(context, R.color.boardA));
        vPathBPaint = new Paint();
        vPathBPaint.setColor(ContextCompat.getColor(context, R.color.boardB));

        xLeft = xOffset-4;
        xNewPos = gInfo.xNewPos;            yNewPos = gInfo.yNewPos;
        xQuitPos = gInfo.xQuitPos;          yQuitPos = gInfo.yQuitPos;
        xUndoPos = gInfo.xUndoPos;          yUndoPos = gInfo.yUndoPos;
        xSwingPos = gInfo.xSwingPos;        ySwingPos = gInfo.ySwingPos;

        yNextBottom = gInfo.yNextPos + blockOutSize + 4;

        newMap = new ScaleMap().build(context, R.drawable.a_new, blockIconSize);
        newSmallMap = new ScaleMap().blink(newMap, blockIconSize);
        quitMap = new ScaleMap().build(context, R.drawable.a_quit, blockIconSize);
        quitSmallMap = new ScaleMap().blink(quitMap, blockIconSize);

        swingOnMap = new ScaleMap().build(context, R.drawable.a_swing_on, blockIconSize);
        swingOffMap = new ScaleMap().build(context, R.drawable.a_swing_off, blockIconSize);
        undoMap = new ScaleMap().build(context, R.drawable.undo_click, blockIconSize);
    }

    public void draw(Canvas canvas) {

        // back ground display
        canvas.drawRect(0, 0, gInfo.screenXSize, gInfo.screenYSize, backPaint);

        for (int x = 0; x <= xBlockCnt - 1; x++) {
            for (int y = 0; y < yBlockCnt; y++) {
                canvas.drawRect(xLeft + blockOutSize * x + 16,
                        yUpOffset + y*blockOutSize+16,
                        xLeft + blockOutSize * (x + 1) - 16,
                        yUpOffset + (y+1)*blockOutSize-16,
                        ((x+y)%2 == 0) ? vPath1Paint:vPath2Paint);
            }
            canvas.drawRect(xLeft + blockOutSize * x + 16, gInfo.yNextPos,
                    xOffset + blockOutSize * (x + 1) - 16, yNextBottom,
                    (x%2 == 0) ? vPathAPaint:vPathBPaint);
        }

        // new Icon

        if (gInfo.newGamePressed) {
            if (newWaitTime < System.currentTimeMillis()) {
                newWaitTime = System.currentTimeMillis() + 333;
                newNo = !newNo;
            }
            canvas.drawBitmap((newNo) ? newMap : newSmallMap, xNewPos, yNewPos,null);
        } else
            canvas.drawBitmap(newMap, xNewPos, yNewPos,null);

        // quit Icon

        if (gInfo.quitGamePressed) {
            if (quitWaitTime < System.currentTimeMillis()) {
                quitWaitTime = System.currentTimeMillis() + 333;
                quitNo = !quitNo;
            }
            canvas.drawBitmap((quitNo) ? quitMap : quitSmallMap, xQuitPos, yQuitPos,null);
        } else
            canvas.drawBitmap(quitMap, xQuitPos, yQuitPos,null);

        // undo Icon

        if (gInfo.undoCount > 0)
            canvas.drawBitmap(undoMap, xUndoPos, yUndoPos,null);

        // swing Icon
        canvas.drawBitmap((gInfo.swing)? swingOnMap:swingOffMap, xSwingPos, ySwingPos,null);

    }

}