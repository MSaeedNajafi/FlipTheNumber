package com.flipthenumber.beta;

import android.app.Dialog;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.flipthenumber.beta.Constants.UserConstant;
import com.flipthenumber.beta.Helper.SharedHelper;
import com.flipthenumber.beta.model.GetHomePageDetailsModel;
import com.flipthenumber.beta.network.NetworkReceiver;
import com.flipthenumber.beta.retrofit.ApiInterface;
import com.flipthenumber.beta.retrofit.AppConfig;
import com.flipthenumber.beta.ui.fragments.EventFragment;
import com.flipthenumber.beta.ui.fragments.HomeFragment;
import com.flipthenumber.beta.ui.fragments.ProfileFragment;
import com.flipthenumber.beta.ui.fragments.RewardFragment;
import com.flipthenumber.beta.ui.fragments.StoreFragment;
import com.flipthenumber.beta.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Unbinder unbinder;
    @BindView(R.id.user_detail_ll)
    LinearLayout user_detail_ll;
    @BindView(R.id.rewards_ll)
    LinearLayout rewards_ll;
    @BindView(R.id.events_ll)
    LinearLayout events_ll;
    @BindView(R.id.home_ll)
    LinearLayout home_ll;
    @BindView(R.id.profile_ll)
    LinearLayout profile_ll;
    @BindView(R.id.store_ll)
    LinearLayout store_ll;
    @BindView(R.id.userNametxt)
    TextView userNametxt;

    public static TextView zokar_txt;

    public static TextView ttxt;

    public static TextView total_coin_txt;

    @BindView(R.id.plusImg)
    ImageView plusImg;

    public static CircleImageView iv_profile_img;

    private LinearLayout layout_coin_two,layout_joker,layout_coins_one;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        initUI();




    }

    private void init() {

        //getSupportActionBar().hide();
        unbinder = ButterKnife.bind(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();
        NetworkReceiver networkReceiver = new NetworkReceiver();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkReceiver, intentFilter);

        layout_coin_two=findViewById(R.id.layout_coin_two);
        layout_coins_one=findViewById(R.id.layout_coins_one);
        layout_joker=findViewById(R.id.layout_joker);


        zokar_txt=findViewById(R.id.zokar_txt);
        ttxt=findViewById(R.id.ttxt);
        total_coin_txt=findViewById(R.id.total_coin_txt);
        iv_profile_img=findViewById(R.id.iv_profile_img);


/*

    TextView zokar_txt;

    TextView ttxt;

    TextView total_coin_txt;
 */



    }



    private void initUI() {

        userNametxt.setText(SharedHelper.getKey(MainActivity.this, UserConstant.name));
        user_detail_ll.setOnClickListener(this::onClick);
        rewards_ll.setOnClickListener(this::onClick);
        events_ll.setOnClickListener(this::onClick);
        home_ll.setOnClickListener(this::onClick);
        profile_ll.setOnClickListener(this::onClick);
        store_ll.setOnClickListener(this::onClick);
        plusImg.setOnClickListener(this::onClick);

        layout_coin_two.setOnClickListener(this);
        layout_coins_one.setOnClickListener(this);
        layout_joker.setOnClickListener(this);


        String img=SharedHelper.getKey(getApplicationContext(), UserConstant.userImage);
        Log.i("image_user_is","img>> "+img);

        if(!img.trim().equals("")){
            iv_profile_img.setVisibility(View.VISIBLE);
            Picasso.get().load(SharedHelper.getKey(getApplicationContext(), UserConstant.userImage)).into(iv_profile_img);
        }

        apiCallGetHomeData();

    }



    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.user_detail_ll:

             //   profile_ll.setBackgroundColor(getResources().getColor(R.color.colorred_light));
                //profile_ll.setEnabled(false);
                events_ll.setEnabled(true);
                home_ll.setEnabled(true);
                rewards_ll.setEnabled(true);
                store_ll.setEnabled(true);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new ProfileFragment()).commit();

                break;

            case R.id.rewards_ll:
                //rewards_ll.setEnabled(false);
                events_ll.setEnabled(true);
                home_ll.setEnabled(true);
                profile_ll.setEnabled(true);
                store_ll.setEnabled(true);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new RewardFragment()).commit();

                break;

            case R.id.events_ll:
                //events_ll.setEnabled(false);
                rewards_ll.setEnabled(true);
                home_ll.setEnabled(true);
                profile_ll.setEnabled(true);
                store_ll.setEnabled(true);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new EventFragment()).commit();

                break;

            case R.id.home_ll:

                //Toast.makeText(getApplicationContext(),"Home Clicked",Toast.LENGTH_LONG).show();
                //home_ll.setEnabled(false);
                events_ll.setEnabled(true);
                rewards_ll.setEnabled(true);
                profile_ll.setEnabled(true);
                store_ll.setEnabled(true);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();

                break;

            case R.id.profile_ll:
             //   profile_ll.setBackgroundColor(getResources().getColor(R.color.colorred_light));
                //profile_ll.setEnabled(false);
                events_ll.setEnabled(true);
                home_ll.setEnabled(true);
                rewards_ll.setEnabled(true);
                store_ll.setEnabled(true);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new ProfileFragment()).commit();

                break;

            case R.id.store_ll:
               // store_ll.setEnabled(false);
                events_ll.setEnabled(true);
                home_ll.setEnabled(true);
                profile_ll.setEnabled(true);
                rewards_ll.setEnabled(true);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new StoreFragment()).commit();

                break;

            case R.id.plusImg:
                //store_ll.setEnabled(false);
                //plusImg.setEnabled(false);
                events_ll.setEnabled(true);
                home_ll.setEnabled(true);
                profile_ll.setEnabled(true);
                rewards_ll.setEnabled(true);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new StoreFragment()).commit();

                break;


            case R.id.layout_joker:

                getSupportFragmentManager().beginTransaction().replace(R.id.container, new StoreFragment()).commit();

                break;

            case R.id.layout_coins_one:

                getSupportFragmentManager().beginTransaction().replace(R.id.container, new StoreFragment()).commit();

                break;


            case R.id.layout_coin_two:

                getSupportFragmentManager().beginTransaction().replace(R.id.container, new StoreFragment()).commit();

                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
    public void showExitDialog(){


        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        View view = getLayoutInflater().inflate(R.layout.exit_app,null);
        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ImageView yes_img=view.findViewById(R.id.yes_img);
        ImageView no_img=view.findViewById(R.id.no_img);


        yes_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
               finishAffinity();
            }
        });


        no_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });


        dialog.show();


    }


    @Override
    public void onBackPressed() {

        showExitDialog();


    }


    private void apiCallGetHomeData() {

            String token = SharedHelper.getKey(getApplicationContext(), UserConstant.userToken);
            String userId = SharedHelper.getKey(getApplicationContext(), UserConstant.id);

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
                                    SharedHelper.putKey(getApplicationContext(),UserConstant.coin,response.body().getData().get(0).getTotal_token());
                                    SharedHelper.putKey(getApplicationContext(),UserConstant.zokar,response.body().getData().get(0).getTotal_joker());
                                    SharedHelper.putKey(getApplicationContext(),UserConstant.point,response.body().getData().get(0).getTotal_points());


                                    Log.i("TokensAndCoins","MainActivity"+response.body().getData().get(0).getTotal_token()+"\n"+
                                            response.body().getData().get(0).getTotal_joker());


                                    zokar_txt.setText(SharedHelper.getKey(MainActivity.this, UserConstant.zokar));
                                    //ttxt.setText(SharedHelper.getKey(MainActivity.this, UserConstant.coin));
                                    total_coin_txt.setText(SharedHelper.getKey(MainActivity.this, UserConstant.coin));
                                    ttxt.setText(SharedHelper.getKey(MainActivity.this, UserConstant.point));

                                    setLeagueLedgeForPoints();


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





    private void setLeagueLedgeForPoints() {


        int points=Integer.parseInt(ttxt.getText().toString().trim());

        if(points>=30 && points<=69){
            plusImg.setImageResource(R.drawable.wood_new);
        }

        if(points>=70 && points<=149){
            plusImg.setImageResource(R.drawable.stone_new);
        }

        if(points>=150 && points<=299){
            plusImg.setImageResource(R.drawable.bronze_new);
        }

        if(points>=300 && points<=449){
            plusImg.setImageResource(R.drawable.silver_new);
        }

        if(points>=450 && points<=599){
            plusImg.setImageResource(R.drawable.gold_new);
        }

        if(points>=600 && points<=1249){
            plusImg.setImageResource(R.drawable.platium_new);
        }

        if(points>=1250 && points<=2499){
            plusImg.setImageResource(R.drawable.shaphire_new);
        }

        if(points>=2500 && points<=4999){
            plusImg.setImageResource(R.drawable.ruby_new);
        }

        if(points>=5000 && points<=9999){
            plusImg.setImageResource(R.drawable.onyx_new);
        }

        if(points>=10000){
            plusImg.setImageResource(R.drawable.diamond_new);
        }



    }



}