package com.rasoftec.tpos2.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


import com.rasoftec.tpos.R;
import com.rasoftec.tpos2.login;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    static final long SCREEN_DELAY = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.ac);
        setContentView(R.layout.activity_splash);

        // Obligar la orientacion PORTRAIT
        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        try {
            getSupportActionBar().hide(); // Ocultar ActionBar
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Intent automatico
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Intent cambiar = new Intent(getApplicationContext(), login.class);
                startActivity(cambiar);
                finish();
            }
        }, SCREEN_DELAY);

    }
}
