package com.inchub.experthub;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.inchub.experthub.Adapters.RecAdapter;
import com.inchub.experthub.Classes.skillAd;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {


    AppCompatImageView pop_menu_btn;
    FloatingActionButton logoutBtn;
    AppCompatImageView menu;
    FloatingActionButton newAdvert;
    private RecyclerView advertsRecycler;
    View v;
    List<skillAd> skillAdList = new ArrayList<>();

    RecAdapter recAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        logoutBtn = findViewById(R.id.logout);
        newAdvert = findViewById(R.id.newAdvert);
        menu = findViewById(R.id.pop_menu_btn);
        pop_menu_btn = findViewById(R.id.pop_menu_btn);

        /*pop_menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                *//*PopupMenu popupMenu = new PopupMenu(getApplicationContext(), view);
                popupMenu.setOnMenuItemClickListener(this);
                popupMenu.inflate(R.menu.popup_menu);
                popupMenu.show();*//*

                android.widget.PopupMenu popupMenu = new PopupMenu(getApplicationContext(), view);
                popupMenu.setOnMenuItemClickListener(this);
                popupMenu.inflate(R.menu.popup_menu);
                popupMenu.show();

            }
        });*/

        newAdvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ExpertiseProfileCreate.class));
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                menuSheet menuSheet = new menuSheet();
                menuSheet.show(getSupportFragmentManager(), "menu sheet");

            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),SignIn.class));
                finish();
            }
        });

        setRecyclerView();


        FirebaseFirestore.getInstance().collection("skillsAdvert")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(!value.isEmpty()){
                            for (DocumentChange dc : value.getDocumentChanges()){
                                switch (dc.getType()){

                                    case ADDED:
                                        skillAd p = dc.getDocument().toObject(skillAd.class);
                                        skillAdList.add(p);
                                        recAdapter.notifyDataSetChanged();
                                        break;
                                    case MODIFIED:
                                        skillAd prod = dc.getDocument().toObject(skillAd.class);
                                        skillAdList.set(dc.getOldIndex(), prod);
                                        recAdapter.notifyDataSetChanged();
                                        break;
                                    case REMOVED:
                                        skillAdList.remove(dc.getOldIndex());
                                        recAdapter.notifyDataSetChanged();
                                        break;
                                }
                            }
                        }
                    }
                });


    }

    private void setRecyclerView() {
        advertsRecycler = findViewById(R.id.DashboardRecycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        advertsRecycler.setLayoutManager(layoutManager);
        recAdapter = new RecAdapter(skillAdList, getApplicationContext());
        advertsRecycler.setAdapter(recAdapter);
        advertsRecycler.setHasFixedSize(true);

    }
    public void popLoginMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(getApplicationContext(), view);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }

    @SuppressLint("NonConstantResourceId")
    public boolean onMenuItemClick(MenuItem item) {

        try {
            switch (item.getItemId()) {
                case R.id.profile:
                    startActivity(new Intent(getApplicationContext(), Profile.class));
                    return true;
                case R.id.myAds:
                    //Toast.makeText(this, "about menu clicked", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MyAdverts.class));
                    return true;
                case R.id.home:
                    //Toast.makeText(this, "admin menu clicked", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), Dashboard.class));
                    return true;
                case R.id.logout:
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getApplicationContext(),SignIn.class));
                    finish();
                    Toast.makeText(this, "logged out.", Toast.LENGTH_SHORT).show();
                    return true;
                default:
                    return false;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


}