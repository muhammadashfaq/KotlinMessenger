package com.example.muhammadashfaq.wallpet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.muhammadashfaq.wallpet.Volley.VolleySinglenton;
import com.github.ybq.android.spinkit.SpinKitView;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.xml.transform.Result;

public class UploadActivity extends AppCompatActivity {

    Toolbar toolbar;
    MaterialSpinner spinner;
    RelativeLayout relativeLayout;

    Button btnSelect,btnUpload;
    EditText edtTxtWallpaperName;
    ImageView imgViewWallpaper;
    String category;
    private final int IMAGE_REQUEST=1;
    private Bitmap bitmap;
    SpinKitView spinKitView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        toolbar=findViewById(R.id.upload_toolbar);
        btnSelect=findViewById(R.id.btn_select);
        btnUpload=findViewById(R.id.btn_upload);
        imgViewWallpaper=findViewById(R.id.img_vu_wallpaper);
        edtTxtWallpaperName=findViewById(R.id.edt_txt_name);
        spinKitView=findViewById(R.id.spin_kit_upload);
        relativeLayout=findViewById(R.id.relative_layout);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Upload Wallpaper");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

         spinner = findViewById(R.id.spinner);
        spinner.setItems("Select Category", "Designs", "Car & Vahicles", "Bollywood", "Nature","Tech","Music","Sport","Entertainment");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
               /* Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();*/
                category=item;
            }
        });

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,IMAGE_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if(requestCode==IMAGE_REQUEST && resultCode==RESULT_OK && data!=null){
                Uri path=data.getData();
                try {
                    bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                    imgViewWallpaper.setImageBitmap(bitmap);
                    edtTxtWallpaperName.setVisibility(View.VISIBLE);
                    spinner.setVisibility(View.VISIBLE);
                    btnUpload.setVisibility(View.VISIBLE);
                    btnUpload.setEnabled(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
    }

    public void onClickUpload(View view) {
        edtTxtWallpaperName.setVisibility(View.GONE);
        spinner.setVisibility(View.GONE);
        spinKitView.setVisibility(View.VISIBLE);
        btnUpload.setEnabled(false);
        btnUpload.setVisibility(View.GONE);
      startloadingThread();
    }

    public String imageToString(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] bytes=byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes,Base64.DEFAULT);
    }
    private void startloadingThread() {

        final String URL="http://192.168.7.115/upload.php";

        try {
            StringRequest request=new StringRequest(1, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                        Toast.makeText(UploadActivity.this, response, Toast.LENGTH_SHORT).show();
                        spinKitView.setVisibility(View.GONE);
                        btnUpload.setEnabled(true);
                        showGreenSnackBar();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(UploadActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    spinKitView.setVisibility(View.GONE);
                    btnUpload.setEnabled(true);
                    showRedSnack();
                }

            })

            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String,String> params=new HashMap<>();
                    if(edtTxtWallpaperName.getText()!=null && category!=null){
                        params.put("name",edtTxtWallpaperName.getText().toString().trim());
                        params.put("image",imageToString(bitmap));
                        params.put("category",category);
                    }

                    return params;
                }
            };
            RequestQueue requestQueue=Volley.newRequestQueue(UploadActivity.this);
            requestQueue.add(request);


        }catch (Exception e){
            e.printStackTrace();
            spinKitView.setVisibility(View.GONE);
            btnUpload.setEnabled(true);
            showRedSnack();
        }
    }



    private void showRedSnack() {
        Snackbar mySnackbar = Snackbar.make(relativeLayout, "Upload Failed.", Snackbar.LENGTH_INDEFINITE)
                .setActionTextColor(Color.WHITE).setAction("Try again", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        edtTxtWallpaperName.setVisibility(View.GONE);
                        spinner.setVisibility(View.GONE);
                        spinKitView.setVisibility(View.VISIBLE);
                        btnUpload.setEnabled(false);
                        startloadingThread();
                    }
                });
        View snackBarView = mySnackbar.getView();
        snackBarView.setBackgroundColor(Color.parseColor("#D32F2F"));
        mySnackbar.show();
    }
    private void showGreenSnackBar() {
        Snackbar mySnackbar = Snackbar.make(relativeLayout, "Wallpaper Uploaded.", Snackbar.LENGTH_SHORT)
                .setActionTextColor(Color.WHITE).setAction("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
        View snackBarView = mySnackbar.getView();
        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
        mySnackbar.show();
    }
}
