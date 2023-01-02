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
import com.urrecliner.merge2048.GamePlate.MessagePlate;
import com.urrecliner.merge2048.GamePlate.NextPlate;
import com.urrecliner.merge2048.GamePlate.OverPlate;
import com.urrecliner.merge2048.GamePlate.ScorePlate;

import java.util.List;

class Game extends SurfaceView implements SurfaceHolder.Callback {
    public final GInfo gInfo;
    private final ScorePlate scorePlate;
    private final GameOverPlate gameOverPlate;
    private final NextPlate nextPlate;
    private final Ani ani;
    private final BasePlate basePlate;
    private final GreatPlate greatPlate;
    private final OverPlate overPlate;
    private final MessagePlate messagePlate;
    private final CheckNearItem checkNearItem;
    private final HighScore HighScore;
    private final CheckGameOver checkGameOver;
    private final TouchEvent touchEvent;
    private GameLoop gameLoop;
    private final int xBlockCnt, yBlockCnt;

    public Game(Context context) {

        super(context);

        SurfaceHolder surfaceHolder =getHolder();
        surfaceHolder.addCallback(this);
        gameLoop = new GameLoop(this, surfaceHolder);
        gInfo = new GInfo(context);

        xBlockCnt = gInfo.xBlockCnt; yBlockCnt = gInfo.yBlockCnt;

        final List<BlockImage> blockImages = new BlockImageMake().make(context, gInfo);
        final ExplodeImage explodeImage = new ExplodeImage(gInfo, context);

        ani = new Ani(gInfo, blockImages, explodeImage, context);
        nextPlate = new NextPlate(gInfo, context);
        checkNearItem = new CheckNearItem(gInfo, ani);
        HighScore = new HighScore(gInfo, context);
        HighScore.get();
        checkGameOver = new CheckGameOver(gInfo, nextPlate,ani);
        touchEvent = new TouchEvent(gInfo);
        greatPlate = new GreatPlate(gInfo, context);
        overPlate = new OverPlate(gInfo, blockImages);
        messagePlate = new MessagePlate(gInfo, context);
        basePlate = new BasePlate(gInfo, context);
        gameOverPlate = new GameOverPlate(gInfo, context);
        scorePlate = new ScorePlate(gInfo, context);

        newGameStart();
    }

    void newGameStart() {

        gInfo.resetValues();
        clearCells();   // clear all cells
        nextPlate.generateNextBlock();
        gInfo.msgHead = "Welcome";
        gInfo.msgLine1 = "게임";
        gInfo.msgLine2 = "시작합니다";
        gInfo.msgTime = System.currentTimeMillis() + 2000;
    }

