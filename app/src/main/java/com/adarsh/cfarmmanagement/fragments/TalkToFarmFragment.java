package com.adarsh.cfarmmanagement.fragments;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.adarsh.cfarmmanagement.DoctorHomeActivity;
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
import soup.neumorphism.NeumorphCardView;

public class TalkToFarmFragment extends Fragment {

    public DoctorHomeActivity activity;
    private NeumorphCardView layout;
    private TextInputEditText talkToDocMsgTitle;
    private TextInputEditText talkToDocMsg;
    private TextInputEditText talkToDocImagePath;
    private Button talkToDocImageUploadBt;
    private LinearLayout loginBtnLayout;
    private TextView messageSendBt;

    // Defining Permission codes.
    private static final int STORAGE_PERMISSION_CODE = 101;
    private static final int RESULT_LOAD_VIDEO = 102;
    private static final int RESULT_LOAD_IMAGE = 103;
    private File imageFile, videoFile;


    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        this.activity = (DoctorHomeActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_talk_to_farm, container, false);

        initView(view);

        String docId = getArguments().getString("docId");
        String farmId = getArguments().getString("farmId");
        String sender = "doctor";

        talkToDocImageUploadBt.setOnClickListener(new View.OnClickListener() {
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
                if (talkToDocMsgTitle.getText().toString().isEmpty()) {
                    talkToDocMsgTitle.setError("Mandatory");
                } else if (talkToDocMsg.getText().toString().isEmpty()) {
                    talkToDocMsg.setError("Mandatory");
                } else {
                    RequestBody sMsgTitle = RequestBody.create(MediaType.parse("text/plain"), talkToDocMsgTitle.getText().toString());
                    RequestBody sMessage = RequestBody.create(MediaType.parse("text/plain"), talkToDocMsg.getText().toString());
                    RequestBody sFarmId = RequestBody.create(MediaType.parse("text/plain"), farmId);
                    RequestBody sDocId = RequestBody.create(MediaType.parse("text/plain"), docId);
                    RequestBody sSender = RequestBody.create(MediaType.parse("text/plain"), sender);
                    MultipartBody.Part imageFilePart = null;
                    MultipartBody.Part videoFilePart = null;
                    try {
                        imageFilePart = MultipartBody.Part.createFormData("image", imageFile.getName(), RequestBody.create(MediaType.parse("image/*"), imageFile));
                        videoFilePart = MultipartBody.Part.createFormData("video", videoFile.getName(), RequestBody.create(MediaType.parse("image/*"), videoFile));
                    } catch (NullPointerException e) {

                    }
                    Api api = ApiClient.cattleFarm().create(Api.class);
                    api.FARM_DOC_CHAT_ROOT_CALL(sFarmId,sDocId,sMsgTitle,sMessage,imageFilePart,videoFilePart,sSender).enqueue(new Callback<FarmDocChatRoot>() {
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


    private void initView(View view) {
        layout = view.findViewById(R.id.layout);
        talkToDocMsgTitle = view.findViewById(R.id.talk_to_doc_msg_title);
        talkToDocMsg = view.findViewById(R.id.talk_to_doc_msg);
        talkToDocImagePath = view.findViewById(R.id.talk_to_doc_image_path);
        talkToDocImageUploadBt = view.findViewById(R.id.talk_to_doc_image_upload_bt);
        loginBtnLayout = view.findViewById(R.id.login_btn_layout);
        messageSendBt = view.findViewById(R.id.message_send_bt);
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
                talkToDocImagePath.setText(picturePath);
            } catch (Exception e) {

            }

        }
    }
}