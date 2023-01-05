package com.urrecliner.merge2048.GameImage;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

import com.urrecliner.merge2048.GInfo;
import com.urrecliner.merge2048.R;

public class BlockImage {
    public int idx;
    public int number;

    public Bitmap bitmap;
    public Bitmap [] flyMaps;
    public Bitmap halfMap;

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
        flyMaps = new Bitmap[9];
        flyMaps[0] = makeFlyMap(bitmap, gInfo, 105); // max 110%
        flyMaps[1] = makeFlyMap(bitmap, gInfo, 110);
        flyMaps[2] = makeFlyMap(bitmap, gInfo, 105);
        flyMaps[3] = makeFlyMap(bitmap, gInfo, 110);
        flyMaps[4] = makeFlyMap(bitmap, gInfo, 105);
        flyMaps[5] = makeFlyMap(bitmap, gInfo, 110);
        flyMaps[6] = makeFlyMap(bitmap, gInfo, 105);
        flyMaps[7] = makeFlyMap(bitmap, gInfo, 100);
        flyMaps[8] = makeXorMap(bitmap, gInfo);

    }

    private Bitmap makeFlyMap(Bitmap bitmap, GInfo gInfo, int pct) {
        int scale = (gInfo.blockInSize + gInfo.blockFlyingGap*2); // 120 %
        Bitmap bigMap = Bitmap.createScaledBitmap(bitmap, scale, scale,false);
        Canvas canvas = new Canvas(bigMap);
        int fScale = scale * pct / 100;
        Bitmap flyMap = Bitmap.createScaledBitmap(bitmap, fScale, fScale,false);
        canvas.drawBitmap(flyMap,(scale-fScale)/2f , (scale-fScale)/2f, null);
        return flyMap;
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

    public void draw(Canvas canvas, int xPos, int yPos) {
        canvas.drawBitmap(bitmap, xPos, yPos, null);
    }
}