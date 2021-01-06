package com.arrowland.arrowland;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arrowland.arrowland.Classes.PaypalConfig;
import com.arrowland.arrowland.REST.ApiClient;
import com.arrowland.arrowland.REST.ApiInterface;
import com.arrowland.arrowland.REST.Payment;
import com.arrowland.arrowland.REST.Reservation;
import com.arrowland.arrowland.REST.ReservationList;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentPaypal extends AppCompatActivity {
    private Button btnPay,btnOkay;
    private int id;
    private AVLoadingIndicatorView avi;
    private Toolbar toolbar;
    private Dialog dialog;
    private ImageView closeImage;
    private EditText etAmount;
    public static final int PAYPAL_REQUEST_CODE = 123;
    private static PayPalConfiguration config;
    private String paymentAmount,member_status;
    private TextView tvTotal,tvDiscount,tvAmount,tvMembershipId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_paypal);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.fontWhite), PorterDuff.Mode.SRC_ATOP);
        this.setTitle("Paypal");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        id = intent.getExtras().getInt("ID");
        member_status = intent.getExtras().getString("member_status");

        initViews();
        loadJson(id);

        if(member_status.equals("Member")) {
            tvMembershipId.setVisibility(View.VISIBLE);
            tvMembershipId.setText("With Membership");

        }

        // PAYPAL

        config = new PayPalConfiguration()
                // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
                // or live (ENVIRONMENT_PRODUCTION)
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(PaypalConfig.PAYPAL_CLIENT_ID);

        Intent intent2 = new Intent(this, PayPalService.class);

        intent2.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        startService(intent2);
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    private void initViews() {
        tvAmount = findViewById(R.id.tvAmount);
        tvMembershipId = findViewById(R.id.tvMembershipId);
        tvDiscount = findViewById(R.id.tvDiscount);
        tvTotal = findViewById(R.id.tvTotal);
        avi = findViewById(R.id.avi);
        dialog = new Dialog(this);
        btnPay = findViewById(R.id.btnPay);
        etAmount = findViewById(R.id.etAmount);

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMethod();
            }
        });



    }

    public void loadJson(int id) {
        avi.smoothToShow();
        ApiInterface inf = ApiClient.getApiService();
        Call<ReservationList> call = inf.reservationDetail(id);
        call.enqueue(new Callback<ReservationList>() {
            @Override
            public void onResponse(Call<ReservationList> call, Response<ReservationList> response) {

                Log.e("onResponse","" + response.body().getReservationList());

                if(response.isSuccessful()) {
                    for(Reservation data:response.body().getReservationList()) {


                        tvAmount.setText("Amount to pay : PHP "+round(data.getTotal(),0));
                        setTotal(data.getTotal());
                        setDiscount(data.getTotal());
                    }
                    avi.smoothToHide();

                }
            }

            @Override
            public void onFailure(Call<ReservationList> call, Throwable t) {
                Log.e("onFailure","" + t);
                avi.smoothToHide();

            }
        });
    }

    private void setTotal(double total) {
        double totalAmount = 0;
        if(member_status.equals("Member")) {
            totalAmount = total -  (total * .10);

        }else {
            totalAmount = total;
        }

        tvTotal.setText("Total : " + round(totalAmount,0));
        etAmount.setText("" + round(totalAmount,0));
        btnPay.setText("Pay "  + round(totalAmount,0) + " PHP");
//        int intTotalAmount = (int) totalAmount;
    }

    private void setDiscount(double total) {
        double totalDiscount = 0;

        if(member_status.equals("Member")) {
            totalDiscount = total * .10;

        }else {
            totalDiscount = 0;
        }

        tvDiscount.setText("Discount : " + round(totalDiscount,0));


    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    private void getMethod() {
        paymentAmount = etAmount.getText().toString();

        //Creating a paypalpayment
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(paymentAmount)), "PHP", "Arrowland",
                PayPalPayment.PAYMENT_INTENT_SALE);

        //Creating Paypal Payment activity intent
        Intent intent = new Intent(this, PaymentActivity.class);

        //putting the paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        //Puting paypal payment to the intent
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        //Starting the intent activity for result
        //the request code will be used on the method onActivityResult
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PAYPAL_REQUEST_CODE) {

            //If the result is OK i.e. user has not canceled the payment
            if (resultCode == Activity.RESULT_OK) {
                //Getting the payment confirmation
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                //if confirmation is not null
                if (confirm != null) {
                    try {
                        //Getting the payment details
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        Log.i("paymentExample", paymentDetails);

                        //Starting a new activity for the payment details and also putting the payment details with intent
//                        startActivity(new Intent(this, ConfirmationActivity.class)
//                                .putExtra("PaymentDetails", paymentDetails)
//                                .putExtra("PaymentAmount", paymentAmount));

//                        Toast.makeText(this, "Success Retrofit", Toast.LENGTH_SHORT).show();
//                        Toast.makeText(this, "" + confirm.toJSONObject().getString("id").toString(), Toast.LENGTH_SHORT).show();
//                        Log.e("ID",confirm.toJSONObject().getString("id"));
                        try {
                            JSONObject jsonDetails = new JSONObject(paymentDetails);
//                            Log.i("PAYMENT ID",jsonDetails.getJSONObject("response").getString("id"));
                            Log.e("PAYMENTID","" + jsonDetails.getJSONObject("response").getString("id"));
//                            setPayment(jsonDetails.getJSONObject("response").getString("id"));
                            setPayment(jsonDetails.getJSONObject("response").getString("id"));



                        }catch (JSONException e) {
                            Log.e("JSON ERROR" , "" + e);

                        }




                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }

    private void loadTotal() {

        ApiInterface inf = ApiClient.getApiService();
        Call<Reservation> call = inf.reservationTotal(id);
        call.enqueue(new Callback<Reservation>() {
            @Override
            public void onResponse(Call<Reservation> call, Response<Reservation> response) {
                if(response.isSuccessful()) {
                    btnPay.setText("Pay " + response.body().getTotal() + " Pesos");
                    etAmount.setText("" + response.body().getTotal());
                }
            }

            @Override
            public void onFailure(Call<Reservation> call, Throwable t) {

            }
        });


    }

    private void setPayment(String ver_id)   {

        ApiInterface inf = ApiClient.getApiService();
        Call<Payment> call = inf.setPaypalPayment(id,ver_id,member_status);
        call.enqueue(new Callback<Payment>() {
            @Override
            public void onResponse(Call<Payment> call, Response<Payment> response) {
                if(response.isSuccessful()) {
                    showDialog();
                }
            }

            @Override
            public void onFailure(Call<Payment> call, Throwable t) {
                Log.e("onError","" + t);

            }
        });
    }


    public void showDialog() {

        dialog.setContentView(R.layout.dialog_payment);
        closeImage = dialog.findViewById(R.id.closeImage);
        btnOkay = dialog.findViewById(R.id.btnOkay);

        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaymentPaypal.this,Home.class);
                intent.putExtra("frag","fragmentReservation");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

        btnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaymentPaypal.this,Home.class);
                intent.putExtra("frag","fragmentReservation");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();


    }
}
