package com.example.foodapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private Context context;
    private ArrayList<Ingredient> ingredients;

    public IngredientAdapter(Context context, ArrayList<Ingredient> ingredients){
        this.context = context;
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_item, parent, false);
        IngredientViewHolder holder = new IngredientViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        holder.ingredient.setText(ingredients.get(position).getIngredient());
        holder.amount.setText(ingredients.get(position).getAmount());
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {

        LinearLayout parent;
        EditText ingredient, amount;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.ingredient_parent);
            ingredient = itemView.findViewById(R.id.ingredient_name);
            amount = itemView.findViewById(R.id.ingredient_amount);
        }

        public String getIngredient() {
            return ingredient.getText().toString();
        }

        public String getAmount() {
            return amount.getText().toString();
        }
    }
}
