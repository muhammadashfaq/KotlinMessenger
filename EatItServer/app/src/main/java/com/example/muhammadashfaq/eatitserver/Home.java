package com.example.muhammadashfaq.eatitserver;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muhammadashfaq.eatitserver.Common.Common;
import com.example.muhammadashfaq.eatitserver.Interface.ItemClickListner;
import com.example.muhammadashfaq.eatitserver.Model.Category;
import com.example.muhammadashfaq.eatitserver.Model.UserModel;
import com.example.muhammadashfaq.eatitserver.Service.ListenOrder;
import com.example.muhammadashfaq.eatitserver.ViewHolder.MenuViewHolder;
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
import com.squareup.picasso.Picasso;
import java.util.Calendar;
import java.util.UUID;


public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseStorage storage;
    StorageReference storageReference;

    //Add new menu
    MaterialEditText edtTxtName;
    Button selectImage;
    Button uploadImage;

    //Adding new category
    Category newCategory;

    private final int PICK_IMAGE_REQUEST=71;
    MaterialEditText edtTxtNewCategoryName;

    FloatingActionButton fab;
    Uri savedImageUri;
    FirebaseRecyclerAdapter<Category,MenuViewHolder> recyclerAdapter;


    TextView userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        edtTxtNewCategoryName=findViewById(R.id.edt_txt_new_item_name);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Menu Mangement");
        setSupportActionBar(toolbar);
        fab =findViewById(R.id.fab);


        //Firebase init
        database=FirebaseDatabase.getInstance();
        reference=database.getReference("Category");
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDailog();
            }
        });


        DrawerLayout drawer =findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Setting header name
      /*  View view=navigationView.getHeaderView(0);
        userName = view.findViewById(R.id.username);
        userName.setText(Common.currentUser.getName());*/
        //View init
        recyclerView=findViewById(R.id.recycler_menu);
        layoutManager=new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);

        loadMenu();

        //Starting the service
        Intent intent=new Intent(this, ListenOrder.class);
        startService(intent);
    }

    private void loadMenu()
    {
        recyclerAdapter=new FirebaseRecyclerAdapter<Category, MenuViewHolder>(
                Category.class,R.layout.menu_layout,
                MenuViewHolder.class,reference) {
            @Override
            protected void populateViewHolder(MenuViewHolder viewHolder, final Category model, int position)
            {
                viewHolder.menuName.setText(model.getName());
                Log.e("Firebase", "image url = " + model.getImage());
                Picasso.get().load(model.getImage()).into(viewHolder.imageView);
                viewHolder.setItemClickListner(new ItemClickListner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Getting menuId
                        Intent intent=new Intent(Home.this,FoodList.class);
                        intent.putExtra("CategoryId",recyclerAdapter.getRef(position).getKey());
                        startActivity(intent);
                        Toast.makeText(Home.this, "Long press item to Update or Delete", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };

        recyclerAdapter.notifyDataSetChanged();//notifiy us if data has been changed.
        recyclerView.setAdapter(recyclerAdapter);
    }

    private void selectImage() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==PICK_IMAGE_REQUEST && resultCode==Activity.RESULT_OK
        && data!=null && data.getData()!=null)
        {
            savedImageUri=data.getData();//getting uri
            selectImage.setText("Image Selected !");
        }
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
                Toast.makeText(Home.this, "Uploaded", Toast.LENGTH_SHORT).show();
                imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                {
                    @Override
                    public void onSuccess(Uri uri)
                    {
                        newCategory=new Category(edtTxtName.getText().toString(),uri.toString());
                        Toast.makeText(Home.this, ""+uri.toString(), Toast.LENGTH_SHORT).show();

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(Home.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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
                if(newCategory!=null){
                    reference.push().setValue(newCategory);

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


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id==R.id.nav_order){
            Intent intent=new Intent(Home.this,OrderStatus.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onClickSelectImage(View view)
    {
        selectImage=view.findViewById(R.id.select);
        selectImage();
    }

    public void onClickUploadImage(View view) {
        uploadImage=view.findViewById(R.id.upload);
        uploadImage();
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if(item.getTitle().equals(Common.UPDATE))
        {
            showUpdateDailog(recyclerAdapter.getRef(item.getOrder()).getKey(),recyclerAdapter.getItem(item.getOrder()));

        }else if(item.getTitle().equals(Common.DELETE)){
            deleteCategory(recyclerAdapter.getRef(item.getOrder()).getKey());
        }
        return true;
    }

    private void deleteCategory(String key) {
        reference.child(key).removeValue();
        //Snackbar.make(findViewById(R.id.activity_main),"Category "+key+" Deleted successfully",Snackbar.LENGTH_SHORT).show();
        Toast.makeText(this, "Category Deleted.", Toast.LENGTH_SHORT).show();
    }


    private void showUpdateDailog(final String key, final Category item)
    {
        final AlertDialog.Builder alertDailog=new AlertDialog.Builder(this);
        alertDailog.setTitle("Update Category");
        alertDailog.setMessage("Please Re-enter Fields");

        LayoutInflater inflater=this.getLayoutInflater();
        View view=inflater.inflate(R.layout.add_new_menu_layout,null);
        edtTxtName=view.findViewById(R.id.edt_txt_new_item_name);
        selectImage=view.findViewById(R.id.select);
        uploadImage=view.findViewById(R.id.upload);


        edtTxtName.setText(item.getName());


        selectImage.setOnClickListener(new View.OnClickListener() {
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
            public void onClick(DialogInterface dialog, int which) {

                //Update Information
                item.setName(edtTxtName.getText().toString());
                reference.child(key).setValue(item);
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
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    private void updateImage(final Category item)
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
                Toast.makeText(Home.this, "Uploaded", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(Home.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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
