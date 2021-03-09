package com.example.foodapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class SearchActivity extends AppCompatActivity {

    RecyclerView recipeRecycler;
    ArrayList<Recipe> recipes = new ArrayList<>();
    FirebaseRecyclerOptions<Recipe> options;
    FirebaseRecyclerAdapter<Recipe, HomePageAdapter.HomeViewHolder> adapter;
    EditText inputSearch;

    final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("RecipeImages");

    HomePageAdapter recipeAdapter;

    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        inputSearch = (EditText) findViewById(R.id.inputSearch);
//        inputSearch.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                if (editable.toString()!=null){
//                    //loadData(editable.toSTring());
//                    category = inputSearch.getText().toString();
//                    initList();
//                }
//                else{
//                    //loadData("");
//                }
//            }
//        });
        inputSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {

                if((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)){
                    //perform action on key press
                    category = inputSearch.getText().toString();
                    recipes = new ArrayList<Recipe>();
                    initList();
                    refreshView();
                    return true;
                }

                return false;
            }
        });

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
                    final Recipe rec1 = rec.getValue(Recipe.class);
                    for (Categories c : rec1.getCategories()) {
                        if(c.value.toLowerCase().equals(category.toLowerCase())) {
                            final long ONE_MEGABYTE = 1024 * 1024;
                            final StorageReference image = storageReference.child(rec1.title + "_image");
                            image.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {
                                    // Data for "---.jpg" is returns, use this as needed
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    // imagetoUpload is the imageView we want to modify
                                    rec1.setImage(bitmap);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                }
                            });
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

   private void loadData(){
//        final Recipe rec1;
//        options = new FirebaseRecyclerOptions.Builder<Recipe>().setQuery(ref, Recipe.class).build();
//        adapter = new FirebaseRecyclerAdapter<Recipe, HomePageAdapter.HomeViewHolder>(options) {
//            @Override
//            protected void onBindViewHolder(@NonNull HomePageAdapter.HomeViewHolder holder, int position, @NonNull Recipe model) {
//                final long ONE_MEGABYTE = 1024 * 1024;
//                holder.profileName.setText(model.getUserId());
//                holder.splashDescription.setText(model.getDescription());
//                holder.splashTags.setText((CharSequence) model.getCategories());
//
//            }
//
//            @NonNull
//            @Override
//            public HomePageAdapter.HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_page_item, parent, false);
//                return new HomePageAdapter.HomeViewHolder(v);
//            }
//        };
    }

    private void initRecipieRecycler(final ArrayList<Recipe> recipes) {
        recipeAdapter = new HomePageAdapter(this, recipes);
        recipeRecycler.setAdapter(recipeAdapter);
        recipeRecycler.setLayoutManager(new LinearLayoutManager(this));

        recipeAdapter.setOnItemClickListener(new HomePageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String s = recipes.get(position).getTitle();
                Intent fullRec = new Intent(SearchActivity.this, HomePageItemView.class);
                fullRec.putExtra("currentID", s);
                startActivity(fullRec);
            }

            @Override
            public void onDeleteClick(int position) {

            }
        });
    }
    private void refreshView(){
        recipeRecycler.setVisibility(View.GONE);
        recipeRecycler.setVisibility(View.VISIBLE);
    }
}
