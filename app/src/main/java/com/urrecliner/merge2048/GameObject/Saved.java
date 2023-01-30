package com.urrecliner.merge2048.GameObject;

import com.urrecliner.merge2048.GInfo;

import java.util.List;

public class Saved {

    public String cell;
    public Cell[][] cells;
    public int next, nextNext;
    public long score;

    public Saved(String cell, int next, int nextNext, long score) {
        this.cell = cell;
        this.next = next;
        this.nextNext = nextNext;
        this.score = score;
    }
}