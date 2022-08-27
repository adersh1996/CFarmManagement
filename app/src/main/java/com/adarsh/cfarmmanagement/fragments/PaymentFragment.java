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
import android.widget.Button;
import android.widget.Toast;

import com.adarsh.cfarmmanagement.CustomerHomeActivity;
import com.adarsh.cfarmmanagement.R;
import com.google.android.material.textfield.TextInputEditText;


public class PaymentFragment extends Fragment {

    public CustomerHomeActivity activity;

    Button payNow;
    TextInputEditText name, cardNumber, expiryDate, cvv;

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        this.activity = (CustomerHomeActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment, container, false);

        payNow = view.findViewById(R.id.add_payment_button);
        name = view.findViewById(R.id.login_email_edt);
        cardNumber = view.findViewById(R.id.cardNumber);
        expiryDate = view.findViewById(R.id.expiry_date);
        cvv = view.findViewById(R.id.cvv);


        expiryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(getContext(), expiryDate);
            }
        });

        SharedPreferences sharedPreferences = activity.getSharedPreferences("customer_pref", Context.MODE_PRIVATE);
        String customerId = sharedPreferences.getString("customerId", "");
        String userPhone = sharedPreferences.getString("userPhoneNumber", "");

        String productId = getArguments().getString("productId");
        String productQuantity = getArguments().getString("productQuantity");
        String price = getArguments().getString("price");

        int totalQuantity = Integer.parseInt(productQuantity);
        int totalPriceOfProduct = Integer.parseInt(price) * totalQuantity;

        String priceOfPurchasedProduct = String.valueOf(totalPriceOfProduct);


        //  Toast.makeText(getActivity(), customerId+productId+productQuantity+userPhone, Toast.LENGTH_SHORT).show();

        payNow.setText(" P A Y " + totalPriceOfProduct);

        payNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (name.getText().toString().isEmpty()) {
                    name.setError("mandatory");
                } else if (cardNumber.getText().toString().isEmpty()) {
                    cardNumber.setError("mandatory");
                } else if (expiryDate.getText().toString().isEmpty()) {
                    cvv.setError("mandatory");
                } else {
                    PurchaseOTPFragment purchaseOTPFragment = new PurchaseOTPFragment();
                    Bundle arguments = new Bundle();
                    arguments.putString("customerId", customerId);
                    arguments.putString("userPhone", userPhone);
                    arguments.putString("productId", productId);
                    arguments.putString("productQuantity", productQuantity);
                    arguments.putString("productPrice",priceOfPurchasedProduct);
                    purchaseOTPFragment.setArguments(arguments);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.customer_home_container, purchaseOTPFragment, "purchaseOTPFragment").addToBackStack(null).commit();
                }


            }
        });

        return view;
    }
}