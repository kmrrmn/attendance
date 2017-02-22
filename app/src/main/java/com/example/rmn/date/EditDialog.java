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
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rmn.date.data.DBcontract;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Calendar;
import java.util.List;

/**
 * Created by rmn on 04-06-2016.
 */

public class EditDialog extends DialogFragment implements View.OnClickListener {

    String sub;
    int a = 0, b = 0, pos,token;
    Cursor cursor;
    String input_date, subject, day;
    AlertDialog.Builder builder;
    Button bunk, attend,free;
    Long _colId;
    static EditCallback mEditCallback;
    int rowCount;

    public static EditDialog NewInstance(EditCallback callback, int token, int pos, long colId, String date, String day, String subject) {

        EditDialog editDialog = new EditDialog();
        Bundle args = new Bundle();

        Log.e("COLID ONCREATE NEWEDIT ", colId + " colid");
        args.putLong("colId", colId);
        args.putInt("pos", pos);
        args.putInt("token", token);
        args.putString("date", date);
        args.putString("sub", subject);
        args.putString("day", day);
        editDialog.setArguments(args);
        mEditCallback = callback;

        return editDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _colId = getArguments().getLong("colId");
        Log.e("_COLID ONCREATE EDIT ", _colId + " colid");
        pos = getArguments().getInt("pos");
        token = getArguments().getInt("token");
        input_date = getArguments().getString("date");
        subject = getArguments().getString("sub");
        day = getArguments().getString("day");

        Cursor cursor1=getActivity().getContentResolver().query(DBcontract.monDB.CONTENT_URI,null,null,null,null);
        while (cursor1.moveToNext()){
            Log.e("colid ",cursor1.getString(cursor1.getColumnIndex(DBcontract.monDB.COLUMN_ID)));
            Log.e("mlid ",cursor1.getString(cursor1.getColumnIndex(DBcontract.monDB.MASTER_ID)));
            Log.e("date ",cursor1.getString(cursor1.getColumnIndex(DBcontract.monDB.COLUMN_DATE)));
            Log.e("atte ",cursor1.getString(cursor1.getColumnIndex(DBcontract.monDB.COLUMN_ATTEND)));
            Log.e("bunk ",cursor1.getString(cursor1.getColumnIndex(DBcontract.monDB.COLUMN_BUNK)));
        }
cursor1.close();

        Cursor cursor = getActivity()
                .getContentResolver()
                .query(DBcontract.monDB.CONTENT_URI, null, DBcontract.monDB.MASTER_ID + "=?  AND " + DBcontract.monDB.COLUMN_DATE + "=?", new String[]{Long.toString(_colId), input_date}, null);
        if (cursor != null) {
            Log.e("count ",cursor.getCount()+"__");
            Log.e("colIdt ",_colId+"_''_");
            Log.e("date ",input_date+"_''_");
            if (cursor.getCount() > 0) {
                cursor.moveToNext();
                rowCount = cursor.getCount();
                a = cursor.getInt(cursor.getColumnIndex(DBcontract.monDB.COLUMN_ATTEND));

                b = cursor.getInt(cursor.getColumnIndex(DBcontract.monDB.COLUMN_BUNK));
            }else if (cursor.getCount()==0){
                Toast.makeText(getActivity(),"You didn't have  any entry at for this period",Toast.LENGTH_SHORT).show();
                this.dismiss();
            }
            cursor.close();
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        builder = new AlertDialog.Builder(getActivity());

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.edit_dialog, null);
        builder.setTitle("Edit");

        TextView sub = (TextView) view.findViewById(R.id.sub);
        TextView msg = (TextView) view.findViewById(R.id.msg);

        final TextView date = (TextView) view.findViewById(R.id.date);

        sub.setText(subject);
        date.setText(input_date);
        bunk = (Button) view.findViewById(R.id.bunk);
        attend = (Button) view.findViewById(R.id.attend);
        free = (Button) view.findViewById(R.id.free);
        bunk.setOnClickListener(this);
        attend.setOnClickListener(this);
        free.setOnClickListener(this);
        builder.setView(view);

        if (a == 1) {
            attend.setClickable(false);
            bunk.setClickable(true);
            msg.setText("you have been attend this class");
            attend.setClickable(false);
            bunk.setClickable(true);
        }
       else if (b == 1) {
            attend.setClickable(true);
            bunk.setClickable(false);
            msg.setText("you have been bunk this class");
            attend.setClickable(true);
            bunk.setClickable(false);
        }

        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if ((a+b) != 0) {
                    ContentValues cv = new ContentValues();
                    cv.put(DBcontract.monDB.COLUMN_ATTEND, a);
                    cv.put(DBcontract.monDB.COLUMN_BUNK, b);


                    int row = getActivity()
                            .getContentResolver()
                            .update(DBcontract.monDB.CONTENT_URI, cv, DBcontract.monDB.MASTER_ID + "=? AND " + DBcontract.monDB.COLUMN_DATE + "=?", new String[]{Long.toString(_colId), input_date});

                }else {
                   int deleterow= getActivity().getContentResolver().delete(DBcontract.monDB.CONTENT_URI,DBcontract.monDB.MASTER_ID + "=? AND " + DBcontract.monDB.COLUMN_DATE + "=?", new String[]{Long.toString(_colId), input_date});
               Log.e("DELETE ROW ","ROW //////////////////////////////////*************************************************** IS "+deleterow);
                }
                String[] projection =
                        {DBcontract.DBmaster.COLUMN_ID, DBcontract.SubTable.COLUMN_SUB, DBcontract.DBmaster.COLUMN_PERIOD, DBcontract.DBmaster.COLUMN_TIME,
                                DBcontract.SubTable.COLUMN_ID};
                String selection=null;
                if (token==0) {
                      selection = DBcontract.DBmaster.COLUMN_DAY + "= ? AND "+DBcontract.DBmaster.COLUMN_IS_EXTRA+" =? ";

                }else  if (token==1) {
                      selection = DBcontract.DBmaster.COLUMN_DAY + "= ? AND "+DBcontract.DBmaster.COLUMN_IS_EXTRA+" !=? ";
                }
                String[] arg = {day,"no"};

                Log.e("weekday rmn ", day);

                Log.e("onCreateLoader  ", " created");

                Cursor cursor1 = getActivity().getContentResolver()
                        .query(DBcontract.SubMasterTable.CONTENT_URI,
                                projection,
                                selection,
                                arg,
                                null);

                mEditCallback.editDetail(token,pos, cursor1);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mEditCallback.editDetail(token,pos, null);
            }
        });

        return builder.create();

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id) {

            case R.id.bunk:
                b = 1;
                a = 0;
                bunk.setClickable(false);
                attend.setClickable(true);
                bunk.setAlpha(0.2f);
                attend.setAlpha(1f);
                free.setAlpha(1f);
                Log.e("EDIT DIALOG on bu ", "a =" + a + "b= " + b);
                break;
            case R.id.attend:
                a = 1;
                b = 0;
                attend.setClickable(false);
                bunk.setClickable(true);
                attend.setAlpha(0.2f);
                bunk.setAlpha(1f);
                free.setAlpha(1f);
                Log.e("EDIT DIALOG at ", "a =" + a + "b= " + b);

            case R.id.free:
                a = 0;
                b = 0;
                free.setAlpha(0.2f);
                Log.e("EDIT DIALOG at ", "a =" + a + "b= " + b);

                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.dismiss();
    }

    interface EditCallback {
        void editDetail(int token,int pos, Cursor cursor);
    }
}