package com.example.muhammadashfaq.snippet.TabFragments;


import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muhammadashfaq.snippet.CallActivity;
import com.example.muhammadashfaq.snippet.Model.FriendModel;
import com.example.muhammadashfaq.snippet.R;
import com.example.muhammadashfaq.snippet.ViewHolder.CallHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.style.FoldingCube;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class CallsFragment extends Fragment {

    RecyclerView recyclerView;
    FirebaseAuth mAuth;
    DatabaseReference friendsDbRef;
    DatabaseReference userDbRef;
    TextView noCalls;

    View mMainView;

    String currentUserUid;

    SpinKitView spinKitView;
    FirebaseRecyclerAdapter<FriendModel,CallHolder> adapter;


    public CallsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mMainView= inflater.inflate(R.layout.fragment_calls, container, false);
        recyclerView=mMainView.findViewById(R.id.recyler_view_calls);
        spinKitView=mMainView.findViewById(R.id.spin_kit_calls);
        noCalls=mMainView.findViewById(R.id.tv_no_no_calls);

        mAuth=FirebaseAuth.getInstance();
        currentUserUid=mAuth.getCurrentUser().getUid();

        friendsDbRef= FirebaseDatabase.getInstance().getReference().child("Friends").child(currentUserUid);
        friendsDbRef.keepSynced(true);
        userDbRef=FirebaseDatabase.getInstance().getReference().child("Users");
        userDbRef.keepSynced(true);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));



        return mMainView;
    }

    @Override
    public void onStart() {
        super.onStart();
        showNewContact();
    }

    private void showNewContact() {
        spinKitView.setVisibility(View.VISIBLE);
        adapter=new FirebaseRecyclerAdapter<FriendModel,CallHolder>(FriendModel.class,R.layout.fab_new_call_recyler_item
                        ,CallHolder.class,friendsDbRef) {
                    @Override
                    protected void populateViewHolder(final CallHolder viewHolder, FriendModel model, int position) {

                        final String listUId=getRef(position).getKey();
                        userDbRef.child(listUId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot)
                            {
                                if(dataSnapshot!=null) {

                                    spinKitView.setVisibility(View.GONE);

                                    final String name = dataSnapshot.child("name").getValue().toString();
                                    final String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();
                                    final String image = dataSnapshot.child("image").getValue().toString();


                                   /* if (dataSnapshot.hasChild("online")) {
                                        String online = dataSnapshot.child("online").getValue().toString();
                                        viewHolder.setOnline(online);
                                    }*/


                                    viewHolder.imgVu.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            AlertDialog.Builder alertDailog = new AlertDialog.Builder(getContext());

                                            LayoutInflater inflater = getLayoutInflater();
                                            View view = inflater.inflate(R.layout.profile_pic_custom_layout, null);
                                            ImageView imageViewProfile = view.findViewById(R.id.img_vu_user_profile);
                                            TextView username = view.findViewById(R.id.tv_user_name);

                                            Picasso.get().load(image).placeholder(R.drawable.default_avatar).into(imageViewProfile);
                                            username.setText(name);


                                            alertDailog.setView(view);

                                            AlertDialog alertDialogg = alertDailog.create();
                                            ;

                                            alertDialogg.show();
                                        }
                                    });


                                    viewHolder.setThumb(thumb_image, getContext());
                                    viewHolder.setName(name);

                                    viewHolder.imgVuAudioCall.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Toast.makeText(getContext(), "Comming soon", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    viewHolder.imgVuVideoCall.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Toast.makeText(getContext(), "Comming soon", Toast.LENGTH_SHORT).show();
                                        }
                                    });







                                   /* viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            CharSequence option[]=new CharSequence[]{"Open Profile","Send Message"};
                                            AlertDialog.Builder alertDailog=new AlertDialog.Builder(AddChatFab.this);
                                            alertDailog.setTitle("Select Options");
                                            alertDailog.setItems(option, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int i) {
                                                    if(i==0){
                                                        Intent intent=new Intent(AddChatFab.this, ProfileActivity.class);
                                                        intent.putExtra("userKey",listUId);
                                                        startActivity(intent);
                                                    }else{
                                                        Intent chatIntent = new Intent(AddChatFab.this, ChatActivity.class);
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
                                    txtVuNOfriends.setVisibility(View.VISIBLE);
                                }*/

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
                            noCalls.setVisibility(View.VISIBLE);
                        }

                    }
                };


        recyclerView.setAdapter(adapter);
    }
}
