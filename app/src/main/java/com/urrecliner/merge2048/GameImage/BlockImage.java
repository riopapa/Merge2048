package com.urrecliner.merge2048.GameImage;

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

    public BlockImage(int idx, int number, GameInfo gameInfo, Context context) {
        this.idx = idx;
        this.number = number;
        int [] orgMapId = {
            R.drawable.i00_0, R.drawable.i01_2, R.drawable.i02_4, R.drawable.i03_8,
                R.drawable.i04_16, R.drawable.i05_32, R.drawable.i06_64,R.drawable.i07_128,
                R.drawable.i08_256, R.drawable.i09_512, R.drawable.i10_1024,R.drawable.i11_2048,
                R.drawable.i12_4096, R.drawable.i13_8192, R.drawable.i14_16384,R.drawable.i15_32768,
                R.drawable.i16_65536, R.drawable.i17_131072, R.drawable.i18_262144,
                R.drawable.i19_524288
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