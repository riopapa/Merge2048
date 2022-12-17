package com.urrecliner.merge2048.GameObject;

import android.graphics.Canvas;

public abstract class GameObject {
    int xPos, yPos;

    public GameObject(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;

    }
    public abstract void draw(Canvas canvas);
    public abstract void update();


}