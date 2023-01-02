package com.urrecliner.merge2048;

import com.urrecliner.merge2048.GameObject.HighMember;
import com.urrecliner.merge2048.GamePlate.NextPlate;

import java.util.Comparator;
import java.util.List;

public class CheckGameOver {
    final GInfo gInfo;
    final NextPlate nextPlate;
    final Animation animation;
    int xBlockCnt, yBlockCnt;

    public CheckGameOver(GInfo gInfo, NextPlate nextPlate, Animation animation) {
        this.gInfo = gInfo;
        this.xBlockCnt = gInfo.xBlockCnt;
        this.yBlockCnt = gInfo.yBlockCnt;
        this.nextPlate = nextPlate;
        this.animation = animation;
    }

    public boolean isOver() {
        for (int x = 0; x < xBlockCnt; x++) {
            if (gInfo.cells[x][yBlockCnt-1].index == 0 ||
                    gInfo.cells[x][yBlockCnt-1].state != GInfo.STATE.PAUSED ||
                    gInfo.cells[x][yBlockCnt-1].index == nextPlate.nextIndex)
                return false;
        }
        return true;
    }

    public boolean updateHighScore() {
        List<HighMember> mbrList = gInfo.highMembers;
        long lastScore;
        if (mbrList.size() == 0)
            lastScore = 0;
        else
            lastScore = mbrList.get(mbrList.size()-1).score;
        if (gInfo.scoreNow > lastScore) {
            mbrList.add(new HighMember(gInfo.scoreNow, "Me"));
            mbrList.sort(Comparator.comparingLong(HighMember::getScore).reversed());
            while (mbrList.size() > 3)
                mbrList.remove(3);
            gInfo.highMembers = mbrList;
            gInfo.msgHead = "♡축하합니다♡";
            gInfo.msgLine1 = "스코어 명단에";
            gInfo.msgLine2 = "올라 갔습니다!";
            return true;
        }
        return false;
    }

}