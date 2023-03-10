package com.urrecliner.merge2048;

/*
 * Purpose of this Animation module is to draw, animate Game Plate
 */

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;

import com.urrecliner.merge2048.GameImage.BlockImage;
import com.urrecliner.merge2048.GameObject.AniStack;

import java.util.List;
import java.util.Random;

public class Animation {

    final GInfo gInfo;
    final List<BlockImage> blockImages;
    final int smooth;

    final int screenXSize, screenYSize;
    final int xBlockCnt, yBlockCnt, xOffset, yUpOffset, xBlockOutSize, yBlockOutSize;

    public Animation(GInfo gInfo, List<BlockImage> blockImages, int smooth){
        this.gInfo = gInfo;
        this.xOffset = gInfo.xOffset;
        this.yUpOffset = gInfo.yUpOffset;
        this.screenXSize = gInfo.screenXSize;
        this.screenYSize = gInfo.screenYSize;
        this.xBlockOutSize = gInfo.blockOutSize;
        this.yBlockOutSize = gInfo.blockOutSize;
        xBlockCnt = gInfo.X_BLOCK_CNT;
        yBlockCnt = gInfo.Y_BLOCK_CNT;
        this.blockImages = blockImages;
        this.smooth = smooth;
    }

    public void draw(Canvas canvas) {

        // reDraw if Paused
        Bitmap blockMap;
        for (int y = 0; y < yBlockCnt; y++) {
            for (int x = 0; x < xBlockCnt; x++) {
                int idx = gInfo.cells[x][y].index;
                if (idx > 0 && gInfo.cells[x][y].state == GInfo.STATE.PAUSED) {
                    if (idx > gInfo.CONTINUE_INDEX) {
                        gInfo.cells[x][y].count++;
                        if (gInfo.cells[x][y].xor && gInfo.cells[x][y].count > 40) {
                            gInfo.cells[x][y].count = 0;
                            gInfo.cells[x][y].xor = !gInfo.cells[x][y].xor;
                        } else if (!gInfo.cells[x][y].xor && gInfo.cells[x][y].count > 6) {
                            gInfo.cells[x][y].count = 0;
                            gInfo.cells[x][y].xor = !gInfo.cells[x][y].xor;
                        }
                        blockMap = gInfo.cells[x][y].xor ?
                                blockImages.get(idx).bitmap :
                                blockImages.get(idx).xorMap;
                    } else {
                        blockMap = blockImages.get(idx).bitmap;
                    }
                    canvas.drawBitmap(blockMap, xOffset + x * xBlockOutSize,
                            yUpOffset + y * yBlockOutSize, null);
                }
            }
        }

        for (int i = 0; i < gInfo.aniStacks.size(); ) {
            if (gInfo.aniStacks.get(i).count == 111) {
                gInfo.aniStacks.remove(i);
            } else
                i++;
        }
        // draw Animation while ani active
        for (int i = 0; i < gInfo.aniStacks.size(); i++) {
            drawCase(canvas, i);
        }
    }

