package com.urrecliner.merge2048;

import android.util.Log;

import com.google.gson.Gson;
import com.urrecliner.merge2048.GameObject.Saved;
import com.urrecliner.merge2048.Sub.DumpCells;
import com.urrecliner.merge2048.GameObject.Cell;
import com.urrecliner.merge2048.GamePlate.NextPlate;

/*
 * block just moved here or initially loaded, if can not go up, then STOP
 */

public class Start2Move {
    public Start2Move(GInfo gInfo, NextPlate nextPlate, CheckNearItem checkNearItem) {

        gInfo.shoutClicked = false;
        if (nextPlate.nextIndex == -1)
            return;
        Gson gson = new Gson();
        String jCell = gson.toJson(gInfo.cells);
        gInfo.sv.add(new Saved(jCell, nextPlate.nextIndex, nextPlate.nextNextIndex, gInfo.scoreNow));
        if (gInfo.sv.size() > 6) {   // max undo control
            gInfo.sv.remove(0);
        }

        if (gInfo.dumpCount > 4)
            new DumpCells(gInfo, gInfo.cells, nextPlate, "Start2Move");
        int x = gInfo.shootIndex;
        Cell cell = gInfo.cells[x][gInfo.Y_BLOCK_CNT -1];
        if (cell.index == 0) {  // empty cell, so start to move
            gInfo.cells[x][gInfo.Y_BLOCK_CNT -1] = new Cell(nextPlate.nextIndex, GInfo.STATE.GO_UP);
        } else if (cell.index == nextPlate.nextIndex) {    // bottom but same index
            gInfo.cells[x][gInfo.Y_BLOCK_CNT -1].index = cell.index + 1;
            gInfo.cells[x][gInfo.Y_BLOCK_CNT -1].state = GInfo.STATE.STOP;
        }
        nextPlate.nextIndex = -1;   // wait while all moved
    }

}