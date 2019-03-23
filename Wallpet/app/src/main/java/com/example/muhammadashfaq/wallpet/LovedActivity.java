package com.example.muhammadashfaq.wallpet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toolbar;

public class LovedActivity extends AppCompatActivity {

    android.support.v7.widget.Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loved);
        toolbar=findViewById(R.id.activty_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Loved");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}
