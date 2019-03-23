package com.example.muhammadashfaq.eatit;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muhammadashfaq.eatit.Common.Common;
import com.example.muhammadashfaq.eatit.Database.Database;
import com.example.muhammadashfaq.eatit.Interface.ItemClickListner;
import com.example.muhammadashfaq.eatit.Model.Category;
import com.example.muhammadashfaq.eatit.Pakage.ListenOrder;
import com.example.muhammadashfaq.eatit.SessionManager.SessionManager;
import com.example.muhammadashfaq.eatit.ViewHolder.MenuViewHolder;
import com.facebook.FacebookSdk;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.paperdb.Paper;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Firebase refrence
    FirebaseDatabase firebaseDatabase;
    DatabaseReference category;

    FirebaseRecyclerAdapter<Category,MenuViewHolder> recyclerAdapter;

    TextView userName;
    FloatingActionButton floatingActionButton;

    SpinKitView spinKitView;

    //Recycler menu
    RecyclerView recyclerMenu;
    RecyclerView.LayoutManager layoutManager;
    Toolbar toolbar;

    //Add favorites
    Database localDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        FacebookSdk.sdkInitialize(getApplicationContext());
        Toolbar toolbar = findViewById(R.id.toolbar);
        spinKitView=findViewById(R.id.spin_kit_home);
        NavigationView navigationView = findViewById(R.id.nav_view);
        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);

        //Firebase init
        firebaseDatabase = FirebaseDatabase.getInstance();
        category = firebaseDatabase.getReference("Category");
        category.keepSynced(true);

        //Add to favrotes
        localDB=new Database(this);

        //Init paerp
        Paper.init(this);


        //For sharing food on facebook
        printKeyHash();

        //Fab

        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, Cart.class);
                startActivity(intent);
            }
        });

        //NavigationView

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);


        //Setting up Name of the user in header

        userName = navigationView.getHeaderView(0).findViewById(R.id.txt_vu_name);
        userName.setText(Common.username);


        //Load menu

        recyclerMenu = findViewById(R.id.recycler_menu);
        recyclerMenu.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this,2);
        recyclerMenu.setLayoutManager(layoutManager);

        //Will get data for recyclerView from Firebase by using Adapter.
        if(Common.isConnectedtoInternet(getBaseContext())) {
            loadMenu();
        }else{
            Toast.makeText(Home.this, "Check your internet connection !!", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent service=new Intent(Home.this, ListenOrder.class);
        startService(service);

    }

    private void printKeyHash() {
        try {
            PackageInfo info=getPackageManager().getPackageInfo("com.example.muhammadashfaq.eatit",
                    PackageManager.GET_SIGNATURES);
            for(Signature signature:info.signatures){
                MessageDigest ds=MessageDigest.getInstance("SHA");
                ds.update(signature.toByteArray());
                Log.d("Keyhash",Base64.encodeToString(ds.digest(),Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private void loadMenu() {

           /* final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading");
            progressDialog.setCancelable(true);
            progressDialog.show();*/
           spinKitView.setVisibility(View.VISIBLE);

            recyclerAdapter = new FirebaseRecyclerAdapter<Category,MenuViewHolder>
                    (Category.class,R.layout.menu_layout, MenuViewHolder.class,category.getRef()) {
                @Override
                protected void populateViewHolder(final MenuViewHolder viewHolder, final Category model, final int position) {
                    spinKitView.setVisibility(View.GONE);
                    viewHolder.menuName.setText(model.getName());
                    Picasso.get().load(model.getImage()).placeholder(R.drawable.placeholder)
                            .into(viewHolder.imageView);



                    //Add to favorites
                    //Add to Favorites
                    if(localDB.isFavorite(recyclerAdapter.getRef(position).getKey())){
                        viewHolder.imageViewfavMenu.setImageResource(R.drawable.ic_favorite_black_24dp);
                    }

                    //change status of favorite
                    viewHolder.imageViewfavMenu.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!localDB.isFavorite(recyclerAdapter.getRef(position).getKey())){
                                localDB.addToFavorites(recyclerAdapter.getRef(position).getKey());
                                viewHolder.imageViewfavMenu.setImageResource(R.drawable.ic_favorite_black_24dp);
                                Toast.makeText(Home.this, " "+ model.getName()+" added to Favorties", Toast.LENGTH_SHORT).show();

                            }else{
                                localDB.removeFromFavorites(recyclerAdapter.getRef(position).getKey());
                                viewHolder.imageViewfavMenu.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                                Toast.makeText(Home.this, " "+ model.getName()+" removed from Favorties", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    final Category clickitem = model;
                    viewHolder.setItemClickListner(new ItemClickListner() {
                        @Override
                        public void onClick(View view, int position, boolean isLongClick) {

                            //Getting category id
                            Intent menuId = new Intent(Home.this, FoodList.class);
                            //Because we just want key to only item key will be send
                            menuId.putExtra("CategoryId", recyclerAdapter.getRef(position).getKey());
                            menuId.putExtra("CategoryName", model.getName());
                            startActivity(menuId);
                        }
                    });


                }
            };
            recyclerAdapter.notifyDataSetChanged();
            recyclerMenu.setAdapter(recyclerAdapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            moveTaskToBack(true);
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

        if(item.getItemId()==R.id.refresh){
            if(Common.isConnectedtoInternet(getBaseContext())) {
                loadMenu();
            }else{
                Toast.makeText(Home.this, "Check your internet connection !!", Toast.LENGTH_SHORT).show();
            }

        }else if(item.getItemId()==R.id.logout){
            SessionManager sessionManager=new SessionManager(getBaseContext());
            sessionManager.logTheUserIn(false,"","");
            Intent intent=new Intent(Home.this,MainActivity.class);
            startActivity(intent);
            finish();

        }

        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (id == R.id.nav_menu)
        {
            toolbar.setTitle("Menu");

        } else if (id == R.id.nav_cart)
        {
            toolbar.setTitle("Cart");
            Intent intent=new Intent(Home.this,Cart.class);
            startActivity(intent);
        } else if (id == R.id.nav_orders)
        {
            toolbar.setTitle("Order");
            Intent intent=new Intent(Home.this,Order.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout)
        {
            SessionManager sessionManager=new SessionManager(getBaseContext());
            sessionManager.logTheUserIn(false,"","");
            Intent intent=new Intent(Home.this,MainActivity.class);
            startActivity(intent);
            finish();
        } else {

        }

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
