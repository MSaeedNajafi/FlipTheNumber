package com.flipthenumber.beta.ui.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flipthenumber.beta.Constants.UserConstant;
import com.flipthenumber.beta.Helper.SharedHelper;
import com.flipthenumber.beta.adapter.FriendAdapter;
import com.flipthenumber.beta.model.FriendsModel;
import com.flipthenumber.beta.retrofit.ApiInterface;
import com.flipthenumber.beta.retrofit.AppConfig;
import com.flipthenumber.beta.R;

import java.net.SocketTimeoutException;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendsFragment extends Fragment{

    @BindView(R.id.frind_RV)
    RecyclerView frind_RV;
    @BindView(R.id.no_txt)
    TextView no_txt;
    String token;
    String userId;
    EditText et_search_username;
    FriendAdapter friendAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        init(view);
        initUI();
        return view;
    }

    private void init(View view) {
        ButterKnife.bind(this, view);
        token = SharedHelper.getKey(getActivity(), UserConstant.userToken);
        userId = SharedHelper.getKey(getActivity(), UserConstant.id);
        System.out.println("+++++++++++++++ "+token);
        Log.i("token_is",token);

        et_search_username=view.findViewById(R.id.et_search_username);

    }

    private void initUI() {

        getAllFriend();

    }

    private void getAllFriend() {
        ProgressDialog progressDialog=new ProgressDialog(getActivity());
        progressDialog.setTitle("Loading");
        progressDialog.show();
        ApiInterface apiInterface = AppConfig.getRetrofit().create(ApiInterface.class);
        Call<FriendsModel> call = apiInterface.getAllFriends("Bearer "+token,userId);
        call.enqueue(new Callback<FriendsModel>() {
            @Override
            public void onResponse(Call<FriendsModel> call, Response<FriendsModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {

                    try {
                        if (response.body().getData().size() > 0) {

                            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                            frind_RV.setLayoutManager(layoutManager);
                            friendAdapter = new FriendAdapter(getActivity(),response.body().getData());
                            frind_RV.setAdapter(friendAdapter);


                        } else {
                            frind_RV.setVisibility(View.GONE);
                            no_txt.setVisibility(View.VISIBLE);
                        }
                    }catch (Exception e){

                    }



                }else {
                    frind_RV.setVisibility(View.GONE);
                    no_txt.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<FriendsModel> call, Throwable t) {
                progressDialog.dismiss();
                System.out.println("++++++ "+t.getMessage());
                if (t instanceof SocketTimeoutException) {
                    getAllFriend();
                }

            }
        });
    }



}