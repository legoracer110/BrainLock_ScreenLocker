package com.otk.fts.myotk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class LockScreenActivity extends Activity implements View.OnClickListener, View.OnTouchListener {

    private Context mContext;
    private Button mBtnButton1;
    private Button mBtnButton2;
    private Button mBtnButton3;
    private Button mBtnButton4;
    private Button mBtnButton5;
    private Button mBtnButton6;
    private Button mBtnButton7;
    private Button mBtnButton8;
    private Button mBtnButton9;
    private Button mBtnButton10;
    private Button mBtnButton11;
    private Button mBtnButton12;
    private Button mBtnShow;
    private String entityContents = null;

    private Button mBtnReset;
    private Button mBtnDel;

    // PassWord List
    private ArrayList pw;
    // 입력 번호 List
    public ArrayList<Integer> input;
    // 키패드 순서 List
    private ArrayList<Integer> pos;

    Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("Activity", "Activity2 run!");

        // 진동
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        getWindow().addFlags(
                // 기본 잠금화면보다 우선출력
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        // 기본 잠금화면 해제시키기
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        setContentView(R.layout.activity_main);
        Intent intent = new Intent(
                getApplicationContext(),//현재제어권자
                LockScreenService.class); // 이동할 컴포넌트
        startService(intent);

        mContext = getApplicationContext();
        mBtnButton1 = (Button) findViewById(R.id.button1);
        mBtnButton1.setOnClickListener(this);
        mBtnButton2 = (Button) findViewById(R.id.button2);
        mBtnButton2.setOnClickListener(this);
        mBtnButton3 = (Button) findViewById(R.id.button3);
        mBtnButton3.setOnClickListener(this);
        mBtnButton4 = (Button) findViewById(R.id.button4);
        mBtnButton4.setOnClickListener(this);
        mBtnButton5 = (Button) findViewById(R.id.button5);
        mBtnButton5.setOnClickListener(this);
        mBtnButton6 = (Button) findViewById(R.id.button6);
        mBtnButton6.setOnClickListener(this);
        mBtnButton7 = (Button) findViewById(R.id.button7);
        mBtnButton7.setOnClickListener(this);
        mBtnButton8 = (Button) findViewById(R.id.button8);
        mBtnButton8.setOnClickListener(this);
        mBtnButton9 = (Button) findViewById(R.id.button9);
        mBtnButton9.setOnClickListener(this);
        mBtnButton10 = (Button) findViewById(R.id.button10);
        mBtnButton10.setOnClickListener(this);
        mBtnButton11 = (Button) findViewById(R.id.button11);
        mBtnButton11.setOnClickListener(this);
        mBtnButton12 = (Button) findViewById(R.id.button12);
        mBtnButton12.setOnClickListener(this);
        mBtnShow = (Button) findViewById(R.id.btn_show);
        mBtnShow.setOnTouchListener(this);

        mBtnReset = (Button) findViewById((R.id.btn_reset));
        mBtnReset.setOnClickListener(this);
        mBtnDel = (Button) findViewById((R.id.btn_del));
        mBtnDel.setOnClickListener(this);

        // 비밀번호 배열 ( 임시비번 = 1233 )
        pw = new ArrayList();
        pw.add(1);
        pw.add(2);

        // 입력받을 배열 생성
        input = new ArrayList<Integer>();

        // 키패드 위치 배열
        pos = new ArrayList<Integer>();
        pos.add(0);
        pos.add(1);
        pos.add(2);
        pos.add(3);
        pos.add(4);
        pos.add(5);
        pos.add(6);
        pos.add(7);
        pos.add(8);
        pos.add(9);
        pos.add(10);
        pos.add(11);

        shuffleKeyPad(pos);
        btnShow();

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                btnUnshow();
            }
        }, 2000);    //3초 뒤에

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

    @Override
    public void onClick(View v) {
        Log.d("FinTech", "reset_btn clicked");
        switch (v.getId()) {
            case R.id.btn_reset:
                shuffleKeyPad(pos);
                Toast.makeText(getApplicationContext(), "키패드 리셋", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_del:
                if(input.size()!=0) {
                    input.remove(input.size() - 1);
                    enterInput();
                }
                break;
            case R.id.button1:
                if(input.size()<pw.size())
                    input.add(pos.get(0));
                enterInput();
                //Log.d("Input", "input num : " + pos.get(0));
                break;
            case R.id.button2:
                if(input.size()<pw.size())
                    input.add(pos.get(1));
                enterInput();
                break;
            case R.id.button3:
                if(input.size()<pw.size())
                    input.add(pos.get(2));
                enterInput();
                break;
            case R.id.button4:
                if(input.size()<pw.size())
                    input.add(pos.get(3));
                enterInput();
                break;
            case R.id.button5:
                if(input.size()<pw.size())
                    input.add(pos.get(4));
                enterInput();
                break;
            case R.id.button6:
                if(input.size()<pw.size())
                    input.add(pos.get(5));
                enterInput();
                break;
            case R.id.button7:
                if(input.size()<pw.size())
                    input.add(pos.get(6));
                enterInput();
                break;
            case R.id.button8:
                if(input.size()<pw.size())
                    input.add(pos.get(7));
                enterInput();
                break;
            case R.id.button9:
                if(input.size()<pw.size())
                    input.add(pos.get(8));
                enterInput();
                break;
            case R.id.button10:
                if(input.size()<pw.size())
                    input.add(pos.get(9));
                enterInput();
                break;
            case R.id.button11:
                if(input.size()<pw.size())
                    input.add(pos.get(10));
                enterInput();
                break;
            case R.id.button12:
                if(input.size()<pw.size())
                    input.add(pos.get(11));
                enterInput();
                break;
        }
    }

    void CheckPW() {
        if(input.size()==pw.size()){
            int index = 0;
            while(index<input.size()){
                if(input.get(index)!=pw.get(index)) {
                    vibrator.vibrate(100);
                    Toast.makeText(getApplicationContext(), "비밀번호가 다릅니다!", Toast.LENGTH_SHORT).show();
                    input.clear();
                    enterInput();
                    return;
                }
                index++;
            }
            Toast.makeText(getApplicationContext(), "잠금 해제", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    void enterInput() {
        /*
        inputTxt.setText("");
        int index = 0;
        while(index<input.size()){
            inputTxt.setText(inputTxt.getText()+"*  ");
            index++;
        }
        */

        Log.d("Input", "Input : " + input);

        if(input.size()==pw.size())
            CheckPW();
    }

    public Drawable getDrawableImage(int number) {
        //Log.d("FinTech", "getDrawableImage number : " + number);
        if (number==0) {
            return getResources().getDrawable(R.drawable.nexus5_10);
        } else if (number==1) {
            return getResources().getDrawable(R.drawable.nexus5_01);
        } else if (number==2) {
            return getResources().getDrawable(R.drawable.nexus5_02);
        } else if (number==3) {
            return getResources().getDrawable(R.drawable.nexus5_03);
        } else if (number==4) {
            return getResources().getDrawable(R.drawable.nexus5_04);
        } else if (number==5) {
            return getResources().getDrawable(R.drawable.nexus5_05);
        } else if (number==6) {
            return getResources().getDrawable(R.drawable.nexus5_06);
        } else if (number==7) {
            return getResources().getDrawable(R.drawable.nexus5_07);
        } else if (number==8) {
            return getResources().getDrawable(R.drawable.nexus5_08);
        } else if (number==9) {
            return getResources().getDrawable(R.drawable.nexus5_09);
        } else if (number==10) {
            return getResources().getDrawable(R.drawable.nexus5_11);
        } else if (number==11) {
            return getResources().getDrawable(R.drawable.nexus5_12);
        } else if (number==12) {
            return getResources().getDrawable(R.drawable.btn_circle_icon);
        }
        return null;
    }

    public void shuffleKeyPad(ArrayList posArr) {
        int index = 0;
        Random random = new Random();
        boolean isCheck[] = new boolean[12];
        while(index < posArr.size()){
            int rand_num = random.nextInt(12);
            if(!isCheck[rand_num]){
                posArr.set(index, rand_num);
                isCheck[rand_num] = true;
                index++;
            }
            if(index==posArr.size())
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        switch(motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN :
                Log.d("FinTech", "show_btn clicked");
                btnShow();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                btnUnshow();
                break;
        }
        return true;
    }

    void btnShow(){
        mBtnButton1.setBackground(getDrawableImage(pos.get(0)));
        mBtnButton1.setEnabled(false);
        mBtnButton2.setBackground(getDrawableImage(pos.get(1)));
        mBtnButton2.setEnabled(false);
        mBtnButton3.setBackground(getDrawableImage(pos.get(2)));
        mBtnButton3.setEnabled(false);
        mBtnButton4.setBackground(getDrawableImage(pos.get(3)));
        mBtnButton4.setEnabled(false);
        mBtnButton5.setBackground(getDrawableImage(pos.get(4)));
        mBtnButton5.setEnabled(false);
        mBtnButton6.setBackground(getDrawableImage(pos.get(5)));
        mBtnButton6.setEnabled(false);
        mBtnButton7.setBackground(getDrawableImage(pos.get(6)));
        mBtnButton7.setEnabled(false);
        mBtnButton8.setBackground(getDrawableImage(pos.get(7)));
        mBtnButton8.setEnabled(false);
        mBtnButton9.setBackground(getDrawableImage(pos.get(8)));
        mBtnButton9.setEnabled(false);
        mBtnButton10.setBackground(getDrawableImage(pos.get(9)));
        mBtnButton10.setEnabled(false);
        mBtnButton11.setBackground(getDrawableImage(pos.get(10)));
        mBtnButton11.setEnabled(false);
        mBtnButton12.setBackground(getDrawableImage(pos.get(11)));
        mBtnButton12.setEnabled(false);
    }
    void btnUnshow(){
        mBtnButton1.setBackground(getDrawableImage(12));
        mBtnButton1.setEnabled(true);
        mBtnButton2.setBackground(getDrawableImage(12));
        mBtnButton2.setEnabled(true);
        mBtnButton3.setBackground(getDrawableImage(12));
        mBtnButton3.setEnabled(true);
        mBtnButton4.setBackground(getDrawableImage(12));
        mBtnButton4.setEnabled(true);
        mBtnButton5.setBackground(getDrawableImage(12));
        mBtnButton5.setEnabled(true);
        mBtnButton6.setBackground(getDrawableImage(12));
        mBtnButton6.setEnabled(true);
        mBtnButton7.setBackground(getDrawableImage(12));
        mBtnButton7.setEnabled(true);
        mBtnButton8.setBackground(getDrawableImage(12));
        mBtnButton8.setEnabled(true);
        mBtnButton9.setBackground(getDrawableImage(12));
        mBtnButton9.setEnabled(true);
        mBtnButton10.setBackground(getDrawableImage(12));
        mBtnButton10.setEnabled(true);
        mBtnButton11.setBackground(getDrawableImage(12));
        mBtnButton11.setEnabled(true);
        mBtnButton12.setBackground(getDrawableImage(12));
        mBtnButton12.setEnabled(true);
    }
}
