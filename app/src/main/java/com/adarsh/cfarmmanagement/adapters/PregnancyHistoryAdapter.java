package com.adarsh.cfarmmanagement.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adarsh.cfarmmanagement.R;
import com.adarsh.cfarmmanagement.model.DetailedMilkStatusRoot;
import com.adarsh.cfarmmanagement.model.PregnancyDetailRoot;

public class PregnancyHistoryAdapter extends RecyclerView.Adapter<PregnancyHistoryAdapter.MyViewHolder> {

    PregnancyDetailRoot pregnancyDetailRoot;
    Context context;

    public PregnancyHistoryAdapter(PregnancyDetailRoot pregnancyDetailRoot, Context context) {
        this.pregnancyDetailRoot = pregnancyDetailRoot;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pregnancy_history_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.breedingDate.setText(pregnancyDetailRoot.pregnancy_details.get(position).breeding_date);
        holder.calfTagNo.setText(pregnancyDetailRoot.pregnancy_details.get(position).tag_id);
        holder.calfDob.setText(pregnancyDetailRoot.pregnancy_details.get(position).baby_dob);

    }

    @Override
    public int getItemCount() {
        return pregnancyDetailRoot.pregnancy_details.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView breedingDate;
        private TextView calfTagNo;
        private TextView calfDob;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            initView(itemView);

        }
        private void initView(View itemView) {
            breedingDate = itemView.findViewById(R.id.breeding_date);
            calfTagNo = itemView.findViewById(R.id.calf_tag_no);
            calfDob = itemView.findViewById(R.id.calf_dob);
        }
    }
}
