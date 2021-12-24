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
import com.flipthenumber.beta.adapter.InviteFriendAdapter;
import com.flipthenumber.beta.model.InviteModel;
import com.flipthenumber.beta.retrofit.ApiInterface;
import com.flipthenumber.beta.retrofit.AppConfig;
import com.flipthenumber.beta.R;

import java.net.SocketTimeoutException;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class InvitesFragment extends Fragment {

    @BindView(R.id.invites_RV)
    RecyclerView invites_RV;
    @BindView(R.id.no_txt)
    TextView no_txt;
    String token;
    String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_invites, container, false);
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

        getInviteFriend();
    }

    private void getInviteFriend() {

        Log.i("token",token);
        Log.i("userId",userId);

        ApiInterface apiInterface = AppConfig.getRetrofit().create(ApiInterface.class);
        Call<InviteModel> call = apiInterface.getInvitesFriends("Bearer "+token,userId);
        call.enqueue(new Callback<InviteModel>() {
            @Override
            public void onResponse(Call<InviteModel> call, Response<InviteModel> response) {
                if (response.isSuccessful()) {
                    if (response.code()==200){

                        try {
                            if (response.body().getData().size() > 0) {
                                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                invites_RV.setLayoutManager(layoutManager);
                                invites_RV.setAdapter(new InviteFriendAdapter(getActivity(), response.body()));
                            } else {
                                invites_RV.setVisibility(View.GONE);
                                no_txt.setVisibility(View.VISIBLE);
                            }
                        }catch (Exception e){

                        }


                    }

                }else {
                    invites_RV.setVisibility(View.GONE);
                    no_txt.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<InviteModel> call, Throwable t) {
                System.out.println("++++++ "+t.getMessage());
                if (t instanceof SocketTimeoutException) {
                    getInviteFriend();
                }

            }
        });


    }
}