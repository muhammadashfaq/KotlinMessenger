package com.example.inventoryapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventoryapp.Common.Common;
import com.example.inventoryapp.Interface.PODetailsBtnInterface;
import com.example.inventoryapp.ItemDetail;
import com.example.inventoryapp.Model.IsKitItemNoModel;
import com.example.inventoryapp.Model.PODetailsModel;
import com.example.inventoryapp.Model.POLocationsModel;
import com.example.inventoryapp.Model.POModel;
import com.example.inventoryapp.Model.POSerialNumberModel;
import com.example.inventoryapp.Model.SODetailModel;
import com.example.inventoryapp.POItemDetail;
import com.example.inventoryapp.R;
import com.example.inventoryapp.RecieveItemDetail;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class POSubItemsAdapter extends RecyclerView.Adapter<POSubItemsAdapter.MyViewHolder>  {


    Context context;
    ArrayList<PODetailsModel> arrayList;
    LayoutInflater layoutInflater;
    ArrayList<POLocationsModel> listLOC;
    String poid;
    String poitemname;

    public POSubItemsAdapter(Context context, ArrayList<PODetailsModel> list, String recId,String poitemname) {
        layoutInflater= LayoutInflater.from(context);
        this.arrayList= list;
        this.context = context;
        this.poid=recId;
        this.poitemname=poitemname;
    }



    @NonNull
    @Override
    public POSubItemsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= layoutInflater.inflate(R.layout.recyler_subitems_title_design,parent,false);
        POSubItemsAdapter.MyViewHolder myViewHolder=new POSubItemsAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull POSubItemsAdapter.MyViewHolder holder, int position) {
            holder.txtVuTitle.setText(arrayList.get(position).getItemName());

            holder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    String qty=arrayList.get(position).getQty();
                    String qtyreceived=arrayList.get(position).getQtyreceived();
                    String item=arrayList.get(position).getItem();
                    String itemname=arrayList.get(position).getItemName();
                    String qtybilled=arrayList.get(position).getQtybilled();
                    Intent intent=new Intent(context, POItemDetail.class);
                    intent.putExtra("qty",qty);
                    intent.putExtra("qtyreceived",qtyreceived);
                    intent.putExtra("item",item);
                    intent.putExtra("poid",poid);
                    intent.putExtra("itemname",itemname);
                    intent.putExtra("qtybilled",qtybilled);
                    intent.putExtra("locationlist", String.valueOf(Common.locationsList));
                    intent.putExtra("serialnumberlist",String.valueOf(Common.serialNumberList));
                    intent.putExtra("poitem",poitemname);
                    context.startActivity(intent);
                }

            });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public   class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        public TextView txtVuTitle;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtVuTitle=itemView.findViewById(R.id.item_tiltle);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
