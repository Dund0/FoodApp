package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private EditText email, password, user, confirmpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.emailAddress);
        password = findViewById(R.id.createpassword);
        user = findViewById(R.id.createuser);
        confirmpassword = findViewById(R.id.confirmpassword);
    }

    public void Signup(View view){
        final String emailId = email.getText().toString().trim();
        final String userID = user.getText().toString().trim();
        final String pwd = password.getText().toString().trim();
        String pwdconfirm = confirmpassword.getText().toString().trim();

        //user Verification
        if(emailId.isEmpty()){
            email.setError("Please enter your email");
            email.requestFocus();
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(emailId).matches()){
            email.setError("PLease enter the correct email format");
            email.requestFocus();
        }
        else if(userID.isEmpty()){
            user.setError("Please enter your username");
            user.requestFocus();
        }
        else if(pwd.isEmpty()){
            password.setError("Please enter your password");
            password.requestFocus();
        }
        else if(pwdconfirm.isEmpty()){
            confirmpassword.setError("Please confirm your password");
            confirmpassword.requestFocus();
        }

        else if(!pwd.equals(pwdconfirm)){
            confirmpassword.setError("Passwords do not match!");
            confirmpassword.requestFocus();
        }
        else{
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            //the next two lines add a recipe
            Recipe recipe = new  Recipe("S","I","C","K","O");
            ref.child("Recipes").child("newExample").setValue(recipe);
            ref.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    final boolean[]exists = {false};
                    for (DataSnapshot child : snapshot.getChildren()) {
                        String usernameDB = child.child("username").getValue().toString().trim();
                        if (usernameDB.equals(userID)) {
                            exists[0] = true;
                            break;
                        }
                    }
                    if(exists[0]){
                        Toast.makeText(RegisterActivity.this, getString(R.string.usernameTaken),Toast.LENGTH_LONG).show();
                    }
                    else {
                        mFirebaseAuth.createUserWithEmailAndPassword(emailId, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    //Send the Email verification
                                    mFirebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                User userstuff = new User(
                                                        userID,
                                                        emailId,
                                                        pwd
                                                );
                                                //put it into database
                                                FirebaseDatabase.getInstance().getReference("Users")
                                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                        .setValue(userstuff).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            Toast.makeText(RegisterActivity.this, getString(R.string.reg_success), Toast.LENGTH_LONG).show();
                                                            finish();
                                                        } else{
                                                            Toast.makeText(RegisterActivity.this, getString(R.string.reg_fail), Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                            }
                                            else{
                                                Toast.makeText(RegisterActivity.this, task.getException().getMessage(),
                                                        Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });

                                } else{
                                    Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(RegisterActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    Toast.makeText(RegisterActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    public void backToLogin(View view){
        finish();
    }
}