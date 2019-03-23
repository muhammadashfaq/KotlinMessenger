package com.braniax.antivirus;

import android.app.Application;
import android.support.annotation.NonNull;

import com.braniax.antivirus.Broadcast.ConnectivityReciever;
import com.braniax.antivirus.Common.Common;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyApplication extends Application {
    public static MyApplication mInstance;

    DatabaseReference dbRef;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance=this;
        FirebaseApp.initializeApp(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);



    }

    public static synchronized MyApplication getInstance(){
        return mInstance;
    }

    public void setConnectivtyListner(ConnectivityReciever.ConnectivityRecieverListner listner){
        ConnectivityReciever.connectivityReceiverListener = listner;
    }
}