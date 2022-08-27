package com.adarsh.cfarmmanagement.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adarsh.cfarmmanagement.R;
import com.adarsh.cfarmmanagement.fragments.DetailedMilkReportFragment;
import com.adarsh.cfarmmanagement.model.DetailedMilkStatusRoot;

public class DetailedMilkReportAdapter extends RecyclerView.Adapter<DetailedMilkReportAdapter.MyViewHolder> {

    DetailedMilkStatusRoot detailedMilkStatusRoot;
    Context context;

    public DetailedMilkReportAdapter(DetailedMilkStatusRoot detailedMilkStatusRoot, Context context) {
        this.detailedMilkStatusRoot = detailedMilkStatusRoot;
        this.context = context;
    }

    @NonNull
    @Override
    public DetailedMilkReportAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.milk_report_layout,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailedMilkReportAdapter.MyViewHolder holder, int position) {

        holder.date.setText(detailedMilkStatusRoot.getMilk_details().get(position).getP_date());
        holder.spoiled.setText(detailedMilkStatusRoot.getMilk_details().get(position).getSpoiled());
        holder.produced.setText(detailedMilkStatusRoot.getMilk_details().get(position).getProduced());

    }

    @Override
    public int getItemCount() {
        return detailedMilkStatusRoot.getMilk_details().size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView date,produced,spoiled;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            date=itemView.findViewById(R.id.date_tv);
            produced=itemView.findViewById(R.id.produced_tv);
            spoiled=itemView.findViewById(R.id.spolied_tv);
        }
    }
}
