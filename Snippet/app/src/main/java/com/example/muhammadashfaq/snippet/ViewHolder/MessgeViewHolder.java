package com.example.muhammadashfaq.snippet.ViewHolder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.muhammadashfaq.snippet.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessgeViewHolder extends RecyclerView.ViewHolder
{
    public TextView txtVuMsg,txtVuUsername,txtVuTime;
   public CircleImageView mProfileImgVu;
   public RelativeLayout messageSinglelayout;
   public Context context;
   public ImageView imgVuMessage;

    public MessgeViewHolder(@NonNull View itemView, Context context)
    {

        super(itemView);
        this.context=context;
        txtVuMsg=itemView.findViewById(R.id.message_single_txt_vu);
        mProfileImgVu=itemView.findViewById(R.id.message_single_profile);
        txtVuUsername=itemView.findViewById(R.id.message_single_user_name);
        messageSinglelayout=itemView.findViewById(R.id.message_single_layout);
        txtVuTime=itemView.findViewById(R.id.message_single_message_time);
        imgVuMessage=itemView.findViewById(R.id.img_vu_message);

    }
}
