package com.example.rmn.date;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.os.Handler;
import android.widget.Toast;

import com.example.rmn.date.data.DBcontract;


public class Splash extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                 Intent intent = new Intent(Splash.this, MainActivity.class);
                 startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);

    }
}
