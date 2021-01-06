package com.arrowland.arrowland.Fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.arrowland.arrowland.Classes.CheckInternet;
import com.arrowland.arrowland.Login;
import com.arrowland.arrowland.R;
import com.arrowland.arrowland.REST.ApiClient;
import com.arrowland.arrowland.REST.ApiInterface;
import com.arrowland.arrowland.REST.Gallery;
import com.arrowland.arrowland.REST.Reservation;
import com.arrowland.arrowland.REST.ReservationList;
import com.arrowland.arrowland.ReservationDetail;
import com.arrowland.arrowland.ReservationSuccess;
import com.squareup.picasso.Picasso;
import com.squareup.timessquare.CalendarPickerView;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mhack Bautista on 8/1/2018.
 */

public class ReservationFragment extends Fragment {
    private Calendar calendar;
    private TextView tvDate,tvYear,tvTotal,tvPackagePrice,tvCoachPrice;
    private View view;
    private CardView cv,cv2;
    private Spinner spinner,spinnerPackage;
    private String reservation_date;
    private String reservation_time;
    private int id,success;
    private Button btnReserve,btnResDate;
    private ImageView img1;
    private CheckInternet ci;
    private boolean isLoggedIn = false;
    private LinearLayout linearTime;
    private AlertDialog alertDialog;
    private AVLoadingIndicatorView avi;
    private CheckBox checkBox;
    private int subtotal = 0;
    private double Total = 0;
    private String with_coaches = "0";

    ArrayList<String> time = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_reservation,null);
        getActivity().setTitle("Make a Reservation");


        // DATE INITIALIZATION

        tvDate = view.findViewById(R.id.tvDate);
        tvYear = view.findViewById(R.id.tvYear);
        tvTotal = view.findViewById(R.id.txtTotal);
        tvPackagePrice = view.findViewById(R.id.tvPackagePrice);
        tvCoachPrice = view.findViewById(R.id.tvCoachPrice);




        // TIME INITIALIZATION

        cv2 = view.findViewById(R.id.cvTime);
        spinner = view.findViewById(R.id.spinner3);
        spinnerPackage = view.findViewById(R.id.spinnerPackage);
        checkBox = view.findViewById(R.id.checkBox2);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    Total += 200;
                    tvTotal.setText("Total : " + Total);
                    with_coaches = "1";
                    tvCoachPrice.setText("Coach Price is PHP : " +200);
                    tvCoachPrice.setVisibility(View.VISIBLE);



                }else {
                    Total -= 200;
                    tvTotal.setText("Total : " + Total);
                    with_coaches = "0";
                    tvCoachPrice.setVisibility(View.GONE);
                }

            }
        });
//        time = new ArrayList<>(Arrays.asList("1","2","3","4"));
        linearTime = view.findViewById(R.id.linearTime);

        // LOADING SPINNER

        avi = view.findViewById(R.id.avi);


        // CHECK INTERNET CONNECTION
        ci = new CheckInternet(getContext());
        if(!ci.isConnected()) {
            ci.displayNoInternetMessage();
        }

        // ALERT DIALOG

        alertDialog = new AlertDialog.Builder(getContext(),R.style.CustomDialogTheme).create();




        // RESERVE BUTTON

        btnReserve = view.findViewById(R.id.btnSetReservation);
        btnReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int count = spinner.getSelectedItemPosition();
                int countPackage = spinnerPackage.getSelectedItemPosition();

                if(count == 0) {
                    Toast.makeText(getActivity(), "Please select a preferred time", Toast.LENGTH_SHORT).show();

                }else if(countPackage == 0) {
                    Toast.makeText(getActivity(), "Please select a package", Toast.LENGTH_SHORT).show();

                }else {
                    reservation_time = spinner.getSelectedItem().toString();
                    insertReservation(reservation_date,reservation_time,countPackage,with_coaches,Total);

                }

            }
        });


        btnResDate = view.findViewById(R.id.btnResDate);

        time.add(0,"Select an available time");
        time.add(1,"7:00AM - 8:00AM");
        time.add(2,"8:00AM - 9:00AM");
        time.add(3,"9:00AM - 10:00AM");
        time.add(4,"10:00AM - 11:00AM");
        time.add(5,"11:00AM - 12:00PM");
        time.add(6,"12:00PM - 1:00PM");
        time.add(7,"1:00PM - 2:00PM");




        // SHAREDPREFERNCE
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        id = sharedPreferences.getInt("ID",0);
        isLoggedIn = sharedPreferences.getBoolean("isLoggedIn",false);



        // Picasso

        img1 = view.findViewById(R.id.img_reservation);
        Picasso.with(getContext())
                .load("http://udarbedentalclinic.com/Arrowland/media/img_reservation.jpg")
                .fit()
                .into(img1);








        // CALENDAR
        calendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                updateLabel();
                getReservationDate();
                loadTime();
                spinner.setSelection(0);




