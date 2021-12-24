package com.flipthenumber.beta.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flipthenumber.beta.Constants.UserConstant;
import com.flipthenumber.beta.Helper.SharedHelper;
import com.flipthenumber.beta.adapter.MyRequestAdapter;
import com.flipthenumber.beta.model.MyRequestModel;
import com.flipthenumber.beta.retrofit.ApiInterface;
import com.flipthenumber.beta.retrofit.AppConfig;
import com.flipthenumber.beta.R;

import java.net.SocketTimeoutException;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RequestFragment extends Fragment {
    @BindView(R.id.request_RV)
    RecyclerView request_RV;
    @BindView(R.id.no_txt)
    TextView no_txt;
    String token;
    String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_request, container, false);

        init(view);
        initUI();
        return view;
    }

    private void init(View view) {
        ButterKnife.bind(this, view);
        token = SharedHelper.getKey(getActivity(), UserConstant.userToken);
        userId = SharedHelper.getKey(getActivity(), UserConstant.id);
        System.out.println("+++++++++++++++ "+token);
    }

    private void initUI() {
        getAllRequest();

    }

    private void getAllRequest() {
        ApiInterface apiInterface = AppConfig.getRetrofit().create(ApiInterface.class);
        Call<MyRequestModel> call = apiInterface.getMyRequest("Bearer "+token,userId);
        call.enqueue(new Callback<MyRequestModel>() {
            @Override
            public void onResponse(Call<MyRequestModel> call, Response<MyRequestModel> response) {

                if (response.isSuccessful()) {
                    if (response.code()==200){

                        try {
                            if (response.body().getData().size() > 0) {
                                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                request_RV.setLayoutManager(layoutManager);
                                request_RV.setAdapter(new MyRequestAdapter(getActivity(), response.body()));
                            } else {
                                request_RV.setVisibility(View.GONE);
                                no_txt.setVisibility(View.VISIBLE);
                            }
                        }catch (Exception e){

                        }


                    }
                }else {
                    request_RV.setVisibility(View.GONE);
                    no_txt.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Call<MyRequestModel> call, Throwable t) {
                Log.d("RewusetFragment",t.getMessage());
                if (t instanceof SocketTimeoutException) {
                    getAllRequest();
                }

            }
        });


    }
}