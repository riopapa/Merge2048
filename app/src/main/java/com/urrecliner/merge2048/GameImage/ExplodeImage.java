package com.urrecliner.merge2048.GameImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.urrecliner.merge2048.GInfo;
import com.urrecliner.merge2048.R;

public class ExplodeImage {
    public Bitmap[] smallMaps;

    public ExplodeImage(GInfo gInfo, Context context) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        int [] maps = new int[] {R.drawable.a_explosion, R.drawable.a_explosion1,
                R.drawable.a_explosion2, R.drawable.a_explosion3, R.drawable.a_explosion};
        smallMaps = new Bitmap[5];  // MOVE_SMOOTH
        for (int i = 0; i < 5; i++) {
            Bitmap map = BitmapFactory.decodeResource(context.getResources(), maps[i] , options);
            smallMaps[i] = changeSize(map, gInfo.explodeGap, 100 - i*10);
        }
    }

    private Bitmap changeSize(Bitmap bitmap, int gap, int pct) {
        int bigSZ = bitmap.getWidth() + gap + gap;
        Bitmap changedMap = Bitmap.createBitmap(bigSZ, bigSZ, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(changedMap);
        int scaled = bigSZ * pct / 100;
        Bitmap newMap = Bitmap.createScaledBitmap(bitmap, scaled, scaled, false);
        canvas.drawBitmap(newMap, (bigSZ - scaled)/2f, (bigSZ - scaled)/4f, null);
        return changedMap;
    }
}