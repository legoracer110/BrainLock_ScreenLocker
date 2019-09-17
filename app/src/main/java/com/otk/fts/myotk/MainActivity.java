package com.otk.fts.myotk;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

public class MainActivity extends Activity{

    Vibrator vibrator;

    private static final int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Activity", "Main run!");
        // 진동
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        getWindow().addFlags(
                // 기본 잠금화면보다 우선출력
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                // 기본 잠금화면 해제시키기
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        //setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        SharedPreferences sf = getSharedPreferences("sFile",MODE_PRIVATE);
        Boolean isRun = sf.getBoolean("firstBoot", false);

        if(!isRun) {
            Log.d("Activity", "First Boot!");
            //SharedPreferences를 sFile이름, 기본모드로 설정
            SharedPreferences sharedPreferences = getSharedPreferences("sFile",MODE_PRIVATE);
            //저장을 하기위해 editor를 이용하여 값을 저장시켜준다.
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("firstBoot",true); // key, value를 이용하여 저장하는 형태
            editor.commit();

            setContentView(R.layout.temp);

            checkPermission();

            /*
            Intent intent = new Intent(
                    getApplicationContext(),//현재제어권자
                    LockScreenService.class); // 이동할 컴포넌트
            startService(intent);


            System.exit(0);
            */
        }else{

            Log.d("Activity", "Not First Boot!");

            setContentView(R.layout.temp);
            //checkPermission();

            Intent intent = new Intent(
                    getApplicationContext(),//현재제어권자
                    beforeSettingActivity.class); // 이동할 컴포넌트
            startActivity(intent);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {   // 마시멜로우 이상일 경우
            if (!Settings.canDrawOverlays(this)) {              // 체크
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
            } else {
                //startService(new Intent(MainActivity.this, totalLockScreenService.class));
                startActivity(new Intent(MainActivity.this, preLockScreenActivity.class));
            }
        } else {
            //startService(new Intent(MainActivity.this, totalLockScreenService.class));
            startActivity(new Intent(MainActivity.this, preLockScreenActivity.class));
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            if (!Settings.canDrawOverlays(this)) {
                // TODO 동의를 얻지 못했을 경우의 처리

            } else {
                //startService(new Intent(MainActivity.this, totalLockScreenService.class));
                startActivity(new Intent(MainActivity.this, preLockScreenActivity.class));
            }
        }
    }
}
