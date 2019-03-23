package com.example.muhammadashfaq.techsupport;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListView;

import com.example.muhammadashfaq.techsupport.Adapter.ExpandleAndroidAdapter;
import com.example.muhammadashfaq.techsupport.Adapter.ExpandleLaptopAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Android extends AppCompatActivity {
    ExpandableListView expandableListViewsAndroid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android);
        expandableListViewsAndroid=findViewById(R.id.ex_lst_vu_android);
        List<String> Headings= new ArrayList<String>();
        List<String> list1=new ArrayList<String >();
        List<String> list2=new ArrayList<String >();
        List<String> list3=new ArrayList<String >();
        List<String> list4=new ArrayList<String >();
        HashMap<String,List<String>> childlist=new HashMap<String, List<String>>();
        String[] heading_items=getResources().getStringArray(R.array.android_title);
        String[] l1=getResources().getStringArray(R.array.andorid_h1);
        String[] l2=getResources().getStringArray(R.array.android_h2);
        String[] l3=getResources().getStringArray(R.array.android_h3);
        String[] l4=getResources().getStringArray(R.array.android_h4);

        for (String title: heading_items) {
            Headings.add(title);

        }
        for (String title: l1){
            list1.add(title);
        }

        for (String title: l2){
            list2.add(title);
        }

        for (String title: l3){
            list3.add(title);
        }
        for (String title: l4){
            list4.add(title);
        }
        childlist.put(Headings.get(0),list1);
        childlist.put(Headings.get(1),list2);
        childlist.put(Headings.get(2),list3);
        childlist.put(Headings.get(3),list4);

         ExpandleAndroidAdapter adapter=new ExpandleAndroidAdapter(this,Headings,childlist);
        expandableListViewsAndroid.setAdapter(adapter);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
