package com.urrecliner.merge2048.GameObject;

import android.content.Context;
import com.urrecliner.merge2048.GameInfo;

import java.util.ArrayList;
import java.util.List;

public class MakeBlockImage {

    public List<BlockImage> make(Context context, GameInfo gameInfo) {

        List<BlockImage> blockImages = new ArrayList<>();
        int nbr = 0;
        for (int i = 0 ; i < 20; i++) {
            blockImages.add(new BlockImage(i, nbr, gameInfo, context));
            if (nbr == 0)
                nbr = 1;
            nbr += nbr;
        }
        return blockImages;
    }

}