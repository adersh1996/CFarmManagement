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
import android.widget.Toast;

import com.adarsh.cfarmmanagement.DoctorHomeActivity;
import com.adarsh.cfarmmanagement.R;
import com.adarsh.cfarmmanagement.Retrofit.Api;
import com.adarsh.cfarmmanagement.Retrofit.ApiClient;
import com.adarsh.cfarmmanagement.adapters.DocChatAdapter;
import com.adarsh.cfarmmanagement.model.DocChatFarmDetailRoot;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DoctorChatFragment extends Fragment {

    public DoctorHomeActivity activity;
    RecyclerView recyclerView;

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        this.activity = (DoctorHomeActivity) activity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.fragment_doctor_chat, container, false);

       recyclerView=view.findViewById(R.id.doc_chat_rv);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("doctor_pref", Context.MODE_PRIVATE);
        String docId = sharedPreferences.getString("docId", "");

        Api api = ApiClient.cattleFarm().create(Api.class);
        api.DOC_CHAT_FARM_DETAIL_ROOT_CALL(docId).enqueue(new Callback<DocChatFarmDetailRoot>() {
            @Override
            public void onResponse(Call<DocChatFarmDetailRoot> call, Response<DocChatFarmDetailRoot> response) {
                if (response.isSuccessful()){
                    DocChatFarmDetailRoot docChatFarmDetailRoot= response.body();
                    if (docChatFarmDetailRoot.status){
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,false);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        DocChatAdapter docChatAdapter=new DocChatAdapter(docChatFarmDetailRoot,getActivity());
                        recyclerView.setAdapter(docChatAdapter);
                    }else {
                        Toast.makeText(activity, "No Messages Available", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<DocChatFarmDetailRoot> call, Throwable t) {

                Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();

            }
        });

        return view;
    }
}