package com.arrowland.arrowland;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arrowland.arrowland.REST.ApiClient;
import com.arrowland.arrowland.REST.ApiInterface;
import com.arrowland.arrowland.REST.News;
import com.arrowland.arrowland.REST.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView txt1,txt2;
    Button btnStart;
    ImageView logo;

    private SharedPreferences sharedPreferences;
    private int id;
    private boolean isLoggedIn;

    Animation frombottom,fromtop,fadein;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //SHAREDPREFERENCE
        sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        id = sharedPreferences.getInt("ID",0);
        isLoggedIn = sharedPreferences.getBoolean("isLoggedIn",false);

        if(isLoggedIn) {
            Intent intent = new Intent(this, Home.class);
            startActivity(intent);
        }



        //        DECLARE ANIMATION
        fromtop = AnimationUtils.loadAnimation(this,R.anim.fromtop);
        frombottom = AnimationUtils.loadAnimation(this,R.anim.frombottom);
        fadein = AnimationUtils.loadAnimation(this,R.anim.fadein);

//        CHANGING FONT STYLE

        TextView txt1 = findViewById(R.id.textView4);
        TextView txt2 = findViewById(R.id.textView5);
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/monaco.ttf");
        txt1.setTypeface(typeface);
        txt2.setTypeface(typeface);

//        TEXT ANIMATION

        txt1.setAnimation(fadein);
        txt2.setAnimation(fadein);



//        IMAGE LOGO ANIMATION

        logo = findViewById(R.id.imageView1);
        logo.setAnimation(fromtop);


//        BUTTON ANIMATION

        btnStart = findViewById(R.id.button2);
        btnStart.setAnimation(frombottom);




//        Toast.makeText(this, ""+id, Toast.LENGTH_SHORT).show();




        // FIREBASE

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                Log.e("newToken",newToken);

                if(id != 0) {


                    insertNewToken(newToken);

                }
            }
        });


        // FIREBASE DATABASE

    }

    public void btnStart(View view){
        Intent intent = new Intent(this,Home.class);
        startActivity(intent);

    }

    public void insertNewToken(String token) {


//
//        ApiInterface apiInterface = ApiClient.getApiService();
//        Call<User> call = apiInterface.userToken(id,token);
//        call.enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//
//                Log.d("onResponse",""+response.body().getSuccess());
//
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//
//                Log.d("onFailure",""+t);
//
//            }
//        });
//
//
//
    }
}
