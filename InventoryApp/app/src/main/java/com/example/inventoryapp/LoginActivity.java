package com.example.inventoryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    TextView txtVuLogin,txtVuAlertMsg,txtVuEmail,txtVuPassword,txtVuFogotPass,txtVuRequiredFirles,txtVuNeedToBeRegister;
    ImageView btnImageLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtVuLogin=findViewById(R.id.txt_vu_login);
        txtVuAlertMsg=findViewById(R.id.txt_vu_alert_message);
        txtVuEmail=findViewById(R.id.txt_vu_email_text);
        txtVuPassword=findViewById(R.id.txt_vu_password_text);
        txtVuFogotPass=findViewById(R.id.txt_vu_forgot_message);
        txtVuRequiredFirles=findViewById(R.id.txt_vu_required_fields);
        txtVuNeedToBeRegister=findViewById(R.id.txt_vu_need_to_registed);

        //Applying  NABILA font to text
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/BebasNeue_Bold.otf");
        txtVuLogin.setTypeface(typeface);

        Typeface typefaceOne = Typeface.createFromAsset(getAssets(), "fonts/OpenSans_Regular_0.ttf");
        txtVuAlertMsg.setTypeface(typefaceOne);

        Typeface typefaceTwo = Typeface.createFromAsset(getAssets(), "fonts/OpenSans_Bold_0.ttf");
        txtVuEmail.setTypeface(typefaceTwo);

        Typeface typefaceTHREE = Typeface.createFromAsset(getAssets(), "fonts/OpenSans_Bold_0.ttf");
        txtVuPassword.setTypeface(typefaceTHREE);


        Typeface typefaceFour = Typeface.createFromAsset(getAssets(), "fonts/OpenSans_Regular_0.ttf");
        txtVuFogotPass.setTypeface(typefaceFour);

        Typeface typefaceFive = Typeface.createFromAsset(getAssets(), "fonts/OpenSans_Regular_0.ttf");
        txtVuRequiredFirles.setTypeface(typefaceFive);
        Typeface typefaceSix = Typeface.createFromAsset(getAssets(), "fonts/OpenSans_Regular_0.ttf");
        txtVuRequiredFirles.setTypeface(typefaceSix);


    }

    public void btnImageLogin(View view) {
        Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
        startActivity(intent);
    }
}
