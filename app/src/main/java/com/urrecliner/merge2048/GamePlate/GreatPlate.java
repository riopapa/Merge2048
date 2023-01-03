package com.urrecliner.merge2048.GamePlate;

/*
 * Purpose of this Animation module is to draw, animate Game Plate
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.urrecliner.merge2048.GInfo;
import com.urrecliner.merge2048.GameImage.GreatImage;
import com.urrecliner.merge2048.GameObject.Great;

import java.util.ArrayList;
import java.util.List;

public class GreatPlate {

    Context context;
    final GInfo gInfo;
    List<Great> greats;
    final GreatImage greatImage;

    public GreatPlate(GInfo gInfo, Context context){
        this.gInfo = gInfo;
        this.context = context;
        greats = new ArrayList<>();    // clear AniStack
        greatImage = new GreatImage(gInfo, context);
    }

    public void addGreat(int xS, int yS, int idx, int loopCount) {

        greats.add(new Great(xS, yS, idx, loopCount,
                gInfo.blockOutSize * (- xS) / (gInfo.greatLoopCount +idx),
                gInfo.blockOutSize * (gInfo.yBlockCnt - yS + 1)/ (gInfo.greatLoopCount +idx), 30));
    }

    public void draw(Canvas canvas) {

        for (int gI = 0; gI < greats.size(); ) {
            Great great = greats.get(gI);
            if (great.timeStamp < System.currentTimeMillis()) {
                if (great.count >= great.loopCount) {
                    greats.remove(gI);
                    gInfo.score2Add += gInfo.greatStacked * great.idx;
                    continue;
                } else {
                    Bitmap bitmap = greatImage.countMaps[great.idx];
                    int xPos = gInfo.xOffset + great.xS * gInfo.blockOutSize
                            + great.xInc * great.count;
                    int yPos = gInfo.yUpOffset + great.yS * gInfo.blockOutSize
                            + great.yInc * great.count;
                    canvas.drawBitmap(bitmap, xPos, yPos, null);
                    great.count++;
                    great.timeStamp = System.currentTimeMillis() + great.delay;
                    greats.set(gI, great);
                }
                gI++;
            }
        }
    }

}