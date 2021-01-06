package com.arrowland.arrowland.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arrowland.arrowland.Adapter.EventsAdapter;
import com.arrowland.arrowland.R;
import com.arrowland.arrowland.REST.Events;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Mhack Bautista on 7/22/2018.
 */

public class UpcomingEventsFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private EventsAdapter adapter;
    private LinearLayoutManager layoutManager;
    private ShimmerFrameLayout shimmerFrameLayout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_events,null);

        initCollapsingBar();

//        shimmerFrameLayout = view.findViewById(R.id.shimmer_events);

        firebaseDatabase();

        return view;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }




    private void generateList(ArrayList<Events> list) {
        recyclerView = view.findViewById(R.id.card_recycler_events_list_view);
        adapter = new EventsAdapter(list,getContext());
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void firebaseDatabase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("events")
                .orderByChild("created_at")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        ArrayList<Events> eventsList = new ArrayList<>();

                        for(DataSnapshot mEventsData: dataSnapshot.getChildren()) {
                            Events events = mEventsData.getValue(Events.class);
                            eventsList.add(events);
                        }

                        generateList(eventsList);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void initCollapsingBar() {
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(" ");

        AppBarLayout appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }

                if(scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle("Events");
                    isShow = true;
                }else if(isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }


            }
        });

    }

}