    void clearCells() {
        for (int y = 0; y < yBlockCnt; y++) {
            for (int x = 0; x < xBlockCnt; x++) {
                ani.cells[x][y] = new Cell(0, GInfo.STATE.PAUSED); // 0 means null
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

        if (gInfo.startNewGame) {
            gInfo.newGamePressed = false;
            gInfo.startNewGame = false;
//            checkGameOver.updateHighScore();
            newGameStart();
        }

        for (int y = yBlockCnt - 1; y >= 0; y--) {
            for (int x = 0; x < xBlockCnt; x++) {
                if (ani.cells[x][y].state == GInfo.STATE.EXPLODE) {
                    return; // wait till all exploded
                }
            }
        }

        for (int y = 0; y < yBlockCnt - 2; y++) {
            for (int x = 0; x < xBlockCnt; x++) {
                if (ani.cells[x][y].state == GInfo.STATE.PAUSED &&
                    ani.cells[x][y].index == 0 &&
                    ani.cells[x][y+1].index != 0 &&
                    ani.cells[x][y+1].state == GInfo.STATE.PAUSED) {
//                    Log.w("not Empty",x+"x"+y+" idx="+ani.cells[x][y].index);
//                    dumpAllInfo();
                    return;
//                    checkIfPullNextUp(x, y);
                }
            }
        }

        if (nextPlate.nextIndex == -1 && gInfo.poolAniSize == 0) {
            boolean getNext = true;
            for (int y = 0; y < yBlockCnt; y++) {
                for (int x = 0; x < xBlockCnt; x++) {
                    if (ani.cells[x][y].state != GInfo.STATE.PAUSED) {
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
                        ani.cells[x][y] = new Cell(0, GInfo.STATE.PAUSED);
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
                        if (ani.poolAnis.size() == 0 && gInfo.greatIdx > 0) {
                            showGreat(x, y);
                        }
                        break;

                    case STOP:
                        checkNearItem.check(x, y);
                        if (ani.poolAnis.size() == 0 && gInfo.greatIdx > 0) {
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

        if (!gInfo.isGameOver) {
            gInfo.isGameOver = checkGameOver.isOver();
            if (gInfo.isGameOver) {
                if (checkGameOver.updateHighScore())
                    HighScore.put();
            }
            if (gInfo.blockClicked) {
                start2Move();
            } else if (gInfo.swingPressed) {
                gInfo.swingPressed = false;
                gInfo.resetSwing();
            }
            if (gInfo.swing)
                gInfo.updateSwing();
        }

    }

    private void showGreat(int x, int y) {
        if (gInfo.greatIdx > 2) {
            greatPlate.addGreat(x, y, gInfo.greatIdx - 2,
                    gInfo.greatCount + gInfo.greatIdx);
            if (gInfo.greatIdx > 4) {
                gInfo.msgHead = "!! 축하합니다 !!";
                gInfo.msgLine1 = "이제부터 블럭종류가";
                gInfo.msgLine2 = "더 다양해집니다!";
                gInfo.msgTime = System.currentTimeMillis() + 2000;
                gInfo.gameDifficulty++;
                gInfo.swingDelay = 800 / (gInfo.gameDifficulty+2);
            }
        }
        gInfo.greatIdx = 0;
        gInfo.greatStacked = 0;
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
                ani.cells[x][y].state = GInfo.STATE.MOVING;
                ani.addMove(x, y, x, yUp);
//                Log.w("Add", y +" > "+yUp);
                return;
            }
        }
        ani.cells[x][y].state = GInfo.STATE.STOP;
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

        gInfo.blockClicked = false;
        if (nextPlate.nextIndex == -1)
            return;
        if (gInfo.dumpCount > 2)
            dumpAllInfo();
        Cell cell = ani.cells[gInfo.touchIndex][yBlockCnt-1];
        if (cell.index == 0) {  // empty cell, so start to move
            ani.cells[gInfo.touchIndex][yBlockCnt-1] = new Cell(nextPlate.nextIndex, GInfo.STATE.CHECK);
            nextPlate.nextIndex = -1;   // wait while all moved;
        } else if (cell.index == nextPlate.nextIndex) {    // bottom but same index
            ani.cells[gInfo.touchIndex][yBlockCnt-1].index = cell.index + 1;
            ani.cells[gInfo.touchIndex][yBlockCnt-1].state = GInfo.STATE.STOP;
        }
    }

    void dumpAllInfo() {
        StringBuilder sb = new StringBuilder("      0     1     2     3     4 ");
        for (int y = 0; y < gInfo.yBlockCnt; y++) {
            sb.append("\n ").append(y).append(" ");
            for (int x = 0; x < gInfo.xBlockCnt; x++) {
                int nbr = checkNearItem.powerIndex(ani.cells[x][y].index);
                String sNbr = ""+nbr;
                int space = (7 - sNbr.length())/2;
                String s = ("       ").substring(0,space)+nbr+("       ").substring(0,space);
                if (s.length()>6)
                    s = s.substring(0,6);
                sb.append(s); // .append(ani.cells[x][y].state);
            }
        }
        sb.append("\n touch=").append(gInfo.touchIndex);
        sb.append(" index=").append(nextPlate.nextIndex);
        sb.append(" block=").append(checkNearItem.powerIndex(nextPlate.nextIndex));

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
        messagePlate.draw(canvas);
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