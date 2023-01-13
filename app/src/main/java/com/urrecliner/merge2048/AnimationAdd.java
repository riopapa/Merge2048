package com.urrecliner.merge2048;

import android.util.Log;

import com.urrecliner.merge2048.GameObject.AniStack;

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
            maxCount = 3;
        else if (maxCount < 6)
            maxCount = 4;
        else if (maxCount < 9)
            maxCount = 5;
        else
            maxCount = 6;

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
                gInfo.blockOutSize * (xF - xS) / smooth,
                gInfo.blockOutSize * (yF - yS)/ smooth, getNextTime(), index));
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