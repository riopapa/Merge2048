package com.urrecliner.merge2048.GameImage;

import android.graphics.Bitmap;

import com.urrecliner.merge2048.GInfo;

public class XorImage {
    public Bitmap xor(Bitmap bitmap, GInfo gInfo) {
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
}