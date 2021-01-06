package com.arrowland.arrowland.REST;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Mhack Bautista on 8/17/2018.
 */

public class Membership {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("customer_id")
    @Expose
    private int customer_id;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("date_started")
    @Expose
    private String date_started;
    @SerializedName("date_ended")
    @Expose
    private String date_ended;
    @SerializedName("remaining_day")
    @Expose
    private String remaining_day;
    @SerializedName("membership_id")
    @Expose
    private String membership_id;



    public int getId() {
        return id;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public int getCount() {
        return count;
    }

    public String getDate_ended() {
        return date_ended;
    }

    public String getDate_started() {
        return date_started;
    }

    public String getRemaining_day() {
        return remaining_day;
    }

    public String getMembership_id() {
        return membership_id;
    }
}
