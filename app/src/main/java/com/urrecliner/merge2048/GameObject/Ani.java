package com.urrecliner.merge2048.GameObject;

/*
 * Purpose of this Ani module is to draw, animate Game Plate
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import com.urrecliner.merge2048.GameImage.BlockImage;
import com.urrecliner.merge2048.GameImage.ExplodeImage;
import com.urrecliner.merge2048.GameInfo;

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
    GameInfo gameInfo;

    public Ani(GameInfo gameInfo, List<BlockImage> blockImages, ExplodeImage explodeImage, Context context){
        this.gameInfo = gameInfo;
        this.context = context;
        this.xOffset = gameInfo.xOffset;
        this.yUpOffset = gameInfo.yUpOffset;
        this.screenXSize = gameInfo.screenXSize;
        this.screenYSize = gameInfo.screenYSize;
        this.xBlockOutSize = gameInfo.blockOutSize;
        this.yBlockOutSize = gameInfo.blockOutSize;
        xBlockCnt = gameInfo.xBlockCnt;
        yBlockCnt = gameInfo.yBlockCnt;
        cells = new Cell[xBlockCnt][yBlockCnt];
        this.blockImages = blockImages;
        this.explodeImage = explodeImage;
        poolAnis = new ArrayList<>();    // clear PoolAni

    }

    public void addMove(int xS, int yS, int xF, int yF) {
        int maxCount = (yF-yS > 2) ? (yF-yS) / 2: 1;
        maxCount *= MOVE_SMOOTH;
        poolAnis.add(new PoolAni(GameInfo.STATE.MOVING, xS, yS, xF, yF,
                gameInfo.blockOutSize * (xF - xS) / maxCount,
                gameInfo.blockOutSize * (yF - yS)/ maxCount, maxCount));
    }

    public void addMerge(int x, int y, int index) {
        poolAnis.add(new PoolAni(GameInfo.STATE.MERGE, x, y, index));
    }

    public void addExplode(int xS, int yS, int xF, int yF) {
        poolAnis.add(new PoolAni(GameInfo.STATE.EXPLODE, xS, yS,
                gameInfo.blockOutSize * (xF - xS) / MOVE_SMOOTH,
                gameInfo.blockOutSize * (yF - yS)/ MOVE_SMOOTH));
    }

    public void draw(Canvas canvas) {

        // reDraw if Paused
        for (int y = 0; y < yBlockCnt; y++) {
            for (int x = 0; x < xBlockCnt; x++) {
                if (cells[x][y].state == GameInfo.STATE.PAUSED && cells[x][y].index > 0) {
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
                            cells[ap.xF][ap.yF] = cells[ap.xS][ap.yS];
                            cells[ap.xF][ap.yF].state = GameInfo.STATE.STOP;
                            cells[ap.xS][ap.yS] = new Cell(0, GameInfo.STATE.PAUSED);
                            poolAnis.remove(apI);
                            continue;
                        } else {
                            if (cells[ap.xS][ap.yS].index > 0) {
                                Bitmap blockMap = blockImages.get(cells[ap.xS][ap.yS].index).flyMaps[ap.count];
                                int xPos = gameInfo.xOffset + ap.xS * gameInfo.blockOutSize
                                        + ap.xInc * ap.count - gameInfo.blockFlyingGap;
                                int yPos = gameInfo.yUpOffset + ap.yS * gameInfo.blockOutSize
                                        + ap.yInc * ap.count - gameInfo.blockFlyingGap;
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
                            cells[ap.xS][ap.yS].state = GameInfo.STATE.EXPLODED;
                            poolAnis.remove(apI);
                            continue;
                        } else {
                            Bitmap explodeMap = explodeImage.smallMaps[ap.count];
                            int xPos = gameInfo.xOffset + ap.xS * gameInfo.blockOutSize
                                    + ap.xInc * ap.count - gameInfo.explodeGap;
                            int yPos = gameInfo.yUpOffset + ap.yS * gameInfo.blockOutSize
                                    + ap.yInc * ap.count - gameInfo.explodeGap;
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
                            cells[ap.xS][ap.yS].state = GameInfo.STATE.MERGED;
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
        gameInfo.poolAniSize = poolAnis.size();
    }

//    public void dumpCells() {
//        Log.w("d", "       0        1        2        3        4");
//        for (int y = 0; y < yBlockCnt; y++) {
//            String s = y+") ";
//            for (int x = 0; x < xBlockCnt; x++) {
//                s += cells[x][y].index+" "+cells[x][y].state+" ";
//            }
//            Log.w("d",s);
//        }
//    }

}