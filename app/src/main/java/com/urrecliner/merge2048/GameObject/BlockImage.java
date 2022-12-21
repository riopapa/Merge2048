package com.urrecliner.merge2048.GameObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.urrecliner.merge2048.GameInfo;
import com.urrecliner.merge2048.R;

public class BlockImage {
    public int idx;
    public int number;

    public Bitmap bitmap;
    public Bitmap [] smallMaps;
    public Bitmap halfMap;


    public BlockImage(int idx, int number, int color, GameInfo gameInfo, Context context) {
        this.idx = idx;
        this.number = number;
        int [] orgMapId = {
            R.drawable.i0_0, R.drawable.i1_2, R.drawable.i2_4, R.drawable.i3_8, R.drawable.i4_16,
            R.drawable.i5_32, R.drawable.i6_64,R.drawable.i7_128, R.drawable.i8_256,
            R.drawable.i9_512, R.drawable.i10_1024,R.drawable.i11_2048, R.drawable.i12_4096,
            R.drawable.i13_8192, R.drawable.i14_16384,R.drawable.i15_32768, R.drawable.i16_65536,
            R.drawable.i17_131072, R.drawable.i18_262144,R.drawable.i19_524288,R.drawable.i19_524288
        };
        bitmap = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(context.getResources(), orgMapId[idx]),
                gameInfo.blockInSize, gameInfo.blockInSize, false);
        smallMaps = new Bitmap[5];
        smallMaps[0] = bitmapSmall(bitmap, gameInfo, 85);
        smallMaps[1] = bitmapSmall(bitmap, gameInfo, 70);
        smallMaps[2] = bitmapSmall(bitmap, gameInfo, 70);
        smallMaps[3] = bitmapSmall(bitmap, gameInfo, 85);
        smallMaps[4] = bitmapSmall(bitmap, gameInfo, 90);

        halfMap = Bitmap.createScaledBitmap(bitmap,
                gameInfo.blockInSize /2, gameInfo.blockInSize /2,false);
    }

    private Bitmap bitmapSmall(Bitmap bitmap, GameInfo gameInfo, int pct) {
        int xScale = gameInfo.blockInSize * pct / 100;
        int yScale = gameInfo.blockInSize * pct / 100;
        Bitmap newMap = Bitmap.createScaledBitmap(bitmap, xScale, yScale,false);
        Bitmap smallMap = Bitmap.createBitmap(gameInfo.blockInSize, gameInfo.blockInSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(smallMap);
        canvas.drawBitmap(newMap, (gameInfo.blockInSize - xScale)/2f,
                (gameInfo.blockInSize - yScale)/2f, null);
        return smallMap;
    }

    public void draw(Canvas canvas, int xPos, int yPos) {
        canvas.drawBitmap(bitmap, xPos, yPos, null);
    }
}