package com.urrecliner.merge2048;

public class CheckGoUp {
    final GInfo gInfo;
    final AnimationAdd animationAdd;
    public CheckGoUp(GInfo gInfo, AnimationAdd animationAdd) {
        this.gInfo = gInfo;
        this.animationAdd = animationAdd;
    }

    public void check(int x, int y) {

        if (y > 0) {
            int yUp = 0;
            for (int yy = y - 1; yy >= 0; yy--) {
                if (gInfo.cells[x][yy].index != 0) {
                    yUp = yy + 1;
                    break;
                }
            }
            if (yUp != y) {
                gInfo.cells[x][y].state = GInfo.STATE.MOVING;
                animationAdd.addMove(x, y, x, yUp, gInfo.cells[x][y].index);
//                Log.w("Add", y +" > "+yUp);
                return;
            }
        }
        gInfo.cells[x][y].state = GInfo.STATE.STOP;
    }
}