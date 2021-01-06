package com.arrowland.arrowland.Classes;

import android.app.Application;
import android.content.Intent;

import com.arrowland.arrowland.BuildConfig;
import com.arrowland.arrowland.MyService;
import com.facebook.stetho.Stetho;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.leakcanary.LeakCanary;

import net.gotev.uploadservice.UploadService;

/**
 * Created by Mhack Bautista on 8/12/2018.
 */

public class App extends Application {

    private static App mInstance;


    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        FirebaseMessaging.getInstance().subscribeToTopic("Arrowland");

        mInstance = this;
        Stetho.initializeWithDefaults(this);
//
//        if(LeakCanary.isInAnalyzerProcess(this)) {
//            return;
//        }
//        LeakCanary.install(this);
//
//        startService(new Intent(this, MyService.class));

        UploadService.NAMESPACE = BuildConfig.APPLICATION_ID;

    }

    public static synchronized App getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }


}
