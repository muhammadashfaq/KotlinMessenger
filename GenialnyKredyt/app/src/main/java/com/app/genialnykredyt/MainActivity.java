package com.app.genialnykredyt;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.app.genialnykredyt.Service.BackgroundService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context=getApplicationContext();
        UpdateToServer serverClass=new UpdateToServer(context);
        serverClass.addtoServer(context);
    }
}
