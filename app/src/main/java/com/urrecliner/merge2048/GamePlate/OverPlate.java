package com.urrecliner.merge2048.GamePlate;

/*
 * Purpose of this Ani module is to draw, animate Game Plate
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;

import com.urrecliner.merge2048.GameImage.BlockImage;
import com.urrecliner.merge2048.GameImage.GreatImage;
import com.urrecliner.merge2048.GameInfo;
import com.urrecliner.merge2048.GameObject.Great;

import java.util.ArrayList;
import java.util.List;

public class OverPlate {

    final GameInfo gameInfo;
    final List<BlockImage> blockImages;
    List<Great> overs;

    public OverPlate(GameInfo gameInfo, List<BlockImage> blockImages){
        this.gameInfo = gameInfo;
        this.blockImages = blockImages;
        overs = new ArrayList<>();    // clear PoolAni
    }


    public void addOver(int xS, int yS, int idx, int loopCount, int delay) {

        overs.add(new Great(xS, yS, idx, loopCount,
                gameInfo.blockOutSize * (- xS) / (gameInfo.greatCount+idx),
                gameInfo.blockOutSize * (gameInfo.yBlockCnt - yS + 1)/ (gameInfo.greatCount+idx), delay));
    }

    public void draw(Canvas canvas) {

        for (int gI = 0; gI < overs.size(); ) {
            Great great = overs.get(gI);
            if (great.timeStamp < System.currentTimeMillis()) {
                if (great.count >= great.loopCount) {
                    overs.remove(gI);
//                    gameInfo.score2Add += gameInfo.greatStacked * great.idx;
                    continue;
                } else {
                    Matrix matrix = new Matrix();
                    int angle =  30 * great.count;
                    matrix.postRotate(angle);
                    Bitmap blockMap = blockImages.get(great.idx).flyMaps[1];
                    blockMap = Bitmap.createBitmap(blockMap, 0, 0,
                            blockMap.getWidth(), blockMap.getHeight()
                            , matrix, true);
                    int w = blockMap.getWidth(); int h = blockMap.getHeight();
                    int xPos = gameInfo.xOffset + great.xS * gameInfo.blockOutSize
                            + gameInfo.blockOutSize/2 - w/2;
                    int yPos = gameInfo.yUpOffset + great.yS * gameInfo.blockOutSize
                            + gameInfo.blockOutSize/2 - h/2;
                    canvas.drawBitmap(blockMap, xPos, yPos, null);
                    great.count++;
                    great.timeStamp = System.currentTimeMillis() + great.delay;
                    overs.set(gI, great);
                }
                gI++;
            }
        }
    }

}