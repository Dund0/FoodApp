package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class ProfileSettingActivity extends AppCompatActivity {
    private EditText newcontactpopup_email, newcontactpopup_username;
    private Button newcontactpopup_submit, newcontactpopup_cancel;
    private AlertDialog dialog;
    private AlertDialog.Builder dialogBuilder;
    private FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setting);
        //adds Cookflex title on appbar
        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        //adds backward on appbar
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }
    public void IntentNotificationSetting(View view){

    }
    public void EditEmail(View view){
        dialogBuilder = new AlertDialog.Builder(this);
        final View PopupEditEmail = getLayoutInflater().inflate(R.layout.change_email, null);
        newcontactpopup_email = (EditText) PopupEditEmail.findViewById(R.id.changeEmail);
        newcontactpopup_email.setText(currentFirebaseUser.getEmail());
        newcontactpopup_submit = (Button) PopupEditEmail.findViewById(R.id.submit);
        newcontactpopup_cancel = (Button) PopupEditEmail.findViewById(R.id.cancel);

        dialogBuilder.setView(PopupEditEmail);
        dialog = dialogBuilder.create();
        dialog.show();

        newcontactpopup_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String newEmail = newcontactpopup_email.getText().toString();
                ref.child("Users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Map<String, Object> postValues = new HashMap<String,Object>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                                    Log.d(null, snapshot.toString());
                            postValues.put(snapshot.getKey(),snapshot.getValue());
                        }
                        postValues.put("email",newEmail);
                        ref.child("Users").child(userId).updateChildren(postValues);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
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
    public void EditUsername(View view){
        dialogBuilder = new AlertDialog.Builder(this);
        final View PopupEditUsername = getLayoutInflater().inflate(R.layout.change_username, null);

        newcontactpopup_username =(EditText) PopupEditUsername.findViewById(R.id.changeUsername);
        //show current username on popup
        ref.child("Users").child(userId).child("username").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(!task.isSuccessful()){
                    //ignore
                }
                else{
                    newcontactpopup_username.setText(String.valueOf(task.getResult().getValue()));
                }
            }
        });

        newcontactpopup_submit = (Button) PopupEditUsername.findViewById(R.id.submit2);
        newcontactpopup_cancel = (Button) PopupEditUsername.findViewById(R.id.cancel2);

        dialogBuilder.setView(PopupEditUsername);
        dialog = dialogBuilder.create();
        dialog.show();

        newcontactpopup_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String newUsername = newcontactpopup_username.getText().toString();
                ref.child("Users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Map<String, Object> postValues = new HashMap<String,Object>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                                    Log.d(null, snapshot.toString());
                            postValues.put(snapshot.getKey(),snapshot.getValue());
                        }
                        postValues.put("username",newUsername);
                        ref.child("Users").child(userId).updateChildren(postValues);
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
    public void Signout(View view){
        FirebaseAuth.getInstance().signOut();
        finish();
        startActivity(new Intent(this, LoginActivity.class));
    }
    public void DeleteAcccount(View view){
        dialogBuilder = new AlertDialog.Builder(this);
        final View PopupdeleteAccount = getLayoutInflater().inflate(R.layout.delete_account, null);

        final EditText getDelete = (EditText) PopupdeleteAccount.findViewById(R.id.deleteText);

        newcontactpopup_submit = (Button) PopupdeleteAccount.findViewById(R.id.submit4);
        newcontactpopup_cancel = (Button) PopupdeleteAccount.findViewById(R.id.cancel4);

        dialogBuilder.setView(PopupdeleteAccount);
        dialog = dialogBuilder.create();
        dialog.show();

        newcontactpopup_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String gettingdelete = getDelete.getText().toString();
                if(gettingdelete.equals("Delete my account")){
                    Toast.makeText(ProfileSettingActivity.this, "success",Toast.LENGTH_LONG).show();
                    dialog.dismiss();
//                    finish();
//                    startActivity(new Intent(ProfileSettingActivity.this, LoginActivity.class));
                }
                else{
                    Toast.makeText(ProfileSettingActivity.this, "fail",Toast.LENGTH_LONG).show();

                }
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
}