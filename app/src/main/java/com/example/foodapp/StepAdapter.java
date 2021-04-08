package com.example.foodapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {

    private Context context;
    private ArrayList<Step> steps;
    private OnItemClickListener mlistener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mlistener = listener;
    }

    public StepAdapter(Context context, ArrayList<Step> steps) {
        this.context = context;
        this.steps = steps;
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.step_item, parent, false);
        StepViewHolder holder = new StepViewHolder(view, mlistener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
        holder.description.setText(steps.get(position).getDescription());
        holder.image.setImageBitmap(steps.get(position).getImageBitmap());
//        Glide.with(context).load(steps.get(position).getImageUri())
//                .placeholder(R.drawable.ic_person)
//                .dontAnimate()
//                .into(holder.image);

    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    public static class StepViewHolder extends RecyclerView.ViewHolder {

        LinearLayout parent;
        EditText description;
        ImageView image;

        public StepViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            parent = itemView.findViewById(R.id.step_parent);
            description = itemView.findViewById(R.id.stepDescription);
            image = itemView.findViewById(R.id.stepUpload);

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

            image.setOnClickListener(new View.OnClickListener() {
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

        public String getDescription() {return description.getText().toString();}

        public Bitmap getImage() {return ((BitmapDrawable) image.getDrawable()).getBitmap();}
    }
}
