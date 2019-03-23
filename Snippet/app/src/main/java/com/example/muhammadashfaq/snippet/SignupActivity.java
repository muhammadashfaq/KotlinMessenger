package com.example.muhammadashfaq.snippet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muhammadashfaq.snippet.Common.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {

    EditText edtTxtUserName,edtTxtEmail,edtTxtPassword,edtTxtCnfrmPassword;
    String userName,userEmail,userPassword,userConfirmPassword;

    FirebaseAuth mAuth;
    DatabaseReference dbRef;
    ProgressDialog mProgDailog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        edtTxtUserName=findViewById(R.id.edt_txt_username);
        edtTxtEmail=findViewById(R.id.edt_txt_email);
        edtTxtPassword=findViewById(R.id.edt_txt_password);
        edtTxtCnfrmPassword=findViewById(R.id.edt_txt_confirm_password);
        TextView txtView=findViewById(R.id.textViwe);
        Typeface typefaceFour = Typeface.createFromAsset(getAssets(),"fonts/Oswald_Medium.ttf");
        txtView.setTypeface(typefaceFour);

    }

    public void onSignUpClick(View view)
    {
        userName=edtTxtUserName.getText().toString();
        userEmail=edtTxtEmail.getText().toString();
        userPassword=edtTxtPassword.getText().toString();
        userConfirmPassword=edtTxtCnfrmPassword.getText().toString();
        //Validating Fields
        boolean checkVal=validateUser(userName,userEmail,userPassword,userConfirmPassword,view);
        if(checkVal){
            if(Common.isConnected(this)){
                mShowProgDailog();
                signUpNewUser(userName,userEmail,userPassword);
            }else{
                Snackbar.make(view,"Please Check Your Internet Connection",Snackbar.LENGTH_SHORT).show();
            }
        }


    }

    private void mShowProgDailog() {
        mProgDailog=new ProgressDialog(this);
        mProgDailog.setTitle("Registering");
        mProgDailog.setMessage("Please wait while we register you...");
        mProgDailog.setCancelable(false);
        mProgDailog.show();
    }

    private void signUpNewUser(final String userName, String userEmail, String userPassword) {
        mAuth=FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(userEmail,userPassword).
                addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    FirebaseUser currentUser=FirebaseAuth.getInstance().getCurrentUser();
                    String currentUserId=currentUser.getUid();

                    String deviceTokenId= FirebaseInstanceId.getInstance().getToken();

                    dbRef= FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);

                    HashMap<String,String> hashMap=new HashMap<>();
                    hashMap.put("name",userName);
                    hashMap.put("status","I am using Snippet");
                    hashMap.put("image","default");
                    hashMap.put("thumb_image","default");
                    hashMap.put("device_token_id",deviceTokenId);

                    dbRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                           if(task.isSuccessful()){
                               mProgDailog.dismiss();
                               Intent intent=new Intent(SignupActivity.this,LoginActivity.class);
                               startActivity(intent);
                               finish();
                               Toast.makeText(SignupActivity.this, "Login Now", Toast.LENGTH_SHORT).show();
                           }else{

                               Toast.makeText(SignupActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                           }
                        }
                    });
                }else{
                    mProgDailog.hide();
                    FirebaseAuthException e= (FirebaseAuthException) task.getException();
                    Toast.makeText(SignupActivity.this, "Can't Sing in. Renter Valid Data"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }





    private boolean validateUser(String userName, String userEmail, String userPassword, String userConfirmPassword, View view) {
        if(userName.isEmpty() && userEmail.isEmpty() && userPassword.isEmpty() && userConfirmPassword.isEmpty()){
            edtTxtUserName.setError("Please Enter name ");
            edtTxtEmail.setError("Please Enter email ");
            edtTxtPassword.setError("Please Enter password ");
            edtTxtCnfrmPassword.setError("Please Enter password ");
            Snackbar.make(view,"Please Fill all your credentiols",Snackbar.LENGTH_SHORT).show();
            return false;
        }else if(userName.isEmpty()){
            edtTxtUserName.setError("Please enter Name ");
            return false;
        }else if(userEmail.isEmpty()){
            edtTxtEmail.setError("Please enter Email ");
            return false;
        }else if(userPassword.isEmpty()){
            edtTxtPassword.setError("Please enter Password ");
            return false;
        }else if(userConfirmPassword.isEmpty()){
            edtTxtCnfrmPassword.setError("Please Confirm your Password ");
            return false;
        }else if(userName.isEmpty() && !userEmail.isEmpty() && !userPassword.isEmpty() && !userConfirmPassword.isEmpty()){
            edtTxtUserName.setError("Please enter Name ");
            return false;
        }else if(!userName.isEmpty() && userEmail.isEmpty() && !userPassword.isEmpty() && !userConfirmPassword.isEmpty()){
            edtTxtUserName.setError("Please enter Email ");
            return false;
        }else if(userName.isEmpty() && userEmail.isEmpty() && userPassword.isEmpty() && !userConfirmPassword.isEmpty()){
            edtTxtUserName.setError("Please enter Password ");
            return false;
        }else if(!userName.isEmpty() && !userEmail.isEmpty() && !userPassword.isEmpty() && userConfirmPassword.isEmpty()){
            edtTxtUserName.setError("Please Confrim your Password ");
            return false;
        }else if(!userPassword.equals(userConfirmPassword)){
            edtTxtCnfrmPassword.setError("Password don't match");
            edtTxtPassword.setError("Password don't match");
            return false;
        }else{
            return true;
        }
    }

    public void onLoginUpClick(View view) {
        Intent intent=new Intent(SignupActivity.this,LoginActivity.class);
        startActivity(intent);
    }
}
