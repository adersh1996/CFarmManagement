package com.adarsh.cfarmmanagement.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adarsh.cfarmmanagement.HomeActivity;
import com.adarsh.cfarmmanagement.R;
import com.adarsh.cfarmmanagement.fragments.FarmChatFragment;
import com.adarsh.cfarmmanagement.model.DoctorsListRoot;
import com.bumptech.glide.Glide;

import java.util.Random;

public class VetenaryDoctorAdapter extends RecyclerView.Adapter<VetenaryDoctorAdapter.MyViewHolder> {
    DoctorsListRoot doctorsList;
    Context context;


    public VetenaryDoctorAdapter(DoctorsListRoot doctorsList, Context context) {
        this.doctorsList = doctorsList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.vetenary_doctor__layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.doctorNameTv.setText(doctorsList.doctorDetails.get(position).name);
        holder.doctorQualification.setText(doctorsList.doctorDetails.get(position).qualification);
        holder.doctorExp.setText(doctorsList.doctorDetails.get(position).experience);
        Glide.with(context).load(doctorsList.doctorDetails.get(position).profile).into(holder.doctorImage);


        holder.docContactBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, chatStatus, Toast.LENGTH_SHORT).show();
                FarmChatFragment farmChatFragment = new FarmChatFragment();
                Bundle arguments = new Bundle();
                arguments.putString("docId", doctorsList.doctorDetails.get(position).doc_id);
                arguments.putBoolean("chatStatus",doctorsList.doctorDetails.get(position).chat_status);
                farmChatFragment.setArguments(arguments);
                ((HomeActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.container, farmChatFragment, "farmChatFragment").addToBackStack(null).commit();
            }
        });


    }

    @Override
    public int getItemCount() {
        return doctorsList.doctorDetails.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView doctorImage;
        TextView doctorNameTv, doctorQualification, doctorExp;
        RelativeLayout docContactBt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            doctorImage = itemView.findViewById(R.id.dr_iv);
            doctorNameTv = itemView.findViewById(R.id.dr_tv);
            doctorExp = itemView.findViewById(R.id.dr_exp);
            doctorQualification = itemView.findViewById(R.id.dr_qlfctn);
            docContactBt = itemView.findViewById(R.id.doc_contact);

        }
    }

    final Random mRandom = new Random(System.currentTimeMillis());

    public int generateRandomColor() {
        // This is the base color which will be mixed with the generated one
        final int baseColor = Color.WHITE;

        final int baseRed = Color.red(baseColor);
        final int baseGreen = Color.green(baseColor);
        final int baseBlue = Color.blue(baseColor);

        final int red = (baseRed + mRandom.nextInt(256)) / 2;
        final int green = (baseGreen + mRandom.nextInt(256)) / 2;
        final int blue = (baseBlue + mRandom.nextInt(256)) / 2;

        return Color.rgb(red, green, blue);
    }
}
