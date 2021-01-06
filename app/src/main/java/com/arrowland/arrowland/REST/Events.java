package com.arrowland.arrowland.REST;

/**
 * Created by Mhack Bautista on 8/12/2018.
 */

public class Events {

    public String title;
    public String message;
    public String image_url;
    public String id;

    public Events() {
    }

    public Events(String title, String message, String image_url) {
        this.title = title;
        this.message = message;
        this.image_url = image_url;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getId() {
        return id;
    }
}
