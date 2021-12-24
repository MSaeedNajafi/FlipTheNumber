package com.flipthenumber.beta.ui.activities.userview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.flipthenumber.beta.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class LoginSingupActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.login_btn)
    Button login_btn;
    @BindView(R.id.singup_btn)
    Button singup_btn;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_singup);

        init();
        initUI();

    }

    private void init() {
        unbinder = ButterKnife.bind(this);
    }

    private void initUI() {
        login_btn.setOnClickListener(this::onClick);
        singup_btn.setOnClickListener(this::onClick);
    }




    @Override
    public void onClick(View view) {


        switch (view.getId()) {

            case R.id.login_btn:
                startActivity(new Intent(LoginSingupActivity.this, LoginActivity.class));
                break;

            case R.id.singup_btn:
                startActivity(new Intent(LoginSingupActivity.this, SignUpActivity.class));
                break;


        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }


}