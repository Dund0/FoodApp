package com.example.foodapp;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username, email, password, description;
    private ArrayList<String> recipes;
    private Bitmap profile;
    public User(){

    }
    public User(String username, String email, String password, String description, ArrayList<String> recipes, Bitmap profile) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.description = description;
        this.recipes = recipes;
        this.profile = profile;
    }
    public void addRecipe(String recipe)
    {
        recipes.add(recipe);
    }
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getDescription() {return description;}

    public void setDescription(String description) {this.description = description;}

    public void setUsername(String username) {this.username = username;}

    public void setEmail(String email){this.email = email;}

    public ArrayList<String> getRecipes() {
        return recipes;
    }
}
