package com.urrecliner.merge2048.GameImage;

import android.content.Context;

import com.urrecliner.merge2048.GInfo;

import java.util.ArrayList;
import java.util.List;

public class BlockImageMake {

    public List<BlockImage> make(Context context, GInfo gInfo) {

        List<BlockImage> blockImages = new ArrayList<>();
        int nbr = 0;
        for (int i = 0 ; i < 18; i++) {
            blockImages.add(new BlockImage(i, nbr, gInfo, context));
            if (nbr == 0)
                nbr = 1;
            nbr += nbr;
        }
        return blockImages;
    }

}