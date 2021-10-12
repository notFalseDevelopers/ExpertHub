package com.inchub.experthub;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
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
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.inchub.experthub.Classes.skillAd;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;


public class ExpertiseProfileCreate extends AppCompatActivity {
    MaterialButton saveButton;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();

    MaterialTextView banner;
    FirebaseStorage storage;
    String userid,itemId;
    CircleImageView profilePhoto;
    private Uri filePath;
    private String adId;
    TextInputEditText locationField,phoneNumber,workDesc;
    StorageReference storageReference;
    MaterialButton saveAdvertButton,addPhotos, skip;
    private final int PICK_IMAGE_REQUEST = 22;
    String advertId;
    String image_Uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expertise_profile_create);

        banner = findViewById(R.id.welcomeBanner);
        userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference documentReference = fStore.collection("users").document(userid);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.exists())
                {
                    banner.setText("Welcome "+value.getString("username"));
                }
            }
        });
        profilePhoto = findViewById(R.id.profilePicture);
        locationField = findViewById(R.id.location);
        phoneNumber = findViewById(R.id.userNumber);
        workDesc = findViewById(R.id.workDescription);
        saveAdvertButton = findViewById(R.id.saveProfile);
        addPhotos = findViewById(R.id.addPhotosButton);
        skip = findViewById(R.id.skipProfile);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Dashboard.class));
            }
        });


        addPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*photos_Fragment pf = new photos_Fragment();
                pf.show(getSupportFragmentManager(),photos_Fragment.class.getSimpleName());
        */
            startActivity(new Intent(getApplicationContext(), PhotosGallery.class));
            }
        });

        StorageReference profileRef = FirebaseStorage.getInstance().getReference().child("users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid() +"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                ProgressDialog progressDialog = new ProgressDialog(ExpertiseProfileCreate.this);
                progressDialog.setMessage("Loading");
                progressDialog.show();
                Picasso.get().load(uri).into(profilePhoto);
                FirebaseFirestore.getInstance().collection("users")
                        .document(itemId)
                        .update("uri", uri.toString()).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ExpertiseProfileCreate.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                Toast.makeText(ExpertiseProfileCreate.this, uri.toString(), Toast.LENGTH_SHORT).show();

            }
        });

        profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SelectImage();

            }

            private void SelectImage() {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(intent.ACTION_GET_CONTENT);
                startActivityForResult(
                        Intent.createChooser(
                                intent,
                                "Select Image from here..."),
                        PICK_IMAGE_REQUEST);

            }
        });


        saveAdvertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    if (validations())
                    {
                        String productID;
                        productID = UUID.randomUUID().toString();
                        skillAd skillAdvert = new skillAd(phoneNumber.getText().toString().trim(),workDesc.getText().toString().trim(),locationField.getText().toString().trim(),null,productID);

                        FirebaseFirestore.getInstance().collection("skillsAdvert").add(skillAdvert)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(ExpertiseProfileCreate.this, "Advert uploaded", Toast.LENGTH_SHORT).show();
                                        adId = documentReference.getId();
                                        uploadImage();
                                        startActivity(new Intent(getApplicationContext(), Dashboard.class));
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ExpertiseProfileCreate.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }
                catch (Exception e){
                    Toast.makeText(ExpertiseProfileCreate.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });


    }


    private void uploadImage() {
        if (filePath != null){
            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    taskSnapshot.getStorage().getDownloadUrl()
                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    try {
                                        FirebaseFirestore.getInstance().collection("skillsAdvert")
                                                .document(adId)
                                                .update("pic", uri.toString());

                                    }catch (Exception e){

                                        Toast.makeText(ExpertiseProfileCreate.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                    }


                                }
                            });
                    Toast.makeText(ExpertiseProfileCreate.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(ExpertiseProfileCreate.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress
                            = (100.0
                            * taskSnapshot.getBytesTransferred()
                            / taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage(
                            "Uploaded "
                                    + (int)progress + "%");
                }
            });
        }
    }

    private boolean validations() {

        boolean valid = true;

        if (locationField.getText().toString().length() == 0)
        {
            locationField.setError("Please enter location");
            valid = false;
        }
        else if (phoneNumber.getText().toString().length() == 0)
        {
            phoneNumber.setError("Please enter phone number");
            valid = false;
        }
        else if (workDesc.getText().toString().length() == 0)
        {
            workDesc.setError("Please enter work description");
            valid = false;
        }
        return valid;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            filePath = data.getData();
            try {
                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                profilePhoto.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

    }
}