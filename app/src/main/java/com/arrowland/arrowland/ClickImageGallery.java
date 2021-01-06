package com.arrowland.arrowland;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ClickImageGallery extends AppCompatActivity {

    private ImageView imgthumbnail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_image_gallery);

        imgthumbnail = findViewById(R.id.imageThumbnail);

        // GET INTENT

        Intent intent = getIntent();
        String url = intent.getExtras().getString("url");

        Picasso.with(this)
                .load(url)
                .placeholder(R.drawable.logo)
                .fit()
                .into(imgthumbnail);




    }
}
