package com.example.muhammadashfaq.eatitserver.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/*import com.example.muhammadashfaq.eatit.Interface.ItemClickListner;
import com.example.muhammadashfaq.eatit.R;*/
import com.example.muhammadashfaq.eatitserver.Common.Common;
import com.example.muhammadashfaq.eatitserver.Interface.ItemClickListner;
import com.example.muhammadashfaq.eatitserver.R;

public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    ,View.OnCreateContextMenuListener
{

    public TextView foodName;
    public ImageView foodImageView;
    private ItemClickListner itemClickListnerFood;

    public void setItemClickListnerFood(ItemClickListner itemClickListnerFood) {
        this.itemClickListnerFood = itemClickListnerFood;
    }

    public FoodViewHolder(@NonNull View itemView) {
        super(itemView);

        foodImageView=itemView.findViewById(R.id.menu_image);

        foodImageView=itemView.findViewById(R.id.food_image);
        foodName=itemView.findViewById(R.id.food_txt_vu);
        itemView.setOnCreateContextMenuListener(this);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {

        itemClickListnerFood.onClick(v,getAdapterPosition(),false);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        menu.setHeaderTitle("Choose Action:");
        menu.add(0,0,getAdapterPosition(), Common.UPDATE);
        menu.add(0,1,getAdapterPosition(),Common.DELETE);
    }
}
