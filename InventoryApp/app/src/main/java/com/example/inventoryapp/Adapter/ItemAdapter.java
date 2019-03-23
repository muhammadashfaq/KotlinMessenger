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

import com.example.inventoryapp.ItemDetail;
import com.example.inventoryapp.Model.POModel;
import com.example.inventoryapp.ModelRecieveActivity;
import com.example.inventoryapp.R;
import com.example.inventoryapp.ModelShipActivity;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> implements Filterable {

    Context context;
    ArrayList<POModel> list;
    private ArrayList<POModel> contactListFiltered;
    boolean quick=false;

    private LayoutInflater layoutInflater;
    public ItemAdapter(Context context, ArrayList<POModel> arrayList){
        layoutInflater= LayoutInflater.from(context);
        this.list= arrayList;
        this.contactListFiltered=arrayList;
        this.context = context;
    }

    public ItemAdapter(Context context, ArrayList<POModel> arrayList, boolean quick) {
        layoutInflater= LayoutInflater.from(context);
        this.list= arrayList;
        this.contactListFiltered=arrayList;
        this.context = context;
        this.quick=quick;
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
        holder.txtVuTitle.setText(contactListFiltered.get(position).getPoName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quick){

                    Intent intent=new Intent(context, ModelRecieveActivity.class);
                    intent.putExtra("recId",contactListFiltered.get(position).getPoName());
                    context.startActivity(intent);
                }else{
                    Intent intent=new Intent(context, ItemDetail.class);
                    intent.putExtra("redId",contactListFiltered.get(position).getPoName());
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
                   ArrayList<POModel> filteredList = new ArrayList<>();
                   for(POModel row: list){
                       if(row.getPoName().toLowerCase().contains(charString.toLowerCase())){
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
               contactListFiltered = (ArrayList<POModel>) results.values;
               notifyDataSetChanged();
           }
       };
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
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
