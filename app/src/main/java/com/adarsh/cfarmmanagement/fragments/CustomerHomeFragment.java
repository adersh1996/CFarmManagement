package com.adarsh.cfarmmanagement.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.adarsh.cfarmmanagement.CustomerHomeActivity;
import com.adarsh.cfarmmanagement.DoctorHomeActivity;
import com.adarsh.cfarmmanagement.R;
import com.adarsh.cfarmmanagement.Retrofit.Api;
import com.adarsh.cfarmmanagement.Retrofit.ApiClient;
import com.adarsh.cfarmmanagement.adapters.ProductAdapter;
import com.adarsh.cfarmmanagement.adapters.ProductTypeAdapter;
import com.adarsh.cfarmmanagement.model.ViewProductDistrictRoot;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CustomerHomeFragment extends Fragment {

    public CustomerHomeActivity activity;
    RecyclerView productTypeRecyclerView, productRecyclerView;

    String[] productCategory = {"All", "Milk", "Flavored Milk", "Cheese", "Curd & Yogurts", "Butter & Spreads", "Paneer & Tofu", "Soya and Almond Milk", "Buttermilk & Lassi", "Milk Powder", "Cream and Condensed Milk"};

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        this.activity = (CustomerHomeActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_home, container, false);

        productTypeRecyclerView = view.findViewById(R.id.product_type_rv);
        productRecyclerView = view.findViewById(R.id.product_rv);

//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
//        productTypeRecyclerView.setLayoutManager(linearLayoutManager);
//        ProductTypeAdapter productTypeAdapter = new ProductTypeAdapter(productCategory, getActivity());
//        productTypeRecyclerView.setAdapter(productTypeAdapter);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("customer_pref", Context.MODE_PRIVATE);
        String district = sharedPreferences.getString("district", "");

        Api api = ApiClient.cattleFarm().create(Api.class);

        api.VIEW_PRODUCT_DISTRICT_ROOT_CALL(district).enqueue(new Callback<ViewProductDistrictRoot>() {
            @Override
            public void onResponse(Call<ViewProductDistrictRoot> call, Response<ViewProductDistrictRoot> response) {
                if (response.isSuccessful()) {
                    ViewProductDistrictRoot viewProductDistrictRoot = response.body();
                    if (viewProductDistrictRoot.getStatus().equals("success")) {
                        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
                        productRecyclerView.setLayoutManager(layoutManager);
                        ProductAdapter productAdapter = new ProductAdapter(viewProductDistrictRoot,getActivity());
                        productRecyclerView.setAdapter(productAdapter);
                    }else {
                        Toast.makeText(activity, "No Produc", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ViewProductDistrictRoot> call, Throwable t) {
                Toast.makeText(activity, "Server Error"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}