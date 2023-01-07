package com.urrecliner.merge2048;

import com.urrecliner.merge2048.GamePlate.NextPlate;

public class CheckGameOver {
    final GInfo gInfo;
    final int xBlockCnt, yBlockCnt;

    public CheckGameOver(GInfo gInfo) {
        this.gInfo = gInfo;
        this.xBlockCnt = gInfo.xBlockCnt;
        this.yBlockCnt = gInfo.yBlockCnt;
    }

    public boolean isOver(int nextIndex) {
        if (nextIndex == -1)
            return false;
        for (int x = 0; x < xBlockCnt; x++) {
            if (gInfo.cells[x][yBlockCnt-1].index == 0 ||
                gInfo.cells[x][yBlockCnt-1].state != GInfo.STATE.PAUSED ||
                gInfo.cells[x][yBlockCnt-1].index == nextIndex)
                return false;
        }
        return true;
    }
}