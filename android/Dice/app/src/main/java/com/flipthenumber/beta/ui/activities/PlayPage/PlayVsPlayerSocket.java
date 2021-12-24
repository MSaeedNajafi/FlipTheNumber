package com.flipthenumber.beta.ui.activities.PlayPage;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flipthenumber.beta.Constants.UserConstant;
import com.flipthenumber.beta.Helper.CustomToast;
import com.flipthenumber.beta.Helper.PreLollipopSoundPool;
import com.flipthenumber.beta.Helper.SharedHelper;
import com.flipthenumber.beta.adapter.ChatMessagesAdapter;
import com.flipthenumber.beta.model.GamePageCounterModel;
import com.flipthenumber.beta.model.ModelAddCoins;
import com.flipthenumber.beta.model.ModelAddDoubleCoins;
import com.flipthenumber.beta.model.ModelAddForReview;
import com.flipthenumber.beta.model.ModelBet;
import com.flipthenumber.beta.model.ModelChatMsg;
import com.flipthenumber.beta.model.ModelCollectReward;
import com.flipthenumber.beta.model.ModelGetCoinsPoints;
import com.flipthenumber.beta.model.ModelGetMsg;
import com.flipthenumber.beta.model.ModelGetReview;
import com.flipthenumber.beta.model.ModelReUseJoker;
import com.flipthenumber.beta.retrofit.ApiInterface;
import com.flipthenumber.beta.retrofit.AppConfig;
import com.flipthenumber.beta.MainActivity;
import com.flipthenumber.beta.R;
import com.flipthenumber.beta.ui.activities.Tournament.TournamentPlayVsPlay;
import com.github.andreilisun.circular_layout.CircularLayout;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.socket.client.IO;
import io.socket.emitter.Emitter;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayVsPlayerSocket extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.img_back)
    ImageView img_back;

    @BindView(R.id.profile_r_layout)
    RelativeLayout profile_r_layout;

    @BindView(R.id.layout_dice_movement)
    RelativeLayout layout_dice_movement;

    @BindView(R.id.imageView)
    ImageView dice_picture;

    @BindView(R.id.dice2_imageView)
    ImageView dice2_picture;

    TextView txt_one;
    TextView txt_two;
    TextView txt_three;
    TextView txt_four;
    TextView txt_five;
    TextView txt_six;
    TextView txt_seven;
    TextView txt_eight;
    TextView txt_nine;

    TextView txt_one_bottem;
    TextView txt_two_bottem;
    TextView txt_three_bottem;
    TextView txt_four_bottem;
    TextView txt_five_bottem;
    TextView txt_six_bottem;
    TextView txt_seven_bottem;
    TextView txt_eight_bottem;
    TextView txt_nine_bottem;

    @BindView(R.id.gif_img)
    GifImageView gif_img;
    @BindView(R.id.nameTxt)
    TextView nameTxt;

    Random rng = new Random();    //generate random numbers
    SoundPool dice_sound;       //For dice sound playing
    int sound_id;               //Used to control sound stream return by SoundPool
    Handler handler;            //Post message to start roll
    Handler handlertwo;            //Post message to start roll
    Handler handlerthree;
    Handler handlerfour;
    Timer timer = new Timer();    //Used to implement feedback to user
    boolean rolling = false;      //Is die rolling?
    RotateAnimation rotate;
    int soundplay;
    int dice1_number;
    int dice2_number;
    String color_pos = "";

    boolean isComputerTurn = false;
    boolean isLastTurn = false;
    AudioManager mAlramMAnager;
    private boolean VolIsMute;

    boolean isMusic;
    boolean isSound;

    @BindView(R.id.iv_profile_img)
    CircleImageView iv_profile_img;

    @BindView(R.id.iv_profile_img_two)
    CircleImageView iv_profile_img_two;

    @BindView(R.id.iv_profile_img_second)
    CircleImageView iv_profile_img_second;

    TextView zokar_txt, ttxt, total_coin_txt, ttxt_two;

    TextView tv_name, tv_name_two;
    ImageView iv_chat;

    private String user_one_id, user_two_id, token;

    boolean run = false;
    private LinearLayout layout_coin_two, layout_joker, layout_coins_one;
    //Socket Init
    io.socket.client.Socket socket;
    int i;
    String userid = "";
    String turn = "";
    String unique_id = "";
    ArrayList<String> arrayList_two, arrayList_three,
            arrayList_four, arrayList_five, arrayList_six, arrayList_seven,
            arrayList_eight, arrayList_nine, arrayList_ten, arrayList_eveven, arrayList_twelve,
            arrayListFilterData;
    AlertDialog.Builder builder;
    private String pair = "";
    private CircleImageView iv_profile_center;
    String playerImg = "";
    int count = 0;
    int count_re_roll = 0;
    TextView txt_one_oppo;
    TextView txt_two_oppo;
    TextView txt_three_oppo;
    TextView txt_four_oppo;
    TextView txt_five_oppo;
    TextView txt_six_oppo;
    TextView txt_seven_oppo;
    TextView txt_eight_oppo;
    TextView txt_nine_oppo;
    CircularLayout layout_top_you,layout_top_opponent;
    TextView txt_one_bottem_you;
    TextView txt_two_bottem_you;
    TextView txt_three_bottem_you;
    TextView txt_four_bottem_you;
    TextView txt_five_bottem_you;
    TextView txt_six_bottem_you;
    TextView txt_seven_bottem_you;
    TextView txt_eight_bottem_you;
    TextView txt_nine_bottem_you;

    CircularLayout layout_opponent,layout_your;
    String name_two;

    private CircleImageView iv_profile_bottem_you,iv_profile_bottem_opponent;

    private String check_trun_for_code="";

    private int count_for_no_combination=0;

    private String bet_status,bet_coins;

    // creating RewardedVideoAd object
    private RewardedVideoAd AdMobrewardedVideoAd;

    // AdMob Rewarded Video Ad Id
    private String AdId
            = "ca-app-pub-3940256099942544/5224354917";

    private AdView adView;
    AdRequest adRequest;

    private ImageView shape_one_opponent,shape_two_opponent,shape_three_opponent,shape_four_opponent,shape_five_opponent,shape_six_opponent,shape_seven_opponent,shape_eight_opponent,shape_nine_opponent;
    private ImageView shape_one_you,shape_two_you,shape_three_you,shape_four_you,shape_five_you,shape_six_you,shape_seven_you,shape_eight_you,shape_nine_you;

    private ImageView shape_one_you_bottem,shape_two_you_bottem,shape_three_you_bottem,shape_four_you_bottem,shape_five_you_bottem,shape_six_you_bottem,shape_seven_you_bottem,shape_eight_you_bottem,shape_nine_you_bottem;
    private ImageView shape_one_opponent_bottem,shape_two_opponent_bottem,shape_three_opponent_bottem,shape_four_opponent_bottem,shape_five_opponent_bottem,shape_six_opponent_bottem,shape_seven_opponent_bottem,shape_eight_opponent_bottem,shape_nine_opponent_bottem;

    int resIdOne[]={R.drawable.shape_one_one,
            R.drawable.shape_one_two,
            R.drawable.shape_one_three,
            R.drawable.shape_one_four,
            R.drawable.shape_one_five};

    int resIdTwo[]={R.drawable.shape_two_one,
            R.drawable.shape_two_two,
            R.drawable.shape_two_three,
            R.drawable.shape_two_four,
            R.drawable.shape_two_five};

    int resIdThree[]={R.drawable.shape_three_one,
            R.drawable.shape_three_two,
            R.drawable.shape_three_three,
            R.drawable.shape_three_four,
            R.drawable.shape_three_five};

    int resIdFour[]={R.drawable.shape_four_one,
            R.drawable.shape_four_two,
            R.drawable.shape_four_three,
            R.drawable.shape_four_four,
            R.drawable.shape_four_five};

    int resIdFive[]={R.drawable.shape_five_one,
            R.drawable.shape_five_two,
            R.drawable.shape_five_three,
            R.drawable.shape_five_four,
            R.drawable.shape_five_five};

    int resIdSix[]={R.drawable.shape_six_one,
            R.drawable.shape_six_two,
            R.drawable.shape_six_three,
            R.drawable.shape_six_four,
            R.drawable.shape_six_five};

    int resIdSeven[]={R.drawable.shape_seven_one,
            R.drawable.shape_seven_two,
            R.drawable.shape_seven_three,
            R.drawable.shape_seven_four,
            R.drawable.shape_seven_five};

    int resIdEight[]={R.drawable.shape_eight_one,
            R.drawable.shape_eight_two,
            R.drawable.shape_eight_three,
            R.drawable.shape_eight_four,
            R.drawable.shape_eight_five};

    int resIdNine[]={R.drawable.shape_nine_one,
            R.drawable.shape_nine_two,
            R.drawable.shape_nine_three,
            R.drawable.shape_nine_four,
            R.drawable.shape_nine_five};

    private RelativeLayout layout_one_opponent_turn,layout_bottem_opponent,layout_opponet_big_board,layout_your_big_board;

    String colorCode = "";

    int counter;

    InterstitialAd mInterstitialAd;

    ImageView plusImg;

    private Dialog dialogNoJoker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_play_with_player);

        MobileAds.initialize(this);

//        mInterstitialAd = new InterstitialAd(this);
//        // set the ad unit ID
//        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));
//
//        AdRequest adRequest = new AdRequest.Builder().build();
//
//        // Load ads into Interstitial Ads
//        mInterstitialAd.loadAd(adRequest);
//
//        mInterstitialAd.setAdListener(new AdListener() {
//            public void onAdLoaded() {
//                showInterstitial();
//            }
//        });


        mAlramMAnager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        VolIsMute = false;

        // userid=SharedHelper.getKey(getApplicationContext(),UserConstant.id);

        init();
        initUI();
        InitArrayListCombinations();
        InitSound();
        InitSocket();

        apiCallCounterForAds();


        apiCallForReview();


        setLeagueLedgeForPoints();


    }


    private void InitArrayListCombinations() {

        arrayList_two = new ArrayList<>();
        arrayList_three = new ArrayList<>();
        arrayList_four = new ArrayList<>();
        arrayList_five = new ArrayList<>();
        arrayList_six = new ArrayList<>();
        arrayList_seven = new ArrayList<>();
        arrayList_eight = new ArrayList<>();
        arrayList_nine = new ArrayList<>();
        arrayList_ten = new ArrayList<>();
        arrayList_eveven = new ArrayList<>();
        arrayList_twelve = new ArrayList<>();
        arrayListFilterData = new ArrayList<>();
        arrayList_two.add("2");
        arrayList_three.add("3");
        arrayList_three.add("2,1");
        arrayList_four.add("4");
        arrayList_four.add("3,1");
        arrayList_five.add("5");
        arrayList_five.add("4,1");
        arrayList_five.add("3,2");
        arrayList_six.add("6");
        arrayList_six.add("5,1");
        arrayList_six.add("4,2");
        arrayList_seven.add("7");
        arrayList_seven.add("6,1");
        arrayList_seven.add("5,2");
        arrayList_seven.add("4,3");
        arrayList_eight.add("8");
        arrayList_eight.add("7,1");
        arrayList_eight.add("6,2");
        arrayList_eight.add("5,3");
        arrayList_nine.add("9");
        arrayList_nine.add("8,1");
        arrayList_nine.add("7,2");
        arrayList_nine.add("6,3");
        arrayList_nine.add("5,4");
        arrayList_ten.add("9,1");
        arrayList_ten.add("8,2");
        arrayList_ten.add("7,3");
        arrayList_ten.add("6,4");


        arrayList_eveven.add("9,2");
        arrayList_eveven.add("8,3");
        arrayList_eveven.add("7,4");
        arrayList_eveven.add("6,5");


        arrayList_twelve.add("9,3");
        arrayList_twelve.add("8,4");
        arrayList_twelve.add("7,5");


    }

    private void init() {

        ButterKnife.bind(this);
        isMusic = SharedHelper.getBooleanKey(PlayVsPlayerSocket.this, UserConstant.isMusic);
        isSound = SharedHelper.getBooleanKey(PlayVsPlayerSocket.this, UserConstant.isSound);

        iv_chat = findViewById(R.id.iv_chat);
        iv_chat.setOnClickListener(this);
        tv_name = findViewById(R.id.tv_name);
        tv_name_two = findViewById(R.id.tv_name_two);

        zokar_txt = findViewById(R.id.zokar_txt);
        ttxt = findViewById(R.id.ttxt);
        total_coin_txt = findViewById(R.id.total_coin_txt);
        ttxt_two = findViewById(R.id.ttxt_two);

        zokar_txt.setText(SharedHelper.getKey(PlayVsPlayerSocket.this, UserConstant.zokar));
        ttxt.setText(SharedHelper.getKey(PlayVsPlayerSocket.this, UserConstant.point));

        total_coin_txt.setText(SharedHelper.getKey(PlayVsPlayerSocket.this, UserConstant.coin));

        ttxt_two.setText(SharedHelper.getKey(PlayVsPlayerSocket.this, UserConstant.coin));

        layout_coin_two = findViewById(R.id.layout_coin_two);
        layout_coins_one = findViewById(R.id.layout_coins_one);
        layout_joker = findViewById(R.id.layout_joker);


        builder = new AlertDialog.Builder(PlayVsPlayerSocket.this);
        builder.setTitle("Dice Combinations");
        builder.setCancelable(false);


        txt_one = findViewById(R.id.txt_one);
        txt_two = findViewById(R.id.txt_two);
        txt_three = findViewById(R.id.txt_three);
        txt_four = findViewById(R.id.txt_four);
        txt_five = findViewById(R.id.txt_five);
        txt_six = findViewById(R.id.txt_six);
        txt_seven = findViewById(R.id.txt_seven);
        txt_eight = findViewById(R.id.txt_eight);
        txt_nine = findViewById(R.id.txt_nine);


        txt_one_bottem = findViewById(R.id.txt_one_bottem);
        txt_two_bottem = findViewById(R.id.txt_two_bottem);
        txt_three_bottem = findViewById(R.id.txt_three_bottem);
        txt_four_bottem = findViewById(R.id.txt_four_bottem);
        txt_five_bottem = findViewById(R.id.txt_five_bottem);
        txt_six_bottem = findViewById(R.id.txt_six_bottem);
        txt_seven_bottem = findViewById(R.id.txt_seven_bottem);
        txt_eight_bottem = findViewById(R.id.txt_eight_bottem);
        txt_nine_bottem = findViewById(R.id.txt_nine_bottem);

        iv_profile_center = findViewById(R.id.iv_profile_center);




         txt_one_oppo= findViewById(R.id.txt_one_oppo);
         txt_two_oppo= findViewById(R.id.txt_two_oppo);
         txt_three_oppo= findViewById(R.id.txt_three_oppo);
         txt_four_oppo= findViewById(R.id.txt_four_oppo);
         txt_five_oppo= findViewById(R.id.txt_five_oppo);
         txt_six_oppo= findViewById(R.id.txt_six_oppo);
         txt_seven_oppo= findViewById(R.id.txt_seven_oppo);
         txt_eight_oppo= findViewById(R.id.txt_eight_oppo);
         txt_nine_oppo= findViewById(R.id.txt_nine_oppo);


         layout_top_you= findViewById(R.id.layout_top_you);
         layout_top_opponent= findViewById(R.id.layout_top_opponent);


         txt_one_bottem_you= findViewById(R.id.txt_one_bottem_you);
         txt_two_bottem_you= findViewById(R.id.txt_two_bottem_you);
         txt_three_bottem_you= findViewById(R.id.txt_three_bottem_you);
         txt_four_bottem_you= findViewById(R.id.txt_four_bottem_you);
         txt_five_bottem_you= findViewById(R.id.txt_five_bottem_you);
         txt_six_bottem_you= findViewById(R.id.txt_six_bottem_you);
         txt_seven_bottem_you= findViewById(R.id.txt_seven_bottem_you);
         txt_eight_bottem_you= findViewById(R.id.txt_eight_bottem_you);
         txt_nine_bottem_you= findViewById(R.id.txt_nine_bottem_you);

         layout_opponent= findViewById(R.id.layout_opponent);
         layout_your= findViewById(R.id.layout_your);



        iv_profile_bottem_you = findViewById(R.id.iv_profile_bottem_you);
        iv_profile_bottem_opponent = findViewById(R.id.iv_profile_bottem_opponent);


        layout_one_opponent_turn = findViewById(R.id.layout_one_opponent_turn);
        layout_bottem_opponent = findViewById(R.id.layout_bottem_opponent);
        layout_opponet_big_board = findViewById(R.id.layout_opponet_big_board);
        layout_your_big_board = findViewById(R.id.layout_your_big_board);



        adsMethod();



        shape_one_opponent=findViewById(R.id.shape_one_opponent);
        shape_two_opponent=findViewById(R.id.shape_two_opponent);
        shape_three_opponent=findViewById(R.id.shape_three_opponent);
        shape_four_opponent=findViewById(R.id.shape_four_opponent);
        shape_five_opponent=findViewById(R.id.shape_five_opponent);
        shape_six_opponent=findViewById(R.id.shape_six_opponent);
        shape_seven_opponent=findViewById(R.id.shape_seven_opponent);
        shape_eight_opponent=findViewById(R.id.shape_eight_opponent);
        shape_nine_opponent=findViewById(R.id.shape_nine_opponent);


        shape_one_you=findViewById(R.id.shape_one_you);
        shape_two_you=findViewById(R.id.shape_two_you);
        shape_three_you=findViewById(R.id.shape_three_you);
        shape_four_you=findViewById(R.id.shape_four_you);
        shape_five_you=findViewById(R.id.shape_five_you);
        shape_six_you=findViewById(R.id.shape_six_you);
        shape_seven_you=findViewById(R.id.shape_seven_you);
        shape_eight_you=findViewById(R.id.shape_eight_you);
        shape_nine_you=findViewById(R.id.shape_nine_you);


        shape_one_opponent_bottem=findViewById(R.id.shape_one_opponent_bottem);
        shape_two_opponent_bottem=findViewById(R.id.shape_two_opponent_bottem);
        shape_three_opponent_bottem=findViewById(R.id.shape_three_opponent_bottem);
        shape_four_opponent_bottem=findViewById(R.id.shape_four_opponent_bottem);
        shape_five_opponent_bottem=findViewById(R.id.shape_five_opponent_bottem);
        shape_six_opponent_bottem=findViewById(R.id.shape_six_opponent_bottem);
        shape_seven_opponent_bottem=findViewById(R.id.shape_seven_opponent_bottem);
        shape_eight_opponent_bottem=findViewById(R.id.shape_eight_opponent_bottem);
        shape_nine_opponent_bottem=findViewById(R.id.shape_nine_opponent_bottem);


        shape_one_you_bottem=findViewById(R.id.shape_one_you_bottem);
        shape_two_you_bottem=findViewById(R.id.shape_two_you_bottem);
        shape_three_you_bottem=findViewById(R.id.shape_three_you_bottem);
        shape_four_you_bottem=findViewById(R.id.shape_four_you_bottem);
        shape_five_you_bottem=findViewById(R.id.shape_five_you_bottem);
        shape_six_you_bottem=findViewById(R.id.shape_six_you_bottem);
        shape_seven_you_bottem=findViewById(R.id.shape_seven_you_bottem);
        shape_eight_you_bottem=findViewById(R.id.shape_eight_you_bottem);
        shape_nine_you_bottem=findViewById(R.id.shape_nine_you_bottem);

        plusImg=findViewById(R.id.plusImg);

        ClickListener();


    }


    private void ClickListener() {
        txt_one.setOnClickListener(this);
        txt_two.setOnClickListener(this);
        txt_three.setOnClickListener(this);
        txt_four.setOnClickListener(this);
        txt_five.setOnClickListener(this);
        txt_six.setOnClickListener(this);
        txt_seven.setOnClickListener(this);
        txt_eight.setOnClickListener(this);
        txt_nine.setOnClickListener(this);
    }


    private void initUI() {

        String img = SharedHelper.getKey(getApplicationContext(), UserConstant.userImage);
        Log.i("image_user_is", "img>> " + img);

        if (!img.trim().equals("")) {

            Picasso.get().load(SharedHelper.getKey(getApplicationContext(), UserConstant.userImage)).into(iv_profile_img);

            Picasso.get().load(SharedHelper.getKey(getApplicationContext(), UserConstant.userImage)).into(iv_profile_img_two);

            Picasso.get().load(SharedHelper.getKey(getApplicationContext(), UserConstant.userImage)).into(iv_profile_bottem_you);

        }

        bet_status = getIntent().getStringExtra("bet_status");
        bet_coins = getIntent().getStringExtra("bet_coins");

        if(bet_status.trim().equals("true")){
            apiCallBet();
           // Toast.makeText(getApplicationContext(),bet_coins,Toast.LENGTH_LONG).show();
        }else {
            //Toast.makeText(getApplicationContext(),"False status",Toast.LENGTH_LONG).show();
        }




         name_two = getIntent().getStringExtra("name");

        user_two_id = getIntent().getStringExtra("id");

        user_one_id = SharedHelper.getKey(getApplicationContext(), UserConstant.id);

        token = SharedHelper.getKey(getApplicationContext(), UserConstant.userToken);


        Log.i("tokenandData", token + "\n" + user_one_id + "\n" + user_two_id);




        // Picasso.get().load(image_two).into(iv_profile_img_second);


        layout_dice_movement.setOnClickListener(new PlayVsPlayerSocket.HandleClick());

        nameTxt.setText(SharedHelper.getKey(PlayVsPlayerSocket.this, UserConstant.name));
        img_back.setOnClickListener(this);

        //profile_r_layout.setOnClickListener(new HandleClick());


        rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(800);
        rotate.setInterpolator(new LinearInterpolator());

        //link handler to callback

        handler = new Handler(callback);
        handlertwo=new Handler(callbacktwo);
        handlerthree=new Handler(callbackthree);
        handlerfour=new Handler(callbackfour);
        gif_img.setVisibility(View.VISIBLE);


        //displayMessage("Your Turn");
        turn = getIntent().getStringExtra("turn");

        unique_id = getIntent().getStringExtra("user_id_player_id");
        Log.i("uniqueIdIsHere", unique_id);

        Toast.makeText(getApplicationContext(),unique_id+"",Toast.LENGTH_LONG).show();


        playerImg = getIntent().getStringExtra("player_img");


        if (!playerImg.equals("")) {

            Log.i("PlayerImgSocket", playerImg);
            Picasso.get().load(playerImg).into(iv_profile_center);

            Picasso.get().load(playerImg).into(iv_profile_img_second);

            Picasso.get().load(playerImg).into(iv_profile_bottem_opponent);

        }





        if (getIntent().getStringExtra("turn").equals("your")) {
            Toast.makeText(getApplicationContext(), "Your Turn", Toast.LENGTH_LONG).show();
            methodVisibleGoneWhenMyTurn();
            check_trun_for_code="Your";

            layout_your_big_board.setVisibility(View.VISIBLE);
            layout_bottem_opponent.setVisibility(View.VISIBLE);

        }

        else
            {
            iv_profile_center.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(), "Opponent Turn", Toast.LENGTH_LONG).show();
            methodVisibleGoneWhenOpponentTurn();
            check_trun_for_code="Opponent";

                layout_opponet_big_board.setVisibility(View.VISIBLE);
                layout_one_opponent_turn.setVisibility(View.VISIBLE);


        }


