package com.flipthenumber.beta.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.flipthenumber.beta.ui.activities.PlayPage.PlayWithComputerActivity;
import com.flipthenumber.beta.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChallengeActivity extends AppCompatActivity {

    @BindView(R.id.logo_img)
    ImageView logo_img;
    @BindView(R.id.profile_one_layout)
    RelativeLayout profile_userone_r_layout;
    @BindView(R.id.profile_two_layout)
    RelativeLayout profile_user_two_r_layout;
    @BindView(R.id.red_dice_img)
    ImageView red_dice_img;
    @BindView(R.id.yellow_dice_img)
    ImageView yellow_dice_img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_challenge);

        init();
        initUI();

    }

    private void init() {
        ButterKnife.bind(this);
    }

    private void initUI(){
        fadeLogoAnimation();
        userProfileAnimation();

    }

    private void fadeLogoAnimation(){

        Thread timer = new Thread() {
            public void run() {
                try {
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
                    logo_img.startAnimation(animation);

                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {

                }
            }
        };
        timer.start();

    }

    private void userProfileAnimation(){

        RotateAnimation rotate = new RotateAnimation(0, 720, Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(3000);
        rotate.setInterpolator(new LinearInterpolator());
        red_dice_img.startAnimation(rotate);
        yellow_dice_img.startAnimation(rotate);
        profile_userone_r_layout.setAnimation(AnimationUtils.loadAnimation(this, R.anim.left_to_right));
        profile_user_two_r_layout.setAnimation(AnimationUtils.loadAnimation(this, R.anim.right_to_left));
        Thread myThread = new Thread(){
            @Override
            public void run(){
                try {
                    sleep(3500);
                    startActivity(new Intent(ChallengeActivity.this, PlayWithComputerActivity.class));
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();

    }
}