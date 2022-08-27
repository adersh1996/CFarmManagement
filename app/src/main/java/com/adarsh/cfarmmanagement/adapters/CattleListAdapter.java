package com.adarsh.cfarmmanagement.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.adarsh.cfarmmanagement.HomeActivity;
import com.adarsh.cfarmmanagement.R;
import com.adarsh.cfarmmanagement.fragments.CattleDetailsFragment;
import com.adarsh.cfarmmanagement.model.ViewCattleRoot;

import java.util.Random;

public class CattleListAdapter extends RecyclerView.Adapter<CattleListAdapter.MyViewHolder> {

    ViewCattleRoot viewCattleRoot;
    Context context;

    public CattleListAdapter(ViewCattleRoot viewCattleRoot, Context context) {
        this.viewCattleRoot = viewCattleRoot;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.cattle_layout,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder,  final int position) {

        holder.tagNumberTV.setText(viewCattleRoot.getDetails().get(position).getTag_id());
        holder.linearLayout.setBackgroundColor(generateRandomColor());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CattleDetailsFragment cattleDetailsFragment=new CattleDetailsFragment();
                Bundle arguments = new Bundle();
                arguments.putString("cattleId",viewCattleRoot.getDetails().get(position).getId());
                cattleDetailsFragment.setArguments(arguments);
                ((HomeActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.container,cattleDetailsFragment,"cattleDetailsFragment").addToBackStack(null).commit();
            }
        });

}

    @Override
    public int getItemCount() {
        return viewCattleRoot.getDetails().size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tagNumberTV;
        LinearLayout linearLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tagNumberTV=itemView.findViewById(R.id.tagnotv);
            linearLayout=itemView.findViewById(R.id.layout);
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