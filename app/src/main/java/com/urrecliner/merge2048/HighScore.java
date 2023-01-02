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
        }
    }

    void put() {
        Gson gson = new Gson();
        String json = gson.toJson(gInfo.highMembers);
        sharedEditor.putString(highMember, json);
        sharedEditor.apply();
    }
}