package com.example.foodapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class HomePageItemView extends AppCompatActivity {
    final StorageReference storageReferenceRecipe = FirebaseStorage.getInstance().getReference().child("RecipeImages");
    final StorageReference storageReferenceSteps = FirebaseStorage.getInstance().getReference().child("StepImages");

    RecyclerView recipeRecycler, stepRecycler, ingredientRecycler;
    HomePageAdapter recipeAdapter;
    StepAdapter stepAdapter;
    IngredientAdapter ingredientAdapter;
    ArrayList<Recipe> recipes = new ArrayList<>();
    ArrayList<Step> steps = new ArrayList<>();
    ArrayList<Ingredient> ingredients = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page_item_view);

        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        recipeRecycler = findViewById(R.id.recipeRecycler);
        stepRecycler = findViewById(R.id.stepRecycler);
        ingredientRecycler = findViewById(R.id.ingredientRecycler);

        recipeRecycler.setNestedScrollingEnabled(false);
        stepRecycler.setNestedScrollingEnabled(false);
        ingredientRecycler.setNestedScrollingEnabled(false);

        String id = getIntent().getExtras().getString("currentID");
        initialize(id);

    }

    private void refreshView(){
        recipeRecycler.setVisibility(View.GONE);
        recipeRecycler.setVisibility(View.VISIBLE);
    }

    private void initialize(final String id) {
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Recipes/" + id);
        //final StorageReference stepImages = storageReferenceSteps.child(id);
        //Semaphore semaphore = new Semaphore(0);



        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Recipe rec = dataSnapshot.getValue(Recipe.class);
                final long ONE_MEGABYTE = 1024 * 1024;
                final StorageReference mainImage = storageReferenceRecipe.child(id + "_image");
                mainImage.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        // Data for "---.jpg" is returns, use this as needed
                        final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        // imagetoUpload is the imageView we want to modify
                        rec.setImage(bitmap);
                        recipes.add(rec);
                        steps = rec.getSteps();

                        for (int i = 0; i < steps.size(); i++){
                            final StorageReference stepImage = storageReferenceSteps.child(id + "/" + i + ".jpg");
                            final int finalI = i;
                            stepImage.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {
                                    Bitmap bitmap1 = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    steps.get(finalI).setImageBitmap(bitmap1);
                                    initStepRecycler();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                    Toast.makeText(HomePageItemView.this, "Failed",
                                            Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                        ingredients = rec.getIngredients();
                        initRecylcers();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }


        });
    }

    private void initRecylcers() {
        recipeAdapter = new HomePageAdapter(this, recipes);
        recipeRecycler.setAdapter(recipeAdapter);
        recipeRecycler.setLayoutManager(new LinearLayoutManager(this));

        ingredientAdapter = new IngredientAdapter(this, ingredients);
        ingredientRecycler.setAdapter(ingredientAdapter);
        ingredientRecycler.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initStepRecycler() {
        stepAdapter = new StepAdapter(this, steps);
        stepRecycler.setAdapter(stepAdapter);
        stepRecycler.setLayoutManager(new LinearLayoutManager(this));
    }
}