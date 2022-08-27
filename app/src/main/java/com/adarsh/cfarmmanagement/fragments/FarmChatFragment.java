package com.adarsh.cfarmmanagement.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.adarsh.cfarmmanagement.HomeActivity;
import com.adarsh.cfarmmanagement.R;
import com.adarsh.cfarmmanagement.Retrofit.Api;
import com.adarsh.cfarmmanagement.Retrofit.ApiClient;
import com.adarsh.cfarmmanagement.adapters.DocChatHistoryAdapter;
import com.adarsh.cfarmmanagement.adapters.FarmChatHistoryAdapter;
import com.adarsh.cfarmmanagement.model.DocChatHistoryRoot;
import com.adarsh.cfarmmanagement.model.RecentChatFromDocRoot;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FarmChatFragment extends Fragment {
    public HomeActivity activity;
    ExtendedFloatingActionButton talkToDoctorBt;
    TextView msgTitle, msg, msgDate, msgTime, chatHistory;
    ImageView msgImg;
    RecyclerView farmChatHistoryRv;


    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        this.activity = (HomeActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_farm_chat, container, false);

        talkToDoctorBt = view.findViewById(R.id.talk_to_doctor);
        farmChatHistoryRv=view.findViewById(R.id.farm_chat_history_rv);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("cattle_pref", MODE_PRIVATE);
        String farmId = sharedPreferences.getString("farm_id", null);
        String docId = getArguments().getString("docId");
        Boolean chatStatus = getArguments().getBoolean("chatStatus");


        Api api = ApiClient.cattleFarm().create(Api.class);
        api.DOC_CHAT_HISTORY_ROOT_CALL(farmId, docId).enqueue(new Callback<DocChatHistoryRoot>() {
            @Override
            public void onResponse(Call<DocChatHistoryRoot> call, Response<DocChatHistoryRoot> response) {
                if (response.isSuccessful()) {
                    DocChatHistoryRoot docChatHistoryRoot = response.body();
                    if (docChatHistoryRoot.status) {
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
                        farmChatHistoryRv.setLayoutManager(linearLayoutManager);
                        FarmChatHistoryAdapter farmChatHistoryAdapter = new FarmChatHistoryAdapter(getActivity(), docChatHistoryRoot);
                        farmChatHistoryRv.setAdapter(farmChatHistoryAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<DocChatHistoryRoot> call, Throwable t) {

            }
        });


        talkToDoctorBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TalkToDoctorFragment talkToDoctorFragment = new TalkToDoctorFragment();
                Bundle arguments = new Bundle();
                arguments.putString("docId", docId);
                talkToDoctorFragment.setArguments(arguments);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, talkToDoctorFragment, "talkToDoctorFragment").addToBackStack(null).commit();
            }
        });

        return view;
    }
}