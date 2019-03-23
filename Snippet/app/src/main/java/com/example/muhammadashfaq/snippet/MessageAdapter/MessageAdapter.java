package com.example.muhammadashfaq.snippet.MessageAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.muhammadashfaq.snippet.ChatActivity;
import com.example.muhammadashfaq.snippet.GetTimeAgo;
import com.example.muhammadashfaq.snippet.Model.MessageModel;
import com.example.muhammadashfaq.snippet.R;
import com.example.muhammadashfaq.snippet.ViewHolder.MessgeViewHolder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;


public class MessageAdapter extends RecyclerView.Adapter<MessgeViewHolder>{

    private List<MessageModel> mMessageList;
    FirebaseAuth mAuth;

    public MessageAdapter(List<MessageModel> mMessageList) {
        this.mMessageList = mMessageList;
        mAuth=FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public MessgeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.message_single_layout,parent,false);
        Context context=parent.getContext();
        return new MessgeViewHolder(view,context);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final MessgeViewHolder holder, int position) {


        String currrentUserID=mAuth.getCurrentUser().getUid();
        MessageModel messageModel=mMessageList.get(position);
        String fromUser=messageModel.getFrom();
        String messageType=messageModel.getType();


            if(fromUser.equals(currrentUserID)){
                holder.messageSinglelayout.setBackgroundResource(R.drawable.message_background);
                holder.txtVuMsg.setTextColor(Color.WHITE);
                holder.txtVuUsername.setTextColor(Color.WHITE);
                holder.txtVuTime.setTextColor(Color.WHITE);


            }else{
                holder.messageSinglelayout.setBackgroundResource(R.drawable.message_background_second);
                holder.txtVuMsg.setTextColor(Color.BLACK);
                holder.txtVuUsername.setTextColor(Color.BLACK);
                holder.txtVuTime.setTextColor(Color.BLACK);
        }

        DatabaseReference mdbRef;
            mdbRef= FirebaseDatabase.getInstance().getReference().child("Users").child(fromUser);
            mdbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String name=dataSnapshot.child("name").getValue().toString();
                    String thumb=dataSnapshot.child("thumb_image").getValue().toString();

                    holder.txtVuUsername.setText(name);
                    Picasso.get().load(thumb).placeholder(R.drawable.default_avatar).into(holder.mProfileImgVu, new Callback() {
                        @Override
                        public void onSuccess() {
                            //Toast.makeText(holder.context, "Success", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(holder.context, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                   // Glide.with(holder.itemView.getContext()).load(thumb).into(holder.mProfileImgVu);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            //Setting up time
        String time= String.valueOf(messageModel.getTime());
        GetTimeAgo getTimeAgo=new GetTimeAgo();
        long lasttime=Long.parseLong(time);
        String lasttimeSeen=GetTimeAgo.getTimeAgo(lasttime,holder.context);
        if(messageType.equals("text")){
            holder.txtVuTime.setText(lasttimeSeen);
            holder.txtVuMsg.setText(messageModel.getMessage());
            holder.imgVuMessage.setVisibility(View.INVISIBLE);
        }else if(messageType.equals("image")){
            holder.txtVuMsg.setVisibility(View.INVISIBLE);
            holder.imgVuMessage.setVisibility(View.VISIBLE);
            holder.txtVuTime.setText(lasttimeSeen);
            Picasso.get().load(messageModel.getMessage()).into(holder.imgVuMessage);
        }
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }
}
