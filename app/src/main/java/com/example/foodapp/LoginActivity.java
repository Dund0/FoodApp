package com.example.foodapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
    }

    public void Login(View view) {
        Intent MainInt = new Intent(this, MainActivity.class);
        startActivity(MainInt);
    }

    public void toSignUp(View view){
        Intent RegInt = new Intent(this, RegisterActivity.class);
        startActivity(RegInt);
    }
}
