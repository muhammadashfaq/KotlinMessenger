package com.example.serverapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.serverapp.Interface.ItemClickListner;
import com.example.serverapp.Model.Category;
import com.example.serverapp.ViewHolder.CategoryHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class HomeActivity extends AppCompatActivity {
    FirebaseDatabase db;
    DatabaseReference dbRef;
    StorageReference storageReference;
    SpinKitView spinKitView;
    CoordinatorLayout coordinatorLayout;
    FloatingActionButton fab;
    MaterialEditText edtTxtName;
    Button selectImage;
    Button uploadImage;
    private final int PICK_IMAGE_REQUEST=71;
    FirebaseRecyclerAdapter<Category,CategoryHolder> adapter;
    Uri savedImageUri,downloadUrl;

    Category newCategory;

    //Recycler menu
    RecyclerView recyclerMenu;
    RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        intiazlizations();

        FirebaseStorage storage=FirebaseStorage.getInstance();
        storageReference= storage.getReference();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDailog();
            }
        });
        dbRef = FirebaseDatabase.getInstance().getReference("Category");
        loadData();

        recyclerMenu.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(HomeActivity.this,2);
        recyclerMenu.setLayoutManager(layoutManager);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==PICK_IMAGE_REQUEST && resultCode== Activity.RESULT_OK
                && data!=null && data.getData()!=null)
        {
            savedImageUri=data.getData();//getting uri
            selectImage.setText("Image Selected !");
            selectImage.setEnabled(false);
        }
    }

    private void selectImage() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);

    }

    private void showDailog() {
        final AlertDialog.Builder alertDailog=new AlertDialog.Builder(this);
        alertDailog.setTitle("Add new Category");
        alertDailog.setMessage("Please fill all the fields");

        LayoutInflater inflater=this.getLayoutInflater();
        View view=inflater.inflate(R.layout.add_new_menu_layout,null);
        edtTxtName=view.findViewById(R.id.edt_txt_new_item_name);

        alertDailog.setView(view);
        alertDailog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        alertDailog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String categoryName= edtTxtName.getText().toString();
                if(!categoryName.isEmpty()){
                  newCategory=new Category(categoryName,downloadUrl.toString());
                  DatabaseReference dbRef=FirebaseDatabase.getInstance().getReference("Category");

                  dbRef.push().setValue(newCategory);
                    Toast.makeText(HomeActivity.this, "all ok", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(HomeActivity.this, "not ok", Toast.LENGTH_SHORT).show();
                }

            }
        });

        alertDailog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDailog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDailog.show();

    }

    private void loadData() {
        spinKitView.setVisibility(View.VISIBLE);


        adapter=new FirebaseRecyclerAdapter<Category, CategoryHolder>(Category.class,R.layout.recyler_item_desing,CategoryHolder.class,dbRef.getRef()) {
            @Override
            protected void populateViewHolder(CategoryHolder viewHolder, final Category model, int position) {

                spinKitView.setVisibility(View.GONE);

                viewHolder.menuName.setText(model.getName());
                Picasso.get().load(model.getImage()).placeholder(R.drawable.placehlder).into(viewHolder.imageView);

                viewHolder.setItemClickListner(new ItemClickListner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(HomeActivity.this, model.getName(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        };
        adapter.notifyDataSetChanged();
        recyclerMenu.setAdapter(adapter);
    }

    private void intiazlizations() {
        recyclerMenu=findViewById(R.id.mvRecyclerView);
        spinKitView=findViewById(R.id.spinkit_home);
        coordinatorLayout=findViewById(R.id.linearLayout);
        fab=findViewById(R.id.fab);


    }

    public void onClickSelectImage(View view) {
        selectImage=view.findViewById(R.id.select);
        selectImage();
    }

    public void onClickUploadImage(View view) {
        uploadImage=view.findViewById(R.id.upload);
        uploadImage();
    }

    private void uploadImage() {

        final ProgressDialog progressDialog=new ProgressDialog(HomeActivity.this);
        progressDialog.setMessage("Uploading Image");
        progressDialog.show();
        String image= UUID.randomUUID().toString();
        final StorageReference imageFolder=storageReference.child("CategoriesImages/"+image);

        imageFolder.putFile(savedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
                Toast.makeText(HomeActivity.this, "uploaded", Toast.LENGTH_SHORT).show();
                imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                {
                    @Override
                    public void onSuccess(Uri uri)
                    {

                        downloadUrl=uri;


                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(HomeActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress=(100 * taskSnapshot.getBytesTransferred()
                        / taskSnapshot.getTotalByteCount());
                progressDialog.setMessage(progress+"%");

            }
        });

    }
}
