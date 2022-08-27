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

import com.adarsh.cfarmmanagement.HomeActivity;
import com.adarsh.cfarmmanagement.R;
import com.adarsh.cfarmmanagement.Retrofit.Api;
import com.adarsh.cfarmmanagement.Retrofit.ApiClient;
import com.adarsh.cfarmmanagement.adapters.DetailedMilkReportAdapter;
import com.adarsh.cfarmmanagement.model.DetailedMilkStatusRoot;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DetailedMilkReportFragment extends Fragment {

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
        View view = inflater.inflate(R.layout.fragment_detailed_milk_report, container, false);

        recyclerView = view.findViewById(R.id.milk_report_rv);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("cattle_pref", Context.MODE_PRIVATE);
        String farm_id = sharedPreferences.getString("farm_id", "");

        Api api = ApiClient.cattleFarm().create(Api.class);
        api.DETAILED_MILK_STATUS_ROOT_CALL(farm_id).enqueue(new Callback<DetailedMilkStatusRoot>() {
            @Override
            public void onResponse(Call<DetailedMilkStatusRoot> call, Response<DetailedMilkStatusRoot> response) {
                if (response.isSuccessful()) {
                    DetailedMilkStatusRoot detailedMilkStatusRoot = response.body();
                    if (detailedMilkStatusRoot.getStatus()) {
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
                        recyclerView.setLayoutManager(layoutManager);
                        DetailedMilkReportAdapter detailedMilkReportAdapter = new DetailedMilkReportAdapter(detailedMilkStatusRoot, getActivity());
                        recyclerView.setAdapter(detailedMilkReportAdapter);
                        recyclerView.setHasFixedSize(true);
                    }
                }
            }

            @Override
            public void onFailure(Call<DetailedMilkStatusRoot> call, Throwable t) {

            }
        });


        return view;
    }
}