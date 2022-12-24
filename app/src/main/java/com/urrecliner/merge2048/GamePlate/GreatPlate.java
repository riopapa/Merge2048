package com.urrecliner.merge2048.GamePlate;

/*
 * Purpose of this Ani module is to draw, animate Game Plate
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.urrecliner.merge2048.GameImage.GreatImage;
import com.urrecliner.merge2048.GameInfo;
import com.urrecliner.merge2048.GameObject.Great;

import java.util.ArrayList;
import java.util.List;

public class GreatPlate {

    Context context;
    final GameInfo gameInfo;
    List<Great> greats;
    final GreatImage greatImage;

    public GreatPlate(GameInfo gameInfo, Context context){
        this.gameInfo = gameInfo;
        this.context = context;
        greats = new ArrayList<>();    // clear PoolAni
        greatImage = new GreatImage(gameInfo, context);
    }


    public void addGreat(int xS, int yS, int idx, int loopCount) {

        greats.add(new Great(xS, yS, idx, loopCount,
                gameInfo.blockOutSize * (- xS) / (gameInfo.greatCount+idx),
                gameInfo.blockOutSize * (gameInfo.yBlockCnt - yS + 1)/ (gameInfo.greatCount+idx)));
    }

    public void draw(Canvas canvas) {

        for (int gI = 0; gI < greats.size(); ) {
            Great great = greats.get(gI);
            if (great.timeStamp < System.currentTimeMillis()) {
                if (great.count >= great.loopCount) {
                    greats.remove(gI);
                    gameInfo.score2Add += gameInfo.greatStacked * great.idx;
                    continue;
                } else {
                    Bitmap bitmap = greatImage.countMaps[great.idx];
                    int xPos = gameInfo.xOffset + great.xS * gameInfo.blockOutSize
                            + great.xInc * great.count;
                    int yPos = gameInfo.yUpOffset + great.yS * gameInfo.blockOutSize
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