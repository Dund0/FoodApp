package com.example.foodapp;

import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

public class Step {
    private String description;
    private ImageView imageView;
    private Bitmap imageBitmap;
    //private Uri image;

    public Step(){};

    public Step(String description, /*Uri image*/ Bitmap imageBitmap, ImageView imageView) {
        this.description = description;
        this.imageBitmap = imageBitmap;
        this.imageView = imageView;
        //this.image = image;
    }

    public String getDescription() {
        return description;
    }

    /*public Uri getImage() {
        return image;
    }*/

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }
}
