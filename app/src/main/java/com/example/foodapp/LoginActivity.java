package com.example.foodapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    private EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFirebaseAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
    }


    public void Login(View view) {
        final String emailId = email.getText().toString().trim();
        final String passwordID = password.getText().toString().trim();
        //user Verification
        if(emailId.isEmpty()){
            email.setError("Please enter your email");
            email.requestFocus();
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(emailId).matches()){
            email.setError("PLease enter the correct email format");
            email.requestFocus();
        }
        else if(passwordID.isEmpty()){
            password.setError("Please enter your password");
            password.requestFocus();
        }
        else
        {
            mFirebaseAuth.signInWithEmailAndPassword(emailId, passwordID)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //check the user verified the email address
                                if(mFirebaseAuth.getCurrentUser().isEmailVerified()){
                                    finish();
                                    Intent mainInt = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(mainInt);
                                } else{
                                    Toast.makeText(LoginActivity.this, "Please verify your email address",
                                            Toast.LENGTH_LONG).show();
                                }

                            } else {
                                Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    public void toSignUp(View view){
       Intent regInt = new Intent(LoginActivity.this,RegisterActivity.class);
       startActivity(regInt);
    }
}
