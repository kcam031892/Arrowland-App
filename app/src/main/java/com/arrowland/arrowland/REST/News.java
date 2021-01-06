package com.arrowland.arrowland.REST;

/**
 * Created by Mhack Bautista on 8/11/2018.
 */

public class News {

    public String title;
    public String message;
    public String image_url;
    public String id;

    public News() {

    }

    public News(String title, String body, String image_url) {
        this.title = title;
        this.message = body;
        this.image_url = image_url;
    }

    public String getMessage() {
        return message;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }
}
