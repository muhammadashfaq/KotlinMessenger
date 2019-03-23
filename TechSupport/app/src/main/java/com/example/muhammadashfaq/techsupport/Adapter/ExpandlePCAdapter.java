package com.example.muhammadashfaq.techsupport.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.muhammadashfaq.techsupport.Computer;
import com.example.muhammadashfaq.techsupport.R;

import org.w3c.dom.Text;

import java.net.ContentHandler;
import java.util.HashMap;
import java.util.List;

import me.biubiubiu.justifytext.library.JustifyTextView;

public class ExpandlePCAdapter extends BaseExpandableListAdapter
{
    private List<String> headerTitles;
    private HashMap<String,List<String>> childTitles;
    private Context context;

    public ExpandlePCAdapter(Context context,List<String> headerTitles,HashMap<String,List<String>> childTitles){
        this.context=context;
        this.headerTitles=headerTitles;
        this.childTitles=childTitles;
    }
    @Override
    public int getGroupCount() {
        return headerTitles.size();
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        return childTitles.get(headerTitles.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return headerTitles.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPostion, int childPostion) {
        return childTitles.get(headerTitles.get(groupPostion)).get(childPostion);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1)
    {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View convertView, ViewGroup viewGroup) {
        String title=(String)this.getGroup(i);
        if(convertView==null){
                LayoutInflater layoutInflater= (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView=layoutInflater.inflate(R.layout.parent_layout,null);

        }
        TextView txtVu=convertView.findViewById(R.id.ex_lst_vu_computers_problems_headers);
        txtVu.setTypeface(null, Typeface.BOLD);
        txtVu.setText(title);

        return convertView;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        String childTitle= (String) this.getChild(i,i1);
        if(view==null){
            LayoutInflater layoutInflater= (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=layoutInflater.inflate(R.layout.child_layout,null);

        }
        JustifyTextView txtVuChild=view.findViewById(R.id.ex_lst_vu_computers_problems_inner_items);
        txtVuChild.setText(childTitle);

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
