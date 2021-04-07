package com.example.foodapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FirstFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirstFragment extends Fragment {

    EditText inputSearch;
    RecyclerView recipeRecycler;
    ArrayList<Recipe> recipes = new ArrayList<>();

    final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("RecipeImages");

    HomePageAdapter recipeAdapter;

    View rootView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private boolean done;

    public FirstFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FirstFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FirstFragment newInstance(String param1, String param2) {
        FirstFragment fragment = new FirstFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_first, container, false);
        inputSearch = rootView.findViewById(R.id.inputSearch);
        inputSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                //If the event is a key-down event on the "enter" button
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
                    //perform action on key press
                    switchFragment(inputSearch.getText().toString());
                    return true;
                }
                return false;
            }
        });


        recipeRecycler = rootView.findViewById(R.id.recipeRecycler);

        //recipes.add(new Recipe("", "", "", "", "", "", "", "", 0, null, null));

        //testFunction();
        initList();

        //initRecipieRecycler(recipes);

        return rootView;
    }

    private void testFunction() {
        FirebaseDatabase.getInstance().getReference("Recipes")
                .child("Temp")
                .setValue(1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
            }
        });
    }

    private void initList() {
        StorageReference storageRef;

        done = false;
        Query query = ref.child("Recipes");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot rec : snapshot.getChildren()) {
                    final Recipe rec1 = rec.getValue(Recipe.class);
                    final long ONE_MEGABYTE = 1024 * 1024;
                    final StorageReference image = storageReference.child(rec1.title + "_image");
                    /*
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
                    */

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
                    recipes.add(rec1);
                    assert rec1 != null;
                }
                done = true;
                initRecipieRecycler(recipes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void initRecipieRecycler(final ArrayList<Recipe> recipes) {
        recipeAdapter = new HomePageAdapter(getContext(), recipes);
        recipeRecycler.setAdapter(recipeAdapter);
        recipeRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        recipeAdapter.setOnItemClickListener(new HomePageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String s = recipes.get(position).getTitle();
                Intent fullRec = new Intent(getContext(), HomePageItemView.class);
                fullRec.putExtra("currentID", s);
                startActivity(fullRec);
            }

            @Override
            public void onProfileClick(int position) {
                String name = recipes.get(position).getUsername();
                String userid = recipes.get(position).getUserId();
                Intent profilePage = new Intent(getContext(), ProfilePage.class);
                profilePage.putExtra("currentName", name);
                profilePage.putExtra("currentID", userid);
                startActivity(profilePage);

            }

            @Override
            public void onDeleteClick(int position) {

            }
        });
    }

    public void switchFragment(String category) {
        Intent intent = new Intent(getContext(), SearchActivity.class);
        intent.putExtra("category", category);
        startActivity(intent);
    }
}