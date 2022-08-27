package com.adarsh.cfarmmanagement.fragments;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.adarsh.cfarmmanagement.CustomerRegistrationActivity;
import com.adarsh.cfarmmanagement.HomeActivity;
import com.adarsh.cfarmmanagement.R;
import com.adarsh.cfarmmanagement.Retrofit.Api;
import com.adarsh.cfarmmanagement.Retrofit.ApiClient;
import com.adarsh.cfarmmanagement.model.AddProductRoot;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddProductFragment extends Fragment {

    public HomeActivity activity;
    TextInputEditText productName, productPrice, quantity, ingredients, expiryDate, productImagePath;
    Button productUploadBt;
    TextView productAddBt;
    Calendar myCalendar = Calendar.getInstance();

    // Defining Permission codes.
    private static final int STORAGE_PERMISSION_CODE = 101;
    private static final int RESULT_LOAD_IMAGE = 102;

    private File imageFile;

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        this.activity = (HomeActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_product, container, false);

        productName = view.findViewById(R.id.add_product_product_name);
        productPrice = view.findViewById(R.id.add_product_price);
        quantity = view.findViewById(R.id.add_product_quantity);
        ingredients = view.findViewById(R.id.add_product_ingredients);
        expiryDate = view.findViewById(R.id.add_product_expiry_date);
        productImagePath = view.findViewById(R.id.add_product_image_path);
        productUploadBt = view.findViewById(R.id.add_product_pic_upload_bt);
        productAddBt = view.findViewById(R.id.add_product_bt);

        productImagePath.setEnabled(false);




        productUploadBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
                } else {
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                }

            }
        });

        DatePickerDialog.OnDateSetListener datePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                myCalendar.set(Calendar.YEAR, i);
                myCalendar.set(Calendar.MONTH, i1);
                myCalendar.set(Calendar.DAY_OF_MONTH, i2);
                updateLabel();
            }
        };

        expiryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(), datePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        productAddBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                SharedPreferences ssharedPreferences=getContext().getSharedPreferences("cattle_pref",MODE_PRIVATE);
//                String ssdistrict=ssharedPreferences.getString("district","");
//                String ssfarmId=ssharedPreferences.getString("farm_id","");
//
//                Toast.makeText(getContext(), ssdistrict+"", Toast.LENGTH_SHORT).show();

                if (productName.getText().toString().isEmpty()) {
                    productName.setError("mandatory");
                } else if (productPrice.getText().toString().isEmpty()) {
                    productPrice.setError("mandatory");
                } else if (quantity.getText().toString().isEmpty()) {
                    quantity.setError("mandatory");
                } else if (ingredients.getText().toString().isEmpty()) {
                    ingredients.setError("mandatory");
                } else if (expiryDate.getText().toString().isEmpty()) {
                    expiryDate.setError("mandatory");
                } else if (productImagePath.getText().toString().isEmpty()) {
                    productImagePath.setError("mandatory");
                } else {

                    SharedPreferences sharedPreferences=getContext().getSharedPreferences("cattle_pref",MODE_PRIVATE);
                    String district=sharedPreferences.getString("district","");
                    String farmId=sharedPreferences.getString("farm_id","");

                   // Toast.makeText(getContext(), district+"", Toast.LENGTH_SHORT).show();

                    RequestBody sProductName = RequestBody.create(MediaType.parse("text/plain"), productName.getText().toString());
                    RequestBody sProductPrice = RequestBody.create(MediaType.parse("text/plain"), productPrice.getText().toString());
                    RequestBody sProductQuantity = RequestBody.create(MediaType.parse("text/plain"), quantity.getText().toString());
                    RequestBody sIngredients = RequestBody.create(MediaType.parse("text/plain"), ingredients.getText().toString());
                    RequestBody sExpiryDate = RequestBody.create(MediaType.parse("text/plain"), expiryDate.getText().toString());
                    RequestBody sDistrict = RequestBody.create(MediaType.parse("text/plain"), district);
                    RequestBody sFarmId = RequestBody.create(MediaType.parse("text/plain"), farmId);
                    MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", imageFile.getName(), RequestBody.create(MediaType.parse("image/*"), imageFile));

                    Api api = ApiClient.cattleFarm().create(Api.class);
                    api.ADD_PRODUCT_ROOT_CALL(sProductName,sProductPrice,sProductQuantity,sIngredients,sExpiryDate,sDistrict,filePart,sFarmId).enqueue(new Callback<AddProductRoot>() {
                        @Override
                        public void onResponse(Call<AddProductRoot> call, Response<AddProductRoot> response) {
                            if (response.isSuccessful()){
                                AddProductRoot addProductRoot= response.body();
                                if (addProductRoot.getStatus()){
                                    Toast.makeText(activity, addProductRoot.getMessage(), Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(activity, addProductRoot.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(activity, "ServerError", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<AddProductRoot> call, Throwable t) {
                            Toast.makeText(activity, "ServerError"+t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


        return view;
    }

    // Function to check and request permission.
    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(getActivity(), permission) == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, requestCode);
        } else {
            Toast.makeText(getActivity(), "Permission Already granted", Toast.LENGTH_SHORT).show();
        }

    }

    // This function is called when the user accepts or decline the permission.
    // Request Code is used to check which permission called this function.
    // This request code is provided when the user is prompt for permission.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContext().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            imageFile = new File(picturePath);
            productImagePath.setText(picturePath);
        }
    }

    private void updateLabel() {
        String myFormat = "yyyy/MM/dd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        expiryDate.setText(dateFormat.format(myCalendar.getTime()));
    }
}