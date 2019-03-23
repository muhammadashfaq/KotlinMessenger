package com.example.inventoryapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventoryapp.Adapter.PODetailsAdapter;
import com.example.inventoryapp.Adapter.POSubItemsAdapter;
import com.example.inventoryapp.Common.Common;
import com.example.inventoryapp.Interface.PODetailsBtnInterface;
import com.example.inventoryapp.Model.IsKitItemNoModel;
import com.example.inventoryapp.Model.PODetailsModel;
import com.example.inventoryapp.Model.POLocationsModel;
import com.example.inventoryapp.Model.POSerialNumberModel;
import com.example.inventoryapp.Model.SODetailModel;
import com.example.inventoryapp.Model.SOSubItemListModel;
import com.github.ybq.android.spinkit.SpinKitView;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.inventoryapp.Common.Common.arrayList;
import static com.example.inventoryapp.RecieveItemDetail.itemname;

public class ItemDetail extends AppCompatActivity implements PODetailsBtnInterface {

    static String recId;
    static RecyclerView recyclerView;
    ProgressDialog progressDialog;
    static TextView txtVuLaoding;
    static SpinKitView spinKitView;
    static String itemname;
    static String poitemName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView title= toolbar.findViewById(R.id.toolbarcustom_title);
        TextView titleMain= toolbar.findViewById(R.id.toolbarcustum_titttle);
        txtVuLaoding=findViewById(R.id.txt_vu_loading);
        spinKitView=findViewById(R.id.spin_kit);
        LinearLayout linearLayoutGoBack=toolbar.findViewById(R.id.linear_layout_go_back);
        titleMain.setText("Select PO#");



        linearLayoutGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
//                Intent intent = new Intent(ItemDetail.this,DetailActivity.class);
//                startActivity(intent);
            }
        });

        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait a while");
        progressDialog.setCancelable(false);

        recyclerView=findViewById(R.id.recyler_view_podetails_item);
        //  initiazlization();
        itemname = getIntent().getStringExtra("redId");
        if(itemname!=null){
            titleMain.setText(itemname);
           // getPOID(itemname);
        }

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(ItemDetail.this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager) ;

        spinKitView.setVisibility(View.VISIBLE);
        MyAsycTask task = new MyAsycTask(this);
        task.execute();

    }


    static class MyAsycTask extends AsyncTask<Integer,Void,Void>{
        Context context;
        String url = "https://3558901.restlets.api.netsuite.com/app/site/hosting/restlet.nl?script=69&deploy=1";
        public MyAsycTask(Context context) {
            super();
            this.context=context;
        }

        @Override
        protected Void doInBackground(Integer... integers) {

            //Getting Poid
            JSONObject jsonObject=null;
            String json = "{'name':'getpurchaseorder','recId':"+itemname+"}";
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


            //Getting PODETAILS
            JSONObject jsonObjectDETAILS=null;
            String jsonDetials = "{'name':'podetails','recId':"+recId+"}";
            try {
                jsonObjectDETAILS=new JSONObject(jsonDetials);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            OkHttpClient clientDetails = new OkHttpClient();

            MediaType mediaTypeDetails = MediaType.parse("application/json");
            RequestBody bodyDetails = RequestBody.create(mediaTypeDetails, String.valueOf(jsonObjectDETAILS));
            Request requestdetails = new Request.Builder()
                    .url("https://3558901.restlets.api.netsuite.com/app/site/hosting/restlet.nl?script=69&deploy=1")
                    .post(bodyDetails)
                    .addHeader("authorization", "NLAuth nlauth_account=3558901, nlauth_email=Eric@heatandcool.com, nlauth_signature=0okmnjI9@!, nlauth_role=1015")
                    .addHeader("content-type", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "45c5fb5e-f6fe-2830-62e2-a67c4c70e5a8")
                    .build();
            String resDetails=null;
            try {
                Response responseDetails = clientDetails.newCall(requestdetails).execute();
                resDetails = responseDetails.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            JSONArray jsonArray1= null;
            try {
                jsonArray1 = new JSONArray(resDetails);
                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject object = jsonArray1.getJSONObject(i);

                    PODetailsModel poDetailsModel=new PODetailsModel();

                    poDetailsModel.setQty(object.getString("qty"));
                    poDetailsModel.setQtyreceived(object.getString("qtyreceived"));
                    poDetailsModel.setItem(object.getString("item"));
                    poDetailsModel.setItemName(object.getString("itemName"));
                    poDetailsModel.setQtybilled(object.getString("qtybilled"));

                    arrayList.add(poDetailsModel);
                }
                Log.i("Details",arrayList.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }



            //Getting polocationlist
            JSONObject jsonObjectLOC=null;
            String jsonLOC = "{'name':'polocation','recId':"+recId+"}";
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

            Response responseLOC = null;
            try {
                responseLOC = clientLOC.newCall(requestLOC).execute();
                ArrayList<POLocationsModel> arrayListLOC = new ArrayList<>();
                JSONArray jsonArrayLOC= null;
                try {
                    jsonArrayLOC = new JSONArray(responseLOC.body().string());
                    for (int i = 0; i <jsonArrayLOC.length(); i++) {
                        JSONObject object = jsonArrayLOC.getJSONObject(i);
                        POLocationsModel poLocationModel=new POLocationsModel();
                        poLocationModel.setLocationId(object.getString("locationId"));
                        poLocationModel.setLocationName(object.getString("locationName"));
                        arrayListLOC.add(poLocationModel);
                    }
                    Common.locationsList=arrayListLOC;
                    Log.i("Locations",Common.locationsList.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            //Getting Serial number list

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
                    .url("https://3558901.restlets.api.netsuite.com/app/site/hosting/restlet.nl?script=69&deploy=1")
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
                Log.i("response",responsee);
                Common.serialNumberList=new ArrayList();
                JSONArray jsonArraySerial= null;
                try {
                    jsonArraySerial = new JSONArray(responsee);
                     Log.i("JsonArray",jsonArraySerial.toString());
                    if(jsonArraySerial.length()==0){

                    }else{
                        Log.i("Else",Common.serialNumberList.toString());
                        for (int i = 0; i < jsonArraySerial.length(); i++) {
                            JSONObject object = jsonArraySerial.getJSONObject(i);
                            POSerialNumberModel serialModel=new POSerialNumberModel();
                            serialModel.setNumber(object.getString("number"));
                            serialModel.setItem(object.getString("item"));
                            Common.serialNumberList.add(serialModel);
                        }
                        Log.i("NewSerial",Common.serialNumberList.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            setAdapter(arrayList);
        }

        private void setAdapter(ArrayList<PODetailsModel> arrayList) {
            POSubItemsAdapter adapterr=new POSubItemsAdapter(context,arrayList,recId,itemname);
            recyclerView.setAdapter(adapterr);
            spinKitView.setVisibility(View.GONE);
        }
    }

    private String getPOID(String itemname) {

       txtVuLaoding.setVisibility(View.VISIBLE);
        ArrayList<PODetailsModel> arrayList=new ArrayList<>();


        // getRecID();

        Thread mThread= new Thread(){
            public void run(){
                super.run();
                //GETTING POID FROM POITEM

                runOnUiThread(new Runnable() {
                    public void run() {
                        new Thread(new Runnable(){
                            @Override
                            public void run() {
                                try {



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

                                        Common.serialNumberList=new ArrayList();
                                        JSONArray jsonArraySerial=new JSONArray(responsee);
                                      //  Log.i("JsonArray",jsonArraySerial.toString());
                                        if(jsonArraySerial.length()==0){

                                        }else{
                                            Log.i("Else",Common.serialNumberList.toString());
                                            for (int i = 0; i < jsonArraySerial.length(); i++) {
                                                JSONObject object = jsonArraySerial.getJSONObject(i);
                                                POSerialNumberModel serialModel=new POSerialNumberModel();
                                                serialModel.setNumber(object.getString("number"));
                                                serialModel.setItem(object.getString("item"));
                                                Common.serialNumberList.add(serialModel);
                                            }
                                            Log.i("NewSerial",Common.serialNumberList.toString());
                                        }


                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {

                                            }
                                        });
                                        Log.i("SerialNumberList",responsee);

                                        //Common.serialNumberList = Common.serialNumberList.add(responsee);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }


                                    runOnUiThread(new Runnable() {

                                        @Override
                                        public void run() {




//                                                    PODetailsAdapter adapter=new PODetailsAdapter(ItemDetail.this,arrayList,itemname);
//                                                    recyclerView.setAdapter(adapter);
//                                                    adapter.setItemClickCallback(ItemDetail.this);
                                          //  progressDialog.dismiss();
                                        }
                                    });








                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                    }
                });


            }
        };
        mThread.start();
        return recId;
    }


    @Override
    public void onbtnSaveClick(int position) {
        Toast.makeText(this, "in", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, Common.arrayListEditText.get(position).getEdtTxtData(), Toast.LENGTH_SHORT).show();

    }

}
