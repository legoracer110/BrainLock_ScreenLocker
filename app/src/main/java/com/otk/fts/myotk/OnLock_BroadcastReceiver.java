package com.otk.fts.myotk;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.concurrent.locks.Lock;

public class OnLock_BroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_SCREEN_ON))
        {
            Toast.makeText(context, "SCREEN_ON", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(context, LockScreenActivity.class);

            i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, 0);
            try {
                pendingIntent.send();
            }catch (PendingIntent.CanceledException e){
                e.printStackTrace();
            }
        }
        else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF))
        {
            //Toast.makeText(context, "SCREEN_OFF", Toast.LENGTH_SHORT).show();
        }
        else if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
        {
            Toast.makeText(context, "BOOT_COMPLETED", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(context, LockScreenService.class);
            context.startService(i);
        }
    }
}
