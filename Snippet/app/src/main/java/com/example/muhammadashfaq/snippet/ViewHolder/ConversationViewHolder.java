package com.example.muhammadashfaq.snippet.ViewHolder;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muhammadashfaq.snippet.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConversationViewHolder extends RecyclerView.ViewHolder
{
    View view;
    public TextView txtVuUserName,txtVuTime,txtVuLastMsg;
    public  CircleImageView imgVuThumb;
    Context context;

    public ConversationViewHolder(@NonNull View itemView) {
        super(itemView);
        view=itemView;
        intilizations(view);
    }

    private void intilizations(View view) {
        txtVuUserName=view.findViewById(R.id.txt_vu_chats_name);
        txtVuTime=view.findViewById(R.id.txt_vu_chats_time);
        txtVuLastMsg=view.findViewById(R.id.txt_vu_chats_last_msg);
        imgVuThumb=view.findViewById(R.id.img_vu_chats_thumb);
    }

    public void setMessage(String message,boolean isSeen){
        txtVuLastMsg.setText(message);
        if(!isSeen){
            txtVuLastMsg.setTypeface(txtVuLastMsg.getTypeface(), Typeface.BOLD);
        }else{
            txtVuLastMsg.setTypeface(txtVuLastMsg.getTypeface(), Typeface.NORMAL);
        }
    }

    public void setUserName(String name) {
        txtVuUserName.setText(name);
    }

    public void setImgVuThumb(String thumb) {
        Picasso.get().load(thumb).placeholder(R.drawable.default_avatar).into(imgVuThumb, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(view.getContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
