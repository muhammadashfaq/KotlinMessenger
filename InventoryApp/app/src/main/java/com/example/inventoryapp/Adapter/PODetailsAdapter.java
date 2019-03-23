package com.example.inventoryapp.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.inventoryapp.Model.PODetailsModel;
import com.example.inventoryapp.Model.POEditTextModel;
import com.example.inventoryapp.Model.POLocationsModel;
import com.example.inventoryapp.R;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PODetailsAdapter extends RecyclerView.Adapter<PODetailsAdapter.MyViewHolder> {

    Context context;
    ArrayList<PODetailsModel> list;
    ArrayList<POLocationsModel> listLOC;
    private LayoutInflater layoutInflater;
    String itemName;
    ArrayList<POEditTextModel> edtTxtArrayList;
    PODetailsBtnInterface itemClickCallback;
    MyViewHolder holder;

    public PODetailsAdapter(Context context, ArrayList<PODetailsModel> arrayList, String itemName){
        layoutInflater= LayoutInflater.from(context);
        this.list= arrayList;
        this.context = context;
        this.itemName=itemName;

    }

    public void setItemClickCallback(final PODetailsBtnInterface itemClickCallback) {
        this.itemClickCallback = itemClickCallback;
    }

    public PODetailsAdapter(Context context, ArrayList<PODetailsModel> arrayList, String itemName, ArrayList<POLocationsModel> arrayListLCC){
        layoutInflater= LayoutInflater.from(context);
        this.list= arrayList;
        this.context = context;
        this.itemName=itemName;
        this.listLOC=arrayListLCC;

    }

    @NonNull
    @Override
    public PODetailsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= layoutInflater.inflate(R.layout.custom_podetails_recycler_item_design,parent,false);
        PODetailsAdapter.MyViewHolder myViewHolder=new PODetailsAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //Setting podetails on items
        holder.txtVuOrderNo.setText("#"+itemName);
        holder.txtVuItemNo.setText(list.get(position).getItemName());
        holder.txtVuItemName.setText(list.get(position).getItemName());
        holder.txtVuBackOrderd.setText(list.get(position).getQtybilled());
        holder.txtVuShipped.setText(list.get(position).getQtyreceived());
        holder.txtVusqQty.setText(list.get(position).getQty());



        //Getting Value from editText
        holder.editTextSerialNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Common.arrayListEditText= new ArrayList<>();

               POEditTextModel poEditModel = new POEditTextModel();
               poEditModel.setEdtTxtData(s.toString());
               Common.arrayListEditText.add(poEditModel);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });





        //setting poserialnumberlist on serial number item
        if(listLOC!=null){

                ArrayList<String> locationNameList=new ArrayList();

                for(int i =0 ; i<listLOC.size();i++){
                    String name = listLOC.get(i).getLocationName();
                    locationNameList.add(name);
                }

                holder.materialSpinnerLocation.setItems(locationNameList);

                holder.btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(Common.arrayListEditText!=null){
                            Toast.makeText(context, Common.arrayListEditText.get(position).getEdtTxtData(), Toast.LENGTH_SHORT).show();
//                            holder.editTextSerialNumber.setText("");
                        }
                    }
                });

            }


    }



    public List<POEditTextModel> retrieveDataEditText()
    {
        return edtTxtArrayList;
    }

    @Override
    public int getItemCount() {
        return  list.size();
    }

        public   class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView txtVuCommited,txtVuBackOrderd,txtVuShipped,txtVuSQQuatity,txtVuSalesOrder,txtVuOrderNo
                ,txtVuItemName,txtVuDetailShip,txtVuItemNo,txtVuItemBackOrder,txtVusqQty,txtVuSipped;
        TextView txtVuItem,txtVulcoaiton,txtVuSerialNumber;

        ImageView btnSave;
        Button btnCancel;

        EditText editTextSerialNumber;

        CheckBox chkBoxShip;
        Button btnShipSO,btnGoBack;
        MaterialSpinner materialSpinnerLocation;
        Spinner materialSpinnerSubItems;
        PODetailsBtnInterface itemClickCallback;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //Intiazlinzing variables
            txtVuSalesOrder=itemView.findViewById(R.id.txt_vu_sales_order);
            txtVuOrderNo=itemView.findViewById(R.id.txt_vu_order_no);
            txtVuItemName=itemView.findViewById(R.id.txt_vu_detail_item_name);
            txtVuDetailShip=itemView.findViewById(R.id.txt_vu_detail_ship);
            txtVuItem=itemView.findViewById(R.id.txt_vu_detail_item);
            txtVuBackOrderd=itemView.findViewById(R.id.txt_vu_detail_backorder);
            txtVuShipped=itemView.findViewById(R.id.txt_vu_detail_shipped);
            txtVuSQQuatity=itemView.findViewById(R.id.txt_vu_detail_sq_quntity);
            txtVulcoaiton=itemView.findViewById(R.id.txt_vu_detail_location);
            txtVuSerialNumber=itemView.findViewById(R.id.txt_vu_detail_serial_number);
            txtVuItemNo = itemView.findViewById(R.id.txt_vu_item_no);
            txtVuBackOrderd = itemView.findViewById(R.id.txt_vu_back_order);
            txtVuShipped = itemView.findViewById(R.id.txt_vu_shipped);
            txtVusqQty= itemView.findViewById(R.id.txt_vu_sq_qty);
            materialSpinnerLocation=itemView.findViewById(R.id.spinner_location);
            editTextSerialNumber=itemView.findViewById(R.id.edt_txt_serail_number);
            btnSave=itemView.findViewById(R.id.btn_img_vu_save_serial_number);
            btnCancel=itemView.findViewById(R.id.btn_img_vu_cancel_serial_number);

            //Setting Font
            Typeface typefaceFour = Typeface.createFromAsset(context.getAssets(), "fonts/BebasNeue_Regular.ttf");
            txtVuSalesOrder.setTypeface(typefaceFour);
            txtVuOrderNo.setTypeface(typefaceFour);
            Typeface typefaceFive = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans_Regular_0.ttf");
            txtVuItem.setTypeface(typefaceFive);
            txtVuDetailShip.setTypeface(typefaceFive);
            txtVuBackOrderd.setTypeface(typefaceFive);
            txtVuSQQuatity.setTypeface(typefaceFive);
            txtVuShipped.setTypeface(typefaceFive);
            txtVuSerialNumber.setTypeface(typefaceFive);
            txtVulcoaiton.setTypeface(typefaceFive);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
