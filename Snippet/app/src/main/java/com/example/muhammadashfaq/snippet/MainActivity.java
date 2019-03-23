package com.example.muhammadashfaq.snippet;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView txtVuSnippet,txtVuQoute;
    Animation translateAnim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtVuSnippet=findViewById(R.id.snippet);
        txtVuQoute=findViewById(R.id.txt_vu_qoute);

        Typeface typefaceFour = Typeface.createFromAsset(getAssets(),"fonts/Oswald_Medium.ttf");
        Typeface typefaceFive = Typeface.createFromAsset(getAssets(),"fonts/BebasNeue Regular.ttf");
        txtVuQoute.setTypeface(typefaceFive);
        txtVuSnippet.setTypeface(typefaceFour);


        translateAnim= AnimationUtils.loadAnimation(this,R.anim.splash_anim);
        txtVuSnippet.setAnimation(translateAnim);
        txtVuQoute.setAnimation(translateAnim);
        startSplash();
    }

    private void startSplash() {
        Thread mThread= new Thread(){
            public void run(){
                super.run();
                try {
                    Thread.sleep(2000);
                    Intent mIntent=new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(mIntent);
                    finish();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        };
        mThread.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
