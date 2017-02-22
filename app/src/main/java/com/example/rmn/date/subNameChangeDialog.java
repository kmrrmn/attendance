package com.example.rmn.date;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.example.rmn.date.data.DBcontract;

/**
 * Created by rmn on 16-09-2016.
 */
public class subNameChangeDialog extends DialogFragment {
    String mSubName, mUpdatedSubName;
    private static OnSubNameChangeListener mNameChangeListener;
    Long mSubId;

    public static subNameChangeDialog NewInstance(OnSubNameChangeListener subNameChangeListener, String subName, long subId) {
        subNameChangeDialog dialog = new subNameChangeDialog();
        Bundle bundle = new Bundle();
        bundle.putString("subName", subName);
        bundle.putLong("subId", subId);
        mNameChangeListener = subNameChangeListener;
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSubName = getArguments().getString("subName");
        mSubId = getArguments().getLong("subId");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Update sub name");
        EditText editText = new EditText(getActivity());
        editText.setText(mSubName);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mUpdatedSubName = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        builder.setView(editText);
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ContentValues cv = new ContentValues();
                cv.put(DBcontract.SubTable.COLUMN_SUB, mUpdatedSubName);
               int updatedRow= getActivity().getContentResolver().update(DBcontract.SubTable.CONTENT_URI, cv, DBcontract.SubTable.COLUMN_ID + "=?", new String[]{Long.toString(mSubId)});

                Log.e("Updatedrow ","== "+updatedRow);
                if (updatedRow>0){
                mNameChangeListener.OnSubNameChange(mUpdatedSubName);
            }

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });
        return builder.create();
    }



    interface OnSubNameChangeListener {
        public void OnSubNameChange(String newSubName);
    }
}
