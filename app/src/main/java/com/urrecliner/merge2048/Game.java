package com.urrecliner.merge2048;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.urrecliner.merge2048.GameObject.BlockImage;
import com.urrecliner.merge2048.GameObject.ExplodeImage;
import com.urrecliner.merge2048.GameObject.MakeBlockImage;
import com.urrecliner.merge2048.GamePlate.Ani;
import com.urrecliner.merge2048.GamePlate.BackPlate;
import com.urrecliner.merge2048.GamePlate.Cell;
import com.urrecliner.merge2048.GamePlate.GameOver;
import com.urrecliner.merge2048.GamePlate.NextBlocks;
import com.urrecliner.merge2048.GamePlate.Score;

import java.util.ArrayList;
import java.util.List;

class Game extends SurfaceView implements SurfaceHolder.Callback {
    public final GameInfo gameInfo;
    private final Score score;
    public final ExplodeImage explodeImage;
    private final GameOver gameOver;
    private final NextBlocks nextBlocks;
    private Ani ani;
    private GameLoop gameLoop;
    List<BlockImage> blockImages;
    int xBlockCnt = 5, yBlockCnt = 8;   // screen Size

    boolean clicked = false;   // clicked means user clicked
    int xIndex;               // user selected x Index (0 ~ xBlockCnt)
    boolean isGameOver = false;
    private BackPlate backPlate;

    public Game(Context context) {
        super(context);
        SurfaceHolder surfaceHolder =getHolder();
        surfaceHolder.addCallback(this);
        gameLoop = new GameLoop(this, surfaceHolder);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        gameInfo = new GameInfo(displayMetrics.widthPixels, displayMetrics.heightPixels,
                xBlockCnt, yBlockCnt);
        blockImages = new MakeBlockImage().make(context, gameInfo);
        explodeImage = new ExplodeImage(gameInfo, context);
        ani = new Ani(gameInfo, blockImages, explodeImage, context);
        nextBlocks = new NextBlocks(gameInfo, context);
        score = new Score(gameInfo, context);
        backPlate = new BackPlate(gameInfo, context);
        gameOver = new GameOver(gameInfo, context);
        newGame();
    }

    void newGame() {
        gameInfo.scoreNow = 0;
        clear();   // clea all cells
        nextBlocks.generateNextBlock();
        isGameOver = false;
    }

