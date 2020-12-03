package com.example.foodapp;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.List;

public class Culture extends Categories{
    final List<String> Culture = (Arrays.asList("Chinese", "Mexican", "Italin", "Korean", "American", "Japanese", "N/A"));
    public Culture(String typeOf, String value) {
        super(typeOf,value);
    }
}
