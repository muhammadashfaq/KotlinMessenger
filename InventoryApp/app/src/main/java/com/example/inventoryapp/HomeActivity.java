package com.example.inventoryapp;

import android.content.Intent;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.inventoryapp.Common.Common;
import com.google.android.material.snackbar.Snackbar;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class HomeActivity extends AppCompatActivity {

    LinearLayout layoutHome;

    Button btnShip,btnRecieve,btnQuickShip,btnQuickRecieve;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        layoutHome=findViewById(R.id.layout_home);

        Typeface typefaceButton = Typeface.createFromAsset(getAssets(),"fonts/BebasNeue_Bold.otf");

        btnShip= findViewById(R.id.btn_ship);
        btnRecieve=findViewById(R.id.btn_recive);
        btnQuickShip=findViewById(R.id.btn_quick_ship);
        btnQuickRecieve=findViewById(R.id.btn_quick_recieve);
        btnShip.setTypeface(typefaceButton);
        btnRecieve.setTypeface(typefaceButton);
        btnQuickShip.setTypeface(typefaceButton);
        btnQuickRecieve.setTypeface(typefaceButton);
    }

    public void btnShip(View view) {

        if(!Common.isConnectedtoInternet(this)){
            Snackbar snackbar;
            snackbar = Snackbar.make(layoutHome, "No Intenet Connection", Snackbar.LENGTH_LONG);
            View snackBarView =  snackbar.getView();
            snackBarView.setBackgroundColor(Color.parseColor("#F44336"));
            snackbar.show();
        }else{
            Intent intent = new Intent(HomeActivity.this,RecieveDetail.class);
            startActivity(intent);
        }


    }

    public void btnRecive(View view) {


        if(!Common.isConnectedtoInternet(this)){
            Snackbar snackbar;
            snackbar = Snackbar.make(layoutHome, "No Intenet Connection", Snackbar.LENGTH_LONG);
            View snackBarView =  snackbar.getView();
            snackBarView.setBackgroundColor(Color.parseColor("#F44336"));
            snackbar.show();
        }else{
            Intent intent = new Intent(HomeActivity.this,DetailActivity.class);
            startActivity(intent);
        }


    }

    public void btnQuickShip(View view) {
        if(Common.isConnectedtoInternet(this)){
            Intent intent = new Intent(HomeActivity.this,QuickShipAcitivity.class);
            startActivity(intent);
        }else{
            Snackbar snackbar;
            snackbar = Snackbar.make(layoutHome, "No Intenet Connection", Snackbar.LENGTH_LONG);
            View snackBarView =  snackbar.getView();
            snackBarView.setBackgroundColor(Color.parseColor("#F44336"));
            snackbar.show();
        }
    }

    public void btnQuickRecive(View view) {
        if(Common.isConnectedtoInternet(this)){
            Intent intent = new Intent(HomeActivity.this,QuickReceiveActiviy.class);
            startActivity(intent);
        }else{
            Snackbar snackbar;
            snackbar = Snackbar.make(layoutHome, "No Intenet Connection", Snackbar.LENGTH_LONG);
            View snackBarView =  snackbar.getView();
            snackBarView.setBackgroundColor(Color.parseColor("#F44336"));
            snackbar.show();
        }
    }
}
