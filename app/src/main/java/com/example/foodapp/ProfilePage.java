package com.example.foodapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ProfilePage extends AppCompatActivity {
    private static final int RESULT_LOAD_IMAGE = 1;
    RecyclerView recipeRecycler;
    ArrayList<Recipe> recipes = new ArrayList<>();
    final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("RecipeImages");
    final StorageReference storageReferenceProfile = FirebaseStorage.getInstance().getReference().child("UserImages");

    HomePageAdapter recipeAdapter;

    private ImageView currentProfilePic;
    private TextView IntroDescription, profileName;
    private Uri selectedImage;

    String userID, username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page);

        currentProfilePic = findViewById(R.id.ProfileImage);
        IntroDescription = findViewById(R.id.ProfileDescription);
        recipeRecycler = findViewById(R.id.userPosts);

        Bundle extras = getIntent().getExtras();

        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        userID = extras.getString("currentID");
        username = extras.getString("currentName");

        profileName = findViewById(R.id.ProfileName);
        profileName.setText(username);

        final StorageReference image = storageReferenceProfile.child(userID + "_image");

        image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext()).load(uri).placeholder(R.drawable.ic_person).dontAnimate().into(currentProfilePic);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        //update Intro
        ref.child("Users").child(userID).child("description").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(!task.isSuccessful()){
                    //ignore
                }
                else{
                    IntroDescription.setText(String.valueOf(task.getResult().getValue()));
                }
            }
        });

        initList();
    }


    private void initList() {
        Query query = ref.child("Recipes");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot rec : snapshot.getChildren()) {
                    final Recipe rec1 = rec.getValue(Recipe.class);
                    if (rec1.getUserId().compareTo(userID) == 0) {
                        final long ONE_MEGABYTE = 1024 * 1024;
                        final StorageReference image = storageReference.child(rec1.title + "_image");
                        image.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        rec1.setImageUri(uri);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                            }
                        });

                        recipes.add(rec1);
                        assert rec1 != null;
                    }
                }

                initRecipieRecycler(recipes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void initRecipieRecycler(final ArrayList<Recipe> recipes) {
        recipeAdapter = new HomePageAdapter(getApplicationContext(), recipes);
        recipeRecycler.setAdapter(recipeAdapter);
        recipeRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        recipeAdapter.setOnItemClickListener(new HomePageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String s = recipes.get(position).getTitle();
                Intent fullRec = new Intent(getApplicationContext(), HomePageItemView.class);
                fullRec.putExtra("currentID", s);
                startActivity(fullRec);
            }

            @Override
            public void onProfileClick(int position) {

            }

            @Override
            public void onDeleteClick(int position) {

            }
        });
    }
}
