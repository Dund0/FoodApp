package com.example.foodapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;

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
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ThirdFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ThirdFragment extends Fragment implements View.OnClickListener{
    private static final int RESULT_LOAD_IMAGE = 1;

    EditText title, description, time;

    RatingBar difficulty;

    ImageView imageToUpload, stepUpload, current;
    Spinner types, materials, method, situation, culture, timeOfDay;
    Button ingredientAdd, stepAdd;

    RecyclerView ingredientRecycler;
    RecyclerView stepRecycler;

    boolean checker;

    StepAdapter stepAdapter;
    IngredientAdapter ingredientAdapter;

    ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
    ArrayList<Step> steps = new ArrayList<Step>();

    View rootView;

    SharedPreferences pass;
    SharedPreferences.Editor editor;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ThirdFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ThirdFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ThirdFragment newInstance(String param1, String param2) {
        ThirdFragment fragment = new ThirdFragment();
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

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_third, container, false);

        pass = getContext().getSharedPreferences("currentImgView", 0);
        editor = pass.edit();

        title = (EditText) rootView.findViewById(R.id.RecipeTitle);
        description = (EditText) rootView.findViewById(R.id.Description);
        time = (EditText) rootView.findViewById(R.id.time);

        difficulty = (RatingBar) rootView.findViewById(R.id.difficultyBar);

        imageToUpload = (ImageView) rootView.findViewById(R.id.imageUpload);
        stepUpload = (ImageView) rootView.findViewById(R.id.stepUpload);

        types = (Spinner) rootView.findViewById(R.id.types);
        materials = (Spinner) rootView.findViewById(R.id.materials);
        method = (Spinner) rootView.findViewById(R.id.method);
        situation = (Spinner) rootView.findViewById(R.id.situation);
        culture = (Spinner) rootView.findViewById(R.id.culture);
        timeOfDay = (Spinner) rootView.findViewById(R.id.TimeOfDay);

        ingredientAdd = (Button) rootView.findViewById(R.id.addIngredient);
        stepAdd = (Button) rootView.findViewById(R.id.addStep);

        ingredientRecycler = rootView.findViewById(R.id.ingredientsRecycler);
        stepRecycler = rootView.findViewById(R.id.StepRecycler);

        imageToUpload.setOnClickListener(this);
        ingredientAdd.setOnClickListener(this);
        stepAdd.setOnClickListener(this);

        ingredients.add(new Ingredient("",""));
        initIngredientRecycler(ingredients);
        steps.add(new Step("", drawableToBitmap(getContext().getDrawable(R.drawable.ic_add_dark)), new ImageView(getContext())));

        initStepRecycler(steps);

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onClick(View view) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        switch (view.getId()) {
            case R.id.imageUpload:
                current = imageToUpload;
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                break;
            //case R.id.stepUpload:
                //current = stepUpload;
                //startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                //break;
            case R.id.addIngredient:
                addIngredient(true);
                break;
            case R.id.addStep:
                addStep(true);
                break;
        }
    }

    private void addStep(boolean update) {
        for (int i = 0; i < stepRecycler.getChildCount(); i++) {
            StepAdapter.StepViewHolder holder = (StepAdapter.StepViewHolder) stepRecycler.findViewHolderForAdapterPosition(i);
            steps.set(i, new Step(holder.getDescription(), holder.getImage(), holder.image));
            stepAdapter.notifyItemChanged(i);
        }

        if(update == true ) {
            steps.add(new Step("", drawableToBitmap(getContext().getDrawable(R.drawable.ic_add_dark)), new ImageView(getContext())));
            stepAdapter.notifyItemInserted(stepRecycler.getChildCount());
        }

    }

    private void addIngredient(boolean update) {
        for (int i = 0; i < ingredientRecycler.getChildCount(); i++) {
            IngredientAdapter.IngredientViewHolder holder = (IngredientAdapter.IngredientViewHolder) ingredientRecycler.findViewHolderForAdapterPosition(i);
            ingredients.set(i, new Ingredient(holder.getIngredient(), holder.getAmount()));
            ingredientAdapter.notifyItemChanged(i);

        }
        if(update == true) {
            ingredients.add(new Ingredient("", ""));
            stepAdapter.notifyItemInserted(ingredientRecycler.getChildCount());
        }
    }

    //method to upload image
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == RESULT_LOAD_IMAGE && data !=null && !checker) {
            Uri selectedImage = data.getData();
            current.setImageURI(selectedImage);
        }
        if (resultCode == RESULT_OK && requestCode == RESULT_LOAD_IMAGE && data !=null && checker) {
            int position = pass.getInt("position", 0);
            Uri selectedImage = data.getData();
            current.setImageURI(selectedImage);
            steps.get(position).setImageBitmap(((BitmapDrawable) current.getDrawable()).getBitmap());
            stepAdapter.notifyItemChanged(position);
            checker = false;
        }
    }

    //create post button
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.post_button, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    //initialize ingredient recycler
    private void initIngredientRecycler(ArrayList<Ingredient> ingredients){
        ingredientAdapter = new IngredientAdapter(getContext(), ingredients);
        ingredientRecycler.setAdapter(ingredientAdapter);
        ingredientRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    //initialize step recycler
    private void initStepRecycler(final ArrayList<Step> steps){
        stepAdapter = new StepAdapter(getContext(), steps);
        stepRecycler.setAdapter(stepAdapter);
        stepRecycler.setLayoutManager(new LinearLayoutManager(getContext()));


        stepAdapter.setOnItemClickListener(new StepAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                editor.putInt("position", position);
                editor.apply();

                checker = true;
                current = steps.get(position).getImageView();
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                //steps.get(position).setImageBitmap(((BitmapDrawable) current.getDrawable()).getBitmap());
            }

            @Override
            public void onDeleteClick(int position) {

            }
        });
    }

    @SuppressLint("WrongThread")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //BUTTON FOR CREATE POST
        addIngredient(false);
        addStep(false);
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        final FirebaseStorage storage = FirebaseStorage.getInstance();
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final String userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

        StorageReference storageRef = storage.getReference();


        Log.d(null,"Trying to add Recipe");

        final Recipe recipe = new Recipe(types.getSelectedItem().toString(),materials.getSelectedItem().toString(),method.getSelectedItem().toString(),
                situation.getSelectedItem().toString(),culture.getSelectedItem().toString(), timeOfDay.getSelectedItem().toString(), title.getText().toString(),
                description.getText().toString(), time.getText().toString(), difficulty.getRating(),ingredients,steps, userId, userName);
      
        Log.d(null,"Trying to update User's list of recipes");
        ref.child("Users").child(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d(null, dataSnapshot.toString());
                        Map<String, Object> postValues = new HashMap<String,Object>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                            Log.d(null, snapshot.toString());
                            postValues.put(snapshot.getKey(),snapshot.getValue());
                        }

