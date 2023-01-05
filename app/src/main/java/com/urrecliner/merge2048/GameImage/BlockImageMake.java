package com.urrecliner.merge2048.GameImage;

import android.content.Context;

import com.urrecliner.merge2048.GInfo;

import java.util.ArrayList;
import java.util.List;

public class BlockImageMake {

    public List<BlockImage> make(Context context, GInfo gInfo) {

        List<BlockImage> blockImages = new ArrayList<>();
        final int[] nbrValues = {0, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768, 65536, 131072};
        int[] range = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17};

        for (int i : range) {
            blockImages.add(new BlockImage(i, nbrValues[i], gInfo, context));
        }
        return blockImages;
    }
}

//public class BlockImageMake {
//
//    public List<BlockImage> make(Context context, GInfo gInfo) {
//
//        List<BlockImage> blockImages = new ArrayList<>();
//        int nbr = 0;
//        for (int i = 0 ; i < 18; i++) {
//            blockImages.add(new BlockImage(i, nbr, gInfo, context));
//            if (nbr == 0)
//                nbr = 1;
//            nbr += nbr;
//        }
//        return blockImages;
//    }
//}