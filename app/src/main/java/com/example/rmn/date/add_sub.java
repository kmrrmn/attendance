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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rmn.date.data.DBcontract;
import com.github.mikephil.charting.data.Entry;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rmn on 12-05-2016.
 */

public class add_sub extends DialogFragment implements View.OnClickListener {

    String day_String, date_string;
    Bundle bundle;
    LinearLayout linearLayout, entryLayout, confirmationLayout;
    CheckBox checkBox;
    String itemValue;
    TextView listItem;
    ArrayAdapter<String> adapter;
    List subList, idList, resultList;
    ListView listView;
    EditText editText, period, sub, hour, minute;
    TextView timeError;
    Spinner apm, day;
    Button attend, bunk, yes, no;
    View view;
    int periodCount = -1, hourNo, periodno, minCount = 1, hourCount = -1, minNo;
    AlertDialog.Builder builder;
    static int mAt, mBu;
    static long mSubId, mMonId;
    Time time;
    static ExtraSubAddCallback mCallback;

   public static add_sub NewInstance(ExtraSubAddCallback callback, String day,String date){
       add_sub dialog=new add_sub();
        mCallback=callback;
       Bundle bundle=new Bundle();
       bundle.putString("day",day);
       bundle.putString("date",date);
       dialog.setArguments(bundle);
       return  dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bundle = getArguments();
        day_String = bundle.getString("day");
        date_string = bundle.getString("date");

        subList = new ArrayList();
        resultList = new ArrayList();
        idList = new ArrayList();
        getSubId();
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        view = inflater.inflate(R.layout.add_extra_sub, null);
        builder.setView(view);
        Log.e("dateString   ",date_string+"--------------------------------------------------------------");
        Log.e("dateString   ",day_String+"--------------------------------------------------------------");
        setLayout();

        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_2, android.R.id.text1, resultList.toArray());

