package com.urrecliner.merge2048;

public class CheckState {

    public boolean paused(GInfo gInfo) {

        for (int y = 0; y < gInfo.Y_BLOCK_CNT; y++) {
            for (int x = 0; x < gInfo.X_BLOCK_CNT; x++) {
                if (gInfo.cells[x][y].state != GInfo.STATE.PAUSED) {
                    return false;
                }
            }
        }
        return true;
    }
}