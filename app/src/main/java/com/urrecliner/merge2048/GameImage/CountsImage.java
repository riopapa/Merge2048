package com.urrecliner.merge2048.GameImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.urrecliner.merge2048.GameInfo;
import com.urrecliner.merge2048.R;

public class CountsImage {

    final public Bitmap [] countMaps;
    final public int countMapLen;
    public CountsImage(GameInfo gameInfo, Context context) {

        int [] orgMapId = {
            R.drawable.z100, R.drawable.z101, R.drawable.z102, R.drawable.z103,
                R.drawable.z104, R.drawable.z105, R.drawable.z106,R.drawable.z107,
                R.drawable.z109, R.drawable.z110, R.drawable.z111,R.drawable.z112,
                R.drawable.z113, R.drawable.z114
        };

        countMapLen = orgMapId.length;
        countMaps = new Bitmap[countMapLen];
        int blockSize = gameInfo.blockOutSize * 2;
        for (int i = 0; i < countMapLen ; i++) {
            Bitmap bitmap = Bitmap.createScaledBitmap(
                    BitmapFactory.decodeResource(context.getResources(), orgMapId[i]),
                    gameInfo.blockInSize, gameInfo.blockInSize, false);
            countMaps[i] = Bitmap.createScaledBitmap(bitmap,
                    blockSize, blockSize,false);
        }
    }
}