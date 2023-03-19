package com.codespacepro.loginappfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.codespacepro.loginappfirebase.Models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {
    EditText edtEmail, edtPass, edtCPass;
    MaterialButton SignupBtn;
    TextView signupToLogin;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        init();

        SignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                createAccount();
                Toast.makeText(SignupActivity.this, "Signup Button Clicked..", Toast.LENGTH_SHORT).show();
            }
        });
        signupToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });
    }

    void init() {
        edtEmail = (EditText) findViewById(R.id.edt_username);
        edtPass = (EditText) findViewById(R.id.edt_fullname);
        edtCPass = (EditText) findViewById(R.id.edt_gender);
        SignupBtn = (MaterialButton) findViewById(R.id.btnSignup);
        signupToLogin = (TextView) findViewById(R.id.signup_to_login);


        edtEmail.setTextColor(Color.WHITE);
        edtPass.setTextColor(Color.WHITE);
        edtCPass.setTextColor(Color.WHITE);
    }

    void createAccount() {
        String email = edtEmail.getText().toString().trim();
        String pass = edtPass.getText().toString().trim();
        progressBar.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(edtEmail.getText().toString()) || TextUtils.isEmpty(edtPass.getText().toString())) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "Values can't be Empty..", Toast.LENGTH_SHORT).show();
        } else {

            if (edtPass.getText().toString().equals(edtCPass.getText().toString())) {

                mAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    progressBar.setVisibility(View.GONE);
                                    Users users = new Users(email, pass);
                                    myRef.child("Users").child(mAuth.getUid()).setValue(users);
                                    Intent intent = new Intent(SignupActivity.this, ProfileActivity.class);
                                    intent.putExtra("email", edtEmail.getText().toString());
                                    intent.putExtra("pass", edtPass.getText().toString());
                                    startActivity(intent);

                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(SignupActivity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        });


            } else {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "Password isn't Same..", Toast.LENGTH_SHORT).show();
            }
        }

    }
}