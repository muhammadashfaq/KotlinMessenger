package com.example.muhammadashfaq.eatitserver.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.muhammadashfaq.eatitserver.Common.Common;
import com.example.muhammadashfaq.eatitserver.Home;
import com.example.muhammadashfaq.eatitserver.Interface.ItemClickListner;
import com.example.muhammadashfaq.eatitserver.R;

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    ,View.OnCreateContextMenuListener
       {

    public TextView menuName;
    public ImageView imageView;
    private ItemClickListner itemClickListner;

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }

    public MenuViewHolder(@NonNull View itemView) {
        super(itemView);

        menuName=itemView.findViewById(R.id.menu_txt_vu);
        imageView=itemView.findViewById(R.id.menu_image);

        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);

    }


    @Override
    public void onClick(View v) {
        itemClickListner.onClick(v,getAdapterPosition(),false);
    }

           @Override
           public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
               menu.setHeaderTitle("Select the Action:");
                menu.add(0,0,getAdapterPosition(), Common.UPDATE);
                menu.add(0,1,getAdapterPosition(),Common.DELETE);
           }
       }
