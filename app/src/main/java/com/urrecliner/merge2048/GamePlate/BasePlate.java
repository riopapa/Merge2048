package com.urrecliner.merge2048.GamePlate;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.urrecliner.merge2048.GameInfo;
import com.urrecliner.merge2048.R;

public class BasePlate {

    Context context;
    GameInfo gameInfo;
    final Paint backPaint, vPath1Paint, vPath2Paint, yesNoPaint;
    private final int xBlockCnt;
    private final int blockOutSize, blockIconSize;
    private final int xOffset;
    private final int xLeft, yTop, yBottom, yNextBottom;
    private final int xNewPos, yNewPos, xYesPos, xNopPos, xSwingPos, ySwingPos;
    private final Bitmap newMap, yesMap, nopMap, swingOMap, swingFMap, quitMap;

    public BasePlate(GameInfo gameInfo, Context context) {
        this.gameInfo = gameInfo;
        this.context = context;

        backPaint = new Paint();
        backPaint.setColor(ContextCompat.getColor(context, R.color.c00000));
        xBlockCnt = gameInfo.xBlockCnt;
        int yBlockCnt = gameInfo.yBlockCnt;
        blockOutSize = gameInfo.blockOutSize;
        blockIconSize = gameInfo.blockIconSize;

        xOffset = gameInfo.xOffset;
        int yUpOffset = gameInfo.yUpOffset;
        vPath1Paint = new Paint();
        vPath1Paint.setColor(ContextCompat.getColor(context, R.color.board0));
        vPath2Paint = new Paint();
        vPath2Paint.setColor(ContextCompat.getColor(context, R.color.board1));

        xLeft = xOffset-4; yTop = yUpOffset -4;
        yBottom = yTop + 8 + blockOutSize * yBlockCnt;
        yNextBottom = gameInfo.yNextPos + blockOutSize + 4;

        xNewPos = gameInfo.xNewPos; yNewPos = gameInfo.yNewPosS;
        xYesPos = xNewPos + blockIconSize;
        xNopPos = xNewPos + blockIconSize * 2;
        xSwingPos = xNewPos; ySwingPos = yNewPos + blockIconSize;

        yesNoPaint = new Paint();
        yesNoPaint.setColor(Color.BLUE);
        yesNoPaint.setStrokeWidth(6);
        yesNoPaint.setStyle(Paint.Style.STROKE);

        newMap = buildMap (R.drawable.a_new);
        yesMap = buildMap (R.drawable.a_yes);
        nopMap = buildMap (R.drawable.a_no);

        swingOMap = buildMap(R.drawable.a_swing);
        swingFMap = buildMap(R.drawable.a_swing_f);

        quitMap = buildMap(R.drawable.a_quit);
    }

    Bitmap buildMap(int resId) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
        return Bitmap.createScaledBitmap(bitmap, blockIconSize-4, blockIconSize-4-4, false);
    }

    public void draw(Canvas canvas) {

        // back ground display
        canvas.drawRect(0, 0, gameInfo.screenXSize, gameInfo.screenYSize, backPaint);

        // block vertical lane
        for (int x = 0; x <= xBlockCnt -1; x += 2) {
            canvas.drawRect(xLeft + blockOutSize * x, yTop,
                    xLeft + blockOutSize * (x+1), yBottom, vPath1Paint);
            canvas.drawRect(xLeft + blockOutSize * x, gameInfo.yNextPos,
                    xOffset + blockOutSize * (x+1), yNextBottom, vPath1Paint);
        }
        for (int x = 1; x <= xBlockCnt -1; x += 2) {
            canvas.drawRect(xLeft + blockOutSize * x, yTop,
                    xLeft + blockOutSize* (x+1), yBottom, vPath2Paint);
            canvas.drawRect(xLeft + blockOutSize * x, gameInfo.yNextPos,
                    xOffset + blockOutSize * (x+1), yNextBottom, vPath2Paint);
        }

        // new Icon
        if (gameInfo.quitGame)
            canvas.drawBitmap(newMap, xNewPos, yNewPos,null);
        else
            canvas.drawBitmap(newMap, xNewPos, yNewPos,null);

        // yes, no
        if (!gameInfo.isGameOver && (gameInfo.newGamePressed || gameInfo.quitPressed)) {
            canvas.drawRoundRect(xYesPos-2, yNewPos-2,
                    xNewPos+ blockIconSize*3, yNewPos+ blockIconSize,
                        4,4, yesNoPaint);
            canvas.drawBitmap(yesMap, xYesPos, yNewPos,null);
            canvas.drawBitmap(nopMap, xNopPos, yNewPos,null);
        }

        if (gameInfo.swing)
            canvas.drawBitmap(swingOMap, xSwingPos, ySwingPos,null);
        else
            canvas.drawBitmap(swingFMap, xSwingPos, ySwingPos,null);

    }

}