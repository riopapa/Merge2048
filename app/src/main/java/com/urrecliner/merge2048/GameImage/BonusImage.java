package com.urrecliner.merge2048.GameImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.urrecliner.merge2048.GInfo;
import com.urrecliner.merge2048.R;

public class BonusImage {

    final public Bitmap [] bonusMaps;
    final public int bonusMapLen;
    public BonusImage(GInfo gInfo, Context context) {

        int [] orgMapId = {
            R.drawable.z100, R.drawable.z101, R.drawable.z102, R.drawable.z103,
                R.drawable.z104, R.drawable.z105, R.drawable.z106,R.drawable.z107,
                R.drawable.z109, R.drawable.z110, R.drawable.z111
        };

        bonusMapLen = orgMapId.length;
        bonusMaps = new Bitmap[bonusMapLen];
        int blockSize = gInfo.blockOutSize * 2;
        for (int i = 0; i < bonusMapLen; i++) {
            Bitmap bitmap = Bitmap.createScaledBitmap(
                    BitmapFactory.decodeResource(context.getResources(), orgMapId[i]),
                    gInfo.blockInSize, gInfo.blockInSize, false);
            bonusMaps[i] = Bitmap.createScaledBitmap(bitmap,
                    blockSize, blockSize,false);
        }
    }
}