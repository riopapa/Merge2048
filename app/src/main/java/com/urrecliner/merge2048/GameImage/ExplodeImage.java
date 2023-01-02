package com.urrecliner.merge2048.GameImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.urrecliner.merge2048.GInfo;
import com.urrecliner.merge2048.R;

public class ExplodeImage {

    public Bitmap [] smallMaps;
    public final int explodeGap = 64;
    public ExplodeImage(GInfo gInfo, Context context) {

        Bitmap explodeMap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.a_explosion);

        Bitmap bitmap = Bitmap.createScaledBitmap(explodeMap,
                gInfo.blockInSize + explodeGap + explodeGap,
                gInfo.blockInSize + explodeGap + explodeGap, false);

        smallMaps = new Bitmap[5];  // MOVE_SMOOTH
        smallMaps[0] = bitmapSmall(bitmap, 100);
        smallMaps[1] = bitmapSmall(bitmap, 90);
        smallMaps[2] = bitmapSmall(bitmap, 80);
        smallMaps[3] = bitmapSmall(bitmap, 70);
        smallMaps[4] = bitmapSmall(bitmap, 60);

    }

    private Bitmap bitmapSmall(Bitmap bitmap, int pct) {

        int xScale = bitmap.getWidth() * pct / 100;
        int yScale = bitmap.getHeight() * pct / 100;
        Bitmap newMap = Bitmap.createScaledBitmap(bitmap, xScale, yScale,false);
        Bitmap smallMap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(smallMap);
        canvas.drawBitmap(newMap, (bitmap.getWidth() - xScale)/2f,
                (bitmap.getHeight() - yScale)/2f, null);
        return smallMap;
    }
}