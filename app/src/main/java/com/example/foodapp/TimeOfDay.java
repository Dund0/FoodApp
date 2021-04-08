package com.example.foodapp;

import java.util.Arrays;
import java.util.List;

public class TimeOfDay extends Categories {
    final List<String> TimeOfDay = (Arrays.asList("Breakfast, Lunch, Dinner, Late-Night, Brunch, N/A"));

    public TimeOfDay(String typeOf, String value) {
        super(typeOf, value);
    }
}
