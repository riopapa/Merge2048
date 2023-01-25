package com.urrecliner.merge2048.GameImage;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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
        flyMaps = new ExplodeImage().makeFlys(bitmap, gInfo);

        xorMap = new XorImage().xor(bitmap, gInfo);

        explodeMaps = new ExplodeImage().makeExplodes(bitmap, gInfo, context);

        destroyMap = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(context.getResources(), R.drawable.destroy, options),
                gInfo.blockOutSize+gInfo.explodeGap+gInfo.explodeGap, gInfo.blockOutSize+gInfo.explodeGap+gInfo.explodeGap, false);
        destroyMap = new ExplodeImage().mixExplode(bitmap, gInfo.explodeGap, destroyMap);
    }

}