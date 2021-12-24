package com.flipthenumber.beta.ui.fragments.Tournaments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flipthenumber.beta.Constants.UserConstant;
import com.flipthenumber.beta.Helper.SharedHelper;
import com.flipthenumber.beta.adapter.TournamentAdapters.UsersListingView;
import com.flipthenumber.beta.model.Modeltournamentplayerlist;
import com.flipthenumber.beta.retrofit.ApiInterface;
import com.flipthenumber.beta.retrofit.AppConfig;
import com.flipthenumber.beta.R;
import com.squareup.picasso.Picasso;

import java.net.SocketTimeoutException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentUserListing extends Fragment implements View.OnClickListener {

private RecyclerView recylerview_players;

private ArrayList<String> arrayList_one,arrayList_two;

    private int tournament_id;
    private String tournament_name,tournament_image;

    private TextView name_txt,tv_running_round;

    private RelativeLayout layout_one,layout_two,layout_three,layout_four;
    private ImageView iv_tournament;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_player_list_all_data, container, false);

        init(view);

        return view;
    }

    private void init(View view){


        Bundle args = getArguments();

        tournament_id = args.getInt("tournament_id", 0);
        tournament_name = args.getString("tournament_name", "");
        tournament_image = args.getString("tournament_image", "");

        name_txt=view.findViewById(R.id.name_txt);
        tv_running_round=view.findViewById(R.id.tv_running_round);
        recylerview_players=view.findViewById(R.id.recylerview_players);
        iv_tournament=view.findViewById(R.id.img);

        layout_one=view.findViewById(R.id.layout_one);
        layout_two=view.findViewById(R.id.layout_two);
        layout_three=view.findViewById(R.id.layout_three);
        layout_four=view.findViewById(R.id.layout_four);

        name_txt.setText(tournament_name);
        Picasso.get().load(tournament_image).into(iv_tournament);

//        arrayList_one=new ArrayList<>();
//        arrayList_two=new ArrayList<>();
//
//        arrayList_one.add("You");
//        arrayList_one.add("Player3");
//        arrayList_one.add("Player5");
//        arrayList_one.add("Player7");
//        arrayList_one.add("Player9");
//        arrayList_one.add("Player11");
//        arrayList_one.add("Player13");
//        arrayList_one.add("Player15");
//
//        arrayList_two.add("Player2");
//        arrayList_two.add("Player4");
//        arrayList_two.add("Player6");
//        arrayList_two.add("Player8");
//        arrayList_two.add("Player10");
//        arrayList_two.add("Player12");
//        arrayList_two.add("Player14");
//        arrayList_two.add("Player16");
//



        apiCall();




    }

    private void apiCall() {

        String token = SharedHelper.getKey(getActivity(), UserConstant.userToken);

        Log.i("DataForListing",token+"\n"+String.valueOf(tournament_id));

        ProgressDialog progressDialog=new ProgressDialog(getActivity());
        progressDialog.setTitle("Loading");
        progressDialog.show();
        ApiInterface apiInterface = AppConfig.getRetrofit().create(ApiInterface.class);
        Call<Modeltournamentplayerlist> call = apiInterface.tournamentplayerlist("Bearer "+token,String.valueOf(tournament_id));
        call.enqueue(new Callback<Modeltournamentplayerlist>() {
            @Override
            public void onResponse(Call<Modeltournamentplayerlist> call, Response<Modeltournamentplayerlist> response) {
                progressDialog.dismiss();



                if (response.isSuccessful() ) {

                    Toast.makeText(getActivity(),response.body().getMessage(),Toast.LENGTH_LONG).show();

                    if(response.body().getStatus()){


                        if(response.body().getData().size()>0){


                            tv_running_round.setText(response.body().getRound());

                            if(response.body().getRound().equals("1")){
                                layout_one.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_white_red));
                            }
                            else  if(response.body().getRound().equals("2")){
                                layout_two.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_white_red));
                            }
                            if(response.body().getRound().equals("3")){
                                layout_three.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_white_red));
                            }
                            if(response.body().getRound().equalsIgnoreCase("final")){
                                layout_four.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_white_red));
                            }


                            setAdapter(response.body());

                        }

                    }

                }

                else {

                    Log.i("ResponseCodeIs",response.code()+"");

                }


            }

            @Override
            public void onFailure(Call<Modeltournamentplayerlist> call, Throwable t) {

                Log.i("ErrorCodeIshere",t.toString());
                progressDialog.dismiss();
                if (t instanceof SocketTimeoutException) {

                }

            }
        });
    }

    private void setAdapter(Modeltournamentplayerlist body) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recylerview_players.setLayoutManager(layoutManager);
        UsersListingView usersListingView = new UsersListingView(getActivity(),body);
        recylerview_players.setAdapter(usersListingView);
    }




    @Override
    public void onClick(View v) {

    }
}
