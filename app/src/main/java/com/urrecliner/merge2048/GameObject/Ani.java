package com.urrecliner.merge2048.GameObject;

/*
 * Purpose of this Ani module is to draw, animate Game Plate
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import com.urrecliner.merge2048.GInfo;
import com.urrecliner.merge2048.GameImage.BlockImage;
import com.urrecliner.merge2048.GameImage.ExplodeImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Ani {

    final int MOVE_SMOOTH = 5;  // refer to flying map array

    Context context;
    public Cell[][] cells;
    public List<PoolAni> poolAnis;
    int screenXSize, screenYSize;
    int xBlockCnt, yBlockCnt, xOffset, yUpOffset, xBlockOutSize, yBlockOutSize;
    public List<BlockImage> blockImages;
    ExplodeImage explodeImage;
    GInfo gInfo;

    public Ani(GInfo gInfo, List<BlockImage> blockImages, ExplodeImage explodeImage, Context context){
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
        cells = new Cell[xBlockCnt][yBlockCnt];
        this.blockImages = blockImages;
        this.explodeImage = explodeImage;
        poolAnis = new ArrayList<>();    // clear PoolAni

    }

    public void addMove(int xS, int yS, int xF, int yF) {
        int maxCount = (yF-yS > 2) ? (yF-yS) / 2: 1;
        maxCount *= MOVE_SMOOTH;
        poolAnis.add(new PoolAni(GInfo.STATE.MOVING, xS, yS, xF, yF,
                gInfo.blockOutSize * (xF - xS) / maxCount,
                gInfo.blockOutSize * (yF - yS)/ maxCount, maxCount));
    }

    public void addMerge(int x, int y, int index) {
        poolAnis.add(new PoolAni(GInfo.STATE.MERGE, x, y, index));
    }

    public void addExplode(int xS, int yS, int xF, int yF) {
        poolAnis.add(new PoolAni(GInfo.STATE.EXPLODE, xS, yS,
                gInfo.blockOutSize * (xF - xS) / MOVE_SMOOTH,
                gInfo.blockOutSize * (yF - yS)/ MOVE_SMOOTH));
    }

    public void draw(Canvas canvas) {

        // reDraw if Paused
        for (int y = 0; y < yBlockCnt; y++) {
            for (int x = 0; x < xBlockCnt; x++) {
                if (cells[x][y].state == GInfo.STATE.PAUSED && cells[x][y].index > 0) {
                    Bitmap blockMap = blockImages.get(cells[x][y].index).bitmap;
                    canvas.drawBitmap(blockMap, xOffset + x * xBlockOutSize,
                            yUpOffset + y * yBlockOutSize, null);
                }
            }
        }

        // draw Animation while ani active
        for (int apI = 0; apI < poolAnis.size();) {
            PoolAni ap = poolAnis.get(apI);
            switch (ap.state) {
                case MOVING:
                    if (ap.timeStamp < System.currentTimeMillis() ) {
                        if (ap.count >= MOVE_SMOOTH) {    // smooth factor
                            if (cells[ap.xF][ap.yF].index == 0) {
                                cells[ap.xF][ap.yF] = cells[ap.xS][ap.yS];
                                cells[ap.xF][ap.yF].state = GInfo.STATE.STOP;
                                cells[ap.xS][ap.yS] = new Cell(0, GInfo.STATE.PAUSED);
                                poolAnis.remove(apI);
                                continue;
                            } else {
                                Log.e("Still","continue move? "+cells[ap.xF][ap.yF].index+" ("+ap.xF+"x"+ap.yF+")");
                                ap.count--;
                            }
                        } else {
                            if (cells[ap.xS][ap.yS].index > 0) {
                                Bitmap blockMap = blockImages.get(cells[ap.xS][ap.yS].index).flyMaps[ap.count];
                                int xPos = gInfo.xOffset + ap.xS * gInfo.blockOutSize
                                        + ap.xInc * ap.count - gInfo.blockFlyingGap;
                                int yPos = gInfo.yUpOffset + ap.yS * gInfo.blockOutSize
                                        + ap.yInc * ap.count - gInfo.blockFlyingGap;
                                canvas.drawBitmap(blockMap, xPos, yPos, null);
                                ap.count++;
                                ap.timeStamp = System.currentTimeMillis() + ap.delay
                                    + new Random().nextInt(30);
                                poolAnis.set(apI, ap);
                            }
                        }
                        apI++;
                    }
                    break;

                case EXPLODE:
                    if (ap.timeStamp < System.currentTimeMillis() ) {
                        if (ap.count >= MOVE_SMOOTH) {    // smooth factor
                            cells[ap.xS][ap.yS].state = GInfo.STATE.EXPLODED;
                            poolAnis.remove(apI);
                            continue;
                        } else {
                            Bitmap explodeMap = explodeImage.smallMaps[ap.count];
                            int xPos = gInfo.xOffset + ap.xS * gInfo.blockOutSize
                                    + ap.xInc * ap.count - gInfo.explodeGap;
                            int yPos = gInfo.yUpOffset + ap.yS * gInfo.blockOutSize
                                    + ap.yInc * ap.count - gInfo.explodeGap;
                            canvas.drawBitmap(explodeMap, xPos, yPos, null);
                            ap.count++;
                            ap.timeStamp = System.currentTimeMillis() + ap.delay;
                            poolAnis.set(apI, ap);
                        }
                        apI++;
                    }
                    break;

                case MERGE:
                    if (ap.timeStamp < System.currentTimeMillis() ) {
                        if (ap.count >= MOVE_SMOOTH) {    // smooth factor
                            cells[ap.xS][ap.yS].state = GInfo.STATE.MERGED;
                            cells[ap.xS][ap.yS].index = ap.xF;  // xF is new Index
                            poolAnis.remove(apI);
                            continue;
                        } else {
                            ap.count++;
                            ap.timeStamp = System.currentTimeMillis() + ap.delay;
                            poolAnis.set(apI, ap);
                        }
                        apI++;
                    }
                    break;

                case EXPLODED:
                case CHECK:
                    break;

                default:
                    Log.w("drawAni", "not applied "+ap.state +" "+ap.xS +"x"+ap.yS +" >"+ap.xF +"x"+ap.yF);
                    break;
            }
        }
        gInfo.poolAniSize = poolAnis.size();
    }
}