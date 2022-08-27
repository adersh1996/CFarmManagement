package com.adarsh.cfarmmanagement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.adarsh.cfarmmanagement.Retrofit.Api;
import com.adarsh.cfarmmanagement.Retrofit.ApiClient;
import com.adarsh.cfarmmanagement.model.FarmReg;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import soup.neumorphism.NeumorphCardView;

public class FarmerRegistrationActivity extends AppCompatActivity {

    RadioGroup selectionGroup;
    NeumorphCardView neumorphCardView;

    TextInputEditText name, email, phone, password, reTypePassword, address, state, profilePicPath, fssaiLicNum, fssaiPicPath;
    AutoCompleteTextView district;
    Button profilePicBt, fssaiPicBt, distDelete;
    TextView regBt;
    String seller ="false";
    String distSelection,licenceImagePicturePath,profileImagePicturePath;


    private static final int STORAGE_PERMISSION_CODE = 104;
    private static final int RESULT_LOAD_LICENSE_IMAGE = 105;
    private static final int RESULT_LOAD_PRO_IMAGE = 106;

    private File licenseImageFile, proImageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_registration);

        selectionGroup = findViewById(R.id.selection_group);
        neumorphCardView = findViewById(R.id.n_cardview);
        name = findViewById(R.id.farm_reg_name);
        email = findViewById(R.id.farm_reg_email);
        phone = findViewById(R.id.farm_reg_phone);
        password = findViewById(R.id.farm_reg_password);
        reTypePassword = findViewById(R.id.farm_reg_re_typ_pass);
        address = findViewById(R.id.farm_reg_address);
        state = findViewById(R.id.farm_reg_state);
        profilePicPath = findViewById(R.id.farm_reg_pro_pic_path);
        fssaiLicNum = findViewById(R.id.farm_reg_fssai_lic_no);
        fssaiPicPath = findViewById(R.id.farm_reg_fssai_lic_pic_path);
        district = findViewById(R.id.farmer_reg_district);
        profilePicBt = findViewById(R.id.farm_reg_pro_pic_bt);
        fssaiPicBt = findViewById(R.id.farm_reg_fssai_lic_pic_bt);
        distDelete = findViewById(R.id.farmer_reg_dist_del);
        regBt = findViewById(R.id.farm_reg_bt);
        fssaiPicPath.setEnabled(false);
        profilePicPath.setEnabled(false);
        selectionGroup.check(R.id.no_rb);

        selectionGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.yes_rb:
                        seller="true";
                        neumorphCardView.setVisibility(View.VISIBLE);
                        break;

                    case R.id.no_rb:
                        seller="false";
                        neumorphCardView.setVisibility(View.GONE);
                        break;
                }
            }
        });

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, getResources()
                .getStringArray(R.array.district_names));

        district.setAdapter(arrayAdapter);
        district.setCursorVisible(false);
        district.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                district.showDropDown();
                distSelection = (String) adapterView.getItemAtPosition(i);
                distDelete.setAlpha(.8f);
            }
        });
        district.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                district.showDropDown();
            }
        });
        distDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                district.setText(null);
                distDelete.setAlpha(.2f);
                distSelection = null;
            }
        });

        profilePicBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(FarmerRegistrationActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
                } else {
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_PRO_IMAGE);
                }

            }
        });

        fssaiPicBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(FarmerRegistrationActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
                } else {
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_LICENSE_IMAGE);
                }

            }
        });

        regBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if (name.getText().toString().isEmpty()) {
                    name.setError("mandatory");
                } else if (!(email.getText().toString().matches(emailPattern)) || email.getText().toString().isEmpty()) {
                    email.setError("mandatory");
                } else if (phone.getText().toString().isEmpty() || !(phone.getText().toString().trim().length() == 10)) {
                    phone.setError("mandatory");
                } else if (password.getText().toString().isEmpty()) {
                    password.setError("mandatory");
                } else if (reTypePassword.getText().toString().isEmpty()) {
                    reTypePassword.setError("mandatory");
                } else if (address.getText().toString().isEmpty()) {
                    address.setError("mandatory");
                } else if (state.getText().toString().isEmpty()) {
                    state.setError("mandatory");
                } else if (district.getText().toString().isEmpty()) {
                    district.setError("mandatory");
                } else if (profilePicPath.getText().toString().isEmpty()) {
                    profilePicPath.setError("mandatory");
                } else if (!(password.getText().toString().equals(reTypePassword.getText().toString()))) {
                    reTypePassword.setError("Password mismatch");
                }else{
                    if (seller.equals("false")){
                        RequestBody sName = RequestBody.create(MediaType.parse("text/plain"),name.getText().toString());
                        RequestBody sEmail = RequestBody.create(MediaType.parse("text/plain"),email.getText().toString());
                        RequestBody sPhone = RequestBody.create(MediaType.parse("text/plain"),phone.getText().toString());
                        RequestBody sPassword =RequestBody.create(MediaType.parse("text/plain"),reTypePassword.getText().toString()) ;
                        RequestBody sAddress = RequestBody.create(MediaType.parse("text/plain"),address.getText().toString());
                        RequestBody sState = RequestBody.create(MediaType.parse("text/plain"),state.getText().toString());
                        RequestBody sDistrict = RequestBody.create(MediaType.parse("text/plain"),district.getText().toString());
                        RequestBody sSeller = RequestBody.create(MediaType.parse("text/plain"),"false");
                        MultipartBody.Part proPicFilePart = MultipartBody.Part.createFormData("icon", proImageFile.getName(), RequestBody.create(MediaType.parse("image/*"), proImageFile));

                        Intent intent=new Intent(getApplicationContext(),FarmVerifyPhoneActivity.class);

                        intent.putExtra("Name",name.getText().toString());
                        intent.putExtra("Email",email.getText().toString());
                        intent.putExtra("Phone",phone.getText().toString());
                        intent.putExtra("Password",reTypePassword.getText().toString());
                        intent.putExtra("Address",address.getText().toString());
                        intent.putExtra("State",state.getText().toString());
                        intent.putExtra("District",district.getText().toString());
                        intent.putExtra("Seller","false");
                        intent.putExtra("profileImagePicturePath",profileImagePicturePath);
                        intent.putExtra("licenceImagePicturePath","");

                       // Toast.makeText(FarmerRegistrationActivity.this, name.getText().toString(), Toast.LENGTH_SHORT).show();


                        startActivity(intent);

//                        Api api = ApiClient.cattleFarm().create(Api.class);
//                        api.FARM_REG_CALL(sName,sPhone,sEmail,sAddress,sDistrict,sState,sPassword,sSeller,proPicFilePart).enqueue(new Callback<FarmReg>() {
//                            @Override
//                            public void onResponse(Call<FarmReg> call, Response<FarmReg> response) {
//                                if (response.isSuccessful()){
//                                    FarmReg farmReg= response.body();
//                                    if(farmReg.getStatus().equals("success")){
//                                       // Toast.makeText(FarmerRegistrationActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
//                                        Toast.makeText(FarmerRegistrationActivity.this, profileImagePicturePath, Toast.LENGTH_SHORT).show();
//                                        Intent intent = new Intent(FarmerRegistrationActivity.this, LoginActivity.class);
//                                        startActivity(intent);
//                                    }else
//                                    {
//                                        Toast.makeText(FarmerRegistrationActivity.this, "Registration UnSuccessful", Toast.LENGTH_SHORT).show();
//                                    }
//
//                                }else {
//                                    Toast.makeText(FarmerRegistrationActivity.this, "Failed", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(Call<FarmReg> call, Throwable t) {
//                                Toast.makeText(FarmerRegistrationActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        });

                    }else{
                        RequestBody sName = RequestBody.create(MediaType.parse("text/plain"),name.getText().toString());
                        RequestBody sEmail = RequestBody.create(MediaType.parse("text/plain"),email.getText().toString());
                        RequestBody sPhone = RequestBody.create(MediaType.parse("text/plain"),phone.getText().toString());
                        RequestBody sPassword =RequestBody.create(MediaType.parse("text/plain"),reTypePassword.getText().toString()) ;
                        RequestBody sAddress = RequestBody.create(MediaType.parse("text/plain"),address.getText().toString());
                        RequestBody sState = RequestBody.create(MediaType.parse("text/plain"),state.getText().toString());
                        RequestBody sDistrict = RequestBody.create(MediaType.parse("text/plain"),district.getText().toString());
                        RequestBody sSeller = RequestBody.create(MediaType.parse("text/plain"),"true");
                        RequestBody sFssaiLicenseNum=RequestBody.create(MediaType.parse("text/plain"),fssaiLicNum.getText().toString());
                        MultipartBody.Part proPicFilePart = MultipartBody.Part.createFormData("icon", proImageFile.getName(), RequestBody.create(MediaType.parse("image/*"), proImageFile));
                        MultipartBody.Part licensePicFilePart = MultipartBody.Part.createFormData("avatar", licenseImageFile.getName(), RequestBody.create(MediaType.parse("image/*"), licenseImageFile));


                        Intent intent=new Intent(getApplicationContext(),FarmVerifyPhoneActivity.class);

                        intent.putExtra("Name",name.getText().toString());
                        intent.putExtra("Email",email.getText().toString());
                        intent.putExtra("Phone",phone.getText().toString());
                        intent.putExtra("Password",reTypePassword.getText().toString());
                        intent.putExtra("Address",address.getText().toString());
                        intent.putExtra("State",state.getText().toString());
                        intent.putExtra("District",district.getText().toString());
                        intent.putExtra("Seller","true");

                        if (fssaiLicNum.getText().toString().isEmpty() || !(fssaiLicNum.getText().toString().trim().length() == 14)){
                            fssaiLicNum.setError("mandatory");
                        }else {
                            intent.putExtra("fssaiLicNum",fssaiLicNum.getText().toString());
                            intent.putExtra("profileImagePicturePath",profileImagePicturePath);
                            intent.putExtra("licenceImagePicturePath",licenceImagePicturePath);

                            startActivity(intent);
                        }



//                        Api api = ApiClient.cattleFarm().create(Api.class);
//                        api.FARM_REG_CALL_SELLER(sName,sPhone,sEmail,sAddress,sDistrict,sState,sPassword,sSeller,sFssaiLicenseNum,licensePicFilePart,proPicFilePart).enqueue(new Callback<FarmReg>() {
//                            @Override
//                            public void onResponse(Call<FarmReg> call, Response<FarmReg> response) {
//                                if (response.isSuccessful()){
//                                    FarmReg farmReg= response.body();
//                                    if(farmReg.getStatus().equals("success")){
//                                       // Toast.makeText(FarmerRegistrationActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
//                                        Toast.makeText(FarmerRegistrationActivity.this, licenceImagePicturePath+profileImagePicturePath, Toast.LENGTH_SHORT).show();
//                                        Intent intent = new Intent(FarmerRegistrationActivity.this, LoginActivity.class);
//                                        startActivity(intent);
//                                    }else
//                                    {
//                                        Toast.makeText(FarmerRegistrationActivity.this, "Registration UnSuccessful", Toast.LENGTH_SHORT).show();
//                                    }
//                                }else {
//                                    Toast.makeText(FarmerRegistrationActivity.this, "Failed", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(Call<FarmReg> call, Throwable t) {
//                                Toast.makeText(FarmerRegistrationActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        });

                    }

                }
            }
        });

    }

    // Function to check and request permission.
    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(FarmerRegistrationActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(FarmerRegistrationActivity.this, new String[]{permission}, requestCode);
        } else {
            Toast.makeText(this, "Permission Already granted", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_LICENSE_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            licenceImagePicturePath = cursor.getString(columnIndex);
            cursor.close();
            licenseImageFile = new File(licenceImagePicturePath);
            fssaiPicPath.setText(licenceImagePicturePath);
        }
        if (requestCode == RESULT_LOAD_PRO_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            profileImagePicturePath = cursor.getString(columnIndex);
            cursor.close();
            proImageFile = new File(profileImagePicturePath);
            profilePicPath.setText(profileImagePicturePath);
        }
    }
}