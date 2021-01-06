package com.arrowland.arrowland;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.arrowland.arrowland.REST.ApiClient;
import com.arrowland.arrowland.REST.ApiInterface;
import com.arrowland.arrowland.REST.User;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.widget.Toast.LENGTH_LONG;

public class Register extends AppCompatActivity {

    private TextView txtRegister,txtPasswordMatch;
    private Calendar myCalendar;
    private EditText etBirthday,etUsername,etPassword,etCPassword,etEmail,etFName,etMName,etLName,etAddress,etContact;
    private Button btnReg;
    private Spinner spGender;
    private Animation fadein;
    private AVLoadingIndicatorView avi;
    private long day,month,year;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // DECLARE ALL INPUT VARIABLES

        etUsername = findViewById(R.id.etRegUsername);
        etPassword = findViewById(R.id.etRegPassword);
        etCPassword = findViewById(R.id.etRegConfirmPassword);
        etEmail = findViewById(R.id.etRegEmail);
        etFName = findViewById(R.id.etRegFName);
        etMName = findViewById(R.id.etRegMName);
        etLName = findViewById(R.id.etRegLName);
        etAddress = findViewById(R.id.etRegAddress);
        etContact = findViewById(R.id.etRegContact);
        etBirthday = findViewById(R.id.etRegBirthday);
        avi = findViewById(R.id.avi);

        btnReg = findViewById(R.id.btnRegisterSubmit);

        txtPasswordMatch = findViewById(R.id.txtPasswordMatch);

        // TYPEFACE

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/monaco.ttf");

        txtRegister = findViewById(R.id.txtRegister);
        txtRegister.setTypeface(typeface);

        //  DECLARE ANIMATION

        fadein = AnimationUtils.loadAnimation(this,R.anim.fadein);


        // SPINNER

        spGender = findViewById(R.id.spRegGender);
        String[] gender = new String[]{
                "Male",
                "Female"
        };

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_style, gender
        );
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spGender.setAdapter(spinnerArrayAdapter);


        // DATE PICKER

        day = 86400000;
        month = day*30;
        year = month * 12;


        myCalendar = Calendar.getInstance();

            final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR,year);
                myCalendar.set(Calendar.MONTH,monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                updateLabel();

            }

        };

        etBirthday.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//
