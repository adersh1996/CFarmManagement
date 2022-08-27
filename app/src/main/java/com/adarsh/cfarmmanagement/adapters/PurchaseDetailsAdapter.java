package com.adarsh.cfarmmanagement.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adarsh.cfarmmanagement.R;
import com.adarsh.cfarmmanagement.model.PurchaseDetailRoot;
import com.adarsh.cfarmmanagement.model.ViewCattleRoot;
import com.bumptech.glide.Glide;

import java.util.Random;

public class PurchaseDetailsAdapter extends RecyclerView.Adapter<PurchaseDetailsAdapter.MyViewHolder> {

    PurchaseDetailRoot purchaseDetailRoot;
    Context context;


    public PurchaseDetailsAdapter(PurchaseDetailRoot purchaseDetailRoot, Context context) {
        this.purchaseDetailRoot = purchaseDetailRoot;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.purchase_details_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.productName.setText(purchaseDetailRoot.purchaseDetails.get(position).product);
        holder.date.setText(purchaseDetailRoot.purchaseDetails.get(position).date);
        holder.time.setText(purchaseDetailRoot.purchaseDetails.get(position).time);
        holder.price.setText(purchaseDetailRoot.purchaseDetails.get(position).price+" "+"₹");
        holder.custName.setText(purchaseDetailRoot.purchaseDetails.get(position).customer_name);
        holder.mobNumber.setText(purchaseDetailRoot.purchaseDetails.get(position).phone);
        holder.mailId.setText(purchaseDetailRoot.purchaseDetails.get(position).email);
        holder.address.setText(purchaseDetailRoot.purchaseDetails.get(position).address);
        Glide.with(context).asBitmap().load(purchaseDetailRoot.purchaseDetails.get(position).file).into(holder.productImg);


    }

    @Override
    public int getItemCount() {
        return purchaseDetailRoot.purchaseDetails.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout layoutZero;
        private LinearLayout layoutOne;
        private ImageView productImg;
        private TextView productName;
        private View viewOne;
        private LinearLayout layoutTwo;
        private TextView date;
        private LinearLayout layout3;
        private TextView time;
        private LinearLayout layout4;
        private TextView quantity;
        private LinearLayout layout5;
        private TextView price;
        private LinearLayout layout6;
        private TextView custName;
        private LinearLayout layout7;
        private TextView mobNumber;
        private LinearLayout layout8;
        private TextView mailId;
        private LinearLayout layout9;
        private TextView address;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            initView(itemView);

        }

        private void initView(View itemView) {
            layoutZero = itemView.findViewById(R.id.layout_zero);
            layoutOne = itemView.findViewById(R.id.layout_one);
            productImg = itemView.findViewById(R.id.product_img);
            productName = itemView.findViewById(R.id.product_name);
            viewOne = itemView.findViewById(R.id.view_one);
            layoutTwo = itemView.findViewById(R.id.layout_two);
            date = itemView.findViewById(R.id.date);
            layout3 = itemView.findViewById(R.id.layout_3);
            time = itemView.findViewById(R.id.time);
            layout4 = itemView.findViewById(R.id.layout_4);
            quantity = itemView.findViewById(R.id.quantity);
            layout5 = itemView.findViewById(R.id.layout_5);
            price = itemView.findViewById(R.id.price);
            layout6 = itemView.findViewById(R.id.layout_6);
            custName = itemView.findViewById(R.id.cust_name);
            layout7 = itemView.findViewById(R.id.layout_7);
            mobNumber = itemView.findViewById(R.id.mob_number);
            layout8 = itemView.findViewById(R.id.layout_8);
            mailId = itemView.findViewById(R.id.mailId);
            layout9 = itemView.findViewById(R.id.layout_9);
            address = itemView.findViewById(R.id.address);
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