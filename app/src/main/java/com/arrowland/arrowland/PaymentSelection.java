package com.arrowland.arrowland;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class PaymentSelection extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView imgSmart,imgCebuanna,imgCc,imgPaypal;
    private int id;
    private String member_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_selection);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.fontWhite), PorterDuff.Mode.SRC_ATOP);
        this.setTitle("Select Payment");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        initViews();





    }

    public void initViews() {

        imgSmart = findViewById(R.id.img_smart_padala);
        imgCebuanna = findViewById(R.id.img_cebuana);
        imgCc = findViewById(R.id.img_cc);
        imgPaypal = findViewById(R.id.img_paypal);

        Intent intent = getIntent();
        id = intent.getExtras().getInt("ID");
        member_status = intent.getExtras().getString("member_status");


        imgSmart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(PaymentSelection.this,PaymentSmartPadala.class);
                intent1.putExtra("ID",id);
                intent1.putExtra("member_status",member_status);
                startActivity(intent1);

            }
        });

        imgCebuanna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent1 = new Intent(PaymentSelection.this,PaymentCebuanna.class);
                intent1.putExtra("ID",id);
                intent1.putExtra("member_status",member_status);
                startActivity(intent1);


            }
        });

        imgCc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(PaymentSelection.this,PaymentCreditCard.class);
                intent1.putExtra("ID",id);
                intent1.putExtra("member_status",member_status);
                startActivity(intent1);

            }
        });

        imgPaypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(PaymentSelection.this,PaymentPaypal.class);
                intent1.putExtra("ID",id);
                intent1.putExtra("member_status",member_status);
                startActivity(intent1);

            }
        });

    }
}
