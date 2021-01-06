package com.arrowland.arrowland.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.arrowland.arrowland.Adapter.NotificationAdapter;
import com.arrowland.arrowland.R;
import com.arrowland.arrowland.REST.ApiClient;
import com.arrowland.arrowland.REST.ApiInterface;
import com.arrowland.arrowland.REST.NotificationList;
import com.arrowland.arrowland.REST.Notifications;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mhack Bautista on 9/20/2018.
 */

public class NotificationFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private View view,view_card;
    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notification,null);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        id = sharedPreferences.getInt("ID",0);

        initViews();
        loadJson();


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initViews() {
        view_card = view.findViewById(R.id.view_empty);
        swipeRefreshLayout = view.findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);

            }
        });

    }

    private void generateList(ArrayList<Notifications> list) {
        recyclerView  = view.findViewById(R.id.recycler_view);
        adapter = new NotificationAdapter(list,getActivity());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        if(adapter.getItemCount() > 0 ) {
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
            view_card.setVisibility(View.INVISIBLE);
        }else {
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
            view_card.setVisibility(View.VISIBLE);
        }

    }

    private void loadJson() {
        ApiInterface inf = ApiClient.getApiService();
        Call<NotificationList> call = inf.getNotficationList(id);
        call.enqueue(new Callback<NotificationList>() {
            @Override
            public void onResponse(Call<NotificationList> call, Response<NotificationList> response) {
                if(response.isSuccessful()) {
                    generateList(response.body().getNotificationList());

                }else {
                    Toast.makeText(getActivity(), "" + response.errorBody(), Toast.LENGTH_SHORT).show();
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<NotificationList> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);

            }
        });

    }

    @Override
    public void onRefresh() {
        loadJson();

    }
}