//                        Log.d(null, postValues.toString());
                        ArrayList<String> recipes = (ArrayList<String>) postValues.get("recipes");
//                        Log.d(null,recipes.toString());
                        recipes.add(recipe.title);
//                        Log.d(null,recipes.toString());
                        ref.child("Users").child(userId).updateChildren(postValues);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
        ref.child("Recipes").child(recipe.title).setValue(recipe);
        Log.d(null,"Successfully added Recipe");

        Log.d(null,"Trying to add Recipe_Image");
        // Get the data from an ImageView as bytes
        if(imageToUpload.getDrawable() != null) {
            imageToUpload.setDrawingCacheEnabled(true);
            imageToUpload.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) imageToUpload.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            StorageReference recipeRef = storageRef.child("RecipeImages").child(recipe.title + "_image");
            //uploads the image
            UploadTask uploadTask = recipeRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...
                }
            });
        }
        Log.d(null,"Added Recipe_Image");
        if(!steps.isEmpty()) {
            for(int i = 0; i < steps.size(); i++)
            {
                Step currentStep = steps.get(i);
                ImageView stepImage = currentStep.getImageView();
                if(stepImage.getDrawable() != null)
                {
                    stepImage.setDrawingCacheEnabled(true);
                    stepImage.buildDrawingCache();
                    Bitmap bitmap = ((BitmapDrawable) stepImage.getDrawable()).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();
                    StorageReference stepRef = storageRef.child("StepImages").child(recipe.title).child(String.valueOf(i));
                    //uploads the image
                    UploadTask uploadTask = stepRef.putBytes(data);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                            // ...
                        }
                    });
                }
            }
        }
        Log.d(null, "successfully added steps images");

        Log.d(null, "updated user");
        return super.onOptionsItemSelected(item);
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
