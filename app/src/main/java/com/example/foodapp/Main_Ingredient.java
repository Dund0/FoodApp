package com.example.foodapp;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.List;

public class Main_Ingredient extends Categories {

    final List<String> Main_Ingredient = (Arrays.asList("beef", "Pork", "Chicken", "Meat", "Vegetables", "Seafood",
            "Dairy", "Rice", "Fruits", "Beans", "Grain", "N/A"));

    public Main_Ingredient(String typeOf, String value) {
        super(typeOf, value);
    }

}




