package com.arrowland.arrowland.Classes;

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
import android.text.Html;
import android.text.TextUtils;
import android.util.Patterns;

import com.arrowland.arrowland.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by Mhack Bautista on 8/11/2018.
 */

public class FirebaseNotificationUI {

    private Context context;

    public FirebaseNotificationUI(Context context) {
        this.context = context;
    }

    public void showNotificationMessage(String title, String message, String timestamp, Intent intent) {
        showNotificationMessage(title,message,timestamp,intent,null);
    }

    public void showNotificationMessage(String title, String message, String timestamp, Intent intent,String imgUrl) {

        if(TextUtils.isEmpty(message)) {
            return;
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,intent,PendingIntent.FLAG_ONE_SHOT);
        int id = (int)(Math.random() * 50 + 1);

        if(!TextUtils.isEmpty(imgUrl)) {

            Bitmap bitmap = getBitmapFromUrl(imgUrl);

            showBigNotification(title,message,timestamp,pendingIntent,bitmap,id);

        }else {
            // SHOW SMALL NOTIFICATION
            showSmallNotification(title,message,timestamp,pendingIntent,id);
        }

    }

    private void showSmallNotification(String title, String message, String timestamp, PendingIntent pendingIntent,int id){
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        inboxStyle.addLine(message);
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle(title);
        bigTextStyle.bigText(message);
        String channelId = "default";

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context,channelId)
                        .setTicker(title)
                        .setWhen(0)
                        .setSmallIcon(R.drawable.arrowland_logo)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setStyle(bigTextStyle)
                        .setColor(context.getResources().getColor(R.color.colorAccent))
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);


        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(id /* ID of notification */, notificationBuilder.build());


    }

    private void showBigNotification(String title,String message, String timestamp, PendingIntent pendingIntent, Bitmap bitmap,int id) {
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(title);
        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
        bigPictureStyle.bigPicture(bitmap);

        String channelId = "default";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context,channelId)
                        .setSmallIcon(R.drawable.arrowland_logo)
                        .setTicker(title)
                        .setWhen(getTimeMilliSec(timestamp))
                        .setContentTitle(title)
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setStyle(bigPictureStyle)
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.logo))
                        .setContentIntent(pendingIntent);


        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(id/* ID of notification */, notificationBuilder.build());
    }

    private static long getTimeMilliSec(String timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(timestamp);
            return date.getTime();

        }catch(ParseException e) {
            e.printStackTrace();
        }
        return 0;

    }

    private Bitmap getBitmapFromUrl(String strURL) {

        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }


}
