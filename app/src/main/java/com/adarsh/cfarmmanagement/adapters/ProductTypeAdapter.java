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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.adarsh.cfarmmanagement.R;

import java.util.Random;

public class ProductTypeAdapter extends RecyclerView.Adapter<ProductTypeAdapter.MyViewHolder> {
    String []productCategoryName;
     Context context;
     int key=0;
    public ProductTypeAdapter(String[] productCategoryName,Context context) {
        this.productCategoryName = productCategoryName;
        this.context=context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_category_layout,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder,  final int position) {

        holder.productCategoryNameTv.setText(productCategoryName[position]);

      holder.itemView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              if( key == 0)
              {
                  holder.layout.setBackground(ContextCompat.getDrawable(context, R.drawable.layout_bg));
                  key=1;

              }

             else if(key==1)
              {
                  holder.layout.setBackground(ContextCompat.getDrawable(context, R.drawable.category_background));
                  holder.productCategoryNameTv.setTextColor(ContextCompat.getColor(context, R.color.black));
                  key=0;
              }
          }
      });
       // holder.productOldPrice.setPaintFlags(holder.productOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                BookDetailsFragment bookDetailsFragment=new BookDetailsFragment();
//                Bundle arguments = new Bundle();
//                arguments.putString("title",bookRootModel.getBook().get(position).getTitle());
//                arguments.putString( "url" ,bookRootModel.getBook().get(position).getPhoto());
//                bookDetailsFragment.setArguments(arguments);
//                ((UserHomeActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.container, bookDetailsFragment , "OptionsFragment").addToBackStack(null).commit();
//
//                //  Toast.makeText(context.getApplicationContext(), pictures[position], Toast.LENGTH_SHORT).show();
//
//         }
//        });
    }

    @Override
    public int getItemCount() {
        return productCategoryName.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView productCategoryNameTv;
        LinearLayout layout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            productCategoryNameTv=itemView.findViewById(R.id.category_view);
            layout=itemView.findViewById(R.id.category_layout);
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
