package com.example.serverapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.serverapp.Common.Common;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {


    FirebaseDatabase db;
    DatabaseReference dbRef;

    EditText edtTxtEmail,edtTxtPass;
    Button btnLogin;
    SpinKitView spinKitView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbRef=FirebaseDatabase.getInstance().getReference("Users");

        initiliaztions();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone= edtTxtEmail.getText().toString();
                String pass = edtTxtPass.getText().toString();

                if(Common.isConnectedtoInternet(LoginActivity.this)){
                    if(phone.isEmpty() && pass.isEmpty()){
                        Toast.makeText(LoginActivity.this, "Please fill fields", Toast.LENGTH_SHORT).show();
                    }else{
                        edtTxtEmail.setVisibility(View.GONE);
                        edtTxtPass.setVisibility(View.GONE);
                        spinKitView.setVisibility(View.VISIBLE);
                        getAdminSignin(phone,pass);
                    }
                }else{
                    Toast.makeText(LoginActivity.this, "Seems no internet connection", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    private void getAdminSignin(final String phone, String pass) {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){

                    if(dataSnapshot.child(phone).exists()){
                        Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(LoginActivity.this, "Logged in success", Toast.LENGTH_SHORT).show();

                    }else{
                        spinKitView.setVisibility(View.GONE);
                        edtTxtEmail.setVisibility(View.VISIBLE);
                        edtTxtPass.setVisibility(View.VISIBLE);
                        Toast.makeText(LoginActivity.this, "Number not exits. Try Again", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void initiliaztions() {
        edtTxtEmail=findViewById(R.id.et_login_email);
        edtTxtPass=findViewById(R.id.et_login_password);
        btnLogin=findViewById(R.id.btnLogin);
        spinKitView=findViewById(R.id.spinkit_home);
    }
}
