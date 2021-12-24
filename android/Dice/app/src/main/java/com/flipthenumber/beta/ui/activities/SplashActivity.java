package com.flipthenumber.beta.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.flipthenumber.beta.Constants.UserConstant;
import com.flipthenumber.beta.Helper.SharedHelper;
import com.flipthenumber.beta.ui.activities.userview.LoginSingupActivity;
import com.flipthenumber.beta.MainActivity;
import com.flipthenumber.beta.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        getToken();
        init();
        initUI();
    }

    private void init(){

        ButterKnife.bind(this);

    }

    private void initUI() {

        boolean token = SharedHelper.getBooleanKey(SplashActivity.this, UserConstant.isLogin);


        Thread myThread = new Thread(){
            @Override
            public void run(){
                try {
                    sleep(5000);
                    if (token) {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));

                    } else {
                        startActivity(new Intent(SplashActivity.this, LoginSingupActivity.class));
                    }
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();



    }
    private void getToken(){

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        SharedHelper.putKey(SplashActivity.this, UserConstant.firbasetoken, token);
                        // Log and toast

                        Log.d(TAG, token);
                        Log.i("TokenIsHereSplash",token);
                        // Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}