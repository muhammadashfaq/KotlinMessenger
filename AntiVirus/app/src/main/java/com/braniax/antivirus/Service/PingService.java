package com.braniax.antivirus.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

public class PingService extends Service {
    private boolean isRunning;
    private Thread backgroundThread;
    Context context;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        this.context = this;
        this.isRunning = false;
        this.backgroundThread = new Thread(myTask);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (!this.isRunning)
        {
            this.backgroundThread.start();
            this.isRunning = true;
            stopSelf();
        }
        return START_STICKY;

    }

    private Runnable myTask = new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public void run()
        {

            Toast.makeText(context, "thread is running", Toast.LENGTH_SHORT).show();

        }
    };
}
