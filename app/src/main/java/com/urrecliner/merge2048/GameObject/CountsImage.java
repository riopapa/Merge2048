package com.urrecliner.merge2048.GameObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.urrecliner.merge2048.GameInfo;
import com.urrecliner.merge2048.R;

public class CountsImage {

    final public Bitmap [] countMaps;
    final public int countMapLen;
    public CountsImage(GameInfo gameInfo, Context context) {

        int [] orgMapId = {
            R.drawable.i_x100, R.drawable.i_x101, R.drawable.i_x102, R.drawable.i_x103,
                R.drawable.i_x104, R.drawable.i_x105, R.drawable.i_x106,R.drawable.i_x107,
                R.drawable.i_x109, R.drawable.i_x110, R.drawable.i_x111,R.drawable.i_x112,
                R.drawable.i_x113, R.drawable.i_x114
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