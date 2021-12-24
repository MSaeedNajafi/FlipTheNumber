package com.flipthenumber.beta.ui.fragments.Tournaments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.flipthenumber.beta.Constants.UserConstant;
import com.flipthenumber.beta.Helper.CustomToast;
import com.flipthenumber.beta.Helper.SharedHelper;
import com.flipthenumber.beta.model.ModelRoundTime;
import com.flipthenumber.beta.model.ModelStartTime;
import com.flipthenumber.beta.model.ModeltournamentEntry;
import com.flipthenumber.beta.retrofit.ApiInterface;
import com.flipthenumber.beta.retrofit.AppConfig;
import com.flipthenumber.beta.MainActivity;
import com.flipthenumber.beta.R;
import com.squareup.picasso.Picasso;

import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EntryFeeFragment extends Fragment implements View.OnClickListener {
    private RelativeLayout layout_pay,layout_play;
    private int tournament_id;
    private String tournament_name,tournament_image,total_player,tournament_player;
    private String round="";
    private TextView name_txt,tv_now_totel_player,tv_total;
    private ProgressBar progressBar;
    private ImageView iv_tournament;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.entry_fee_fragment, container, false);
        init(view);
        initUI();

        return view;
    }

    private void init(View view){
        Log.i("UsreIdHere", SharedHelper.getKey(getActivity(), UserConstant.id));

        layout_pay=view.findViewById(R.id.layout_pay);

        iv_tournament=view.findViewById(R.id.img);

        layout_play=view.findViewById(R.id.layout_play);

        name_txt=view.findViewById(R.id.name_txt);

        tv_now_totel_player=view.findViewById(R.id.tv_now_totel_player);

        tv_total=view.findViewById(R.id.tv_total);

        progressBar=view.findViewById(R.id.progressBar);

        ClickListener();

    }

    private void ClickListener() {
        layout_pay.setOnClickListener(this);
        layout_play.setOnClickListener(this);

    }


    private void initUI(){

        getTournamentData();

    }


    private void getTournamentData() {

         Bundle args = getArguments();

         tournament_id = args.getInt("tournament_id", 0);
         tournament_name = args.getString("tournament_name", "");
         tournament_image = args.getString("tournament_image", "");
         total_player = args.getString("total_player", "");
         tournament_player = args.getString("tournament_player", "");

         try {
             round=args.getString("round","");
             Log.i("RoundIs",round);
         }
         catch (Exception e){
             Log.i("RoundIs",round);
         }

         name_txt.setText(tournament_name);
         Picasso.get().load(tournament_image).into(iv_tournament);
         tv_now_totel_player.setText(total_player);
         tv_total.setText(tournament_player);

         progressBar.setProgress(Integer.parseInt(total_player));

         if(total_player.equals("16")){
             layout_play.setVisibility(View.VISIBLE);
             layout_pay.setVisibility(View.GONE);
         }
         else {
             layout_play.setVisibility(View.GONE);
             layout_pay.setVisibility(View.VISIBLE);
         }

         Log.i("DataIsHere",tournament_id+tournament_name+tournament_image+total_player+tournament_player);

    }


    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.layout_pay:

                String token = SharedHelper.getKey(getActivity(), UserConstant.userToken);

                Log.i("SendingDataIs","Token"+token+"\n"+String.valueOf(tournament_id)
                +"\n"+ SharedHelper.getKey(getActivity(), UserConstant.id));

                Log.i("TokenIsentryTime ",token);

                ProgressDialog progressDialog=new ProgressDialog(getActivity());
                progressDialog.setTitle("Loading");
                progressDialog.show();
                ApiInterface apiInterface = AppConfig.getRetrofit().create(ApiInterface.class);
                Call<ModeltournamentEntry> call = apiInterface.tournamentEntry("Bearer "+token,String.valueOf(tournament_id),
                        SharedHelper.getKey(getActivity(), UserConstant.id),"25");
                call.enqueue(new Callback<ModeltournamentEntry>() {
                    @Override
                    public void onResponse(Call<ModeltournamentEntry> call, Response<ModeltournamentEntry> response) {
                        progressDialog.dismiss();

                        if (response.isSuccessful() ) {

                            Toast.makeText(getActivity(),response.body().getMessage(),Toast.LENGTH_LONG).show();


                            try {

                                SharedHelper.putKey(getActivity(),UserConstant.coin,response.body().getData().getTotalToken()+"");
                                MainActivity.total_coin_txt.setText(SharedHelper.getKey(getActivity(), UserConstant.coin));

                            }catch (Exception e){

                            }

                            if(response.body().getStatus()){


                            }

                        }

                        else {

                            Log.i("ResponseCodeIs",response.code()+"");

                        }


                    }

                    @Override
                    public void onFailure(Call<ModeltournamentEntry> call, Throwable t) {

                        Log.i("ErrorCodeIshere",t.toString());
                        progressDialog.dismiss();
                        if (t instanceof SocketTimeoutException) {

                        }

                    }
                });

                break;


                case R.id.layout_play:


                if(round.trim().equals("1")){
                    apiTournamentStart();
                }
                else {
                    apiRoundStart();
                }

                break;
        }
    }

    private void apiRoundStart() {
        ProgressDialog progressDialog=new ProgressDialog(getActivity());
        progressDialog.setTitle("Loading..");
        progressDialog.show();
        ApiInterface apiInterface = AppConfig.getRetrofit().create(ApiInterface.class);
        String token= SharedHelper.getKey(getActivity(), UserConstant.userToken);
        Call<ModelRoundTime> call = apiInterface.roundStartTime("Bearer " + token,String.valueOf(tournament_id));
        call.enqueue(new Callback<ModelRoundTime>() {
            @Override
            public void onResponse(Call<ModelRoundTime> call, Response<ModelRoundTime> response) {

                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        FragmentUntilRoundStart optionsFrag = new FragmentUntilRoundStart();
                        Bundle args = new Bundle();
                        args.putInt("tournament_id", tournament_id);
                        args.putString("tournament_name", tournament_name);
                        args.putString("tournament_image", tournament_image);
                        optionsFrag.setArguments(args);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,optionsFrag,"FragmentUntilRoundStart").addToBackStack(null).commit();

                    }

                    else {
                        CustomToast.displayError(getActivity(), response.body().getMessage());
                        FragmentUserListing optionsFrag = new FragmentUserListing();
                        Bundle args = new Bundle();
                        args.putInt("tournament_id", tournament_id);
                        args.putString("tournament_name", tournament_name);
                        args.putString("tournament_image", tournament_image);
                        optionsFrag.setArguments(args);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,optionsFrag,"FragmentUserListing").addToBackStack(null).commit();


                    }

                }


            }

            @Override
            public void onFailure(Call<ModelRoundTime> call, Throwable t) {
                Log.i("fail_login", t.toString());
                progressDialog.dismiss();
                CustomToast.displayError(getActivity(), t.getMessage());
                if (t instanceof SocketTimeoutException) {

                }
            }
        });
    }

    private void apiTournamentStart() {

        ProgressDialog progressDialog=new ProgressDialog(getActivity());
        progressDialog.setTitle("Loading..");
        progressDialog.show();
        ApiInterface apiInterface = AppConfig.getRetrofit().create(ApiInterface.class);
        String token= SharedHelper.getKey(getActivity(), UserConstant.userToken);
        Log.i("DataisHere",token+"\n"+String.valueOf(tournament_id));
        Call<ModelStartTime> call = apiInterface.tournamentStartTime("Bearer " + token,String.valueOf(tournament_id));
        call.enqueue(new Callback<ModelStartTime>() {
            @Override
            public void onResponse(Call<ModelStartTime> call, Response<ModelStartTime> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {

                    if (response.body().getStatus()) {

                        FragmentUntilTournamentStart optionsFrag = new FragmentUntilTournamentStart();
                        Bundle args = new Bundle();
                        args.putInt("tournament_id", tournament_id);
                        args.putString("tournament_name", tournament_name);
                        args.putString("tournament_image", tournament_image);
                        optionsFrag.setArguments(args);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,optionsFrag,"FragmentUntilTournamentStart").addToBackStack(null).commit();

                    }
                    else {
                        CustomToast.displayError(getActivity(), response.body().getMessage());
                        FragmentUserListing optionsFrag = new FragmentUserListing();
                        Bundle args = new Bundle();
                        args.putInt("tournament_id", tournament_id);
                        args.putString("tournament_name", tournament_name);
                        args.putString("tournament_image", tournament_image);
                        optionsFrag.setArguments(args);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,optionsFrag,"FragmentUserListing").addToBackStack(null).commit();

                    }

                }

            }

            @Override
            public void onFailure(Call<ModelStartTime> call, Throwable t) {
                progressDialog.dismiss();
                Log.i("fail_login", t.toString());

                CustomToast.displayError(getActivity(), t.getMessage());
                if (t instanceof SocketTimeoutException) {

                }
            }
        });
    }

}
