package com.example.rmn.date.utiles;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Switch;
import android.widget.Toast;

import com.example.rmn.date.Home;
import com.example.rmn.date.Main2Activity;
import com.example.rmn.date.R;
import com.example.rmn.date.data.DBcontract;
import com.example.rmn.date.data.DBcontract.monDB;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by rmn on 10-08-2016.
 */
public class service extends IntentService {

    int period = 0, i = -1;
    Long subId, masterId;
    long timeSet = 0;
    String sub = "rmn";
    String dayString;
    String dateString;
    public static final String CREATE="create";
    IntentFilter intentFilter;
     long subid;
    public service() {
        super("hii");
       // intentFilter.addAction(CREATE);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.e("Service","i'mcalled");

          Cursor cursor=getApplication().getContentResolver().query(DBcontract.SubMasterTable.CONTENT_URI,null,null,null,null);
          if (cursor!=null)
             // cursor.moveToFirst();
            if (cursor.getCount()>0)
          while (cursor.moveToNext()) {
              Log.e("service",cursor.getColumnCount()+""+cursor.getCount());

              timeSet = cursor.getLong(cursor.getColumnIndex(DBcontract.DBmaster.COLUMN_TIME));
              sub = cursor.getString(cursor.getColumnIndex(DBcontract.SubTable.COLUMN_SUB));
              period = cursor.getInt(cursor.getColumnIndex(DBcontract.DBmaster.COLUMN_PERIOD));
              subid = cursor.getLong(cursor.getColumnIndex(DBcontract.SubTable.COLUMN_ID));
              dayString = cursor.getString(cursor.getColumnIndex(DBcontract.DBmaster.COLUMN_DAY));
              String apm = cursor.getString(cursor.getColumnIndex(DBcontract.DBmaster.COLUMN_DAY));

              Intent intent1 = new Intent(this, Receiver.class);
              intent1.putExtra(Constants.EXTRA_SUB, sub);
              intent1.putExtra(Constants.EXTRA_SUBID, subid);
              intent1.putExtra(Constants.EXTRA_TIME, timeSet);
              intent1.putExtra(Constants.EXTRA_PERIOD, period);
              intent1.putExtra(Constants.EXTRA_DAY, dayString);

              PendingIntent notifyIntent = PendingIntent
                      .getBroadcast(this, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

              Calendar calendar1 = Calendar.getInstance();
              calendar1.setTimeInMillis(timeSet);
              Log.e("calendar.get(HOUR)", calendar1.get(Calendar.HOUR) + "");
              Log.e("calendar.get(HOUR)", calendar1.get(Calendar.MINUTE) + "");

              Calendar c = Calendar.getInstance();
              c.set(Calendar.DAY_OF_WEEK,getCurrentDay(dayString));
              c.set(Calendar.HOUR, calendar1.get(Calendar.HOUR));
              c.set(Calendar.MINUTE, calendar1.get(Calendar.MINUTE) - 5);
              if (apm.equals("PM"))
                  c.set(Calendar.AM_PM, 1);
              else c.set(Calendar.AM_PM, 0);
Log.e("service","day"+c.get(Calendar.DAY_OF_WEEK));
              AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
              alarmManager
                      .set(AlarmManager.RTC_WAKEUP,
                              c.getTimeInMillis(),
                              notifyIntent);
//




          }
        cursor.close();
    }

    int getCurrentDay(String day){
        Log.e("SERVICES","day:->"+day);
        switch (day){
            case "Sun" : return Calendar.SUNDAY;
            case "Mon" : return Calendar.MONDAY;
            case "Tue" : return Calendar.TUESDAY;
            case "Wed" : return Calendar.WEDNESDAY;
            case "Thu" : return Calendar.THURSDAY;
            case "Sat" : return Calendar.SATURDAY;
            case "Fri" : return Calendar.FRIDAY;
            default: return -1;
        }

    }

}
