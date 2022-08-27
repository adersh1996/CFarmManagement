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

import com.adarsh.cfarmmanagement.CustomerHomeActivity;
import com.adarsh.cfarmmanagement.LoginActivity;
import com.adarsh.cfarmmanagement.R;
import com.adarsh.cfarmmanagement.Retrofit.Api;
import com.adarsh.cfarmmanagement.Retrofit.ApiClient;
import com.adarsh.cfarmmanagement.model.CustomerViewProfileRoot;
import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerProfileFragment extends Fragment {

    public CustomerHomeActivity activity;

    private ImageView userCoverImage;
    private TextView profileUsername;
    private TextView userSubscriptionStatus;
    private LinearLayout profilePersonalDetails;
    private TextView educationTxt;
    private TextView docExperience;
    private TextView mailIdProfile;
    private TextView mobileNumberProfile;
    private LinearLayout loginBtnLayout;
    private TextView addCattleBt;
    private CircleImageView profileImage;
    LinearLayout editProfileBtnLayout;

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        this.activity = (CustomerHomeActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_profile, container, false);

        initView(view);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("customer_pref", Context.MODE_PRIVATE);
        String customerId=sharedPreferences.getString("customerId","");

       // Toast.makeText(getContext(), customerId, Toast.LENGTH_SHORT).show();

        Api api = ApiClient.cattleFarm().create(Api.class);
        api.CUSTOMER_VIEW_PROFILE_ROOT_CALL(customerId).enqueue(new Callback<CustomerViewProfileRoot>() {
            @Override
            public void onResponse(Call<CustomerViewProfileRoot> call, Response<CustomerViewProfileRoot> response) {
                if (response.isSuccessful()){
                    CustomerViewProfileRoot customerViewProfileRoot= response.body();
                    if (customerViewProfileRoot.status){
                        profileUsername.setText(customerViewProfileRoot.customer_details.get(0).name);
                        educationTxt.setText(customerViewProfileRoot.customer_details.get(0).address);
                        docExperience.setText(customerViewProfileRoot.customer_details.get(0).district);
                        mailIdProfile.setText(customerViewProfileRoot.customer_details.get(0).email);
                        mobileNumberProfile.setText(customerViewProfileRoot.customer_details.get(0).phone);
                        Glide.with(activity).load(customerViewProfileRoot.customer_details.get(0).image).into(profileImage);
                        Glide.with(activity).load(customerViewProfileRoot.customer_details.get(0).image).into(userCoverImage);

                        SharedPreferences sP = getContext().getSharedPreferences("customer_profile", MODE_PRIVATE);
                        SharedPreferences.Editor myEditor=sP.edit();
                        myEditor.putString("customerId",customerViewProfileRoot.customer_details.get(0).id);
                        myEditor.putString("name",customerViewProfileRoot.customer_details.get(0).name);
                        myEditor.putString("mailId",customerViewProfileRoot.customer_details.get(0).email);
                        myEditor.putString("phone",customerViewProfileRoot.customer_details.get(0).phone);
                        myEditor.putString("address",customerViewProfileRoot.customer_details.get(0).address);
                        myEditor.putString("state",customerViewProfileRoot.customer_details.get(0).state);
                        myEditor.putString("district",customerViewProfileRoot.customer_details.get(0).district);
                        myEditor.putString("proPic",customerViewProfileRoot.customer_details.get(0).image);
                        myEditor.commit();


                    }
                }
            }

            @Override
            public void onFailure(Call<CustomerViewProfileRoot> call, Throwable t) {

            }
        });

        loginBtnLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sP = activity.getSharedPreferences("login_pref",MODE_PRIVATE);
                SharedPreferences.Editor speditor = sP.edit();
                speditor.putBoolean("sesson",false);
                speditor.putString("role","");
                speditor.commit();

                Intent intent =new Intent(getActivity(), LoginActivity.class);
                Toast.makeText(activity, "LoggedOut Successfully", Toast.LENGTH_SHORT).show();
                startActivity(intent);

            }
        });

        editProfileBtnLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditCustomerProfileFragment editCustomerProfileFragment=new EditCustomerProfileFragment();
                Bundle arguments = new Bundle();
                editCustomerProfileFragment.setArguments(arguments);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.customer_home_container, editCustomerProfileFragment, "editCustomerProfileFragment").addToBackStack(null).commit();

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
        docExperience = view.findViewById(R.id.doc_experience);
        mailIdProfile = view.findViewById(R.id.mail_id_profile);
        mobileNumberProfile = view.findViewById(R.id.mobile_number_profile);
        loginBtnLayout = view.findViewById(R.id.login_btn_layout);
        addCattleBt = view.findViewById(R.id.add_cattle_bt);
        profileImage = view.findViewById(R.id.profile_image);
        editProfileBtnLayout=view.findViewById(R.id.edit_profile_bt_layout);
    }
}