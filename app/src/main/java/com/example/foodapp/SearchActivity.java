package com.example.foodapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    RecyclerView recipeRecycler;
    ArrayList<Recipe> recipes = new ArrayList<>();

    final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

    HomePageAdapter recipeAdapter;

    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomAppBar);
//        NavController navController = Navigation.findNavController(this,  R.id.fragment);
//        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        recipeRecycler = findViewById(R.id.recipeRecycler);

        category = getIntent().getStringExtra("category");

        initList();
    }

    private void initList() {
        Query query = ref.child("Recipes");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot rec: snapshot.getChildren()) {
                    Recipe rec1 = rec.getValue(Recipe.class);
                    for (Categories c : rec1.getCategories()) {
                        if(c.value.toLowerCase().equals(category.toLowerCase())) {
                            recipes.add(rec1);
                            break;
                        }
                    }
                    assert rec1 != null;
                }
                initRecipieRecycler(recipes);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private void initRecipieRecycler(ArrayList<Recipe> recipes) {
        recipeAdapter = new HomePageAdapter(this, recipes);
        recipeRecycler.setAdapter(recipeAdapter);
        recipeRecycler.setLayoutManager(new LinearLayoutManager(this));
    }
}
