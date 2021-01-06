package com.arrowland.arrowland.Classes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

import com.arrowland.arrowland.R;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by Mhack Bautista on 7/20/2018.
 */

public class CheckInternet{

    private Context ctx;

    public CheckInternet(Context context) {
        this.ctx = context;
    }


    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.getState() == NetworkInfo.State.CONNECTED){
            return true;
        }else {
            return false;
        }

    }

    public void displayNoInternetMessage() {
        final AlertDialog alertDialog = new AlertDialog.Builder(this.ctx, R.style.CustomDialogTheme).create();
        alertDialog.setTitle("Error !");
        alertDialog.setMessage("You're offline. Please connect to your internet or WIFI");
        alertDialog.setCancelable(false);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                ((Activity)ctx).finish();


            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Go To Settings",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                        ctx.startActivity(intent);

                    }
                });



        alertDialog.show();
    }


    public void cantConnectToServerMessage() {

        final AlertDialog alertDialog = new AlertDialog.Builder(this.ctx, R.style.CustomDialogTheme).create();
        alertDialog.setTitle("Error !");
        alertDialog.setMessage("Server is down or you're not connected in Internet. Please Try Again");
        alertDialog.setCancelable(false);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                ((Activity)ctx).finish();


            }
        });



        alertDialog.show();

    }



}
