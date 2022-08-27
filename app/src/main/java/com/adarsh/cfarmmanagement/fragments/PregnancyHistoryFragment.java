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
import android.widget.Toast;

import com.adarsh.cfarmmanagement.HomeActivity;
import com.adarsh.cfarmmanagement.R;
import com.adarsh.cfarmmanagement.Retrofit.Api;
import com.adarsh.cfarmmanagement.Retrofit.ApiClient;
import com.adarsh.cfarmmanagement.adapters.DetailedMilkReportAdapter;
import com.adarsh.cfarmmanagement.adapters.PregnancyHistoryAdapter;
import com.adarsh.cfarmmanagement.model.PregnancyDetailRoot;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PregnancyHistoryFragment extends Fragment {
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
        View view=inflater.inflate(R.layout.fragment_pregnancy_history, container, false);

        recyclerView=view.findViewById(R.id.pregnancy_his_rv);


        String farmId=getArguments().getString("farmId");
        String cattleId=getArguments().getString("cattleId");


        Api api = ApiClient.cattleFarm().create(Api.class);
        api.PREGNANCY_DETAIL_ROOT_CALL(cattleId,farmId).enqueue(new Callback<PregnancyDetailRoot>() {
            @Override
            public void onResponse(Call<PregnancyDetailRoot> call, Response<PregnancyDetailRoot> response) {
                if (response.isSuccessful()){
                    PregnancyDetailRoot pregnancyDetailRoot= response.body();
                    if (pregnancyDetailRoot.status){
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);
                        recyclerView.setLayoutManager(layoutManager);
                        PregnancyHistoryAdapter pregnancyHistoryAdapter=new PregnancyHistoryAdapter(pregnancyDetailRoot,getContext());
                        recyclerView.setAdapter(pregnancyHistoryAdapter);
                        recyclerView.setHasFixedSize(true);
                       // Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(activity, "No Data", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PregnancyDetailRoot> call, Throwable t) {
                Toast.makeText(activity, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }
}