package com.urrecliner.merge2048;

public class CheckNearItem {
    final Animation animation;
    final AnimationAdd animationAdd;
    final GInfo gInfo;
    final int xBlockCnt, yBlockCnt;

    public CheckNearItem(GInfo gInfo, Animation animation, AnimationAdd animationAdd) {
        this.gInfo = gInfo;
        this.animation = animation;
        this.animationAdd = animationAdd;
        xBlockCnt = gInfo.xBlockCnt;
        yBlockCnt = gInfo.yBlockCnt;
    }

    public void check(int x, int y) {

        int index = gInfo.cells[x][y].index;
        int indexR = -1, indexL = -1, indexU = -1;
        GInfo.STATE stateR = null, stateL = null, stateU = null;

        if (x < xBlockCnt - 1) {
            indexR = gInfo.cells[x + 1][y].index;
            stateR = gInfo.cells[x + 1][y].state;
        }
        if (x > 0) {
            indexL = gInfo.cells[x - 1][y].index;
            stateL = gInfo.cells[x - 1][y].state;
        }
        if (y > 0) {
            indexU = gInfo.cells[x][y - 1].index;
            stateU = gInfo.cells[x][y - 1].state;
        }
        if (index == indexL && index == indexR && index == indexU &&
            stateL == GInfo.STATE.PAUSED && stateR == GInfo.STATE.PAUSED &&
            stateU == GInfo.STATE.PAUSED) {
            index += 3;
            gInfo.score2Add += addNumber(index);
            explodeThis(x -1, y, x, y -1);
            explodeThis(x +1, y, x, y -1);
            explodeThis(x, y, x, y -1);
            mergeToHere(x, y -1, index);
            return;
        }
        if (index == indexL && index == indexR &&
            stateL == GInfo.STATE.PAUSED && stateR == GInfo.STATE.PAUSED) {
            index += 2;
            gInfo.score2Add += addNumber(index);
            explodeThis(x -1, y, x, y);
            explodeThis(x +1, y, x, y);
            mergeToHere(x, y, index);
            return;
        }
        if (index == indexL && index == indexU &&
            stateL == GInfo.STATE.PAUSED && stateU == GInfo.STATE.PAUSED) {
            index += 2;
            gInfo.score2Add += addNumber(index);
            explodeThis(x -1, y, x, y -1);
            explodeThis(x, y, x, y -1);
            mergeToHere(x, y -1, index);
            return;
        }
        if (index == indexR && index == indexU &&
            stateR == GInfo.STATE.PAUSED && stateU == GInfo.STATE.PAUSED) {
            index += 2;
            gInfo.score2Add += addNumber(index);
            explodeThis(x + 1, y, x, y -1);
            explodeThis(x, y, x, y -1);
            mergeToHere(x, y -1, index);
            return;
        }
        if (index == indexL && stateL == GInfo.STATE.PAUSED) {
            index++;
            gInfo.score2Add += addNumber(index);
            explodeThis(x -1, y, x, y);
            mergeToHere(x, y, index);
            return;
        }
        if (index == indexR && stateR == GInfo.STATE.PAUSED) {
            index++;
            gInfo.score2Add += addNumber(index);
            explodeThis(x +1, y, x, y);
            mergeToHere(x, y, index);
            return;
        }
        if (index == indexU && stateU == GInfo.STATE.PAUSED) {
            index++;
            gInfo.score2Add += addNumber(index);
            explodeThis(x, y, x, y -1);
            mergeToHere(x, y -1, index);
            return;
        }
        gInfo.cells[x][y].state = GInfo.STATE.PAUSED;
    }

    private void mergeToHere(int x, int y, int index) {
        gInfo.bonusCount++;
        gInfo.cells[x][y].state = GInfo.STATE.MERGE;
        animationAdd.addMerge(x,y, index);
    }

    private void explodeThis(int x, int y, int xTo, int yTo) {
        gInfo.cells[x][y].state = GInfo.STATE.EXPLODE;
        gInfo.cells[x][y].index = 0;
        animationAdd.addExplode(x,y, xTo, yTo);
    }

    public int addNumber(int index) {
        int addVal = powerIndex(index);
        if (!gInfo.showNext)
            addVal += addVal;
        if (gInfo.swing)
            addVal += addVal / 2;
        gInfo.bonusStacked += addVal;
        return addVal;
    }

    final int[] nbrValues = {0, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768, 65536, 131072};
    public int powerIndex(int index) {
        return nbrValues[index];
    }
}