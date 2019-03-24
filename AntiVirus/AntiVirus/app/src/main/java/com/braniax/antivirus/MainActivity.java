package com.braniax.antivirus;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;

public class MainActivity extends AppCompatActivity {

    Intent mServiceIntent;
    //private BackgroundService mYourService;

    ProgressBar progressBar;
    SpinKitView spinKitView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //PackageManager p = getPackageManager();
        //ComponentName componentName = new ComponentName(this, com.braniax.antivirus.MainActivity.class); // activity which is first time open in manifiest file which is declare as <category android:name="android.intent.category.LAUNCHER" />
        //p.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);


        spinKitView=findViewById(R.id.spin_kit);

        /*mYourService = new BackgroundService();
        mServiceIntent = new Intent(this, mYourService.getClass());
        if (!isMyServiceRunning(mYourService.getClass()))
        {
            startService(mServiceIntent);
            Toast.makeText(this, "Service called fom Act", Toast.LENGTH_SHORT).show();
            }*/
    }

    private boolean isMyServiceRunning(Class<?> serviceClass)
    {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
        {
            if (serviceClass.getName().equals(service.service.getClassName()))
            {
                Log.i ("Service status", "Running");
                return true;
            }
        }
        Log.i ("Service status", "Not running");
        return false;
    }


    //@Override
   /* protected void onDestroy()
    {
        //stopService(mServiceIntent);

        if (!isMyServiceRunning(mYourService.getClass()))
        {
            startService(mServiceIntent);
        }
        super.onDestroy();
    }

    @Override
    protected void onStop()
    {

        if (!isMyServiceRunning(mYourService.getClass()))
        {
            startService(mServiceIntent);
        }
        super.onStop();

    }

    @Override
    protected void onPause()
    {

        if (!isMyServiceRunning(mYourService.getClass()))
        {
            startService(mServiceIntent);
        }
        super.onPause();
    }

    @Override
    protected void onRestart()
    {

        if (!isMyServiceRunning(mYourService.getClass()))
        {
            startService(mServiceIntent);

        }
        super.onRestart();
    }

    @Override
    protected void onResume()
    {
        if (!isMyServiceRunning(mYourService.getClass()))
        {
            startService(mServiceIntent);
        }
        super.onResume();
    }*/





    public void onScanClick(View view) {

        spinKitView.setVisibility(View.VISIBLE);

        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        wait(5000);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                spinKitView.setVisibility(View.GONE);
                                showExitDailog();
                            }
                        });

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };
        };
        thread.start();

    }

    private void showExitDailog() {
        spinKitView.setVisibility(View.GONE);
        final AlertDialog.Builder alertDailog=new AlertDialog.Builder(this);

        LayoutInflater inflater=this.getLayoutInflater();
        View view=inflater.inflate(R.layout.custom_dailog_layout,null);

        alertDailog.setView(view);

        alertDailog.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        alertDailog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDailog.show();
    }
   /* protected void onStop()
    {

        if (!isMyServiceRunning(mYourService.getClass()))
        {
            startService(mServiceIntent);
        }
        super.onStop();

    }
    protected void onPause()
    {

        if (!isMyServiceRunning(mYourService.getClass()))
        {
            startService(mServiceIntent);
        }
        super.onPause();
    }*/


}
