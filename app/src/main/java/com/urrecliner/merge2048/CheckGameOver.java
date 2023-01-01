package com.urrecliner.merge2048;

import com.urrecliner.merge2048.GameObject.HighMember;
import com.urrecliner.merge2048.GameObject.Ani;
import com.urrecliner.merge2048.GamePlate.NextPlate;

import java.util.Comparator;
import java.util.List;

public class CheckGameOver {
    GameInfo gameInfo;
    NextPlate nextPlate;
    Ani ani;
    int xBlockCnt, yBlockCnt;

    public CheckGameOver(GameInfo gameInfo, NextPlate nextPlate, Ani ani) {
        this.gameInfo = gameInfo;
        this.xBlockCnt = gameInfo.xBlockCnt;
        this.yBlockCnt = gameInfo.yBlockCnt;
        this.nextPlate = nextPlate;
        this.ani = ani;
    }

    public boolean isOver() {
        for (int x = 0; x < xBlockCnt; x++) {
            if (ani.cells[x][yBlockCnt-1].index == 0 ||
                    ani.cells[x][yBlockCnt-1].state != GameInfo.STATE.PAUSED ||
                    ani.cells[x][yBlockCnt-1].index == nextPlate.nextIndex)
                return false;
        }
        return true;
    }

    public boolean updateHighScore() {
        List<HighMember> mbrList = gameInfo.highMembers;
        long lastScore;
        if (mbrList.size() == 0)
            lastScore = 0;
        else
            lastScore = mbrList.get(mbrList.size()-1).score;
        if (gameInfo.scoreNow > lastScore) {
            mbrList.add(new HighMember(gameInfo.scoreNow, "Me"));
            mbrList.sort(Comparator.comparingLong(HighMember::getScore).reversed());
            while (mbrList.size() > 3)
                mbrList.remove(3);
            gameInfo.highMembers = mbrList;
            return true;
        }
        return false;
    }

}