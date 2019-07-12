package com.otk.fts.myotk;


import android.content.Context;
import android.content.SharedPreferences;

public class Utils {
    private final static String TAG = Utils.class.getSimpleName();

    public final static String APP_PREF         = "APP_PREF";
    public final static String PREF_KEY_OTKID   = "OTK_ID";

    public static SharedPreferences pref = null;
    public static SharedPreferences.Editor prefEdit = null;

    /**
     * 공유 데이터
     */
    // 공유 데이터 삽입 스트링
    public synchronized static void setShareData(Context mCon, String mKey, String mData) {
        if (pref == null) {
            pref = mCon.getSharedPreferences(Utils.APP_PREF, Context.MODE_PRIVATE);
        }
        if (prefEdit == null) {
            prefEdit = pref.edit();
        }

        prefEdit.putString(mKey, mData);
        prefEdit.commit();
    }

    // 공유 데이터 꺼내오기 스트링
    public synchronized static String getShareStringData(Context mCon, String mKey, String defaultStr) {
        String mReturnString = null;
        String mDefaultString = defaultStr;

        if (pref == null) {
            pref = mCon.getSharedPreferences(Utils.APP_PREF, Context.MODE_PRIVATE);
        }

        mReturnString = pref.getString(mKey, mDefaultString);
        mDefaultString = null;

        return mReturnString;
    }
}
