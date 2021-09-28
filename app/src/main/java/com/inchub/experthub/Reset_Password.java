package com.inchub.experthub;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.tvbarthel.lib.blurdialogfragment.SupportBlurDialogFragment;


public class Reset_Password extends SupportBlurDialogFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reset__password, container, false);
    }
}