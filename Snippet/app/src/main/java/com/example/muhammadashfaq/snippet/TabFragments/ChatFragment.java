package com.example.muhammadashfaq.snippet.TabFragments;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muhammadashfaq.snippet.ChatActivity;
import com.example.muhammadashfaq.snippet.Common.Common;
import com.example.muhammadashfaq.snippet.GetTimeAgo;
import com.example.muhammadashfaq.snippet.Model.ConversationModel;
import com.example.muhammadashfaq.snippet.R;
import com.example.muhammadashfaq.snippet.ViewHolder.ConversationViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class ChatFragment extends Fragment {
    RecyclerView recyclerView;
    View mMainView;

    private DatabaseReference mConvsRef,mMsgDbRef,mUserRef;
    private FirebaseAuth mAuth;
    String mCurrentUid;

    ProgressDialog mProgDailog;


    TextView txtVuNoChats;

    SpinKitView spinKitView;
    FirebaseRecyclerAdapter<ConversationModel,ConversationViewHolder> adapter;




    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView= inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerView=mMainView.findViewById(R.id.all_chat_recycler_view);
        txtVuNoChats=mMainView.findViewById(R.id.tv_no_chatss);
        spinKitView=mMainView.findViewById(R.id.spin_kit_chats);
        mAuth=FirebaseAuth.getInstance();
        mCurrentUid=mAuth.getCurrentUser().getUid();

        mConvsRef= FirebaseDatabase.getInstance().getReference().child("Chats").child(mCurrentUid);
        mConvsRef.keepSynced(true);
        mUserRef=FirebaseDatabase.getInstance().getReference().child("Users");
        mMsgDbRef=FirebaseDatabase.getInstance().getReference().child("messages").child(mCurrentUid);
        mMsgDbRef.keepSynced(true);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);

        /*DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.horizental_devider));
        recyclerView.addItemDecoration(divider);*/

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        return mMainView;
        }


    @Override
    public void onStart() {
        super.onStart();

        spinKitView.setVisibility(View.VISIBLE);
        Query conversatinQuery=mConvsRef.orderByChild("timestamp");
        //mShowProgDailog();
         adapter=new
                FirebaseRecyclerAdapter<ConversationModel, ConversationViewHolder>(ConversationModel.class,
                        R.layout.chat_single_item_design,ConversationViewHolder.class,conversatinQuery) {
            @Override
            protected void populateViewHolder(final ConversationViewHolder viewHolder, final ConversationModel model, int position) {

                spinKitView.setVisibility(View.GONE);
               // mProgDailog.dismiss();
                final String listUserKey=getRef(position).getKey();
                mUserRef.child(listUserKey).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot!=null){
                            final String username=dataSnapshot.child("name").getValue().toString();
                           final String thumb=dataSnapshot.child("thumb_image").getValue().toString();
                           final String image=dataSnapshot.child("image").getValue().toString();

                            viewHolder.setUserName(username);
                            viewHolder.setImgVuThumb(thumb);

                            viewHolder.imgVuThumb.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    AlertDialog.Builder alertDailog=new AlertDialog.Builder(getContext());

                                    LayoutInflater inflater=getLayoutInflater();
                                    View view=inflater.inflate(R.layout.profile_pic_custom_layout,null);
                                    ImageView imageViewProfile=view.findViewById(R.id.img_vu_user_profile);
                                    TextView name=view.findViewById(R.id.tv_user_name);

                                    Picasso.get().load(image).placeholder(R.drawable.default_avatar).networkPolicy(NetworkPolicy.OFFLINE).into(imageViewProfile);
                                    name.setText(username);


                                    alertDailog.setView(view);

                                    AlertDialog alertDialogg = alertDailog.create();;

                                    alertDialogg.show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                mConvsRef.child(listUserKey).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                        if(dataSnapshot!=null){
                            if(dataSnapshot.hasChild("timestamp")){
                                String time=dataSnapshot.child("timestamp").getValue().toString();
                                final GetTimeAgo getTimeAgo=new GetTimeAgo();
                                long lasttime=Long.parseLong(time);
                                String lasttimeSeen=GetTimeAgo.getTimeAgo(lasttime,getContext());
                                viewHolder.txtVuTime.setText(lasttimeSeen);

                                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        mUserRef.child(listUserKey).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if(dataSnapshot!=null){
                                                    String name=dataSnapshot.child("name").getValue().toString();
                                                    String thumb=dataSnapshot.child("thumb_image").getValue().toString();

                                                    Intent chatIntent = new Intent(getContext(), ChatActivity.class);
                                                    chatIntent.putExtra("UserId", listUserKey);
                                                    chatIntent.putExtra("user_name",name);
                                                    //Toast.makeText(getContext(), thumb, Toast.LENGTH_SHORT).show();
                                                    chatIntent.putExtra("user_image",thumb);
                                                    startActivity(chatIntent);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                    }
                                });
                            }
                        }else{
                            Toast.makeText(getActivity().getBaseContext(), "NO Chats", Toast.LENGTH_SHORT).show();
                            txtVuNoChats.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                Query lastMessageQuery=mMsgDbRef.child(listUserKey).limitToLast(1);
                lastMessageQuery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        if(dataSnapshot!=null){
                            if(dataSnapshot.hasChild("message")){
                                String data=dataSnapshot.child("message").getValue().toString();
                                viewHolder.setMessage(data,model.isSeen());
                            }else{
                                //Toast.makeText(getContext(), "No message child", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(getContext(), "datasnapshot is null", Toast.LENGTH_SHORT).show();
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
                            txtVuNoChats.setVisibility(View.VISIBLE);
                        }
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    private void mShowProgDailog() {
        mProgDailog=new ProgressDialog(getContext());
        mProgDailog.setTitle("Loading");
        mProgDailog.setMessage("Loading All Suggested Friends...");
        mProgDailog.setCancelable(false);
        mProgDailog.show();
    }
}
