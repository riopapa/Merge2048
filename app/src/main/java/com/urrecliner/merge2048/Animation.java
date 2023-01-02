package com.urrecliner.merge2048;

/*
 * Purpose of this Animation module is to draw, animate Game Plate
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import com.urrecliner.merge2048.GInfo;
import com.urrecliner.merge2048.GameImage.BlockImage;
import com.urrecliner.merge2048.GameImage.ExplodeImage;
import com.urrecliner.merge2048.GameObject.AniStack;

import java.util.List;

public class Animation {

    final int smooth;

    Context context;
    int screenXSize, screenYSize;
    int xBlockCnt, yBlockCnt, xOffset, yUpOffset, xBlockOutSize, yBlockOutSize;
    public List<BlockImage> blockImages;
    ExplodeImage explodeImage;
    final GInfo gInfo;

    public Animation(GInfo gInfo, List<BlockImage> blockImages, ExplodeImage explodeImage, Context context, int smooth){
        this.gInfo = gInfo;
        this.context = context;
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
        for (int y = 0; y < yBlockCnt; y++) {
            for (int x = 0; x < xBlockCnt; x++) {
                if (gInfo.cells[x][y].state == GInfo.STATE.PAUSED && gInfo.cells[x][y].index > 0) {
                    Bitmap blockMap = blockImages.get(gInfo.cells[x][y].index).bitmap;
                    canvas.drawBitmap(blockMap, xOffset + x * xBlockOutSize,
                            yUpOffset + y * yBlockOutSize, null);
                }
            }
        }

        for (int apI = gInfo.aniStacks.size()-1 ; apI >= 0; apI--) {
            if (gInfo.aniStacks.get(apI).count == 100) {
                gInfo.aniStacks.remove(apI);
            }
        }
        // draw Animation while ani active
        for (int apI = 0; apI < gInfo.aniStacks.size(); apI++) {
            drawCase(canvas, apI);
        }
    }

    private void drawCase(Canvas canvas, int apI) {
        AniStack ani = gInfo.aniStacks.get(apI);
        switch (ani.state) {
            case MOVING:
                if (ani.timeStamp < System.currentTimeMillis() ) {
                    if (ani.count >= ani.maxCount) {    // smooth factor
                        gInfo.cells[ani.xS][ani.yS].index = 0;
                        gInfo.cells[ani.xS][ani.yS].state = GInfo.STATE.PAUSED;
                        gInfo.cells[ani.xF][ani.yF].index = ani.block;
                        gInfo.cells[ani.xF][ani.yF].state = GInfo.STATE.STOP;
                        ani.count = 100;
                        gInfo.aniStacks.set(apI, ani);
                    } else {
                        Bitmap blockMap = blockImages.get(ani.block).flyMaps[ani.count];
                        int xPos = gInfo.xOffset + ani.xS * gInfo.blockOutSize
                                + ani.xInc * ani.count - gInfo.blockFlyingGap;
                        int yPos = gInfo.yUpOffset + ani.yS * gInfo.blockOutSize
                                + ani.yInc * ani.count - gInfo.blockFlyingGap;
                        canvas.drawBitmap(blockMap, xPos, yPos, null);
                        ani.count++;
                        ani.timeStamp = System.currentTimeMillis() + ani.delay;
                        gInfo.aniStacks.set(apI, ani);
                    }
                }
                break;

            case EXPLODE:
                if (ani.timeStamp < System.currentTimeMillis() ) {
                    if (ani.count >= smooth) {    // smooth factor
                        gInfo.cells[ani.xS][ani.yS].state = GInfo.STATE.EXPLODED;
                        gInfo.aniStacks.remove(apI);
                    } else {
                        Bitmap explodeMap = explodeImage.smallMaps[ani.count];
                        int xPos = gInfo.xOffset + ani.xS * gInfo.blockOutSize
                                + ani.xInc * ani.count - gInfo.explodeGap;
                        int yPos = gInfo.yUpOffset + ani.yS * gInfo.blockOutSize
                                + ani.yInc * ani.count - gInfo.explodeGap;
                        canvas.drawBitmap(explodeMap, xPos, yPos, null);
                        ani.count++;
                        ani.timeStamp = System.currentTimeMillis() + ani.delay;
                        gInfo.aniStacks.set(apI, ani);
                    }
                }
                break;

            case MERGE:
                if (ani.timeStamp < System.currentTimeMillis() ) {
                    if (ani.count >= smooth) {    // smooth factor
                        gInfo.cells[ani.xS][ani.yS].state = GInfo.STATE.MERGED;
                        gInfo.cells[ani.xS][ani.yS].index = ani.xF;  // xF is new Index
                        gInfo.aniStacks.remove(apI);
                    } else {
                        ani.count++;
                        ani.timeStamp = System.currentTimeMillis() + ani.delay;
                        gInfo.aniStacks.set(apI, ani);
                    }
                }
                break;

            case EXPLODED:
            case GO_UP:
                break;

            default:
                Log.w("drawAni", "not applied "+ani.state +" "+ani.xS +"x"+ani.yS +" >"+ani.xF +"x"+ani.yF);
                break;
        }
    }
}