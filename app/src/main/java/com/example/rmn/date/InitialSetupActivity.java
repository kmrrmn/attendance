package com.example.rmn.date;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.sql.Time;

public class InitialSetupActivity extends AppCompatActivity {

    TextInputEditText timeEdit,timeIntervalView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_setup);
        timeEdit=(TextInputEditText) findViewById(R.id.time);
        timeIntervalView=(TextInputEditText) findViewById(R.id.time_interval);
    }

    public void submit(View view){
        String time=timeEdit.getText().toString();
        String timeInterval=timeIntervalView.getText().toString();

        Log.e("time",time);
        Log.e("timeInetval",timeInterval);
//
//        SharedPreferences preferences=getSharedPreferences("time", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor=preferences.edit();
////        Time time1=new Time(time);
//        editor.putString("clgTime",);
    }
}
