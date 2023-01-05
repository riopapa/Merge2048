package com.urrecliner.merge2048.GamePlate;

/*
 * Purpose of this Animation module is to draw, animate Game Plate
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.urrecliner.merge2048.GInfo;
import com.urrecliner.merge2048.GameImage.BonusImage;
import com.urrecliner.merge2048.GameObject.Bonus;

import java.util.ArrayList;
import java.util.List;

public class BonusPlate {

    Context context;
    final GInfo gInfo;
    List<Bonus> bonuses;
    final BonusImage bonusImage;

    public BonusPlate(GInfo gInfo, Context context){
        this.gInfo = gInfo;
        this.context = context;
        bonuses = new ArrayList<>();    // clear AniStack
        bonusImage = new BonusImage(gInfo, context);
    }

    public void addBonus(int xS, int yS, int idx, int loopCount) {

        bonuses.add(new Bonus(xS, yS, idx, loopCount,
                gInfo.blockOutSize * (- xS) / loopCount,
                gInfo.blockOutSize * (gInfo.yBlockCnt - yS + 1)/ loopCount, 60));
    }

    public void draw(Canvas canvas) {

        for (int i = 0; i < bonuses.size(); ) {
            Bonus bonus = bonuses.get(i);
            if (bonus.timeStamp < System.currentTimeMillis()) {
                if (bonus.count >= bonus.loopCount) {
                    bonuses.remove(i);
                    gInfo.score2Add += gInfo.bonusStacked * bonus.idx;
                    continue;
                } else {
                    Bitmap bitmap = bonusImage.bonusMaps[bonus.idx];
                    int xPos = gInfo.xOffset + bonus.xS * gInfo.blockOutSize
                            + bonus.xInc * bonus.count;
                    int yPos = gInfo.yUpOffset + bonus.yS * gInfo.blockOutSize
                            + bonus.yInc * bonus.count;
                    canvas.drawBitmap(bitmap, xPos, yPos, null);
                    bonus.count++;
                    bonus.timeStamp = System.currentTimeMillis() + bonus.delay;
                    bonuses.set(i, bonus);
                }
                i++;
            }
        }
    }

}