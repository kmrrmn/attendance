package com.example.rmn.date.utiles;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.rmn.date.Home;
import com.example.rmn.date.Main2Activity;
import com.example.rmn.date.R;
import com.example.rmn.date.data.DBcontract;

import com.example.rmn.date.utiles.Constants;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by rmn on 09-08-2016.
 */
public class Receiver extends BroadcastReceiver {

    int period = 0, i = -1;
    Context mContext;
    Long subId, masterId;
    String sub = "rmn";
    String dayString;
    String dateString;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;

        i = 0;

        String sub = intent.getStringExtra(Constants.EXTRA_SUB);
        Long time = intent.getLongExtra(Constants.EXTRA_TIME, 0);
        int period = intent.getIntExtra(Constants.EXTRA_PERIOD, 0);
        subId=intent.getLongExtra(Constants.EXTRA_SUBID,0);
        dayString=intent.getStringExtra(Constants.EXTRA_DAY );

        Log.e("intent ", "daystring " + dayString);
        Log.e("intent ", "sub " + sub);
        Log.e("intent ", "time " + time);
        Log.e("intent ", "period " + period);
        Log.e("intent ", "siubid " + subId);
        Log.e("ricver ", " ---------------------------------------------------------------");
        String space;

        if (period == 1) {
            space = "'st";
        } else if (period == 2) {
            space = "'nd";
        } else if (period == 3) {
            space = "'rd";
        } else {
            space = "'th";
        }


        Log.e("TIME :", time + " ");

        Date date = new Date(time);
        DateFormat formatter = new SimpleDateFormat("HH:mm");
        String dateFormatted = formatter.format(date);
         Calendar calendar=Calendar.getInstance();
         Log.e("RECIEVER ","monthformated ---...>>"+calendar.get(calendar.DAY_OF_MONTH)+"/"+(calendar.get(calendar.MONTH)+1)+"/"+calendar.get(calendar.YEAR) );

         String dateFormated= calendar.get(calendar.DAY_OF_MONTH)+"/"+(calendar.get(calendar.MONTH)+1)+"/"+calendar.get(calendar.YEAR) ;

        Log.e("RECIEVER ","dateFormated ---...>>"+dateFormated);

        String title = "At " + period + space + " Period (" + dateFormatted + "),\n\n You have class of " +
                sub;
        Log.e("timeSet ", dateFormatted + "  det");

        Log.e("title ", title);

        Intent intent1 = new Intent(context, Main2Activity.class);
        intent1.setAction("op");
        intent1.putExtra(Constants.EXTRA_DATE, dateFormated);
        intent1.putExtra(Constants.EXTRA_DAY, dayString);

        PendingIntent notifyIntent = PendingIntent
                .getActivity(context, period, intent1, 0);

Cursor cursor=getTotalAttend();

int total=cursor.getInt(0)+cursor.getInt(1);

        int attend=cursor.getInt(0);

        NotificationCompat.Builder builder = new NotificationCompat
                .Builder(mContext)
                .setTicker("Up coming class")
                .setContentText(title)
                .setGroup("Up coming Classes")
                .setSmallIcon(R.drawable.add)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setStyle(new NotificationCompat.BigTextStyle()
                 .bigText(title+"\n Total : "+total+" \n\n attend : "+attend))
                .setContentIntent(notifyIntent);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(i++, builder.build());
    }

  /*  public PendingIntent getAttendIntent() {
        PendingIntent pendingIntent = null;

        Intent attendIntent = new Intent(mContext, service.class);
        attendIntent.putExtra("date", dateString);
        attendIntent.putExtra("subid", subId);
        attendIntent.putExtra("masterid", masterId);
        attendIntent.putExtra("attend", 1);
        attendIntent.putExtra("bunk", 0);

        pendingIntent = PendingIntent.getService(mContext, i++, attendIntent, 0);

        return pendingIntent;
    }

    public PendingIntent getBunkIntent() {
        PendingIntent pendingIntent = null;

        Intent attendIntent = new Intent(mContext, service.class);
        attendIntent.putExtra("date", dateString);
        attendIntent.putExtra("subid", subId);
        attendIntent.putExtra("masterid", masterId);

        attendIntent.putExtra("bunk", 1);
        attendIntent.putExtra("attend", 0);
        pendingIntent = PendingIntent.getService(mContext, i++, attendIntent, 0);

        return pendingIntent;
    }
*/

    public Cursor getTotalAttend(){

        String[] projection = {
                "SUM(" + DBcontract.monDB.COLUMN_ATTEND + ")",
                "SUM(" + DBcontract.monDB.COLUMN_BUNK + ")"};

        Cursor main_cursor = mContext.getContentResolver()
                .query(DBcontract.monDB.CONTENT_URI, projection,
                        DBcontract.monDB.SUB_ID + " = ? ",
                        new String[]{Long.toString(subId)}, null);
        main_cursor.moveToNext();
        return main_cursor;
    }
}
