package com.example.foodapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
                        ingredients = rec.getIngredients();

                        for (int i = 0; i < steps.size(); i++){
                            final StorageReference stepImage = storageReferenceSteps.child(id + "/" + i);
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

                                }
                            });
                        }
                        initRecylcers();
                        initStepRecycler();
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

        recipeAdapter.setOnItemClickListener(new HomePageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onProfileClick(int position) {
                String name = recipes.get(position).getUsername();
                String userid = recipes.get(position).getUserId();
                Intent profilePage = new Intent(getApplicationContext(), ProfilePage.class);
                profilePage.putExtra("currentName", name);
                profilePage.putExtra("currentID", userid);
                startActivity(profilePage);

            }

            @Override
            public void onDeleteClick(int position) {

            }
        });
    }

    private void initStepRecycler() {
        stepAdapter = new StepAdapter(this, steps);
        stepRecycler.setAdapter(stepAdapter);
        stepRecycler.setLayoutManager(new LinearLayoutManager(this));
    }

    //Change activity on history button press
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.favorite:
                item.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_baseline_favorite_dark));
                break;
            case R.id.like:
                item.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_baseline_thumb_up_dark));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //Show history button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
