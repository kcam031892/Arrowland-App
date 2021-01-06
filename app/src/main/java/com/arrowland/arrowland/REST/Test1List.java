package com.arrowland.arrowland.REST;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Mhack Bautista on 9/7/2018.
 */

public class Test1List {
    @SerializedName("server_response")
    @Expose
    private ArrayList<Test1> server_response;


    public ArrayList<Test1> getServer_response() {
        return server_response;
    }
}
