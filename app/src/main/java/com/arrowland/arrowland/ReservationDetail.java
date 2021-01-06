package com.arrowland.arrowland;

import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.wang.avi.AVLoadingIndicatorView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationDetail extends AppCompatActivity {

    Intent intentHome;
    ImageView img;
    private Toolbar toolbar;
    TextView txtResTime,txtResDate,txtResId,txtPackage,txtCoaches,txtTotal;
    Button btn,btnCancel;
    String text2Qr,status;
    AVLoadingIndicatorView avi,avi2;
    private Dialog dialog;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_detail);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.fontWhite), PorterDuff.Mode.SRC_ATOP);
        toolbar.setTitle("Reservation Detail");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBack();

                finish();


            }
        });

        initViews();

        Intent intent = getIntent();
        id = intent.getExtras().getInt("ID");
        status = intent.getExtras().getString("Status");
        Log.e("Status", "" + status);

        if(status.equals("Accepted")) {
            buttonCancel();
        }


        loadJson(id);
        loadCheckPayment(id);


    }

    public void initViews() {
        txtResTime = findViewById(R.id.tvResTime);
        txtResDate = findViewById(R.id.tvResDate);
        txtResId = findViewById(R.id.tvResId);
        txtPackage = findViewById(R.id.tvPackage);
        txtTotal = findViewById(R.id.tvTotal);
        txtCoaches = findViewById(R.id.tvCoaches);
        btn = findViewById(R.id.mBtn1);
        avi = findViewById(R.id.avi);
        btnCancel = findViewById(R.id.btnCancel);
        dialog = new Dialog(this);


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



                        txtResDate.setText("Date : " + data.getDate());
                        txtResTime.setText("Time : " + data.getTime());
                        txtResId.setText("Code : " + data.getReservation_code());
                        txtPackage.setText("Package : " + data.getmPckage());
                        if(data.getWith_coaches().equals("1")) {
                            txtCoaches.setText("Coach : With Coach");

                        }else {
                            txtCoaches.setText("Coach : Without Coach");

                        }

                        txtTotal.setText("Total : PHP "+data.getTotal());
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

    public void loadCheckPayment(int id) {
        ApiInterface inf = ApiClient.getApiService();
        Call<Payment> call = inf.checkReservationPayment(id);
        call.enqueue(new Callback<Payment>() {
            @Override
            public void onResponse(Call<Payment> call, Response<Payment> response) {
                if(response.isSuccessful()) {
                    Payment mCall = response.body();
                    buttonIntent(mCall.getSuccess(),mCall.getId());

                }
            }

            @Override
            public void onFailure(Call<Payment> call, Throwable t) {

            }
        });

    }

    public void buttonIntent(int success, final int payment_id) {

        Log.e("ID","" + id);

        if(success == 0 ) {
            btn.setVisibility(View.VISIBLE);
            btn.setText("Please pay your reservation fee");
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent1 = new Intent(ReservationDetail.this, MembershipInput.class);
                    intent1.putExtra("ID",id);
                    startActivity(intent1);
                }
            });

        }else if(success == 1){
            btn.setVisibility(View.VISIBLE);
            btn.setText("Check your payment detail ");
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(ReservationDetail.this, "YOUR ARE PAID!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ReservationDetail.this, PaymentDetail.class);
                    intent.putExtra("ID",payment_id);
                    startActivity(intent);
                }
            });

        }

    }

    public void buttonCancel() {
        btnCancel.setVisibility(View.VISIBLE);
        btnCancel.setText("Cancel your reservation");
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();

            }
        });

    }


    public void generateQrCode(String txtQr) {

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(txtQr, BarcodeFormat.QR_CODE,500,250);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            img.setImageBitmap(bitmap);

        }catch (WriterException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {

        setBack();


        FragmentManager fm = getFragmentManager();
        fm.popBackStack("My Reservation",FragmentManager.POP_BACK_STACK_INCLUSIVE);

        finish();

    }

    private void setBack() {

        Intent intent = new Intent(this,Home.class);
        intent.putExtra("frag","fragmentReservation");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    private void showDialog() {
        dialog.setContentView(R.layout.dialog_danger);
        ImageView closeImage = dialog.findViewById(R.id.closeImage);
        Button btn = dialog.findViewById(R.id.btn1);
        TextView txtContent = dialog.findViewById(R.id.txtContent);
        avi2 = dialog.findViewById(R.id.avi);


        txtContent.setText("Cancellation of Reservation has a penalty. Once you cancel your reservation fee will be deducted of 200");
        btn.setText("Cancel your reservation");

        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.hide();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                avi2.smoothToShow();
                ApiInterface inf = ApiClient.getApiService();
                Call<ReservationList> call = inf.reservationCancel(id);
                call.enqueue(new Callback<ReservationList>() {
                    @Override
                    public void onResponse(Call<ReservationList> call, Response<ReservationList> response) {
                        if(response.isSuccessful()){
                            if(response.body().getMessage().equals("Success")) {
                                Toast.makeText(ReservationDetail.this, "Successfully Cancel!", Toast.LENGTH_SHORT).show();
                                setBack();

                            }

                        }else {
                            Toast.makeText(ReservationDetail.this, "" + response.errorBody(), Toast.LENGTH_SHORT).show();
                        }
                        avi2.smoothToHide();

                    }

                    @Override
                    public void onFailure(Call<ReservationList> call, Throwable t) {
                        avi2.smoothToHide();
                        dialog.hide();

                    }
                });

            }
        });


        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

}
