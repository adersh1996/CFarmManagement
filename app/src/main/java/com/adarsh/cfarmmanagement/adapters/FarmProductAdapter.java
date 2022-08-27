package com.adarsh.cfarmmanagement.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adarsh.cfarmmanagement.HomeActivity;
import com.adarsh.cfarmmanagement.R;
import com.adarsh.cfarmmanagement.fragments.ViewProductDetails;
import com.adarsh.cfarmmanagement.model.ViewProductFarmRoot;
import com.bumptech.glide.Glide;

import java.util.Random;

public class FarmProductAdapter extends RecyclerView.Adapter<FarmProductAdapter.MyViewHolder> {

    ViewProductFarmRoot viewProduct;
    Context context;

    public FarmProductAdapter(ViewProductFarmRoot viewProduct, Context context) {
        this.viewProduct = viewProduct;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_layout_farm, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.productNameTv.setText(viewProduct.getDetails().get(position).getProduct());
        holder.productPriceTv.setText(viewProduct.getDetails().get(position).getPrice());
        holder.productQuantity.setText(viewProduct.getDetails().get(position).getQuantity()+" Unit");
        Glide.with(context).asBitmap().load(viewProduct.getDetails().get(position).getFile()).into(holder.product_image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewProductDetails viewProductDetailsFrag = new ViewProductDetails();
                Bundle arguments = new Bundle();
                arguments.putString("product_id",viewProduct.getDetails().get(position).getId());
                viewProductDetailsFrag.setArguments(arguments);
                ((HomeActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.container,viewProductDetailsFrag,"ProductDetailsFrag").addToBackStack(null).commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return viewProduct.getDetails().size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView product_image;
        TextView productNameTv, productPriceTv,productQuantity;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            product_image = itemView.findViewById(R.id.product_farm_iv);
            productNameTv = itemView.findViewById(R.id.product_farm_tv);
            productPriceTv = itemView.findViewById(R.id.product_farm_price);
            productQuantity=itemView.findViewById(R.id.product_farm_unit);


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
