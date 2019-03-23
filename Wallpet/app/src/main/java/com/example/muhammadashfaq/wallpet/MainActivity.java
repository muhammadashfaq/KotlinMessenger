package com.example.muhammadashfaq.wallpet;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    String now_playing, earned;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        startSplash();


    }

    private void startSplash() {
        Thread mThread= new Thread(){
            public void run(){
                super.run();
                try {
                    Thread.sleep(5000);
                    startloadingThread();
                    Intent mIntent=new Intent(MainActivity.this,HomeActivity.class);
                    startActivity(mIntent);
                    finish();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        };
        mThread.start();
    }

    private void startloadingThread() {

        final String URL="http://192.168.7.105/index.php";

        Thread thread=new Thread(){

            @Override
            public void run() {
                super.run();

                try {
                    StringRequest request=new StringRequest(1, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                        }


                    });

                    RequestQueue requestQueue=Volley.newRequestQueue(MainActivity.this);
                    requestQueue.add(request);


                }catch (Exception e){
                    e.printStackTrace();
                }



            }
        };
        thread.start();
    }
}
