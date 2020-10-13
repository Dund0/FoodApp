package com.example.foodapp;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
        private List<Categories> categories = new ArrayList<Categories>();

        Recipe(String type, String main_ingredient, String method, String situation, String culture)
        {
            categories.add(new Type("type", type));
            categories.add(new Main_Ingredient("main ingredient", main_ingredient));
            categories.add(new Method("method", method));
            categories.add(new Situation("situation", situation));
            categories.add(new Culture("culture", culture));
        }
        public List<Categories> getCategories()
        {
                return this.categories;
        }

}
