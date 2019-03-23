package com.example.inventoryapp.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.inventoryapp.Adapter.PODetailsAdapter;
import com.example.inventoryapp.Model.PODetailsModel;
import com.example.inventoryapp.Model.POEditTextModel;
import com.example.inventoryapp.Model.POLocationsModel;
import com.example.inventoryapp.Model.POSerialNumberModel;
import com.example.inventoryapp.Model.SOLocationsModel;
import com.example.inventoryapp.Model.SOSerialNumberModel;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class Common {
    public static ArrayList<PODetailsModel> arrayList=new ArrayList<>();
    public static ArrayList<POEditTextModel> arrayListEditText;
    public static  ArrayList<POSerialNumberModel> serialNumberList;
    public static  ArrayList<SOSerialNumberModel> SoserialNumberList;
    public static ArrayList<POLocationsModel> locationsList;
    public static ArrayList<SOLocationsModel> SOlocationsList;

    public static String qty;
    public static String qtyrecived;
    public static String qtyitem;
    public static String itemName;
    public static String qtybilled;


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
