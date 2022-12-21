package com.urrecliner.merge2048;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

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
    private final CheckNearItem checkNearItem;
    private GameLoop gameLoop;
    private final int xNewPosS, yNewPosS, xNewPosE, yNewPosE;
    private final int xOffset, yDownOffset, yNextBottom, blockOutSize;
    List<BlockImage> blockImages;
    int xBlockCnt = 5, yBlockCnt = 6;   // screen Size
    boolean blockClicked = false;   // clicked means user clicked
    int touchIndex;               // user selected x Index (0 ~ xBlockCnt)
    boolean isGameOver = false;
    boolean newGamePressed = false;
    final View gameView;

    public Game(Context context, View gameView) {
        super(context);
        this.gameView = gameView;
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
        checkNearItem = new CheckNearItem(gameInfo, context, ani);
        xOffset = gameInfo.xOffset; yDownOffset = gameInfo.yDownOffset;
        blockOutSize = gameInfo.blockOutSize;

        yNextBottom = gameInfo.yNextPos + blockOutSize + 4;

        xNewPosS = gameInfo.xNewPos;
        xNewPosE = xNewPosS + gameInfo.blockOutSize*2/3;
        yNewPosS = gameInfo.yNewPosS;
        yNewPosE = yNewPosS+ gameInfo.blockOutSize*2/3;

        gameStart();


    }

    void gameStart() {
        gameInfo.scoreNow = 0;
        clearCells();   // clea all cells
        nextBlocks.generateNextBlock();
        isGameOver = false;
    }

    void clearCells() {
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

        if (gameInfo.newGameStart) {
            gameInfo.newGameStart = false;
            gameStart();
        }
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
                        checkNearItem.check(x, y);
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
        if (!isGameOver && blockClicked)
            start2Move();
        if (newGamePressed) {

        }
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

    private void start2Move() {
        Cell cell = ani.cells[touchIndex][yBlockCnt-1];
        if (cell.index == 0) {  // empty cell, so start to move
            blockClicked = false;
            ani.cells[touchIndex][yBlockCnt-1] = new Cell(nextBlocks.nextIndex, Ani.STATE.CHECK);
            nextBlocks.generateNextBlock();
        } else if (cell.index == nextBlocks.nextIndex) {    // bottom but same index
            ani.cells[touchIndex][yBlockCnt-1].index = cell.index + 1;
            ani.cells[touchIndex][yBlockCnt-1].state = Ani.STATE.STOP;
        } else {
            blockClicked = false;    // cannot move, ignore this try
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
                if (yTouchPos > yDownOffset) {
                    if (gameInfo.newGamePressed) {
                        if (xTouchPos >= xNewPosE &&
                            xTouchPos <= xNewPosE + gameInfo.blockOutSize*2/3 &&
                            yTouchPos >= yNewPosS && yTouchPos <= yNewPosE) {
                            gameInfo.newGamePressed = false;
                            gameInfo.newGameStart = true;
                        } else if (xTouchPos >= xNewPosE + gameInfo.blockOutSize*2/3 &&
                                    xTouchPos <= xNewPosE + gameInfo.blockOutSize*4/3 &&
                                    yTouchPos >= yNewPosS && yTouchPos <= yNewPosE) {
                                gameInfo.newGamePressed = false;
                                gameInfo.newGameStart = false;
                        }

                    } else if (xTouchPos >= xNewPosS && xTouchPos <= xNewPosE &&
                            yTouchPos >= yNewPosS && yTouchPos <= yNewPosE) {
                        gameInfo.newGamePressed = true;
                    } else if (yTouchPos <= yNextBottom){
                        xTouchPos -= xOffset;
                        if (xTouchPos > 0) {
                            touchIndex = xTouchPos / blockOutSize;
                            if (touchIndex < xBlockCnt) {
                                blockClicked = true;
                            }
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