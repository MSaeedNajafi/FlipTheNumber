package com.flipthenumber.beta.ui.activities.PlayPage;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flipthenumber.beta.Constants.UserConstant;
import com.flipthenumber.beta.Helper.CustomToast;
import com.flipthenumber.beta.Helper.PreLollipopSoundPool;
import com.flipthenumber.beta.Helper.SharedHelper;
import com.flipthenumber.beta.adapter.ChatMessagesAdapter;
import com.flipthenumber.beta.model.ModelChatMsg;
import com.flipthenumber.beta.model.ModelGetMsg;
import com.flipthenumber.beta.retrofit.ApiInterface;
import com.flipthenumber.beta.retrofit.AppConfig;
import com.flipthenumber.beta.R;
import com.squareup.picasso.Picasso;

import java.net.SocketTimeoutException;
import java.net.URISyntaxException;
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

public class PlayWithPlayer extends AppCompatActivity implements View.OnClickListener {

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

    @BindView(R.id.iv_profile_img_two)
    CircleImageView iv_profile_img_two;

    @BindView(R.id.iv_profile_img_second)
    CircleImageView iv_profile_img_second;

    TextView zokar_txt,ttxt,total_coin_txt,ttxt_two;

    TextView tv_name,tv_name_two;
    ImageView iv_chat;

    private String user_one_id,user_two_id,token;

    boolean run =false;

    private LinearLayout layout_coin_two,layout_joker,layout_coins_one;



    //Socket Init

