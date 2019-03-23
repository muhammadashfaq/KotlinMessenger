package com.example.muhammadashfaq.eatit.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.muhammadashfaq.eatit.Model.UserModel;

public class Common {
    public static UserModel currentUser;

    public static  String username,password,phone;

    public static final String DELETE="Delete";
    public static final String USER_KEY="User";
    public static final String PWD_KEY="Password";

    public static String convertCodeToStatus(String status) {
        if(status.equals("0")){
            return "Placed";
        }else if(status.equals("1")){
            return "On your way";

        }else{
            return "Shipped";
        }
    }

    public static boolean isConnectedtoInternet(Context context)
    {
        ConnectivityManager connectivityManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connectivityManager!=null){
            NetworkInfo[] networkInfo=connectivityManager.getAllNetworkInfo();
            if(networkInfo!=null){
                for (int i=0;i<networkInfo.length;i++){
                    if(networkInfo[i].getState()== NetworkInfo.State.CONNECTED){
                        return true;
                    }
                }
            }

        }
        return false;
    }
}
