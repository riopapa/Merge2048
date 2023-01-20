package com.urrecliner.merge2048.GamePlate;

/*
 * Purpose of this Animation module is to draw, animate Game Plate
 */

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

import com.urrecliner.merge2048.GInfo;
import com.urrecliner.merge2048.GameImage.BlockImage;
import com.urrecliner.merge2048.GameObject.BonusRotate;

import java.util.ArrayList;
import java.util.List;

public class RotatePlate {

    final GInfo gInfo;
    final List<BlockImage> blockImages;
    List<BonusRotate> rotates;

    public RotatePlate(GInfo gInfo, final List<BlockImage> blockImages){
        this.gInfo = gInfo;
        this.blockImages = blockImages;
        rotates = new ArrayList<>();    // clear AniStack
    }


    public void addRotate(int xS, int yS, int idx, int loopCount, int delay) {

        rotates.add(new BonusRotate(xS, yS, idx, loopCount,
                gInfo.blockOutSize * (- xS) / (gInfo.bonusLoopCount +idx),
                gInfo.blockOutSize * (gInfo.Y_BLOCK_CNT - yS + 1)/ (gInfo.bonusLoopCount +idx), delay));
    }

    public void draw(Canvas canvas) {

        for (int i = 0; i < rotates.size(); ) {
            BonusRotate bonusRotate = rotates.get(i);
            if (bonusRotate.timeStamp < System.currentTimeMillis()) {
                if (bonusRotate.count >= bonusRotate.loopCount) {
                    rotates.remove(i);
                    continue;
                } else {
                    Matrix matrix = new Matrix();
                    int angle =  30 * bonusRotate.count;
                    matrix.postRotate(angle);
                    Bitmap blockMap = blockImages.get(bonusRotate.idx).flyMaps[1];
                    blockMap = Bitmap.createBitmap(blockMap, 0, 0,
                            blockMap.getWidth(), blockMap.getHeight()
                            , matrix, true);
                    int w = blockMap.getWidth(); int h = blockMap.getHeight();
                    int xPos = gInfo.xOffset + bonusRotate.xS * gInfo.blockOutSize
                            + gInfo.blockOutSize/2 - w/2;
                    int yPos = gInfo.yUpOffset + bonusRotate.yS * gInfo.blockOutSize
                            + gInfo.blockOutSize/2 - h/2;
                    canvas.drawBitmap(blockMap, xPos, yPos, null);
                    bonusRotate.count++;
                    bonusRotate.timeStamp = System.currentTimeMillis() + bonusRotate.delay;
                    rotates.set(i, bonusRotate);
                }
                i++;
            }
        }
    }

}