package com.adarsh.cfarmmanagement.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.adarsh.cfarmmanagement.HomeActivity;
import com.adarsh.cfarmmanagement.R;
import com.adarsh.cfarmmanagement.Retrofit.Api;
import com.adarsh.cfarmmanagement.Retrofit.ApiClient;
import com.adarsh.cfarmmanagement.adapters.DetailedMilkReportAdapter;
import com.adarsh.cfarmmanagement.adapters.PurchaseDetailsAdapter;
import com.adarsh.cfarmmanagement.model.DetailedMilkStatusRoot;
import com.adarsh.cfarmmanagement.model.PurchaseDetailRoot;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PurchaseDetailsFragment extends Fragment {

RecyclerView recyclerView;
    public HomeActivity activity;

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        this.activity = (HomeActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_purchase_details, container, false);

        recyclerView=view.findViewById(R.id.purchase_details_rv);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("cattle_pref", Context.MODE_PRIVATE);
        String farm_id = sharedPreferences.getString("farm_id", "");

        Api api = ApiClient.cattleFarm().create(Api.class);
        api.PURCHASE_DETAIL_ROOT_CALL(farm_id).enqueue(new Callback<PurchaseDetailRoot>() {
            @Override
            public void onResponse(Call<PurchaseDetailRoot> call, Response<PurchaseDetailRoot> response) {
                if (response.isSuccessful()){
                    PurchaseDetailRoot purchaseDetailRoot= response.body();
                    if (purchaseDetailRoot.status){
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);
                        recyclerView.setLayoutManager(layoutManager);
                        PurchaseDetailsAdapter purchaseDetailsAdapter=new PurchaseDetailsAdapter(purchaseDetailRoot,getContext());
                        recyclerView.setAdapter(purchaseDetailsAdapter);
                        recyclerView.setHasFixedSize(true);
                    }
                    else {
                        Toast.makeText(activity, "No Purchase History Available", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PurchaseDetailRoot> call, Throwable t) {
                Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }
}