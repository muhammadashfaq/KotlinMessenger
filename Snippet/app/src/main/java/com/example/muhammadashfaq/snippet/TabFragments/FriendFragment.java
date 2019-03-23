package com.example.muhammadashfaq.snippet.TabFragments;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muhammadashfaq.snippet.ChatActivity;
import com.example.muhammadashfaq.snippet.Model.FriendModel;
import com.example.muhammadashfaq.snippet.ProfileActivity;
import com.example.muhammadashfaq.snippet.R;
import com.example.muhammadashfaq.snippet.ViewHolder.FriendViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendFragment extends Fragment {

    RecyclerView recyclerView;
    FirebaseAuth mAuth;
    DatabaseReference friendsDbRef;
    DatabaseReference userDbRef;

    View mMainView;

    String currentUserUid;
    TextView txtVuNOfriends;
    ProgressDialog mProgDailog;

    SpinKitView spinKitView;
    FirebaseRecyclerAdapter<FriendModel,FriendViewHolder> adapter;



    public FriendFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         mMainView=inflater.inflate(R.layout.fragment_friend, container, false);
        recyclerView=mMainView.findViewById(R.id.all_friends_recycler_view);
        spinKitView=mMainView.findViewById(R.id.spin_kit_friends);
        txtVuNOfriends=mMainView.findViewById(R.id.tv_no_friends);


         mAuth=FirebaseAuth.getInstance();
         currentUserUid=mAuth.getCurrentUser().getUid();

         friendsDbRef= FirebaseDatabase.getInstance().getReference().child("Friends").child(currentUserUid);
         friendsDbRef.keepSynced(true);
         userDbRef=FirebaseDatabase.getInstance().getReference().child("Users");
         userDbRef.keepSynced(true);

       /* DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.horizental_devider));
        recyclerView.addItemDecoration(divider);*/

         recyclerView.setHasFixedSize(true);
         recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

         return mMainView;
    }


    @Override
    public void onStart() {
        super.onStart();

       // mShowProgDailog();

        spinKitView.setVisibility(View.VISIBLE);
        adapter=new
                FirebaseRecyclerAdapter<FriendModel, FriendViewHolder>(FriendModel.class,R.layout.friends_recyler_item
                ,FriendViewHolder.class,friendsDbRef) {
            @Override
            protected void populateViewHolder(final FriendViewHolder viewHolder, FriendModel model, int position) {
                viewHolder.setDate(model.getDate());
            // mProgDailog.dismiss();
                final String listUId=getRef(position).getKey();
                userDbRef.child(listUId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        if(dataSnapshot!=null){

                            spinKitView.setVisibility(View.GONE);
                            final String name=dataSnapshot.child("name").getValue().toString();
                            final String thumb_image=dataSnapshot.child("thumb_image").getValue().toString();
                            final String image=dataSnapshot.child("image").getValue().toString();


                            if(dataSnapshot.hasChild("online")){
                                String online=  dataSnapshot.child("online").getValue().toString();
                                viewHolder.setOnline(online);
                            }


                            viewHolder.imgVu.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AlertDialog.Builder alertDailog=new AlertDialog.Builder(getContext());

                                    LayoutInflater inflater=getLayoutInflater();
                                    View view=inflater.inflate(R.layout.profile_pic_custom_layout,null);
                                    ImageView imageViewProfile=view.findViewById(R.id.img_vu_user_profile);
                                    TextView username=view.findViewById(R.id.tv_user_name);

                                    Picasso.get().load(image).placeholder(R.drawable.default_avatar).into(imageViewProfile);
                                    username.setText(name);


                                    alertDailog.setView(view);

                                    AlertDialog alertDialogg = alertDailog.create();;

                                    alertDialogg.show();
                                }
                            });

                                viewHolder.setThumb(thumb_image,getActivity());
                                viewHolder.setName(name);




                            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    CharSequence option[]=new CharSequence[]{"Open Profile","Send Message"};
                                    AlertDialog.Builder alertDailog=new AlertDialog.Builder(getContext());
                                    alertDailog.setTitle("Select Options");
                                    alertDailog.setItems(option, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int i) {
                                            if(i==0){
                                                Intent intent=new Intent(getContext(), ProfileActivity.class);
                                                intent.putExtra("userKey",listUId);
                                                startActivity(intent);
                                            }else{
                                                Intent chatIntent = new Intent(getContext(), ChatActivity.class);
                                                chatIntent.putExtra("UserId", listUId);
                                                chatIntent.putExtra("user_name", name);
                                                chatIntent.putExtra("user_image",thumb_image);
                                                startActivity(chatIntent);
                                            }
                                        }
                                    });
                                    alertDailog.show();
                                }
                            });
                        }else {

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

                    @Override
                    protected void onDataChanged() {
                        super.onDataChanged();
                        if(adapter.getItemCount()==0){
                            spinKitView.setVisibility(View.GONE);
                            txtVuNOfriends.setVisibility(View.VISIBLE);
                        }
                    }
                };

        adapter.notifyDataSetChanged();
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
