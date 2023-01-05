package com.urrecliner.merge2048;

import android.app.Activity;
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
import com.urrecliner.merge2048.GamePlate.BasePlate;
import com.urrecliner.merge2048.GameObject.Cell;
import com.urrecliner.merge2048.GamePlate.GameOverPlate;
import com.urrecliner.merge2048.GamePlate.BonusPlate;
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
    private final Animation animation;
    private final AnimationAdd animationAdd;
    private final BasePlate basePlate;
    private final BonusPlate bonusPlate;
    private final OverPlate overPlate;
    private final MessagePlate messagePlate;
    private final CheckNearItem checkNearItem;
    private final HighScore highScore;
    private final CheckGameOver checkGameOver;
    private final TouchEvent touchEvent;
    private GameLoop gameLoop;
    private final int xBlockCnt, yBlockCnt;
    final int SMOOTH = 4;
    Context context;
    public boolean needQuit = false;
    int bonusX, bonusY;
    
    public Game(Context context) {

        super(context);
        this.context = context;
        SurfaceHolder surfaceHolder =getHolder();
        surfaceHolder.addCallback(this);
        gameLoop = new GameLoop(this, surfaceHolder);
        gInfo = new GInfo(context);

        xBlockCnt = gInfo.xBlockCnt; yBlockCnt = gInfo.yBlockCnt;

        final List<BlockImage> blockImages = new BlockImageMake().make(context, gInfo);
        final ExplodeImage explodeImage = new ExplodeImage(gInfo, context);

        animation = new Animation(gInfo, blockImages, explodeImage, context, SMOOTH);
        animationAdd = new AnimationAdd(gInfo, SMOOTH);
        nextPlate = new NextPlate(gInfo, context);
        checkNearItem = new CheckNearItem(gInfo, animation, animationAdd);
        highScore = new HighScore(gInfo, context);
        checkGameOver = new CheckGameOver(gInfo, nextPlate);
        touchEvent = new TouchEvent(gInfo);
        bonusPlate = new BonusPlate(gInfo, context);
        overPlate = new OverPlate(gInfo, blockImages);
        messagePlate = new MessagePlate(gInfo, context);
        basePlate = new BasePlate(gInfo, context);
        gameOverPlate = new GameOverPlate(gInfo, context);
        scorePlate = new ScorePlate(gInfo, context);
        newGameStart();
    }

    void newGameStart() {

        messagePlate.set(true,"Welcome", "게임을", "시작합니다",
                System.currentTimeMillis(), 1000);

        gInfo.resetValues();
        highScore.get();
        gInfo.highLowScore = gInfo.highMembers.get(gInfo.highMembers.size()-1).score;

        clearCells();
        nextPlate.generateNextBlock();
    }

    void clearCells() {
        for (int y = 0; y < yBlockCnt; y++) {
            for (int x = 0; x < xBlockCnt; x++) {
                gInfo.cells[x][y] = new Cell(0, GInfo.STATE.PAUSED); // 0 means null
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

        if (gInfo.startNewGameYes) {
            gInfo.startNewGameYes = false;
            newGameStart();
        }
//        if (gInfo.quitGame) {
//            exitApp();
//        }
        if (gInfo.dumpClicked && gInfo.dumpCount > 5) {
            gInfo.dumpClicked = false;
            new DumpCells(gInfo, animation, checkNearItem, nextPlate, "Dump Update");
        }
        if (gInfo.aniStacks.size() > 0)
            return;

        if (nextPlate.nextIndex == -1 &&  isAllPaused()) {
            nextPlate.generateNextBlock();
            if (gInfo.bonusIdx > 0) {
                showBonus(bonusX, bonusY);
            }
        }

        /*
        *   check state info and then ...
         */
        for (int y = 0; y < yBlockCnt; y++) {
            for (int x = 0; x < xBlockCnt; x++) {
                switch (gInfo.cells[x][y].state) {
                    case MOVING:
                    case MERGE:
                    case EXPLODE:
                        break;

                    case EXPLODED:
                        gInfo.cells[x][y] = new Cell(0, GInfo.STATE.PAUSED);
                        pullBelowBlock(x, y);
                        break;

                    case GO_UP:
                        goingUp(x, y);
                        break;

                    case MERGED:
                        if (gInfo.cells[x][y].index >= 10) {
                            overPlate.addOver(x, y, gInfo.cells[x][y].index, 12, 40);
                        }
                        checkNearItem.check(x, y);
                        if (gInfo.bonusIdx > 0) {
                            bonusX = x; bonusY = y;
                        }
                        break;

                    case STOP:
                        checkNearItem.check(x, y);
                        if (gInfo.bonusIdx > 0) {
                            bonusX = x; bonusY = y;
                        }
                        break;

                    case PAUSED:
                        if (y < yBlockCnt-1 && gInfo.cells[x][y].index == 0 &&
                                gInfo.cells[x][y+1].index != 0 &&
                                gInfo.cells[x][y+1].state == GInfo.STATE.PAUSED) {  // should go down
                            gInfo.cells[x][y].state = GInfo.STATE.MOVING;
                            animationAdd.addMove(x, y+1, x, y, gInfo.cells[x][y+1].index);
//                            Log.e("game","Empty cell "+x+"x"+y);
                        }
                        break;

                    default:
                        Log.w("animation not handled", "state=" + gInfo.cells[x][y].state
                                + " idx=" + gInfo.cells[x][y].index + " (" + x + " x " + y + ")");
                        break;
                }
            }
        }

        if (!gInfo.isGameOver) {
            gInfo.isGameOver = checkGameOver.isOver();
            if (gInfo.isGameOver)
                highScore.put();

            if (gInfo.touchClicked) {
                start2Move();

            } else if (gInfo.showNextPressed) {
                gInfo.showNextPressed = false;
                gInfo.showNext = !gInfo.showNext;
                messagePlate.set(gInfo.showNext, "다음 블럭", (gInfo.showNext) ? "보입니다" : "안 보입니다",
                        (gInfo.showNext) ? "":"안 보이면 점수2배",System.currentTimeMillis(), 1500);

            } else if (gInfo.swingPressed) {
                gInfo.swingPressed = false;
                gInfo.swing = !gInfo.swing;
                messagePlate.set(gInfo.swing, "움직이기", (gInfo.swing) ? "움직입니다" : "고정됩니다",
                        (gInfo.swing) ? "움직이면 점수2배":"",System.currentTimeMillis(), 1500);
                gInfo.resetSwing();
            }
            if (gInfo.swing)
                gInfo.updateSwing();
        }
    }

    private boolean isAllPaused() {
        boolean getNext = true;
        for (int y = 0; y < yBlockCnt; y++) {
            for (int x = 0; x < xBlockCnt; x++) {
                if (gInfo.cells[x][y].state != GInfo.STATE.PAUSED) {
                    getNext = false;
                    break;
                }
            }
        }
        return getNext;
    }

    private void showBonus(int x, int y) {
        if (gInfo.aniStacks.size() > 0)
            return;
        if (gInfo.bonusIdx > 2) {
            bonusPlate.addBonus(x, y, gInfo.bonusIdx - 2,
                    gInfo.bonusLoopCount + gInfo.bonusIdx + gInfo.bonusIdx);
            if (gInfo.bonusIdx > 5) {
                gInfo.gameDifficulty++;
                messagePlate.set(true, "!잘 했어요!", "블럭 종류가",
                    "더("+gInfo.gameDifficulty+") 많아져요!",
                    System.currentTimeMillis() + 1500, 2000);
                gInfo.swingDelay = 800 / (gInfo.gameDifficulty+2);
            }
        }
        gInfo.bonusIdx = 0;
        gInfo.bonusStacked = 0;
    }

    /*
    * block just moved here or initially loaded, if can not go up, then STOP
     */
    private void goingUp(int x, int y) {
        if (y > 0) {
            int yUp = 0;
            for (int yy = y - 1; yy >= 0; yy--) {
                if (gInfo.cells[x][yy].index != 0) {
                    yUp = yy + 1;
                    break;
                }
            }
            if (yUp != y) {
                gInfo.cells[x][y].state = GInfo.STATE.MOVING;
                animationAdd.addMove(x, y, x, yUp, gInfo.cells[x][y].index);
//                Log.w("Add", y +" > "+yUp);
                return;
            }
        }
        gInfo.cells[x][y].state = GInfo.STATE.STOP;
    }

    /*
    *   block exploded, so if any block below, pull them up
     */
    private void pullBelowBlock(int x, int y) {

        if (y == yBlockCnt -1 || gInfo.cells[x][y+1].index == 0)
            return;
        for (int yy = y+1; yy < yBlockCnt; yy++) {
            if (gInfo.cells[x][yy].index > 0) {
                gInfo.cells[x][yy].state = GInfo.STATE.MOVING;
                animationAdd.addMove(x, yy, x, yy-1, gInfo.cells[x][yy].index);
            } else
                break;
        }
    }

    private void start2Move() {

        gInfo.touchClicked = false;
        if (nextPlate.nextIndex == -1)
            return;
        if (gInfo.dumpCount > 5)
            new DumpCells(gInfo, animation, checkNearItem, nextPlate, "Start2Move");
        Cell cell = gInfo.cells[gInfo.touchIndex][yBlockCnt-1];
        if (cell.index == 0) {  // empty cell, so start to move
            gInfo.cells[gInfo.touchIndex][yBlockCnt-1] = new Cell(nextPlate.nextIndex, GInfo.STATE.GO_UP);
            nextPlate.nextIndex = -1;   // wait while all moved;
        } else if (cell.index == nextPlate.nextIndex) {    // bottom but same index
            gInfo.cells[gInfo.touchIndex][yBlockCnt-1].index = cell.index + 1;
            gInfo.cells[gInfo.touchIndex][yBlockCnt-1].state = GInfo.STATE.STOP;
        }
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        basePlate.draw(canvas);
        Bitmap nextItem = (nextPlate.nextIndex == -1) ? null: animation.blockImages.get(nextPlate.nextIndex).bitmap;
        nextPlate.draw(canvas, nextItem, animation.blockImages.get(nextPlate.nextNextIndex).halfMap);
        animation.draw(canvas);
        scorePlate.draw(canvas);
        bonusPlate.draw(canvas);
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
        super.destroyDrawingCache();
    }

    public void pause() {
        gameLoop.stopLoop();
    }

    void exitApp() {
        Log.w("game","stopLoop");
        gameLoop.stopLoop();
        if (gInfo.quitGame) {
            Log.w("game","exitApp()");
            Activity activity = (Activity)context;
            activity.finish();
            int id= android.os.Process.myPid();
            android.os.Process.killProcess(id);
        }
    }
}