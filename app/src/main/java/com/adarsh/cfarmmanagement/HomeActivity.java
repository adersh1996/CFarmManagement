package com.adarsh.cfarmmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.adarsh.cfarmmanagement.fragments.CattleFragment;
import com.adarsh.cfarmmanagement.fragments.DoctorFragment;
import com.adarsh.cfarmmanagement.fragments.FarmProfileFragment;
import com.adarsh.cfarmmanagement.fragments.MilkFragment;
import com.adarsh.cfarmmanagement.fragments.SellProductFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    CattleFragment firstFragment = new CattleFragment();
    MilkFragment secondFragment = new MilkFragment();
    DoctorFragment thirdFragment = new DoctorFragment();
    SellProductFragment sellProductFragment = new SellProductFragment();
    FarmProfileFragment farmProfileFragment = new FarmProfileFragment();
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.nav_view);

        builder = new AlertDialog.Builder(this);

        SharedPreferences sharedPreferences = getSharedPreferences("cattle_pref", MODE_PRIVATE);
        String seller = sharedPreferences.getString("seller", "false");


        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.navigation_cattle);

        if (seller.equals("false")) {
            bottomNavigationView.getMenu().removeItem(R.id.navigation_seller);
        }


    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.navigation_cattle:
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, firstFragment).addToBackStack(null).commit();
                return true;

            case R.id.navigation_milk:
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, secondFragment).addToBackStack(null).commit();
                return true;

            case R.id.navigation_doctors:
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, thirdFragment).addToBackStack(null).commit();
                return true;
            case R.id.navigation_seller:
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, sellProductFragment).addToBackStack(null).commit();
                return true;
            case R.id.navigation_profile:
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, farmProfileFragment).addToBackStack(null).commit();
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
            AlertDialog alert = builder.create();
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