package com.flipthenumber.beta.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.flipthenumber.beta.Constants.UserConstant;
import com.flipthenumber.beta.Helper.SharedHelper;
import com.flipthenumber.beta.MainActivity;
import com.flipthenumber.beta.R;
import com.flipthenumber.beta.model.ModelFilterUser;
import com.flipthenumber.beta.retrofit.ApiInterface;
import com.flipthenumber.beta.retrofit.AppConfig;
import com.flipthenumber.beta.ui.activities.PlayPage.PlayVsPlayerSocket;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityPlayerVsPlayerViewNew extends AppCompatActivity {

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
        Log.i("HereFragment","OneActivityVsPlayerNew");
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
                            Intent intent = new Intent(ActivityPlayerVsPlayerViewNew.this, MainActivity.class);
                            startActivity(intent);
                            finish();

                        }

                    }
                });

                //your code
            }
        }, delay, period);




    }



    // Socket Coding

    private void InitSocket() {

        try {

            socket = IO.socket("http://141.136.36.151:3000");

            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {

                    Log.i("SocketListener","Connected to Server");
                    Log.d(getClass().getCanonicalName(), "Connected to server");

                    if(SharedHelper.getKey(getApplicationContext(),UserConstant.userImage).equals("")){
                        playerImgEmit="";
                    }else {
                        playerImgEmit=SharedHelper.getKey(getApplicationContext(),UserConstant.userImage);
                    }

                     Log.i("SocketListener", "EmitData"+ Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));
                     socket.emit("findPlayer", Integer.parseInt(SharedHelper.getKey(getApplicationContext(), UserConstant.id)));


                }

            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... arg0) {
                    Log.i("SocketListener","Disconnected to Server");
                    Log.d(getClass().getCanonicalName(), "Disconnected from server");
                }

            }).on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {

                @Override
                public void call(Object... arg0) {
                    Log.i("SocketListener","EVENT_CONNECT_ERROR");
                    Log.d(getClass().getCanonicalName(), "Disconnected from server");
                }

            });

            socket.on("findPlayerDetails", new Emitter.Listener() {
                @Override

                public void call(Object... args) {

                    final String data = args[0].toString();


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            JSONObject object = null;

                            try {
                                object = new JSONObject(data);

                                Log.i("SocketListener", "ListenData"+object.toString());

                                try {

                                    if(String.valueOf(object.getInt("player_id_one")).trim().equals(SharedHelper.getKey(getApplicationContext(), UserConstant.id))){

                                        playerName = object.getString("player_name_two");
                                        playerImg=object.getString("player_img_two");
                                        String user_id_player_id = object.getString("unique_id");
                                        String turn = object.getString("turn_one");
                                        String player_id = String.valueOf(object.getInt("player_id_two"));

                                        timer.cancel();
                                        StringBuilder sb = new StringBuilder(playerName);
                                        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
                                        tv_name_two.setText(sb.toString());
                                        socket.disconnect();

                                        Intent intent = new Intent(ActivityPlayerVsPlayerViewNew.this, PlayVsPlayerSocket.class);
                                        intent.putExtra("name", playerName);
                                        intent.putExtra("id", player_id);
                                        intent.putExtra("turn", turn);
                                        intent.putExtra("user_id_player_id", user_id_player_id);
                                        intent.putExtra("player_img", playerImg);
                                        intent.putExtra("bet_status",bet_status);
                                        intent.putExtra("bet_coins",bet_coins);
                                        startActivity(intent);
                                        finish();
                                    }

                                    else {
                                        playerName = object.getString("player_name_one");
                                        playerImg=object.getString("player_img_one");
                                        String user_id_player_id = object.getString("unique_id");
                                        String turn = object.getString("turn_two");
                                        String player_id = String.valueOf(object.getInt("player_id_one"));

                                        timer.cancel();
                                        StringBuilder sb = new StringBuilder(playerName);
                                        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
                                        tv_name_two.setText(sb.toString());
                                        socket.disconnect();

                                        Intent intent = new Intent(ActivityPlayerVsPlayerViewNew.this, PlayVsPlayerSocket.class);
                                        intent.putExtra("name", playerName);
                                        intent.putExtra("id", player_id);
                                        intent.putExtra("turn", turn);
                                        intent.putExtra("user_id_player_id", user_id_player_id);
                                        intent.putExtra("player_img", playerImg);
                                        intent.putExtra("bet_status",bet_status);
                                        intent.putExtra("bet_coins",bet_coins);
                                        startActivity(intent);
                                        finish();

                                    }





                                }
                                catch (Exception e){

                                    Log.i("SocketListener", "ErrorCatch"+e.toString());

                                }

                            } catch (JSONException e) {

                                e.printStackTrace();
                            }

                        }
                    });
                }
            });

            socket.connect();


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