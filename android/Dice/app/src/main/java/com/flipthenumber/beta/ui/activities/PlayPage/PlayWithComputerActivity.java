package com.flipthenumber.beta.ui.activities.PlayPage;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.flipthenumber.beta.Constants.UserConstant;
import com.flipthenumber.beta.Helper.PreLollipopSoundPool;
import com.flipthenumber.beta.Helper.SharedHelper;
import com.flipthenumber.beta.MainActivity;
import com.flipthenumber.beta.R;
import com.flipthenumber.beta.model.GamePageCounterModel;
import com.flipthenumber.beta.model.InviteToPlay;
import com.flipthenumber.beta.model.ModelAddCoins;
import com.flipthenumber.beta.retrofit.ApiInterface;
import com.flipthenumber.beta.retrofit.AppConfig;
import com.github.andreilisun.circular_layout.CircularLayout;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;

import java.lang.String;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayWithComputerActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.img_back)
    ImageView img_back;

    @BindView(R.id.layout_dice_movement)
    RelativeLayout layout_dice_movement;

    @BindView(R.id.imageView)
    ImageView dice_picture;
    @BindView(R.id.dice2_imageView)
    ImageView dice2_picture;

    @BindView(R.id.txt_one)
    TextView txt_one;
    @BindView(R.id.txt_two)
    TextView txt_two;
    @BindView(R.id.txt_three)
    TextView txt_three;
    @BindView(R.id.txt_four)
    TextView txt_four;
    @BindView(R.id.txt_five)
    TextView txt_five;
    @BindView(R.id.txt_six)
    TextView txt_six;
    @BindView(R.id.txt_seven)
    TextView txt_seven;
    @BindView(R.id.txt_eight)
    TextView txt_eight;
    @BindView(R.id.txt_nine)
    TextView txt_nine;

    @BindView(R.id.gif_img)
    GifImageView gif_img;
    @BindView(R.id.nameTxt)

    TextView nameTxt;
    Random rng = new Random();    //generate random numbers
    SoundPool dice_sound;       //For dice sound playing
    int sound_id;               //Used to control sound stream return by SoundPool
    Handler handler;            //Post message to start roll
    Timer timer = new Timer();    //Used to implement feedback to user
    boolean rolling = false;      //Is die rolling?
    RotateAnimation rotate;
    int soundplay;
    int dice1_number;
    int dice2_number;
    boolean isComputerTurn = false;
    boolean isLastTurn = false;
    AudioManager mAlramMAnager;
    private boolean VolIsMute;
    boolean isMusic;
    boolean isSound;
    @BindView(R.id.iv_profile_img)
    CircleImageView iv_profile_img;
    TextView zokar_txt,ttxt,total_coin_txt,ttxt_two,tv_name;
    boolean run =false;
    private LinearLayout layout_coin_two,layout_joker,layout_coins_one;
    ArrayList<String> arrayList_two,arrayList_three,
            arrayList_four,arrayList_five,arrayList_six,arrayList_seven,
            arrayList_eight,arrayList_nine,arrayList_ten,arrayList_eveven,arrayList_twelve,
            arrayListFilterData;
    AlertDialog.Builder builder;
    private String pair="";
    AlertDialog dialog;
    private CircleImageView iv_profile_center;
    TextView txt_one_com;
    TextView txt_two_com;
    TextView txt_three_com;
    TextView txt_four_com;
    TextView txt_five_com;
    TextView txt_six_com;
    TextView txt_seven_com;
    TextView txt_eight_com;
    TextView txt_nine_com;
    RelativeLayout layout_you_bottem,layout_com_bottem;
    TextView tv_name_you,tv_name_com;
    TextView txt_one_bottem;
    TextView txt_two_bottem;
    TextView txt_three_bottem;
    TextView txt_four_bottem;
    TextView txt_five_bottem;
    TextView txt_six_bottem;
    TextView txt_seven_bottem;
    TextView txt_eight_bottem;
    TextView txt_nine_bottem;
    TextView txt_one_bottem_com;
    TextView txt_two_bottem_com;
    TextView txt_three_bottem_com;
    TextView txt_four_bottem_com;
    TextView txt_five_bottem_com;
    TextView txt_six_bottem_com;
    TextView txt_seven_bottem_com;
    TextView txt_eight_bottem_com;
    TextView txt_nine_bottem_com;
    CircularLayout layout_circuler_com,layout_circuler_you;
    int count = 0;
    int count_re_roll = 0;
    private CircleImageView iv_profile_bottem_you;
    private int count_for_no_combination=0;
    private String token;

    private AdView adView;
    AdRequest adRequest;

    private ImageView shape_one_com,shape_two_com,shape_three_com,shape_four_com,shape_five_com,shape_six_com,shape_seven_com,shape_eight_com,shape_nine_com;
    private ImageView shape_one_you,shape_two_you,shape_three_you,shape_four_you,shape_five_you,shape_six_you,shape_seven_you,shape_eight_you,shape_nine_you;

    private ImageView shape_one_you_bottem,shape_two_you_bottem,shape_three_you_bottem,shape_four_you_bottem,shape_five_you_bottem,shape_six_you_bottem,shape_seven_you_bottem,shape_eight_you_bottem,shape_nine_you_bottem;

    private ImageView shape_one_com_bottem,shape_two_com_bottem,shape_three_com_bottem,shape_four_com_bottem,shape_five_com_bottem,shape_six_com_bottem,shape_seven_com_bottem,shape_eight_com_bottem,shape_nine_com_bottem;



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

    int i;

    int counter;

    ImageView plusImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_with_computer);
        mAlramMAnager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        VolIsMute = false;

        init();
        initUI();
        InitArrayListCombinations();
        InitSound();

        setLeagueLedgeForPoints();


        //  apiCallCounterForAds();

    }



    private void InitArrayListCombinations() {

        arrayList_two=new ArrayList<>();
        arrayList_three=new ArrayList<>();
        arrayList_four=new ArrayList<>();
        arrayList_five=new ArrayList<>();
        arrayList_six=new ArrayList<>();
        arrayList_seven=new ArrayList<>();
        arrayList_eight=new ArrayList<>();
        arrayList_nine=new ArrayList<>();
        arrayList_ten=new ArrayList<>();
        arrayList_eveven=new ArrayList<>();
        arrayList_twelve=new ArrayList<>();
        arrayListFilterData=new ArrayList<>();

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

    private void init()
    {

        ButterKnife.bind(this);
        isMusic = SharedHelper.getBooleanKey(PlayWithComputerActivity.this, UserConstant.isMusic);
        isSound = SharedHelper.getBooleanKey(PlayWithComputerActivity.this, UserConstant.isSound);

        zokar_txt=findViewById(R.id.zokar_txt);
        ttxt=findViewById(R.id.ttxt);
        total_coin_txt=findViewById(R.id.total_coin_txt);
        ttxt_two=findViewById(R.id.ttxt_two);

        zokar_txt.setText(SharedHelper.getKey(PlayWithComputerActivity.this, UserConstant.zokar));
        ttxt.setText(SharedHelper.getKey(PlayWithComputerActivity.this, UserConstant.point));
        total_coin_txt.setText(SharedHelper.getKey(PlayWithComputerActivity.this, UserConstant.coin));

        layout_coin_two=findViewById(R.id.layout_coin_two);
        layout_coins_one=findViewById(R.id.layout_coins_one);
        layout_joker=findViewById(R.id.layout_joker);

        builder = new AlertDialog.Builder(PlayWithComputerActivity.this);
        builder.setTitle("Dice Combinations");
        builder.setCancelable(false);

        iv_profile_center=findViewById(R.id.iv_profile_center);


        txt_one_com=findViewById(R.id.txt_one_com);
     txt_two_com=findViewById(R.id.txt_two_com);
     txt_three_com=findViewById(R.id.txt_three_com);
     txt_four_com=findViewById(R.id.txt_four_com);
     txt_five_com=findViewById(R.id.txt_five_com);
     txt_six_com=findViewById(R.id.txt_six_com);
     txt_seven_com=findViewById(R.id.txt_seven_com);
     txt_eight_com=findViewById(R.id.txt_eight_com);
     txt_nine_com=findViewById(R.id.txt_nine_com);

     layout_you_bottem=findViewById(R.id.layout_you_bottem);
    layout_com_bottem=findViewById(R.id.layout_com_bottem);

    tv_name_you=findViewById(R.id.tv_name_you);
    tv_name_com=findViewById(R.id.tv_name_com);

     txt_one_bottem=findViewById(R.id.txt_one_bottem);
     txt_two_bottem=findViewById(R.id.txt_two_bottem);
     txt_three_bottem=findViewById(R.id.txt_three_bottem);
     txt_four_bottem=findViewById(R.id.txt_four_bottem);
     txt_five_bottem=findViewById(R.id.txt_five_bottem);
     txt_six_bottem=findViewById(R.id.txt_six_bottem);
     txt_seven_bottem=findViewById(R.id.txt_seven_bottem);
     txt_eight_bottem=findViewById(R.id.txt_eight_bottem);
     txt_nine_bottem=findViewById(R.id.txt_nine_bottem);

     txt_one_bottem_com=findViewById(R.id.txt_one_bottem_com);
     txt_two_bottem_com=findViewById(R.id.txt_two_bottem_com);
     txt_three_bottem_com=findViewById(R.id.txt_three_bottem_com);
     txt_four_bottem_com=findViewById(R.id.txt_four_bottem_com);
     txt_five_bottem_com=findViewById(R.id.txt_five_bottem_com);
     txt_six_bottem_com=findViewById(R.id.txt_six_bottem_com);
     txt_seven_bottem_com=findViewById(R.id.txt_seven_bottem_com);
     txt_eight_bottem_com=findViewById(R.id.txt_eight_bottem_com);
     txt_nine_bottem_com=findViewById(R.id.txt_nine_bottem_com);

     layout_circuler_com=findViewById(R.id.layout_circuler_com);
     layout_circuler_you=findViewById(R.id.layout_circuler_you);

     iv_profile_bottem_you=findViewById(R.id.iv_profile_bottem_you);
        adsMethod();



        shape_one_com=findViewById(R.id.shape_one_com);
        shape_two_com=findViewById(R.id.shape_two_com);
        shape_three_com=findViewById(R.id.shape_three_com);
        shape_four_com=findViewById(R.id.shape_four_com);
        shape_five_com=findViewById(R.id.shape_five_com);
        shape_six_com=findViewById(R.id.shape_six_com);
        shape_seven_com=findViewById(R.id.shape_seven_com);
        shape_eight_com=findViewById(R.id.shape_eight_com);
        shape_nine_com=findViewById(R.id.shape_nine_com);


        shape_one_you=findViewById(R.id.shape_one_you);
        shape_two_you=findViewById(R.id.shape_two_you);
        shape_three_you=findViewById(R.id.shape_three_you);
        shape_four_you=findViewById(R.id.shape_four_you);
        shape_five_you=findViewById(R.id.shape_five_you);
        shape_six_you=findViewById(R.id.shape_six_you);
        shape_seven_you=findViewById(R.id.shape_seven_you);
        shape_eight_you=findViewById(R.id.shape_eight_you);
        shape_nine_you=findViewById(R.id.shape_nine_you);


        shape_one_com_bottem=findViewById(R.id.shape_one_com_bottem);
        shape_two_com_bottem=findViewById(R.id.shape_two_com_bottem);
        shape_three_com_bottem=findViewById(R.id.shape_three_com_bottem);
        shape_four_com_bottem=findViewById(R.id.shape_four_com_bottem);
        shape_five_com_bottem=findViewById(R.id.shape_five_com_bottem);
        shape_six_com_bottem=findViewById(R.id.shape_six_com_bottem);
        shape_seven_com_bottem=findViewById(R.id.shape_seven_com_bottem);
        shape_eight_com_bottem=findViewById(R.id.shape_eight_com_bottem);
        shape_nine_com_bottem=findViewById(R.id.shape_nine_com_bottem);


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


        String img=SharedHelper.getKey(getApplicationContext(), UserConstant.userImage);
        Log.i("image_user_is","img>> "+img);

        if(!img.trim().equals("")){
            Picasso.get().load(SharedHelper.getKey(getApplicationContext(), UserConstant.userImage)).into(iv_profile_img);
            Picasso.get().load(SharedHelper.getKey(getApplicationContext(), UserConstant.userImage)).into(iv_profile_bottem_you);

        }


        layout_dice_movement.setOnClickListener(new HandleClick());
        nameTxt.setText(SharedHelper.getKey(PlayWithComputerActivity.this, UserConstant.name));
        img_back.setOnClickListener(this);

        token = SharedHelper.getKey(getApplicationContext(), UserConstant.userToken);

        rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(800);
        rotate.setInterpolator(new LinearInterpolator());

        //link handler to callback
        handler = new Handler(callback);
        Random random = new Random();
        int i = random.nextInt(2);

        if (i == 0) {
            displayMessage("Computer Turn");

            iv_profile_center.setVisibility(View.VISIBLE);

            methodVisibleGoneWhenComputerTurn();

            computerTurn();

        }

        else {

            gif_img.setVisibility(View.VISIBLE);
            displayMessage("Your Turn");
            iv_profile_center.setVisibility(View.GONE);


            methodVisibleGoneWhenMyTurn();

        }


        layout_coin_two.setOnClickListener(this);
        layout_coins_one.setOnClickListener(this);
        layout_joker.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.img_back:

                showExitDialog();

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
                    timer.schedule(new Roll(), 1100);
                }catch (Exception e){

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

        }

        else {
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

    private void setExitDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure ? You want to exit game!.");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                    }
                });
        alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
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


            rolling = false;

            i = dice1_number + dice2_number;

            if(i==2){

                Log.i("arrayList",arrayList_two.toString());

                arrayListFilterData=new ArrayList<>();

                for (int s=0;s<arrayList_two.size();s++){

                    Log.i("ArrayListData",arrayList_two.get(s));


                    if(arrayList_two.get(s).equals("2")) {


                        if(isComputerTurn)
                       {

                           if (txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                               //arrayList_two.remove(s);
                           }
                           else {
                               arrayListFilterData.add(arrayList_two.get(s));
                           }

                       }

                       else
                           {

                           if (txt_two_com.getCurrentTextColor() != getResources().getColor(R.color.colorWhite)) {
                               //arrayList_two.remove(s);
                           }
                           else {
                               arrayListFilterData.add(arrayList_two.get(s));
                           }

                           }



                    }

                }


                new Handler().postDelayed(new Runnable() {

                    @Override

                    public void run() {


                        if(arrayListFilterData.size()>0)
                        {
                            if(count_for_no_combination==1){
                                methodCountAdd();
                            }


                            String[] str = GetStringArray(arrayListFilterData);


                            for(int i=0;i<arrayListFilterData.size();i++){
                                if(arrayListFilterData.get(i).equals("2")){
                                    shape_two_you.setImageResource(resIdTwo[0]);
                                }
                            }


                            builder.setItems(str, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Toast.makeText(getApplicationContext(),str[which],Toast.LENGTH_LONG).show();

                                        Log.i("SelectedPair",str[which]);

                                        pair=str[which];

                                        setPairData(pair,1,"you");

                                    }

                                });

                             dialog = builder.create();


                             if (isComputerTurn) {

                                //dialog.show();
                                mehtodGravityForAlertDialog();

                             }

                            else {

                                pair=str[0];

                                setPairData(pair,0,"com");

                            }

                        }

                        else {

                            count_for_no_combination=count_for_no_combination+1;

                            if(isComputerTurn){
                                setPairData("",1,"you");
                            }else {
                                setPairData("",0,"com");
                            }

                            Toast.makeText(getApplicationContext(),"No Combination Possible",Toast.LENGTH_LONG).show();
                        }


                    }

                }, 1000);

             }

            else if(i==3)
            {
                Log.i("arrayList",arrayList_three.toString());

                arrayListFilterData=new ArrayList<>();

                for (int s=0;s<arrayList_three.size();s++){
                    Log.i("ArrayListData",arrayList_three.get(s));

                    if(arrayList_three.get(s).equals("3")){


                        if(isComputerTurn){
                            if (txt_three.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                                //arrayList_two.remove(s);
                            }
                            else {
                                arrayListFilterData.add(arrayList_three.get(s));
                            }
                        }
                        else {
                            if (txt_three_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                                //arrayList_two.remove(s);
                            }
                            else {
                                arrayListFilterData.add(arrayList_three.get(s));
                            }
                        }





                    }
                    else if(arrayList_three.get(s).equals("2,1")){

                        if(isComputerTurn){
                            if (txt_two.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)
                                    || txt_one.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                                // arrayList_two.remove(s);
                            }
                            else {
                                arrayListFilterData.add(arrayList_three.get(s));
                            }
                        }
                        else {
                            if (txt_two_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)
                                    || txt_one_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                                // arrayList_two.remove(s);
                            }
                            else {
                                arrayListFilterData.add(arrayList_three.get(s));
                            }
                        }

                    }


                }


                new Handler().postDelayed(new Runnable() {


                    @Override

                    public void run() {



                        if(arrayListFilterData.size()>0)
                        {

                            if(count_for_no_combination==1){
                                methodCountAdd();
                            }

                            String[] str = GetStringArray(arrayListFilterData);


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

                                    Toast.makeText(getApplicationContext(),str[which],Toast.LENGTH_LONG).show();

                                    Log.i("SelectedPair",str[which]);

                                    pair=str[which];

                                    setPairData(pair,1,"you");

                                }
                            });

                            dialog = builder.create();


                            if (isComputerTurn) {

                                //dialog.show();
                                mehtodGravityForAlertDialog();

                            }
                            else {

                                pair=str[0];
                                setPairData(pair,0,"com");

                            }




                        }
                        else {
                            count_for_no_combination=count_for_no_combination+1;
                            if(isComputerTurn){
                                setPairData("",1,"you");
                            }else {
                                setPairData("",0,"com");
                            }
                            Toast.makeText(getApplicationContext(),"No Combination Possible",Toast.LENGTH_LONG).show();
                        }



                    }

                }, 1000);






            }
            else if(i==4)
            {
                Log.i("arrayList",arrayList_four.toString());

                arrayListFilterData=new ArrayList<>();

                for (int s=0;s<arrayList_four.size();s++){
                    Log.i("ArrayListData",arrayList_four.get(s));

                    if(arrayList_four.get(s).equals("4")){

                        if(isComputerTurn){
                            if (txt_four.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                                // arrayList_four.remove(s);
                            }
                            else {
                                arrayListFilterData.add(arrayList_four.get(s));
                            }
                        }
                        else {
                            if (txt_four_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                                // arrayList_four.remove(s);
                            }
                            else {
                                arrayListFilterData.add(arrayList_four.get(s));
                            }
                        }

                    }
                    else if(arrayList_four.get(s).equals("3,1")){

                        if(isComputerTurn){

                            if (txt_three.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)
                                    || txt_one.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                                // arrayList_four.remove(s);
                            }
                            else {
                                arrayListFilterData.add(arrayList_four.get(s));
                            }
                        }
                        else {
                            if (txt_three_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)
                                    || txt_one_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                                // arrayList_four.remove(s);
                            }
                            else {
                                arrayListFilterData.add(arrayList_four.get(s));
                            }

                        }

                    }

                }

                new Handler().postDelayed(new Runnable() {

                    @Override

                    public void run() {

                        if(arrayListFilterData.size()>0)
                        {

                            if(count_for_no_combination==1){
                                methodCountAdd();
                            }

                            String[] str = GetStringArray(arrayListFilterData);


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

                                    Toast.makeText(getApplicationContext(),str[which],Toast.LENGTH_LONG).show();

                                    Log.i("SelectedPair",str[which]);

                                    pair=str[which];

                                    setPairData(pair,1,"you");


                                }
                            });

                            dialog = builder.create();



                            if (isComputerTurn) {

                                //dialog.show();
                                mehtodGravityForAlertDialog();

                            }
                            else {

                                pair=str[0];
                                setPairData(pair,0,"com");

                            }




                        }
                        else {
                            count_for_no_combination=count_for_no_combination+1;
                            if(isComputerTurn){
                                setPairData("",1,"you");
                            }else {
                                setPairData("",0,"com");
                            }
                            Toast.makeText(getApplicationContext(),"No Combination Possible",Toast.LENGTH_LONG).show();
                        }



                    }

                }, 1000);








            }

            else if(i==5)
            {
                Log.i("arrayList",arrayList_five.toString());

                arrayListFilterData=new ArrayList<>();

                for (int s=0;s<arrayList_five.size();s++){
                    Log.i("ArrayListData",arrayList_five.get(s));

                    if(arrayList_five.get(s).equals("5")){

                        if(isComputerTurn){
                            if (txt_five.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                                // arrayList_five.remove(s);
                            }
                            else {
                                arrayListFilterData.add(arrayList_five.get(s));
                            }
                        }else {
                            if (txt_five_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                                // arrayList_five.remove(s);
                            }
                            else {
                                arrayListFilterData.add(arrayList_five.get(s));
                            }
                        }



                    }
                    else if(arrayList_five.get(s).equals("4,1")){

                        if(isComputerTurn){
                            if (txt_four.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)
                                    || txt_one.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                                //arrayList_five.remove(s);
                            }
                            else {
                                arrayListFilterData.add(arrayList_five.get(s));
                            }
                        }
                        else {
                            if (txt_four_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)
                                    || txt_one_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                                //arrayList_five.remove(s);
                            }
                            else {
                                arrayListFilterData.add(arrayList_five.get(s));
                            }
                        }



                    }

                    else if(arrayList_five.get(s).equals("3,2")){


                        if(isComputerTurn){
                            if (txt_three.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)
                                    || txt_two.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                                // arrayList_five.remove(s);
                            }
                            else {
                                arrayListFilterData.add(arrayList_five.get(s));
                            }
                        }else {
                            if (txt_three_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)
                                    || txt_two_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                                // arrayList_five.remove(s);
                            }
                            else {
                                arrayListFilterData.add(arrayList_five.get(s));
                            }
                        }




                    }



                }


                new Handler().postDelayed(new Runnable() {


                    @Override

                    public void run() {



                        if(arrayListFilterData.size()>0)
                        {

                            if(count_for_no_combination==1){
                                methodCountAdd();
                            }

                            String[] str = GetStringArray(arrayListFilterData);


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




                                    Toast.makeText(getApplicationContext(),str[which],Toast.LENGTH_LONG).show();

                                    Log.i("SelectedPair",str[which]);

                                    pair=str[which];

                                    setPairData(pair,1,"you");


                                }
                            });

                            dialog = builder.create();



                            if (isComputerTurn) {

                               // dialog.show();
                                mehtodGravityForAlertDialog();

                            }
                            else {

                                pair=str[0];
                                setPairData(pair,0,"com");

                            }




                        }
                        else {
                            count_for_no_combination=count_for_no_combination+1;
                            if(isComputerTurn){
                                setPairData("",1,"you");
                            }else {
                                setPairData("",0,"com");
                            }
                            Toast.makeText(getApplicationContext(),"No Combination Possible",Toast.LENGTH_LONG).show();
                        }



                    }

                }, 1000);






            }
            else if(i==6)
            {
                Log.i("arrayList",arrayList_six.toString());

                arrayListFilterData=new ArrayList<>();
                for (int s=0;s<arrayList_six.size();s++){
                    Log.i("ArrayListData",arrayList_six.get(s));

                    if(arrayList_six.get(s).equals("6")){

                        if(isComputerTurn){
                            if (txt_six.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                                //arrayList_six.remove(s);
                            }
                            else {
                                arrayListFilterData.add(arrayList_six.get(s));
                            }
                        }else {
                            if (txt_six_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                                //arrayList_six.remove(s);
                            }
                            else {
                                arrayListFilterData.add(arrayList_six.get(s));
                            }
                        }



                    }
                    else if(arrayList_six.get(s).equals("5,1")){

                        if(isComputerTurn){
                            if (txt_five.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)
                                    || txt_one.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                                //arrayList_six.remove(s);
                            }
                            else {
                                arrayListFilterData.add(arrayList_six.get(s));
                            }
                        }else {
                            if (txt_five_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)
                                    || txt_one_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                                //arrayList_six.remove(s);
                            }
                            else {
                                arrayListFilterData.add(arrayList_six.get(s));
                            }
                        }

                    }
                    else if(arrayList_six.get(s).equals("4,2")){

                        if(isComputerTurn){
                            if (txt_four.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)
                                    || txt_two.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                                // arrayList_six.remove(s);
                            }
                            else {
                                arrayListFilterData.add(arrayList_six.get(s));
                            }
                        }
                        else {
                            if (txt_four_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)
                                    || txt_two_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                                // arrayList_six.remove(s);
                            }
                            else {
                                arrayListFilterData.add(arrayList_six.get(s));
                            }
                        }



                    }

                }
                new Handler().postDelayed(new Runnable() {


                    @Override

                    public void run() {


                        if(arrayListFilterData.size()>0)
                        {

                            if(count_for_no_combination==1){
                                methodCountAdd();
                            }

                            String[] str = GetStringArray(arrayListFilterData);



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




                                    Toast.makeText(getApplicationContext(),str[which],Toast.LENGTH_LONG).show();

                                    Log.i("SelectedPair",str[which]);

                                    pair=str[which];

                                    setPairData(pair,1,"you");


                                }
                            });

                            dialog = builder.create();



                            if (isComputerTurn) {

                               // dialog.show();
                                mehtodGravityForAlertDialog();

                            }
                            else {

                                pair=str[0];
                                setPairData(pair,0,"com");

                            }




                        }
                        else {
                            count_for_no_combination=count_for_no_combination+1;
                            if(isComputerTurn){
                                setPairData("",1,"you");
                            }else {
                                setPairData("",0,"com");
                            }
                            Toast.makeText(getApplicationContext(),"No Combination Possible",Toast.LENGTH_LONG).show();
                        }




                    }

                }, 1000);





            }
            else if(i==7)
            {
                Log.i("arrayList",arrayList_seven.toString());

                arrayListFilterData=new ArrayList<>();

                for (int s=0;s<arrayList_seven.size();s++){
                    Log.i("ArrayListData",arrayList_seven.get(s));

                    if(arrayList_seven.get(s).equals("7")){

                        if(isComputerTurn){
                            if (txt_seven.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                                //arrayList_seven.remove(s);
                            }
                            else {
                                arrayListFilterData.add(arrayList_seven.get(s));
                            }
                        }
                        else {
                            if (txt_seven_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                                //arrayList_seven.remove(s);
                            }
                            else {
                                arrayListFilterData.add(arrayList_seven.get(s));
                            }
                        }



                    }
                    else if(arrayList_seven.get(s).equals("6,1")){

                        if(isComputerTurn){
                            if (txt_six.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)
                                    || txt_one.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                                //arrayList_seven.remove(s);
                            }
                            else {
                                arrayListFilterData.add(arrayList_seven.get(s));
                            }
                        }
                        else {
                            if (txt_six_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)
                                    || txt_one_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                                //arrayList_seven.remove(s);
                            }
                            else {
                                arrayListFilterData.add(arrayList_seven.get(s));
                            }
                        }


                    }
                    else if(arrayList_seven.get(s).equals("5,2")){
                        if(isComputerTurn){
                            if (txt_five.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)
                                    || txt_two.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                                //arrayList_seven.remove(s);
                            }
                            else {
                                arrayListFilterData.add(arrayList_seven.get(s));
                            }
                        }else {
                            if (txt_five_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)
                                    || txt_two_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                                //arrayList_seven.remove(s);
                            }
                            else {
                                arrayListFilterData.add(arrayList_seven.get(s));
                            }
                        }


                    }

                    else if(arrayList_seven.get(s).equals("4,3")){

                        if(isComputerTurn){
                            if (txt_four.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)
                                    || txt_three.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                                // arrayList_seven.remove(s);
                            }
                            else {
                                arrayListFilterData.add(arrayList_seven.get(s));
                            }
                        }
                        else {
                            if (txt_four_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)
                                    || txt_three_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                                // arrayList_seven.remove(s);
                            }
                            else {
                                arrayListFilterData.add(arrayList_seven.get(s));
                            }
                        }





                    }


                }


                new Handler().postDelayed(new Runnable() {


                    @Override

                    public void run() {


                        if(arrayListFilterData.size()>0)
                        {

                            if(count_for_no_combination==1){
                                methodCountAdd();
                            }

                            String[] str = GetStringArray(arrayListFilterData);

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




                                    Toast.makeText(getApplicationContext(),str[which],Toast.LENGTH_LONG).show();

                                    Log.i("SelectedPair",str[which]);

                                    pair=str[which];

                                    setPairData(pair,1,"you");


                                }
                            });

                            dialog = builder.create();



                            if (isComputerTurn) {

                               // dialog.show();
                                mehtodGravityForAlertDialog();

                            }
                            else {

                                pair=str[0];
                                setPairData(pair,0,"com");

                            }




                        }
                        else {
                            count_for_no_combination=count_for_no_combination+1;
                            if(isComputerTurn){
                                setPairData("",1,"you");
                            }else {
                                setPairData("",0,"com");
                            }
                            Toast.makeText(getApplicationContext(),"No Combination Possible",Toast.LENGTH_LONG).show();
                        }




                    }

                }, 1000);




            }

            else if(i==8)
            {
                Log.i("arrayList",arrayList_eight.toString());

                arrayListFilterData=new ArrayList<>();
                for (int s=0;s<arrayList_eight.size();s++){
                    Log.i("ArrayListData",arrayList_eight.get(s));

                    if(arrayList_eight.get(s).equals("8")){



                        if(isComputerTurn){
                            if (txt_eight.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                                //arrayList_eight.remove(s);
                            }
                            else {
                                arrayListFilterData.add(arrayList_eight.get(s));
                            }
                        }
                        else {
                            if (txt_eight_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                                //arrayList_eight.remove(s);
                            }
                            else {
                                arrayListFilterData.add(arrayList_eight.get(s));
                            }
                        }




                    }
                    else if(arrayList_eight.get(s).equals("7,1")){

                        if(isComputerTurn){
                            if (txt_seven.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)
                                    || txt_one.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                                // arrayList_eight.remove(s);
                            }
                            else {
                                arrayListFilterData.add(arrayList_eight.get(s));
                            }
                        }
                        else {
                            if (txt_seven_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)
                                    || txt_one_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                                // arrayList_eight.remove(s);
                            }
                            else {
                                arrayListFilterData.add(arrayList_eight.get(s));
                            }
                        }





                    }
                    else if(arrayList_eight.get(s).equals("6,2")){

                        if(isComputerTurn){
                            if (txt_six.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)
                                    || txt_two.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                                // arrayList_eight.remove(s);
                            }
                            else {
                                arrayListFilterData.add(arrayList_eight.get(s));
                            }
                        }
                        else {
                            if (txt_six_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)
                                    || txt_two_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                                // arrayList_eight.remove(s);
                            }
                            else {
                                arrayListFilterData.add(arrayList_eight.get(s));
                            }
                        }




                    }

                    else if(arrayList_eight.get(s).equals("5,3")){

                        if(isComputerTurn){
                            if (txt_five.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)
                                    || txt_three.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                                //arrayList_eight.remove(s);
                            }
                            else {
                                arrayListFilterData.add(arrayList_eight.get(s));
                            }
                        }
                        else {
                            if (txt_five_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)
                                    || txt_three_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                                //arrayList_eight.remove(s);
                            }
                            else {
                                arrayListFilterData.add(arrayList_eight.get(s));
                            }
                        }




                    }

                }


                new Handler().postDelayed(new Runnable() {


                    @Override

                    public void run() {


                        if(arrayListFilterData.size()>0)
                        {

                            if(count_for_no_combination==1){
                                methodCountAdd();
                            }

                            String[] str = GetStringArray(arrayListFilterData);


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




                                    Toast.makeText(getApplicationContext(),str[which],Toast.LENGTH_LONG).show();

                                    Log.i("SelectedPair",str[which]);

                                    pair=str[which];

                                    setPairData(pair,1,"you");


                                }
                            });

                            dialog = builder.create();



                            if (isComputerTurn) {

                               // dialog.show();
                                mehtodGravityForAlertDialog();

                            }
                            else {

                                pair=str[0];
                                setPairData(pair,0,"com");

                            }




                        }
                        else {
                            count_for_no_combination=count_for_no_combination+1;
                            if(isComputerTurn){
                                setPairData("",1,"you");
                            }else {
                                setPairData("",0,"com");
                            }
                            Toast.makeText(getApplicationContext(),"No Combination Possible",Toast.LENGTH_LONG).show();
                        }




                    }

                }, 1000);






            }
            else if(i==9)
            {
                Log.i("arrayList",arrayList_nine.toString());

                arrayListFilterData=new ArrayList<>();

                for (int s=0;s<arrayList_nine.size();s++){
                    Log.i("ArrayListData",arrayList_nine.get(s));

                    if(arrayList_nine.get(s).equals("9")){


                        if(isComputerTurn){
                            if (txt_nine.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                                //arrayList_nine.remove(s);
                            }
                            else {
                                arrayListFilterData.add(arrayList_nine.get(s));
                            }
                        }
                        else {
                            if (txt_nine_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                                //arrayList_nine.remove(s);
                            }
                            else {
                                arrayListFilterData.add(arrayList_nine.get(s));
                            }
                        }




                    }
                    else if(arrayList_nine.get(s).equals("8,1")){


                        if(isComputerTurn){
                            if (txt_eight.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)
                                    || txt_one.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                                //arrayList_nine.remove(s);
                            }
                            else {
                                arrayListFilterData.add(arrayList_nine.get(s));
                            }
                        }
                        else {
                            if (txt_eight_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)
                                    || txt_one_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                                //arrayList_nine.remove(s);
                            }
                            else {
                                arrayListFilterData.add(arrayList_nine.get(s));
                            }
                        }




                    }
                    else if(arrayList_nine.get(s).equals("7,2")){


                        if(isComputerTurn){
                            if (txt_seven.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)
                                    || txt_two.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                                // arrayList_nine.remove(s);
                            }
                            else {
                                arrayListFilterData.add(arrayList_nine.get(s));
                            }
                        }
                        else {
                            if (txt_seven_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)
                                    || txt_two_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                                // arrayList_nine.remove(s);
                            }
                            else {
                                arrayListFilterData.add(arrayList_nine.get(s));
                            }
                        }




                    }

                    else if(arrayList_nine.get(s).equals("6,3")){

                        if(isComputerTurn){
                            if (txt_six.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)
                                    || txt_three.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                                //arrayList_nine.remove(s);
                            }
                            else {
                                arrayListFilterData.add(arrayList_nine.get(s));
                            }
                        }
                        else {
                            if (txt_six_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)
                                    || txt_three_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                                //arrayList_nine.remove(s);
                            }
                            else {
                                arrayListFilterData.add(arrayList_nine.get(s));
                            }
                        }



                    }

                    else if(arrayList_nine.get(s).equals("5,4")){

                        if(isComputerTurn){
                            if (txt_five.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)
                                    || txt_four.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                                //arrayList_nine.remove(s);
                            }
                            else {
                                arrayListFilterData.add(arrayList_nine.get(s));
                            }
                        }
                        else {
                            if (txt_five_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)
                                    || txt_four_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                                //arrayList_nine.remove(s);
                            }
                            else {
                                arrayListFilterData.add(arrayList_nine.get(s));
                            }
                        }





                    }


                }

                new Handler().postDelayed(new Runnable() {


                    @Override

                    public void run() {


                        if(arrayListFilterData.size()>0)
                        {

                            if(count_for_no_combination==1){
                                methodCountAdd();
                            }

                            String[] str = GetStringArray(arrayListFilterData);


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




                                    Toast.makeText(getApplicationContext(),str[which],Toast.LENGTH_LONG).show();

                                    Log.i("SelectedPair",str[which]);

                                    pair=str[which];

                                    setPairData(pair,1,"you");


                                }
                            });

                            dialog = builder.create();



                            if (isComputerTurn) {

                               // dialog.show();
                                mehtodGravityForAlertDialog();

                            }
                            else {

                                pair=str[0];
                                setPairData(pair,0,"com");

                            }




                        }
                        else {
                            count_for_no_combination=count_for_no_combination+1;
                            if(isComputerTurn){
                                setPairData("",1,"you");
                            }else {
                                setPairData("",0,"com");
                            }
                            Toast.makeText(getApplicationContext(),"No Combination Possible",Toast.LENGTH_LONG).show();
                        }




                    }

                }, 1000);





            }
            else if(i==10)
            {
                Log.i("arrayList",arrayList_ten.toString());

                arrayListFilterData=new ArrayList<>();

                for (int s=0;s<arrayList_ten.size();s++){
                    Log.i("ArrayListData",arrayList_ten.get(s));

                  if(arrayList_ten.get(s).equals("9,1")){

                      if(isComputerTurn){
                          if (txt_nine.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)
                                  || txt_one.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                              // arrayList_ten.remove(s);
                          }
                          else {
                              arrayListFilterData.add(arrayList_ten.get(s));
                          }
                      }
                      else {
                          if (txt_nine_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)
                                  || txt_one_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                              // arrayList_ten.remove(s);
                          }
                          else {
                              arrayListFilterData.add(arrayList_ten.get(s));
                          }
                      }



                    }
                    else if(arrayList_ten.get(s).equals("8,2")){

                      if(isComputerTurn){
                          if (txt_eight.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)
                                  || txt_two.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                              //arrayList_ten.remove(s);
                          }
                          else {
                              arrayListFilterData.add(arrayList_ten.get(s));
                          }
                      }
                      else {
                          if (txt_eight_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)
                                  || txt_two_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                              //arrayList_ten.remove(s);
                          }
                          else {
                              arrayListFilterData.add(arrayList_ten.get(s));
                          }
                      }




                    }

                    else if(arrayList_ten.get(s).equals("7,3")){

                      if(isComputerTurn){
                          if (txt_seven.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)
                                  || txt_three.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                              // arrayList_ten.remove(s);
                          }
                          else {
                              arrayListFilterData.add(arrayList_ten.get(s));
                          }
                      }
                      else {
                          if (txt_seven_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)
                                  || txt_three_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                              // arrayList_ten.remove(s);
                          }
                          else {
                              arrayListFilterData.add(arrayList_ten.get(s));
                          }
                      }



                    }

                    else if(arrayList_ten.get(s).equals("6,4")){
                      if(isComputerTurn){
                          if (txt_six.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)
                                  || txt_four.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                              //arrayList_ten.remove(s);
                          }
                          else {
                              arrayListFilterData.add(arrayList_ten.get(s));
                          }
                      }
                      else {
                          if (txt_six_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)
                                  || txt_four_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                              //arrayList_ten.remove(s);
                          }
                          else {
                              arrayListFilterData.add(arrayList_ten.get(s));
                          }
                      }




                    }


                }



                new Handler().postDelayed(new Runnable() {


                    @Override

                    public void run() {



                        if(arrayListFilterData.size()>0)
                        {

                            if(count_for_no_combination==1){
                                methodCountAdd();
                            }

                            String[] str = GetStringArray(arrayListFilterData);


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




                                    Toast.makeText(getApplicationContext(),str[which],Toast.LENGTH_LONG).show();

                                    Log.i("SelectedPair",str[which]);

                                    pair=str[which];

                                    setPairData(pair,1,"you");


                                }
                            });

                            dialog = builder.create();



                            if (isComputerTurn) {

                               // dialog.show();
                                mehtodGravityForAlertDialog();

                            }
                            else {

                                pair=str[0];
                                setPairData(pair,0,"com");

                            }




                        }
                        else {
                            count_for_no_combination=count_for_no_combination+1;
                            if(isComputerTurn){
                                setPairData("",1,"you");
                            }else {
                                setPairData("",0,"com");
                            }
                            Toast.makeText(getApplicationContext(),"No Combination Possible",Toast.LENGTH_LONG).show();
                        }



                    }

                }, 1000);






            }
            else if(i==11)
            {
                Log.i("arrayList",arrayList_eveven.toString());

                arrayListFilterData=new ArrayList<>();

                for (int s=0;s<arrayList_eveven.size();s++){
                    Log.i("ArrayListData",arrayList_eveven.get(s));

                    if(arrayList_eveven.get(s).equals("9,2")){

                        if(isComputerTurn){
                            if (txt_nine.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)
                                    || txt_two.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                                //arrayList_eveven.remove(s);
                            }
                            else {
                                arrayListFilterData.add(arrayList_eveven.get(s));
                            }
                        }
                        else {
                            if (txt_nine_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)
                                    || txt_two_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                                //arrayList_eveven.remove(s);
                            }
                            else {
                                arrayListFilterData.add(arrayList_eveven.get(s));
                            }
                        }



                    }
                    else if(arrayList_eveven.get(s).equals("8,3")){
                        if(isComputerTurn){
                            if (txt_eight.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)
                                    || txt_three.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                                // arrayList_eveven.remove(s);
                            }
                            else {
                                arrayListFilterData.add(arrayList_eveven.get(s));
                            }
                        }
                        else {
                            if (txt_eight_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)
                                    || txt_three_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                                // arrayList_eveven.remove(s);
                            }
                            else {
                                arrayListFilterData.add(arrayList_eveven.get(s));
                            }
                        }




                    }

                    else if(arrayList_eveven.get(s).equals("7,4")){
                        if(isComputerTurn){
                            if (txt_seven.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)
                                    || txt_four.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                                // arrayList_eveven.remove(s);
                            }
                            else {
                                arrayListFilterData.add(arrayList_eveven.get(s));
                            }
                        }
                        else {
                            if (txt_seven_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)
                                    || txt_four_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                                // arrayList_eveven.remove(s);
                            }
                            else {
                                arrayListFilterData.add(arrayList_eveven.get(s));
                            }
                        }


                    }

                    else if(arrayList_eveven.get(s).equals("6,5")){

                        if(isComputerTurn){
                            if (txt_six.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)
                                    || txt_five.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                                //arrayList_eveven.remove(s);
                            }
                            else {
                                arrayListFilterData.add(arrayList_eveven.get(s));
                            }
                        }
                        else {
                            if (txt_six_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)
                                    || txt_five_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                                //arrayList_eveven.remove(s);
                            }
                            else {
                                arrayListFilterData.add(arrayList_eveven.get(s));
                            }
                        }






                    }


                }



                new Handler().postDelayed(new Runnable() {


                    @Override

                    public void run() {


                        if(arrayListFilterData.size()>0)
                        {

                            if(count_for_no_combination==1){
                                methodCountAdd();
                            }

                            String[] str = GetStringArray(arrayListFilterData);

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

                                    Toast.makeText(getApplicationContext(),str[which],Toast.LENGTH_LONG).show();

                                    Log.i("SelectedPair",str[which]);

                                    pair=str[which];

                                    setPairData(pair,1,"you");


                                }
                            });

                            dialog = builder.create();



                            if (isComputerTurn) {

                                //dialog.show();
                                mehtodGravityForAlertDialog();

                            }
                            else {

                                pair=str[0];
                                setPairData(pair,0,"com");

                            }




                        }
                        else {
                            count_for_no_combination=count_for_no_combination+1;
                            if(isComputerTurn){
                                setPairData("",1,"you");
                            }else {
                                setPairData("",0,"com");
                            }
                            Toast.makeText(getApplicationContext(),"No Combination Possible",Toast.LENGTH_LONG).show();
                        }



                    }

                }, 1000);





            }


            else if(i==12)
            {
                Log.i("arrayList",arrayList_twelve.toString());

                arrayListFilterData=new ArrayList<>();


                for (int s=0;s<arrayList_twelve.size();s++){
                    Log.i("ArrayListData",arrayList_twelve.get(s));

                    if(arrayList_twelve.get(s).equals("9,3")){

                        if(isComputerTurn){
                            if (txt_nine.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)
                                    || txt_three.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                                //arrayList_twelve.remove(s);
                            }
                            else {
                                arrayListFilterData.add(arrayList_twelve.get(s));
                            }
                        }
                        else {
                            if (txt_nine_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)
                                    || txt_three_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                                //arrayList_twelve.remove(s);
                            }
                            else {
                                arrayListFilterData.add(arrayList_twelve.get(s));
                            }
                        }



                    }

                    else if(arrayList_twelve.get(s).equals("8,4")){


                        if(isComputerTurn){
                            if (txt_eight.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)
                                    || txt_four.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                                //arrayList_twelve.remove(s);
                            }
                            else {
                                arrayListFilterData.add(arrayList_twelve.get(s));
                            }
                        }
                        else {
                            if (txt_eight_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)
                                    || txt_four_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                                //arrayList_twelve.remove(s);
                            }
                            else {
                                arrayListFilterData.add(arrayList_twelve.get(s));
                            }
                        }



                    }

                    else if(arrayList_twelve.get(s).equals("7,5")){


                        if(isComputerTurn){
                            if (txt_seven.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)
                                    || txt_five.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                                //  arrayList_twelve.remove(s);
                            }
                            else {
                                arrayListFilterData.add(arrayList_twelve.get(s));
                            }
                        }
                        else {
                            if (txt_seven_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)
                                    || txt_five_com.getCurrentTextColor()!=getResources().getColor(R.color.colorWhite)) {
                                //  arrayList_twelve.remove(s);
                            }
                            else {
                                arrayListFilterData.add(arrayList_twelve.get(s));
                            }
                        }



                    }




                }


                new Handler().postDelayed(new Runnable() {


                    @Override

                    public void run() {


                        if(arrayListFilterData.size()>0)
                        {

                            if(count_for_no_combination==1){
                                methodCountAdd();
                            }

                            String[] str = GetStringArray(arrayListFilterData);



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




                                    Toast.makeText(getApplicationContext(),str[which],Toast.LENGTH_LONG).show();

                                    Log.i("SelectedPair",str[which]);

                                    pair=str[which];

                                    setPairData(pair,1,"you");


                                }
                            });

                            dialog = builder.create();



                            if (isComputerTurn) {

                                //dialog.show();
                                mehtodGravityForAlertDialog();

                            }
                            else {

                                pair=str[0];
                                setPairData(pair,0,"com");

                            }

                        }

                        else {

                            count_for_no_combination=count_for_no_combination+1;

                            if(isComputerTurn){
                                setPairData("",1,"you");
                            }
                            else {
                                setPairData("",0,"com");
                            }

                            Toast.makeText(getApplicationContext(),"No Combination Possible",Toast.LENGTH_LONG).show();

                        }




                    }

                }, 1000);


            }


            return true;

        }
    };

    private void computerTurn() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (!rolling) {
                    isLastTurn = false;
                    isComputerTurn = false;
                    rolling = true;
                    //Show rolling image
                    dice_picture.setImageResource(R.drawable.dice3d160);
                    dice2_picture.setImageResource(R.drawable.dice3d160);
                    if (!isMusic) {
                        //Start rolling sound
                        // soundplay = dice_sound.play(sound_id, 1.0f, 1.0f, 0, 0, 1.0f);
                    } else {
                        //Start rolling sound
                        soundplay = dice_sound.play(sound_id, 1.0f, 1.0f, 0, 0, 1.0f);
                    }
                    //Pause to allow image to update
                    dice_picture.startAnimation(rotate);
                    dice2_picture.startAnimation(rotate);
                    try {
                        timer.schedule(new Roll(), 1100);
                    }catch (Exception e){

                    }

                }


            }
        }, 2000);
    }

    private void displayMessage(String message) {
        Toast toast = Toast.makeText(PlayWithComputerActivity.this, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }

    public void showDialog(String msg) {

        final Dialog dialog = new Dialog(PlayWithComputerActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.win_lose_dialog_item);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView txt = dialog.findViewById(R.id.txt);
        RelativeLayout plus_layout=dialog.findViewById(R.id.plus_layout);
        RelativeLayout plus_layout_two=dialog.findViewById(R.id.plus_layout_two);

        if(msg.equalsIgnoreCase("BETTER LUCK NEXT TIME!"))
        {
            txt.setText("BETTER LUCK" + "\n" + "NEXT TIME!");
            plus_layout.setVisibility(View.GONE);
            plus_layout_two.setVisibility(View.GONE);
        }
        else {

            txt.setText(msg);
            plus_layout.setVisibility(View.VISIBLE);
            plus_layout_two.setVisibility(View.VISIBLE);

        }

       // txt.setText(hello + "\n" + world);
       //

        ImageView close_img = dialog.findViewById(R.id.close_img);
        ImageView win_lose_img = dialog.findViewById(R.id.win_lose_img);
        ImageView home_img = dialog.findViewById(R.id.home_img);

        home_img.setVisibility(View.VISIBLE);

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

        final Dialog dialog = new Dialog(PlayWithComputerActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        View view = getLayoutInflater().inflate(R.layout.exit_game_dialog, null);
        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ImageView yes_img = view.findViewById(R.id.yes_img);
        ImageView sound_img = view.findViewById(R.id.sound_img);
        ImageView music_img = view.findViewById(R.id.music_img);
        ImageView no_img = view.findViewById(R.id.no_img);

        isMusic = SharedHelper.getBooleanKey(PlayWithComputerActivity.this, UserConstant.isMusic);




        if (isMusic) {
            music_img.setImageResource(R.drawable.mute_neww);

        } else {
            music_img.setImageResource(R.drawable.mute_neww);
        }

        isSound = SharedHelper.getBooleanKey(PlayWithComputerActivity.this, UserConstant.isSound);

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
                }catch (Exception e){

                }

                dialog.dismiss();

                showDialog("BETTER LUCK NEXT TIME!");

                //  finish();

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


    public String[] GetStringArray(ArrayList<String> arr)
    {

        // declaration and initialise String Array
        String str[] = new String[arr.size()];

        // ArrayList to Array Conversion
        for (int j = 0; j < arr.size(); j++) {

            // Assign each value to String array

            Log.i("ArrayListGetData",arr.get(j));

            str[j] = arr.get(j);

        }

        return str;
    }


    private void setPairData(String pair,int color_pos,String from)
    {

        shape_one_you.setImageResource(android.R.color.transparent);
        shape_two_you.setImageResource(android.R.color.transparent);
        shape_three_you.setImageResource(android.R.color.transparent);
        shape_four_you.setImageResource(android.R.color.transparent);
        shape_five_you.setImageResource(android.R.color.transparent);
        shape_six_you.setImageResource(android.R.color.transparent);
        shape_seven_you.setImageResource(android.R.color.transparent);
        shape_eight_you.setImageResource(android.R.color.transparent);
        shape_nine_you.setImageResource(android.R.color.transparent);


        if(from.equals("you"))
        {

            if(pair.contains(","))
          {

            String[] split_list = pair.split(",");
            Log.i("pairValue",split_list[0]+"second"+split_list[1]);

            if(split_list[0].trim().equals(txt_one.getText().toString().trim())){
                if(color_pos==0)
                {

                    txt_one.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txt_one_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                }
                else if(color_pos==1){

                    txt_one.setTextColor(getResources().getColor(R.color.coloryellow));
                    txt_one_bottem.setTextColor(getResources().getColor(R.color.coloryellow));
                }


            }
            else if(split_list[0].trim().equals(txt_two.getText().toString().trim())){
                if(color_pos==0)
                {
                    txt_two.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txt_two_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
                else if(color_pos==1){

                    txt_two.setTextColor(getResources().getColor(R.color.coloryellow));
                    txt_two_bottem.setTextColor(getResources().getColor(R.color.coloryellow));
                }

            }
            else if(split_list[0].trim().equals(txt_three.getText().toString().trim())){
                if(color_pos==0)
                {
                    txt_three.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txt_three_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
                else if(color_pos==1){

                    txt_three.setTextColor(getResources().getColor(R.color.coloryellow));
                    txt_three_bottem.setTextColor(getResources().getColor(R.color.coloryellow));
                }

            }
            else if(split_list[0].trim().equals(txt_four.getText().toString().trim())){
                if(color_pos==0)
                {
                    txt_four.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txt_four_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
                else if(color_pos==1){

                    txt_four.setTextColor(getResources().getColor(R.color.coloryellow));
                    txt_four_bottem.setTextColor(getResources().getColor(R.color.coloryellow));
                }

            }
            else if(split_list[0].trim().equals(txt_five.getText().toString().trim())){
                if(color_pos==0)
                {
                    txt_five.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txt_five_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
                else if(color_pos==1){

                    txt_five.setTextColor(getResources().getColor(R.color.coloryellow));
                    txt_five_bottem.setTextColor(getResources().getColor(R.color.coloryellow));
                }

            }
            else if(split_list[0].trim().equals(txt_six.getText().toString().trim())){
                if(color_pos==0)
                {
                    txt_six.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txt_six_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
                else if(color_pos==1){

                    txt_six.setTextColor(getResources().getColor(R.color.coloryellow));
                    txt_six_bottem.setTextColor(getResources().getColor(R.color.coloryellow));
                }

            }
            else if(split_list[0].trim().equals(txt_seven.getText().toString().trim())){
                if(color_pos==0)
                {
                    txt_seven.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txt_seven_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
                else if(color_pos==1){

                    txt_seven.setTextColor(getResources().getColor(R.color.coloryellow));
                    txt_seven_bottem.setTextColor(getResources().getColor(R.color.coloryellow));
                }

            }
            else if(split_list[0].trim().equals(txt_eight.getText().toString().trim())){
                if(color_pos==0)
                {
                    txt_eight.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txt_eight_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
                else if(color_pos==1){

                    txt_eight.setTextColor(getResources().getColor(R.color.coloryellow));
                    txt_eight_bottem.setTextColor(getResources().getColor(R.color.coloryellow));
                }

            }
            else if(split_list[0].trim().equals(txt_nine.getText().toString().trim())){
                if(color_pos==0)
                {
                    txt_nine.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txt_nine_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
                else if(color_pos==1){

                    txt_nine.setTextColor(getResources().getColor(R.color.coloryellow));
                    txt_nine_bottem.setTextColor(getResources().getColor(R.color.coloryellow));
                }

            }



            if(split_list[1].trim().equals(txt_one.getText().toString().trim())){
                if(color_pos==0)
                {
                    txt_one.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txt_one_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
                else if(color_pos==1){

                    txt_one.setTextColor(getResources().getColor(R.color.coloryellow));
                    txt_one_bottem.setTextColor(getResources().getColor(R.color.coloryellow));
                }

            }
            else if(split_list[1].trim().equals(txt_two.getText().toString().trim())){
                if(color_pos==0)
                {
                    txt_two.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txt_two_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
                else if(color_pos==1){

                    txt_two.setTextColor(getResources().getColor(R.color.coloryellow));
                    txt_two_bottem.setTextColor(getResources().getColor(R.color.coloryellow));
                }

            }
            else if(split_list[1].trim().equals(txt_three.getText().toString().trim())){
                if(color_pos==0)
                {
                    txt_three.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txt_three_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
                else if(color_pos==1){

                    txt_three.setTextColor(getResources().getColor(R.color.coloryellow));
                    txt_three_bottem.setTextColor(getResources().getColor(R.color.coloryellow));
                }

            }
            else if(split_list[1].trim().equals(txt_four.getText().toString().trim())){
                if(color_pos==0)
                {
                    txt_four.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txt_four_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
                else if(color_pos==1){

                    txt_four.setTextColor(getResources().getColor(R.color.coloryellow));
                    txt_four_bottem.setTextColor(getResources().getColor(R.color.coloryellow));
                }

            }
            else if(split_list[1].trim().equals(txt_five.getText().toString().trim())){
                if(color_pos==0)
                {
                    txt_five.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txt_five_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
                else if(color_pos==1){

                    txt_five.setTextColor(getResources().getColor(R.color.coloryellow));
                    txt_five_bottem.setTextColor(getResources().getColor(R.color.coloryellow));
                }

            }
            else if(split_list[1].trim().equals(txt_six.getText().toString().trim())){
                if(color_pos==0)
                {
                    txt_six.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txt_six_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
                else if(color_pos==1){

                    txt_six.setTextColor(getResources().getColor(R.color.coloryellow));
                    txt_six_bottem.setTextColor(getResources().getColor(R.color.coloryellow));
                }

            }
            else if(split_list[1].trim().equals(txt_seven.getText().toString().trim())){
                if(color_pos==0)
                {
                    txt_seven.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txt_seven_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
                else if(color_pos==1){

                    txt_seven.setTextColor(getResources().getColor(R.color.coloryellow));
                    txt_seven_bottem.setTextColor(getResources().getColor(R.color.coloryellow));
                }

            }
            else if(split_list[1].trim().equals(txt_eight.getText().toString().trim())){
                if(color_pos==0)
                {
                    txt_eight.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txt_eight_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
                else if(color_pos==1){

                    txt_eight.setTextColor(getResources().getColor(R.color.coloryellow));
                    txt_eight_bottem.setTextColor(getResources().getColor(R.color.coloryellow));
                }

            }
            else if(split_list[1].trim().equals(txt_nine.getText().toString().trim())){
                if(color_pos==0)
                {
                    txt_nine.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txt_nine_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
                else if(color_pos==1){

                    txt_nine.setTextColor(getResources().getColor(R.color.coloryellow));
                    txt_nine_bottem.setTextColor(getResources().getColor(R.color.coloryellow));
                }

            }


        }

        else {

            Log.i("pairValue",pair);

            if(pair.trim().equals(txt_one.getText().toString().trim())){
                if(color_pos==0)
                {
                    txt_one.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txt_one_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
                else if(color_pos==1){


                    txt_one.setTextColor(getResources().getColor(R.color.coloryellow));
                    txt_one_bottem.setTextColor(getResources().getColor(R.color.coloryellow));
                }

            }
            else if(pair.trim().equals(txt_two.getText().toString().trim())){
                if(color_pos==0)
                {
                    txt_two.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txt_two_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
                else if(color_pos==1){

                    txt_two.setTextColor(getResources().getColor(R.color.coloryellow));
                    txt_two_bottem.setTextColor(getResources().getColor(R.color.coloryellow));
                }

            }
            else if(pair.trim().equals(txt_three.getText().toString().trim())){
                if(color_pos==0)
                {
                    txt_three.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txt_three_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
                else if(color_pos==1){

                    txt_three.setTextColor(getResources().getColor(R.color.coloryellow));
                    txt_three_bottem.setTextColor(getResources().getColor(R.color.coloryellow));
                }

            }
            else if(pair.trim().equals(txt_four.getText().toString().trim())){
                if(color_pos==0)
                {
                    txt_four.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txt_four_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
                else if(color_pos==1){

                    txt_four.setTextColor(getResources().getColor(R.color.coloryellow));
                    txt_four_bottem.setTextColor(getResources().getColor(R.color.coloryellow));
                }

            }
            else if(pair.trim().equals(txt_five.getText().toString().trim())){
                if(color_pos==0)
                {
                    txt_five.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txt_five_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
                else if(color_pos==1){

                    txt_five.setTextColor(getResources().getColor(R.color.coloryellow));
                    txt_five_bottem.setTextColor(getResources().getColor(R.color.coloryellow));
                }

            }
            else if(pair.trim().equals(txt_six.getText().toString().trim())){
                if(color_pos==0)
                {
                    txt_six.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txt_six_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
                else if(color_pos==1){

                    txt_six.setTextColor(getResources().getColor(R.color.coloryellow));
                    txt_six_bottem.setTextColor(getResources().getColor(R.color.coloryellow));
                }

            }
            else if(pair.trim().equals(txt_seven.getText().toString().trim())){
                if(color_pos==0)
                {
                    txt_seven.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txt_seven_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
                else if(color_pos==1){

                    txt_seven.setTextColor(getResources().getColor(R.color.coloryellow));
                    txt_seven_bottem.setTextColor(getResources().getColor(R.color.coloryellow));
                }

            }
            else if(pair.trim().equals(txt_eight.getText().toString().trim())){
                if(color_pos==0)
                {
                    txt_eight.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txt_eight_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
                else if(color_pos==1){

                    txt_eight.setTextColor(getResources().getColor(R.color.coloryellow));
                    txt_eight_bottem.setTextColor(getResources().getColor(R.color.coloryellow));
                }

            }
            else if(pair.trim().equals(txt_nine.getText().toString().trim())){
                if(color_pos==0)
                {
                    txt_nine.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txt_nine_bottem.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
                else if(color_pos==1){

                    txt_nine.setTextColor(getResources().getColor(R.color.coloryellow));
                    txt_nine_bottem.setTextColor(getResources().getColor(R.color.coloryellow));
                }

            }

        }

        }

        else {

            if(pair.contains(","))
      {

            String[] split_list = pair.split(",");
            Log.i("pairValue",split_list[0]+"second"+split_list[1]);

            if(split_list[0].trim().equals(txt_one.getText().toString().trim())){
                if(color_pos==0)
                {


                    txt_one_com.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txt_one_bottem_com.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                }


            }
            else if(split_list[0].trim().equals(txt_two.getText().toString().trim())){
                if(color_pos==0)
                {


                    txt_two_com.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txt_two_bottem_com.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }

            }
            else if(split_list[0].trim().equals(txt_three.getText().toString().trim())){
                if(color_pos==0)
                {


                    txt_three_com.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txt_three_bottem_com.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }

            }
            else if(split_list[0].trim().equals(txt_four.getText().toString().trim())){
                if(color_pos==0)
                {


                    txt_four_com.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txt_four_bottem_com.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }

            }
            else if(split_list[0].trim().equals(txt_five.getText().toString().trim())){
                if(color_pos==0)
                {


                    txt_five_com.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txt_five_bottem_com.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }

            }
            else if(split_list[0].trim().equals(txt_six.getText().toString().trim())){
                if(color_pos==0)
                {


                    txt_six_com.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txt_six_bottem_com.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }

            }
            else if(split_list[0].trim().equals(txt_seven.getText().toString().trim())){
                if(color_pos==0)
                {



                    txt_seven_com.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txt_seven_bottem_com.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }

            }
            else if(split_list[0].trim().equals(txt_eight.getText().toString().trim())){
                if(color_pos==0)
                {


                    txt_eight_com.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txt_eight_bottem_com.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }

            }
            else if(split_list[0].trim().equals(txt_nine.getText().toString().trim())){
                if(color_pos==0)
                {


                    txt_nine_com.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txt_nine_bottem_com.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }

            }



            if(split_list[1].trim().equals(txt_one.getText().toString().trim())){
                if(color_pos==0)
                {

                    txt_one_com.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txt_one_bottem_com.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }

            }

            else if(split_list[1].trim().equals(txt_two.getText().toString().trim())){
                if(color_pos==0)
                {


                    txt_two_com.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txt_two_bottem_com.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }

            }

            else if(split_list[1].trim().equals(txt_three.getText().toString().trim())){
                if(color_pos==0)
                {


                    txt_three_com.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txt_three_bottem_com.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }

            }

            else if(split_list[1].trim().equals(txt_four.getText().toString().trim())){
                if(color_pos==0)
                {


                    txt_four_com.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txt_four_bottem_com.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }

            }

            else if(split_list[1].trim().equals(txt_five.getText().toString().trim())){
                if(color_pos==0)
                {


                    txt_five_com.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txt_five_bottem_com.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }

            }

            else if(split_list[1].trim().equals(txt_six.getText().toString().trim())){
                if(color_pos==0)
                {


                    txt_six_com.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txt_six_bottem_com.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }

            }

            else if(split_list[1].trim().equals(txt_seven.getText().toString().trim())){
                if(color_pos==0)
                {


                    txt_seven_com.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txt_seven_bottem_com.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }

            }

            else if(split_list[1].trim().equals(txt_eight.getText().toString().trim())){
                if(color_pos==0)
                {


                    txt_eight_com.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txt_eight_bottem_com.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }

            }

            else if(split_list[1].trim().equals(txt_nine.getText().toString().trim())){
                if(color_pos==0)
                {


                    txt_nine_com.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txt_nine_bottem_com.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }

            }


        }

        else {

            Log.i("pairValue",pair);


            if(pair.trim().equals(txt_one.getText().toString().trim())){
                if(color_pos==0)
                {

                    txt_one_com.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txt_one_bottem_com.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }

            }
            else if(pair.trim().equals(txt_two.getText().toString().trim())){
                if(color_pos==0)
                {

                    txt_two_com.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txt_two_bottem_com.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }

            }
            else if(pair.trim().equals(txt_three.getText().toString().trim())){
                if(color_pos==0)
                {

                    txt_three_com.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txt_three_bottem_com.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }

            }
            else if(pair.trim().equals(txt_four.getText().toString().trim())){
                if(color_pos==0)
                {

                    txt_four_com.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txt_four_bottem_com.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }

            }
            else if(pair.trim().equals(txt_five.getText().toString().trim())){
                if(color_pos==0)
                {

                    txt_five_com.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txt_five_bottem_com.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }

            }
            else if(pair.trim().equals(txt_six.getText().toString().trim())){
                if(color_pos==0)
                {

                    txt_six_com.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txt_six_bottem_com.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }

            }
            else if(pair.trim().equals(txt_seven.getText().toString().trim())){
                if(color_pos==0)
                {

                    txt_seven_com.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txt_seven_bottem_com.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }

            }
            else if(pair.trim().equals(txt_eight.getText().toString().trim())){
                if(color_pos==0)
                {

                    txt_eight_com.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txt_eight_bottem_com.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }

            }
            else if(pair.trim().equals(txt_nine.getText().toString().trim())){
                if(color_pos==0)
                {

                    txt_nine_com.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txt_nine_bottem_com.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }

            }

        }

        }

        if(count_for_no_combination==2){
            isComputerTurn = false;
            layout_dice_movement.setEnabled(false);
            run=true;

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



            if (txt_one_com.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                count_opponent=count_opponent+1;
            }
            if (txt_two_com.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                count_opponent=count_opponent+2;
            }
            if (txt_three_com.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                count_opponent=count_opponent+3;
            }
            if (txt_four_com.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                count_opponent=count_opponent+4;
            }
            if (txt_five_com.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                count_opponent=count_opponent+5;
            }
            if (txt_six_com.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                count_opponent=count_opponent+6;
            }
            if (txt_seven_com.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                count_opponent=count_opponent+7;
            }
            if (txt_eight_com.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                count_opponent=count_opponent+8;
            }
            if (txt_nine_com.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                count_opponent=count_opponent+9;
            }


            if(count_your<=count_opponent){

                showDialog("WINNER!");
                apiCallAddCoins();

            }
            else {

                showDialog("BETTER LUCK NEXT TIME!");
            }


        }

        else {
            if (!run){

                if (txt_one_com.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_one_com.getCurrentTextColor() == getResources().getColor(R.color.coloryellow) || txt_one_com.getCurrentTextColor() == getResources().getColor(R.color.colorsolidGreen) || txt_one_com.getCurrentTextColor() == getResources().getColor(R.color.colorsDarkSky) ||txt_one_com.getCurrentTextColor() == getResources().getColor(R.color.colorRedLightNew)) {
                    if (txt_two_com.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_two_com.getCurrentTextColor() == getResources().getColor(R.color.coloryellow) || txt_two_com.getCurrentTextColor() == getResources().getColor(R.color.colorsolidGreen)|| txt_two_com.getCurrentTextColor() == getResources().getColor(R.color.colorsDarkSky)|| txt_two_com.getCurrentTextColor() == getResources().getColor(R.color.colorRedLightNew)) {
                        if (txt_three_com.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_three_com.getCurrentTextColor() == getResources().getColor(R.color.coloryellow) || txt_three_com.getCurrentTextColor() == getResources().getColor(R.color.colorsolidGreen)|| txt_three_com.getCurrentTextColor() == getResources().getColor(R.color.colorsDarkSky)|| txt_three_com.getCurrentTextColor() == getResources().getColor(R.color.colorRedLightNew)) {
                            if (txt_four_com.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_four_com.getCurrentTextColor() == getResources().getColor(R.color.coloryellow) || txt_four_com.getCurrentTextColor() == getResources().getColor(R.color.colorsolidGreen)|| txt_four_com.getCurrentTextColor() == getResources().getColor(R.color.colorsDarkSky)|| txt_four_com.getCurrentTextColor() == getResources().getColor(R.color.colorRedLightNew)) {
                                if (txt_five_com.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_five_com.getCurrentTextColor() == getResources().getColor(R.color.coloryellow) || txt_five_com.getCurrentTextColor() == getResources().getColor(R.color.colorsolidGreen)|| txt_five_com.getCurrentTextColor() == getResources().getColor(R.color.colorsDarkSky)|| txt_five_com.getCurrentTextColor() == getResources().getColor(R.color.colorRedLightNew)) {
                                    if (txt_six_com.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_six_com.getCurrentTextColor() == getResources().getColor(R.color.coloryellow) || txt_six_com.getCurrentTextColor() == getResources().getColor(R.color.colorsolidGreen)|| txt_six_com.getCurrentTextColor() == getResources().getColor(R.color.colorsDarkSky)|| txt_six_com.getCurrentTextColor() == getResources().getColor(R.color.colorRedLightNew)) {
                                        if (txt_seven_com.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_seven_com.getCurrentTextColor() == getResources().getColor(R.color.coloryellow)|| txt_seven_com.getCurrentTextColor() == getResources().getColor(R.color.colorsolidGreen)|| txt_seven_com.getCurrentTextColor() == getResources().getColor(R.color.colorsDarkSky)|| txt_seven_com.getCurrentTextColor() == getResources().getColor(R.color.colorRedLightNew)) {
                                            if (txt_eight_com.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_eight_com.getCurrentTextColor() == getResources().getColor(R.color.coloryellow)|| txt_eight_com.getCurrentTextColor() == getResources().getColor(R.color.colorsolidGreen)|| txt_eight_com.getCurrentTextColor() == getResources().getColor(R.color.colorsDarkSky)|| txt_eight_com.getCurrentTextColor() == getResources().getColor(R.color.colorRedLightNew)) {
                                                if (txt_nine_com.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_nine_com.getCurrentTextColor() == getResources().getColor(R.color.coloryellow)|| txt_nine_com.getCurrentTextColor() == getResources().getColor(R.color.colorsolidGreen)|| txt_nine_com.getCurrentTextColor() == getResources().getColor(R.color.colorsDarkSky)|| txt_nine_com.getCurrentTextColor() == getResources().getColor(R.color.colorRedLightNew)){

                                                    if (!isLastTurn) {

                                                        isComputerTurn = false;
                                                        layout_dice_movement.setEnabled(false);
                                                        showDialog("BETTER LUCK NEXT TIME!");
                                                        run=true;

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


            if (!run){
                if (txt_one.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_one.getCurrentTextColor() == getResources().getColor(R.color.coloryellow) || txt_one.getCurrentTextColor() == getResources().getColor(R.color.colorsolidGreen) || txt_one.getCurrentTextColor() == getResources().getColor(R.color.colorsDarkSky) ||txt_one.getCurrentTextColor() == getResources().getColor(R.color.colorRedLightNew)) {
                    if (txt_two.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_two.getCurrentTextColor() == getResources().getColor(R.color.coloryellow) || txt_two.getCurrentTextColor() == getResources().getColor(R.color.colorsolidGreen)|| txt_two.getCurrentTextColor() == getResources().getColor(R.color.colorsDarkSky)|| txt_two.getCurrentTextColor() == getResources().getColor(R.color.colorRedLightNew)) {
                        if (txt_three.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_three.getCurrentTextColor() == getResources().getColor(R.color.coloryellow) || txt_three.getCurrentTextColor() == getResources().getColor(R.color.colorsolidGreen)|| txt_three.getCurrentTextColor() == getResources().getColor(R.color.colorsDarkSky)|| txt_three.getCurrentTextColor() == getResources().getColor(R.color.colorRedLightNew)) {
                            if (txt_four.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_four.getCurrentTextColor() == getResources().getColor(R.color.coloryellow) || txt_four.getCurrentTextColor() == getResources().getColor(R.color.colorsolidGreen)|| txt_four.getCurrentTextColor() == getResources().getColor(R.color.colorsDarkSky)|| txt_four.getCurrentTextColor() == getResources().getColor(R.color.colorRedLightNew)) {
                                if (txt_five.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_five.getCurrentTextColor() == getResources().getColor(R.color.coloryellow) || txt_five.getCurrentTextColor() == getResources().getColor(R.color.colorsolidGreen)|| txt_five.getCurrentTextColor() == getResources().getColor(R.color.colorsDarkSky)|| txt_five.getCurrentTextColor() == getResources().getColor(R.color.colorRedLightNew)) {
                                    if (txt_six.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_six.getCurrentTextColor() == getResources().getColor(R.color.coloryellow) || txt_six.getCurrentTextColor() == getResources().getColor(R.color.colorsolidGreen)|| txt_six.getCurrentTextColor() == getResources().getColor(R.color.colorsDarkSky)|| txt_six.getCurrentTextColor() == getResources().getColor(R.color.colorRedLightNew)) {
                                        if (txt_seven.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_seven.getCurrentTextColor() == getResources().getColor(R.color.coloryellow)|| txt_seven.getCurrentTextColor() == getResources().getColor(R.color.colorsolidGreen)|| txt_seven.getCurrentTextColor() == getResources().getColor(R.color.colorsDarkSky)|| txt_seven.getCurrentTextColor() == getResources().getColor(R.color.colorRedLightNew)) {
                                            if (txt_eight.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_eight.getCurrentTextColor() == getResources().getColor(R.color.coloryellow)|| txt_eight.getCurrentTextColor() == getResources().getColor(R.color.colorsolidGreen)|| txt_eight.getCurrentTextColor() == getResources().getColor(R.color.colorsDarkSky)|| txt_eight.getCurrentTextColor() == getResources().getColor(R.color.colorRedLightNew)) {
                                                if (txt_nine.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_nine.getCurrentTextColor() == getResources().getColor(R.color.coloryellow)|| txt_nine.getCurrentTextColor() == getResources().getColor(R.color.colorsolidGreen)|| txt_nine.getCurrentTextColor() == getResources().getColor(R.color.colorsDarkSky)|| txt_nine.getCurrentTextColor() == getResources().getColor(R.color.colorRedLightNew)){

                                                    if (isLastTurn) {

                                                        layout_dice_movement.setEnabled(false);

                                                        count = 0;

                                                        if (txt_one_com.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                                                            count++;
                                                        }
                                                        if (txt_two_com.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                                                            count++;
                                                        }
                                                        if (txt_three_com.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                                                            count++;
                                                        }
                                                        if (txt_four_com.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                                                            count++;
                                                        }
                                                        if (txt_five_com.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                                                            count++;
                                                        }
                                                        if (txt_six_com.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                                                            count++;
                                                        }
                                                        if (txt_seven_com.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                                                            count++;
                                                        }
                                                        if (txt_eight_com.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                                                            count++;
                                                        }
                                                        if (txt_nine_com.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                                                            count++;
                                                        }

                                                        if (count <= 1) {

                                                            showDialog("WINNER!");
                                                            apiCallAddCoins();

                                                        }
                                                        else {

                                                            showDialogBucky();
                                                            apiCallAddCoins();

                                                        }

                                                        run=true;

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



            if (isComputerTurn) {

                Log.i("value_run",String.valueOf(run));

                layout_dice_movement.setEnabled(false);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        if(!run){

                            if (txt_one.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark) || txt_one.getCurrentTextColor() != getResources().getColor(R.color.coloryellow) || txt_one.getCurrentTextColor() != getResources().getColor(R.color.colorsolidGreen) || txt_one.getCurrentTextColor() != getResources().getColor(R.color.colorsDarkSky) ||txt_one.getCurrentTextColor() != getResources().getColor(R.color.colorRedLightNew)) {
                                if (txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark) || txt_two.getCurrentTextColor() != getResources().getColor(R.color.coloryellow) || txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorsolidGreen)|| txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorsDarkSky)|| txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorRedLightNew)) {
                                    if (txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark) || txt_three.getCurrentTextColor() != getResources().getColor(R.color.coloryellow) || txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorsolidGreen)|| txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorsDarkSky)|| txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorRedLightNew)) {
                                        if (txt_four.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark) || txt_four.getCurrentTextColor() != getResources().getColor(R.color.coloryellow) || txt_four.getCurrentTextColor() != getResources().getColor(R.color.colorsolidGreen)|| txt_four.getCurrentTextColor() != getResources().getColor(R.color.colorsDarkSky)|| txt_four.getCurrentTextColor() != getResources().getColor(R.color.colorRedLightNew)) {
                                            if (txt_five.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark) || txt_five.getCurrentTextColor() != getResources().getColor(R.color.coloryellow) || txt_five.getCurrentTextColor() != getResources().getColor(R.color.colorsolidGreen)|| txt_five.getCurrentTextColor() != getResources().getColor(R.color.colorsDarkSky)|| txt_five.getCurrentTextColor() != getResources().getColor(R.color.colorRedLightNew)) {
                                                if (txt_six.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark) || txt_six.getCurrentTextColor() != getResources().getColor(R.color.coloryellow) || txt_six.getCurrentTextColor() != getResources().getColor(R.color.colorsolidGreen)|| txt_six.getCurrentTextColor() != getResources().getColor(R.color.colorsDarkSky)|| txt_six.getCurrentTextColor() != getResources().getColor(R.color.colorRedLightNew)) {
                                                    if (txt_seven.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark) || txt_seven.getCurrentTextColor() != getResources().getColor(R.color.coloryellow)|| txt_seven.getCurrentTextColor() != getResources().getColor(R.color.colorsolidGreen)|| txt_seven.getCurrentTextColor() != getResources().getColor(R.color.colorsDarkSky)|| txt_seven.getCurrentTextColor() != getResources().getColor(R.color.colorRedLightNew)) {
                                                        if (txt_eight.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark) || txt_eight.getCurrentTextColor() != getResources().getColor(R.color.coloryellow)|| txt_eight.getCurrentTextColor() != getResources().getColor(R.color.colorsolidGreen)|| txt_eight.getCurrentTextColor() != getResources().getColor(R.color.colorsDarkSky)|| txt_eight.getCurrentTextColor() != getResources().getColor(R.color.colorRedLightNew)) {
                                                            if (txt_nine.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark) || txt_nine.getCurrentTextColor() != getResources().getColor(R.color.coloryellow)|| txt_nine.getCurrentTextColor() != getResources().getColor(R.color.colorsolidGreen)|| txt_nine.getCurrentTextColor() != getResources().getColor(R.color.colorsDarkSky)|| txt_nine.getCurrentTextColor() != getResources().getColor(R.color.colorRedLightNew)){

                                                                computerTurn();
                                                                displayMessage("Computer Turn");
                                                                //gif_img.setVisibility(View.GONE);

                                                                iv_profile_center.setVisibility(View.VISIBLE);
                                                                methodVisibleGoneWhenComputerTurn();

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
                }, 1000);

            }

            else {


                if (!run){

                    if (txt_one_com.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark) || txt_one_com.getCurrentTextColor() != getResources().getColor(R.color.coloryellow) || txt_one_com.getCurrentTextColor() != getResources().getColor(R.color.colorsolidGreen) || txt_one_com.getCurrentTextColor() != getResources().getColor(R.color.colorsDarkSky) ||txt_one_com.getCurrentTextColor() != getResources().getColor(R.color.colorRedLightNew)) {
                        if (txt_two_com.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark) || txt_two_com.getCurrentTextColor() != getResources().getColor(R.color.coloryellow) || txt_two_com.getCurrentTextColor() != getResources().getColor(R.color.colorsolidGreen)|| txt_two_com.getCurrentTextColor() != getResources().getColor(R.color.colorsDarkSky)|| txt_two_com.getCurrentTextColor() != getResources().getColor(R.color.colorRedLightNew)) {
                            if (txt_three_com.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark) || txt_three_com.getCurrentTextColor() != getResources().getColor(R.color.coloryellow) || txt_three_com.getCurrentTextColor() != getResources().getColor(R.color.colorsolidGreen)|| txt_three_com.getCurrentTextColor() != getResources().getColor(R.color.colorsDarkSky)|| txt_three_com.getCurrentTextColor() != getResources().getColor(R.color.colorRedLightNew)) {
                                if (txt_four_com.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark) || txt_four_com.getCurrentTextColor() != getResources().getColor(R.color.coloryellow) || txt_four_com.getCurrentTextColor() != getResources().getColor(R.color.colorsolidGreen)|| txt_four_com.getCurrentTextColor() != getResources().getColor(R.color.colorsDarkSky)|| txt_four_com.getCurrentTextColor() != getResources().getColor(R.color.colorRedLightNew)) {
                                    if (txt_five_com.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark) || txt_five_com.getCurrentTextColor() != getResources().getColor(R.color.coloryellow) || txt_five_com.getCurrentTextColor() != getResources().getColor(R.color.colorsolidGreen)|| txt_five_com.getCurrentTextColor() != getResources().getColor(R.color.colorsDarkSky)|| txt_five_com.getCurrentTextColor() != getResources().getColor(R.color.colorRedLightNew)) {
                                        if (txt_six_com.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark) || txt_six_com.getCurrentTextColor() != getResources().getColor(R.color.coloryellow) || txt_six_com.getCurrentTextColor() != getResources().getColor(R.color.colorsolidGreen)|| txt_six_com.getCurrentTextColor() != getResources().getColor(R.color.colorsDarkSky)|| txt_six_com.getCurrentTextColor() != getResources().getColor(R.color.colorRedLightNew)) {
                                            if (txt_seven_com.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark) || txt_seven_com.getCurrentTextColor() != getResources().getColor(R.color.coloryellow)|| txt_seven_com.getCurrentTextColor() != getResources().getColor(R.color.colorsolidGreen)|| txt_seven_com.getCurrentTextColor() != getResources().getColor(R.color.colorsDarkSky)|| txt_seven_com.getCurrentTextColor() != getResources().getColor(R.color.colorRedLightNew)) {
                                                if (txt_eight_com.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark) || txt_eight_com.getCurrentTextColor() != getResources().getColor(R.color.coloryellow)|| txt_eight_com.getCurrentTextColor() != getResources().getColor(R.color.colorsolidGreen)|| txt_eight_com.getCurrentTextColor() != getResources().getColor(R.color.colorsDarkSky)|| txt_eight_com.getCurrentTextColor() != getResources().getColor(R.color.colorRedLightNew)) {
                                                    if (txt_nine_com.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark) || txt_nine_com.getCurrentTextColor() != getResources().getColor(R.color.coloryellow)|| txt_nine_com.getCurrentTextColor() != getResources().getColor(R.color.colorsolidGreen)|| txt_nine_com.getCurrentTextColor() != getResources().getColor(R.color.colorsDarkSky)|| txt_nine_com.getCurrentTextColor() != getResources().getColor(R.color.colorRedLightNew)){

                                                        layout_dice_movement.setEnabled(true);
                                                        displayMessage("Your Turn");
                                                        iv_profile_center.setVisibility(View.GONE);
                                                        methodVisibleGoneWhenMyTurn();

                                                        //gif_img.setVisibility(View.VISIBLE);

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


    private void methodVisibleGoneWhenComputerTurn() {

        layout_you_bottem.setVisibility(View.VISIBLE);
        layout_com_bottem.setVisibility(View.GONE);
        tv_name_you.setVisibility(View.VISIBLE);
        tv_name_com.setVisibility(View.GONE);
        layout_circuler_you.setVisibility(View.GONE);
        layout_circuler_com.setVisibility(View.VISIBLE);



    }

    private void methodVisibleGoneWhenMyTurn() {

        layout_you_bottem.setVisibility(View.GONE);
        layout_com_bottem.setVisibility(View.VISIBLE);
        tv_name_com.setVisibility(View.VISIBLE);
        tv_name_you.setVisibility(View.GONE);
        layout_circuler_you.setVisibility(View.VISIBLE);
        layout_circuler_com.setVisibility(View.GONE);


    }

    private void showDialogBucky() {
        final Dialog dialog = new Dialog(PlayWithComputerActivity.this);
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


    private void methodCountAdd() {

        count_for_no_combination=count_for_no_combination+1;

    }

    private void mehtodGravityForAlertDialog() {

        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }


    private void apiCallAddCoins(){

        ApiInterface apiInterface = AppConfig.getRetrofit().create(ApiInterface.class);
        Call<ModelAddCoins> call = apiInterface.add_coins("Bearer " + token, SharedHelper.getKey(getApplicationContext(), UserConstant.id)
        ,"false","");
        call.enqueue(new Callback<ModelAddCoins>() {
            @Override
            public void onResponse(Call<ModelAddCoins> call, Response<ModelAddCoins> response) {


                if (response.isSuccessful()) {

                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();


                    if (response.body().getStatus()) {

                        try {
                            SharedHelper.putKey(getApplicationContext(),UserConstant.coin,response.body().getData().get(0).getTotal_coins());
                            total_coin_txt.setText(SharedHelper.getKey(PlayWithComputerActivity.this, UserConstant.coin));
                            MainActivity.total_coin_txt.setText(SharedHelper.getKey(PlayWithComputerActivity.this, UserConstant.coin));

                            SharedHelper.putKey(getApplicationContext(),UserConstant.point,response.body().getData().get(0).getTotal_points());
                            ttxt.setText(SharedHelper.getKey(PlayWithComputerActivity.this, UserConstant.point));
                            MainActivity.ttxt.setText(SharedHelper.getKey(PlayWithComputerActivity.this, UserConstant.point));
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


    //Clean up
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
        super.onDestroy();
        try {
            timer.cancel();
        }catch (Exception e){

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
                    setPairData("2",1,"you");
                }

                break;


            case "3":

                if(selected_number.equals("3")){
                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("3")){
                            setPairData("3",1,"you");
                        }
                    }

                }
                 if(selected_number.equals("2")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("2,1")){
                            setPairData("2,1",1,"you");
                        }
                    }

                }
                if(selected_number.equals("1")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("2,1")){
                            setPairData("2,1",1,"you");
                        }
                    }


                }

                break;


            case "4":

                if(selected_number.equals("4")){
                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("4")){
                            setPairData("4",1,"you");
                        }
                    }

                }
                if(selected_number.equals("3")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("3,1")){
                            setPairData("3,1",1,"you");
                        }
                    }

                }
                if(selected_number.equals("1")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("3,1")){
                            setPairData("3,1",1,"you");
                        }
                    }


                }

                break;


            case "5":



                if(selected_number.equals("5")){
                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("5")){
                            setPairData("5",1,"you");
                        }
                    }

                }
                if(selected_number.equals("4")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("4,1")){
                            setPairData("4,1",1,"you");
                        }
                    }

                }
                if(selected_number.equals("1")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("4,1")){
                            setPairData("4,1",1,"you");
                        }
                    }


                }



                if(selected_number.equals("3")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("3,2")){
                            setPairData("3,2",1,"you");
                        }
                    }

                }
                if(selected_number.equals("2")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("3,2")){
                            setPairData("3,2",1,"you");
                        }
                    }


                }


                break;


            case "6":


                if(selected_number.equals("6")){
                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("6")){
                            setPairData("6",1,"you");
                        }
                    }

                }
                if(selected_number.equals("5")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("5,1")){
                            setPairData("5,1",1,"you");
                        }
                    }

                }
                if(selected_number.equals("1")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("5,1")){
                            setPairData("5,1",1,"you");
                        }
                    }


                }



                if(selected_number.equals("4")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("4,2")){
                            setPairData("4,2",1,"you");
                        }
                    }

                }
                if(selected_number.equals("2")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("4,2")){
                            setPairData("4,2",1,"you");
                        }
                    }


                }




                break;


            case "7":



                if(selected_number.equals("7")){
                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("7")){
                            setPairData("7",1,"you");
                        }
                    }

                }
                if(selected_number.equals("6")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("6,1")){
                            setPairData("6,1",1,"you");
                        }
                    }

                }
                if(selected_number.equals("1")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("6,1")){
                            setPairData("6,1",1,"you");
                        }
                    }


                }



                if(selected_number.equals("5")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("5,2")){
                            setPairData("5,2",1,"you");
                        }
                    }

                }
                if(selected_number.equals("2")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("5,2")){
                            setPairData("5,2",1,"you");
                        }
                    }


                }




                if(selected_number.equals("4")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("4,3")){
                            setPairData("4,3",1,"you");
                        }
                    }

                }
                if(selected_number.equals("3")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("4,3")){
                            setPairData("4,3",1,"you");
                        }
                    }


                }



                break;


            case "8":



                if(selected_number.equals("8")){
                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("8")){
                            setPairData("8",1,"you");
                        }
                    }

                }
                if(selected_number.equals("7")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("7,1")){
                            setPairData("7,1",1,"you");
                        }
                    }

                }
                if(selected_number.equals("1")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("7,1")){
                            setPairData("7,1",1,"you");
                        }
                    }


                }



                if(selected_number.equals("6")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("6,2")){
                            setPairData("6,2",1,"you");
                        }
                    }

                }
                if(selected_number.equals("2")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("6,2")){
                            setPairData("6,2",1,"you");
                        }
                    }


                }




                if(selected_number.equals("5")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("5,3")){
                            setPairData("5,3",1,"you");
                        }
                    }

                }
                if(selected_number.equals("3")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("5,3")){
                            setPairData("5,3",1,"you");
                        }
                    }


                }


                break;


            case "9":



                if(selected_number.equals("9")){
                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("9")){
                            setPairData("9",1,"you");
                        }
                    }

                }
                if(selected_number.equals("8")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("8,1")){
                            setPairData("8,1",1,"you");
                        }
                    }

                }
                if(selected_number.equals("1")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("8,1")){
                            setPairData("8,1",1,"you");
                        }
                    }


                }



                if(selected_number.equals("7")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("7,2")){
                            setPairData("7,2",1,"you");
                        }
                    }

                }
                if(selected_number.equals("2")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("7,2")){
                            setPairData("7,2",1,"you");
                        }
                    }


                }




                if(selected_number.equals("6")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("6,3")){
                            setPairData("6,3",1,"you");
                        }
                    }

                }
                if(selected_number.equals("3")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("6,3")){
                            setPairData("6,3",1,"you");
                        }
                    }


                }


                if(selected_number.equals("5")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("5,4")){
                            setPairData("5,4",1,"you");
                        }
                    }

                }
                if(selected_number.equals("4")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("5,4")){
                            setPairData("5,4",1,"you");
                        }
                    }


                }



                break;


            case "10":


                if(selected_number.equals("9")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("9,1")){
                            setPairData("9,1",1,"you");
                        }
                    }

                }
                if(selected_number.equals("1")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("9,1")){
                            setPairData("9,1",1,"you");
                        }
                    }


                }




                if(selected_number.equals("8")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("8,2")){
                            setPairData("8,2",1,"you");
                        }
                    }

                }
                if(selected_number.equals("2")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("8,2")){
                            setPairData("8,2",1,"you");
                        }
                    }


                }


                if(selected_number.equals("7")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("7,3")){
                            setPairData("7,3",1,"you");
                        }
                    }

                }
                if(selected_number.equals("3")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("7,3")){
                            setPairData("7,3",1,"you");
                        }
                    }


                }



                if(selected_number.equals("6")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("6,4")){
                            setPairData("6,4",1,"you");
                        }
                    }

                }
                if(selected_number.equals("4")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("6,4")){
                            setPairData("6,4",1,"you");
                        }
                    }


                }



                break;


            case "11":


                if(selected_number.equals("9")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("9,2")){
                            setPairData("9,2",1,"you");
                        }
                    }

                }
                if(selected_number.equals("2")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("9,2")){
                            setPairData("9,2",1,"you");
                        }
                    }


                }




                if(selected_number.equals("8")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("8,3")){
                            setPairData("8,3",1,"you");
                        }
                    }

                }
                if(selected_number.equals("3")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("8,3")){
                            setPairData("8,3",1,"you");
                        }
                    }


                }


                if(selected_number.equals("7")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("7,4")){
                            setPairData("7,4",1,"you");
                        }
                    }

                }
                if(selected_number.equals("4")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("7,4")){
                            setPairData("7,4",1,"you");
                        }
                    }


                }



                if(selected_number.equals("6")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("6,5")){
                            setPairData("6,5",1,"you");
                        }
                    }

                }
                if(selected_number.equals("5")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("6,5")){
                            setPairData("6,5",1,"you");
                        }
                    }


                }



                break;



            case "12":


                if(selected_number.equals("9")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("9,3")){
                            setPairData("9,3",1,"you");
                        }
                    }

                }
                if(selected_number.equals("3")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("9,3")){
                            setPairData("9,3",1,"you");
                        }
                    }


                }


                if(selected_number.equals("8")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("8,4")){
                            setPairData("8,4",1,"you");
                        }
                    }

                }
                if(selected_number.equals("4")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("8,4")){
                            setPairData("8,4",1,"you");
                        }
                    }


                }


                if(selected_number.equals("7")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("7,5")){
                            setPairData("7,5",1,"you");
                        }
                    }

                }
                if(selected_number.equals("5")){

                    for(int i=0;i<arrayListFilterData.size();i++){
                        if(arrayListFilterData.get(i).equals("7,5")){
                            setPairData("7,5",1,"you");
                        }
                    }


                }

                break;
        }

    }


    private void apiCallCounterForAds() {

//        ApiInterface apiInterface = AppConfig.getRetrofit().create(ApiInterface.class);
//        Call<GamePageCounterModel> call = apiInterface.gamepagecounter("Bearer "+token,SharedHelper.getKey(PlayWithComputerActivity.this,UserConstant.id));
//        call.enqueue(new Callback<GamePageCounterModel>() {
//            @Override
//            public void onResponse(Call<GamePageCounterModel> call, Response<GamePageCounterModel> response) {
//
//               if(response.body().getStatus()){
//                   counter=Integer.parseInt(response.body().getData().get(0).getCounter());
//                   Log.i("CounterIs",counter+"");
//               }
//
//
//            }
//
//            @Override
//            public void onFailure(Call<GamePageCounterModel> call, Throwable t) {
//
//
//                if (t instanceof SocketTimeoutException) {
//
//                }
//
//            }
//        });

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