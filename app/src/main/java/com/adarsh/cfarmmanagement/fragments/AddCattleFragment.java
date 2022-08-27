package com.adarsh.cfarmmanagement.fragments;

import static com.adarsh.cfarmmanagement.Utils.showDatePickerDialog;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.adarsh.cfarmmanagement.HomeActivity;
import com.adarsh.cfarmmanagement.R;
import com.adarsh.cfarmmanagement.Retrofit.Api;
import com.adarsh.cfarmmanagement.Retrofit.ApiClient;
import com.adarsh.cfarmmanagement.model.AddCattle;
import com.adarsh.cfarmmanagement.model.TagId;
import com.adarsh.cfarmmanagement.model.TagIdRoot;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddCattleFragment extends Fragment {

    public HomeActivity activity;

    TextInputEditText tagNo, breed, dob, weight;
    TextView addCattleBt;
    AutoCompleteTextView cattleStatus, parentTag;
    MaterialAutoCompleteTextView source;
    Button statusDelete, parentTagDelete;
    RadioGroup genderGroup;
    String status, gender, sParentTagNumber;

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        this.activity = (HomeActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_cattle, container, false);

        tagNo = view.findViewById(R.id.add_cattle_tag_no);
        breed = view.findViewById(R.id.add_cattle_breed);
        dob = view.findViewById(R.id.add_cattle_dob);
        weight = view.findViewById(R.id.add_cattle_weight);
        addCattleBt = view.findViewById(R.id.add_cattle_bt);
        cattleStatus = view.findViewById(R.id.add_cattle_status);
        statusDelete = view.findViewById(R.id.add_cattle_status_del);
        genderGroup = view.findViewById(R.id.add_cattle_gender_group);
        source = view.findViewById(R.id.add_cattle_source);

        parentTag = view.findViewById(R.id.add_parent_tag);
        parentTagDelete = view.findViewById(R.id.add_parent_tag_del);

        genderGroup.check(R.id.add_cattle_gender_female);
        gender = "female";


        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(getContext(), dob);
            }
        });

        genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.add_cattle_gender_male:
                        gender = "male";
                        break;
                    case R.id.add_cattle_gender_female:
                        gender = "female";
                        break;
                }
            }
        });

        //source spinner

        final ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_list_item_1, getResources()
                .getStringArray(R.array.source));
        source.setAdapter(myArrayAdapter);
        source.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                source.showDropDown();
            }
        });


        //parent Tag spinner

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("cattle_pref", Context.MODE_PRIVATE);
        String farm_id = sharedPreferences.getString("farm_id", "");

        Api api = ApiClient.cattleFarm().create(Api.class);
        api.TAG_ID_ROOT_CALL(farm_id).enqueue(new Callback<TagIdRoot>() {
            @Override
            public void onResponse(Call<TagIdRoot> call, Response<TagIdRoot> response) {
                if (response.isSuccessful()) {
                    TagIdRoot tagIdRoot = response.body();
                    if (tagIdRoot.Details != null && tagIdRoot.Details.size() > 0) {
                        String[] parentTagNumber = new String[tagIdRoot.Details.size()];

                        for (int i = 0; i < tagIdRoot.Details.size(); i++) {
                            parentTagNumber[i] = tagIdRoot.Details.get(i).tag_id;

                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                                    getActivity(), android.R.layout.simple_list_item_1, parentTagNumber);
                            parentTag.setAdapter(arrayAdapter);
                            parentTag.setCursorVisible(false);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<TagIdRoot> call, Throwable t) {

            }
        });

        parentTag.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                parentTag.showDropDown();
                sParentTagNumber = (String) adapterView.getItemAtPosition(i);
                parentTagDelete.setAlpha(.8f);
            }
        });
        parentTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parentTag.showDropDown();
            }
        });
        parentTagDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parentTag.setText(null);
                parentTagDelete.setAlpha(.2f);
                sParentTagNumber = null;
            }
        });


////////////////////
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_list_item_1, getResources()
                .getStringArray(R.array.cattle_status));

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

        addCattleBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("cattle_pref", Context.MODE_PRIVATE);
                String farm_id = sharedPreferences.getString("farm_id", "");

                if (tagNo.getText().toString().isEmpty()) {
                    tagNo.setError("mandatory");
                } else if (breed.getText().toString().isEmpty()) {
                    breed.setError("mandatory");
                } else if (dob.getText().toString().isEmpty()) {
                    dob.setError("mandatory");
                } else if (weight.getText().toString().isEmpty()) {
                    weight.setError("mandatory");
                } else if (cattleStatus.getText().toString().isEmpty()) {
                    cattleStatus.setError("mandatory");
                } else if (source.getText().toString().isEmpty()) {
                    source.setError("mandatory");
                } else {
                    String sTagNo = tagNo.getText().toString();
                    String sBreed = breed.getText().toString();
                    String sDob = dob.getText().toString();
                    String sWeight = weight.getText().toString();
                    String sCattleStatus = cattleStatus.getText().toString();
                    String sSource = source.getText().toString();

                   // Toast.makeText(activity, ""+sParentTagNumber, Toast.LENGTH_SHORT).show();
                    
                    Api api = ApiClient.cattleFarm().create(Api.class);
                    api.ADD_CATTLE_CALL(sTagNo, farm_id, gender, sBreed, sDob, sWeight, sCattleStatus, sParentTagNumber, sSource).enqueue(new Callback<AddCattle>() {
                        @Override
                        public void onResponse(Call<AddCattle> call, Response<AddCattle> response) {
                            if (response.isSuccessful()) {
                                AddCattle addCattle = response.body();
                                if (addCattle.status.equals("success")) {
                                    Toast.makeText(activity, "Added Successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(activity, "Failed to add Cattle", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<AddCattle> call, Throwable t) {
                            Toast.makeText(activity, "Server Error" + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });

        return view;
    }
}