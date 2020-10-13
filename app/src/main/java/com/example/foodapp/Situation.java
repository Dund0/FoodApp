package com.example.foodapp;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.List;

public class Situation extends Categories {
    final List<String> Situation = (Arrays.asList("Daily Life", "Super-Quick Meal", "For Guests", "Diet",
            "Bento-Box", "Nutrient-Dense", "Midnight Snack", "Holiday", "Baby Food", "Vegan"));

    public Situation(String typeOf, String value) {
        super(typeOf,value);
    }
}