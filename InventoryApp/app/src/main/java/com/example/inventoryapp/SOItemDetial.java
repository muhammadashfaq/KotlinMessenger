package com.example.inventoryapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventoryapp.Common.Common;
import com.example.inventoryapp.Model.KitComponentsModel;
import com.example.inventoryapp.Model.POLocationsModel;
import com.example.inventoryapp.Model.SOSerialNumberModel;
import com.google.android.material.snackbar.Snackbar;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class SOItemDetial extends AppCompatActivity {

    private static final String TAG = "SODETAILS";
    TextView txtVuBackOrderd,txtVuShipped,txtVuSQQuatity,txtVuSalesOrder,txtVuOrderNo
            ,txtVuItemName,txtVuDetailShip,txtVuItemNo,txtVusqQty,txtVuItem,txtVulcoaiton,txtVuSerialNumber
            ,txtVuIsKitItem,txtVuisKitItemValue;
    ImageView btnSave;
    Button btnCancel;
    static RelativeLayout relativeLayoutSO;
    static EditText editTextSerialNumber;
    MaterialSpinner materialSpinnerLocation;
    static String soid,soItemName;
    String itemName;
    String qty;
    String qtyshipped;
    static ProgressDialog progressDialog;
    static String item;
    static boolean FLAGDONE = false;
    String qtycommited;
    String qtybackordered;
    ArrayList<KitComponentsModel> kitcomponents;
    String yesorno;


    ArrayList<POLocationsModel> locationList;
    TextView txtVuLaoding;
    String loclist;
    String serialnumber;
    static String userSerialNumber;
    String FLAG = "";

    static String responseSer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soitem_detial);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView title= toolbar.findViewById(R.id.toolbarcustom_title);
        TextView titleMain= toolbar.findViewById(R.id.toolbarcustum_titttle);
        txtVuLaoding=findViewById(R.id.txt_vu_loading);
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("We're saving serial number .Please wait");
        progressDialog.setCancelable(false);


        relativeLayoutSO=findViewById(R.id.rv_so);
        LinearLayout linearLayoutGoBack=toolbar.findViewById(R.id.linear_layout_go_back);





        linearLayoutGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
