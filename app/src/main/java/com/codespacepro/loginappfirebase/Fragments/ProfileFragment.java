package com.codespacepro.loginappfirebase.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.codespacepro.loginappfirebase.Models.Users;
import com.codespacepro.loginappfirebase.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfileFragment extends Fragment {


    public ProfileFragment() {
        // Required empty public constructor
    }


    FirebaseDatabase database;
    DatabaseReference myRef;
    TextView username, fullname, email, gender, dob;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        username = view.findViewById(R.id.username_profile);
        fullname = view.findViewById(R.id.fullname_profile);
        email = view.findViewById(R.id.email_profile);
        gender = view.findViewById(R.id.gender_profile);
        dob = view.findViewById(R.id.dob_profile);

        myRef.child("Users").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);

                username.setText(users.getUsername());
                fullname.setText(users.getFullname());
                email.setText(users.getEmail());
                gender.setText(users.getGender());
                dob.setText(users.getDob());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }
}