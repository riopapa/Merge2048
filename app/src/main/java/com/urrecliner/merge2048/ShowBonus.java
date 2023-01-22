package com.urrecliner.merge2048;

import com.urrecliner.merge2048.GamePlate.BonusPlate;
import com.urrecliner.merge2048.GamePlate.MessagePlate;
import com.urrecliner.merge2048.Sub.PowerIndex;

public class ShowBonus {

    public ShowBonus(GInfo gInfo, int x, int y, BonusPlate bonusPlate, MessagePlate messagePlate) {

        if (gInfo.bonusCount > 2) {
            bonusPlate.addBonus(x, y, gInfo.bonusCount - 2,
                    gInfo.bonusLoopCount + gInfo.bonusCount + gInfo.bonusCount);
            if (gInfo.bonusCount > 4) {
                gInfo.gameDifficulty++;
                messagePlate.set("!연속 블럭 깨기!",
                        "큰 블럭("+new PowerIndex().power(gInfo.gameDifficulty+1)+")이",
                        "나올 수 있어요",
                        System.currentTimeMillis() + 1500, 2500);
            }
        }
        gInfo.bonusCount = 0;
        gInfo.bonusStacked = 0;
    }
}