    void clear() {
        for (int y = 0; y < yBlockCnt; y++) {
            for (int x = 0; x < xBlockCnt; x++) {
                ani.cells[x][y] = new Cell(0); // 0 means null
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (gameLoop.getState().equals(Thread.State.TERMINATED)) {
            SurfaceHolder surfaceHolder = getHolder();
            surfaceHolder.addCallback(this);
            gameLoop = new GameLoop(this, surfaceHolder);
        }
        gameLoop.startLoop();
    }

    public void update() {

        for (int y = yBlockCnt - 1; y >= 0; y--) {
            for (int x = 0; x < xBlockCnt; x++) {
                if (ani.cells[x][y].state == Ani.STATE.EXPLODE) {
                    return; // wait till all exploded
                }
            }
        }
        for (int y = 0; y < yBlockCnt; y++) {
            for (int x = 0; x < xBlockCnt; x++) {
                switch (ani.cells[x][y].state) {
                    case PAUSED:
                    case MOVING:
                        break;

                    case CHECK:
                        if (y > 0) {    // check for going up
                            int yUp = 0;
                            for (int yy = y - 1; yy >= 0; yy--) {
                                if (ani.cells[x][yy].index != 0) {
                                    yUp = yy + 1;
                                    break;
                                }
                            }
                            if (yUp != y) {
                                ani.cells[x][y].state = Ani.STATE.MOVING;
                                ani.addMove(x, y, x, yUp);
                                Log.w("Add",y+" > "+yUp);
                                break;
                            }
                        }
                        ani.cells[x][y].state = Ani.STATE.PAUSED;
//                        cellDump("paused", ani.cells[x][y], x, y);
                        break;
                    case STOP:  // stoped moving so check Cross
                        int index = ani.cells[x][y].index;
                        int indexR = -1, indexL = -1, indexU = -1, number;

                        if (x < xBlockCnt - 1)
                            indexR = ani.cells[x+1][y].index;
                        if (x > 0)
                            indexL = ani.cells[x-1][y].index;
                        if (y > 0)
                            indexU = ani.cells[x][y-1].index;
                        if (index == indexL && index == indexR && index == indexU) {
                            index += 3; number = calcNumber(index);
                            ani.cells[x][y-1].index = index;
                            ani.cells[x][y-1].number = number;
                            gameInfo.scoreNow += number;
                            mergeThis(x,y-1);
                            explodeThis(x-1, y, x, y-1);
                            explodeThis(x+1, y, x, y-1);
                            explodeThis(x, y, x, y-1);
                            break;
                        }
                        if (index == indexL && index == indexR) {
                            index += 2; number = calcNumber(index);
                            ani.cells[x][y].index = index;
                            ani.cells[x][y].number = number;
                            gameInfo.scoreNow += number;
                            mergeThis(x,y);
                            explodeThis(x-1, y, x, y);
                            explodeThis(x+1, y, x, y);
                            break;
                        }
                        if (index == indexL && index == indexU) {
                            index += 2; number = calcNumber(index);
                            ani.cells[x][y-1].index = index;
                            ani.cells[x][y-1].number = number;
                            gameInfo.scoreNow += number;
                            mergeThis(x,y-1);
                            explodeThis(x-1, y, x, y-1);
                            explodeThis(x, y, x, y-1);
                            break;
                        }
                        if (index == indexR && index == indexU) {
                            index += 2; number = calcNumber(index);
                            ani.cells[x][y-1].index = index;
                            ani.cells[x][y-1].number = number;
                            gameInfo.scoreNow += number;
                            mergeThis(x,y-1);
                            explodeThis(x + 1, y, x, y-1);
                            explodeThis(x, y, x, y-1);
                            break;
                        }
                        if (index == indexL) {
                            index += 1; number = calcNumber(index);
                            ani.cells[x][y].index = index;
                            ani.cells[x][y].number = number;
                            gameInfo.scoreNow += number;
                            mergeThis(x,y);
                            explodeThis(x-1, y, x, y);
                            break;
                        }
                        if (index == indexR) {
                            index += 1; number = calcNumber(index);
                            ani.cells[x][y].index = index;
                            ani.cells[x][y].number = number;
                            gameInfo.scoreNow += number;
                            mergeThis(x,y);
                            explodeThis(x+1, y, x, y);
                            break;
                        }
                        if (index == indexU) {
                            index += 1; number = calcNumber(index);
                            ani.cells[x][y-1].index = index;
                            ani.cells[x][y-1].number = number;
                            gameInfo.scoreNow += number;
                            mergeThis(x,y-1);
                            explodeThis(x, y, x, y-1);
                            break;
                        }
                        ani.cells[x][y].state = Ani.STATE.PAUSED;
                        break;

                    case ENDMERGE:
                        ani.cells[x][y].state = Ani.STATE.STOP;
                        break;

                    case ENDEXPLODE:
                        ani.cells[x][y] = new Cell(0, Ani.STATE.PAUSED);
                        checkDown2Up(x,y);
                        break;

                    case MERGE:
                    default:
                        cellDump("unknown", ani.cells[x][y], x, y);
                        break;
                }
            }
        }
        checkGameOver();
        if (!isGameOver && clicked)
            start2Move();

    }

    private void checkDown2Up(int xNow, int yNow) {
        if (yNow < yBlockCnt - 1) {
            for (int y = yNow + 1; y < yBlockCnt; y++) {
                if (ani.cells[xNow][y].index > 0) {
                    ani.addMove(xNow, y, xNow, y-1);
                } else
                    break;
            }
        }
    }

    private void mergeThis(int x, int y) {
        ani.cells[x][y].state = Ani.STATE.MERGE;
        ani.addMerge(x,y);
    }

    private void explodeThis(int x, int y, int xTo, int yTo) {
        ani.cells[x][y].state = Ani.STATE.EXPLODE;
        ani.addExplode(x,y, xTo, yTo);
    }

    private void start2Move() {
        Cell cell = ani.cells[xIndex][yBlockCnt-1];
        if (cell.index == 0) {  // empty cell, move start
            clicked = false;
            ani.cells[xIndex][yBlockCnt-1] = new Cell(nextBlocks.nextIndex, Ani.STATE.CHECK);
            nextBlocks.generateNextBlock();
        }
    }

    private void cellDump(String s, Cell cell, int x, int y) {
        Log.w("ani "+s,"state="+cell.state+" idx="+cell.index+" num="+cell.number+" (" + x+ " x "+y+")" );
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        backPlate.draw(canvas);
        nextBlocks.draw(canvas, ani.blockImages.get(nextBlocks.nextIndex).bitmap,
                ani.blockImages.get(nextBlocks.nNextIndex).halfMap);
        ani.draw(canvas);
        score.draw(canvas);
        if (isGameOver)
            gameOver.draw(canvas);
    }

    private int calcNumber(int index) {
        int number;
        if (index != 0) {
            number = 1;
            while (index-- > 0)
                number = number + number;
        } else
            number = 0;
        return number;
    }

    void checkGameOver() {
        for (int x = 0; x < xBlockCnt; x++) {
            if (ani.cells[x][yBlockCnt-1].index == 0 ||
                ani.cells[x][yBlockCnt-1].index == nextBlocks.nextIndex)
                return;
        }
        isGameOver = true;

    }
    int xTouchPos, yTouchPos;
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // Handle user input touch event actions
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if (ani.aniPools.size() != 0) // if animation not completed
                    return true;              // ignore touch Up
                xTouchPos = (int) event.getX();
                yTouchPos = (int) event.getY();
                if (yTouchPos > gameInfo.yDownOffset) {
                    xTouchPos -= gameInfo.xOffset;
                    if (xTouchPos > 0) {
                        xTouchPos /= gameInfo.xBlockOutSize;
                        if (xTouchPos < xBlockCnt) {
                            clicked = true;
                            xIndex = xTouchPos;
                            Log.w("touch", "xindex="+xIndex);
                        }
                    }
                }
                return true;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                return true;
        }

        return super.onTouchEvent(event);
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