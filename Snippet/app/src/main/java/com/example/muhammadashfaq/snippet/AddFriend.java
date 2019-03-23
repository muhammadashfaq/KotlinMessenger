package com.example.muhammadashfaq.snippet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.example.muhammadashfaq.snippet.Common.Common;
import com.example.muhammadashfaq.snippet.Model.UserModel;
import com.example.muhammadashfaq.snippet.ViewHolder.FriendFabHolder;
import com.example.muhammadashfaq.snippet.ViewHolder.UserViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddFriend extends AppCompatActivity {

    RecyclerView recyclerView;
    View mMainView;

    DatabaseReference mdbRef;
    ProgressDialog mProgDailog;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        recyclerView=findViewById(R.id.recyler_view);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Friend");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mdbRef= FirebaseDatabase.getInstance().getReference().child("Users");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(AddFriend.this));

        if(!Common.isConnected(AddFriend.this)){
            Toast.makeText(AddFriend.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

        retriveUsersDate();

    }
    private void retriveUsersDate() {
        FirebaseRecyclerAdapter<UserModel,FriendFabHolder> adapter=new
                FirebaseRecyclerAdapter<UserModel, FriendFabHolder>(
                        UserModel.class,
                        R.layout.add_fab_friends_item,
                        FriendFabHolder.class,
                        mdbRef) {
                    @Override
                    protected void populateViewHolder(FriendFabHolder viewHolder, UserModel model, int position) {
                        //mProgDailog.dismiss();
                        viewHolder.setName(model.getName());
                        viewHolder.setStatus(model.getStatus());
                        viewHolder.setThumb(model.getThumb_image(),AddFriend.this);


                        final String  itemKey=getRef(position).getKey();

                        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(AddFriend.this, ProfileActivity.class);
                                intent.putExtra("userKey",itemKey);
                                startActivity(intent);
                            }
                        });

                    }
                };

        recyclerView.setAdapter(adapter);
    }


    private void mShowProgDailog() {
        mProgDailog=new ProgressDialog(AddFriend.this);
        mProgDailog.setTitle("Loading");
        mProgDailog.setMessage("Loading All Suggested Friends...");
        mProgDailog.setCancelable(false);
        mProgDailog.show();
    }
}

