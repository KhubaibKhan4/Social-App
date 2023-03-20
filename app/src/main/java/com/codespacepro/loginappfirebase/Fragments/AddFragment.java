package com.codespacepro.loginappfirebase.Fragments;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.codespacepro.loginappfirebase.Models.Posts;
import com.codespacepro.loginappfirebase.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class AddFragment extends Fragment {


    public AddFragment() {
        // Required empty public constructor
    }


    EditText edtPostsDesc;
    ImageView PostImage;
    MaterialButton PostsNow, CancelNow;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseStorage storage;
    StorageReference mStorageRef;
    private static final int POST_IMAGE_REQUEST_CODE = 1;
    String postDesc;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        storage = FirebaseStorage.getInstance();
        mStorageRef = storage.getReference().child("post_images/");

        edtPostsDesc = view.findViewById(R.id.post_desc);
        PostImage = view.findViewById(R.id.post_image);
        PostsNow = view.findViewById(R.id.publishPostsBtn);
        CancelNow = view.findViewById(R.id.cancelPosts);

        PostsNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, new HomeFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        CancelNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.child("Users")
                        .child(mAuth.getUid())
                        .child("posts")
                        .push()
                        .removeValue();
            }
        });

        PostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, POST_IMAGE_REQUEST_CODE);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == POST_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data.getData() != null) {

            Uri imageUri = data.getData();
            PostImage.setImageURI(imageUri);

            UploadTask uploadTask = mStorageRef.putFile(imageUri);

            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        Task<Uri> DownloaduriTask = mStorageRef.getDownloadUrl();
                        DownloaduriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                if (DownloaduriTask.isSuccessful()) {
                                    Posts posts = new Posts(edtPostsDesc.getText().toString(), DownloaduriTask.getResult().toString());
                                    myRef.child("Users")
                                            .child(mAuth.getUid())
                                            .child("posts")
                                            .push()
                                            .setValue(posts);
                                }

                            }
                        });
                    }
                }
            });


        }
    }
}