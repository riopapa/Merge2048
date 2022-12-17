package com.urrecliner.merge2048.GamePlate;

import static com.urrecliner.merge2048.GamePlate.Grid.MOVE_SMOOTH;

public class AniPool {
    public enum aniType {
        ANI_MOVE,
        ANI_DELETE
    }

    int aniType;
    int xStart, yStart, xFinish, yFinish;   // move Start to Finish cell
    int xIncrease, yIncrease, count;     // ani increments stack
    boolean active;

    long delay = 100;   // redraw at every 100 mili sec
    long timeStamp = 0;


    public AniPool(int aniType, int xStart, int yStart, int xFinish, int yFinish, int xOSize, int yOSize) {
        this.aniType = aniType;
        this.xStart = xStart;
        this.yStart = yStart;
        this.xFinish = xFinish;
        this.yFinish = yFinish;
        xIncrease = xOSize / MOVE_SMOOTH;    // to make move smooth
        yIncrease = yOSize / MOVE_SMOOTH;
        if (xStart > xFinish)
            xIncrease = -xIncrease;
        else if (xStart == xFinish)
            xIncrease = 0;
        if (yStart > yFinish)
            yIncrease = -yIncrease;
        else if (yStart == yFinish)
            yIncrease = 0;
        count = 0;
        active = true;
    }
}