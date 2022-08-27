package com.adarsh.cfarmmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.adarsh.cfarmmanagement.Retrofit.Api;
import com.adarsh.cfarmmanagement.Retrofit.ApiClient;
import com.adarsh.cfarmmanagement.model.CustomerLoginRoot;
import com.adarsh.cfarmmanagement.model.DocLoginRoot;
import com.adarsh.cfarmmanagement.model.FarmLogin;
import com.adarsh.cfarmmanagement.model.FarmLoginRoot;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    RadioGroup userGroup;
    RadioButton userFarmRB, userCustomerRB, userDoctorRB;
    TextInputEditText phoneEt, passwordEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userGroup = findViewById(R.id.user_group);
        userFarmRB = findViewById(R.id.user_farm);
        userCustomerRB = findViewById(R.id.user_customer);
        userDoctorRB = findViewById(R.id.user_doctor);
        phoneEt = findViewById(R.id.phoneNumber);
        passwordEt = findViewById(R.id.password);


    }

    public void farmRegistrationClick(View view) {
        Intent intent = new Intent(getApplicationContext(), FarmerRegistrationActivity.class);
        startActivity(intent);
    }

    public void customerRegistrationClick(View view) {
        Intent intent = new Intent(getApplicationContext(), CustomerRegistrationActivity.class);
        startActivity(intent);
    }

    public void doctorRegistrationClick(View view) {
        Intent intent = new Intent(getApplicationContext(), DoctorRegistrationActivity.class);
        startActivity(intent);
    }

    public void loginClick(View view) {

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("token", getApplicationContext().MODE_PRIVATE);
        String deviceToken=sharedPreferences.getString("token","");


        if (userFarmRB.isChecked()) {
            if (phoneEt.getText().toString().isEmpty() && !(phoneEt.getText().toString().trim().length() == 10)) {
                phoneEt.setError("Error");
            } else if (passwordEt.getText().toString().isEmpty()) {
                passwordEt.setError("Error");
            } else {
                String phone = phoneEt.getText().toString();
                String password = passwordEt.getText().toString();

                Api api = ApiClient.cattleFarm().create(Api.class);
                api.FARM_LOGIN_ROOT_CALL(phone, password,deviceToken).enqueue(new Callback<FarmLoginRoot>() {
                    @Override
                    public void onResponse(Call<FarmLoginRoot> call, Response<FarmLoginRoot> response) {
                        if (response.isSuccessful()) {
                            FarmLoginRoot farmLoginRoot = response.body();
                            if (farmLoginRoot.getStatus().equals("Success")&&farmLoginRoot.getUser_data().getStatus()==1) {

                                SharedPreferences sharedPreferences = getSharedPreferences("cattle_pref", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("farm_id", farmLoginRoot.getUser_data().getFarm_id());
                                editor.putString("seller", farmLoginRoot.getUser_data().getSeller());
                                editor.putString("district", farmLoginRoot.getUser_data().getDist());
                                editor.commit();

                              // Toast.makeText(LoginActivity.this, farmLoginRoot.getUser_data().getDist()+"", Toast.LENGTH_SHORT).show();

                                SharedPreferences sP = getSharedPreferences("login_pref", MODE_PRIVATE);
                                SharedPreferences.Editor speditor = sP.edit();
                                speditor.putBoolean("sesson",true);
                                speditor.putString("role","farm");
                                speditor.commit();

                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(LoginActivity.this, "Verification in Progress" + farmLoginRoot.getStatus(), Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(LoginActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<FarmLoginRoot> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "Server Error" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }
        if (userCustomerRB.isChecked()) {
            if (phoneEt.getText().toString().isEmpty() && !(phoneEt.getText().toString().trim().length() == 10)) {
                phoneEt.setError("Error");
            } else if (passwordEt.getText().toString().isEmpty()) {
                passwordEt.setError("Error");
            } else {
                String phone = phoneEt.getText().toString();
                String password = passwordEt.getText().toString();
                Api api = ApiClient.cattleFarm().create(Api.class);
                api.CUSTOMER_LOGIN_ROOT_CALL(phone, password).enqueue(new Callback<CustomerLoginRoot>() {
                    @Override
                    public void onResponse(Call<CustomerLoginRoot> call, Response<CustomerLoginRoot> response) {
                        if (response.isSuccessful()) {
                            CustomerLoginRoot customerLoginRoot = response.body();
                            if (customerLoginRoot.status) {

                                SharedPreferences sharedPreferences = getSharedPreferences("customer_pref", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("district", customerLoginRoot.customer_details.get(0).district);
                                editor.putString("customerId", customerLoginRoot.customer_details.get(0).id);
                                editor.putString("userPhoneNumber",customerLoginRoot.customer_details.get(0).phone);
                                editor.commit();

                                SharedPreferences sP = getSharedPreferences("login_pref", MODE_PRIVATE);
                                SharedPreferences.Editor speditor = sP.edit();
                                speditor.putBoolean("sesson",true);
                                speditor.putString("role","customer");
                                speditor.commit();

                                Intent intent = new Intent(getApplicationContext(), CustomerHomeActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(LoginActivity.this, "Wrong Credentials", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CustomerLoginRoot> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "Server Error" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }


        }
        if (userDoctorRB.isChecked()) {
            if (phoneEt.getText().toString().isEmpty() && !(phoneEt.getText().toString().trim().length() == 10)) {
                phoneEt.setError("Error");
            } else if (passwordEt.getText().toString().isEmpty()) {
                passwordEt.setError("Error");
            } else {
                String phone = phoneEt.getText().toString();
                String password = passwordEt.getText().toString();
                Api api = ApiClient.cattleFarm().create(Api.class);
                api.DOC_LOGIN_ROOT_CALL(phone, password,deviceToken).enqueue(new Callback<DocLoginRoot>() {
                    @Override
                    public void onResponse(Call<DocLoginRoot> call, Response<DocLoginRoot> response) {
                        if (response.isSuccessful()) {
                            DocLoginRoot docLoginRoot = response.body();
                            if (docLoginRoot.getStatus().equals("Success") && docLoginRoot.getUser_data().getStatus() == 1) {

                                SharedPreferences sharedPreferences = getSharedPreferences("doctor_pref", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("docId",docLoginRoot.getUser_data().getDoc_id());
                                editor.commit();

                                SharedPreferences sP = getSharedPreferences("login_pref", MODE_PRIVATE);
                                SharedPreferences.Editor speditor = sP.edit();
                                speditor.putBoolean("sesson",true);
                                speditor.putString("role","doctor");
                                speditor.commit();

                                Intent intent = new Intent(getApplicationContext(), DoctorHomeActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(LoginActivity.this, "Verification in Progress", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<DocLoginRoot> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "Server Error" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }


        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}