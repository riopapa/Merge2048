package com.urrecliner.merge2048;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.urrecliner.merge2048.GameObject.HighMember;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HighScore {
    final GInfo gInfo;
    final Context context;
    final SharedPreferences sharedPref;
    final SharedPreferences.Editor sharedEditor;
    final String highMember = "highMember";

    public HighScore(GInfo gInfo, Context context) {
        this.gInfo = gInfo;
        this.context = context;
        sharedPref = context.getSharedPreferences("merge2048", MODE_PRIVATE);
        sharedEditor = sharedPref.edit();
    }

    void get() {
        gInfo.highMembers = new ArrayList<>();
        Gson gson = new Gson();
        String json = sharedPref.getString(highMember, "");
        if (!json.isEmpty()) {
            Type type = new TypeToken<List<HighMember>>() {
            }.getType();
            gInfo.highMembers = gson.fromJson(json, type);
        } else {
            gInfo.highMembers = new ArrayList<>();
            gInfo.highMembers.add(new HighMember(88888, "riopapa",
                    System.currentTimeMillis()-240000));
            gInfo.highMembers.add(new HighMember(44444, "riopapa",
                    System.currentTimeMillis()-120000));
            gInfo.highMembers.add(new HighMember(22222, "riopapa",
                    System.currentTimeMillis()-60000));
        }

    }

    final String highHeart = "♥";

    void put() {
        for (int i = 0; i < gInfo.highMembers.size(); i++) {
            HighMember hm = gInfo.highMembers.get(i);
            if (hm.who.equals(highHeart)) {
                hm.who = "Me";
                gInfo.highMembers.set(i, hm);
            }
        }
        Gson gson = new Gson();
        String json = gson.toJson(gInfo.highMembers);
        sharedEditor.putString(highMember, json);
        sharedEditor.apply();
    }
}