//        Random random = new Random();
//        int i = random.nextInt(2);
//        if (i == 0) {
//            displayMessage("Computer Turn");
//            computerTurn();
//        }
//        else {
//            gif_img.setVisibility(View.VISIBLE);
//            displayMessage("Your Turn");
//        }


        layout_coin_two.setOnClickListener(this);
        layout_coins_one.setOnClickListener(this);
        layout_joker.setOnClickListener(this);


    }

    private void apiCallBet() {

        ApiInterface apiInterface = AppConfig.getRetrofit().create(ApiInterface.class);
        token=SharedHelper.getKey(getApplicationContext(), UserConstant.userToken);
        Call<ModelBet> call = apiInterface.add_bet("Bearer " + token,SharedHelper.getKey(getApplicationContext(), UserConstant.id),bet_coins);
        call.enqueue(new Callback<ModelBet>() {
            @Override
            public void onResponse(Call<ModelBet> call, Response<ModelBet> response) {

                if (response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        SharedHelper.putKey(getApplicationContext(),UserConstant.coin,response.body().getData().get(0).getTotal_coins());
                        total_coin_txt.setText(SharedHelper.getKey(PlayVsPlayerSocket.this, UserConstant.coin));
                        MainActivity.total_coin_txt.setText(SharedHelper.getKey(PlayVsPlayerSocket.this, UserConstant.coin));

                    }

                }


            }

            @Override
            public void onFailure(Call<ModelBet> call, Throwable t) {
                Log.i("fail_login", t.toString());

                CustomToast.displayError(getApplicationContext(), t.getMessage());
                if (t instanceof SocketTimeoutException) {

                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.img_back:

                showExitDialog();

                break;

            case R.id.profile_r_layout:

                break;

            case R.id.iv_chat:

                showChatDialog();

                break;

            case R.id.layout_coin_two:

                break;


                case R.id.txt_one:

                    if(arrayListFilterData.size()>0){


                        methodForSetPairData("1");


                     }

                    break;

            case R.id.txt_two:

                if(arrayListFilterData.size()>0){

                    methodForSetPairData("2");

                }

                break;

            case R.id.txt_three:

                if(arrayListFilterData.size()>0){
                    methodForSetPairData("3");
                }

                break;

            case R.id.txt_four:

                if(arrayListFilterData.size()>0){
                    methodForSetPairData("4");
                }

                break;

            case R.id.txt_five:


                if(arrayListFilterData.size()>0){
                    methodForSetPairData("5");
                }

                break;

            case R.id.txt_six:


                if(arrayListFilterData.size()>0){
                    methodForSetPairData("6");
                }

                break;

            case R.id.txt_seven:


                if(arrayListFilterData.size()>0){
                    methodForSetPairData("7");
                }

                break;

            case R.id.txt_eight:


                if(arrayListFilterData.size()>0){
                    methodForSetPairData("8");
                }

                break;

            case R.id.txt_nine:

                if(arrayListFilterData.size()>0){
                    methodForSetPairData("9");
                }

                break;

        }
    }

    //User pressed dice, lets start

    private class HandleClick implements View.OnClickListener {

        public void onClick(View arg0) {

            if (userid.equals("")) {

                if (getIntent().getStringExtra("turn").equals("your")) {

                    if(count_for_no_combination==1){

                        isLastTurn = true;
                    isComputerTurn = true;
                    rolling = true;
                    //Show rolling image
                    dice_picture.setImageResource(R.drawable.dice3d160);
                    dice2_picture.setImageResource(R.drawable.dice3d160);

                    //Start rolling sound
                    soundplay = dice_sound.play(sound_id, 1.0f, 1.0f, 0, 0, 1.0f);

                    //Pause to allow image to update
                    dice_picture.startAnimation(rotate);
                    dice2_picture.startAnimation(rotate);
                    try {
                        timer.schedule(new PlayVsPlayerSocket.RollThree(), 1100);
                    } catch (Exception e) {

                    }


                    if (!rolling) {

                        isLastTurn = true;
                        isComputerTurn = true;
                        rolling = true;
                        //Show rolling image
                        dice_picture.setImageResource(R.drawable.dice3d160);
                        dice2_picture.setImageResource(R.drawable.dice3d160);

                        //Start rolling sound
                        soundplay = dice_sound.play(sound_id, 1.0f, 1.0f, 0, 0, 1.0f);

                        //Pause to allow image to update
                        dice_picture.startAnimation(rotate);
                        dice2_picture.startAnimation(rotate);
                        try {
                            timer.schedule(new PlayVsPlayerSocket.RollThree(), 1100);
                        } catch (Exception e) {

                        }


                    }

                    }

                    else {
                          isLastTurn = true;
                    isComputerTurn = true;
                    rolling = true;
                    //Show rolling image
                    dice_picture.setImageResource(R.drawable.dice3d160);
                    dice2_picture.setImageResource(R.drawable.dice3d160);

                    //Start rolling sound
                    soundplay = dice_sound.play(sound_id, 1.0f, 1.0f, 0, 0, 1.0f);

                    //Pause to allow image to update
                    dice_picture.startAnimation(rotate);
                    dice2_picture.startAnimation(rotate);
                    try {
                        timer.schedule(new PlayVsPlayerSocket.Roll(), 1100);
                    } catch (Exception e) {

                    }


                    if (!rolling) {

                        isLastTurn = true;
                        isComputerTurn = true;
                        rolling = true;
                        //Show rolling image
                        dice_picture.setImageResource(R.drawable.dice3d160);
                        dice2_picture.setImageResource(R.drawable.dice3d160);

                        //Start rolling sound
                        soundplay = dice_sound.play(sound_id, 1.0f, 1.0f, 0, 0, 1.0f);

                        //Pause to allow image to update
                        dice_picture.startAnimation(rotate);
                        dice2_picture.startAnimation(rotate);
                        try {
                            timer.schedule(new PlayVsPlayerSocket.Roll(), 1100);
                        } catch (Exception e) {

                        }


                    }
                    }

                }

                else {
                    Toast.makeText(getApplicationContext(), "Opponent Turn", Toast.LENGTH_LONG).show();
                }
            }

            else {


                if (userid.equals(SharedHelper.getKey(getApplicationContext(), UserConstant.id))) {

                    Toast.makeText(getApplicationContext(), "Wait For Opponent", Toast.LENGTH_LONG).show();

                }

                else {

                    if(count_for_no_combination==1){
                          isLastTurn = true;
                    isComputerTurn = true;
                    rolling = true;
                    //Show rolling image
                    dice_picture.setImageResource(R.drawable.dice3d160);
                    dice2_picture.setImageResource(R.drawable.dice3d160);

                    //Start rolling sound
                    soundplay = dice_sound.play(sound_id, 1.0f, 1.0f, 0, 0, 1.0f);

                    //Pause to allow image to update
                    dice_picture.startAnimation(rotate);
                    dice2_picture.startAnimation(rotate);

                    try {
                        timer.schedule(new PlayVsPlayerSocket.RollThree(), 1100);
                    } catch (Exception e) {

                    }


                    if (!rolling) {

                        isLastTurn = true;
                        isComputerTurn = true;
                        rolling = true;
                        //Show rolling image
                        dice_picture.setImageResource(R.drawable.dice3d160);
                        dice2_picture.setImageResource(R.drawable.dice3d160);

                        //Start rolling sound
                        soundplay = dice_sound.play(sound_id, 1.0f, 1.0f, 0, 0, 1.0f);

                        //Pause to allow image to update
                        dice_picture.startAnimation(rotate);
                        dice2_picture.startAnimation(rotate);
                        try {
                            timer.schedule(new PlayVsPlayerSocket.RollThree(), 1100);
                        } catch (Exception e) {

                        }


                    }
                    }

                    else {

                         isLastTurn = true;
                    isComputerTurn = true;
                    rolling = true;
                    //Show rolling image
                    dice_picture.setImageResource(R.drawable.dice3d160);
                    dice2_picture.setImageResource(R.drawable.dice3d160);

                    //Start rolling sound
                    soundplay = dice_sound.play(sound_id, 1.0f, 1.0f, 0, 0, 1.0f);

                    //Pause to allow image to update
                    dice_picture.startAnimation(rotate);
                    dice2_picture.startAnimation(rotate);

                    try {
                        timer.schedule(new PlayVsPlayerSocket.Roll(), 1100);
                    } catch (Exception e) {

                    }


                    if (!rolling) {

                        isLastTurn = true;
                        isComputerTurn = true;
                        rolling = true;
                        //Show rolling image
                        dice_picture.setImageResource(R.drawable.dice3d160);
                        dice2_picture.setImageResource(R.drawable.dice3d160);

                        //Start rolling sound
                        soundplay = dice_sound.play(sound_id, 1.0f, 1.0f, 0, 0, 1.0f);

                        //Pause to allow image to update
                        dice_picture.startAnimation(rotate);
                        dice2_picture.startAnimation(rotate);
                        try {
                            timer.schedule(new PlayVsPlayerSocket.Roll(), 1100);
                        } catch (Exception e) {

                        }


                    }

                    }



                }
            }


        }
    }

    //New code to initialise sound playback
    void InitSound() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //Use the newer SoundPool.Builder
            //Set the audio attributes, SONIFICATION is for interaction events
            //uses builder pattern
            AudioAttributes aa = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            //default max streams is 1
            //also uses builder pattern
            dice_sound = new SoundPool.Builder().setAudioAttributes(aa).build();

        } else {
            //Running on device earlier than Lollipop
            //Use the older SoundPool constructor
            dice_sound = PreLollipopSoundPool.NewSoundPool();
        }
        //Load the dice sound
        sound_id = dice_sound.load(this, R.raw.shake_dice, 1);
    }

    //When pause completed message sent to callback
    class Roll extends TimerTask {
        public void run() {
            handler.sendEmptyMessage(0);
        }
    }


    Handler.Callback callback = new Handler.Callback() {

        public boolean handleMessage(Message msg) {


            //Get roll result
            //Remember nextInt returns 0 to 5 for argument of 6
            //hence + 1

            switch (rng.nextInt(6) + 1) {
                case 1:
                    rotate.cancel();
                    dice1_number = 1;
                    // dice_no.setText("Dice Number : 1");
                    dice_picture.setImageResource(R.drawable.one);
                    break;
                case 2:
                    rotate.cancel();
                    dice1_number = 2;
                    //  dice_no.setText("Dice Number : 2");
                    dice_picture.setImageResource(R.drawable.two);
                    break;
                case 3:
                    rotate.cancel();
                    dice1_number = 3;
                    // dice_no.setText("Dice Number : 3");
                    dice_picture.setImageResource(R.drawable.three);
                    break;
                case 4:
                    rotate.cancel();
                    dice1_number = 4;
                    // dice_no.setText("Dice Number : 4");
                    dice_picture.setImageResource(R.drawable.four);
                    break;
                case 5:
                    rotate.cancel();
                    dice1_number = 5;
                    // dice_no.setText("Dice Number : 5");
                    dice_picture.setImageResource(R.drawable.five);
                    break;
                case 6:
                    rotate.cancel();
                    dice1_number = 6;
                    // dice_no.setText("Dice Number : 6");
                    dice_picture.setImageResource(R.drawable.six);
                    break;
                default:
            }
            switch (rng.nextInt(6) + 1) {
                case 1:
                    rotate.cancel();
                    dice2_number = 1;
                    //  dice_no.setText("Dice Number : 1");
                    dice2_picture.setImageResource(R.drawable.one);
                    break;
                case 2:
                    rotate.cancel();
                    dice2_number = 2;
                    // dice_no.setText("Dice Number : 2");
                    dice2_picture.setImageResource(R.drawable.two);
                    break;

                case 3:
                    rotate.cancel();
                    dice2_number = 3;
                    // dice_no.setText("Dice Number : 3");
                    dice2_picture.setImageResource(R.drawable.three);
                    break;

                case 4:
                    rotate.cancel();
                    dice2_number = 4;
                    //  dice_no.setText("Dice Number : 4");
                    dice2_picture.setImageResource(R.drawable.four);
                    break;

                case 5:
                    rotate.cancel();
                    dice2_number = 5;
                    // dice_no.setText("Dice Number : 5");
                    dice2_picture.setImageResource(R.drawable.five);
                    break;
                case 6:
                    rotate.cancel();
                    dice2_number = 6;
                    // dice_no.setText("Dice Number : 6");
                    dice2_picture.setImageResource(R.drawable.six);
                    break;
                default:
            }

            rolling = false;  //user can press again

            i = dice1_number + dice2_number;


            if (i == 2) {

                Log.i("arrayList", arrayList_two.toString());

                arrayListFilterData = new ArrayList<>();

                for (int s = 0; s < arrayList_two.size(); s++) {

                    Log.i("ArrayListData", arrayList_two.get(s));


                    if (arrayList_two.get(s).equals("2")) {

                        if (txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_two.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_two.get(s));
                        }

                    }

                }


                new Handler().postDelayed(new Runnable() {

                    @Override

                    public void run() {


                        if (arrayListFilterData.size() > 0) {

                            String[] str = GetStringArray(arrayListFilterData);

                            layout_dice_movement.setEnabled(false);


                            for(int i=0;i<arrayListFilterData.size();i++){
                                if(arrayListFilterData.get(i).equals("2")){
                                    shape_two_you.setImageResource(resIdTwo[0]);
                                }
                            }


                            builder.setItems(str, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Toast.makeText(getApplicationContext(), str[which], Toast.LENGTH_LONG).show();

                                    Log.i("SelectedPair", str[which]);

                                    pair = str[which];

                                    if(check_trun_for_code.trim().equals("Your")){
                                        colorCode="yellow";
                                    }
                                    else {
                                        colorCode="red";
                                    }

                                    JSONObject jsonObject = new JSONObject();

                                    try {
                                        jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                        jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                        jsonObject.put("turn_id", 11);
                                        jsonObject.put("number", i);
                                        jsonObject.put("dice1_number", dice1_number);
                                        jsonObject.put("dice2_number", dice2_number);
                                        jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                        jsonObject.put("pair", pair);
                                        jsonObject.put("pos_color", colorCode);
                                        jsonObject.put("count_for_no_combination", count_for_no_combination);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Log.i("jsonDataIs", jsonObject.toString());

                                    socket.emit("playEvent", jsonObject.toString());



                                }
                            });

                            AlertDialog dialog = builder.create();
                           // dialog.show();
                          //  dialog.getWindow().setGravity(Gravity.BOTTOM);
                        }

                        else {


                               Toast.makeText(getApplicationContext(), "No Combination Possible", Toast.LENGTH_LONG).show();


                               if(zokar_txt.getText().toString().trim().equals("0")){

                                   methodNOJoker();

                                   JSONObject jsonObject = new JSONObject();

                                   try {
                                       jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                       jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                       jsonObject.put("turn_id", 11);
                                       jsonObject.put("number", i);
                                       jsonObject.put("dice1_number", dice1_number);
                                       jsonObject.put("dice2_number", dice2_number);
                                       jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                       jsonObject.put("pair", "");
                                       jsonObject.put("pos_color", "");
                                       jsonObject.put("count_for_no_combination", 1);
                                   } catch (JSONException e) {
                                       e.printStackTrace();
                                   }

                                   Log.i("jsonDataIs", jsonObject.toString());

                                   socket.emit("playEvent", jsonObject.toString());

                               }

                               else {

                                   final Dialog dialog = new Dialog(PlayVsPlayerSocket.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(false);
                                dialog.setContentView(R.layout.dialog_re_roll);


                                Button no = dialog.findViewById(R.id.no);
                                Button yes = dialog.findViewById(R.id.yes);


                                no.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {


                                        dialog.dismiss();

                                        JSONObject jsonObject = new JSONObject();

                                        try {
                                            jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                            jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                            jsonObject.put("turn_id", 11);
                                            jsonObject.put("number", i);
                                            jsonObject.put("dice1_number", dice1_number);
                                            jsonObject.put("dice2_number", dice2_number);
                                            jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                            jsonObject.put("pair", "");
                                            jsonObject.put("pos_color", "");
                                            jsonObject.put("count_for_no_combination", 1);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        Log.i("jsonDataIs", jsonObject.toString());

                                        socket.emit("playEvent", jsonObject.toString());


                                    }
                                });


                                yes.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                        //total_coin_txt.setText(SharedHelper.getKey(PlayVsPlayerSocket.this, UserConstant.coin));

                                        methodReRoll();

                                    }
                                });

                                dialog.show();

                               }





                        }


                    }

                }, 1000);


            }

            else if (i == 3) {
                Log.i("arrayList", arrayList_three.toString());

                arrayListFilterData = new ArrayList<>();

                for (int s = 0; s < arrayList_three.size(); s++) {
                    Log.i("ArrayListData", arrayList_three.get(s));

                    if (arrayList_three.get(s).equals("3")) {

                        if (txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_two.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_three.get(s));
                        }

                    } else if (arrayList_three.get(s).equals("2,1")) {
                        if (txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_one.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            // arrayList_two.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_three.get(s));
                        }
                    }


                }


                new Handler().postDelayed(new Runnable() {


                    @Override

                    public void run() {


                        if (arrayListFilterData.size() > 0) {

                            String[] str = GetStringArray(arrayListFilterData);

                            layout_dice_movement.setEnabled(false);


                            for(int i=0;i<arrayListFilterData.size();i++){
                                if(arrayListFilterData.get(i).equals("3")){
                                    shape_three_you.setImageResource(resIdThree[0]);
                                }
                                if(arrayListFilterData.get(i).equals("2,1")){
                                    shape_two_you.setImageResource(resIdTwo[1]);
                                    shape_one_you.setImageResource(resIdOne[1]);
                                }
                            }


                            builder.setItems(str, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(), str[which], Toast.LENGTH_LONG).show();
                                    Log.i("SelectedPair", str[which]);
                                    pair = str[which];


                                    if(check_trun_for_code.trim().equals("Your")){
                                        colorCode="yellow";
                                    }
                                    else {
                                        colorCode="red";
                                    }


                                    JSONObject jsonObject = new JSONObject();

                                    try {
                                        jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                        jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                        jsonObject.put("turn_id", 11);
                                        jsonObject.put("number", i);
                                        jsonObject.put("dice1_number", dice1_number);
                                        jsonObject.put("dice2_number", dice2_number);
                                        jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                        jsonObject.put("pair", pair);
                                        jsonObject.put("pos_color", colorCode);
                                        jsonObject.put("count_for_no_combination", count_for_no_combination);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Log.i("jsonDataIs", jsonObject.toString());

                                    socket.emit("playEvent", jsonObject.toString());
                                }
                            });

                            AlertDialog dialog = builder.create();
                            //dialog.show();
                            //dialog.getWindow().setGravity(Gravity.BOTTOM);
                        }

                        else {

                            Toast.makeText(getApplicationContext(), "No Combination Possible", Toast.LENGTH_LONG).show();


                            if(zokar_txt.getText().toString().trim().equals("0")){

                                methodNOJoker();

                                JSONObject jsonObject = new JSONObject();

                                try {
                                    jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                    jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                    jsonObject.put("turn_id", 11);
                                    jsonObject.put("number", i);
                                    jsonObject.put("dice1_number", dice1_number);
                                    jsonObject.put("dice2_number", dice2_number);
                                    jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                    jsonObject.put("pair", "");
                                    jsonObject.put("pos_color", "");
                                    jsonObject.put("count_for_no_combination", 1);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Log.i("jsonDataIs", jsonObject.toString());

                                socket.emit("playEvent", jsonObject.toString());

                            }



                            else {
                                final Dialog dialog = new Dialog(PlayVsPlayerSocket.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(false);
                                dialog.setContentView(R.layout.dialog_re_roll);


                                Button no = dialog.findViewById(R.id.no);
                                Button yes = dialog.findViewById(R.id.yes);


                                no.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        dialog.dismiss();

                                        JSONObject jsonObject = new JSONObject();

                                        try {
                                            jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                            jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                            jsonObject.put("turn_id", 11);
                                            jsonObject.put("number", i);
                                            jsonObject.put("dice1_number", dice1_number);
                                            jsonObject.put("dice2_number", dice2_number);
                                            jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                            jsonObject.put("pair", "");
                                            jsonObject.put("pos_color", "");
                                            jsonObject.put("count_for_no_combination", 1);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        Log.i("jsonDataIs", jsonObject.toString());

                                        socket.emit("playEvent", jsonObject.toString());


                                    }
                                });


                                yes.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                        methodReRoll();
                                    }
                                });

                                dialog.show();
                            }




                        }


                    }

                }, 1000);


            }

            else if (i == 4) {

                Log.i("arrayList", arrayList_four.toString());

                arrayListFilterData = new ArrayList<>();

                for (int s = 0; s < arrayList_four.size(); s++) {
                    Log.i("ArrayListData", arrayList_four.get(s));

                    if (arrayList_four.get(s).equals("4")) {

                        if (txt_four.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            // arrayList_four.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_four.get(s));
                        }

                    } else if (arrayList_four.get(s).equals("3,1")) {
                        if (txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_one.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            // arrayList_four.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_four.get(s));
                        }
                    }

                }


                new Handler().postDelayed(new Runnable() {


                    @Override

                    public void run() {


                        if (arrayListFilterData.size() > 0) {

                            String[] str = GetStringArray(arrayListFilterData);

                            layout_dice_movement.setEnabled(false);


                            for(int i=0;i<arrayListFilterData.size();i++){
                                if(arrayListFilterData.get(i).equals("4")){
                                    shape_four_you.setImageResource(resIdFour[0]);
                                }
                                if(arrayListFilterData.get(i).equals("3,1")){
                                    shape_three_you.setImageResource(resIdThree[1]);
                                    shape_one_you.setImageResource(resIdOne[1]);
                                }
                            }

                            builder.setItems(str, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(), str[which], Toast.LENGTH_LONG).show();
                                    Log.i("SelectedPair", str[which]);
                                    pair = str[which];


                                    if(check_trun_for_code.trim().equals("Your")){
                                        colorCode="yellow";
                                    }
                                    else {
                                        colorCode="red";
                                    }

                                    JSONObject jsonObject = new JSONObject();

                                    try {
                                        jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                        jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                        jsonObject.put("turn_id", 11);
                                        jsonObject.put("number", i);
                                        jsonObject.put("dice1_number", dice1_number);
                                        jsonObject.put("dice2_number", dice2_number);
                                        jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                        jsonObject.put("pair", pair);
                                        jsonObject.put("pos_color", colorCode);
                                        jsonObject.put("count_for_no_combination",count_for_no_combination);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Log.i("jsonDataIs", jsonObject.toString());

                                    socket.emit("playEvent", jsonObject.toString());
                                }
                            });

                            AlertDialog dialog = builder.create();
                           // dialog.show();
                           // dialog.getWindow().setGravity(Gravity.BOTTOM);
                        }
                        else {

                                Toast.makeText(getApplicationContext(), "No Combination Possible", Toast.LENGTH_LONG).show();


                            if(zokar_txt.getText().toString().trim().equals("0")){

                                methodNOJoker();

                                JSONObject jsonObject = new JSONObject();

                                try {
                                    jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                    jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                    jsonObject.put("turn_id", 11);
                                    jsonObject.put("number", i);
                                    jsonObject.put("dice1_number", dice1_number);
                                    jsonObject.put("dice2_number", dice2_number);
                                    jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                    jsonObject.put("pair", "");
                                    jsonObject.put("pos_color", "");
                                    jsonObject.put("count_for_no_combination", 1);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Log.i("jsonDataIs", jsonObject.toString());

                                socket.emit("playEvent", jsonObject.toString());

                            }

                            else {
                                final Dialog dialog = new Dialog(PlayVsPlayerSocket.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(false);
                                dialog.setContentView(R.layout.dialog_re_roll);


                                Button no = dialog.findViewById(R.id.no);
                                Button yes = dialog.findViewById(R.id.yes);


                                no.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        dialog.dismiss();

                                        JSONObject jsonObject = new JSONObject();

                                        try {
                                            jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                            jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                            jsonObject.put("turn_id", 11);
                                            jsonObject.put("number", i);
                                            jsonObject.put("dice1_number", dice1_number);
                                            jsonObject.put("dice2_number", dice2_number);
                                            jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                            jsonObject.put("pair", "");
                                            jsonObject.put("pos_color", "");
                                            jsonObject.put("count_for_no_combination", 1);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        Log.i("jsonDataIs", jsonObject.toString());

                                        socket.emit("playEvent", jsonObject.toString());


                                    }
                                });


                                yes.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                        methodReRoll();
                                    }
                                });


                                dialog.show();
                            }






                        }


                    }

                }, 1000);


            }

            else if (i == 5) {

                Log.i("arrayList", arrayList_five.toString());

                arrayListFilterData = new ArrayList<>();

                for (int s = 0; s < arrayList_five.size(); s++) {
                    Log.i("ArrayListData", arrayList_five.get(s));

                    if (arrayList_five.get(s).equals("5")) {

                        if (txt_five.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            // arrayList_five.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_five.get(s));
                        }

                    } else if (arrayList_five.get(s).equals("4,1")) {
                        if (txt_four.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_one.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_five.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_five.get(s));
                        }
                    } else if (arrayList_five.get(s).equals("3,2")) {
                        if (txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            // arrayList_five.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_five.get(s));
                        }
                    }


                }


                new Handler().postDelayed(new Runnable() {


                    @Override

                    public void run() {


                        if (arrayListFilterData.size() > 0) {

                            String[] str = GetStringArray(arrayListFilterData);

                            layout_dice_movement.setEnabled(false);


                            for(int i=0;i<arrayListFilterData.size();i++){
                                if(arrayListFilterData.get(i).equals("5")){
                                    shape_five_you.setImageResource(resIdFive[0]);
                                }
                                if(arrayListFilterData.get(i).equals("4,1")){
                                    shape_four_you.setImageResource(resIdFour[1]);
                                    shape_one_you.setImageResource(resIdOne[1]);
                                }
                                if(arrayListFilterData.get(i).equals("3,2")){
                                    shape_three_you.setImageResource(resIdThree[2]);
                                    shape_two_you.setImageResource(resIdTwo[2]);
                                }
                            }


                            builder.setItems(str, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(), str[which], Toast.LENGTH_LONG).show();
                                    Log.i("SelectedPair", str[which]);
                                    pair = str[which];



                                    if(check_trun_for_code.trim().equals("Your")){
                                        colorCode="yellow";
                                    }
                                    else {
                                        colorCode="red";
                                    }

                                    JSONObject jsonObject = new JSONObject();

                                    try {
                                        jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                        jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                        jsonObject.put("turn_id", 11);
                                        jsonObject.put("number", i);
                                        jsonObject.put("dice1_number", dice1_number);
                                        jsonObject.put("dice2_number", dice2_number);
                                        jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                        jsonObject.put("pair", pair);
                                        jsonObject.put("pos_color", colorCode);
                                        jsonObject.put("count_for_no_combination", count_for_no_combination);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Log.i("jsonDataIs", jsonObject.toString());

                                    socket.emit("playEvent", jsonObject.toString());

                                }
                            });

                            AlertDialog dialog = builder.create();
                            //dialog.show();
                           // dialog.getWindow().setGravity(Gravity.BOTTOM);
                        }
                        else {

                                Toast.makeText(getApplicationContext(), "No Combination Possible", Toast.LENGTH_LONG).show();




                            if(zokar_txt.getText().toString().trim().equals("0")){

                                methodNOJoker();

                                JSONObject jsonObject = new JSONObject();

                                try {
                                    jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                    jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                    jsonObject.put("turn_id", 11);
                                    jsonObject.put("number", i);
                                    jsonObject.put("dice1_number", dice1_number);
                                    jsonObject.put("dice2_number", dice2_number);
                                    jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                    jsonObject.put("pair", "");
                                    jsonObject.put("pos_color", "");
                                    jsonObject.put("count_for_no_combination", 1);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Log.i("jsonDataIs", jsonObject.toString());

                                socket.emit("playEvent", jsonObject.toString());

                            }

                            else {
                                    final Dialog dialog = new Dialog(PlayVsPlayerSocket.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(false);
                                dialog.setContentView(R.layout.dialog_re_roll);


                                Button no = dialog.findViewById(R.id.no);
                                Button yes = dialog.findViewById(R.id.yes);


                                no.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        dialog.dismiss();

                                        JSONObject jsonObject = new JSONObject();

                                        try {
                                            jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                            jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                            jsonObject.put("turn_id", 11);
                                            jsonObject.put("number", i);
                                            jsonObject.put("dice1_number", dice1_number);
                                            jsonObject.put("dice2_number", dice2_number);
                                            jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                            jsonObject.put("pair", "");
                                            jsonObject.put("pos_color", "");
                                            jsonObject.put("count_for_no_combination", 1);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        Log.i("jsonDataIs", jsonObject.toString());

                                        socket.emit("playEvent", jsonObject.toString());


                                    }
                                });


                                yes.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                        methodReRoll();
                                    }
                                });


                                dialog.show();
                            }





                        }


                    }

                }, 1000);

            }


            else if (i == 6) {

                Log.i("arrayList", arrayList_six.toString());

                arrayListFilterData = new ArrayList<>();
                for (int s = 0; s < arrayList_six.size(); s++) {
                    Log.i("ArrayListData", arrayList_six.get(s));

                    if (arrayList_six.get(s).equals("6")) {

                        if (txt_six.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_six.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_six.get(s));
                        }

                    } else if (arrayList_six.get(s).equals("5,1")) {
                        if (txt_five.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_one.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_six.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_six.get(s));
                        }
                    } else if (arrayList_six.get(s).equals("4,2")) {
                        if (txt_four.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            // arrayList_six.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_six.get(s));
                        }
                    }

                }
                new Handler().postDelayed(new Runnable() {


                    @Override

                    public void run() {


                        if (arrayListFilterData.size() > 0) {

                            String[] str = GetStringArray(arrayListFilterData);

                            layout_dice_movement.setEnabled(false);
                            for(int i=0;i<arrayListFilterData.size();i++){
                                if(arrayListFilterData.get(i).equals("6")){
                                    shape_six_you.setImageResource(resIdSix[0]);
                                }
                                if(arrayListFilterData.get(i).equals("5,1")){
                                    shape_five_you.setImageResource(resIdFive[1]);
                                    shape_one_you.setImageResource(resIdOne[1]);
                                }
                                if(arrayListFilterData.get(i).equals("4,2")){
                                    shape_four_you.setImageResource(resIdFour[2]);
                                    shape_two_you.setImageResource(resIdTwo[2]);
                                }
                            }

                            builder.setItems(str, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(), str[which], Toast.LENGTH_LONG).show();
                                    Log.i("SelectedPair", str[which]);
                                    pair = str[which];




                                    if(check_trun_for_code.trim().equals("Your")){
                                        colorCode="yellow";
                                    }
                                    else {
                                        colorCode="red";
                                    }

                                    JSONObject jsonObject = new JSONObject();

                                    try {
                                        jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                        jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                        jsonObject.put("turn_id", 11);
                                        jsonObject.put("number", i);
                                        jsonObject.put("dice1_number", dice1_number);
                                        jsonObject.put("dice2_number", dice2_number);
                                        jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                        jsonObject.put("pair", pair);
                                        jsonObject.put("pos_color", colorCode);
                                        jsonObject.put("count_for_no_combination", count_for_no_combination);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Log.i("jsonDataIs", jsonObject.toString());

                                    socket.emit("playEvent", jsonObject.toString());

                                }
                            });

                            AlertDialog dialog = builder.create();
                            //dialog.show();
                            //dialog.getWindow().setGravity(Gravity.BOTTOM);
                        }
                        else {

                             Toast.makeText(getApplicationContext(), "No Combination Possible", Toast.LENGTH_LONG).show();


                            if(zokar_txt.getText().toString().trim().equals("0")){

                                methodNOJoker();

                                JSONObject jsonObject = new JSONObject();

                                try {
                                    jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                    jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                    jsonObject.put("turn_id", 11);
                                    jsonObject.put("number", i);
                                    jsonObject.put("dice1_number", dice1_number);
                                    jsonObject.put("dice2_number", dice2_number);
                                    jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                    jsonObject.put("pair", "");
                                    jsonObject.put("pos_color", "");
                                    jsonObject.put("count_for_no_combination", 1);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Log.i("jsonDataIs", jsonObject.toString());

                                socket.emit("playEvent", jsonObject.toString());

                            }

                            else {
                                final Dialog dialog = new Dialog(PlayVsPlayerSocket.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(false);
                                dialog.setContentView(R.layout.dialog_re_roll);


                                Button no = dialog.findViewById(R.id.no);
                                Button yes = dialog.findViewById(R.id.yes);


                                no.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        dialog.dismiss();

                                        JSONObject jsonObject = new JSONObject();

                                        try {
                                            jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                            jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                            jsonObject.put("turn_id", 11);
                                            jsonObject.put("number", i);
                                            jsonObject.put("dice1_number", dice1_number);
                                            jsonObject.put("dice2_number", dice2_number);
                                            jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                            jsonObject.put("pair", "");
                                            jsonObject.put("pos_color", "");
                                            jsonObject.put("count_for_no_combination", 1);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        Log.i("jsonDataIs", jsonObject.toString());

                                        socket.emit("playEvent", jsonObject.toString());


                                    }
                                });


                                yes.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        dialog.dismiss();
                                        methodReRoll();
                                    }
                                });


                                dialog.show();
                            }









                        }


                    }

                }, 1000);


            }

            else if (i == 7) {
                Log.i("arrayList", arrayList_seven.toString());

                arrayListFilterData = new ArrayList<>();

                for (int s = 0; s < arrayList_seven.size(); s++) {
                    Log.i("ArrayListData", arrayList_seven.get(s));

                    if (arrayList_seven.get(s).equals("7")) {

                        if (txt_seven.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_seven.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_seven.get(s));
                        }

                    } else if (arrayList_seven.get(s).equals("6,1")) {
                        if (txt_six.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_one.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_seven.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_seven.get(s));
                        }
                    } else if (arrayList_seven.get(s).equals("5,2")) {
                        if (txt_five.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_seven.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_seven.get(s));
                        }
                    } else if (arrayList_seven.get(s).equals("4,3")) {
                        if (txt_four.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            // arrayList_seven.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_seven.get(s));
                        }
                    }


                }


                new Handler().postDelayed(new Runnable() {


                    @Override

                    public void run() {


                        if (arrayListFilterData.size() > 0) {
                            String[] str = GetStringArray(arrayListFilterData);
                            layout_dice_movement.setEnabled(false);
                            for(int i=0;i<arrayListFilterData.size();i++){
                                if(arrayListFilterData.get(i).equals("7")){
                                    shape_seven_you.setImageResource(resIdSeven[0]);
                                }
                                if(arrayListFilterData.get(i).equals("6,1")){
                                    shape_six_you.setImageResource(resIdSix[1]);
                                    shape_one_you.setImageResource(resIdOne[1]);
                                }
                                if(arrayListFilterData.get(i).equals("5,2")){
                                    shape_five_you.setImageResource(resIdFive[2]);
                                    shape_two_you.setImageResource(resIdTwo[2]);
                                }
                                if(arrayListFilterData.get(i).equals("4,3")){
                                    shape_four_you.setImageResource(resIdFour[3]);
                                    shape_three_you.setImageResource(resIdThree[3]);
                                }
                            }

                            builder.setItems(str, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(), str[which], Toast.LENGTH_LONG).show();
                                    Log.i("SelectedPair", str[which]);
                                    pair = str[which];




                                    if(check_trun_for_code.trim().equals("Your")){
                                        colorCode="yellow";
                                    }
                                    else {
                                        colorCode="red";
                                    }


                                    JSONObject jsonObject = new JSONObject();

                                    try {
                                        jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                        jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                        jsonObject.put("turn_id", 11);
                                        jsonObject.put("number", i);
                                        jsonObject.put("dice1_number", dice1_number);
                                        jsonObject.put("dice2_number", dice2_number);
                                        jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                        jsonObject.put("pair", pair);
                                        jsonObject.put("pos_color", colorCode);
                                        jsonObject.put("count_for_no_combination", count_for_no_combination);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Log.i("jsonDataIs", jsonObject.toString());

                                    socket.emit("playEvent", jsonObject.toString());

                                }
                            });

                            AlertDialog dialog = builder.create();
                           // dialog.show();
                            //dialog.getWindow().setGravity(Gravity.BOTTOM);
                        }

                        else {

                            Toast.makeText(getApplicationContext(), "No Combination Possible", Toast.LENGTH_LONG).show();


                            if(zokar_txt.getText().toString().trim().equals("0")){

                                methodNOJoker();

                                JSONObject jsonObject = new JSONObject();

                                try {
                                    jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                    jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                    jsonObject.put("turn_id", 11);
                                    jsonObject.put("number", i);
                                    jsonObject.put("dice1_number", dice1_number);
                                    jsonObject.put("dice2_number", dice2_number);
                                    jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                    jsonObject.put("pair", "");
                                    jsonObject.put("pos_color", "");
                                    jsonObject.put("count_for_no_combination", 1);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Log.i("jsonDataIs", jsonObject.toString());

                                socket.emit("playEvent", jsonObject.toString());

                            }

                            else {
                                 final Dialog dialog = new Dialog(PlayVsPlayerSocket.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(false);
                                dialog.setContentView(R.layout.dialog_re_roll);

                                Button no = dialog.findViewById(R.id.no);
                                Button yes = dialog.findViewById(R.id.yes);


                                no.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        dialog.dismiss();

                                        JSONObject jsonObject = new JSONObject();

                                        try {
                                            jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                            jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                            jsonObject.put("turn_id", 11);
                                            jsonObject.put("number", i);
                                            jsonObject.put("dice1_number", dice1_number);
                                            jsonObject.put("dice2_number", dice2_number);
                                            jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                            jsonObject.put("pair", "");
                                            jsonObject.put("pos_color", "");
                                            jsonObject.put("count_for_no_combination", 1);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        Log.i("jsonDataIs", jsonObject.toString());

                                        socket.emit("playEvent", jsonObject.toString());


                                    }
                                });


                                yes.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        dialog.dismiss();
                                        methodReRoll();
                                    }
                                });


                                dialog.show();
                            }










                        }


                    }

                }, 1000);


            }


            else if (i == 8) {

                Log.i("arrayList", arrayList_eight.toString());

                arrayListFilterData = new ArrayList<>();
                for (int s = 0; s < arrayList_eight.size(); s++) {
                    Log.i("ArrayListData", arrayList_eight.get(s));

                    if (arrayList_eight.get(s).equals("8")) {

                        if (txt_eight.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_eight.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_eight.get(s));
                        }

                    } else if (arrayList_eight.get(s).equals("7,1")) {
                        if (txt_seven.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_one.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            // arrayList_eight.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_eight.get(s));
                        }
                    } else if (arrayList_eight.get(s).equals("6,2")) {
                        if (txt_six.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            // arrayList_eight.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_eight.get(s));
                        }
                    } else if (arrayList_eight.get(s).equals("5,3")) {
                        if (txt_five.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_eight.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_eight.get(s));
                        }
                    }

                }


                new Handler().postDelayed(new Runnable() {


                    @Override

                    public void run() {


                        if (arrayListFilterData.size() > 0) {
                            String[] str = GetStringArray(arrayListFilterData);
                            layout_dice_movement.setEnabled(false);

                            for(int i=0;i<arrayListFilterData.size();i++){
                                if(arrayListFilterData.get(i).equals("8")){
                                    shape_eight_you.setImageResource(resIdEight[0]);
                                }
                                if(arrayListFilterData.get(i).equals("7,1")){
                                    shape_seven_you.setImageResource(resIdSeven[1]);
                                    shape_one_you.setImageResource(resIdOne[1]);
                                }
                                if(arrayListFilterData.get(i).equals("6,2")){
                                    shape_six_you.setImageResource(resIdSix[2]);
                                    shape_two_you.setImageResource(resIdTwo[2]);
                                }
                                if(arrayListFilterData.get(i).equals("5,3")){
                                    shape_five_you.setImageResource(resIdFive[3]);
                                    shape_three_you.setImageResource(resIdThree[3]);
                                }
                            }


                            builder.setItems(str, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(), str[which], Toast.LENGTH_LONG).show();
                                    Log.i("SelectedPair", str[which]);
                                    pair = str[which];


                                    if(check_trun_for_code.trim().equals("Your")){
                                        colorCode="yellow";
                                    }
                                    else {
                                        colorCode="red";
                                    }


                                    JSONObject jsonObject = new JSONObject();

                                    try {
                                        jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                        jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                        jsonObject.put("turn_id", 11);
                                        jsonObject.put("number", i);
                                        jsonObject.put("dice1_number", dice1_number);
                                        jsonObject.put("dice2_number", dice2_number);
                                        jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                        jsonObject.put("pair", pair);
                                        jsonObject.put("pos_color", colorCode);
                                        jsonObject.put("count_for_no_combination", count_for_no_combination);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Log.i("jsonDataIs", jsonObject.toString());

                                    socket.emit("playEvent", jsonObject.toString());

                                }
                            });

                            AlertDialog dialog = builder.create();
                          //  dialog.show();
                          //  dialog.getWindow().setGravity(Gravity.BOTTOM);
                        }

                        else {



                                Toast.makeText(getApplicationContext(), "No Combination Possible", Toast.LENGTH_LONG).show();


                            if(zokar_txt.getText().toString().trim().equals("0")){

                                methodNOJoker();

                                JSONObject jsonObject = new JSONObject();

                                try {
                                    jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                    jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                    jsonObject.put("turn_id", 11);
                                    jsonObject.put("number", i);
                                    jsonObject.put("dice1_number", dice1_number);
                                    jsonObject.put("dice2_number", dice2_number);
                                    jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                    jsonObject.put("pair", "");
                                    jsonObject.put("pos_color", "");
                                    jsonObject.put("count_for_no_combination", 1);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Log.i("jsonDataIs", jsonObject.toString());

                                socket.emit("playEvent", jsonObject.toString());

                            }

                            else {

                                final Dialog dialog = new Dialog(PlayVsPlayerSocket.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(false);
                                dialog.setContentView(R.layout.dialog_re_roll);

                                Button no = dialog.findViewById(R.id.no);
                                Button yes = dialog.findViewById(R.id.yes);

                                no.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                        JSONObject jsonObject = new JSONObject();

                                        try {
                                            jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                            jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                            jsonObject.put("turn_id", 11);
                                            jsonObject.put("number", i);
                                            jsonObject.put("dice1_number", dice1_number);
                                            jsonObject.put("dice2_number", dice2_number);
                                            jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                            jsonObject.put("pair", "");
                                            jsonObject.put("pos_color", "");
                                            jsonObject.put("count_for_no_combination", 1);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        Log.i("jsonDataIs", jsonObject.toString());

                                        socket.emit("playEvent", jsonObject.toString());

                                    }
                                });


                                yes.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        dialog.dismiss();
                                        methodReRoll();
                                    }
                                });


                                dialog.show();
                            }

                        }

                    }

                }, 1000);


            }

            else if (i == 9) {

                Log.i("arrayList", arrayList_nine.toString());

                arrayListFilterData = new ArrayList<>();

                for (int s = 0; s < arrayList_nine.size(); s++) {
                    Log.i("ArrayListData", arrayList_nine.get(s));

                    if (arrayList_nine.get(s).equals("9")) {

                        if (txt_nine.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_nine.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_nine.get(s));
                        }

                    } else if (arrayList_nine.get(s).equals("8,1")) {
                        if (txt_eight.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_one.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_nine.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_nine.get(s));
                        }
                    } else if (arrayList_nine.get(s).equals("7,2")) {
                        if (txt_seven.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            // arrayList_nine.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_nine.get(s));
                        }
                    } else if (arrayList_nine.get(s).equals("6,3")) {
                        if (txt_six.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_nine.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_nine.get(s));
                        }
                    } else if (arrayList_nine.get(s).equals("5,4")) {
                        if (txt_five.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_four.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_nine.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_nine.get(s));
                        }
                    }

                }

                new Handler().postDelayed(new Runnable() {

                    @Override

                    public void run() {


                        if (arrayListFilterData.size() > 0) {
                            String[] str = GetStringArray(arrayListFilterData);
                            layout_dice_movement.setEnabled(false);

                            for(int i=0;i<arrayListFilterData.size();i++){
                                if(arrayListFilterData.get(i).equals("9")){
                                    shape_nine_you.setImageResource(resIdNine[0]);
                                }
                                if(arrayListFilterData.get(i).equals("8,1")){
                                    shape_eight_you.setImageResource(resIdEight[1]);
                                    shape_one_you.setImageResource(resIdOne[1]);
                                }
                                if(arrayListFilterData.get(i).equals("7,2")){
                                    shape_seven_you.setImageResource(resIdSeven[2]);
                                    shape_two_you.setImageResource(resIdTwo[2]);
                                }
                                if(arrayListFilterData.get(i).equals("6,3")){
                                    shape_six_you.setImageResource(resIdSix[3]);
                                    shape_three_you.setImageResource(resIdThree[3]);
                                }
                                if(arrayListFilterData.get(i).equals("5,4")){
                                    shape_five_you.setImageResource(resIdFive[4]);
                                    shape_four_you.setImageResource(resIdFour[4]);
                                }
                            }


                            builder.setItems(str, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(), str[which], Toast.LENGTH_LONG).show();
                                    Log.i("SelectedPair", str[which]);
                                    pair = str[which];


                                    if(check_trun_for_code.trim().equals("Your")){
                                        colorCode="yellow";
                                    }
                                    else {
                                        colorCode="red";
                                    }


                                    JSONObject jsonObject = new JSONObject();

                                    try {
                                        jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                        jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                        jsonObject.put("turn_id", 11);
                                        jsonObject.put("number", i);
                                        jsonObject.put("dice1_number", dice1_number);
                                        jsonObject.put("dice2_number", dice2_number);
                                        jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                        jsonObject.put("pair", pair);
                                        jsonObject.put("pos_color", colorCode);
                                        jsonObject.put("count_for_no_combination", count_for_no_combination);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Log.i("jsonDataIs", jsonObject.toString());

                                    socket.emit("playEvent", jsonObject.toString());

                                }
                            });

                            AlertDialog dialog = builder.create();
                          //  dialog.show();
                          //  dialog.getWindow().setGravity(Gravity.BOTTOM);
                        } else {

                            Toast.makeText(getApplicationContext(), "No Combination Possible", Toast.LENGTH_LONG).show();


                                if(zokar_txt.getText().toString().trim().equals("0")){

                                methodNOJoker();

                                JSONObject jsonObject = new JSONObject();

                                try {
                                    jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                    jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                    jsonObject.put("turn_id", 11);
                                    jsonObject.put("number", i);
                                    jsonObject.put("dice1_number", dice1_number);
                                    jsonObject.put("dice2_number", dice2_number);
                                    jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                    jsonObject.put("pair", "");
                                    jsonObject.put("pos_color", "");
                                    jsonObject.put("count_for_no_combination", 1);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Log.i("jsonDataIs", jsonObject.toString());
                                socket.emit("playEvent", jsonObject.toString());
                            }

                            else {
                                final Dialog dialog = new Dialog(PlayVsPlayerSocket.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(false);
                                dialog.setContentView(R.layout.dialog_re_roll);

                                Button no = dialog.findViewById(R.id.no);
                                Button yes = dialog.findViewById(R.id.yes);

                                no.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();

                                        JSONObject jsonObject = new JSONObject();

                                        try {
                                            jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                            jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                            jsonObject.put("turn_id", 11);
                                            jsonObject.put("number", i);
                                            jsonObject.put("dice1_number", dice1_number);
                                            jsonObject.put("dice2_number", dice2_number);
                                            jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                            jsonObject.put("pair", "");
                                            jsonObject.put("pos_color", "");
                                            jsonObject.put("count_for_no_combination", 1);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        Log.i("jsonDataIs", jsonObject.toString());

                                        socket.emit("playEvent", jsonObject.toString());

                                    }
                                });


                                yes.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                        methodReRoll();

                                    }
                                });


                                dialog.show();
                            }

                        }

                    }

                }, 1000);


            }
            else if (i == 10) {
                Log.i("arrayList", arrayList_ten.toString());

                arrayListFilterData = new ArrayList<>();

                for (int s = 0; s < arrayList_ten.size(); s++) {
                    Log.i("ArrayListData", arrayList_ten.get(s));

                    if (arrayList_ten.get(s).equals("9,1")) {
                        if (txt_nine.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_one.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            // arrayList_ten.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_ten.get(s));
                        }
                    } else if (arrayList_ten.get(s).equals("8,2")) {
                        if (txt_eight.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_ten.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_ten.get(s));
                        }
                    } else if (arrayList_ten.get(s).equals("7,3")) {
                        if (txt_seven.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            // arrayList_ten.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_ten.get(s));
                        }
                    } else if (arrayList_ten.get(s).equals("6,4")) {
                        if (txt_six.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_four.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_ten.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_ten.get(s));
                        }
                    }


                }


                new Handler().postDelayed(new Runnable() {


                    @Override

                    public void run() {


                        if (arrayListFilterData.size() > 0) {


                            String[] str = GetStringArray(arrayListFilterData);
                            layout_dice_movement.setEnabled(false);

                            for(int i=0;i<arrayListFilterData.size();i++){

                                if(arrayListFilterData.get(i).equals("9,1")){
                                    shape_nine_you.setImageResource(resIdNine[0]);
                                    shape_one_you.setImageResource(resIdOne[0]);
                                }
                                if(arrayListFilterData.get(i).equals("8,2")){
                                    shape_eight_you.setImageResource(resIdEight[1]);
                                    shape_two_you.setImageResource(resIdTwo[1]);
                                }
                                if(arrayListFilterData.get(i).equals("7,3")){
                                    shape_seven_you.setImageResource(resIdSeven[2]);
                                    shape_three_you.setImageResource(resIdThree[2]);
                                }
                                if(arrayListFilterData.get(i).equals("6,4")){
                                    shape_six_you.setImageResource(resIdSix[3]);
                                    shape_four_you.setImageResource(resIdFour[3]);
                                }
                            }



                            builder.setItems(str, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(), str[which], Toast.LENGTH_LONG).show();
                                    Log.i("SelectedPair", str[which]);
                                    pair = str[which];



                                    if(check_trun_for_code.trim().equals("Your")){
                                        colorCode="yellow";
                                    }
                                    else {
                                        colorCode="red";
                                    }


                                    JSONObject jsonObject = new JSONObject();

                                    try {
                                        jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                        jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                        jsonObject.put("turn_id", 11);
                                        jsonObject.put("number", i);
                                        jsonObject.put("dice1_number", dice1_number);
                                        jsonObject.put("dice2_number", dice2_number);
                                        jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                        jsonObject.put("pair", pair);
                                        jsonObject.put("pos_color", colorCode);
                                        jsonObject.put("count_for_no_combination", count_for_no_combination);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Log.i("jsonDataIs", jsonObject.toString());

                                    socket.emit("playEvent", jsonObject.toString());

                                }
                            });

                            AlertDialog dialog = builder.create();
                           // dialog.show();
                           // dialog.getWindow().setGravity(Gravity.BOTTOM);
                        } else {

                            Toast.makeText(getApplicationContext(), "No Combination Possible", Toast.LENGTH_LONG).show();

                            if(zokar_txt.getText().toString().trim().equals("0")){

                                methodNOJoker();

                                JSONObject jsonObject = new JSONObject();

                                try {
                                    jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                    jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                    jsonObject.put("turn_id", 11);
                                    jsonObject.put("number", i);
                                    jsonObject.put("dice1_number", dice1_number);
                                    jsonObject.put("dice2_number", dice2_number);
                                    jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                    jsonObject.put("pair", "");
                                    jsonObject.put("pos_color", "");
                                    jsonObject.put("count_for_no_combination", 1);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Log.i("jsonDataIs", jsonObject.toString());

                                socket.emit("playEvent", jsonObject.toString());

                            }

                            else {
                                  final Dialog dialog = new Dialog(PlayVsPlayerSocket.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(false);
                                dialog.setContentView(R.layout.dialog_re_roll);


                                Button no = dialog.findViewById(R.id.no);
                                Button yes = dialog.findViewById(R.id.yes);


                                no.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        dialog.dismiss();

                                        JSONObject jsonObject = new JSONObject();

                                        try {
                                            jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                            jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                            jsonObject.put("turn_id", 11);
                                            jsonObject.put("number", i);
                                            jsonObject.put("dice1_number", dice1_number);
                                            jsonObject.put("dice2_number", dice2_number);
                                            jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                            jsonObject.put("pair", "");
                                            jsonObject.put("pos_color", "");
                                            jsonObject.put("count_for_no_combination", 1);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        Log.i("jsonDataIs", jsonObject.toString());

                                        socket.emit("playEvent", jsonObject.toString());


                                    }
                                });


                                yes.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                        methodReRoll();
                                    }
                                });


                                dialog.show();
                            }

                        }

                    }

                }, 1000);

            }

            else if (i == 11) {

                Log.i("arrayList", arrayList_eveven.toString());

                arrayListFilterData = new ArrayList<>();

                for (int s = 0; s < arrayList_eveven.size(); s++) {
                    Log.i("ArrayListData", arrayList_eveven.get(s));

                    if (arrayList_eveven.get(s).equals("9,2")) {
                        if (txt_nine.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_eveven.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_eveven.get(s));
                        }
                    } else if (arrayList_eveven.get(s).equals("8,3")) {
                        if (txt_eight.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            // arrayList_eveven.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_eveven.get(s));
                        }
                    } else if (arrayList_eveven.get(s).equals("7,4")) {
                        if (txt_seven.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_four.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            // arrayList_eveven.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_eveven.get(s));
                        }
                    } else if (arrayList_eveven.get(s).equals("6,5")) {
                        if (txt_six.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_five.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_eveven.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_eveven.get(s));
                        }
                    }

                }


                new Handler().postDelayed(new Runnable() {

                    @Override

                    public void run() {

                        if (arrayListFilterData.size() > 0) {

                            String[] str = GetStringArray(arrayListFilterData);
                            layout_dice_movement.setEnabled(false);

                            for(int i=0;i<arrayListFilterData.size();i++){

                                if(arrayListFilterData.get(i).equals("9,2")){
                                    shape_nine_you.setImageResource(resIdNine[0]);
                                    shape_two_you.setImageResource(resIdTwo[0]);
                                }
                                if(arrayListFilterData.get(i).equals("8,3")){
                                    shape_eight_you.setImageResource(resIdEight[1]);
                                    shape_three_you.setImageResource(resIdThree[1]);
                                }
                                if(arrayListFilterData.get(i).equals("7,4")){
                                    shape_seven_you.setImageResource(resIdSeven[2]);
                                    shape_four_you.setImageResource(resIdFour[2]);
                                }
                                if(arrayListFilterData.get(i).equals("6,5")){
                                    shape_six_you.setImageResource(resIdSix[3]);
                                    shape_five_you.setImageResource(resIdFive[3]);
                                }
                            }

                            builder.setItems(str, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(), str[which], Toast.LENGTH_LONG).show();
                                    Log.i("SelectedPair", str[which]);
                                    pair = str[which];


                                    if(check_trun_for_code.trim().equals("Your")){
                                        colorCode="yellow";
                                    }
                                    else {
                                        colorCode="red";
                                    }


                                    JSONObject jsonObject = new JSONObject();

                                    try {
                                        jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                        jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                        jsonObject.put("turn_id", 11);
                                        jsonObject.put("number", i);
                                        jsonObject.put("dice1_number", dice1_number);
                                        jsonObject.put("dice2_number", dice2_number);
                                        jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                        jsonObject.put("pair", pair);
                                        jsonObject.put("pos_color", colorCode);
                                        jsonObject.put("count_for_no_combination", count_for_no_combination);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Log.i("jsonDataIs", jsonObject.toString());

                                    socket.emit("playEvent", jsonObject.toString());

                                }
                            });

                            AlertDialog dialog = builder.create();
                           // dialog.show();
                           // dialog.getWindow().setGravity(Gravity.BOTTOM);
                        }
                        else {

                                Toast.makeText(getApplicationContext(), "No Combination Possible", Toast.LENGTH_LONG).show();



                            if(zokar_txt.getText().toString().trim().equals("0")){

                                methodNOJoker();

                                JSONObject jsonObject = new JSONObject();

                                try {
                                    jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                    jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                    jsonObject.put("turn_id", 11);
                                    jsonObject.put("number", i);
                                    jsonObject.put("dice1_number", dice1_number);
                                    jsonObject.put("dice2_number", dice2_number);
                                    jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                    jsonObject.put("pair", "");
                                    jsonObject.put("pos_color", "");
                                    jsonObject.put("count_for_no_combination", 1);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Log.i("jsonDataIs", jsonObject.toString());

                                socket.emit("playEvent", jsonObject.toString());

                            }
                            else {
                                 final Dialog dialog = new Dialog(PlayVsPlayerSocket.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(false);
                                dialog.setContentView(R.layout.dialog_re_roll);


                                Button no = dialog.findViewById(R.id.no);
                                Button yes = dialog.findViewById(R.id.yes);


                                no.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        dialog.dismiss();

                                        JSONObject jsonObject = new JSONObject();

                                        try {
                                            jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                            jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                            jsonObject.put("turn_id", 11);
                                            jsonObject.put("number", i);
                                            jsonObject.put("dice1_number", dice1_number);
                                            jsonObject.put("dice2_number", dice2_number);
                                            jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                            jsonObject.put("pair", "");
                                            jsonObject.put("pos_color", "");
                                            jsonObject.put("count_for_no_combination", 1);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        Log.i("jsonDataIs", jsonObject.toString());

                                        socket.emit("playEvent", jsonObject.toString());


                                    }
                                });


                                yes.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                        methodReRoll();
                                    }
                                });


                                dialog.show();
                            }

                        }

                    }

                }, 1000);

            }

            else if (i == 12) {

                Log.i("arrayList", arrayList_twelve.toString());

                arrayListFilterData = new ArrayList<>();


                for (int s = 0; s < arrayList_twelve.size(); s++) {
                    Log.i("ArrayListData", arrayList_twelve.get(s));

                    if (arrayList_twelve.get(s).equals("9,3")) {
                        if (txt_nine.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_twelve.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_twelve.get(s));
                        }
                    } else if (arrayList_twelve.get(s).equals("8,4")) {
                        if (txt_eight.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_four.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_twelve.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_twelve.get(s));
                        }
                    } else if (arrayList_twelve.get(s).equals("7,5")) {
                        if (txt_seven.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_five.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //  arrayList_twelve.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_twelve.get(s));
                        }
                    }

                }

                new Handler().postDelayed(new Runnable() {

                    @Override

                    public void run() {


                        if (arrayListFilterData.size() > 0) {

                            String[] str = GetStringArray(arrayListFilterData);
                            layout_dice_movement.setEnabled(false);

                            for(int i=0;i<arrayListFilterData.size();i++){

                                if(arrayListFilterData.get(i).equals("9,3")){
                                    shape_nine_you.setImageResource(resIdNine[0]);
                                    shape_three_you.setImageResource(resIdThree[0]);
                                }
                                if(arrayListFilterData.get(i).equals("8,4")){
                                    shape_eight_you.setImageResource(resIdEight[1]);
                                    shape_four_you.setImageResource(resIdFour[1]);
                                }
                                if(arrayListFilterData.get(i).equals("7,5")){
                                    shape_seven_you.setImageResource(resIdSeven[2]);
                                    shape_five_you.setImageResource(resIdFive[2]);
                                }

                            }

                            builder.setItems(str, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(), str[which], Toast.LENGTH_LONG).show();
                                    Log.i("SelectedPair", str[which]);
                                    pair = str[which];

                                    if(check_trun_for_code.trim().equals("Your")){
                                        colorCode="yellow";
                                    }
                                    else {
                                        colorCode="red";
                                    }


                                    JSONObject jsonObject = new JSONObject();

                                    try {
                                        jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                        jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                        jsonObject.put("turn_id", 11);
                                        jsonObject.put("number", i);
                                        jsonObject.put("dice1_number", dice1_number);
                                        jsonObject.put("dice2_number", dice2_number);
                                        jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                        jsonObject.put("pair", pair);
                                        jsonObject.put("pos_color", colorCode);
                                        jsonObject.put("count_for_no_combination", count_for_no_combination);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Log.i("jsonDataIs", jsonObject.toString());

                                    socket.emit("playEvent", jsonObject.toString());

                                }
                            });

                            AlertDialog dialog = builder.create();
                         //   dialog.show();
                          //  dialog.getWindow().setGravity(Gravity.BOTTOM);
                        }
                        else {

                            Toast.makeText(getApplicationContext(), "No Combination Possible", Toast.LENGTH_LONG).show();

                            if(zokar_txt.getText().toString().trim().equals("0")){

                                methodNOJoker();

                                JSONObject jsonObject = new JSONObject();

                                try {
                                    jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                    jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                    jsonObject.put("turn_id", 11);
                                    jsonObject.put("number", i);
                                    jsonObject.put("dice1_number", dice1_number);
                                    jsonObject.put("dice2_number", dice2_number);
                                    jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                    jsonObject.put("pair", "");
                                    jsonObject.put("pos_color", "");
                                    jsonObject.put("count_for_no_combination", 1);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Log.i("jsonDataIs", jsonObject.toString());

                                socket.emit("playEvent", jsonObject.toString());

                            }

                            else {
                                final Dialog dialog = new Dialog(PlayVsPlayerSocket.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(false);
                                dialog.setContentView(R.layout.dialog_re_roll);


                                Button no = dialog.findViewById(R.id.no);
                                Button yes = dialog.findViewById(R.id.yes);


                                no.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        dialog.dismiss();

                                        JSONObject jsonObject = new JSONObject();

                                        try {
                                            jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                            jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                            jsonObject.put("turn_id", 11);
                                            jsonObject.put("number", i);
                                            jsonObject.put("dice1_number", dice1_number);
                                            jsonObject.put("dice2_number", dice2_number);
                                            jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                            jsonObject.put("pair", "");
                                            jsonObject.put("pos_color", "");
                                            jsonObject.put("count_for_no_combination", 1);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        Log.i("jsonDataIs", jsonObject.toString());

                                        socket.emit("playEvent", jsonObject.toString());


                                    }
                                });


                                yes.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                        methodReRoll();
                                    }
                                });


                                dialog.show();
                            }


                        }


                    }

                }, 1000);

            }

            return true;
        }
    };


    //Clean up

    public void showDialog(String msg) {

        final Dialog dialog = new Dialog(PlayVsPlayerSocket.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.win_lose_dialog_item);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView txt = dialog.findViewById(R.id.txt);
        RelativeLayout plus_layout = dialog.findViewById(R.id.plus_layout);
        RelativeLayout plus_layout_two = dialog.findViewById(R.id.plus_layout_two);
        RelativeLayout layout_double_coins=dialog.findViewById(R.id.layout_double_coins);
        ImageView home_img = dialog.findViewById(R.id.home_img);

        if (msg.equalsIgnoreCase("BETTER LUCK NEXT TIME!")) {
            txt.setText("BETTER LUCK" + "\n" + "NEXT TIME!");
            plus_layout.setVisibility(View.GONE);
            plus_layout_two.setVisibility(View.GONE);

            if(bet_status.trim().equals("true")){
                layout_double_coins.setVisibility(View.VISIBLE);
                home_img.setVisibility(View.GONE);
            }
            else {
                layout_double_coins.setVisibility(View.GONE);
                home_img.setVisibility(View.VISIBLE);
            }


        }
        else {
            txt.setText(msg);
            plus_layout.setVisibility(View.VISIBLE);
            plus_layout_two.setVisibility(View.VISIBLE);
            layout_double_coins.setVisibility(View.VISIBLE);
            home_img.setVisibility(View.GONE);
        }

        ImageView close_img = dialog.findViewById(R.id.close_img);
        ImageView win_lose_img = dialog.findViewById(R.id.win_lose_img);


        layout_double_coins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogNoJoker.dismiss();
                dialog.dismiss();
                apiCallDoubleCoins(msg);

            }
        });



        if (msg.equalsIgnoreCase("You are Lose")) {
            win_lose_img.setImageDrawable(getDrawable(R.drawable.loss));
        } else {
            win_lose_img.setImageDrawable(getDrawable(R.drawable.win));
        }


        close_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        home_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        dialog.show();


    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void MuteAudio() {

        mAlramMAnager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
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

    public void UnMuteAudio() {
        mAlramMAnager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
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

    public void showExitDialog() {


        final Dialog dialog = new Dialog(PlayVsPlayerSocket.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        View view = getLayoutInflater().inflate(R.layout.exit_game_dialog, null);
        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ImageView yes_img = view.findViewById(R.id.yes_img);
        ImageView sound_img = view.findViewById(R.id.sound_img);
        ImageView music_img = view.findViewById(R.id.music_img);
        ImageView no_img = view.findViewById(R.id.no_img);


        isMusic = SharedHelper.getBooleanKey(PlayVsPlayerSocket.this, UserConstant.isMusic);


        if (isMusic) {
            music_img.setImageResource(R.drawable.mute_neww);

        } else {
            music_img.setImageResource(R.drawable.mute_neww);
        }

        isSound = SharedHelper.getBooleanKey(PlayVsPlayerSocket.this, UserConstant.isSound);

        if (isSound) {
            sound_img.setImageResource(R.drawable.sound_neww);
        } else {
            sound_img.setImageResource(R.drawable.sound_neww);
        }


//        if (isMusic) {
//            music_img.setImageResource(R.drawable.music_mute);
//
//        } else {
//            music_img.setImageResource(R.drawable.mute_neww);
//        }
//
//        isSound = SharedHelper.getBooleanKey(PlayWithComputerActivity.this, UserConstant.isSound);
//
//        if (isSound) {
//            sound_img.setImageResource(R.drawable.mute);
//        } else {
//            sound_img.setImageResource(R.drawable.sound);
//        }

        yes_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dice_sound.pause(sound_id);
                try {
                    timer.cancel();


                    JSONObject jsonObject = new JSONObject();

                    try {

                        jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                        jsonObject.put("player_id", Integer.parseInt(user_two_id));
                        jsonObject.put("turn_id", 11);
                        jsonObject.put("number", 0);
                        jsonObject.put("dice1_number", 0);
                        jsonObject.put("dice2_number", 0);
                        jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                        jsonObject.put("pair", "");
                        jsonObject.put("pos_color", "");
                        jsonObject.put("count_for_no_combination", 0);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }




                    Log.i("jsonDataIs", jsonObject.toString());

                    socket.emit("playEvent", jsonObject.toString());

                    socket.disconnect();


                } catch (Exception e) {

                }

                dialog.dismiss();

                showDialog("BETTER LUCK NEXT TIME!");

                //finish();
            }
        });

        no_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dice_sound.pause(sound_id);
                dialog.dismiss();

            }
        });


