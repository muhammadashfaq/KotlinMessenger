package com.example.muhammadashfaq.techsupport;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.strictmode.WebViewMethodCalledOnWrongThreadViolation;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button btnComputer;
    Button btnLaptop;
    Button btnAndroid;
    FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (isConnected()){
            Toast.makeText(this, "Online", Toast.LENGTH_SHORT).show();

        }else if(isConnected()==false) {

            final AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Network");
            builder.setIcon(R.drawable.ic_network_check_black_24dp);
            builder.setMessage("No Internet Connection");
            builder.setCancelable(true);
           /* builder.setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });*/
            builder.create();
            builder.show();
        }


        btnComputer=findViewById(R.id.btn_computer);
        btnLaptop=findViewById(R.id.btn_laptop);
        btnAndroid=findViewById(R.id.btn_android);
        floatingActionButton=findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Rate us");
                builder.setMessage("Do you love our effort ?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Check Internet Connection.
                    }
                });
                builder.setNegativeButton("Later", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       Snackbar.make(view,"We'll remind your Later !",Snackbar.LENGTH_LONG).setAction("Thanks",null).setActionTextColor(getResources().getColor(R.color.colorPrimary)).show();
                    }
                });
                AlertDialog alertDialog=builder.create();
                builder.show();
            }
        });
        btnComputer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,Computer.class);
                startActivity(intent);
            }
        });
        btnLaptop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,Laptop.class);
                startActivity(intent);
            }
        });
        btnAndroid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,Android.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_option,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case (R.id.rate_us):
                Toast.makeText(this, "Thank you for rating us", Toast.LENGTH_SHORT).show();
                break;
            case (R.id.about_us):
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("About us");
                builder.setMessage("Creative Technologies");
                AlertDialog alertDialog=builder.create();
                builder.show();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Confirm");
        builder.setMessage("Sure to exit ?");
        builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create();
        builder.show();
    }


        public boolean isConnected(){
        ConnectivityManager connectivityManager= (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo=connectivityManager.getActiveNetworkInfo();
        if(netInfo != null && netInfo.isConnectedOrConnecting()){
            NetworkInfo wifi=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mobile=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if(wifi != null && wifi.isConnectedOrConnecting() || mobile!=null && mobile.isConnectedOrConnecting()){
                    return true;
            }else {
                return false;
            }

        }
            return false;
        }

}