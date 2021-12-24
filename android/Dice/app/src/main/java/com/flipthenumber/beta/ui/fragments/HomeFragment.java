package com.flipthenumber.beta.ui.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flipthenumber.beta.Constants.UserConstant;
import com.flipthenumber.beta.Helper.SharedHelper;
import com.flipthenumber.beta.adapter.ChallengeAdapter;
import com.flipthenumber.beta.model.GetHomePageDetailsModel;
import com.flipthenumber.beta.retrofit.ApiInterface;
import com.flipthenumber.beta.retrofit.AppConfig;
import com.flipthenumber.beta.ui.activities.ActivityPlayerVsPlayerView;
import com.flipthenumber.beta.ui.activities.ActivityPlayerVsPlayerViewNew;
import com.flipthenumber.beta.ui.activities.FriendListActivity;
import com.flipthenumber.beta.ui.activities.PlayPage.PlayWithComputerActivity;
import com.flipthenumber.beta.ui.activities.Privacy_Policy;
import com.flipthenumber.beta.MainActivity;
import com.flipthenumber.beta.R;
import com.flipthenumber.beta.ui.fragments.InviteConcept.FragmentChallangeFriend;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private Unbinder unbinder;
    @BindView(R.id.challenge_recyclerview)
    RecyclerView challenge_recyclerview;
    @BindView(R.id.computer_ll)
    LinearLayout computer_ll;
    @BindView(R.id.tournament_ll)
    LinearLayout tournament_ll;
    @BindView(R.id.challemge_friends_ll)
    LinearLayout challemge_friends_ll;
    ArrayList imageRvList = new ArrayList<Integer>(Arrays.asList(R.drawable.challenge_card, R.drawable.challenge_card, R.drawable.challenge_card, R.drawable.challenge_card));
    @BindView(R.id.shareImg)
    ImageView shareImg;
    @BindView(R.id.likeImg)
    ImageView likeImg;
    @BindView(R.id.settingImg)
    ImageView settingImg;
    AudioManager mAlramMAnager;
    private boolean VolIsMute;
    @BindView(R.id.layout_player_vs_player)
    LinearLayout layout_player_vs_player;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        init(view);
        initUI();
        return view;
    }

    private void init(View view) {
        unbinder = ButterKnife.bind(this, view);
        mAlramMAnager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        VolIsMute = false;
    }

    private void initUI() {
        layout_player_vs_player.setOnClickListener(this::onClick);
        computer_ll.setOnClickListener(this::onClick);
        challemge_friends_ll.setOnClickListener(this::onClick);
        tournament_ll.setOnClickListener(this::onClick);
        settingImg.setOnClickListener(this::onClick);
        likeImg.setOnClickListener(this::onClick);
        shareImg.setOnClickListener(this::onClick);
        setChallenge_recyclerviewData();
        apiCallGetHomeData();
    }

    private void setChallenge_recyclerviewData() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        challenge_recyclerview.setLayoutManager(layoutManager);
        challenge_recyclerview.setAdapter(new ChallengeAdapter(getActivity(), imageRvList));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.computer_ll:
                startActivity(new Intent(getActivity(), PlayWithComputerActivity.class));
                preventTwoClick(computer_ll);
                break;

            case R.id.challemge_friends_ll:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new FragmentChallangeFriend()).commit();
                preventTwoClick(tournament_ll);
                //startActivity(new Intent(getActivity(), FriendListActivity.class));
                //preventTwoClick(challemge_friends_ll);
                break;

            case R.id.tournament_ll:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new EventFragment()).commit();
                preventTwoClick(tournament_ll);
                break;

            case R.id.settingImg:
                showSettingDialog();
                break;

            case R.id.likeImg:
                break;

            case R.id.shareImg:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, R.string.app_name);
                intent.putExtra(android.content.Intent.EXTRA_TEXT, R.string.app_name);
                startActivity(intent);
                break;

            case R.id.layout_player_vs_player:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new FragmentBet()).commit();
                preventTwoClick(layout_player_vs_player);
                //startActivity(new Intent(getActivity(), ActivityPlayerVsPlayerView.class));
                //preventTwoClick(layout_player_vs_player);
                break;

        }
    }
    public void MuteAudio(){

        //mAlramMAnager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mAlramMAnager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_MUTE, 0);
            mAlramMAnager.adjustStreamVolume(AudioManager.STREAM_ALARM, AudioManager.ADJUST_MUTE, 0);
            //mAlramMAnager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0);
            mAlramMAnager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_MUTE, 0);
            mAlramMAnager.adjustStreamVolume(AudioManager.STREAM_SYSTEM, AudioManager.ADJUST_MUTE, 0);
        } else {
            mAlramMAnager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
            mAlramMAnager.setStreamMute(AudioManager.STREAM_ALARM, true);
            //mAlramMAnager.setStreamMute(AudioManager.STREAM_MUSIC, true);
            mAlramMAnager.setStreamMute(AudioManager.STREAM_RING, true);
            mAlramMAnager.setStreamMute(AudioManager.STREAM_SYSTEM, true);
        }
    }

    public void UnMuteAudio(){
        //mAlramMAnager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mAlramMAnager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_UNMUTE, 0);
            mAlramMAnager.adjustStreamVolume(AudioManager.STREAM_ALARM, AudioManager.ADJUST_UNMUTE, 0);
            //mAlramMAnager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE,0);
            mAlramMAnager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_UNMUTE, 0);
            mAlramMAnager.adjustStreamVolume(AudioManager.STREAM_SYSTEM, AudioManager.ADJUST_UNMUTE, 0);
        } else {
            mAlramMAnager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);
            mAlramMAnager.setStreamMute(AudioManager.STREAM_ALARM, false);
            //mAlramMAnager.setStreamMute(AudioManager.STREAM_MUSIC, false);
            mAlramMAnager.setStreamMute(AudioManager.STREAM_RING, false);
            mAlramMAnager.setStreamMute(AudioManager.STREAM_SYSTEM, false);
        }
    }
    public void showSettingDialog() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        View view = getLayoutInflater().inflate(R.layout.setting, null);
        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ImageView soundImg = view.findViewById(R.id.sound_img);
        ImageView musicImg = view.findViewById(R.id.music_img);
        TextView store = view.findViewById(R.id.store);
        TextView policy = view.findViewById(R.id.policy);
        ImageView back = view.findViewById(R.id.back_home);

        boolean isMusic = SharedHelper.getBooleanKey(getActivity(), UserConstant.isMusic);
        if (isMusic) {
            musicImg.setImageResource(R.drawable.music_mute);
        } else {
            musicImg.setImageResource(R.drawable.music);
        }

        boolean isSound= SharedHelper.getBooleanKey(getActivity(),UserConstant.isSound);
        if (isSound){
            soundImg.setImageResource(R.drawable.mute);
        }else {
            soundImg.setImageResource(R.drawable.sound);
        }
        soundImg.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (VolIsMute) {
                    //manager.setStreamMute(AudioManager.STREAM_MUSIC, false);
                    VolIsMute = false;
                    UnMuteAudio();
                    soundImg.setImageResource(R.drawable.sound);
                    SharedHelper.putBooleanKey(getActivity(),UserConstant.isSound,false);

                } else {
                    //manager.setStreamMute(AudioManager.STREAM_MUSIC, true);
                    VolIsMute = true;
                    MuteAudio();
                    soundImg.setImageResource(R.drawable.mute);
                    SharedHelper.putBooleanKey(getActivity(),UserConstant.isSound,true);
                }
            }
        });
        musicImg.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (VolIsMute) {
                    mAlramMAnager.setStreamMute(AudioManager.STREAM_SYSTEM, false);
                    VolIsMute = false;
                    //UnMuteAudio();
                    musicImg.setImageResource(R.drawable.music);
                    SharedHelper.putBooleanKey(getActivity(),UserConstant.isMusic,false);

                } else {
                    mAlramMAnager.setStreamMute(AudioManager.STREAM_SYSTEM, true);
                    VolIsMute = true;
                    //MuteAudio();
                    musicImg.setImageResource(R.drawable.music_mute);
                    SharedHelper.putBooleanKey(getActivity(),UserConstant.isMusic,true);
                }
            }
        });
        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new StoreFragment()).commit();

            }
        });
        policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Privacy_Policy.class);
                /* intent.putExtra(Privacy_Policy.KEY_PRODUCT_ID, product.getId()); */
                startActivity(intent);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        dialog.show();
    }




    private void apiCallGetHomeData() {

        String token = SharedHelper.getKey(getActivity(), UserConstant.userToken);
        String userId = SharedHelper.getKey(getActivity(), UserConstant.id);

        Log.i("tokenHomePage",token+"\n"+userId);

        ApiInterface apiInterface = AppConfig.getRetrofit().create(ApiInterface.class);


        Call<GetHomePageDetailsModel> call = apiInterface.GetHomePageDetails("Bearer "+token,userId);
        call.enqueue(new Callback<GetHomePageDetailsModel>() {
            @Override
            public void onResponse(Call<GetHomePageDetailsModel> call, Response<GetHomePageDetailsModel> response) {
                if (response.isSuccessful()){

                    if (response.code()==200){



                        if(response.body().getStatus()){
                            try {
                                assert response.body() != null;
                                SharedHelper.putKey(getActivity(),UserConstant.coin,response.body().getData().get(0).getTotal_token());
                                SharedHelper.putKey(getActivity(),UserConstant.zokar,response.body().getData().get(0).getTotal_joker());
                                SharedHelper.putKey(getActivity(),UserConstant.point,response.body().getData().get(0).getTotal_points());

                                Log.i("TokensAndCoins","MainActivity"+response.body().getData().get(0).getTotal_token()+"\n"+
                                        response.body().getData().get(0).getTotal_joker());


                                MainActivity.zokar_txt.setText(SharedHelper.getKey(getActivity(), UserConstant.zokar));
                               // MainActivity.ttxt.setText(SharedHelper.getKey(getActivity(), UserConstant.coin));
                                MainActivity.total_coin_txt.setText(SharedHelper.getKey(getActivity(), UserConstant.coin));
                                MainActivity.ttxt.setText(SharedHelper.getKey(getActivity(), UserConstant.point));
                            }catch (Exception e){

                            }

                        }


                    }
                }
            }

            @Override
            public void onFailure(Call<GetHomePageDetailsModel> call, Throwable t) {

            }
        });




    }



    //Prevent double click

    private void preventTwoClick(LinearLayout view){
        view.setEnabled(false);
        view.postDelayed(new Runnable() {
            public void run() {
                view.setEnabled(true);
            }
        }, 500);
    }



}