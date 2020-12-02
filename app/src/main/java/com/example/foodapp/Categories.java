package com.example.foodapp;

import androidx.annotation.NonNull;

public class Categories {
    final String[] categories = {"type", "main_ingredient","method","situation","culture"};
    public String typeOf;
    public String value;
    Categories(){}
    Categories(String typeOf, String value)
    {
        this.typeOf = typeOf;
        this.value = value;
    }
    Categories(String typeOf)
    {
        this.typeOf = typeOf;
    }
    @Override
    public String toString() {
        return this.value;
    }
}