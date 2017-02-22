package com.example.rmn.date;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.rmn.date.data.DBcontract;
import com.example.rmn.date.utiles.Constants;
import com.example.rmn.date.utiles.Receiver;
import com.example.rmn.date.utiles.service;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements CriteriaDialog.EditNameDialogListener {
    public String token = "a";
    DrawerLayout drawer;
    ActionBarDrawerToggle drawerToggle;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    DrawerAdapter adapter;
    FloatingActionButton fab;
    FragmentManager.BackStackEntry entry;
    FragmentManager fm;
    String Titles[] = {"Home", "Subjects", "Attendance", "Settings"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.context_menu);
        toolbar.setCollapsible(true);

      //  CallBroadcast();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_main);

        recyclerView.setHasFixedSize(true);

        adapter = new DrawerAdapter(Titles);
        recyclerView.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        drawer = (DrawerLayout) findViewById(R.id.drawer);

        drawerToggle = new ActionBarDrawerToggle(this, drawer, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawer.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
//
//        fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setVisibility(View.VISIBLE);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), Add.class);
//                startActivity(intent);
//            }
//        });
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawer.setDrawerListener(drawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setHomeButtonEnabled(true);

        if (savedInstanceState == null) {
            fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.container, new Home());
            setTitle("Home");
            ft.commit();
        }

        final GestureDetector gestureDetector = new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

        });

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

                View child = rv.findChildViewUnder(e.getX(), e.getY());

                fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();

                if (child != null && gestureDetector.onTouchEvent(e)) {

                    int position = recyclerView.getChildAdapterPosition(child);

                    Fragment fragment = null;
                    switch (position) {

                        case 0:
                            fragment = new Home();
                            token = "h";
                            ft.replace(R.id.container, fragment);
                            ft.addToBackStack(token);
                            break;
                        case 1:
                            fragment = new Subject_Detail();
                            Bundle bundle = new Bundle();
                            bundle.putInt("pos", 0);
                            fragment.setArguments(bundle);
                            startService(new Intent(MainActivity.this,service.class));
                            ft.replace(R.id.container, fragment);
                            ft.addToBackStack(null);
                            break;
                        case 2:
                            fragment = new TotalPercent();
                            ft.replace(R.id.container, fragment);
                            ft.addToBackStack(null);

                            break;
                        case 3:
                            fragment = new Settings();
                            ft.replace(R.id.container, fragment);
                            ft.addToBackStack(null);

                        default:
                            break;
                    }

                    ft.replace(R.id.container, fragment);
                    ft.addToBackStack(token);
                    ft.commit();
                    setTitle(Titles[position]);
                    drawer.closeDrawers();
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        // Activate the navigation drawer toggle
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        fm = getSupportFragmentManager();
        int count = fm.getBackStackEntryCount();
        if (count >= 1) {
            fm.popBackStack(fm.getBackStackEntryAt(0).getName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        setTitle(Titles[0]);
    }

    @Override
    public void onFinishEditDialog(String inputText, int pos) {

    }

   /* public void CallBroadcast() {


        int period = 0, i = -1;

        Long subId, masterId;
        long timeSet = 0, timrseExtra,subid;
        String sub = "rmn";
        String dayString;
        String dateString;

        SharedPreferences preferences = getSharedPreferences(Constants.PREF_FILE, Context.MODE_PRIVATE);
        Map map = preferences.getAll();

        int k = 0;

        Calendar calendar = Calendar.getInstance();

        String selection = DBcontract.DBmaster.COLUMN_DAY + " =? AND "
                + DBcontract.DBmaster.COLUMN_IS_EXTRA + "=?";

        Date date = calendar.getTime();

        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
        dateString = dateFormat1.format(date);

        SimpleDateFormat dateFormat2 = new SimpleDateFormat("EEE");
        dayString = dateFormat2.format(date);
        String selectionArgs[] = {dayString, "no"};

        Cursor cursor = this.getContentResolver().query(DBcontract.SubMasterTable.CONTENT_URI,
                null, selection, selectionArgs, null);

        if (cursor != null) {
            if (cursor.getCount() != 0) {
                while (cursor.moveToNext()) {
                    timeSet = cursor.getLong(cursor.getColumnIndex(DBcontract.DBmaster.COLUMN_TIME));
                    sub = cursor.getString(cursor.getColumnIndex(DBcontract.SubTable.COLUMN_SUB));
                    period = cursor.getInt(cursor.getColumnIndex(DBcontract.DBmaster.COLUMN_PERIOD));
                    subid=cursor.getLong(cursor.getColumnIndex(DBcontract.SubTable.COLUMN_ID));
                    dayString=cursor.getString(cursor.getColumnIndex(DBcontract.DBmaster.COLUMN_DAY));
                    String apm=cursor.getString(cursor.getColumnIndex(DBcontract.DBmaster.COLUMN_DAY));

                    Intent intent1 = new Intent(this, Receiver.class);
                    intent1.putExtra(Constants.EXTRA_SUB, sub);
                    intent1.putExtra(Constants.EXTRA_SUBID, subid);
                    intent1.putExtra(Constants.EXTRA_TIME, timeSet);
                    intent1.putExtra(Constants.EXTRA_PERIOD, period);
                    intent1.putExtra(Constants.EXTRA_DAY, dayString);

                    PendingIntent notifyIntent = PendingIntent
                            .getBroadcast(this, k++, intent1, 0);

                    Calendar calendar1 = Calendar.getInstance();
                    calendar1.setTimeInMillis(timeSet);
                    Log.e("calendar.get(HOUR)", calendar1.get(Calendar.HOUR) + "");
                    Log.e("calendar.get(HOUR)", calendar1.get(Calendar.MINUTE) + "");

                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.HOUR, calendar1.get(Calendar.HOUR));
                    c.set(Calendar.MINUTE, calendar1.get(Calendar.MINUTE) - 5);
                    if (apm.equals("PM"))
                    c.set(Calendar.AM_PM,1);
                    else c.set(Calendar.AM_PM,0);

                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                     alarmManager
                            .set(AlarmManager.RTC_WAKEUP,
                                    c.getTimeInMillis(),
                                    notifyIntent);
                }
            }
        }

        cursor.close();


    }*/

}
