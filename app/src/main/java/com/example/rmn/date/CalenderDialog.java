package com.example.rmn.date;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class CalenderDialog extends DialogFragment {
    SimpleDateFormat format1;
    DateFormat format2;
    static CallbackForAttendance mCallback;
    static Context mContext;
    String week_day = null;
    int year, month, weekDay;
    String input_date;

    public static CalenderDialog newInstance(Context context, CallbackForAttendance callback) {
        mContext = context;
        mCallback = callback;
        CalenderDialog dialog = new CalenderDialog();
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_calender_dialog, null);
        DatePicker datePicker = (DatePicker) view.findViewById(R.id.datepicker);
        Calendar calendar = Calendar.getInstance();

        Log.e("year ", calendar.get(calendar.YEAR) + "");

        int montInt = calendar.get(calendar.MONTH) + 1;

        input_date = calendar.get(calendar.DAY_OF_MONTH) + "/" + montInt + "/" + calendar.get(calendar.YEAR);


        try {
            format1 = new SimpleDateFormat("dd/MM/yyyy");
            Date dt1 = format1.parse(input_date);
            format2 = new SimpleDateFormat("EEE");
            week_day = format2.format(dt1);
            year=calendar.YEAR;
            month=calendar.MONTH;
            Log.e("SELECTED DAY ", week_day);
            Log.e("input_date ", input_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        datePicker.init(calendar
                        .get(calendar.YEAR), calendar.get(calendar.MONTH),
                calendar.get(calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int ye, int monthOfYear, int dayOfMonth) {

                        Log.e("HOME SDK VER ", Build.VERSION.SDK_INT + " SDK ");

                        month = monthOfYear + 1;
                        year = ye;
                        weekDay = dayOfMonth;


                        input_date = weekDay + "/" + month + "/" + year;
                        Log.e("SELECTED DAY ", input_date);
                        try {
                            SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
                            Date dt1 = format1.parse(input_date);
                            DateFormat format2 = new SimpleDateFormat("EEE");

                            week_day = format2.format(dt1);
                            Log.e("SELECTED DAY ", week_day);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });

        builder.setPositiveButton("Go ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mCallback.startActivity(input_date, year, month, week_day);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setView(view);
        return builder.create();
    }

    public interface CallbackForAttendance {
        public void startActivity(String date, int year, int month, String weekDay);
    }
}
