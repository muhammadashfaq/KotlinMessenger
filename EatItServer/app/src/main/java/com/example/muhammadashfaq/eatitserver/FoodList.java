package com.example.muhammadashfaq.eatitserver;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.example.muhammadashfaq.eatitserver.Common.Common;
import com.example.muhammadashfaq.eatitserver.Interface.ItemClickListner;
import com.example.muhammadashfaq.eatitserver.Model.Category;
import com.example.muhammadashfaq.eatitserver.Model.Food;
import com.example.muhammadashfaq.eatitserver.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class FoodList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FloatingActionButton fab;

    FirebaseDatabase db;
    DatabaseReference foodList;

    FirebaseStorage storage;
    StorageReference storageReference;

    String categoryId="";
    FirebaseRecyclerAdapter<Food,FoodViewHolder> foodAdapter;

    Uri savedImageUri;
    RelativeLayout relativeLayout;
    Food newFood;


    //Add new Food
    MaterialEditText edtTxtDlgName,edtTxtDlgDescription,edtTxtDlgDiscount,edtTxtDlPrice;
    Button selcetImage,uploadImage;


    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);
        relativeLayout=findViewById(R.id.foodRootLayout);

        //
        recyclerView=findViewById(R.id.recycler_food_list);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Firebase init
        db=FirebaseDatabase.getInstance();
        foodList=db.getReference("Foods");
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();


        fab=findViewById(R.id.fab_food_list);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddFoodDailog();
            }
        });

        categoryId=getIntent().getStringExtra("CategoryId");
        if(!categoryId.isEmpty()){
            loadFood(categoryId);
        }

    }

    private void showAddFoodDailog()
    {
        final AlertDialog.Builder alertDailog=new AlertDialog.Builder(this);
        alertDailog.setTitle("Add new Food");
        alertDailog.setMessage("Please fill all the fields");

        LayoutInflater inflater=this.getLayoutInflater();
        View view=inflater.inflate(R.layout.add_new_food,null);
        //init
        edtTxtDlgName=view.findViewById(R.id.edt_txt_new_item_name);
        edtTxtDlgDescription=view.findViewById(R.id.edt_txt_new_item_description);
        edtTxtDlgDiscount=view.findViewById(R.id.edt_txt_new_item_discount);
        edtTxtDlPrice=view.findViewById(R.id.edt_txt_new_item_price);
        selcetImage=view.findViewById(R.id.select);
        uploadImage=view.findViewById(R.id.upload);

        selcetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selcetImage();
            }
        });
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });



        alertDailog.setView(view);
        alertDailog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        alertDailog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(newFood!=null)
                {
                    Toast.makeText(FoodList.this, "Food added", Toast.LENGTH_SHORT).show();
                    foodList.push().setValue(newFood);
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

    private void uploadImage() {
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Uploading Image");
        progressDialog.show();

        String image= UUID.randomUUID().toString();
        final StorageReference imageFolder=storageReference.child("images/"+image);
        imageFolder.putFile(savedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
                Toast.makeText(FoodList.this, "Uploaded", Toast.LENGTH_SHORT).show();
                imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                {
                    @Override
                    public void onSuccess(Uri uri)
                    {
                        newFood=new Food();
                        newFood.setName(edtTxtDlgName.getText().toString());
                        newFood.setDescription(edtTxtDlgDescription.getText().toString());
                        newFood.setDiscount(edtTxtDlgDiscount.getText().toString());
                        newFood.setPrice(edtTxtDlPrice.getText().toString());
                        newFood.setMenuId(categoryId);
                        newFood.setImage(uri.toString());

                        Toast.makeText(FoodList.this, ""+uri.toString(), Toast.LENGTH_SHORT).show();

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(FoodList.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress=(100.0 * taskSnapshot.getBytesTransferred()
                        / taskSnapshot.getTotalByteCount());
                progressDialog.setMessage("Uploaded "+progress+" %");

            }
        });
    }

    private void selcetImage() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, Common.PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==Common.PICK_IMAGE_REQUEST && resultCode== Activity.RESULT_OK
                && data!=null && data.getData()!=null)
        {
            savedImageUri=data.getData();//getting uri
            selcetImage.setText("Image Selected !");
        }
    }

    private void loadFood(String categoryId)
    {
        String key = foodList.child("Foods").push().getKey();

        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading Sub Menu");
        progressDialog.setCancelable(true);
        progressDialog.show();

        foodAdapter=new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class,R.layout.food_list_layout,FoodViewHolder.class,
                foodList.orderByChild("menuId").equalTo(categoryId))
        {
            @Override
            protected void populateViewHolder(FoodViewHolder viewHolder, final Food model, int position)
            {
                progressDialog.dismiss();
                viewHolder.foodName.setText(model.getName());
                Picasso.get().load(model.getImage()).into(viewHolder.foodImageView);
                viewHolder.setItemClickListnerFood(new ItemClickListner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick)
                    {

                        Toast.makeText(FoodList.this, "Long press item to Update or Delete", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };

        foodAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(foodAdapter);


    }


    @Override
    public boolean onContextItemSelected(MenuItem item)
    {


        if(item.getTitle().equals(Common.UPDATE))
        {
            showUpdateDailog(foodAdapter.getRef(item.getOrder()).getKey(),foodAdapter.getItem(item.getOrder()));

        }else if(item.getTitle().equals(Common.DELETE)){
            deleteCategory(foodAdapter.getRef(item.getOrder()).getKey());
        }else {

        }
        return true;
    }

    private void deleteCategory(String key)
    {
        foodList.child(key).removeValue();
        //Snackbar.make(relativeLayout,"Category "+key+" Deleted successfully",Snackbar.LENGTH_SHORT).show();
        Toast.makeText(this, "Category Deleted.", Toast.LENGTH_SHORT).show();
    }
    private void showUpdateDailog(final String key, final Food item)
    {
        final AlertDialog.Builder alertDailog=new AlertDialog.Builder(this);
        alertDailog.setTitle("Update Category");
        alertDailog.setMessage("Please Re-enter Fields");

        LayoutInflater inflater=this.getLayoutInflater();
        View view=inflater.inflate(R.layout.add_new_food,null);

        edtTxtDlgName=view.findViewById(R.id.edt_txt_new_item_name);
        edtTxtDlgDescription=view.findViewById(R.id.edt_txt_new_item_description);
        edtTxtDlgDiscount=view.findViewById(R.id.edt_txt_new_item_discount);
        edtTxtDlPrice=view.findViewById(R.id.edt_txt_new_item_price);
        selcetImage=view.findViewById(R.id.select);
        uploadImage=view.findViewById(R.id.upload);

        edtTxtDlgName.setText(item.getName());
        edtTxtDlgDescription.setText(item.getDescription());
        edtTxtDlgDiscount.setText(item.getDiscount());
        edtTxtDlPrice.setText(item.getPrice());


        selcetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectNewImage();
            }
        });
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateImage(item);
            }
        });

        alertDailog.setView(view);
        alertDailog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        alertDailog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if(item!=null)
                {
                    Toast.makeText(FoodList.this, "chlaaa", Toast.LENGTH_SHORT).show();

                    item.setName(edtTxtDlgName.getText().toString());
                    item.setDescription(edtTxtDlgDescription.getText().toString());
                    item.setDiscount(edtTxtDlgDiscount.getText().toString());
                    item.setPrice(edtTxtDlPrice.getText().toString());



                    foodList.child(key).setValue(item);
                    Toast.makeText(FoodList.this, "Item Updated Successfully", Toast.LENGTH_SHORT).show();
                    //Snackbar.make(relativeLayout,"Item "+newFood.getName()+"Updated Succesfully.",Snackbar.LENGTH_LONG).show();
            }

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

    private void selectNewImage()
    {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,Common.PICK_IMAGE_REQUEST);
    }

    private void updateImage(final Food item)
    {
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Uploading Image");
        progressDialog.show();
        String image= UUID.randomUUID().toString();
        final StorageReference imageFolder=storageReference.child("images/"+image);
        imageFolder.putFile(savedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
                Toast.makeText(FoodList.this, "Uploaded", Toast.LENGTH_SHORT).show();
                imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        item.setImage(uri.toString());

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(FoodList.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress=(100.0 * taskSnapshot.getBytesTransferred()
                        / taskSnapshot.getTotalByteCount());
                progressDialog.setMessage("Uploaded "+progress+" %");

            }
        });
    }
}
