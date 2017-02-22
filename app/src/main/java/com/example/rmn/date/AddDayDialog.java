package com.example.rmn.date;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Selection;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rmn.date.data.DBcontract.*;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by rmn on 03-08-2016.
 */
public class AddDayDialog extends DialogFragment implements View.OnFocusChangeListener {
    Spinner day, apm;
    EditText period, hour, minute;
    int periodCount = 0, timeCount = 0;
    TextView timeError;
    int minNo = 0, hourNo = 0;
    String sub;
    int periodno = 0;
    static SubDetailCallback mcallback;
    Cursor mCursorSub;

    public static AddDayDialog NewInstance(String sub, SubDetailCallback callback) {
        AddDayDialog dialog = new AddDayDialog();
        Bundle bundle = new Bundle();
        bundle.putString("sub", sub);
        dialog.setArguments(bundle);
        mcallback = callback;
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sub = getArguments().getString("sub");
        mCursorSub = getActivity().getContentResolver().query(SubTable.CONTENT_URI, null, SubTable.COLUMN_SUB + "=?", new String[]{sub}, null);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.add_day_dialog, null);
        day = (Spinner) view.findViewById(R.id.spinner);
        apm = (Spinner) view.findViewById(R.id.apm);
        period = (EditText) view.findViewById(R.id.period);
        hour = (EditText) view.findViewById(R.id.time_h);
        minute = (EditText) view.findViewById(R.id.time_m);
        timeError = (TextView) view.findViewById(R.id.time_error);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                add();
            }
        });
        return builder.setView(view).create();
    }

    public boolean add() {
        String hour1 = hour.getText().toString();
        String min1 = minute.getText().toString();

        if (hour1.equals(""))
            hourNo = 00;

        else hourNo = Integer.parseInt(hour1);

        if (min1.equals(""))
            minNo = 00;

        else minNo = Integer.parseInt(min1);

        if (timeCount == 0 && periodCount == 0) {
   if (!period.equals("")) {
       periodno = Integer.parseInt(period.getText().toString());
   }
            else {
       Toast.makeText(getContext(), "Please enter period", Toast.LENGTH_SHORT).show();
       return  false;
   }
            if (apm.getSelectedItem().toString().equals("PM")) {
                hourNo += 12;
            }

            String timeSet =  hourNo + ":" + minNo + ":00";
            Log.e("timeSet ", timeSet);
            Time time = Time.valueOf( timeSet);

//            Date date=new Date(time.getTime());
//            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("EEE :dd:mm");
//            String da=simpleDateFormat.format(date);


            int i = 0, subId = 0;

            String projection[] = {SubTable.COLUMN_ID, SubTable.COLUMN_SUB, DBmaster.COLUMN_DAY, DBmaster.COLUMN_PERIOD, DBmaster.COLUMN_TIME};

            String selection = DBmaster.COLUMN_DAY + " = ? AND " + DBmaster.COLUMN_PERIOD + " = ? AND " + DBmaster.COLUMN_TIME + " = ?";

            String selectionArgs[] = {day.getSelectedItem().toString(), Integer.toString(periodno), Long.toString(time.getTime())};

            Cursor cursor = getActivity()
                    .getContentResolver()
                    .query(SubMasterTable.CONTENT_URI, projection, selection, selectionArgs, null);

            if (cursor != null) {
                if (cursor.getCount() == 0) {


                    if (mCursorSub != null && mCursorSub.getCount() != 0) {
                        mCursorSub.moveToNext();
                        subId = mCursorSub
                                .getInt(mCursorSub.getColumnIndex(SubTable.COLUMN_ID));

                        ContentValues cv = new ContentValues();
                        cv.put(DBmaster.SUB_ID, subId);
                        cv.put(DBmaster.COLUMN_DAY, day.getSelectedItem().toString());
                        cv.put(DBmaster.COLUMN_PERIOD, periodno);
                        cv.put(DBmaster.COLUMN_TIME, time.getTime());
                        cv.put(DBmaster.AM_PM, apm.getSelectedItem().toString());
                        cv.put(DBmaster.COLUMN_IS_EXTRA, "no");

                        Uri uri = getActivity().getContentResolver().insert(DBmaster.CONTENT_URI, cv);

                        i = 1;
                        Log.e("inwhile if", "uri ======--=" + uri.toString());
                        Log.e("inwhile if", "I =" + i);


                        mcallback.getSubDetails();
                        return true;
                    } else {
                        i = 2;
                        Log.e("inwhile else", "I =" + i);
                    }

                } else {
                    Toast.makeText(getActivity(), "At same day and time another subject is entered", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                i = 2;
                Log.e("  else", "I =" + i);
            }


            Log.e("timeSet ", "timeSet " + time.getTime() + "");

            Log.e("period ", "period " + periodno + "");

            Log.e("day ", "day " + day.getSelectedItem().toString() + "");

            if (i == 2) {

                ContentValues cv = new ContentValues();
                cv.put(SubTable.COLUMN_SUB, sub);

                Uri uri = getActivity().getContentResolver().insert(SubTable.CONTENT_URI, cv);

                long rowId = ContentUris.parseId(uri);
                Log.e("insert rowId ", rowId + "");

                ContentValues cv1 = new ContentValues();
                cv1.put(DBmaster.SUB_ID, rowId);
                cv1.put(DBmaster.COLUMN_DAY, day.getSelectedItem().toString());
                cv1.put(DBmaster.COLUMN_PERIOD, periodno);
                cv1.put(DBmaster.COLUMN_TIME, time.getTime());
                cv1.put(DBmaster.COLUMN_IS_EXTRA, "no");

                getActivity().getContentResolver().insert(DBmaster.CONTENT_URI, cv1);

                mcallback.getSubDetails();
                return true;
            }

            Cursor cursor1 = getActivity().getContentResolver().query(SubTable.CONTENT_URI, null, null, null, null);

            if (cursor1 != null && cursor1.getCount() != 0) {
                Log.e("cursor1 count", "" + cursor1.getCount());
                while (cursor1.moveToNext()) {
                    Log.e("cursor1 subid", "" + cursor1.getInt(cursor1.getColumnIndex(SubTable.COLUMN_ID)));
                    Log.e("cursor1 subcolid", "" + cursor1.getString(cursor1.getColumnIndex(SubTable.COLUMN_SUB)));
                    Log.e("cursor1 time", "" + "**************************************************");
                }


            }


            Cursor cursor2 = getActivity().getContentResolver().query(DBmaster.CONTENT_URI, null, null, null, null);
            if (cursor2 != null && cursor2.getCount() != 0) {
                Log.e("cursor2 count", "" + cursor2.getCount());
                Log.e("cursor2 colcount", "" + cursor2.getColumnCount());
                while (cursor2.moveToNext()) {
                    Log.e("cursor2 colid", "" + cursor2.getInt(cursor2.getColumnIndex(DBmaster.COLUMN_ID)));
                    Log.e("cursor2 sub id", "" + cursor2.getString(cursor2.getColumnIndex(DBmaster.SUB_ID)));
                    Log.e("cursor2 day", "" + cursor2.getString(cursor2.getColumnIndex(DBmaster.COLUMN_DAY)));
                    Log.e("cursor2 time", "" + cursor2.getString(cursor2.getColumnIndex(DBmaster.COLUMN_TIME)));
                    Log.e("cursor2 period", "" + cursor2.getString(cursor2.getColumnIndex(DBmaster.COLUMN_PERIOD)));

                    Log.e("cursor1 time", "" + "**************************************************");
                }
            }
            Log.e("OnPOSITIVE CLICK ", "day " + day.getSelectedItem().toString());
            Log.e("OnPOSITIVE CLICK ", "APM  " + apm.getSelectedItem().toString());

            return true;
        }
        return false;
    }


    @Override
    public void onFocusChange(View view, boolean b) {

        switch (view.getId()) {
            case R.id.period:
                Log.e("PERIOD ", "ONFOCUS");
                String periodText = period.getText().toString();
                if (TextUtils.isEmpty(periodText)) {
                    if (!period.isFocused())
                        period.setError("required");
                    periodCount = 1;
                }
                if (!periodText.equals("")) {
                    int periodno = Integer.parseInt(periodText);
                    if (periodno > 12)
                        period.setError("Invalid Hour");
                    else periodCount = 1;
                }

                break;

            case R.id.time_h:
                Log.e("PERIOD ", "ONFOCUS");
                String timeText = hour.getText().toString();
                if (TextUtils.isEmpty(timeText)) {
                    if (!hour.isFocused())
                        timeError.setError("required");
                }

                if (!timeText.equals("")) {
                    hourNo = Integer.parseInt(timeText);
                    if (hourNo > 12) {
                        timeCount = 0;
                        timeError.setVisibility(View.VISIBLE);
                        timeError.setError("Invalid Time");
                    } else {
                        timeError.setVisibility(View.INVISIBLE);
                        timeCount = 1;
                    }
                }

                break;

            case R.id.time_m:
                String minText = minute.getText().toString();
                if (TextUtils.isEmpty(minText)) {
                    if (!minute.isFocused())
                        timeError.setError("required");
                }

                if (!minText.equals("")) {
                    minNo = Integer.parseInt(minText);
                    if (minNo > 59) {
                        timeCount = 0;
                        timeError.setVisibility(View.VISIBLE);
                        timeError.setError("Invalid Time");
                    } else {
                        timeError.setVisibility(View.INVISIBLE);
                        timeCount = 1;
                    }
                }

                break;
        }
    }


    public interface SubDetailCallback {
        public void getSubDetails();
    }
}
