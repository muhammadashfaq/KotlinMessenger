package com.example.muhammadashfaq.techsupport;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListView;

import com.example.muhammadashfaq.techsupport.Adapter.ExpandleLaptopAdapter;
import com.example.muhammadashfaq.techsupport.Adapter.ExpandlePCAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Laptop extends AppCompatActivity {
    ExpandableListView expandableListViewsLaptop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laptop);
        expandableListViewsLaptop=findViewById(R.id.ex_lst_vu_laptops);
        List<String> Headings= new ArrayList<String>();
        List<String> list1=new ArrayList<String >();
        List<String> list2=new ArrayList<String >();
        List<String> list3=new ArrayList<String >();
        HashMap<String,List<String>> childlist=new HashMap<String, List<String>>();
        String[] heading_items=getResources().getStringArray(R.array.laptop_titles);
        String[] l1=getResources().getStringArray(R.array.laptop_h1);
        String[] l2=getResources().getStringArray(R.array.laptop_h2);
        String[] l3=getResources().getStringArray(R.array.laptop_h3);

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
        childlist.put(Headings.get(0),list1);
        childlist.put(Headings.get(1),list2);
        childlist.put(Headings.get(2),list3);

        ExpandleLaptopAdapter adapter=new ExpandleLaptopAdapter(this,Headings,childlist);
        expandableListViewsLaptop.setAdapter(adapter);

    }
}
