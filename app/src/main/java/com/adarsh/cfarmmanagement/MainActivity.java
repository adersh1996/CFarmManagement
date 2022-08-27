package com.adarsh.cfarmmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN_TIME_OUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences sP = getSharedPreferences("login_pref", MODE_PRIVATE);
                if (sP.getBoolean("sesson", false)) {
                    switch (sP.getString("role", "")) {
                        case "doctor":
                            Intent intent = new Intent(MainActivity.this, DoctorHomeActivity.class);
                            startActivity(intent);
                            break;
                        case "customer":
                            Intent intentC = new Intent(MainActivity.this, CustomerHomeActivity.class);
                            startActivity(intentC);
                            break;
                        case "farm":
                            Intent intentF = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(intentF);
                            break;
                    }
                } else {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }


            }
        }, SPLASH_SCREEN_TIME_OUT);
    }


}