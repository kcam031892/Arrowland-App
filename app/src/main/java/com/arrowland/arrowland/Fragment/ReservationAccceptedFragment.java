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

import com.arrowland.arrowland.Adapter.ReservationAdapter;
import com.arrowland.arrowland.Classes.CheckInternet;
import com.arrowland.arrowland.R;
import com.arrowland.arrowland.REST.ApiClient;
import com.arrowland.arrowland.REST.ApiInterface;
import com.arrowland.arrowland.REST.Reservation;
import com.arrowland.arrowland.REST.ReservationList;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mhack Bautista on 8/14/2018.
 */

public class ReservationAccceptedFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private View view,view_card;
    private RecyclerView recyclerView;
    private ReservationAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int id;
    private CheckInternet ci;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_reservation_accepted,null);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        id = sharedPreferences.getInt("ID",0);

        view_card = view.findViewById(R.id.view_empty);


        ci = new CheckInternet(getContext());

        if(!ci.isConnected()) {
            ci.displayNoInternetMessage();
        }



        swipeRefreshLayout = view.findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);

            }
        });


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

    private void generateList(ArrayList<Reservation> list) {



        recyclerView = view.findViewById(R.id.recycler_view);
        adapter = new ReservationAdapter(list, getActivity());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        if(adapter.getItemCount() > 0) {


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

        String status = "Accepted";

        ApiInterface inf = ApiClient.getApiService();
        Call<ReservationList> call = inf.myReservationList(id,status);
        call.enqueue(new Callback<ReservationList>() {
            @Override
            public void onResponse(Call<ReservationList> call, Response<ReservationList> response) {

                if(response.isSuccessful()) {
                    generateList(response.body().getReservationList());

                }else {
                    Toast.makeText(getActivity(), ""  + response.errorBody(), Toast.LENGTH_SHORT).show();
                }
                swipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<ReservationList> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                ci.cantConnectToServerMessage();
            }
        });

    }
}
