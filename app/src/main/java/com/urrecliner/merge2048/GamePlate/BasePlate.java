package com.urrecliner.merge2048.GamePlate;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.urrecliner.merge2048.GInfo;
import com.urrecliner.merge2048.R;

import java.util.Random;

public class BasePlate {

    Context context;
    final GInfo gInfo;
    final Paint backPaint, vPath1Paint, vPath2Paint, yesNoPaint, rioPaint;
    private final int xBlockCnt, yBlockCnt;
    private final int blockOutSize, blockIconSize;
    private final int xOffset, yUpOffset;
    private final int xLeft, yTop, yBottom, yNextBottom;
    private final int xNewPos, yNewPos, xQuitPos, yQuitPos, xYesPos, xNopPos, xSwingPos, ySwingPos;
    private final Bitmap newMap, yesMap, nopMap, swingOMap, swingFMap, quitMap;
    private final int [] xRioPos, yRioPos, xRioInc, yRioInc;
    private final Bitmap [] rioMap;

    public BasePlate(GInfo gInfo, Context context) {
        this.gInfo = gInfo;
        this.context = context;

        backPaint = new Paint();
        backPaint.setColor(ContextCompat.getColor(context, R.color.game_background));
        xBlockCnt = gInfo.xBlockCnt;
        yBlockCnt = gInfo.yBlockCnt;
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

        xLeft = xOffset-4; yTop = yUpOffset -4;
        yBottom = yTop + 8 + blockOutSize * yBlockCnt;
        yNextBottom = gInfo.yNextPos + blockOutSize + 4;

        xNewPos = gInfo.xNewPos; yNewPos = gInfo.yNewPosS;
        xQuitPos = gInfo.screenXSize-xOffset-blockIconSize; yQuitPos = gInfo.yNewPosS;
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
        quitMap = buildMap(R.drawable.a_quit);

        swingOMap = buildMap(R.drawable.a_swing);
        swingFMap = buildMap(R.drawable.a_swing_f);

        xRioPos = new int[]{300, 800, xYesPos};
        yRioPos = new int[]{blockOutSize,  yNextBottom - blockOutSize, blockIconSize};
        xRioInc = new int[]{2, 2, -2};
        yRioInc = new int[]{2, -2, 2};
        rioMap = new Bitmap[] {
                Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.sign_cr), gInfo.piece, gInfo.piece*2/3, false),
                Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.sign_gb), gInfo.piece, gInfo.piece*2/3, false),
                Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.sign_yb), gInfo.piece, gInfo.piece*2/3, false)};
        rioPaint = new Paint();
    }

    Bitmap buildMap(int resId) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
        return Bitmap.createScaledBitmap(bitmap, blockIconSize-4, blockIconSize-4-4, false);
    }

    public void draw(Canvas canvas) {

        // back ground display
        canvas.drawRect(0, 0, gInfo.screenXSize, gInfo.screenYSize, backPaint);

        // draw my signature
        for (int i = 0; i < 3; i++) {
            canvas.drawBitmap(rioMap[i], xRioPos[i], yRioPos[i], rioPaint);
            if (new Random().nextInt(2) == 0) {
                xRioPos[i] += xRioInc[i];
                yRioPos[i] += yRioInc[i];
                if (xRioPos[i] < 4)
                    xRioInc[i] = 1 + new Random().nextInt(2);
                if (xRioPos[i] > gInfo.screenXSize - 64)
                    xRioInc[i] = -1 - new Random().nextInt(2);
                if (yRioPos[i] < 4)
                    yRioInc[i] = 1 + new Random().nextInt(2);
                if (yRioPos[i] > gInfo.yNewPosS - 64)
                    yRioInc[i] = -1 - new Random().nextInt(2);
                if (new Random().nextInt(18) == 0 && xRioInc[i] > 0)
                    xRioInc[i]++;
                if (new Random().nextInt(18) == 0 && xRioInc[i] < 0)
                    xRioInc[i]--;
                if (new Random().nextInt(18) == 0 && yRioInc[i] > 0)
                    yRioInc[i]++;
                if (new Random().nextInt(18) == 0 && yRioInc[i] < 0)
                    yRioInc[i]--;
            }
        }

        // block vertical lane
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
                    (x%2 == 0) ? vPath1Paint:vPath2Paint);
        }

        // new Icon
        canvas.drawBitmap(newMap, xNewPos, yNewPos,null);
        canvas.drawBitmap(quitMap, xQuitPos, yQuitPos,null);

        // yes, no
        if (gInfo.newGamePressed || gInfo.quitGamePressed) {
            canvas.drawRoundRect(xYesPos-2, yNewPos-2,
                    xNewPos+ blockIconSize*3, yNewPos+ blockIconSize,
                        4,4, yesNoPaint);
            canvas.drawBitmap(yesMap, xYesPos, yNewPos,null);
            canvas.drawBitmap(nopMap, xNopPos, yNewPos,null);
        }

        if (gInfo.swing)
            canvas.drawBitmap(swingOMap, xSwingPos, ySwingPos,null);
        else
            canvas.drawBitmap(swingFMap, xSwingPos, ySwingPos,null);

    }

}