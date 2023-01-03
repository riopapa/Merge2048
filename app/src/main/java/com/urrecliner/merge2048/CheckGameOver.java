package com.urrecliner.merge2048;

import com.urrecliner.merge2048.GamePlate.NextPlate;

public class CheckGameOver {
    final GInfo gInfo;
    final NextPlate nextPlate;
    int xBlockCnt, yBlockCnt;

    public CheckGameOver(GInfo gInfo, NextPlate nextPlate) {
        this.gInfo = gInfo;
        this.xBlockCnt = gInfo.xBlockCnt;
        this.yBlockCnt = gInfo.yBlockCnt;
        this.nextPlate = nextPlate;
    }

    public boolean isOver() {
        for (int x = 0; x < xBlockCnt; x++) {
            if (gInfo.cells[x][yBlockCnt-1].index == 0 ||
                    gInfo.cells[x][yBlockCnt-1].state != GInfo.STATE.PAUSED ||
                    gInfo.cells[x][yBlockCnt-1].index == nextPlate.nextIndex)
                return false;
        }
        return true;
    }
}