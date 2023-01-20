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
        xBlockCnt = gInfo.X_BLOCK_CNT;
        yBlockCnt = gInfo.Y_BLOCK_CNT;
    }

    public void check(int x, int y) {

        int index = gInfo.cells[x][y].index;
        int indexR = -2, indexL = -2, indexU = -2;

        boolean stateR = false, stateL = false, stateU = false;

        if (x < xBlockCnt - 1) {
            indexR = gInfo.cells[x + 1][y].index;
            stateR = gInfo.cells[x + 1][y].state == GInfo.STATE.PAUSED;
        }
        if (x > 0) {
            indexL = gInfo.cells[x - 1][y].index;
            stateL = gInfo.cells[x - 1][y].state == GInfo.STATE.PAUSED;
        }
        if (y > 0) {
            indexU = gInfo.cells[x][y - 1].index;
            stateU = gInfo.cells[x][y - 1].state == GInfo.STATE.PAUSED;
        }
        if (index == indexL && index == indexR && index == indexU &&
            stateL && stateR && stateU) {
            index += 3;
            gInfo.score2Add += addNumber(index);
            explodeThis(x -1, y, x, y -1, index);
            explodeThis(x +1, y, x, y -1, index);
            explodeThis(x, y, x, y -1, index);
            mergeToHere(x, y -1, index);
            return;
        }
        if (index == indexL && index == indexR && stateL && stateR) {
            index += 2;
            gInfo.score2Add += addNumber(index);
            explodeThis(x -1, y, x, y, index);
            explodeThis(x +1, y, x, y, index);
            mergeToHere(x, y, index);
            return;
        }
        if (index == indexL && index == indexU && stateL && stateU) {
            index += 2;
            gInfo.score2Add += addNumber(index);
            explodeThis(x -1, y, x, y -1, index);
            explodeThis(x, y, x, y -1, index);
            mergeToHere(x, y -1, index);
            return;
        }
        if (index == indexR && index == indexU && stateR && stateU) {
            index += 2;
            gInfo.score2Add += addNumber(index);
            explodeThis(x + 1, y, x, y -1, index);
            explodeThis(x, y, x, y -1, index);
            mergeToHere(x, y -1, index);
            return;
        }
        if (index == indexL && stateL) {
            index++;
            gInfo.score2Add += addNumber(index);
            explodeThis(x -1, y, x, y, index);
            mergeToHere(x, y, index);
            return;
        }
        if (index == indexR && stateR) {
            index++;
            gInfo.score2Add += addNumber(index);
            explodeThis(x +1, y, x, y, index);
            mergeToHere(x, y, index);
            return;
        }
        if (index == indexU && stateU) {
            index++;
            gInfo.score2Add += addNumber(index);
            explodeThis(x, y, x, y -1, index);
            mergeToHere(x, y -1, index);
            return;
        }
        gInfo.cells[x][y].state = GInfo.STATE.PAUSED;
    }

    public void checkUp(int x, int y) {

        int index = gInfo.cells[x][y].index;
        if (index == gInfo.cells[x][y-1].index && gInfo.cells[x][y-1].state == GInfo.STATE.PAUSED) {
            index++;
            gInfo.score2Add += addNumber(index);
            explodeThis(x, y, x, y -1, index);
            mergeToHere(x, y -1, index);
            return;
        }
        gInfo.cells[x][y].state = GInfo.STATE.PAUSED;
    }

    public void checkRight(int x, int y) {
        if (x+1 >= xBlockCnt)
            return;
        int index = gInfo.cells[x][y].index;
        if (index == gInfo.cells[x+1][y].index && gInfo.cells[x+1][y].state == GInfo.STATE.PAUSED) {
            index++;
            gInfo.score2Add += addNumber(index);
            explodeThis(x, y, x, y -1, index);
            mergeToHere(x, y -1, index);
            return;
        }
    }

    private void mergeToHere(int x, int y, int index) {
        gInfo.bonusCount++;
        gInfo.cells[x][y].state = GInfo.STATE.MERGE;
        animationAdd.addMerge(x,y, index);
    }

    private void explodeThis(int x, int y, int xTo, int yTo, int index) {
        gInfo.cells[x][y].state = GInfo.STATE.EXPLODE;
        gInfo.cells[x][y].index = 0;
        animationAdd.addExplode(x,y, xTo, yTo, index);
    }

    public int addNumber(int index) {
        int addVal = new PowerIndex().power(index);
        if (!gInfo.showNext)
            addVal += addVal;
        if (gInfo.swing)
            addVal += addVal / 2;
        gInfo.bonusStacked += addVal;
        return addVal;
    }

}