package com.urrecliner.merge2048.GamePlate;

/*
 * Purpose of this Ani module is to draw, animate Game Plate
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import com.urrecliner.merge2048.GameInfo;
import com.urrecliner.merge2048.GameObject.BlockImage;
import com.urrecliner.merge2048.GameObject.CountsImage;
import com.urrecliner.merge2048.GameObject.ExplodeImage;

import java.util.ArrayList;
import java.util.List;

public class Ani {

    public enum STATE {
        PAUSED, MOVING, STOP, CHECK, MERGE, MERGED, EXPLODE, GREAT, EXPLODED
    }

    Context context;
    public Cell [][] cells;
    public List<PoolAni> poolAnis;
    int screenXSize, screenYSize;
    int xBlockCnt, yBlockCnt, xOffset, yUpOffset, xBlockOutSize, yBlockOutSize;
    public List<BlockImage> blockImages;
    ExplodeImage explodeImage;
    CountsImage countsImage;
    GameInfo gameInfo;
    Paint explodePaint;

    public Ani(GameInfo gameInfo, List<BlockImage> blockImages, ExplodeImage explodeImage,
               CountsImage countsImage, Context context){
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
        this.countsImage = countsImage;
        poolAnis = new ArrayList<>();    // clear PoolAni

        explodePaint = new Paint();
        explodePaint.setAlpha(180);

    }

    public void addMove(int xS, int yS, int xF, int yF) {
        poolAnis.add(new PoolAni(STATE.MOVING, xS, yS, xF, yF,
                gameInfo.blockOutSize * (xF - xS) / MOVE_SMOOTH,
                gameInfo.blockOutSize * (yF - yS)/ MOVE_SMOOTH));
    }

    public void addMerge(int x, int y, int index) {
        poolAnis.add(new PoolAni(STATE.MERGE, x, y, index));
    }

    public void addExplode(int xS, int yS, int xF, int yF) {
        poolAnis.add(new PoolAni(STATE.EXPLODE, xS, yS,
                gameInfo.blockOutSize * (xF - xS) / MOVE_SMOOTH,
                gameInfo.blockOutSize * (yF - yS)/ MOVE_SMOOTH));
    }

    public void addGreat(int xS, int yS, int countIdx) {
        poolAnis.add(new PoolAni(STATE.GREAT, xS, yS,
                gameInfo.blockOutSize * (- xS) / gameInfo.greatCount,
                gameInfo.blockOutSize * (yBlockCnt - yS + 1)/ gameInfo.greatCount, countIdx));
    }

    public void draw(Canvas canvas) {

        // reDraw if Paused
        for (int y = 0; y < yBlockCnt; y++) {
            for (int x = 0; x < xBlockCnt; x++) {
                if (cells[x][y].state == STATE.PAUSED && cells[x][y].index > 0) {
                    BlockImage blockImage = blockImages.get(cells[x][y].index);
                    blockImage.draw(canvas, xOffset + x * xBlockOutSize,
                            yUpOffset + y * yBlockOutSize);
                }
            }
        }
        // draw Animation
        drawAni(canvas);
    }

    public static final int MOVE_SMOOTH = 4;

    private void drawAni(Canvas canvas) {
        for (int apI = 0; apI < poolAnis.size();) {
            PoolAni ap = poolAnis.get(apI);
            switch (ap.state) {
                case MOVING:
                    if (ap.timeStamp < System.currentTimeMillis() ) {
                        if (ap.count >= MOVE_SMOOTH) {    // smooth factor
                            cells[ap.xF][ap.yF] = cells[ap.xS][ap.yS];
                            cells[ap.xF][ap.yF].state = Ani.STATE.STOP;
                            cells[ap.xS][ap.yS] = new Cell(0, Ani.STATE.PAUSED);
                            poolAnis.remove(apI);
                            continue;
                        } else {
                            if (cells[ap.xS][ap.yS].index > 0) {
                                BlockImage blockImage = blockImages.get(cells[ap.xS][ap.yS].index);
                                int xPos = gameInfo.xOffset + ap.xS * gameInfo.blockOutSize
                                        + ap.xInc * ap.count;
                                int yPos = gameInfo.yUpOffset + ap.yS * gameInfo.blockOutSize
                                        + ap.yInc * ap.count;
                                canvas.drawBitmap(blockImage.smallMaps[ap.count], xPos, yPos, null);
                                ap.count++;
                                ap.timeStamp = System.currentTimeMillis() + ap.delay;
                                poolAnis.set(apI, ap);
                            }
                        }
                        apI++;
                    }
                    break;

                case EXPLODE:
                    if (ap.timeStamp < System.currentTimeMillis() ) {
                        if (ap.count >= MOVE_SMOOTH) {    // smooth factor
                            cells[ap.xS][ap.yS].state = STATE.EXPLODED;
                            poolAnis.remove(apI);
                            continue;
                        } else {
                            Bitmap explodeMap = explodeImage.smallMaps[ap.count];
                            int xPos = gameInfo.xOffset + ap.xS * gameInfo.blockOutSize
                                    + ap.xInc * ap.count;
                            int yPos = gameInfo.yUpOffset + ap.yS * gameInfo.blockOutSize
                                    + ap.yInc * ap.count;
                            BlockImage blockImage = blockImages.get(cells[ap.xS][ap.yS].index);
                            canvas.drawBitmap(blockImage.smallMaps[ap.count],
                                    xPos, yPos, null);
                            xPos -= explodeImage.explodeGap;
                            yPos -= explodeImage.explodeGap;
                            canvas.drawBitmap(explodeMap, xPos, yPos,explodePaint);
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
                            cells[ap.xS][ap.yS].state = STATE.MERGED;
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

                case GREAT:
                    if (ap.timeStamp < System.currentTimeMillis() ) {
                        if (ap.count >= gameInfo.greatCount) {    // smooth factor
                            poolAnis.remove(apI);
                            gameInfo.scoreNow += (long) gameInfo.greatStacked * (long) gameInfo.greatIdx;
                            continue;
                        } else {
                            Bitmap bitmap = countsImage.countMaps[ap.xF];   // countIdx
                            int xPos = gameInfo.xOffset + ap.xS * gameInfo.blockOutSize
                                    + ap.xInc * ap.count;
                            int yPos = gameInfo.yUpOffset + ap.yS * gameInfo.blockOutSize
                                    + ap.yInc * ap.count;
                            canvas.drawBitmap(bitmap, xPos, yPos, null);
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
    }
}