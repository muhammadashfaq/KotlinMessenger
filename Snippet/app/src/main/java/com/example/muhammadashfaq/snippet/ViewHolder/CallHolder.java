package com.example.muhammadashfaq.snippet.ViewHolder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.muhammadashfaq.snippet.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class CallHolder extends RecyclerView.ViewHolder {
    TextView txtVuUserName;
    String name,status;
    public CircleImageView imgVu;
    public ImageView imgVuVideoCall,imgVuAudioCall;

    public CallHolder(@NonNull View itemView) {
        super(itemView);
        txtVuUserName = itemView.findViewById(R.id.txt_vu_call_friends_name);
        imgVu=itemView.findViewById(R.id.img_vu_call_friends_thumb);
        imgVuVideoCall = itemView.findViewById(R.id.img_vu_video_call);
        imgVuAudioCall = itemView.findViewById(R.id.img_vu_audio_call);
    }


    public void setName(String UserName) {
        name=UserName;
        txtVuUserName.setText(name);
    }

    public void setThumb(final String thumb, final Context context) {

        Picasso.get().load(thumb).placeholder(R.drawable.default_avatar).into(imgVu, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(context, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void setSendBtnVisible(boolean condition){

    }
}
