package com.example.foodapp;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
        List<Categories> categories = new ArrayList<Categories>();
        String title;
        private String description;
        private String time;
        private double difficulty;
        private ArrayList<Step> steps;
        private ArrayList<Ingredient> ingredients;
        private String userId;
        private String username;
        private String recipeImage;
        private Bitmap image;
        private Uri imageUri;

        Recipe(){}
        Recipe(String type, String main_ingredient, String method, String situation, String culture, String title, String description,
               String time, double difficulty, ArrayList<Ingredient> ingredients, ArrayList<Step> steps, String userId, String username)
        {
            categories.add(new Type("type", type));
            categories.add(new Main_Ingredient("main ingredient", main_ingredient));
            categories.add(new Method("method", method));
            categories.add(new Situation("situation", situation));
            categories.add(new Culture("culture", culture));
            this.title = title;
            this.description = description;
            this.time = time;
            this.difficulty = difficulty;
            this.steps = steps;
            this.ingredients = ingredients;
            this.userId = userId;
            this.username = username;
            this.recipeImage = title + "_image";
        }
        public List<Categories> getCategories()
        {
                return this.categories;
        }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getTime() {
        return time;
    }

    public double getDifficulty() {
        return difficulty;
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    @NonNull
    @Override
    public String toString() {
        return "Recipe{" +
                "categories=" + categories +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", time='" + time + '\'' +
                ", difficulty=" + difficulty +
                ", steps=" + steps +
                ", ingredients=" + ingredients +
                '}';
    }

    public String getUserId() {
        return userId;
    }

    public String getRecipeImage() {
        return recipeImage;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setImageUri(Uri imageUri) { this.imageUri = imageUri; }

    public Bitmap getImage() {
            return image;
    }

    public Uri getImageUri() { return imageUri; }

    public String getUsername() { return username; }
}
