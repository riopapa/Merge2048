package com.urrecliner.merge2048;

import android.content.Context;
import android.util.Log;

import com.urrecliner.merge2048.GamePlate.Ani;

public class CheckNearItem {
    Ani ani;
    Context context;
    GameInfo gameInfo;
    final int xBlockCnt, yBlockCnt;
    public CheckNearItem(GameInfo gameInfo, Context context, Ani ani) {
        this.gameInfo = gameInfo;
        this.xBlockCnt = gameInfo.xBlockCnt;
        this.yBlockCnt = gameInfo.yBlockCnt;
        this.ani = ani;
        this.context = context;
    }

    public void check(int x, int y) {

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

    private void mergeToHere(int x, int y, int index) {
        ani.cells[x][y].state = Ani.STATE.MERGE;
        ani.addMerge(x,y, index);
    }

    private void explodeThis(int x, int y, int xTo, int yTo) {
        ani.cells[x][y].state = Ani.STATE.EXPLODE;
        ani.cells[x][y].index = 0;
        ani.addExplode(x,y, xTo, yTo);
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

}