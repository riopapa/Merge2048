package com.urrecliner.merge2048;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.urrecliner.merge2048.GameObject.HighMember;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/*
 *  HighScore table has 4 items but show only three
 */
public class HighScore {
    final GInfo gInfo;
    final SharedPreferences sharedPref;
    final SharedPreferences.Editor sharedEditor;
    final String highMember = "highMember";

    public HighScore(GInfo gInfo, Context context) {
        this.gInfo = gInfo;
        sharedPref = context.getSharedPreferences("merge2048", MODE_PRIVATE);
        sharedEditor = sharedPref.edit();
    }

    public void get() {
        gInfo.highMembers = new ArrayList<>();
        Gson gson = new Gson();
        String json = sharedPref.getString(highMember+gInfo.X_BLOCK_CNT + gInfo.Y_BLOCK_CNT, "");
        if (!json.isEmpty()) {
            Type type = new TypeToken<List<HighMember>>() {
            }.getType();
            gInfo.highMembers = gson.fromJson(json, type);
        } else {
            resetHigh();
        }
        for (int i = 0; i < gInfo.highMembers.size(); i++) {
            HighMember hm = gInfo.highMembers.get(i);
            if (hm.who.equals(highHeart)) {
                hm.who = "riopapa";
                gInfo.highMembers.set(i, hm);
                break;
            }
        }
        if (gInfo.highMembers.size() < 4)
            gInfo.highMembers.add(new HighMember(1111, "riopapa",
                    System.currentTimeMillis()));
        gInfo.userName = sharedPref.getString("userName","Me");

    }

    final String highHeart = "♥♥";

    public void put() {

        for (int i = 0; i < gInfo.highMembers.size(); i++) {
            HighMember hm = gInfo.highMembers.get(i);
            if (hm.who.equals(highHeart)) {
                hm.who = gInfo.userName;
                gInfo.highMembers.set(i, hm);
                break;
            }
        }
        Gson gson = new Gson();
        String json = gson.toJson(gInfo.highMembers);
        sharedEditor.putString(highMember+gInfo.X_BLOCK_CNT +gInfo.Y_BLOCK_CNT, json);
        sharedEditor.putString("userName", gInfo.userName);
        sharedEditor.apply();
    }

    void undoScore() {
        for (int i = 0; i < gInfo.highMembers.size(); i++) {
            HighMember hm = gInfo.highMembers.get(i);
            if (hm.who.equals(highHeart)) {
                hm.score = gInfo.scoreNow;
            }
        }
        gInfo.highLowScore = gInfo.highMembers.get(gInfo.highMembers.size()-1).score;
        gInfo.highMembers.sort(Comparator.comparingLong(HighMember::getScore).reversed());
    }

    void resetHigh() {
        gInfo.highMembers = new ArrayList<>();
        gInfo.highMembers.add(new HighMember(888888, "riopapa",
                System.currentTimeMillis()-960000));
        gInfo.highMembers.add(new HighMember( 44444, "riopapa",
                System.currentTimeMillis()-480000));
        gInfo.highMembers.add(new HighMember(2222, "riopapa",
                System.currentTimeMillis()-120000));
        gInfo.highMembers.add(new HighMember(1111, "riopapa",
                System.currentTimeMillis()));
        gInfo.highLowScore = gInfo.highMembers.get(gInfo.highMembers.size()-1).score;
    }
}