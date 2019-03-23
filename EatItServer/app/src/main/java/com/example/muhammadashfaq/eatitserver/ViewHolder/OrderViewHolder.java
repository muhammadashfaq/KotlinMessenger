package com.example.muhammadashfaq.eatitserver.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import com.example.muhammadashfaq.eatitserver.Interface.ItemClickListner;
import com.example.muhammadashfaq.eatitserver.R;
import com.example.muhammadashfaq.eatitserver.Interface.ItemClickListner;
import com.example.muhammadashfaq.eatitserver.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener {

    public TextView txtVuOrderid,txtVuOrderStatus,txtVuOrderPhone,txtVuOrderAdress,txtVuOrderPrice;

    private ItemClickListner itemClickListner;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);
        txtVuOrderid=itemView.findViewById(R.id.order_id);
        txtVuOrderStatus=itemView.findViewById(R.id.order_status);
        txtVuOrderPhone=itemView.findViewById(R.id.order_phone);
        txtVuOrderAdress=itemView.findViewById(R.id.order_adress);
        txtVuOrderPrice=itemView.findViewById(R.id.order_price);

        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);
    }

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }

    @Override
    public void onClick(View v) {
        itemClickListner.onClick(v,getAdapterPosition(),false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Choose Action");
        menu.add(0,0,getAdapterPosition(),"Update");
        menu.add(0,0,getAdapterPosition(),"Delete");
    }
}
