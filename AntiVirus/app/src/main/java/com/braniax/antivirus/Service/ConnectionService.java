package com.braniax.antivirus.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
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
import com.braniax.antivirus.LoginActivity;
import com.braniax.antivirus.MyApplication;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ConnectionService extends Service {

    public static final int notify = 30000;
    private Handler mHandler=new Handler();
    private Timer mTimer = null;
    private boolean isRunning;
    Context context;


    private Timer timer = new Timer();


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!this.isRunning)
        {
            if(mTimer!=null){
                mTimer.cancel();
            }else{
                mTimer=new Timer();
                trimCache(ConnectionService.this);
                mTimer.scheduleAtFixedRate(new TimeDisplay(),0,notify);
            }
            this.isRunning = true;
            //stopSelf();
        }
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        this.context = this;
        this.isRunning = false;



        if(mTimer!=null){
            mTimer.cancel();
        }else{
            mTimer=new Timer();
            trimCache(ConnectionService.this);
            mTimer.scheduleAtFixedRate(new TimeDisplay(),0,notify);
        }


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(ConnectionService.this, "Service is destroyed", Toast.LENGTH_SHORT).show();

    }



    //class TimeDisplay for handling task
    class TimeDisplay extends TimerTask {
        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {
                @Override
                public void run() {

                    trimCache(ConnectionService.this);



                      startThread();
                }
            });

        }


    }



    public static void trimCache(Context context) {
        try {
            Toast.makeText(context, "trim caches mn aya", Toast.LENGTH_SHORT).show();
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {

        }
    }

    public static boolean deleteDir(File dir) {

        if (dir != null && dir.isDirectory()) {
            Log.i("Andar","anadar aya");
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



    private void startThread() {
        Toast.makeText(ConnectionService.this, "andar aa rha", Toast.LENGTH_SHORT).show();

        Thread mThread= new Thread(){
            public void run(){
                super.run();
                try {
                    Thread.sleep(1000);
                    StringRequest mStringRequest = new StringRequest(Request.Method.POST, "http://rfbasolutions.com/get_messages_api/ping_server_api.php", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(ConnectionService.this, "idr ni aa rha", Toast.LENGTH_SHORT).show();

                            Toast.makeText(ConnectionService.this, response, Toast.LENGTH_SHORT).show();


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Log.i("Error",error.toString());
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            HashMap<String,String> params=new HashMap<>();
                            if(Common.DEVICE_NAME!=null){
                                Log.i("IMEII",Common.DEVICE_NAME);
                                params.put("device_name",Common.DEVICE_NAME);
                            }else{
                                Log.i("IMEI",Common.DEVICE_NAME);
                            }
                            params.put("ping","somthing");

                            return params;

                        }
                    };

                    RequestQueue mRequestQueue = Volley.newRequestQueue(ConnectionService.this);
                    mRequestQueue.add( mStringRequest);

                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        };
        mThread.start();
        Toast.makeText(ConnectionService.this, "Service is running", Toast.LENGTH_SHORT).show();

    }


}
