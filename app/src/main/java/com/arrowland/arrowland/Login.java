package com.arrowland.arrowland;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arrowland.arrowland.Classes.CheckInternet;
import com.arrowland.arrowland.Classes.FieldValidation;
import com.arrowland.arrowland.REST.ApiClient;
import com.arrowland.arrowland.REST.ApiInterface;
import com.arrowland.arrowland.REST.User;
import com.arrowland.arrowland.REST.UserList;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    Button btnRegister,btnLogin;
    EditText etUsername,etPasssword;
    CheckInternet ci;
    FieldValidation fv;
    AVLoadingIndicatorView avi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);
        etUsername = findViewById(R.id.etUsername);
        etPasssword = findViewById(R.id.etPassword);

        fv = new FieldValidation();
        avi = findViewById(R.id.avi);





        ci = new CheckInternet(Login.this);

        if(!ci.isConnected()) {
            ci.displayNoInternetMessage();
        }


//         Intent intent = getIntent();
//         String title = intent.getExtras().getString("url");
//        Toast.makeText(this, title, Toast.LENGTH_SHORT).show();





    }



    // GO TO REGISTER

    public void btnRegister(View view){
        Intent intent = new Intent(this,Register.class);
        startActivity(intent);

    }

    public void btnLogin(View view) {

        if(ci.isConnected()) {

            final String username = etUsername.getText().toString();
            String password = etPasssword.getText().toString();
            boolean valid = true;

            // VALIDATION

            if(fv.isEmpty(etUsername)) {
                etUsername.setError("Username must not empty");
                etUsername.setFocusable(true);
                valid = false;
            }
            if(fv.setMinLen(etUsername,5)) {
                etUsername.setError("Username must 5 letters and above");
                etUsername.setFocusable(true);
                valid = false;
            }

            if(fv.isEmpty(etPasssword)){
                etPasssword.setError("Password must not empty");
                etPasssword.setFocusable(true);
                valid = false;
            }



            if(valid) {
                // SHARED PREFERNCE INITIALIZE
                SharedPreferences preferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                final SharedPreferences.Editor editor = preferences.edit();


                // PROGRESSBAR



                avi.smoothToShow();

                // BUTTON

                btnLogin.setEnabled(false);




                // SEND TO SERVER
                ApiInterface inf = ApiClient.getApiService();
                final Call<User> userCall = inf.userLogin(username,password);
                userCall.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {

                        if(response.isSuccessful()) {



                            User userResponse = response.body();

                            if(response.body().getSuccess() == 1 ){
                                Toast.makeText(Login.this, "Successfully Login", Toast.LENGTH_SHORT).show();
                                Log.d("ID",""+userResponse.getUserData().getId());
                                editor.putInt("ID",userResponse.getUserData().getId());
                                editor.putString("Name",userResponse.getUserData().getFirst_name().toString());
                                editor.putBoolean("isLoggedIn",true);
                                editor.apply();

                                Intent intent = new Intent(Login.this, Home.class);
                                startActivity(intent);

                                Log.d("onResponse","" + userResponse.getUserData().getUsername());
                            }else if(response.body().getSuccess() == 0) {
                                setMessage("ERROR!","Your account is not yet verified");

                            }else if(response.body().getSuccess() == 2){
                                setMessage("ERROR!","Incorrect Password");
                                etPasssword.getText().clear();
                                etPasssword.setFocusable(true);
                            }else {
                                setMessage("ERROR!","Username not found!");
                                etPasssword.getText().clear();
                                etPasssword.setFocusable(true);

                            }

                        }else {
                            Toast.makeText(Login.this, "" + response.errorBody(), Toast.LENGTH_SHORT).show();
                        }
                        avi.smoothToHide();
                        btnLogin.setEnabled(true);

                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.d("onFailure", "" + t);
                        avi.smoothToHide();
                        btnLogin.setEnabled(true);


                        setMessage("Error!","Cannot connect to the server!");

                    }
                });

            }

        }else {
            ci.displayNoInternetMessage();
        }

    }

    public void setMessage(String title, String message) {
        final AlertDialog alertDialog = new AlertDialog.Builder(this,R.style.CustomDialogTheme).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.dismiss();
            }
        });


        alertDialog.show();
    }


}
