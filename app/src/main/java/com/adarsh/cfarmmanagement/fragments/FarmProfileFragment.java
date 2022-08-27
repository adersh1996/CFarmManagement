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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.adarsh.cfarmmanagement.HomeActivity;
import com.adarsh.cfarmmanagement.LoginActivity;
import com.adarsh.cfarmmanagement.R;
import com.adarsh.cfarmmanagement.Retrofit.Api;
import com.adarsh.cfarmmanagement.Retrofit.ApiClient;
import com.adarsh.cfarmmanagement.model.CustomerViewProfileRoot;
import com.adarsh.cfarmmanagement.model.FarmLogOutRoot;
import com.adarsh.cfarmmanagement.model.FarmProfileRoot;
import com.adarsh.cfarmmanagement.model.HerdStatusRoot;
import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FarmProfileFragment extends Fragment {

    public HomeActivity activity;

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
    private TextView bull;
    private CircleImageView profileImage;
    TextView notificationCount;
    RelativeLayout notificationIconLayout;

    TextView totalCow, adult, milking, dry, calf, dead, sold, currentTotal;

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        this.activity = (HomeActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_farm_profile, container, false);

        totalCow = view.findViewById(R.id.total_cow);
        adult = view.findViewById(R.id.adult);
        milking = view.findViewById(R.id.milking);
        dry = view.findViewById(R.id.dry);
        calf = view.findViewById(R.id.calf);
        dead = view.findViewById(R.id.dead);
        sold = view.findViewById(R.id.sold);
        currentTotal = view.findViewById(R.id.current_total);
        notificationCount=view.findViewById(R.id.notification_count_profile);
        notificationIconLayout=view.findViewById(R.id.notification_icon_layout);
        initView(view);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("cattle_pref", MODE_PRIVATE);
        String farm_id = sharedPreferences.getString("farm_id", "");

        Api api = ApiClient.cattleFarm().create(Api.class);
        api.FARM_PROFILE_ROOT_CALL(farm_id).enqueue(new Callback<FarmProfileRoot>() {
            @Override
            public void onResponse(Call<FarmProfileRoot> call, Response<FarmProfileRoot> response) {
                if (response.isSuccessful()) {
                    FarmProfileRoot farmProfileRoot = response.body();
                    if (farmProfileRoot.status) {
                        profileUsername.setText(farmProfileRoot.farmDetails.get(0).name);
                        educationTxt.setText(farmProfileRoot.farmDetails.get(0).address);
                        docExperience.setText(farmProfileRoot.farmDetails.get(0).district);
                        mailIdProfile.setText(farmProfileRoot.farmDetails.get(0).mailid);
                        mobileNumberProfile.setText(farmProfileRoot.farmDetails.get(0).phone);
                        notificationCount.setVisibility(View.VISIBLE);
                        notificationCount.setText(String.valueOf(farmProfileRoot.farmDetails.get(0).booster_dose_count));
                        Glide.with(activity).load(farmProfileRoot.farmDetails.get(0).profile).into(profileImage);
                        Glide.with(activity).load(farmProfileRoot.farmDetails.get(0).profile).into(userCoverImage);

                        SharedPreferences sP = activity.getSharedPreferences("cattle_profile", MODE_PRIVATE);
                        SharedPreferences.Editor myEditor=sP.edit();
                        myEditor.putString("farmId",farmProfileRoot.farmDetails.get(0).farm_id);
                        myEditor.putString("name",farmProfileRoot.farmDetails.get(0).name);
                        myEditor.putString("mailId",farmProfileRoot.farmDetails.get(0).mailid);
                        myEditor.putString("phone",farmProfileRoot.farmDetails.get(0).phone);
                        myEditor.putString("address",farmProfileRoot.farmDetails.get(0).address);
                        myEditor.putString("state",farmProfileRoot.farmDetails.get(0).states);
                        myEditor.putString("district",farmProfileRoot.farmDetails.get(0).district);
                        myEditor.putString("seller",farmProfileRoot.farmDetails.get(0).seller);
                        myEditor.putString("fssaiNo",farmProfileRoot.farmDetails.get(0).fssai);
                        myEditor.putString("proPic",farmProfileRoot.farmDetails.get(0).profile);
                        myEditor.putString("licensePic",farmProfileRoot.farmDetails.get(0).document);
                        myEditor.commit();



                    }
                }
            }

            @Override
            public void onFailure(Call<FarmProfileRoot> call, Throwable t) {

            }
        });

        api.HERD_STATUS_ROOT_CALL(farm_id).enqueue(new Callback<HerdStatusRoot>() {
            @Override
            public void onResponse(Call<HerdStatusRoot> call, Response<HerdStatusRoot> response) {
                if (response.isSuccessful()) {
                    HerdStatusRoot herdStatusRoot = response.body();
                    if (herdStatusRoot.status.equals("success")) {
                        totalCow.setText(herdStatusRoot.Details.get(0).total_count+"");
                        adult.setText(herdStatusRoot.Details.get(0).adult+"");
                        milking.setText(herdStatusRoot.Details.get(0).milking+"");
                        dry.setText(herdStatusRoot.Details.get(0).dry+"");
                        calf.setText(herdStatusRoot.Details.get(0).calf+"");
                        dead.setText(herdStatusRoot.Details.get(0).dead+"");
                        sold.setText(herdStatusRoot.Details.get(0).sold+"");
                        currentTotal.setText(herdStatusRoot.Details.get(0).current_total+"");
                        bull.setText(herdStatusRoot.Details.get(0).bull+"");
                    }
                } else {
                    Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HerdStatusRoot> call, Throwable t) {
                Toast.makeText(activity, "Error" + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        notificationIconLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FarmNotificationFragment farmNotificationFragment=new FarmNotificationFragment();
                Bundle arguments = new Bundle();
                arguments.putString("farm_id",farm_id);
                farmNotificationFragment.setArguments(arguments);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, farmNotificationFragment, "farmNotificationFragment").addToBackStack(null).commit();
            }
        });

        loginBtnLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                api.FARM_LOG_OUT_ROOT_CALL(farm_id).enqueue(new Callback<FarmLogOutRoot>() {
                    @Override
                    public void onResponse(Call<FarmLogOutRoot> call, Response<FarmLogOutRoot> response) {
                        if (response.isSuccessful()) {
                            FarmLogOutRoot farmLogOutRoot = response.body();
                            if (farmLogOutRoot.status) {
                                Toast.makeText(activity, "LoggedOut Successfully", Toast.LENGTH_SHORT).show();
                                SharedPreferences sP = getContext().getSharedPreferences("login_pref",MODE_PRIVATE);
                                SharedPreferences.Editor speditor = sP.edit();
                                speditor.putBoolean("sesson",false);
                                speditor.putString("role","");
                                speditor.commit();

                                Intent intent =new Intent(getActivity(), LoginActivity.class);
                                startActivity(intent);
                            }
                        } else {
                            Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<FarmLogOutRoot> call, Throwable t) {
                        Toast.makeText(activity, "Error" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

        editProfileBtnLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditFramProfileFragment editFramProfileFragment=new EditFramProfileFragment();
                Bundle arguments = new Bundle();
                arguments.putString("farm_id",farm_id);
                editFramProfileFragment.setArguments(arguments);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, editFramProfileFragment, "editFramProfileFragment").addToBackStack(null).commit();

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
        bull=view.findViewById(R.id.bull);
    }
}