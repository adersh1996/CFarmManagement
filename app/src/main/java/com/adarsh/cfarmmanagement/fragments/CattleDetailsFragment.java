package com.adarsh.cfarmmanagement.fragments;

import static com.adarsh.cfarmmanagement.Utils.showDatePickerDialog;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adarsh.cfarmmanagement.HomeActivity;
import com.adarsh.cfarmmanagement.R;
import com.adarsh.cfarmmanagement.Retrofit.Api;
import com.adarsh.cfarmmanagement.Retrofit.ApiClient;
import com.adarsh.cfarmmanagement.adapters.BreedingHistoryAdapter;
import com.adarsh.cfarmmanagement.adapters.VaccineHistoryAdapter;
import com.adarsh.cfarmmanagement.model.AddPregnancyDetailsRoot;
import com.adarsh.cfarmmanagement.model.BreedingHistoryRoot;
import com.adarsh.cfarmmanagement.model.EditCattleDetailsRoot;
import com.adarsh.cfarmmanagement.model.TakenVaccineRoot;
import com.adarsh.cfarmmanagement.model.VaccinationHistoryRoot;
import com.adarsh.cfarmmanagement.model.ViewCattleDetailsRoot;
import com.adarsh.cfarmmanagement.model.ViewVaccineRoot;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CattleDetailsFragment extends Fragment {

    public HomeActivity activity;

    TextInputEditText tagNo, breed, dOB, weight, parentTagNo, vaccineDate, breedinDate;
    TextView editCattleBt, submitBt, addTakenVaccineBt, addBreedingBt, pregnancyCalcBt, pregnancyHistoryTv, addPregnancyDetailsBt, vaccinationHistoryTv;
    AutoCompleteTextView cattleStatus, selectVaccine, source;
    Button statusDelete, selectVaccineDelBt;
    RadioGroup genderGroup;
    String status, vaccineName;
    RecyclerView vaccinationHistoryRV;
    Calendar myCalendar = Calendar.getInstance();
    String age, dueDate, gender;
    LinearLayout pregnancyLayout;
    TextInputLayout parentTagTil;

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        this.activity = (HomeActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cattle_details, container, false);

        tagNo = view.findViewById(R.id.view_cattle_tag_no);
        breed = view.findViewById(R.id.view_cattle_breed);
        dOB = view.findViewById(R.id.view_cattle_dob);
        weight = view.findViewById(R.id.view_cattle_weight);
        parentTagNo = view.findViewById(R.id.view_cattle_parent_tag_no);
        source = view.findViewById(R.id.view_cattle_source);
        editCattleBt = view.findViewById(R.id.edit_cattle_bt);
        submitBt = view.findViewById(R.id.edit_submit_cattle_bt);
        cattleStatus = view.findViewById(R.id.view_cattle_status);
        statusDelete = view.findViewById(R.id.view_cattle_status_del);
        genderGroup = view.findViewById(R.id.view_cattle_gender_group);
        vaccinationHistoryRV = view.findViewById(R.id.vaccine_history_recycler);
        selectVaccine = view.findViewById(R.id.select_vaccine);
        selectVaccineDelBt = view.findViewById(R.id.select_vaccine_del);
        vaccineDate = view.findViewById(R.id.vaccine_date);
        addTakenVaccineBt = view.findViewById(R.id.add_taken_vaccine_bt);
        pregnancyCalcBt = view.findViewById(R.id.pregnancy_calc_bt);
        pregnancyHistoryTv = view.findViewById(R.id.pregnancy_history_tv);
        addPregnancyDetailsBt = view.findViewById(R.id.add_pregnancy_details_bt);
        vaccinationHistoryTv = view.findViewById(R.id.vaccination_history_tv);
        pregnancyLayout = view.findViewById(R.id.pregnancy_layout);
        parentTagTil=view.findViewById(R.id.parent_tag_til);


        tagNo.setEnabled(false);
        breed.setEnabled(false);
        dOB.setEnabled(false);
        weight.setEnabled(false);
        parentTagNo.setEnabled(false);
        source.setEnabled(false);
        cattleStatus.setEnabled(false);
        statusDelete.setEnabled(false);
        for (int i = 0; i < genderGroup.getChildCount(); i++) {
            genderGroup.getChildAt(i).setEnabled(false);
        }

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("cattle_pref", Context.MODE_PRIVATE);
        String sfarm_id = sharedPreferences.getString("farm_id", "");

        String scattleId = getArguments().getString("cattleId");

        //Toast.makeText(getActivity(), scattleId, Toast.LENGTH_SHORT).show();

        Api api = ApiClient.cattleFarm().create(Api.class);
        api.VIEW_CATTLE_DETAILS_ROOT_CALL(scattleId, sfarm_id).enqueue(new Callback<ViewCattleDetailsRoot>() {
            @Override
            public void onResponse(Call<ViewCattleDetailsRoot> call, Response<ViewCattleDetailsRoot> response) {
                if (response.isSuccessful()) {
                    ViewCattleDetailsRoot viewCattleDetailsRoot = response.body();
                    if (viewCattleDetailsRoot.status.equals("success")) {

                        tagNo.setText(viewCattleDetailsRoot.Details.get(0).tag_id);
                        breed.setText(viewCattleDetailsRoot.Details.get(0).breed);
                        dOB.setText(viewCattleDetailsRoot.Details.get(0).dob);
                        weight.setText(viewCattleDetailsRoot.Details.get(0).weight);

                        try {
                            if (viewCattleDetailsRoot.Details.get(0).source.equals("External")) {
                                parentTagTil.setVisibility(View.GONE);
                                parentTagNo.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {

                        }

                        parentTagNo.setText(viewCattleDetailsRoot.Details.get(0).parent_tag);
                        source.setText(viewCattleDetailsRoot.Details.get(0).source);
                        cattleStatus.setText(viewCattleDetailsRoot.Details.get(0).status);
                        if (viewCattleDetailsRoot.Details.get(0).gender.equals("female")) {
                            genderGroup.check(R.id.view_cattle_gender_female);
                        }
                        if (viewCattleDetailsRoot.Details.get(0).gender.equals("male")) {
                            genderGroup.check(R.id.view_cattle_gender_male);
                            pregnancyLayout.setVisibility(View.GONE);
                        }


                    }
                } else {
                    Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ViewCattleDetailsRoot> call, Throwable t) {
                Toast.makeText(activity, "Failed" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.view_cattle_gender_male:
                        gender = "male";
                        break;
                    case R.id.view_cattle_gender_female:
                        gender = "female";
                        break;
                }
            }
        });


        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_list_item_1, getResources()
                .getStringArray(R.array.edit_cattle_status));

        cattleStatus.setAdapter(arrayAdapter);
        cattleStatus.setCursorVisible(false);
        cattleStatus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                cattleStatus.showDropDown();
                status = (String) adapterView.getItemAtPosition(i);
                statusDelete.setAlpha(.8f);
            }
        });
        cattleStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  cattleStatus.setText(null);
                cattleStatus.showDropDown();
            }
        });
        statusDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cattleStatus.setText(null);
                statusDelete.setAlpha(.2f);
                status = null;
            }
        });


        final ArrayAdapter<String> arrayAdapter_one = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_list_item_1, getResources()
                .getStringArray(R.array.source));

        source.setAdapter(arrayAdapter_one);
        source.setCursorVisible(false);
        source.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                source.showDropDown();
            }
        });
        source.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                source.showDropDown();
            }
        });


        editCattleBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitBt.setVisibility(View.VISIBLE);
                editCattleBt.setVisibility(View.INVISIBLE);

                breed.setEnabled(true);
                dOB.setEnabled(true);
                weight.setEnabled(true);
                cattleStatus.setEnabled(true);
                statusDelete.setEnabled(true);
                parentTagNo.setEnabled(true);
                source.setEnabled(true);
                for (int i = 0; i < genderGroup.getChildCount(); i++) {
                    genderGroup.getChildAt(i).setEnabled(true);
                }
            }
        });


        submitBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (breed.getText().toString().isEmpty()) {
                    breed.setError("mandatory");
                } else if (dOB.getText().toString().isEmpty()) {
                    dOB.setError("mandatory");
                } else if (weight.getText().toString().isEmpty()) {
                    weight.setError("mandatory");
                } else if (cattleStatus.getText().toString().isEmpty()) {
                    cattleStatus.setError("mandatory");
                } else if (source.getText().toString().isEmpty()) {
                    source.setError("mandatory");
                } else {

                    String sBreed = breed.getText().toString();
                    String sDOB = dOB.getText().toString();
                    String sWeight = weight.getText().toString();
                    String sStatus = cattleStatus.getText().toString();
                    String sParentTagNo = parentTagNo.getText().toString();
                    String sSource = source.getText().toString();
                    String sTagId = tagNo.getText().toString();
                    String sGender = gender;

                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("cattle_pref", Context.MODE_PRIVATE);
                    String ssfarm_id = sharedPreferences.getString("farm_id", "");

                    String sscattleId = getArguments().getString("cattleId");

                    api.EDIT_CATTLE_DETAILS_ROOT_CALL(sGender, sBreed, sDOB, sWeight, sStatus, sParentTagNo, sSource, sscattleId, sTagId, ssfarm_id).enqueue(new Callback<EditCattleDetailsRoot>() {
                        @Override
                        public void onResponse(Call<EditCattleDetailsRoot> call, Response<EditCattleDetailsRoot> response) {
                            if (response.isSuccessful()) {
                                EditCattleDetailsRoot editCattleDetailsRoot = response.body();
                                if (editCattleDetailsRoot.status) {
                                    Toast.makeText(activity, "Updated Successfully", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<EditCattleDetailsRoot> call, Throwable t) {
                            Toast.makeText(activity, "Error" + t.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            }
        });

        //vaccineList Spinner

        api.VIEW_VACCINE_ROOT_CALL().enqueue(new Callback<ViewVaccineRoot>() {
            @Override
            public void onResponse(Call<ViewVaccineRoot> call, Response<ViewVaccineRoot> response) {
                if (response.isSuccessful()) {
                    ViewVaccineRoot viewVaccineRoot = response.body();
                    if (viewVaccineRoot.Details != null && viewVaccineRoot.Details.size() > 0) {
                        String[] vaccines = new String[viewVaccineRoot.Details.size()];
                        for (int i = 0; i < viewVaccineRoot.Details.size(); i++) {
                            vaccines[i] = viewVaccineRoot.Details.get(i).disease;

                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                                    getContext(), android.R.layout.simple_list_item_1, vaccines);
                            selectVaccine.setAdapter(arrayAdapter);
                            selectVaccine.setCursorVisible(false);
                        }

                    }
                } else {
                    Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ViewVaccineRoot> call, Throwable t) {
                Toast.makeText(activity, "Server Error" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        selectVaccine.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectVaccine.showDropDown();
                vaccineName = (String) adapterView.getItemAtPosition(i);
                selectVaccineDelBt.setAlpha(.8f);
            }
        });
        selectVaccine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectVaccine.showDropDown();
            }
        });
        selectVaccineDelBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectVaccine.setText(null);
                selectVaccineDelBt.setAlpha(.2f);
                vaccineName = null;
            }
        });

        //vaccine Date picker

        DatePickerDialog.OnDateSetListener datePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                myCalendar.set(Calendar.YEAR, i);
                myCalendar.set(Calendar.MONTH, i1);
                myCalendar.set(Calendar.DAY_OF_MONTH, i2);
                updateLabel();
            }
        };
        vaccineDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // new DatePickerDialog(getContext(), datePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                showDatePickerDialog(getContext(), vaccineDate);
            }
        });

        // add taken vaccine data

        addTakenVaccineBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vaccineDate.getText().toString().isEmpty()) {
                    vaccineDate.setError("mandatory");
                } else if (selectVaccine.getText().toString().isEmpty()) {
                    vaccineDate.setError("mandatory");
                } else {
                    String cattleTagId = tagNo.getText().toString();
                    String sVaccineDate = vaccineDate.getText().toString();
                    //  Toast.makeText(getActivity(), ""+vaccineName, Toast.LENGTH_SHORT).show();
                    api.TAKEN_VACCINE_ROOT_CALL(sfarm_id, scattleId, vaccineName, sVaccineDate, cattleTagId).enqueue(new Callback<TakenVaccineRoot>() {
                        @Override
                        public void onResponse(Call<TakenVaccineRoot> call, Response<TakenVaccineRoot> response) {
                            if (response.isSuccessful()) {
                                TakenVaccineRoot takenVaccineRoot = response.body();
                                if (takenVaccineRoot.status) {

                                    if (takenVaccineRoot.boosterdose.equals("")) {
                                        Toast.makeText(activity, "Vaccine Details Added Successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(activity, "Vaccine Details Added Successfully " + "Booster Dose date is" + takenVaccineRoot.boosterdose, Toast.LENGTH_SHORT).show();
                                    }
                                    // vaccinationHistoryApiCall();
                                }
                            } else {
                                Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<TakenVaccineRoot> call, Throwable t) {
                            Toast.makeText(activity, "Server Error" + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });


        // Pregnancy Calculator
        pregnancyCalcBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Pregnancy Calculator");
                View customlayout = getLayoutInflater().inflate(R.layout.pregnancy_calculator_dialogue, null);
                builder.setView(customlayout);
                builder.setCancelable(false);

                LinearLayout linearLayout = customlayout.findViewById(R.id.dialogue_layout);
                Button calcBt = customlayout.findViewById(R.id.pregnancy_calc_bt);
                TextView expDeliveryTv = customlayout.findViewById(R.id.expected_delivery_tv);
                TextView ageOfPregTv = customlayout.findViewById(R.id.age_ofPregnancy_tv);
                TextInputEditText breedingDateEt = customlayout.findViewById(R.id.pregnancy_calc_et);


                breedingDateEt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Calendar cldr = Calendar.getInstance();
                        int day = cldr.get(Calendar.DAY_OF_MONTH);
                        int month = cldr.get(Calendar.MONTH);
                        int year = cldr.get(Calendar.YEAR);
                        DatePickerDialog picker = new DatePickerDialog(getContext(),
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                        breedingDateEt.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                        age = getAge(year, monthOfYear, dayOfMonth);
                                        dueDate = getDueDate(year, monthOfYear, dayOfMonth);
                                    }
                                }, year, month, day);
                        picker.getDatePicker().setMaxDate(System.currentTimeMillis());
                        picker.show();
                    }
                });

                calcBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (breedingDateEt.getText().toString().isEmpty()) {
                            breedingDateEt.setError("mandatory");
                        } else {
                            linearLayout.setVisibility(View.VISIBLE);
                            ageOfPregTv.setText(age + "Days");
                            expDeliveryTv.setText(dueDate);
                        }
                    }
                });


                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        //pregnancy history
        pregnancyHistoryTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PregnancyHistoryFragment pregnancyHistoryFragment = new PregnancyHistoryFragment();
                Bundle arguments = new Bundle();
                arguments.putString("farmId", sfarm_id);
                arguments.putString("cattleId", scattleId);
                pregnancyHistoryFragment.setArguments(arguments);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, pregnancyHistoryFragment, "pregnancyHistoryFragment").addToBackStack(null).commit();
            }
        });

        //addPregnancyDetails
        addPregnancyDetailsBt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Add Breeding Data");
                View customlayout = getLayoutInflater().inflate(R.layout.pregnancy_data_dialogue_layout, null);
                builder.setView(customlayout);
                AlertDialog dialog = builder.create();

                RelativeLayout cancelBt = customlayout.findViewById(R.id.cancel_bt);
                RelativeLayout addBt = customlayout.findViewById(R.id.add_bt);
                TextInputEditText calfDob = customlayout.findViewById(R.id.calf_dob);
                TextInputEditText cattleTagNo = customlayout.findViewById(R.id.cattle_tag_no);
                TextInputEditText cattleDateOfBreeding = customlayout.findViewById(R.id.cattle_date_of_breeding);

                cattleDateOfBreeding.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDatePickerDialog(getContext(), cattleDateOfBreeding);
                    }
                });

                calfDob.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDatePickerDialog(getContext(), calfDob);
                    }
                });

                cancelBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                addBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (cattleDateOfBreeding.getText().toString().isEmpty()) {
                            cattleDateOfBreeding.setError("mandatory");
                        } else if (cattleTagNo.getText().toString().isEmpty()) {
                            cattleTagNo.setError("mandatory");
                        } else if (calfDob.getText().toString().isEmpty()) {
                            calfDob.setError("mandatory");
                        } else {
                            String dateOfBreeding = cattleDateOfBreeding.getText().toString();
                            String babyTagNo = cattleTagNo.getText().toString();
                            String babyDob = calfDob.getText().toString();

                            api.ADD_PREGNANCY_DETAILS_ROOT_CALL(dateOfBreeding, scattleId, sfarm_id, babyTagNo, babyDob).enqueue(new Callback<AddPregnancyDetailsRoot>() {
                                @Override
                                public void onResponse(Call<AddPregnancyDetailsRoot> call, Response<AddPregnancyDetailsRoot> response) {
                                    if (response.isSuccessful()) {
                                        AddPregnancyDetailsRoot addPregnancyDetailsRoot = response.body();
                                        if (addPregnancyDetailsRoot.status) {
                                            Toast.makeText(activity, "Pregnancy Details Added Successfully", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<AddPregnancyDetailsRoot> call, Throwable t) {

                                }
                            });
                        }
                    }
                });


                dialog.show();

            }
        });

        vaccinationHistoryTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VaccinationHistoryFragment vaccinationHistoryFragment = new VaccinationHistoryFragment();
                Bundle arguments = new Bundle();
                arguments.putString("farmId", sfarm_id);
                arguments.putString("cattleId", scattleId);
                vaccinationHistoryFragment.setArguments(arguments);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, vaccinationHistoryFragment, "vaccinationHistoryFragment").addToBackStack(null).commit();
            }
        });

        return view;
    }

    private String getAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);


        long msDiff = Calendar.getInstance().getTimeInMillis() - dob.getTimeInMillis();
        long daysDiff = TimeUnit.MILLISECONDS.toDays(msDiff);
        String days = String.valueOf(daysDiff);
        return days;

        // return ageS;
    }

    private String getDueDate(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);
        dob.add(Calendar.DATE, 283);
        int iday = dob.get(Calendar.DAY_OF_MONTH);
        int imonth = dob.get(Calendar.MONTH);
        int iyear = dob.get(Calendar.YEAR);

        String ageS = String.valueOf(iday) + "-" + String.valueOf(imonth + 1) + "-" + String.valueOf(iyear);

        return ageS;
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
                        vaccinationHistoryRV.setLayoutManager(layoutManager);
                        VaccineHistoryAdapter vaccineHistoryAdapter = new VaccineHistoryAdapter(getActivity(), vaccinationHistoryRoot);
                        vaccinationHistoryRV.setAdapter(vaccineHistoryAdapter);
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

    public void breedingHistoryApiCall() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("cattle_pref", Context.MODE_PRIVATE);
        String sfarm_id = sharedPreferences.getString("farm_id", "");

        String scattleId = getArguments().getString("cattleId");

        Api api = ApiClient.cattleFarm().create(Api.class);
        api.BREEDING_HISTORY_ROOT_CALL(scattleId, sfarm_id).enqueue(new Callback<BreedingHistoryRoot>() {
            @Override
            public void onResponse(Call<BreedingHistoryRoot> call, Response<BreedingHistoryRoot> response) {
                if (response.isSuccessful()) {
                    BreedingHistoryRoot breedingHistoryRoot = response.body();
                    if (breedingHistoryRoot.status) {
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
                        // breedingHistoryRv.setLayoutManager(layoutManager);
                        BreedingHistoryAdapter breedingHistoryAdapter = new BreedingHistoryAdapter(getActivity(), breedingHistoryRoot);
                        //  breedingHistoryRv.setAdapter(breedingHistoryAdapter);
                    } else {
                        Toast.makeText(activity, "No Breeding History", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BreedingHistoryRoot> call, Throwable t) {
                Toast.makeText(activity, "Server Error" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateLabel() {
        String myFormat = "yyyy/MM/dd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        vaccineDate.setText(dateFormat.format(myCalendar.getTime()));
    }
}