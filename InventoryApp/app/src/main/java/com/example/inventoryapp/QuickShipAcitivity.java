package com.example.inventoryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventoryapp.Adapter.SalesAdapter;
import com.example.inventoryapp.Model.SalesModel;
import com.github.ybq.android.spinkit.SpinKitView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class QuickShipAcitivity extends AppCompatActivity implements SearchView.OnQueryTextListener ,SalesAdapter.ContactsAdapterListener
    {

    static RecyclerView recyclerView;
    TextView txtViewToolbar;
    ProgressDialog progressDialog;
    static SalesAdapter adapter;
    static TextView txtVuLaoding;
    static ArrayList<SalesModel> arrayList;
    static SpinKitView spinKitView;
    static String LASTITEMSOID;
    static boolean isScrolling = false;
    int currentItems,totalItems,scrollOutItems;
    static ProgressBar progressBar;
    private int visibleThreshold = 1;
    static   boolean RefreshFLAG= false;
    static LinearLayoutManager linearLayoutManager;
    SearchView searchView;
    static int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_ship_acitivity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView title= toolbar.findViewById(R.id.toolbarcustom_title);
        txtViewToolbar=toolbar.findViewById(R.id.txt_vu_inventory_app);
        TextView txtVu=toolbar.findViewById(R.id.toolbarcustum_titttle);
        LinearLayout linearLayoutGoBack=toolbar.findViewById(R.id.linear_layout_go_back);
        searchView=findViewById(R.id.po_search_view);
        searchView.setQueryHint("SEARCH SO");
        searchView.setOnQueryTextListener(QuickShipAcitivity.this);
        searchView.setBackgroundColor(Color.parseColor("#E0E0E0"));
        progressBar = findViewById(R.id.progress_bar);

        txtVu.setText("SELECT SO#");
        spinKitView=findViewById(R.id.spin_kit);

        txtVuLaoding=findViewById(R.id.txt_vu_loading);
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait a while");
        progressDialog.setCancelable(false);

        recyclerView=findViewById(R.id.recyler_view_items);

        Typeface typefaceFour = Typeface.createFromAsset(getAssets(),"fonts/OpenSans_Regular_0.ttf");
        txtViewToolbar.setTypeface(typefaceFour);

        linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        RecyclerView.OnScrollListener mListener = new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int  totalItemCount = linearLayoutManager.getItemCount();
                int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (isScrolling && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    isScrolling=false;
                    int lastindexi= arrayList.size()-1;
                    fetchData(String.valueOf(lastindexi+1));

                }
            }
        };


        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(linearLayoutManager) ;
        recyclerView.addOnScrollListener(mListener);

        linearLayoutGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        MyAsycTask task=new MyAsycTask(this,0);
        task.execute(0);
    }

    private void fetchData(String index) {
        LoadMoreAyscTask task=new LoadMoreAyscTask(QuickShipAcitivity.this,Integer.valueOf(index));
        task.execute();
    }

        @Override
        public boolean onQueryTextSubmit(String query) {
            Toast.makeText(this, query, Toast.LENGTH_SHORT).show();
            adapter.getFilter().filter(query, new Filter.FilterListener() {
                @Override
                public void onFilterComplete(int count) {

                }
            });
            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            adapter.getFilter().filter(newText);
            return true;
        }

        @Override
        public void onContactSelected(SalesModel salesModel) {
            Toast.makeText(getApplicationContext(), "Selected: " + salesModel.getSoName(), Toast.LENGTH_LONG).show();
        }


        static class LoadMoreAyscTask extends AsyncTask<Void,Void,Void> {

        Context ctx;
        int index;

        public LoadMoreAyscTask(Context context,int index) {
            super();
            this.ctx=context;
            this.index=index;
            Log.i("TESTING", String.valueOf(index));
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            JSONObject jsonObject=null;
            String json = "{'name':'solist','start':"+index+"}";
            try {
                jsonObject=new JSONObject(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, String.valueOf(jsonObject));

            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url("https://3558901.restlets.api.netsuite.com/app/site/hosting/restlet.nl?script=70&deploy=1")
                    .post(body)
                    .addHeader("authorization", "NLAuth nlauth_account=3558901, nlauth_email=Eric@heatandcool.com, nlauth_signature=0okmnjI9@!, nlauth_role=1015")
                    .addHeader("content-type", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "43bd4abe-3c49-9d49-6fae-bcb27b36b637")
                    .build();
            okhttp3.Response response = null;
            String jsonArray=null;
            try {
                response = client.newCall(request).execute();
                jsonArray = response.body().string();

                JSONArray jsonArray1 = null;
                try {
                    jsonArray1 = new JSONArray(jsonArray);
                    Log.i("response",String.valueOf(jsonArray1.length()));

                    for (int i = 0; i < jsonArray1.length(); i++) {
                        JSONObject object = jsonArray1.getJSONObject(i);
                        SalesModel soModel = new SalesModel();
                        soModel.setSoId(object.getString("soId"));
                        soModel.setSoName(object.getString("soName"));
                        arrayList.add(soModel);
                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }


            static class MyAsycTask extends AsyncTask<Integer,Void,Void> {

        Context context;
        int start;

        @Override
        protected Void doInBackground(Integer... integers) {

            JSONObject jsonObject=null;
            String json = "{'name':'solist','start':"+start+"}";
            try {
                jsonObject=new JSONObject(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }



            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, String.valueOf(jsonObject));

            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url("https://3558901.restlets.api.netsuite.com/app/site/hosting/restlet.nl?script=70&deploy=1")
                    .post(body)
                    .addHeader("authorization", "NLAuth nlauth_account=3558901, nlauth_email=Eric@heatandcool.com, nlauth_signature=0okmnjI9@!, nlauth_role=1015")
                    .addHeader("content-type", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "43bd4abe-3c49-9d49-6fae-bcb27b36b637")
                    .build();
            okhttp3.Response response = null;
            String jsonArray=null;
            try {
                response = client.newCall(request).execute();
                arrayList = new ArrayList<>();
                jsonArray = response.body().string();

                JSONArray jsonArray1 = null;
                try {
                    jsonArray1 = new JSONArray(jsonArray);
                    Log.i("response",String.valueOf(jsonArray1.length()));
                    counter=counter+jsonArray1.length();

                    for (int i = 0; i < jsonArray1.length(); i++) {
                        JSONObject object = jsonArray1.getJSONObject(i);
                        SalesModel soModel = new SalesModel();
                        soModel.setSoId(object.getString("soId"));
                        soModel.setSoName(object.getString("soName"));
                        arrayList.add(soModel);
                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        public MyAsycTask(Context context, int start) {
            super();
            this.start=start;
            this.context=context;
        }

        @Override
        protected void onCancelled(Void aVoid) {
            super.onCancelled(aVoid);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            spinKitView.setVisibility(View.VISIBLE);
//            txtVuLaoding.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            setDataAdapter(arrayList);
            spinKitView.setVisibility(View.GONE);
        }

        private void setDataAdapter(ArrayList<SalesModel> arrayList) {
            if(String.valueOf(arrayList.size())!=null){

                adapter=new SalesAdapter(context,arrayList,true);
                recyclerView.setAdapter(adapter);
//                    txtVuLaoding.setVisibility(View.GONE);
            }else{
                txtVuLaoding.setVisibility(View.GONE);
                Toast.makeText(context, "Some error occured", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
