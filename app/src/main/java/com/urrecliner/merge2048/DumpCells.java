package com.urrecliner.merge2048;

import android.util.Log;

import com.urrecliner.merge2048.GamePlate.NextPlate;

public class DumpCells {

    public DumpCells(GInfo gInfo, final CheckNearItem checkNearItem, final NextPlate nextPlate,
                     String msg) {
        StringBuilder sb = new StringBuilder("    |    0        |    1        |    2        |    3        |    4 ");
        for (int y = 0; y < gInfo.Y_BLOCK_CNT; y++) {
            sb.append("\n ").append(y).append(" ");
            for (int x = 0; x < gInfo.X_BLOCK_CNT; x++) {
                int nbr = new PowerIndex().power(gInfo.cells[x][y].index);
                String sNbr = ""+nbr;
                int space = (7 - sNbr.length())/2;
                String s = ("       ").substring(0,space)+nbr+("       ").substring(0,space);
                if (s.length()>6)
                    s = s.substring(0,6);
                sb.append(" |").append(s).append(gInfo.cells[x][y].state);
            }
        }
        sb.append("\n touch=").append(gInfo.shootIndex);
        sb.append(" index=").append(nextPlate.nextIndex);
        sb.append(" block=").append(new PowerIndex().power(nextPlate.nextIndex));
        sb.append(" nxtblock=").append(new PowerIndex().power(nextPlate.nextNextIndex));
        sb.append(" bonus=").append(gInfo.bonusCount);

        Log.w("dump "+gInfo.aniStacks.size(), "<<< " + msg + " >>>");
        Log.w("dump "+gInfo.aniStacks.size(), sb.toString());
    }

}