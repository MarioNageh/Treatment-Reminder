package com.marionageh.treatmentreminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.marionageh.treatmentreminder.Alarams.AlarmWhenStartSecviec;

public class autoStart extends BroadcastReceiver {
    public void onReceive(Context context, Intent arg1)
    {
        Log.d("MarioooooBreadcaos","Mario");
        Intent intent = new Intent(context,AlarmWhenStartSecviec.class);
        context.startService(intent);

    }
}
