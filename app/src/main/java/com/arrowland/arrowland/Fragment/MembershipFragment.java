package com.arrowland.arrowland.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arrowland.arrowland.Classes.CheckInternet;
import com.arrowland.arrowland.Home;
import com.arrowland.arrowland.Login;
import com.arrowland.arrowland.Membership;
import com.arrowland.arrowland.MembershipStatusCallbacks;
import com.arrowland.arrowland.R;
import com.arrowland.arrowland.REST.ApiClient;
import com.arrowland.arrowland.REST.ApiInterface;
import com.google.android.gms.common.api.Api;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mhack Bautista on 8/16/2018.
 */

public class MembershipFragment extends Fragment {
    private View view;
    private Button btnMembership,btnMembershipAgain;
    private boolean isLoggedIn;
    private int ID,responseId;
    private LayoutInflater mInflater;
    private String mStatus;
    private TextView txtStatus,txtDateStarted,txtDateEnded,txtRemainingDay,txtMembershipId,txtApplicationStatus;
    private LinearLayout linearLayout1;
    private RelativeLayout layoutPending,layoutAccepted;
    private AVLoadingIndicatorView avi;
    private CheckInternet ci;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_membership,container,false);

        getActivity().setTitle("Membership");

        //TYPEFACE
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(),"fonts/monaco.ttf");

        linearLayout1 = view.findViewById(R.id.memLayout);


        // MEMBERSHIP ACCEPTED INIT


        txtApplicationStatus = view.findViewById(R.id.tvApplicationStatus);
        txtApplicationStatus.setTypeface(typeface);


        txtMembershipId = view.findViewById(R.id.tvMembershipId);
        txtDateStarted = view.findViewById(R.id.tvDateStarted);
        txtDateEnded = view.findViewById(R.id.tvDateEnded);
        txtRemainingDay = view.findViewById(R.id.tvRemainingDay);
        avi = view.findViewById(R.id.avi);




        btnMembership = view.findViewById(R.id.btnMembership);
        btnMembershipAgain = view.findViewById(R.id.btnApplyMembershipAgain);
        layoutAccepted = view.findViewById(R.id.memLayoutAccepted);
        layoutPending = view.findViewById(R.id.memLayoutPending);

        ci = new CheckInternet(getActivity());



















        // SHAREDPREFERENCE

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        isLoggedIn = sharedPreferences.getBoolean("isLoggedIn",false);
        ID = sharedPreferences.getInt("ID",0);


        if(ci.isConnected()) {

            loadJson();

        }else {
            getActivity().setTitle("Apply for membership");
            linearLayout1.setVisibility(View.VISIBLE);
            primaryLayout();
        }



//        loadJsonAccepted();












        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }




    public void loadJson() {

        avi.smoothToShow();

        ApiInterface inf = ApiClient.getApiService();
        Call<com.arrowland.arrowland.REST.Membership> call = inf.getMembershipStatus(ID);
        call.enqueue(new Callback<com.arrowland.arrowland.REST.Membership>() {
            @Override
            public void onResponse(Call<com.arrowland.arrowland.REST.Membership> call, Response<com.arrowland.arrowland.REST.Membership> response) {

                com.arrowland.arrowland.REST.Membership membershipResponse = response.body();

                if(response.isSuccessful()) {

                    setStatus(membershipResponse.getStatus());





                }else {
                    Toast.makeText(getActivity(), "" +response.errorBody(), Toast.LENGTH_SHORT).show();
                }
                avi.smoothToHide();


            }

            @Override
            public void onFailure(Call<com.arrowland.arrowland.REST.Membership> call, Throwable t) {
                ci.cantConnectToServerMessage();
                primaryLayout();
                avi.smoothToHide();


            }
        });

    }


    private void loadJsonAccepted() {
        avi.smoothToShow();
        ApiInterface inf = ApiClient.getApiService();
        Call<com.arrowland.arrowland.REST.Membership> call = inf.getMembershipDetail(ID);
        call.enqueue(new Callback<com.arrowland.arrowland.REST.Membership>() {
            @Override
            public void onResponse(Call<com.arrowland.arrowland.REST.Membership> call, Response<com.arrowland.arrowland.REST.Membership> response) {
                com.arrowland.arrowland.REST.Membership membershipResponse = response.body();

                if(response.isSuccessful()) {

                    if(response.body().getCount() > 0) {


                    txtDateStarted.setText("Date Started : \t" + membershipResponse.getDate_started());
                    txtDateEnded.setText("Date Ended : \t" + membershipResponse.getDate_ended());
                    txtRemainingDay.setText("Remaining Day : \t" + membershipResponse.getRemaining_day());
                    txtMembershipId.setText("Membership ID : \t" + membershipResponse.getMembership_id());
                    btnMembershipAgain.setVisibility(View.INVISIBLE);

                    }else {
                        txtDateStarted.setText("Your application is expired.");
                        btnMembershipAgain.setVisibility(View.VISIBLE);
                    }



                    avi.smoothToHide();
                }

            }

            @Override
            public void onFailure(Call<com.arrowland.arrowland.REST.Membership> call, Throwable t) {

                ci.cantConnectToServerMessage();

                avi.smoothToHide();


            }
        });
    }


    private void setStatus(String status) {
        mStatus = status;
        if(mStatus.equals("None")) {
            linearLayout1.setVisibility(View.VISIBLE);
            primaryLayout();
            getActivity().setTitle("Apply For Membership");
        }else if(mStatus.equals("Accepted")){
            loadJsonAccepted();
            acceptedLayout();
            layoutAccepted.setVisibility(View.VISIBLE);
            getActivity().setTitle("My Membership");
        }else if(mStatus.equals("Pending")) {
            layoutPending.setVisibility(View.VISIBLE);
            getActivity().setTitle("My Membership");

        }

    }

    private void primaryLayout() {

        btnMembership.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isLoggedIn) {
                    Intent intent = new Intent(getActivity(), Membership.class);
                    startActivity(intent);

                }else {
                    final AlertDialog alertDialog = new AlertDialog.Builder(getActivity(), R.style.CustomDialogTheme).create();
                    alertDialog.setTitle("Error!");
                    alertDialog.setMessage("Sorry, You must log in first before you can apply for membership");
                    alertDialog.setCancelable(false);
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Login", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(getActivity(), Login.class);
                            startActivity(intent);

                        }
                    });


                    alertDialog.show();

                }

            }
        });

    }

    private void acceptedLayout() {
        btnMembershipAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isLoggedIn) {
                    Intent intent = new Intent(getActivity(), Membership.class);
                    startActivity(intent);

                }else {
                    final AlertDialog alertDialog = new AlertDialog.Builder(getActivity(), R.style.CustomDialogTheme).create();
                    alertDialog.setTitle("Error!");
                    alertDialog.setMessage("Sorry, You must log in first before you can apply for membership");
                    alertDialog.setCancelable(false);
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Login", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(getActivity(), Login.class);
                            startActivity(intent);

                        }
                    });


                    alertDialog.show();

                }

            }
        });
    }



    private String getStatus() {
        return mStatus;
    }







}
