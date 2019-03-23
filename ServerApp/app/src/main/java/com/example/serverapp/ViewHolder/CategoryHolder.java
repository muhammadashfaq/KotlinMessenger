package com.example.serverapp.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.serverapp.Interface.ItemClickListner;
import com.example.serverapp.R;

public class CategoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView menuName;
    public ImageView imageView;
    private ItemClickListner itemClickListner;

    public CategoryHolder(@NonNull View itemView) {
        super(itemView);

        menuName=itemView.findViewById(R.id.itemName);
        imageView=itemView.findViewById(R.id.itemThumbnail);

        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        itemClickListner.onClick(itemView,getAdapterPosition(),false);
    }
    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }
}
