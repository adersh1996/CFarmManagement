package com.adarsh.cfarmmanagement.fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adarsh.cfarmmanagement.HomeActivity;
import com.adarsh.cfarmmanagement.R;
import com.adarsh.cfarmmanagement.Retrofit.Api;
import com.adarsh.cfarmmanagement.Retrofit.ApiClient;
import com.adarsh.cfarmmanagement.adapters.DetailedMilkReportAdapter;
import com.adarsh.cfarmmanagement.adapters.FarmNotificationAdapter;
import com.adarsh.cfarmmanagement.model.VaccineNotificationRoot;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FarmNotificationFragment extends Fragment {

    public HomeActivity activity;

    RecyclerView recyclerView;


    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        this.activity = (HomeActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_farm_notification, container, false);

        recyclerView = view.findViewById(R.id.notification_rv);

        String farmId = getArguments().getString("farm_id");

        Api api = ApiClient.cattleFarm().create(Api.class);
        api.VACCINE_NOTIFICATION_ROOT_CALL(farmId).enqueue(new Callback<VaccineNotificationRoot>() {
            @Override
            public void onResponse(Call<VaccineNotificationRoot> call, Response<VaccineNotificationRoot> response) {

                if (response.isSuccessful()){
                    VaccineNotificationRoot vaccineNotificationRoot= response.body();
                    if (vaccineNotificationRoot.status){
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);
                        recyclerView.setLayoutManager(layoutManager);
                        FarmNotificationAdapter farmNotificationAdapter=new FarmNotificationAdapter(getContext(),vaccineNotificationRoot);
                        recyclerView.setAdapter(farmNotificationAdapter);
                        recyclerView.setHasFixedSize(true);
                    }
                }


            }

            @Override
            public void onFailure(Call<VaccineNotificationRoot> call, Throwable t) {

            }
        });


        return view;
    }
}