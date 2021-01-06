package com.arrowland.arrowland.REST;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Mhack Bautista on 9/21/2018.
 */

public class Notifications {

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("time")
    @Expose
    private String time;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("notificationable_type")
    @Expose
    private String notificationable_type;

    @SerializedName("count")
    @Expose
    private String count;

    public String getCount() {
        return count;
    }

    public String getMessage() {
        return message;
    }

    public int getId() {
        return id;
    }

    public String getNotificationable_type() {
        return notificationable_type;
    }

    public String getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }
}
