package com.urrecliner.merge2048.GamePlate;

/*
 * Purpose of this Animation module is to draw, animate Game Plate
 */

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

import com.urrecliner.merge2048.GInfo;
import com.urrecliner.merge2048.GameImage.BlockImage;
import com.urrecliner.merge2048.GameObject.Great;

import java.util.ArrayList;
import java.util.List;

public class OverPlate {

    final GInfo gInfo;
    final List<BlockImage> blockImages;
    List<Great> overs;

    public OverPlate(GInfo gInfo, List<BlockImage> blockImages){
        this.gInfo = gInfo;
        this.blockImages = blockImages;
        overs = new ArrayList<>();    // clear AniStack
    }


    public void addOver(int xS, int yS, int idx, int loopCount, int delay) {

        overs.add(new Great(xS, yS, idx, loopCount,
                gInfo.blockOutSize * (- xS) / (gInfo.greatCount+idx),
                gInfo.blockOutSize * (gInfo.yBlockCnt - yS + 1)/ (gInfo.greatCount+idx), delay));
    }

    public void draw(Canvas canvas) {

        for (int gI = 0; gI < overs.size(); ) {
            Great great = overs.get(gI);
            if (great.timeStamp < System.currentTimeMillis()) {
                if (great.count >= great.loopCount) {
                    overs.remove(gI);
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
                    int xPos = gInfo.xOffset + great.xS * gInfo.blockOutSize
                            + gInfo.blockOutSize/2 - w/2;
                    int yPos = gInfo.yUpOffset + great.yS * gInfo.blockOutSize
                            + gInfo.blockOutSize/2 - h/2;
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