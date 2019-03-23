package com.example.muhammadashfaq.wallpet.Volley;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySinglenton {

    private static VolleySinglenton myInstance;
    private RequestQueue requestQueue;
    private Context context;


    private VolleySinglenton(Context context){
        this.context=context;
        requestQueue=getRequestQueue();
    }

    private RequestQueue getRequestQueue() {
        if(requestQueue==null){
            requestQueue=Volley.newRequestQueue(context);
        }
        return requestQueue;
    }

    public static synchronized VolleySinglenton getInstance(Context context){

        if(myInstance==null){
            myInstance=new VolleySinglenton(context);
        }
        return myInstance;
    }

    public void addToQueue(Request request){
        getRequestQueue().add(request);
    }
}
