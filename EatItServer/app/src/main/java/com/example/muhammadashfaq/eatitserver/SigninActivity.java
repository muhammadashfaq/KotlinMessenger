package com.example.muhammadashfaq.eatitserver;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.UserManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.muhammadashfaq.eatitserver.Common.Common;
import com.example.muhammadashfaq.eatitserver.Model.UserModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SigninActivity extends AppCompatActivity {
    MaterialEditText editTextPhn,editTextPassword;
    Button btnSignin;

    FirebaseDatabase db;
    DatabaseReference user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        editTextPhn=findViewById(R.id.edt_txt_phone);
        editTextPassword=findViewById(R.id.edt_txt_password);
        btnSignin=findViewById(R.id.btn_sign_in);


        //initfirebase
        db=FirebaseDatabase.getInstance();
        user=db.getReference("User");
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInAdmin(editTextPhn.getText().toString(),editTextPassword.getText().toString());
            }
        });
    }

    private void signInAdmin(final String phone, String password) {

        if (Common.isConnectedtoInternet(getBaseContext())) {


            final ProgressDialog progressDialog = new ProgressDialog(SigninActivity.this);
            progressDialog.setTitle("Loading");
            progressDialog.setMessage("Wait while we are proceesing");
            progressDialog.show();

            final String localPhone = phone;
            final String edtTxtPassword = password;

            user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(localPhone).exists()) {
                        progressDialog.dismiss();
                        UserModel user = dataSnapshot.child(localPhone).getValue(UserModel.class);
                        user.setPhone(localPhone);
                        if (Boolean.parseBoolean(user.getIsStaff())) {
                            if (user.getPassword().equals(edtTxtPassword)) {

                                Intent intent = new Intent(SigninActivity.this, Home.class);
                                startActivity(intent);
                                finish();

                            } else {
                                Toast.makeText(SigninActivity.this, "Wrong Password.", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(SigninActivity.this, "Please login with staff account", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(SigninActivity.this, "Staff account not exits", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else
            {
                Toast.makeText(this, "Check your internet connection !!", Toast.LENGTH_SHORT).show();
                Snackbar.make(getWindow().getDecorView().getRootView(),"Check your internet connection !!",Snackbar.LENGTH_LONG).show();
                return;
        }
    }
}
