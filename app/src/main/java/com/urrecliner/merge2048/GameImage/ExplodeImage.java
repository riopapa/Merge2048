package com.urrecliner.merge2048.GameImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;

import com.urrecliner.merge2048.GInfo;
import com.urrecliner.merge2048.R;

public class ExplodeImage {

    public Bitmap[] makeExplodes(Bitmap bitmap, GInfo gInfo, Context context) {
        Bitmap [] explodeMaps = new Bitmap[6];
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        Bitmap explode = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(context.getResources(), R.drawable.a_explosion_e, options),
                gInfo.blockOutSize+gInfo.explodeGap, gInfo.blockOutSize+gInfo.explodeGap, false);

        Bitmap expMap = mixExplode(bitmap, gInfo.explodeGap, explode);
        explodeMaps[0] = makeExplode(bitmap, gInfo, 80);
        explodeMaps[1] = makeExplode(expMap, gInfo, 76);
        explodeMaps[2] = makeExplode(bitmap, gInfo, 77);
        explodeMaps[3] = makeExplode(bitmap, gInfo, 70);
        explodeMaps[4] = makeExplode(expMap, gInfo, 65);
        explodeMaps[5] = makeExplode(bitmap, gInfo, 65);
        return explodeMaps;
    }

    private Bitmap makeExplode(Bitmap bitmap, GInfo gInfo, int pct) {
        int scale = (gInfo.blockInSize + gInfo.blockFlyingGap*2); // 120 %
        Bitmap bigMap = Bitmap.createBitmap(scale, scale, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bigMap);
        int fScale = scale * pct / 100;
        Bitmap map = Bitmap.createScaledBitmap(bitmap, fScale, fScale,false);
        canvas.drawBitmap(map,(scale-fScale)/2f , (scale-fScale)/2f, null);
        return bigMap;
    }

    public Bitmap mixExplode(Bitmap bitmap, int gap, Bitmap explode) {

        int full = bitmap.getHeight();
        int half = full / 2;
        Bitmap tmpMap = Bitmap.createBitmap(full, full, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(tmpMap);
        canvas.drawBitmap(bitmap, new Rect(0, 0, half, half), new Rect(0, 0, half - gap, half - gap),null);
        canvas.drawBitmap(bitmap, new Rect(half, 0, full, half), new Rect(half + gap, 0, full, half-gap),null);
        canvas.drawBitmap(bitmap, new Rect(0, half, half, full), new Rect(0, half + gap, half - gap, full),null);
        canvas.drawBitmap(bitmap, new Rect(half, half, full, full), new Rect(half + gap, half + gap, full, full),null);

        Paint exPaint = new Paint();
        exPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN));
        canvas.drawBitmap(explode, -gap/2f, -gap/2f, exPaint);
        return tmpMap;
    }

    public Bitmap[] makeFlys(Bitmap bitmap, GInfo gInfo) {
        Bitmap[] flyMaps = new Bitmap[7];
        flyMaps[0] = makeFly(bitmap, gInfo, 105); // max 110%
        flyMaps[1] = makeFly(bitmap, gInfo, 110);
        flyMaps[2] = makeFly(bitmap, gInfo, 105);
        flyMaps[3] = makeFly(bitmap, gInfo, 100);
        flyMaps[4] = makeFly(bitmap, gInfo, 95);
        flyMaps[5] = makeFly(bitmap, gInfo, 100);
        flyMaps[6] = makeFly(bitmap, gInfo, 105);
        return flyMaps;
    }

    private Bitmap makeFly(Bitmap bitmap, GInfo gInfo, int pct) {
        int scale = (gInfo.blockInSize + gInfo.blockFlyingGap*2); // 120 %
        Bitmap bigMap = Bitmap.createBitmap(scale, scale, Bitmap.Config.ARGB_8888);
        int fScale = scale * pct / 100;
        Bitmap flyMap = Bitmap.createScaledBitmap(bitmap, fScale, fScale,false);
        Canvas canvas = new Canvas(bigMap);
        canvas.drawBitmap(flyMap,(scale-fScale)/2f , (scale-fScale)/2f, null);
        return bigMap;
    }
}