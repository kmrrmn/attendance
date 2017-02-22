package com.example.rmn.date;


import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import com.example.rmn.date.utiles.*;

/**
 * Created by rmn on 12-02-2017.
 */

public class AlarmSetter extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context, service.class);
        intent1.setAction(service.CREATE);
        context.startService(intent1);
    }
}
