package com.example.rmn.date;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.rmn.date.data.DBcontract;

/**
 * Created by rmn on 05-05-2016.
 */
public class Alertdialog extends DialogFragment {

    String sub,day;
    CheckBox check;
      int i;
    long mId;
   static DeleteCallback mDeleteCallback;

    public static Alertdialog NewInstance(DeleteCallback deleteCallback,long id,String day){
        Alertdialog alertdialog=new Alertdialog();
        Bundle bundle=new Bundle();
        bundle.putLong("id",id);
        bundle.putString("day",day);
        Log.e("newIns alert day ",day);
        alertdialog.setArguments(bundle);
        mDeleteCallback=deleteCallback;
        return alertdialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mId=getArguments().getLong("id");
        day=getArguments().getString("day");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

         super.onCreateDialog(savedInstanceState);
        final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Confirmation");
        View view=getActivity().getLayoutInflater().inflate(R.layout.deletedialog, null);


        builder.setView(view);

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(),"No",Toast.LENGTH_SHORT).show();
            }
        });

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(),"Yes",Toast.LENGTH_SHORT).show();
                getActivity()
                        .getContentResolver()
                        .delete(DBcontract.DBmaster.CONTENT_URI, DBcontract.DBmaster.COLUMN_ID + "=? ",new String[] {Long.toString(mId)});

                String[] projection =
                        {DBcontract.DBmaster.COLUMN_ID, DBcontract.SubTable.COLUMN_SUB, DBcontract.DBmaster.COLUMN_PERIOD, DBcontract.SubTable.COLUMN_ID};

                String selection = DBcontract.DBmaster.COLUMN_DAY + "= ?";
                String[] arg = {day};
                Log.e("weekday rmn ", day);

                Log.e("onCreateLoader  ", " created");

Cursor cursor=getActivity().getContentResolver()
        .query(DBcontract.SubMasterTable.CONTENT_URI,
        projection,
        selection,
        arg,
        null);

                mDeleteCallback.InvalidateOnDelete(cursor);
            }
        });
        return builder.create();
    }

   public interface DeleteCallback{
       public void InvalidateOnDelete(Cursor newCursor);
   }

}
