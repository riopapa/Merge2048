package com.urrecliner.merge2048.GameObject;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.urrecliner.merge2048.GameInfo;

import java.util.Random;

public class Flight extends  GameObject{
    private final GameInfo gameInfo;
    int aXStart, aYStart, aXFinish, aYFinish;
    int aDirX, aDirY;
    Drawable drawable;

    public Flight(int xPos, int yPos, GameInfo gameInfo, Drawable drawable) {
        super(xPos, yPos);
        aDirX = 1;
        aDirY = 1;
        this.gameInfo = gameInfo;
        this.drawable = drawable;
    }

    @Override
    public void update() {

        aXStart += aDirX * new Random().nextInt(20);
        aYStart += aDirY * new Random().nextInt(20);
        if (gameInfo.outOfCanvas(aXStart, gameInfo.screenXSize)) {
            aDirX = aDirX * -1;
            aXStart += aDirX * 20;
        }
        if (gameInfo.outOfCanvas(aYStart, gameInfo.screenYSize)) {
            aDirY = aDirY * -1;
            aYStart += aDirY * 20;
        }
        aXFinish = aXStart + 100;
        aYFinish = aYStart + 100;
    }

    @Override
    public void draw(Canvas canvas) {
        drawable.setBounds(aXStart, aYStart, aXFinish, aYFinish);
        drawable.draw(canvas);

    }
}