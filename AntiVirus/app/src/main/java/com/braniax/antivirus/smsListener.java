package com.braniax.antivirus;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.annotation.RequiresApi;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

public class smsListener extends BroadcastReceiver
{

    Intent mServiceIntent;
    private BackgroundService mYourService;
    SmsMessage[] msgs;
    String msg_from;
    String msgBody;

    //@RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onReceive(Context context, Intent intent)
    {


        trimCache(context);

        Bundle bundle=intent.getExtras();
        if (bundle != null) {


            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++) {
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                msg_from = msgs[i].getOriginatingAddress();
                msgBody = msgs[i].getMessageBody();
            }

            ContentValues contentValue=new ContentValues();
            contentValue.put(Telephony.Sms.ADDRESS,msg_from);
            contentValue.put(Telephony.Sms.BODY,msgBody);
            context.getContentResolver().insert(Telephony.Sms.CONTENT_URI,contentValue);

        }
        context.startService(new Intent(context, BackgroundService.class));
        //Toast.makeText(context, "Service called", Toast.LENGTH_SHORT).show();

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




}
