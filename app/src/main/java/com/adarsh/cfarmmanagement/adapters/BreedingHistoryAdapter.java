package com.adarsh.cfarmmanagement.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adarsh.cfarmmanagement.R;
import com.adarsh.cfarmmanagement.model.BreedingHistoryRoot;

public class BreedingHistoryAdapter extends RecyclerView.Adapter<BreedingHistoryAdapter.MyViewHolder> {

    Context context;
    BreedingHistoryRoot breedingHistoryRoot;

    public BreedingHistoryAdapter(Context context, BreedingHistoryRoot breedingHistoryRoot) {
        this.context = context;
        this.breedingHistoryRoot = breedingHistoryRoot;
    }

    @NonNull
    @Override
    public BreedingHistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.breeding_history_layout,parent,false);
        return new BreedingHistoryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BreedingHistoryAdapter.MyViewHolder holder, int position) {

        holder.breedingDate.setText(breedingHistoryRoot.breeding_details.get(position).breeding_date);
        holder.weaningDate.setText(breedingHistoryRoot.breeding_details.get(position).weaning_date);

    }

    @Override
    public int getItemCount() {
        return breedingHistoryRoot.breeding_details.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView breedingDate,weaningDate;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            breedingDate=itemView.findViewById(R.id.breeding_date);
            weaningDate=itemView.findViewById(R.id.weaning_date);
        }
    }
}
