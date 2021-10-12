package com.inchub.experthub;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUp extends AppCompatActivity {

    AppCompatImageView backNavigation;
    AppCompatTextView gotAnAccount;
    CircleImageView profilePic;
    StorageReference storageReference, profRef;
    FirebaseFirestore fStore;
    String userid;
    TextInputEditText usernameVar, emailVar, passwordVar;
    MaterialButton signUpButtonVar;
    CollectionReference ref = FirebaseFirestore.getInstance().collection("user");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        backNavigation = findViewById(R.id.backArrow);
        gotAnAccount = findViewById(R.id.gotAnAccountLink);
        profilePic = findViewById(R.id.profilePicture);

        usernameVar = findViewById(R.id.username);
        emailVar = findViewById(R.id.regEmail);
        passwordVar = findViewById(R.id.regPassword);

        signUpButtonVar = findViewById(R.id.signUpButton);

        signUpButtonVar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(usernameVar.getText().toString().trim())) {
                    usernameVar.setError("Please provide a username");
                    return;
                } else if (TextUtils.isEmpty(emailVar.getText().toString().trim())) {
                    emailVar.setError("Please provide your email");
                    return;
                } else if (TextUtils.isEmpty(passwordVar.getText().toString().trim())) {
                    passwordVar.setError("Please provide a password");
                    return;
                }

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailVar.getText().toString().trim(), passwordVar.getText().toString().trim()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(SignUp.this, "Sign Up Complete", Toast.LENGTH_SHORT).show();
                        // User userData = new User(usernameVar.getText().toString().trim(),emailVar.getText().toString().trim(),profilePic.getResources().toString());
                        userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        DocumentReference documentReference = fStore.collection("users").document(userid);
                        Map<String, Object> user = new HashMap<>();
                        user.put("username", usernameVar.getText().toString().trim());
                        user.put("userEmail", emailVar.getText().toString().trim());

                        // CollectionReference ref = fStore.collection("user");


                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(SignUp.this, "In", Toast.LENGTH_SHORT).show();
                            }
                        });
                        startActivity(new Intent(getApplicationContext(), ExpertiseProfileCreate.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        fStore = FirebaseFirestore.getInstance();


      /*  storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference profileRef = storageReference.child("profile.jpg");
        profRef = profileRef.child("users/profile.jpg");


        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profilePic);
            }
        });
       */

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGallery, 1000);

            }


        });

        backNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AppDesc.class));
                finish();
            }
        });
        gotAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignIn.class));
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10001) {
            if (resultCode == Activity.RESULT_OK) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                profilePic.setImageBitmap(bitmap);
                uploadImage(bitmap);
            }
        }

    }

    private void uploadImage(Bitmap bitmap) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        StorageReference reference = FirebaseStorage.getInstance().getReference()
                .child("profileImages")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid() + ".jpeg");

        reference.putBytes(baos.toByteArray()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                downloadUrl(reference);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void downloadUrl(StorageReference reference) {

        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Toast.makeText(SignUp.this, "Success " + uri, Toast.LENGTH_SHORT).show();
                setImageUrl(uri);
            }
        });
    }

    private void setImageUrl(Uri uri) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder().setPhotoUri(uri).build();
        user.updateProfile(request).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(SignUp.this, "Updated.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUp.this, "Profile image failed ...", Toast.LENGTH_SHORT).show();
            }
        });


    }

    /*private void uploadImage(Uri imageUri) {

        StorageReference fileRef = storageReference.child("users/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profilePic);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }*/
}