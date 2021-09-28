package com.inchub.experthub;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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

public class Dashboard extends AppCompatActivity {



    MaterialRadioButton logoutBtn;
    private RecyclerView advertsRecycler;
    List<skillAd> skillAdList = new ArrayList<>();
    ArrayList<skillAd> list;
    RecAdapter recAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        logoutBtn = findViewById(R.id.logout);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),SignIn.class));
                finish();
            }
        });

        setRecyclerView();

        FirebaseFirestore.getInstance().collection("product")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(!value.isEmpty()){
                            for (DocumentChange dc : value.getDocumentChanges()){
                                switch (dc.getType()){

                                    case ADDED:
                                        skillAd p = dc.getDocument().toObject(skillAd.class);
                                        p.setAdvertId(dc.getDocument().getId());
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
/*
        skillAdList.add(new skillAd("0152996112", "DBA", "Polokwane", "3", "https://image.makewebeasy.net/makeweb/0/PevRXTY8V/DefaultData/001_note20series_productimage_mo_720.jpg"));
        skillAdList.add(new skillAd("0152996113", "FAS", "JHB", "3", "https://securecontent.jackyselectronics.com/Images/product-images/b143feb8-e210-72b0-ff90-f41acaeffa29.jpg"));
        skillAdList.add(new skillAd("0152996114", "TECH", "DBN", "3", "https://securecontent.jackyselectronics.com/Images/product-images/b143feb8-e210-72b0-ff90-f41acaeffa29.jpg"));
        skillAdList.add(new skillAd("0152996115", "HAIR", "LTT", "3", "https://image.makewebeasy.net/makeweb/0/PevRXTY8V/DefaultData/001_note20series_productimage_mo_720.jpg"));
        skillAdList.add(new skillAd("0152996116", "NAILS", "TZN", "3", "https://securecontent.jackyselectronics.com/Images/product-images/b143feb8-e210-72b0-ff90-f41acaeffa29.jpg"));
        skillAdList.add(new skillAd("0152996117", "CARPENTRY", "PTA", "3", "https://image.makewebeasy.net/makeweb/0/PevRXTY8V/DefaultData/001_note20series_productimage_mo_720.jpg"));
        skillAdList.add(new skillAd("0152996118", "FAS", "CPT", "3", "https://securecontent.jackyselectronics.com/Images/product-images/b143feb8-e210-72b0-ff90-f41acaeffa29.jpg"));
        skillAdList.add(new skillAd("0152996119", "FAS", "THH", "3", "https://image.makewebeasy.net/makeweb/0/PevRXTY8V/DefaultData/001_note20series_productimage_mo_720.jpg"));
        skillAdList.add(new skillAd("0152996112", "FAS", "NZH", "3", "https://securecontent.jackyselectronics.com/Images/product-images/b143feb8-e210-72b0-ff90-f41acaeffa29.jpg"));
        skillAdList.add(new skillAd("0152996112", "FAS", "GP", "3", "https://image.makewebeasy.net/makeweb/0/PevRXTY8V/DefaultData/001_note20series_productimage_mo_720.jpg"));
        skillAdList.add(new skillAd("0152996112", "FAS", "Home", "3", "https://securecontent.jackyselectronics.com/Images/product-images/b143feb8-e210-72b0-ff90-f41acaeffa29.jpg"));
*/
        recAdapter = new RecAdapter(skillAdList, getApplicationContext());
        advertsRecycler.setAdapter(recAdapter);
        advertsRecycler.setHasFixedSize(true);

    }
}