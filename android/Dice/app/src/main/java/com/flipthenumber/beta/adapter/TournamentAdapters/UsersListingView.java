package com.flipthenumber.beta.adapter.TournamentAdapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flipthenumber.beta.Constants.UserConstant;
import com.flipthenumber.beta.Helper.SharedHelper;
import com.flipthenumber.beta.R;
import com.flipthenumber.beta.model.Modeltournamentplayerlist;
import com.flipthenumber.beta.ui.activities.Tournament.TournamentPlayVsPlay;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersListingView extends RecyclerView.Adapter<UsersListingView.MyViewHolder> {

    private Context mContext;
    private Modeltournamentplayerlist model;



    public UsersListingView(Context mContext,Modeltournamentplayerlist model) {

        this.mContext = mContext;
        this.model=model;


    }

    @NonNull
    @Override
    public UsersListingView.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_tournament_two_players, parent, false);
        return new UsersListingView.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersListingView.MyViewHolder holder, int position) {

        Log.i("HereDataIs","FragmentUserAdapter");


      //  Log.i("AllIds",)




        if(model.getData().get(position).getUser_id().equals(SharedHelper.getKey(mContext, UserConstant.id)))
        {
            holder.name_one.setText("You");
        }else {
            holder.name_one.setText(model.getData().get(position).getUser_name());
        }

        if(model.getData().get(position).getPlayer_id().equals(SharedHelper.getKey(mContext,UserConstant.id)))
        {
            holder.name_two.setText("You");
        }else {
            holder.name_two.setText(model.getData().get(position).getPlayer_name());
        }


        Picasso.get().load(model.getData().get(position).getUser_img()).into(holder.iv_one);


        Picasso.get().load(model.getData().get(position).getPlayerimg()).into(holder.iv_two);



        holder.layout_you.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(model.getData().get(position).getUser_id().equals(SharedHelper.getKey(mContext,UserConstant.id))||
                        model.getData().get(position).getPlayer_id().equals(SharedHelper.getKey(mContext,UserConstant.id)))
                {

                    String player_id="";
                    String turn="";
                    String player_name="";
                    String playerImg="";

                    if(!model.getData().get(position).getUser_id().equals(SharedHelper.getKey(mContext,UserConstant.id)))
                    {
                        player_id=model.getData().get(position).getUser_id();
                    }
                    else {
                        player_id=model.getData().get(position).getPlayer_id();
                    }

                    if(model.getData().get(position).getUser_id().equals(SharedHelper.getKey(mContext,UserConstant.id)))
                    {
                        turn="your";
                    }
                    else {
                        turn="opponent";
                    }


                    if(model.getData().get(position).getUser_id().equals(SharedHelper.getKey(mContext,UserConstant.id)))
                    {
                        player_name=model.getData().get(position).getPlayer_name();
                        playerImg=model.getData().get(position).getPlayerimg();

                    }
                    else {
                        player_name=model.getData().get(position).getUser_name();
                        playerImg=model.getData().get(position).getUser_img();
                    }


                    String user_id_player_id=model.getData().get(position).getUser_id().concat(model.getData().get(position).getPlayer_id());

                    Intent intent=new Intent(mContext, TournamentPlayVsPlay .class);
                    intent.putExtra("name",player_name);
                    intent.putExtra("id", player_id);
                    intent.putExtra("turn", turn);
                    intent.putExtra("user_id_player_id", user_id_player_id);
                    intent.putExtra("round",model.getRound());
                    intent.putExtra("tournament_id",model.getData().get(position).getTournament_id());
                    intent.putExtra("player_img",playerImg);

                    mContext.startActivity(intent);



                }



            }
        });





    }






    @Override
    public int getItemCount() {
        return model.getData().size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {




        private TextView name_one;

        private TextView name_two;

        private LinearLayout layout_you;

        private CircleImageView iv_one,iv_two;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name_one=itemView.findViewById(R.id.name_one);
            name_two=itemView.findViewById(R.id.name_two);
            layout_you=itemView.findViewById(R.id.layout_you);

            iv_one=itemView.findViewById(R.id.iv_one);
            iv_two=itemView.findViewById(R.id.iv_two);

        }
    }
}
