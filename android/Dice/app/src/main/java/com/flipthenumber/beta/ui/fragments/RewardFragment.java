package com.flipthenumber.beta.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flipthenumber.beta.Helper.CustomToast;
import com.flipthenumber.beta.adapter.RewardAdapter;
import com.flipthenumber.beta.model.RewardModel;
import com.flipthenumber.beta.retrofit.ApiInterface;
import com.flipthenumber.beta.retrofit.AppConfig;
import com.flipthenumber.beta.R;

import java.net.SocketTimeoutException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RewardFragment extends Fragment {

    private Unbinder unbinder;

    @BindView(R.id.reward_RecyclerView)
    RecyclerView reward_RecyclerView;
     @BindView(R.id.no_txt)
    TextView no_txt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reward, container, false);
        init(view);
        initUI();

        return view;
    }

    private void init(View view) {
        unbinder = ButterKnife.bind(this, view);
    }

    private void initUI() {
        setReward_RecyclerViewData();
    }

    private void setReward_RecyclerViewData() {

      //  ProgressDialog dialog = ProgressDialog.show(getActivity(), "Loading", "Please wait...", true);
        ApiInterface apiInterface = AppConfig.getRetrofit().create(ApiInterface.class);
        Call<RewardModel> call = apiInterface.getReward();
        call.enqueue(new Callback<RewardModel>() {
            @Override
            public void onResponse(Call<RewardModel> call, Response<RewardModel> response) {
               // dialog.dismiss();
                if (response.isSuccessful()){
                    if (response.body().getData().size()>0){

                        try {
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                            reward_RecyclerView.setLayoutManager(layoutManager);
                            reward_RecyclerView.setAdapter(new RewardAdapter(getActivity(), response.body()));
                        }catch (Exception e){

                        }

                    }else {
                        reward_RecyclerView.setVisibility(View.GONE);
                        no_txt.setVisibility(View.VISIBLE);
                    }
                }
            }
            @Override
            public void onFailure(Call<RewardModel> call, Throwable t) {
             //   dialog.dismiss();
                CustomToast.displayError(getActivity(),t.getMessage());
                if (t instanceof SocketTimeoutException){
                    setReward_RecyclerViewData();
                }
            }
        });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

}