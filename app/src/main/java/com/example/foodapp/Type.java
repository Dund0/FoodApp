package com.example.foodapp;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.List;

public class Type extends Categories{
    final List<String> Type = (Arrays.asList("Main dish", "Side dish", "Soup", "Snacks", "Salads", "Sauce/Jam", "Drinks", "Beverages", "N/A"));

    public Type(String typeOf, String value) {
        super(typeOf,value);

    }
}