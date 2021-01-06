package com.arrowland.arrowland;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.arrowland.arrowland.Classes.CheckInternet;
import com.arrowland.arrowland.REST.ApiClient;
import com.arrowland.arrowland.REST.ApiInterface;
import com.wang.avi.AVLoadingIndicatorView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Membership extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Dialog dialog;
    ImageView closeImage;
    Button btnAccept,btnCancel;
    AVLoadingIndicatorView avi;
    SharedPreferences sharedPreferences;
    CheckInternet ci;
    private int ID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership);

        sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        ID = sharedPreferences.getInt("ID",0);


        dialog = new Dialog(this);

        ci = new CheckInternet(this);


        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.apply:
                        showDialog();

                        break;


                }
                return true;
            }
        });
    }


    public void showDialog() {
        dialog.setContentView(R.layout.dialog_membership);
        avi = dialog.findViewById(R.id.avi);
        closeImage =  dialog.findViewById(R.id.closeImage);
        btnAccept = dialog.findViewById(R.id.btnAccept);
        btnCancel = dialog.findViewById(R.id.btnCancel);
        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ci.isConnected()) {


                    avi.smoothToShow();

                    ApiInterface inf = ApiClient.getApiService();
                    Call<com.arrowland.arrowland.REST.Membership> call = inf.setMembership(ID);
                    call.enqueue(new Callback<com.arrowland.arrowland.REST.Membership>() {
                        @Override
                        public void onResponse(Call<com.arrowland.arrowland.REST.Membership> call, Response<com.arrowland.arrowland.REST.Membership> response) {
                            if (response.isSuccessful()) {
                                Log.e("onResponse", "" + response.body().getMessage());

                                Toast.makeText(Membership.this, "Successfully Apply! Thank you!", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(Membership.this, Home.class);
                                intent.putExtra("frag", "fragmentPendingMembership");
                                startActivity(intent);

                                finish();
                            }else {
                                Toast.makeText(Membership.this, "" + response.errorBody(), Toast.LENGTH_SHORT).show();
                            }

                            avi.smoothToHide();


                        }

                        @Override
                        public void onFailure(Call<com.arrowland.arrowland.REST.Membership> call, Throwable t) {
                            Log.e("onFailure", "" + t);
                            ci.cantConnectToServerMessage();

                        }
                    });

                }else {
                    ci.displayNoInternetMessage();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
