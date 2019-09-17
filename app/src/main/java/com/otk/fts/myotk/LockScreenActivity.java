package com.otk.fts.myotk;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Random;

public class LockScreenActivity extends Activity implements View.OnTouchListener, View.OnClickListener {

    private boolean isActive;

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
    private ImageButton mBtnShow;
    private ImageButton mBtnDel;

    //private Button txtInput0;
    //private Button txtInput1;
    //private Button txtInput2;
    //private Button txtInput3;

    private ImageView img_Input;
    private Integer numType;
    private Integer btnType;

    private LinearLayout bg_screen;

    // Custom 버튼 이미지 여부
    private boolean customBgImg;
    // Custom 버튼 이미지 경로
    private String customBgImgPath;
    // Custom 버튼 Drawable
    private Drawable custom_btn_drawable;

    // PassWord Size
    private int pwSize;

    // PassWord List
    private ArrayList pw;
    // 입력 번호 List
    public ArrayList<Integer> input;
    // 키패드 순서 List
    private ArrayList<Integer> pos;

    // 초기 비밀번호 보이는 시간
    private Integer f_timer;

    boolean isStart = false;

    Vibrator vibrator;
    private Integer wrongCount;
    private Integer wrongTrigger;
    private Integer wrong_lockTimer;

    private int backupPin;

    WindowManager wm;
    View mView;

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch(keyCode) {
            case KeyEvent.KEYCODE_BACK:
                // 여기에 뒤로가기 버튼을 눌렀을 때 행동 입력
                return false;

            case KeyEvent.KEYCODE_HOME:
                // 여기에 홈 버튼을 눌렀을 때 행동 입력
                return false;
            case KeyEvent.KEYCODE_MENU:
                return false;
        }

        return super.onKeyDown(keyCode, event);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 진동
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);


        getWindow().addFlags(
                // 기본 잠금화면보다 우선출력
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        // 기본 잠금화면 해제시키기
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        //setContentView(R.layout.activity_main);
        Intent intent = new Intent(
                getApplicationContext(),//현재제어권자
                LockScreenService.class); // 이동할 컴포넌트
        startService(intent);

        LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);


        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        |WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        |WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                        |WindowManager.LayoutParams.FLAG_FULLSCREEN,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.CENTER;

        mView = inflate.inflate(R.layout.activity_main, null);
        mView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN);
        bg_screen = (LinearLayout)mView.findViewById(R.id.Linear_bg);
