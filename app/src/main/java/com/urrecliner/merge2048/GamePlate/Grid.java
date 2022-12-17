package com.urrecliner.merge2048.GamePlate;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import com.urrecliner.merge2048.GameInfo;
import com.urrecliner.merge2048.GameObject.BlockImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Grid {

    Cell [][] cells;
    int xBlockCnt, yBlockCnt;
    List<BlockImage> blockImages;
    List<AniPool> aniPools;
    GameInfo gameInfo;

    public Grid(GameInfo gameInfo, List<BlockImage> blockImages){
        this.gameInfo = gameInfo;
        xBlockCnt = gameInfo.xBlockCnt;
        yBlockCnt = gameInfo.yBlockCnt;
        cells = new Cell[xBlockCnt][yBlockCnt];
        this.blockImages = blockImages;
        aniPools = new ArrayList<AniPool>();    // clear AniPool
    }

    public void clear() {
        for (int y = 0; y < yBlockCnt; y++) {
            for (int x = 0; x < xBlockCnt; x++) {
                cells[x][y] = new Cell(0); // -1, -1 means null
            }
        }
    }

    public void makeRandom() {
        for (int y = 0; y < yBlockCnt; y++) {
            for (int x = 0; x < xBlockCnt; x++) {
                int rnd = new Random().nextInt(10);
                cells[x][y] = new Cell(rnd);
            }
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawRect(0, 0, gameInfo.screenXSize, gameInfo.screenYSize, gameInfo.backPaint);
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
            if (ap.timeStamp < System.currentTimeMillis() ) {
                if (ap.aniType == 0) {  // move
                    if (ap.count >= MOVE_SMOOTH) {    // smooth factor
                        Cell cellF = cells[ap.xStart][ap.yStart];
                        cells[ap.xFinish][ap.yFinish] = cellF;
                        cells[ap.xStart][ap.yStart] = new Cell(0);
                        aniPools.remove(apI);
                        continue;
                    } else {
                        Cell cell = cells[ap.xStart][ap.yStart];
                        if (cell.index > 0) {
                            BlockImage blockImage = blockImages.get(cell.index);
                            int xPos = ap.xStart * gameInfo.xBlockOutSize + ap.xIncrease * ap.count;
                            int yPos = ap.yStart * gameInfo.yBlockOutSize + ap.yIncrease * ap.count;
                            canvas.drawBitmap(blockImage.smallMaps[ap.count], xPos, yPos, null);
                            ap.count++;
                            ap.timeStamp = System.currentTimeMillis() + ap.delay;
                            aniPools.set(apI, ap);
                        }
                    }
                }
            }
            apI++;
        }
    }

    private void drawCell(Canvas canvas, int x, int y) {
        Cell cell = cells[x][y];
        BlockImage blockImage = blockImages.get(cell.index);
        if (cell.active) {
            if (cell.timeStamp < System.currentTimeMillis() )    {
                Bitmap bitmap = blockImage.getBitmap();
                int xScale = bitmap.getWidth() * cell.percent / 100;
                int yScale = bitmap.getHeight() * cell.percent / 100;
                Bitmap newMap = Bitmap.createScaledBitmap(bitmap, xScale, yScale,false);
                int xPos = x * gameInfo.xBlockOutSize + gameInfo.xBlockOutSize * cell.percent /2;
                int yPos = y * gameInfo.yBlockOutSize + gameInfo.yBlockOutSize * cell.percent /2;
                canvas.drawBitmap(newMap, xPos, yPos, null);
                cell.timeStamp = System.currentTimeMillis() + cell.delay;
                cell.percent += 20;
                if (cell.percent >=100) {
                    cell.active = false;
                }
                cells[x][y] = cell;
            }
        } else {
            blockImage.draw(canvas, x * gameInfo.xBlockOutSize,
                    y * gameInfo.yBlockOutSize);
        }
    }

    public void moveCell(int xStart, int yStart, int xDelta, int yDelta) {
        int xT = xStart + xDelta;
        int yT = yStart + yDelta;
        aniPools.add(new AniPool(0, xStart, yStart, xT, yT, gameInfo.xBlockOutSize, gameInfo.yBlockOutSize));
    }

    public void set(int x, int y, int index) {
        cells[x][y] = new Cell(index);
    }

}