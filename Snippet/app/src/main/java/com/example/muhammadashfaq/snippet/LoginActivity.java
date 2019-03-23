package com.example.muhammadashfaq.snippet;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muhammadashfaq.snippet.Common.Common;
import com.example.muhammadashfaq.snippet.SessionManager.SessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class LoginActivity extends AppCompatActivity {
    EditText edtTxtEmail,edtTxtPassword;
    String email,password;
    SessionManager mSessionManger;

    FirebaseAuth mAuth;
    DatabaseReference dbUserRef;
    String currentUserID;
    ProgressDialog mProgDailog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edtTxtEmail=findViewById(R.id.edt_txt_login_email);
        edtTxtPassword=findViewById(R.id.edt_txt_login_password);

       /*FirebaseUser currentUser=FirebaseAuth.getInstance().getCurrentUser();
        currentUserID=currentUser.getUid();
        dbUserRef= FirebaseDatabase.getInstance().getReference().child("Users");*/
       TextView txtView = findViewById(R.id.textView);
        Typeface typefaceFour = Typeface.createFromAsset(getAssets(),"fonts/Oswald_Medium.ttf");
        txtView.setTypeface(typefaceFour);


        mSessionManger=new SessionManager(this);
        boolean isUserLogedIn=mSessionManger.checkUserLogin();
        if(isUserLogedIn){
            Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
            startActivity(intent);
            finish();
        }else{
            return;
        }
    }



    public void onSignUpClick(View view) {
        Intent intent=new Intent(LoginActivity.this,SignupActivity.class);
        startActivity(intent);
    }

    public void onForgotPasswordClick(View view) {
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Reset Password");
        builder.setIcon(R.drawable.ic_lock_black_24dp);
        builder.setMessage("Enter email to get new password");
        final EditText editText=new EditText(this);
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(5,5,5,5);
        editText.setLayoutParams(layoutParams);
        editText.setHint("Email");
        builder.setView(editText);
        builder.setCancelable(false);

        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public void onClickLogin(View view) {
        email=edtTxtEmail.getText().toString();
        password=edtTxtPassword.getText().toString();
        boolean checkVal=validateLogin(email,password);
        if(Common.isConnected(this)){
            if(checkVal){
                mShowProgDailog();
                loginUser(email,password);
            }
        }else{
            Snackbar.make(view,"Please Check Your Internet Connection",Snackbar.LENGTH_SHORT).show();
        }
    }

    private void mShowProgDailog() {
        mProgDailog=new ProgressDialog(this);
        mProgDailog.setTitle("Logining in");
        mProgDailog.setMessage("Checking your Credentionals...");
        mProgDailog.setCancelable(false);
        mProgDailog.show();
    }

    private void loginUser(final String email, final String password) {
        mAuth=FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    mProgDailog.dismiss();

                    String deviceTokenid= FirebaseInstanceId.getInstance().getToken();

                    mSessionManger.logTheUserIn(true,email,password);
                    Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    Log.d("Login", "signInWithEmail:success");
                    /*dbUserRef.child(currentUserID).child("device_token_id").setValue(deviceTokenid).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });*/

                }else{
                    mProgDailog.hide();
                    FirebaseAuthException e= (FirebaseAuthException) task.getException();
                    Toast.makeText(LoginActivity.this, "Login failed.Please try again."+ e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                    // If sign in fails, display a message to the user.
                    Log.w("Login", "signInWithEmail:failure", task.getException());
                }

            }
        });

    }

    private boolean validateLogin(String email, String password) {
        if (email.isEmpty() && password.isEmpty()) {
            edtTxtEmail.setError("Please enter your Email");
            edtTxtPassword.setError("Please enter your Password");
            return false;
        } else if (email.isEmpty()) {
            edtTxtEmail.setError("Please enter your Email");
            return false;
        } else if (password.isEmpty()) {
            edtTxtPassword.setError("Please enter your Password");
            return false;
        } else if (!email.isEmpty() && password.isEmpty()) {
            edtTxtPassword.setError("Please enter your Password");
        } else if (email.isEmpty() && !password.isEmpty()) {
            edtTxtEmail.setError("Please enter your Email");
            return false;
        }
        return true;
    }
}
