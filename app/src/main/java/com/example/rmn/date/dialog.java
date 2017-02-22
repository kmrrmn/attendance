package com.example.rmn.date;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by rmn on 22-02-2016.
 */
public class dialog extends DialogFragment {

    public Dialog onCreateDialog(Bundle savedInstanceState){

        final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setMessage("Attend/Total");
        EditText editText=new EditText(getActivity());
        builder.setView(editText);
        builder.setTitle("Edit");

        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(),"OK clcked",Toast.LENGTH_SHORT).show();
            }
        });
      return builder.create();
    }
}
