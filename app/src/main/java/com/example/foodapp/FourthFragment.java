package com.example.foodapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FourthFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FourthFragment extends Fragment {
    RecyclerView recipeRecycler;
    ArrayList<Recipe> recipes = new ArrayList<>();

    final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("RecipeImages");
    final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

    private TextView IntroDescription;
    private AlertDialog dialog;
    private AlertDialog.Builder dialogBuilder;
    private Button newcontactpopup_submit, newcontactpopup_cancel, uploadbtn;
    String user;

    HomePageAdapter recipeAdapter;

    View rootView;
    private boolean done = false;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FourthFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FourthFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FourthFragment newInstance(String param1, String param2) {
        FourthFragment fragment = new FourthFragment();
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

        rootView = inflater.inflate(R.layout.fragment_fourth, container, false);
        IntroDescription = rootView.findViewById(R.id.ProfileDescription);
        Button profSetting = rootView.findViewById(R.id.ProfileSetting);
        profSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ProfileSettingActivity.class);
                startActivity(intent);
            }
        });
        ImageView editIntro = rootView.findViewById(R.id.EditBtn);
        //update Intro
        ref.child("Users").child(userId).child("description").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
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
        editIntro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder = new AlertDialog.Builder(getContext());
                final View PopupEditIntro = getLayoutInflater().inflate(R.layout.edit_intro, null);
                final TextInputEditText EditIntro = (TextInputEditText) PopupEditIntro.findViewById(R.id.introduction);
                //set TextInputEditTExt
                ref.child("Users").child(userId).child("description").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(!task.isSuccessful()){
                            //ignore
                        }
                        else{
                            EditIntro.setText(String.valueOf(task.getResult().getValue()));
                        }
                    }
                });
                Log.d(null,EditIntro.getText().toString());
                newcontactpopup_submit = (Button) PopupEditIntro.findViewById(R.id.submit3);
                newcontactpopup_cancel = (Button) PopupEditIntro.findViewById(R.id.cancel3);

                dialogBuilder.setView(PopupEditIntro);
                dialog = dialogBuilder.create();
                dialog.show();

                newcontactpopup_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //define save button
                        final String introduction = EditIntro.getText().toString();
                        ref.child("Users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Map<String, Object> postValues = new HashMap<String,Object>();
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                                    Log.d(null, snapshot.toString());
                                    postValues.put(snapshot.getKey(),snapshot.getValue());
                                }
                                postValues.put("description",introduction);
                                ref.child("Users").child(userId).updateChildren(postValues);

                                //update intro
                                ref.child("Users").child(userId).child("description").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
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
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        dialog.dismiss();
                    }
                });



                newcontactpopup_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //close popup window
                        dialog.dismiss();
                    }
                });
            }
        });

        //UPLOAD IMAGE
        ImageView uploadimage = rootView.findViewById(R.id.ProfileImage);
        uploadimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder = new AlertDialog.Builder(getContext());
                final View PopupEditIntro = getLayoutInflater().inflate(R.layout.upload_image, null);
                ImageView editimage = PopupEditIntro.findViewById(R.id.editImage);
                uploadbtn = (Button) PopupEditIntro.findViewById(R.id.uploadImagebtn);

                dialogBuilder.setView(PopupEditIntro);
                dialog = dialogBuilder.create();
                dialog.show();
                //edit image
                editimage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //go to user's local files
                        //Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    }
                });
                //submit image button
                uploadbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //upload image onclick
                    }
                });
            }
        });
        recipeRecycler = rootView.findViewById(R.id.userPosts);
        FirebaseUser curruser = FirebaseAuth.getInstance().getCurrentUser();
        user = curruser.getUid();
        initList();
        // Inflate the layout for this fragment
        return rootView;


    }

    private void initList() {
        StorageReference storageRef;

        Query query = ref.child("Recipes");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot rec : snapshot.getChildren()) {
                    final Recipe rec1 = rec.getValue(Recipe.class);
                    if (rec1.getUserId().compareTo(user) == 0) {
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
            public void onDeleteClick(int position) {

            }
        });
    }

}