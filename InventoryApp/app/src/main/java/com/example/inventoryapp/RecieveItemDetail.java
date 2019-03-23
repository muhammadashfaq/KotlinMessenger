package com.example.inventoryapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventoryapp.Adapter.POSubItemsAdapter;
import com.example.inventoryapp.Adapter.SOSubItemAdapter;
import com.example.inventoryapp.Common.Common;
import com.example.inventoryapp.Model.IsKitItemNoModel;
import com.example.inventoryapp.Model.KitComponentsModel;
import com.example.inventoryapp.Model.PODetailsModel;
import com.example.inventoryapp.Model.POLocationsModel;
import com.example.inventoryapp.Model.POSerialNumberModel;
import com.example.inventoryapp.Model.SODetailModel;
import com.example.inventoryapp.Model.SOLocationsModel;
import com.example.inventoryapp.Model.SOSerialNumberModel;
import com.example.inventoryapp.Model.SOSubItemListModel;
import com.github.ybq.android.spinkit.SpinKitView;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class RecieveItemDetail extends AppCompatActivity {

    MaterialSpinner locationSpinner;
    EditText edtTxtShipQuantity,editTxtSerialNumber;
    TextView txtVuCommited,txtVuBackOrderd,txtVuShipped,txtVuSQQuatity,txtVuSalesOrder,txtVuOrderNo
            ,txtVuItemName,txtVuDetailShip;
    static SpinKitView spinKitView;

    TextView txtVuItem,txtVulcoaiton,txtVuSerialNumber;
    CheckBox chkBoxShip;
    Button btnShipSO,btnGoBack;
    static String itemname;
    static String recId;
    static TextView noKitItem;

    static RecyclerView recyclerView;
    static Button btnIsKitItemYes;
    static Button btnIsKitItemNo;
    static TextView txtVuLaoding;
    static LinearLayout linearLayout;
    static ArrayList<KitComponentsModel> kitComponents;
    static boolean FLAG = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recieve_item_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView title= toolbar.findViewById(R.id.toolbarcustom_title);
        TextView titleMain= toolbar.findViewById(R.id.toolbarcustum_titttle);
        txtVuLaoding=findViewById(R.id.txt_vu_loading);
        btnIsKitItemYes=findViewById(R.id.btn_kititem_yes);
        btnIsKitItemNo=findViewById(R.id.btn_kititem_no);
        linearLayout=findViewById(R.id.radio_group);
        spinKitView=findViewById(R.id.spin_kit);
        recyclerView=findViewById(R.id.recyler_view_podetails_item);
        noKitItem=findViewById(R.id.no_kit);
        LinearLayout linearLayoutGoBack=toolbar.findViewById(R.id.linear_layout_go_back);



        linearLayoutGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
//                Intent intent = new Intent(RecieveItemDetail.this,RecieveDetail.class);
//                startActivity(intent);
            }
        });

        itemname = getIntent().getStringExtra("recId");
        if(itemname!=null){
            titleMain.setText(itemname);
        }

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(RecieveItemDetail.this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager) ;

        MyAsycTask task=new MyAsycTask(this);
        task.execute(0);
    }

    static class MyAsycTask extends AsyncTask<Integer,Void,Void> {
        ArrayList<SODetailModel> arrayListSoDetail=new ArrayList<>();
        ArrayList<SOSubItemListModel> arrayListSoSubItemsList=new ArrayList<>();
        ArrayList<IsKitItemNoModel> arrayListIsKitItemNo=new ArrayList<>();
        Context context;
        String url="https://3558901.restlets.api.netsuite.com/app/site/hosting/restlet.nl?script=70&deploy=1";

        @Override
        protected Void doInBackground(Integer... integers) {

            //Getting Poid
            JSONObject jsonObject=null;
            String json = "{'name':'getsalesorder','recId':"+itemname+"}";
            try {
                jsonObject=new JSONObject(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, String.valueOf(jsonObject));

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("authorization", "NLAuth nlauth_account=3558901, nlauth_email=Eric@heatandcool.com, nlauth_signature=0okmnjI9@!, nlauth_role=1015")
                    .addHeader("content-type", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "78db70be-e35d-13b2-58a1-56fe9e989d9b")
                    .build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
                recId=response.body().string();
                Log.i("recid",recId);
            } catch (IOException e) {
                e.printStackTrace();
            }



            //Getting SODetails
            JSONObject jsonObjectDetail=null;
            String jsondetials = "{'name':'sodetails','recId':"+recId+"}";
            try {
                jsonObjectDetail=new JSONObject(jsondetials);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            OkHttpClient clientDetails = new OkHttpClient();

            MediaType mediaTypeDetails = MediaType.parse("application/json");
            RequestBody bodyDetials = RequestBody.create(mediaTypeDetails, String.valueOf(jsonObjectDetail));
            Request requestDetials = new Request.Builder()
                    .url(url)
                    .post(bodyDetials)
                    .addHeader("authorization", "NLAuth nlauth_account=3558901, nlauth_email=Eric@heatandcool.com, nlauth_signature=0okmnjI9@!, nlauth_role=1015")
                    .addHeader("content-type", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "45c5fb5e-f6fe-2830-62e2-a67c4c70e5a8")
                    .build();
            Response responseDetials = null;
            try {
                responseDetials = clientDetails.newCall(requestDetials).execute();
                String responseDetails = responseDetials.body().string();

                Log.i("ResponseDetials",responseDetails);

                //JSONObject parent = new JSONObject(responseDetails);
                //JSONArray eventDetails = parent.getJSONArray("event")

                try {
                    JSONArray jsonArray1=new JSONArray(responseDetails);
                   // Toast.makeText(context, jsonArray1.toString(), Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < jsonArray1.length(); i++) {
                        JSONObject object = jsonArray1.getJSONObject(i);

                        if(object.getString("iskititem").equalsIgnoreCase("Yes")){
                            FLAG=true;
                            SOSubItemListModel soSubItemListModel=new SOSubItemListModel();
                            soSubItemListModel.setItemName(object.getString("itemName"));
                            soSubItemListModel.setIskititem(object.getString("iskititem"));
                            arrayListSoSubItemsList.add(soSubItemListModel);

                            SODetailModel soDetailModel=new SODetailModel();
                            soDetailModel.setIskititem(object.getString("iskititem"));
                            soDetailModel.setQty(object.getString("qty"));
                            soDetailModel.setQtyshipped(object.getString("qtyshipped"));
                            soDetailModel.setItem(object.getString("item"));
                            soDetailModel.setItemName(object.getString("itemName"));
                            soDetailModel.setQtycommitted(object.getString("qtycommitted"));
                            soDetailModel.setQtybackordered(object.getString("qtybackordered"));
                            soDetailModel.setArray(object.getJSONArray("kitcomponents"));


                            arrayListSoDetail.add(soDetailModel);
                        }else if(object.getString("iskititem").equalsIgnoreCase("No")){

                            SOSubItemListModel soSubItemListModel=new SOSubItemListModel();
                            soSubItemListModel.setItemName(object.getString("itemName"));
                            soSubItemListModel.setIskititem(object.getString("iskititem"));
                            arrayListSoSubItemsList.add(soSubItemListModel);

                            IsKitItemNoModel isKitItemNoModel=new IsKitItemNoModel();
                            isKitItemNoModel.setIskititem(object.getString("iskititem"));
                            isKitItemNoModel.setQty(object.getString("qty"));
                            isKitItemNoModel.setQtyshipped(object.getString("qtyshipped"));
                            isKitItemNoModel.setItem(object.getString("item"));
                            isKitItemNoModel.setItemName(object.getString("itemName"));
                            isKitItemNoModel.setQtycommitted(object.getString("qtycommitted"));
                            isKitItemNoModel.setQtybackordered(object.getString("qtybackordered"));
                            arrayListIsKitItemNo.add(isKitItemNoModel);
                        }else{

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            //Getting Locations
            JSONObject jsonObjectLOC=null;
            String jsonLOC = "{'name':'solocation','recId':"+recId+"}";
            try {
                jsonObjectLOC=new JSONObject(jsonLOC);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            OkHttpClient clientLOC = new OkHttpClient();

            MediaType mediaTypeLOC = MediaType.parse("application/json");
            RequestBody bodyLOC = RequestBody.create(mediaTypeLOC, String.valueOf(jsonObjectLOC));
            Request requestLOC = new Request.Builder()
                    .url(url)
                    .post(bodyLOC)
                    .addHeader("authorization", "NLAuth nlauth_account=3558901, nlauth_email=Eric@heatandcool.com, nlauth_signature=0okmnjI9@!, nlauth_role=1015")
                    .addHeader("content-type", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "429d9f9d-5d47-cc3e-a40c-5953cc281dc4")
                    .build();

            Response responseLOC = null;
            try {
                responseLOC = clientLOC.newCall(requestLOC).execute();
                String responeLocation = responseLOC.body().string();
                try {
                    JSONArray jsonArrayLOC=new JSONArray(responeLocation);
                    ArrayList<SOLocationsModel> arrayListLOC = new ArrayList<>();
                    SOLocationsModel pomodel=new SOLocationsModel();
                    pomodel.setLocationId("1");
                    pomodel.setLocationName("UEP");
                    arrayListLOC.add(pomodel);
                    for (int i = 0; i < jsonArrayLOC.length(); i++) {
                        JSONObject object = jsonArrayLOC.getJSONObject(i);

                        SOLocationsModel soLocationModel=new SOLocationsModel();

                        soLocationModel.setLocationId(object.getString("locationId"));
                        soLocationModel.setLocationName(object.getString("locationName"));

                        arrayListLOC.add(soLocationModel);
                    }
                    Common.SOlocationsList=arrayListLOC;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            //Getting serialnumber
            JSONObject jsonObjectSerial=null;
            String jsonSerial = "{'name':'serialnumberlist','recId':"+recId+"}";
            try {
                jsonObjectSerial=new JSONObject(jsonSerial);
            } catch (JSONException e) {
                e.printStackTrace();
            }
                OkHttpClient clientSerial = new OkHttpClient();

                MediaType mediaTypeSerial = MediaType.parse("application/json");
                RequestBody bodySerial = RequestBody.create(mediaTypeSerial, String.valueOf(jsonObjectSerial));
                Request requestSerial = new Request.Builder()
                        .url(url)
                        .post(bodySerial)
                        .addHeader("authorization", "NLAuth nlauth_account=3558901, nlauth_email=Eric@heatandcool.com, nlauth_signature=0okmnjI9@!, nlauth_role=1015")
                        .addHeader("content-type", "application/json")
                        .addHeader("cache-control", "no-cache")
                        .addHeader("postman-token", "7493b49b-8abe-6e0b-9924-f7e4327b53e2")
                        .build();

                Response responseSerial = null;

            try {
                responseSerial = clientSerial.newCall(requestSerial).execute();
                String responsee=responseSerial.body().string();
                try {
                    JSONArray jsonArraySerial=new JSONArray(responsee);
                    Common.SoserialNumberList=new ArrayList();

                    //  Log.i("JsonArray",jsonArraySerial.toString());
                    if(jsonArraySerial.length()==0){

                    }else{
                        Log.i("Else",Common.SoserialNumberList.toString());
                        for (int i = 0; i < jsonArraySerial.length(); i++) {
                            JSONObject object = jsonArraySerial.getJSONObject(i);
                            SOSerialNumberModel serialModel=new SOSerialNumberModel();
                            serialModel.setNumber(object.getString("number"));
                            serialModel.setItem(object.getString("item"));
                            Common.SoserialNumberList.add(serialModel);
                        }
                        Log.i("NewSerial",Common.SoserialNumberList.toString());
                    }
                    Log.i("SerialNumberList",responsee);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        public MyAsycTask(Context context) {
            super();
            this.context=context;
        }

        @Override
        protected void onCancelled(Void aVoid) {
            super.onCancelled(aVoid);
            Toast.makeText(context, "on canceled called", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            spinKitView.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            setAdapter(arrayListSoDetail);

        }

        private void setAdapter(ArrayList<SODetailModel> arrayListSoDetail) {
            spinKitView.setVisibility(View.GONE);
            JSONArray jsonArrayKit = null;
            boolean KitFLAG=false;

            if(FLAG){
                for(int i=0;i<arrayListSoDetail.size();i++){
                    KitFLAG=true;
                    jsonArrayKit=arrayListSoDetail.get(i).getArray();
                }
                if(KitFLAG){
                    try {
                        kitComponents=new ArrayList<>();
                        for(int i=0;i<jsonArrayKit.length();i++){
                            JSONObject jsonObject=jsonArrayKit.getJSONObject(i);
                            KitComponentsModel model=new KitComponentsModel();
                            model.setMemberitem(jsonObject.getString("memberitem"));
                            model.setMemberitemname(jsonObject.getString("memberitemname"));
                            model.setMemberQty(jsonObject.getString("memberQty"));
                            kitComponents.add(model);
                            Log.i("Kit",kitComponents.toString());
                        }
                        SOSubItemAdapter adapter=new SOSubItemAdapter(context,arrayListSoDetail,recId,arrayListSoSubItemsList,arrayListIsKitItemNo,kitComponents,"YES",itemname);
                        recyclerView.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }else{
                FLAG=false;
                noKitItem.setVisibility(View.VISIBLE);
            }

        }
    }




    private String getPOID(String itemname) {
        linearLayout.setVisibility(View.INVISIBLE);
        txtVuLaoding.setVisibility(View.VISIBLE);
        ArrayList<SODetailModel> arrayListSoDetail=new ArrayList<>();
        ArrayList<IsKitItemNoModel> arrayListIsKitItemNo=new ArrayList<>();

        Thread mThread= new Thread(){
            public void run(){
                super.run();
                //GETTING POID FROM POITEM

                JSONObject jsonObject=null;
                String json = "{'name':'getsalesorder','recId':"+itemname+"}";
                try {
                    jsonObject=new JSONObject(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                OkHttpClient client = new OkHttpClient();

                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, String.valueOf(jsonObject));

                Request request = new Request.Builder()
                        .url("https://3558901.restlets.api.netsuite.com/app/site/hosting/restlet.nl?script=69&deploy=1")
                        .post(body)
                        .addHeader("authorization", "NLAuth nlauth_account=3558901, nlauth_email=Eric@heatandcool.com, nlauth_signature=0okmnjI9@!, nlauth_role=1015")
                        .addHeader("content-type", "application/json")
                        .addHeader("cache-control", "no-cache")
                        .addHeader("postman-token", "78db70be-e35d-13b2-58a1-56fe9e989d9b")
                        .build();



                try {
                    okhttp3.Response response = client.newCall(request).execute();
                    recId=response.body().string();
                    runOnUiThread(new Runnable() {
                        public void run() {
                            new Thread(new Runnable(){
                                @Override
                                public void run() {
                                    JSONObject jsonObject=null;
                                    String json = "{'name':'sodetails','recId':"+recId+"}";
                                    try {
                                        jsonObject=new JSONObject(json);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    OkHttpClient client = new OkHttpClient();

                                    MediaType mediaType = MediaType.parse("application/json");
                                    RequestBody body = RequestBody.create(mediaType, String.valueOf(jsonObject));
                                    Request request = new Request.Builder()
                                            .url("https://3558901.restlets.api.netsuite.com/app/site/hosting/restlet.nl?script=69&deploy=1")
                                            .post(body)
                                            .addHeader("authorization", "NLAuth nlauth_account=3558901, nlauth_email=Eric@heatandcool.com, nlauth_signature=0okmnjI9@!, nlauth_role=1015")
                                            .addHeader("content-type", "application/json")
                                            .addHeader("cache-control", "no-cache")
                                            .addHeader("postman-token", "45c5fb5e-f6fe-2830-62e2-a67c4c70e5a8")
                                            .build();
                                    try {
                                        Response response = client.newCall(request).execute();
                                        try {
                                            JSONArray jsonArray1=new JSONArray(response.body().string());
                                            for (int i = 0; i < jsonArray1.length(); i++) {
                                                JSONObject object = jsonArray1.getJSONObject(i);

                                                if(object.getString("iskititem").equalsIgnoreCase("Yes")){
                                                    SODetailModel soDetailModel=new SODetailModel();
                                                    soDetailModel.setIskititem(object.getString("iskititem"));
                                                    soDetailModel.setQty(object.getString("qty"));
                                                    soDetailModel.setQtyshipped(object.getString("qtyshipped"));
                                                    soDetailModel.setItem(object.getString("item"));
                                                    soDetailModel.setItemName(object.getString("itemName"));
                                                    soDetailModel.setQtycommitted(object.getString("qtycommitted"));
                                                    soDetailModel.setQtybackordered(object.getString("qtybackordered"));
                                                    soDetailModel.setArray(object.getJSONArray("kitcomponents"));
                                                    arrayListSoDetail.add(soDetailModel);
                                                }else if(object.getString("iskititem").equalsIgnoreCase("No")){
                                                    IsKitItemNoModel isKitItemNoModel=new IsKitItemNoModel();
                                                    isKitItemNoModel.setIskititem(object.getString("iskititem"));
                                                    isKitItemNoModel.setQty(object.getString("qty"));
                                                    isKitItemNoModel.setQtyshipped(object.getString("qtyshipped"));
                                                    isKitItemNoModel.setItem(object.getString("item"));
                                                    isKitItemNoModel.setItemName(object.getString("itemName"));
                                                    isKitItemNoModel.setQtycommitted(object.getString("qtycommitted"));
                                                    isKitItemNoModel.setQtybackordered(object.getString("qtybackordered"));
                                                    arrayListIsKitItemNo.add(isKitItemNoModel);
                                                }else{

                                                }
                                            }

                                            //Getting solocationlist

                                            JSONObject jsonObjectLOC=null;
                                            String jsonLOC = "{'name':'solocation','recId':"+recId+"}";
                                            try {
                                                jsonObjectLOC=new JSONObject(jsonLOC);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }


                                            OkHttpClient clientLOC = new OkHttpClient();

                                            MediaType mediaTypeLOC = MediaType.parse("application/json");
                                            RequestBody bodyLOC = RequestBody.create(mediaTypeLOC, String.valueOf(jsonObjectLOC));
                                            Request requestLOC = new Request.Builder()
                                                    .url("https://3558901.restlets.api.netsuite.com/app/site/hosting/restlet.nl?script=69&deploy=1")
                                                    .post(bodyLOC)
                                                    .addHeader("authorization", "NLAuth nlauth_account=3558901, nlauth_email=Eric@heatandcool.com, nlauth_signature=0okmnjI9@!, nlauth_role=1015")
                                                    .addHeader("content-type", "application/json")
                                                    .addHeader("cache-control", "no-cache")
                                                    .addHeader("postman-token", "429d9f9d-5d47-cc3e-a40c-5953cc281dc4")
                                                    .build();

                                            Response responseLOC = clientLOC.newCall(requestLOC).execute();

                                            ArrayList<SOLocationsModel> arrayListLOC = new ArrayList<>();
                                            JSONArray jsonArrayLOC=new JSONArray(responseLOC.body().string());
                                            for (int i =0 ; i <jsonArrayLOC.length(); i++) {
                                                JSONObject object = jsonArrayLOC.getJSONObject(i);

                                                SOLocationsModel soLocationModel=new SOLocationsModel();

                                                soLocationModel.setLocationId(object.getString("locationId"));
                                                soLocationModel.setLocationName(object.getString("locationName"));

                                                arrayListLOC.add(soLocationModel);
                                            }
                                            Common.SOlocationsList=arrayListLOC;
                                            //Getting Serial number list

                                            JSONObject jsonObjectSerial=null;
                                            String jsonSerial = "{'name':'serialnumberlist','recId':"+recId+"}";
                                            try {
                                                jsonObjectSerial=new JSONObject(jsonSerial);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            try {
                                                OkHttpClient clientSerial = new OkHttpClient();

                                                MediaType mediaTypeSerial = MediaType.parse("application/json");
                                                RequestBody bodySerial = RequestBody.create(mediaTypeSerial, String.valueOf(jsonObjectSerial));
                                                Request requestSerial = new Request.Builder()
                                                        .url("https://3558901.restlets.api.netsuite.com/app/site/hosting/restlet.nl?script=69&deploy=1")
                                                        .post(bodySerial)
                                                        .addHeader("authorization", "NLAuth nlauth_account=3558901, nlauth_email=Eric@heatandcool.com, nlauth_signature=0okmnjI9@!, nlauth_role=1015")
                                                        .addHeader("content-type", "application/json")
                                                        .addHeader("cache-control", "no-cache")
                                                        .addHeader("postman-token", "7493b49b-8abe-6e0b-9924-f7e4327b53e2")
                                                        .build();

                                                Response responseSerial = null;

                                                responseSerial = clientSerial.newCall(requestSerial).execute();

                                                String responsee=responseSerial.body().string();

                                                Common.SoserialNumberList=new ArrayList();
                                                JSONArray jsonArraySerial=new JSONArray(responsee);
                                                //  Log.i("JsonArray",jsonArraySerial.toString());
                                                if(jsonArraySerial.length()==0){

                                                }else{
                                                    Log.i("Else",Common.SoserialNumberList.toString());
                                                    for (int i = 0; i < jsonArraySerial.length(); i++) {
                                                        JSONObject object = jsonArraySerial.getJSONObject(i);
                                                        SOSerialNumberModel serialModel=new SOSerialNumberModel();
                                                        serialModel.setNumber(object.getString("number"));
                                                        serialModel.setItem(object.getString("item"));
                                                        Common.SoserialNumberList.add(serialModel);
                                                    }
                                                    Log.i("NewSerial",Common.SoserialNumberList.toString());
                                                }
                                                Log.i("SerialNumberList",responsee);

                                                //Common.serialNumberList = Common.serialNumberList.add(responsee);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }



                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(RecieveItemDetail.this, "chlaaa", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                            setAdapter(arrayListIsKitItemNo,arrayListSoDetail,recId);


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();

                        }
                    });


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        mThread.start();
        return recId;
    }

    private void setAdapter(ArrayList<IsKitItemNoModel> arrayListIsKitItemNo, ArrayList<SODetailModel> arrayListSoDetail, String recId) {


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
    }


}
