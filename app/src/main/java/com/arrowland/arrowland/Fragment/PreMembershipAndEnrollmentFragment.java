package com.arrowland.arrowland.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arrowland.arrowland.Home;
import com.arrowland.arrowland.LessonSchedule;
import com.arrowland.arrowland.R;
import com.arrowland.arrowland.REST.ApiClient;
import com.arrowland.arrowland.REST.ApiInterface;
import com.arrowland.arrowland.REST.Lesson;
import com.arrowland.arrowland.REST.Membership;
import com.wang.avi.AVLoadingIndicatorView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mhack Bautista on 10/11/2018.
 */

public class PreMembershipAndEnrollmentFragment extends Fragment {
    private View view;
    private LinearLayout llMembership,llLesson,llTransaction,llShow;
    private CheckBox cbMembership,cbLesson;
    private TextView tvSubtotal,tvDiscount,tvTotal;
    private int ID,subtotal = 0;
    private double Total;
    private Button btnSubmit;
    private AVLoadingIndicatorView avi;
    private Dialog dialogLesson,dialogMembership,dialogBoth;
    private String member_status;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pre_membership_enrollment,null);
        initViews();
        getActivity().setTitle("Pre-membership & Enrollment");

        dialogLesson = new Dialog(getContext());
        dialogMembership = new Dialog(getContext());
        dialogBoth = new Dialog(getContext());

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        ID = sharedPreferences.getInt("ID",0);

        loadMembershipStatus();
        loadLessonStatus();
        loadBothStatus();


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initViews() {
        llMembership = view.findViewById(R.id.llMembership);
        llLesson = view.findViewById(R.id.llLesson);
        llTransaction = view.findViewById(R.id.llTransaction);
        llShow = view.findViewById(R.id.llShow);

        cbMembership = view.findViewById(R.id.cbMembership);
        cbMembership.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    subtotal += 2000;
                    llTransaction.setVisibility(View.VISIBLE);
                    tvSubtotal.setText("Subtotal : " + subtotal);
                    deductDiscount();

                }else {
                    subtotal -= 2000;
                    tvSubtotal.setText("Subtotal : " + subtotal);
                    deductDiscount();

                }

            }
        });

        cbLesson = view.findViewById(R.id.cbLesson);
        cbLesson.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    subtotal += 8500;
                    llTransaction.setVisibility(View.VISIBLE);
                    tvSubtotal.setText("Subtotal : " + subtotal);
                    deductDiscount();

                }else {
                    subtotal -= 8500;
                    tvSubtotal.setText("Subtotal : " + subtotal);
                    deductDiscount();
                    Total = subtotal;
                }
            }
        });

        tvSubtotal = view.findViewById(R.id.tvSubtotal);
        tvDiscount = view.findViewById(R.id.tvDiscount);
        tvTotal = view.findViewById(R.id.tvTotal);


        btnSubmit = view.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cbMembership.isChecked() || cbLesson.isChecked()) {
                    if(cbLesson.isChecked() && cbMembership.isChecked()) {
                        dialogBoth();

                    }else if(cbMembership.isChecked()) {
                        showMembershipDialog();

                    }else if(cbLesson.isChecked()) {
                        dialogLesson();
                    }

                }else {
                    Toast.makeText(getActivity(), "Please Select your one or two", Toast.LENGTH_SHORT).show();


                }
            }
        });



        avi = view.findViewById(R.id.avi);

    }

    public void loadMembershipStatus() {

        avi.smoothToShow();

        ApiInterface inf = ApiClient.getApiService();
        Call<Membership> call = inf.getMembershipStatus(ID);
        call.enqueue(new Callback<Membership>() {
            @Override
            public void onResponse(Call<com.arrowland.arrowland.REST.Membership> call, Response<Membership> response) {

                com.arrowland.arrowland.REST.Membership membershipResponse = response.body();

                if(response.isSuccessful()) {

                    setMembershipStatus(membershipResponse.getStatus());
                    setDiscount(membershipResponse.getStatus());





                }else {
                    Toast.makeText(getActivity(), "" +response.errorBody(), Toast.LENGTH_SHORT).show();
                }
                avi.smoothToHide();


            }

            @Override
            public void onFailure(Call<com.arrowland.arrowland.REST.Membership> call, Throwable t) {

            }
        });

    }

    private void setMembershipStatus(String status) {
        if(status.equals("None")) {
            llMembership.setVisibility(View.VISIBLE);
        }else {
            llMembership.setVisibility(View.GONE);

        }

    }

    private void loadLessonStatus() {
        avi.smoothToShow();
        ApiInterface inf = ApiClient.getApiService();
        Call<Lesson> call = inf.getLessonStatus(ID);
        call.enqueue(new Callback<Lesson>() {
            @Override
            public void onResponse(Call<Lesson> call, Response<Lesson> response) {
                final Lesson mResponse = response.body();
                if(response.isSuccessful()) {
                    setLessonStatus(mResponse.getStatus());

                }else {
                    Toast.makeText(getActivity(), "" + response.errorBody(), Toast.LENGTH_SHORT).show();
                }
                avi.smoothToHide();
            }

            @Override
            public void onFailure(Call<Lesson> call, Throwable t) {
                avi.smoothToHide();

            }
        });
    }


    private void setLessonStatus(String status) {
        if(status.equals("None")) {
            llLesson.setVisibility(View.VISIBLE);
        }else {
            llLesson.setVisibility(View.GONE);


        }

    }

    private void loadBothStatus() {
        avi.smoothToShow();
        ApiInterface inf = ApiClient.getApiService();
        Call<Lesson> call = inf.statusBoth(ID);
        call.enqueue(new Callback<Lesson>() {
            @Override
            public void onResponse(Call<Lesson> call, Response<Lesson> response) {
                if(response.isSuccessful()) {
                    setBothStatus(response.body().getStatus());
                }
            }

            @Override
            public void onFailure(Call<Lesson> call, Throwable t) {

            }
        });


    }

    private void setBothStatus(String status) {

        if(status.equals("Success")) {
            llShow.setVisibility(View.VISIBLE);
        }else {
            llShow.setVisibility(View.GONE);
        }


    }


    private void setDiscount(String status) {
        if(status.equals("Accepted")) {
            tvDiscount.setText("Discount : 10%" );
            tvDiscount.setVisibility(View.VISIBLE);
        }else {
            tvDiscount.setText("");
            tvDiscount.setVisibility(View.GONE);
        }

    }

    private void deductDiscount() {
        String dc = tvDiscount.getText().toString();
        if(!dc.isEmpty()){
            double discount = subtotal * .10;
            Total = subtotal - discount;
            tvTotal.setText("Total : " + Total);
            member_status = "Member";

        }else {
            Total = subtotal;
            tvTotal.setText("Total : " + Total);
            member_status = "Non Member";

        }
    }

    private void isCheckMembership() {
        if(cbMembership.isChecked()) {
            double discount = subtotal * .10;
            Total = subtotal - discount;
            tvTotal.setText("Total : " + Total);
        }else {
            Total = subtotal;
            tvTotal.setText("Total : " + Total);


        }
    }

    private void dialogLesson() {
        dialogLesson.setContentView(R.layout.dialog_membership);
        ImageView closeImage = dialogLesson.findViewById(R.id.closeImage);
        Button btnApply = dialogLesson.findViewById(R.id.btnAccept);
        Button btnCancel = dialogLesson.findViewById(R.id.btnCancel);

        final AVLoadingIndicatorView avi = dialogLesson.findViewById(R.id.avi);
        TextView txtContent = dialogLesson.findViewById(R.id.tvContent);
        txtContent.setText("Enrolling for a lesson with cost PHP"+ Total+ ". You will get notification when you get accepted");
        btnApply.setText("Enroll");

        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogLesson.dismiss();
            }
        });

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                avi.smoothToShow();

                ApiInterface inf = ApiClient.getApiService();
                Call<Lesson> call = inf.setLesson(ID,member_status);
                call.enqueue(new Callback<Lesson>() {
                    @Override
                    public void onResponse(Call<Lesson> call, Response<Lesson> response) {

                        Lesson mResponse = response.body();

                        if(response.isSuccessful()) {


                            Toast.makeText(getActivity(), "Successfully Enroll! Thank you!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getContext(), Home.class);
                            intent.putExtra("frag", "fragmentLesson");
                            startActivity(intent);





                            avi.smoothToHide();

                        }


                    }

                    @Override
                    public void onFailure(Call<Lesson> call, Throwable t) {

                        avi.smoothToHide();

                    }
                });

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogLesson.dismiss();
            }
        });

        dialogLesson.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogLesson.show();


    }


    public void showMembershipDialog() {
        dialogMembership.setContentView(R.layout.dialog_membership);
        final AVLoadingIndicatorView avi = dialogMembership.findViewById(R.id.avi);
        ImageView closeImage =  dialogMembership.findViewById(R.id.closeImage);
        Button btnAccept = dialogMembership.findViewById(R.id.btnAccept);
        Button btnCancel = dialogMembership.findViewById(R.id.btnCancel);
        TextView txtContent = dialogMembership.findViewById(R.id.txtContent);

        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogMembership.dismiss();
            }
        });


        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                    avi.smoothToShow();

                    ApiInterface inf = ApiClient.getApiService();
                    Call<com.arrowland.arrowland.REST.Membership> call = inf.setMembership(ID);
                    call.enqueue(new Callback<com.arrowland.arrowland.REST.Membership>() {
                        @Override
                        public void onResponse(Call<com.arrowland.arrowland.REST.Membership> call, Response<com.arrowland.arrowland.REST.Membership> response) {
                            if (response.isSuccessful()) {
                                Log.e("onResponse", "" + response.body().getMessage());

                                Toast.makeText(getContext(), "Successfully Apply! Thank you!", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getContext(), Home.class);
                                intent.putExtra("frag", "fragmentMembership");
                                startActivity(intent);

                            }else {
                                Toast.makeText(getContext(), "" + response.errorBody(), Toast.LENGTH_SHORT).show();
                            }

                            avi.smoothToHide();


                        }

                        @Override
                        public void onFailure(Call<com.arrowland.arrowland.REST.Membership> call, Throwable t) {
                            Log.e("onFailure", "" + t);
                        }
                    });


            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogMembership.dismiss();
            }
        });
        dialogMembership.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogMembership.setCancelable(false);
        dialogMembership.show();

    }

    public void dialogBoth() {
        dialogBoth.setContentView(R.layout.dialog_membership);
        final AVLoadingIndicatorView avi = dialogBoth.findViewById(R.id.avi);
        ImageView closeImage =  dialogBoth.findViewById(R.id.closeImage);
        Button btnAccept = dialogBoth.findViewById(R.id.btnAccept);
        Button btnCancel = dialogBoth.findViewById(R.id.btnCancel);
        TextView txtContent = dialogBoth.findViewById(R.id.tvContent);
        txtContent.setText("Applying for membership and Enrolling lesson will cost of " + Total + ".You will get notified when you get accepted!");
        btnAccept.setText("Submit");
        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBoth.dismiss();
            }
        });


        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                avi.smoothToShow();

                ApiInterface inf = ApiClient.getApiService();
                Call<Lesson> call = inf.setBoth(ID,member_status);
                call.enqueue(new Callback<Lesson>() {
                    @Override
                    public void onResponse(Call<Lesson> call, Response<Lesson> response) {
                        if(response.isSuccessful()) {
                            Toast.makeText(getContext(), "Successfully Apply! Thank you!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getContext(), Home.class);
                            intent.putExtra("frag", "fragmentMembership");
                            startActivity(intent);

                            avi.smoothToHide();
                        }
                    }

                    @Override
                    public void onFailure(Call<Lesson> call, Throwable t) {
                        avi.smoothToHide();

                    }
                });


            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBoth.dismiss();
            }
        });
        dialogBoth.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogBoth.setCancelable(false);
        dialogBoth.show();

    }
}