    io.socket.client.Socket socket ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_play_with_player);
        mAlramMAnager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        VolIsMute = false;

        init();

        initUI();

        InitSound();

        InitSocket();


    }



    private void init() {

        ButterKnife.bind(this);
        isMusic = SharedHelper.getBooleanKey(PlayWithPlayer.this, UserConstant.isMusic);
        isSound = SharedHelper.getBooleanKey(PlayWithPlayer.this, UserConstant.isSound);

        iv_chat=findViewById(R.id.iv_chat);
        iv_chat.setOnClickListener(this);
        tv_name=findViewById(R.id.tv_name);
        tv_name_two=findViewById(R.id.tv_name_two);

        zokar_txt=findViewById(R.id.zokar_txt);
        ttxt=findViewById(R.id.ttxt);
        total_coin_txt=findViewById(R.id.total_coin_txt);
        ttxt_two=findViewById(R.id.ttxt_two);

        zokar_txt.setText(SharedHelper.getKey(PlayWithPlayer.this, UserConstant.zokar));
        ttxt.setText(SharedHelper.getKey(PlayWithPlayer.this, UserConstant.coin));
        total_coin_txt.setText(SharedHelper.getKey(PlayWithPlayer.this, UserConstant.coin));
        ttxt_two.setText(SharedHelper.getKey(PlayWithPlayer.this, UserConstant.coin));

        layout_coin_two=findViewById(R.id.layout_coin_two);
        layout_coins_one=findViewById(R.id.layout_coins_one);
        layout_joker=findViewById(R.id.layout_joker);

    }

    private void initUI() {

        String img=SharedHelper.getKey(getApplicationContext(), UserConstant.userImage);
        Log.i("image_user_is","img>> "+img);

        if(!img.trim().equals("")){
            Picasso.get().load(SharedHelper.getKey(getApplicationContext(), UserConstant.userImage)).into(iv_profile_img);
            Picasso.get().load(SharedHelper.getKey(getApplicationContext(), UserConstant.userImage)).into(iv_profile_img_two);
        }


        tv_name.setText(SharedHelper.getKey(getApplicationContext(), UserConstant.name));

        String name_two = getIntent().getStringExtra("name");
        String image_two = getIntent().getStringExtra("image");
        user_two_id=getIntent().getStringExtra("id");

        user_one_id=SharedHelper.getKey(getApplicationContext(), UserConstant.id);
        token=SharedHelper.getKey(getApplicationContext(), UserConstant.userToken);


        Log.i("tokenandData",token+"\n"+user_one_id+"\n"+user_two_id);

        tv_name_two.setText(name_two);
        Picasso.get().load(image_two).into(iv_profile_img_second);


        layout_dice_movement.setOnClickListener(new PlayWithPlayer.HandleClick());

        nameTxt.setText(SharedHelper.getKey(PlayWithPlayer.this, UserConstant.name));
        img_back.setOnClickListener(this);

        //profile_r_layout.setOnClickListener(new HandleClick());


        rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(800);
        rotate.setInterpolator(new LinearInterpolator());

        //link handler to callback
        handler = new Handler(callback);
        Random random = new Random();
        int i = random.nextInt(2);
        if (i == 0) {
            displayMessage("Computer Turn");
            computerTurn();
        } else {
            gif_img.setVisibility(View.VISIBLE);
            displayMessage("Your Turn");
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
            case R.id.profile_r_layout:
                break;

            case R.id.iv_chat:

                showChatDialog();

                break;



            case R.id.layout_coin_two:

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
                timer.schedule(new PlayWithPlayer.Roll(), 1100);
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

            rolling = false;  //user can press again
            int i = dice1_number + dice2_number;
            //Toast.makeText(PlayWithComputerActivity.this, "Total Number : "+i, Toast.LENGTH_SHORT).show();
           /* String text=String.valueOf(dice1_number);
            String text2=String.valueOf(dice2_number);*/


            if (i == 2) {

                if (txt_two.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                    txt_two.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                } else {
                    if (txt_two.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark)) {
                        // txt_two.setTextColor(getResources().getColor(R.color.colorPrimaryDark));


                        if (dice1_number == 1 && dice2_number == 1) {
                            txt_one.setTextColor(getResources().getColor(R.color.coloryellow));

                        }

                    } else if (txt_two.getCurrentTextColor() == getResources().getColor(R.color.coloryellow)) {
                        if (dice1_number == 1 && dice2_number == 1) {
                            txt_one.setTextColor(getResources().getColor(R.color.coloryellow));

                        }
                    }
                }


            }


            else if (i == 3) {
                if (txt_three.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                    txt_three.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                } else {
                    if (txt_three.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark)) {
                        // txt_two.setTextColor(getResources().getColor(R.color.colorPrimaryDark));


                        if (dice1_number == 1 && dice2_number == 2) {
                            if (txt_one.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_one.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                            if (txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_two.setTextColor(getResources().getColor(R.color.coloryellow));
                            }

                        } else if (dice1_number == 2 && dice2_number == 1) {
                            if (txt_one.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_one.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                            if (txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_two.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                        }

                    } else if (txt_three.getCurrentTextColor() == getResources().getColor(R.color.coloryellow)) {
                        // txt_two.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                        if (dice1_number == 1 && dice2_number == 2) {
                            if (txt_one.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_one.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                            if (txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_two.setTextColor(getResources().getColor(R.color.coloryellow));
                            }

                        } else if (dice1_number == 2 && dice2_number == 1) {
                            if (txt_one.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_one.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                            if (txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_two.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                        }
                    }
                }

            }



            else if (i == 4) {
                if (txt_four.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                    txt_four.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                } else {

                    if (txt_four.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark)) {
                        // txt_two.setTextColor(getResources().getColor(R.color.colorPrimaryDark));


                        if (dice1_number == 1 && dice2_number == 3) {
                            if (txt_one.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_one.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                            if (txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_three.setTextColor(getResources().getColor(R.color.coloryellow));
                            }

                        } else if (dice1_number == 2 && dice2_number == 2) {
                            if (txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_two.setTextColor(getResources().getColor(R.color.coloryellow));
                            }


                        } else if (dice1_number == 3 && dice2_number == 1) {
                            if (txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_three.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                            if (txt_one.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_one.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                        }

                    } else if (txt_four.getCurrentTextColor() == getResources().getColor(R.color.coloryellow)) {
                        // txt_two.setTextColor(getResources().getColor(R.color.colorPrimaryDark));


                        if (dice1_number == 1 && dice2_number == 3) {
                            if (txt_one.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_one.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                            if (txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_three.setTextColor(getResources().getColor(R.color.coloryellow));
                            }

                        } else if (dice1_number == 2 && dice2_number == 2) {
                            if (txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_two.setTextColor(getResources().getColor(R.color.coloryellow));
                            }


                        } else if (dice1_number == 3 && dice2_number == 1) {
                            if (txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_three.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                            if (txt_one.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_one.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                        }
                    }
                }

            }


            else if (i == 5) {
                if (txt_five.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                    txt_five.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                } else {


                    if (txt_five.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark)) {
                        // txt_two.setTextColor(getResources().getColor(R.color.colorPrimaryDark));


                        if (dice1_number == 1 && dice2_number == 4) {
                            if (txt_one.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_one.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                            if (txt_four.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_four.setTextColor(getResources().getColor(R.color.coloryellow));
                            }

                        } else if (dice1_number == 2 && dice2_number == 3) {
                            if (txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_two.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                            if (txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_three.setTextColor(getResources().getColor(R.color.coloryellow));
                            }


                        } else if (dice1_number == 4 && dice2_number == 1) {
                            if (txt_four.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_four.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                            if (txt_one.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_one.setTextColor(getResources().getColor(R.color.coloryellow));
                            }

                        } else if (dice1_number == 3 && dice2_number == 2) {
                            if (txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_three.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                            if (txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_two.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                        }

                    } else if (txt_five.getCurrentTextColor() == getResources().getColor(R.color.coloryellow)) {
                        // txt_two.setTextColor(getResources().getColor(R.color.colorPrimaryDark));


                        if (dice1_number == 1 && dice2_number == 4) {
                            if (txt_one.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_one.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                            if (txt_four.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_four.setTextColor(getResources().getColor(R.color.coloryellow));
                            }

                        } else if (dice1_number == 2 && dice2_number == 3) {
                            if (txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_two.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                            if (txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_three.setTextColor(getResources().getColor(R.color.coloryellow));
                            }


                        } else if (dice1_number == 4 && dice2_number == 1) {
                            if (txt_four.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_four.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                            if (txt_one.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_one.setTextColor(getResources().getColor(R.color.coloryellow));
                            }

                        } else if (dice1_number == 3 && dice2_number == 2) {
                            if (txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_three.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                            if (txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_two.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                        }
                    }
                }

            }





            else if (i == 6) {
                if (txt_six.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                    txt_six.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                } else {

                    if (txt_six.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark)) {
                        // txt_two.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                        if (dice1_number == 1 && dice2_number == 5) {
                            if (txt_one.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_one.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                            if (txt_five.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_five.setTextColor(getResources().getColor(R.color.coloryellow));
                            }

                        } else if (dice1_number == 2 && dice2_number == 4) {
                            if (txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_two.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                            if (txt_four.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_four.setTextColor(getResources().getColor(R.color.coloryellow));
                            }


                        } else if (dice1_number == 3 && dice2_number == 3) {
                            if (txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_three.setTextColor(getResources().getColor(R.color.coloryellow));
                            }


                        } else if (dice1_number == 5 && dice2_number == 1) {
                            if (txt_five.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_five.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                            if (txt_one.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_one.setTextColor(getResources().getColor(R.color.coloryellow));
                            }

                        } else if (dice1_number == 4 && dice2_number == 2) {
                            if (txt_four.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_four.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                            if (txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_two.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                        }

                    } else if (txt_six.getCurrentTextColor() == getResources().getColor(R.color.coloryellow)) {
                        // txt_two.setTextColor(getResources().getColor(R.color.colorPrimaryDark));


                        if (dice1_number == 1 && dice2_number == 5) {
                            if (txt_one.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_one.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                            if (txt_five.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_five.setTextColor(getResources().getColor(R.color.coloryellow));
                            }

                        } else if (dice1_number == 2 && dice2_number == 4) {
                            if (txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_two.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                            if (txt_four.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_four.setTextColor(getResources().getColor(R.color.coloryellow));
                            }


                        } else if (dice1_number == 3 && dice2_number == 3) {
                            if (txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_three.setTextColor(getResources().getColor(R.color.coloryellow));
                            }


                        } else if (dice1_number == 5 && dice2_number == 1) {
                            if (txt_five.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_five.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                            if (txt_one.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_one.setTextColor(getResources().getColor(R.color.coloryellow));
                            }

                        } else if (dice1_number == 4 && dice2_number == 2) {
                            if (txt_four.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_four.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                            if (txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_two.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                        }
                    }
                }

            } else if (i == 7) {
                if (txt_seven.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                    txt_seven.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                } else {


                    if (txt_seven.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark)) {
                        // txt_two.setTextColor(getResources().getColor(R.color.colorPrimaryDark));


                        if (dice1_number == 1 && dice2_number == 6) {
                            if (txt_one.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_one.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                            if (txt_six.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_six.setTextColor(getResources().getColor(R.color.coloryellow));
                            }

                        } else if (dice1_number == 2 && dice2_number == 5) {
                            if (txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_two.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                            if (txt_five.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_five.setTextColor(getResources().getColor(R.color.coloryellow));
                            }


                        } else if (dice1_number == 3 && dice2_number == 4) {
                            if (txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_three.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                            if (txt_four.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_four.setTextColor(getResources().getColor(R.color.coloryellow));
                            }


                        } else if (dice1_number == 6 && dice2_number == 1) {
                            if (txt_six.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_six.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                            if (txt_one.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_one.setTextColor(getResources().getColor(R.color.coloryellow));
                            }

                        } else if (dice1_number == 5 && dice2_number == 2) {
                            if (txt_five.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_five.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                            if (txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_two.setTextColor(getResources().getColor(R.color.coloryellow));
                            }

                        } else if (dice1_number == 4 && dice2_number == 3) {
                            if (txt_four.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_four.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                            if (txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_three.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                        }


                    } else if (txt_seven.getCurrentTextColor() == getResources().getColor(R.color.coloryellow)) {
                        // txt_two.setTextColor(getResources().getColor(R.color.colorPrimaryDark));


                        if (dice1_number == 1 && dice2_number == 6) {
                            if (txt_one.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_one.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                            if (txt_six.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_six.setTextColor(getResources().getColor(R.color.coloryellow));
                            }

                        } else if (dice1_number == 2 && dice2_number == 5) {
                            if (txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_two.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                            if (txt_five.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_five.setTextColor(getResources().getColor(R.color.coloryellow));
                            }


                        } else if (dice1_number == 3 && dice2_number == 4) {
                            if (txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_three.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                            if (txt_four.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_four.setTextColor(getResources().getColor(R.color.coloryellow));
                            }


                        } else if (dice1_number == 6 && dice2_number == 1) {
                            if (txt_six.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_six.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                            if (txt_one.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_one.setTextColor(getResources().getColor(R.color.coloryellow));
                            }

                        } else if (dice1_number == 5 && dice2_number == 2) {
                            if (txt_five.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_five.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                            if (txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_two.setTextColor(getResources().getColor(R.color.coloryellow));
                            }

                        } else if (dice1_number == 4 && dice2_number == 3) {
                            if (txt_four.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_four.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                            if (txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_three.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                        }
                    }
                }

            } else if (i == 8) {


                if (txt_eight.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                    txt_eight.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                } else {


                    if (txt_eight.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark)) {
                        // txt_two.setTextColor(getResources().getColor(R.color.colorPrimaryDark));


                        if (dice1_number == 2 && dice2_number == 6) {
                            if (txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_two.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                            if (txt_six.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_six.setTextColor(getResources().getColor(R.color.coloryellow));
                            }

                        } else if (dice1_number == 3 && dice2_number == 5) {
                            if (txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_three.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                            if (txt_five.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_five.setTextColor(getResources().getColor(R.color.coloryellow));
                            }


                        } else if (dice1_number == 4 && dice2_number == 4) {
                            if (txt_four.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_four.setTextColor(getResources().getColor(R.color.coloryellow));
                            }


                        } else if (dice1_number == 6 && dice2_number == 2) {
                            if (txt_six.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_six.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                            if (txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_two.setTextColor(getResources().getColor(R.color.coloryellow));
                            }

                        } else if (dice1_number == 5 && dice2_number == 3) {
                            if (txt_five.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_five.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                            if (txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_three.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                        }

                    } else if (txt_eight.getCurrentTextColor() == getResources().getColor(R.color.coloryellow)) {
                        // txt_two.setTextColor(getResources().getColor(R.color.colorPrimaryDark));


                        if (dice1_number == 2 && dice2_number == 6) {
                            if (txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_two.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                            if (txt_six.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_six.setTextColor(getResources().getColor(R.color.coloryellow));
                            }

                        } else if (dice1_number == 3 && dice2_number == 5) {
                            if (txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_three.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                            if (txt_five.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_five.setTextColor(getResources().getColor(R.color.coloryellow));
                            }


                        } else if (dice1_number == 4 && dice2_number == 4) {
                            if (txt_four.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_four.setTextColor(getResources().getColor(R.color.coloryellow));
                            }


                        } else if (dice1_number == 6 && dice2_number == 2) {
                            if (txt_six.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_six.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                            if (txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_two.setTextColor(getResources().getColor(R.color.coloryellow));
                            }

                        } else if (dice1_number == 5 && dice2_number == 3) {
                            if (txt_five.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_five.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                            if (txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_three.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                        }
                    }
                }
            } else if (i == 9) {
                if (txt_nine.getCurrentTextColor() == getResources().getColor(R.color.colorWhite)) {
                    txt_nine.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                } else {
                    if (txt_nine.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark)) {
                        // txt_two.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                        if (dice1_number == 3 && dice2_number == 6) {
                            if (txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_three.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                            if (txt_six.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_six.setTextColor(getResources().getColor(R.color.coloryellow));
                            }

                        } else if (dice1_number == 4 && dice2_number == 5) {
                            if (txt_four.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_four.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                            if (txt_five.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_five.setTextColor(getResources().getColor(R.color.coloryellow));
                            }


                        } else if (dice1_number == 6 && dice2_number == 3) {
                            if (txt_six.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_six.setTextColor(getResources().getColor(R.color.coloryellow));
                            }

                            if (txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_three.setTextColor(getResources().getColor(R.color.coloryellow));
                            }


                        } else if (dice1_number == 5 && dice2_number == 4) {
                            if (txt_five.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_five.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                            if (txt_four.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_four.setTextColor(getResources().getColor(R.color.coloryellow));
                            }

                        }


                    } else if (txt_nine.getCurrentTextColor() == getResources().getColor(R.color.coloryellow)) {
                        // txt_two.setTextColor(getResources().getColor(R.color.colorPrimaryDark));


                        if (dice1_number == 3 && dice2_number == 6) {
                            if (txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_three.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                            if (txt_six.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_six.setTextColor(getResources().getColor(R.color.coloryellow));
                            }

                        } else if (dice1_number == 4 && dice2_number == 5) {
                            if (txt_four.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_four.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                            if (txt_five.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_five.setTextColor(getResources().getColor(R.color.coloryellow));
                            }


                        } else if (dice1_number == 6 && dice2_number == 3) {
                            if (txt_six.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_six.setTextColor(getResources().getColor(R.color.coloryellow));
                            }

                            if (txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_three.setTextColor(getResources().getColor(R.color.coloryellow));
                            }


                        } else if (dice1_number == 5 && dice2_number == 4) {
                            if (txt_five.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_five.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                            if (txt_four.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                                txt_four.setTextColor(getResources().getColor(R.color.coloryellow));
                            }
                        }
                    }
                }
            }
            else if (i == 10) {
                if (dice1_number == 4 && dice2_number == 6) {
                    if (txt_four.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                        txt_four.setTextColor(getResources().getColor(R.color.coloryellow));
                    }
                    if (txt_six.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                        txt_six.setTextColor(getResources().getColor(R.color.coloryellow));
                    }

                } else if (dice1_number == 5 && dice2_number == 5) {
                    if (txt_five.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                        txt_five.setTextColor(getResources().getColor(R.color.coloryellow));
                    }


                } else if (dice1_number == 6 && dice2_number == 4) {
                    if (txt_six.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                        txt_six.setTextColor(getResources().getColor(R.color.coloryellow));
                    }

                    if (txt_four.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                        txt_four.setTextColor(getResources().getColor(R.color.coloryellow));
                    }
                }

            } else if (i == 11) {

                if (dice1_number == 5 && dice2_number == 6) {
                    if (txt_five.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                        txt_five.setTextColor(getResources().getColor(R.color.coloryellow));
                    }
                    if (txt_six.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                        txt_six.setTextColor(getResources().getColor(R.color.coloryellow));
                    }

                } else if (dice1_number == 6 && dice2_number == 5) {
                    if (txt_six.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                        txt_six.setTextColor(getResources().getColor(R.color.coloryellow));
                    }
                    if (txt_five.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                        txt_five.setTextColor(getResources().getColor(R.color.coloryellow));
                    }
                }

            } else if (i == 12) {

                if (dice1_number == 6 && dice2_number == 6) {
                    if (txt_six.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark)) {
                        txt_six.setTextColor(getResources().getColor(R.color.coloryellow));
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
                            if (txt_one.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark) || txt_one.getCurrentTextColor() != getResources().getColor(R.color.coloryellow)) {
                                if (txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark) || txt_two.getCurrentTextColor() != getResources().getColor(R.color.coloryellow)) {
                                    if (txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark) || txt_three.getCurrentTextColor() != getResources().getColor(R.color.coloryellow)) {
                                        if (txt_four.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark) || txt_four.getCurrentTextColor() != getResources().getColor(R.color.coloryellow)) {
                                            if (txt_five.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark) || txt_five.getCurrentTextColor() != getResources().getColor(R.color.coloryellow)) {
                                                if (txt_six.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark) || txt_six.getCurrentTextColor() != getResources().getColor(R.color.coloryellow)) {
                                                    if (txt_seven.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark) || txt_seven.getCurrentTextColor() != getResources().getColor(R.color.coloryellow)) {
                                                        if (txt_eight.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark) || txt_eight.getCurrentTextColor() != getResources().getColor(R.color.coloryellow)) {
                                                            if (txt_nine.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark) || txt_nine.getCurrentTextColor() != getResources().getColor(R.color.coloryellow)) {

                                                                computerTurn();

                                                                displayMessage("Computer Turn");
                                                                gif_img.setVisibility(View.GONE);

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
                    if (txt_one.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark) || txt_one.getCurrentTextColor() != getResources().getColor(R.color.coloryellow)) {
                        if (txt_two.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark) || txt_two.getCurrentTextColor() != getResources().getColor(R.color.coloryellow)) {
                            if (txt_three.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark) || txt_three.getCurrentTextColor() != getResources().getColor(R.color.coloryellow)) {
                                if (txt_four.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark) || txt_four.getCurrentTextColor() != getResources().getColor(R.color.coloryellow)) {
                                    if (txt_five.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark) || txt_five.getCurrentTextColor() != getResources().getColor(R.color.coloryellow)) {
                                        if (txt_six.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark) || txt_six.getCurrentTextColor() != getResources().getColor(R.color.coloryellow)) {
                                            if (txt_seven.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark) || txt_seven.getCurrentTextColor() != getResources().getColor(R.color.coloryellow)) {
                                                if (txt_eight.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark) || txt_eight.getCurrentTextColor() != getResources().getColor(R.color.coloryellow)) {
                                                    if (txt_nine.getCurrentTextColor() != getResources().getColor(R.color.colorPrimaryDark) || txt_nine.getCurrentTextColor() != getResources().getColor(R.color.coloryellow)) {

                                                        layout_dice_movement.setEnabled(true);
                                                        displayMessage("Your Turn");
                                                        gif_img.setVisibility(View.VISIBLE);

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


            Log.e("TAG", "handleMessage: "+run );
            if (!run){
                if (txt_one.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_one.getCurrentTextColor() == getResources().getColor(R.color.coloryellow)) {
                    if (txt_two.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_two.getCurrentTextColor() == getResources().getColor(R.color.coloryellow)) {
                        if (txt_three.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_three.getCurrentTextColor() == getResources().getColor(R.color.coloryellow)) {
                            if (txt_four.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_four.getCurrentTextColor() == getResources().getColor(R.color.coloryellow)) {
                                if (txt_five.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_five.getCurrentTextColor() == getResources().getColor(R.color.coloryellow)) {
                                    if (txt_six.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_six.getCurrentTextColor() == getResources().getColor(R.color.coloryellow)) {
                                        if (txt_seven.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_seven.getCurrentTextColor() == getResources().getColor(R.color.coloryellow)) {
                                            if (txt_eight.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_eight.getCurrentTextColor() == getResources().getColor(R.color.coloryellow)) {
                                                if (txt_nine.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_nine.getCurrentTextColor() == getResources().getColor(R.color.coloryellow)){
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
                if (txt_one.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_one.getCurrentTextColor() == getResources().getColor(R.color.coloryellow)) {
                    if (txt_two.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_two.getCurrentTextColor() == getResources().getColor(R.color.coloryellow)) {
                        if (txt_three.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_three.getCurrentTextColor() == getResources().getColor(R.color.coloryellow)) {
                            if (txt_four.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_four.getCurrentTextColor() == getResources().getColor(R.color.coloryellow)) {
                                if (txt_five.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_five.getCurrentTextColor() == getResources().getColor(R.color.coloryellow)) {
                                    if (txt_six.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_six.getCurrentTextColor() == getResources().getColor(R.color.coloryellow)) {
                                        if (txt_seven.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_seven.getCurrentTextColor() == getResources().getColor(R.color.coloryellow)) {
                                            if (txt_eight.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_eight.getCurrentTextColor() == getResources().getColor(R.color.coloryellow)) {
                                                if (txt_nine.getCurrentTextColor() == getResources().getColor(R.color.colorPrimaryDark) || txt_nine.getCurrentTextColor() == getResources().getColor(R.color.coloryellow)){
                                                    if (isLastTurn) {


                                                        layout_dice_movement.setEnabled(false);
                                                        //   displayMessage("You are Winner");
                                                        showDialog("WINNER!");
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






            return true;
        }
    };

    //Clean up
    protected void onPause() {
        super.onPause();
        dice_sound.pause(sound_id);
    }



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
                    timer.schedule(new PlayWithPlayer.Roll(), 1100);
                }


            }
        }, 2000);
    }

    private void displayMessage(String message) {
        Toast toast = Toast.makeText(PlayWithPlayer.this, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }

    public void showDialog(String msg) {

        final Dialog dialog = new Dialog(PlayWithPlayer.this);
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

        final Dialog dialog = new Dialog(PlayWithPlayer.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        View view = getLayoutInflater().inflate(R.layout.exit_game_dialog, null);
        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ImageView yes_img = view.findViewById(R.id.yes_img);
        ImageView sound_img = view.findViewById(R.id.sound_img);
        ImageView music_img = view.findViewById(R.id.music_img);
        ImageView no_img = view.findViewById(R.id.no_img);

        isMusic = SharedHelper.getBooleanKey(PlayWithPlayer.this, UserConstant.isMusic);


        if (isMusic) {
            music_img.setImageResource(R.drawable.mute_neww);

        } else {
            music_img.setImageResource(R.drawable.mute_neww);
        }

        isSound = SharedHelper.getBooleanKey(PlayWithPlayer.this, UserConstant.isSound);

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
                timer.cancel();
                dialog.dismiss();
                finish();
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



        Dialog dialog = new Dialog(PlayWithPlayer.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_chat_dialog_view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        ImageView iv_close_chat=dialog.findViewById(R.id.iv_close_chat);

        RecyclerView recylerview=dialog.findViewById(R.id.recylerview);

        NestedScrollView nested_scroll_view=dialog.findViewById(R.id.nested_scroll_view);



        EditText et_msg=dialog.findViewById(R.id.et_msg);

        RelativeLayout layout_send_msg=dialog.findViewById(R.id.layout_send_msg);

        iv_close_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        layout_send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!et_msg.getText().toString().trim().equals("")){
                    apiCallSendMsg(et_msg.getText().toString().trim(),recylerview,nested_scroll_view);
                    et_msg.setText("");
                }else {
                    Toast.makeText(getApplicationContext(),"Message Empty!",Toast.LENGTH_LONG).show();
                }

            }
        });

        apiCallGetMessages(recylerview,nested_scroll_view);

    }



    private void apiCallSendMsg(String msg,RecyclerView recyclerView,NestedScrollView nestedScrollView) {

        ProgressDialog dialog=new ProgressDialog(PlayWithPlayer.this);
        dialog.setTitle("Loading");
        dialog.setMessage("Please wait...");

        dialog.show();
        ApiInterface apiInterface = AppConfig.getRetrofit().create(ApiInterface.class);


        Call<ModelChatMsg> call = apiInterface.chat_msg(
                "Bearer "+token,
                 user_one_id,
                user_two_id,
                 msg);



        call.enqueue(new Callback<ModelChatMsg>() {
            @Override
            public void onResponse(Call<ModelChatMsg> call, Response<ModelChatMsg> response) {

                Log.i("response_code_",response.code()+"");
                Log.i("response_code_",response.message()+"");

                dialog.dismiss();

                Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_LONG).show();

                if(response.isSuccessful()){

                    if(response.body().getStatus()){

                        apiCallGetMessages(recyclerView,nestedScrollView);

                    }

                }





            }
            @Override
            public void onFailure(Call<ModelChatMsg> call, Throwable t) {
                Log.i("fail_login",t.toString());
                dialog.dismiss();
                CustomToast.displayError(getApplicationContext(),t.getMessage());
                if (t instanceof SocketTimeoutException){

                }
            }
        });

    }

    private void apiCallGetMessages(RecyclerView recyclerView,NestedScrollView nestedScrollView) {

        ProgressDialog dialog=new ProgressDialog(PlayWithPlayer.this);
        dialog.setTitle("Loading");
        dialog.setMessage("Please wait...");

        dialog.show();
        ApiInterface apiInterface = AppConfig.getRetrofit().create(ApiInterface.class);


        Call<ModelGetMsg> call = apiInterface.get_msg(
                "Bearer "+token,
                user_one_id,
                user_two_id);



        call.enqueue(new Callback<ModelGetMsg>() {
            @Override
            public void onResponse(Call<ModelGetMsg> call, Response<ModelGetMsg> response) {

                Log.i("response_code_",response.code()+"");
                Log.i("response_code_",response.message()+"");

                dialog.dismiss();



                if(response.isSuccessful()){

                    if(response.body().getStatus()){
                        //Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_LONG).show();
                        if(response.body().getData().size()>0){
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
                Log.i("fail_login",t.toString());
                dialog.dismiss();
                CustomToast.displayError(getApplicationContext(),t.getMessage());
                if (t instanceof SocketTimeoutException){

                }
            }
        });

    }






    // Socket Coding

    private void InitSocket() {

        try {
            socket = IO.socket("http://141.136.36.151:6050");
            socket.connect();
            Toast.makeText(getApplicationContext(),"Connected",Toast.LENGTH_LONG).show();

            socket.on("playEvent", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    final String data = (String) args[0];
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           // String olddata=listenData.getText().toString().trim();
                            //listenData.setText(olddata.concat("\n"+data));
                        }
                    });
                }
            });

        }
        catch (URISyntaxException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
        }

    }


    protected void onDestroy() {
        socket.disconnect();
        //Toast.makeText(getApplicationContext(),"DisConnected",Toast.LENGTH_LONG).show();
        super.onDestroy();
        timer.cancel();
    }




}