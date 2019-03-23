package com.example.inventoryapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventoryapp.Model.PODetailsModel;
import com.example.inventoryapp.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SubItemSpinnerAdapter extends ArrayAdapter<PODetailsModel> {
    public SubItemSpinnerAdapter(@NonNull Context context, ArrayList<PODetailsModel> arrayList) {
        super(context,0,arrayList);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position,convertView,parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position,convertView,parent);
    }


    private View initView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_spinner_layout,parent,false);
        }

        TextView textView= convertView.findViewById(R.id.txt_vu_spinner_item);

        PODetailsModel currentItem = getItem(position);

        textView.setText(currentItem.getItemName());
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(), currentItem.getItemName(), Toast.LENGTH_SHORT).show();
            }
        });

        return  convertView;

    }


}
