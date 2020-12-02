package com.example.foodapp;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username, email, password;
    private ArrayList<String> recipes;

    public User(){

    }
    public User(String username, String email, String password, ArrayList<String> recipes) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.recipes = recipes;
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

    public ArrayList<String> getRecipes() {
        return recipes;
    }
}
