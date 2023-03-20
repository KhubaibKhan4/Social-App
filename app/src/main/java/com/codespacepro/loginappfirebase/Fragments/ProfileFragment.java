package com.codespacepro.loginappfirebase.Fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codespacepro.loginappfirebase.Models.Users;
import com.codespacepro.loginappfirebase.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {


    public ProfileFragment() {
        // Required empty public constructor
    }


    FirebaseDatabase database;
    DatabaseReference myRef;
    TextView username, fullname, email, gender, dob;
    CircleImageView ProfileImage;
    FirebaseStorage storage;
    StorageReference myStorageRef;
    Uri imageUri;
    private static final int PICK_IMAGE_REQUEST = 1;
    UploadTask uploadTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        storage = FirebaseStorage.getInstance();
        myStorageRef = storage.getReference().child("images/");


        username = view.findViewById(R.id.username_profile);
        fullname = view.findViewById(R.id.fullname_profile);
        email = view.findViewById(R.id.email_profile);
        gender = view.findViewById(R.id.gender_profile);
        dob = view.findViewById(R.id.dob_profile);

        ProfileImage = view.findViewById(R.id.profile_image);

        ProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        myRef.child("Users").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);

                username.setText(users.getUsername());
                fullname.setText(users.getFullname());
                email.setText(users.getEmail());
                Glide.with(getContext()).load(users.getProfile()).placeholder(R.drawable.avatar_profile).into(ProfileImage);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Get the image URI from the result data
            Uri imageUri = data.getData();

            // Set the image URI to the ImageView to display the selected image
            ProfileImage.setImageURI(imageUri);
            uploadTask = myStorageRef.putFile(imageUri);
            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        Task<Uri> downloadUriTask = myStorageRef.getDownloadUrl();
                        downloadUriTask.addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (downloadUriTask.isSuccessful()) {
                                    // Store the download URL in Firebase Realtime Database
                                    myRef.child("Users").child(FirebaseAuth.getInstance().getUid()).child("profile")
                                            .setValue(downloadUriTask.getResult().toString());
                                }
                            }
                        });
                    }
                }
            });
        }
    }
}