/*
        int uiOption = bg_screen.getSystemUiVisibility();
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH )
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT )
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        bg_screen.setSystemUiVisibility( uiOption );
*/
        mContext = getApplicationContext();
        mBtnButton1 = (Button) mView.findViewById(R.id.button1);
        mBtnButton1.setOnClickListener(this);
        mBtnButton2 = (Button) mView.findViewById(R.id.button2);
        mBtnButton2.setOnClickListener(this);
        mBtnButton3 = (Button) mView.findViewById(R.id.button3);
        mBtnButton3.setOnClickListener(this);
        mBtnButton4 = (Button) mView.findViewById(R.id.button4);
        mBtnButton4.setOnClickListener(this);
        mBtnButton5 = (Button) mView.findViewById(R.id.button5);
        mBtnButton5.setOnClickListener(this);
        mBtnButton6 = (Button) mView.findViewById(R.id.button6);
        mBtnButton6.setOnClickListener(this);
        mBtnButton7 = (Button) mView.findViewById(R.id.button7);
        mBtnButton7.setOnClickListener(this);
        mBtnButton8 = (Button) mView.findViewById(R.id.button8);
        mBtnButton8.setOnClickListener(this);
        mBtnButton9 = (Button) mView.findViewById(R.id.button9);
        mBtnButton9.setOnClickListener(this);
        mBtnButton10 = (Button) mView.findViewById(R.id.button10);
        mBtnButton10.setOnClickListener(this);
        mBtnButton11 = (Button) mView.findViewById(R.id.button11);
        mBtnButton11.setOnClickListener(this);
        mBtnButton12 = (Button) mView.findViewById(R.id.button12);
        mBtnButton12.setOnClickListener(this);
        mBtnShow = (ImageButton) mView.findViewById(R.id.btn_show);
        mBtnShow.setOnTouchListener(this);

        img_Input = (ImageView)mView.findViewById(R.id.input_img);

        mBtnDel = (ImageButton) mView.findViewById((R.id.btn_del));
        mBtnDel.setOnTouchListener(this);

        //저장된 값을 불러오기 위해 같은 네임파일을 찾음.
        SharedPreferences sf = getSharedPreferences("sFile",MODE_PRIVATE);

        isActive = sf.getBoolean("isLock", true);

        //sizePW라는 key에 저장된 값이 있는지 확인. 아무값도 들어있지 않으면 ""를 반환
        Integer spw = sf.getInt("pwSize",2);
        pwSize = spw;
        Integer lpw = sf.getInt("pwList", 0);
        backupPin = sf.getInt("backupPin", 1234);

        f_timer = sf.getInt("pwTimer", 2000);

        numType = sf.getInt("numType", 3);
        btnType = sf.getInt("btnType", 3);

        customBgImg = sf.getBoolean("customBgImg", false);
        customBgImgPath = sf.getString("pwImgPath","");

        if(customBgImg){

            BitmapFactory.Options options = new BitmapFactory.Options();
            Bitmap originalBm = BitmapFactory.decodeFile(customBgImgPath, options);

            Drawable drawable = new BitmapDrawable(originalBm);
            //drawable.setAlpha(30);
            bg_screen.setBackground(drawable);

        }else {
            //Drawable originbg = getResources().getDrawable(R.drawable.sample_bg);
            //originbg.setAlpha(30);
            //bg_screen.setBackgroundResource(R.drawable.sample_bg);
            bg_screen.setBackgroundColor(getResources().getColor(android.R.color.black));
            //bg_screen.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        }


        // 비밀번호 배열 ( 임시비번 = 12 )
        pw = new ArrayList();
        if(lpw==0) {
            pw.add(0);
            pw.add(0);
            img_Input.setImageResource(R.drawable.input_2pw0);
        }else{
            if(pwSize==2){
                // 비밀번호 사이즈 2
                Integer a = lpw/10;
                Integer b = lpw%10;
                pw.add(a);
                pw.add(b);
                img_Input.setImageResource(R.drawable.input_2pw0);
            }else{
                // 비밀번호 사이즈 4
                Integer a = lpw/1000;
                Integer b = (lpw%1000)/100;
                Integer c = (lpw%100)/10;
                Integer d = lpw%10;
                pw.add(a);
                pw.add(b);
                pw.add(c);
                pw.add(d);
                img_Input.setImageResource(R.drawable.input_4pw0);
            }
        }

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

        wrongCount = 0;
        wrongTrigger = 0;

        //lockHomeButton();
        wm.addView(mView, params);

        // 테스트 버전
        //wrong_lockTimer = 100;
        // 실제 버전
        wrong_lockTimer = 10000;
    }

    @Override
    public void onResume(){

        if(isActive) {
            super.onResume();

            wrongCount = 0;
            wrongTrigger = 0;

            shuffleKeyPad(pos);
            btnShow();
            btnsDisable();
            isStart = false;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    btnUnshow();
                    btnsEnable();
                }
            }, f_timer);    //2초 뒤에

        }else {
            finishAffinity();
        }
    }



    void btnsDisable(){
        mBtnButton1.setEnabled(false);
        mBtnButton2.setEnabled(false);
        mBtnButton3.setEnabled(false);
        mBtnButton4.setEnabled(false);
        mBtnButton5.setEnabled(false);
        mBtnButton6.setEnabled(false);
        mBtnButton7.setEnabled(false);
        mBtnButton8.setEnabled(false);
        mBtnButton9.setEnabled(false);
        mBtnButton10.setEnabled(false);
        mBtnButton11.setEnabled(false);
        mBtnButton12.setEnabled(false);
        mBtnShow.setEnabled(false);
        mBtnDel.setEnabled(false);
    }

    void btnsEnable(){
        mBtnButton1.setEnabled(true);
        mBtnButton2.setEnabled(true);
        mBtnButton3.setEnabled(true);
        mBtnButton4.setEnabled(true);
        mBtnButton5.setEnabled(true);
        mBtnButton6.setEnabled(true);
        mBtnButton7.setEnabled(true);
        mBtnButton8.setEnabled(true);
        mBtnButton9.setEnabled(true);
        mBtnButton10.setEnabled(true);
        mBtnButton11.setEnabled(true);
        mBtnButton12.setEnabled(true);
        mBtnShow.setEnabled(true);
        mBtnDel.setEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void CheckPW() {
        if(input.size()==pw.size()){
            int index = 0;

            while(index<input.size()){
                if(input.get(index)!=pw.get(index)) {
                    vibrator.vibrate(100);
                    wrongCount += 1;
                    wrongTrigger += 1;
                    if(wrongTrigger>=5&&wrongCount>=55) {
                        // PhoneLockForSeconds();

                    }else if(wrongTrigger>=5&&wrongCount>=50){
                        // PhoneLockForSeconds();

                        wrongTrigger=0;

                        pw.clear();

                        Integer a = backupPin/1000;
                        Integer b = (backupPin%1000)/100;
                        Integer c = (backupPin%100)/10;
                        Integer d = backupPin%10;
                        pw.add(a);
                        pw.add(b);
                        pw.add(c);
                        pw.add(d);
                        pwSize = 4;
                        img_Input.setImageResource(R.drawable.input_4pw0);
                        SharedPreferences sharedPreferences = getSharedPreferences("sFile", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("pwSize", pwSize);
                        editor.putInt("pwList", backupPin);
                        editor.commit();

                        btnsDisable();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                btnUnshow();
                                btnsEnable();
                            }
                        }, wrong_lockTimer);    //10초 뒤에
                    } else if(wrongTrigger>=5){
                        // PhoneLockForSeconds();
                        btnsDisable();
                        wrongTrigger=0;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                btnUnshow();
                                btnsEnable();
                            }
                        }, wrong_lockTimer);    //10초 뒤에
                    } else {
                        /*
                        Toast toast = Toast.makeText(getApplicationContext(), "비밀번호가 다릅니다!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                        toast.show();
                        */
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Incorrect();
                            input.clear();
                            enterInput();
                        }
                    }, 200);    //0.2초 뒤에
                    return;
                }
                index++;
            }
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run(){
                    Unlock();
                }
            }, 200);    //0.2초 뒤에
        }

    }

    void Unlock(){

        //wm.removeViewImmediate(mView);
        finishAffinity();
    }

    void Incorrect(){
        if(pwSize==2) {
            img_Input.setImageResource(R.drawable.input_2pw0);
        }else if(pwSize==4){
            img_Input.setImageResource(R.drawable.input_4pw0);
        }

        shuffleKeyPad(pos);
    }

    void enterInput() {

        if(pwSize==2) {
            if (input.size() == 1) {
                img_Input.setImageResource(R.drawable.input_2pw1);
            } else if (input.size() == 2) {
                img_Input.setImageResource(R.drawable.input_2pw2);
            } else {
                img_Input.setImageResource(R.drawable.input_2pw0);
            }
        }else {
            switch (input.size()) {
                case 0:
                    img_Input.setImageResource(R.drawable.input_4pw0);
                    break;
                case 1:
                    img_Input.setImageResource(R.drawable.input_4pw1);
                    break;
                case 2:
                    img_Input.setImageResource(R.drawable.input_4pw2);
                    break;
                case 3:
                    img_Input.setImageResource(R.drawable.input_4pw3);
                    break;
                case 4:
                    img_Input.setImageResource(R.drawable.input_4pw4);
                    break;
            }
        }

        if(input.size()==pw.size())
            CheckPW();
    }

    @Override
    public void onDestroy(){

        btnsEnable();
        if(wm!=null)
            if(mView!=null)
                wm.removeViewImmediate(mView);
        finishAffinity();

        super.onDestroy();
    }

    @Override
    protected void onStop(){
        btnsEnable();

        super.onStop();
        //finishAffinity();
    }

    public Drawable getDrawableImage(int number) {
        if (number==0) {
            if(numType==0) {
                return getResources().getDrawable(R.drawable.zero_0);
            }else if(numType==1){
                return getResources().getDrawable(R.drawable.line_0);
            }else if(numType==2){
                return getResources().getDrawable(R.drawable.tra35_0);
            }else if(numType==3){
                return getResources().getDrawable(R.drawable.tra100_0);
            }
        } else if (number==1) {
            if(numType==0) {
                return getResources().getDrawable(R.drawable.zero_1);
            }else if(numType==1){
                return getResources().getDrawable(R.drawable.line_1);
            }else if(numType==2){
                return getResources().getDrawable(R.drawable.tra35_1);
            }else if(numType==3){
                return getResources().getDrawable(R.drawable.tra100_1);
            }
        } else if (number==2) {
            if(numType==0) {
                return getResources().getDrawable(R.drawable.zero_2);
            }else if(numType==1){
                return getResources().getDrawable(R.drawable.line_2);
            }else if(numType==2){
                return getResources().getDrawable(R.drawable.tra35_2);
            }else if(numType==3){
                return getResources().getDrawable(R.drawable.tra100_2);
            }
        } else if (number==3) {
            if(numType==0) {
                return getResources().getDrawable(R.drawable.zero_3);
            }else if(numType==1){
                return getResources().getDrawable(R.drawable.line_3);
            }else if(numType==2){
                return getResources().getDrawable(R.drawable.tra35_3);
            }else if(numType==3){
                return getResources().getDrawable(R.drawable.tra100_3);
            }
        } else if (number==4) {
            if(numType==0) {
                return getResources().getDrawable(R.drawable.zero_4);
            }else if(numType==1){
                return getResources().getDrawable(R.drawable.line_4);
            }else if(numType==2){
                return getResources().getDrawable(R.drawable.tra35_4);
            }else if(numType==3){
                return getResources().getDrawable(R.drawable.tra100_4);
            }
        } else if (number==5) {
            if(numType==0) {
                return getResources().getDrawable(R.drawable.zero_5);
            }else if(numType==1){
                return getResources().getDrawable(R.drawable.line_5);
            }else if(numType==2){
                return getResources().getDrawable(R.drawable.tra35_5);
            }else if(numType==3){
                return getResources().getDrawable(R.drawable.tra100_5);
            }
        } else if (number==6) {
            if(numType==0) {
                return getResources().getDrawable(R.drawable.zero_6);
            }else if(numType==1){
                return getResources().getDrawable(R.drawable.line_6);
            }else if(numType==2){
                return getResources().getDrawable(R.drawable.tra35_6);
            }else if(numType==3){
                return getResources().getDrawable(R.drawable.tra100_6);
            }
        } else if (number==7) {
            if(numType==0) {
                return getResources().getDrawable(R.drawable.zero_7);
            }else if(numType==1){
                return getResources().getDrawable(R.drawable.line_7);
            }else if(numType==2){
                return getResources().getDrawable(R.drawable.tra35_7);
            }else if(numType==3){
                return getResources().getDrawable(R.drawable.tra100_7);
            }
        } else if (number==8) {
            if(numType==0) {
                return getResources().getDrawable(R.drawable.zero_8);
            }else if(numType==1){
                return getResources().getDrawable(R.drawable.line_8);
            }else if(numType==2){
                return getResources().getDrawable(R.drawable.tra35_8);
            }else if(numType==3){
                return getResources().getDrawable(R.drawable.tra100_8);
            }
        } else if (number==9) {
            if(numType==0) {
                return getResources().getDrawable(R.drawable.zero_9);
            }else if(numType==1){
                return getResources().getDrawable(R.drawable.line_9);
            }else if(numType==2){
                return getResources().getDrawable(R.drawable.tra35_9);
            }else if(numType==3){
                return getResources().getDrawable(R.drawable.tra100_9);
            }
        } else if (number==10) {
            if(numType==0) {
                return getResources().getDrawable(R.drawable.zero_shap);
            }else if(numType==1){
                return getResources().getDrawable(R.drawable.line_shap);
            }else if(numType==2){
                return getResources().getDrawable(R.drawable.tra35_shap);
            }else if(numType==3){
                return getResources().getDrawable(R.drawable.tra100_shap);
            }
        } else if (number==11) {
            if(numType==0) {
                return getResources().getDrawable(R.drawable.zero_star);
            }else if(numType==1){
                return getResources().getDrawable(R.drawable.line_star);
            }else if(numType==2){
                return getResources().getDrawable(R.drawable.tra35_star);
            }else if(numType==3){
                return getResources().getDrawable(R.drawable.tra100_star);
            }
        } else if (number==12) {

            if(btnType==0){
                return getResources().getDrawable(R.drawable.btn_new_btn);
            }else if(btnType==1){
                return getResources().getDrawable(R.drawable.btn_new_btn2);
            }else if(btnType==2){
                return getResources().getDrawable(R.drawable.btn_new_btn3);
            }else if(btnType==3){
                return getResources().getDrawable(R.drawable.btn_new_btn4);
            }
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
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.button1:
                if(input.size()<pw.size())
                    input.add(pos.get(0));
                enterInput();
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

    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        switch(motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN :
                //v.setPadding(10,10,10,10);
                v.setAlpha(0.55f);
                switch (v.getId()) {

                    case R.id.btn_del:
                        if(input.size()!=0) {
                            input.remove(input.size() - 1);
                            enterInput();
                        }
                        break;

                    case R.id.btn_show:
                        shuffleKeyPad(pos);
                        btnShow();
                        break;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                //v.setPadding(0,0,0,0);
                v.setAlpha(1.0f);
                if(v.getId()==R.id.btn_show)
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

    public static Bitmap setRoundCorner(Bitmap bitmap, int pixel) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        int color = 0xff424242;
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        paint.setColor(color);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, pixel, pixel, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }




}
