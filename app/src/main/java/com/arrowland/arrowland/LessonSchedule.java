package com.arrowland.arrowland;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.arrowland.arrowland.REST.ApiClient;
import com.arrowland.arrowland.REST.ApiInterface;
import com.arrowland.arrowland.REST.Lesson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LessonSchedule extends AppCompatActivity {
    private int id;
    private TextView tvDate,tvYear;
    private Button btnSched,btnSchedDate;
    private Spinner spinner;
    private Calendar calendar;
    private String schedule_time;
    private String schedule_date;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_schedule);

        initViews();
        showSpinner();

        calendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                spinner.setSelection(0);
                updateLabel();
                getReservationDate();
                loadDate();

            }
        };

        btnSchedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog dialog = new DatePickerDialog(LessonSchedule.this,date,
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

        Intent intent = getIntent();
        id = intent.getExtras().getInt("lesson_id");

    }

    private void initViews() {
        tvDate = findViewById(R.id.tvDate);
        tvYear = findViewById(R.id.tvYear);
        btnSched = findViewById(R.id.btnSetSchedule);
        btnSched.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = spinner.getSelectedItemPosition();
                schedule_time = spinner.getSelectedItem().toString();
                if(count == 0) {
                    Toast.makeText(LessonSchedule.this, "Please select a preferred time", Toast.LENGTH_SHORT).show();
                }else {
//                    Toast.makeText(LessonSchedule.this, "Working Button", Toast.LENGTH_SHORT).show();

                    insertSchedule(id,schedule_date,schedule_time);

                }


            }
        });
        btnSchedDate = findViewById(R.id.btnSchedDate);
        spinner = findViewById(R.id.spinner3);
        linearLayout = findViewById(R.id.linearTime);
    }
    public void updateLabel(){
        String format = "EEE dd MMMM";
        int year = calendar.get(Calendar.YEAR);
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        tvDate.setText(sdf.format(calendar.getTime()));
        tvYear.setText("" + year);

    }

    public void loadDate() {
        ApiInterface inf = ApiClient.getApiService();
        Call<Lesson> call = inf.getScheduleDate(1,schedule_date);
        call.enqueue(new Callback<Lesson>() {
            @Override
            public void onResponse(Call<Lesson> call, Response<Lesson> response) {
                if(response.isSuccessful()){
                    if(response.body().getCount() == 0) {
                        linearLayout.setVisibility(View.VISIBLE);

                    }else {
                        Toast.makeText(LessonSchedule.this, "You have already scheduled in this date! Select another date", Toast.LENGTH_SHORT).show();
                        linearLayout.setVisibility(View.INVISIBLE);
                    }

                }
            }

            @Override
            public void onFailure(Call<Lesson> call, Throwable t) {

            }
        });
    }

    public void insertSchedule(int id, String date, String time) {

        ApiInterface inf = ApiClient.getApiService();
        Call<Lesson> call = inf.setSchedule(id,date,time);
        call.enqueue(new Callback<Lesson>() {
            @Override
            public void onResponse(Call<Lesson> call, Response<Lesson> response) {
                if(response.isSuccessful()) {
                    if(response.body().getCount() == 1) {
                        Toast.makeText(LessonSchedule.this, "Successfully set schedule!", Toast.LENGTH_SHORT).show();
                        setBack();
                    }else {
                        Toast.makeText(LessonSchedule.this, "Error Try Again!!", Toast.LENGTH_SHORT).show();
                    }

                }

            }

            @Override
            public void onFailure(Call<Lesson> call, Throwable t) {
                Toast.makeText(LessonSchedule.this, "Failure : " + t, Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void getReservationDate() {
        String format = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format,Locale.getDefault());
        schedule_date = sdf.format(calendar.getTime());
    }

    public void showSpinner() {
        List<String> time = new ArrayList<>();
        time.add(0,"Select A time");
        time.add(1,"7:00AM - 8:00AM");
        time.add(2,"8:00AM - 9:00AM");
        time.add(3,"9:00AM - 10:00AM");
        time.add(4,"10:00AM - 11:00AM");
        time.add(5,"11:00AM - 12:00PM");
        time.add(6,"12:00PM - 1:00PM");
        time.add(7,"1:00PM - 2:00PM");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.spinner_style2,time);
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

    private void setBack() {

        Intent intent = new Intent(this,Home.class);
        intent.putExtra("frag","fragmentLesson");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
}
