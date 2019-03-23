package com.example.muhammadashfaq.snippet.TabFragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.muhammadashfaq.snippet.Common.Common;
import com.example.muhammadashfaq.snippet.Model.UserModel;
import com.example.muhammadashfaq.snippet.ProfileActivity;
import com.example.muhammadashfaq.snippet.R;
import com.example.muhammadashfaq.snippet.ViewHolder.UserViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class UsersFragment extends Fragment {
    RecyclerView recyclerView;
    View mMainView;

    DatabaseReference mdbRef;
    ProgressDialog mProgDailog;



    public UsersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView = inflater.inflate(R.layout.fragment_users, container, false);
        recyclerView = mMainView.findViewById(R.id.all_users_recycler_view);

        mdbRef=FirebaseDatabase.getInstance().getReference().child("Users");


        DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.horizental_devider));
        recyclerView.addItemDecoration(divider);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if(!Common.isConnected(getContext())){
            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

        return mMainView;
    }

    @Override
    public void onStart() {
        super.onStart();
           // mShowProgDailog();
            retriveUsersDate();
    }

    private void retriveUsersDate() {
        FirebaseRecyclerAdapter<UserModel,UserViewHolder> adapter=new
                FirebaseRecyclerAdapter<UserModel, UserViewHolder>(
                        UserModel.class,
                        R.layout.custom_recycler_view_item_desing,
                        UserViewHolder.class,
                        mdbRef) {
            @Override
            protected void populateViewHolder(UserViewHolder viewHolder, final UserModel model, int position) {
                //mProgDailog.dismiss();
                viewHolder.setName(model.getName());
                viewHolder.setStatus(model.getStatus());
                viewHolder.setThumb(model.getThumb_image(),getContext());


                 final String  itemKey=getRef(position).getKey();

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getContext(), ProfileActivity.class);
                        intent.putExtra("userKey",itemKey);
                        startActivity(intent);
                    }
                });

            }
        };

        recyclerView.setAdapter(adapter);
    }


    private void mShowProgDailog() {
        mProgDailog=new ProgressDialog(getContext());
        mProgDailog.setTitle("Loading");
        mProgDailog.setMessage("Loading All Suggested Friends...");
        mProgDailog.setCancelable(false);
        mProgDailog.show();
    }


}
