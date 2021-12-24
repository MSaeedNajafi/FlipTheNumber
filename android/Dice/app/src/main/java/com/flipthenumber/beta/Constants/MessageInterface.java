package com.flipthenumber.beta.Constants;

public interface MessageInterface {

    interface View{
        void onSuccess(String message);
        void OnError(String  message);
    }

    interface PresenterLogin{
        void doLogin(String email, String pass);
        void doGuestLogin();
        void doSocialLogin(String name,String email,String type,String device_type,String device_token);
        void doEditProfile(String user_id,String name,String gender,String dob,String countryCode,String mobile);
    }

    interface SignUpPresenter{
        void doSignUp(String name,String email,String dob,String gender,String countryCode,String mobile,String pass);
    }



}
