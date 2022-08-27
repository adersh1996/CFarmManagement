package com.adarsh.cfarmmanagement.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adarsh.cfarmmanagement.DoctorHomeActivity;
import com.adarsh.cfarmmanagement.R;
import com.adarsh.cfarmmanagement.Retrofit.Api;
import com.adarsh.cfarmmanagement.Retrofit.ApiClient;
import com.adarsh.cfarmmanagement.adapters.DocChatHistoryAdapter;
import com.adarsh.cfarmmanagement.model.DocChatHistoryRoot;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DocChatHistoryFragment extends Fragment {


    RecyclerView docChatHistoryRv;
    ExtendedFloatingActionButton talkToFarmBt;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doc_chat_history, container, false);

        docChatHistoryRv = view.findViewById(R.id.doc_chat_history_rv);
        talkToFarmBt = view.findViewById(R.id.talk_to_farm);

        String farmId = getArguments().getString("farmId");
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("doctor_pref", Context.MODE_PRIVATE);
        String docId = sharedPreferences.getString("docId", "");

        Api api = ApiClient.cattleFarm().create(Api.class);
        api.DOC_CHAT_HISTORY_ROOT_CALL(farmId, docId).enqueue(new Callback<DocChatHistoryRoot>() {
            @Override
            public void onResponse(Call<DocChatHistoryRoot> call, Response<DocChatHistoryRoot> response) {
                if (response.isSuccessful()) {
                    DocChatHistoryRoot docChatHistoryRoot = response.body();
                    if (docChatHistoryRoot.status) {
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
                        docChatHistoryRv.setLayoutManager(linearLayoutManager);
                        DocChatHistoryAdapter docChatHistoryAdapter = new DocChatHistoryAdapter(getActivity(), docChatHistoryRoot);
                        docChatHistoryRv.setAdapter(docChatHistoryAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<DocChatHistoryRoot> call, Throwable t) {

            }
        });

        talkToFarmBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        TalkToFarmFragment talkToFarmFragment=new TalkToFarmFragment();
                Bundle arguments = new Bundle();
                arguments.putString("docId", docId);
                arguments.putString("farmId", farmId);
                talkToFarmFragment.setArguments(arguments);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.doctor_home_container, talkToFarmFragment, "talkToFarmFragment").addToBackStack(null).commit();
            }
        });


        return view;
    }
}