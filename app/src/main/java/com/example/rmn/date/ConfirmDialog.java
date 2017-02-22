package com.example.rmn.date;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Notification;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by rmn on 14-08-2016.
 */
public class ConfirmDialog extends DialogFragment {
 static callbackToAddSub mtoAddSub;
    private int at,bu;
    private long subid,monid;
    public static ConfirmDialog NewInstance(callbackToAddSub toAddSub, int at, int bu, long subid, long monId){
        ConfirmDialog dialog=new ConfirmDialog();
        Bundle bundle=new Bundle();
        bundle.putInt("at",at);
        bundle.putInt("bu",bu);
        bundle.putLong("subid",subid);
                bundle.putLong("monId",monId);
        dialog.setArguments(bundle);
        mtoAddSub=toAddSub;
        return dialog;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=getArguments();
        at=bundle.getInt("at");
        bu=bundle.getInt("bu");
        subid=bundle.getLong("subid");
        monid=bundle.getLong("mionid");
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Confirmation ");
        builder.setMessage("At same time attendance entry is already performed. Do you want to override this ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mtoAddSub.OverrideEntry(at,bu,subid,monid);
            }
        });
        builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return  builder.create();
    }

  public   interface callbackToAddSub{
        public void OverrideEntry(int at, int bu, long subid, long monid);
    }
}
