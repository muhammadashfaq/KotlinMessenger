package com.example.torchtechndevs;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Permission;

public class MainActivity extends AppCompatActivity {


    private CameraManager mCameraManager;
    private String mCameraId;

    private AdView mAdView;
    private InterstitialAd interstitialAd;

    private int CAMERA_PERMISSION = 200;

    private boolean flashStatus;
    String deviceId;
    boolean  PERMISSION_STATUS = false;
    //String cemaraId;

    private static Camera camera=  null;
    private Parameters params=  null;

    ImageButton btnSwitch;

    private boolean isFlashOn;



    ImageButton btnActionFlash;
    TextView txtVuStatus;
    String android_id;
    int sdkVersion;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Torch");
        sdkVersion = Build.VERSION.SDK_INT;

       btnActionFlash = findViewById(R.id.btAction);
       txtVuStatus = findViewById(R.id.tvStatus);

        android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        deviceId = md5(android_id).toUpperCase();
      //  Toast.makeText(this, deviceId, Toast.LENGTH_SHORT).show();



//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
//            try {
//                mCameraId = mCameraManager.getCameraIdList()[0];
//            } catch (CameraAccessException e) {
//                e.printStackTrace();
//            }
//        }

        getCamera();
        toggleButtonImage();


        showInterstialAdd();
         makeAdds();


       btnActionFlash.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {


               if(checkCameraHardware(MainActivity.this)){


                   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                       takePermission();
                       if(PERMISSION_STATUS){
                          btnActionFlash.setImageDrawable(getDrawable(R.drawable.flashlight_off));
                          txtVuStatus.setText("OFF");
                           if(!flashStatus){
                          //     Toast.makeText(MainActivity.this, "on", Toast.LENGTH_SHORT).show();
                               Camera cam = Camera.open();
                               Camera.Parameters p = cam.getParameters();
                               p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                               cam.setParameters(p);
                               SurfaceTexture dummy = new SurfaceTexture(1);
                               try {
                                   cam.setPreviewTexture(dummy);
                               } catch (IOException e) {
                                   e.printStackTrace();
                               }
                               cam.startPreview();
                               flashStatus= true;
                               toggleButtonImage();
                           }else{
                            //   Toast.makeText(MainActivity.this, "off", Toast.LENGTH_SHORT).show();
                               Camera cam = Camera.open();
                               Camera.Parameters p = cam.getParameters();
                               p.setFlashMode(Parameters.FLASH_MODE_OFF);
                               cam.setParameters(p);
                               SurfaceTexture dummy = new SurfaceTexture(1);
                               try {
                                   cam.setPreviewTexture(dummy);
                               } catch (IOException e) {
                                   e.printStackTrace();
                               }
                               cam.stopPreview();
                               flashStatus= false;
                               toggleButtonImage();
                           }
                       }
                   }else {

                    //   Toast.makeText(MainActivity.this, "in hoa", Toast.LENGTH_SHORT).show();
                       takePermission();
                       if(PERMISSION_STATUS){

                           if (isFlashOn) {
                            //   Toast.makeText(MainActivity.this, "off", Toast.LENGTH_SHORT).show();
                               turnOffFlash();
                           } else {
                            //   Toast.makeText(MainActivity.this, "ON", Toast.LENGTH_SHORT).show();
                               turnOnFlash();
                           }
                       }
                   }




               }else{
                   Toast.makeText(MainActivity.this, "Device has not camera feature", Toast.LENGTH_SHORT).show();
               }

           }
       });

    }

    private void showInterstialAdd() {
        interstitialAd=new InterstitialAd(this);
        interstitialAd.setAdUnitId(getResources().getString(R.string.admob_interstial_add_id));
        interstitialAd.loadAd(new AdRequest.Builder().build());

        interstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }


        });
    }

    public String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void makeAdds() {
        mAdView=findViewById(R.id.adView);
        if(deviceId!=null){
            AdRequest adRequest = new AdRequest.Builder()
                    .build();

            mAdView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                   // Toast.makeText(MainActivity.this, "Ad loaded", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAdClosed() {
                   // Toast.makeText(getApplicationContext(), "Ad is closed!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
                 //   Toast.makeText(getApplicationContext(), "Ad failed to load! error code: " + errorCode, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAdLeftApplication() {
                 //   Toast.makeText(getApplicationContext(), "Ad left application!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAdOpened() {
                    super.onAdOpened();
                }
            });

            mAdView.loadAd(adRequest);
        }

    }

    private void openFlashForNewerVersion() throws CameraAccessException {
        takePermission();

        if(PERMISSION_STATUS) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

          CameraManager  mCameraManager = (CameraManager) this.getSystemService(Context.CAMERA_SERVICE);
            try {
                for (String camID : mCameraManager.getCameraIdList()) {
                    CameraCharacteristics cameraCharacteristics = mCameraManager.getCameraCharacteristics(camID);
                    int lensFacing = cameraCharacteristics.get(CameraCharacteristics.LENS_FACING);
                    if (lensFacing == CameraCharacteristics.LENS_FACING_FRONT && cameraCharacteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)) {
                        mCameraId = camID;
                        break;
                    } else if (lensFacing == CameraCharacteristics.LENS_FACING_BACK && cameraCharacteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)) {
                        mCameraId = camID;
                    }
                }
                if (mCameraId != null) {

//                    if(!flashStatus){
//
//                        mCameraManager.setTorchMode(mCameraId,true);
//                        Toast.makeText(this, "in", Toast.LENGTH_SHORT).show();
//                        btnActionFlash.setImageDrawable(getDrawable(R.drawable.flashlight_on));
//                        txtVuStatus.setText("ON");
//                        flashStatus = true;
//
//                    }


                }else{
                   // Toast.makeText(this, "out", Toast.LENGTH_SHORT).show();
                    mCameraManager.setTorchMode(mCameraId, false);
                    btnActionFlash.setImageDrawable(getDrawable(R.drawable.flashlight_off));
                    txtVuStatus.setText("OFF");
                    flashStatus = false;
                }
            } catch (CameraAccessException e) {
                e.printStackTrace();

            }

            }


