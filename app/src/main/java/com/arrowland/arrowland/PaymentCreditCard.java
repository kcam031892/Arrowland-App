package com.arrowland.arrowland;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arrowland.arrowland.REST.ApiClient;
import com.arrowland.arrowland.REST.ApiInterface;
import com.arrowland.arrowland.REST.Payment;
import com.arrowland.arrowland.REST.Reservation;
import com.arrowland.arrowland.REST.ReservationList;
import com.braintreepayments.cardform.view.CardForm;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.wang.avi.AVLoadingIndicatorView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentCreditCard extends AppCompatActivity {
    private Button btnPay,btnOkay;
    private int id;
    private AVLoadingIndicatorView avi;
    private Toolbar toolbar;
    private Dialog dialog;
    private ImageView closeImage;
    private String member_status;
    private TextView tvTotal,tvDiscount,tvAmount,tvMembershipId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_credit_card);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.fontWhite), PorterDuff.Mode.SRC_ATOP);
        this.setTitle("Credit Card");

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
        loadTotal();
        loadJson(id);

        if(member_status.equals("Member")) {
            tvMembershipId.setVisibility(View.VISIBLE);
            tvMembershipId.setText("With Membership");

        }



    }


    private void initViews() {

        avi = findViewById(R.id.avi);
        dialog = new Dialog(this);
        tvAmount = findViewById(R.id.tvAmount);
        tvMembershipId = findViewById(R.id.tvMembershipId);
        tvDiscount = findViewById(R.id.tvDiscount);
        tvTotal = findViewById(R.id.tvTotal);

        final CardForm cardForm = (CardForm) findViewById(R.id.card_form);
        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .actionLabel("Payment")
                .setup(this);

        btnPay = findViewById(R.id.btnPay);
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    cc(cardForm.getCardNumber(),Integer.parseInt(cardForm.getExpirationMonth()),Integer.parseInt(cardForm.getExpirationYear()),cardForm.getCvv());

                }catch (NumberFormatException e) {
                    Toast.makeText(PaymentCreditCard.this, "Fill out all forms", Toast.LENGTH_SHORT).show();
                }

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
        btnPay.setText("Pay " + round(totalAmount,0) + " Pesos");
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

    public void cc(String cardNumber, int cardExpMonth, int cardExpYear, String cardCVC) {

        if (cardNumber.isEmpty() || cardExpMonth < 0 || cardExpYear < 0 || cardCVC.isEmpty() ) {
            Toast.makeText(this, "Fill out all forms", Toast.LENGTH_SHORT).show();

        }else {


            Card card = new Card(
                    cardNumber,
                    cardExpMonth,
                    cardExpYear,
                    cardCVC
            );



            card.validateNumber();
            card.validateCVC();
            if(!card.validateCard()) {
                Toast.makeText(this, "Failed! Incorrect Information", Toast.LENGTH_SHORT).show();
            }else {
                avi.smoothToShow();
                Stripe stripe = new Stripe(this, "pk_test_Zj65WHDQuE076alE3zh7XB6g");
                stripe.createToken(
                        card,
                        new TokenCallback() {
                            public void onSuccess(Token token) {

                                ApiInterface inf = ApiClient.getApiService();
                                Call<Payment> call = inf.setCcPayment(id,token.getId(),member_status);
                                call.enqueue(new Callback<Payment>() {
                                    @Override
                                    public void onResponse(Call<Payment> call, Response<Payment> response) {
                                        if(response.isSuccessful()) {
//                                            Toast.makeText(PaymentCreditCard.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                            avi.smoothToHide();
                                            showDialog();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Payment> call, Throwable t) {
                                        avi.smoothToHide();

                                    }
                                });
    //

                            }
                            public void onError(Exception error) {
                                avi.smoothToHide();
                                // Show localized error message
                                Toast.makeText(PaymentCreditCard.this, "You number card is incorrect!" , Toast.LENGTH_SHORT).show();
                            }
                        }
                );
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

                }
            }

            @Override
            public void onFailure(Call<Reservation> call, Throwable t) {

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
                Intent intent = new Intent(PaymentCreditCard.this,Home.class);
                intent.putExtra("frag","fragmentReservation");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

        btnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaymentCreditCard.this,Home.class);
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
