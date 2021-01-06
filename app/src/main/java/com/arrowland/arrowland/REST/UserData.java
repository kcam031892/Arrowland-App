package com.arrowland.arrowland.REST;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Mhack Bautista on 7/20/2018.
 */

public class UserData {

    @SerializedName("first_name")
    @Expose
    private String first_name;
    @SerializedName("middle_name")
    @Expose
    private String middle_name;
    @SerializedName("last_name")
    @Expose
    private String last_name;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("id")
    @Expose
    private int id;

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getMiddle_name() {
        return middle_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getFullName() {
        return this.first_name + " " + this.last_name;

    }

}
