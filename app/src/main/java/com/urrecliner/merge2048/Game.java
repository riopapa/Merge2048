package com.urrecliner.merge2048;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.urrecliner.merge2048.GameObject.BlockImage;
import com.urrecliner.merge2048.GamePlate.Grid;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Game extends SurfaceView implements SurfaceHolder.Callback {
    private final GameInfo gameInfo;
    private Grid grid;
    private GameLoop gameLoop;
    Paint p, backPaint;
    int[][] xyValue;
    List<BlockImage> blockImages;
    int xBlockCnt = 5, yBlockCnt = 8;
    int nextNumber, nextNumber2;

    public Game(Context context) {
        super(context);
        SurfaceHolder surfaceHolder =getHolder();
        surfaceHolder.addCallback(this);
        gameLoop = new GameLoop(this, surfaceHolder);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        p = new android.graphics.Paint();
        backPaint = new Paint();
        backPaint.setColor(ContextCompat.getColor(context, R.color.c00000));
        gameInfo = new GameInfo(displayMetrics.widthPixels, displayMetrics.heightPixels,
                xBlockCnt, yBlockCnt, backPaint);
        buildBlockImage(context);
        grid = new Grid(gameInfo, blockImages);
        grid.clear();   // clea all cells
        grid.makeRandom();
    }

    private void buildBlockImage(Context context) {
        int [] colors = {
                ContextCompat.getColor(context, R.color.c00000),    // 0    blank
                ContextCompat.getColor(context, R.color.c00002),    // 1    2
                ContextCompat.getColor(context, R.color.c00004),    // 2    4
                ContextCompat.getColor(context, R.color.c00008),    // 3    8
                ContextCompat.getColor(context, R.color.c00016),    // 4    16
                ContextCompat.getColor(context, R.color.c00032),    // 5    32
                ContextCompat.getColor(context, R.color.c00064),    // 6    64
                ContextCompat.getColor(context, R.color.c00128),    // 7    128
                ContextCompat.getColor(context, R.color.c00256),    // 8    256
                ContextCompat.getColor(context, R.color.c00512),    // 9    512
                ContextCompat.getColor(context, R.color.c01024),    // 10   1024
                ContextCompat.getColor(context, R.color.c02048),    // 11   2048
                ContextCompat.getColor(context, R.color.c04096),    // 12   4096
                ContextCompat.getColor(context, R.color.c08192),    // 13   8192
                ContextCompat.getColor(context, R.color.c16384),    // 14   16384, just in case
                ContextCompat.getColor(context, R.color.c32768),    // 15   32768
                ContextCompat.getColor(context, R.color.c32768),    // 16   65536
                ContextCompat.getColor(context, R.color.c32768),    // 17   65536
                ContextCompat.getColor(context, R.color.c32768),    // 18   65536
                ContextCompat.getColor(context, R.color.c32768),    // 19   65536
                ContextCompat.getColor(context, R.color.c32768),    // 20   65536
                0x000000
        };
        blockImages = new ArrayList<>();
        int nbr = 0;
        for (int i = 0 ; i < 20; i++) {
            blockImages.add(new BlockImage(i, nbr, colors[i], gameInfo, context));
            if (nbr == 0)
                nbr = 1;
            nbr += nbr;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.w("Game.java", "surfaceCreated()");
        if (gameLoop.getState().equals(Thread.State.TERMINATED)) {
            SurfaceHolder surfaceHolder = getHolder();
            surfaceHolder.addCallback(this);
            gameLoop = new GameLoop(this, surfaceHolder);
        }
        gameLoop.startLoop();
    }

    public void update() {

        if (new Random().nextInt(300) > 284) {

            int xStart = new Random().nextInt(gameInfo.xBlockCnt);
            int yStart = new Random().nextInt(gameInfo.yBlockCnt);
            int x = new Random().nextInt(3) - 1;
            int y = new Random().nextInt(3) - 1;
            if ((xStart+x) >= 0 && (xStart+x) < gameInfo.xBlockCnt
                    && (yStart+y) >= 0 && (yStart+y) < gameInfo.yBlockCnt
                && (x != 0 & y != 0)) {
                grid.moveCell(xStart, yStart, x,y);
            }
        }
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        drawBackGround(canvas);
        grid.draw(canvas);
    }

    private void drawBackGround(Canvas canvas) {
        canvas.drawRect(0, 0, gameInfo.screenXSize, gameInfo.screenYSize, backPaint);
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {}

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        Log.w("game", "surfaceDestroyed");
    }

    public void pause() {
        gameLoop.stopLoop();
    }
}