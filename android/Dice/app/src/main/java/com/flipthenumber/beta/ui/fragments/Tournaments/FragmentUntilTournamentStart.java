package com.flipthenumber.beta.ui.fragments.Tournaments;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.flipthenumber.beta.Constants.UserConstant;
import com.flipthenumber.beta.Helper.CustomToast;
import com.flipthenumber.beta.Helper.SharedHelper;
import com.flipthenumber.beta.MainActivity;
import com.flipthenumber.beta.R;
import com.flipthenumber.beta.model.ModelBet;
import com.flipthenumber.beta.model.ModelStartTime;
import com.flipthenumber.beta.retrofit.ApiInterface;
import com.flipthenumber.beta.retrofit.AppConfig;
import com.flipthenumber.beta.ui.activities.PlayPage.PlayVsPlayerSocket;
import com.squareup.picasso.Picasso;

import java.net.SocketTimeoutException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentUntilTournamentStart extends Fragment implements View.OnClickListener{
    private TextView tv_hour,tv_minutes,tv_seconds;
    private View view;
    int hour,minutes,seconds;
    long milliseconds;
    private int tournament_id;
    private String tournament_name,tournament_image;
    private ImageView img;
    private TextView name_txt;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_until_tournament_start, container, false);
        Init();
        InitUI();

        return view;

    }

    private void Init() {
        Bundle args = getArguments();

        tournament_id = args.getInt("tournament_id", 0);
        tournament_name = args.getString("tournament_name", "");
        tournament_image = args.getString("tournament_image", "");

        tv_hour=view.findViewById(R.id.tv_hour);
        tv_minutes=view.findViewById(R.id.tv_minutes);
        tv_seconds=view.findViewById(R.id.tv_seconds);

        img=view.findViewById(R.id.img);
        name_txt=view.findViewById(R.id.name_txt);

        name_txt.setText(tournament_name);
        Picasso.get().load(tournament_image).into(img);


        apiCall();
    }

    private void apiCall() {

        ApiInterface apiInterface = AppConfig.getRetrofit().create(ApiInterface.class);
        String token= SharedHelper.getKey(getActivity(), UserConstant.userToken);
        Call<ModelStartTime> call = apiInterface.tournamentStartTime("Bearer " + token,String.valueOf(tournament_id));
        call.enqueue(new Callback<ModelStartTime>() {
            @Override
            public void onResponse(Call<ModelStartTime> call, Response<ModelStartTime> response) {

                if (response.isSuccessful()) {
                    if (response.body().getStatus()) {

                        String values[] = response.body().getData().get(0).getRemaining_time().trim().split(":");

                        hour=Integer.parseInt(values[0].trim());
                        minutes=Integer.parseInt(values[1].trim());
                        seconds=Integer.parseInt(values[2].trim());

                        tv_hour.setText(String.valueOf(hour));
                        tv_minutes.setText(String.valueOf(minutes));
                        tv_seconds.setText(String.valueOf(seconds));



                        // Log.i("TimeRemainingIs",hour+":"+minutes+":"+seconds);

                        methodStartTimer();

                    }
                    else {
                        CustomToast.displayError(getActivity(), response.body().getMessage());
                    }

                }


            }

            @Override
            public void onFailure(Call<ModelStartTime> call, Throwable t) {
                Log.i("fail_login", t.toString());

                CustomToast.displayError(getActivity(), t.getMessage());
                if (t instanceof SocketTimeoutException) {

                }
            }
        });
    }

    private void methodStartTimer() {

        milliseconds=hour*60*60*1000;
        milliseconds=milliseconds+minutes*60*1000;
        milliseconds=milliseconds+seconds*1000;

        new CountDownTimer(milliseconds, 1000) {
            public void onTick(long millisUntilFinished) {
                NumberFormat f = new DecimalFormat("00");
                long hour = (millisUntilFinished / 3600000) % 24;
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;
                tv_hour.setText(f.format(hour));
                tv_minutes.setText(f.format(min));
                tv_seconds.setText(f.format(sec));
            }

            public void onFinish() {
                tv_hour.setText("00");
                tv_minutes.setText("00");
                tv_seconds.setText("00");
            }
        }.start();

    }



    private void InitUI() {

    }

    @Override
    public void onClick(View v) {

    }

}
