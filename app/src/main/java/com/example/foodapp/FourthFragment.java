package com.example.foodapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.bumptech.glide.Glide;
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
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FourthFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FourthFragment extends Fragment {
    private static final int RESULT_LOAD_IMAGE = 1;
    RecyclerView recipeRecycler;
    ArrayList<Recipe> recipes = new ArrayList<>();
    final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("RecipeImages");
    final StorageReference storageReferenceProfile = FirebaseStorage.getInstance().getReference().child("UserImages");
    final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

    private ImageView currentProfilePic, editImage;
    private TextView IntroDescription, profilename;
    private AlertDialog dialog;
    private AlertDialog.Builder dialogBuilder;
    private Button newcontactpopup_submit, newcontactpopup_cancel, uploadbtn;
    private Uri selectedImage;

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
        currentProfilePic = rootView.findViewById(R.id.ProfileImage);
        IntroDescription = rootView.findViewById(R.id.ProfileDescription);
        Button profSetting = rootView.findViewById(R.id.ProfileSetting);
        user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        profSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ProfileSettingActivity.class);
                startActivity(intent);
            }
        });
        ImageView editIntro = rootView.findViewById(R.id.EditBtn);

        final StorageReference image = storageReferenceProfile.child(user + "_image");

        image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getContext()).load(uri).placeholder(R.drawable.ic_person).dontAnimate().into(currentProfilePic);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

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
        //show current username
        profilename = rootView.findViewById(R.id.ProfileName);
        ref.child("Users").child(userId).child("username").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(!task.isSuccessful()){
                    //ignore
                }
                else{
                    profilename.setText(String.valueOf(task.getResult().getValue()));
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
                editImage = PopupEditIntro.findViewById(R.id.editImage);
                uploadbtn = (Button) PopupEditIntro.findViewById(R.id.uploadImagebtn);

                dialogBuilder.setView(PopupEditIntro);
                dialog = dialogBuilder.create();
                dialog.show();
                //edit image
                editImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //go to user's local files
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                    }
                });
                //submit image button
                uploadbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //upload image onclick
                        currentProfilePic.setImageURI(selectedImage);
                        //save it into database
                        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                        if(currentProfilePic.getDrawable() != null) {
                            currentProfilePic.setDrawingCacheEnabled(true);
                            currentProfilePic.buildDrawingCache();
                            Bitmap bitmap = ((BitmapDrawable) currentProfilePic.getDrawable()).getBitmap();
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] data = baos.toByteArray();
                            StorageReference recipeRef = storageRef.child("UserImages").child(userId + "_image");
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
                        dialog.dismiss();
                    }
                });
            }
        });
        recipeRecycler = rootView.findViewById(R.id.userPosts);
        initList();
        // Inflate the layout for this fragment
        return rootView;


    }

    private void initList() {
        Query query = ref.child("Recipes");
        final String userID = user;
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot rec : snapshot.getChildren()) {
                    final Recipe rec1 = rec.getValue(Recipe.class);
                    if (rec1.getUserId().compareTo(userID) == 0) {
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
            public void onProfileClick(int position) {

            }

            @Override
            public void onDeleteClick(int position) {

            }
        });
    }

    //method to upload image
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == RESULT_LOAD_IMAGE && data !=null) {
            selectedImage = data.getData();
            editImage.setImageURI(selectedImage);
        }
    }
}