package com.adarsh.cfarmmanagement.fragments;

import android.app.Activity;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.adarsh.cfarmmanagement.CustomerHomeActivity;
import com.adarsh.cfarmmanagement.R;
import com.adarsh.cfarmmanagement.Retrofit.Api;
import com.adarsh.cfarmmanagement.Retrofit.ApiClient;
import com.adarsh.cfarmmanagement.model.CustomerViewProductDetailsRoot;
import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProductDetailsFragment extends Fragment {

    public CustomerHomeActivity activity;

    private TextView customerProductFarmName;
    private ImageView customerProductDetailProductImage;
    private TextView customerProductDetailProductName;
    private LinearLayout productPriceLayout;
    private TextView customerProductDetailProductOldPrice;
    private TextView customerProductDetailProductPrice;
    private TextView productDeliveryDate;
    private TextView customerProductDetailProductExpiryDate;
    private TextView selectQuantityTxt;
    private EditText customerProductDetailProductQuantity;
    private TextView customerProductDetailProductIngredients;
    private TextView customerProductDetailProductBuyBt;
    TextView remainingQuantity;
    String productQuantity,productId;

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        this.activity = (CustomerHomeActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_details, container, false);
        remainingQuantity=view.findViewById(R.id.remaining_quantity);
        initView(view);

        productId = getArguments().getString("product_id");

        Api api = ApiClient.cattleFarm().create(Api.class);
        api.CUSTOMER_VIEW_PRODUCT_DETAILS_ROOT_CALL(productId).enqueue(new Callback<CustomerViewProductDetailsRoot>() {
            @Override
            public void onResponse(Call<CustomerViewProductDetailsRoot> call, Response<CustomerViewProductDetailsRoot> response) {
                if (response.isSuccessful()) {
                    CustomerViewProductDetailsRoot productDetails = response.body();
                    if (productDetails.status.equals("success")) {
                        customerProductFarmName.setText(productDetails.Details.get(0).farm);
                        customerProductDetailProductName.setText(productDetails.Details.get(0).product);
                        customerProductDetailProductPrice.setText(productDetails.Details.get(0).price);
                        customerProductDetailProductExpiryDate.setText(productDetails.Details.get(0).exp_date);
                        customerProductDetailProductIngredients.setText(productDetails.Details.get(0).ingredients);
                        remainingQuantity.setText("(Hurry, "+productDetails.Details.get(0).quantity+" Left)");

                        //old price
//                        int mrp = Integer.parseInt(productDetails.Details.get(0).price);
//                        int productMrp = ((mrp * 5) / 100) + mrp;
//                        customerProductDetailProductOldPrice.setText(String.valueOf(productMrp));
//                        customerProductDetailProductOldPrice.setPaintFlags(customerProductDetailProductOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                        //product quantity
//                        int quantity = Integer.parseInt(productDetails.Details.get(0).quantity);
//                        if (quantity != 0) {
//                            String[] quantityArray = new String[quantity];
//                            for (int i = 1; i < quantity; i++) {
//                                quantityArray[i] = String.valueOf(i);
//                            }
//                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
//                                    getActivity(), android.R.layout.simple_list_item_1, quantityArray);
//                            customerProductDetailProductQuantity.setAdapter(arrayAdapter);
//                            customerProductDetailProductQuantity.setCursorVisible(false);
//                        }

                        Glide.with(activity).load(productDetails.Details.get(0).file).into(customerProductDetailProductImage);

                    }
                } else {
                    Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CustomerViewProductDetailsRoot> call, Throwable t) {
                Toast.makeText(activity, "Server Error" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //quantity spinner
//        customerProductDetailProductQuantity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                customerProductDetailProductQuantity.showDropDown();
//                productQuantity = (String) adapterView.getItemAtPosition(i);
//            }
//        });
//        customerProductDetailProductQuantity.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                customerProductDetailProductQuantity.showDropDown();
//            }
//        });

        //show dropdown in single click
//        customerProductDetailProductQuantity.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
//                    customerProductDetailProductQuantity.showDropDown();
//                }
//                return false;
//            }
//        });


        //buy product
        customerProductDetailProductBuyBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PaymentFragment paymentFragment = new PaymentFragment();
                Bundle arguments = new Bundle();
                if (customerProductDetailProductQuantity.getText().toString().isEmpty()){
                    Toast.makeText(activity, "Select Quantity", Toast.LENGTH_SHORT).show();
                }else {
                    arguments.putString("productId",productId);
                    arguments.putString("productQuantity",customerProductDetailProductQuantity.getText().toString());
                    arguments.putString("price",customerProductDetailProductPrice.getText().toString());
                    paymentFragment.setArguments(arguments);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.customer_home_container,paymentFragment,"paymentFragment").addToBackStack(null).commit();
                }


            }
        });

        return view;
    }

    private void initView(View view) {
        customerProductFarmName = view.findViewById(R.id.customer_product_farm_name);
        customerProductDetailProductImage = view.findViewById(R.id.customer_product_detail_product_image);
        customerProductDetailProductName = view.findViewById(R.id.customer_product_detail_product_name);
        productPriceLayout = view.findViewById(R.id.product_price_layout);
        customerProductDetailProductOldPrice = view.findViewById(R.id.customer_product_detail_product_old_price);
        customerProductDetailProductPrice = view.findViewById(R.id.customer_product_detail_product_price);
        productDeliveryDate = view.findViewById(R.id.product_delivery_date);
        customerProductDetailProductExpiryDate = view.findViewById(R.id.customer_product_detail_product_expiry_date);
        selectQuantityTxt = view.findViewById(R.id.select_quantity_txt);
        customerProductDetailProductQuantity = view.findViewById(R.id.customer_product_detail_product_quantity);
        customerProductDetailProductIngredients = view.findViewById(R.id.customer_product_detail_product_ingredients);
        customerProductDetailProductBuyBt = view.findViewById(R.id.customer_product_detail_product_buy_bt);
    }
}