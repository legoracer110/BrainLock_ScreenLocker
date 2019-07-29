package com.otk.fts.myotk;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.soundcloud.android.crop.Crop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class SettingsActivity extends Activity{

    private CheckBox spw1;
    private CheckBox spw2;
    private EditText input;
    private EditText recheck;

    private CheckBox timer1;
    private CheckBox timer2;
    private CheckBox timer3;

    private CheckBox btn_img1;
    private CheckBox btn_img2;
    private CheckBox btn_img3;

    private Button confirm;
    private Button cancel;

    private Integer pwSize;
    private Integer pwList;
    private Integer timer;

    private Uri imgUri, photoURI, albumURI;
    private ImageView img1, img2, img3, img4;
    private File tempFile;
    private String mCurrentPhotoPath;

    private String tempFilePath;

    private Bitmap thePic;
    private ImageView userImg;
    private String img_str;

    private static final int PICK_FROM_ALBUM = 0;
    private static final int CROP_FROM_ALBUM = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Activity", "Setting Run!");
        setContentView(R.layout.activity_setting2);

        //TedPermission 라이브러리 -> 카메라 권한 획득
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(getApplicationContext(), "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission] ")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();


        spw1 = (CheckBox)findViewById(R.id.pwSize2);
        spw2 = (CheckBox)findViewById(R.id.pwSize4);
        img1 = (ImageView)findViewById(R.id.testImg);
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

        btn_img1 = (CheckBox)findViewById(R.id.check_nullpad_default);
        btn_img1.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v){
                // 기존 버튼 이미지 사용
                btn_img1.setChecked(true);
                btn_img2.setChecked(false);
                btn_img3.setChecked(false);
            }
        });
        btn_img2 = (CheckBox)findViewById(R.id.check_nullpad_image);
        btn_img2.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v){
                // 앨범에서 이미지 선택
                btn_img1.setChecked(false);
                btn_img2.setChecked(true);
                btn_img3.setChecked(false);
                makeDialog();
            }
        });
        btn_img3 = (CheckBox)findViewById(R.id.check_nullpad_reward);
        btn_img3.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v){
                // 리워드 이미지 사용
                btn_img1.setChecked(false);
                btn_img2.setChecked(false);
                btn_img3.setChecked(true);
            }
        });
        confirm = (Button)findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                ConfirmSettings();
            }
        });
        cancel = (Button)findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                CancelSettings();
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
            editor.putString("pwImgPath", tempFilePath);

            //editor.putString("userPhoto", img_str);

            editor.commit();
            Toast.makeText(getApplicationContext(), "패스워드 설정이 변경되었습니다.", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable(){
                @Override
                public void run(){
                    Intent intent = new Intent(
                            getApplicationContext(),//현재제어권자
                            LockScreenService.class); // 이동할 컴포넌트
                    startService(intent);
                    finishAffinity();
                }
            }, 500);    //0.5초 뒤에
            /*
            Intent intent = new Intent(
                    getApplicationContext(),//현재제어권자
                    LockScreenService.class); // 이동할 컴포넌트
            startService(intent);
            */
        }
    }

    void CancelSettings(){
        Toast.makeText(getApplicationContext(), "설정을 취소합니다.", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                Intent intent = new Intent(
                        getApplicationContext(),//현재제어권자
                        LockScreenService.class); // 이동할 컴포넌트
                startService(intent);
                finishAffinity();
            }
        }, 500);    //2초 뒤에
    }

    private void makeDialog(){
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setTitle("버튼 이미지 변경").setMessage("버튼 이미지를 앨범에서\n사용자 지정 이미지로 변경합니다")
                .setIcon(R.drawable.album64)
                .setPositiveButton("앨범선택",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int id) {
                        Log.v("알림", "다이얼로그 > 앨범선택 선택");
                        //앨범에서 선택
                        selectAlbum();
                        //Intent intent = new Intent("com.android.camera.action.CROP");
                        //startService(intent);
                        // 인텐트+뷰 넘기기
                    }
                }).setNegativeButton("취소   ",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.v("알림", "다이얼로그 > 취소 선택");
                        // 취소 클릭. dialog 닫기.
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alt_bld.create();
        alert.show();
    }

    //앨범 선택 클릭
    public void selectAlbum(){
        //앨범 열기
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
            if(tempFile != null) {
                if (tempFile.exists()) {
                    if (tempFile.delete()) {
                        tempFile = null;
                    }
                }
            }
            return;
        }
        switch (requestCode) {
            case PICK_FROM_ALBUM: {
                if(data.getData()!=null){
                    try{
                        File albumFile = null;
                        albumFile = createImageFile();
                        photoURI = data.getData();
                        albumURI = Uri.fromFile(albumFile);
                        galleryAddPic();
                        img1.setImageURI(photoURI);

                        userImg = (ImageView)findViewById(R.id.testImg);
                        Drawable d = userImg.getDrawable();

                        thePic = ((BitmapDrawable)d).getBitmap();
                        FileOutputStream out = null;
                        try{
                            out=new FileOutputStream(Environment.getExternalStorageDirectory().getPath()+"/sampleImg.png");
                            thePic.compress(Bitmap.CompressFormat.PNG, 50, out);
                        }
                        catch(FileNotFoundException e){
                            e.printStackTrace();
                        }

                        /*
                        thePic = ((BitmapDrawable)d).getBitmap();
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        thePic.compress(Bitmap.CompressFormat.PNG, 90, stream);
                        byte[] imageC = stream.toByteArray();
                        img_str = Base64.encodeToString(imageC,0);
                        */
                        //cropImage();
                    }catch (Exception e){
                        e.printStackTrace();
                        Log.v("알림","앨범에서 가져오기 에러");
                    }
                }
                break;
            }
            case Crop.REQUEST_CROP: {
                //setImage();
                Toast.makeText(this, "크롭 리퀘스트", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void cropImage(Uri photoUri) {
        /**
         *  갤러리에서 선택한 경우에는 tempFile 이 없으므로 새로 생성해줍니다.
         */
        if(tempFile == null) {
            try {
                tempFile = createImageFile();
            } catch (IOException e) {
                Toast.makeText(this, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                finish();
                e.printStackTrace();
            }
        }
        //크롭 후 저장할 Uri
        Uri savingUri = Uri.fromFile(tempFile);
        Crop.of(photoUri, savingUri).asSquare().start(this);
    }

    public File createImageFile() throws IOException{
        String imgFileName = System.currentTimeMillis() + ".jpg";
        File imageFile= null;
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/Pictures", "ireh");

        if(!storageDir.exists()){
            Log.v("알림","storageDir 존재 x " + storageDir.toString());
            storageDir.mkdirs();
        }

        Log.v("알림","storageDir 존재함 " + storageDir.toString());
        imageFile = new File(storageDir,imgFileName);
        mCurrentPhotoPath = imageFile.getAbsolutePath();
        return imageFile;
    }

    public void galleryAddPic(){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
        Toast.makeText(this,"사진이 저장되었습니다",Toast.LENGTH_SHORT).show();
    }

}