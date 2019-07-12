package com.otk.fts.myotk;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class LockScreenService extends Service {
    private BroadcastReceiver mReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new OnLock_BroadcastReceiver();
        registerReceiver(mReceiver, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if( intent == null)
        {
            IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            mReceiver = new OnLock_BroadcastReceiver();
            registerReceiver(mReceiver, filter);
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
