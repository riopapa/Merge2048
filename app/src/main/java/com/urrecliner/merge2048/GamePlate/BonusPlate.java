package com.urrecliner.merge2048.GamePlate;

/*
 * Purpose of this Animation module is to draw, animate Game Plate
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.urrecliner.merge2048.GInfo;
import com.urrecliner.merge2048.GameImage.BonusImage;
import com.urrecliner.merge2048.GameObject.BonusRotate;

import java.util.ArrayList;
import java.util.List;

public class BonusPlate {

    final Context context;
    final BonusImage bonusImage;
    final GInfo gInfo;
    List<BonusRotate> bonuses;

    public BonusPlate(GInfo gInfo, Context context){
        this.gInfo = gInfo;
        this.context = context;
        bonuses = new ArrayList<>();    // clear AniStack
        bonusImage = new BonusImage(gInfo, context);
    }

    public void addBonus(int xS, int yS, int idx, int loopCount) {

        bonuses.add(new BonusRotate(xS, yS, idx, loopCount,
                gInfo.blockOutSize * (- xS) / loopCount,
                gInfo.blockOutSize * (gInfo.yBlockCnt - yS + 1)/ loopCount, 40));
    }

    public void draw(Canvas canvas) {

        for (int i = 0; i < bonuses.size(); ) {
            BonusRotate bonusRotate = bonuses.get(i);
            if (bonusRotate.timeStamp < System.currentTimeMillis()) {
                if (bonusRotate.count >= bonusRotate.loopCount) {
                    bonuses.remove(i);
                    gInfo.score2Add += gInfo.bonusStacked * bonusRotate.idx;
                    continue;
                } else {
                    Bitmap bitmap = bonusImage.bonusMaps[bonusRotate.idx];
                    int xPos = gInfo.xOffset + bonusRotate.xS * gInfo.blockOutSize
                            + bonusRotate.xInc * bonusRotate.count;
                    int yPos = gInfo.yUpOffset + bonusRotate.yS * gInfo.blockOutSize
                            + bonusRotate.yInc * bonusRotate.count;
                    canvas.drawBitmap(bitmap, xPos, yPos, null);
                    bonusRotate.count++;
                    bonusRotate.timeStamp = System.currentTimeMillis() + bonusRotate.delay;
                    bonuses.set(i, bonusRotate);
                }
                i++;
            }
        }
    }

}