//                new DatePickerDialog(Register.this, date, myCalendar
//                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                DatePickerDialog dialog = new DatePickerDialog(Register.this,date,myCalendar.get(Calendar.YEAR),
                                                                myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMaxDate(new Date().getTime() - year * 10);
                dialog.show();


            }
        });

        // PASSWORD KEYPRESS

        etCPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {



            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                Boolean checkPassword = checkConfirmPassword();


                if(!checkPassword) {
                    txtPasswordMatch.setVisibility(View.VISIBLE);
                }else {
                    txtPasswordMatch.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String confirmPassword = etCPassword.getText().toString();
                Boolean checkPassword = checkConfirmPassword();

                if(!confirmPassword.isEmpty()){
                    if(!checkPassword) {
                        txtPasswordMatch.setVisibility(View.VISIBLE);
                    }else {
                        txtPasswordMatch.setVisibility(View.GONE);
                    }

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    // UPDATE BIRTHDAY DATE PICKER FUNCTION

    public void updateLabel(){
        String format = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format,Locale.US);
        etBirthday.setText(sdf.format(myCalendar.getTime()));
    }

    // FUNCTION TO CHECK MATCH PASSWORD

    public boolean checkConfirmPassword(){

        String password = etPassword.getText().toString();
        String confirmPassword = etCPassword.getText().toString();

        if(password.equals(confirmPassword)) {
            return true;

        }else {
            return false;
        }


    }

    // CHECK IF EDITTEXT IS EMPTY

    public int checkEditTextEmpty(EditText et){

        String myEt = et.getText().toString().trim();
        int error = 0;

        if(myEt.isEmpty()) {
            error++;

        }

        return error;



    }

    // CHECK IF EDITTEXT IS LESS 5 CHARACTERS

    public int checkEditTextLength(EditText et,int len) {
        String myEt = et.getText().toString().trim();
        int error = 0;
        if(myEt.length() > 0 && myEt.length() < len) {

            error++;

        }

        return error;


    }

    public boolean validate(){
        boolean valid = true;


        ArrayList<String> displayError = new ArrayList<String>();

        int username_empty = checkEditTextEmpty(etUsername);

        if(username_empty > 0) {
            displayError.add("Username must not empty");
            etUsername.requestFocus();
            valid = false;
        }

        int username_len = checkEditTextLength(etUsername,5);

        if(username_len > 0) {
            displayError.add("Username must 5 characters and up");
            etUsername.requestFocus();
            valid = false;
        }


        int password_empty = checkEditTextEmpty(etPassword);

        if(password_empty > 0) {
            displayError.add("Password must not empty");
            etPassword.requestFocus();
            valid = false;
        }

        int password_len = checkEditTextLength(etPassword,5);

        if(password_len > 0) {
            displayError.add("Password must 5 characters and up");
            etPassword.requestFocus();
            valid = false;
        }


        int cpassword_empty = checkEditTextEmpty(etCPassword);

        if(cpassword_empty > 0) {
            displayError.add("Confirm Password must not empty");
            etCPassword.requestFocus();
            valid = false;
        }

        int cpassword_len = checkEditTextLength(etCPassword,5);

        if(cpassword_len > 0) {
            displayError.add("Confirm Password must 5 characters and up");
            etCPassword.requestFocus();
            valid = false;
        }

        int email_empty = checkEditTextEmpty(etEmail);

        if(email_empty > 0) {
            displayError.add("Email must not empty");
            etEmail.requestFocus();
            valid = false;
        }

        int email_len = checkEditTextLength(etEmail,5);

        if(email_len > 0) {
            displayError.add("Email must 5 characters and up");
            etEmail.requestFocus();
            valid = false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()){
            displayError.add("Enter a valid email");
            etEmail.requestFocus();
            valid = false;
        }



        int fname_empty = checkEditTextEmpty(etFName);

        if(fname_empty > 0) {
            displayError.add("First Name must not empty");
            etFName.requestFocus();
            valid = false;
        }

        int lname_empty = checkEditTextEmpty(etLName);

        if(lname_empty > 0) {
            displayError.add("Last Name must not empty");
            etLName.requestFocus();
            valid = false;
        }

        int address_empty = checkEditTextEmpty(etAddress);

        if(address_empty > 0) {
            displayError.add("Address must not empty");
            etAddress.requestFocus();
            valid = false;
        }

        int contact_empty = checkEditTextEmpty(etContact);

        if(contact_empty > 0) {
            displayError.add("Contact Number must not empty");
            etContact.requestFocus();
            valid = false;
        }

        int contact_len = checkEditTextLength(etContact,11);

        if(contact_len > 0) {
            displayError.add("Contact Number must be 11 digits");
            etContact.requestFocus();
            valid = false;
        }

        int birthday_empty = checkEditTextEmpty(etBirthday);
        if(birthday_empty > 0) {
            displayError.add("Birthday must not empty");
            valid = false;
        }

        if(!checkConfirmPassword()) {
            displayError.add("Password not match");
            etPassword.requestFocus();
            valid = false;
        }


        final AlertDialog alertDialog = new AlertDialog.Builder(this,R.style.CustomDialogTheme).create();
        alertDialog.setTitle("Error!");

        String disErr = "";
        for(int i=0;i<displayError.size();i++) {

            disErr = disErr.concat(displayError.get(i) + "\n");

        }

        alertDialog.setMessage(disErr.toString());
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.dismiss();
                    }
                }
        );


        if(!valid){
            alertDialog.show();
        }

        return valid;
    }



    // BUTTON REGISTER SUBMIT FUNCTION

    public void register_submit(View view) {




        // CHECK ERRORS FOR VALIDATION



        int error = 0;

        // Declare variables
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        String email = etEmail.getText().toString();
        String first_name = etFName.getText().toString();
        String middle_name = etMName.getText().toString();
        String last_name = etLName.getText().toString();
        String address = etAddress.getText().toString();
        String contact = etContact.getText().toString().trim();
        String birthday = etBirthday.getText().toString();
        String gender =  spGender.getSelectedItem().toString();

//        System.out.println(""+etContact.getText().toString());

        final AlertDialog alertDialog = new AlertDialog.Builder(this,R.style.CustomDialogTheme).create();


        // REGISTER SUBMIT AND INSERT TO DATABASE
        if(validate() == false) {
            return;
        }else {

            avi.smoothToShow();
            btnReg.setEnabled(false);
            ApiInterface inf = ApiClient.getApiService();

            Call<User> userCall = inf.userSignUp(
                    username,password,email,
                    first_name,middle_name,last_name,

                    address,contact,birthday,gender
                                                );

            userCall.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if(response.isSuccessful()) {
                        if(response.body().getSuccess() == 0) {
                            alertDialog.setTitle("Registration Failed !");
                            alertDialog.setMessage(response.body().getMessage());
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            alertDialog.dismiss();
                                        }
                                    }
                            );
                            alertDialog.show();
                        }else {
                            alertDialog.setCancelable(false);
                            alertDialog.setTitle("Registration Success !");
                            alertDialog.setMessage("You're successfully registered. Please verify your account before you can log in.");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent = new Intent(Register.this,Login.class);
                                            startActivity(intent);

                                        }
                                    }
                            );

                            alertDialog.show();

                        }
                    }else {
                        Toast.makeText(Register.this, "" + response.errorBody(), Toast.LENGTH_SHORT).show();
                    }


                    btnReg.setEnabled(true);
                    avi.smoothToHide();



                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    btnReg.setEnabled(true);
                    avi.smoothToHide();
                    Toast.makeText(Register.this, t.toString(), Toast.LENGTH_SHORT).show();

                }
            });

        }




    }





}
