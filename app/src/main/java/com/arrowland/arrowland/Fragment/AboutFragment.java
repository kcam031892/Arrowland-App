package com.arrowland.arrowland.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.arrowland.arrowland.R;

import java.util.ArrayList;

/**
 * Created by Mhack Bautista on 7/22/2018.
 */

public class AboutFragment extends Fragment {

    View view;
    SwipeRefreshLayout swipeRefreshLayout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_about,null);
        getActivity().setTitle("About Us");





        return view;
    }







}
