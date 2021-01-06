package com.arrowland.arrowland.REST;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Mhack Bautista on 8/20/2018.
 */

public class Lesson {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("customer_id")
    @Expose
    private int customer_id;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("enrolled_date")
    private String enrolled_date;
    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("session_count")
    @Expose
    private int session_count;
    @SerializedName("pending_count")
    @Expose
    private int pending_count;
    @SerializedName("accepted_count")
    @Expose
    private int accepted_count;
    @SerializedName("accepted_message")
    @Expose
    private String accepted_message;

    public String getAccepted_message() {
        return accepted_message;
    }

    public int getAccepted_count() {
        return accepted_count;
    }

    public int getPending_count() {
        return pending_count;
    }



    public int getSession_count() {
        return session_count;
    }

    public int getId() {
        return id;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public String getStatus() {
        return status;
    }

    public String getEnrolled_date() {
        return enrolled_date;
    }

    public int getCount() {
        return count;
    }
}
