package com.flipthenumber.beta.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flipthenumber.beta.Helper.CustomToast;
import com.flipthenumber.beta.adapter.EventAdapter;
import com.flipthenumber.beta.model.EventModel;
import com.flipthenumber.beta.retrofit.ApiInterface;
import com.flipthenumber.beta.retrofit.AppConfig;
import com.flipthenumber.beta.R;

import java.net.SocketTimeoutException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EventFragment extends Fragment {

    private Unbinder unbinder;

    @BindView(R.id.recylerview1)
    RecyclerView recylerview1;
    @BindView(R.id.recylerview2)
    RecyclerView recylerview2;
    @BindView(R.id.no_txt)
    TextView no_txt;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_event, container, false);
        init(view);
        initUI();
        return view;
    }

    private void init(View view){
       unbinder= ButterKnife.bind(this,view);
    }


    private void initUI(){

        setRecylerview2Data();
    }

    private void setRecylerview2Data(){
        // ProgressDialog dialog = ProgressDialog.show(getActivity(), "Loading", "Please wait...", true);
        ApiInterface apiInterface = AppConfig.getRetrofit().create(ApiInterface.class);
        Call<EventModel> call = apiInterface.getEvent();
        call.enqueue(new Callback<EventModel>() {
            @Override
            public void onResponse(Call<EventModel> call, Response<EventModel> response) {
                //  dialog.dismiss();
                if (response.isSuccessful()){
                    if (response.body().getData().size()>0){
                        try {
                            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
                            recylerview2.setLayoutManager(gridLayoutManager);
                            recylerview2.setAdapter(new EventAdapter(getActivity(), response.body()));
                        }catch (Exception e){

                        }

                    }else {
                        recylerview2.setVisibility(View.GONE);
                        no_txt.setVisibility(View.VISIBLE);
                    }
                }
            }
            @Override
            public void onFailure(Call<EventModel> call, Throwable t) {
                // dialog.dismiss();
                CustomToast.displayError(getActivity(),t.getMessage());
                if (t instanceof SocketTimeoutException){
                    setRecylerview2Data();
                }
            }
        });

       // LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
       /* GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
        recylerview2.setLayoutManager(gridLayoutManager);
        recylerview2.setAdapter(new Event1adapter(getActivity(),event2_dummyList));*/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}