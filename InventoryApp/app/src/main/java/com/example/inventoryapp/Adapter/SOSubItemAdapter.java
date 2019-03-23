package com.example.inventoryapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.inventoryapp.Model.IsKitItemNoModel;
import com.example.inventoryapp.Model.KitComponentsModel;
import com.example.inventoryapp.Model.PODetailsModel;
import com.example.inventoryapp.Model.POLocationsModel;
import com.example.inventoryapp.Model.SODetailModel;
import com.example.inventoryapp.Model.SOLocationsModel;
import com.example.inventoryapp.Model.SOSubItemListModel;
import com.example.inventoryapp.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.inventoryapp.Model.PODetailsModel;
import com.example.inventoryapp.Model.POLocationsModel;
import com.example.inventoryapp.Model.POModel;
import com.example.inventoryapp.Model.POSerialNumberModel;
import com.example.inventoryapp.POItemDetail;
import com.example.inventoryapp.R;
import com.example.inventoryapp.RecieveItemDetail;
import com.example.inventoryapp.SOItemDetial;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SOSubItemAdapter extends RecyclerView.Adapter<SOSubItemAdapter.MyViewHolder>  {


    Context context;
    ArrayList<SODetailModel> arrayListSodetails;
    ArrayList<IsKitItemNoModel> arrayListIsKitNo;
    ArrayList<SOSubItemListModel> arrayListSOSubItems;
    ArrayList<KitComponentsModel> kitComponets;
    LayoutInflater layoutInflater;
    ArrayList<SOLocationsModel> listLOC;
    String soid;
    String itemName,isKitItemNo;
    String qty,qtyshipped,item,qtycommited,qtybackordered;
    ArrayList<KitComponentsModel> kitcomponentsList;
    JSONArray  kitcomponents;
    String yesorno,soItemName;

    public SOSubItemAdapter(Context context, ArrayList<SODetailModel> arrayListSoDetail, String recId, ArrayList<SOSubItemListModel> arrayListSoSubItemsList, ArrayList<IsKitItemNoModel> arrayListIsKitItemNo, ArrayList<KitComponentsModel> kitComponents, String yes, String itemname) {
        this.context=context;
        this.arrayListIsKitNo=arrayListIsKitItemNo;
        this.arrayListSodetails=arrayListSoDetail;
        layoutInflater= LayoutInflater.from(context);
        this.arrayListSOSubItems=arrayListSoSubItemsList;
        this.soid=recId;
        this.yesorno=yes;
        this.soItemName=itemname;
        this.kitcomponentsList=kitComponents;
    }
    @Override
    public SOSubItemAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= layoutInflater.inflate(R.layout.recyler_subitems_title_design,parent,false);
        SOSubItemAdapter.MyViewHolder myViewHolder=new SOSubItemAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder( MyViewHolder holder, int position) {

       holder.txtVuTitle.setText(kitcomponentsList.get(position).getMemberitemname());


        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(yesorno.equalsIgnoreCase("Yes")) {
                    for (int i = 0; i < arrayListSodetails.size(); i++) {

                        if(arrayListSodetails.get(i).getIskititem().equalsIgnoreCase(yesorno)){
                                qty=arrayListSodetails.get(i).getQty();
                                qtyshipped=arrayListSodetails.get(i).getQtyshipped();
                                item=arrayListSodetails.get(i).getItem();
                                itemName=arrayListSodetails.get(i).getItemName();
                                qtycommited=arrayListSodetails.get(i).getQtycommitted();
                                qtybackordered=arrayListSodetails.get(i).getQtybackordered();
                                kitcomponents=arrayListSodetails.get(i).getArray();
                        }
                    }


                    Intent intent=new Intent(context, SOItemDetial.class);
                    intent.putExtra("qty", qty);
                    intent.putExtra("qtyshipped",qtyshipped);
                    intent.putExtra("item",item);
                    intent.putExtra("qtycommited",qtycommited);
                    intent.putExtra("qtybackordered",qtybackordered);
                    intent.putExtra("itemName",itemName);
                    intent.putExtra("iskititem",yesorno);
                    intent.putExtra("soid",soid);
                    intent.putExtra("soname",soItemName);
                    if(yesorno.equalsIgnoreCase("yes")){
                        intent.putExtra("kitcomponents",String.valueOf(kitcomponents));
                    }
                    context.startActivity(intent);


                }else{
                    for(int i=0;i<arrayListIsKitNo.size();i++){
                        if(arrayListIsKitNo.get(i).getIskititem().equalsIgnoreCase(yesorno)){
                            qty=arrayListIsKitNo.get(i).getQty();
                            qtyshipped=arrayListIsKitNo.get(i).getQtyshipped();
                            item=arrayListIsKitNo.get(i).getItem();
                            qtycommited=arrayListIsKitNo.get(i).getQtycommitted();
                            qtybackordered=arrayListIsKitNo.get(i).getQtybackordered();
                        }
                    }
                }


            }

        });
    }

    @Override
    public int getItemCount() {
        return kitcomponentsList.size();
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

