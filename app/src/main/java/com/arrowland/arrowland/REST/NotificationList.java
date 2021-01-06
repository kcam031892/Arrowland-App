package com.arrowland.arrowland.REST;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Mhack Bautista on 9/21/2018.
 */

public class NotificationList {
    @SerializedName("notificationList")
    @Expose
    private ArrayList<Notifications> notificationList;

    public ArrayList<Notifications> getNotificationList() {
        return notificationList;
    }
}
