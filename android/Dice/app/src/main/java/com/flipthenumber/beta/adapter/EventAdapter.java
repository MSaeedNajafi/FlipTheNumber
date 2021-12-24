package com.flipthenumber.beta.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flipthenumber.beta.Constants.UserConstant;
import com.flipthenumber.beta.retrofit.ApiInterface;
import com.flipthenumber.beta.retrofit.AppConfig;
import com.flipthenumber.beta.Helper.SharedHelper;
import com.flipthenumber.beta.MainActivity;
import com.flipthenumber.beta.R;
import com.flipthenumber.beta.model.EventModel;
import com.flipthenumber.beta.model.ModeltournamentplayCount;
import com.flipthenumber.beta.ui.fragments.Tournaments.EntryFeeFragment;
import com.flipthenumber.beta.ui.fragments.Tournaments.FragmentUntilRoundStart;
import com.flipthenumber.beta.ui.fragments.Tournaments.FragmentUntilTournamentStart;
import com.squareup.picasso.Picasso;

import java.net.SocketTimeoutException;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {
    Context mContext;
    EventModel model;

    public EventAdapter(Context mContext, EventModel model) {
        this.mContext = mContext;
        this.model = model;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Picasso.get().load( model.getData().get(position).getImage()).into(holder.img);
        holder.name_txt.setText(model.getData().get(position).getName());

        Log.i("TorunamentId",model.getData().get(position).getId());


        holder.layout_tournament.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  FragmentUntilRoundStart optionsFrag = new FragmentUntilRoundStart();

               // ((MainActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.container,optionsFrag,"FragmentUntilTournamentStart").addToBackStack(null).commit();


                if(model.getData().get(position).getLocked().equals("0")){

                    Log.i("TorunamentId",model.getData().get(position).getId());
                String token = SharedHelper.getKey(mContext, UserConstant.userToken);
                Log.i("TokenIsWhenSelect",token);


                Log.i("DataIsHere",token+"\n"+
                        model.getData().get(position).getId()+"\n"+
                        SharedHelper.getKey(mContext, UserConstant.id));

                ProgressDialog progressDialog=new ProgressDialog(mContext);
                progressDialog.setTitle("Loading..");
                progressDialog.show();
                ApiInterface apiInterface = AppConfig.getRetrofit().create(ApiInterface.class);

                Log.i("SelecteddataIs",model.getData().get(position).getId()+"\n"+
                       SharedHelper.getKey(mContext, UserConstant.id));

                Call<ModeltournamentplayCount> call = apiInterface.tournamentplayCount("Bearer "+token,
                        model.getData().get(position).getId(),SharedHelper.getKey(mContext, UserConstant.id));
                call.enqueue(new Callback<ModeltournamentplayCount>() {
                    @Override
                    public void onResponse(Call<ModeltournamentplayCount> call, Response<ModeltournamentplayCount> response) {
                        progressDialog.dismiss();

                        Log.i("ResponseCode",response.code()+"");

                        if (response.isSuccessful() ) {

                            if(response.body().getStatus()){

                                if(response.body().getData().get(0).getTotal_player().equals("16")){

                                    if(response.body().getData().get(0).getPlayer_in_out().equals("true")){

                                        EntryFeeFragment optionsFrag = new EntryFeeFragment();
                                        Bundle args = new Bundle();
                                        args.putInt("tournament_id", response.body().getData().get(0).getTournament_id());
                                        args.putString("tournament_name", response.body().getData().get(0).getTournament_name());
                                        args.putString("tournament_image", response.body().getData().get(0).getTournament_image());
                                        args.putString("total_player", response.body().getData().get(0).getTotal_player());
                                        args.putString("tournament_player", response.body().getData().get(0).getTournament_player());
                                        args.putString("round", response.body().getData().get(0).getRound());
                                        optionsFrag.setArguments(args);

                                        ((MainActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.container,optionsFrag,"EntryFeeFragment").addToBackStack(null).commit();

                                    }
                                    else {
                                        Toast.makeText(mContext,"Tournament Already Full",Toast.LENGTH_LONG).show();
                                    }

                                }
                                else {
                                    EntryFeeFragment optionsFrag = new EntryFeeFragment();

                                    Bundle args = new Bundle();
                                    args.putInt("tournament_id", response.body().getData().get(0).getTournament_id());
                                    args.putString("tournament_name", response.body().getData().get(0).getTournament_name());
                                    args.putString("tournament_image", response.body().getData().get(0).getTournament_image());
                                    args.putString("total_player", response.body().getData().get(0).getTotal_player());
                                    args.putString("tournament_player", response.body().getData().get(0).getTournament_player());
                                    optionsFrag.setArguments(args);
                                    ((MainActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.container,optionsFrag,"EntryFeeFragment").addToBackStack(null).commit();

                                }

                            }

                        }

                    }

                    @Override
                    public void onFailure(Call<ModeltournamentplayCount> call, Throwable t) {
                        progressDialog.dismiss();
                        if (t instanceof SocketTimeoutException) {

                        }

                    }
                });
                }

                else {
                    Toast.makeText(mContext,"This Tournament is Locked",Toast.LENGTH_LONG).show();
                }


            }
        });


    }

    @Override
    public int getItemCount() {
        return model.getData().size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img)
        public ImageView img;
        @BindView(R.id.name_txt)
        public TextView name_txt;


        private RelativeLayout layout_tournament;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            layout_tournament=itemView.findViewById(R.id.layout_tournament);
        }
    }




}
