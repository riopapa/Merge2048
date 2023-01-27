package com.urrecliner.merge2048;

import android.content.Context;

import com.urrecliner.merge2048.GamePlate.MessagePlate;
import com.urrecliner.merge2048.GamePlate.NextPlate;
import com.urrecliner.merge2048.Sub.ClearCells;
import com.urrecliner.merge2048.Sub.UserName;

public class NewGame {
    public NewGame(GInfo gInfo, MessagePlate messagePlate, NextPlate nextPlate, Context context) {

        new ClearCells(gInfo);
        gInfo.resetValues();
        new HighScore(gInfo, context).get();
        nextPlate.setNextBlock();
        nextPlate.setNextBlock();
        messagePlate.set(gInfo.userName +" 님", "게임을", "시작합니다",
                System.currentTimeMillis(), 2000);
    }
}