//                Intent intent = new Intent(SOItemDetial.this,RecieveItemDetail.class);
//                startActivity(intent);
            }
        });


        initiazlization();
        applyFont();
        getData();
        setData();
        titleMain.setText(soItemName);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSave.setOnClickListener(v -> {
            userSerialNumber = editTextSerialNumber.getText().toString().trim();

            if(!userSerialNumber.isEmpty()){
                progressDialog.show();
                doComparison();
            }else{
                editTextSerialNumber.setError("Please enter serial number");
            }

        });

    }

    private void doComparison() {


        if(Common.SoserialNumberList.isEmpty()){
            MyAyscnTask myNewTask = new MyAyscnTask(SOItemDetial.this);
            myNewTask.execute(0);
        }else{
            doComparisonOfItem();
        }

    }



    static class MyAyscnTask extends AsyncTask<Integer,Void,Void>{
        Context context;
        @Override
        protected Void doInBackground(Integer... integers) {

            JSONObject jsonObjectSerial=null;
            String jsonSerial = "{'name':'saveserialnumber','recId':"+soid+",'serialNumber':"+userSerialNumber+",'item':"+item+"}";
            try {
                jsonObjectSerial=new JSONObject(jsonSerial);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, String.valueOf(jsonObjectSerial));
            Request request = new Request.Builder()
                    .url("https://3558901.restlets.api.netsuite.com/app/site/hosting/restlet.nl?script=69&deploy=1")
                    .post(body)
                    .addHeader("Authorization", "NLAuth nlauth_account=3558901, nlauth_email=Eric@heatandcool.com, nlauth_signature=0okmnjI9@!, nlauth_role=1015")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("Postman-Token", "b460cdaa-2e35-48f0-812b-5b39f6433b3f")
                    .build();


            Response response = null;
            try {
                response = client.newCall(request).execute();
                String savedResponse=response.body().string();
                if(savedResponse!=null){
                    FLAGDONE=true;
                    Log.i("Response",savedResponse);
                    Log.i("Response","Item Saved");
                }
                Log.i("Response",savedResponse);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Update Serial Number list bacuse new serialnumber is added!!!
            JSONObject jsonObjectSer=null;
            String jsonSer = "{'name':'serialnumberlist','recId':"+soid+"}";
            try {
                jsonObjectSer=new JSONObject(jsonSer);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            OkHttpClient clientSerial = new OkHttpClient();

            MediaType mediaTypeSerial = MediaType.parse("application/json");
            RequestBody bodySerial = RequestBody.create(mediaTypeSerial, String.valueOf(jsonObjectSer));
            Request requestSerial = new Request.Builder()
                    .url("https://3558901.restlets.api.netsuite.com/app/site/hosting/restlet.nl?script=69&deploy=1")
                    .post(bodySerial)
                    .addHeader("authorization", "NLAuth nlauth_account=3558901, nlauth_email=Eric@heatandcool.com, nlauth_signature=0okmnjI9@!, nlauth_role=1015")
                    .addHeader("content-type", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "7493b49b-8abe-6e0b-9924-f7e4327b53e2")
                    .build();



            try {
                Response responseSerial = clientSerial.newCall(requestSerial).execute();
                responseSer=responseSerial.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Common.SoserialNumberList=new ArrayList();
            try {
                JSONArray jsonArraySerial= null;
                jsonArraySerial = new JSONArray(responseSer);
                //  Log.i("JsonArray",jsonArraySerial.toString());
                if(jsonArraySerial.length()==0){

                }else{
                    Log.i("Else",Common.SoserialNumberList.toString());
                    for (int i = 0; i < jsonArraySerial.length(); i++) {

                        JSONObject object = jsonArraySerial.getJSONObject(i);
                        SOSerialNumberModel serialModel = new SOSerialNumberModel();
                        serialModel.setNumber(object.getString("number"));
                        serialModel.setItem(object.getString("item"));
                        Common.SoserialNumberList.add(serialModel);
                    }
                }

                Log.i("NewSerial",Common.SoserialNumberList.toString());
            }catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        public MyAyscnTask(Context context) {
            super();
            this.context=context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(FLAGDONE){
                editTextSerialNumber.setText("");
                Snackbar snackbar;
                snackbar = Snackbar.make(relativeLayoutSO, "Serial number saved succefully", Snackbar.LENGTH_SHORT);
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#43A047"));
                snackbar.show();
                progressDialog.dismiss();
            }else{
                editTextSerialNumber.setText("");
                Snackbar snackbar;
                snackbar = Snackbar.make(relativeLayoutSO, "Some Error Occured. TryAgain!", Snackbar.LENGTH_SHORT);
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#F44336"));
                snackbar.show();
                progressDialog.dismiss();
            }
        }
    }

    private void doComparisonOfItem() {
        for(int i=0;i<Common.SoserialNumberList.size();i++){
            if(Common.SoserialNumberList.get(i).getNumber().equalsIgnoreCase(userSerialNumber+".0") &&
                    Common.SoserialNumberList.get(i).getItem().equalsIgnoreCase(item+".0")){
                qty= String.valueOf(Integer.valueOf(qty)+1);
                txtVusqQty.setText(qty);
                editTextSerialNumber.setText("");
                Snackbar snackbar;
                snackbar = Snackbar.make(relativeLayoutSO, "QTY increased", Snackbar.LENGTH_LONG);
                View snackBarView =  snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#43A047"));
                snackbar.show();
                progressDialog.dismiss();
                return;
            }

            if(Common.SoserialNumberList.get(i).getNumber().equals(userSerialNumber+".0")){
                editTextSerialNumber.setText("");
                userSerialNumber="";
                Snackbar snackbar;
                snackbar = Snackbar.make(relativeLayoutSO, "This number isn't available.! Try another one", Snackbar.LENGTH_LONG);
                View snackBarView =  snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#F44336"));
                snackbar.show();
                FLAG="DUP";
                progressDialog.dismiss();
                // Toast.makeText(this, "bahar aya 1", Toast.LENGTH_SHORT).show();
                return;

            }else if(Common.SoserialNumberList.get(i).getItem().equals(item+".0")){
                editTextSerialNumber.setText("");
                userSerialNumber="";
                Snackbar snackbar;
                snackbar = Snackbar.make(relativeLayoutSO, "This item already has a serial number assigned to it, please select another item", Snackbar.LENGTH_LONG);
                View snackBarView =  snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#F44336"));
                snackbar.show();
                progressDialog.dismiss();
                FLAG="DUP";
                //  Toast.makeText(this, "bahar aya 2", Toast.LENGTH_SHORT).show();
                return;
            } else if(Common.SoserialNumberList.get(i).getNumber().equals(userSerialNumber)){
                editTextSerialNumber.setText("");
                userSerialNumber="";
                FLAG="DUP";
                return;
            } else if(Common.SoserialNumberList.get(i).getItem().equals(item)){
                progressDialog.dismiss();
                editTextSerialNumber.setText("");
                userSerialNumber="";
               // editTextSerialNumber.setError("Already used! Please enter other serial number.");
                FLAG="DUP";

                //  Toast.makeText(this, "bahar aya 4", Toast.LENGTH_SHORT).show();
                return;
            }else{

            }
        }
        Toast.makeText(this, "Serial Number Saved", Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
        MyAyscnTask myNewTask = new MyAyscnTask(SOItemDetial.this);
        myNewTask.execute(0);
    }

    private void getData() {
        if(getIntent()!=null){
            soid=getIntent().getStringExtra("soid");
            soItemName=getIntent().getStringExtra("soname");
            qty=getIntent().getStringExtra("qty");
            qtyshipped=getIntent().getStringExtra("qtyshipped");
            yesorno=getIntent().getStringExtra("iskititem");
            item=getIntent().getStringExtra("item");
            itemName=getIntent().getStringExtra("itemName");
            qtycommited=getIntent().getStringExtra("qtycommited");
            qtybackordered=getIntent().getStringExtra("qtybackordered");
            if(yesorno.equalsIgnoreCase("Yes")){
              String array=getIntent().getStringExtra("kitcomponents");
                try {
                    JSONArray jsonArray=new JSONArray(array);
                    kitcomponents=new ArrayList<>();
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        KitComponentsModel model=new KitComponentsModel();
                        model.setMemberitem(jsonObject.getString("memberitem"));
                        model.setMemberitemname(jsonObject.getString("memberitemname"));
                        model.setMemberQty(jsonObject.getString("memberQty"));
                        kitcomponents.add(model);
                        Log.i(TAG,kitcomponents.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            Log.i(TAG,soid+"\n"+qty+"/n"+qtyshipped+"\n"+yesorno+"\n"+item+"\n"+itemName+"\n"+
                    qtycommited+"\n"+qtybackordered);
        }

    }

    private void setData() {
        txtVuOrderNo.setText("#"+soItemName);
        txtVuItemNo.setText(itemName);
        txtVuItemName.setText(itemName);
        txtVuBackOrderd.setText(qtybackordered);
        txtVuShipped.setText(qtyshipped);
        txtVusqQty.setText(qty);
        txtVuisKitItemValue.setText(yesorno);
        if(Common.SOlocationsList !=null){
            ArrayList<String> locationNameList=new ArrayList();

            for(int i =0 ; i<Common.SOlocationsList.size();i++){
                String name = Common.SOlocationsList.get(i).getLocationName();
                locationNameList.add(name);
            }

            Collections.reverse(locationNameList);

            materialSpinnerLocation.setItems(locationNameList);
        }

    }

    private void applyFont() {
        //Setting Font
        Typeface typefaceFour = Typeface.createFromAsset(this.getAssets(), "fonts/BebasNeue_Regular.ttf");
        txtVuSalesOrder.setTypeface(typefaceFour);
        txtVuOrderNo.setTypeface(typefaceFour);

        Typeface typefaceFive = Typeface.createFromAsset(this.getAssets(), "fonts/OpenSans_Regular_0.ttf");
        txtVuItem.setTypeface(typefaceFive);
        txtVuDetailShip.setTypeface(typefaceFive);
        txtVuBackOrderd.setTypeface(typefaceFive);
        txtVuSQQuatity.setTypeface(typefaceFive);
        txtVuIsKitItem.setTypeface(typefaceFive);
        txtVuShipped.setTypeface(typefaceFive);
        txtVuSerialNumber.setTypeface(typefaceFive);
        txtVulcoaiton.setTypeface(typefaceFive);
    }

    private void initiazlization() {
        txtVuSalesOrder=findViewById(R.id.txt_vu_sales_order);
        txtVuOrderNo=findViewById(R.id.txt_vu_order_no);
        txtVuItemName=findViewById(R.id.txt_vu_detail_item_name);
        txtVuDetailShip=findViewById(R.id.txt_vu_detail_ship);
        txtVuItem=findViewById(R.id.txt_vu_detail_item);
        txtVuBackOrderd=findViewById(R.id.txt_vu_detail_backorder);
        txtVuShipped=findViewById(R.id.txt_vu_detail_shipped);
        txtVuSQQuatity=findViewById(R.id.txt_vu_detail_sq_quntity);
        txtVulcoaiton=findViewById(R.id.txt_vu_detail_location);
        txtVuSerialNumber=findViewById(R.id.txt_vu_detail_serial_number);
        txtVuItemNo = findViewById(R.id.txt_vu_item_no);
        txtVuBackOrderd = findViewById(R.id.txt_vu_back_order);
        txtVuShipped = findViewById(R.id.txt_vu_shipped);
        txtVusqQty= findViewById(R.id.txt_vu_sq_qty);
        materialSpinnerLocation=findViewById(R.id.spinner_location);
        editTextSerialNumber=findViewById(R.id.edt_txt_serail_number);
        btnSave=findViewById(R.id.btn_img_vu_save_serial_number);
        btnCancel=findViewById(R.id.btn_img_vu_cancel_serial_number);
        txtVuIsKitItem=findViewById(R.id.txt_vu_is_kit_item_txt);
        txtVuisKitItemValue=findViewById(R.id.txt_vu_is_kit_item_value);

    }
}
