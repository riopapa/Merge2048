package com.urrecliner.merge2048.GamePlate;

/*
 * Purpose of this Ani module is to draw, animate Game Plate
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.urrecliner.merge2048.GameInfo;
import com.urrecliner.merge2048.GameObject.BlockImage;
import com.urrecliner.merge2048.GameObject.ExplodeImage;
import com.urrecliner.merge2048.R;

import java.util.ArrayList;
import java.util.List;

public class Ani {

    public enum STATE {
        PAUSED,
        MOVING,
        ENDMOVE,
        STOP,
        CHECK,
        MERGE,
        ENDMERGE,
        EXPLODE,
        ENDEXPLODE;
    }

    Context context;
    public Cell [][] cells;
    public List<AniPool> aniPools;
    int screenXSize, screenYSize;
    int xBlockCnt, yBlockCnt, xOffset, yUpOffset, xBlockOutSize, yBlockOutSize;
    public List<BlockImage> blockImages;
    ExplodeImage explodeImage;
    GameInfo gameInfo;
    Paint explodePaint;
    int nextBlock, nextNBlock;  // next and next,next Block Index

    public Ani(GameInfo gameInfo, List<BlockImage> blockImages, ExplodeImage explodeImage, Context context){
        this.gameInfo = gameInfo;
        this.context = context;
        this.xOffset = gameInfo.xOffset;
        this.yUpOffset = gameInfo.yUpOffset;
        this.screenXSize = gameInfo.screenXSize;
        this.screenYSize = gameInfo.screenYSize;
        this.xBlockOutSize = gameInfo.xBlockOutSize;
        this.yBlockOutSize = gameInfo.yBlockOutSize;
        xBlockCnt = gameInfo.xBlockCnt;
        yBlockCnt = gameInfo.yBlockCnt;
        cells = new Cell[xBlockCnt][yBlockCnt];
        this.blockImages = blockImages;
        this.explodeImage = explodeImage;
        aniPools = new ArrayList<AniPool>();    // clear AniPool

        explodePaint = new Paint();
        explodePaint.setAlpha(180);

    }

    public void updateNext(int nextBlock, int nextNBlock) {
        this.nextBlock = nextBlock;
        this.nextNBlock = nextNBlock;
    }

    public void addMove(int xS, int yS, int xF, int yF) {

        aniPools.add(new AniPool(STATE.MOVING, xS, yS, xF, yF,
                gameInfo.xBlockOutSize * (xF - xS) / MOVE_SMOOTH,
                gameInfo.yBlockOutSize * (yF - yS)/ MOVE_SMOOTH));
    }

    public void addMerge(int x, int y) {
        aniPools.add(new AniPool(STATE.MERGE, x, y));
    }

    public void addExplode(int xS, int yS, int xF, int yF) {
        aniPools.add(new AniPool(STATE.EXPLODE, xS, yS,
                gameInfo.xBlockOutSize * (xF - xS) / MOVE_SMOOTH,
                gameInfo.yBlockOutSize * (yF - yS)/ MOVE_SMOOTH));
    }

    public void draw(Canvas canvas) {

        for (int y = 0; y < yBlockCnt; y++) {
            for (int x = 0; x < xBlockCnt; x++) {
                drawCell(canvas, x, y);
            }
        }
        drawAni(canvas);
    }

    public static final int MOVE_SMOOTH = 4;

    private void drawAni(Canvas canvas) {
        for (int apI = 0; apI < aniPools.size();) {
            AniPool ap = aniPools.get(apI);
            switch (ap.state) {
                case MOVING:
                    if (ap.timeStamp < System.currentTimeMillis() ) {
                        if (ap.count >= MOVE_SMOOTH) {    // smooth factor
                            cells[ap.xF][ap.yF] = cells[ap.xS][ap.yS];
                            cells[ap.xF][ap.yF].state = Ani.STATE.STOP;
                            cells[ap.xS][ap.yS] = new Cell(0, Ani.STATE.PAUSED);
                            aniPools.remove(apI);
                            continue;
                        } else {
                            if (cells[ap.xS][ap.yS].index > 0) {
                                BlockImage blockImage = blockImages.get(cells[ap.xS][ap.yS].index);
                                int xPos = gameInfo.xOffset + ap.xS * gameInfo.xBlockOutSize
                                        + ap.xInc * ap.count;
                                int yPos = gameInfo.yUpOffset + ap.yS * gameInfo.yBlockOutSize
                                        + ap.yInc * ap.count;
                                canvas.drawBitmap(blockImage.smallMaps[ap.count], xPos, yPos, null);
                                ap.count++;
                                ap.timeStamp = System.currentTimeMillis() + ap.delay;
                                aniPools.set(apI, ap);
                            }
                        }
                        apI++;
                    }
                    break;

                case EXPLODE:
                    if (ap.timeStamp < System.currentTimeMillis() ) {
                        if (ap.count >= MOVE_SMOOTH) {    // smooth factor
                            cells[ap.xS][ap.yS].state = STATE.ENDEXPLODE;
                            aniPools.remove(apI);
                            continue;
                        } else {
                            Bitmap explodeMap = explodeImage.smallMaps[ap.count];
                            int xPos = gameInfo.xOffset + ap.xS * gameInfo.xBlockOutSize
                                    + ap.xInc * ap.count;
                            int yPos = gameInfo.yUpOffset + ap.yS * gameInfo.yBlockOutSize
                                    + ap.yInc * ap.count;
                            BlockImage blockImage = blockImages.get(cells[ap.xS][ap.yS].index);
                            canvas.drawBitmap(blockImage.smallMaps[ap.count],
                                    xPos, yPos, null);
                            xPos -= explodeImage.explodeGap;
                            yPos -= explodeImage.explodeGap;
                            canvas.drawBitmap(explodeMap, xPos, yPos,explodePaint);
                            ap.count++;
                            ap.timeStamp = System.currentTimeMillis() + ap.delay;
                            aniPools.set(apI, ap);
                        }
                        apI++;
                    }
                    break;

                case MERGE:
                    if (ap.timeStamp < System.currentTimeMillis() ) {
                        if (ap.count >= MOVE_SMOOTH) {    // smooth factor
                            cells[ap.xS][ap.yS].state = STATE.ENDMERGE;
                            BlockImage blockImage = blockImages.get(cells[ap.xS][ap.yS].index);
                            int xPos = gameInfo.xOffset + ap.xS * gameInfo.xBlockOutSize;
                            int yPos = gameInfo.yUpOffset + ap.yS * gameInfo.yBlockOutSize;
                            canvas.drawBitmap(blockImage.bitmap, xPos, yPos, null);
                            aniPools.remove(apI);
                            continue;
                        } else {
                            ap.count++;
                            ap.timeStamp = System.currentTimeMillis() + ap.delay;
                            aniPools.set(apI, ap);
                        }
                        apI++;
                    }
                    break;

                case ENDMOVE:
                case ENDEXPLODE:
                case CHECK:
                    break;

                default:
                    Log.w("drawAni", "not applied "+ap.state +" "+ap.xS +"x"+ap.yS +" number = "+cells[ap.xS][ap.yS].number);
                    break;
            }
        }
    }

    private void drawCell(Canvas canvas, int x, int y) {
        Cell cell = cells[x][y];
        BlockImage blockImage = blockImages.get(cell.index);
        switch (cell.state) {
            case PAUSED:
                blockImage.draw(canvas, xOffset + x * xBlockOutSize,
                        yUpOffset + y * yBlockOutSize);
                break;

            case MERGE:
            case EXPLODE:
            case MOVING:
            case CHECK:
            case ENDMOVE:
                break;
            default:
                Log.w("not applied", x+"x"+y+" state = " + cell.state);
        }
    }


}