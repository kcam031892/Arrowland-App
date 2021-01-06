package com.arrowland.arrowland;

import android.*;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arrowland.arrowland.REST.ApiClient;
import com.arrowland.arrowland.REST.ApiInterface;
import com.arrowland.arrowland.REST.Payment;
import com.arrowland.arrowland.REST.Reservation;
import com.arrowland.arrowland.REST.ReservationList;
import com.google.android.gms.common.internal.Constants;
import com.wang.avi.AVLoadingIndicatorView;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;


public class PaymentSmartPadala extends AppCompatActivity {

    private Toolbar toolbar;
    private static final int PICK_IMAGE_REQUEST = 100;
    private static final int STORAGE_PERMISSION_CODE = 123;
    private Uri filePath;
    private Bitmap bitmap;
    private ImageView imageView,closeImage;
    private Button btnSelectImage,btnOkay,btnPay;
    private EditText etContact,etReference,etAmount;
    private int id;
    private AVLoadingIndicatorView avi;
    private Dialog dialog;
    private TextView tvTotal,tvDiscount,tvAmount,tvMembershipId;
    private String member_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_smart_padala);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.fontWhite), PorterDuff.Mode.SRC_ATOP);
        this.setTitle("Smart Padala");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        id = intent.getExtras().getInt("ID");
        member_status = intent.getExtras().getString("member_status");
        Log.e("ID","" + id);

        initViews();
        loadJson(id);

        if(member_status.equals("Member")) {
            tvMembershipId.setVisibility(View.VISIBLE);
            tvMembershipId.setText("With Membership");

        }


        requestPermissionStorage();

    }

    private void initViews() {
        etContact = findViewById(R.id.etContactNumber);
        etReference = findViewById(R.id.etReferenceNumber);
        etAmount = findViewById(R.id.etAmount);

        tvAmount = findViewById(R.id.tvAmount);
        tvMembershipId = findViewById(R.id.tvMembershipId);
        tvDiscount = findViewById(R.id.tvDiscount);
        tvTotal = findViewById(R.id.tvTotal);

        imageView = findViewById(R.id.img_payment);

        btnSelectImage = findViewById(R.id.btnSelectImage);

        avi = findViewById(R.id.avi);

        dialog = new Dialog(this);

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });

        btnPay = findViewById(R.id.btnPay);
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendPayment();
            }
        });





    }

    public void loadJson(int id) {
        avi.smoothToShow();
        ApiInterface inf = ApiClient.getApiService();
        Call<ReservationList> call = inf.reservationDetail(id);
        call.enqueue(new Callback<ReservationList>() {
            @Override
            public void onResponse(Call<ReservationList> call, Response<ReservationList> response) {

                Log.e("onResponse","" + response.body().getReservationList());

                if(response.isSuccessful()) {
                    for(Reservation data:response.body().getReservationList()) {


                        tvAmount.setText("Amount to pay : PHP "+round(data.getTotal(),0));
                        setTotal(data.getTotal());
                        setDiscount(data.getTotal());
                    }
                    avi.smoothToHide();

                }
            }

            @Override
            public void onFailure(Call<ReservationList> call, Throwable t) {
                Log.e("onFailure","" + t);
                avi.smoothToHide();

            }
        });
    }

    private void setTotal(double total) {
        double totalAmount = 0;
        if(member_status.equals("Member")) {
            totalAmount = total -  (total * .10);

        }else {
            totalAmount = total;
        }

        tvTotal.setText("Total : " + round(totalAmount,0));
    }

    private void setDiscount(double total) {
        double totalDiscount = 0;

        if(member_status.equals("Member")) {
            totalDiscount = total * .10;

        }else {
            totalDiscount = 0;
        }

        tvDiscount.setText("Discount : " + round(totalDiscount,0));


    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select a Image"),PICK_IMAGE_REQUEST);
    }

    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri,null,null,null,null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null,MediaStore.Images.Media._ID + " =? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data !=null && data.getData() != null) {
            filePath = data.getData();


            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);


            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    public void sendPayment() {


        String mContactNumber = etContact.getText().toString().trim();
        String mReference_number = etReference.getText().toString().trim();
        String mAmount = etAmount.getText().toString().trim();
        String imageData = imageToString();


        if(imageData == null) {
            Toast.makeText(this, "Image must not empty", Toast.LENGTH_SHORT).show();

        }else if(!validate()) {
            return;
        }else {
            avi.smoothToShow();

            ApiInterface inf = ApiClient.getApiService();
            Call<Payment> call = inf.setPayment(id,imageData,mContactNumber,mReference_number,mAmount,"Cebuanna Lhuiller",member_status);
            call.enqueue(new Callback<Payment>() {
                @Override
                public void onResponse(Call<Payment> call, Response<Payment> response) {
                    if(response.isSuccessful()) {
                        avi.smoothToHide();
                        showDialog();

                    }


                }

                @Override
                public void onFailure(Call<Payment> call, Throwable t) {

                    avi.smoothToHide();

                }
            });

        }





    }

    private String imageToString()
    {
        if(bitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,20,byteArrayOutputStream);
            byte[] imgByte = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(imgByte,Base64.DEFAULT);

        }
        return null;

    }

    private void uploadMultiPart() {


        String mContactNumber = etContact.getText().toString().trim();
        String mReference_number = etReference.getText().toString().trim();
        String mAmount = etAmount.getText().toString().trim();



        if(filePath == null) {
            Toast.makeText(this, "Image must not empty", Toast.LENGTH_SHORT).show();
        }else if(validate() == false){
            return;

        }else {
            String path = getPath(filePath);

            try {
                String uploadId = UUID.randomUUID().toString();
                String url = ApiClient.BASE_URL + "payment/set/" + id;

                new MultipartUploadRequest(this,uploadId, url)
                        .addFileToUpload(path,"file")
                        .addParameter("sender_name",mContactNumber)
                        .addParameter("reference_number",mReference_number)
                        .addParameter("amount",mAmount)
                        .addParameter("payment_method","Smart Padala")
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(5)
                        .startUpload();




                showDialog();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }




    public void showDialog() {

        dialog.setContentView(R.layout.dialog_payment);
        closeImage = dialog.findViewById(R.id.closeImage);
        btnOkay = dialog.findViewById(R.id.btnOkay);

        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaymentSmartPadala.this,Home.class);
                intent.putExtra("frag","fragmentReservation");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

        btnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaymentSmartPadala.this,Home.class);
                intent.putExtra("frag","fragmentReservation");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();


    }

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

    public boolean validate() {
        boolean valid = true;

        ArrayList<String> displayError = new ArrayList<>();

        int contact_empty = checkEditTextEmpty(etContact);

        if(contact_empty > 0) {
            displayError.add("Contact Number must not empty");
            etContact.requestFocus();
            valid = false;
        }

        int contact_len = checkEditTextLength(etContact,11);
        if(contact_len > 0) {
            displayError.add("Contact Number must be 11 digit");
            etContact.requestFocus();
            valid = false;
        }

        int reference_empty = checkEditTextEmpty(etReference);

        if(reference_empty > 0) {
            displayError.add("Reference Number must not empty");
            etReference.requestFocus();
            valid = false;
        }

        int reference_len = checkEditTextLength(etReference,10);
        if(reference_len > 0) {
            displayError.add("Rference Number must be 10 characters ");
            etReference.requestFocus();
            valid = false;
        }

        int amount_empty = checkEditTextEmpty(etAmount);

        if(amount_empty > 0) {
            displayError.add("Amount must not empty");
            etAmount.requestFocus();
            valid = false;
        }else {
            String sAmount = etAmount.getText().toString().trim();
            int amountValue = Integer.parseInt(sAmount);

            if(amountValue < 200) {
                displayError.add("Amount should not be less than 200");
                valid = false;

            }

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

        if(!valid) {
            alertDialog.show();
        }



        return valid;
    }

    private void requestPermissionStorage() {
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
            return;
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)){

        }
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
    }
}
