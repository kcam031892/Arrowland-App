package com.arrowland.arrowland.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arrowland.arrowland.Classes.CheckInternet;
import com.arrowland.arrowland.LessonSchedule;
import com.arrowland.arrowland.Login;
import com.arrowland.arrowland.R;
import com.arrowland.arrowland.REST.ApiClient;
import com.arrowland.arrowland.REST.ApiInterface;
import com.arrowland.arrowland.REST.Lesson;
import com.wang.avi.AVLoadingIndicatorView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mhack Bautista on 8/20/2018.
 */

public class LessonFragment extends Fragment {
    private View view;
    private Dialog dialog;
    private AVLoadingIndicatorView avi;
    private Button btnApply, btnCancel,btnEnroll,btnSetSchedule;
    private TextView txtContent,txtRemainingLesson,txtMessage;
    private ImageView closeImage;
    private RelativeLayout primaryLayout,pendingLayout,acceptedLayout;
    SharedPreferences sharedPreferences;
    private int ID;
    private boolean isLoggedIn;
    private CheckInternet ci;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_lesson,container,false);

        getActivity().setTitle("Lesson");

        dialog = new Dialog(getContext());

        primaryLayout = view.findViewById(R.id.lessonPrimaryLayout);

        pendingLayout = view.findViewById(R.id.lessonPendingLayout);

        acceptedLayout = view.findViewById(R.id.lessonAcceptedLayout);

        btnEnroll = view.findViewById(R.id.btnEroll);

        avi = view.findViewById(R.id.avi);

        txtRemainingLesson = view.findViewById(R.id.txtRemainingLesson);

        btnSetSchedule = view.findViewById(R.id.btnSetSchedule);

        txtMessage = view.findViewById(R.id.txtMessage);






        sharedPreferences = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        ID = sharedPreferences.getInt("ID",0);
        isLoggedIn = sharedPreferences.getBoolean("isLoggedIn",false);

        ci = new CheckInternet(getActivity());

        if(ci.isConnected()) {

            loadJson();

        }else {
            getActivity().setTitle("Enroll Lesson");
            primaryLayout.setVisibility(View.VISIBLE);
            primaryLayout();
        }














        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void loadJson() {
        avi.smoothToShow();
        ApiInterface inf = ApiClient.getApiService();
        Call<Lesson> call = inf.getLessonStatus(ID);
        call.enqueue(new Callback<Lesson>() {
            @Override
            public void onResponse(Call<Lesson> call, Response<Lesson> response) {
                final Lesson mResponse = response.body();
                if(response.isSuccessful()) {
                    setStatus(mResponse.getStatus());
                    if(mResponse.getStatus().equals("Accepted")) {
                        int session = 10;
                        int remaining = session - mResponse.getSession_count();
                        txtRemainingLesson.setText(remaining + " SESSION LEFT");
//                        Toast.makeText(getActivity(), ""+ mResponse.getAccepted_count(), Toast.LENGTH_SHORT).show();
                        if(mResponse.getPending_count() > 0) {
                            txtMessage.setVisibility(View.VISIBLE);
                            txtMessage.setText("You still have pending request please wait for the confirmation");
                            btnSetSchedule.setVisibility(View.INVISIBLE);
                        }else if(mResponse.getAccepted_count() > 0) {
                            txtMessage.setVisibility(View.VISIBLE);
                            txtMessage.setText(mResponse.getAccepted_message());
                            btnSetSchedule.setVisibility(View.INVISIBLE);

                        }else{
                            txtMessage.setVisibility(View.GONE);
                            btnSetSchedule.setVisibility(View.VISIBLE);

                        }



                        btnSetSchedule.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getActivity(), LessonSchedule.class);
                                intent.putExtra("lesson_id",mResponse.getId());
                                startActivity(intent);

                            }
                        });
                    }

                }else {
                    Toast.makeText(getActivity(), "" + response.errorBody(), Toast.LENGTH_SHORT).show();
                }
                avi.smoothToHide();
            }

            @Override
            public void onFailure(Call<Lesson> call, Throwable t) {
                ci.cantConnectToServerMessage();
                avi.smoothToHide();

            }
        });

    }



    private void setStatus(String status) {

        if(status.equals("None")) {
            primaryLayout.setVisibility(View.VISIBLE);
            primaryLayout();
            getActivity().setTitle("Enroll Lesson");
        }else if(status.equals("Accepted")){
            acceptedLayout.setVisibility(View.VISIBLE);
            getActivity().setTitle("Lesson");
        }else if(status.equals("Pending")) {
            pendingLayout.setVisibility(View.VISIBLE);
            getActivity().setTitle("Lesson");

        }else {
            primaryLayout.setVisibility(View.VISIBLE);
            primaryLayout();
            getActivity().setTitle("Enroll Lesson");

        }

    }


    private void primaryLayout() {




            btnEnroll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(isLoggedIn) {
                        showDialog();
                    }else {

                        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity(), R.style.CustomDialogTheme).create();
                        alertDialog.setTitle("Error!");
                        alertDialog.setMessage("Sorry, You must log in first before you can enroll");
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

    private void showDialog() {
//        dialog.setContentView(R.layout.dialog_membership);
//        closeImage = dialog.findViewById(R.id.closeImage);
//        btnApply = dialog.findViewById(R.id.btnAccept);
//        btnCancel = dialog.findViewById(R.id.btnCancel);
//        txtContent = dialog.findViewById(R.id.tvContent);
//
//        txtContent.setText("Enrolling for a lesson with cost P8,500. You will get notification when you get accepted");
//        btnApply.setText("Enroll");
//
//        closeImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//
//        btnApply.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                avi.smoothToShow();
//
//                ApiInterface inf = ApiClient.getApiService();
//                Call<Lesson> call = inf.setLesson(ID);
//                call.enqueue(new Callback<Lesson>() {
//                    @Override
//                    public void onResponse(Call<Lesson> call, Response<Lesson> response) {
//
//                        Lesson mResponse = response.body();
//
//                        if(response.isSuccessful()) {
//
//
//                            Toast.makeText(getActivity(), "Successfully Enroll! Thank you!", Toast.LENGTH_SHORT).show();
//                            primaryLayout.setVisibility(View.INVISIBLE);
//                            pendingLayout.setVisibility(View.VISIBLE);
//                            dialog.dismiss();
//
//
//
//
//                            avi.smoothToHide();
//
//                        }
//
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<Lesson> call, Throwable t) {
//
//                        ci.cantConnectToServerMessage();
//                        avi.smoothToHide();
//
//                    }
//                });
//
//            }
//        });
//
//        btnCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.show();


    }



}