    private void drawCase(Canvas canvas, int i) {
        AniStack ani = gInfo.aniStacks.get(i);
        switch (ani.state) {
            case MOVING:
                if (ani.timeStamp < System.currentTimeMillis() ) {
                    if (ani.count >= ani.maxCount) {    // smooth factor
                        gInfo.cells[ani.xS][ani.yS].index = 0;
                        gInfo.cells[ani.xS][ani.yS].state = GInfo.STATE.PAUSED;
                        gInfo.cells[ani.xF][ani.yF].index = ani.index;
                        gInfo.cells[ani.xF][ani.yF].state = GInfo.STATE.STOP;
                        ani.count = 111;    // 111 means this ani should be removed soon
                        gInfo.aniStacks.set(i, ani);
                    } else {
                        Bitmap blockMap = blockImages.get(ani.index).flyMaps[ani.count];
                        int xPos = gInfo.xOffset + ani.xS * gInfo.blockOutSize
                                + ani.xInc * ani.count - gInfo.blockFlyingGap;
                        int yPos = gInfo.yUpOffset + ani.yS * gInfo.blockOutSize
                                + ani.yInc * ani.count - gInfo.blockFlyingGap;
                        canvas.drawBitmap(blockMap, xPos, yPos, null);
                        ani.count++;
                        ani.timeStamp = System.currentTimeMillis();
                        gInfo.aniStacks.set(i, ani);
                    }
                }
                break;

            case EXPLODE:
                if (ani.timeStamp < System.currentTimeMillis() ) {
                    if (ani.count >= ani.maxCount) {    // smooth factor
                        gInfo.cells[ani.xS][ani.yS].state = GInfo.STATE.EXPLODED;
                        gInfo.aniStacks.remove(i);
                    } else {
                        Bitmap explodeMap = blockImages.get(
                                (ani.count > 3)? ani.index:ani.index-1).explodeMaps[ani.count];
                        int xPos = gInfo.xOffset + ani.xS * gInfo.blockOutSize
                                + ani.xInc * ani.count - gInfo.explodeGap;
                        int yPos = gInfo.yUpOffset + ani.yS * gInfo.blockOutSize
                                + ani.yInc * ani.count - gInfo.explodeGap;
                        canvas.drawBitmap(explodeMap, xPos, yPos, null);
                        ani.timeStamp = System.currentTimeMillis() + ani.delay;
                        ani.delay += new Random().nextInt(20) + 5;
                        ani.count++;
                        gInfo.aniStacks.set(i, ani);
                    }
                }
                break;

            case DESTROY:
                if (ani.timeStamp < System.currentTimeMillis() ) {
                    if (ani.count >= ani.maxCount) {
                        gInfo.cells[ani.xS][ani.yS].state = GInfo.STATE.DESTROYED;
                        gInfo.aniStacks.remove(i);
                    } else {    // 0~4 : lower block, 5~7 merged block
                        Matrix matrix = new Matrix();
                        int angle =  45 * ani.count;
                        if (ani.xS%2 == 0)
                            angle = -angle;
                        matrix.postRotate(angle);
                        Bitmap blockMap = (ani.count % 2 == 0)?
                                blockImages.get(ani.index).destroyMap :
                                blockImages.get(ani.index).bitmap;

                        blockMap = Bitmap.createBitmap(blockMap, 0, 0,
                                blockMap.getWidth(), blockMap.getHeight()
                                , matrix, true);
                        int w = blockMap.getWidth(); int h = blockMap.getHeight();
                        int xPos = gInfo.xOffset + ani.xS * gInfo.blockOutSize
                                + gInfo.blockOutSize/2 - w/2;
                        int yPos = gInfo.yUpOffset + ani.yS * gInfo.blockOutSize
                                + gInfo.blockOutSize/2 - h/2;
                        canvas.drawBitmap(blockMap, xPos, yPos, null);
                        ani.count++;
                        ani.timeStamp = System.currentTimeMillis() + ani.delay;
                        gInfo.aniStacks.set(i, ani);

                    }
                }
                break;

            case MERGE:
                if (ani.timeStamp < System.currentTimeMillis() ) {
                    if (ani.count >= ani.maxCount) {    // smooth factor
                        gInfo.cells[ani.xS][ani.yS].state = GInfo.STATE.MERGED;
                        gInfo.cells[ani.xS][ani.yS].index = ani.index;
                        ani.count = 111;
                    } else {
                        ani.count++;
                        ani.timeStamp = System.currentTimeMillis() + ani.delay;
                        gInfo.aniStacks.set(i, ani);
                    }
                }
                break;

            case EXPLODED:
            case GO_UP:
                break;

            case PULL:
                if (ani.timeStamp < System.currentTimeMillis() ) {
                    if (ani.count >= ani.maxCount) {    // smooth factor
                        gInfo.cells[ani.xS][ani.yS].index = 0;
                        gInfo.cells[ani.xS][ani.yS].state = GInfo.STATE.PAUSED;
                        gInfo.cells[ani.xF][ani.yF].index = ani.index;
                        gInfo.cells[ani.xF][ani.yF].state = GInfo.STATE.STOP;
                        ani.count = 111;    // 111 means this ani should be removed soon
                        gInfo.aniStacks.set(i, ani);
                    } else {
                        Matrix matrix = new Matrix();
                        int angle =  (((ani.yS) % 2 == 0)? 40: -40) * ani.count;
                        matrix.postRotate(angle);
                        Bitmap blockMap = blockImages.get(ani.index).flyMaps[ani.count/2];
                        blockMap = Bitmap.createBitmap(blockMap, 0, 0,
                                blockMap.getWidth(), blockMap.getHeight()
                                , matrix, true);
                        int w = blockMap.getWidth(); int h = blockMap.getHeight();
                        int xPos = gInfo.xOffset + ani.xS * gInfo.blockOutSize
                                + gInfo.blockOutSize/2 - w/2;
                        int yPos = gInfo.yUpOffset + ani.yS * gInfo.blockOutSize
                                + gInfo.blockOutSize/2 - h/2;
                        canvas.drawBitmap(blockMap, xPos, yPos, null);
                        ani.count++;
                        ani.timeStamp = System.currentTimeMillis() + ani.delay;
                        gInfo.aniStacks.set(i, ani);
                    }
                }
                break;

            default:
                Log.w("drawCase", "not applied "+ani.state +" "+ani.xS +"x"+ani.yS +" >"+ani.xF +"x"+ani.yF);
                break;
        }
    }
}