package com.example.foodapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HomePageAdapter extends RecyclerView.Adapter<HomePageAdapter.HomeViewHolder> {

    private Context context;
    private ArrayList<Recipe> recipes;

    public HomePageAdapter(Context context, ArrayList<Recipe> recipes) {
        this.context = context;
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_page_item, parent, false);
        HomeViewHolder holder = new HomeViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        holder.splashDescription.setText(recipes.get(position).getDescription());
        holder.splashTags.setText("Tags: " + recipes.get(position).getCategories().toString());
        holder.difficulty.setRating((float) recipes.get(position).getDifficulty());
        holder.profileName.setText(recipes.get(position).title);
        holder.splashImage.setImageBitmap(recipes.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public static class HomeViewHolder extends RecyclerView.ViewHolder {

        LinearLayout parent;
        ImageView profile, splashImage;
        TextView profileName, splashDescription, splashTags;
        RatingBar difficulty;

        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.home_parent);
            profile = itemView.findViewById(R.id.profileImage);
            splashImage = itemView.findViewById(R.id.splashImage);
            profileName = itemView.findViewById(R.id.profileName);
            splashDescription = itemView.findViewById(R.id.splashDescription);
            splashTags = itemView.findViewById(R.id.splashTags);
            difficulty = itemView.findViewById(R.id.splashDifficulty);

        }
    }
}
