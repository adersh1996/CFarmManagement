package com.adarsh.cfarmmanagement.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.adarsh.cfarmmanagement.HomeActivity;
import com.adarsh.cfarmmanagement.R;
import com.adarsh.cfarmmanagement.Retrofit.Api;
import com.adarsh.cfarmmanagement.Retrofit.ApiClient;
import com.adarsh.cfarmmanagement.adapters.CattleListAdapter;
import com.adarsh.cfarmmanagement.adapters.FarmProductAdapter;
import com.adarsh.cfarmmanagement.model.ViewProductFarmRoot;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SellProductFragment extends Fragment {

    public HomeActivity activity;

    ExtendedFloatingActionButton addProduct, purchaseDetailsFab;
    RecyclerView recyclerView;

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        this.activity = (HomeActivity) activity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sell_product, container, false);

        addProduct = view.findViewById(R.id.add_product);
        recyclerView = view.findViewById(R.id.farm_product_rv);
        purchaseDetailsFab = view.findViewById(R.id.purchase_details);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("cattle_pref", Context.MODE_PRIVATE);
        String farm_id = sharedPreferences.getString("farm_id", "");

        Api api = ApiClient.cattleFarm().create(Api.class);
        api.VIEW_PRODUCT_FARM_ROOT_CALL(farm_id).enqueue(new Callback<ViewProductFarmRoot>() {
            @Override
            public void onResponse(Call<ViewProductFarmRoot> call, Response<ViewProductFarmRoot> response) {
                if (response.isSuccessful()) {
                    ViewProductFarmRoot viewProductFarm = response.body();
                    if (viewProductFarm.getStatus().equals("success")) {

                        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
                        recyclerView.setLayoutManager(layoutManager);
                        FarmProductAdapter farmProductAdapter = new FarmProductAdapter(viewProductFarm, getActivity());
                        recyclerView.setAdapter(farmProductAdapter);

//                        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                            @Override
//                            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                                if (newState == RecyclerView.SCROLL_STATE_IDLE)
//                                    purchaseDetailsFab.show();
//                                super.onScrollStateChanged(recyclerView, newState);
//                            }
//
//                            @Override
//                            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                                if (dy > 0 || dy < 0 && purchaseDetailsFab.isShown())
//                                    purchaseDetailsFab.hide();
//                            }
//                        });

                    } else {
                        Toast.makeText(activity, "No Products To display", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ViewProductFarmRoot> call, Throwable t) {

            }
        });


        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddProductFragment addProductFragment = new AddProductFragment();
                Bundle arguments = new Bundle();
                addProductFragment.setArguments(arguments);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, addProductFragment, "AddProductFragment").addToBackStack(null).commit();

            }
        });

        purchaseDetailsFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PurchaseDetailsFragment purchaseDetailsFragment = new PurchaseDetailsFragment();
                Bundle arguments = new Bundle();
                purchaseDetailsFragment.setArguments(arguments);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, purchaseDetailsFragment, "purchaseDetailsFragment").addToBackStack(null).commit();
            }
        });


        return view;
    }
}