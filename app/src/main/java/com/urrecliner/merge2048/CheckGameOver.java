package com.urrecliner.merge2048;

public class CheckGameOver {
    final GInfo gInfo;
    final AnimationAdd animationAdd;
    final int xBlockCnt, yBlockCnt;

    public CheckGameOver(GInfo gInfo, AnimationAdd animationAdd) {
        this.gInfo = gInfo;
        this.animationAdd = animationAdd;
        this.xBlockCnt = gInfo.X_BLOCK_CNT;
        this.yBlockCnt = gInfo.Y_BLOCK_CNT;
    }

    public boolean isOver(int nextIndex) {

        for (int x = 0; x < xBlockCnt; x++) {
            if (gInfo.cells[x][yBlockCnt-1].index == 0 ||
                gInfo.cells[x][yBlockCnt-1].state != GInfo.STATE.PAUSED ||
                gInfo.cells[x][yBlockCnt-1].index == nextIndex)
                return false;
        }
        return true;
    }

    public void destroy() {
        for (int y = 0; y < yBlockCnt; y++) {
            for (int x = 0; x < xBlockCnt; x++) {
                if (gInfo.cells[x][y].index > gInfo.CONTINUE_INDEX) {
                    animationAdd.addDestroy(x,y, gInfo.cells[x][y].index);
                    gInfo.cells[x][y].state = GInfo.STATE.EXPLODE;
                    gInfo.cells[x][y].index = 0;
                    return;
                }
            }
        }
        gInfo.isGameOver = false;
        gInfo.continueYes = false;
        gInfo.is2048 = false;
    }
}