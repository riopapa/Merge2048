package com.urrecliner.merge2048.Sub;

import android.content.Context;

import com.urrecliner.merge2048.GInfo;
import com.urrecliner.merge2048.GameImage.BlockImage;

import java.util.ArrayList;
import java.util.List;

public class MakeBlockImage {

    public List<BlockImage> make(Context context, GInfo gInfo) {

        List<BlockImage> blockImages = new ArrayList<>();
        final int[] nbrValues = {0, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768, 65536, 131072};

        for (int i = 0; i < 18; i++) {
            blockImages.add(new BlockImage(i, nbrValues[i], gInfo, context));
        }
        return blockImages;
    }
}