package com.arrowland.arrowland;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.arrowland.arrowland.REST.*;
import com.arrowland.arrowland.REST.Membership;
import com.google.android.gms.common.api.Api;
import com.wang.avi.AVLoadingIndicatorView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MembershipInput extends AppCompatActivity {
    private RadioGroup radioGroup;
    private Button btn;
    private int id,customer_id;
    private Dialog dialog;
    private ImageView closeImage;
    private Button btnSend,btnClose;
    private EditText etMembershipId;
    private AVLoadingIndicatorView avi;
    private TextView txtError;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership_input);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.fontWhite), PorterDuff.Mode.SRC_ATOP);
        this.setTitle("Select Status");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        id = intent.getExtras().getInt("ID");

        SharedPreferences sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        customer_id = sharedPreferences.getInt("ID",0);

        initViews();
    }


    private void initViews() {
        dialog = new Dialog(this);
        radioGroup = findViewById(R.id.radioGroup);
        radioGroup.clearCheck();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rb = (RadioButton) radioGroup.findViewById(i);
                if(null != rb && i > -1) {
                    if(i == R.id.radioButtonMember) {
                        showDialog();

                    }
                }
            }
        });



        btn = findViewById(R.id.btnChoosePayment);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(radioGroup.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(MembershipInput.this, "Please select your status!", Toast.LENGTH_SHORT).show();
                }else {
//                    Toast.makeText(MembershipInput.this, "Submit", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(MembershipInput.this, PaymentSelection.class);
                    intent1.putExtra("ID",id);
                    if(radioGroup.getCheckedRadioButtonId() == R.id.radioButtonMember) {
                        intent1.putExtra("member_status","Member");
                    }else {
                        intent1.putExtra("member_status","Non Member");
                    }
                    startActivity(intent1);

                }

            }
        });

    }


    private void showDialog() {

        dialog.setContentView(R.layout.dialog_input_membership);
        closeImage = dialog.findViewById(R.id.closeImage);
        btnSend = dialog.findViewById(R.id.btnSend);
        btnClose = dialog.findViewById(R.id.btnClose);
        etMembershipId = dialog.findViewById(R.id.etMembershipId);
        avi = dialog.findViewById(R.id.avi);
        txtError = dialog.findViewById(R.id.txtError);


        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioGroup.clearCheck();
                dialog.hide();

            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioGroup.clearCheck();
                dialog.hide();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                avi.smoothToShow();
                String membershipId = etMembershipId.getText().toString();
                ApiInterface inf = ApiClient.getApiService();
                Call<com.arrowland.arrowland.REST.Membership> call = inf.checkMembershipId(customer_id,membershipId);
                call.enqueue(new Callback<Membership>() {
                    @Override
                    public void onResponse(Call<Membership> call, Response<Membership> response) {
                        if(response.isSuccessful()) {
                            if(response.body().getMessage().equals("Success")) {
                                txtError.setVisibility(View.GONE);
                                radioGroup.check(R.id.radioButtonMember);
                                dialog.hide();
                            }else {
                                txtError.setVisibility(View.VISIBLE);
                                txtError.setText("Invalid Membership ID Please Try Again!");
                                etMembershipId.setText("");
//                                Toast.makeText(MembershipInput.this, "", Toast.LENGTH_SHORT).show();

                            }


                            avi.smoothToHide();
                        }
                    }

                    @Override
                    public void onFailure(Call<Membership> call, Throwable t) {
                        avi.smoothToHide();

                    }
                });


            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();


    }
}
