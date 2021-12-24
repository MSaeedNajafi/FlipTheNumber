package com.flipthenumber.beta.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.flipthenumber.beta.Constants.UserConstant;
import com.flipthenumber.beta.Helper.SharedHelper;
import com.flipthenumber.beta.model.ModelFilterUser;
import com.flipthenumber.beta.retrofit.ApiInterface;
import com.flipthenumber.beta.retrofit.AppConfig;
import com.flipthenumber.beta.ui.activities.PlayPage.PlayVsPlayerSocket;
import com.flipthenumber.beta.MainActivity;
import com.flipthenumber.beta.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import io.socket.client.IO;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityPlayerVsPlayerView extends AppCompatActivity {
    private CircleImageView iv_profile_img, iv_profile_img_two;
    private TextView tv_name, ttxt, tv_name_two, ttxt_two, tv_searchtime;
    private String img_two;
    int delay = 1000; // delay for 5 sec.
    int period = 1000; // repeat every sec.
    int t = 30;
    Timer timer;
    io.socket.client.Socket socket;
    String userid = "";
    String playerid = "";
    String playerName = "";
    String playerImg="";
    String playerImgEmit="";
    private String bet_status,bet_coins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_vs_player_view);
        Log.i("HereFragment","OneActivityVsPlayer");
        Init();
        InitUI();

    }

    private void Init() {
        iv_profile_img = findViewById(R.id.iv_profile_img);
        iv_profile_img_two = findViewById(R.id.iv_profile_img_two);
        tv_name = findViewById(R.id.tv_name);
        ttxt = findViewById(R.id.ttxt);
        tv_name_two = findViewById(R.id.tv_name_two);
        ttxt_two = findViewById(R.id.ttxt_two);
        tv_searchtime = findViewById(R.id.tv_searchtime);
        tv_searchtime.setText(String.valueOf(t));

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
           bet_status=extras.getString("bet_status");
           bet_coins=extras.getString("bet_coins");
        }

    }

    private void InitUI() {
        InitSocket();
        String coins = SharedHelper.getKey(getApplicationContext(), UserConstant.coin);
        ttxt.setText(coins);
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                t--;

                Log.i("ValueofTime", String.valueOf(t));

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_searchtime.setText(String.valueOf(t));

                        if (t == 0) {

                            timer.cancel();
                            Toast.makeText(getApplicationContext(), "No player found", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(ActivityPlayerVsPlayerView.this, MainActivity.class);
                            startActivity(intent);
                            finish();

                        }

                    }
                });

                //your code
            }
        }, delay, period);

        //setDataApi();


    }

    private void setDataApi() {

        String token = SharedHelper.getKey(getApplicationContext(), UserConstant.userToken);
        String userId = SharedHelper.getKey(getApplicationContext(), UserConstant.id);

        Log.i("tokenHomePage", token + "\n" + userId);

        ApiInterface apiInterface = AppConfig.getRetrofit().create(ApiInterface.class);
        Call<ModelFilterUser> call = apiInterface.filteruser("Bearer " + token, userId, "user");
        call.enqueue(new Callback<ModelFilterUser>() {
            @Override
            public void onResponse(Call<ModelFilterUser> call, Response<ModelFilterUser> response) {
                if (response.isSuccessful()) {

                    if (response.code() == 200) {

                        String img = SharedHelper.getKey(getApplicationContext(), UserConstant.userImage);
                        String name = SharedHelper.getKey(getApplicationContext(), UserConstant.name);
                        String coins = SharedHelper.getKey(getApplicationContext(), UserConstant.coin);


                        try {
                            if (!response.body().getData().get(0).getImage().equalsIgnoreCase("null")) {
                                img_two = response.body().getData().get(0).getImage();
                            }

                        } catch (Exception e) {
                            Log.i("hereIsError", "catch");
                            img_two = "";
                        }


                        String name_two = response.body().getData().get(0).getName();
                        String coins_two = response.body().getData().get(0).getTotal_token();


                        if (!img.trim().equals("")) {
                            Picasso.get().load(SharedHelper.getKey(getApplicationContext(), UserConstant.userImage)).into(iv_profile_img);
                        }

                        if (!name.equals("")) {
                            tv_name.setText(name);
                        }

                        if (!coins.equals("")) {
                            ttxt.setText(coins);
                        }


                        try {

                            if (!img.trim().equals("")) {
                                Picasso.get().load(img_two).into(iv_profile_img_two);
                            }

                        } catch (Exception e) {

                        }


                        if (!name_two.equals("")) {

                           // String upperString = name_two.substring(0, 1).toUpperCase() + name_two.substring(1).toLowerCase();

                            StringBuilder sb = new StringBuilder(name_two);
                            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));

                            tv_name_two.setText(sb.toString());



                        }

                        if (!coins_two.equals("")) {
                            ttxt_two.setText(coins_two);
                        }


                        Thread myThread = new Thread() {
                            @Override
                            public void run() {
                                try {

                                    sleep(3000);


                                    Log.i("user_id_Is", response.body().getData().get(0).getUserId() + "");

                                    Intent intent = new Intent(ActivityPlayerVsPlayerView.this, PlayVsPlayerSocket.class);
                                    intent.putExtra("name", name_two);
                                    intent.putExtra("image", img_two);
                                    intent.putExtra("id", response.body().getData().get(0).getUserId() + "");
                                    startActivity(intent);
                                    finish();


                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        myThread.start();


                    }
                }
            }

            @Override
            public void onFailure(Call<ModelFilterUser> call, Throwable t) {

            }
        });


    }

    // Socket Coding

    private void InitSocket() {

        try {

            socket = IO.socket("http://141.136.36.151:6005");
            socket.connect();
            Log.i("ImageIshere","imageIs"+SharedHelper.getKey(getApplicationContext(),UserConstant.userImage));
            if(SharedHelper.getKey(getApplicationContext(),UserConstant.userImage).equals("")){
                playerImgEmit="";
            }else {
                playerImgEmit=SharedHelper.getKey(getApplicationContext(),UserConstant.userImage);
            }
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                jsonObject.put("name", SharedHelper.getKey(getApplicationContext(), UserConstant.name));
                jsonObject.put("player_id", 0);
                jsonObject.put("player_img",playerImgEmit);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.i("jsonDataIs", jsonObject.toString());
            socket.emit("connectionPlay", jsonObject.toString());
            socket.on("connectionPlay", new Emitter.Listener() {
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
                                userid = String.valueOf(object.getInt("user_id"));
                                playerid = String.valueOf(object.getInt("player_id"));
                                Log.i("PlayerIdIs", "IdIs" + playerid);

                                if (playerid.equals(SharedHelper.getKey(getApplicationContext(), UserConstant.id))) {

                                    Log.i("HereIsPlayerId", "ifPart" + playerid);

                                    if (!userid.equals(SharedHelper.getKey(getApplicationContext(), UserConstant.id))) {


                                        playerName = object.getString("name");
                                        playerImg=object.getString("player_img");
                                        String user_id_player_id = object.getString("user_id_player_id");
                                        timer.cancel();
                                        StringBuilder sb = new StringBuilder(playerName);
                                        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
                                        tv_name_two.setText(sb.toString());
                                        socket.disconnect();
                                        Intent intent = new Intent(ActivityPlayerVsPlayerView.this, PlayVsPlayerSocket.class);
                                        intent.putExtra("name", playerName);
                                        intent.putExtra("id", userid);
                                        intent.putExtra("turn", "opponent");
                                        intent.putExtra("user_id_player_id", user_id_player_id);
                                        intent.putExtra("player_img", playerImg);
                                        intent.putExtra("bet_status",bet_status);
                                        intent.putExtra("bet_coins",bet_coins);
                                        startActivity(intent);
                                        finish();


                                    }

                                }

                                else {

                                    Log.i("HereIsPlayerId", "elsePart" + playerid);

                                    if (!userid.equals(SharedHelper.getKey(getApplicationContext(), UserConstant.id))) {

                                        playerName = object.getString("name");
                                        playerImg=object.getString("player_img");

                                        timer.cancel();

                                        StringBuilder sb = new StringBuilder(playerName);
                                        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
                                        tv_name_two.setText(sb.toString());

                                        JSONObject jsonObject = new JSONObject();
                                        try {
                                            jsonObject.put("user_id", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                                            jsonObject.put("name", SharedHelper.getKey(getApplicationContext(), UserConstant.name));
                                            jsonObject.put("player_id", Integer.parseInt(userid));
                                            jsonObject.put("user_id_player_id", SharedHelper.getKey(getApplicationContext(), UserConstant.id).concat(userid));
                                            jsonObject.put("player_img",playerImgEmit);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        Log.i("jsonDataIs", jsonObject.toString());

                                        socket.emit("connectionPlay", jsonObject.toString());

                                        socket.disconnect();

                                        Intent intent = new Intent(ActivityPlayerVsPlayerView.this, PlayVsPlayerSocket.class);
                                        intent.putExtra("name", playerName);
                                        intent.putExtra("id", userid);
                                        intent.putExtra("turn", "your");
                                        intent.putExtra("user_id_player_id", SharedHelper.getKey(getApplicationContext(), UserConstant.id).concat(userid));
                                        intent.putExtra("player_img", playerImg);
                                        intent.putExtra("bet_status",bet_status);
                                        intent.putExtra("bet_coins",bet_coins);
                                        startActivity(intent);
                                        finish();

                                    }
                                }


                            } catch (JSONException e) {

                                e.printStackTrace();
                            }

                            //dice1_number=object.getInt("dice1_number");
                            // if (!userid.equals(SharedHelper.getKey(getApplicationContext(),UserConstant.id))){

                        }
                    });
                }
            });


        } catch (URISyntaxException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }

    }


    protected void onDestroy() {
        socket.disconnect();
        super.onDestroy();
        timer.cancel();
    }


}