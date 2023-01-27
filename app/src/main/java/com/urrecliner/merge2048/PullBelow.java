package com.urrecliner.merge2048;

public class PullBelow {
    /*
     *   block exploded, so if any block below, pull them up
     */
    final GInfo gInfo;
    final AnimationAdd animationAdd;
    final int yBlockCnt;
    public PullBelow(GInfo gInfo, AnimationAdd animationAdd) {
        this.gInfo = gInfo;
        this.animationAdd = animationAdd;
        yBlockCnt = gInfo.Y_BLOCK_CNT;
    }

    public void check(int x, int y, boolean rotate) {

        if (y == yBlockCnt - 1 || gInfo.cells[x][y+1].index == 0)
            return;
        for (int yy = y+1; yy < yBlockCnt; yy++) {
            if (gInfo.cells[x][yy].index > 0) {
                if (rotate) {
                    gInfo.cells[x][yy].state = GInfo.STATE.PULL;
                    animationAdd.addPull(x, yy, x, yy-1, gInfo.cells[x][yy].index);
                } else {
                    gInfo.cells[x][yy].state = GInfo.STATE.MOVING;
                    animationAdd.addMove(x, yy, x, yy-1, gInfo.cells[x][yy].index);
                }
            } else
                break;
        }
    }

}