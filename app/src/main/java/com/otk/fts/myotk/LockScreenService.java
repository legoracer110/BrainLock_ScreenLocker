package com.otk.fts.myotk;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
//import android.util.Log;
import android.view.View;
import android.view.WindowManager;

public class LockScreenService extends Service {
    private BroadcastReceiver mReceiver;

    WindowManager wm;
    View mView;


    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new OnLock_BroadcastReceiver();
        registerReceiver(mReceiver, filter);

        /*
        LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(

                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        |WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        |WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.CENTER;
        mView = inflate.inflate(R.layout.prelockscreen, null);
        final ImageButton bt =  (ImageButton) mView.findViewById(R.id.btn_show);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(wm!=null) {
                    if(mView!=null) {
                        wm.removeViewImmediate(mView);
                        mView = null;
                    }
                    wm=null;
                }
            }
        });
        wm.addView(mView, params);
        */
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
        if(wm != null) {
            if(mView != null) {
                wm.removeView(mView);
                mView = null;
            }
            wm = null;
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
