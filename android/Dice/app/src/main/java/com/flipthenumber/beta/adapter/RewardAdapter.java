package com.flipthenumber.beta.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flipthenumber.beta.Constants.UserConstant;
import com.flipthenumber.beta.MainActivity;
import com.flipthenumber.beta.retrofit.ApiInterface;
import com.flipthenumber.beta.retrofit.AppConfig;
import com.flipthenumber.beta.Helper.SharedHelper;
import com.flipthenumber.beta.R;
import com.flipthenumber.beta.model.ModelCollectReward;
import com.flipthenumber.beta.model.RewardModel;
import com.flipthenumber.beta.ui.activities.PlayPage.PlayVsPlayerSocket;
import com.flipthenumber.beta.ui.activities.userview.SignUpActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RewardAdapter extends RecyclerView.Adapter<RewardAdapter.MyViewHolder> {
    Context mContext;
    RewardModel model;
    // creating RewardedVideoAd object
    private RewardedVideoAd AdMobrewardedVideoAd;
    private String coins;
    // AdMob Rewarded Video Ad Id
    private String AdId
            = "ca-app-pub-3940256099942544/5224354917";
    public RewardAdapter(Context mContext, RewardModel model) {
        this.mContext = mContext;
        this.model = model;
    }


    RelativeLayout layout_ads_one;
    RelativeLayout layout_ads_two;
    RelativeLayout layout_ads_three;
    RelativeLayout layout_ads_four;
    RelativeLayout layout_ads_five;
    RelativeLayout layout_ads_six;


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.reward_item, parent, false);
        // initializing the Google Admob  SDK
        MobileAds.initialize(mContext);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Picasso.get().load( model.getData().get(position).getImage()).into(holder.img);

        holder.collect_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(position==0){
                    showAdsDialog(position);
                    //showDialog(position);
                }
                else {
                    showDialog(position);
                }
            }
        });

        // holder.name_txt.setText(model.getData().get(position).getName());

    }


    private void showDialog(int pos) {



        loadRewardedVideoAd(pos);



//        Dialog dialog = new Dialog(mContext);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setCancelable(true);
//        dialog.setContentView(R.layout.dialog_ads_to_confirm);
//        dialog.show();
//        Window window = dialog.getWindow();
//        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
//
//
//        RelativeLayout layout_ok=dialog.findViewById(R.id.layout_ok);
//        RelativeLayout layout_cancel=dialog.findViewById(R.id.layout_cancel);
//
//        layout_ok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//// loading Video Ad
//                loadRewardedVideoAd(pos);
//            }
//        });
//
//
//
//        layout_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//
//            }
//        });


    }


    private void showDialogNew(int pos,String from) {



        loadRewardedVideoAdNew(pos,from);



//        Dialog dialog = new Dialog(mContext);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setCancelable(true);
//        dialog.setContentView(R.layout.dialog_ads_to_confirm);
//        dialog.show();
//        Window window = dialog.getWindow();
//        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
//
//
//        RelativeLayout layout_ok=dialog.findViewById(R.id.layout_ok);
//        RelativeLayout layout_cancel=dialog.findViewById(R.id.layout_cancel);
//
//        layout_ok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//// loading Video Ad
//                loadRewardedVideoAd(pos);
//            }
//        });
//
//
//
//        layout_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//
//            }
//        });


    }


    @Override
    public int getItemCount() {
        return model.getData().size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img)
        public ImageView img;

        //@BindView(R.id.name_txt)
       // public TextView name_txt;

        @BindView(R.id.collect_img)
        public TextView collect_img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    void loadRewardedVideoAd(int p)
    {

        AdMobrewardedVideoAd = MobileAds.getRewardedVideoAdInstance(mContext);

        AdMobrewardedVideoAd.setRewardedVideoAdListener(
                new RewardedVideoAdListener() {
                    @Override
                    public void onRewardedVideoAdLoaded()
                    {

                        Toast.makeText(mContext,
                                        "onRewardedVideoAdLoaded",
                                        Toast.LENGTH_SHORT).show();

                        showRewardedVideoAd();

                    }

                    @Override
                    public void onRewardedVideoAdOpened()
                    {

                        Toast.makeText(mContext,
                                        "onRewardedVideoAdOpened",
                                        Toast.LENGTH_SHORT)
                                .show();

                    }

                    @Override
                    public void onRewardedVideoStarted()
                    {

                    }

                    @Override
                    public void onRewardedVideoAdClosed()
                    {

                    }

                    @Override
                    public void onRewarded(
                            RewardItem rewardItem)
                    {

                        int amt=rewardItem.getAmount();
                        Log.i("amt_rewarded",String.valueOf(amt));

                        String userId = SharedHelper.getKey(mContext, UserConstant.id);

                        String total_token="";
                        String total_joker="";

                        if(p==0){
                            total_token=coins;
                            total_joker="0";
                        }
                        else {
                            total_token="0";
                            total_joker="1";
                        }

                        Log.i("userIdnData",userId+"\n"+total_token+"\n"+total_joker+"\n"+String.valueOf(amt));

                        ApiInterface apiInterface = AppConfig.getRetrofit().create(ApiInterface.class);

                        Log.i("userIdnData",userId+"\n"+total_token+"\n"+total_joker);

                        Call<ModelCollectReward> call = apiInterface.collect_reward(userId,total_token,total_joker);
                        call.enqueue(new Callback<ModelCollectReward>() {
                            @Override
                            public void onResponse(Call<ModelCollectReward> call, Response<ModelCollectReward> response) {
                                if (response.isSuccessful())
                                {



                                    Toast.makeText(mContext,response.body().getMessage(),Toast.LENGTH_LONG).show();

                                    try {
                                        SharedHelper.putKey(mContext,UserConstant.coin,response.body().getData().get(0).getTotal_token());
                                        MainActivity.total_coin_txt.setText(SharedHelper.getKey(mContext, UserConstant.coin));
                                        SharedHelper.putKey(mContext,UserConstant.zokar,response.body().getData().get(0).getTotal_joker());
                                        MainActivity.zokar_txt.setText(SharedHelper.getKey(mContext, UserConstant.zokar));
                                    }catch (Exception e){

                                    }

                                }
                            }

                            @Override
                            public void onFailure(Call<ModelCollectReward> call, Throwable t) {

                            }
                        });

                    }

                    @Override
                    public void
                    onRewardedVideoAdLeftApplication()
                    {
                        // Showing Toast Message
//                        Toast
//                                .makeText(
//                                        mContext,
//                                        "onRewardedVideoAdLeftApplication",
//                                        Toast.LENGTH_SHORT)
//                                .show();
                    }

                    @Override
                    public void onRewardedVideoAdFailedToLoad(
                            int i)
                    {
                        // Showing Toast Message
//                        Toast
//                                .makeText(
//                                        mContext,
//                                        "onRewardedVideoAdFailedToLoad",
//                                        Toast.LENGTH_SHORT)
//                                .show();
                    }

                    @Override
                    public void onRewardedVideoCompleted()
                    {
                        // Showing Toast Message
//                        Toast
//                                .makeText(
//                                        mContext,
//                                        "onRewardedVideoCompleted",
//                                        Toast.LENGTH_SHORT)
//                                .show();
                    }
                });

        // Loading Rewarded Video Ad
        AdMobrewardedVideoAd.loadAd(
                AdId, new AdRequest.Builder().build());
    }




    void loadRewardedVideoAdNew(int p,String from)
    {

        AdMobrewardedVideoAd = MobileAds.getRewardedVideoAdInstance(mContext);

        AdMobrewardedVideoAd.setRewardedVideoAdListener(
                new RewardedVideoAdListener() {
                    @Override
                    public void onRewardedVideoAdLoaded()
                    {

                        Toast.makeText(mContext,
                                "onRewardedVideoAdLoaded",
                                Toast.LENGTH_SHORT).show();



                        if(from.trim().equals("one")){
                            SharedHelper.putKey(mContext, UserConstant.adsOne,"true");
                            layout_ads_one.setBackgroundColor(Color.parseColor("#A1A1A1"));
                        }
                        if(from.trim().equals("two")){
                            layout_ads_two.setBackgroundColor(Color.parseColor("#A1A1A1"));
                            SharedHelper.putKey(mContext, UserConstant.adsTwo,"true");
                        }
                        if(from.trim().equals("three")){
                            layout_ads_three.setBackgroundColor(Color.parseColor("#A1A1A1"));
                            SharedHelper.putKey(mContext, UserConstant.adsThree,"true");
                        }
                        if(from.trim().equals("four")){
                            layout_ads_four.setBackgroundColor(Color.parseColor("#A1A1A1"));
                            SharedHelper.putKey(mContext, UserConstant.adsFour,"true");
                        }
                        if(from.trim().equals("five")){
                            layout_ads_five.setBackgroundColor(Color.parseColor("#A1A1A1"));
                            SharedHelper.putKey(mContext, UserConstant.adsFive,"true");
                        }
                        if(from.trim().equals("six")){
                            layout_ads_six.setBackgroundColor(Color.parseColor("#A1A1A1"));
                            SharedHelper.putKey(mContext, UserConstant.adsSix,"true");
                        }




                        showRewardedVideoAd();

                    }

                    @Override
                    public void onRewardedVideoAdOpened()
                    {

                        Toast.makeText(mContext,
                                "onRewardedVideoAdOpened",
                                Toast.LENGTH_SHORT)
                                .show();

                    }

                    @Override
                    public void onRewardedVideoStarted()
                    {

                    }

                    @Override
                    public void onRewardedVideoAdClosed()
                    {

                    }

                    @Override
                    public void onRewarded(
                            RewardItem rewardItem)
                    {

                        int amt=rewardItem.getAmount();
                        Log.i("amt_rewarded",String.valueOf(amt));

                        String userId = SharedHelper.getKey(mContext, UserConstant.id);

                        String total_token="";
                        String total_joker="";

                        if(p==0){
                            total_token=coins;
                            total_joker="0";
                        }
                        else {
                            total_token="0";
                            total_joker="1";
                        }

                        Log.i("userIdnData",userId+"\n"+total_token+"\n"+total_joker+"\n"+String.valueOf(amt));

                        ApiInterface apiInterface = AppConfig.getRetrofit().create(ApiInterface.class);

                        Log.i("userIdnData",userId+"\n"+total_token+"\n"+total_joker);

                        Call<ModelCollectReward> call = apiInterface.collect_reward(userId,total_token,total_joker);
                        call.enqueue(new Callback<ModelCollectReward>() {
                            @Override
                            public void onResponse(Call<ModelCollectReward> call, Response<ModelCollectReward> response) {
                                if (response.isSuccessful())
                                {



                                    Toast.makeText(mContext,response.body().getMessage(),Toast.LENGTH_LONG).show();

                                    try {
                                        SharedHelper.putKey(mContext,UserConstant.coin,response.body().getData().get(0).getTotal_token());
                                        MainActivity.total_coin_txt.setText(SharedHelper.getKey(mContext, UserConstant.coin));
                                        SharedHelper.putKey(mContext,UserConstant.zokar,response.body().getData().get(0).getTotal_joker());
                                        MainActivity.zokar_txt.setText(SharedHelper.getKey(mContext, UserConstant.zokar));
                                    }catch (Exception e){

                                    }

                                }
                            }

                            @Override
                            public void onFailure(Call<ModelCollectReward> call, Throwable t) {

                            }
                        });

                    }

                    @Override
                    public void
                    onRewardedVideoAdLeftApplication()
                    {
                        // Showing Toast Message
//                        Toast
//                                .makeText(
//                                        mContext,
//                                        "onRewardedVideoAdLeftApplication",
//                                        Toast.LENGTH_SHORT)
//                                .show();
                    }

                    @Override
                    public void onRewardedVideoAdFailedToLoad(
                            int i)
                    {
                        // Showing Toast Message
//                        Toast
//                                .makeText(
//                                        mContext,
//                                        "onRewardedVideoAdFailedToLoad",
//                                        Toast.LENGTH_SHORT)
//                                .show();
                    }

                    @Override
                    public void onRewardedVideoCompleted()
                    {
                        // Showing Toast Message
//                        Toast
//                                .makeText(
//                                        mContext,
//                                        "onRewardedVideoCompleted",
//                                        Toast.LENGTH_SHORT)
//                                .show();
                    }
                });

        // Loading Rewarded Video Ad
        AdMobrewardedVideoAd.loadAd(
                AdId, new AdRequest.Builder().build());
    }





    public void showRewardedVideoAd()
    {
        // Checking If Ad is Loaded or Not
        if (AdMobrewardedVideoAd.isLoaded()) {
            // showing Video Ad
            AdMobrewardedVideoAd.show();
        }
        else {
            // Loading Rewarded Video Ad
            AdMobrewardedVideoAd.loadAd(
                    AdId, new AdRequest.Builder().build());
        }
    }


    private void showAdsDialog(int pos) {


        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_ads);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ImageView iv_close = dialog.findViewById(R.id.iv_close);
        layout_ads_one=dialog.findViewById(R.id.layout_ads_one);
        layout_ads_two=dialog.findViewById(R.id.layout_ads_two);
        layout_ads_three=dialog.findViewById(R.id.layout_ads_three);
        layout_ads_four=dialog.findViewById(R.id.layout_ads_four);
        layout_ads_five=dialog.findViewById(R.id.layout_ads_five);
        layout_ads_six=dialog.findViewById(R.id.layout_ads_six);


        if(SharedHelper.getKey(mContext,UserConstant.adsOne).equals("true")){
            layout_ads_one.setBackgroundColor(Color.parseColor("#A1A1A1"));

        }
        if(SharedHelper.getKey(mContext,UserConstant.adsTwo).equals("true")){
            layout_ads_two.setBackgroundColor(Color.parseColor("#A1A1A1"));

        }
        if(SharedHelper.getKey(mContext,UserConstant.adsThree).equals("true")){
            layout_ads_three.setBackgroundColor(Color.parseColor("#A1A1A1"));
        }
        if(SharedHelper.getKey(mContext,UserConstant.adsFour).equals("true")){
            layout_ads_four.setBackgroundColor(Color.parseColor("#A1A1A1"));
        }
        if(SharedHelper.getKey(mContext,UserConstant.adsFive).equals("true")){
            layout_ads_five.setBackgroundColor(Color.parseColor("#A1A1A1"));
        }
        if(SharedHelper.getKey(mContext,UserConstant.adsSix).equals("true")){
            layout_ads_six.setBackgroundColor(Color.parseColor("#A1A1A1"));
        }


        if(SharedHelper.getKey(mContext,UserConstant.adsOne).equals("true")&&
                SharedHelper.getKey(mContext,UserConstant.adsTwo).equals("true")&&
                SharedHelper.getKey(mContext,UserConstant.adsThree).equals("true")&&
                SharedHelper.getKey(mContext,UserConstant.adsFour).equals("true")&&
                SharedHelper.getKey(mContext,UserConstant.adsFive).equals("true")&&
                SharedHelper.getKey(mContext,UserConstant.adsSix).equals("true"))
        {

            SharedHelper.putKey(mContext, UserConstant.adsOne,"false");
            SharedHelper.putKey(mContext, UserConstant.adsTwo,"false");
            SharedHelper.putKey(mContext, UserConstant.adsThree,"false");
            SharedHelper.putKey(mContext, UserConstant.adsFour,"false");
            SharedHelper.putKey(mContext, UserConstant.adsFive,"false");
            SharedHelper.putKey(mContext, UserConstant.adsSix,"false");


            layout_ads_one.setBackgroundColor(Color.parseColor("#51AF2F"));
            layout_ads_two.setBackgroundColor(Color.parseColor("#51AF2F"));
            layout_ads_three.setBackgroundColor(Color.parseColor("#51AF2F"));
            layout_ads_four.setBackgroundColor(Color.parseColor("#51AF2F"));
            layout_ads_five.setBackgroundColor(Color.parseColor("#51AF2F"));
            layout_ads_six.setBackgroundColor(Color.parseColor("#51AF2F"));

        }


        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        layout_ads_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialog.dismiss();

                if(!SharedHelper.getKey(mContext,UserConstant.adsOne).equals("true")){

                    coins="10";
                    showDialogNew(pos,"one");
                }
                else {
                    Toast.makeText(mContext,"Collect with another ads",Toast.LENGTH_LONG).show();
                }



            }
        });

        layout_ads_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(SharedHelper.getKey(mContext,UserConstant.adsOne).equals("true")){

                    if(!SharedHelper.getKey(mContext,UserConstant.adsTwo).equals("true")){

                        //dialog.dismiss();
                        coins="25";
                        showDialogNew(pos,"two");
                    }
                    else {
                        Toast.makeText(mContext,"Collect with another ads",Toast.LENGTH_LONG).show();
                    }

                }
                else {
                    Toast.makeText(mContext,"Firstly see above ads",Toast.LENGTH_LONG).show();
                }




            }
        });

        layout_ads_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(SharedHelper.getKey(mContext,UserConstant.adsOne).equals("true")  &&
                        SharedHelper.getKey(mContext,UserConstant.adsTwo).equals("true")){


                    if(!SharedHelper.getKey(mContext,UserConstant.adsThree).equals("true")){

                        //dialog.dismiss();
                        coins="40";
                        showDialogNew(pos,"three");
                    }
                    else {
                        Toast.makeText(mContext,"Collect with another ads",Toast.LENGTH_LONG).show();
                    }



                }


                else {
                    Toast.makeText(mContext,"Firstly see above ads",Toast.LENGTH_LONG).show();
                }




            }
        });

        layout_ads_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(SharedHelper.getKey(mContext,UserConstant.adsOne).equals("true")  &&
                        SharedHelper.getKey(mContext,UserConstant.adsTwo).equals("true") &&
                        SharedHelper.getKey(mContext,UserConstant.adsThree).equals("true")){

                    if(!SharedHelper.getKey(mContext,UserConstant.adsFour).equals("true")){

                        //dialog.dismiss();
                        coins="70";
                        showDialogNew(pos,"four");
                    }
                    else {
                        Toast.makeText(mContext,"Collect with another ads",Toast.LENGTH_LONG).show();
                    }


                }

                else {
                    Toast.makeText(mContext,"Firstly see above ads",Toast.LENGTH_LONG).show();
                }





            }
        });

        layout_ads_five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(SharedHelper.getKey(mContext,UserConstant.adsOne).equals("true")  &&
                        SharedHelper.getKey(mContext,UserConstant.adsTwo).equals("true") &&
                        SharedHelper.getKey(mContext,UserConstant.adsThree).equals("true")&&
                        SharedHelper.getKey(mContext,UserConstant.adsFour).equals("true")){

                    if(!SharedHelper.getKey(mContext,UserConstant.adsFive).equals("true")){

                        //dialog.dismiss();
                        coins="120";
                        showDialogNew(pos,"five");
                    }
                    else {
                        Toast.makeText(mContext,"Collect with another ads",Toast.LENGTH_LONG).show();
                    }

                }
                else {
                    Toast.makeText(mContext,"Firstly see above ads",Toast.LENGTH_LONG).show();
                }




            }

        });

        layout_ads_six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(SharedHelper.getKey(mContext,UserConstant.adsOne).equals("true")  &&
                        SharedHelper.getKey(mContext,UserConstant.adsTwo).equals("true") &&
                        SharedHelper.getKey(mContext,UserConstant.adsThree).equals("true")&&
                        SharedHelper.getKey(mContext,UserConstant.adsFour).equals("true")&&
                        SharedHelper.getKey(mContext,UserConstant.adsFive).equals("true")){

                    if(!SharedHelper.getKey(mContext,UserConstant.adsSix).equals("true")){

                        // dialog.dismiss();
                        coins="250";
                        showDialogNew(pos,"six");
                    }
                    else {
                        Toast.makeText(mContext,"Collect with another ads",Toast.LENGTH_LONG).show();
                    }

                }

                else {
                    Toast.makeText(mContext,"Firstly see above ads",Toast.LENGTH_LONG).show();
                }




            }
        });

        dialog.show();

    }



}