//                Toast.makeText(getActivity(), ""+countSpinner(), Toast.LENGTH_SHORT).show();
//                cv2.setVisibility(View.VISIBLE);

            }
        };


        btnResDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog dialog = new DatePickerDialog(getActivity(),date,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                );

                long day = 86400000;
                long month = day * 30;
                long max = month * 3;

                dialog.getDatePicker().setMinDate(new Date().getTime() + 86400000);
                dialog.getDatePicker().setMaxDate(new Date().getTime() + max);

                dialog.show();


            }
        });


//
//        LinearLayout linearLayout = view.findViewById(R.id.llayout3);
//        ArrayList<Integer> arrayList = new ArrayList<>();
//        arrayList.add(1);
//        arrayList.add(2);
//        arrayList.add(3);




        showSpinner();
        packageSpinner();



//
//    if(!isLoggedIn) {
//            final AlertDialog alertDialog = new AlertDialog.Builder(getContext(),R.style.CustomDialogTheme).create();
//            alertDialog.setTitle("Error!");
//            alertDialog.setMessage("You must log in first before you can set your reservation");
//            alertDialog.setCancelable(false);
//            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Cancel", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    alertDialog.dismiss();
//                }
//            });
//            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Login", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    Intent intent = new Intent(getActivity(), Login.class);
//                    startActivity(intent);
//
//                }
//            });
//
//
//            alertDialog.show();
//
//
//
//
//    }

//        intentService();

