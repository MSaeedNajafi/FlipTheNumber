package com.flipthenumber.beta.ui.activities.userview;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.flipthenumber.beta.Constants.MessageInterface;
import com.flipthenumber.beta.Helper.CustomToast;
import com.flipthenumber.beta.model.ModelForgotPassword;
import com.flipthenumber.beta.retrofit.ApiInterface;
import com.flipthenumber.beta.retrofit.AppConfig;
import com.flipthenumber.beta.R;

import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityForgetPassword extends AppCompatActivity implements View.OnClickListener, MessageInterface.View {

    private EditText email_etxt;
    private Button submit_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Init();
        InitUI();
    }

    private void Init() {
        email_etxt=findViewById(R.id.email_etxt);
        submit_btn=findViewById(R.id.submit_btn);
    }

    private void InitUI() {

        submit_btn.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        switch(v.getId()) {

            case R.id.submit_btn:
                isValidateData();
                break;

        }

    }

    private void isValidateData() {

        String email = email_etxt.getText().toString();

        if (TextUtils.isEmpty(email)) {
            OnError("Please Enter your Email Address!");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_etxt.setError("Please enter a Valid Email Address!");
        } else {

           apiCallForgetPassword();

        }
    }

    private void apiCallForgetPassword() {
        ProgressDialog progressDialog=new ProgressDialog(ActivityForgetPassword.this);
        progressDialog.setTitle("Loading");
        progressDialog.show();
        ApiInterface apiInterface = AppConfig.getRetrofit().create(ApiInterface.class);
        Call<ModelForgotPassword> call = apiInterface.forget_pswd(email_etxt.getText().toString().trim());
        call.enqueue(new Callback<ModelForgotPassword>() {
            @Override
            public void onResponse(Call<ModelForgotPassword> call, Response<ModelForgotPassword> response) {
                progressDialog.dismiss();

                if (response.isSuccessful() ) {

                    Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_LONG).show();

                    if(response.body().getStatus()){

                    }
                }

                else {

                    Log.i("ResponseCodeIs",response.code()+"");

                }


            }

            @Override
            public void onFailure(Call<ModelForgotPassword> call, Throwable t) {

                Log.i("ErrorCodeIshere",t.toString());
                progressDialog.dismiss();
                if (t instanceof SocketTimeoutException) {

                }

            }
        });
    }

    @Override
    public void onSuccess(String message) {

    }

    @Override
    public void OnError(String message) {
        CustomToast.displayMessage(ActivityForgetPassword.this, message);
    }
}