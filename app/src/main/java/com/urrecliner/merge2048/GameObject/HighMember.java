package com.urrecliner.merge2048.GameObject;

public class HighMember {

    public long score;
    public long when;
    public String who;

    public HighMember(long score, String who) {
        this.score = score;
        this.who = who;
        this.when = System.currentTimeMillis();
    }
    public HighMember(long score, String who, long time) {  // if highMembers empty
        this.score = score;
        this.who = who;
        this.when = time;
    }

    public long getScore() {return score;}  // for descending sort
}