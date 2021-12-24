package com.flipthenumber.beta.ui.activities.userview.presenter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;

import com.flipthenumber.beta.Constants.MessageInterface;
import com.flipthenumber.beta.Constants.UserConstant;
import com.flipthenumber.beta.Helper.CustomToast;
import com.flipthenumber.beta.Helper.SharedHelper;
import com.flipthenumber.beta.model.UserModel;
import com.flipthenumber.beta.retrofit.ApiInterface;
import com.flipthenumber.beta.retrofit.AppConfig;
import com.flipthenumber.beta.ui.activities.userview.LoginActivity;

import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivityPresenter implements MessageInterface.SignUpPresenter {
    MessageInterface.View view;
    Activity mContext;
    public SignUpActivityPresenter(Activity mContext,MessageInterface.View view) {
        this.mContext = mContext;
        this.view = view;
    }

    @Override
    public void doSignUp(String name, String email, String dob, String gender,String countryCode, String mobile, String pass) {
        String token = SharedHelper.getKey(mContext, UserConstant.firbasetoken);
        System.out.println("+++++++++++++++ "+token);
        ProgressDialog dialog = ProgressDialog.show(mContext, "Loading", "Please wait...", true);
        ApiInterface apiInterface = AppConfig.getRetrofit().create(ApiInterface.class);
        Call<UserModel> call = apiInterface.registerUser(name,email,dob,gender,countryCode,mobile,pass,pass,"Android",token,"user");
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                dialog.dismiss();
                if (response.isSuccessful()){

                    CustomToast.displayMessage(mContext, response.body().getMessage());

                    if(response.body().getStatus()){
                        mContext.startActivity(new Intent(mContext, LoginActivity.class));
                        ((Activity)mContext).finish();
                    }


                }else {

                    CustomToast.displayError(mContext,response.message());

                }
            }
            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                dialog.dismiss();
                CustomToast.displayError(mContext,t.getMessage());
                if (t instanceof SocketTimeoutException){
                    doSignUp(name,email,dob,gender,countryCode,mobile,pass);
                }
            }
        });

    }
}



