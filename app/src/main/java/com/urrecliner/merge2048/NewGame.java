package com.urrecliner.merge2048;

import android.content.Context;

import com.urrecliner.merge2048.GamePlate.MessagePlate;
import com.urrecliner.merge2048.GamePlate.NextPlate;
import com.urrecliner.merge2048.Sub.ClearCells;
import com.urrecliner.merge2048.Sub.UserName;

public class NewGame {
    public NewGame(GInfo gInfo, MessagePlate messagePlate, HighScore highScore, Context context) {

        new ClearCells(gInfo);
        gInfo.resetValues();
        highScore.get();
        gInfo.highLowScore = gInfo.highMembers.get(gInfo.highMembers.size()-1).score;

        new UserName().get(context, gInfo);
        messagePlate.set("Welcome", "게임을", "시작합니다",
                System.currentTimeMillis(), 2000);

    }
}