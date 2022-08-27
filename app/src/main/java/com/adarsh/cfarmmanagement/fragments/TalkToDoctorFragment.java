package com.adarsh.cfarmmanagement.fragments;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import com.adarsh.cfarmmanagement.HomeActivity;
import com.adarsh.cfarmmanagement.R;
import com.adarsh.cfarmmanagement.Retrofit.Api;
import com.adarsh.cfarmmanagement.Retrofit.ApiClient;
import com.adarsh.cfarmmanagement.model.FarmDocChatRoot;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TalkToDoctorFragment extends Fragment {

    public HomeActivity activity;
    Button videoUploadBt, imageUploadBt;
    TextView messageSendBt;
    TextInputEditText videoPath, imagePath, messageTitle, message;

    // Defining Permission codes.
    private static final int STORAGE_PERMISSION_CODE = 101;
    private static final int RESULT_LOAD_VIDEO = 102;
    private static final int RESULT_LOAD_IMAGE = 103;

    private File imageFile, videoFile;

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        this.activity = (HomeActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_talk_to_doctor, container, false);

        videoUploadBt = view.findViewById(R.id.talk_to_doc_video_upload_bt);
        videoPath = view.findViewById(R.id.talk_to_doc_video_path);
        imagePath = view.findViewById(R.id.talk_to_doc_image_path);
        imageUploadBt = view.findViewById(R.id.talk_to_doc_image_upload_bt);
        messageSendBt = view.findViewById(R.id.message_send_bt);
        messageTitle = view.findViewById(R.id.talk_to_doc_msg_title);
        message = view.findViewById(R.id.talk_to_doc_msg);


        SharedPreferences sharedPreferences = getContext().getSharedPreferences("cattle_pref", Context.MODE_PRIVATE);
        String farm_id = sharedPreferences.getString("farm_id", "");
        String docId = getArguments().getString("docId");
        String sender = "farm";

        videoUploadBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
                } else {
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_VIDEO);
                }
            }
        });

        imageUploadBt.setOnClickListener(new View.OnClickListener() {
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

        messageSendBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (messageTitle.getText().toString().isEmpty()) {
                    messageTitle.setError("Mandatory");
                } else if (message.getText().toString().isEmpty()) {
                    message.setError("Mandatory");
                } else {
                    RequestBody sMsgTitle = RequestBody.create(MediaType.parse("text/plain"), messageTitle.getText().toString());
                    RequestBody sMessage = RequestBody.create(MediaType.parse("text/plain"), message.getText().toString());
                    RequestBody sFarmId = RequestBody.create(MediaType.parse("text/plain"), farm_id);
                    RequestBody sDocId = RequestBody.create(MediaType.parse("text/plain"), docId);
                    RequestBody sSender = RequestBody.create(MediaType.parse("text/plain"), sender);
                    MultipartBody.Part imageFilePart = null;
                    MultipartBody.Part videoFilePart = null;
                    try {
                        imageFilePart = MultipartBody.Part.createFormData("image", imageFile.getName(), RequestBody.create(MediaType.parse("image/*"), imageFile));

                    } catch (NullPointerException e) {

                    }
                    try {
                        videoFilePart = MultipartBody.Part.createFormData("video", videoFile.getName(), RequestBody.create(MediaType.parse("image/*"), videoFile));
                    } catch (NullPointerException e) {

                    }
                    Api api = ApiClient.cattleFarm().create(Api.class);
                    api.FARM_DOC_CHAT_ROOT_CALL(sFarmId, sDocId, sMsgTitle, sMessage, imageFilePart, videoFilePart, sSender).enqueue(new Callback<FarmDocChatRoot>() {
                        @Override
                        public void onResponse(Call<FarmDocChatRoot> call, Response<FarmDocChatRoot> response) {
                            if (response.isSuccessful()) {
                                FarmDocChatRoot farmDocChatRoot = response.body();
                                if (farmDocChatRoot.status) {
                                    Toast.makeText(activity, "sent", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<FarmDocChatRoot> call, Throwable t) {
                            Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
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
        if (requestCode == RESULT_LOAD_VIDEO && resultCode == RESULT_OK && null != data) {
            Uri selectedVideo = data.getData();
            String[] filePathColumn = {MediaStore.Video.Media.DATA};
            Cursor cursor = getContext().getContentResolver().query(selectedVideo, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String sVideoPath = cursor.getString(columnIndex);
            cursor.close();
            try {
                videoFile = new File(sVideoPath);
                videoPath.setText(sVideoPath);
            } catch (Exception e) {

            }

        }
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContext().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            try {
                imageFile = new File(picturePath);
                imagePath.setText(picturePath);
            } catch (Exception e) {

            }

        }
    }
}