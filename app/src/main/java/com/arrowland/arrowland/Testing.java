package com.arrowland.arrowland;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.arrowland.arrowland.REST.ApiClient;
import com.arrowland.arrowland.REST.ApiInterface;
import com.arrowland.arrowland.REST.Test1;
import com.arrowland.arrowland.REST.Test1List;
import com.arrowland.arrowland.REST.User;
import com.arrowland.arrowland.REST.UserList;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Testing extends AppCompatActivity implements View.OnClickListener {

    private Button btnTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);

        btnTest = findViewById(R.id.button3);
        btnTest.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
//        ApiInterface inf = ApiClient.getApiService();
//        Call<User> call = inf.test();
//        call.enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//                Log.e("onResponse", "" + response.body().getSuccess());
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//                Log.e("onFailure","" + t);
//
//            }
//        });

//        ApiInterface inf = ApiClient.getApiService();
//        Call<Test1List> call = inf.userLogin("admin","1234");
//        call.enqueue(new Callback<Test1List>() {
//            @Override
//            public void onResponse(Call<Test1List> call, Response<Test1List> response) {
//                ArrayList<Test1> test1Lists = response.body().getServer_response();
//
//                for (Test1 test1: test1Lists) {
//                    Toast.makeText(Testing.this, "" + test1.getCode(), Toast.LENGTH_SHORT).show();
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<Test1List> call, Throwable t) {
//
//            }
//        });

    }
}
