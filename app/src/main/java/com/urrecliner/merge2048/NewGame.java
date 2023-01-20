package com.urrecliner.merge2048;

import com.urrecliner.merge2048.GamePlate.MessagePlate;
import com.urrecliner.merge2048.GamePlate.NextPlate;

public class NewGame {
    public NewGame(GInfo gInfo, MessagePlate messagePlate, HighScore highScore, NextPlate nextPlate) {

        messagePlate.set("Welcome", "게임을", "시작합니다",
                System.currentTimeMillis(), 1000);

        gInfo.resetValues();
        highScore.get();
        gInfo.highLowScore = gInfo.highMembers.get(gInfo.highMembers.size()-1).score;

        new ClearCells(gInfo);
        nextPlate.generateNextBlock(true);
        nextPlate.generateNextBlock(true);

    }
}