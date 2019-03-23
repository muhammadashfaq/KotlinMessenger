package com.example.muhammadashfaq.snippet.ViewHolder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.muhammadashfaq.snippet.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserViewHolder extends RecyclerView.ViewHolder {
    TextView txtVuUserName,txtVuStatus;
    Button btnSendReq,btnRejReq;
    String name,status;
    CircleImageView imgVu;

    public UserViewHolder(@NonNull View itemView) {
        super(itemView);
        txtVuUserName = itemView.findViewById(R.id.all_users_txt_vu_username);
        txtVuStatus = itemView.findViewById(R.id.all_users_txt_vu_status);
        imgVu = itemView.findViewById(R.id.all_users_thumb_profile);
    }

    public void sendFriendRequest(String userKey,Context context) {
        Toast.makeText(context, userKey, Toast.LENGTH_SHORT).show();
    }
    public void rejFriendRequest(String userKey,Context context) {
        Toast.makeText(context, userKey, Toast.LENGTH_SHORT).show();
    }


    public void setStatus(String Status) {
        status = Status;
        txtVuStatus.setText(status);
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
