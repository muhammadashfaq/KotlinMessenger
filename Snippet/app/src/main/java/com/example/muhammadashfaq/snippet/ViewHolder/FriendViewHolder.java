package com.example.muhammadashfaq.snippet.ViewHolder;

import android.app.Activity;
import android.content.Context;
import android.nfc.cardemulation.OffHostApduService;
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
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendViewHolder extends RecyclerView.ViewHolder {

    View view;
    String date;
    public CircleImageView imgVu;
    TextView txtVuName,txtVuDate;
    ImageView imgVuOnline;
    public FriendViewHolder(@NonNull View itemView) {
        super(itemView);
        view=itemView;
        intializations(view);
    }

    public void setDate(String date) {

        this.date = date;
        txtVuDate.setText(date);
    }
    public void setName(String UserName) {
        txtVuName.setText(UserName);
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

       /*RequestOptions requestOptions = new RequestOptions();
       requestOptions.placeholder(R.drawable.default_avatar);
        Glide.with(context).load(thumb).apply(requestOptions).into(imgVu);*/

    }

    public void setOnline(String online){
        if( online.equals("true")) {
            imgVuOnline.setVisibility(View.VISIBLE);
        } else{
            imgVuOnline.setVisibility(View.GONE);
        }
    }

    private void intializations(View mMainView) {
        imgVu=mMainView.findViewById(R.id.img_vu_friends_thumb);
        txtVuName=mMainView.findViewById(R.id.txt_vu_friends_name);
        txtVuDate=mMainView.findViewById(R.id.txt_vu_friends_date);
        imgVuOnline=mMainView.findViewById(R.id.img_vu_online);

    }
}
