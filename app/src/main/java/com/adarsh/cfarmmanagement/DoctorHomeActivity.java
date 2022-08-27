package com.adarsh.cfarmmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.adarsh.cfarmmanagement.fragments.DoctorChatFragment;
import com.adarsh.cfarmmanagement.fragments.DoctorProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DoctorHomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    DoctorChatFragment doctorChatFragment = new DoctorChatFragment();
    DoctorProfileFragment doctorProfileFragment = new DoctorProfileFragment();
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_home);

        builder=new AlertDialog.Builder(this);

        bottomNavigationView = findViewById(R.id.doctor_home_nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.navigation_enquiries);



    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.navigation_enquiries:
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getSupportFragmentManager().beginTransaction().replace(R.id.doctor_home_container, doctorChatFragment,"doctorChatFragment").addToBackStack(null).commit();
                return true;

            case R.id.navigation_doctor_profile:
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getSupportFragmentManager().beginTransaction().replace(R.id.doctor_home_container, doctorProfileFragment,"doctorProfileFragment").addToBackStack(null).commit();
                return true;

        }

        return false;
    }

    @Override
    public void onBackPressed() {
        int fragments = getSupportFragmentManager().getBackStackEntryCount();
        if (fragments == 1) {

            //dialogue box

            builder.setMessage("Do you want to exit?").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finishAffinity();
                }
            }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alert=builder.create();
            alert.setTitle("Exit");
            alert.show();


        } else {
            if (getFragmentManager().getBackStackEntryCount() > 1) {
                getFragmentManager().popBackStack();
            } else {
                super.onBackPressed();

            }
        }
    }
}
