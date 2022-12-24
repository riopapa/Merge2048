package com.urrecliner.merge2048;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.urrecliner.merge2048.GameImage.BlockImage;
import com.urrecliner.merge2048.GameImage.ExplodeImage;
import com.urrecliner.merge2048.GameImage.MakeBlockImage;
import com.urrecliner.merge2048.GameObject.Ani;
import com.urrecliner.merge2048.GamePlate.BasePlate;
import com.urrecliner.merge2048.GameObject.Cell;
import com.urrecliner.merge2048.GamePlate.GameOverPlate;
import com.urrecliner.merge2048.GamePlate.GreatPlate;
import com.urrecliner.merge2048.GamePlate.NextPlate;
import com.urrecliner.merge2048.GamePlate.ScorePlate;

import java.util.List;

class Game extends SurfaceView implements SurfaceHolder.Callback {
    public final GameInfo gameInfo;
    private final ScorePlate scorePlate;
    private final GameOverPlate gameOverPlate;
    private final NextPlate nextPlate;
    private final Ani ani;
    private final BasePlate basePlate;
    private final GreatPlate greatPlate;
    private final CheckNearItem checkNearItem;
    private final ManageHighScore manageHighScore;
    private final CheckGameOver checkGameOver;
    private final TouchEvent touchEvent;
    private GameLoop gameLoop;
    private final int xBlockCnt, yBlockCnt;

    public Game(Context context) {

        super(context);

        SurfaceHolder surfaceHolder =getHolder();
        surfaceHolder.addCallback(this);
        gameLoop = new GameLoop(this, surfaceHolder);
        gameInfo = new GameInfo(context);

        xBlockCnt = gameInfo.xBlockCnt; yBlockCnt = gameInfo.yBlockCnt;

        final List<BlockImage> blockImages = new MakeBlockImage().make(context, gameInfo);
        final ExplodeImage explodeImage = new ExplodeImage(gameInfo, context);

        ani = new Ani(gameInfo, blockImages, explodeImage, context);
        nextPlate = new NextPlate(gameInfo, context);
        checkNearItem = new CheckNearItem(gameInfo, ani);
        manageHighScore = new ManageHighScore(gameInfo, context);
        manageHighScore.get();
        checkGameOver = new CheckGameOver(gameInfo, nextPlate,ani);
        touchEvent = new TouchEvent(gameInfo);
        greatPlate = new GreatPlate(gameInfo, context);

        basePlate = new BasePlate(gameInfo, context);
        gameOverPlate = new GameOverPlate(gameInfo, context);
        scorePlate = new ScorePlate(gameInfo, context);

        newGameStart();
    }

    void newGameStart() {

        gameInfo.resetValues();
        clearCells();   // clear all cells
        nextPlate.generateNextBlock();

    }

    void clearCells() {
        for (int y = 0; y < yBlockCnt; y++) {
            for (int x = 0; x < xBlockCnt; x++) {
                ani.cells[x][y] = new Cell(0, GameInfo.STATE.PAUSED); // 0 means null
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

        if (gameInfo.startNewGame) {
            gameInfo.newGamePressed = false;
            gameInfo.startNewGame = false;
//            checkGameOver.updateHighScore();
            newGameStart();
        }

        for (int y = yBlockCnt - 1; y >= 0; y--) {
            for (int x = 0; x < xBlockCnt; x++) {
                if (ani.cells[x][y].state == GameInfo.STATE.EXPLODE) {
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
                        if (ani.poolAnis.size() == 0 && gameInfo.greatIdx > 0) {
                            showGreat(x, y);
                        }

                        break;

                    case MERGED:
                        ani.cells[x][y].state = GameInfo.STATE.STOP;
                        break;

                    case EXPLODED:
                        checkIfPullNextUp(x, y);
                        break;

                    default:
                        Log.w("ani default", "state=" + ani.cells[x][y].state
                                + " idx=" + ani.cells[x][y].index + " (" + x + " x " + y + ")");
                        break;
                }
            }
        }

        gameInfo.isGameOver = checkGameOver.isOver();
        if (gameInfo.isGameOver) {
            manageHighScore.put();
        } else if (gameInfo.blockClicked) {
            start2Move();
        } else if (gameInfo.swingPressed) {
            gameInfo.swingPressed = false;
            gameInfo.resetSwing();
        }
        if (gameInfo.swing)
            gameInfo.updateSwing();

    }

    private void showGreat(int x, int y) {
        if (gameInfo.greatIdx > 2) {
            greatPlate.addGreat(x, y, gameInfo.greatIdx - 2,
                    gameInfo.greatCount + gameInfo.greatIdx);
            if (gameInfo.greatIdx > 5) {
                gameInfo.gameDifficulty++;
                gameInfo.swingDelay = 800 / (gameInfo.gameDifficulty+2);
            }
        }
        gameInfo.greatIdx = 0;
        gameInfo.greatStacked = 0;
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
                ani.cells[x][y].state = GameInfo.STATE.MOVING;
                ani.addMove(x, y, x, yUp);
//                Log.w("Add", y +" > "+yUp);
                return;
            }
        }
        ani.cells[x][y].state = GameInfo.STATE.STOP;
    }

    private void checkIfPullNextUp(int x, int y) {
        // this cell is exploded, so check whether below item can be moved up
        ani.cells[x][y] = new Cell(0, GameInfo.STATE.PAUSED);
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

        gameInfo.blockClicked = false;
        Cell cell = ani.cells[gameInfo.touchIndex][yBlockCnt-1];
        if (cell.index == 0) {  // empty cell, so start to move
            ani.cells[gameInfo.touchIndex][yBlockCnt-1] = new Cell(nextPlate.nextIndex, GameInfo.STATE.CHECK);
            nextPlate.generateNextBlock();
        } else if (cell.index == nextPlate.nextIndex) {    // bottom but same index
            ani.cells[gameInfo.touchIndex][yBlockCnt-1].index = cell.index + 1;
            ani.cells[gameInfo.touchIndex][yBlockCnt-1].state = GameInfo.STATE.STOP;
        }
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        basePlate.draw(canvas);
        nextPlate.draw(canvas, ani.blockImages.get(nextPlate.nextIndex).bitmap,
                ani.blockImages.get(nextPlate.nextNextIndex).halfMap);
        ani.draw(canvas);
        scorePlate.draw(canvas);
        greatPlate.draw(canvas);
        if (gameInfo.isGameOver)
            gameOverPlate.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        performClick();
        touchEvent.check(event);
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