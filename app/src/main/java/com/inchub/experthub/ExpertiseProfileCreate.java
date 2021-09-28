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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.inchub.experthub.Classes.SkillsAdvert;
import com.inchub.experthub.Classes.skillAd;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class ExpertiseProfileCreate extends AppCompatActivity {
    MaterialButton saveButton;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();

    MaterialTextView banner;
    String userid,itemId;
    CircleImageView profilePhoto;
    TextInputEditText locationField,phoneNumber,workDesc;
    MaterialButton saveAdvertButton,addPhotos, skip;
    String advertId;


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
                        .update("uri", uri.toString());

            }
        });

        profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGallery, 1000);
            }
        });



        saveAdvertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(locationField.getText()))
                {
                    locationField.setError("Please enter your location");
                }
              else  if (TextUtils.isEmpty(phoneNumber.getText()))
                {
                    phoneNumber.setError("Please enter your phone number");
                }
               else if (TextUtils.isEmpty(workDesc.getText()))
                {
                    workDesc.setError("Please enter your work description");
                }
                else {
                    try {
                        advertId = UUID.randomUUID().toString();
                        skillAd advert = new skillAd(phoneNumber.getText().toString().trim(), workDesc.getText().toString().trim(), locationField.getText().toString().trim(), null, advertId, null);
                        FirebaseFirestore.getInstance().collection("SkillsAdvert").document(userid).set(advert).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(ExpertiseProfileCreate.this, "Profile created", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), Dashboard.class));
                                itemId = documentReference.getId();
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ExpertiseProfileCreate.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(ExpertiseProfileCreate.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                //Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                Uri imgUri = data.getData();
                //profilePhoto.setImageBitmap(imgUri);
                uploadImage(imgUri);


            }
        }

    }
    private void uploadImage(Uri imageUri) {

            StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/profile.jpg");
            fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.get().load(uri).into(profilePhoto);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ExpertiseProfileCreate.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            /*
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

         */
        /*
    private void downloadUrl(StorageReference reference) {

        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Toast.makeText(ExpertiseProfileCreate.this, "Success "+uri, Toast.LENGTH_SHORT).show();
                setImageUrl(uri);
            }
        });
    }

         */
/*
    private void setImageUrl(Uri uri) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder().setPhotoUri(uri).build();
        user.updateProfile(request).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ExpertiseProfileCreate.this, "Updated.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ExpertiseProfileCreate.this, "Profile image failed ...", Toast.LENGTH_SHORT).show();
            }
        });

 */

    }
}