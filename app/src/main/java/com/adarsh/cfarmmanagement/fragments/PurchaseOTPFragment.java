package com.adarsh.cfarmmanagement.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.adarsh.cfarmmanagement.CustomerHomeActivity;
import com.adarsh.cfarmmanagement.FarmVerifyPhoneActivity;
import com.adarsh.cfarmmanagement.LoginActivity;
import com.adarsh.cfarmmanagement.R;
import com.adarsh.cfarmmanagement.Retrofit.Api;
import com.adarsh.cfarmmanagement.Retrofit.ApiClient;
import com.adarsh.cfarmmanagement.model.FarmReg;
import com.adarsh.cfarmmanagement.model.OrderProductRoot;
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


public class PurchaseOTPFragment extends Fragment {

    public CustomerHomeActivity activity;
    TextView verifyBt, phoneNumberTv;
    EditText editTextCode;
    ProgressBar progressBar;

    //These are the objects needed
    //It is the verification id that will be sent to the user
    private String mVerificationId;

    String productId, productQuantity, customerId,productPrice;

    //firebase auth object
    private FirebaseAuth mAuth;

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        this.activity = (CustomerHomeActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_purchase_o_t_p, container, false);

        verifyBt = view.findViewById(R.id.payment_otp_verify_bt);
        phoneNumberTv = view.findViewById(R.id.payment_verifi_phone_number);
        editTextCode = view.findViewById(R.id.payment_verification_otp);
        progressBar=view.findViewById(R.id.progressbar);

        String mobile = getArguments().getString("userPhone");
        // String mobile="9074937901";
        productId = getArguments().getString("productId");
        productQuantity = getArguments().getString("productQuantity");
        customerId = getArguments().getString("customerId");
        productPrice=getArguments().getString("productPrice");

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
                    verifyBt.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        return view;
    }

    private void sendVerificationCode(String mobile) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91" + mobile) // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(getActivity()) // Activity (for callback binding)
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
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
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

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //verification successful we will start the profile activity
//                            Intent intent = new Intent(VerifyPhoneActivity.this, HomeActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(intent);

                            Api api = ApiClient.cattleFarm().create(Api.class);
                            api.ORDER_PRODUCT_ROOT_CALL(productId,customerId,productQuantity,productPrice).enqueue(new Callback<OrderProductRoot>() {
                                @Override
                                public void onResponse(Call<OrderProductRoot> call, Response<OrderProductRoot> response) {
                                    if (response.isSuccessful()){
                                        OrderProductRoot orderProductRoot= response.body();
                                        if (orderProductRoot.status){

                                            progressBar.setVisibility(View.VISIBLE);

                                            Toast.makeText(getContext(), orderProductRoot.message, Toast.LENGTH_SHORT).show();

                                            CustomerHomeFragment customerHomeFragment=new CustomerHomeFragment();
                                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                            getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                            fragmentTransaction.replace(R.id.customer_home_container, customerHomeFragment);
                                            fragmentTransaction.addToBackStack(null);
                                            fragmentTransaction.commit();


                                        }else {
                                            verifyBt.setVisibility(View.VISIBLE);
                                            progressBar.setVisibility(View.INVISIBLE);
                                        }
                                    }else {
                                        verifyBt.setVisibility(View.VISIBLE);
                                        progressBar.setVisibility(View.INVISIBLE);
                                    }
                                }

                                @Override
                                public void onFailure(Call<OrderProductRoot> call, Throwable t) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            });

                        } else {

                            //verification unsuccessful.. display an error message
                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }

//                            Snackbar snackbar = Snackbar.make(findViewById(R.id.farmverification), message, Snackbar.LENGTH_LONG);
//                            snackbar.setAction("Dismiss", new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//
//                                }
//                            });
//                            snackbar.show();
                        }
                    }
                });
    }
}