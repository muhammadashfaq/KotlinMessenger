package com.example.muhammadashfaq.eatitserver.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.example.muhammadashfaq.eatitserver.Model.Request;
import com.example.muhammadashfaq.eatitserver.OrderStatus;
import com.example.muhammadashfaq.eatitserver.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class ListenOrder extends Service implements ChildEventListener{

    FirebaseDatabase db;
    DatabaseReference orders;

    @Override
    public void onCreate() {
        super.onCreate();
        db=FirebaseDatabase.getInstance();
        orders=db.getReference("Requests");


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        orders.addChildEventListener(this);
        return super.onStartCommand(intent, flags, startId);
    }

    public ListenOrder() {
    }

    @Override
    public IBinder onBind(Intent intent) {
       return null;
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        //Trigger here
        Request request=dataSnapshot.getValue(Request.class);
        if(request.getStatus()=="0"){

            showNotificaton(dataSnapshot.getKey(),request);
        }

    }

    private void showNotificaton(String key, Request request) {
    Intent intent=new Intent(this, OrderStatus.class);
        PendingIntent contentintent=PendingIntent.getActivity(getBaseContext(),0,intent,0);


        NotificationCompat.Builder builder=new NotificationCompat.Builder(getBaseContext());

        builder.setAutoCancel(true).setDefaults(Notification.DEFAULT_ALL).setContentTitle("EatIt").setTicker("EatIt")
                .setContentText("You have new order"+key).setSmallIcon(R.mipmap.ic_launcher);
        NotificationManager manager= (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        int randomint=new  Random().nextInt(9999-1)+1;
        manager.notify(randomint,builder.build());
    }


    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
