
package com.arrowland.arrowland.REST;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Mhack Bautista on 8/6/2018.
 */

public class Reservation {

    @SerializedName("reservation_time")
    @Expose
    private String time;
    @SerializedName("reservation_date")
    @Expose
    private String date;
    @SerializedName("year")
    private String year;
    @SerializedName("customer_id")
    @Expose
    private int customer_id;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("reservation_code")
    @Expose
    private String reservation_code;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("package")
    @Expose
    private String mPckage;
    @SerializedName("total")
    @Expose
    private double total;
    @SerializedName("with_coaches")
    @Expose
    private String with_coaches;







    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public String getYear() {
        return year;
    }


    public int getId() {
        return id;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public String getReservation_code() {
        return reservation_code;
    }

    public String getStatus() {
        return status;
    }

    public String getmPckage() {
        return mPckage;
    }

    public double getTotal() {
        return total;
    }

    public String getWith_coaches() {
        return with_coaches;
    }
}
