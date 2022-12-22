package com.urrecliner.merge2048.GameObject;

public class HighMember {

    public long score;
    public long when;
    public String who;

    public HighMember(long score, String who) {
        this.score = score;
        this.when = System.currentTimeMillis();
        this.who = who;
    }

    public long getScore() {return score;}
}