//        sound_img.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.M)
//            @Override
//            public void onClick(View v) {
//                if (VolIsMute) {
//
//                    VolIsMute = false;
//                    UnMuteAudio();
//                    sound_img.setImageResource(R.drawable.mute_neww);
//                    SharedHelper.putBooleanKey(PlayWithComputerActivity.this, UserConstant.isSound, false);
//
//                } else {
//
//                    VolIsMute = true;
//                    MuteAudio();
//                    sound_img.setImageResource(R.drawable.mute_neww);
//                    SharedHelper.putBooleanKey(PlayWithComputerActivity.this, UserConstant.isSound, true);
//                }
//            }
//        });
//
//
//
//        music_img.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.M)
//            @Override
//            public void onClick(View v) {
//                if (VolIsMute) {
//                    mAlramMAnager.setStreamMute(AudioManager.STREAM_SYSTEM, false);
//                    VolIsMute = false;
//
//                    music_img.setImageResource(R.drawable.sound_neww);
//                    SharedHelper.putBooleanKey(PlayWithComputerActivity.this, UserConstant.isMusic, false);
//
//                } else {
//                    mAlramMAnager.setStreamMute(AudioManager.STREAM_SYSTEM, true);
//                    VolIsMute = true;
//
//                    music_img.setImageResource(R.drawable.sound_neww);
//                    SharedHelper.putBooleanKey(PlayWithComputerActivity.this, UserConstant.isMusic, true);
//                }
//            }
//        });


        dialog.show();

    }

    @Override
    public void onBackPressed() {
        showExitDialog();
    }

    private void showChatDialog() {


        Dialog dialog = new Dialog(PlayVsPlayerSocket.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_chat_dialog_view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        ImageView iv_close_chat = dialog.findViewById(R.id.iv_close_chat);

        RecyclerView recylerview = dialog.findViewById(R.id.recylerview);

        NestedScrollView nested_scroll_view = dialog.findViewById(R.id.nested_scroll_view);


        EditText et_msg = dialog.findViewById(R.id.et_msg);

        RelativeLayout layout_send_msg = dialog.findViewById(R.id.layout_send_msg);

        iv_close_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        layout_send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!et_msg.getText().toString().trim().equals("")) {
                    apiCallSendMsg(et_msg.getText().toString().trim(), recylerview, nested_scroll_view);
                    et_msg.setText("");
                } else {
                    Toast.makeText(getApplicationContext(), "Message Empty!", Toast.LENGTH_LONG).show();
                }

            }
        });

        apiCallGetMessages(recylerview, nested_scroll_view);

    }

    private void apiCallSendMsg(String msg, RecyclerView recyclerView, NestedScrollView nestedScrollView) {

        ProgressDialog dialog = new ProgressDialog(PlayVsPlayerSocket.this);
        dialog.setTitle("Loading");
        dialog.setMessage("Please wait...");

        dialog.show();
        ApiInterface apiInterface = AppConfig.getRetrofit().create(ApiInterface.class);


        Call<ModelChatMsg> call = apiInterface.chat_msg(
                "Bearer " + token,
                user_one_id,
                user_two_id,
                msg);


        call.enqueue(new Callback<ModelChatMsg>() {
            @Override
            public void onResponse(Call<ModelChatMsg> call, Response<ModelChatMsg> response) {

                Log.i("response_code_", response.code() + "");
                Log.i("response_code_", response.message() + "");

                dialog.dismiss();

                Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();

                if (response.isSuccessful()) {

                    if (response.body().getStatus()) {

                        apiCallGetMessages(recyclerView, nestedScrollView);

                    }

                }
            }

            @Override
            public void onFailure(Call<ModelChatMsg> call, Throwable t) {
                Log.i("fail_login", t.toString());
                dialog.dismiss();
                CustomToast.displayError(getApplicationContext(), t.getMessage());
                if (t instanceof SocketTimeoutException) {

                }
            }
        });

    }

    private void apiCallGetMessages(RecyclerView recyclerView, NestedScrollView nestedScrollView) {

        ProgressDialog dialog = new ProgressDialog(PlayVsPlayerSocket.this);
        dialog.setTitle("Loading");
        dialog.setMessage("Please wait...");

        dialog.show();
        ApiInterface apiInterface = AppConfig.getRetrofit().create(ApiInterface.class);


        Call<ModelGetMsg> call = apiInterface.get_msg(
                "Bearer " + token,
                user_one_id,
                user_two_id);


        call.enqueue(new Callback<ModelGetMsg>() {
            @Override
            public void onResponse(Call<ModelGetMsg> call, Response<ModelGetMsg> response) {

                Log.i("response_code_", response.code() + "");
                Log.i("response_code_", response.message() + "");

                dialog.dismiss();


                if (response.isSuccessful()) {

                    if (response.body().getStatus()) {
                        //Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_LONG).show();
                        if (response.body().getData().size() > 0) {
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(new ChatMessagesAdapter(getApplicationContext(), response.body()));


                            nestedScrollView.post(new Runnable() {
                                @Override
                                public void run() {
                                    nestedScrollView.fullScroll(View.FOCUS_DOWN);
                                }
                            });

                        }

                    }

                }


            }

            @Override
            public void onFailure(Call<ModelGetMsg> call, Throwable t) {
                Log.i("fail_login", t.toString());
                dialog.dismiss();
                CustomToast.displayError(getApplicationContext(), t.getMessage());
                if (t instanceof SocketTimeoutException) {

                }
            }
        });

    }

    // Socket Coding

    private void InitSocket() {

        try {

            socket = IO.socket("http://141.136.36.151:4050");
            socket.connect();

            socket.on("playEvent", new Emitter.Listener() {
                @Override

                public void call(Object... args) {

                    Log.i("ArgumentsAre", args[0].toString());
                    final String data = args[0].toString();

                    Log.i("data_is", data);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            JSONObject object = null;
                            try {

                                object = new JSONObject(data);

                                Log.i("ObjectData", object.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try {

                                String unique_idd = String.valueOf(object.getInt("user_id_player_id"));

                                Log.i("IduniqueIs", unique_idd + "uniId>" + unique_id);

                                if (unique_idd.equals(unique_id)) {

                                    shape_one_you.setImageResource(android.R.color.transparent);
                                    shape_two_you.setImageResource(android.R.color.transparent);
                                    shape_three_you.setImageResource(android.R.color.transparent);
                                    shape_four_you.setImageResource(android.R.color.transparent);
                                    shape_five_you.setImageResource(android.R.color.transparent);
                                    shape_six_you.setImageResource(android.R.color.transparent);
                                    shape_seven_you.setImageResource(android.R.color.transparent);
                                    shape_eight_you.setImageResource(android.R.color.transparent);
                                    shape_nine_you.setImageResource(android.R.color.transparent);


                                    try {
                                        i = object.getInt("number");
                                    } catch (Exception e) {

                                        Log.i("ErrorIshere", e.toString());
                                    }


                                    dice1_number = object.getInt("dice1_number");
                                    dice2_number = object.getInt("dice2_number");
                                    userid = String.valueOf(object.getInt("user_id"));
                                    pair = object.getString("pair");
                                    color_pos = object.getString("pos_color");
                                    Log.i("numberIshere", String.valueOf(i));
                                    count_for_no_combination=object.getInt("count_for_no_combination");


                                    if (i == 0) {

                                        addsAndAddCoins();

                                        layout_dice_movement.setEnabled(false);

                                        count = 0;

                                        if (txt_one_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                                            count++;
                                        }
                                        if (txt_two_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                                            count++;
                                        }
                                        if (txt_three_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                                            count++;
                                        }
                                        if (txt_four_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                                            count++;
                                        }
                                        if (txt_five_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                                            count++;
                                        }
                                        if (txt_six_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                                            count++;
                                        }
                                        if (txt_seven_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                                            count++;
                                        }
                                        if (txt_eight_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                                            count++;
                                        }
                                        if (txt_nine_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                                            count++;
                                        }


                                        if (count <= 1) {

                                            showDialog("WINNER!");

                                        }
                                        else {

                                            showDialog("WINNER!");
                                            //showDialogBucky();

                                        }

                                        run = true;

                                    }


                                      else if (count_for_no_combination == 2) {

                                          layout_dice_movement.setEnabled(false);

                                          if (!userid.equals(SharedHelper.getKey(getApplicationContext(), UserConstant.id))){

                                              if (pair.contains(",")) {

                                                  String[] split_list = pair.split(",");
                                                  Log.i("pairValue", split_list[0] + "second" + split_list[1]);


                                                  if (split_list[0].trim().equals(txt_one.getText().toString().trim())) {

                                                      if (color_pos.trim().equals("red")) {

                                                          txt_one_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                          txt_one_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                                                      } else if (color_pos.trim().equals("yellow")) {

                                                          txt_one_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                          txt_one_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                      }

                                                  }

                                                  else if (split_list[0].trim().equals(txt_two.getText().toString().trim())) {

                                                      if (color_pos.trim().equals("red")) {

                                                          txt_two_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                          txt_two_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                      } else if (color_pos.trim().equals("yellow")) {

                                                          txt_two_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                          txt_two_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                      }


                                                  }

                                                  else if (split_list[0].trim().equals(txt_three.getText().toString().trim())) {

                                                      if (color_pos.trim().equals("red")) {

                                                          txt_three_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                          txt_three_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                      } else if (color_pos.trim().equals("yellow")) {

                                                          txt_three_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                          txt_three_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                      }


                                                  }

                                                  else if (split_list[0].trim().equals(txt_four.getText().toString().trim())) {

                                                      if (color_pos.trim().equals("red")) {


                                                          txt_four_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                          txt_four_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                      } else if (color_pos.trim().equals("yellow")) {

                                                          txt_four_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                          txt_four_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                      }


                                                  } else if (split_list[0].trim().equals(txt_five.getText().toString().trim())) {

                                                      if (color_pos.trim().equals("red")) {

                                                          txt_five_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                          txt_five_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                      } else if (color_pos.trim().equals("yellow")) {

                                                          txt_five_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                          txt_five_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                      }


                                                  } else if (split_list[0].trim().equals(txt_six.getText().toString().trim())) {

                                                      if (color_pos.trim().equals("red")) {

                                                          txt_six_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                          txt_six_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                      } else if (color_pos.trim().equals("yellow")) {

                                                          txt_six_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                          txt_six_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                      }


                                                  } else if (split_list[0].trim().equals(txt_seven.getText().toString().trim())) {

                                                      if (color_pos.trim().equals("red")) {

                                                          txt_seven_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                          txt_seven_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                      } else if (color_pos.trim().equals("yellow")) {

                                                          txt_seven_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                          txt_seven_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                      }


                                                  } else if (split_list[0].trim().equals(txt_eight.getText().toString().trim())) {

                                                      if (color_pos.trim().equals("red")) {
                                                          txt_eight_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                          txt_eight_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                      } else if (color_pos.trim().equals("yellow")) {

                                                          txt_eight_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                          txt_eight_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                      }


                                                  }
                                                  else if (split_list[0].trim().equals(txt_nine.getText().toString().trim())) {

                                                      if (color_pos.trim().equals("red")) {


                                                          txt_nine_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                          txt_nine_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                                                      } else if (color_pos.trim().equals("yellow")) {


                                                          txt_nine_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                          txt_nine_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                      }


                                                  }


                                                  if (split_list[1].trim().equals(txt_one.getText().toString().trim())) {

                                                      if (color_pos.trim().equals("red")) {



                                                          txt_one_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                          txt_one_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                      } else if (color_pos.trim().equals("yellow")) {

                                                          txt_one_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                          txt_one_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                      }


                                                  }

                                                  else if (split_list[1].trim().equals(txt_two.getText().toString().trim())) {

                                                      if (color_pos.trim().equals("red")) {




                                                          txt_two_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                          txt_two_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                      } else if (color_pos.trim().equals("yellow")) {

                                                          txt_two_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                          txt_two_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                      }


                                                  } else if (split_list[1].trim().equals(txt_three.getText().toString().trim())) {

                                                      if (color_pos.trim().equals("red")) {



                                                          txt_three_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                          txt_three_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                      } else if (color_pos.trim().equals("yellow")) {

                                                          txt_three_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                          txt_three_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                      }


                                                  } else if (split_list[1].trim().equals(txt_four.getText().toString().trim())) {

                                                      if (color_pos.trim().equals("red")) {




                                                          txt_four_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                          txt_four_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                      } else if (color_pos.trim().equals("yellow")) {



                                                          txt_four_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                          txt_four_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                      }


                                                  } else if (split_list[1].trim().equals(txt_five.getText().toString().trim())) {

                                                      if (color_pos.trim().equals("red")) {




                                                          txt_five_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                          txt_five_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                      } else if (color_pos.trim().equals("yellow")) {

                                                          txt_five_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                          txt_five_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                      }


                                                  } else if (split_list[1].trim().equals(txt_six.getText().toString().trim())) {

                                                      if (color_pos.trim().equals("red")) {



                                                          txt_six_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                          txt_six_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                      } else if (color_pos.trim().equals("yellow")) {

                                                          txt_six_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                          txt_six_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                      }


                                                  } else if (split_list[1].trim().equals(txt_seven.getText().toString().trim())) {

                                                      if (color_pos.trim().equals("red")) {




                                                          txt_seven_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                          txt_seven_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                      } else if (color_pos.trim().equals("yellow")) {


                                                          txt_seven_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                          txt_seven_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                      }


                                                  } else if (split_list[1].trim().equals(txt_eight.getText().toString().trim())) {

                                                      if (color_pos.trim().equals("red")) {




                                                          txt_eight_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                          txt_eight_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                      } else if (color_pos.trim().equals("yellow")) {


                                                          txt_eight_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                          txt_eight_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                      }


                                                  }
                                                  else if (split_list[1].trim().equals(txt_nine.getText().toString().trim())) {

                                                      if (color_pos.trim().equals("red")) {

                                                          txt_nine_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                          txt_nine_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                      } else if (color_pos.trim().equals("yellow")) {

                                                          txt_nine_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                          txt_nine_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                      }


                                                  }

                                              }

                                              else {

                                                  Log.i("pairValue", pair);

                                                  Random rand = new Random();
                                                  int index = rand.nextInt((resIdOne.length- 1) - 0 + 1) + 0;


                                                  if (pair.trim().equals(txt_one.getText().toString().trim())) {

                                                      if (color_pos.trim().equals("red")) {


                                                          txt_one_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                          txt_one_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                      } else if (color_pos.trim().equals("yellow")) {

                                                          txt_one_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                          txt_one_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                      }


                                                  } else if (pair.trim().equals(txt_two.getText().toString().trim())) {

                                                      if (color_pos.trim().equals("red")) {



                                                          txt_two_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                          txt_two_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                      } else if (color_pos.trim().equals("yellow")) {

                                                          txt_two_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                          txt_two_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                      }


                                                  } else if (pair.trim().equals(txt_three.getText().toString().trim())) {

                                                      if (color_pos.trim().equals("red")) {



                                                          txt_three_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                          txt_three_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                      } else if (color_pos.trim().equals("yellow")) {

                                                          txt_three_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                          txt_three_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                      }


                                                  } else if (pair.trim().equals(txt_four.getText().toString().trim())) {

                                                      if (color_pos.trim().equals("red")) {




                                                          txt_four_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                          txt_four_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                      } else if (color_pos.trim().equals("yellow")) {

                                                          txt_four_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                          txt_four_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                      }


                                                  } else if (pair.trim().equals(txt_five.getText().toString().trim())) {

                                                      if (color_pos.trim().equals("red")) {




                                                          txt_five_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                          txt_five_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                      } else if (color_pos.trim().equals("yellow")) {

                                                          txt_five_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                          txt_five_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                      }

                                                  } else if (pair.trim().equals(txt_six.getText().toString().trim())) {

                                                      if (color_pos.trim().equals("red")) {



                                                          txt_six_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                          txt_six_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                      } else if (color_pos.trim().equals("yellow")) {

                                                          txt_six_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                          txt_six_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                      }


                                                  } else if (pair.trim().equals(txt_seven.getText().toString().trim())) {

                                                      if (color_pos.trim().equals("red")) {




                                                          txt_seven_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                          txt_seven_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                      } else if (color_pos.trim().equals("yellow")) {

                                                          txt_seven_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                          txt_seven_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                      }


                                                  } else if (pair.trim().equals(txt_eight.getText().toString().trim())) {

                                                      if (color_pos.trim().equals("red")) {

                                                          txt_eight_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                          txt_eight_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                      } else if (color_pos.trim().equals("yellow")) {

                                                          txt_eight_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                          txt_eight_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                      }


                                                  } else if (pair.trim().equals(txt_nine.getText().toString().trim())) {

                                                      if (color_pos.trim().equals("red")) {




                                                          txt_nine_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                          txt_nine_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                      } else if (color_pos.trim().equals("yellow")) {

                                                          txt_nine_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                          txt_nine_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                      }

                                                  }

                                              }

                                          }

                                          else
                                              {


                                              if (pair.contains(",")) {

                                                  Random rand = new Random();
                                                  int index = rand.nextInt((resIdOne.length- 1) - 0 + 1) + 0;

                                                  String[] split_list = pair.split(",");
                                                  Log.i("pairValue", split_list[0] + "second" + split_list[1]);


                                                  if (split_list[0].trim().equals(txt_one.getText().toString().trim())) {

                                                      if (color_pos.trim().equals("red")) {

                                                          txt_one.setTextColor(getResources().getColor(R.color.coloryellow));
                                                          txt_one_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                      } else if (color_pos.trim().equals("yellow")) {


                                                          txt_one.setTextColor(getResources().getColor(R.color.coloryellow));
                                                          txt_one_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                      }


                                                  } else if (split_list[0].trim().equals(txt_two.getText().toString().trim())) {
                                                      if (color_pos.trim().equals("red")) {

                                                          txt_two.setTextColor(getResources().getColor(R.color.coloryellow));
                                                          txt_two_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                      } else if (color_pos.trim().equals("yellow")) {



                                                          txt_two.setTextColor(getResources().getColor(R.color.coloryellow));
                                                          txt_two_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                      }

                                                  }
                                                  else if (split_list[0].trim().equals(txt_three.getText().toString().trim())) {
                                                      if (color_pos.trim().equals("red")) {


                                                          txt_three.setTextColor(getResources().getColor(R.color.coloryellow));
                                                          txt_three_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                      } else if (color_pos.trim().equals("yellow")) {




                                                          txt_three.setTextColor(getResources().getColor(R.color.coloryellow));
                                                          txt_three_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                      }


                                                  } else if (split_list[0].trim().equals(txt_four.getText().toString().trim())) {
                                                      if (color_pos.trim().equals("red")) {

                                                          txt_four.setTextColor(getResources().getColor(R.color.coloryellow));
                                                          txt_four_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                      } else if (color_pos.trim().equals("yellow")) {




                                                          txt_four.setTextColor(getResources().getColor(R.color.coloryellow));
                                                          txt_four_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                      }

                                                  } else if (split_list[0].trim().equals(txt_five.getText().toString().trim())) {
                                                      if (color_pos.trim().equals("red")) {


                                                          txt_five.setTextColor(getResources().getColor(R.color.coloryellow));
                                                          txt_five_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                      } else if (color_pos.trim().equals("yellow")) {




                                                          txt_five.setTextColor(getResources().getColor(R.color.coloryellow));
                                                          txt_five_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                      }


                                                  } else if (split_list[0].trim().equals(txt_six.getText().toString().trim())) {
                                                      if (color_pos.trim().equals("red")) {

                                                          txt_six.setTextColor(getResources().getColor(R.color.coloryellow));
                                                          txt_six_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                      } else if (color_pos.trim().equals("yellow")) {


                                                          txt_six.setTextColor(getResources().getColor(R.color.coloryellow));
                                                          txt_six_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                      }

                                                  } else if (split_list[0].trim().equals(txt_seven.getText().toString().trim())) {
                                                      if (color_pos.trim().equals("red")) {

                                                          txt_seven.setTextColor(getResources().getColor(R.color.coloryellow));
                                                          txt_seven_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                      } else if (color_pos.trim().equals("yellow")) {




                                                          txt_seven.setTextColor(getResources().getColor(R.color.coloryellow));
                                                          txt_seven_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                      }

                                                  } else if (split_list[0].trim().equals(txt_eight.getText().toString().trim())) {
                                                      if (color_pos.trim().equals("red")) {

                                                          txt_eight.setTextColor(getResources().getColor(R.color.coloryellow));
                                                          txt_eight_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                      } else if (color_pos.trim().equals("yellow")) {

                                                          txt_eight.setTextColor(getResources().getColor(R.color.coloryellow));
                                                          txt_eight_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                      }

                                                  } else if (split_list[0].trim().equals(txt_nine.getText().toString().trim())) {
                                                      if (color_pos.trim().equals("red")) {

                                                          txt_nine.setTextColor(getResources().getColor(R.color.coloryellow));
                                                          txt_nine_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                      } else if (color_pos.trim().equals("yellow")) {


                                                          txt_nine.setTextColor(getResources().getColor(R.color.coloryellow));
                                                          txt_nine_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                      }

                                                  }


                                                  if (split_list[1].trim().equals(txt_one.getText().toString().trim())) {

                                                      if (color_pos.trim().equals("red")) {

                                                          txt_one.setTextColor(getResources().getColor(R.color.coloryellow));
                                                          txt_one_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                      } else if (color_pos.trim().equals("yellow")) {


                                                          txt_one.setTextColor(getResources().getColor(R.color.coloryellow));
                                                          txt_one_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                      }

                                                  } else if (split_list[1].trim().equals(txt_two.getText().toString().trim())) {
                                                      if (color_pos.trim().equals("red")) {

                                                          txt_two.setTextColor(getResources().getColor(R.color.coloryellow));
                                                          txt_two_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                      } else if (color_pos.trim().equals("yellow")) {


                                                          txt_two.setTextColor(getResources().getColor(R.color.coloryellow));
                                                          txt_two_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                      }

                                                  }
                                                  else if (split_list[1].trim().equals(txt_three.getText().toString().trim())) {
                                                      if (color_pos.trim().equals("red")) {

                                                          txt_three.setTextColor(getResources().getColor(R.color.coloryellow));
                                                          txt_three_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                      } else if (color_pos.trim().equals("yellow")) {



                                                          txt_three.setTextColor(getResources().getColor(R.color.coloryellow));
                                                          txt_three_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                      }

                                                  }
                                                  else if (split_list[1].trim().equals(txt_four.getText().toString().trim())) {
                                                      if (color_pos.trim().equals("red")) {

                                                          txt_four.setTextColor(getResources().getColor(R.color.coloryellow));
                                                          txt_four_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                      } else if (color_pos.trim().equals("yellow")) {


                                                          txt_four.setTextColor(getResources().getColor(R.color.coloryellow));
                                                          txt_four_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                      }


                                                  } else if (split_list[1].trim().equals(txt_five.getText().toString().trim())) {
                                                      if (color_pos.trim().equals("red")) {

                                                          txt_five.setTextColor(getResources().getColor(R.color.coloryellow));
                                                          txt_five_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                      } else if (color_pos.trim().equals("yellow")) {




                                                          txt_five.setTextColor(getResources().getColor(R.color.coloryellow));
                                                          txt_five_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                      }

                                                  } else if (split_list[1].trim().equals(txt_six.getText().toString().trim())) {
                                                      if (color_pos.trim().equals("red")) {

                                                          txt_six.setTextColor(getResources().getColor(R.color.coloryellow));
                                                          txt_six_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                      } else if (color_pos.trim().equals("yellow")) {


                                                          txt_six.setTextColor(getResources().getColor(R.color.coloryellow));
                                                          txt_six_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                      }

                                                  }

                                                  else if (split_list[1].trim().equals(txt_seven.getText().toString().trim())) {
                                                      if (color_pos.trim().equals("red")) {

                                                          txt_seven.setTextColor(getResources().getColor(R.color.coloryellow));
                                                          txt_seven_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                      } else if (color_pos.trim().equals("yellow")) {



                                                          txt_seven.setTextColor(getResources().getColor(R.color.coloryellow));
                                                          txt_seven_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                      }

                                                  } else if (split_list[1].trim().equals(txt_eight.getText().toString().trim())) {
                                                      if (color_pos.trim().equals("red")) {

                                                          txt_eight.setTextColor(getResources().getColor(R.color.coloryellow));
                                                          txt_eight_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                      } else if (color_pos.trim().equals("yellow")) {




                                                          txt_eight.setTextColor(getResources().getColor(R.color.coloryellow));
                                                          txt_eight_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                      }

                                                  } else if (split_list[1].trim().equals(txt_nine.getText().toString().trim())) {
                                                      if (color_pos.trim().equals("red")) {


                                                          txt_nine.setTextColor(getResources().getColor(R.color.coloryellow));
                                                          txt_nine_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                      } else if (color_pos.trim().equals("yellow")) {




                                                          txt_nine.setTextColor(getResources().getColor(R.color.coloryellow));
                                                          txt_nine_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                      }

                                                  }


                                              }

                                              else {

                                                  Log.i("pairValue", pair);

                                                  Random rand = new Random();
                                                  int index = rand.nextInt((resIdOne.length- 1) - 0 + 1) + 0;


                                                  if (pair.trim().equals(txt_one.getText().toString().trim())) {

                                                      if (color_pos.trim().equals("red")) {

                                                          txt_one.setTextColor(getResources().getColor(R.color.coloryellow));
                                                          txt_one_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                      } else if (color_pos.trim().equals("yellow")) {




                                                          txt_one.setTextColor(getResources().getColor(R.color.coloryellow));
                                                          txt_one_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                      }


                                                  } else if (pair.trim().equals(txt_two.getText().toString().trim())) {

                                                      if (color_pos.trim().equals("red")) {

                                                          txt_two.setTextColor(getResources().getColor(R.color.coloryellow));
                                                          txt_two_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                      } else if (color_pos.trim().equals("yellow")) {




                                                          txt_two.setTextColor(getResources().getColor(R.color.coloryellow));
                                                          txt_two_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                      }

                                                  } else if (pair.trim().equals(txt_three.getText().toString().trim())) {
                                                      if (color_pos.trim().equals("red")) {

                                                          txt_three.setTextColor(getResources().getColor(R.color.coloryellow));
                                                          txt_three_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                      } else if (color_pos.trim().equals("yellow")) {




                                                          txt_three.setTextColor(getResources().getColor(R.color.coloryellow));
                                                          txt_three_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                      }
                                                  }
                                                  else if (pair.trim().equals(txt_four.getText().toString().trim())) {
                                                      if (color_pos.trim().equals("red")) {


                                                          txt_four.setTextColor(getResources().getColor(R.color.coloryellow));
                                                          txt_four_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                      } else if (color_pos.trim().equals("yellow")) {




                                                          txt_four.setTextColor(getResources().getColor(R.color.coloryellow));
                                                          txt_four_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                      }

                                                  } else if (pair.trim().equals(txt_five.getText().toString().trim())) {
                                                      if (color_pos.trim().equals("red")) {

                                                          txt_five.setTextColor(getResources().getColor(R.color.coloryellow));
                                                          txt_five_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                      } else if (color_pos.trim().equals("yellow")) {


                                                          txt_five.setTextColor(getResources().getColor(R.color.coloryellow));
                                                          txt_five_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                      }
                                                  } else if (pair.trim().equals(txt_six.getText().toString().trim())) {
                                                      if (color_pos.trim().equals("red")) {

                                                          txt_six.setTextColor(getResources().getColor(R.color.coloryellow));
                                                          txt_six_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                      } else if (color_pos.trim().equals("yellow")) {




                                                          txt_six.setTextColor(getResources().getColor(R.color.coloryellow));
                                                          txt_six_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                      }
                                                  }
                                                  else if (pair.trim().equals(txt_seven.getText().toString().trim())) {
                                                      if (color_pos.trim().equals("red")) {

                                                          txt_seven.setTextColor(getResources().getColor(R.color.coloryellow));
                                                          txt_seven_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                      } else if (color_pos.trim().equals("yellow")) {




                                                          txt_seven.setTextColor(getResources().getColor(R.color.coloryellow));
                                                          txt_seven_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                      }
                                                  } else if (pair.trim().equals(txt_eight.getText().toString().trim())) {
                                                      if (color_pos.trim().equals("red")) {

                                                          txt_eight.setTextColor(getResources().getColor(R.color.coloryellow));
                                                          txt_eight_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                      } else if (color_pos.trim().equals("yellow")) {




                                                          txt_eight.setTextColor(getResources().getColor(R.color.coloryellow));
                                                          txt_eight_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                      }
                                                  } else if (pair.trim().equals(txt_nine.getText().toString().trim())) {
                                                      if (color_pos.trim().equals("red")) {

                                                          txt_nine.setTextColor(getResources().getColor(R.color.coloryellow));
                                                          txt_nine_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                      } else if (color_pos.trim().equals("yellow")) {




                                                          txt_nine.setTextColor(getResources().getColor(R.color.coloryellow));
                                                          txt_nine_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                      }
                                                  }

                                              }



                                          }



                                        int count_your=0;
                                        int count_opponent=0;


                                        if (txt_one.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                                             count_your=count_your+1;
                                         }
                                         if (txt_two.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                                             count_your=count_your+2;
                                         }
                                         if (txt_three.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                                             count_your=count_your+3;
                                         }
                                         if (txt_four.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                                             count_your=count_your+4;
                                         }
                                         if (txt_five.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                                             count_your=count_your+5;
                                         }
                                         if (txt_six.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                                             count_your=count_your+6;
                                         }
                                         if (txt_seven.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                                             count_your=count_your+7;
                                         }
                                         if (txt_eight.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                                             count_your=count_your+8;
                                         }
                                         if (txt_nine.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                                             count_your=count_your+9;
                                         }



                                        if (txt_one_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                                            count_opponent=count_opponent+1;
                                        }
                                        if (txt_two_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                                            count_opponent=count_opponent+2;
                                        }
                                        if (txt_three_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                                            count_opponent=count_opponent+3;
                                        }
                                        if (txt_four_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                                            count_opponent=count_opponent+4;
                                        }
                                        if (txt_five_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                                            count_opponent=count_opponent+5;
                                        }
                                        if (txt_six_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                                            count_opponent=count_opponent+6;
                                        }
                                        if (txt_seven_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                                            count_opponent=count_opponent+7;
                                        }
                                        if (txt_eight_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                                            count_opponent=count_opponent+8;
                                        }
                                        if (txt_nine_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                                            count_opponent=count_opponent+9;
                                        }


                                        if(count_your<=count_opponent){


                                            addsAndAddCoins();


                                            showDialog("WINNER!");

                                        }
                                        else {

                                            showDialog("BETTER LUCK NEXT TIME!");
                                        }

                                        run = true;

                                    }


                                    else {

                                        if (!userid.equals(SharedHelper.getKey(getApplicationContext(), UserConstant.id))) {


                                            Log.i("HereElsePrt", "ifPart");

                                           // dice_picture.startAnimation(rotate);
                                           // dice2_picture.startAnimation(rotate);

                                            new Handler().postDelayed(new Runnable() {


                                                @Override

                                                public void run() {



                                                    iv_profile_center.setVisibility(View.GONE);

                                                    Toast.makeText(getApplicationContext(),"Your Turn",Toast.LENGTH_LONG).show();

                                                    methodVisibleGoneWhenMyTurn();




                                                    if (pair.contains(",")) {

                                                        Random rand = new Random();
                                                        int index = rand.nextInt((resIdOne.length- 1) - 0 + 1) + 0;
                                                        String[] split_list = pair.split(",");
                                                        Log.i("pairValue", split_list[0] + "second" + split_list[1]);


                                                        if (split_list[0].trim().equals(txt_one.getText().toString().trim())) {

                                                            if (color_pos.trim().equals("red")) {



                                                                txt_one_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                                txt_one_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                            } else if (color_pos.trim().equals("yellow")) {

                                                                txt_one_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                                txt_one_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                            }

                                                        }

                                                        else if (split_list[0].trim().equals(txt_two.getText().toString().trim())) {

                                                            if (color_pos.trim().equals("red")) {

                                                                //Random rand = new Random();
                                                                //int index = rand.nextInt((resIdTwo.length- 1) - 0 + 1) + 0;


                                                                txt_two_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                                txt_two_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                            } else if (color_pos.trim().equals("yellow")) {

                                                                txt_two_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                                txt_two_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                            }


                                                        }

                                                        else if (split_list[0].trim().equals(txt_three.getText().toString().trim())) {

                                                            if (color_pos.trim().equals("red")) {



                                                                txt_three_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                                txt_three_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                            } else if (color_pos.trim().equals("yellow")) {

                                                                txt_three_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                                txt_three_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                            }


                                                        }

                                                        else if (split_list[0].trim().equals(txt_four.getText().toString().trim())) {

                                                            if (color_pos.trim().equals("red")) {


                                                                txt_four_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                                txt_four_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                            } else if (color_pos.trim().equals("yellow")) {

                                                                txt_four_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                                txt_four_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                            }


                                                        } else if (split_list[0].trim().equals(txt_five.getText().toString().trim())) {

                                                            if (color_pos.trim().equals("red")) {



                                                                txt_five_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                                txt_five_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                            } else if (color_pos.trim().equals("yellow")) {

                                                                txt_five_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                                txt_five_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                            }


                                                        } else if (split_list[0].trim().equals(txt_six.getText().toString().trim())) {

                                                            if (color_pos.trim().equals("red")) {



                                                                txt_six_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                                txt_six_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                            } else if (color_pos.trim().equals("yellow")) {

                                                                txt_six_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                                txt_six_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                            }


                                                        } else if (split_list[0].trim().equals(txt_seven.getText().toString().trim())) {

                                                            if (color_pos.trim().equals("red")) {



                                                                txt_seven_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                                txt_seven_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                            } else if (color_pos.trim().equals("yellow")) {

                                                                txt_seven_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                                txt_seven_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                            }


                                                        } else if (split_list[0].trim().equals(txt_eight.getText().toString().trim())) {

                                                            if (color_pos.trim().equals("red")) {


                                                                txt_eight_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                                txt_eight_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                            } else if (color_pos.trim().equals("yellow")) {

                                                                txt_eight_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                                txt_eight_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                            }


                                                        }
                                                        else if (split_list[0].trim().equals(txt_nine.getText().toString().trim())) {

                                                            if (color_pos.trim().equals("red")) {




                                                                txt_nine_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                                txt_nine_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                                                            } else if (color_pos.trim().equals("yellow")) {


                                                                txt_nine_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                                txt_nine_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                            }


                                                        }


                                                        if (split_list[1].trim().equals(txt_one.getText().toString().trim())) {

                                                            if (color_pos.trim().equals("red")) {



                                                                txt_one_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                                txt_one_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                            } else if (color_pos.trim().equals("yellow")) {

                                                                txt_one_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                                txt_one_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                            }


                                                        }

                                                        else if (split_list[1].trim().equals(txt_two.getText().toString().trim())) {

                                                            if (color_pos.trim().equals("red")) {




                                                                txt_two_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                                txt_two_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                            } else if (color_pos.trim().equals("yellow")) {

                                                                txt_two_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                                txt_two_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                            }


                                                        } else if (split_list[1].trim().equals(txt_three.getText().toString().trim())) {

                                                            if (color_pos.trim().equals("red")) {



                                                                txt_three_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                                txt_three_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                            } else if (color_pos.trim().equals("yellow")) {

                                                                txt_three_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                                txt_three_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                            }


                                                        } else if (split_list[1].trim().equals(txt_four.getText().toString().trim())) {

                                                            if (color_pos.trim().equals("red")) {




                                                                txt_four_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                                txt_four_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                            } else if (color_pos.trim().equals("yellow")) {



                                                                txt_four_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                                txt_four_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                            }


                                                        } else if (split_list[1].trim().equals(txt_five.getText().toString().trim())) {

                                                            if (color_pos.trim().equals("red")) {




                                                                txt_five_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                                txt_five_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                            } else if (color_pos.trim().equals("yellow")) {

                                                                txt_five_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                                txt_five_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                            }


                                                        } else if (split_list[1].trim().equals(txt_six.getText().toString().trim())) {

                                                            if (color_pos.trim().equals("red")) {



                                                                txt_six_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                                txt_six_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                            } else if (color_pos.trim().equals("yellow")) {

                                                                txt_six_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                                txt_six_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                            }


                                                        } else if (split_list[1].trim().equals(txt_seven.getText().toString().trim())) {

                                                            if (color_pos.trim().equals("red")) {




                                                                txt_seven_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                                txt_seven_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                            } else if (color_pos.trim().equals("yellow")) {


                                                                txt_seven_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                                txt_seven_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                            }


                                                        } else if (split_list[1].trim().equals(txt_eight.getText().toString().trim())) {

                                                            if (color_pos.trim().equals("red")) {




                                                                txt_eight_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                                txt_eight_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                            } else if (color_pos.trim().equals("yellow")) {


                                                                txt_eight_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                                txt_eight_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                            }


                                                        }
                                                        else if (split_list[1].trim().equals(txt_nine.getText().toString().trim())) {

                                                            if (color_pos.trim().equals("red")) {




                                                                txt_nine_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                                txt_nine_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                            } else if (color_pos.trim().equals("yellow")) {

                                                                txt_nine_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                                txt_nine_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                            }


                                                        }

                                                    }

                                                    else {

                                                        Log.i("pairValue", pair);

                                                        Random rand = new Random();
                                                        int index = rand.nextInt((resIdOne.length- 1) - 0 + 1) + 0;


                                                        if (pair.trim().equals(txt_one.getText().toString().trim())) {

                                                            if (color_pos.trim().equals("red")) {


                                                                txt_one_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                                txt_one_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                            } else if (color_pos.trim().equals("yellow")) {

                                                                txt_one_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                                txt_one_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                            }


                                                        } else if (pair.trim().equals(txt_two.getText().toString().trim())) {

                                                            if (color_pos.trim().equals("red")) {



                                                                txt_two_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                                txt_two_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                            } else if (color_pos.trim().equals("yellow")) {

                                                                txt_two_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                                txt_two_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                            }


                                                        } else if (pair.trim().equals(txt_three.getText().toString().trim())) {

                                                            if (color_pos.trim().equals("red")) {



                                                                txt_three_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                                txt_three_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                            } else if (color_pos.trim().equals("yellow")) {

                                                                txt_three_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                                txt_three_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                            }


                                                        } else if (pair.trim().equals(txt_four.getText().toString().trim())) {

                                                            if (color_pos.trim().equals("red")) {




                                                                txt_four_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                                txt_four_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                            } else if (color_pos.trim().equals("yellow")) {

                                                                txt_four_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                                txt_four_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                            }


                                                        } else if (pair.trim().equals(txt_five.getText().toString().trim())) {

                                                            if (color_pos.trim().equals("red")) {




                                                                txt_five_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                                txt_five_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                            } else if (color_pos.trim().equals("yellow")) {

                                                                txt_five_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                                txt_five_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                            }

                                                        } else if (pair.trim().equals(txt_six.getText().toString().trim())) {

                                                            if (color_pos.trim().equals("red")) {



                                                                txt_six_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                                txt_six_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                            } else if (color_pos.trim().equals("yellow")) {

                                                                txt_six_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                                txt_six_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                            }


                                                        } else if (pair.trim().equals(txt_seven.getText().toString().trim())) {

                                                            if (color_pos.trim().equals("red")) {




                                                                txt_seven_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                                txt_seven_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                            } else if (color_pos.trim().equals("yellow")) {

                                                                txt_seven_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                                txt_seven_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                            }


                                                        } else if (pair.trim().equals(txt_eight.getText().toString().trim())) {

                                                            if (color_pos.trim().equals("red")) {




                                                                txt_eight_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                                txt_eight_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                            } else if (color_pos.trim().equals("yellow")) {

                                                                txt_eight_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                                txt_eight_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                            }


                                                        } else if (pair.trim().equals(txt_nine.getText().toString().trim())) {

                                                            if (color_pos.trim().equals("red")) {




                                                                txt_nine_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                                txt_nine_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                            } else if (color_pos.trim().equals("yellow")) {

                                                                txt_nine_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                                txt_nine_oppo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                                            }

                                                        }

                                                    }


                                                    Log.e("TAG", "handleMessage: " + run);
                                                    if (!userid.equals(SharedHelper.getKey(getApplicationContext(), UserConstant.id))) {
                                                        if (txt_one_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_one_bottem.getCurrentTextColor() == getResources().getColor(R.color.coloryellow) || txt_one_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorsolidGreen) || txt_one_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorsDarkSky) || txt_one_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorRedLightNew)) {
                                                            if (txt_two_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_two_bottem.getCurrentTextColor() == getResources().getColor(R.color.coloryellow) || txt_two_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorsolidGreen) || txt_two_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorsDarkSky) || txt_two_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorRedLightNew)) {
                                                                if (txt_three_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_three_bottem.getCurrentTextColor() == getResources().getColor(R.color.coloryellow) || txt_three_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorsolidGreen) || txt_three_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorsDarkSky) || txt_three_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorRedLightNew)) {
                                                                    if (txt_four_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_four_bottem.getCurrentTextColor() == getResources().getColor(R.color.coloryellow) || txt_four_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorsolidGreen) || txt_four_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorsDarkSky) || txt_four_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorRedLightNew)) {
                                                                        if (txt_five_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_five_bottem.getCurrentTextColor() == getResources().getColor(R.color.coloryellow) || txt_five_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorsolidGreen) || txt_five_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorsDarkSky) || txt_five_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorRedLightNew)) {
                                                                            if (txt_six_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_six_bottem.getCurrentTextColor() == getResources().getColor(R.color.coloryellow) || txt_six_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorsolidGreen) || txt_six_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorsDarkSky) || txt_six_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorRedLightNew)) {
                                                                                if (txt_seven_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_seven_bottem.getCurrentTextColor() == getResources().getColor(R.color.coloryellow) || txt_seven_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorsolidGreen) || txt_seven_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorsDarkSky) || txt_seven_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorRedLightNew)) {
                                                                                    if (txt_eight_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_eight_bottem.getCurrentTextColor() == getResources().getColor(R.color.coloryellow) || txt_eight_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorsolidGreen) || txt_eight_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorsDarkSky) || txt_eight_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorRedLightNew)) {
                                                                                        if (txt_nine_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_nine_bottem.getCurrentTextColor() == getResources().getColor(R.color.coloryellow) || txt_nine_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorsolidGreen) || txt_nine_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorsDarkSky) || txt_nine_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorRedLightNew)) {


                                                                                            isComputerTurn = false;
                                                                                            layout_dice_movement.setEnabled(false);
                                                                                            showDialog("BETTER LUCK NEXT TIME!");
                                                                                            run = true;


                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                    else if (userid.equals(SharedHelper.getKey(getApplicationContext(), UserConstant.id))) {
                                                        if (txt_one.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_one.getCurrentTextColor() == getResources().getColor(R.color.coloryellow) || txt_one.getCurrentTextColor() == getResources().getColor(R.color.colorsolidGreen) || txt_one.getCurrentTextColor() == getResources().getColor(R.color.colorsDarkSky) || txt_one.getCurrentTextColor() == getResources().getColor(R.color.colorRedLightNew)) {
                                                            if (txt_two.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_two.getCurrentTextColor() == getResources().getColor(R.color.coloryellow) || txt_two.getCurrentTextColor() == getResources().getColor(R.color.colorsolidGreen) || txt_two.getCurrentTextColor() == getResources().getColor(R.color.colorsDarkSky) || txt_two.getCurrentTextColor() == getResources().getColor(R.color.colorRedLightNew)) {
                                                                if (txt_three.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_three.getCurrentTextColor() == getResources().getColor(R.color.coloryellow) || txt_three.getCurrentTextColor() == getResources().getColor(R.color.colorsolidGreen) || txt_three.getCurrentTextColor() == getResources().getColor(R.color.colorsDarkSky) || txt_three.getCurrentTextColor() == getResources().getColor(R.color.colorRedLightNew)) {
                                                                    if (txt_four.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_four.getCurrentTextColor() == getResources().getColor(R.color.coloryellow) || txt_four.getCurrentTextColor() == getResources().getColor(R.color.colorsolidGreen) || txt_four.getCurrentTextColor() == getResources().getColor(R.color.colorsDarkSky) || txt_four.getCurrentTextColor() == getResources().getColor(R.color.colorRedLightNew)) {
                                                                        if (txt_five.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_five.getCurrentTextColor() == getResources().getColor(R.color.coloryellow) || txt_five.getCurrentTextColor() == getResources().getColor(R.color.colorsolidGreen) || txt_five.getCurrentTextColor() == getResources().getColor(R.color.colorsDarkSky) || txt_five.getCurrentTextColor() == getResources().getColor(R.color.colorRedLightNew)) {
                                                                            if (txt_six.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_six.getCurrentTextColor() == getResources().getColor(R.color.coloryellow) || txt_six.getCurrentTextColor() == getResources().getColor(R.color.colorsolidGreen) || txt_six.getCurrentTextColor() == getResources().getColor(R.color.colorsDarkSky) || txt_six.getCurrentTextColor() == getResources().getColor(R.color.colorRedLightNew)) {
                                                                                if (txt_seven.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_seven.getCurrentTextColor() == getResources().getColor(R.color.coloryellow) || txt_seven.getCurrentTextColor() == getResources().getColor(R.color.colorsolidGreen) || txt_seven.getCurrentTextColor() == getResources().getColor(R.color.colorsDarkSky) || txt_seven.getCurrentTextColor() == getResources().getColor(R.color.colorRedLightNew)) {
                                                                                    if (txt_eight.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_eight.getCurrentTextColor() == getResources().getColor(R.color.coloryellow) || txt_eight.getCurrentTextColor() == getResources().getColor(R.color.colorsolidGreen) || txt_eight.getCurrentTextColor() == getResources().getColor(R.color.colorsDarkSky) || txt_eight.getCurrentTextColor() == getResources().getColor(R.color.colorRedLightNew)) {
                                                                                        if (txt_nine.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_nine.getCurrentTextColor() == getResources().getColor(R.color.coloryellow) || txt_nine.getCurrentTextColor() == getResources().getColor(R.color.colorsolidGreen) || txt_nine.getCurrentTextColor() == getResources().getColor(R.color.colorsDarkSky) || txt_nine.getCurrentTextColor() == getResources().getColor(R.color.colorRedLightNew)) {


                                                                                            layout_dice_movement.setEnabled(false);

                                                                                            addsAndAddCoins();

                                                                                            count = 0;

                                                                                            if (txt_one_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                                                                                                count++;
                                                                                            }
                                                                                            if (txt_two_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                                                                                                count++;
                                                                                            }
                                                                                            if (txt_three_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                                                                                                count++;
                                                                                            }
                                                                                            if (txt_four_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                                                                                                count++;
                                                                                            }
                                                                                            if (txt_five_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                                                                                                count++;
                                                                                            }
                                                                                            if (txt_six_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                                                                                                count++;
                                                                                            }
                                                                                            if (txt_seven_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                                                                                                count++;
                                                                                            }
                                                                                            if (txt_eight_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                                                                                                count++;
                                                                                            }
                                                                                            if (txt_nine_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                                                                                                count++;
                                                                                            }


                                                                                            if (count <= 1) {

                                                                                                showDialog("WINNER!");

                                                                                            } else {
                                                                                                showDialogBucky();
                                                                                            }


                                                                                            run = true;

                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }


                                                    }


                                                }

                                            }, 500);


                                        }

                                        else
                                            {


                                            Log.i("HereElsePrt", "elsePart");

                                            iv_profile_center.setVisibility(View.VISIBLE);


                                            Toast.makeText(getApplicationContext(),"Opponent Turn",Toast.LENGTH_LONG).show();


                                            methodVisibleGoneWhenOpponentTurn();


                                                if (pair.contains(",")) {

                                                    Random rand = new Random();
                                                    int index = rand.nextInt((resIdOne.length- 1) - 0 + 1) + 0;

                                                    String[] split_list = pair.split(",");
                                                    Log.i("pairValue", split_list[0] + "second" + split_list[1]);


                                                    if (split_list[0].trim().equals(txt_one.getText().toString().trim())) {

                                                        if (color_pos.trim().equals("red")) {

                                                            txt_one.setTextColor(getResources().getColor(R.color.coloryellow));
                                                            txt_one_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                        } else if (color_pos.trim().equals("yellow")) {


                                                            txt_one.setTextColor(getResources().getColor(R.color.coloryellow));
                                                            txt_one_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                        }


                                                    } else if (split_list[0].trim().equals(txt_two.getText().toString().trim())) {
                                                        if (color_pos.trim().equals("red")) {

                                                            txt_two.setTextColor(getResources().getColor(R.color.coloryellow));
                                                            txt_two_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                        } else if (color_pos.trim().equals("yellow")) {



                                                            txt_two.setTextColor(getResources().getColor(R.color.coloryellow));
                                                            txt_two_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                        }

                                                    }
                                                    else if (split_list[0].trim().equals(txt_three.getText().toString().trim())) {
                                                        if (color_pos.trim().equals("red")) {


                                                            txt_three.setTextColor(getResources().getColor(R.color.coloryellow));
                                                            txt_three_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                        } else if (color_pos.trim().equals("yellow")) {




                                                            txt_three.setTextColor(getResources().getColor(R.color.coloryellow));
                                                            txt_three_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                        }


                                                    } else if (split_list[0].trim().equals(txt_four.getText().toString().trim())) {
                                                        if (color_pos.trim().equals("red")) {

                                                            txt_four.setTextColor(getResources().getColor(R.color.coloryellow));
                                                            txt_four_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                        } else if (color_pos.trim().equals("yellow")) {




                                                            txt_four.setTextColor(getResources().getColor(R.color.coloryellow));
                                                            txt_four_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                        }

                                                    } else if (split_list[0].trim().equals(txt_five.getText().toString().trim())) {
                                                        if (color_pos.trim().equals("red")) {


                                                            txt_five.setTextColor(getResources().getColor(R.color.coloryellow));
                                                            txt_five_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                        } else if (color_pos.trim().equals("yellow")) {




                                                            txt_five.setTextColor(getResources().getColor(R.color.coloryellow));
                                                            txt_five_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                        }


                                                    } else if (split_list[0].trim().equals(txt_six.getText().toString().trim())) {
                                                        if (color_pos.trim().equals("red")) {

                                                            txt_six.setTextColor(getResources().getColor(R.color.coloryellow));
                                                            txt_six_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                        } else if (color_pos.trim().equals("yellow")) {


                                                            txt_six.setTextColor(getResources().getColor(R.color.coloryellow));
                                                            txt_six_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                        }

                                                    } else if (split_list[0].trim().equals(txt_seven.getText().toString().trim())) {
                                                        if (color_pos.trim().equals("red")) {

                                                            txt_seven.setTextColor(getResources().getColor(R.color.coloryellow));
                                                            txt_seven_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                        } else if (color_pos.trim().equals("yellow")) {




                                                            txt_seven.setTextColor(getResources().getColor(R.color.coloryellow));
                                                            txt_seven_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                        }

                                                    } else if (split_list[0].trim().equals(txt_eight.getText().toString().trim())) {
                                                        if (color_pos.trim().equals("red")) {

                                                            txt_eight.setTextColor(getResources().getColor(R.color.coloryellow));
                                                            txt_eight_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                        } else if (color_pos.trim().equals("yellow")) {




                                                            txt_eight.setTextColor(getResources().getColor(R.color.coloryellow));
                                                            txt_eight_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                        }

                                                    } else if (split_list[0].trim().equals(txt_nine.getText().toString().trim())) {
                                                        if (color_pos.trim().equals("red")) {

                                                            txt_nine.setTextColor(getResources().getColor(R.color.coloryellow));
                                                            txt_nine_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                        } else if (color_pos.trim().equals("yellow")) {


                                                            txt_nine.setTextColor(getResources().getColor(R.color.coloryellow));
                                                            txt_nine_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                        }

                                                    }


                                                    if (split_list[1].trim().equals(txt_one.getText().toString().trim())) {

                                                        if (color_pos.trim().equals("red")) {

                                                            txt_one.setTextColor(getResources().getColor(R.color.coloryellow));
                                                            txt_one_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                        } else if (color_pos.trim().equals("yellow")) {


                                                            txt_one.setTextColor(getResources().getColor(R.color.coloryellow));
                                                            txt_one_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                        }

                                                    } else if (split_list[1].trim().equals(txt_two.getText().toString().trim())) {
                                                        if (color_pos.trim().equals("red")) {

                                                            txt_two.setTextColor(getResources().getColor(R.color.coloryellow));
                                                            txt_two_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                        } else if (color_pos.trim().equals("yellow")) {


                                                            txt_two.setTextColor(getResources().getColor(R.color.coloryellow));
                                                            txt_two_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                        }

                                                    }
                                                    else if (split_list[1].trim().equals(txt_three.getText().toString().trim())) {
                                                        if (color_pos.trim().equals("red")) {

                                                            txt_three.setTextColor(getResources().getColor(R.color.coloryellow));
                                                            txt_three_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                        } else if (color_pos.trim().equals("yellow")) {



                                                            txt_three.setTextColor(getResources().getColor(R.color.coloryellow));
                                                            txt_three_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                        }

                                                    }
                                                    else if (split_list[1].trim().equals(txt_four.getText().toString().trim())) {
                                                        if (color_pos.trim().equals("red")) {

                                                            txt_four.setTextColor(getResources().getColor(R.color.coloryellow));
                                                            txt_four_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                        } else if (color_pos.trim().equals("yellow")) {


                                                            txt_four.setTextColor(getResources().getColor(R.color.coloryellow));
                                                            txt_four_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                        }


                                                    } else if (split_list[1].trim().equals(txt_five.getText().toString().trim())) {
                                                        if (color_pos.trim().equals("red")) {

                                                            txt_five.setTextColor(getResources().getColor(R.color.coloryellow));
                                                            txt_five_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                        } else if (color_pos.trim().equals("yellow")) {




                                                            txt_five.setTextColor(getResources().getColor(R.color.coloryellow));
                                                            txt_five_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                        }

                                                    } else if (split_list[1].trim().equals(txt_six.getText().toString().trim())) {
                                                        if (color_pos.trim().equals("red")) {

                                                            txt_six.setTextColor(getResources().getColor(R.color.coloryellow));
                                                            txt_six_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                        } else if (color_pos.trim().equals("yellow")) {


                                                            txt_six.setTextColor(getResources().getColor(R.color.coloryellow));
                                                            txt_six_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                        }

                                                    }

                                                    else if (split_list[1].trim().equals(txt_seven.getText().toString().trim())) {
                                                        if (color_pos.trim().equals("red")) {

                                                            txt_seven.setTextColor(getResources().getColor(R.color.coloryellow));
                                                            txt_seven_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                        } else if (color_pos.trim().equals("yellow")) {



                                                            txt_seven.setTextColor(getResources().getColor(R.color.coloryellow));
                                                            txt_seven_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                        }

                                                    } else if (split_list[1].trim().equals(txt_eight.getText().toString().trim())) {
                                                        if (color_pos.trim().equals("red")) {

                                                            txt_eight.setTextColor(getResources().getColor(R.color.coloryellow));
                                                            txt_eight_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                        } else if (color_pos.trim().equals("yellow")) {




                                                            txt_eight.setTextColor(getResources().getColor(R.color.coloryellow));
                                                            txt_eight_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                        }

                                                    } else if (split_list[1].trim().equals(txt_nine.getText().toString().trim())) {
                                                        if (color_pos.trim().equals("red")) {


                                                            txt_nine.setTextColor(getResources().getColor(R.color.coloryellow));
                                                            txt_nine_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                        } else if (color_pos.trim().equals("yellow")) {




                                                            txt_nine.setTextColor(getResources().getColor(R.color.coloryellow));
                                                            txt_nine_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                        }

                                                    }


                                                }

                                                else {

                                                    Log.i("pairValue", pair);

                                                    Random rand = new Random();
                                                    int index = rand.nextInt((resIdOne.length- 1) - 0 + 1) + 0;


                                                    if (pair.trim().equals(txt_one.getText().toString().trim())) {

                                                        if (color_pos.trim().equals("red")) {

                                                            txt_one.setTextColor(getResources().getColor(R.color.coloryellow));
                                                            txt_one_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                        } else if (color_pos.trim().equals("yellow")) {




                                                            txt_one.setTextColor(getResources().getColor(R.color.coloryellow));
                                                            txt_one_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                        }


                                                    } else if (pair.trim().equals(txt_two.getText().toString().trim())) {

                                                        if (color_pos.trim().equals("red")) {

                                                            txt_two.setTextColor(getResources().getColor(R.color.coloryellow));
                                                            txt_two_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                        } else if (color_pos.trim().equals("yellow")) {




                                                            txt_two.setTextColor(getResources().getColor(R.color.coloryellow));
                                                            txt_two_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                        }

                                                    } else if (pair.trim().equals(txt_three.getText().toString().trim())) {
                                                        if (color_pos.trim().equals("red")) {

                                                            txt_three.setTextColor(getResources().getColor(R.color.coloryellow));
                                                            txt_three_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                        } else if (color_pos.trim().equals("yellow")) {




                                                            txt_three.setTextColor(getResources().getColor(R.color.coloryellow));
                                                            txt_three_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                        }
                                                    }
                                                    else if (pair.trim().equals(txt_four.getText().toString().trim())) {
                                                        if (color_pos.trim().equals("red")) {


                                                            txt_four.setTextColor(getResources().getColor(R.color.coloryellow));
                                                            txt_four_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                        } else if (color_pos.trim().equals("yellow")) {




                                                            txt_four.setTextColor(getResources().getColor(R.color.coloryellow));
                                                            txt_four_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                        }

                                                    } else if (pair.trim().equals(txt_five.getText().toString().trim())) {
                                                        if (color_pos.trim().equals("red")) {

                                                            txt_five.setTextColor(getResources().getColor(R.color.coloryellow));
                                                            txt_five_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                        } else if (color_pos.trim().equals("yellow")) {




                                                            txt_five.setTextColor(getResources().getColor(R.color.coloryellow));
                                                            txt_five_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                        }
                                                    } else if (pair.trim().equals(txt_six.getText().toString().trim())) {
                                                        if (color_pos.trim().equals("red")) {

                                                            txt_six.setTextColor(getResources().getColor(R.color.coloryellow));
                                                            txt_six_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                        } else if (color_pos.trim().equals("yellow")) {




                                                            txt_six.setTextColor(getResources().getColor(R.color.coloryellow));
                                                            txt_six_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                        }
                                                    }
                                                    else if (pair.trim().equals(txt_seven.getText().toString().trim())) {
                                                        if (color_pos.trim().equals("red")) {

                                                            txt_seven.setTextColor(getResources().getColor(R.color.coloryellow));
                                                            txt_seven_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                        } else if (color_pos.trim().equals("yellow")) {




                                                            txt_seven.setTextColor(getResources().getColor(R.color.coloryellow));
                                                            txt_seven_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                        }
                                                    } else if (pair.trim().equals(txt_eight.getText().toString().trim())) {
                                                        if (color_pos.trim().equals("red")) {

                                                            txt_eight.setTextColor(getResources().getColor(R.color.coloryellow));
                                                            txt_eight_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                        } else if (color_pos.trim().equals("yellow")) {




                                                            txt_eight.setTextColor(getResources().getColor(R.color.coloryellow));
                                                            txt_eight_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                        }
                                                    } else if (pair.trim().equals(txt_nine.getText().toString().trim())) {
                                                        if (color_pos.trim().equals("red")) {

                                                            txt_nine.setTextColor(getResources().getColor(R.color.coloryellow));
                                                            txt_nine_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                        } else if (color_pos.trim().equals("yellow")) {




                                                            txt_nine.setTextColor(getResources().getColor(R.color.coloryellow));
                                                            txt_nine_bottem_you.setTextColor(getResources().getColor(R.color.coloryellow));
                                                        }
                                                    }

                                                }


                                            Log.e("TAG", "handleMessage: " + run);
                                            if (!userid.equals(SharedHelper.getKey(getApplicationContext(), UserConstant.id))) {
                                                if (txt_one_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_one_bottem.getCurrentTextColor() == getResources().getColor(R.color.coloryellow) || txt_one_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorsolidGreen) || txt_one_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorsDarkSky) || txt_one_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorRedLightNew)) {
                                                    if (txt_two_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_two_bottem.getCurrentTextColor() == getResources().getColor(R.color.coloryellow) || txt_two_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorsolidGreen) || txt_two_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorsDarkSky) || txt_two_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorRedLightNew)) {
                                                        if (txt_three_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_three_bottem.getCurrentTextColor() == getResources().getColor(R.color.coloryellow) || txt_three_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorsolidGreen) || txt_three_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorsDarkSky) || txt_three_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorRedLightNew)) {
                                                            if (txt_four_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_four_bottem.getCurrentTextColor() == getResources().getColor(R.color.coloryellow) || txt_four_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorsolidGreen) || txt_four_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorsDarkSky) || txt_four_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorRedLightNew)) {
                                                                if (txt_five_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_five_bottem.getCurrentTextColor() == getResources().getColor(R.color.coloryellow) || txt_five_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorsolidGreen) || txt_five_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorsDarkSky) || txt_five_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorRedLightNew)) {
                                                                    if (txt_six_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_six_bottem.getCurrentTextColor() == getResources().getColor(R.color.coloryellow) || txt_six_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorsolidGreen) || txt_six_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorsDarkSky) || txt_six_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorRedLightNew)) {
                                                                        if (txt_seven_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_seven_bottem.getCurrentTextColor() == getResources().getColor(R.color.coloryellow) || txt_seven_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorsolidGreen) || txt_seven_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorsDarkSky) || txt_seven_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorRedLightNew)) {
                                                                            if (txt_eight_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_eight_bottem.getCurrentTextColor() == getResources().getColor(R.color.coloryellow) || txt_eight_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorsolidGreen) || txt_eight_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorsDarkSky) || txt_eight_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorRedLightNew)) {
                                                                                if (txt_nine_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_nine_bottem.getCurrentTextColor() == getResources().getColor(R.color.coloryellow) || txt_nine_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorsolidGreen) || txt_nine_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorsDarkSky) || txt_nine_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorRedLightNew)) {


                                                                                    isComputerTurn = false;
                                                                                    layout_dice_movement.setEnabled(false);
                                                                                    showDialog("BETTER LUCK NEXT TIME!");
                                                                                    run = true;


                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            } else {
                                                if (txt_one.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_one.getCurrentTextColor() == getResources().getColor(R.color.coloryellow) || txt_one.getCurrentTextColor() == getResources().getColor(R.color.colorsolidGreen) || txt_one.getCurrentTextColor() == getResources().getColor(R.color.colorsDarkSky) || txt_one.getCurrentTextColor() == getResources().getColor(R.color.colorRedLightNew)) {
                                                    if (txt_two.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_two.getCurrentTextColor() == getResources().getColor(R.color.coloryellow) || txt_two.getCurrentTextColor() == getResources().getColor(R.color.colorsolidGreen) || txt_two.getCurrentTextColor() == getResources().getColor(R.color.colorsDarkSky) || txt_two.getCurrentTextColor() == getResources().getColor(R.color.colorRedLightNew)) {
                                                        if (txt_three.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_three.getCurrentTextColor() == getResources().getColor(R.color.coloryellow) || txt_three.getCurrentTextColor() == getResources().getColor(R.color.colorsolidGreen) || txt_three.getCurrentTextColor() == getResources().getColor(R.color.colorsDarkSky) || txt_three.getCurrentTextColor() == getResources().getColor(R.color.colorRedLightNew)) {
                                                            if (txt_four.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_four.getCurrentTextColor() == getResources().getColor(R.color.coloryellow) || txt_four.getCurrentTextColor() == getResources().getColor(R.color.colorsolidGreen) || txt_four.getCurrentTextColor() == getResources().getColor(R.color.colorsDarkSky) || txt_four.getCurrentTextColor() == getResources().getColor(R.color.colorRedLightNew)) {
                                                                if (txt_five.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_five.getCurrentTextColor() == getResources().getColor(R.color.coloryellow) || txt_five.getCurrentTextColor() == getResources().getColor(R.color.colorsolidGreen) || txt_five.getCurrentTextColor() == getResources().getColor(R.color.colorsDarkSky) || txt_five.getCurrentTextColor() == getResources().getColor(R.color.colorRedLightNew)) {
                                                                    if (txt_six.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_six.getCurrentTextColor() == getResources().getColor(R.color.coloryellow) || txt_six.getCurrentTextColor() == getResources().getColor(R.color.colorsolidGreen) || txt_six.getCurrentTextColor() == getResources().getColor(R.color.colorsDarkSky) || txt_six.getCurrentTextColor() == getResources().getColor(R.color.colorRedLightNew)) {
                                                                        if (txt_seven.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_seven.getCurrentTextColor() == getResources().getColor(R.color.coloryellow) || txt_seven.getCurrentTextColor() == getResources().getColor(R.color.colorsolidGreen) || txt_seven.getCurrentTextColor() == getResources().getColor(R.color.colorsDarkSky) || txt_seven.getCurrentTextColor() == getResources().getColor(R.color.colorRedLightNew)) {
                                                                            if (txt_eight.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_eight.getCurrentTextColor() == getResources().getColor(R.color.coloryellow) || txt_eight.getCurrentTextColor() == getResources().getColor(R.color.colorsolidGreen) || txt_eight.getCurrentTextColor() == getResources().getColor(R.color.colorsDarkSky) || txt_eight.getCurrentTextColor() == getResources().getColor(R.color.colorRedLightNew)) {
                                                                                if (txt_nine.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_nine.getCurrentTextColor() == getResources().getColor(R.color.coloryellow) || txt_nine.getCurrentTextColor() == getResources().getColor(R.color.colorsolidGreen) || txt_nine.getCurrentTextColor() == getResources().getColor(R.color.colorsDarkSky) || txt_nine.getCurrentTextColor() == getResources().getColor(R.color.colorRedLightNew)) {


                                                                                    addsAndAddCoins();


                                                                                    layout_dice_movement.setEnabled(false);

                                                                                    count = 0;

                                                                                    if (txt_one_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                                                                                        count++;
                                                                                    }
                                                                                    if (txt_two_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                                                                                        count++;
                                                                                    }
                                                                                    if (txt_three_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                                                                                        count++;
                                                                                    }
                                                                                    if (txt_four_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                                                                                        count++;
                                                                                    }
                                                                                    if (txt_five_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                                                                                        count++;
                                                                                    }
                                                                                    if (txt_six_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                                                                                        count++;
                                                                                    }
                                                                                    if (txt_seven_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                                                                                        count++;
                                                                                    }
                                                                                    if (txt_eight_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                                                                                        count++;
                                                                                    }
                                                                                    if (txt_nine_bottem.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                                                                                        count++;
                                                                                    }


                                                                                    if (count <= 1) {

                                                                                        showDialog("WINNER!");

                                                                                    } else {


                                                                                        showDialogBucky();

                                                                                    }


                                                                                    run = true;


                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }

                                        }
                                    }


                                }


                            } catch (JSONException e) {
                                Log.i("ExceptionIshere", e.toString());
                                e.printStackTrace();
                            }


                        }
                    });
                }
            });

        } catch (URISyntaxException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }

    }


    public String[] GetStringArray(ArrayList<String> arr) {

        // declaration and initialise String Array
        String str[] = new String[arr.size()];

        // ArrayList to Array Conversion
        for (int j = 0; j < arr.size(); j++) {

            // Assign each value to String array

            Log.i("ArrayListGetData", arr.get(j));

            str[j] = arr.get(j);

        }

        return str;
    }

    private void showDialogBucky() {

        final Dialog dialog = new Dialog(PlayVsPlayerSocket.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_bucky);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ImageView close_img = dialog.findViewById(R.id.close_img);

        close_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                showDialog("WINNER!");
            }
        });

        dialog.show();

    }

    //Re-Roll>>>>>>>>>>>>>>>>

    private void methodReRoll() {




        ApiInterface apiInterface = AppConfig.getRetrofit().create(ApiInterface.class);

        token=SharedHelper.getKey(getApplicationContext(), UserConstant.userToken);


        Call<ModelReUseJoker> call = apiInterface.re_use_joker("Bearer " + token,SharedHelper.getKey(getApplicationContext(), UserConstant.id));


        call.enqueue(new Callback<ModelReUseJoker>() {
            @Override
            public void onResponse(Call<ModelReUseJoker> call, Response<ModelReUseJoker> response) {


                if (response.isSuccessful()) {

                    if (response.body().getStatus()) {

                        SharedHelper.putKey(PlayVsPlayerSocket.this,UserConstant.zokar,response.body().getData().get(0).getTotal_zoker());

                        zokar_txt.setText(SharedHelper.getKey(PlayVsPlayerSocket.this, UserConstant.zokar));

                        //Show rolling image
                        dice_picture.setImageResource(R.drawable.dice3d160);
                        dice2_picture.setImageResource(R.drawable.dice3d160);

                        //Start rolling sound
                        soundplay = dice_sound.play(sound_id, 1.0f, 1.0f, 0, 0, 1.0f);

                        //Pause to allow image to update
                        dice_picture.startAnimation(rotate);
                        dice2_picture.startAnimation(rotate);
                        try {
                            timer.schedule(new PlayVsPlayerSocket.RollTwo(), 1100);
                        } catch (Exception e) {

                        }

                    }

                }


            }

            @Override
            public void onFailure(Call<ModelReUseJoker> call, Throwable t) {
                Log.i("fail_login", t.toString());

                CustomToast.displayError(getApplicationContext(), t.getMessage());
                if (t instanceof SocketTimeoutException) {

                }
            }
        });
    }

    class RollTwo extends TimerTask {
        public void run() {
            handlertwo.sendEmptyMessage(0);
        }
    }


    Handler.Callback callbacktwo = new Handler.Callback() {

        public boolean handleMessage(Message msg) {



            switch (rng.nextInt(6) + 1) {
                case 1:
                    rotate.cancel();
                    dice1_number = 1;
                    // dice_no.setText("Dice Number : 1");
                    dice_picture.setImageResource(R.drawable.one);
                    break;
                case 2:
                    rotate.cancel();
                    dice1_number = 2;
                    //  dice_no.setText("Dice Number : 2");
                    dice_picture.setImageResource(R.drawable.two);
                    break;
                case 3:
                    rotate.cancel();
                    dice1_number = 3;
                    // dice_no.setText("Dice Number : 3");
                    dice_picture.setImageResource(R.drawable.three);
                    break;
                case 4:
                    rotate.cancel();
                    dice1_number = 4;
                    // dice_no.setText("Dice Number : 4");
                    dice_picture.setImageResource(R.drawable.four);
                    break;
                case 5:
                    rotate.cancel();
                    dice1_number = 5;
                    // dice_no.setText("Dice Number : 5");
                    dice_picture.setImageResource(R.drawable.five);
                    break;
                case 6:
                    rotate.cancel();
                    dice1_number = 6;
                    // dice_no.setText("Dice Number : 6");
                    dice_picture.setImageResource(R.drawable.six);
                    break;
                default:
            }
            switch (rng.nextInt(6) + 1) {
                case 1:
                    rotate.cancel();
                    dice2_number = 1;
                    //  dice_no.setText("Dice Number : 1");
                    dice2_picture.setImageResource(R.drawable.one);
                    break;
                case 2:
                    rotate.cancel();
                    dice2_number = 2;
                    // dice_no.setText("Dice Number : 2");
                    dice2_picture.setImageResource(R.drawable.two);
                    break;

                case 3:
                    rotate.cancel();
                    dice2_number = 3;
                    // dice_no.setText("Dice Number : 3");
                    dice2_picture.setImageResource(R.drawable.three);
                    break;

                case 4:
                    rotate.cancel();
                    dice2_number = 4;
                    //  dice_no.setText("Dice Number : 4");
                    dice2_picture.setImageResource(R.drawable.four);
                    break;

                case 5:
                    rotate.cancel();
                    dice2_number = 5;
                    // dice_no.setText("Dice Number : 5");
                    dice2_picture.setImageResource(R.drawable.five);
                    break;
                case 6:
                    rotate.cancel();
                    dice2_number = 6;
                    // dice_no.setText("Dice Number : 6");
                    dice2_picture.setImageResource(R.drawable.six);
                    break;
                default:
            }

            rolling = false;  //user can press again


            i = dice1_number + dice2_number;


            if (i == 2) {

                Log.i("arrayList", arrayList_two.toString());

                arrayListFilterData = new ArrayList<>();

                for (int s = 0; s < arrayList_two.size(); s++) {

                    Log.i("ArrayListData", arrayList_two.get(s));


                    if (arrayList_two.get(s).equals("2")) {

                        if (txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_two.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_two.get(s));
                        }

                    }

                }


                new Handler().postDelayed(new Runnable() {


                    @Override

                    public void run() {


                        if (arrayListFilterData.size() > 0) {

                            String[] str = GetStringArray(arrayListFilterData);
                            layout_dice_movement.setEnabled(false);
                            for(int i=0;i<arrayListFilterData.size();i++){
                                if(arrayListFilterData.get(i).equals("2")){
                                    shape_two_you.setImageResource(resIdTwo[0]);
                                }
                            }

                            builder.setItems(str, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                    Toast.makeText(getApplicationContext(), str[which], Toast.LENGTH_LONG).show();
                                    Log.i("SelectedPair", str[which]);

                                    pair = str[which];

                                    if(check_trun_for_code.trim().equals("Your")){
                                        colorCode="yellow";
                                    }
                                    else {
                                        colorCode="red";
                                    }

                                    JSONObject jsonObject = new JSONObject();

                                    try {
                                        jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                        jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                        jsonObject.put("turn_id", 11);
                                        jsonObject.put("number", i);
                                        jsonObject.put("dice1_number", dice1_number);
                                        jsonObject.put("dice2_number", dice2_number);
                                        jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                        jsonObject.put("pair", pair);
                                        jsonObject.put("pos_color", colorCode);
                                        jsonObject.put("count_for_no_combination", 1);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Log.i("jsonDataIs", jsonObject.toString());

                                    socket.emit("playEvent", jsonObject.toString());

                                }
                            });

                            AlertDialog dialog = builder.create();
                            //dialog.show();
                            //dialog.getWindow().setGravity(Gravity.BOTTOM);
                        } else {

                                JSONObject jsonObject = new JSONObject();

                                try {
                                    jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                    jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                    jsonObject.put("turn_id", 11);
                                    jsonObject.put("number", i);
                                    jsonObject.put("dice1_number", dice1_number);
                                    jsonObject.put("dice2_number", dice2_number);
                                    jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                    jsonObject.put("pair", "");
                                    jsonObject.put("pos_color", "");
                                    jsonObject.put("count_for_no_combination", 1);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Log.i("jsonDataIs", jsonObject.toString());

                                socket.emit("playEvent", jsonObject.toString());

                                Toast.makeText(getApplicationContext(), "No Combination Possible", Toast.LENGTH_LONG).show();

                        }


                    }

                }, 1000);


            }
            else if (i == 3) {
                Log.i("arrayList", arrayList_three.toString());

                arrayListFilterData = new ArrayList<>();

                for (int s = 0; s < arrayList_three.size(); s++) {
                    Log.i("ArrayListData", arrayList_three.get(s));

                    if (arrayList_three.get(s).equals("3")) {

                        if (txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_two.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_three.get(s));
                        }

                    } else if (arrayList_three.get(s).equals("2,1")) {
                        if (txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_one.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            // arrayList_two.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_three.get(s));
                        }
                    }


                }


                new Handler().postDelayed(new Runnable() {


                    @Override

                    public void run() {


                        if (arrayListFilterData.size() > 0) {
                            String[] str = GetStringArray(arrayListFilterData);
                            layout_dice_movement.setEnabled(false);
                            for(int i=0;i<arrayListFilterData.size();i++){
                                if(arrayListFilterData.get(i).equals("3")){
                                    shape_three_you.setImageResource(resIdThree[0]);
                                }
                                if(arrayListFilterData.get(i).equals("2,1")){
                                    shape_two_you.setImageResource(resIdTwo[1]);
                                    shape_one_you.setImageResource(resIdOne[1]);
                                }
                            }

                            builder.setItems(str, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(), str[which], Toast.LENGTH_LONG).show();
                                    Log.i("SelectedPair", str[which]);
                                    pair = str[which];


                                    if(check_trun_for_code.trim().equals("Your")){
                                        colorCode="yellow";
                                    }
                                    else {
                                        colorCode="red";
                                    }


                                    JSONObject jsonObject = new JSONObject();

                                    try {
                                        jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                        jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                        jsonObject.put("turn_id", 11);
                                        jsonObject.put("number", i);
                                        jsonObject.put("dice1_number", dice1_number);
                                        jsonObject.put("dice2_number", dice2_number);
                                        jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                        jsonObject.put("pair", pair);
                                        jsonObject.put("pos_color", colorCode);
                                        jsonObject.put("count_for_no_combination", 1);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Log.i("jsonDataIs", jsonObject.toString());

                                    socket.emit("playEvent", jsonObject.toString());
                                }
                            });

                            AlertDialog dialog = builder.create();
                            //dialog.show();
                            //dialog.getWindow().setGravity(Gravity.BOTTOM);
                        } else {

                                JSONObject jsonObject = new JSONObject();

                                try {
                                    jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                    jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                    jsonObject.put("turn_id", 11);
                                    jsonObject.put("number", i);
                                    jsonObject.put("dice1_number", dice1_number);
                                    jsonObject.put("dice2_number", dice2_number);
                                    jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                    jsonObject.put("pair", "");
                                    jsonObject.put("pos_color", "");
                                    jsonObject.put("count_for_no_combination", 1);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Log.i("jsonDataIs", jsonObject.toString());

                                socket.emit("playEvent", jsonObject.toString());

                                Toast.makeText(getApplicationContext(), "No Combination Possible", Toast.LENGTH_LONG).show();

                        }

                    }

                }, 1000);


            }
            else if (i == 4) {
                Log.i("arrayList", arrayList_four.toString());

                arrayListFilterData = new ArrayList<>();

                for (int s = 0; s < arrayList_four.size(); s++) {
                    Log.i("ArrayListData", arrayList_four.get(s));

                    if (arrayList_four.get(s).equals("4")) {

                        if (txt_four.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            // arrayList_four.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_four.get(s));
                        }

                    } else if (arrayList_four.get(s).equals("3,1")) {
                        if (txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_one.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            // arrayList_four.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_four.get(s));
                        }
                    }

                }


                new Handler().postDelayed(new Runnable() {


                    @Override

                    public void run() {


                        if (arrayListFilterData.size() > 0) {

                            String[] str = GetStringArray(arrayListFilterData);
                            layout_dice_movement.setEnabled(false);
                            for(int i=0;i<arrayListFilterData.size();i++){
                                if(arrayListFilterData.get(i).equals("4")){
                                    shape_four_you.setImageResource(resIdFour[0]);
                                }
                                if(arrayListFilterData.get(i).equals("3,1")){
                                    shape_three_you.setImageResource(resIdThree[1]);
                                    shape_one_you.setImageResource(resIdOne[1]);
                                }
                            }

                            builder.setItems(str, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(), str[which], Toast.LENGTH_LONG).show();
                                    Log.i("SelectedPair", str[which]);
                                    pair = str[which];


                                    if(check_trun_for_code.trim().equals("Your")){
                                        colorCode="yellow";
                                    }
                                    else {
                                        colorCode="red";
                                    }


                                    JSONObject jsonObject = new JSONObject();

                                    try {
                                        jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                        jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                        jsonObject.put("turn_id", 11);
                                        jsonObject.put("number", i);
                                        jsonObject.put("dice1_number", dice1_number);
                                        jsonObject.put("dice2_number", dice2_number);
                                        jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                        jsonObject.put("pair", pair);
                                        jsonObject.put("pos_color", colorCode);
                                        jsonObject.put("count_for_no_combination", 1);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Log.i("jsonDataIs", jsonObject.toString());

                                    socket.emit("playEvent", jsonObject.toString());
                                }
                            });

                            AlertDialog dialog = builder.create();
                            //dialog.show();
                            //dialog.getWindow().setGravity(Gravity.BOTTOM);
                        }
                        else {







                                JSONObject jsonObject = new JSONObject();

                                try {
                                    jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                    jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                    jsonObject.put("turn_id", 11);
                                    jsonObject.put("number", i);
                                    jsonObject.put("dice1_number", dice1_number);
                                    jsonObject.put("dice2_number", dice2_number);
                                    jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                    jsonObject.put("pair", "");
                                    jsonObject.put("pos_color", "");
                                    jsonObject.put("count_for_no_combination", 1);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Log.i("jsonDataIs", jsonObject.toString());

                                socket.emit("playEvent", jsonObject.toString());

                                Toast.makeText(getApplicationContext(), "No Combination Possible", Toast.LENGTH_LONG).show();


                        }


                    }

                }, 1000);


            }
            else if (i == 5) {
                Log.i("arrayList", arrayList_five.toString());

                arrayListFilterData = new ArrayList<>();

                for (int s = 0; s < arrayList_five.size(); s++) {
                    Log.i("ArrayListData", arrayList_five.get(s));

                    if (arrayList_five.get(s).equals("5")) {

                        if (txt_five.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            // arrayList_five.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_five.get(s));
                        }

                    } else if (arrayList_five.get(s).equals("4,1")) {
                        if (txt_four.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_one.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_five.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_five.get(s));
                        }
                    } else if (arrayList_five.get(s).equals("3,2")) {
                        if (txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            // arrayList_five.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_five.get(s));
                        }
                    }


                }


                new Handler().postDelayed(new Runnable() {


                    @Override

                    public void run() {


                        if (arrayListFilterData.size() > 0) {
                            String[] str = GetStringArray(arrayListFilterData);
                            layout_dice_movement.setEnabled(false);
                            for(int i=0;i<arrayListFilterData.size();i++){
                                if(arrayListFilterData.get(i).equals("5")){
                                    shape_five_you.setImageResource(resIdFive[0]);
                                }
                                if(arrayListFilterData.get(i).equals("4,1")){
                                    shape_four_you.setImageResource(resIdFour[1]);
                                    shape_one_you.setImageResource(resIdOne[1]);
                                }
                                if(arrayListFilterData.get(i).equals("3,2")){
                                    shape_three_you.setImageResource(resIdThree[2]);
                                    shape_two_you.setImageResource(resIdTwo[2]);
                                }
                            }

                            builder.setItems(str, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(), str[which], Toast.LENGTH_LONG).show();
                                    Log.i("SelectedPair", str[which]);
                                    pair = str[which];


                                    if(check_trun_for_code.trim().equals("Your")){
                                        colorCode="yellow";
                                    }
                                    else {
                                        colorCode="red";
                                    }


                                    JSONObject jsonObject = new JSONObject();

                                    try {
                                        jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                        jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                        jsonObject.put("turn_id", 11);
                                        jsonObject.put("number", i);
                                        jsonObject.put("dice1_number", dice1_number);
                                        jsonObject.put("dice2_number", dice2_number);
                                        jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                        jsonObject.put("pair", pair);
                                        jsonObject.put("pos_color", colorCode);
                                        jsonObject.put("count_for_no_combination", 1);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Log.i("jsonDataIs", jsonObject.toString());

                                    socket.emit("playEvent", jsonObject.toString());

                                }
                            });

                            AlertDialog dialog = builder.create();
                            //dialog.show();
                            //dialog.getWindow().setGravity(Gravity.BOTTOM);
                        } else {






                                JSONObject jsonObject = new JSONObject();

                                try {
                                    jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                    jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                    jsonObject.put("turn_id", 11);
                                    jsonObject.put("number", i);
                                    jsonObject.put("dice1_number", dice1_number);
                                    jsonObject.put("dice2_number", dice2_number);
                                    jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                    jsonObject.put("pair", "");
                                    jsonObject.put("pos_color", "");
                                    jsonObject.put("count_for_no_combination", 1);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Log.i("jsonDataIs", jsonObject.toString());

                                socket.emit("playEvent", jsonObject.toString());

                                Toast.makeText(getApplicationContext(), "No Combination Possible", Toast.LENGTH_LONG).show();




                        }


                    }

                }, 1000);


            }
            else if (i == 6) {
                Log.i("arrayList", arrayList_six.toString());

                arrayListFilterData = new ArrayList<>();
                for (int s = 0; s < arrayList_six.size(); s++) {
                    Log.i("ArrayListData", arrayList_six.get(s));

                    if (arrayList_six.get(s).equals("6")) {

                        if (txt_six.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_six.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_six.get(s));
                        }

                    } else if (arrayList_six.get(s).equals("5,1")) {
                        if (txt_five.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_one.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_six.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_six.get(s));
                        }
                    } else if (arrayList_six.get(s).equals("4,2")) {
                        if (txt_four.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            // arrayList_six.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_six.get(s));
                        }
                    }

                }
                new Handler().postDelayed(new Runnable() {


                    @Override

                    public void run() {


                        if (arrayListFilterData.size() > 0) {

                            String[] str = GetStringArray(arrayListFilterData);
                            layout_dice_movement.setEnabled(false);
                            for(int i=0;i<arrayListFilterData.size();i++){
                                if(arrayListFilterData.get(i).equals("6")){
                                    shape_six_you.setImageResource(resIdSix[0]);
                                }
                                if(arrayListFilterData.get(i).equals("5,1")){
                                    shape_five_you.setImageResource(resIdFive[1]);
                                    shape_one_you.setImageResource(resIdOne[1]);
                                }
                                if(arrayListFilterData.get(i).equals("4,2")){
                                    shape_four_you.setImageResource(resIdFour[2]);
                                    shape_two_you.setImageResource(resIdTwo[2]);
                                }
                            }

                            builder.setItems(str, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(), str[which], Toast.LENGTH_LONG).show();
                                    Log.i("SelectedPair", str[which]);
                                    pair = str[which];

                                    if(check_trun_for_code.trim().equals("Your")){
                                        colorCode="yellow";
                                    }
                                    else {
                                        colorCode="red";
                                    }

                                    JSONObject jsonObject = new JSONObject();

                                    try {
                                        jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                        jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                        jsonObject.put("turn_id", 11);
                                        jsonObject.put("number", i);
                                        jsonObject.put("dice1_number", dice1_number);
                                        jsonObject.put("dice2_number", dice2_number);
                                        jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                        jsonObject.put("pair", pair);
                                        jsonObject.put("pos_color", colorCode);
                                        jsonObject.put("count_for_no_combination", 1);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Log.i("jsonDataIs", jsonObject.toString());

                                    socket.emit("playEvent", jsonObject.toString());

                                }
                            });

                            AlertDialog dialog = builder.create();
                            //dialog.show();
                           // dialog.getWindow().setGravity(Gravity.BOTTOM);
                        } else {






                                JSONObject jsonObject = new JSONObject();

                                try {
                                    jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                    jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                    jsonObject.put("turn_id", 11);
                                    jsonObject.put("number", i);
                                    jsonObject.put("dice1_number", dice1_number);
                                    jsonObject.put("dice2_number", dice2_number);
                                    jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                    jsonObject.put("pair", "");
                                    jsonObject.put("pos_color", "");
                                    jsonObject.put("count_for_no_combination", 1);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Log.i("jsonDataIs", jsonObject.toString());

                                socket.emit("playEvent", jsonObject.toString());

                                Toast.makeText(getApplicationContext(), "No Combination Possible", Toast.LENGTH_LONG).show();

                        }


                    }

                }, 1000);


            }
            else if (i == 7) {
                Log.i("arrayList", arrayList_seven.toString());

                arrayListFilterData = new ArrayList<>();

                for (int s = 0; s < arrayList_seven.size(); s++) {
                    Log.i("ArrayListData", arrayList_seven.get(s));

                    if (arrayList_seven.get(s).equals("7")) {

                        if (txt_seven.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_seven.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_seven.get(s));
                        }

                    } else if (arrayList_seven.get(s).equals("6,1")) {
                        if (txt_six.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_one.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_seven.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_seven.get(s));
                        }
                    } else if (arrayList_seven.get(s).equals("5,2")) {
                        if (txt_five.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_seven.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_seven.get(s));
                        }
                    } else if (arrayList_seven.get(s).equals("4,3")) {
                        if (txt_four.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            // arrayList_seven.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_seven.get(s));
                        }
                    }


                }


                new Handler().postDelayed(new Runnable() {


                    @Override

                    public void run() {


                        if (arrayListFilterData.size() > 0) {

                            String[] str = GetStringArray(arrayListFilterData);
                            layout_dice_movement.setEnabled(false);
                            for(int i=0;i<arrayListFilterData.size();i++){

                                if(arrayListFilterData.get(i).equals("7")){
                                    shape_seven_you.setImageResource(resIdSeven[0]);
                                }
                                if(arrayListFilterData.get(i).equals("6,1")){
                                    shape_six_you.setImageResource(resIdSix[1]);
                                    shape_one_you.setImageResource(resIdOne[1]);
                                }
                                if(arrayListFilterData.get(i).equals("5,2")){
                                    shape_five_you.setImageResource(resIdFive[2]);
                                    shape_two_you.setImageResource(resIdTwo[2]);
                                }
                                if(arrayListFilterData.get(i).equals("4,3")){
                                    shape_four_you.setImageResource(resIdFour[3]);
                                    shape_three_you.setImageResource(resIdThree[3]);
                                }
                            }


                            builder.setItems(str, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(), str[which], Toast.LENGTH_LONG).show();
                                    Log.i("SelectedPair", str[which]);
                                    pair = str[which];

                                    if(check_trun_for_code.trim().equals("Your")){
                                        colorCode="yellow";
                                    }
                                    else {
                                        colorCode="red";
                                    }


                                    JSONObject jsonObject = new JSONObject();

                                    try {
                                        jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                        jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                        jsonObject.put("turn_id", 11);
                                        jsonObject.put("number", i);
                                        jsonObject.put("dice1_number", dice1_number);
                                        jsonObject.put("dice2_number", dice2_number);
                                        jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                        jsonObject.put("pair", pair);
                                        jsonObject.put("pos_color", colorCode);
                                        jsonObject.put("count_for_no_combination", 1);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Log.i("jsonDataIs", jsonObject.toString());

                                    socket.emit("playEvent", jsonObject.toString());

                                }
                            });

                            AlertDialog dialog = builder.create();
                            //dialog.show();
                            //dialog.getWindow().setGravity(Gravity.BOTTOM);
                        } else {






                                JSONObject jsonObject = new JSONObject();

                                try {
                                    jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                    jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                    jsonObject.put("turn_id", 11);
                                    jsonObject.put("number", i);
                                    jsonObject.put("dice1_number", dice1_number);
                                    jsonObject.put("dice2_number", dice2_number);
                                    jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                    jsonObject.put("pair", "");
                                    jsonObject.put("pos_color", "");
                                    jsonObject.put("count_for_no_combination", 1);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Log.i("jsonDataIs", jsonObject.toString());

                                socket.emit("playEvent", jsonObject.toString());

                                Toast.makeText(getApplicationContext(), "No Combination Possible", Toast.LENGTH_LONG).show();



                        }


                    }

                }, 1000);


            }
            else if (i == 8) {
                Log.i("arrayList", arrayList_eight.toString());

                arrayListFilterData = new ArrayList<>();
                for (int s = 0; s < arrayList_eight.size(); s++) {
                    Log.i("ArrayListData", arrayList_eight.get(s));

                    if (arrayList_eight.get(s).equals("8")) {

                        if (txt_eight.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_eight.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_eight.get(s));
                        }

                    } else if (arrayList_eight.get(s).equals("7,1")) {
                        if (txt_seven.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_one.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            // arrayList_eight.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_eight.get(s));
                        }
                    } else if (arrayList_eight.get(s).equals("6,2")) {
                        if (txt_six.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            // arrayList_eight.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_eight.get(s));
                        }
                    } else if (arrayList_eight.get(s).equals("5,3")) {
                        if (txt_five.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_eight.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_eight.get(s));
                        }
                    }

                }


                new Handler().postDelayed(new Runnable() {


                    @Override

                    public void run() {


                        if (arrayListFilterData.size() > 0) {

                            String[] str = GetStringArray(arrayListFilterData);
                            layout_dice_movement.setEnabled(false);
                            for(int i=0;i<arrayListFilterData.size();i++){
                                if(arrayListFilterData.get(i).equals("8")){
                                    shape_eight_you.setImageResource(resIdEight[0]);
                                }
                                if(arrayListFilterData.get(i).equals("7,1")){
                                    shape_seven_you.setImageResource(resIdSeven[1]);
                                    shape_one_you.setImageResource(resIdOne[1]);
                                }
                                if(arrayListFilterData.get(i).equals("6,2")){
                                    shape_six_you.setImageResource(resIdSix[2]);
                                    shape_two_you.setImageResource(resIdTwo[2]);
                                }
                                if(arrayListFilterData.get(i).equals("5,3")){
                                    shape_five_you.setImageResource(resIdFive[3]);
                                    shape_three_you.setImageResource(resIdThree[3]);
                                }
                            }


                            builder.setItems(str, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(), str[which], Toast.LENGTH_LONG).show();
                                    Log.i("SelectedPair", str[which]);
                                    pair = str[which];



                                    if(check_trun_for_code.trim().equals("Your")){
                                        colorCode="yellow";
                                    }
                                    else {
                                        colorCode="red";
                                    }


                                    JSONObject jsonObject = new JSONObject();

                                    try {
                                        jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                        jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                        jsonObject.put("turn_id", 11);
                                        jsonObject.put("number", i);
                                        jsonObject.put("dice1_number", dice1_number);
                                        jsonObject.put("dice2_number", dice2_number);
                                        jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                        jsonObject.put("pair", pair);
                                        jsonObject.put("pos_color", colorCode);
                                        jsonObject.put("count_for_no_combination", 1);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Log.i("jsonDataIs", jsonObject.toString());

                                    socket.emit("playEvent", jsonObject.toString());

                                }
                            });

                            AlertDialog dialog = builder.create();
                            //dialog.show();
                            //dialog.getWindow().setGravity(Gravity.BOTTOM);
                        } else {







                                JSONObject jsonObject = new JSONObject();

                                try {
                                    jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                    jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                    jsonObject.put("turn_id", 11);
                                    jsonObject.put("number", i);
                                    jsonObject.put("dice1_number", dice1_number);
                                    jsonObject.put("dice2_number", dice2_number);
                                    jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                    jsonObject.put("pair", "");
                                    jsonObject.put("pos_color", "");
                                    jsonObject.put("count_for_no_combination", 1);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Log.i("jsonDataIs", jsonObject.toString());

                                socket.emit("playEvent", jsonObject.toString());

                                Toast.makeText(getApplicationContext(), "No Combination Possible", Toast.LENGTH_LONG).show();


                        }


                    }

                }, 1000);


            }
            else if (i == 9) {
                Log.i("arrayList", arrayList_nine.toString());

                arrayListFilterData = new ArrayList<>();

                for (int s = 0; s < arrayList_nine.size(); s++) {
                    Log.i("ArrayListData", arrayList_nine.get(s));

                    if (arrayList_nine.get(s).equals("9")) {

                        if (txt_nine.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_nine.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_nine.get(s));
                        }

                    } else if (arrayList_nine.get(s).equals("8,1")) {
                        if (txt_eight.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_one.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_nine.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_nine.get(s));
                        }
                    } else if (arrayList_nine.get(s).equals("7,2")) {
                        if (txt_seven.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            // arrayList_nine.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_nine.get(s));
                        }
                    } else if (arrayList_nine.get(s).equals("6,3")) {
                        if (txt_six.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_nine.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_nine.get(s));
                        }
                    } else if (arrayList_nine.get(s).equals("5,4")) {
                        if (txt_five.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_four.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_nine.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_nine.get(s));
                        }
                    }


                }

                new Handler().postDelayed(new Runnable() {


                    @Override

                    public void run() {


                        if (arrayListFilterData.size() > 0) {

                            String[] str = GetStringArray(arrayListFilterData);
                            layout_dice_movement.setEnabled(false);
                            for(int i=0;i<arrayListFilterData.size();i++){
                                if(arrayListFilterData.get(i).equals("9")){
                                    shape_nine_you.setImageResource(resIdNine[0]);
                                }
                                if(arrayListFilterData.get(i).equals("8,1")){
                                    shape_eight_you.setImageResource(resIdEight[1]);
                                    shape_one_you.setImageResource(resIdOne[1]);
                                }
                                if(arrayListFilterData.get(i).equals("7,2")){
                                    shape_seven_you.setImageResource(resIdSeven[2]);
                                    shape_two_you.setImageResource(resIdTwo[2]);
                                }
                                if(arrayListFilterData.get(i).equals("6,3")){
                                    shape_six_you.setImageResource(resIdSix[3]);
                                    shape_three_you.setImageResource(resIdThree[3]);
                                }
                                if(arrayListFilterData.get(i).equals("5,4")){
                                    shape_five_you.setImageResource(resIdFive[4]);
                                    shape_four_you.setImageResource(resIdFour[4]);
                                }
                            }

                            builder.setItems(str, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(), str[which], Toast.LENGTH_LONG).show();
                                    Log.i("SelectedPair", str[which]);
                                    pair = str[which];





                                    if(check_trun_for_code.trim().equals("Your")){
                                        colorCode="yellow";
                                    }
                                    else {
                                        colorCode="red";
                                    }

                                    JSONObject jsonObject = new JSONObject();

                                    try {
                                        jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                        jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                        jsonObject.put("turn_id", 11);
                                        jsonObject.put("number", i);
                                        jsonObject.put("dice1_number", dice1_number);
                                        jsonObject.put("dice2_number", dice2_number);
                                        jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                        jsonObject.put("pair", pair);
                                        jsonObject.put("pos_color", colorCode);
                                        jsonObject.put("count_for_no_combination", 1);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Log.i("jsonDataIs", jsonObject.toString());

                                    socket.emit("playEvent", jsonObject.toString());

                                }
                            });

                            AlertDialog dialog = builder.create();
                            //dialog.show();
                            //dialog.getWindow().setGravity(Gravity.BOTTOM);
                        } else {





                                JSONObject jsonObject = new JSONObject();

                                try {
                                    jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                    jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                    jsonObject.put("turn_id", 11);
                                    jsonObject.put("number", i);
                                    jsonObject.put("dice1_number", dice1_number);
                                    jsonObject.put("dice2_number", dice2_number);
                                    jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                    jsonObject.put("pair", "");
                                    jsonObject.put("pos_color", "");
                                    jsonObject.put("count_for_no_combination", 1);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Log.i("jsonDataIs", jsonObject.toString());

                                socket.emit("playEvent", jsonObject.toString());

                                Toast.makeText(getApplicationContext(), "No Combination Possible", Toast.LENGTH_LONG).show();

                        }


                    }

                }, 1000);


            }
            else if (i == 10) {
                Log.i("arrayList", arrayList_ten.toString());

                arrayListFilterData = new ArrayList<>();

                for (int s = 0; s < arrayList_ten.size(); s++) {
                    Log.i("ArrayListData", arrayList_ten.get(s));

                    if (arrayList_ten.get(s).equals("9,1")) {
                        if (txt_nine.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_one.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            // arrayList_ten.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_ten.get(s));
                        }
                    } else if (arrayList_ten.get(s).equals("8,2")) {
                        if (txt_eight.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_ten.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_ten.get(s));
                        }
                    } else if (arrayList_ten.get(s).equals("7,3")) {
                        if (txt_seven.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            // arrayList_ten.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_ten.get(s));
                        }
                    } else if (arrayList_ten.get(s).equals("6,4")) {
                        if (txt_six.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_four.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_ten.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_ten.get(s));
                        }
                    }


                }


                new Handler().postDelayed(new Runnable() {


                    @Override

                    public void run() {


                        if (arrayListFilterData.size() > 0) {

                            String[] str = GetStringArray(arrayListFilterData);
                            layout_dice_movement.setEnabled(false);
                            for(int i=0;i<arrayListFilterData.size();i++){

                                if(arrayListFilterData.get(i).equals("9,1")){
                                    shape_nine_you.setImageResource(resIdNine[0]);
                                    shape_one_you.setImageResource(resIdOne[0]);
                                }
                                if(arrayListFilterData.get(i).equals("8,2")){
                                    shape_eight_you.setImageResource(resIdEight[1]);
                                    shape_two_you.setImageResource(resIdTwo[1]);
                                }
                                if(arrayListFilterData.get(i).equals("7,3")){
                                    shape_seven_you.setImageResource(resIdSeven[2]);
                                    shape_three_you.setImageResource(resIdThree[2]);
                                }
                                if(arrayListFilterData.get(i).equals("6,4")){
                                    shape_six_you.setImageResource(resIdSix[3]);
                                    shape_four_you.setImageResource(resIdFour[3]);
                                }
                            }

                            builder.setItems(str, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(), str[which], Toast.LENGTH_LONG).show();
                                    Log.i("SelectedPair", str[which]);
                                    pair = str[which];




                                    if(check_trun_for_code.trim().equals("Your")){
                                        colorCode="yellow";
                                    }
                                    else {
                                        colorCode="red";
                                    }


                                    JSONObject jsonObject = new JSONObject();

                                    try {
                                        jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                        jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                        jsonObject.put("turn_id", 11);
                                        jsonObject.put("number", i);
                                        jsonObject.put("dice1_number", dice1_number);
                                        jsonObject.put("dice2_number", dice2_number);
                                        jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                        jsonObject.put("pair", pair);
                                        jsonObject.put("pos_color", colorCode);
                                        jsonObject.put("count_for_no_combination", 1);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Log.i("jsonDataIs", jsonObject.toString());

                                    socket.emit("playEvent", jsonObject.toString());

                                }
                            });

                            AlertDialog dialog = builder.create();
                           // dialog.show();
                           // dialog.getWindow().setGravity(Gravity.BOTTOM);
                        } else {






                                JSONObject jsonObject = new JSONObject();

                                try {
                                    jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                    jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                    jsonObject.put("turn_id", 11);
                                    jsonObject.put("number", i);
                                    jsonObject.put("dice1_number", dice1_number);
                                    jsonObject.put("dice2_number", dice2_number);
                                    jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                    jsonObject.put("pair", "");
                                    jsonObject.put("pos_color", "");
                                    jsonObject.put("count_for_no_combination", 1);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Log.i("jsonDataIs", jsonObject.toString());

                                socket.emit("playEvent", jsonObject.toString());


                                Toast.makeText(getApplicationContext(), "No Combination Possible", Toast.LENGTH_LONG).show();

                        }


                    }

                }, 1000);


            }
            else if (i == 11) {
                Log.i("arrayList", arrayList_eveven.toString());

                arrayListFilterData = new ArrayList<>();

                for (int s = 0; s < arrayList_eveven.size(); s++) {
                    Log.i("ArrayListData", arrayList_eveven.get(s));

                    if (arrayList_eveven.get(s).equals("9,2")) {
                        if (txt_nine.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_eveven.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_eveven.get(s));
                        }
                    } else if (arrayList_eveven.get(s).equals("8,3")) {
                        if (txt_eight.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            // arrayList_eveven.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_eveven.get(s));
                        }
                    } else if (arrayList_eveven.get(s).equals("7,4")) {
                        if (txt_seven.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_four.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            // arrayList_eveven.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_eveven.get(s));
                        }
                    } else if (arrayList_eveven.get(s).equals("6,5")) {
                        if (txt_six.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_five.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_eveven.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_eveven.get(s));
                        }
                    }


                }


                new Handler().postDelayed(new Runnable() {


                    @Override

                    public void run() {


                        if (arrayListFilterData.size() > 0) {

                            String[] str = GetStringArray(arrayListFilterData);
                            layout_dice_movement.setEnabled(false);
                            for(int i=0;i<arrayListFilterData.size();i++){

                                if(arrayListFilterData.get(i).equals("9,2")){
                                    shape_nine_you.setImageResource(resIdNine[0]);
                                    shape_two_you.setImageResource(resIdTwo[0]);
                                }
                                if(arrayListFilterData.get(i).equals("8,3")){
                                    shape_eight_you.setImageResource(resIdEight[1]);
                                    shape_three_you.setImageResource(resIdThree[1]);
                                }
                                if(arrayListFilterData.get(i).equals("7,4")){
                                    shape_seven_you.setImageResource(resIdSeven[2]);
                                    shape_four_you.setImageResource(resIdFour[2]);
                                }
                                if(arrayListFilterData.get(i).equals("6,5")){
                                    shape_six_you.setImageResource(resIdSix[3]);
                                    shape_five_you.setImageResource(resIdFive[3]);
                                }
                            }

                            builder.setItems(str, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(), str[which], Toast.LENGTH_LONG).show();
                                    Log.i("SelectedPair", str[which]);
                                    pair = str[which];





                                    if(check_trun_for_code.trim().equals("Your")){
                                        colorCode="yellow";
                                    }
                                    else {
                                        colorCode="red";
                                    }


                                    JSONObject jsonObject = new JSONObject();

                                    try {
                                        jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                        jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                        jsonObject.put("turn_id", 11);
                                        jsonObject.put("number", i);
                                        jsonObject.put("dice1_number", dice1_number);
                                        jsonObject.put("dice2_number", dice2_number);
                                        jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                        jsonObject.put("pair", pair);
                                        jsonObject.put("pos_color", colorCode);
                                        jsonObject.put("count_for_no_combination", 1);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Log.i("jsonDataIs", jsonObject.toString());

                                    socket.emit("playEvent", jsonObject.toString());

                                }
                            });

                            AlertDialog dialog = builder.create();
                            //dialog.show();
                            //dialog.getWindow().setGravity(Gravity.BOTTOM);
                        } else {





                                JSONObject jsonObject = new JSONObject();

                                try {
                                    jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                    jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                    jsonObject.put("turn_id", 11);
                                    jsonObject.put("number", i);
                                    jsonObject.put("dice1_number", dice1_number);
                                    jsonObject.put("dice2_number", dice2_number);
                                    jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                    jsonObject.put("pair", "");
                                    jsonObject.put("pos_color", "");
                                    jsonObject.put("count_for_no_combination", 1);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Log.i("jsonDataIs", jsonObject.toString());

                                socket.emit("playEvent", jsonObject.toString());


                                Toast.makeText(getApplicationContext(), "No Combination Possible", Toast.LENGTH_LONG).show();

                        }


                    }

                }, 1000);


            }
            else if (i == 12) {
                Log.i("arrayList", arrayList_twelve.toString());

                arrayListFilterData = new ArrayList<>();


                for (int s = 0; s < arrayList_twelve.size(); s++) {
                    Log.i("ArrayListData", arrayList_twelve.get(s));

                    if (arrayList_twelve.get(s).equals("9,3")) {
                        if (txt_nine.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_twelve.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_twelve.get(s));
                        }
                    } else if (arrayList_twelve.get(s).equals("8,4")) {
                        if (txt_eight.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_four.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_twelve.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_twelve.get(s));
                        }
                    } else if (arrayList_twelve.get(s).equals("7,5")) {
                        if (txt_seven.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_five.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //  arrayList_twelve.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_twelve.get(s));
                        }
                    }


                }


                new Handler().postDelayed(new Runnable() {


                    @Override

                    public void run() {


                        if (arrayListFilterData.size() > 0) {

                            String[] str = GetStringArray(arrayListFilterData);
                            layout_dice_movement.setEnabled(false);

                            for(int i=0;i<arrayListFilterData.size();i++){

                                if(arrayListFilterData.get(i).equals("9,3")){
                                    shape_nine_you.setImageResource(resIdNine[0]);
                                    shape_three_you.setImageResource(resIdThree[0]);
                                }
                                if(arrayListFilterData.get(i).equals("8,4")){
                                    shape_eight_you.setImageResource(resIdEight[1]);
                                    shape_four_you.setImageResource(resIdFour[1]);
                                }
                                if(arrayListFilterData.get(i).equals("7,5")){
                                    shape_seven_you.setImageResource(resIdSeven[2]);
                                    shape_five_you.setImageResource(resIdFive[2]);
                                }

                            }

                            builder.setItems(str, new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(), str[which], Toast.LENGTH_LONG).show();
                                    Log.i("SelectedPair", str[which]);
                                    pair = str[which];




                                    if(check_trun_for_code.trim().equals("Your")){
                                        colorCode="yellow";
                                    }
                                    else {
                                        colorCode="red";
                                    }


                                    JSONObject jsonObject = new JSONObject();

                                    try {
                                        jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                        jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                        jsonObject.put("turn_id", 11);
                                        jsonObject.put("number", i);
                                        jsonObject.put("dice1_number", dice1_number);
                                        jsonObject.put("dice2_number", dice2_number);
                                        jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                        jsonObject.put("pair", pair);
                                        jsonObject.put("pos_color", colorCode);
                                        jsonObject.put("count_for_no_combination", 1);
                                    }
                                    catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Log.i("jsonDataIs", jsonObject.toString());

                                    socket.emit("playEvent", jsonObject.toString());

                                }
                            });

                            AlertDialog dialog = builder.create();
                            //dialog.show();
                            //dialog.getWindow().setGravity(Gravity.BOTTOM);
                        }

                        else {

                            JSONObject jsonObject = new JSONObject();

                                try {
                                    jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                    jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                    jsonObject.put("turn_id", 11);
                                    jsonObject.put("number", i);
                                    jsonObject.put("dice1_number", dice1_number);
                                    jsonObject.put("dice2_number", dice2_number);
                                    jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                    jsonObject.put("pair", "");
                                    jsonObject.put("pos_color", "");
                                    jsonObject.put("count_for_no_combination", 1);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Log.i("jsonDataIs", jsonObject.toString());

                                socket.emit("playEvent", jsonObject.toString());

                                Toast.makeText(getApplicationContext(), "No Combination Possible", Toast.LENGTH_LONG).show();

                        }


                    }

                }, 1000);


            }


            return true;


        }

    };

    private void methodVisibleGoneWhenOpponentTurn() {
        layout_top_you.setVisibility(View.GONE);
        layout_top_opponent.setVisibility(View.VISIBLE);
        layout_opponent.setVisibility(View.GONE);
        layout_your.setVisibility(View.VISIBLE);
        tv_name_two.setText("You");
        iv_profile_bottem_you.setVisibility(View.VISIBLE);
        iv_profile_bottem_opponent.setVisibility(View.GONE);

        layout_opponet_big_board.setVisibility(View.VISIBLE);
        layout_one_opponent_turn.setVisibility(View.VISIBLE);
        layout_your_big_board.setVisibility(View.GONE);
        layout_bottem_opponent.setVisibility(View.GONE);


    }

    private void methodVisibleGoneWhenMyTurn() {
        layout_top_you.setVisibility(View.VISIBLE);
        layout_top_opponent.setVisibility(View.GONE);
        layout_opponent.setVisibility(View.VISIBLE);
        layout_your.setVisibility(View.GONE);
        StringBuilder sb = new StringBuilder(name_two);
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        tv_name_two.setText(sb.toString());
        iv_profile_bottem_you.setVisibility(View.GONE);
        iv_profile_bottem_opponent.setVisibility(View.VISIBLE);

        layout_opponet_big_board.setVisibility(View.GONE);
        layout_one_opponent_turn.setVisibility(View.GONE);
        layout_your_big_board.setVisibility(View.VISIBLE);
        layout_bottem_opponent.setVisibility(View.VISIBLE);


    }

    // For No Combination matched New Conditions

    class RollThree extends TimerTask {
        public void run() {
            handlerthree.sendEmptyMessage(0);
        }
    }


    Handler.Callback callbackthree = new Handler.Callback() {

        public boolean handleMessage(Message msg) {


            //Get roll result
            //Remember nextInt returns 0 to 5 for argument of 6
            //hence + 1

            switch (rng.nextInt(6) + 1) {
                case 1:
                    rotate.cancel();
                    dice1_number = 1;
                    // dice_no.setText("Dice Number : 1");
                    dice_picture.setImageResource(R.drawable.one);
                    break;
                case 2:
                    rotate.cancel();
                    dice1_number = 2;
                    //  dice_no.setText("Dice Number : 2");
                    dice_picture.setImageResource(R.drawable.two);
                    break;
                case 3:
                    rotate.cancel();
                    dice1_number = 3;
                    // dice_no.setText("Dice Number : 3");
                    dice_picture.setImageResource(R.drawable.three);
                    break;
                case 4:
                    rotate.cancel();
                    dice1_number = 4;
                    // dice_no.setText("Dice Number : 4");
                    dice_picture.setImageResource(R.drawable.four);
                    break;
                case 5:
                    rotate.cancel();
                    dice1_number = 5;
                    // dice_no.setText("Dice Number : 5");
                    dice_picture.setImageResource(R.drawable.five);
                    break;
                case 6:
                    rotate.cancel();
                    dice1_number = 6;
                    // dice_no.setText("Dice Number : 6");
                    dice_picture.setImageResource(R.drawable.six);
                    break;
                default:
            }
            switch (rng.nextInt(6) + 1) {
                case 1:
                    rotate.cancel();
                    dice2_number = 1;
                    //  dice_no.setText("Dice Number : 1");
                    dice2_picture.setImageResource(R.drawable.one);
                    break;
                case 2:
                    rotate.cancel();
                    dice2_number = 2;
                    // dice_no.setText("Dice Number : 2");
                    dice2_picture.setImageResource(R.drawable.two);
                    break;

                case 3:
                    rotate.cancel();
                    dice2_number = 3;
                    // dice_no.setText("Dice Number : 3");
                    dice2_picture.setImageResource(R.drawable.three);
                    break;

                case 4:
                    rotate.cancel();
                    dice2_number = 4;
                    //  dice_no.setText("Dice Number : 4");
                    dice2_picture.setImageResource(R.drawable.four);
                    break;

                case 5:
                    rotate.cancel();
                    dice2_number = 5;
                    // dice_no.setText("Dice Number : 5");
                    dice2_picture.setImageResource(R.drawable.five);
                    break;
                case 6:
                    rotate.cancel();
                    dice2_number = 6;
                    // dice_no.setText("Dice Number : 6");
                    dice2_picture.setImageResource(R.drawable.six);
                    break;
                default:
            }

            rolling = false;  //user can press again

             i = dice1_number + dice2_number;

             if (i == 2) {

                Log.i("arrayList", arrayList_two.toString());

                arrayListFilterData = new ArrayList<>();

                for (int s = 0; s < arrayList_two.size(); s++) {

                    Log.i("ArrayListData", arrayList_two.get(s));

                    if (arrayList_two.get(s).equals("2")) {

                        if (txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_two.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_two.get(s));
                        }

                    }

                }


                new Handler().postDelayed(new Runnable() {

                    @Override

                    public void run() {

                        if (arrayListFilterData.size() > 0) {

                            String[] str = GetStringArray(arrayListFilterData);
                            layout_dice_movement.setEnabled(false);
                            for(int i=0;i<arrayListFilterData.size();i++){
                                if(arrayListFilterData.get(i).equals("2")){
                                    shape_two_you.setImageResource(resIdTwo[0]);
                                }
                            }

                            builder.setItems(str, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                    Toast.makeText(getApplicationContext(), str[which], Toast.LENGTH_LONG).show();
                                    Log.i("SelectedPair", str[which]);

                                    pair = str[which];



                                    if(check_trun_for_code.trim().equals("Your")){
                                        colorCode="yellow";
                                    }
                                    else {
                                        colorCode="red";
                                    }


                                    JSONObject jsonObject = new JSONObject();

                                    try {
                                        jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                        jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                        jsonObject.put("turn_id", 11);
                                        jsonObject.put("number", i);
                                        jsonObject.put("dice1_number", dice1_number);
                                        jsonObject.put("dice2_number", dice2_number);
                                        jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                        jsonObject.put("pair", pair);
                                        jsonObject.put("pos_color", colorCode);
                                        jsonObject.put("count_for_no_combination", 2);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Log.i("jsonDataIs", jsonObject.toString());

                                    socket.emit("playEvent", jsonObject.toString());

                                }
                            });

                            AlertDialog dialog = builder.create();
                            //dialog.show();
                            //dialog.getWindow().setGravity(Gravity.BOTTOM);
                        }
                        else {


                            Toast.makeText(getApplicationContext(), "No Combination Possible", Toast.LENGTH_LONG).show();


                            if(zokar_txt.getText().toString().trim().equals("0")){

                                methodNOJoker();

                                JSONObject jsonObject = new JSONObject();

                                try {
                                    jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                    jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                    jsonObject.put("turn_id", 11);
                                    jsonObject.put("number", i);
                                    jsonObject.put("dice1_number", dice1_number);
                                    jsonObject.put("dice2_number", dice2_number);
                                    jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                    jsonObject.put("pair", "");
                                    jsonObject.put("pos_color", "");
                                    jsonObject.put("count_for_no_combination", 2);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Log.i("jsonDataIs", jsonObject.toString());

                                socket.emit("playEvent", jsonObject.toString());

                            }

                            else {



                                final Dialog dialog = new Dialog(PlayVsPlayerSocket.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(false);
                                dialog.setContentView(R.layout.dialog_re_roll);


                                Button no = dialog.findViewById(R.id.no);
                                Button yes = dialog.findViewById(R.id.yes);


                                no.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {


                                        dialog.dismiss();

                                        JSONObject jsonObject = new JSONObject();

                                        try {
                                            jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                            jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                            jsonObject.put("turn_id", 11);
                                            jsonObject.put("number", i);
                                            jsonObject.put("dice1_number", dice1_number);
                                            jsonObject.put("dice2_number", dice2_number);
                                            jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                            jsonObject.put("pair", "");
                                            jsonObject.put("pos_color", "");
                                            jsonObject.put("count_for_no_combination", 2);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        Log.i("jsonDataIs", jsonObject.toString());

                                        socket.emit("playEvent", jsonObject.toString());


                                    }
                                });


                                yes.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                        //total_coin_txt.setText(SharedHelper.getKey(PlayVsPlayerSocket.this, UserConstant.coin));



                                        methodReRollNew();






                                    }
                                });

                                dialog.show();

                            }





                        }


                    }

                }, 1000);


            }

            else if (i == 3) {
                Log.i("arrayList", arrayList_three.toString());

                arrayListFilterData = new ArrayList<>();

                for (int s = 0; s < arrayList_three.size(); s++) {
                    Log.i("ArrayListData", arrayList_three.get(s));

                    if (arrayList_three.get(s).equals("3")) {

                        if (txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_two.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_three.get(s));
                        }

                    } else if (arrayList_three.get(s).equals("2,1")) {
                        if (txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_one.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            // arrayList_two.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_three.get(s));
                        }
                    }


                }


                new Handler().postDelayed(new Runnable() {


                    @Override

                    public void run() {


                        if (arrayListFilterData.size() > 0) {

                            String[] str = GetStringArray(arrayListFilterData);
                            layout_dice_movement.setEnabled(false);
                            for(int i=0;i<arrayListFilterData.size();i++){
                                if(arrayListFilterData.get(i).equals("3")){
                                    shape_three_you.setImageResource(resIdThree[0]);
                                }
                                if(arrayListFilterData.get(i).equals("2,1")){
                                    shape_two_you.setImageResource(resIdTwo[1]);
                                    shape_one_you.setImageResource(resIdOne[1]);
                                }
                            }

                            builder.setItems(str, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(), str[which], Toast.LENGTH_LONG).show();
                                    Log.i("SelectedPair", str[which]);
                                    pair = str[which];


                                    if(check_trun_for_code.trim().equals("Your")){
                                        colorCode="yellow";
                                    }
                                    else {
                                        colorCode="red";
                                    }


                                    JSONObject jsonObject = new JSONObject();

                                    try {
                                        jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                        jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                        jsonObject.put("turn_id", 11);
                                        jsonObject.put("number", i);
                                        jsonObject.put("dice1_number", dice1_number);
                                        jsonObject.put("dice2_number", dice2_number);
                                        jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                        jsonObject.put("pair", pair);
                                        jsonObject.put("pos_color", colorCode);
                                        jsonObject.put("count_for_no_combination", 2);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Log.i("jsonDataIs", jsonObject.toString());

                                    socket.emit("playEvent", jsonObject.toString());
                                }
                            });

                            AlertDialog dialog = builder.create();
                           // dialog.show();
                           // dialog.getWindow().setGravity(Gravity.BOTTOM);
                        }

                        else {




                            Toast.makeText(getApplicationContext(), "No Combination Possible", Toast.LENGTH_LONG).show();


                            if(zokar_txt.getText().toString().trim().equals("0")){

                                methodNOJoker();

                                JSONObject jsonObject = new JSONObject();

                                try {
                                    jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                    jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                    jsonObject.put("turn_id", 11);
                                    jsonObject.put("number", i);
                                    jsonObject.put("dice1_number", dice1_number);
                                    jsonObject.put("dice2_number", dice2_number);
                                    jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                    jsonObject.put("pair", "");
                                    jsonObject.put("pos_color", "");
                                    jsonObject.put("count_for_no_combination", 2);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Log.i("jsonDataIs", jsonObject.toString());

                                socket.emit("playEvent", jsonObject.toString());

                            }



                            else {
                                final Dialog dialog = new Dialog(PlayVsPlayerSocket.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(false);
                                dialog.setContentView(R.layout.dialog_re_roll);


                                Button no = dialog.findViewById(R.id.no);
                                Button yes = dialog.findViewById(R.id.yes);


                                no.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        dialog.dismiss();

                                        JSONObject jsonObject = new JSONObject();

                                        try {
                                            jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                            jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                            jsonObject.put("turn_id", 11);
                                            jsonObject.put("number", i);
                                            jsonObject.put("dice1_number", dice1_number);
                                            jsonObject.put("dice2_number", dice2_number);
                                            jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                            jsonObject.put("pair", "");
                                            jsonObject.put("pos_color", "");
                                            jsonObject.put("count_for_no_combination", 2);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        Log.i("jsonDataIs", jsonObject.toString());

                                        socket.emit("playEvent", jsonObject.toString());


                                    }
                                });


                                yes.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                        methodReRollNew();
                                    }
                                });

                                dialog.show();
                            }




                        }


                    }

                }, 1000);


            }

            else if (i == 4) {

                Log.i("arrayList", arrayList_four.toString());

                arrayListFilterData = new ArrayList<>();

                for (int s = 0; s < arrayList_four.size(); s++) {
                    Log.i("ArrayListData", arrayList_four.get(s));

                    if (arrayList_four.get(s).equals("4")) {

                        if (txt_four.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            // arrayList_four.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_four.get(s));
                        }

                    } else if (arrayList_four.get(s).equals("3,1")) {
                        if (txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_one.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            // arrayList_four.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_four.get(s));
                        }
                    }

                }


                new Handler().postDelayed(new Runnable() {


                    @Override

                    public void run() {


                        if (arrayListFilterData.size() > 0) {

                            String[] str = GetStringArray(arrayListFilterData);
                            layout_dice_movement.setEnabled(false);
                            for(int i=0;i<arrayListFilterData.size();i++){
                                if(arrayListFilterData.get(i).equals("4")){
                                    shape_four_you.setImageResource(resIdFour[0]);
                                }
                                if(arrayListFilterData.get(i).equals("3,1")){
                                    shape_three_you.setImageResource(resIdThree[1]);
                                    shape_one_you.setImageResource(resIdOne[1]);
                                }
                            }

                            builder.setItems(str, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(), str[which], Toast.LENGTH_LONG).show();
                                    Log.i("SelectedPair", str[which]);
                                    pair = str[which];


                                    if(check_trun_for_code.trim().equals("Your")){
                                        colorCode="yellow";
                                    }
                                    else {
                                        colorCode="red";
                                    }

                                    JSONObject jsonObject = new JSONObject();

                                    try {
                                        jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                        jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                        jsonObject.put("turn_id", 11);
                                        jsonObject.put("number", i);
                                        jsonObject.put("dice1_number", dice1_number);
                                        jsonObject.put("dice2_number", dice2_number);
                                        jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                        jsonObject.put("pair", pair);
                                        jsonObject.put("pos_color", colorCode);
                                        jsonObject.put("count_for_no_combination",2);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Log.i("jsonDataIs", jsonObject.toString());

                                    socket.emit("playEvent", jsonObject.toString());
                                }
                            });

                            AlertDialog dialog = builder.create();
                           // dialog.show();
                            //dialog.getWindow().setGravity(Gravity.BOTTOM);
                        }
                        else {

                            Toast.makeText(getApplicationContext(), "No Combination Possible", Toast.LENGTH_LONG).show();


                            if(zokar_txt.getText().toString().trim().equals("0")){

                                methodNOJoker();

                                JSONObject jsonObject = new JSONObject();

                                try {
                                    jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                    jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                    jsonObject.put("turn_id", 11);
                                    jsonObject.put("number", i);
                                    jsonObject.put("dice1_number", dice1_number);
                                    jsonObject.put("dice2_number", dice2_number);
                                    jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                    jsonObject.put("pair", "");
                                    jsonObject.put("pos_color", "");
                                    jsonObject.put("count_for_no_combination", 2);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Log.i("jsonDataIs", jsonObject.toString());

                                socket.emit("playEvent", jsonObject.toString());

                            }

                            else {
                                final Dialog dialog = new Dialog(PlayVsPlayerSocket.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(false);
                                dialog.setContentView(R.layout.dialog_re_roll);


                                Button no = dialog.findViewById(R.id.no);
                                Button yes = dialog.findViewById(R.id.yes);


                                no.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        dialog.dismiss();

                                        JSONObject jsonObject = new JSONObject();

                                        try {
                                            jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                            jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                            jsonObject.put("turn_id", 11);
                                            jsonObject.put("number", i);
                                            jsonObject.put("dice1_number", dice1_number);
                                            jsonObject.put("dice2_number", dice2_number);
                                            jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                            jsonObject.put("pair", "");
                                            jsonObject.put("pos_color", "");
                                            jsonObject.put("count_for_no_combination", 2);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        Log.i("jsonDataIs", jsonObject.toString());

                                        socket.emit("playEvent", jsonObject.toString());


                                    }
                                });


                                yes.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                        methodReRollNew();
                                    }
                                });


                                dialog.show();
                            }






                        }


                    }

                }, 1000);


            }

            else if (i == 5) {

                Log.i("arrayList", arrayList_five.toString());

                arrayListFilterData = new ArrayList<>();

                for (int s = 0; s < arrayList_five.size(); s++) {
                    Log.i("ArrayListData", arrayList_five.get(s));

                    if (arrayList_five.get(s).equals("5")) {

                        if (txt_five.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            // arrayList_five.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_five.get(s));
                        }

                    } else if (arrayList_five.get(s).equals("4,1")) {
                        if (txt_four.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_one.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_five.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_five.get(s));
                        }
                    } else if (arrayList_five.get(s).equals("3,2")) {
                        if (txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            // arrayList_five.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_five.get(s));
                        }
                    }


                }


                new Handler().postDelayed(new Runnable() {


                    @Override

                    public void run() {


                        if (arrayListFilterData.size() > 0) {

                            String[] str = GetStringArray(arrayListFilterData);
                            layout_dice_movement.setEnabled(false);
                            for(int i=0;i<arrayListFilterData.size();i++){
                                if(arrayListFilterData.get(i).equals("5")){
                                    shape_five_you.setImageResource(resIdFive[0]);
                                }
                                if(arrayListFilterData.get(i).equals("4,1")){
                                    shape_four_you.setImageResource(resIdFour[1]);
                                    shape_one_you.setImageResource(resIdOne[1]);
                                }
                                if(arrayListFilterData.get(i).equals("3,2")){
                                    shape_three_you.setImageResource(resIdThree[2]);
                                    shape_two_you.setImageResource(resIdTwo[2]);
                                }
                            }

                            builder.setItems(str, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(), str[which], Toast.LENGTH_LONG).show();
                                    Log.i("SelectedPair", str[which]);
                                    pair = str[which];

                                    if(check_trun_for_code.trim().equals("Your")){
                                        colorCode="yellow";
                                    }
                                    else {
                                        colorCode="red";
                                    }

                                    JSONObject jsonObject = new JSONObject();

                                    try {
                                        jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                        jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                        jsonObject.put("turn_id", 11);
                                        jsonObject.put("number", i);
                                        jsonObject.put("dice1_number", dice1_number);
                                        jsonObject.put("dice2_number", dice2_number);
                                        jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                        jsonObject.put("pair", pair);
                                        jsonObject.put("pos_color", colorCode);
                                        jsonObject.put("count_for_no_combination", 2);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Log.i("jsonDataIs", jsonObject.toString());

                                    socket.emit("playEvent", jsonObject.toString());

                                }
                            });

                            AlertDialog dialog = builder.create();
                           // dialog.show();
                           // dialog.getWindow().setGravity(Gravity.BOTTOM);
                        }
                        else {

                            Toast.makeText(getApplicationContext(), "No Combination Possible", Toast.LENGTH_LONG).show();




                            if(zokar_txt.getText().toString().trim().equals("0")){

                                methodNOJoker();

                                JSONObject jsonObject = new JSONObject();

                                try {
                                    jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                    jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                    jsonObject.put("turn_id", 11);
                                    jsonObject.put("number", i);
                                    jsonObject.put("dice1_number", dice1_number);
                                    jsonObject.put("dice2_number", dice2_number);
                                    jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                    jsonObject.put("pair", "");
                                    jsonObject.put("pos_color", "");
                                    jsonObject.put("count_for_no_combination", 2);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Log.i("jsonDataIs", jsonObject.toString());

                                socket.emit("playEvent", jsonObject.toString());

                            }

                            else {
                                final Dialog dialog = new Dialog(PlayVsPlayerSocket.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(false);
                                dialog.setContentView(R.layout.dialog_re_roll);


                                Button no = dialog.findViewById(R.id.no);
                                Button yes = dialog.findViewById(R.id.yes);


                                no.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        dialog.dismiss();

                                        JSONObject jsonObject = new JSONObject();

                                        try {
                                            jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                            jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                            jsonObject.put("turn_id", 11);
                                            jsonObject.put("number", i);
                                            jsonObject.put("dice1_number", dice1_number);
                                            jsonObject.put("dice2_number", dice2_number);
                                            jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                            jsonObject.put("pair", "");
                                            jsonObject.put("pos_color", "");
                                            jsonObject.put("count_for_no_combination", 2);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        Log.i("jsonDataIs", jsonObject.toString());

                                        socket.emit("playEvent", jsonObject.toString());


                                    }
                                });


                                yes.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                        methodReRollNew();
                                    }
                                });


                                dialog.show();
                            }





                        }


                    }

                }, 1000);

            }


            else if (i == 6) {

                Log.i("arrayList", arrayList_six.toString());

                arrayListFilterData = new ArrayList<>();
                for (int s = 0; s < arrayList_six.size(); s++) {
                    Log.i("ArrayListData", arrayList_six.get(s));

                    if (arrayList_six.get(s).equals("6")) {

                        if (txt_six.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_six.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_six.get(s));
                        }

                    } else if (arrayList_six.get(s).equals("5,1")) {
                        if (txt_five.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_one.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_six.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_six.get(s));
                        }
                    } else if (arrayList_six.get(s).equals("4,2")) {
                        if (txt_four.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            // arrayList_six.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_six.get(s));
                        }
                    }

                }
                new Handler().postDelayed(new Runnable() {


                    @Override

                    public void run() {


                        if (arrayListFilterData.size() > 0) {

                            String[] str = GetStringArray(arrayListFilterData);
                            layout_dice_movement.setEnabled(false);
                            for(int i=0;i<arrayListFilterData.size();i++){
                                if(arrayListFilterData.get(i).equals("6")){
                                    shape_six_you.setImageResource(resIdSix[0]);
                                }
                                if(arrayListFilterData.get(i).equals("5,1")){
                                    shape_five_you.setImageResource(resIdFive[1]);
                                    shape_one_you.setImageResource(resIdOne[1]);
                                }
                                if(arrayListFilterData.get(i).equals("4,2")){
                                    shape_four_you.setImageResource(resIdFour[2]);
                                    shape_two_you.setImageResource(resIdTwo[2]);
                                }
                            }

                            builder.setItems(str, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(), str[which], Toast.LENGTH_LONG).show();
                                    Log.i("SelectedPair", str[which]);
                                    pair = str[which];


                                    if(check_trun_for_code.trim().equals("Your")){
                                        colorCode="yellow";
                                    }
                                    else {
                                        colorCode="red";
                                    }

                                    JSONObject jsonObject = new JSONObject();

                                    try {
                                        jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                        jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                        jsonObject.put("turn_id", 11);
                                        jsonObject.put("number", i);
                                        jsonObject.put("dice1_number", dice1_number);
                                        jsonObject.put("dice2_number", dice2_number);
                                        jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                        jsonObject.put("pair", pair);
                                        jsonObject.put("pos_color", colorCode);
                                        jsonObject.put("count_for_no_combination", 2);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Log.i("jsonDataIs", jsonObject.toString());

                                    socket.emit("playEvent", jsonObject.toString());

                                }
                            });

                            AlertDialog dialog = builder.create();
                          //  dialog.show();
                          //  dialog.getWindow().setGravity(Gravity.BOTTOM);
                        }
                        else {

                            Toast.makeText(getApplicationContext(), "No Combination Possible", Toast.LENGTH_LONG).show();


                            if(zokar_txt.getText().toString().trim().equals("0")){

                                methodNOJoker();

                                JSONObject jsonObject = new JSONObject();

                                try {
                                    jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                    jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                    jsonObject.put("turn_id", 11);
                                    jsonObject.put("number", i);
                                    jsonObject.put("dice1_number", dice1_number);
                                    jsonObject.put("dice2_number", dice2_number);
                                    jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                    jsonObject.put("pair", "");
                                    jsonObject.put("pos_color", "");
                                    jsonObject.put("count_for_no_combination", 2);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Log.i("jsonDataIs", jsonObject.toString());

                                socket.emit("playEvent", jsonObject.toString());

                            }

                            else {
                                final Dialog dialog = new Dialog(PlayVsPlayerSocket.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(false);
                                dialog.setContentView(R.layout.dialog_re_roll);


                                Button no = dialog.findViewById(R.id.no);
                                Button yes = dialog.findViewById(R.id.yes);


                                no.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        dialog.dismiss();

                                        JSONObject jsonObject = new JSONObject();

                                        try {
                                            jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                            jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                            jsonObject.put("turn_id", 11);
                                            jsonObject.put("number", i);
                                            jsonObject.put("dice1_number", dice1_number);
                                            jsonObject.put("dice2_number", dice2_number);
                                            jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                            jsonObject.put("pair", "");
                                            jsonObject.put("pos_color", "");
                                            jsonObject.put("count_for_no_combination", 2);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        Log.i("jsonDataIs", jsonObject.toString());

                                        socket.emit("playEvent", jsonObject.toString());


                                    }
                                });


                                yes.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        dialog.dismiss();
                                        methodReRollNew();
                                    }
                                });


                                dialog.show();
                            }









                        }


                    }

                }, 1000);


            }

            else if (i == 7) {
                Log.i("arrayList", arrayList_seven.toString());

                arrayListFilterData = new ArrayList<>();

                for (int s = 0; s < arrayList_seven.size(); s++) {
                    Log.i("ArrayListData", arrayList_seven.get(s));

                    if (arrayList_seven.get(s).equals("7")) {

                        if (txt_seven.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_seven.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_seven.get(s));
                        }

                    } else if (arrayList_seven.get(s).equals("6,1")) {
                        if (txt_six.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_one.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_seven.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_seven.get(s));
                        }
                    } else if (arrayList_seven.get(s).equals("5,2")) {
                        if (txt_five.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_seven.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_seven.get(s));
                        }
                    } else if (arrayList_seven.get(s).equals("4,3")) {
                        if (txt_four.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            // arrayList_seven.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_seven.get(s));
                        }
                    }


                }


                new Handler().postDelayed(new Runnable() {


                    @Override

                    public void run() {


                        if (arrayListFilterData.size() > 0) {

                            String[] str = GetStringArray(arrayListFilterData);
                            layout_dice_movement.setEnabled(false);
                            for(int i=0;i<arrayListFilterData.size();i++){

                                if(arrayListFilterData.get(i).equals("7")){
                                    shape_seven_you.setImageResource(resIdSeven[0]);
                                }
                                if(arrayListFilterData.get(i).equals("6,1")){
                                    shape_six_you.setImageResource(resIdSix[1]);
                                    shape_one_you.setImageResource(resIdOne[1]);
                                }
                                if(arrayListFilterData.get(i).equals("5,2")){
                                    shape_five_you.setImageResource(resIdFive[2]);
                                    shape_two_you.setImageResource(resIdTwo[2]);
                                }
                                if(arrayListFilterData.get(i).equals("4,3")){
                                    shape_four_you.setImageResource(resIdFour[3]);
                                    shape_three_you.setImageResource(resIdThree[3]);
                                }
                            }

                            builder.setItems(str, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(), str[which], Toast.LENGTH_LONG).show();
                                    Log.i("SelectedPair", str[which]);
                                    pair = str[which];

                                    if(check_trun_for_code.trim().equals("Your")){
                                        colorCode="yellow";
                                    }
                                    else {
                                        colorCode="red";
                                    }


                                    JSONObject jsonObject = new JSONObject();

                                    try {
                                        jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                        jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                        jsonObject.put("turn_id", 11);
                                        jsonObject.put("number", i);
                                        jsonObject.put("dice1_number", dice1_number);
                                        jsonObject.put("dice2_number", dice2_number);
                                        jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                        jsonObject.put("pair", pair);
                                        jsonObject.put("pos_color", colorCode);
                                        jsonObject.put("count_for_no_combination", 2);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Log.i("jsonDataIs", jsonObject.toString());

                                    socket.emit("playEvent", jsonObject.toString());

                                }
                            });

                            AlertDialog dialog = builder.create();
                          //  dialog.show();
                          //  dialog.getWindow().setGravity(Gravity.BOTTOM);
                        }

                        else {

                            Toast.makeText(getApplicationContext(), "No Combination Possible", Toast.LENGTH_LONG).show();


                            if(zokar_txt.getText().toString().trim().equals("0")){

                                methodNOJoker();

                                JSONObject jsonObject = new JSONObject();

                                try {
                                    jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                    jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                    jsonObject.put("turn_id", 11);
                                    jsonObject.put("number", i);
                                    jsonObject.put("dice1_number", dice1_number);
                                    jsonObject.put("dice2_number", dice2_number);
                                    jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                    jsonObject.put("pair", "");
                                    jsonObject.put("pos_color", "");
                                    jsonObject.put("count_for_no_combination", 2);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Log.i("jsonDataIs", jsonObject.toString());

                                socket.emit("playEvent", jsonObject.toString());

                            }

                            else {
                                final Dialog dialog = new Dialog(PlayVsPlayerSocket.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(false);
                                dialog.setContentView(R.layout.dialog_re_roll);

                                Button no = dialog.findViewById(R.id.no);
                                Button yes = dialog.findViewById(R.id.yes);


                                no.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        dialog.dismiss();

                                        JSONObject jsonObject = new JSONObject();

                                        try {
                                            jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                            jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                            jsonObject.put("turn_id", 11);
                                            jsonObject.put("number", i);
                                            jsonObject.put("dice1_number", dice1_number);
                                            jsonObject.put("dice2_number", dice2_number);
                                            jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                            jsonObject.put("pair", "");
                                            jsonObject.put("pos_color", "");
                                            jsonObject.put("count_for_no_combination", 2);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        Log.i("jsonDataIs", jsonObject.toString());

                                        socket.emit("playEvent", jsonObject.toString());


                                    }
                                });


                                yes.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        dialog.dismiss();
                                        methodReRollNew();
                                    }
                                });


                                dialog.show();
                            }










                        }


                    }

                }, 1000);


            }


            else if (i == 8) {

                Log.i("arrayList", arrayList_eight.toString());

                arrayListFilterData = new ArrayList<>();
                for (int s = 0; s < arrayList_eight.size(); s++) {
                    Log.i("ArrayListData", arrayList_eight.get(s));

                    if (arrayList_eight.get(s).equals("8")) {

                        if (txt_eight.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_eight.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_eight.get(s));
                        }

                    } else if (arrayList_eight.get(s).equals("7,1")) {
                        if (txt_seven.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_one.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            // arrayList_eight.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_eight.get(s));
                        }
                    } else if (arrayList_eight.get(s).equals("6,2")) {
                        if (txt_six.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            // arrayList_eight.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_eight.get(s));
                        }
                    } else if (arrayList_eight.get(s).equals("5,3")) {
                        if (txt_five.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_eight.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_eight.get(s));
                        }
                    }

                }


                new Handler().postDelayed(new Runnable() {


                    @Override

                    public void run() {


                        if (arrayListFilterData.size() > 0) {

                            String[] str = GetStringArray(arrayListFilterData);
                            layout_dice_movement.setEnabled(false);
                            for(int i=0;i<arrayListFilterData.size();i++){
                                if(arrayListFilterData.get(i).equals("8")){
                                    shape_eight_you.setImageResource(resIdEight[0]);
                                }
                                if(arrayListFilterData.get(i).equals("7,1")){
                                    shape_seven_you.setImageResource(resIdSeven[1]);
                                    shape_one_you.setImageResource(resIdOne[1]);
                                }
                                if(arrayListFilterData.get(i).equals("6,2")){
                                    shape_six_you.setImageResource(resIdSix[2]);
                                    shape_two_you.setImageResource(resIdTwo[2]);
                                }
                                if(arrayListFilterData.get(i).equals("5,3")){
                                    shape_five_you.setImageResource(resIdFive[3]);
                                    shape_three_you.setImageResource(resIdThree[3]);
                                }
                            }

                            builder.setItems(str, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(), str[which], Toast.LENGTH_LONG).show();
                                    Log.i("SelectedPair", str[which]);
                                    pair = str[which];


                                    if(check_trun_for_code.trim().equals("Your")){
                                        colorCode="yellow";
                                    }
                                    else {
                                        colorCode="red";
                                    }


                                    JSONObject jsonObject = new JSONObject();

                                    try {
                                        jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                        jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                        jsonObject.put("turn_id", 11);
                                        jsonObject.put("number", i);
                                        jsonObject.put("dice1_number", dice1_number);
                                        jsonObject.put("dice2_number", dice2_number);
                                        jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                        jsonObject.put("pair", pair);
                                        jsonObject.put("pos_color", colorCode);
                                        jsonObject.put("count_for_no_combination", 2);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Log.i("jsonDataIs", jsonObject.toString());

                                    socket.emit("playEvent", jsonObject.toString());

                                }
                            });

                            AlertDialog dialog = builder.create();
                          //  dialog.show();
                           // dialog.getWindow().setGravity(Gravity.BOTTOM);
                        }

                        else {



                            Toast.makeText(getApplicationContext(), "No Combination Possible", Toast.LENGTH_LONG).show();


                            if(zokar_txt.getText().toString().trim().equals("0")){

                                methodNOJoker();

                                JSONObject jsonObject = new JSONObject();

                                try {
                                    jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                    jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                    jsonObject.put("turn_id", 11);
                                    jsonObject.put("number", i);
                                    jsonObject.put("dice1_number", dice1_number);
                                    jsonObject.put("dice2_number", dice2_number);
                                    jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                    jsonObject.put("pair", "");
                                    jsonObject.put("pos_color", "");
                                    jsonObject.put("count_for_no_combination", 2);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Log.i("jsonDataIs", jsonObject.toString());

                                socket.emit("playEvent", jsonObject.toString());

                            }
                            else {
                                final Dialog dialog = new Dialog(PlayVsPlayerSocket.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(false);
                                dialog.setContentView(R.layout.dialog_re_roll);


                                Button no = dialog.findViewById(R.id.no);
                                Button yes = dialog.findViewById(R.id.yes);


                                no.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        dialog.dismiss();

                                        JSONObject jsonObject = new JSONObject();

                                        try {
                                            jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                            jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                            jsonObject.put("turn_id", 11);
                                            jsonObject.put("number", i);
                                            jsonObject.put("dice1_number", dice1_number);
                                            jsonObject.put("dice2_number", dice2_number);
                                            jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                            jsonObject.put("pair", "");
                                            jsonObject.put("pos_color", "");
                                            jsonObject.put("count_for_no_combination", 2);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        Log.i("jsonDataIs", jsonObject.toString());

                                        socket.emit("playEvent", jsonObject.toString());


                                    }
                                });


                                yes.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        dialog.dismiss();
                                        methodReRollNew();
                                    }
                                });


                                dialog.show();
                            }









                        }


                    }

                }, 1000);


            }
            else if (i == 9) {
                Log.i("arrayList", arrayList_nine.toString());

                arrayListFilterData = new ArrayList<>();

                for (int s = 0; s < arrayList_nine.size(); s++) {
                    Log.i("ArrayListData", arrayList_nine.get(s));

                    if (arrayList_nine.get(s).equals("9")) {

                        if (txt_nine.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_nine.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_nine.get(s));
                        }

                    } else if (arrayList_nine.get(s).equals("8,1")) {
                        if (txt_eight.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_one.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_nine.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_nine.get(s));
                        }
                    } else if (arrayList_nine.get(s).equals("7,2")) {
                        if (txt_seven.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            // arrayList_nine.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_nine.get(s));
                        }
                    } else if (arrayList_nine.get(s).equals("6,3")) {
                        if (txt_six.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_nine.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_nine.get(s));
                        }
                    } else if (arrayList_nine.get(s).equals("5,4")) {
                        if (txt_five.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_four.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_nine.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_nine.get(s));
                        }
                    }


                }

                new Handler().postDelayed(new Runnable() {


                    @Override

                    public void run() {


                        if (arrayListFilterData.size() > 0) {

                            String[] str = GetStringArray(arrayListFilterData);
                            layout_dice_movement.setEnabled(false);
                            for(int i=0;i<arrayListFilterData.size();i++){
                                if(arrayListFilterData.get(i).equals("9")){
                                    shape_nine_you.setImageResource(resIdNine[0]);
                                }
                                if(arrayListFilterData.get(i).equals("8,1")){
                                    shape_eight_you.setImageResource(resIdEight[1]);
                                    shape_one_you.setImageResource(resIdOne[1]);
                                }
                                if(arrayListFilterData.get(i).equals("7,2")){
                                    shape_seven_you.setImageResource(resIdSeven[2]);
                                    shape_two_you.setImageResource(resIdTwo[2]);
                                }
                                if(arrayListFilterData.get(i).equals("6,3")){
                                    shape_six_you.setImageResource(resIdSix[3]);
                                    shape_three_you.setImageResource(resIdThree[3]);
                                }
                                if(arrayListFilterData.get(i).equals("5,4")){
                                    shape_five_you.setImageResource(resIdFive[4]);
                                    shape_four_you.setImageResource(resIdFour[4]);
                                }
                            }


                            builder.setItems(str, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(), str[which], Toast.LENGTH_LONG).show();
                                    Log.i("SelectedPair", str[which]);
                                    pair = str[which];


                                    if(check_trun_for_code.trim().equals("Your")){
                                        colorCode="yellow";
                                    }
                                    else {
                                        colorCode="red";
                                    }


                                    JSONObject jsonObject = new JSONObject();

                                    try {
                                        jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                        jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                        jsonObject.put("turn_id", 11);
                                        jsonObject.put("number", i);
                                        jsonObject.put("dice1_number", dice1_number);
                                        jsonObject.put("dice2_number", dice2_number);
                                        jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                        jsonObject.put("pair", pair);
                                        jsonObject.put("pos_color", colorCode);
                                        jsonObject.put("count_for_no_combination", 2);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Log.i("jsonDataIs", jsonObject.toString());

                                    socket.emit("playEvent", jsonObject.toString());

                                }
                            });

                            AlertDialog dialog = builder.create();
                          //  dialog.show();
                          //  dialog.getWindow().setGravity(Gravity.BOTTOM);
                        } else {




                            Toast.makeText(getApplicationContext(), "No Combination Possible", Toast.LENGTH_LONG).show();



                            if(zokar_txt.getText().toString().trim().equals("0")){

                                methodNOJoker();

                                JSONObject jsonObject = new JSONObject();

                                try {
                                    jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                    jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                    jsonObject.put("turn_id", 11);
                                    jsonObject.put("number", i);
                                    jsonObject.put("dice1_number", dice1_number);
                                    jsonObject.put("dice2_number", dice2_number);
                                    jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                    jsonObject.put("pair", "");
                                    jsonObject.put("pos_color", "");
                                    jsonObject.put("count_for_no_combination", 2);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Log.i("jsonDataIs", jsonObject.toString());

                                socket.emit("playEvent", jsonObject.toString());

                            }

                            else {
                                final Dialog dialog = new Dialog(PlayVsPlayerSocket.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(false);
                                dialog.setContentView(R.layout.dialog_re_roll);


                                Button no = dialog.findViewById(R.id.no);
                                Button yes = dialog.findViewById(R.id.yes);


                                no.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();

                                        JSONObject jsonObject = new JSONObject();

                                        try {
                                            jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                            jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                            jsonObject.put("turn_id", 11);
                                            jsonObject.put("number", i);
                                            jsonObject.put("dice1_number", dice1_number);
                                            jsonObject.put("dice2_number", dice2_number);
                                            jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                            jsonObject.put("pair", "");
                                            jsonObject.put("pos_color", "");
                                            jsonObject.put("count_for_no_combination", 2);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        Log.i("jsonDataIs", jsonObject.toString());

                                        socket.emit("playEvent", jsonObject.toString());


                                    }
                                });


                                yes.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                        methodReRollNew();

                                    }
                                });


                                dialog.show();
                            }









                        }


                    }

                }, 1000);


            }
            else if (i == 10) {
                Log.i("arrayList", arrayList_ten.toString());

                arrayListFilterData = new ArrayList<>();

                for (int s = 0; s < arrayList_ten.size(); s++) {
                    Log.i("ArrayListData", arrayList_ten.get(s));

                    if (arrayList_ten.get(s).equals("9,1")) {
                        if (txt_nine.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_one.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            // arrayList_ten.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_ten.get(s));
                        }
                    } else if (arrayList_ten.get(s).equals("8,2")) {
                        if (txt_eight.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_ten.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_ten.get(s));
                        }
                    } else if (arrayList_ten.get(s).equals("7,3")) {
                        if (txt_seven.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            // arrayList_ten.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_ten.get(s));
                        }
                    } else if (arrayList_ten.get(s).equals("6,4")) {
                        if (txt_six.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_four.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_ten.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_ten.get(s));
                        }
                    }


                }


                new Handler().postDelayed(new Runnable() {


                    @Override

                    public void run() {


                        if (arrayListFilterData.size() > 0) {

                            String[] str = GetStringArray(arrayListFilterData);
                            layout_dice_movement.setEnabled(false);
                            for(int i=0;i<arrayListFilterData.size();i++){

                                if(arrayListFilterData.get(i).equals("9,1")){
                                    shape_nine_you.setImageResource(resIdNine[0]);
                                    shape_one_you.setImageResource(resIdOne[0]);
                                }
                                if(arrayListFilterData.get(i).equals("8,2")){
                                    shape_eight_you.setImageResource(resIdEight[1]);
                                    shape_two_you.setImageResource(resIdTwo[1]);
                                }
                                if(arrayListFilterData.get(i).equals("7,3")){
                                    shape_seven_you.setImageResource(resIdSeven[2]);
                                    shape_three_you.setImageResource(resIdThree[2]);
                                }
                                if(arrayListFilterData.get(i).equals("6,4")){
                                    shape_six_you.setImageResource(resIdSix[3]);
                                    shape_four_you.setImageResource(resIdFour[3]);
                                }
                            }

                            builder.setItems(str, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(), str[which], Toast.LENGTH_LONG).show();
                                    Log.i("SelectedPair", str[which]);
                                    pair = str[which];


                                    if(check_trun_for_code.trim().equals("Your")){
                                        colorCode="yellow";
                                    }
                                    else {
                                        colorCode="red";
                                    }


                                    JSONObject jsonObject = new JSONObject();

                                    try {
                                        jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                        jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                        jsonObject.put("turn_id", 11);
                                        jsonObject.put("number", i);
                                        jsonObject.put("dice1_number", dice1_number);
                                        jsonObject.put("dice2_number", dice2_number);
                                        jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                        jsonObject.put("pair", pair);
                                        jsonObject.put("pos_color", colorCode);
                                        jsonObject.put("count_for_no_combination", 2);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Log.i("jsonDataIs", jsonObject.toString());

                                    socket.emit("playEvent", jsonObject.toString());

                                }
                            });

                            AlertDialog dialog = builder.create();
                           // dialog.show();
                           // dialog.getWindow().setGravity(Gravity.BOTTOM);
                        } else {



                            Toast.makeText(getApplicationContext(), "No Combination Possible", Toast.LENGTH_LONG).show();


                            if(zokar_txt.getText().toString().trim().equals("0")){

                                methodNOJoker();

                                JSONObject jsonObject = new JSONObject();

                                try {
                                    jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                    jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                    jsonObject.put("turn_id", 11);
                                    jsonObject.put("number", i);
                                    jsonObject.put("dice1_number", dice1_number);
                                    jsonObject.put("dice2_number", dice2_number);
                                    jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                    jsonObject.put("pair", "");
                                    jsonObject.put("pos_color", "");
                                    jsonObject.put("count_for_no_combination", 2);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Log.i("jsonDataIs", jsonObject.toString());

                                socket.emit("playEvent", jsonObject.toString());

                            }

                            else {
                                final Dialog dialog = new Dialog(PlayVsPlayerSocket.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(false);
                                dialog.setContentView(R.layout.dialog_re_roll);


                                Button no = dialog.findViewById(R.id.no);
                                Button yes = dialog.findViewById(R.id.yes);


                                no.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        dialog.dismiss();

                                        JSONObject jsonObject = new JSONObject();

                                        try {
                                            jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                            jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                            jsonObject.put("turn_id", 11);
                                            jsonObject.put("number", i);
                                            jsonObject.put("dice1_number", dice1_number);
                                            jsonObject.put("dice2_number", dice2_number);
                                            jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                            jsonObject.put("pair", "");
                                            jsonObject.put("pos_color", "");
                                            jsonObject.put("count_for_no_combination", 2);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        Log.i("jsonDataIs", jsonObject.toString());

                                        socket.emit("playEvent", jsonObject.toString());


                                    }
                                });


                                yes.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                        methodReRollNew();
                                    }
                                });


                                dialog.show();
                            }










                        }


                    }

                }, 1000);


            }
            else if (i == 11) {

                Log.i("arrayList", arrayList_eveven.toString());

                arrayListFilterData = new ArrayList<>();

                for (int s = 0; s < arrayList_eveven.size(); s++) {
                    Log.i("ArrayListData", arrayList_eveven.get(s));

                    if (arrayList_eveven.get(s).equals("9,2")) {
                        if (txt_nine.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_eveven.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_eveven.get(s));
                        }
                    } else if (arrayList_eveven.get(s).equals("8,3")) {
                        if (txt_eight.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            // arrayList_eveven.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_eveven.get(s));
                        }
                    } else if (arrayList_eveven.get(s).equals("7,4")) {
                        if (txt_seven.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_four.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            // arrayList_eveven.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_eveven.get(s));
                        }
                    } else if (arrayList_eveven.get(s).equals("6,5")) {
                        if (txt_six.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_five.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_eveven.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_eveven.get(s));
                        }
                    }


                }


                new Handler().postDelayed(new Runnable() {


                    @Override

                    public void run() {


                        if (arrayListFilterData.size() > 0) {

                            String[] str = GetStringArray(arrayListFilterData);
                            layout_dice_movement.setEnabled(false);
                            for(int i=0;i<arrayListFilterData.size();i++){

                                if(arrayListFilterData.get(i).equals("9,2")){
                                    shape_nine_you.setImageResource(resIdNine[0]);
                                    shape_two_you.setImageResource(resIdTwo[0]);
                                }
                                if(arrayListFilterData.get(i).equals("8,3")){
                                    shape_eight_you.setImageResource(resIdEight[1]);
                                    shape_three_you.setImageResource(resIdThree[1]);
                                }
                                if(arrayListFilterData.get(i).equals("7,4")){
                                    shape_seven_you.setImageResource(resIdSeven[2]);
                                    shape_four_you.setImageResource(resIdFour[2]);
                                }
                                if(arrayListFilterData.get(i).equals("6,5")){
                                    shape_six_you.setImageResource(resIdSix[3]);
                                    shape_five_you.setImageResource(resIdFive[3]);
                                }
                            }


                            builder.setItems(str, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(), str[which], Toast.LENGTH_LONG).show();
                                    Log.i("SelectedPair", str[which]);
                                    pair = str[which];

                                    if(check_trun_for_code.trim().equals("Your")){
                                        colorCode="yellow";
                                    }
                                    else {
                                        colorCode="red";
                                    }


                                    JSONObject jsonObject = new JSONObject();

                                    try {
                                        jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                        jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                        jsonObject.put("turn_id", 11);
                                        jsonObject.put("number", i);
                                        jsonObject.put("dice1_number", dice1_number);
                                        jsonObject.put("dice2_number", dice2_number);
                                        jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                        jsonObject.put("pair", pair);
                                        jsonObject.put("pos_color", colorCode);
                                        jsonObject.put("count_for_no_combination", 2);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Log.i("jsonDataIs", jsonObject.toString());

                                    socket.emit("playEvent", jsonObject.toString());

                                }
                            });

                            AlertDialog dialog = builder.create();
                           // dialog.show();
                           // dialog.getWindow().setGravity(Gravity.BOTTOM);
                        }
                        else {

                            Toast.makeText(getApplicationContext(), "No Combination Possible", Toast.LENGTH_LONG).show();



                            if(zokar_txt.getText().toString().trim().equals("0")){

                                methodNOJoker();

                                JSONObject jsonObject = new JSONObject();

                                try {
                                    jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                    jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                    jsonObject.put("turn_id", 11);
                                    jsonObject.put("number", i);
                                    jsonObject.put("dice1_number", dice1_number);
                                    jsonObject.put("dice2_number", dice2_number);
                                    jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                    jsonObject.put("pair", "");
                                    jsonObject.put("pos_color", "");
                                    jsonObject.put("count_for_no_combination", 2);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Log.i("jsonDataIs", jsonObject.toString());

                                socket.emit("playEvent", jsonObject.toString());

                            }
                            else {
                                final Dialog dialog = new Dialog(PlayVsPlayerSocket.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(false);
                                dialog.setContentView(R.layout.dialog_re_roll);


                                Button no = dialog.findViewById(R.id.no);
                                Button yes = dialog.findViewById(R.id.yes);


                                no.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        dialog.dismiss();

                                        JSONObject jsonObject = new JSONObject();

                                        try {
                                            jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                            jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                            jsonObject.put("turn_id", 11);
                                            jsonObject.put("number", i);
                                            jsonObject.put("dice1_number", dice1_number);
                                            jsonObject.put("dice2_number", dice2_number);
                                            jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                            jsonObject.put("pair", "");
                                            jsonObject.put("pos_color", "");
                                            jsonObject.put("count_for_no_combination", 2);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        Log.i("jsonDataIs", jsonObject.toString());

                                        socket.emit("playEvent", jsonObject.toString());


                                    }
                                });


                                yes.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                        methodReRollNew();
                                    }
                                });


                                dialog.show();
                            }











                        }


                    }

                }, 1000);


            }

            else if (i == 12) {

                Log.i("arrayList", arrayList_twelve.toString());

                arrayListFilterData = new ArrayList<>();


                for (int s = 0; s < arrayList_twelve.size(); s++) {
                    Log.i("ArrayListData", arrayList_twelve.get(s));

                    if (arrayList_twelve.get(s).equals("9,3")) {
                        if (txt_nine.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_twelve.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_twelve.get(s));
                        }
                    } else if (arrayList_twelve.get(s).equals("8,4")) {
                        if (txt_eight.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_four.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_twelve.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_twelve.get(s));
                        }
                    } else if (arrayList_twelve.get(s).equals("7,5")) {
                        if (txt_seven.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_five.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //  arrayList_twelve.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_twelve.get(s));
                        }
                    }


                }


                new Handler().postDelayed(new Runnable() {


                    @Override

                    public void run() {


                        if (arrayListFilterData.size() > 0) {

                            String[] str = GetStringArray(arrayListFilterData);
                            layout_dice_movement.setEnabled(false);
                            for(int i=0;i<arrayListFilterData.size();i++){

                                if(arrayListFilterData.get(i).equals("9,3")){
                                    shape_nine_you.setImageResource(resIdNine[0]);
                                    shape_three_you.setImageResource(resIdThree[0]);
                                }
                                if(arrayListFilterData.get(i).equals("8,4")){
                                    shape_eight_you.setImageResource(resIdEight[1]);
                                    shape_four_you.setImageResource(resIdFour[1]);
                                }
                                if(arrayListFilterData.get(i).equals("7,5")){
                                    shape_seven_you.setImageResource(resIdSeven[2]);
                                    shape_five_you.setImageResource(resIdFive[2]);
                                }

                            }

                            builder.setItems(str, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(), str[which], Toast.LENGTH_LONG).show();
                                    Log.i("SelectedPair", str[which]);
                                    pair = str[which];

                                    if(check_trun_for_code.trim().equals("Your")){
                                        colorCode="yellow";
                                    }
                                    else {
                                        colorCode="red";
                                    }


                                    JSONObject jsonObject = new JSONObject();

                                    try {
                                        jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                        jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                        jsonObject.put("turn_id", 11);
                                        jsonObject.put("number", i);
                                        jsonObject.put("dice1_number", dice1_number);
                                        jsonObject.put("dice2_number", dice2_number);
                                        jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                        jsonObject.put("pair", pair);
                                        jsonObject.put("pos_color", colorCode);
                                        jsonObject.put("count_for_no_combination", 2);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Log.i("jsonDataIs", jsonObject.toString());

                                    socket.emit("playEvent", jsonObject.toString());

                                }
                            });

                            AlertDialog dialog = builder.create();
                         //   dialog.show();
                         //   dialog.getWindow().setGravity(Gravity.BOTTOM);
                        } else {

                            Toast.makeText(getApplicationContext(), "No Combination Possible", Toast.LENGTH_LONG).show();



                            if(zokar_txt.getText().toString().trim().equals("0")){

                                methodNOJoker();

                                JSONObject jsonObject = new JSONObject();

                                try {
                                    jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                    jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                    jsonObject.put("turn_id", 11);
                                    jsonObject.put("number", i);
                                    jsonObject.put("dice1_number", dice1_number);
                                    jsonObject.put("dice2_number", dice2_number);
                                    jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                    jsonObject.put("pair", "");
                                    jsonObject.put("pos_color", "");
                                    jsonObject.put("count_for_no_combination", 2);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Log.i("jsonDataIs", jsonObject.toString());

                                socket.emit("playEvent", jsonObject.toString());

                            }

                            else {
                                final Dialog dialog = new Dialog(PlayVsPlayerSocket.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(false);
                                dialog.setContentView(R.layout.dialog_re_roll);


                                Button no = dialog.findViewById(R.id.no);
                                Button yes = dialog.findViewById(R.id.yes);


                                no.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        dialog.dismiss();

                                        JSONObject jsonObject = new JSONObject();

                                        try {
                                            jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                            jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                            jsonObject.put("turn_id", 11);
                                            jsonObject.put("number", i);
                                            jsonObject.put("dice1_number", dice1_number);
                                            jsonObject.put("dice2_number", dice2_number);
                                            jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                            jsonObject.put("pair", "");
                                            jsonObject.put("pos_color", "");
                                            jsonObject.put("count_for_no_combination", 2);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        Log.i("jsonDataIs", jsonObject.toString());

                                        socket.emit("playEvent", jsonObject.toString());


                                    }
                                });


                                yes.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                        methodReRollNew();
                                    }
                                });


                                dialog.show();
                            }

                        }


                    }

                }, 1000);


            }


            return true;


        }
    };

    //Re-Roll New>>>>>>>>>>>>>>>>

    private void methodReRollNew(){


        ApiInterface apiInterface = AppConfig.getRetrofit().create(ApiInterface.class);

        token=SharedHelper.getKey(getApplicationContext(), UserConstant.userToken);


        Call<ModelReUseJoker> call = apiInterface.re_use_joker("Bearer " + token,SharedHelper.getKey(getApplicationContext(), UserConstant.id));


        call.enqueue(new Callback<ModelReUseJoker>() {
            @Override
            public void onResponse(Call<ModelReUseJoker> call, Response<ModelReUseJoker> response) {


                if (response.isSuccessful()) {

                    if (response.body().getStatus()) {

                        SharedHelper.putKey(PlayVsPlayerSocket.this,UserConstant.zokar,response.body().getData().get(0).getTotal_zoker());

                        zokar_txt.setText(SharedHelper.getKey(PlayVsPlayerSocket.this, UserConstant.zokar));

                        //Show rolling image
                        dice_picture.setImageResource(R.drawable.dice3d160);
                        dice2_picture.setImageResource(R.drawable.dice3d160);

                        //Start rolling sound
                        soundplay = dice_sound.play(sound_id, 1.0f, 1.0f, 0, 0, 1.0f);

                        //Pause to allow image to update
                        dice_picture.startAnimation(rotate);
                        dice2_picture.startAnimation(rotate);
                        try {
                            timer.schedule(new PlayVsPlayerSocket.RollFour(), 1100);
                        } catch (Exception e) {

                        }

                    }

                }


            }

            @Override
            public void onFailure(Call<ModelReUseJoker> call, Throwable t) {
                Log.i("fail_login", t.toString());

                CustomToast.displayError(getApplicationContext(), t.getMessage());
                if (t instanceof SocketTimeoutException) {

                }
            }
        });


    }

    // Roll Four>>>>>

    class RollFour extends TimerTask {
        public void run() {
            handlerfour.sendEmptyMessage(0);
        }
    }

    Handler.Callback callbackfour = new Handler.Callback() {

        public boolean handleMessage(Message msg) {



            switch (rng.nextInt(6) + 1) {
                case 1:
                    rotate.cancel();
                    dice1_number = 1;
                    // dice_no.setText("Dice Number : 1");
                    dice_picture.setImageResource(R.drawable.one);
                    break;
                case 2:
                    rotate.cancel();
                    dice1_number = 2;
                    //  dice_no.setText("Dice Number : 2");
                    dice_picture.setImageResource(R.drawable.two);
                    break;
                case 3:
                    rotate.cancel();
                    dice1_number = 3;
                    // dice_no.setText("Dice Number : 3");
                    dice_picture.setImageResource(R.drawable.three);
                    break;
                case 4:
                    rotate.cancel();
                    dice1_number = 4;
                    // dice_no.setText("Dice Number : 4");
                    dice_picture.setImageResource(R.drawable.four);
                    break;
                case 5:
                    rotate.cancel();
                    dice1_number = 5;
                    // dice_no.setText("Dice Number : 5");
                    dice_picture.setImageResource(R.drawable.five);
                    break;
                case 6:
                    rotate.cancel();
                    dice1_number = 6;
                    // dice_no.setText("Dice Number : 6");
                    dice_picture.setImageResource(R.drawable.six);
                    break;
                default:
            }
            switch (rng.nextInt(6) + 1) {
                case 1:
                    rotate.cancel();
                    dice2_number = 1;
                    //  dice_no.setText("Dice Number : 1");
                    dice2_picture.setImageResource(R.drawable.one);
                    break;
                case 2:
                    rotate.cancel();
                    dice2_number = 2;
                    // dice_no.setText("Dice Number : 2");
                    dice2_picture.setImageResource(R.drawable.two);
                    break;

                case 3:
                    rotate.cancel();
                    dice2_number = 3;
                    // dice_no.setText("Dice Number : 3");
                    dice2_picture.setImageResource(R.drawable.three);
                    break;

                case 4:
                    rotate.cancel();
                    dice2_number = 4;
                    //  dice_no.setText("Dice Number : 4");
                    dice2_picture.setImageResource(R.drawable.four);
                    break;

                case 5:
                    rotate.cancel();
                    dice2_number = 5;
                    // dice_no.setText("Dice Number : 5");
                    dice2_picture.setImageResource(R.drawable.five);
                    break;
                case 6:
                    rotate.cancel();
                    dice2_number = 6;
                    // dice_no.setText("Dice Number : 6");
                    dice2_picture.setImageResource(R.drawable.six);
                    break;
                default:
            }

            rolling = false;  //user can press again

            i = dice1_number + dice2_number;


            if (i == 2) {

                Log.i("arrayList", arrayList_two.toString());

                arrayListFilterData = new ArrayList<>();

                for (int s = 0; s < arrayList_two.size(); s++) {

                    Log.i("ArrayListData", arrayList_two.get(s));


                    if (arrayList_two.get(s).equals("2")) {

                        if (txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_two.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_two.get(s));
                        }

                    }

                }


                new Handler().postDelayed(new Runnable() {


                    @Override

                    public void run() {


                        if (arrayListFilterData.size() > 0) {

                            String[] str = GetStringArray(arrayListFilterData);
                            layout_dice_movement.setEnabled(false);
                            for(int i=0;i<arrayListFilterData.size();i++){
                                if(arrayListFilterData.get(i).equals("2")){
                                    shape_two_you.setImageResource(resIdTwo[0]);
                                }
                            }

                            builder.setItems(str, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                    Toast.makeText(getApplicationContext(), str[which], Toast.LENGTH_LONG).show();
                                    Log.i("SelectedPair", str[which]);

                                    pair = str[which];

                                    if(check_trun_for_code.trim().equals("Your")){
                                        colorCode="yellow";
                                    }
                                    else {
                                        colorCode="red";
                                    }


                                    JSONObject jsonObject = new JSONObject();

                                    try {
                                        jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                        jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                        jsonObject.put("turn_id", 11);
                                        jsonObject.put("number", i);
                                        jsonObject.put("dice1_number", dice1_number);
                                        jsonObject.put("dice2_number", dice2_number);
                                        jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                        jsonObject.put("pair", pair);
                                        jsonObject.put("pos_color", colorCode);
                                        jsonObject.put("count_for_no_combination", 2);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Log.i("jsonDataIs", jsonObject.toString());

                                    socket.emit("playEvent", jsonObject.toString());

                                }
                            });

                            AlertDialog dialog = builder.create();
                           // dialog.show();
                           // dialog.getWindow().setGravity(Gravity.BOTTOM);
                        } else {

                            JSONObject jsonObject = new JSONObject();

                            try {
                                jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                jsonObject.put("turn_id", 11);
                                jsonObject.put("number", i);
                                jsonObject.put("dice1_number", dice1_number);
                                jsonObject.put("dice2_number", dice2_number);
                                jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                jsonObject.put("pair", "");
                                jsonObject.put("pos_color", "");
                                jsonObject.put("count_for_no_combination", 2);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Log.i("jsonDataIs", jsonObject.toString());

                            socket.emit("playEvent", jsonObject.toString());

                            Toast.makeText(getApplicationContext(), "No Combination Possible", Toast.LENGTH_LONG).show();

                        }


                    }

                }, 1000);


            }
            else if (i == 3) {
                Log.i("arrayList", arrayList_three.toString());

                arrayListFilterData = new ArrayList<>();

                for (int s = 0; s < arrayList_three.size(); s++) {
                    Log.i("ArrayListData", arrayList_three.get(s));

                    if (arrayList_three.get(s).equals("3")) {

                        if (txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_two.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_three.get(s));
                        }

                    } else if (arrayList_three.get(s).equals("2,1")) {
                        if (txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_one.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            // arrayList_two.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_three.get(s));
                        }
                    }


                }


                new Handler().postDelayed(new Runnable() {


                    @Override

                    public void run() {


                        if (arrayListFilterData.size() > 0) {
                            String[] str = GetStringArray(arrayListFilterData);
                            layout_dice_movement.setEnabled(false);
                            for(int i=0;i<arrayListFilterData.size();i++){
                                if(arrayListFilterData.get(i).equals("3")){
                                    shape_three_you.setImageResource(resIdThree[0]);
                                }
                                if(arrayListFilterData.get(i).equals("2,1")){
                                    shape_two_you.setImageResource(resIdTwo[1]);
                                    shape_one_you.setImageResource(resIdOne[1]);
                                }
                            }

                            builder.setItems(str, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(), str[which], Toast.LENGTH_LONG).show();
                                    Log.i("SelectedPair", str[which]);
                                    pair = str[which];

                                    if(check_trun_for_code.trim().equals("Your")){
                                        colorCode="yellow";
                                    }
                                    else {
                                        colorCode="red";
                                    }


                                    JSONObject jsonObject = new JSONObject();

                                    try {
                                        jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                        jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                        jsonObject.put("turn_id", 11);
                                        jsonObject.put("number", i);
                                        jsonObject.put("dice1_number", dice1_number);
                                        jsonObject.put("dice2_number", dice2_number);
                                        jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                        jsonObject.put("pair", pair);
                                        jsonObject.put("pos_color", colorCode);
                                        jsonObject.put("count_for_no_combination", 2);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Log.i("jsonDataIs", jsonObject.toString());

                                    socket.emit("playEvent", jsonObject.toString());
                                }
                            });

                            AlertDialog dialog = builder.create();
                           // dialog.show();
                           // dialog.getWindow().setGravity(Gravity.BOTTOM);
                        } else {





                            JSONObject jsonObject = new JSONObject();

                            try {
                                jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                jsonObject.put("turn_id", 11);
                                jsonObject.put("number", i);
                                jsonObject.put("dice1_number", dice1_number);
                                jsonObject.put("dice2_number", dice2_number);
                                jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                jsonObject.put("pair", "");
                                jsonObject.put("pos_color", "");
                                jsonObject.put("count_for_no_combination", 2);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Log.i("jsonDataIs", jsonObject.toString());

                            socket.emit("playEvent", jsonObject.toString());

                            Toast.makeText(getApplicationContext(), "No Combination Possible", Toast.LENGTH_LONG).show();







                        }


                    }

                }, 1000);


            }
            else if (i == 4) {
                Log.i("arrayList", arrayList_four.toString());

                arrayListFilterData = new ArrayList<>();

                for (int s = 0; s < arrayList_four.size(); s++) {
                    Log.i("ArrayListData", arrayList_four.get(s));

                    if (arrayList_four.get(s).equals("4")) {

                        if (txt_four.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            // arrayList_four.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_four.get(s));
                        }

                    } else if (arrayList_four.get(s).equals("3,1")) {
                        if (txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_one.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            // arrayList_four.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_four.get(s));
                        }
                    }

                }


                new Handler().postDelayed(new Runnable() {


                    @Override

                    public void run() {


                        if (arrayListFilterData.size() > 0) {

                            String[] str = GetStringArray(arrayListFilterData);
                            layout_dice_movement.setEnabled(false);
                            for(int i=0;i<arrayListFilterData.size();i++){
                                if(arrayListFilterData.get(i).equals("4")){
                                    shape_four_you.setImageResource(resIdFour[0]);
                                }
                                if(arrayListFilterData.get(i).equals("3,1")){
                                    shape_three_you.setImageResource(resIdThree[1]);
                                    shape_one_you.setImageResource(resIdOne[1]);
                                }
                            }

                            builder.setItems(str, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(), str[which], Toast.LENGTH_LONG).show();
                                    Log.i("SelectedPair", str[which]);
                                    pair = str[which];


                                    if(check_trun_for_code.trim().equals("Your")){
                                        colorCode="yellow";
                                    }
                                    else {
                                        colorCode="red";
                                    }


                                    JSONObject jsonObject = new JSONObject();

                                    try {
                                        jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                        jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                        jsonObject.put("turn_id", 11);
                                        jsonObject.put("number", i);
                                        jsonObject.put("dice1_number", dice1_number);
                                        jsonObject.put("dice2_number", dice2_number);
                                        jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                        jsonObject.put("pair", pair);
                                        jsonObject.put("pos_color", colorCode);
                                        jsonObject.put("count_for_no_combination", 2);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Log.i("jsonDataIs", jsonObject.toString());

                                    socket.emit("playEvent", jsonObject.toString());
                                }
                            });

                            AlertDialog dialog = builder.create();
                          //  dialog.show();
                          //  dialog.getWindow().setGravity(Gravity.BOTTOM);
                        }
                        else {







                            JSONObject jsonObject = new JSONObject();

                            try {
                                jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                jsonObject.put("turn_id", 11);
                                jsonObject.put("number", i);
                                jsonObject.put("dice1_number", dice1_number);
                                jsonObject.put("dice2_number", dice2_number);
                                jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                jsonObject.put("pair", "");
                                jsonObject.put("pos_color", "");
                                jsonObject.put("count_for_no_combination", 2);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Log.i("jsonDataIs", jsonObject.toString());

                            socket.emit("playEvent", jsonObject.toString());

                            Toast.makeText(getApplicationContext(), "No Combination Possible", Toast.LENGTH_LONG).show();


                        }


                    }

                }, 1000);


            }
            else if (i == 5) {
                Log.i("arrayList", arrayList_five.toString());

                arrayListFilterData = new ArrayList<>();

                for (int s = 0; s < arrayList_five.size(); s++) {
                    Log.i("ArrayListData", arrayList_five.get(s));

                    if (arrayList_five.get(s).equals("5")) {

                        if (txt_five.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            // arrayList_five.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_five.get(s));
                        }

                    } else if (arrayList_five.get(s).equals("4,1")) {
                        if (txt_four.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_one.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_five.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_five.get(s));
                        }
                    } else if (arrayList_five.get(s).equals("3,2")) {
                        if (txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            // arrayList_five.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_five.get(s));
                        }
                    }


                }


                new Handler().postDelayed(new Runnable() {


                    @Override

                    public void run() {


                        if (arrayListFilterData.size() > 0) {

                            String[] str = GetStringArray(arrayListFilterData);
                            layout_dice_movement.setEnabled(false);
                            for(int i=0;i<arrayListFilterData.size();i++){
                                if(arrayListFilterData.get(i).equals("5")){
                                    shape_five_you.setImageResource(resIdFive[0]);
                                }
                                if(arrayListFilterData.get(i).equals("4,1")){
                                    shape_four_you.setImageResource(resIdFour[1]);
                                    shape_one_you.setImageResource(resIdOne[1]);
                                }
                                if(arrayListFilterData.get(i).equals("3,2")){
                                    shape_three_you.setImageResource(resIdThree[2]);
                                    shape_two_you.setImageResource(resIdTwo[2]);
                                }
                            }

                            builder.setItems(str, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(), str[which], Toast.LENGTH_LONG).show();
                                    Log.i("SelectedPair", str[which]);
                                    pair = str[which];


                                    if(check_trun_for_code.trim().equals("Your")){
                                        colorCode="yellow";
                                    }
                                    else {
                                        colorCode="red";
                                    }


                                    JSONObject jsonObject = new JSONObject();

                                    try {
                                        jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                        jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                        jsonObject.put("turn_id", 11);
                                        jsonObject.put("number", i);
                                        jsonObject.put("dice1_number", dice1_number);
                                        jsonObject.put("dice2_number", dice2_number);
                                        jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                        jsonObject.put("pair", pair);
                                        jsonObject.put("pos_color", colorCode);
                                        jsonObject.put("count_for_no_combination", 2);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Log.i("jsonDataIs", jsonObject.toString());

                                    socket.emit("playEvent", jsonObject.toString());

                                }
                            });

                            AlertDialog dialog = builder.create();
                          //  dialog.show();
                           // dialog.getWindow().setGravity(Gravity.BOTTOM);
                        } else {






                            JSONObject jsonObject = new JSONObject();

                            try {
                                jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                jsonObject.put("turn_id", 11);
                                jsonObject.put("number", i);
                                jsonObject.put("dice1_number", dice1_number);
                                jsonObject.put("dice2_number", dice2_number);
                                jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                jsonObject.put("pair", "");
                                jsonObject.put("pos_color", "");
                                jsonObject.put("count_for_no_combination", 2);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Log.i("jsonDataIs", jsonObject.toString());

                            socket.emit("playEvent", jsonObject.toString());

                            Toast.makeText(getApplicationContext(), "No Combination Possible", Toast.LENGTH_LONG).show();




                        }


                    }

                }, 1000);


            }
            else if (i == 6) {
                Log.i("arrayList", arrayList_six.toString());

                arrayListFilterData = new ArrayList<>();
                for (int s = 0; s < arrayList_six.size(); s++) {
                    Log.i("ArrayListData", arrayList_six.get(s));

                    if (arrayList_six.get(s).equals("6")) {

                        if (txt_six.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_six.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_six.get(s));
                        }

                    } else if (arrayList_six.get(s).equals("5,1")) {
                        if (txt_five.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_one.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_six.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_six.get(s));
                        }
                    } else if (arrayList_six.get(s).equals("4,2")) {
                        if (txt_four.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            // arrayList_six.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_six.get(s));
                        }
                    }

                }
                new Handler().postDelayed(new Runnable() {


                    @Override

                    public void run() {


                        if (arrayListFilterData.size() > 0) {

                            String[] str = GetStringArray(arrayListFilterData);
                            layout_dice_movement.setEnabled(false);
                            for(int i=0;i<arrayListFilterData.size();i++){
                                if(arrayListFilterData.get(i).equals("6")){
                                    shape_six_you.setImageResource(resIdSix[0]);
                                }
                                if(arrayListFilterData.get(i).equals("5,1")){
                                    shape_five_you.setImageResource(resIdFive[1]);
                                    shape_one_you.setImageResource(resIdOne[1]);
                                }
                                if(arrayListFilterData.get(i).equals("4,2")){
                                    shape_four_you.setImageResource(resIdFour[2]);
                                    shape_two_you.setImageResource(resIdTwo[2]);
                                }
                            }


                            builder.setItems(str, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(), str[which], Toast.LENGTH_LONG).show();
                                    Log.i("SelectedPair", str[which]);
                                    pair = str[which];


                                    if(check_trun_for_code.trim().equals("Your")){
                                        colorCode="yellow";
                                    }
                                    else {
                                        colorCode="red";
                                    }

                                    JSONObject jsonObject = new JSONObject();

                                    try {
                                        jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                        jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                        jsonObject.put("turn_id", 11);
                                        jsonObject.put("number", i);
                                        jsonObject.put("dice1_number", dice1_number);
                                        jsonObject.put("dice2_number", dice2_number);
                                        jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                        jsonObject.put("pair", pair);
                                        jsonObject.put("pos_color", colorCode);
                                        jsonObject.put("count_for_no_combination", 2);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Log.i("jsonDataIs", jsonObject.toString());

                                    socket.emit("playEvent", jsonObject.toString());

                                }
                            });

                            AlertDialog dialog = builder.create();
                           // dialog.show();
                          //  dialog.getWindow().setGravity(Gravity.BOTTOM);
                        } else {






                            JSONObject jsonObject = new JSONObject();

                            try {
                                jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                jsonObject.put("turn_id", 11);
                                jsonObject.put("number", i);
                                jsonObject.put("dice1_number", dice1_number);
                                jsonObject.put("dice2_number", dice2_number);
                                jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                jsonObject.put("pair", "");
                                jsonObject.put("pos_color", "");
                                jsonObject.put("count_for_no_combination", 2);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Log.i("jsonDataIs", jsonObject.toString());

                            socket.emit("playEvent", jsonObject.toString());

                            Toast.makeText(getApplicationContext(), "No Combination Possible", Toast.LENGTH_LONG).show();

                        }


                    }

                }, 1000);


            }
            else if (i == 7) {
                Log.i("arrayList", arrayList_seven.toString());

                arrayListFilterData = new ArrayList<>();

                for (int s = 0; s < arrayList_seven.size(); s++) {
                    Log.i("ArrayListData", arrayList_seven.get(s));

                    if (arrayList_seven.get(s).equals("7")) {

                        if (txt_seven.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_seven.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_seven.get(s));
                        }

                    } else if (arrayList_seven.get(s).equals("6,1")) {
                        if (txt_six.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_one.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_seven.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_seven.get(s));
                        }
                    } else if (arrayList_seven.get(s).equals("5,2")) {
                        if (txt_five.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_seven.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_seven.get(s));
                        }
                    } else if (arrayList_seven.get(s).equals("4,3")) {
                        if (txt_four.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            // arrayList_seven.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_seven.get(s));
                        }
                    }


                }


                new Handler().postDelayed(new Runnable() {


                    @Override

                    public void run() {


                        if (arrayListFilterData.size() > 0) {

                            String[] str = GetStringArray(arrayListFilterData);
                            layout_dice_movement.setEnabled(false);
                            for(int i=0;i<arrayListFilterData.size();i++){

                                if(arrayListFilterData.get(i).equals("7")){
                                    shape_seven_you.setImageResource(resIdSeven[0]);
                                }
                                if(arrayListFilterData.get(i).equals("6,1")){
                                    shape_six_you.setImageResource(resIdSix[1]);
                                    shape_one_you.setImageResource(resIdOne[1]);
                                }
                                if(arrayListFilterData.get(i).equals("5,2")){
                                    shape_five_you.setImageResource(resIdFive[2]);
                                    shape_two_you.setImageResource(resIdTwo[2]);
                                }
                                if(arrayListFilterData.get(i).equals("4,3")){
                                    shape_four_you.setImageResource(resIdFour[3]);
                                    shape_three_you.setImageResource(resIdThree[3]);
                                }
                            }


                            builder.setItems(str, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(), str[which], Toast.LENGTH_LONG).show();
                                    Log.i("SelectedPair", str[which]);
                                    pair = str[which];


                                    if(check_trun_for_code.trim().equals("Your")){
                                        colorCode="yellow";
                                    }
                                    else {
                                        colorCode="red";
                                    }


                                    JSONObject jsonObject = new JSONObject();

                                    try {
                                        jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                        jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                        jsonObject.put("turn_id", 11);
                                        jsonObject.put("number", i);
                                        jsonObject.put("dice1_number", dice1_number);
                                        jsonObject.put("dice2_number", dice2_number);
                                        jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                        jsonObject.put("pair", pair);
                                        jsonObject.put("pos_color", colorCode);
                                        jsonObject.put("count_for_no_combination", 2);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Log.i("jsonDataIs", jsonObject.toString());

                                    socket.emit("playEvent", jsonObject.toString());

                                }
                            });

                            AlertDialog dialog = builder.create();
                          //  dialog.show();
                          //  dialog.getWindow().setGravity(Gravity.BOTTOM);
                        } else {






                            JSONObject jsonObject = new JSONObject();

                            try {
                                jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                jsonObject.put("turn_id", 11);
                                jsonObject.put("number", i);
                                jsonObject.put("dice1_number", dice1_number);
                                jsonObject.put("dice2_number", dice2_number);
                                jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                jsonObject.put("pair", "");
                                jsonObject.put("pos_color", "");
                                jsonObject.put("count_for_no_combination", 2);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Log.i("jsonDataIs", jsonObject.toString());

                            socket.emit("playEvent", jsonObject.toString());

                            Toast.makeText(getApplicationContext(), "No Combination Possible", Toast.LENGTH_LONG).show();



                        }


                    }

                }, 1000);


            }
            else if (i == 8) {
                Log.i("arrayList", arrayList_eight.toString());

                arrayListFilterData = new ArrayList<>();
                for (int s = 0; s < arrayList_eight.size(); s++) {
                    Log.i("ArrayListData", arrayList_eight.get(s));

                    if (arrayList_eight.get(s).equals("8")) {

                        if (txt_eight.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_eight.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_eight.get(s));
                        }

                    } else if (arrayList_eight.get(s).equals("7,1")) {
                        if (txt_seven.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_one.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            // arrayList_eight.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_eight.get(s));
                        }
                    } else if (arrayList_eight.get(s).equals("6,2")) {
                        if (txt_six.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            // arrayList_eight.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_eight.get(s));
                        }
                    } else if (arrayList_eight.get(s).equals("5,3")) {
                        if (txt_five.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_eight.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_eight.get(s));
                        }
                    }

                }


                new Handler().postDelayed(new Runnable() {


                    @Override

                    public void run() {


                        if (arrayListFilterData.size() > 0) {

                            String[] str = GetStringArray(arrayListFilterData);
                            layout_dice_movement.setEnabled(false);
                            for(int i=0;i<arrayListFilterData.size();i++){
                                if(arrayListFilterData.get(i).equals("8")){
                                    shape_eight_you.setImageResource(resIdEight[0]);
                                }
                                if(arrayListFilterData.get(i).equals("7,1")){
                                    shape_seven_you.setImageResource(resIdSeven[1]);
                                    shape_one_you.setImageResource(resIdOne[1]);
                                }
                                if(arrayListFilterData.get(i).equals("6,2")){
                                    shape_six_you.setImageResource(resIdSix[2]);
                                    shape_two_you.setImageResource(resIdTwo[2]);
                                }
                                if(arrayListFilterData.get(i).equals("5,3")){
                                    shape_five_you.setImageResource(resIdFive[3]);
                                    shape_three_you.setImageResource(resIdThree[3]);
                                }
                            }


                            builder.setItems(str, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(), str[which], Toast.LENGTH_LONG).show();
                                    Log.i("SelectedPair", str[which]);
                                    pair = str[which];


                                    if(check_trun_for_code.trim().equals("Your")){
                                        colorCode="yellow";
                                    }
                                    else {
                                        colorCode="red";
                                    }


                                    JSONObject jsonObject = new JSONObject();

                                    try {
                                        jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                        jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                        jsonObject.put("turn_id", 11);
                                        jsonObject.put("number", i);
                                        jsonObject.put("dice1_number", dice1_number);
                                        jsonObject.put("dice2_number", dice2_number);
                                        jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                        jsonObject.put("pair", pair);
                                        jsonObject.put("pos_color", colorCode);
                                        jsonObject.put("count_for_no_combination", 2);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Log.i("jsonDataIs", jsonObject.toString());

                                    socket.emit("playEvent", jsonObject.toString());

                                }
                            });

                            AlertDialog dialog = builder.create();
                          //  dialog.show();
                          //  dialog.getWindow().setGravity(Gravity.BOTTOM);
                        } else {







                            JSONObject jsonObject = new JSONObject();

                            try {
                                jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                jsonObject.put("turn_id", 11);
                                jsonObject.put("number", i);
                                jsonObject.put("dice1_number", dice1_number);
                                jsonObject.put("dice2_number", dice2_number);
                                jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                jsonObject.put("pair", "");
                                jsonObject.put("pos_color", "");
                                jsonObject.put("count_for_no_combination", 2);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Log.i("jsonDataIs", jsonObject.toString());

                            socket.emit("playEvent", jsonObject.toString());

                            Toast.makeText(getApplicationContext(), "No Combination Possible", Toast.LENGTH_LONG).show();


                        }


                    }

                }, 1000);


            }
            else if (i == 9) {
                Log.i("arrayList", arrayList_nine.toString());

                arrayListFilterData = new ArrayList<>();

                for (int s = 0; s < arrayList_nine.size(); s++) {
                    Log.i("ArrayListData", arrayList_nine.get(s));

                    if (arrayList_nine.get(s).equals("9")) {

                        if (txt_nine.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_nine.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_nine.get(s));
                        }

                    } else if (arrayList_nine.get(s).equals("8,1")) {
                        if (txt_eight.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_one.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_nine.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_nine.get(s));
                        }
                    } else if (arrayList_nine.get(s).equals("7,2")) {
                        if (txt_seven.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            // arrayList_nine.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_nine.get(s));
                        }
                    } else if (arrayList_nine.get(s).equals("6,3")) {
                        if (txt_six.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_nine.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_nine.get(s));
                        }
                    } else if (arrayList_nine.get(s).equals("5,4")) {
                        if (txt_five.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_four.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_nine.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_nine.get(s));
                        }
                    }


                }

                new Handler().postDelayed(new Runnable() {


                    @Override

                    public void run() {


                        if (arrayListFilterData.size() > 0) {

                            String[] str = GetStringArray(arrayListFilterData);
                            layout_dice_movement.setEnabled(false);
                            for(int i=0;i<arrayListFilterData.size();i++){
                                if(arrayListFilterData.get(i).equals("9")){
                                    shape_nine_you.setImageResource(resIdNine[0]);
                                }
                                if(arrayListFilterData.get(i).equals("8,1")){
                                    shape_eight_you.setImageResource(resIdEight[1]);
                                    shape_one_you.setImageResource(resIdOne[1]);
                                }
                                if(arrayListFilterData.get(i).equals("7,2")){
                                    shape_seven_you.setImageResource(resIdSeven[2]);
                                    shape_two_you.setImageResource(resIdTwo[2]);
                                }
                                if(arrayListFilterData.get(i).equals("6,3")){
                                    shape_six_you.setImageResource(resIdSix[3]);
                                    shape_three_you.setImageResource(resIdThree[3]);
                                }
                                if(arrayListFilterData.get(i).equals("5,4")){
                                    shape_five_you.setImageResource(resIdFive[4]);
                                    shape_four_you.setImageResource(resIdFour[4]);
                                }
                            }


                            builder.setItems(str, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(), str[which], Toast.LENGTH_LONG).show();
                                    Log.i("SelectedPair", str[which]);
                                    pair = str[which];


                                    if(check_trun_for_code.trim().equals("Your")){
                                        colorCode="yellow";
                                    }
                                    else {
                                        colorCode="red";
                                    }

                                    JSONObject jsonObject = new JSONObject();

                                    try {
                                        jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                        jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                        jsonObject.put("turn_id", 11);
                                        jsonObject.put("number", i);
                                        jsonObject.put("dice1_number", dice1_number);
                                        jsonObject.put("dice2_number", dice2_number);
                                        jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                        jsonObject.put("pair", pair);
                                        jsonObject.put("pos_color", colorCode);
                                        jsonObject.put("count_for_no_combination", 2);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Log.i("jsonDataIs", jsonObject.toString());

                                    socket.emit("playEvent", jsonObject.toString());

                                }
                            });

                            AlertDialog dialog = builder.create();
                          //  dialog.show();
                          //  dialog.getWindow().setGravity(Gravity.BOTTOM);
                        } else {





                            JSONObject jsonObject = new JSONObject();

                            try {
                                jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                jsonObject.put("turn_id", 11);
                                jsonObject.put("number", i);
                                jsonObject.put("dice1_number", dice1_number);
                                jsonObject.put("dice2_number", dice2_number);
                                jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                jsonObject.put("pair", "");
                                jsonObject.put("pos_color", "");
                                jsonObject.put("count_for_no_combination", 2);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Log.i("jsonDataIs", jsonObject.toString());

                            socket.emit("playEvent", jsonObject.toString());

                            Toast.makeText(getApplicationContext(), "No Combination Possible", Toast.LENGTH_LONG).show();

                        }


                    }

                }, 1000);

            }

            else if (i == 10) {
                Log.i("arrayList", arrayList_ten.toString());

                arrayListFilterData = new ArrayList<>();

                for (int s = 0; s < arrayList_ten.size(); s++) {
                    Log.i("ArrayListData", arrayList_ten.get(s));

                    if (arrayList_ten.get(s).equals("9,1")) {
                        if (txt_nine.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_one.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            // arrayList_ten.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_ten.get(s));
                        }
                    } else if (arrayList_ten.get(s).equals("8,2")) {
                        if (txt_eight.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_ten.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_ten.get(s));
                        }
                    } else if (arrayList_ten.get(s).equals("7,3")) {
                        if (txt_seven.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            // arrayList_ten.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_ten.get(s));
                        }
                    } else if (arrayList_ten.get(s).equals("6,4")) {
                        if (txt_six.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_four.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_ten.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_ten.get(s));
                        }
                    }


                }


                new Handler().postDelayed(new Runnable() {


                    @Override

                    public void run() {


                        if (arrayListFilterData.size() > 0) {

                            String[] str = GetStringArray(arrayListFilterData);
                            layout_dice_movement.setEnabled(false);
                            for(int i=0;i<arrayListFilterData.size();i++){

                                if(arrayListFilterData.get(i).equals("9,1")){
                                    shape_nine_you.setImageResource(resIdNine[0]);
                                    shape_one_you.setImageResource(resIdOne[0]);
                                }
                                if(arrayListFilterData.get(i).equals("8,2")){
                                    shape_eight_you.setImageResource(resIdEight[1]);
                                    shape_two_you.setImageResource(resIdTwo[1]);
                                }
                                if(arrayListFilterData.get(i).equals("7,3")){
                                    shape_seven_you.setImageResource(resIdSeven[2]);
                                    shape_three_you.setImageResource(resIdThree[2]);
                                }
                                if(arrayListFilterData.get(i).equals("6,4")){
                                    shape_six_you.setImageResource(resIdSix[3]);
                                    shape_four_you.setImageResource(resIdFour[3]);
                                }
                            }

                            builder.setItems(str, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(), str[which], Toast.LENGTH_LONG).show();
                                    Log.i("SelectedPair", str[which]);
                                    pair = str[which];

                                    if(check_trun_for_code.trim().equals("Your")){
                                        colorCode="yellow";
                                    }
                                    else {
                                        colorCode="red";
                                    }


                                    JSONObject jsonObject = new JSONObject();

                                    try {
                                        jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                        jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                        jsonObject.put("turn_id", 11);
                                        jsonObject.put("number", i);
                                        jsonObject.put("dice1_number", dice1_number);
                                        jsonObject.put("dice2_number", dice2_number);
                                        jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                        jsonObject.put("pair", pair);
                                        jsonObject.put("pos_color", colorCode);
                                        jsonObject.put("count_for_no_combination", 2);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Log.i("jsonDataIs", jsonObject.toString());

                                    socket.emit("playEvent", jsonObject.toString());

                                }
                            });

                            AlertDialog dialog = builder.create();
                           // dialog.show();
                           // dialog.getWindow().setGravity(Gravity.BOTTOM);
                        } else {

                            JSONObject jsonObject = new JSONObject();

                            try {
                                jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                jsonObject.put("turn_id", 11);
                                jsonObject.put("number", i);
                                jsonObject.put("dice1_number", dice1_number);
                                jsonObject.put("dice2_number", dice2_number);
                                jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                jsonObject.put("pair", "");
                                jsonObject.put("pos_color", "");
                                jsonObject.put("count_for_no_combination", 2);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Log.i("jsonDataIs", jsonObject.toString());

                            socket.emit("playEvent", jsonObject.toString());


                            Toast.makeText(getApplicationContext(), "No Combination Possible", Toast.LENGTH_LONG).show();

                        }


                    }

                }, 1000);


            }
            else if (i == 11) {
                Log.i("arrayList", arrayList_eveven.toString());

                arrayListFilterData = new ArrayList<>();

                for (int s = 0; s < arrayList_eveven.size(); s++) {
                    Log.i("ArrayListData", arrayList_eveven.get(s));

                    if (arrayList_eveven.get(s).equals("9,2")) {
                        if (txt_nine.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_eveven.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_eveven.get(s));
                        }
                    } else if (arrayList_eveven.get(s).equals("8,3")) {
                        if (txt_eight.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            // arrayList_eveven.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_eveven.get(s));
                        }
                    } else if (arrayList_eveven.get(s).equals("7,4")) {
                        if (txt_seven.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_four.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            // arrayList_eveven.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_eveven.get(s));
                        }
                    } else if (arrayList_eveven.get(s).equals("6,5")) {
                        if (txt_six.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_five.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_eveven.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_eveven.get(s));
                        }
                    }


                }


                new Handler().postDelayed(new Runnable() {


                    @Override

                    public void run() {


                        if (arrayListFilterData.size() > 0) {

                            String[] str = GetStringArray(arrayListFilterData);
                            layout_dice_movement.setEnabled(false);
                            for(int i=0;i<arrayListFilterData.size();i++){

                                if(arrayListFilterData.get(i).equals("9,2")){
                                    shape_nine_you.setImageResource(resIdNine[0]);
                                    shape_two_you.setImageResource(resIdTwo[0]);
                                }
                                if(arrayListFilterData.get(i).equals("8,3")){
                                    shape_eight_you.setImageResource(resIdEight[1]);
                                    shape_three_you.setImageResource(resIdThree[1]);
                                }
                                if(arrayListFilterData.get(i).equals("7,4")){
                                    shape_seven_you.setImageResource(resIdSeven[2]);
                                    shape_four_you.setImageResource(resIdFour[2]);
                                }
                                if(arrayListFilterData.get(i).equals("6,5")){
                                    shape_six_you.setImageResource(resIdSix[3]);
                                    shape_five_you.setImageResource(resIdFive[3]);
                                }
                            }

                            builder.setItems(str, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(), str[which], Toast.LENGTH_LONG).show();
                                    Log.i("SelectedPair", str[which]);
                                    pair = str[which];

                                    if(check_trun_for_code.trim().equals("Your")){
                                        colorCode="yellow";
                                    }
                                    else {
                                        colorCode="red";
                                    }


                                    JSONObject jsonObject = new JSONObject();

                                    try {
                                        jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                        jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                        jsonObject.put("turn_id", 11);
                                        jsonObject.put("number", i);
                                        jsonObject.put("dice1_number", dice1_number);
                                        jsonObject.put("dice2_number", dice2_number);
                                        jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                        jsonObject.put("pair", pair);
                                        jsonObject.put("pos_color", colorCode);
                                        jsonObject.put("count_for_no_combination", 2);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Log.i("jsonDataIs", jsonObject.toString());

                                    socket.emit("playEvent", jsonObject.toString());

                                }
                            });

                            AlertDialog dialog = builder.create();
                           // dialog.show();
                           // dialog.getWindow().setGravity(Gravity.BOTTOM);
                        }
                        else {

                            JSONObject jsonObject = new JSONObject();

                            try {
                                jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                jsonObject.put("turn_id", 11);
                                jsonObject.put("number", i);
                                jsonObject.put("dice1_number", dice1_number);
                                jsonObject.put("dice2_number", dice2_number);
                                jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                jsonObject.put("pair", "");
                                jsonObject.put("pos_color", "");
                                jsonObject.put("count_for_no_combination", 2);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Log.i("jsonDataIs", jsonObject.toString());

                            socket.emit("playEvent", jsonObject.toString());


                            Toast.makeText(getApplicationContext(), "No Combination Possible", Toast.LENGTH_LONG).show();

                        }


                    }

                }, 1000);


            }
            else if (i == 12) {
                Log.i("arrayList", arrayList_twelve.toString());

                arrayListFilterData = new ArrayList<>();


                for (int s = 0; s < arrayList_twelve.size(); s++) {
                    Log.i("ArrayListData", arrayList_twelve.get(s));

                    if (arrayList_twelve.get(s).equals("9,3")) {
                        if (txt_nine.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_twelve.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_twelve.get(s));
                        }
                    } else if (arrayList_twelve.get(s).equals("8,4")) {
                        if (txt_eight.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_four.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //arrayList_twelve.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_twelve.get(s));
                        }
                    } else if (arrayList_twelve.get(s).equals("7,5")) {
                        if (txt_seven.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)
                                || txt_five.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                            //  arrayList_twelve.remove(s);
                        } else {
                            arrayListFilterData.add(arrayList_twelve.get(s));
                        }
                    }


                }


                new Handler().postDelayed(new Runnable() {


                    @Override

                    public void run() {


                        if (arrayListFilterData.size() > 0) {

                            String[] str = GetStringArray(arrayListFilterData);
                            layout_dice_movement.setEnabled(false);
                            for(int i=0;i<arrayListFilterData.size();i++){

                                if(arrayListFilterData.get(i).equals("9,3")){
                                    shape_nine_you.setImageResource(resIdNine[0]);
                                    shape_three_you.setImageResource(resIdThree[0]);
                                }
                                if(arrayListFilterData.get(i).equals("8,4")){
                                    shape_eight_you.setImageResource(resIdEight[1]);
                                    shape_four_you.setImageResource(resIdFour[1]);
                                }
                                if(arrayListFilterData.get(i).equals("7,5")){
                                    shape_seven_you.setImageResource(resIdSeven[2]);
                                    shape_five_you.setImageResource(resIdFive[2]);
                                }

                            }

                            builder.setItems(str, new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(), str[which], Toast.LENGTH_LONG).show();
                                    Log.i("SelectedPair", str[which]);
                                    pair = str[which];


                                    if(check_trun_for_code.trim().equals("Your")){
                                        colorCode="yellow";
                                    }
                                    else {
                                        colorCode="red";
                                    }


                                    JSONObject jsonObject = new JSONObject();

                                    try {
                                        jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                        jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                        jsonObject.put("turn_id", 11);
                                        jsonObject.put("number", i);
                                        jsonObject.put("dice1_number", dice1_number);
                                        jsonObject.put("dice2_number", dice2_number);
                                        jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                        jsonObject.put("pair", pair);
                                        jsonObject.put("pos_color", colorCode);
                                        jsonObject.put("count_for_no_combination", 2);
                                    }
                                    catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Log.i("jsonDataIs", jsonObject.toString());

                                    socket.emit("playEvent", jsonObject.toString());

                                }
                            });

                            AlertDialog dialog = builder.create();
                         //   dialog.show();
                          //  dialog.getWindow().setGravity(Gravity.BOTTOM);
                        }

                        else {

                            JSONObject jsonObject = new JSONObject();

                            try {
                                jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                jsonObject.put("player_id", Integer.parseInt(user_two_id));
                                jsonObject.put("turn_id", 11);
                                jsonObject.put("number", i);
                                jsonObject.put("dice1_number", dice1_number);
                                jsonObject.put("dice2_number", dice2_number);
                                jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
                                jsonObject.put("pair", "");
                                jsonObject.put("pos_color", "");
                                jsonObject.put("count_for_no_combination", 2);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Log.i("jsonDataIs", jsonObject.toString());

                            socket.emit("playEvent", jsonObject.toString());

                            Toast.makeText(getApplicationContext(), "No Combination Possible", Toast.LENGTH_LONG).show();

                        }

                    }

                }, 1000);


            }

            return true;

        }

    };

    private void methodNOJoker() {

        dialogNoJoker = new Dialog(PlayVsPlayerSocket.this);
        dialogNoJoker.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogNoJoker.setContentView(R.layout.dialog_if_not_joker);

        RelativeLayout layout_ok = dialogNoJoker.findViewById(R.id.layout_ok);

        layout_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogNoJoker.dismiss();
            }
        });

        dialogNoJoker.show();

    }


    private void addsAndAddCoins(){

        if(counter==3){

            AdMobrewardedVideoAd
                = MobileAds.getRewardedVideoAdInstance(getApplicationContext());

        AdMobrewardedVideoAd.setRewardedVideoAdListener(
                new RewardedVideoAdListener() {
                    @Override
                    public void onRewardedVideoAdLoaded()
                    {

                        Toast.makeText(getApplicationContext(),
                                        "onRewardedVideoAdLoaded",
                                        Toast.LENGTH_SHORT)
                                .show();

                        showRewardedVideoAd();
                    }

                    @Override
                    public void onRewardedVideoAdOpened()
                    {
                        // Showing Toast Message
                        Toast
                                .makeText(getApplicationContext(),
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


                    }

                    @Override
                    public void
                    onRewardedVideoAdLeftApplication()
                    {

                    }

                    @Override
                    public void onRewardedVideoAdFailedToLoad(
                            int i)
                    {

                    }

                    @Override
                    public void onRewardedVideoCompleted()
                    {

                    }
                });


        AdMobrewardedVideoAd.loadAd(
                AdId, new AdRequest.Builder().build());

        }

        else {

            apiCallAddCoins();

        }


    }


    private void showRewardedVideoAd() {
        // Checking If Ad is Loaded or Not
        if (AdMobrewardedVideoAd.isLoaded()) {
            // showing Video Ad
            AdMobrewardedVideoAd.show();
            apiCallAddCoins();
        }
        else {
            // Loading Rewarded Video Ad
            AdMobrewardedVideoAd.loadAd(
                    AdId, new AdRequest.Builder().build());
        }
    }


    private void apiCallAddCoins() {

        ApiInterface apiInterface = AppConfig.getRetrofit().create(ApiInterface.class);
        Call<ModelAddCoins> call = apiInterface.add_coins("Bearer " + token, SharedHelper.getKey(getApplicationContext(), UserConstant.id),bet_status,bet_coins);
        call.enqueue(new Callback<ModelAddCoins>() {
            @Override
            public void onResponse(Call<ModelAddCoins> call, Response<ModelAddCoins> response) {

                if (response.isSuccessful()) {

                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();

                    if (response.body().getStatus()) {

                        try {
                            SharedHelper.putKey(getApplicationContext(),UserConstant.coin,response.body().getData().get(0).getTotal_coins());
                            total_coin_txt.setText(SharedHelper.getKey(PlayVsPlayerSocket.this, UserConstant.coin));
                            MainActivity.total_coin_txt.setText(SharedHelper.getKey(PlayVsPlayerSocket.this, UserConstant.coin));

                            SharedHelper.putKey(getApplicationContext(),UserConstant.point,response.body().getData().get(0).getTotal_points());
                            ttxt.setText(SharedHelper.getKey(PlayVsPlayerSocket.this, UserConstant.point));
                            MainActivity.ttxt.setText(SharedHelper.getKey(PlayVsPlayerSocket.this, UserConstant.point));


                        }catch (Exception e){

                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<ModelAddCoins> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {

                }
            }
        });

    }


    private void adsMethod() {
        adView = (AdView) findViewById(R.id.ad_view);
        adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        if(SharedHelper.getKey(getApplicationContext(),UserConstant.adsenseFree).equals("true")){
            adView.setVisibility(View.GONE);
        }

    }

    protected void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
        dice_sound.pause(sound_id);
    }

    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        socket.disconnect();
        //Toast.makeText(getApplicationContext(), "DisConnected", Toast.LENGTH_LONG).show();
        super.onDestroy();
        try {
            timer.cancel();
        } catch (Exception e) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }




    private void methodForSetPairData(String selected_number)
    {


        switch (String.valueOf(i))
        {
            case "2":

                if(selected_number.equals("2")){

                    methodEmitData("2");

                }

                break;


            case "3":

                if(selected_number.equals("3")){
                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("3")){

                            methodEmitData("3");

                        }
                    }
                }

                if(selected_number.equals("2")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("2,1")){

                            methodEmitData("2,1");

                        }
                    }
                }

                if(selected_number.equals("1")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("2,1")){

                            methodEmitData("2,1");

                        }
                    }

                }

                break;


            case "4":

                if(selected_number.equals("4")){
                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("4")){
                            methodEmitData("4");
                        }
                    }

                }
                if(selected_number.equals("3")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("3,1")){
                            methodEmitData("3,1");
                        }
                    }

                }
                if(selected_number.equals("1")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("3,1")){
                            methodEmitData("3,1");
                        }
                    }


                }

                break;


            case "5":


                if(selected_number.equals("5")){
                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("5")){
                            methodEmitData("5");
                        }
                    }
                }

                if(selected_number.equals("4")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("4,1")){
                            methodEmitData("4,1");
                        }
                    }

                }
                if(selected_number.equals("1")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("4,1")){
                            methodEmitData("4,1");
                        }
                    }


                }


                if(selected_number.equals("3")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("3,2")){
                            methodEmitData("3,2");
                        }
                    }

                }

                if(selected_number.equals("2")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("3,2")){
                            methodEmitData("3,2");
                        }
                    }


                }


                break;


            case "6":


                if(selected_number.equals("6")){
                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("6")){
                            methodEmitData("6");
                        }
                    }

                }
                if(selected_number.equals("5")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("5,1")){
                            methodEmitData("5,1");
                        }
                    }

                }
                if(selected_number.equals("1")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("5,1")){
                            methodEmitData("5,1");
                        }
                    }


                }



                if(selected_number.equals("4")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("4,2")){
                            methodEmitData("4,2");
                        }
                    }

                }
                if(selected_number.equals("2")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("4,2")){
                            methodEmitData("4,2");
                        }
                    }

                }


                break;


            case "7":


                if(selected_number.equals("7")){
                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("7")){
                            methodEmitData("7");
                        }
                    }

                }

                if(selected_number.equals("6")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("6,1")){
                            methodEmitData("6,1");
                        }
                    }

                }

                if(selected_number.equals("1")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("6,1")){
                            methodEmitData("6,1");
                        }
                    }

                }


                if(selected_number.equals("5")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("5,2")){
                            methodEmitData("5,2");
                        }
                    }

                }

                if(selected_number.equals("2")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("5,2")){
                            methodEmitData("5,2");
                        }
                    }

                }


                if(selected_number.equals("4")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("4,3")){
                            methodEmitData("4,3");
                        }
                    }

                }
                if(selected_number.equals("3")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("4,3")){
                            methodEmitData("4,3");
                        }
                    }


                }



                break;


            case "8":



                if(selected_number.equals("8")){
                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("8")){
                            methodEmitData("8");
                        }
                    }

                }
                if(selected_number.equals("7")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("7,1")){
                            methodEmitData("7,1");
                        }
                    }

                }
                if(selected_number.equals("1")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("7,1")){
                            methodEmitData("7,1");
                        }
                    }


                }


                if(selected_number.equals("6")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("6,2")){
                            methodEmitData("6,2");
                        }
                    }

                }
                if(selected_number.equals("2")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("6,2")){
                            methodEmitData("6,2");
                        }
                    }


                }




                if(selected_number.equals("5")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("5,3")){
                            methodEmitData("5,3");
                        }
                    }

                }
                if(selected_number.equals("3")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("5,3")){
                            methodEmitData("5,3");
                        }
                    }


                }


                break;


            case "9":



                if(selected_number.equals("9")){
                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("9")){
                            methodEmitData("9");
                        }
                    }

                }
                if(selected_number.equals("8")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("8,1")){
                            methodEmitData("8,1");
                        }
                    }

                }
                if(selected_number.equals("1")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("8,1")){
                            methodEmitData("8,1");
                        }
                    }


                }



                if(selected_number.equals("7")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("7,2")){
                            methodEmitData("7,2");
                        }
                    }

                }
                if(selected_number.equals("2")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("7,2")){
                            methodEmitData("7,2");
                        }
                    }


                }




                if(selected_number.equals("6")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("6,3")){
                            methodEmitData("6,3");
                        }
                    }

                }
                if(selected_number.equals("3")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("6,3")){
                            methodEmitData("6,3");
                        }
                    }


                }


                if(selected_number.equals("5")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("5,4")){
                            methodEmitData("5,4");
                        }
                    }

                }
                if(selected_number.equals("4")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("5,4")){
                            methodEmitData("5,4");
                        }
                    }


                }



                break;


            case "10":


                if(selected_number.equals("9")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("9,1")){
                            methodEmitData("9,1");
                        }
                    }

                }
                if(selected_number.equals("1")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("9,1")){
                            methodEmitData("9,1");
                        }
                    }


                }




                if(selected_number.equals("8")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("8,2")){
                            methodEmitData("8,2");
                        }
                    }

                }
                if(selected_number.equals("2")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("8,2")){
                            methodEmitData("8,2");
                        }
                    }


                }


                if(selected_number.equals("7")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("7,3")){
                            methodEmitData("7,3");
                        }
                    }

                }
                if(selected_number.equals("3")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("7,3")){
                            methodEmitData("7,3");
                        }
                    }


                }



                if(selected_number.equals("6")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("6,4")){
                            methodEmitData("6,4");
                        }
                    }

                }
                if(selected_number.equals("4")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("6,4")){
                            methodEmitData("6,4");
                        }
                    }


                }



                break;


            case "11":


                if(selected_number.equals("9")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("9,2")){
                            methodEmitData("9,2");
                        }
                    }

                }
                if(selected_number.equals("2")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("9,2")){
                            methodEmitData("9,2");
                        }
                    }


                }




                if(selected_number.equals("8")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("8,3")){
                            methodEmitData("8,3");
                        }
                    }

                }
                if(selected_number.equals("3")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("8,3")){
                            methodEmitData("8,3");
                        }
                    }


                }


                if(selected_number.equals("7")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("7,4")){
                            methodEmitData("7,4");
                        }
                    }

                }
                if(selected_number.equals("4")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("7,4")){
                            methodEmitData("7,4");
                        }
                    }


                }



                if(selected_number.equals("6")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("6,5")){
                            methodEmitData("6,5");
                        }
                    }

                }
                if(selected_number.equals("5")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("6,5")){
                            methodEmitData("6,5");
                        }
                    }


                }



                break;



            case "12":


                if(selected_number.equals("9")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("9,3")){
                            methodEmitData("9,3");
                        }
                    }

                }
                if(selected_number.equals("3")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("9,3")){
                            methodEmitData("9,3");
                        }
                    }

                }

                if(selected_number.equals("8")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("8,4")){
                            methodEmitData("8,4");
                        }
                    }

                }

                if(selected_number.equals("4")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("8,4")){
                            methodEmitData("8,4");
                        }
                    }


                }

                if(selected_number.equals("7")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("7,5")){
                            methodEmitData("7,5");
                        }
                    }

                }

                if(selected_number.equals("5")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("7,5")){
                            methodEmitData("7,5");
                        }
                    }
                }

                break;
        }
    }

    private void methodEmitData(String pair) {

        layout_dice_movement.setEnabled(true);

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
            jsonObject.put("player_id", Integer.parseInt(user_two_id));
            jsonObject.put("turn_id", 11);
            jsonObject.put("number", i);
            jsonObject.put("dice1_number", dice1_number);
            jsonObject.put("dice2_number", dice2_number);
            jsonObject.put("user_id_player_id", Integer.parseInt(unique_id));
            jsonObject.put("pair", pair);
            jsonObject.put("pos_color", "yellow");
            jsonObject.put("count_for_no_combination", count_for_no_combination);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("jsonDataIs", jsonObject.toString());

        socket.emit("playEvent", jsonObject.toString());

    }



    private void apiCallCounterForAds() {

        ApiInterface apiInterface = AppConfig.getRetrofit().create(ApiInterface.class);
        Call<GamePageCounterModel> call = apiInterface.gamepagecounter("Bearer "+token,SharedHelper.getKey(PlayVsPlayerSocket.this, UserConstant.id));
        call.enqueue(new Callback<GamePageCounterModel>() {
            @Override
            public void onResponse(Call<GamePageCounterModel> call, Response<GamePageCounterModel> response) {

                if(response.body().getStatus()){
                    counter=Integer.parseInt(response.body().getData().get(0).getCounter());
                    Log.i("CounterIs",counter+"");
                }

            }

            @Override
            public void onFailure(Call<GamePageCounterModel> call, Throwable t) {

                if (t instanceof SocketTimeoutException) {

                }

            }
        });

    }


    //full Screen ads

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }




    private void apiCallDoubleCoins(String msg){

        AdMobrewardedVideoAd
                = MobileAds.getRewardedVideoAdInstance(getApplicationContext());

        AdMobrewardedVideoAd.setRewardedVideoAdListener(
                new RewardedVideoAdListener() {
                    @Override
                    public void onRewardedVideoAdLoaded()
                    {

                        Toast.makeText(getApplicationContext(),
                                "onRewardedVideoAdLoaded",
                                Toast.LENGTH_SHORT)
                                .show();

                        showRewardedVideo(msg);

                    }

                    @Override
                    public void onRewardedVideoAdOpened()
                    {
                        // Showing Toast Message
                        Toast
                                .makeText(getApplicationContext(),
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


                    }

                    @Override
                    public void
                    onRewardedVideoAdLeftApplication()
                    {

                    }

                    @Override
                    public void onRewardedVideoAdFailedToLoad(
                            int i)
                    {

                    }

                    @Override
                    public void onRewardedVideoCompleted()
                    {

                    }
                });


        AdMobrewardedVideoAd.loadAd(
                AdId, new AdRequest.Builder().build());




    }




    private void showRewardedVideo(String msg) {
        // Checking If Ad is Loaded or Not
        if (AdMobrewardedVideoAd.isLoaded()) {
            // showing Video Ad
            AdMobrewardedVideoAd.show();

            apiCallAddDoubleCoins(msg);

        }
        else {
            // Loading Rewarded Video Ad
            AdMobrewardedVideoAd.loadAd(
                    AdId, new AdRequest.Builder().build());
        }
    }


    private void apiCallAddDoubleCoins(String msg) {

        int tokens;

        if(bet_status.trim().equals("true")){

            if(msg.trim().equals("You are Lose")){

                tokens=Integer.parseInt(bet_coins);

            }
            else {
                tokens=Integer.parseInt(bet_coins)*2+20;
            }


        }

        else {

            if(msg.trim().equals("You are Lose")){

                tokens=0;

            }
            else {
                tokens=20;
            }

        }

        ApiInterface apiInterface = AppConfig.getRetrofit().create(ApiInterface.class);
        Call<ModelAddDoubleCoins> call = apiInterface.add_coinpoints_by_userid(
                "Bearer " + token,
                SharedHelper.getKey(getApplicationContext(), UserConstant.id),
                String.valueOf(tokens),"0");

        call.enqueue(new Callback<ModelAddDoubleCoins>() {
            @Override
            public void onResponse(Call<ModelAddDoubleCoins> call, Response<ModelAddDoubleCoins> response) {

                if (response.isSuccessful()) {

                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();

                    if (response.body().getStatus()) {


                        apiCallGetCoinsAfterDouble();


//                        try {
//                            SharedHelper.putKey(getApplicationContext(),UserConstant.coin,response.body().getData().get(0).getTotal_coins());
//                            total_coin_txt.setText(SharedHelper.getKey(PlayVsPlayerSocket.this, UserConstant.coin));
//                            MainActivity.total_coin_txt.setText(SharedHelper.getKey(PlayVsPlayerSocket.this, UserConstant.coin));
//
//                            SharedHelper.putKey(getApplicationContext(),UserConstant.point,response.body().getData().get(0).getTotal_points());
//                            ttxt.setText(SharedHelper.getKey(PlayVsPlayerSocket.this, UserConstant.point));
//                            MainActivity.ttxt.setText(SharedHelper.getKey(PlayVsPlayerSocket.this, UserConstant.point));
//
//
//                        }catch (Exception e){
//
//                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<ModelAddDoubleCoins> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {

                }
            }
        });




    }


    private void apiCallGetCoinsAfterDouble() {

        ApiInterface apiInterface = AppConfig.getRetrofit().create(ApiInterface.class);

        Call<ModelGetCoinsPoints> call = apiInterface.get_coinpoints_by_userid(
                "Bearer " + token,
                SharedHelper.getKey(getApplicationContext(), UserConstant.id));

        call.enqueue(new Callback<ModelGetCoinsPoints>() {
            @Override
            public void onResponse(Call<ModelGetCoinsPoints> call, Response<ModelGetCoinsPoints> response) {

                if (response.isSuccessful()) {

                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();

                    if (response.body().getStatus()) {

                        finish();

                        try {
                            SharedHelper.putKey(getApplicationContext(),UserConstant.coin,response.body().getData().get(0).getCoins());
                            total_coin_txt.setText(SharedHelper.getKey(PlayVsPlayerSocket.this, UserConstant.coin));
                            MainActivity.total_coin_txt.setText(SharedHelper.getKey(PlayVsPlayerSocket.this, UserConstant.coin));
                        }catch (Exception e){

                        }


                    }
                }
            }
            @Override
            public void onFailure(Call<ModelGetCoinsPoints> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {

                }
            }
        });


    }





    private void apiCallForReview() {

        ApiInterface apiInterface = AppConfig.getRetrofit().create(ApiInterface.class);

        Call<ModelAddForReview> call = apiInterface.add_reviewdialog_by_userid(
                "Bearer " + token,
                SharedHelper.getKey(getApplicationContext(), UserConstant.id),
                "1");

        call.enqueue(new Callback<ModelAddForReview>() {
            @Override
            public void onResponse(Call<ModelAddForReview> call, Response<ModelAddForReview> response) {

                if (response.isSuccessful()) {

                   // Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();

                    if (response.body().getStatus()) {

                      apiCallGetReviewStatus();

                    }
                }
            }
            @Override
            public void onFailure(Call<ModelAddForReview> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {

                }
            }
        });


    }


    private void apiCallGetReviewStatus() {
        ApiInterface apiInterface = AppConfig.getRetrofit().create(ApiInterface.class);

        Call<ModelGetReview> call = apiInterface.get_reviewdialog_by_userid(
                "Bearer " + token,
                SharedHelper.getKey(getApplicationContext(), UserConstant.id));

        call.enqueue(new Callback<ModelGetReview>() {
            @Override
            public void onResponse(Call<ModelGetReview> call, Response<ModelGetReview> response) {

                if (response.isSuccessful()) {

                    if (response.body().getStatus()) {

                        showReviewDialog();

                    }


                }
            }
            @Override
            public void onFailure(Call<ModelGetReview> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {

                }
            }
        });
    }

    private void showReviewDialog() {

        final Dialog dialog = new Dialog(PlayVsPlayerSocket.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_for_review);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        RelativeLayout layout_double_coins=dialog.findViewById(R.id.layout_double_coins);


        layout_double_coins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }

            }
        });



        dialog.show();

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