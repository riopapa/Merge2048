package com.urrecliner.merge2048;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.urrecliner.merge2048.GameObject.Saved;
import com.urrecliner.merge2048.GamePlate.YesNoPlate;
import com.urrecliner.merge2048.Sub.DumpCells;
import com.urrecliner.merge2048.GameImage.BlockImage;
import com.urrecliner.merge2048.Sub.MakeBlockImage;
import com.urrecliner.merge2048.GamePlate.BasePlate;
import com.urrecliner.merge2048.GameObject.Cell;
import com.urrecliner.merge2048.GamePlate.GameOverPlate;
import com.urrecliner.merge2048.GamePlate.BonusPlate;
import com.urrecliner.merge2048.GamePlate.MessagePlate;
import com.urrecliner.merge2048.GamePlate.NextPlate;
import com.urrecliner.merge2048.GamePlate.RotatePlate;
import com.urrecliner.merge2048.GamePlate.ScorePlate;
import com.urrecliner.merge2048.Sub.TouchEvent;
import com.urrecliner.merge2048.Sub.UserName;

import java.lang.reflect.Type;
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
    private final RotatePlate rotatePlate;
    private final MessagePlate messagePlate;
    private final YesNoPlate yesNoPlate;
    private final CheckNearItem checkNearItem;
    private final HighScore highScore;
    private final CheckGameOver checkGameOver;
    private final CheckGoUp checkGoUp;
    private final PullBelow pullBelow;
    private final TouchEvent touchEvent;
    private GameLoop gameLoop;
    private final int xBlockCnt, yBlockCnt;
    final int SMOOTH = 4;
    Context context;
    int bonusX, bonusY;

    public Game(Context context) {

        super(context);
        this.context = context;
        SurfaceHolder surfaceHolder =getHolder();
        surfaceHolder.addCallback(this);
        gameLoop = new GameLoop(this, surfaceHolder);
        gInfo = new GInfo(context);

        xBlockCnt = gInfo.X_BLOCK_CNT; yBlockCnt = gInfo.Y_BLOCK_CNT;

        List<BlockImage> blockImages = new MakeBlockImage().make(context, gInfo);

        animation = new Animation(gInfo, blockImages, SMOOTH);
        animationAdd = new AnimationAdd(gInfo, SMOOTH);
        nextPlate = new NextPlate(gInfo, context, blockImages);
        checkNearItem = new CheckNearItem(gInfo, animation, animationAdd);
        highScore = new HighScore(gInfo, context);
        checkGameOver = new CheckGameOver(gInfo, animationAdd);
        checkGoUp = new CheckGoUp(gInfo, animationAdd);
        pullBelow = new PullBelow(gInfo, animationAdd);

        bonusPlate = new BonusPlate(gInfo, context);
        rotatePlate = new RotatePlate(gInfo, blockImages);
        messagePlate = new MessagePlate(gInfo, context);
        basePlate = new BasePlate(gInfo, context);
        gameOverPlate = new GameOverPlate(gInfo, context);
        scorePlate = new ScorePlate(gInfo, context);
        yesNoPlate = new YesNoPlate(gInfo, context);
        touchEvent = new TouchEvent(gInfo);     // touchEvent should be after scorePlate

        new NewGame(gInfo, messagePlate, nextPlate, context);
        gInfo.highLowScore = gInfo.highMembers.get(gInfo.highMembers.size()-1).score;
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
            if (gInfo.isRanked) {
                gInfo.isRanked = false;
                new UserName(context, gInfo);
            }
            new NewGame(gInfo, messagePlate, nextPlate, context);
            gInfo.highLowScore = gInfo.highMembers.get(gInfo.highMembers.size()-1).score;
            return;
        }

        if (gInfo.dumpClicked) {
            gInfo.dumpClicked = false;
            if (gInfo.dumpCount > 3) {
                new DumpCells(gInfo, gInfo.cells, nextPlate, "Dump Old");
            }
        }
        if (gInfo.aniStacks.size() > 0)
            return;

        /*
        *   check state info and then ...
         */
        for (int y = 0; y < yBlockCnt; y++) {
            for (int x = 0; x < xBlockCnt; x++) {
                switch (gInfo.cells[x][y].state) {
                    case MOVING:
                    case MERGE:
                    case DESTROY:
                    case EXPLODE:
                    case PULL:
                        break;

                    case EXPLODED:
                        gInfo.cells[x][y] = new Cell(0, GInfo.STATE.PAUSED);
                        pullBelow.check(x, y, false);
                        break;

                    case DESTROYED:
                        gInfo.cells[x][y] = new Cell(0, GInfo.STATE.PAUSED);
                        pullBelow.check(x, y, true);
                        break;

                    case GO_UP:
                        checkGoUp.check(x, y);
                        break;

                    case MERGED:
                        if (gInfo.cells[x][y].index > gInfo.CONTINUE_INDEX) {
                            gInfo.is2048 = true;
                            rotatePlate.addRotate(x, y, gInfo.cells[x][y].index, 12, 40);
                        }
                        checkNearItem.check(x, y);
                        if (gInfo.bonusCount > 0) {
                            bonusX = x; bonusY = y;
                        }
                        break;

                    case STOP:
                        checkNearItem.check(x, y);
                        if (gInfo.bonusCount > 0) {
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

        for (int y = 1; y < yBlockCnt; y++) {   // added because of check completeness
            for (int x = 0; x < xBlockCnt; x++) {
                if (gInfo.cells[x][y].index != 0 && gInfo.cells[x][y].state == GInfo.STATE.PAUSED) {
                    checkNearItem.checkUp(x, y);
                }
            }
        }
        if (gInfo.aniStacks.size() > 0)
            return;

        for (int y = 1; y < yBlockCnt; y++) {   // added because of check completeness
            for (int x = 0; x < xBlockCnt; x++) {
                if (gInfo.cells[x][y].index != 0 && gInfo.cells[x][y].state == GInfo.STATE.PAUSED) {
                    checkNearItem.checkRight(x, y);
                }
            }
        }
        if (gInfo.aniStacks.size() > 0)
            return;

        if (gInfo.isGameOver) {
            if (gInfo.continueYes) {
                checkGameOver.destroy();
                return;
            }
            if (!gInfo.is2048) {
                if (gInfo.isRanked) {
                    gInfo.isRanked = false;
                    new UserName(context, gInfo);
                }
            }
        } else {
            if (gInfo.bonusCount > 0 && new CheckAllState().paused(gInfo))
                new ShowBonus(gInfo, bonusX, bonusY, bonusPlate, messagePlate);

            if (gInfo.showNextPressed) {
                gInfo.showNextPressed = false;
                gInfo.showCount--;
                if (gInfo.showCount > 0) {
                    gInfo.showNext = !gInfo.showNext;
                    messagePlate.set("다음 블럭 "+((gInfo.showNext) ? "보이기":" 안 보이기"),
                            "("+gInfo.showCount+") 회 전환가능",
                            (gInfo.showNext) ? "" : "점수는 두배로", System.currentTimeMillis(), 2300);
                }
            } else if (gInfo.swingPressed) {
                gInfo.swingPressed = false;
                gInfo.swing = !gInfo.swing;
                messagePlate.set("블럭 움직이기", (gInfo.swing) ? "움직이니까" : "고정됩니다",
                        (gInfo.swing) ? "점수는 두배로":"",System.currentTimeMillis(), 2300);
                nextPlate.resetSwing();

            } else if (gInfo.swapPressed) {
                gInfo.swapPressed = false;
                if (gInfo.swapCount > 0) {
                    gInfo.swapCount--;
                    nextPlate.swapNextBlock();
                    if (gInfo.swapCount > 0)
                        messagePlate.set("블럭 바꾸기", "앞으로",
                        gInfo.swapCount+" 번 더 가능",System.currentTimeMillis(), 1500);
                }

            } else if (gInfo.shoutClicked) {
                new Start2Move(gInfo, nextPlate, checkNearItem);

            } else if (gInfo.undoPressed && gInfo.sv.size() > 2) {
                gInfo.undoPressed = false;

                if (gInfo.undoCount > 0) {
                    gInfo.undoCount--;
                    if (gInfo.undoCount > 0)
                        messagePlate.set("블럭 취소", "취소는 앞으로 ",
                                gInfo.undoCount+" 번 더 가능해요",System.currentTimeMillis(), 1000);
                    Saved saved = gInfo.sv.get(gInfo.sv.size()-1);
                    Gson gson = new Gson();
                    String json = saved.cell;
                    Type type = new TypeToken<Cell[][]>() {}.getType();
                    gInfo.cells = gson.fromJson(json, type);
                    nextPlate.nextIndex = saved.next;
                    nextPlate.nextNextIndex = saved.nextNext;
                    gInfo.scoreNow = saved.score;
                    gInfo.sv.remove(gInfo.sv.size()-1);
                    highScore.undoScore();
                }
            }

            if (nextPlate.nextIndex == -1)
                nextPlate.setNextBlock();
            if (gInfo.aniStacks.size() == 0)
                gInfo.isGameOver = checkGameOver.isOver(nextPlate.nextIndex);
        }

        if (gInfo.quitGame) {
            exitApp();      // no update high score

        } else if (gInfo.highTouchPressed) {
            gInfo.highTouchPressed = false;
            gInfo.highTouchCount++;
            if (gInfo.highTouchCount > 5) {
                highScore.resetHigh();
                highScore.put();
                gInfo.highTouchCount = 0;
            }
        }
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        basePlate.draw(canvas);
        nextPlate.draw(canvas);
        animation.draw(canvas);
        scorePlate.draw(canvas);
        bonusPlate.draw(canvas);
        rotatePlate.draw(canvas);
        gameOverPlate.draw(canvas);
        messagePlate.draw(canvas);
        yesNoPlate.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        performClick();
        touchEvent.check(event);
        return super.onTouchEvent(event);
    }
    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {}

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        Log.w("game", "surfaceDestroyed");
        super.destroyDrawingCache();
        if (gInfo.quitGame) {
            gameLoop.stopLoop();
            ((Activity) context).finish();
        }
    }

    public void pause() {
        gameLoop.stopLoop();
        if (gInfo.quitGame) {
            int id= android.os.Process.myPid();
            android.os.Process.killProcess(id);
        }
    }

    void exitApp() {
        if (gInfo.quitGame) {
            Activity activity = (Activity)context;
            activity.finish();
        }
    }
}