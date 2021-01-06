package com.arrowland.arrowland.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.arrowland.arrowland.Adapter.CustomerAdapter;
import com.arrowland.arrowland.Adapter.GalleryAdapter;
import com.arrowland.arrowland.Adapter.NewsAdapter;
import com.arrowland.arrowland.R;
import com.arrowland.arrowland.REST.ApiClient;
import com.arrowland.arrowland.REST.ApiInterface;
import com.arrowland.arrowland.REST.Gallery;
import com.arrowland.arrowland.REST.GalleryList;
import com.arrowland.arrowland.REST.News;
import com.arrowland.arrowland.REST.User;
import com.arrowland.arrowland.REST.UserList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mhack Bautista on 7/23/2018.
 */

public class GalleryFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private View view;
    private RecyclerView recyclerView;
    private GalleryAdapter adapter;
    private ArrayList<Gallery> data;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_gallery,null);

        getActivity().setTitle("Gallery");


        //REFRESHER LAYOUT
        swipeRefreshLayout = view.findViewById(R.id.gallery_swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {

                swipeRefreshLayout.setRefreshing(true);

            }
        });



        // LOAD JSON FROM DATABASE
        loadJson();

        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onRefresh() {
        loadJson();

    }

    // GENERATE A LIST AND SET IT ON RECYCLER VIEW

    private void generateCustomer(ArrayList<Gallery> list) {
        recyclerView = view.findViewById(R.id.card_recycler_view);

        adapter = new GalleryAdapter(list,getActivity());

        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity().getApplicationContext(),2);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);

//        adapter.setOnItemClickListener(new GalleryAdapter.ClickListener() {
//            @Override
//            public void onItemClick(int position, View v) {
//                Log.d("onClick",""+v);
//            }
//
//            @Override
//            public void onItemLongClick(int position, View v) {
//
//            }
//        });

    }


    // FUNCTION TO LOAD JSON FROM DATABASE
    private void loadJson() {
        ApiInterface inf = ApiClient.getApiService();
        Call<GalleryList> call = inf.galleryList();
        call.enqueue(new Callback<GalleryList>() {
            @Override
            public void onResponse(Call<GalleryList> call, Response<GalleryList> response) {
                if(response.isSuccessful()) {
                    generateCustomer(response.body().getGalleryList());


                }else {
                    Toast.makeText(getActivity(), "" + response.errorBody(), Toast.LENGTH_SHORT).show();
                }
                swipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<GalleryList> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);

            }
        });


    }










}
