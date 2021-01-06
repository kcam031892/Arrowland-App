package com.arrowland.arrowland.Fragment;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arrowland.arrowland.Adapter.NewsAdapter;
import com.arrowland.arrowland.Adapter.ReservationAdapter;
import com.arrowland.arrowland.Classes.App;
import com.arrowland.arrowland.Classes.ConnectivityReceiver;
import com.arrowland.arrowland.Classes.NetworkSchedulerService;
import com.arrowland.arrowland.R;
import com.arrowland.arrowland.REST.News;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mhack Bautista on 7/22/2018.
 */

public class NewsFragment extends Fragment{

    private View view;
    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private CoordinatorLayout coordinatorLayout;
    ArrayList<News> newsList = new ArrayList<>();



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_news,null);

        coordinatorLayout = view.findViewById(R.id.coordinatorLayout);


        fbRealtime();

//        scheduleJob();



//        checkConnection();





        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }



    private void generateList(ArrayList<News> list) {
        recyclerView = view.findViewById(R.id.card_recycler_news_list_view);
        adapter = new NewsAdapter(list,getActivity());
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);



        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

    }

    private void fbRealtime() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("news")
                .orderByChild("created_at")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                ArrayList<News> mNews = new ArrayList<>();
                for(DataSnapshot mNewsData: dataSnapshot.getChildren()){
                    News news = mNewsData.getValue(News.class);
                    mNews.add(news);
                }



                generateList(mNews);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
