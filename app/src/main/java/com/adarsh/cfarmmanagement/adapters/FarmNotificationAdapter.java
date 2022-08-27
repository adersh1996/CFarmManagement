package com.adarsh.cfarmmanagement.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adarsh.cfarmmanagement.R;
import com.adarsh.cfarmmanagement.model.VaccineNotificationRoot;
import com.bumptech.glide.Glide;

public class FarmNotificationAdapter extends RecyclerView.Adapter<FarmNotificationAdapter.MyViewHolder> {

    Context context;
    VaccineNotificationRoot vaccineNotificationRoot;


    public FarmNotificationAdapter(Context context, VaccineNotificationRoot vaccineNotificationRoot) {
        this.context = context;
        this.vaccineNotificationRoot = vaccineNotificationRoot;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.farm_profile_notification_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

       holder.notificationText.setText("Take Booster Dose of \n"+vaccineNotificationRoot.vaccine_details.get(position).disease+"\n on "+vaccineNotificationRoot.vaccine_details.get(position).booster_dose+"\n For Cattle"+vaccineNotificationRoot.vaccine_details.get(position).tag_id);
    }

    @Override
    public int getItemCount() {
        return vaccineNotificationRoot.vaccine_details.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView notificationImg;
        private TextView notificationText;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            notificationImg = itemView.findViewById(R.id.notification_img);
            notificationText = itemView.findViewById(R.id.notification_text);
        }
    }
}
