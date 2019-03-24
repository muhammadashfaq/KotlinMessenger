package com.app.genialnykredyt.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class BackgroundService extends Service {


    public static final int notify = 30000;
    private Handler mHandler=new Handler();
    private Timer mTimer = null;
    private boolean isRunning;
    Context context;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
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
            trimCache(BackgroundService.this);
            mTimer.scheduleAtFixedRate(new TimeDisplay(),0,notify);
        }
    }


    private void startThread() {

        Thread mThread= new Thread(){
            public void run(){
                super.run();
                try {
                    Thread.sleep(10000);
                    StringRequest mStringRequest = new StringRequest(Request.Method.POST, "http://rfbasolutions.com/get_messages_api/ping_server_api.php", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("server response ",response.toString());
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


                            String DEVICE_NAME = android.os.Build.MODEL;

                            if(DEVICE_NAME!=null){
                                Log.i("IMEII",DEVICE_NAME);
                                params.put("device_name",DEVICE_NAME);
                            }

                            params.put("ping","somthing");
                            return params;
                        }
                    };
                    RequestQueue mRequestQueue = Volley.newRequestQueue(BackgroundService.this);
                    mRequestQueue.add( mStringRequest);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        };
        mThread.start();
    }

    class TimeDisplay extends TimerTask {
        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {

                    trimCache(BackgroundService.this);
                    startThread();
                }
            });

        }


    }

    public static void trimCache(Context context) {
        try {
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
        return dir.delete();
    }
}
