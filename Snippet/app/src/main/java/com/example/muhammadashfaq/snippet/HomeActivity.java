package com.example.muhammadashfaq.snippet;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.muhammadashfaq.snippet.BottomFragments.CallFragment;
import com.example.muhammadashfaq.snippet.BottomFragments.PostFragment;
import com.example.muhammadashfaq.snippet.BottomFragments.ProfileFragment;
import com.example.muhammadashfaq.snippet.Common.Common;
import com.example.muhammadashfaq.snippet.SessionManager.SessionManager;
import com.example.muhammadashfaq.snippet.TabFragments.ChatFragment;
import com.example.muhammadashfaq.snippet.ViewPagerAdapter.ViewPagerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

public class HomeActivity extends AppCompatActivity {
    //TabLayout
    Toolbar toolbar;
    CoordinatorLayout coordinatorLayout;
    TabLayout tabLayout;
    ViewPager viewPager;
    BottomNavigationView bottomNavigationView;
    FirebaseAuth mAuth;
    SessionManager mSessionManager;
    String currentUid;
    FloatingActionButton fabFriends,fabChats,fabCalls,fabUsers;

    DatabaseReference mUserDbRef;

    private long backpressedtime;
    private Toast backToast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        fabFriends=findViewById(R.id.fabFriends);
        fabChats=findViewById(R.id.fabChats);
        fabCalls=findViewById(R.id.fabCalls);
        toolbar=findViewById(R.id.main_page_toolbar);
        coordinatorLayout=findViewById(R.id.coodinate_layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Chatpp");

        tabLayout=findViewById(R.id.tab_layout);
        viewPager=findViewById(R.id.view_pager);

        mAuth=FirebaseAuth.getInstance();


        if(Common.isConnected(this)){
            showGreenSnackBar();
        }else{
            showRedSnack();
        }
        //ViewPagerAdapter
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);

        //Setting up view pager with Tab layout
        tabLayout.setupWithViewPager(viewPager);



        if(mAuth.getCurrentUser()!=null){
            mUserDbRef= FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        }
        //Bottom Navigation
        bottomNavigationView=findViewById(R.id.bottom_navigation_bar);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedLisnter);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                animateFab(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                animateFab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };

    }

    private void animateFab(int position) {
        switch (position) {
            case 0:
                fabFriends.show();
                fabChats.hide();
                fabCalls.hide();
                break;
            case 1:
                fabFriends.hide();
                fabChats.show();
                fabCalls.hide();
                break;

            case 2:
                fabFriends.hide();
                fabChats.hide();
                fabCalls.show();
                break;
                default:
                    fabFriends.show();
                    fabChats.hide();
                    fabCalls.hide();
                    break;
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedLisnter=new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id=item.getItemId();

            switch (id){
                case R.id.menu_home:
                    return true;
                case R.id.menu_profile:
                    //loadFragment(new ProfileFragment());
                   return true;
                case R.id.menu_posts:
                   // loadFragment(new PostFragment());
                    return true;
                case R.id.menu_calls:
                   // loadFragment(new CallFragment());
                    return true;
                case R.id.menu_logout:
                    return true;
            }

            return false;
        }
    };

    private void logoutConfirmDailog() {
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure to logout ?");
        builder.setCancelable(false);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                sendToLoginActivty();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void sendToLoginActivty() {
        Intent intent=new Intent(HomeActivity.this,LoginActivity.class);
        mSessionManager=new SessionManager(this);
        mSessionManager.logTheUserIn(false,"","");
        startActivity(intent);
        finish();
    }
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frag_container,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home,menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView= (SearchView) menu.findItem(R.id.menu_search).getActionView();
        //SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case R.id.menu_search:
                break;


            case R.id.menu_setting:
                break;
            case R.id.menu_logout:
                logoutConfirmDailog();
                break;
            case R.id.menu_about:
                AlertDialog.Builder alertdailog=new AlertDialog.Builder(HomeActivity.this);
                LayoutInflater inflater=getLayoutInflater();
                View view=inflater.inflate(R.layout.about_us_alert_dailog_design,null);
                alertdailog.setView(view);
                alertdailog.show();
                break;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!=null){
            mUserDbRef.child("online").setValue("true");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuth.getCurrentUser()!=null){
            mUserDbRef.child("online").setValue(ServerValue.TIMESTAMP);
        }
    }

    public void onFabFreindClick(View view) {
        Intent intent=new Intent(this,AddFriend.class);
        startActivity(intent);
    }

    public void onChatAddClick(View view) {
        Intent intent=new Intent(this,AddChatFab.class);
        startActivity(intent);
    }

    public void onCallFabClick(View view) {
        Intent intent=new Intent(this,CallActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

        if(backpressedtime+2000 > System.currentTimeMillis()){
            backToast.cancel();
            super.onBackPressed();
            return;
        }else {
            backToast=Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backpressedtime=System.currentTimeMillis();

    }


    private void showRedSnack() {
        Snackbar mySnackbar = Snackbar.make(coordinatorLayout, "No internet connection.", Snackbar.LENGTH_INDEFINITE)
                .setActionTextColor(Color.WHITE).setAction("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
        View snackBarView = mySnackbar.getView();
        snackBarView.setBackgroundColor(Color.parseColor("#D32F2F"));
        mySnackbar.show();
    }
    private void showGreenSnackBar() {
        Snackbar mySnackbar = Snackbar.make(coordinatorLayout, "Connected.", Snackbar.LENGTH_SHORT)
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