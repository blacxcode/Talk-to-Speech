package com.blacxcode.tts.activity;

import android.os.Bundle;
import android.os.Handler;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.blacxcode.tts.R;

/**
 * Created by blacXcode on 10/27/2016.
 */

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 5000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);

                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
