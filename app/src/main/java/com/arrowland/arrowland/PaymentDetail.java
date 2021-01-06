package com.arrowland.arrowland;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.arrowland.arrowland.REST.ApiClient;
import com.arrowland.arrowland.REST.ApiInterface;
import com.arrowland.arrowland.REST.Payment;
import com.wang.avi.AVLoadingIndicatorView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentDetail extends AppCompatActivity {

    TextView txtPaymentMethod,txtSender,txtReference,txtAmount;
    AVLoadingIndicatorView avi;
    Toolbar toolbar;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_detail);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.fontWhite), PorterDuff.Mode.SRC_ATOP);
        toolbar.setTitle("Payment Detail");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        Intent intent = getIntent();
        id = intent.getExtras().getInt("ID");
        loadJson(id);


        initViews();
        avi.smoothToShow();


    }

    private void initViews() {
        txtPaymentMethod = findViewById(R.id.tvPaymentMethod);
        txtSender = findViewById(R.id.tvSender);
        txtReference = findViewById(R.id.tvReferenceNumber);
        txtAmount = findViewById(R.id.tvAmount);
        avi = findViewById(R.id.avi);
    }

    private void loadJson(int id) {

        ApiInterface inf = ApiClient.getApiService();
        Call<Payment> call = inf.detailPayment(id);
        call.enqueue(new Callback<Payment>() {
            @Override
            public void onResponse(Call<Payment> call, Response<Payment> response) {
                if(response.isSuccessful()) {
                    Payment mResponse = response.body();
                    txtPaymentMethod.setText(mResponse.getPayment_method());
                    txtSender.setText(mResponse.getSender_name());
                    txtReference.setText(mResponse.getTransaction_code());
                    txtAmount.setText("" + mResponse.getAmount_pay());


                    avi.smoothToHide();
                }
            }

            @Override
            public void onFailure(Call<Payment> call, Throwable t) {
                avi.smoothToHide();

            }
        });


    }
}
