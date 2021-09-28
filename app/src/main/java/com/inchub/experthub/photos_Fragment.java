package com.inchub.experthub;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;

import fr.tvbarthel.lib.blurdialogfragment.SupportBlurDialogFragment;

public class photos_Fragment extends SupportBlurDialogFragment {

    View view;

    private FloatingActionButton cam;
    AppCompatImageView camPhoto;
    private Uri imgUri;

    private  static final int TAKE_PICTURE = 1888;
    private  static final int TAKE_MY_PICTURE = 100;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.activity_photos_gallery, container, false);
        cam = view.findViewById(R.id.cameraFloat);
        camPhoto = view.findViewById(R.id.Photo1Upload);

        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.Companion.with(photos_Fragment.this)
                        .crop().compress(1024)
                        .maxResultSize(512,512)
                        .start();
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if (data != null)
        {
            imgUri = data.getData();
            camPhoto.setImageURI(imgUri);
        }
    }
    private void uploadPhoto(Uri imgUri)
    {
        Picasso.get().load(imgUri).into(camPhoto);
    }
}