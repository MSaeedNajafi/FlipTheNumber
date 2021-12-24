package com.flipthenumber.beta.ui.fragments.InviteConcept;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flipthenumber.beta.Constants.UserConstant;
import com.flipthenumber.beta.Helper.SharedHelper;
import com.flipthenumber.beta.R;
import com.flipthenumber.beta.adapter.FriendAdapter;
import com.flipthenumber.beta.adapter.InviteAdapters.AllFriendsAdapter;
import com.flipthenumber.beta.adapter.InviteAdapters.AllUserAdapter;
import com.flipthenumber.beta.adapter.InviteAdapters.AllinvitesRequestsAdapter;
import com.flipthenumber.beta.model.FriendsModel;
import com.flipthenumber.beta.model.InviteToPlay;
import com.flipthenumber.beta.model.MyRequestModel;
import com.flipthenumber.beta.retrofit.ApiInterface;
import com.flipthenumber.beta.retrofit.AppConfig;

import java.net.SocketTimeoutException;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class FragmentAllFriends extends Fragment implements View.OnClickListener {

    @BindView(R.id.all_user_RV)
    RecyclerView all_user_RV;
    @BindView(R.id.no_txt)
    TextView no_txt;
    String token;
    String userId;
    private ImageView iv_invite;
    private RelativeLayout layout_top;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_all_friends, container, false);

        init(view);

        return view;
    }

    private void init(View view) {
        ButterKnife.bind(this, view);
        iv_invite=view.findViewById(R.id.iv_invite);
        layout_top=view.findViewById(R.id.layout_top);
        iv_invite.setOnClickListener(this);
        layout_top.setOnClickListener(this);
        token = SharedHelper.getKey(getActivity(), UserConstant.userToken);
        Log.i("tokenHere",token);
        userId = SharedHelper.getKey(getActivity(), UserConstant.id);
        System.out.println("+++++++++++++++ "+token);

        apiCall();

    }

    private void apiCall() {
        Log.i("userid",userId);
        ProgressDialog progressDialog=new ProgressDialog(getActivity());
        progressDialog.setTitle("Loading..");
        progressDialog.show();

        ApiInterface apiInterface = AppConfig.getRetrofit().create(ApiInterface.class);
        Call<FriendsModel> call = apiInterface.getAllFriends("Bearer "+token,userId);
        call.enqueue(new Callback<FriendsModel>() {
            @Override
            public void onResponse(Call<FriendsModel> call, Response<FriendsModel> response) {

                progressDialog.dismiss();
                if (response.isSuccessful()) {


                    try {
                        if (response.body().getData().size() > 0) {

                            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                            all_user_RV.setLayoutManager(layoutManager);
                            all_user_RV.setAdapter(new AllFriendsAdapter(getActivity(), response.body()));
                            all_user_RV.setVisibility(View.VISIBLE);
                            no_txt.setVisibility(View.GONE);
                            layout_top.setVisibility(View.VISIBLE);


                        } else {

                            all_user_RV.setVisibility(View.GONE);
                            no_txt.setVisibility(View.VISIBLE);
                            //layout_top.setVisibility(View.GONE);
                        }
                    }catch (Exception e){

                    }

                }else {

                    all_user_RV.setVisibility(View.GONE);
                    no_txt.setVisibility(View.VISIBLE);
                    //layout_top.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<FriendsModel> call, Throwable t) {
                progressDialog.dismiss();
                System.out.println("++++++ "+t.getMessage());
                if (t instanceof SocketTimeoutException) {

                }

            }
        });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.iv_invite:

                //apiCallInviteFriends();
                break;


            case R.id.layout_top:

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.flipthenumber.beta");
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share with friends");
                startActivity(Intent.createChooser(intent, "Share"));

                break;
        }

    }

    private void hideKeyboard(){
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }catch (Exception e){

        }
    }

    private void apiCallInviteFriends() {

        Log.i("userid",userId);
        ProgressDialog progressDialog=new ProgressDialog(getActivity());
        progressDialog.setTitle("Loading..");
        progressDialog.show();
        ApiInterface apiInterface = AppConfig.getRetrofit().create(ApiInterface.class);
        Call<InviteToPlay> call = apiInterface.inviteFriendsToPlay("Bearer "+token,userId);
        call.enqueue(new Callback<InviteToPlay>() {
            @Override
            public void onResponse(Call<InviteToPlay> call, Response<InviteToPlay> response) {
                progressDialog.dismiss();

                try {
                    Toast.makeText(getActivity(),response.body().getMessage(),Toast.LENGTH_LONG).show();
                }catch (Exception e){
                    Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<InviteToPlay> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(),t.toString(),Toast.LENGTH_LONG).show();
                if (t instanceof SocketTimeoutException) {

                }

            }
        });

    }

}

