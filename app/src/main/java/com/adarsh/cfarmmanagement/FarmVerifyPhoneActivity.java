package com.adarsh.cfarmmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.adarsh.cfarmmanagement.Retrofit.Api;
import com.adarsh.cfarmmanagement.Retrofit.ApiClient;
import com.adarsh.cfarmmanagement.model.FarmReg;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FarmVerifyPhoneActivity extends AppCompatActivity {

    TextView verifyBt, phoneNumberTv;
    EditText editTextCode;
    ProgressBar progressBar;

    //These are the objects needed
    //It is the verification id that will be sent to the user
    private String mVerificationId;


    //firebase auth object
    private FirebaseAuth mAuth;

    private File licenseImageFile, proImageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_verify_phone);

        verifyBt = findViewById(R.id.farm_otp_verify_bt);
        phoneNumberTv = findViewById(R.id.farm_verifi_phone_number);
        editTextCode = findViewById(R.id.farm_verification_otp);
        progressBar = findViewById(R.id.progressbar);

        String mobile = getIntent().getStringExtra("Phone");
        phoneNumberTv.setText(mobile);

        //initializing objects
        mAuth = FirebaseAuth.getInstance();

        sendVerificationCode(mobile);

        verifyBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = editTextCode.getText().toString().trim();
                if (code.isEmpty() || code.length() < 6) {
                    editTextCode.setError("Enter valid code");
                    editTextCode.requestFocus();
                    return;
                }
                try {
                    //verifying the code entered manually
                    verifyVerificationCode(code);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void sendVerificationCode(String mobile) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91" + mobile) // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this) // Activity (for callback binding)
                        .setCallbacks(mCallbacks) // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    //the callback to detect the verification status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                editTextCode.setText(code);
                //verifying the code
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(FarmVerifyPhoneActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            // progressBar.setVisibility(View.GONE);
            // buttonSignIn.setVisibility(View.VISIBLE);

            //storing the verification id that is sent to the user
            mVerificationId = s;
        }
    };

    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        progressBar.setVisibility(View.VISIBLE);
        verifyBt.setVisibility(View.GONE);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(FarmVerifyPhoneActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //verification successful we will start the profile activity
//                            Intent intent = new Intent(VerifyPhoneActivity.this, HomeActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(intent);

                            progressBar.setVisibility(View.GONE);
                            verifyBt.setVisibility(View.VISIBLE);

                            if (getIntent().getStringExtra("licenceImagePicturePath").isEmpty()) {
                                String name = getIntent().getStringExtra("Name");
                                String email = getIntent().getStringExtra("Email");
                                String phone = getIntent().getStringExtra("Phone");
                                String password = getIntent().getStringExtra("Password");
                                String address = getIntent().getStringExtra("Address");
                                String state = getIntent().getStringExtra("State");
                                String district = getIntent().getStringExtra("District");
                                String seller = getIntent().getStringExtra("Seller");
                                String profileImagePicturePath = getIntent().getStringExtra("profileImagePicturePath");

                                proImageFile = new File(profileImagePicturePath);

                                RequestBody sName = RequestBody.create(MediaType.parse("text/plain"), name);
                                RequestBody sEmail = RequestBody.create(MediaType.parse("text/plain"), email);
                                RequestBody sPhone = RequestBody.create(MediaType.parse("text/plain"), phone);
                                RequestBody sPassword = RequestBody.create(MediaType.parse("text/plain"), password);
                                RequestBody sAddress = RequestBody.create(MediaType.parse("text/plain"), address);
                                RequestBody sState = RequestBody.create(MediaType.parse("text/plain"), state);
                                RequestBody sDistrict = RequestBody.create(MediaType.parse("text/plain"), district);
                                RequestBody sSeller = RequestBody.create(MediaType.parse("text/plain"), seller);
                                MultipartBody.Part proPicFilePart = MultipartBody.Part.createFormData("icon", proImageFile.getName(), RequestBody.create(MediaType.parse("image/*"), proImageFile));


                                Api api = ApiClient.cattleFarm().create(Api.class);
                                api.FARM_REG_CALL(sName, sPhone, sEmail, sAddress, sDistrict, sState, sPassword, sSeller, proPicFilePart).enqueue(new Callback<FarmReg>() {
                                    @Override
                                    public void onResponse(Call<FarmReg> call, Response<FarmReg> response) {
                                        if (response.isSuccessful()) {
                                            FarmReg farmReg = response.body();
                                            if (farmReg.getStatus().equals("success")) {
                                                Toast.makeText(FarmVerifyPhoneActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                                // Toast.makeText(FarmVerifyPhoneActivity.this, profileImagePicturePath, Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(FarmVerifyPhoneActivity.this, LoginActivity.class);
                                                startActivity(intent);
                                            } else {
                                                Toast.makeText(FarmVerifyPhoneActivity.this, "Registration UnSuccessful", Toast.LENGTH_SHORT).show();
                                            }

                                        } else {
                                            Toast.makeText(FarmVerifyPhoneActivity.this, "Respose Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<FarmReg> call, Throwable t) {
                                        Toast.makeText(FarmVerifyPhoneActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });


                            } else {
                                String name = getIntent().getStringExtra("Name");
                                String email = getIntent().getStringExtra("Email");
                                String phone = getIntent().getStringExtra("Phone");
                                String password = getIntent().getStringExtra("Password");
                                String address = getIntent().getStringExtra("Address");
                                String state = getIntent().getStringExtra("State");
                                String district = getIntent().getStringExtra("District");
                                String seller = getIntent().getStringExtra("Seller");
                                String fssaiLicNum = getIntent().getStringExtra("fssaiLicNum");
                                String profileImagePicturePath = getIntent().getStringExtra("profileImagePicturePath");
                                String licenceImagePicturePath = getIntent().getStringExtra("licenceImagePicturePath");

                                proImageFile = new File(profileImagePicturePath);
                                licenseImageFile = new File(licenceImagePicturePath);

                                RequestBody sName = RequestBody.create(MediaType.parse("text/plain"), name);
                                RequestBody sEmail = RequestBody.create(MediaType.parse("text/plain"), email);
                                RequestBody sPhone = RequestBody.create(MediaType.parse("text/plain"), phone);
                                RequestBody sPassword = RequestBody.create(MediaType.parse("text/plain"), password);
                                RequestBody sAddress = RequestBody.create(MediaType.parse("text/plain"), address);
                                RequestBody sState = RequestBody.create(MediaType.parse("text/plain"), state);
                                RequestBody sDistrict = RequestBody.create(MediaType.parse("text/plain"), district);
                                RequestBody sSeller = RequestBody.create(MediaType.parse("text/plain"), seller);
                                RequestBody sFssaiLicenseNum = RequestBody.create(MediaType.parse("text/plain"), fssaiLicNum);
                                MultipartBody.Part proPicFilePart = MultipartBody.Part.createFormData("icon", proImageFile.getName(), RequestBody.create(MediaType.parse("image/*"), proImageFile));
                                MultipartBody.Part licensePicFilePart = MultipartBody.Part.createFormData("avatar", licenseImageFile.getName(), RequestBody.create(MediaType.parse("image/*"), licenseImageFile));

                                //Toast.makeText(FarmVerifyPhoneActivity.this, "seller", Toast.LENGTH_SHORT).show();

                                Api api = ApiClient.cattleFarm().create(Api.class);
                                api.FARM_REG_CALL_SELLER(sName, sPhone, sEmail, sAddress, sDistrict, sState, sPassword, sSeller, sFssaiLicenseNum, licensePicFilePart, proPicFilePart).enqueue(new Callback<FarmReg>() {
                                    @Override
                                    public void onResponse(Call<FarmReg> call, Response<FarmReg> response) {
                                        if (response.isSuccessful()) {
                                            FarmReg farmReg = response.body();
                                            if (farmReg.getStatus().equals("success")) {
                                                Toast.makeText(FarmVerifyPhoneActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                                // Toast.makeText(FarmVerifyPhoneActivity.this, licenceImagePicturePath+profileImagePicturePath, Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(FarmVerifyPhoneActivity.this, LoginActivity.class);
                                                startActivity(intent);
                                            } else {
                                                Toast.makeText(FarmVerifyPhoneActivity.this, "Registration UnSuccessful", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(FarmVerifyPhoneActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<FarmReg> call, Throwable t) {
                                        Toast.makeText(FarmVerifyPhoneActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }


                        } else {

                            progressBar.setVisibility(View.GONE);
                            verifyBt.setVisibility(View.VISIBLE);
//
                            //verification unsuccessful.. display an error message
                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                              message = "Invalid code entered...";
                            }

                            Snackbar snackbar = Snackbar.make(findViewById(R.id.farmverification), message, Snackbar.LENGTH_LONG);
                            snackbar.setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                            snackbar.show();
                        }
                    }
                });
    }
}