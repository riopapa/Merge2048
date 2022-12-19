package com.urrecliner.merge2048.GameObject;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.urrecliner.merge2048.GameInfo;
import com.urrecliner.merge2048.R;

import java.util.ArrayList;
import java.util.List;

public class MakeBlockImage {

    public List<BlockImage> make(Context context, GameInfo gameInfo) {
        int [] colors = {
                ContextCompat.getColor(context, R.color.c00000),    // 0    blank
                ContextCompat.getColor(context, R.color.c00002),    // 1    2
                ContextCompat.getColor(context, R.color.c00004),    // 2    4
                ContextCompat.getColor(context, R.color.c00008),    // 3    8
                ContextCompat.getColor(context, R.color.c00016),    // 4    16
                ContextCompat.getColor(context, R.color.c00032),    // 5    32
                ContextCompat.getColor(context, R.color.c00064),    // 6    64
                ContextCompat.getColor(context, R.color.c00128),    // 7    128
                ContextCompat.getColor(context, R.color.c00256),    // 8    256
                ContextCompat.getColor(context, R.color.c00512),    // 9    512
                ContextCompat.getColor(context, R.color.c01024),    // 10   1024
                ContextCompat.getColor(context, R.color.c02048),    // 11   2048
                ContextCompat.getColor(context, R.color.c04096),    // 12   4096
                ContextCompat.getColor(context, R.color.c08192),    // 13   8192
                ContextCompat.getColor(context, R.color.c16384),    // 14   16384, just in case
                ContextCompat.getColor(context, R.color.c32768),    // 15   32768
                ContextCompat.getColor(context, R.color.c32768),    // 16   65536
                ContextCompat.getColor(context, R.color.c32768),    // 17   65536
                ContextCompat.getColor(context, R.color.c32768),    // 18   65536
                ContextCompat.getColor(context, R.color.c32768),    // 19   65536
                ContextCompat.getColor(context, R.color.c32768),    // 20   65536
                0x000000
        };
        List<BlockImage> blockImages = new ArrayList<>();
        int nbr = 0;
        for (int i = 0 ; i < 20; i++) {
            blockImages.add(new BlockImage(i, nbr, colors[i], gameInfo, context));
            if (nbr == 0)
                nbr = 1;
            nbr += nbr;
        }
        return blockImages;
    }

}