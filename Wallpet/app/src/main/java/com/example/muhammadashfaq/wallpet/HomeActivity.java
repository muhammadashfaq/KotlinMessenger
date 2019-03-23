package com.example.muhammadashfaq.wallpet;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.muhammadashfaq.wallpet.ViewPagerAdapter.ViewPagerAdapter;

public class HomeActivity extends AppCompatActivity {



    Toolbar toolbar;
    NavigationView navigationView;
    TabLayout tabLayout;
    ViewPager viewPager;
    AlertDialog.Builder alertDailog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);



        //TabLayout
        tabLayout=findViewById(R.id.tab_layout);
        viewPager=findViewById(R.id.view_pager);
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        //NavigationView
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();


                switch (id){
                    case R.id.nav_collections:
                        Intent intent=new Intent(HomeActivity.this,CollectionActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_loved:
                        Intent intentt=new Intent(HomeActivity.this,LovedActivity.class);
                        startActivity(intentt);
                        break;
                    case R.id.nav_downloads:
                        Intent intenttt=new Intent(HomeActivity.this,DownloadsActivity.class);
                        startActivity(intenttt);
                        break;
                    case R.id.nav_upload:
                    Intent upload=new Intent(HomeActivity.this,UploadActivity.class);
                    startActivity(upload);
                    break;
                    case R.id.nav_facebook:

                        break;
                    case R.id.nav_twitter:

                        break;
                    case R.id.nav_whatsapp:

                        break;
                }

                DrawerLayout drawer =  findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case R.id.nav_search:
                break;
            case R.id.nav_settings:
                break;
            case R.id.nav_rate_us:
                break;
            case R.id.nav_about:
                alertDailog=new AlertDialog.Builder(HomeActivity.this);
                LayoutInflater inflater=getLayoutInflater();
                View view=inflater.inflate(R.layout.about_us_custom_design,null);
                alertDailog.setView(view);
                alertDailog.show();
                break;
        }
        return true;
    }
}