        listView.setAdapter(adapter);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    linearLayout.setVisibility(View.GONE);
                    listView.setVisibility(View.GONE);
                } else {
                    searchItem(s.toString());
                    Log.e("else text chage  ", s.toString());
                    linearLayout.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                searchItem(s.toString());
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listItem = (TextView) view.findViewById(android.R.id.text1);
                itemValue = listItem.getText().toString();
                editText.setText(itemValue);
                adapter.notifyDataSetChanged();
                listView.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
            }
        });


        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                Toast.makeText(getActivity(), "OK clicked", Toast.LENGTH_SHORT).show();
            }
        });
        return builder.create();
    }


    public void add(int at, int bu, long subid) {
        String hour1 = hour.getText().toString();
        String min1 = minute.getText().toString();

        if (hour1.equals(""))
            hourNo = 00;

        else hourNo = Integer.parseInt(hour1);

        if (min1.equals(""))
            minNo = 00;

        else minNo = Integer.parseInt(min1);
        Log.e("hourCount", hourCount + " periodcount " + periodCount+"moncount "+minCount);
        if (hourCount == 1 && periodCount == 1 && minCount == 1) {
            try {
                periodno = Integer.parseInt(period.getText().toString());

            } catch (Exception e) {
                period.setError("invalid period");
            }

            if (apm.getSelectedItem().toString().equals("PM")) {
                hourNo += 12;
            }

            String timeSet = hourNo + ":" + minNo + ":00";
            Log.e("timeSet ", timeSet);
            time = Time.valueOf(timeSet);

            String project[] = {DBcontract.SubTable.COLUMN_ID, DBcontract.DBmaster.COLUMN_ID};
            String select = DBcontract.DBmaster.COLUMN_DAY + "=? AND " +
                    DBcontract.DBmaster.COLUMN_PERIOD + "=? AND " + DBcontract.DBmaster.COLUMN_TIME + "=?";
            String selectArgs[] = {day_String, period.getText().toString(), Long.toString(time.getTime())};
            Log.e("day_str", day_String + " per " + period.getText().toString() + " tim " + time.getTime());
            Cursor mCursorSub = getActivity()
                    .getContentResolver()
                    .query
                            (DBcontract.SubMasterTable.CONTENT_URI, project,
                                    select, selectArgs, null);

            if (mCursorSub != null && mCursorSub.getCount() != 0) {
                mCursorSub.moveToNext();

                long masterId = mCursorSub.getLong(mCursorSub.getColumnIndex(DBcontract.DBmaster.COLUMN_ID));
                Cursor cursor1 = getActivity().getContentResolver()
                        .query(DBcontract.monDB.CONTENT_URI, null, DBcontract.monDB.MASTER_ID + "=? ",
                        new String[]{Long.toString(masterId)}, null);
                if (cursor1 != null && cursor1.getCount() != 0) {
                    linearLayout.setVisibility(View.GONE);
                    confirmationLayout.setVisibility(View.VISIBLE);
                    mAt = at;
                    mBu = bu;
                    mSubId = subid;
                    mMonId = mCursorSub.getLong(mCursorSub.getColumnIndex(DBcontract.DBmaster.COLUMN_ID));
                } else {
                    Log.e("ENTRY CALLE", 1 + "");
                    Entry(at, bu, subid);
                }
            } else {
                Log.e("ENTRY CALLE", 2 + "");
                Entry(at, bu, subid);
            }

        } else Toast.makeText(getActivity(), "Please enter valid entry", Toast.LENGTH_SHORT).show();

    }


    public void searchItem(String sub) {
        int sub_size = subList.size(), id_size = idList.size(), i = 0, j = 0;
        String result = null;
        resultList.clear();
        Log.e("search sub ", sub);
        if (sub == null) {
            resultList.clear();
        } else {
            while (i < sub_size && j < id_size) {
                result = subList.get(i++).toString();
                if (result.contains(sub)) {

                    Log.e("search result add ", result);

                    resultList.add(result);
                    adapter.notifyDataSetChanged();

                    adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_2, android.R.id.text1, resultList.toArray());

                    listView.setAdapter(adapter);
                }
            }
        }
    }

    public void getSubId() {

        String project[] = {"DISTINCT " + DBcontract.SubTable.COLUMN_ID, DBcontract.SubTable.COLUMN_SUB};
        String select = day_String;
        Log.e("bundlestring ", day_String);
        if (select != null) {

            Cursor cursor = getActivity()
                    .getContentResolver()
                    .query(DBcontract.SubMasterTable.CONTENT_URI,
                            project, null, null,
                            DBcontract.SubTable.COLUMN_SUB + " ASC");

            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {

                    subList.add(cursor.getString(cursor.getColumnIndex(DBcontract.SubTable.COLUMN_SUB)));
                    Log.e("get_SUB wh i  ", "sub " + cursor.getString(cursor.getColumnIndex(DBcontract.SubTable.COLUMN_SUB)));

                    idList.add(cursor.getString(cursor.getColumnIndex(DBcontract.SubTable.COLUMN_ID)));
                    Log.e("get_SUB while ", "id " + cursor.getString(cursor.getColumnIndex(DBcontract.SubTable.COLUMN_ID)) + "");
                }
            }
            cursor.close();
        }
        Log.e("get_SUB  ", "select SIZE " + select);
        Log.e("get_SUB  ", "SUBLIST SIZE " + subList.size());
    }


    public void Entry(int at, int bu, long subid) {

        ContentValues cvMaster = new ContentValues();
        cvMaster.put(DBcontract.DBmaster.SUB_ID, subid);
        cvMaster.put(DBcontract.DBmaster.COLUMN_PERIOD, period.getText().toString());
        cvMaster.put(DBcontract.DBmaster.COLUMN_TIME, time.getTime());
        cvMaster.put(DBcontract.DBmaster.COLUMN_DAY, day_String);
        cvMaster.put(DBcontract.DBmaster.COLUMN_IS_EXTRA, "yes");

        Uri uriMaster = getActivity().getContentResolver().insert(DBcontract.DBmaster.CONTENT_URI, cvMaster);

        long masterId = ContentUris.parseId(uriMaster);
        Log.e("masterId", masterId + "Long");
        Log.e("dayuyyyyyyyyyyyyy ", day_String + "=====================================Long");
        Log.e("dayuyyyyyyyyyyyyy ", date_string + "=============================================Long");

        ContentValues cv = new ContentValues();
        cv.put(DBcontract.monDB.SUB_ID, subid);
        cv.put(DBcontract.monDB.MASTER_ID, masterId);
        cv.put(DBcontract.monDB.COLUMN_DATE, date_string);
        cv.put(DBcontract.monDB.COLUMN_ATTEND, at);
        cv.put(DBcontract.monDB.COLUMN_BUNK, bu);
        cv.put(DBcontract.monDB.COLUMN_IS_EXTRA, "yes");

        Uri uriMon = getActivity().getContentResolver().insert(DBcontract.monDB.CONTENT_URI, cv);

        Log.e("inwhile if", "uriMaster =" + uriMaster.toString());
        Log.e("inwhile if", "urimon =" + uriMon.toString());
        mCallback.ExtraSubAdd();
        dismiss();

    }


    public void OverrideEntry(int at, int bu, long subid, long monid) {
        int noofrow = getActivity()
                .getContentResolver()
                .delete(DBcontract.monDB.CONTENT_URI, DBcontract.monDB.COLUMN_ID + "=?", new String[]{Long.toString(monid)});
        if (noofrow == 1) {
            Entry(at, bu, subid);
        } else {
            Toast.makeText(getActivity(), "monid notdeleted", Toast.LENGTH_SHORT).show();
        }
    }


    private void setLayout() {

        attend = (Button) view.findViewById(R.id.attend);
        bunk = (Button) view.findViewById(R.id.bunk);

        confirmationLayout = (LinearLayout) view.findViewById(R.id.confirmation);
        linearLayout = (LinearLayout) view.findViewById(R.id.hide);
        entryLayout = (LinearLayout) view.findViewById(R.id.entry);
        editText = (EditText) view.findViewById(R.id.add_sub);
        listView = (ListView) view.findViewById(R.id.listview);
        period = (EditText) view.findViewById(R.id.period);
        day = (Spinner) view.findViewById(R.id.spinner);
        apm = (Spinner) view.findViewById(R.id.apm);
        period = (EditText) view.findViewById(R.id.period);
        hour = (EditText) view.findViewById(R.id.time_h);
        minute = (EditText) view.findViewById(R.id.time_m);
        timeError = (TextView) view.findViewById(R.id.time_error);
        yes = (Button) view.findViewById(R.id.yes);
        no = (Button) view.findViewById(R.id.no);
        attend.setOnClickListener(this);
        bunk.setOnClickListener(this);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);

        period.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String periodText = period.getText().toString();
                if (periodText.equals("")) {
                    period.setError("required");
                    periodCount = -1;

                    Log.e(".equals(\"\") || Text ", "" + periodCount);
                } else {
                    int periodno;
                    try {
                        periodno = Integer.parseInt(periodText);
                        if (periodno == 0) {
                            period.setError("Invalid period");
                            Log.e("o", periodCount + "");
                        } else if (periodno > 0) {
                            periodCount = 1;
                            Log.e("period", periodCount + " ");
                        }
                    } catch (Exception e) {
                        period.setError("e Invalid period");
                        Log.e("catch", periodCount + "");
                        periodCount = -1;
                    }


                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        hour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String timeText = s.toString();

                if (!timeText.equals("")) {
                    hourNo = Integer.parseInt(timeText);
                    if (hourNo > 11) {
                        hourCount = -1;
                        timeError.setVisibility(View.VISIBLE);
                        timeError.setError("Invalid Time");
                    } else {
                        timeError.setVisibility(View.INVISIBLE);
                        hourCount = 1;
                    }
                } else {
                    if (timeText.equals("")) {
                        timeError.setError("required");
                        hourCount = -1;

                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        minute.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String timeText = s.toString();

                if (!timeText.equals("")) {
                    hourNo = Integer.parseInt(timeText);
                    if (hourNo > 59) {
                        minCount = -1;
                        timeError.setVisibility(View.VISIBLE);
                        timeError.setError("Invalid Time");
                    } else {
                        timeError.setVisibility(View.INVISIBLE);
                        minCount = 1;
                    }
                } else {
                    if (timeText.equals("")) {
                        timeError.setError("required");
                        minCount = -1;

                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.attend:

                if (subList.contains(editText.getText().toString())) {

                    add(1, 0, Long.parseLong((String) idList.get(subList.indexOf(editText.getText().toString()))));

                } else {
                    Toast.makeText(getActivity(), "Please Select Valid Subject", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bunk:

                if (subList.contains(editText.getText().toString())) {
                    add(0, 1, Long.parseLong((String) idList.get(subList.indexOf(editText.getText().toString()))));
                } else {
                    Toast.makeText(getActivity(), "Please Select Valid Subject", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.yes:
                OverrideEntry(mAt, mBu, mSubId, mMonId);
                confirmationLayout.setVisibility(View.GONE);

                break;

            case R.id.no:
                confirmationLayout.setVisibility(View.GONE);
                dismiss();
                break;
        }
    }

    interface ExtraSubAddCallback{
        public void ExtraSubAdd();
    }
}
