package com.urrecliner.merge2048.GameObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import androidx.core.content.res.ResourcesCompat;

import com.urrecliner.merge2048.GameInfo;
import com.urrecliner.merge2048.R;

public class BlockImage {
    private final int idx;
    private final int number;

    public Bitmap bitmap;

    public Bitmap [] smallMaps;


    public BlockImage(int idx, int number, int color, GameInfo gameInfo, Context context) {
        this.idx = idx;
        this.number = number;

        bitmap = Bitmap.createBitmap(gameInfo.xBlockInSize, gameInfo.yBlockInSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRoundRect(0,0, gameInfo.xBlockInSize, gameInfo.yBlockInSize, gameInfo.xBlockInSize/8,gameInfo.yBlockInSize/8, paint);

        if (idx == 0)
            return;
        String s = " "+number+" ";
        int txtSize = 120;
        Rect rect = new Rect();
        Paint tPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        tPaint.setTypeface(ResourcesCompat.getFont(context, R.font.campus_a));
        tPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        tPaint.setTextSize(txtSize);
        tPaint.setStrokeWidth(0);
        tPaint.setTextAlign(Paint.Align.CENTER);
        tPaint.setColor(Color.BLACK);
        tPaint.getTextBounds(s, 0, s.length(), rect);
        int width = rect.width();
        while ((width - 24) > gameInfo.xBlockInSize) {
            txtSize = txtSize * 8/10;
            tPaint.setTextSize(txtSize);
            tPaint.getTextBounds(s, 0, s.length(), rect);
            width = rect.width();
        }
        tPaint.setTextSize(txtSize);
        int xOffset = gameInfo.xBlockOutSize/2-16;
        int yOffset = gameInfo.yBlockOutSize/2 + rect.height()/2-16;
        canvas.drawText(s, (float) xOffset, (float) yOffset, tPaint);

        smallMaps = new Bitmap[5];
        smallMaps[0] = bitmapSmall(bitmap, gameInfo, 85);
        smallMaps[1] = bitmapSmall(bitmap, gameInfo, 70);
        smallMaps[2] = bitmapSmall(bitmap, gameInfo, 70);
        smallMaps[3] = bitmapSmall(bitmap, gameInfo, 85);
        smallMaps[4] = bitmapSmall(bitmap, gameInfo, 90);


//        tPaint.setColor(Color.BLACK);
//        tPaint.setStrokeWidth(0);
//        canvas.drawText(s, (float) xOffset, (float) yOffset, tPaint);
    }

    private Bitmap bitmapSmall(Bitmap bitmap, GameInfo gameInfo, int pct) {
        int xScale = gameInfo.xBlockInSize * pct / 100;
        int yScale = gameInfo.yBlockInSize * pct / 100;
        Bitmap newMap = Bitmap.createScaledBitmap(bitmap, xScale, yScale,false);
        Bitmap smallMap = Bitmap.createBitmap(gameInfo.xBlockInSize, gameInfo.yBlockInSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(smallMap);
        canvas.drawBitmap(newMap, (gameInfo.xBlockInSize - xScale)/2f,
                (gameInfo.yBlockInSize - yScale)/2f, null);
        return smallMap;
    }

    public void draw(Canvas canvas, int xPos, int yPos) {
        canvas.drawBitmap(bitmap, xPos, yPos, null);
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}