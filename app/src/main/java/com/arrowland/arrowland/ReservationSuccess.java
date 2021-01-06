package com.arrowland.arrowland;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ReservationSuccess extends AppCompatActivity {
    private int id;
    private Button btnPay,btnInstruction,btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_success);

        Intent intent = getIntent();
        id = intent.getExtras().getInt("ID");


        initViews();
    }

    public void initViews() {
        btnPay = findViewById(R.id.btnPayNow);
        btnInstruction = findViewById(R.id.btnPaymentInstruction);
        btnClose = findViewById(R.id.btnClose);

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReservationSuccess.this, ReservationDetail.class);
                intent.putExtra("Status","Pending");
                intent.putExtra("ID",id);
                startActivity(intent);
            }
        });


        btnInstruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReservationSuccess.this, Home.class);
                intent.putExtra("frag","fragmentPaymentInstruction");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReservationSuccess.this,Home.class);
                intent.putExtra("frag","fragmentReservation");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ReservationSuccess.this,Home.class);
        startActivity(intent);
    }
}
