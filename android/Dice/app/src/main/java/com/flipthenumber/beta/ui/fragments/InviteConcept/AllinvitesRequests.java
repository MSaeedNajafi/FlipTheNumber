package com.flipthenumber.beta.ui.fragments.InviteConcept;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flipthenumber.beta.Constants.UserConstant;
import com.flipthenumber.beta.Helper.SharedHelper;
import com.flipthenumber.beta.R;
import com.flipthenumber.beta.adapter.InviteAdapters.AllUserAdapter;
import com.flipthenumber.beta.adapter.InviteAdapters.AllinvitesRequestsAdapter;
import com.flipthenumber.beta.model.InviteModel;
import com.flipthenumber.beta.model.MyRequestModel;
import com.flipthenumber.beta.model.UserListModel;
import com.flipthenumber.beta.retrofit.ApiInterface;
import com.flipthenumber.beta.retrofit.AppConfig;

import java.net.SocketTimeoutException;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class AllinvitesRequests extends Fragment implements View.OnClickListener {

    @BindView(R.id.all_user_RV)
    RecyclerView all_user_RV;
    @BindView(R.id.no_txt)
    TextView no_txt;
    String token;
    String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.layout_all_invites_users, container, false);

        init(view);

        return view;
    }

    private void init(View view) {
        ButterKnife.bind(this, view);
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
            Call<MyRequestModel> call = apiInterface.myrequestAndInvites("Bearer "+token,userId);
            call.enqueue(new Callback<MyRequestModel>() {
                @Override
                public void onResponse(Call<MyRequestModel> call, Response<MyRequestModel> response) {
                    progressDialog.dismiss();

                    if (response.isSuccessful() ) {

                        if (response.body().getData().size() > 0) {
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                            all_user_RV.setLayoutManager(layoutManager);
                            all_user_RV.setAdapter(new AllinvitesRequestsAdapter(getActivity(),response.body()));
                            all_user_RV.setVisibility(View.VISIBLE);
                            no_txt.setVisibility(View.GONE);
                        } else {
                            all_user_RV.setVisibility(View.GONE);
                            no_txt.setVisibility(View.VISIBLE);
                        }

                    }else {
                        all_user_RV.setVisibility(View.GONE);
                        no_txt.setVisibility(View.VISIBLE);
                    }

                }

                @Override
                public void onFailure(Call<MyRequestModel> call, Throwable t) {
                    progressDialog.dismiss();
                    if (t instanceof SocketTimeoutException) {

                    }

                }
            });



    }

    @Override
    public void onClick(View v) {

    }




    private void hideKeyboard(){
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }catch (Exception e){

        }

    }

}
