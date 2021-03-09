package com.example.foodapp;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
    private OnItemClickListener mlistener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) { mlistener = listener; }

    public HomePageAdapter(Context context, ArrayList<Recipe> recipes) {
        this.context = context;
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_page_item, parent, false);
        HomeViewHolder holder = new HomeViewHolder(view, mlistener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        holder.splashDescription.setText(recipes.get(position).getDescription());
        holder.splashTags.setText("Tags: " + recipes.get(position).getCategories().toString());
        holder.difficulty.setRating((float) recipes.get(position).getDifficulty());
        holder.profileName.setText(recipes.get(position).title);
        holder.itemID.setText(recipes.get(position).title);
        holder.splashImage.setImageBitmap(recipes.get(position).getImage());
        holder.time.setText(recipes.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public static class HomeViewHolder extends RecyclerView.ViewHolder {

        LinearLayout parent;
        ImageView profile, splashImage;
        TextView profileName, splashDescription, splashTags, itemID, time;
        RatingBar difficulty;

        public HomeViewHolder(@NonNull View itemView, final OnItemClickListener listener ) {
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position =  getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }
}
