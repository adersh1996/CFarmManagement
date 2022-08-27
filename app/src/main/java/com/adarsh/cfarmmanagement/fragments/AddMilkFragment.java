package com.adarsh.cfarmmanagement.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.adarsh.cfarmmanagement.HomeActivity;
import com.adarsh.cfarmmanagement.R;
import com.adarsh.cfarmmanagement.Retrofit.Api;
import com.adarsh.cfarmmanagement.Retrofit.ApiClient;
import com.adarsh.cfarmmanagement.model.AddMilk;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddMilkFragment extends Fragment {

    public HomeActivity activity;
    TextInputEditText date, totalProduced, totalSpoiled;
    TextView addMilkBt;
    Calendar myCalendar = Calendar.getInstance();


    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        this.activity = (HomeActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_milk, container, false);

        date = view.findViewById(R.id.add_milk_date);
        totalProduced = view.findViewById(R.id.add_milk_produced);
        totalSpoiled = view.findViewById(R.id.add_milk_spoiled);
        addMilkBt = view.findViewById(R.id.add_milk_bt);

        DatePickerDialog.OnDateSetListener datePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                myCalendar.set(Calendar.YEAR, i);
                myCalendar.set(Calendar.MONTH, i1);
                myCalendar.set(Calendar.DAY_OF_MONTH, i2);
                updateLabel();
            }
        };

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(), datePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        addMilkBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sharedPreferences = getContext().getSharedPreferences("cattle_pref", Context.MODE_PRIVATE);
                String farm_id = sharedPreferences.getString("farm_id", "");

                if (date.getText().toString().isEmpty()) {
                    date.setError("mandatory");
                } else if (totalProduced.getText().toString().isEmpty()) {
                    totalProduced.setError("mandatory");
                } else if (totalSpoiled.getText().toString().isEmpty()) {
                    totalSpoiled.setError("mandatory");
                } else {
                    String sDate = date.getText().toString();
                    String sProduced = totalProduced.getText().toString();
                    String sSpoiled = totalSpoiled.getText().toString();


                    Api api = ApiClient.cattleFarm().create(Api.class);
                    api.ADD_MILK_CALL(farm_id, sDate, sProduced, sSpoiled).enqueue(new Callback<AddMilk>() {
                        @Override
                        public void onResponse(Call<AddMilk> call, Response<AddMilk> response) {
                            if (response.isSuccessful()) {
                                AddMilk addMilk = response.body();
                                if (addMilk.status.equals("success")) {
                                    Toast.makeText(activity, "Added Successfully", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(activity, "Failed to Add Details", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(activity, "ServerError", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<AddMilk> call, Throwable t) {
                            Toast.makeText(activity, "ServerError" + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });


        return view;
    }

    private void updateLabel() {
        String myFormat = "yyyy/MM/dd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        date.setText(dateFormat.format(myCalendar.getTime()));
    }
}