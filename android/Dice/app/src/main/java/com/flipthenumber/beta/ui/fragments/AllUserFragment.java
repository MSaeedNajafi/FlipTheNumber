package com.flipthenumber.beta.ui.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flipthenumber.beta.Constants.UserConstant;
import com.flipthenumber.beta.Helper.SharedHelper;
import com.flipthenumber.beta.adapter.AllUserAdapter;
import com.flipthenumber.beta.model.UserListModel;
import com.flipthenumber.beta.retrofit.ApiInterface;
import com.flipthenumber.beta.retrofit.AppConfig;
import com.flipthenumber.beta.R;

import java.net.SocketTimeoutException;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class AllUserFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.all_user_RV)
    RecyclerView all_user_RV;
    @BindView(R.id.no_txt)
    TextView no_txt;
    String token;
    String userId;
    EditText et_search_username;
    Button btn_search;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_user, container, false);

        init(view);
        initUI();

        return view;
    }

    private void init(View view) {
        ButterKnife.bind(this, view);
        token = SharedHelper.getKey(getActivity(), UserConstant.userToken);
        Log.i("tokenHere",token);
        userId = SharedHelper.getKey(getActivity(), UserConstant.id);
        System.out.println("+++++++++++++++ "+token);

        et_search_username=view.findViewById(R.id.et_search_username);
        btn_search=view.findViewById(R.id.btn_search);


    }

    private void initUI() {
        ClickListener();

       //getAllUser();

    }

    private void ClickListener() {
        btn_search.setOnClickListener(this);
    }

    private void getAllUser() {
        ApiInterface apiInterface = AppConfig.getRetrofit().create(ApiInterface.class);
        Call<UserListModel> call = apiInterface.getAllUser("Bearer "+token,userId);
        call.enqueue(new Callback<UserListModel>() {
            @Override
            public void onResponse(Call<UserListModel> call, Response<UserListModel> response) {

                if (response.isSuccessful() ) {

                    try {
                        if (response.body().getData().size() > 0) {
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                            all_user_RV.setLayoutManager(layoutManager);
                            all_user_RV.setAdapter(new AllUserAdapter(getActivity(), response.body()));
                        } else {
                            all_user_RV.setVisibility(View.GONE);
                            no_txt.setVisibility(View.VISIBLE);
                        }
                    }catch (Exception e){

                    }



                }else {
                    all_user_RV.setVisibility(View.GONE);
                    no_txt.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Call<UserListModel> call, Throwable t) {

                if (t instanceof SocketTimeoutException) {
                    getAllUser();
                }

            }
        });


    }


    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btn_search:

                if(et_search_username.getText().toString().trim().equals("")){
                    Toast.makeText(getActivity(),"Filed Empty!",Toast.LENGTH_LONG).show();
                }
                else {
                    hideKeyboard();
                    getFilterUser(et_search_username.getText().toString().trim());
                }

                break;
        }
    }




    private void getFilterUser(String name) {

        Log.i("userid",userId);
        ProgressDialog progressDialog=new ProgressDialog(getActivity());
        progressDialog.setTitle("Searching..");
        progressDialog.show();
        ApiInterface apiInterface = AppConfig.getRetrofit().create(ApiInterface.class);
        Call<UserListModel> call = apiInterface.filtergetUser("Bearer "+token,userId,name);
        call.enqueue(new Callback<UserListModel>() {
            @Override
            public void onResponse(Call<UserListModel> call, Response<UserListModel> response) {
                progressDialog.dismiss();

                if (response.isSuccessful() ) {

                    if (response.body().getData().size() > 0) {
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                        all_user_RV.setLayoutManager(layoutManager);
                        all_user_RV.setAdapter(new AllUserAdapter(getActivity(), response.body()));
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
            public void onFailure(Call<UserListModel> call, Throwable t) {
                progressDialog.dismiss();
                if (t instanceof SocketTimeoutException) {

                }

            }
        });


    }


    private void hideKeyboard(){
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }catch (Exception e){

        }

    }

}