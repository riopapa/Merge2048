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
    public Bitmap [] flyMaps;
    public Bitmap halfMap;

    public BlockImage(int idx, int number, GameInfo gameInfo, Context context) {
        this.idx = idx;
        this.number = number;
        int [] orgMapId = {
            R.drawable.i00_0, R.drawable.i01_2, R.drawable.i02_4, R.drawable.i03_8,
                R.drawable.i04_16, R.drawable.i05_32, R.drawable.i06_64,
                R.drawable.i07_128, R.drawable.i08_256, R.drawable.i09_512,
                R.drawable.i10_1024,R.drawable.i11_2048, R.drawable.i12_4096,
                R.drawable.i13_8192, R.drawable.i14_16384,R.drawable.i15_32768,
                R.drawable.i16_65536, R.drawable.i17_131072
        };
        bitmap = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(context.getResources(), orgMapId[idx]),
                gameInfo.blockInSize, gameInfo.blockInSize, false);
        flyMaps = new Bitmap[5];
        flyMaps[0] = makeFlyMap(bitmap, gameInfo, 110); // max 120%
        flyMaps[1] = makeFlyMap(bitmap, gameInfo, 120);
        flyMaps[2] = makeFlyMap(bitmap, gameInfo, 120);
        flyMaps[3] = makeFlyMap(bitmap, gameInfo, 110);
        flyMaps[4] = makeFlyMap(bitmap, gameInfo, 100);

        halfMap = Bitmap.createScaledBitmap(bitmap,
                gameInfo.blockInSize /2, gameInfo.blockInSize /2,false);
    }

    private Bitmap makeFlyMap(Bitmap bitmap, GameInfo gameInfo, int pct) {
        int scale = (gameInfo.blockInSize + gameInfo.blockFlyingGap*2); // 120 %
        Bitmap bigMap = Bitmap.createScaledBitmap(bitmap, scale, scale,false);
        Canvas canvas = new Canvas(bigMap);
        int fScale = scale * pct / 100;
        Bitmap flyMap = Bitmap.createScaledBitmap(bitmap, fScale, fScale,false);
        canvas.drawBitmap(flyMap,(scale-fScale)/2f , (scale-fScale)/2f, null);
        return flyMap;
    }

    public void draw(Canvas canvas, int xPos, int yPos) {
        canvas.drawBitmap(bitmap, xPos, yPos, null);
    }
}