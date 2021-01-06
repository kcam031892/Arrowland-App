package com.arrowland.arrowland.REST;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Mhack Bautista on 8/6/2018.
 */

public class ReservationList {

    @SerializedName("reservationList")
    @Expose
    private ArrayList<Reservation> reservationList;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("success")
    @Expose
    private int success;
    @SerializedName("last_id")
    @Expose
    private int last_id;
    @SerializedName("count")
    @Expose
    private int reservation_count;

    public ArrayList<Reservation> getReservationList() {
        return reservationList;
    }

    public String getMessage() {
        return message;
    }

    public int getSuccess() {
        return success;
    }

    public int getLast_id() {
        return last_id;
    }

    public int getReservation_count() {
        return reservation_count;
    }
}
