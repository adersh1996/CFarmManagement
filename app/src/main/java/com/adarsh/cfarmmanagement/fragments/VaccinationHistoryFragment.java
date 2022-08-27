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
import com.adarsh.cfarmmanagement.adapters.VaccineHistoryAdapter;
import com.adarsh.cfarmmanagement.model.VaccinationHistoryRoot;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class VaccinationHistoryFragment extends Fragment {
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
        View view = inflater.inflate(R.layout.fragment_vaccination_history, container, false);

        recyclerView=view.findViewById(R.id.vaccine_history_recycler);

        vaccinationHistoryApiCall();

        return view;
    }

    public void vaccinationHistoryApiCall() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("cattle_pref", Context.MODE_PRIVATE);
        String sfarm_id = sharedPreferences.getString("farm_id", "");

        String scattleId = getArguments().getString("cattleId");

        Api api = ApiClient.cattleFarm().create(Api.class);
        api.VACCINATION_HISTORY_ROOT_CALL(sfarm_id, scattleId).enqueue(new Callback<VaccinationHistoryRoot>() {
            @Override
            public void onResponse(Call<VaccinationHistoryRoot> call, Response<VaccinationHistoryRoot> response) {

                if (response.isSuccessful()) {
                    VaccinationHistoryRoot vaccinationHistoryRoot = response.body();
                    if (vaccinationHistoryRoot.status.equals("success")) {
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
                        recyclerView.setLayoutManager(layoutManager);
                        VaccineHistoryAdapter vaccineHistoryAdapter = new VaccineHistoryAdapter(getActivity(), vaccinationHistoryRoot);
                        recyclerView.setAdapter(vaccineHistoryAdapter);
                    } else {
                        Toast.makeText(activity, "No Vaccines Taken", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onFailure(Call<VaccinationHistoryRoot> call, Throwable t) {
                Toast.makeText(activity, "Server Error" + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}