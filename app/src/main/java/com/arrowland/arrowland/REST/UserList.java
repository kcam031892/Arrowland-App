package com.arrowland.arrowland.REST;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Mhack Bautista on 7/23/2018.
 */

public class UserList {

    @SerializedName("userList")
    private ArrayList<User> userList;
    @SerializedName("success")
    private String success;


    public ArrayList<User> getUserList() {
        return userList;
    }

    public String getSuccess() {
        return success;
    }

    public void setUserList(ArrayList<User> userList) {
        this.userList = userList;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}
