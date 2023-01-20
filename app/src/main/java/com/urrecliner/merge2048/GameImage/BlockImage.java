package com.urrecliner.merge2048.GameImage;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;

import com.urrecliner.merge2048.GInfo;
import com.urrecliner.merge2048.R;

public class BlockImage {
    public int idx;
    public int number;

    public Bitmap bitmap, xorMap;
    public Bitmap [] flyMaps;
    public Bitmap halfMap;
    public Bitmap [] explodeMaps;
    public Bitmap destroyMap;  // self destroy 2048

    public BlockImage(int idx, int number, GInfo gInfo, Context context) {
        this.idx = idx;
        this.number = number;
        final TypedArray orgMapId = context.getResources().obtainTypedArray(R.array.block_images);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        bitmap = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(context.getResources(), orgMapId.getResourceId(idx, -1), options),
                gInfo.blockInSize, gInfo.blockInSize, false);
        halfMap = Bitmap.createScaledBitmap(bitmap,
                gInfo.blockInSize /2, gInfo.blockInSize /2,false);
        flyMaps = new Bitmap[7];
        flyMaps[0] = makeFlyMap(bitmap, gInfo, 105); // max 110%
        flyMaps[1] = makeFlyMap(bitmap, gInfo, 110);
        flyMaps[2] = makeFlyMap(bitmap, gInfo, 105);
        flyMaps[3] = makeFlyMap(bitmap, gInfo, 100);
        flyMaps[4] = makeFlyMap(bitmap, gInfo, 95);
        flyMaps[5] = makeFlyMap(bitmap, gInfo, 100);
        flyMaps[6] = makeFlyMap(bitmap, gInfo, 105);

        xorMap = makeXorMap(bitmap, gInfo);

        Bitmap explode = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(context.getResources(), R.drawable.a_explosion_e, options),
                gInfo.blockOutSize+gInfo.explodeGap, gInfo.blockOutSize+gInfo.explodeGap, false);

        Bitmap expMap = makeExplode(bitmap, gInfo.explodeGap, explode);
        explodeMaps = new Bitmap[6];
        explodeMaps[0] = makeExplodeMap(bitmap, gInfo, 80);
        explodeMaps[1] = makeExplodeMap(expMap, gInfo, 76);
        explodeMaps[2] = makeExplodeMap(bitmap, gInfo, 77);
        explodeMaps[3] = makeExplodeMap(bitmap, gInfo, 70);
        explodeMaps[4] = makeExplodeMap(expMap, gInfo, 65);
        explodeMaps[5] = makeExplodeMap(bitmap, gInfo, 65);

        destroyMap = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(context.getResources(), R.drawable.destroy, options),
                gInfo.blockOutSize+gInfo.explodeGap, gInfo.blockOutSize+gInfo.explodeGap, false);
        destroyMap = makeExplode(bitmap, gInfo.explodeGap, destroyMap);
    }

    private Bitmap makeFlyMap(Bitmap bitmap, GInfo gInfo, int pct) {
        int scale = (gInfo.blockInSize + gInfo.blockFlyingGap*2); // 120 %
        Bitmap bigMap = Bitmap.createBitmap(scale, scale, Bitmap.Config.ARGB_8888);
        int fScale = scale * pct / 100;
        Bitmap flyMap = Bitmap.createScaledBitmap(bitmap, fScale, fScale,false);
        Canvas canvas = new Canvas(bigMap);
        canvas.drawBitmap(flyMap,(scale-fScale)/2f , (scale-fScale)/2f, null);
        return bigMap;
    }

    private Bitmap makeExplodeMap(Bitmap bitmap, GInfo gInfo, int pct) {
        int scale = (gInfo.blockInSize + gInfo.blockFlyingGap*2); // 120 %
        Bitmap bigMap = Bitmap.createBitmap(scale, scale, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bigMap);
        int fScale = scale * pct / 100;
        Bitmap map = Bitmap.createScaledBitmap(bitmap, fScale, fScale,false);
        canvas.drawBitmap(map,(scale-fScale)/2f , (scale-fScale)/2f, null);
        return bigMap;
    }

    private Bitmap makeXorMap(Bitmap bitmap, GInfo gInfo) {
        int scale = gInfo.blockOutSize;
        Bitmap xorMap = Bitmap.createScaledBitmap(bitmap, scale, scale, false);
        int[] colors = new int[scale * scale];
        xorMap.getPixels(colors, 0, scale, 0, 0, scale, scale);
        for (int i = 0; i < scale * scale; i++) {
            if (colors[i] != 0) {
                colors[i] = ((colors[i] ^ 0x00AAAAAA) | 0xFF000000) & 0x80FFFFFF;
            }
        }
        xorMap.setPixels(colors, 0, scale, 0, 0, scale, scale);
        return xorMap;
    }

    private Bitmap makeExplode(Bitmap bitmap, int gap, Bitmap explode) {

        int full = bitmap.getHeight();
        int half = full / 2;
        Bitmap tmpMap = Bitmap.createBitmap(full, full,
                Bitmap.Config.ARGB_8888);
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
}