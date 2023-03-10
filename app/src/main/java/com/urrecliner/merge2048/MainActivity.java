package com.urrecliner.merge2048;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * MainActivity is the entry point to our application.
 */

public class MainActivity extends Activity {

    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        game = new Game(this);
        setContentView(game);
    }

//    @Override
//    protected void onStart() {
//        Log.w("MainActivity.java", "onStart()");
//        super.onStart();
//    }
//
    @Override
    protected void onResume() {
        Log.w("MainActivity.java", "onResume()");
        super.onResume();
        setContentView(game);
    }

    @Override
    protected void onPause() {
        Log.w("MainActivity.java", "onPause()");
        if (game.gInfo.quitGame) {
            finish();
            game = null;
            System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());
        } else
            game.pause();

        super.onPause();
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//    }

    @Override
    public void onBackPressed() {
            super.onBackPressed();
    }
}