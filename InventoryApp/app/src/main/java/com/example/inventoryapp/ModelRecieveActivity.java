package com.example.inventoryapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventoryapp.Fragments.ScanFragment;

public class ModelRecieveActivity extends AppCompatActivity {

    String itemName;
    TextView txtVuScanModel;
    Button btnNext,btnCancel;
    private int CAMERA_PERMISSION = 200;
    boolean  PERMISSION_STATUS = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_recieve);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView title= toolbar.findViewById(R.id.toolbarcustom_title);
        TextView titleMain= toolbar.findViewById(R.id.toolbarcustum_titttle);
        LinearLayout linearLayoutGoBack=toolbar.findViewById(R.id.linear_layout_go_back);
        initiazlizaitons();
        Typeface typefaceFour = Typeface.createFromAsset(getAssets(),"fonts/BebasNeue_Regular.ttf");
        txtVuScanModel.setTypeface(typefaceFour);

        Typeface typefaceButton = Typeface.createFromAsset(getAssets(),"fonts/BebasNeue_Bold.otf");

        btnNext.setTypeface(typefaceButton);
        btnCancel.setTypeface(typefaceButton);

        takePermission();

        ScanFragment scanFragment = new ScanFragment();
        loadFragmentWithOutBackStack(scanFragment);


        linearLayoutGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(getIntent()!=null){
            itemName = getIntent().getStringExtra("recId");
        }

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(getIntent()!=null){
            itemName=getIntent().getStringExtra("recId");
            titleMain.setText(itemName);
        }
    }


    private void takePermission() {
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        if(permission!= PackageManager.PERMISSION_GRANTED){
            setUpPersission();
        }else{
            PERMISSION_STATUS=true;
        }
    }

    private void setUpPersission() {
        ActivityCompat.requestPermissions(ModelRecieveActivity.this,
                new String[]{Manifest.permission.CAMERA},
                CAMERA_PERMISSION);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==CAMERA_PERMISSION){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                PERMISSION_STATUS=true;
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();

            } else {
                PERMISSION_STATUS = false;
                message();

            }
        }
    }

    private void loadFragmentWithOutBackStack(Fragment mFragment) {
        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.fragment_container, mFragment);
        mFragmentTransaction.commit();
    }


    private void initiazlizaitons() {
        txtVuScanModel=findViewById(R.id.txt_vu_scan_model);
        btnNext=findViewById(R.id.btn_ship_model_next);
        btnCancel=findViewById(R.id.btn_ship_model_cancel);
    }

    private void message() {
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setMessage("Please allow Camera Permission. It is required in this App");
        builder.setTitle("Alert");
        builder.setPositiveButton("Go to Settings", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!PERMISSION_STATUS){
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivityForResult(intent, CAMERA_PERMISSION);
                    //   permissions();

                }else {
                    takePermission();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Toast.makeText(ModelRecieveActivity.this,"Please allow the following Permissions ", Toast.LENGTH_LONG).show();
                ModelRecieveActivity.this.finish();
            }
        });

        AlertDialog alertDialog=builder.create();
        alertDialog.show();

    }
}
