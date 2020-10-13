package com.example.foodapp;
import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.List;

public class Method extends Categories{
    final List<String> Method = (Arrays.asList("Stir-fry", "Boiled", "Steamed", "Fried", "Baked"));

    public Method(String typeOf, String value) {
        super(typeOf,value);
    }
}