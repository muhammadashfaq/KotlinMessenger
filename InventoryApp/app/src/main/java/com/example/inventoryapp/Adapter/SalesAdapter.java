package com.example.inventoryapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventoryapp.Model.SalesModel;
import com.example.inventoryapp.R;
import com.example.inventoryapp.RecieveItemDetail;
import com.example.inventoryapp.ModelShipActivity;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SalesAdapter extends RecyclerView.Adapter<SalesAdapter.MyViewHolder> implements Filterable  {

    Context context;
    ArrayList<SalesModel> list;
    private ArrayList<SalesModel> contactListFiltered;
    private ContactsAdapterListener listener;
    boolean quick=false;


    private LayoutInflater layoutInflater;
    public SalesAdapter(Context context, ArrayList<SalesModel> arrayList){
        layoutInflater= LayoutInflater.from(context);
        this.list= arrayList;
        this.contactListFiltered=arrayList;
        this.context = context;
        this.listener= (ContactsAdapterListener) context;
    }

    public SalesAdapter(Context context, ArrayList<SalesModel> arrayList, boolean quick) {
        layoutInflater= LayoutInflater.from(context);
        this.list= arrayList;
        this.contactListFiltered=arrayList;
        this.context = context;
        this.listener= (ContactsAdapterListener) context;
        this.quick = quick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= layoutInflater.inflate(R.layout.recyler_item_title_design,parent,false);
        MyViewHolder myViewHolder=new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        holder.txtVuTitle.setText(contactListFiltered.get(position).getSoName());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(quick){
                    Intent intent=new Intent(context, ModelShipActivity.class);
                    intent.putExtra("recId",contactListFiltered.get(position).getSoName());
                    context.startActivity(intent);
                }else{
                    Intent intent=new Intent(context, RecieveItemDetail.class);
                    intent.putExtra("recId",contactListFiltered.get(position).getSoName());
                    context.startActivity(intent);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = list;
                }else{
                    ArrayList<SalesModel> filteredList = new ArrayList<>();
                    for(SalesModel row: list){
                        if(row.getSoName().toLowerCase().contains(charString.toLowerCase())){
                            filteredList.add(row);
                        }
                    }
                    contactListFiltered=filteredList;

                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                contactListFiltered = (ArrayList<SalesModel>) results.values;
                notifyDataSetChanged();
            }


        };
    }

    public interface ContactsAdapterListener {
        void onContactSelected(SalesModel salesModel);
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public TextView txtVuTitle;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtVuTitle=itemView.findViewById(R.id.item_tiltle);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(contactListFiltered.get(getAdapterPosition()));
                }
            });
        }

        @Override
        public void onClick(View v) {

        }
    }


}
