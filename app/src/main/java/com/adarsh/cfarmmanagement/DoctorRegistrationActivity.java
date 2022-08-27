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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.adarsh.cfarmmanagement.Retrofit.Api;
import com.adarsh.cfarmmanagement.Retrofit.ApiClient;
import com.adarsh.cfarmmanagement.model.DoctorReg;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorRegistrationActivity extends AppCompatActivity {

    TextInputEditText name, email, phone, password, reTypePassword, address, profilePicPath, experience, qualification, idPicPath;
    TextView regBt;
    Button proPicBt, idPicBt;

    private static final int STORAGE_PERMISSION_CODE = 101;
    private static final int RESULT_LOAD_ID_IMAGE = 102;
    private static final int RESULT_LOAD_PRO_IMAGE = 103;

    private File idImageFile, proImageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_registration);

        name = findViewById(R.id.doc_reg_name);
        email = findViewById(R.id.doc_reg_email);
        phone = findViewById(R.id.doc_reg_phone);
        password = findViewById(R.id.doc_reg_pass);
        reTypePassword = findViewById(R.id.doc_reg_re_typ_pass);
        address = findViewById(R.id.doc_reg_address);
        profilePicPath = findViewById(R.id.doc_reg_pro_pic_path);
        experience = findViewById(R.id.doc_reg_exp);
        qualification = findViewById(R.id.doc_reg_qualification);
        idPicPath = findViewById(R.id.doc_reg_id_path);
        regBt = findViewById(R.id.doc_reg_bt);
        proPicBt = findViewById(R.id.doc_reg_pro_pic_bt);
        idPicBt = findViewById(R.id.doc_reg_id_up_bt);
        idPicPath.setEnabled(false);
        profilePicPath.setEnabled(false);


        proPicBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(DoctorRegistrationActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
                } else {
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_PRO_IMAGE);
                }
            }
        });
        idPicBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(DoctorRegistrationActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
                } else {
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_ID_IMAGE);
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
                } else if (qualification.getText().toString().isEmpty()) {
                    qualification.setError("mandatory");
                } else if (experience.getText().toString().isEmpty()) {
                    experience.setError("mandatory");
                } else if (profilePicPath.getText().toString().isEmpty()) {
                    profilePicPath.setError("mandatory");
                } else if (idPicPath.getText().toString().isEmpty()) {
                    idPicPath.setError("mandatory");
                } else if (!(password.getText().toString().equals(reTypePassword.getText().toString()))) {
                    reTypePassword.setError("Password mismatch");
                } else {
                    RequestBody sName = RequestBody.create(MediaType.parse("text/plain"), name.getText().toString());
                    RequestBody sEmail = RequestBody.create(MediaType.parse("text/plain"), email.getText().toString());
                    RequestBody sPhone = RequestBody.create(MediaType.parse("text/plain"), phone.getText().toString());
                    RequestBody sPassword = RequestBody.create(MediaType.parse("text/plain"), reTypePassword.getText().toString());
                    RequestBody sAddress = RequestBody.create(MediaType.parse("text/plain"), address.getText().toString());
                    RequestBody sQualification = RequestBody.create(MediaType.parse("text/plain"), qualification.getText().toString());
                    RequestBody sExperience = RequestBody.create(MediaType.parse("text/plain"), experience.getText().toString());
                    MultipartBody.Part idPicFIlePart = MultipartBody.Part.createFormData("avatar", idImageFile.getName(), RequestBody.create(MediaType.parse("image/*"), idImageFile));
                    MultipartBody.Part proPicFilePart = MultipartBody.Part.createFormData("icon", proImageFile.getName(), RequestBody.create(MediaType.parse("image/*"), proImageFile));

                    Api api = ApiClient.cattleFarm().create(Api.class);
                    api.DOCTOR_REG_CALL(sName, sEmail, sPhone, sAddress, sPassword, sQualification, sExperience, idPicFIlePart, proPicFilePart).enqueue(new Callback<DoctorReg>() {
                        @Override
                        public void onResponse(Call<DoctorReg> call, Response<DoctorReg> response) {

                            if (response.isSuccessful()) {
                                DoctorReg doctorReg = response.body();
                                Toast.makeText(DoctorRegistrationActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(DoctorRegistrationActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(DoctorRegistrationActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<DoctorReg> call, Throwable t) {
                            Toast.makeText(DoctorRegistrationActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });


    }

    // Function to check and request permission.
    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(DoctorRegistrationActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(DoctorRegistrationActivity.this, new String[]{permission}, requestCode);
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
        if (requestCode == RESULT_LOAD_ID_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            idImageFile = new File(picturePath);
            idPicPath.setText(picturePath);
        }
        if (requestCode == RESULT_LOAD_PRO_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            proImageFile = new File(picturePath);
            profilePicPath.setText(picturePath);
        }
    }
}