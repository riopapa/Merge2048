package com.urrecliner.merge2048;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.urrecliner.merge2048.GameImage.BlockImage;
import com.urrecliner.merge2048.GameImage.ExplodeImage;
import com.urrecliner.merge2048.GameImage.BlockImageMake;
import com.urrecliner.merge2048.GameObject.Ani;
import com.urrecliner.merge2048.GamePlate.BasePlate;
import com.urrecliner.merge2048.GameObject.Cell;
import com.urrecliner.merge2048.GamePlate.GameOverPlate;
import com.urrecliner.merge2048.GamePlate.GreatPlate;
import com.urrecliner.merge2048.GamePlate.NextPlate;
import com.urrecliner.merge2048.GamePlate.OverPlate;
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
    private final OverPlate overPlate;
    private final CheckNearItem checkNearItem;
    private final GetPutHighScore getPutHighScore;
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

        final List<BlockImage> blockImages = new BlockImageMake().make(context, gameInfo);
        final ExplodeImage explodeImage = new ExplodeImage(gameInfo, context);

        ani = new Ani(gameInfo, blockImages, explodeImage, context);
        nextPlate = new NextPlate(gameInfo, context);
        checkNearItem = new CheckNearItem(gameInfo, ani);
        getPutHighScore = new GetPutHighScore(gameInfo, context);
        getPutHighScore.get();
        checkGameOver = new CheckGameOver(gameInfo, nextPlate,ani);
        touchEvent = new TouchEvent(gameInfo);
        greatPlate = new GreatPlate(gameInfo, context);
        overPlate = new OverPlate(gameInfo, blockImages);
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
        if (nextPlate.nextIndex == -1 && gameInfo.poolAniSize == 0) {
            boolean getNext = true;
            for (int y = 0; y < yBlockCnt; y++) {
                for (int x = 0; x < xBlockCnt; x++) {
                    if (ani.cells[x][y].state != GameInfo.STATE.PAUSED) {
                        getNext = false;
                        break;
                    }
                }
            }
            if (getNext)
                nextPlate.generateNextBlock();
        }

        for (int y = 0; y < yBlockCnt; y++) {
            for (int x = 0; x < xBlockCnt; x++) {
                switch (ani.cells[x][y].state) {
                    case PAUSED:
                    case MOVING:
                    case MERGE:
                    case EXPLODE:
                        break;

                    case EXPLODED:
                        ani.cells[x][y] = new Cell(0, GameInfo.STATE.PAUSED);
                        checkIfPullNextUp(x, y);
                        break;

                    case CHECK:
                        checkIfGoingUpPossible(x, y);
                        break;

                    case MERGED:
                        if (ani.cells[x][y].index >= 10) {
                            overPlate.addOver(x, y, ani.cells[x][y].index, 12, 40);
                        }
                        checkNearItem.check(x, y);
                        if (ani.poolAnis.size() == 0 && gameInfo.greatIdx > 0) {
                            showGreat(x, y);
                        }
                        break;

                    case STOP:
                        checkNearItem.check(x, y);
                        if (ani.poolAnis.size() == 0 && gameInfo.greatIdx > 0) {
                            showGreat(x, y);
                        }
                        break;

                    default:
                        Log.w("ani not handled", "state=" + ani.cells[x][y].state
                                + " idx=" + ani.cells[x][y].index + " (" + x + " x " + y + ")");
                        break;
                }
            }
        }

        gameInfo.isGameOver = checkGameOver.isOver();
        if (gameInfo.isGameOver) {
            getPutHighScore.put();
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
        if (nextPlate.nextIndex == -1)
            return;
        if (gameInfo.dumpCellClicked)
            dumpAllInfo();
        Cell cell = ani.cells[gameInfo.touchIndex][yBlockCnt-1];
        if (cell.index == 0) {  // empty cell, so start to move
            ani.cells[gameInfo.touchIndex][yBlockCnt-1] = new Cell(nextPlate.nextIndex, GameInfo.STATE.CHECK);
            nextPlate.nextIndex = -1;   // wait while all moved;
        } else if (cell.index == nextPlate.nextIndex) {    // bottom but same index
            ani.cells[gameInfo.touchIndex][yBlockCnt-1].index = cell.index + 1;
            ani.cells[gameInfo.touchIndex][yBlockCnt-1].state = GameInfo.STATE.STOP;
        }
    }

    void dumpAllInfo() {
        StringBuilder sb = new StringBuilder("      0     1     2     3     4 ");
        for (int y = 0; y < gameInfo.yBlockCnt; y++) {
            sb.append("\n "+ y+" ");
            for (int x = 0; x < gameInfo.xBlockCnt; x++) {
                int nbr = checkNearItem.calcNumber(ani.cells[x][y].index);
                String sNbr = ""+nbr;
                int space = (7 - sNbr.length())/2;
                String s = ("       ").substring(0,space)+nbr+("       ").substring(0,space);
                if (s.length()>6)
                    s = s.substring(0,6);
                sb.append(s); // .append(ani.cells[x][y].state);
            }
        }
        sb.append("\n touch="+gameInfo.touchIndex);
        sb.append(" block="+checkNearItem.calcNumber(nextPlate.nextIndex));

        Log.w("dump", sb.toString());
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        basePlate.draw(canvas);
        Bitmap nextItem = (nextPlate.nextIndex == -1) ? null:ani.blockImages.get(nextPlate.nextIndex).bitmap;
        nextPlate.draw(canvas, nextItem, ani.blockImages.get(nextPlate.nextNextIndex).halfMap);
        ani.draw(canvas);
        scorePlate.draw(canvas);
        greatPlate.draw(canvas);
        overPlate.draw(canvas);
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


    void exitApp() {
        gameLoop.stopLoop();
        System.exit(0);
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}