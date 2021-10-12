package com.inchub.experthub.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.inchub.experthub.Classes.skillAd;
import com.inchub.experthub.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecAdapter extends RecyclerView.Adapter<RecAdapter.ViewHolder> {



    private List<skillAd> skillsAdvertList;
    private Context context;

    public RecAdapter(List<skillAd> skillsAdvertList, Context context) {
        this.skillsAdvertList = skillsAdvertList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.container, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.PhoneNo.setText(skillsAdvertList.get(position).getPhoneNo());
        holder.WorkDesc.setText(skillsAdvertList.get(position).getWorkDesc());
        holder.Location.setText(skillsAdvertList.get(position).getLocation());
        Picasso.get().load(skillsAdvertList.get(position).getPic()).into(holder.pic);

    }

    @Override
    public int getItemCount() {
        return skillsAdvertList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        AppCompatTextView WorkDesc, PhoneNo, Location;
        ImageView pic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            WorkDesc = itemView.findViewById(R.id.cv_workDesc);
            PhoneNo = itemView.findViewById(R.id.cv_phoneNum);
            Location = itemView.findViewById(R.id.cv_location);
            pic = itemView.findViewById(R.id.profilePicCard);

        }
    }

}
