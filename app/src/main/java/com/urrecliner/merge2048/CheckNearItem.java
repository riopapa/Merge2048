package com.urrecliner.merge2048;

import com.urrecliner.merge2048.GameObject.Ani;

public class CheckNearItem {
    Ani ani;
    GInfo gInfo;
    final int xBlockCnt, yBlockCnt;

    public CheckNearItem(GInfo gInfo, Ani ani) {
        this.gInfo = gInfo;
        this.xBlockCnt = gInfo.xBlockCnt;
        this.yBlockCnt = gInfo.yBlockCnt;
        this.ani = ani;
    }

    public void check(int x, int y) {

        int index = ani.cells[x][y].index;
        int indexR = -1, indexL = -1, indexU = -1;

        if (x < xBlockCnt - 1)
            indexR = ani.cells[x +1][y].index;
        if (x > 0)
            indexL = ani.cells[x -1][y].index;
        if (y > 0)
            indexU = ani.cells[x][y -1].index;
        if (index == indexL && index == indexR && index == indexU) {
            index += 3;
            gInfo.score2Add += addNumber(index);
            explodeThis(x -1, y, x, y -1);
            explodeThis(x +1, y, x, y -1);
            explodeThis(x, y, x, y -1);
            mergeToHere(x, y -1, index);
            return;
        }
        if (index == indexL && index == indexR) {
            index += 2;
            gInfo.score2Add += addNumber(index);
            explodeThis(x -1, y, x, y);
            explodeThis(x +1, y, x, y);
            mergeToHere(x, y, index);
            return;
        }
        if (index == indexL && index == indexU) {
            index += 2;
            gInfo.score2Add += addNumber(index);
            explodeThis(x -1, y, x, y -1);
            explodeThis(x, y, x, y -1);
            mergeToHere(x, y -1, index);
            return;
        }
        if (index == indexR && index == indexU) {
            index += 2;
            gInfo.score2Add += addNumber(index);
            explodeThis(x + 1, y, x, y -1);
            explodeThis(x, y, x, y -1);
            mergeToHere(x, y -1, index);
            return;
        }
        if (index == indexL) {
            index++;
            gInfo.score2Add += addNumber(index);
            explodeThis(x -1, y, x, y);
            mergeToHere(x, y, index);
            return;
        }
        if (index == indexR) {
            index++;
            gInfo.score2Add += addNumber(index);
            explodeThis(x +1, y, x, y);
            mergeToHere(x, y, index);
            return;
        }
        if (index == indexU) {
            index++;
            gInfo.score2Add += addNumber(index);
            explodeThis(x, y, x, y -1);
            mergeToHere(x, y -1, index);
            return;
        }
        ani.cells[x][y].state = GInfo.STATE.PAUSED;
    }

    private void mergeToHere(int x, int y, int index) {
        gInfo.greatIdx++;
        ani.cells[x][y].state = GInfo.STATE.MERGE;
        ani.addMerge(x,y, index);
    }

    private void explodeThis(int x, int y, int xTo, int yTo) {
        ani.cells[x][y].state = GInfo.STATE.EXPLODE;
        ani.cells[x][y].index = 0;
        ani.addExplode(x,y, xTo, yTo);
    }

    public int addNumber(int index) {
        int addVal = powerIndex(index);
        if (!gInfo.showNext)
            addVal += addVal;
        if (gInfo.swing)
            addVal += addVal / 2;
        gInfo.greatStacked += addVal;
        return addVal;
    }

    public int powerIndex(int index) {
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