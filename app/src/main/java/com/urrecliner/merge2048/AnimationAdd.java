package com.urrecliner.merge2048;

import android.util.Log;

import com.urrecliner.merge2048.GameObject.AniStack;

import java.util.List;

public class AnimationAdd {

    final GInfo gInfo;
    final int smooth;
    public AnimationAdd( GInfo gInfo, int smooth) {
        this.gInfo = gInfo;
        this.smooth = smooth;
    }

    public void addMove(int xS, int yS, int xF, int yF, int block) {
        int maxCount = (yF-yS > 2) ? (yF-yS) / 2: 1;
        maxCount *= smooth;
        long timeStamp = System.currentTimeMillis();
        if (gInfo.aniStacks.size() > 0) {
            long adjustTime = gInfo.aniStacks.get(gInfo.aniStacks.size() - 1).timeStamp + 40L;
            if (adjustTime > timeStamp)
                timeStamp = adjustTime;
        }
        gInfo.aniStacks.add(new AniStack(GInfo.STATE.MOVING, xS, yS, xF, yF,
                gInfo.blockOutSize * (xF - xS) / maxCount,
                gInfo.blockOutSize * (yF - yS)/ maxCount, maxCount, timeStamp, block));
    }

    public void addMerge(int x, int y, int index) {
        gInfo.aniStacks.add(new AniStack(GInfo.STATE.MERGE, x, y, index));
    }

    public void addExplode(int xS, int yS, int xF, int yF) {
        gInfo.aniStacks.add(new AniStack(GInfo.STATE.EXPLODE, xS, yS,
                gInfo.blockOutSize * (xF - xS) / smooth,
                gInfo.blockOutSize * (yF - yS)/ smooth));
    }

}