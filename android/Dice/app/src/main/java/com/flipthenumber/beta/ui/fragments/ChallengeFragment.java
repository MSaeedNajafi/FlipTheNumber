package com.flipthenumber.beta.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import com.flipthenumber.beta.R;

public class ChallengeFragment extends Fragment {

    private View view;
    private ImageView logo_img;
    private RelativeLayout profile_userone_r_layout;
    private RelativeLayout profile_user_two_r_layout;
    private ImageView red_dice_img;
    private ImageView yellow_dice_img;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.activity_challenge, container, false);


        Init();
        InitUI();

        return view;
    }

    private void Init() {
        logo_img=view.findViewById(R.id.logo_img);
        profile_userone_r_layout=view.findViewById(R.id.profile_one_layout);
        profile_user_two_r_layout=view.findViewById(R.id.profile_two_layout);
        red_dice_img=view.findViewById(R.id.red_dice_img);
        yellow_dice_img=view.findViewById(R.id.yellow_dice_img);
    }

    private void InitUI() {
        fadeLogoAnimation();
        userProfileAnimation();
    }


    private void fadeLogoAnimation(){

        Thread timer = new Thread() {
            public void run() {
                try {

                    Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade);
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
        profile_userone_r_layout.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.left_to_right));
        profile_user_two_r_layout.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.right_to_left));
        Thread myThread = new Thread(){
            @Override
            public void run(){
//                try {
//                    sleep(3500);
//                    startActivity(new Intent(ChallengeActivity.this, PlayWithComputerActivity.class));
//                    finish();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
        };
        myThread.start();

    }




}
