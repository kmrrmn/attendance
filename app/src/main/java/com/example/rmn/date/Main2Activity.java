package com.example.rmn.date;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentContainer;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.rmn.date.data.DBcontract;
import com.example.rmn.date.utiles.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Main2Activity extends AppCompatActivity implements CriteriaDialog.EditNameDialogListener,add_sub.ExtraSubAddCallback {
    String extra_day,extra_date;
    add_sub addAlert;
     public static String date;
    static int[] start_day = {-10, -10, -10};
    Fragment fragment;
    CharSequence title;
    FragmentManager fm;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        String day_week = null;
        date=null;
        Intent intent = getIntent();

        Log.e("MAIN2ACTIVITY","Win--->////"+intent.getAction() );

        day_week = intent.getStringExtra(Constants.EXTRA_DAY);
        date = intent.getStringExtra(Constants.EXTRA_DATE);
        start_day[1] = intent.getIntExtra(Constants.EXTRA_MONTH, 0);
        start_day[2] = intent.getIntExtra(Constants.EXTRA_YEAR, 0);
        title = "DAYS";

        getDay(day_week);
        extra_date=date;

        Log.e("MAIN2ACTIVITY","WEEK____DAY--->////"+day_week);
        Log.e("MAIN2ACTIVITY","WEEK____DAte--->////"+date);

        Monday fragment = Monday.newInstance(date, day_week);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, fragment)
                .commit();
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setSubtitle(date);
       date=null;
     }

    public void getDay(String day_week) {
        if (day_week == null)
            day_week = "Mon";

        fragment = new Monday();

        switch (day_week) {
            case "Mon":
                title = "MONDAY";
                extra_day = "Mon";
                break;
            case "Tue":
                title = "TUESDAY";
                extra_day = "Tue";
                break;
            case "Wed":
                title = "WEDNESDAY";
                extra_day = "Wed";
                break;
            case "Thu":
                title = "THURSDAY";
                extra_day = "Thu";
                break;
            case "Fri":
                title = "FRIDAY";
                extra_day = "Fri";
                break;
            case "Sat":
                title = "SATURDAY";
                extra_day = "Sat";
                break;
            case "Sun":
                title = "SUNDAY";
                extra_day = "Sun";
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add) {
            addAlert =   add_sub.NewInstance(this,extra_day,extra_date);
//            Bundle bundle = new Bundle();
//            bundle.putString("day", extra_day);
//            bundle.putString("date",extra_date);
//            addAlert.setArguments(bundle);
            android.app.FragmentManager manager = ((this).getFragmentManager());
            addAlert.show(manager, "rmnkmr");
            Toast.makeText(this, "add", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFinishEditDialog(String inputText, int pos) {
        fragment = new Monday();
        Bundle bundle = new Bundle();
        bundle.putInt("pos", pos);
        fragment.setArguments(bundle);
        fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.container, fragment);
        ft.commit();
    }

    @Override
    public void ExtraSubAdd() {
        Log.e("extra_ day  ","--------------------------------------------------------------------------"+extra_day);
        Log.e("extra_ date  ",extra_date);
        Monday fragment = Monday.newInstance(extra_date, extra_day);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, fragment)
                .commit();
    }
}
