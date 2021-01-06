package com.arrowland.arrowland;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arrowland.arrowland.REST.Events;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class NotificationHomeDetail extends AppCompatActivity {
    String id,intentMsg;
    ImageView img;
    TextView txtTititle,txtMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_home_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.fontWhite), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();


            }
        });

        initCollapsingBar();

        img = findViewById(R.id.notification_detail_img);
        txtTititle = findViewById(R.id.notification_detail_tv_title);
        txtMessage = findViewById(R.id.notification_detail_tv_message);

        Intent intent = getIntent();
        id = intent.getExtras().getString("ID");
        if(intent.hasExtra("fbIntent")) {
            intentMsg = intent.getExtras().getString("fbIntent");
        }


        if(intentMsg.equals("Events")) {
            eventFirebaseDatabase();

        }else {
            newsFirebaseDatabase();

        }





    }

    private void generateList(ArrayList<Events> list) {

        for(int i=0; i < list.size();i++) {

            txtTititle.setText(list.get(i).getTitle());

            Picasso.with(this)
                    .load(list.get(i).getImage_url())
                    .placeholder(R.drawable.logo)
                    .fit()
                    .into(img);


            txtMessage.setText(list.get(i).getMessage());

        }



    }


    private void eventFirebaseDatabase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("events")
                .orderByChild("id")
                .equalTo(id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists()) {


                            ArrayList<Events> eventsList = new ArrayList<>();

                            for (DataSnapshot mEventsData: dataSnapshot.getChildren()) {
                                Events event = mEventsData.getValue(Events.class);
                                eventsList.add(event);



                            }

                            generateList(eventsList);

                        }



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void newsFirebaseDatabase(){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("news")
                .orderByChild("id")
                .equalTo(id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists()) {


                            ArrayList<Events> eventsList = new ArrayList<>();

                            for (DataSnapshot mEventsData: dataSnapshot.getChildren()) {
                                Events event = mEventsData.getValue(Events.class);
                                eventsList.add(event);



                            }

                            generateList(eventsList);

                        }



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }



    private void initCollapsingBar() {
        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(" ");
        AppBarLayout appBarLayout = findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();

                }

                if(scrollRange + verticalOffset == 0) {
                    if(intentMsg.equals("Events")) {
                        collapsingToolbarLayout.setTitle("Events");

                    }else {
                        collapsingToolbarLayout.setTitle("News");

                    }


                    isShow = true;
                }else if(isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }

            }
        });
    }
}
