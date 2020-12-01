package com.example.foodapp;
import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.List;

public class Method extends Categories{
    final List<String> Method = (Arrays.asList("Stir","Stir-fry", "Boiled", "Steamed", "Fried", "Baked", "N/A"));

    public Method(String typeOf, String value) {
        super(typeOf,value);
    }
}