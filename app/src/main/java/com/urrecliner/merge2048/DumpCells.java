package com.urrecliner.merge2048;

import android.util.Log;

import com.urrecliner.merge2048.GamePlate.NextPlate;

public class DumpCells {

    public DumpCells(GInfo gInfo, Animation animation, final CheckNearItem checkNearItem, NextPlate nextPlate, String msg) {
        StringBuilder sb = new StringBuilder("    |    0        |    1        |    2        |    3        |    4 ");
        for (int y = 0; y < gInfo.yBlockCnt; y++) {
            sb.append("\n ").append(y).append(" ");
            for (int x = 0; x < gInfo.xBlockCnt; x++) {
                int nbr = checkNearItem.powerIndex(gInfo.cells[x][y].index);
                String sNbr = ""+nbr;
                int space = (7 - sNbr.length())/2;
                String s = ("       ").substring(0,space)+nbr+("       ").substring(0,space);
                if (s.length()>6)
                    s = s.substring(0,6);
                sb.append(" |").append(s).append(gInfo.cells[x][y].state);
            }
        }
        sb.append("\n touch=").append(gInfo.touchIndex);
        sb.append(" index=").append(nextPlate.nextIndex);
        sb.append(" block=").append(checkNearItem.powerIndex(nextPlate.nextIndex));

        Log.w("dumpA "+gInfo.aniStacks.size(), "<<< "+msg+" >>>");
        Log.w("dumpA "+gInfo.aniStacks.size(), sb.toString());
    }

    public DumpCells(GInfo gInfo, String msg) {
        StringBuilder sb = new StringBuilder("           0           1           2          3           4 ");
        for (int y = 0; y < gInfo.yBlockCnt; y++) {
            sb.append("\n ").append(y).append(" ");
            for (int x = 0; x < gInfo.xBlockCnt; x++) {
                int nbr = powerIndex(gInfo.cells[x][y].index);
                String sNbr = ""+nbr;
                int space = (7 - sNbr.length())/2;
                String s = ("       ").substring(0,space)+nbr+("       ").substring(0,space);
                if (s.length()>6)
                    s = s.substring(0,6);
                sb.append(s).append(gInfo.cells[x][y].state);
            }
        }

        Log.w("dumpB "+gInfo.aniStacks.size(), "<<< "+msg+" >>>");
        Log.w("dumpB "+gInfo.aniStacks.size(), sb.toString());
    }


    private int powerIndex(int index) {
        int number;
        if (index != 0) {
            number = 1;
            while (index-- > 0)
                number = number + number;
        } else
            number = 0;
        return number;
    }

}