package com.otk.fts.myotk;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends Activity{

    private CheckBox spw1;
    private CheckBox spw2;
    private EditText input;
    private EditText recheck;

    private CheckBox timer1;
    private CheckBox timer2;
    private CheckBox timer3;


    private Button confirm;

    private Integer pwSize;
    private Integer pwList;
    private Integer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Activity", "Setting Run!");
        setContentView(R.layout.activity_setting2);

        spw1 = (CheckBox)findViewById(R.id.pwSize2);
        spw2 = (CheckBox)findViewById(R.id.pwSize4);
        spw1.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO : process the click event.
                Log.d("Activity", "CheckBox1 Clicked!");
                pwSize = 2;
                spw1.setChecked(true);
                spw2.setChecked(false);
                input.setHint("**");
                recheck.setHint("**");
            }
        }) ;
        spw2.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO : process the click event.
                pwSize = 4;
                spw2.setChecked(true);
                spw1.setChecked(false);
                input.setHint("****");
                recheck.setHint("****");

            }
        }) ;

        input = (EditText)findViewById(R.id.input_pw);
        recheck = (EditText)findViewById(R.id.re_pw);

        timer1 = (CheckBox)findViewById(R.id.set_time_1);
        timer1.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v){
                timer = 2000;
                timer1.setChecked(true);
                timer2.setChecked(false);
                timer3.setChecked(false);
            }
        });
        timer2 = (CheckBox)findViewById(R.id.set_time_2);
        timer2.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v){
                timer = 3000;
                timer1.setChecked(false);
                timer2.setChecked(true);
                timer3.setChecked(false);
            }
        });
        timer3 = (CheckBox)findViewById(R.id.set_time_3);
        timer3.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v){
                timer = 4000;
                timer1.setChecked(false);
                timer2.setChecked(false);
                timer3.setChecked(true);
            }
        });

        confirm = (Button)findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                ConfirmSettings();
            }
        });

        pwSize = 2;
        spw2.setChecked(false);
        spw1.setChecked(true);
        input.setHint("**");
        recheck.setHint("**");
        timer = 2000;
    }

    @Override
    protected void onStop(){
        // 일단 현재는 종료될 때 세팅 되는걸로 설정
        // 설정 완료 버튼으로 고쳐야됨;;
        super.onStop();
    }

    void ConfirmSettings(){
        String text = input.getText().toString();
        String pwCheck = recheck.getText().toString();

        if(!text.equals(pwCheck) || text == null || text.equals("") == true
        || (pwSize==2 && text.length()!=2) || (pwSize==4 && text.length()!=4)) {
            Toast.makeText(getApplicationContext(), "패스워드를 확인해주세요.", Toast.LENGTH_SHORT).show();
        }else {
            pwList = Integer.parseInt(text);

            SharedPreferences sharedPreferences = getSharedPreferences("sFile", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("pwSize", pwSize);
            editor.putInt("pwList", pwList);
            editor.putInt("pwTimer", timer);

            editor.commit();
            Toast.makeText(getApplicationContext(), "패스워드 설정이 변경되었습니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(
                    getApplicationContext(),//현재제어권자
                    LockScreenService.class); // 이동할 컴포넌트
            startService(intent);

            new Handler().postDelayed(new Runnable(){
                @Override
                public void run(){
                    System.exit(0);
                }
            }, 2000);    //2초 뒤에

        }


    }
}