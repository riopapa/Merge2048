package com.urrecliner.merge2048;

public class PowerIndex {
    final int[] nbrValues = {0, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768, 65536, 131072};
    public int power(int index) {
        return nbrValues[index];
    }
}