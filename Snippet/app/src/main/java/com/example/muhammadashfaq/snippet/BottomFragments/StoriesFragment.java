package com.example.muhammadashfaq.snippet.BottomFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.muhammadashfaq.snippet.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoriesFragment extends Fragment {


    public StoriesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stories, container, false);
    }

}
