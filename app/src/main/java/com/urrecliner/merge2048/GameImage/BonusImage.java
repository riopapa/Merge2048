package com.urrecliner.merge2048.GameImage;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.urrecliner.merge2048.GInfo;
import com.urrecliner.merge2048.R;

public class BonusImage {

//  source optimized by chatGPT suggested   23/01/05

    final public Bitmap[] bonusMaps;
    final public int bonusMapLen;
    public BonusImage(GInfo gInfo, Context context) {
        // Use a TypedArray to get the resource IDs of the drawables
        final TypedArray orgMapId = context.getResources().obtainTypedArray(R.array.bonus_images);
        bonusMapLen = orgMapId.length();
        bonusMaps = new Bitmap[bonusMapLen];
        int blockSize = gInfo.blockOutSize * 2;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        for (int i = 0; i < bonusMapLen; i++) {
            // Use the inSampleSize option when decoding the bitmaps to reduce the memory footprint
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), orgMapId.getResourceId(i, -1), options);
            // Use a Bitmap.Config enum to specify the desired bitmap configuration
            bonusMaps[i] = Bitmap.createScaledBitmap(bitmap, blockSize, blockSize, false);
        }
    }
}