package com.example.foodapp;

public class Ingredient {
    private String ingredient, amount;

    public Ingredient(){}

    public Ingredient (String ingredient, String amount) {
        this.ingredient = ingredient;
        this.amount = amount;
    }

    public String getIngredient() {
        return ingredient;
    }

    public String getAmount(){
        return amount;
    }
}
