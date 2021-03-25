package com.example.foodapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    LinearLayout parent;
    ImageView profile, splashImage;
    TextView profileName, splashDescription, splashTags, itemID, time;
    RatingBar difficulty;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        parent = itemView.findViewById(R.id.home_parent);
        itemID = itemView.findViewById(R.id.itemID);
        profile = itemView.findViewById(R.id.profileImage);
        splashImage = itemView.findViewById(R.id.splashImage);
        profileName = itemView.findViewById(R.id.profileName);
        splashDescription = itemView.findViewById(R.id.splashDescription);
        splashTags = itemView.findViewById(R.id.splashTags);
        difficulty = itemView.findViewById(R.id.splashDifficulty);
        time = itemView.findViewById(R.id.time);
    }
}
