package com.inchub.experthub;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.inchub.experthub.R;

public class menuSheet extends BottomSheetDialogFragment {
    MaterialButton profile,ads,home, logout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.sheet_layout, container, false);


        hooks(v);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),Profile.class));
                getActivity().finish();

            }
        });
        ads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),MyAdverts.class));
                getActivity().finish();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),Dashboard.class));
                getActivity().finish();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getContext())
                        .setMessage("Are you sure you want to logout?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(getContext(),SignIn.class));
                                getActivity().finish();
                            }
                        }).setNegativeButton("No",null).show();
            }
        });

        return v;
    }

    public void hooks(View v)
    {

        profile = v.findViewById(R.id.menu_profile);
        ads = v.findViewById(R.id.menu_myAdverts);
        home = v.findViewById(R.id.menu_myHome);
        logout = v.findViewById(R.id.menu_logout);
    }

}
