package com.adarsh.cfarmmanagement.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.adarsh.cfarmmanagement.DoctorHomeActivity;
import com.adarsh.cfarmmanagement.LoginActivity;
import com.adarsh.cfarmmanagement.R;
import com.adarsh.cfarmmanagement.Retrofit.Api;
import com.adarsh.cfarmmanagement.Retrofit.ApiClient;
import com.adarsh.cfarmmanagement.model.DoctorLogOutRoot;
import com.adarsh.cfarmmanagement.model.DoctorProfileRoot;
import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DoctorProfileFragment extends Fragment {


    public DoctorHomeActivity activity;
    private ImageView userCoverImage;
    private TextView profileUsername;
    private TextView userSubscriptionStatus;
    private LinearLayout profilePersonalDetails;
    private TextView educationTxt;
    private TextView docExperience;
    private TextView mailIdProfile;
    private TextView mobileNumberProfile;
    private LinearLayout loginBtnLayout;
    private LinearLayout editProfileBtnLayout;
    private TextView addCattleBt;
    private CircleImageView profileImage;

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        this.activity = (DoctorHomeActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctor_profile, container, false);

        initView(view);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("doctor_pref", Context.MODE_PRIVATE);
        String docId = sharedPreferences.getString("docId", "");
       // Toast.makeText(getActivity(), docId, Toast.LENGTH_SHORT).show();

        Api api = ApiClient.cattleFarm().create(Api.class);
        api.DOCTOR_PROFILE_ROOT_CALL(docId).enqueue(new Callback<DoctorProfileRoot>() {
            @Override
            public void onResponse(Call<DoctorProfileRoot> call, Response<DoctorProfileRoot> response) {
                if (response.isSuccessful()) {
                    DoctorProfileRoot doctorProfileRoot = response.body();
                    if (doctorProfileRoot.status) {
                        profileUsername.setText(doctorProfileRoot.doctorDetails.get(0).name);
                        educationTxt.setText(doctorProfileRoot.doctorDetails.get(0).qualification);
                        docExperience.setText(doctorProfileRoot.doctorDetails.get(0).experience);
                        mailIdProfile.setText(doctorProfileRoot.doctorDetails.get(0).mail);
                        mobileNumberProfile.setText(doctorProfileRoot.doctorDetails.get(0).phone);
                        Glide.with(activity).load(doctorProfileRoot.doctorDetails.get(0).profile).into(profileImage);
                        Glide.with(activity).load(doctorProfileRoot.doctorDetails.get(0).profile).into(userCoverImage);


                        SharedPreferences sP = getContext().getSharedPreferences("doctor_profile", MODE_PRIVATE);
                        SharedPreferences.Editor myEditor=sP.edit();
                        myEditor.putString("docId",doctorProfileRoot.doctorDetails.get(0).doc_id);
                        myEditor.putString("name",doctorProfileRoot.doctorDetails.get(0).name);
                        myEditor.putString("mailId",doctorProfileRoot.doctorDetails.get(0).mail);
                        myEditor.putString("phone",doctorProfileRoot.doctorDetails.get(0).phone);
                        myEditor.putString("address",doctorProfileRoot.doctorDetails.get(0).address);
                        myEditor.putString("qualification",doctorProfileRoot.doctorDetails.get(0).qualification);
                        myEditor.putString("experience",doctorProfileRoot.doctorDetails.get(0).experience);
                        myEditor.putString("proPic",doctorProfileRoot.doctorDetails.get(0).profile);
                        myEditor.putString("licensePic",doctorProfileRoot.doctorDetails.get(0).document);
                        myEditor.commit();



                    }
                }
            }

            @Override
            public void onFailure(Call<DoctorProfileRoot> call, Throwable t) {

            }
        });

        loginBtnLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                api.DOCTOR_LOG_OUT_ROOT_CALL(docId).enqueue(new Callback<DoctorLogOutRoot>() {
                    @Override
                    public void onResponse(Call<DoctorLogOutRoot> call, Response<DoctorLogOutRoot> response) {
                    if (response.isSuccessful()){
                        DoctorLogOutRoot doctorLogOutRoot= response.body();
                        if (doctorLogOutRoot.status){
                            Toast.makeText(activity, "LoggedOut Successfully", Toast.LENGTH_SHORT).show();

                            SharedPreferences sP = getContext().getSharedPreferences("login_pref",MODE_PRIVATE);
                            SharedPreferences.Editor speditor = sP.edit();
                            speditor.putBoolean("sesson",false);
                            speditor.putString("role","");
                            speditor.commit();

                            Intent intent =new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);
                        }
                    }else {
                        Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
                    }
                    }

                    @Override
                    public void onFailure(Call<DoctorLogOutRoot> call, Throwable t) {
                        Toast.makeText(activity, "Error"+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        editProfileBtnLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditDoctorProfileFragment editDoctorProfileFragment=new EditDoctorProfileFragment();
                Bundle arguments = new Bundle();
                editDoctorProfileFragment.setArguments(arguments);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.doctor_home_container, editDoctorProfileFragment, "editDoctorProfileFragment").addToBackStack(null).commit();

            }
        });


        return view;
    }

    private void initView(View view) {
        userCoverImage = view.findViewById(R.id.user_cover_image);
        profileUsername = view.findViewById(R.id.profile_username);
        userSubscriptionStatus = view.findViewById(R.id.user_subscription_status);
        profilePersonalDetails = view.findViewById(R.id.profile_personal_details);
        educationTxt = view.findViewById(R.id.education_txt);
        mailIdProfile = view.findViewById(R.id.mail_id_profile);
        mobileNumberProfile = view.findViewById(R.id.mobile_number_profile);
        loginBtnLayout = view.findViewById(R.id.login_btn_layout);
        addCattleBt = view.findViewById(R.id.add_cattle_bt);
        profileImage = view.findViewById(R.id.profile_image);
        docExperience = view.findViewById(R.id.doc_experience);
        editProfileBtnLayout=view.findViewById(R.id.edit_profile_bt_layout);
    }
}