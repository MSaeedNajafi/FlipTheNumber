package com.flipthenumber.beta.ui.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flipthenumber.beta.Constants.UserConstant;
import com.flipthenumber.beta.Helper.CustomToast;
import com.flipthenumber.beta.Helper.SharedHelper;
import com.flipthenumber.beta.adapter.StoreAdapter;
import com.flipthenumber.beta.model.InviteToPlay;
import com.flipthenumber.beta.model.ModelGetPayment;
import com.flipthenumber.beta.model.ModelPaymentForAds;
import com.flipthenumber.beta.model.StoreModel;
import com.flipthenumber.beta.retrofit.ApiInterface;
import com.flipthenumber.beta.retrofit.AppConfig;
import com.flipthenumber.beta.R;
import com.flipthenumber.beta.ui.activities.ActivityPayAmtSecond;

import java.net.SocketTimeoutException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StoreFragment extends Fragment implements View.OnClickListener {
    private Unbinder unbinder;

    @BindView(R.id.store_RV)
    RecyclerView store_RV;
    @BindView(R.id.no_txt)
    TextView no_txt;

    private RelativeLayout layout_buy;
    private String amt="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_store, container, false);
        init(view);
        initUI();
        apiCallForPaymentGet();
        return view;
    }



    private void init(View view) {
        unbinder = ButterKnife.bind(this, view);

        layout_buy=view.findViewById(R.id.layout_buy);
        layout_buy.setOnClickListener(this);

        Log.i("UserIdHereIs",SharedHelper.getKey(getActivity(), UserConstant.id));

    }
    private void initUI() {
        setstore_RVData();
    }
    private void setstore_RVData() {
       // ProgressDialog dialog = ProgressDialog.show(getActivity(), "Loading", "Please wait...", true);
        ApiInterface apiInterface = AppConfig.getRetrofit().create(ApiInterface.class);
        Call<StoreModel> call = apiInterface.getStore();
        call.enqueue(new Callback<StoreModel>() {
            @Override
            public void onResponse(Call<StoreModel> call, Response<StoreModel> response) {
              //  dialog.dismiss();
                if (response.isSuccessful()){
                    if (response.body().getData().size()>0){
                        try {
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                            store_RV.setLayoutManager(layoutManager);
                            store_RV.setAdapter(new StoreAdapter(getActivity(), response.body()));
                        }catch (Exception e){


                        }
                    }else {
                        store_RV.setVisibility(View.GONE);
                        no_txt.setVisibility(View.VISIBLE);
                    }
                }
            }
            @Override
            public void onFailure(Call<StoreModel> call, Throwable t) {
               // dialog.dismiss();
                CustomToast.displayError(getActivity(),t.getMessage());
                if (t instanceof SocketTimeoutException){
                    setstore_RVData();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_buy:
                //Toast.makeText(getActivity(),"click",Toast.LENGTH_LONG).show();

                Log.i("paymentIs",amt);



                if(amt.trim().equals("0")){
                    apiCallPaymentGateway();
                }
                else {
                    Toast.makeText(getActivity(),"You Have already Paid for Free ads Plan",Toast.LENGTH_LONG).show();
                }


                break;
        }

    }



    private void apiCallPaymentGateway() {


        String token = SharedHelper.getKey(getActivity(), UserConstant.userToken);

        ProgressDialog progressDialog=new ProgressDialog(getActivity());
        progressDialog.setTitle("Loading..");
        progressDialog.show();
        ApiInterface apiInterface = AppConfig.getRetrofit().create(ApiInterface.class);

        Call<ModelPaymentForAds> call = apiInterface.add_payment_by_userid("Bearer "+token,
                SharedHelper.getKey(getActivity(), UserConstant.id),"1.99");
        call.enqueue(new Callback<ModelPaymentForAds>() {
            @Override
            public void onResponse(Call<ModelPaymentForAds> call, Response<ModelPaymentForAds> response) {
                progressDialog.dismiss();

                try {
                    Toast.makeText(getActivity(),response.body().getMessage(),Toast.LENGTH_LONG).show();

                    if(response.body().getStatus()){

                        SharedHelper.putKey(getActivity(), UserConstant.adsenseFree,"true");

                        layout_buy.setBackgroundColor(Color.parseColor("#A1A1A1"));


                    }

                }catch (Exception e){
                    Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ModelPaymentForAds> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(),t.toString(),Toast.LENGTH_LONG).show();
                if (t instanceof SocketTimeoutException) {

                }

            }
        });






       /* Intent intent=new Intent(getActivity(), ActivityPayAmtSecond.class);
        intent.putExtra("price", "1.99");
        intent.putExtra("type",  "");
        intent.putExtra("total",  "");
        intent.putExtra("add_type","");
        intent.putExtra("total_add_type","");
        intent.putExtra("from",  "fragment");
        startActivity(intent);*/

    }



    private void apiCallForPaymentGet() {

        String token = SharedHelper.getKey(getActivity(), UserConstant.userToken);

        ProgressDialog progressDialog=new ProgressDialog(getActivity());
        progressDialog.setTitle("Loading..");
        progressDialog.show();
        ApiInterface apiInterface = AppConfig.getRetrofit().create(ApiInterface.class);

        Call<ModelGetPayment> call = apiInterface.get_payment_by_userid("Bearer "+token,
                SharedHelper.getKey(getActivity(), UserConstant.id));
        call.enqueue(new Callback<ModelGetPayment>() {
            @Override
            public void onResponse(Call<ModelGetPayment> call, Response<ModelGetPayment> response) {
                progressDialog.dismiss();

               Log.i("ResponseIs",response.body().getData().get(0).getPayment());

                amt=response.body().getData().get(0).getPayment();

                if(!response.body().getData().get(0).getPayment().trim().equals("0")){

                    SharedHelper.putKey(getActivity(), UserConstant.adsenseFree,"true");

                   layout_buy.setBackgroundColor(Color.parseColor("#A1A1A1"));

               }


            }

            @Override
            public void onFailure(Call<ModelGetPayment> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(),t.toString(),Toast.LENGTH_LONG).show();
                if (t instanceof SocketTimeoutException) {

                }

            }
        });
    }



}