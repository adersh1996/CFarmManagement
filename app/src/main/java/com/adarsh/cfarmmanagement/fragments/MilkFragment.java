package com.adarsh.cfarmmanagement.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.adarsh.cfarmmanagement.HomeActivity;
import com.adarsh.cfarmmanagement.R;
import com.adarsh.cfarmmanagement.Retrofit.Api;
import com.adarsh.cfarmmanagement.Retrofit.ApiClient;
import com.adarsh.cfarmmanagement.model.MilkStatusMonthlyRoot;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.mdgiitr.suyash.graphkit.DataPoint;
import com.mdgiitr.suyash.graphkit.PieChart;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MilkFragment extends Fragment {

    public HomeActivity activity;
    ExtendedFloatingActionButton addMilkBt;
    PieChart pieChart;
    TextView totalProducedMilk,detailedMilkReportTv;


    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        this.activity = (HomeActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_milk, container, false);

        pieChart = root.findViewById(R.id.grid_pie);
        totalProducedMilk=root.findViewById(R.id.total_produced_milk);
        addMilkBt=root.findViewById(R.id.add_milk_float_bt);
        detailedMilkReportTv=root.findViewById(R.id.milk_det_report_tv);

        Calendar today = Calendar.getInstance();
        int imonth = today.get(Calendar.MONTH)+1;

        //api call

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("cattle_pref", Context.MODE_PRIVATE);
        String farm_id = sharedPreferences.getString("farm_id", "");
        String month=String.valueOf(imonth);

        Api api = ApiClient.cattleFarm().create(Api.class);
        api.MILK_STATUS_MONTHLY_ROOT_CALL(month,farm_id).enqueue(new Callback<MilkStatusMonthlyRoot>() {
            @Override
            public void onResponse(Call<MilkStatusMonthlyRoot> call, Response<MilkStatusMonthlyRoot> response) {
                if (response.isSuccessful()){
                    MilkStatusMonthlyRoot milkStatusMonthlyRoot=response.body();
                    if (milkStatusMonthlyRoot.getStatus()){

                        float spoiled=(float) milkStatusMonthlyRoot.getMilk_details().get(0).getSpoiled();
                        float used=(float)(milkStatusMonthlyRoot.getMilk_details().get(0).getProduced())-spoiled;

                        ArrayList<DataPoint> points = new ArrayList<>();

                        points.add(new DataPoint("Milk Used", used, Color.parseColor("#228b22")));
                        points.add(new DataPoint("Spoiled", spoiled, Color.parseColor("#d2042d")));
                        pieChart.setPoints(points);

                        totalProducedMilk.setText(String.valueOf(milkStatusMonthlyRoot.getMilk_details().get(0).getProduced()));


                    }else {
                        Toast.makeText(activity, "No Data Available", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<MilkStatusMonthlyRoot> call, Throwable t) {
                Toast.makeText(activity, "Server Error"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });





//        PieChart pieChart = new PieChart(getActivity(),700,700); //Pass view width and view height as parameters
//        layout.addView(pieChart);


//        points.add(new DataPoint("Basketball", (float)15.8,Color.parseColor("#2ECC71")));
//        points.add(new DataPoint("Voleyball",(float)12.4,Color.parseColor("#F5B041")));


        detailedMilkReportTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailedMilkReportFragment detailedMilkReportFragment=new DetailedMilkReportFragment();
                Bundle arguments = new Bundle();
                detailedMilkReportFragment.setArguments(arguments);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, detailedMilkReportFragment, "OptionsFragment").addToBackStack(null).commit();
            }
        });


        addMilkBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddMilkFragment addMilkFragment=new AddMilkFragment();
                Bundle arguments = new Bundle();
                addMilkFragment.setArguments(arguments);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, addMilkFragment, "OptionsFragment").addToBackStack(null).commit();
            }
        });


        return root;
    }
}