package com.urrecliner.merge2048.Sub;

import com.urrecliner.merge2048.GInfo;
import com.urrecliner.merge2048.GameObject.Cell;

public class ClearCells {
    public ClearCells(GInfo gInfo) {
        for (int y = 0; y < gInfo.Y_BLOCK_CNT; y++) {
            for (int x = 0; x < gInfo.X_BLOCK_CNT; x++) {
                gInfo.cells[x][y] = new Cell(0, GInfo.STATE.PAUSED); // 0 means null
            }
        }
    }
}