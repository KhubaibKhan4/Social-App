package com.codespacepro.loginappfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.codespacepro.loginappfirebase.Models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity {
    ImageView Profile;
    EditText edtUsername, edtFullName, edtGender, edtDOB;
    MaterialButton btnNext;

    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profile);
        init();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });


    }

    private void init() {
        Profile = (ImageView) findViewById(R.id.profile_image);
        edtFullName = (EditText) findViewById(R.id.edt_fullname);
        edtUsername = (EditText) findViewById(R.id.edt_username);
        edtGender = (EditText) findViewById(R.id.edt_gender);
        edtDOB = (EditText) findViewById(R.id.edt_dob);

        edtUsername.setTextColor(Color.WHITE);
        edtFullName.setTextColor(Color.WHITE);
        edtGender.setTextColor(Color.WHITE);
        edtDOB.setTextColor(Color.WHITE);

        btnNext = (MaterialButton) findViewById(R.id.Next_btn);
    }

    private void updateProfile() {
        String username = edtUsername.getText().toString();
        String fullname = edtFullName.getText().toString();
        String email = getIntent().getStringExtra("email").toString().trim();
        String pass = getIntent().getStringExtra("pass").toString().trim();
        String gender = edtGender.getText().toString();
        String dob = edtDOB.getText().toString();
        Users users = new Users(username, fullname, email, pass, gender, dob);
        myRef.child("Users").child(FirebaseAuth.getInstance().getUid()).setValue(users)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                            finishAffinity();
                        } else {
                            Toast.makeText(ProfileActivity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}