//        loadJson();
    return view;


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void updateLabel(){
        String format = "EEE dd MMMM";
        int year = calendar.get(Calendar.YEAR);
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        tvDate.setText(sdf.format(calendar.getTime()));
        tvYear.setText("" + year);

    }

    public void getReservationDate() {
        String format = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format,Locale.getDefault());
        reservation_date = sdf.format(calendar.getTime());
    }


    public void showSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),R.layout.spinner_style2,time);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i != 0) {
//                    Toast.makeText(getActivity(), "" + spinner.getSelectedItem(), Toast.LENGTH_SHORT).show();



                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    public void packageSpinner() {
        List<String> list = new ArrayList<String>();
        list.add(0,"Select Package");
        list.add(1,"25 Arrows");
        list.add(2,"50 Arrows");
        list.add(3,"75 Arrows");
        list.add(4,"120 Arrows");
        list.add(5,"150 Arrows");
        list.add(6,"200 Arrows");
        list.add(7,"240 Arrows");
        list.add(8,"300 Arrows");

        final int[] prices = {250,475,675,1020,1200,1500,1680,1950};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),R.layout.spinner_style2,list);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerPackage.setAdapter(adapter);

        spinnerPackage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Total = 300;

                if(spinnerPackage.getSelectedItemPosition() != 0) {
                    Total += prices[spinnerPackage.getSelectedItemPosition() - 1];
                    checkBox.setChecked(false);
                    tvCoachPrice.setVisibility(View.GONE);
                    tvTotal.setText("Total : " + Total);
                    tvPackagePrice.setText("Package Price is PHP " + prices[spinnerPackage.getSelectedItemPosition() - 1] );


                }else {
                    Total += 0;
                    checkBox.setChecked(false);
                }





            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    public int countSpinner() {
        int spinnerCount = spinner.getCount();
        return spinnerCount;
    }


    public void loadTime() {


//        time = new ArrayList<>(Arrays.asList("1","2","3","4"));


        time.clear();
        time.add(0,"Select A time");
        time.add(1,"7:00AM - 8:00AM");
        time.add(2,"8:00AM - 9:00AM");
        time.add(3,"9:00AM - 10:00AM");
        time.add(4,"10:00AM - 11:00AM");
        time.add(5,"11:00AM - 12:00PM");
        time.add(6,"12:00PM - 1:00PM");
        time.add(7,"1:00PM - 2:00PM");

        avi.smoothToShow();






        ApiInterface inf = ApiClient.getApiService();
        Call<ReservationList> call = inf.getReservationTime(reservation_date,id);
        call.enqueue(new Callback<ReservationList>() {
            @Override
            public void onResponse(Call<ReservationList> call, Response<ReservationList> response) {
                ArrayList<Reservation> reservations = response.body().getReservationList();


                if(response.isSuccessful()) {

                    if(response.body().getSuccess() == 0) {
    //                    Log.d("onSuccess",""+response.body().getSuccess());
    //                    Log.d("success",""+success);
                        Toast.makeText(getActivity(), "You have already scheduled in this date! Select another date", Toast.LENGTH_SHORT).show();
                        linearTime.setVisibility(View.INVISIBLE);

                    }else {

                        Log.d("onSuccess",""+response.body().getSuccess());
                        Log.d("success",""+success);
                        linearTime.setVisibility(View.VISIBLE);

                        for(Reservation reservation:reservations) {

                            time.remove(reservation.getTime());
                            Log.d("response",""+reservation.getTime());
                        }
                    }



                }else {
                    Toast.makeText(getActivity(), "" + response.errorBody(), Toast.LENGTH_SHORT).show();
                }
                avi.smoothToHide();

            }

            @Override
            public void onFailure(Call<ReservationList> call, Throwable t) {
                ci.cantConnectToServerMessage();

            }
        });



    }

    public void insertReservation(String date, String time,int mPackage,String with_coaches,double total) {

        avi.smoothToShow();
        btnReserve.setEnabled(false);

        ApiInterface inf = ApiClient.getApiService();
        Call<ReservationList> call = inf.setReservation(date,time,mPackage,with_coaches,total,id);
        call.enqueue(new Callback<ReservationList>() {
            @Override
            public void onResponse(Call<ReservationList> call, final Response<ReservationList> response) {
                final ReservationList reservationResponse = response.body();

                if (response.isSuccessful()) {
                    if(reservationResponse.getSuccess() == 1) {
                        Log.e("onResponse","" + reservationResponse.getLast_id());
                        intentService(reservationResponse.getLast_id());


                    }
                    btnReserve.setEnabled(true);
                    avi.smoothToHide();
                }

                Log.d("onResponse",""+response.body().getMessage());

            }

            @Override
            public void onFailure(Call<ReservationList> call, Throwable t) {
                avi.smoothToHide();
                btnReserve.setEnabled(true);
                ci.cantConnectToServerMessage();

            }
        });



    }

    public void loadJson() {

//        avi.smoothToShow();
//
//        ApiInterface inf = ApiClient.getApiService();
//        Call<com.arrowland.arrowland.REST.Membership> call = inf.getMembershipStatus(id);
//        call.enqueue(new Callback<com.arrowland.arrowland.REST.Membership>() {
//            @Override
//            public void onResponse(Call<com.arrowland.arrowland.REST.Membership> call, Response<com.arrowland.arrowland.REST.Membership> response) {
//
//                com.arrowland.arrowland.REST.Membership membershipResponse = response.body();
//
//                if(response.isSuccessful()) {
//
//                    setDiscount(membershipResponse.getStatus());
//
//
//
//
//
//
//                }else {
//                    Toast.makeText(getActivity(), "" +response.errorBody(), Toast.LENGTH_SHORT).show();
//                }
//                avi.smoothToHide();
//
//
//            }
//
//            @Override
//            public void onFailure(Call<com.arrowland.arrowland.REST.Membership> call, Throwable t) {
//                ci.cantConnectToServerMessage();
//                avi.smoothToHide();
//
//
//            }
//        });

    }

//    private void setDiscount(String status) {
//        if(status.equals("Accepted")) {
//            tvDiscount.setText("Discount : 10%" );
//            tvDiscount.setVisibility(View.VISIBLE);
//        }else {
//            tvDiscount.setText("");
//            tvDiscount.setVisibility(View.GONE);
//        }
//
//    }




    private void intentService(int id) {
        Intent intent = new Intent(getActivity(), ReservationSuccess.class);
        intent.putExtra("ID",id);
        getActivity().startActivity(intent);

    }



    // DIALOG BOX




}
