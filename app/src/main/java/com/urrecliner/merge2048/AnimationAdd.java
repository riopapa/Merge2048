package com.urrecliner.merge2048;

import android.util.Log;

import com.urrecliner.merge2048.GameObject.AniStack;

import java.util.Random;

public class AnimationAdd {

    final GInfo gInfo;
    final int smooth;
    public AnimationAdd( GInfo gInfo, int smooth) {
        this.gInfo = gInfo;
        this.smooth = smooth;
    }


    public void addMove(int xS, int yS, int xF, int yF, int block) {
        int maxCount = (Math.abs(yF-yS)+1)*(Math.abs(xF-xS)+1);
        if (maxCount < 3)
            maxCount = 2;
        else if (maxCount < 6)
            maxCount = 3;
        else
            maxCount = 4;
        maxCount += new Random().nextInt(2);

        gInfo.aniStacks.add(new AniStack(GInfo.STATE.MOVING, xS, yS, xF, yF,
                gInfo.blockOutSize * (xF - xS) / maxCount,
                gInfo.blockOutSize * (yF - yS)/ maxCount,
                maxCount, getNextTime(), block));
    }

    public void addMerge(int x, int y, int index) {

        gInfo.aniStacks.add(new AniStack(GInfo.STATE.MERGE, x, y, index, getNextTime()));
    }

    public void addExplode(int xS, int yS, int xF, int yF, int index) {

        gInfo.aniStacks.add(new AniStack(GInfo.STATE.EXPLODE, xS, yS,
                gInfo.blockOutSize * (xF - xS) / 4,
                gInfo.blockOutSize * (yF - yS)/ 4, getNextTime(), index));
    }

    public void addDestroy(int xS, int yS, int index) {
        gInfo.aniStacks.add(new AniStack(GInfo.STATE.DESTROY, xS, yS, getNextTime() + 200, index));
    }
    private long getNextTime() {
        long timeStamp;
        if (gInfo.aniStacks.size() > 0)
            timeStamp = gInfo.aniStacks.get(gInfo.aniStacks.size() - 1).timeStamp + 5L;
        else
            timeStamp = System.currentTimeMillis() + 5L;
        return  timeStamp;
    }
}