//            Toast.makeText(this, "ayaa", Toast.LENGTH_SHORT).show();
//
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//
//                if(!flashStatus) {
//                  CameraManager  cemeraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
//
//                        String cemaraId = cemeraManager.getCameraIdList()[0];
//
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//
//                        try {
//                            cemeraManager.setTorchMode(cemaraId,true);
//                        } catch (CameraAccessException e) {
//                            Log.e("Exception",e.getMessage());
//                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                            e.printStackTrace();
//                        }
//                    }
//                    btnActionFlash.setImageDrawable(getDrawable(R.drawable.flashlight_on));
//                    txtVuStatus.setText("ON");
//                    flashStatus = true;
//                }
//                else{
//                   CameraManager cemeraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
//                       String cemaraId = cemeraManager.getCameraIdList()[0];
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        try {
//                            cemeraManager.setTorchMode(cemaraId,false);
//                        } catch (CameraAccessException e) {
//                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                            e.printStackTrace();
//                        }
//                    }
//
//                    btnActionFlash.setImageDrawable(getDrawable(R.drawable.flashlight_off));
//                    txtVuStatus.setText("OFF");
//                    flashStatus = false;
//                }
//
//            }
        }
    }

    private boolean checkCameraHardware(Context context){
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    // getting camera parameters
    private void getCamera() {
        if (camera == null) {
            try {
                camera = Camera.open();
                params = camera.getParameters();
            } catch (RuntimeException e) {
                Log.e("Camera Error", e.getMessage());
            }
        }
    }


    /*
     * Turning Off flash
     */
    private void turnOffFlash() {
        if (isFlashOn) {
            if (camera == null || params == null) {
                return;

            }


            params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(params);

            camera.stopPreview();
            isFlashOn = false;

            // changing button/switch image
            toggleButtonImage();
        }
    }

    /*
     * Turning On flash
     */
    private void turnOnFlash() {
        if (!isFlashOn) {
            if (camera == null || params == null) {
                return;
            }



            params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(params);
            camera.startPreview();
            isFlashOn = true;

            // changing button/switch image
            toggleButtonImage();
        }

    }

    private void toggleButtonImage(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(flashStatus){
                btnActionFlash.setImageResource(R.drawable.flashlight_on);
                txtVuStatus.setText("ON");
            }else {
                btnActionFlash.setImageResource(R.drawable.flashlight_off);
                txtVuStatus.setText("OFF")  ;
            }
        }else{
            if(isFlashOn){
                btnActionFlash.setImageResource(R.drawable.flashlight_on);
                txtVuStatus.setText("ON");
            }else{
                btnActionFlash.setImageResource(R.drawable.flashlight_off);
                txtVuStatus.setText("OFF")  ;
            }
        }


    }


    private void takePermission() {
        int permissions = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        if(permissions != PackageManager.PERMISSION_GRANTED)
            setUpPermissions();
        else
            PERMISSION_STATUS = true;
    }

    private void setUpPermissions() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.CAMERA},
                CAMERA_PERMISSION);

    }


    private void message() {


android.support.v7.app.AlertDialog.Builder builder= new android.support.v7.app.AlertDialog.Builder(this);
        builder.setMessage("Please Allow All the Permissions For APP its required in this Application");
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
                Toast.makeText(MainActivity.this,"Please allow the following Permissions ", Toast.LENGTH_LONG).show();
                MainActivity.this.finish();
            }
        });

        AlertDialog alertDialog=builder.create();
        alertDialog.show();

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
                Toast.makeText(this,"Permission denied",Toast.LENGTH_LONG).show();

            }
        }
    }

    @Override
    protected void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

        }else{
            // on pause turn off the flash
            turnOffFlash();
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }

        if(interstitialAd.isLoaded()){
            interstitialAd.show();
        }



//        // on resume turn on the flash
//            if(checkCameraHardware(MainActivity.this))
//            turnOnFlash();
    }

    @Override
    protected void onStart() {
        super.onStart();


        // on starting the app get the camera params
        getCamera();
    }



    @Override
    protected void onStop() {

        super.onStop();

        // on stop release the camera
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }


//    private void openFlashForOlderVersion() {
//
//
//        try {
//            Toast.makeText(this,"OlderVersions",Toast.LENGTH_LONG).show();
//            camera = android.hardware.Camera.open();
//            Camera.Parameters parameters = camera.getParameters();
//            if(!flashStatus){
//                Toast.makeText(this,"flashlight on",Toast.LENGTH_LONG).show();
//
//                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
//                camera.setParameters(parameters);
//                camera.startPreview();
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    btnActionFlash.setImageDrawable(getDrawable(R.drawable.flashlight_on));
//                }
//                txtVuStatus.setText("ON");
//                flashStatus = true;
//            }else{
//                Toast.makeText(this,"Else mn aya",Toast.LENGTH_LONG).show();
//                Log.i("Log","in");
//                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
//                camera.setParameters(parameters);
//                camera.stopPreview();
//                camera.release();
//                camera = null;
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    btnActionFlash.setImageDrawable(getDrawable(R.drawable.flashlight_off));
//                }
//                txtVuStatus.setText("OFF");
//                flashStatus = false;
//
//            }
//
//        } catch(Exception e) {
//            Log.e("Error", ""+e);
//        }
//
//    }
}
