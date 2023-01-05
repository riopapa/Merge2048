package com.urrecliner.merge2048;

/*
 * Purpose of this Animation module is to draw, animate Game Plate
 */

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import com.urrecliner.merge2048.GameImage.BlockImage;
import com.urrecliner.merge2048.GameImage.ExplodeImage;
import com.urrecliner.merge2048.GameObject.AniStack;

import java.util.List;

public class Animation {

    final GInfo gInfo;
    final List<BlockImage> blockImages;
    final ExplodeImage explodeImage;
    final int smooth;

    final int screenXSize, screenYSize;
    final int xBlockCnt, yBlockCnt, xOffset, yUpOffset, xBlockOutSize, yBlockOutSize;

    public Animation(GInfo gInfo, List<BlockImage> blockImages, ExplodeImage explodeImage, int smooth){
        this.gInfo = gInfo;
        this.xOffset = gInfo.xOffset;
        this.yUpOffset = gInfo.yUpOffset;
        this.screenXSize = gInfo.screenXSize;
        this.screenYSize = gInfo.screenYSize;
        this.xBlockOutSize = gInfo.blockOutSize;
        this.yBlockOutSize = gInfo.blockOutSize;
        xBlockCnt = gInfo.xBlockCnt;
        yBlockCnt = gInfo.yBlockCnt;
        this.blockImages = blockImages;
        this.explodeImage = explodeImage;
        this.smooth = smooth;
    }

    public void draw(Canvas canvas) {

        // reDraw if Paused
        Bitmap blockMap;
        for (int y = 0; y < yBlockCnt; y++) {
            for (int x = 0; x < xBlockCnt; x++) {
                if (gInfo.cells[x][y].state == GInfo.STATE.PAUSED && gInfo.cells[x][y].index > 0) {
                    if (gInfo.cells[x][y].index < 11)
                        blockMap = blockImages.get(gInfo.cells[x][y].index).bitmap;
                    else {
                        gInfo.cells[x][y].count++;
                        if (gInfo.cells[x][y].xor && gInfo.cells[x][y].count > 40) {
                            gInfo.cells[x][y].count = 0;
                            gInfo.cells[x][y].xor = !gInfo.cells[x][y].xor;
                        } else if (!gInfo.cells[x][y].xor && gInfo.cells[x][y].count > 4) {
                            gInfo.cells[x][y].count = 0;
                            gInfo.cells[x][y].xor = !gInfo.cells[x][y].xor;
                        }
                        blockMap = gInfo.cells[x][y].xor?
                                blockImages.get(gInfo.cells[x][y].index).bitmap :
                                blockImages.get(gInfo.cells[x][y].index).flyMaps[8];
                    }
                    canvas.drawBitmap(blockMap, xOffset + x * xBlockOutSize,
                            yUpOffset + y * yBlockOutSize, null);
                }
            }
        }

        for (int i = 0; i < gInfo.aniStacks.size(); ) {
            if (gInfo.aniStacks.get(i).count == 100) {
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
                        gInfo.cells[ani.xF][ani.yF].index = ani.block;
                        gInfo.cells[ani.xF][ani.yF].state = GInfo.STATE.STOP;
                        ani.count = 100;
                        gInfo.aniStacks.set(i, ani);
                    } else {
                        Bitmap blockMap = blockImages.get(ani.block).flyMaps[ani.count];
                        int xPos = gInfo.xOffset + ani.xS * gInfo.blockOutSize
                                + ani.xInc * ani.count - gInfo.blockFlyingGap;
                        int yPos = gInfo.yUpOffset + ani.yS * gInfo.blockOutSize
                                + ani.yInc * ani.count - gInfo.blockFlyingGap;
                        canvas.drawBitmap(blockMap, xPos, yPos, null);
                        ani.count++;
                        ani.timeStamp = System.currentTimeMillis() + ani.delay;
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
                        Bitmap explodeMap = explodeImage.smallMaps[ani.count];
                        int xPos = gInfo.xOffset + ani.xS * gInfo.blockOutSize
                                + ani.xInc * ani.count - gInfo.explodeGap;
                        int yPos = gInfo.yUpOffset + ani.yS * gInfo.blockOutSize
                                + ani.yInc * ani.count - gInfo.explodeGap;
                        canvas.drawBitmap(explodeMap, xPos, yPos, null);
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
                        gInfo.cells[ani.xS][ani.yS].index = ani.block;
                        ani.count = 100;
//                        gInfo.aniStacks.remove(i);
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

            default:
                Log.w("drawCase", "not applied "+ani.state +" "+ani.xS +"x"+ani.yS +" >"+ani.xF +"x"+ani.yF);
                break;
        }
    }
}