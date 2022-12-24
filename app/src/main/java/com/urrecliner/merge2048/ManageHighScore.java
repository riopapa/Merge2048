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

public class ManageHighScore {
    GameInfo gameInfo;
    Context context;
    SharedPreferences sharedPref;
    SharedPreferences.Editor sharedEditor;
    final String highMember = "highMember";

    public ManageHighScore(GameInfo gameInfo, Context context) {
        this.gameInfo = gameInfo;
        this.context = context;
        sharedPref = context.getSharedPreferences("merge2048", MODE_PRIVATE);
        sharedEditor = sharedPref.edit();

    }

    void get() {
        gameInfo.highMembers = new ArrayList<>();
        Gson gson = new Gson();
        String json = sharedPref.getString("highMember", "");
        if (!json.isEmpty()) {
            Type type = new TypeToken<List<HighMember>>() {
            }.getType();
            gameInfo.highMembers = gson.fromJson(json, type);
        }
    }

    void put() {
        Gson gson = new Gson();
        String json = gson.toJson(gameInfo.highMembers);
        sharedEditor.putString("highMember", json);
        sharedEditor.apply();

    }



}