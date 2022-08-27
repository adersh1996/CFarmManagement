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

import com.adarsh.cfarmmanagement.DoctorHomeActivity;
import com.adarsh.cfarmmanagement.HomeActivity;
import com.adarsh.cfarmmanagement.R;
import com.adarsh.cfarmmanagement.Retrofit.Api;
import com.adarsh.cfarmmanagement.Retrofit.ApiClient;
import com.adarsh.cfarmmanagement.adapters.CattleListAdapter;
import com.adarsh.cfarmmanagement.model.ViewCattleRoot;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CattleFragment extends Fragment {

    public HomeActivity activity;
    ExtendedFloatingActionButton addCattleButton;
    RecyclerView cattleRecyclerView;

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        this.activity = (HomeActivity) activity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_cattle, container, false);
        addCattleButton = root.findViewById(R.id.add_cattle);
        cattleRecyclerView = root.findViewById(R.id.cattle_recycler);
        addCattleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddCattleFragment addCattleFragment = new AddCattleFragment();
                Bundle arguments = new Bundle();
//                 arguments.putString("title",bookRootModel.getBook().get(position).getTitle());
//                 arguments.putString( "url" ,bookRootModel.getBook().get(position).getPhoto());
                addCattleFragment.setArguments(arguments);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, addCattleFragment, "OptionsFragment").addToBackStack(null).commit();

            }
        });

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("cattle_pref", Context.MODE_PRIVATE);
        String farm_id = sharedPreferences.getString("farm_id", "");

        Api api = ApiClient.cattleFarm().create(Api.class);
        api.VIEW_CATTLE_ROOT_CALL(farm_id).enqueue(new Callback<ViewCattleRoot>() {
            @Override
            public void onResponse(Call<ViewCattleRoot> call, Response<ViewCattleRoot> response) {
                if (response.isSuccessful()) {
                    ViewCattleRoot viewCattleRoot = response.body();
                    if (viewCattleRoot.getStatus().equals("success")) {
                        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);
                        cattleRecyclerView.setLayoutManager(layoutManager);
                        CattleListAdapter cattleListAdapter = new CattleListAdapter(viewCattleRoot, getActivity());
                        cattleRecyclerView.setAdapter(cattleListAdapter);
                    } else {
                        Toast.makeText(activity, "No Cattle Added", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ViewCattleRoot> call, Throwable t) {

                Toast.makeText(activity, "Server Error" + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


        return root;
    }
}