package com.adarsh.cfarmmanagement.adapters;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.adarsh.cfarmmanagement.CustomerHomeActivity;
import com.adarsh.cfarmmanagement.HomeActivity;
import com.adarsh.cfarmmanagement.R;
import com.adarsh.cfarmmanagement.fragments.CattleDetailsFragment;
import com.adarsh.cfarmmanagement.fragments.ProductDetailsFragment;
import com.adarsh.cfarmmanagement.model.ViewProductDistrictRoot;
import com.bumptech.glide.Glide;

import java.util.Random;

import soup.neumorphism.NeumorphCardView;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {
    ViewProductDistrictRoot viewProduct;
    Context context;


    public ProductAdapter(ViewProductDistrictRoot viewProduct, Context context) {
        this.viewProduct = viewProduct;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_layout,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder,  final int position) {


            holder.productNameTv.setText(viewProduct.getDetails().get(position).getProduct());
            holder.productPriceTv.setText(viewProduct.getDetails().get(position).getPrice());
            holder.productUnit.setText(viewProduct.getDetails().get(position).getQuantity()+" Unit");
            Glide.with(context).asBitmap().load(viewProduct.getDetails().get(position).getFile()).into(holder.product_image);

//            //  holder.productOldPrice.setPaintFlags(holder.productOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//            try {
//                int mrp=Integer.parseInt(viewProduct.getDetails().get(position).getPrice());
//                int productMrp=((mrp*5)/100)+mrp;
//                holder.productOldPrice.setText(String.valueOf(productMrp));
//                holder.productOldPrice.setPaintFlags(holder.productOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//            }catch (Exception e){
//
//            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ProductDetailsFragment productDetailsFragment=new ProductDetailsFragment();
                    Bundle arguments = new Bundle();
                    arguments.putString("product_id",viewProduct.getDetails().get(position).getId());
                    productDetailsFragment.setArguments(arguments);
                    ((CustomerHomeActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.customer_home_container,productDetailsFragment,"productDetailsFragment").addToBackStack(null).commit();
                }
            });


    }

    @Override
    public int getItemCount() {
        return viewProduct.getDetails().size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView product_image;
        TextView productNameTv,productPriceTv,productOldPrice,productUnit;
        LinearLayout linearLayout;
        NeumorphCardView productLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            product_image=itemView.findViewById(R.id.product_iv);
            productNameTv=itemView.findViewById(R.id.product_tv);
            productPriceTv=itemView.findViewById(R.id.product_price);
            productOldPrice=itemView.findViewById(R.id.product_old_price);
            productUnit=itemView.findViewById(R.id.product_unit);
            productLayout=itemView.findViewById(R.id.product_rl_layout);
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
