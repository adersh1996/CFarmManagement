package com.adarsh.cfarmmanagement.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adarsh.cfarmmanagement.HomeActivity;
import com.adarsh.cfarmmanagement.R;
import com.adarsh.cfarmmanagement.Retrofit.Api;
import com.adarsh.cfarmmanagement.Retrofit.ApiClient;
import com.adarsh.cfarmmanagement.model.DeleteProductRoot;
import com.adarsh.cfarmmanagement.model.ViewProductsRoot;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewProductDetails extends Fragment {


    ImageView productImg;
    TextInputEditText productName, productPrice, productQuantity, productIngredients, productExpiryDate;
    TextView deleteProduct;
    RelativeLayout deleteProductBt;
    public HomeActivity activity;

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        this.activity = (HomeActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_product_details, container, false);

        productImg = view.findViewById(R.id.view_product_image);
        productName = view.findViewById(R.id.view_product_product_name);
        productPrice = view.findViewById(R.id.view_product_price);
        productQuantity = view.findViewById(R.id.view_product_quantity);
        productIngredients = view.findViewById(R.id.view_product_ingredients);
        productExpiryDate = view.findViewById(R.id.view_product_expiry_date);
        deleteProduct = view.findViewById(R.id.add_product_bt);
        deleteProductBt = view.findViewById(R.id.delete_product_bt);

        productName.setEnabled(false);
        productPrice.setEnabled(false);
        productQuantity.setEnabled(false);
        productIngredients.setEnabled(false);
        productExpiryDate.setEnabled(false);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("cattle_pref", Context.MODE_PRIVATE);
        String farm_id = sharedPreferences.getString("farm_id", "");

        String product_id = getArguments().getString("product_id");

        Api api = ApiClient.cattleFarm().create(Api.class);
        api.VIEW_PRODUCTS_ROOT_CALL(product_id, farm_id).enqueue(new Callback<ViewProductsRoot>() {
            @Override
            public void onResponse(Call<ViewProductsRoot> call, Response<ViewProductsRoot> response) {
                if (response.isSuccessful()) {
                    ViewProductsRoot viewProductsRoot = response.body();
                    if (viewProductsRoot.status.equals("success")) {
                        productName.setText(viewProductsRoot.Details.get(0).product);
                        productName.setEnabled(false);
                        productPrice.setText(viewProductsRoot.Details.get(0).price);
                        productQuantity.setText(viewProductsRoot.Details.get(0).quantity);
                        productIngredients.setText(viewProductsRoot.Details.get(0).ingredients);
                        productExpiryDate.setText(viewProductsRoot.Details.get(0).exp_date);
                        Glide.with(activity).asBitmap().load(viewProductsRoot.Details.get(0).file).into(productImg);
                    }
                } else {
                    Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ViewProductsRoot> call, Throwable t) {
                Toast.makeText(activity, "Failed" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        deleteProductBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                api.DELETE_PRODUCT_ROOT_CALL(product_id).enqueue(new Callback<DeleteProductRoot>() {
                    @Override
                    public void onResponse(Call<DeleteProductRoot> call, Response<DeleteProductRoot> response) {
                        if (response.isSuccessful()) {
                            DeleteProductRoot deleteProductRoot = response.body();
                            if (deleteProductRoot.status) {
                                Toast.makeText(activity, "Product Deleted", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DeleteProductRoot> call, Throwable t) {
                        Toast.makeText(activity, "Failed" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        return view;
    }
}