package com.example.muhammadashfaq.wallpet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class CollectionActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        toolbar=findViewById(R.id.activty_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Collections");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}
