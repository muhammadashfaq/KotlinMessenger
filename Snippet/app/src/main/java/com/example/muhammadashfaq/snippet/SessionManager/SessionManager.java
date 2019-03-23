package com.example.muhammadashfaq.snippet.SessionManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SessionManager {
    SharedPreferences mSharefPref;
    SharedPreferences.Editor editor;
    public static String userKey;

    String mPrefName="SessionPref";
    boolean LOGGEDIN =false;

    public static String getUserKey() {
        return userKey;
    }

    public static   void setUserKey(String userKey) {
        SessionManager.userKey = userKey;
    }

    public SessionManager(Context context) {
        mSharefPref=context.getSharedPreferences(mPrefName,Context.MODE_PRIVATE);
        editor=mSharefPref.edit();
    }

    public void logTheUserIn(boolean LOGGEDIN,String email,String password){
        editor.putBoolean("userLoginType",LOGGEDIN);
        editor.putString("userEmail",email);
        editor.putString("userPassword",password);
        editor.commit();
    }

    public boolean checkUserLogin(){
        return mSharefPref.getBoolean("userLoginType",false);
    }
}
