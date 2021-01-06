package com.arrowland.arrowland.REST;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Mhack Bautista on 7/23/2018.
 */

public class GalleryList {

    @SerializedName("galleryList")
    @Expose
    private ArrayList<Gallery> galleryList;

    public ArrayList<Gallery> getGalleryList() {
        return galleryList;
    }

    public void setGalleryList(ArrayList<Gallery> galleryList) {
        this.galleryList = galleryList;
    }

}
