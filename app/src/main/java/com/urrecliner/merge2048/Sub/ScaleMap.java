package com.urrecliner.merge2048.Sub;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class ScaleMap {

    public Bitmap build(Context context, int resId, int blockIconSize) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
        return Bitmap.createScaledBitmap(bitmap, blockIconSize-4, blockIconSize-4, false);
    }

    public Bitmap blink(Bitmap bitmap, int blockIconSize) {
        Bitmap bMap = Bitmap.createBitmap(blockIconSize, blockIconSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bMap);
        Bitmap sMap = Bitmap.createScaledBitmap(bitmap, blockIconSize*7/8, blockIconSize*7/8, false);
        canvas.drawBitmap(sMap, blockIconSize/16f, blockIconSize/16f, null);
        return bMap;
    }

}