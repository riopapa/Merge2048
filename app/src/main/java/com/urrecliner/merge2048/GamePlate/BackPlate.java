package com.urrecliner.merge2048.GamePlate;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.urrecliner.merge2048.GameInfo;
import com.urrecliner.merge2048.R;

public class BackPlate {

    Context context;
    GameInfo gameInfo;
    Paint backPaint, vPath1Paint, vPath2Paint;
    private final int xBlockCnt;
    private final int blockOutSize;
    private final int xOffset;
    private final int yNextPos;
    private final int xLeft, yTop, yBottom, yNextBottom;
    private final int xNewPos, yNewPos, xYesPos, xNopPos;
    private final Bitmap newMap, yesMap, nopMap, quitMap;

    public BackPlate(GameInfo gameInfo, Context context) {
        this.gameInfo = gameInfo;
        this.context = context;

        backPaint = new Paint();
        backPaint.setColor(ContextCompat.getColor(context, R.color.c00000));
        xBlockCnt = gameInfo.xBlockCnt;
        int yBlockCnt = gameInfo.yBlockCnt;
        blockOutSize = gameInfo.blockOutSize;
        xOffset = gameInfo.xOffset;
        int yUpOffset = gameInfo.yUpOffset;
        yNextPos = gameInfo.yNextPos;
        vPath1Paint = new Paint();
        vPath1Paint.setColor(ContextCompat.getColor(context, R.color.ver_line));
        vPath2Paint = new Paint();
        vPath2Paint.setColor(ContextCompat.getColor(context, R.color.ver_line) & 0xffF0F0F0);

        xLeft = xOffset-4; yTop = yUpOffset -4;
        yBottom = yTop + 8 + blockOutSize * yBlockCnt;
        yNextBottom = yNextPos + blockOutSize + 4;

        xNewPos = gameInfo.xNewPos; yNewPos = gameInfo.yNewPosS;
        xYesPos = xNewPos + blockOutSize * 4/5;
        xNopPos = xNewPos + blockOutSize * 8/5;
        newMap = buildMap (R.drawable.i_new);
        yesMap = buildMap (R.drawable.i_yes);
        nopMap = buildMap (R.drawable.i_no);
        quitMap = buildMap (R.drawable.i_quit);

    }

    Bitmap buildMap(int resId) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
        return Bitmap.createScaledBitmap(bitmap, blockOutSize*2/3, blockOutSize*2/3, false);
    }

    public void draw(Canvas canvas) {

        // back ground display
        canvas.drawRect(0, 0, gameInfo.screenXSize, gameInfo.screenYSize, backPaint);
        // top horizon line
//        canvas.drawLine(xLeft, yTop, xRight, yTop, vPath1Paint);
        // block vertical line
        for (int x = 0; x <= xBlockCnt -1; x += 2) {
            canvas.drawRect(xLeft + blockOutSize * x, yTop,
                    xLeft + blockOutSize * (x+1), yBottom, vPath1Paint);
            canvas.drawRect(xLeft + blockOutSize * x, yNextPos,
                    xOffset + blockOutSize * (x+1), yNextBottom, vPath1Paint);
        }
        for (int x = 1; x <= xBlockCnt -1; x += 2) {
            canvas.drawRect(xLeft + blockOutSize * x, yTop,
                    xLeft + blockOutSize* (x+1), yBottom, vPath2Paint);
            canvas.drawRect(xLeft + blockOutSize * x, yNextPos,
                    xOffset + blockOutSize * (x+1), yNextBottom, vPath2Paint);
        }
//        if (gameInfo.quitPressed)
//            canvas.drawBitmap(quitMap, xNewPos, yNewPos,null);
//        else
            canvas.drawBitmap(newMap, xNewPos, yNewPos,null);

        if (!gameInfo.isGameOver && (gameInfo.newGamePressed || gameInfo.quitPressed)) {
//            int xNewGameLeft = xNopPos+blockOutSize*2/3;
            canvas.drawBitmap(yesMap, xYesPos, yNewPos,null);
            canvas.drawBitmap(nopMap, xNopPos, yNewPos,null);
        }

    }

}