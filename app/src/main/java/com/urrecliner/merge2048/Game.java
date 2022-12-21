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

import com.urrecliner.merge2048.GameObject.BlockImage;
import com.urrecliner.merge2048.GameObject.ExplodeImage;
import com.urrecliner.merge2048.GameObject.MakeBlockImage;
import com.urrecliner.merge2048.GamePlate.Ani;
import com.urrecliner.merge2048.GamePlate.BackPlate;
import com.urrecliner.merge2048.GamePlate.Cell;
import com.urrecliner.merge2048.GamePlate.GameOver;
import com.urrecliner.merge2048.GamePlate.NextBlocks;
import com.urrecliner.merge2048.GamePlate.Score;

import java.util.List;

class Game extends SurfaceView implements SurfaceHolder.Callback {
    public final GameInfo gameInfo;
    private final Score score;
    public final ExplodeImage explodeImage;
    private final GameOver gameOver;
    private final NextBlocks nextBlocks;
    private final Ani ani;
    private final BackPlate backPlate;
    private GameLoop gameLoop;
    List<BlockImage> blockImages;
    int xBlockCnt = 5, yBlockCnt = 6;   // screen Size

    boolean clicked = false;   // clicked means user clicked
    int touchIndex;               // user selected x Index (0 ~ xBlockCnt)
    boolean isGameOver = false;

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
        gameStart();

    }

    void gameStart() {
        gameInfo.scoreNow = 0;
        clear();   // clea all cells
        nextBlocks.generateNextBlock();
        isGameOver = false;
    }

    void clear() {
        for (int y = 0; y < yBlockCnt; y++) {
            for (int x = 0; x < xBlockCnt; x++) {
                ani.cells[x][y] = new Cell(0, Ani.STATE.PAUSED); // 0 means null
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
                    case MERGE:
                    case EXPLODE:
                        break;

                    case CHECK:
                        checkIfGoingUpPossible(x, y);
                        break;

                    case STOP:
                        checkIfSameNearItem(x, y);
                        break;

                    case ENDMERGE:
                        ani.cells[x][y].state = Ani.STATE.STOP;
                        break;

                    case ENDEXPLODE:
                        checkIfPullNextUp(x, y);
                        break;

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

    private void checkIfSameNearItem(int x, int y) {

        int index = ani.cells[x][y].index;
        int indexR = -1, indexL = -1, indexU = -1, number;

        if (x < xBlockCnt - 1)
            indexR = ani.cells[x +1][y].index;
        if (x > 0)
            indexL = ani.cells[x -1][y].index;
        if (y > 0)
            indexU = ani.cells[x][y -1].index;
        if (index == indexL && index == indexR && index == indexU) {
            index += 3;
            gameInfo.scoreNow += calcNumber(index);
            explodeThis(x -1, y, x, y -1);
            explodeThis(x +1, y, x, y -1);
            explodeThis(x, y, x, y -1);
            mergeToHere(x, y -1, index);
            return;
        }
        if (index == indexL && index == indexR) {
            index += 2;
            gameInfo.scoreNow += calcNumber(index);
            explodeThis(x -1, y, x, y);
            explodeThis(x +1, y, x, y);
            mergeToHere(x, y, index);
            return;
        }
        if (index == indexL && index == indexU) {
            index += 2;
            gameInfo.scoreNow += calcNumber(index);
            explodeThis(x -1, y, x, y -1);
            explodeThis(x, y, x, y -1);
            mergeToHere(x, y -1, index);
            return;
        }
        if (index == indexR && index == indexU) {
            index += 2;
            gameInfo.scoreNow += calcNumber(index);
            explodeThis(x + 1, y, x, y -1);
            explodeThis(x, y, x, y -1);
            mergeToHere(x, y -1, index);
            return;
        }
        if (index == indexL) {
            index++;
            gameInfo.scoreNow += calcNumber(index);
            explodeThis(x -1, y, x, y);
            mergeToHere(x, y, index);
            return;
        }
        if (index == indexR) {
            index++;
            gameInfo.scoreNow += calcNumber(index);
            explodeThis(x +1, y, x, y);
            mergeToHere(x, y, index);
            return;
        }
        if (index == indexU) {
            if (index == 1) {
                Log.w("Index ","2");
            }
            index++;
            gameInfo.scoreNow += calcNumber(index);
            explodeThis(x, y, x, y -1);
            mergeToHere(x, y -1, index);
            return;
        }
        ani.cells[x][y].state = Ani.STATE.PAUSED;
    }

    private void checkIfGoingUpPossible(int x, int y) {
        if (y > 0) {
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
//                Log.w("Add", y +" > "+yUp);
                return;
            }
        }
        ani.cells[x][y].state = Ani.STATE.STOP;
    }

    private void checkIfPullNextUp(int x, int y) {
        // this cell is exploded, so check whether below item can be moved up
        ani.cells[x][y] = new Cell(0, Ani.STATE.PAUSED);
        if (y < yBlockCnt - 1) {
            for (int yy = y + 1; yy < yBlockCnt; yy++) {
                if (ani.cells[x][yy].index > 0) {
                    ani.addMove(x, yy, x, yy-1);
                } else
                    break;
            }
        }
    }

    private void mergeToHere(int x, int y, int index) {
        ani.cells[x][y].state = Ani.STATE.MERGE;
        ani.addMerge(x,y, index);
    }

    private void explodeThis(int x, int y, int xTo, int yTo) {
        ani.cells[x][y].state = Ani.STATE.EXPLODE;
        ani.cells[x][y].index = 0;
        ani.addExplode(x,y, xTo, yTo);
    }

    private void start2Move() {
        Cell cell = ani.cells[touchIndex][yBlockCnt-1];
        if (cell.index == 0) {  // empty cell, so start to move
            clicked = false;
            ani.cells[touchIndex][yBlockCnt-1] = new Cell(nextBlocks.nextIndex, Ani.STATE.CHECK);
            nextBlocks.generateNextBlock();
        } else if (cell.index == nextBlocks.nextIndex) {    // bottom but same index
            ani.cells[touchIndex][yBlockCnt-1].index = cell.index + 1;
            ani.cells[touchIndex][yBlockCnt-1].state = Ani.STATE.STOP;
        } else {
            clicked = false;    // cannot move, ignore this try
        }
    }

    private void cellDump(String s, Cell cell, int x, int y) {
        Log.w("ani "+s,"state="+cell.state+" idx="+cell.index+" (" + x+ " x "+y+")" );
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
                ani.cells[x][yBlockCnt-1].state != Ani.STATE.PAUSED ||
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
                if (ani.poolAnis.size() != 0) // if animation not completed
                    return true;              // ignore touch Up
                xTouchPos = (int) event.getX();
                yTouchPos = (int) event.getY();
                if (yTouchPos > gameInfo.yDownOffset) {
                    xTouchPos -= gameInfo.xOffset;
                    if (xTouchPos > 0) {
                        touchIndex = xTouchPos / gameInfo.blockOutSize;
                        if (touchIndex < xBlockCnt) {
                            clicked = true;
//                            Log.w("touch", "xindex="+ touchIndex);
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