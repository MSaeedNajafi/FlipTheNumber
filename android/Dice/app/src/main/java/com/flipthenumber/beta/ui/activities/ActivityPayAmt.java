package com.flipthenumber.beta.ui.activities;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.flipthenumber.beta.Constants.UserConstant;
import com.flipthenumber.beta.Helper.CustomToast;
import com.flipthenumber.beta.Helper.SharedHelper;
import com.flipthenumber.beta.model.ModelPayment;
import com.flipthenumber.beta.retrofit.ApiInterface;
import com.flipthenumber.beta.retrofit.AppConfig;
import com.flipthenumber.beta.MainActivity;
import com.flipthenumber.beta.R;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ActivityPayAmt extends AppCompatActivity implements View.OnClickListener, PaymentResultListener {

    private String price,type,total,add_type,total_add_type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_amt);

        Init();


    }

    private void Init() {

        price = getIntent().getStringExtra("price");
        type = getIntent().getStringExtra("type");
        total = getIntent().getStringExtra("total");
        add_type = getIntent().getStringExtra("add_type");
        total_add_type = getIntent().getStringExtra("total_add_type");

        startPayment();

    }


    private void startPayment() {


        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Dice");
            options.put("description", "App Payment");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://rzp-mobile.s3.amazonaws.com/images/rzp.png");
            options.put("currency", "INR");
            String payment = price;
            // amount is in paise so please multiple it by 100
            //Payment failed Invalid amount (should be passed in integer paise. Minimum value is 100 paise, i.e. ₹ 1)
            double total = Double.parseDouble(payment);
            total = total * 100;
            options.put("amount", total);


           // JSONObject preFill = new JSONObject();
            //preFill.put("email", SharedHelper.getKey(getApplicationContext(), UserConstant.email));
           // preFill.put("contact", SharedHelper.getKey(getApplicationContext(), UserConstant.phone));

          //  options.put("prefill", preFill);

            //total_amt=String.valueOf(total);

            //Toast.makeText(getApplicationContext(),total_amt,Toast.LENGTH_LONG).show();

            co.open(this, options);


        } catch (Exception e) {

            Log.i("error_in_wallet",e.toString());

            Log.i("error_in_wallet",e.getMessage());


            e.printStackTrace();
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

    }

    @Override
    public void onPaymentSuccess(String s) {



        Log.i("success_id",s);



        String token = SharedHelper.getKey(getApplicationContext(), UserConstant.userToken);
        String userId = SharedHelper.getKey(getApplicationContext(), UserConstant.id);

        ProgressDialog dialog=new ProgressDialog(ActivityPayAmt.this);
        dialog.setTitle("Loading");
        dialog.setMessage("Please wait...");

        dialog.show();
        ApiInterface apiInterface = AppConfig.getRetrofit().create(ApiInterface.class);


        Call<ModelPayment> call = apiInterface.paymentWith(
                "Bearer "+token,
                userId,
                s,
                price,
                "success",
                "1",
                type,
                total,
                add_type,
                total_add_type);



        call.enqueue(new Callback<ModelPayment>() {
            @Override
            public void onResponse(Call<ModelPayment> call, Response<ModelPayment> response) {

                Log.i("response_code_",response.code()+"");
                Log.i("response_code_",response.message()+"");

                dialog.dismiss();

                Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_LONG).show();

                if(response.body().getStatus()){
                    Intent intent=new Intent(ActivityPayAmt.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }



            }
            @Override
            public void onFailure(Call<ModelPayment> call, Throwable t) {
                Log.i("fail_login",t.toString());
                dialog.dismiss();
                CustomToast.displayError(getApplicationContext(),t.getMessage());
                if (t instanceof SocketTimeoutException){

                }
            }
        });





    }

    @Override
    public void onPaymentError(int i, String s) {
        Log.i("error_payment",s);
    }

}