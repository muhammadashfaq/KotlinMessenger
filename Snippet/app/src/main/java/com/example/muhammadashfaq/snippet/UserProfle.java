package com.example.muhammadashfaq.snippet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.muhammadashfaq.snippet.Common.Common;
import com.example.muhammadashfaq.snippet.SessionManager.SessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class UserProfle extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String currentUserId;
    DatabaseReference dbUserRef;
    StorageReference mStorgeRef;
    ProgressDialog mProgressDailog;
    Bitmap thumb_image;
    ProgressDialog mProgProfileDailog;
    Button btnAcceptReq,btnDeclineReq;

    TextView txtVuUserName,txtVuStatus;
    CircleImageView imgVuProfile;
    public static final  int GALLEY_PICK=1;




    String currentNavigaionUserId;

    DatabaseReference friendReqRef,friendsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profle);

        txtVuUserName=findViewById(R.id.txt_vu_user_name);
        txtVuStatus=findViewById(R.id.txt_vu_user_status);
        imgVuProfile=findViewById(R.id.img_vu_profile);
        btnAcceptReq=findViewById(R.id.btn_accept_friend_req);

        currentNavigaionUserId=getIntent().getStringExtra("currentUserID").toString();

        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        currentUserId=currentUser.getUid();

        dbUserRef= FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
        dbUserRef.keepSynced(true);

        //For Acceting freind Request
        friendReqRef=FirebaseDatabase.getInstance().getReference().child("Friend_Request");
        friendsRef=FirebaseDatabase.getInstance().getReference().child("Friends");

        mStorgeRef= FirebaseStorage.getInstance().getReference();





    }

    private void retriveDataOfUser() {
        dbUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name=dataSnapshot.child("name").getValue().toString();
                String status=dataSnapshot.child("status").getValue().toString();
                final String image=dataSnapshot.child("image").getValue().toString();
                String thumb_image=dataSnapshot.child("thumb_image").getValue().toString();

                txtVuUserName.setText(name);
                txtVuStatus.setText(status);

                mProgProfileDailog.dismiss();
                if(!image.equals("default")){
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.placeholder(R.drawable.default_avatar);
                    Glide.with(UserProfle.this).load(image).apply(requestOptions).into(imgVuProfile);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void onClickUpdateStatus(View view) {
        showAlertDailog();
    }

    private void showProgDailog() {
        mProgressDailog=new ProgressDialog(this);
        mProgressDailog.setTitle("Updating");
        mProgressDailog.setMessage("Please Wait We're Updating your Status");
        mProgressDailog.setCancelable(false);
        mProgressDailog.show();
    }

    private void mShowProfileProgDailog() {
        mProgProfileDailog=new ProgressDialog(this);
        mProgProfileDailog.setTitle("Loading");
        mProgProfileDailog.setMessage("Wait We're Loading your Profile...");
        mProgProfileDailog.setCancelable(false);
        mProgProfileDailog.show();
    }

    private void showAlertDailog() {
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Update Status");
        builder.setIcon(R.drawable.ic_edit_black_24dp);
        builder.setMessage("Enter New Status");
        final EditText editText=new EditText(this);
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        editText.setLayoutParams(layoutParams);
        editText.setHint("Status");
        builder.setView(editText);
        builder.setCancelable(false);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showProgDailog();
                String newStatus=editText.getText().toString();
                dbUserRef.child("status").setValue(newStatus).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            mProgressDailog.dismiss();
                            Toast.makeText(UserProfle.this,"Yahoo..Status Updated", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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

    public void onClickUpdateProfile(View view) {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==GALLEY_PICK && resultCode==Activity.RESULT_OK){
            Uri imageUri=data.getData();

            CropImage.activity(imageUri)
                    .setAspectRatio(1,1)
                    .start(this);
        }
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            if(resultCode==RESULT_OK){
                showUploadImageProgDailog();
                Uri resultUri=result.getUri();
                //Convert image to File
                File thumbe_file=new File(resultUri.getPath());
                final byte[] finalThumbByte= compressThumbnial(thumbe_file);

                StorageReference image_filepath=mStorgeRef.child("profile_images").child(currentUserId+".jpg");
                final StorageReference thumb_filepath=mStorgeRef.child("profile_images").child("thumbs").child(currentUserId+".jpg");

                image_filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            final String downloadLink=task.getResult().getDownloadUrl().toString();

                            UploadTask uploadTask=thumb_filepath.putBytes(finalThumbByte);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    String thumbDownloadUrl=task.getResult().getDownloadUrl().toString();
                                    if(task.isSuccessful()){
                                        Map updataHasMap=new HashMap<>();
                                        updataHasMap.put("image",downloadLink);
                                        updataHasMap.put("thumb_image",thumbDownloadUrl);

                                        dbUserRef.updateChildren(updataHasMap).addOnCompleteListener(new OnCompleteListener() {
                                            @Override
                                            public void onComplete(@NonNull Task task) {
                                                mProgressDailog.dismiss();
                                                Toast.makeText(UserProfle.this, "Yahoo..Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }
                                }
                            });
                        }
                    }
                });

            }
        }

    }

    private byte[] compressThumbnial(File thumbe_file) {
        Bitmap thumb_bitmap;
        byte[] thumb_bye=new byte[0];
        try {
            thumb_bitmap = new Compressor(this).
                    setMaxWidth(200)
                    .setMaxHeight(200)
                    .setQuality(75)
                    .compressToBitmap(thumbe_file);
            //Creating byte Stream
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            thumb_bye = baos.toByteArray();
            return thumb_bye;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return thumb_bye;
    }

    private void showUploadImageProgDailog() {
        mProgressDailog=new ProgressDialog(this);
        mProgressDailog.setTitle("Updating");
        mProgressDailog.setMessage("Please Wait We're Updating your Profile...");
        mProgressDailog.setCancelable(false);
        mProgressDailog.show();

    }


}
