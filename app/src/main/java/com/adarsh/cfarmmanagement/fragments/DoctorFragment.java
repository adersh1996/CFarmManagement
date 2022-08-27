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
import com.adarsh.cfarmmanagement.adapters.ProductTypeAdapter;
import com.adarsh.cfarmmanagement.adapters.VetenaryDoctorAdapter;
import com.adarsh.cfarmmanagement.model.DoctorsListRoot;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DoctorFragment extends Fragment {

    public HomeActivity activity;

    RecyclerView docRecyclerView;
    String []drName={"Dr.Anil Kumar","Dr.Sumalatha","Dr.John Kaattuparambil","Dr.Sindu","Dr.Aravindakshan"};
    int[] doctorImages={R.drawable.dfranilkumar,R.drawable.sumalatha,R.drawable.john,R.drawable.sindu,R.drawable.aravindakshan};


    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        this.activity = (HomeActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root=inflater.inflate(R.layout.fragment_doctor, container, false);
        docRecyclerView=root.findViewById(R.id.doc_rv);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("cattle_pref", Context.MODE_PRIVATE);
        String farm_id = sharedPreferences.getString("farm_id", "");

        Api api = ApiClient.cattleFarm().create(Api.class);
        api.DOCTORS_LIST_ROOT_CALL(farm_id).enqueue(new Callback<DoctorsListRoot>() {
            @Override
            public void onResponse(Call<DoctorsListRoot> call, Response<DoctorsListRoot> response) {
                if (response.isSuccessful()){
                    DoctorsListRoot doctorsListRoot= response.body();
                    if (doctorsListRoot.status){
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);
                        docRecyclerView.setLayoutManager(linearLayoutManager);
                        VetenaryDoctorAdapter vetenaryDoctorAdapter=new VetenaryDoctorAdapter(doctorsListRoot,getActivity());
                        docRecyclerView.setAdapter(vetenaryDoctorAdapter);
                    }else {
                        Toast.makeText(activity, "No Doctors Available", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<DoctorsListRoot> call, Throwable t) {
                Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }
}