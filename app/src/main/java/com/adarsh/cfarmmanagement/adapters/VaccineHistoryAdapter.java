package com.adarsh.cfarmmanagement.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adarsh.cfarmmanagement.R;
import com.adarsh.cfarmmanagement.model.VaccinationHistoryRoot;

public class VaccineHistoryAdapter extends RecyclerView.Adapter<VaccineHistoryAdapter.MyViewHolder> {

    Context context;
    VaccinationHistoryRoot vaccinationHistoryRoot;

    public VaccineHistoryAdapter(Context context, VaccinationHistoryRoot vaccinationHistoryRoot) {
        this.context = context;
        this.vaccinationHistoryRoot = vaccinationHistoryRoot;
    }

    @NonNull
    @Override
    public VaccineHistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.vaccine_history_layout,parent,false);
        return new VaccineHistoryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VaccineHistoryAdapter.MyViewHolder holder, int position) {

        holder.vaccineName.setText(vaccinationHistoryRoot.Details.get(position).disease);
        holder.takenDate.setText(vaccinationHistoryRoot.Details.get(position).date);

    }

    @Override
    public int getItemCount() {
        return vaccinationHistoryRoot.Details.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView vaccineName,takenDate;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            vaccineName=itemView.findViewById(R.id.taken_vaccine_name);
            takenDate=itemView.findViewById(R.id.vaccine_taken_date);
        }
    }
}
