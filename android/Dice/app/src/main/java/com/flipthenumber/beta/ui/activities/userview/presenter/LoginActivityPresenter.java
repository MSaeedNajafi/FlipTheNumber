package com.flipthenumber.beta.ui.activities.userview.presenter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;

import com.flipthenumber.beta.Constants.MessageInterface;
import com.flipthenumber.beta.Constants.UserConstant;
import com.flipthenumber.beta.Helper.CustomToast;
import com.flipthenumber.beta.Helper.SharedHelper;
import com.flipthenumber.beta.model.UserModel;
import com.flipthenumber.beta.retrofit.ApiInterface;
import com.flipthenumber.beta.retrofit.AppConfig;
import com.flipthenumber.beta.MainActivity;

import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivityPresenter implements MessageInterface.PresenterLogin {


    Activity mContext;

    public LoginActivityPresenter(Activity mContext) {
        this.mContext = mContext;

    }

    @Override
    public void doLogin(String email, String pass) {
        ProgressDialog dialog = ProgressDialog.show(mContext, "Loading", "Please wait...", true);
        ApiInterface apiInterface = AppConfig.getRetrofit().create(ApiInterface.class);
        Call<UserModel> call = apiInterface.loginUser(email, pass);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {

                    Log.i("ResponseCodeIsHere",response.code()+"");

                    CustomToast.displayMessage(mContext, response.body().getMessage());


                    if(response.body().getStatus()){


                        SharedHelper.clearSharedPreferences(mContext);

                        SharedHelper.putBooleanKey(mContext, UserConstant.isLogin, true);
                        SharedHelper.putBooleanKey(mContext, UserConstant.isLoginGuest, false);

                        SharedHelper.putKey(mContext, UserConstant.userToken, String.valueOf(response.body().getData().get(0).getToken()));
                        SharedHelper.putKey(mContext, UserConstant.id, String.valueOf(response.body().getData().get(0).getUserId()));
                        SharedHelper.putKey(mContext, UserConstant.name, response.body().getData().get(0).getName());
                        SharedHelper.putKey(mContext, UserConstant.email, response.body().getData().get(0).getEmail());
                        SharedHelper.putKey(mContext, UserConstant.dob, response.body().getData().get(0).getDob());
                        SharedHelper.putKey(mContext, UserConstant.countryCode, response.body().getData().get(0).getCountryCode());
                        SharedHelper.putKey(mContext, UserConstant.mobile, response.body().getData().get(0).getMobile());
                        SharedHelper.putKey(mContext, UserConstant.gender, response.body().getData().get(0).getGender());
                        SharedHelper.putKey(mContext, UserConstant.password,pass);
                        SharedHelper.putKey(mContext, UserConstant.userImage,response.body().getData().get(0).getUserImage());



                        Log.i("UserIdIsHere",""+response.body().getData().get(0).getUserId()+"");

                       // SharedHelper.putKey(mContext, UserConstant.coin, String.valueOf(response.body().getData().get(0).getTotalToken()));
                       // SharedHelper.putKey(mContext, UserConstant.zokar, String.valueOf(response.body().getData().get(0).getTotalJoker()));

                        mContext.startActivity(new Intent(mContext, MainActivity.class));


                        ((Activity) mContext).finish();


                    }




                } else {
                    Log.i("ResponseCodeIsHere",response.code()+"");
                    CustomToast.displayError(mContext, response.message());
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                dialog.dismiss();
                CustomToast.displayError(mContext, t.getMessage());
                if (t instanceof SocketTimeoutException) {
                    doLogin(email, pass);
                }
            }
        });


    }

    @Override
    public void doGuestLogin() {

        String token = SharedHelper.getKey(mContext, UserConstant.firbasetoken);
        ProgressDialog dialog = ProgressDialog.show(mContext, "Loading", "Please wait...", true);
        ApiInterface apiInterface = AppConfig.getRetrofit().create(ApiInterface.class);
        Call<UserModel> call = apiInterface.registerGuestUser("GuestUser", token, "Android", "guest");
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    CustomToast.displayMessage(mContext,"Guest User Register");
                    SharedHelper.clearSharedPreferences(mContext);
                    SharedHelper.putBooleanKey(mContext, UserConstant.isLogin, true);
                    SharedHelper.putBooleanKey(mContext, UserConstant.isLoginGuest, true);
                    SharedHelper.putKey(mContext, UserConstant.id, String.valueOf(response.body().getData().get(0).getUserId()));
                    SharedHelper.putKey(mContext, UserConstant.userToken, String.valueOf(response.body().getData().get(0).getToken()));
                    SharedHelper.putKey(mContext, UserConstant.name, response.body().getData().get(0).getName());
                    SharedHelper.putKey(mContext, UserConstant.email, response.body().getData().get(0).getEmail());
                    SharedHelper.putKey(mContext, UserConstant.dob, String.valueOf(response.body().getData().get(0).getDob()));
                    SharedHelper.putKey(mContext, UserConstant.countryCode, response.body().getData().get(0).getCountryCode());
                    SharedHelper.putKey(mContext, UserConstant.mobile, response.body().getData().get(0).getMobile());
                    SharedHelper.putKey(mContext, UserConstant.coin, String.valueOf(response.body().getData().get(0).getTotalToken()));
                    SharedHelper.putKey(mContext, UserConstant.zokar, String.valueOf(response.body().getData().get(0).getTotalJoker()));
                    SharedHelper.putKey(mContext, UserConstant.gender, String.valueOf(response.body().getData().get(0).getGender()));
                    mContext.startActivity(new Intent(mContext, MainActivity.class));
                    ((Activity) mContext).finish();
                } else {
                    CustomToast.displayError(mContext, response.message());
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                dialog.dismiss();
                CustomToast.displayError(mContext, t.getMessage());
                if (t instanceof SocketTimeoutException) {
                    doGuestLogin();
                }
            }
        });

    }

    @Override
    public void doSocialLogin(String name, String email, String type, String device_type, String device_token) {

        ProgressDialog dialog = ProgressDialog.show(mContext, "Loading", "Please wait...", true);
        ApiInterface apiInterface = AppConfig.getRetrofit().create(ApiInterface.class);
        Call<UserModel> call = apiInterface.socialLogin(name, email, type, device_type, device_token);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    CustomToast.displayMessage(mContext, response.body().getMessage());

                    if(response.body().getStatus()){
                        SharedHelper.clearSharedPreferences(mContext);
                        SharedHelper.putBooleanKey(mContext, UserConstant.isLogin, true);
                        SharedHelper.putBooleanKey(mContext, UserConstant.isLoginGuest, false);
                        SharedHelper.putKey(mContext, UserConstant.id, String.valueOf(response.body().getData().get(0).getUserId()));
                        SharedHelper.putKey(mContext, UserConstant.userToken, String.valueOf(response.body().getData().get(0).getToken()));
                        SharedHelper.putKey(mContext, UserConstant.name, response.body().getData().get(0).getName());
                        SharedHelper.putKey(mContext, UserConstant.email, response.body().getData().get(0).getEmail());
                        SharedHelper.putKey(mContext, UserConstant.dob, String.valueOf(response.body().getData().get(0).getDob()));
                        SharedHelper.putKey(mContext, UserConstant.countryCode, response.body().getData().get(0).getCountryCode());
                        SharedHelper.putKey(mContext, UserConstant.mobile, response.body().getData().get(0).getMobile());
                        SharedHelper.putKey(mContext, UserConstant.gender, String.valueOf(response.body().getData().get(0).getGender()));
                        SharedHelper.putKey(mContext, UserConstant.userImage,response.body().getData().get(0).getUserImage());
                        mContext.startActivity(new Intent(mContext, MainActivity.class));
                        ((Activity) mContext).finish();
                    }



                } else {

                    CustomToast.displayError(mContext, response.message());
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                dialog.dismiss();
                CustomToast.displayError(mContext, t.getMessage());
                if (t instanceof SocketTimeoutException) {
                    doSocialLogin(name, email, type, device_type, device_token);
                }
            }
        });
    }

    @Override
    public void doEditProfile(String user_id, String name, String gender, String dob,String countryCode,String mobile) {
        ProgressDialog dialog = ProgressDialog.show(mContext, "Loading", "Please wait...", true);
        String userToken = SharedHelper.getKey(mContext, UserConstant.userToken);
        ApiInterface apiInterface = AppConfig.getRetrofit().create(ApiInterface.class);

        Log.i("sending_detail_is",userToken+"\n"+user_id+"\n"+name+"\n"+gender+"\n"+dob+"\n"+countryCode+"\n"+mobile);

        Call<UserModel> call = apiInterface.editProfile("Bearer " + userToken, user_id, name, gender, dob,countryCode,mobile);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    System.out.println("+++++++++++++ "+response.body().getData());
                    CustomToast.displayMessage(mContext, response.body().getMessage());
                   // SharedHelper.putKey(mContext, UserConstant.id, String.valueOf(response.body().getData().get(0).getUserId()));
                    SharedHelper.putKey(mContext, UserConstant.name, response.body().getData().get(0).getName());
                    //SharedHelper.putKey(mContext, UserConstant.email, response.body().getData().get(0).getEmail());
                    //SharedHelper.putKey(mContext, UserConstant.dob, response.body().getData().get(0).getDob());
                    //SharedHelper.putKey(mContext, UserConstant.countryCode, response.body().getData().get(0).getCountryCode());
                   // SharedHelper.putKey(mContext, UserConstant.mobile, response.body().getData().get(0).getMobile());
                   // SharedHelper.putKey(mContext, UserConstant.gender, response.body().getData().get(0).getGender());
                   // SharedHelper.putKey(mContext, UserConstant.coin, String.valueOf(response.body().getData().get(0).getTotalToken()));
                   // SharedHelper.putKey(mContext, UserConstant.zokar, String.valueOf(response.body().getData().get(0).getTotalJoker()));
                    mContext.startActivity(new Intent(mContext, MainActivity.class));
                    ((Activity) mContext).finish();

                } else {

                    CustomToast.displayError(mContext, response.message());
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                dialog.dismiss();
                CustomToast.displayError(mContext, t.getMessage());
                if (t instanceof SocketTimeoutException) {
                    doEditProfile(userToken, name, gender, dob,countryCode,mobile);
                }
            }
        });

    }


}
