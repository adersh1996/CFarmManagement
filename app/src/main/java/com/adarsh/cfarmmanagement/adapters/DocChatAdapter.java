package com.adarsh.cfarmmanagement.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adarsh.cfarmmanagement.DoctorHomeActivity;
import com.adarsh.cfarmmanagement.HomeActivity;
import com.adarsh.cfarmmanagement.R;
import com.adarsh.cfarmmanagement.fragments.DocChatHistoryFragment;
import com.adarsh.cfarmmanagement.fragments.FarmChatFragment;
import com.adarsh.cfarmmanagement.model.DocChatFarmDetailRoot;

public class DocChatAdapter extends RecyclerView.Adapter<DocChatAdapter.MyViewHolder> {

    DocChatFarmDetailRoot docChatFarmDetailRoot;
    Context context;

    public DocChatAdapter(DocChatFarmDetailRoot docChatFarmDetailRoot, Context context) {
        this.docChatFarmDetailRoot = docChatFarmDetailRoot;
        this.context = context;
    }

    @NonNull
    @Override
    public DocChatAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.doc_chat_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DocChatAdapter.MyViewHolder holder, int position) {

        holder.farmName.setText(docChatFarmDetailRoot.farm_details.get(position).farm_name);
        holder.farmNumber.setText(docChatFarmDetailRoot.farm_details.get(position).phone);
        holder.farmMailId.setText(docChatFarmDetailRoot.farm_details.get(position).mailid);

        holder.respondBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocChatHistoryFragment docChatHistoryFragment = new DocChatHistoryFragment();
                Bundle arguments = new Bundle();
                arguments.putString("farmId",docChatFarmDetailRoot.farm_details.get(position).farm_id);
                docChatHistoryFragment.setArguments(arguments);
                ((DoctorHomeActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.doctor_home_container, docChatHistoryFragment, "docChatHistoryFragment").addToBackStack(null).commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return docChatFarmDetailRoot.farm_details.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView farmName, farmNumber, farmMailId;
        RelativeLayout respondBt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            farmName = itemView.findViewById(R.id.doc_chat_farm_name);
            farmNumber = itemView.findViewById(R.id.doc_chat_farm_phone);
            farmMailId = itemView.findViewById(R.id.doc_chat_farm_mail_id);
            respondBt = itemView.findViewById(R.id.contact_farm_bt);

        }
    }
}
