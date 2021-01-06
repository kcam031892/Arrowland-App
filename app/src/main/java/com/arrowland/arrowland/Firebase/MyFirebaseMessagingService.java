package com.arrowland.arrowland.Firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;

import com.arrowland.arrowland.Classes.Config;
import com.arrowland.arrowland.Classes.FirebaseNotificationUI;

import com.arrowland.arrowland.Classes.NotificationUtils;
import com.arrowland.arrowland.Home;
import com.arrowland.arrowland.Login;
import com.arrowland.arrowland.MainActivity;
import com.arrowland.arrowland.NotificationHomeDetail;
import com.arrowland.arrowland.PaymentDetail;
import com.arrowland.arrowland.R;
import com.arrowland.arrowland.ReservationDetail;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by Mhack Bautista on 7/30/2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";

    private FirebaseNotificationUI firebaseNotificationUI;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("NEW_TOKEN",s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String>  data = remoteMessage.getData();

        if(remoteMessage.getNotification() != null ) {

            Log.e(TAG,"" + remoteMessage.getNotification());


        }


        if(remoteMessage.getData().size() > 0) {
            Log.e(TAG, "DATA PAYLOAD : " + remoteMessage.getData().toString());

            try {

                JSONObject jsonObject = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(jsonObject);

            } catch (Exception e) {
                Log.e(TAG, "Exception : " + e.getMessage());

            }


        }

        super.onMessageReceived(remoteMessage);

    }


//    private void handleData(Map<String,String> data) {
//        String title = data.get("title");
//        String message = data.get("message");
//        String timestamp = data.get("timestamp");
//        String imgUrl = data.get("image");
//        String intentMessage = data.get("intent");
//        String id = data.get("id");
//        Intent intent;
//
//        if(intentMessage.equals("All")) {
//            intent = new Intent(this, NotificationHomeDetail.class);
//            intent.putExtra("ID",id);
//
//        }else {
//            intent = new Intent(this, Login.class);
//        }
//
//
//
//
//        if(TextUtils.isEmpty(imgUrl)) {
//            showNotification(title,message,timestamp,intent);
//            Log.e("small","Small Notification");
//        }else {
//            showNotificationWithImage(title,message,timestamp,intent,imgUrl);
//            Log.e("big", "Big Notification");
//        }
//
//
//
//
//    }

    private void handleDataMessage(JSONObject jsonObject) {
        Log.e(TAG, "Push Json : " + jsonObject.toString());
        try {
            JSONObject data = jsonObject.getJSONObject("data");

            String title = data.getString("title");
            String message = data.getString("message");
            String timestamp = data.getString("timestamp");
            String imgUrl = data.getString("image");
            String intentMessage = data.getString("intent");
            String id = data.getString("id");
            String fbIntent = data.getString("fbIntent");
            Intent intent;
            if(intentMessage.equals("All")) {
                intent = new Intent(this, NotificationHomeDetail.class);
                intent.putExtra("ID",id);
                intent.putExtra("fbIntent",fbIntent);


            }else if(intentMessage.equals("Reservation")) {
                intent = new Intent(this,ReservationDetail.class);
                int reservation_id = Integer.parseInt(id);
                intent.putExtra("ID",reservation_id);
                intent.putExtra("Status","Accepted");

            }else if(intentMessage.equals("Payment")){
                intent = new Intent(this, PaymentDetail.class);
                int payment_id = Integer.parseInt(id);
                intent.putExtra("ID",payment_id);
            }else{
                intent = new Intent(this, Home.class);

            }





            if(TextUtils.isEmpty(imgUrl))  {
                showNotification(title,message,timestamp,intent);
                Log.e("small","Small Notification");
            }else {
                showNotificationWithImage(title,message,timestamp,intent,imgUrl);
                Log.e("big", "Big Notification");
            }

        }catch (JSONException e) {
            Log.e(TAG, "JSON Exception :" + e.getMessage());

        }catch(Exception e) {
            Log.e(TAG, "Exception : " + e.getMessage());

        }





    }

    private void showNotification(String title, String message, String timestamp, Intent intent) {

        firebaseNotificationUI = new FirebaseNotificationUI(getApplicationContext());
        firebaseNotificationUI.showNotificationMessage(title,message,timestamp,intent);

    }

    private void showNotificationWithImage(String title,String message, String timestamp, Intent intent, String imgUrl) {
        firebaseNotificationUI = new FirebaseNotificationUI(getApplicationContext());
        firebaseNotificationUI.showNotificationMessage(title,message,timestamp,intent,imgUrl);
    }


    private void sendNotification(String title, String message, String timestamp, Intent intent) {
        firebaseNotificationUI = new FirebaseNotificationUI(getApplicationContext());
        firebaseNotificationUI.showNotificationMessage(title,message,timestamp,intent);
    }



    //







}
