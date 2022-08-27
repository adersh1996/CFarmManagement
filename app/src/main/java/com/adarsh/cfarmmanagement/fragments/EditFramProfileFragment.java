package com.adarsh.cfarmmanagement.fragments;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.adarsh.cfarmmanagement.R;
import com.adarsh.cfarmmanagement.Retrofit.Api;
import com.adarsh.cfarmmanagement.Retrofit.ApiClient;
import com.adarsh.cfarmmanagement.model.EditFarmProfileRoot;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditFramProfileFragment extends Fragment {


    private RelativeLayout rlOne;
    private CircleImageView proImg;
    private CircleImageView editProImg;
    private TextInputEditText farmName;
    private TextInputEditText mailId;
    private TextInputEditText address;
    private TextInputEditText state;
    private AutoCompleteTextView district;
    private TextInputEditText licenseNumber;
    private TextInputEditText fssaiLicenseImagePath;
    private Button fssaiLicenseBt;
    private LinearLayout submitBtnLayout;
    private TextView submitBt;

    boolean isAllFieldsChecked = false;

    private static final int STORAGE_PERMISSION_CODE = 101;
    private static final int RESULT_LICENSE_LOAD_IMAGE = 102;
    private static final int RESULT_LOAD_PRO_IMAGE = 106;

    private File licenseImageFile, proImageFile;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_fram_profile, container, false);
        initView(view);

        SharedPreferences sP = getContext().getSharedPreferences("cattle_profile", MODE_PRIVATE);
        String sFarmId = sP.getString("farmId", "");
        String sName = sP.getString("name", "");
        String sMailId = sP.getString("mailId", "");
        String sPhone = sP.getString("phone", "");
        String sAddress = sP.getString("address", "");
        String sState = sP.getString("state", "");
        String sDistrict = sP.getString("district", "");
        String sSeller = sP.getString("seller", "");
        String sFssaiNo = sP.getString("fssaiNo", "");
        String sProPic = sP.getString("proPic", "");
        String sLicensePic = sP.getString("licensePic", "");

        farmName.setText(sName);
        mailId.setText(sMailId);
        address.setText(sAddress);
        state.setText(sState);
        district.setText(sDistrict);
        licenseNumber.setText(sFssaiNo);
        fssaiLicenseImagePath.setText(sLicensePic);
        Glide.with(getContext()).load(sProPic).into(proImg);


        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_list_item_1, getResources()
                .getStringArray(R.array.district_names));

        district.setAdapter(arrayAdapter);
        district.setCursorVisible(false);
        district.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                district.showDropDown();
            }
        });
        district.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                district.showDropDown();
            }
        });



        fssaiLicenseBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
                } else {
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LICENSE_LOAD_IMAGE);
                }

            }
        });
        editProImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
                } else {
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_PRO_IMAGE);
                }
            }
        });


        submitBtnLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isAllFieldsChecked=checkAllFields();
                if (isAllFieldsChecked){
                    RequestBody rName = RequestBody.create(MediaType.parse("text/plain"), farmName.getText().toString());
                    RequestBody rFarmId = RequestBody.create(MediaType.parse("text/plain"), sFarmId);
                    RequestBody rMailId = RequestBody.create(MediaType.parse("text/plain"), mailId.getText().toString());
                    RequestBody rPhone = RequestBody.create(MediaType.parse("text/plain"), sPhone);
                    RequestBody rAddress = RequestBody.create(MediaType.parse("text/plain"), address.getText().toString());
                    RequestBody rState = RequestBody.create(MediaType.parse("text/plain"), state.getText().toString());
                    RequestBody rDistrict = RequestBody.create(MediaType.parse("text/plain"), district.getText().toString());
                    RequestBody rSeller = RequestBody.create(MediaType.parse("text/plain"), sSeller);
                    RequestBody rFssaiNo = RequestBody.create(MediaType.parse("text/plain"), licenseNumber.getText().toString());
                    MultipartBody.Part proImageFilePart = null;
                    MultipartBody.Part licenseImageFilePart = null;

                    try {
                       proImageFilePart  = MultipartBody.Part.createFormData("avatar", proImageFile.getName(), RequestBody.create(MediaType.parse("image/*"), proImageFile));

                    } catch (NullPointerException e) {

                    }
                    try {
                        licenseImageFilePart = MultipartBody.Part.createFormData("document", licenseImageFile.getName(), RequestBody.create(MediaType.parse("image/*"), licenseImageFile));
                    } catch (NullPointerException e) {

                    }
                    Api api = ApiClient.cattleFarm().create(Api.class);
                    api.EDIT_FARM_PROFILE_ROOT_CALL(rName,rFarmId,rMailId,rPhone,rAddress,rState,rDistrict,rSeller,rFssaiNo,proImageFilePart,licenseImageFilePart).enqueue(new Callback<EditFarmProfileRoot>() {
                        @Override
                        public void onResponse(Call<EditFarmProfileRoot> call, Response<EditFarmProfileRoot> response) {
                            if (response.isSuccessful()){
                                EditFarmProfileRoot editFarmProfileRoot=response.body();
                                if (editFarmProfileRoot.status){

                                    SharedPreferences sPP = getContext().getSharedPreferences("cattle_pref", MODE_PRIVATE);
                                    SharedPreferences.Editor myEditor=sPP.edit();
                                    myEditor.putString("district",district.getText().toString());
                                    myEditor.commit();

                                    FarmProfileFragment farmProfileFragment = new FarmProfileFragment();
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                    fragmentTransaction.replace(R.id.container, farmProfileFragment);
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();

                                    Toast.makeText(getContext(), "Updated Sucessfully", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(getContext(), "Server Error", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(getContext(), "Server Error", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<EditFarmProfileRoot> call, Throwable t) {

                        }
                    });

                }




            }
        });




        return view;
    }

    private void initView(View view) {
        rlOne = view.findViewById(R.id.rl_one);
        proImg = view.findViewById(R.id.pro_img);
        editProImg = view.findViewById(R.id.edit_pro_img);
        farmName = view.findViewById(R.id.farm_name);
        mailId = view.findViewById(R.id.mail_id);
        address = view.findViewById(R.id.address);
        state = view.findViewById(R.id.state);
        district = view.findViewById(R.id.district);
        licenseNumber = view.findViewById(R.id.license_number);
        fssaiLicenseImagePath = view.findViewById(R.id.fssai_license_image_path);
        fssaiLicenseBt = view.findViewById(R.id.fssai_license_bt);
        submitBtnLayout = view.findViewById(R.id.submit_btn_layout);
        submitBt = view.findViewById(R.id.submit_bt);
    }

    public boolean checkAllFields(){
        if (farmName.getText().toString().isEmpty()){
            farmName.setError("mandatory");
            return false;
        }
        if (mailId.getText().toString().isEmpty()){
            mailId.setError("mandatory");
            return false;
        }
        if (address.getText().toString().isEmpty()){
            address.setError("mandatory");
            return false;
        }
        if (state.getText().toString().isEmpty()){
            state.setError("mandatory");
            return false;
        }
        if (district.getText().toString().isEmpty()){
            district.setError("mandatory");
            return false;
        }
        if (licenseNumber.getText().toString().isEmpty()|| !(licenseNumber.getText().toString().trim().length() == 14)){
            licenseNumber.setError("mandatory");
            return false;
        }
        if (fssaiLicenseImagePath.getText().toString().isEmpty()){
            fssaiLicenseImagePath.setError("mandatory");
            return false;
        }
        return true;
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
        if (requestCode == RESULT_LOAD_PRO_IMAGE && resultCode == RESULT_OK && null != data) {
            try {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContext().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                proImageFile = new File(picturePath);

                final InputStream imageStream = getContext().getContentResolver().openInputStream(selectedImage);
                final Bitmap selectedImageBit = BitmapFactory.decodeStream(imageStream);
                proImg.setImageBitmap(selectedImageBit);

            } catch (Exception e) {

            }
        }
        if (requestCode == RESULT_LICENSE_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            try {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContext().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                licenseImageFile = new File(picturePath);
                fssaiLicenseImagePath.setText(picturePath);
            } catch (Exception e) {

            }
        }
    }
}