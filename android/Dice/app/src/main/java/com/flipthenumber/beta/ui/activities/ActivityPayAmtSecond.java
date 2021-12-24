package com.flipthenumber.beta.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flipthenumber.beta.Constants.UserConstant;
import com.flipthenumber.beta.Helper.CustomToast;
import com.flipthenumber.beta.Helper.SharedHelper;
import com.flipthenumber.beta.PaymentStripe.ActivityPayment;
import com.flipthenumber.beta.model.ModelPayment;
import com.flipthenumber.beta.retrofit.ApiInterface;
import com.flipthenumber.beta.retrofit.AppConfig;
import com.flipthenumber.beta.MainActivity;
import com.flipthenumber.beta.R;

import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;

import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputWidget;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.SetupIntent;
import com.stripe.model.checkout.Session;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.checkout.SessionCreateParams;

import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityPayAmtSecond extends AppCompatActivity implements View.OnClickListener {
    private CardInputWidget mCardInputWidget;
    private Button pay,payWithIdeal;
    private Card card;
    private TextView tv_add_type,tv_amt;
    private ImageView iv_back;
    private String price,type,total,add_type,total_add_type,from;
    private RelativeLayout layout_google_pay;
    private String paymentIntentClientSecret;
    private Stripe stripe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_amt_second);
        Init();
        InitUI();
    }

    private void Init() {
        payWithIdeal=findViewById(R.id.payWithIdeal);
        layout_google_pay=findViewById(R.id.layout_google_pay);
        iv_back=findViewById(R.id.iv_back);
        mCardInputWidget=findViewById(R.id.card_input_widget);
        pay=findViewById(R.id.pay);
        tv_add_type=findViewById(R.id.tv_add_type);
        tv_amt=findViewById(R.id.tv_amt);
    }

    private void InitUI() {
        price = getIntent().getStringExtra("price");
        type = getIntent().getStringExtra("type");
        total = getIntent().getStringExtra("total");
        add_type = getIntent().getStringExtra("add_type");
        total_add_type = getIntent().getStringExtra("total_add_type");
        from = getIntent().getStringExtra("from");
        tv_add_type.setText(add_type);
        tv_amt.setText("Amount : $"+price);

        ClickListener();

    }

    private void ClickListener() {
        pay.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        layout_google_pay.setOnClickListener(this);
        payWithIdeal.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.iv_back:

                onBackPressed();

                break;

            case R.id.pay:

                if(!pay.getText().toString().trim().equalsIgnoreCase("paid"))
                {

                    Log.d("Stripe", "going");

                    card = mCardInputWidget.getCard();

                    if (card == null) {
                        Toast.makeText(ActivityPayAmtSecond.this, "Invalid Card Data", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!card.validateCard()) {
                        Toast.makeText(ActivityPayAmtSecond.this, "Invalid Card Data", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Log.d("card number", card.getNumber());

                    Dialog dialog = new Dialog(ActivityPayAmtSecond.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(true);
                    dialog.setContentView(R.layout.pay_dialog);
                    dialog.show();
                    Window window = dialog.getWindow();
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

                    Button btn_cancel=dialog.findViewById(R.id.btn_cancel);
                    Button btn_ok=dialog.findViewById(R.id.btn_ok);


                    btn_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    btn_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dialog.dismiss();

                            ProgressDialog pd = new ProgressDialog(ActivityPayAmtSecond.this);
                            pd.setMessage("loading");
                            pd.show();
                            pd.setCancelable(false);

                            Log.d("card number", card.getNumber());

                            //Stripe stripe = new Stripe(getApplicationContext(), "pk_test_3EGFyPzcAVhTuZRZiFmri7Xh");
                            //pk_test_51HRYGUDE7aXkljMPwJEDRsbM2kDjzTC5MpzY6PMmJGTU8g7ym9yEsHNwiLhA5YKviK394nfhunNraZqIMvV5mUtp00YyJf1gkc
                            //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
                            Stripe stripe = new Stripe(getApplicationContext(), "pk_test_51HRYGUDE7aXkljMPwJEDRsbM2kDjzTC5MpzY6PMmJGTU8g7ym9yEsHNwiLhA5YKviK394nfhunNraZqIMvV5mUtp00YyJf1gkc");
                            stripe.createToken(
                                    card,
                                    new TokenCallback() {
                                        public void onSuccess(Token token) {
                                            pd.dismiss();

                                            Log.i("success_token",token.getId());

                                            methodApiCall(token.getId());

                                        }

                                        public void onError(Exception error) {
                                            pd.dismiss();
                                            Log.d("Stripe token error", error.getMessage());
                                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    );

                        }
                    });
                }else {
                    Toast.makeText(getApplicationContext(),"Already Paid", Toast.LENGTH_SHORT).show();
                }


                break;

            case R.id.layout_google_pay:

                Intent intent=new Intent(ActivityPayAmtSecond.this, ActivityPayment.class);
                startActivity(intent);

                break;


            case R.id.payWithIdeal:

                //Toast.makeText(getApplicationContext(),"click",Toast.LENGTH_LONG).show();

                //methodIdealTwo();

                methodIdeal();

                break;

        }

    }

    private void methodIdealTwo() {



    }


    private void methodApiCall(String token_id) {

        String token = SharedHelper.getKey(getApplicationContext(), UserConstant.userToken);
        String userId = SharedHelper.getKey(getApplicationContext(), UserConstant.id);

        ProgressDialog dialog=new ProgressDialog(ActivityPayAmtSecond.this);
        dialog.setTitle("Loading");
        dialog.setMessage("Please wait...");

        dialog.show();
        ApiInterface apiInterface = AppConfig.getRetrofit().create(ApiInterface.class);


        Log.i("data_is",token+"\n"+userId+"\n"+token_id+"\n"+price+"\n"+"success"+"\n"+"1"+"\n"+
                type+"\n"+total+"\n"+add_type+"\n"+total_add_type);

        Call<ModelPayment> call = apiInterface.paymentWith(
                "Bearer "+token,
                 userId,
                 token_id,
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


                if(response.body().getMessage().trim().equals("Purchase successfully")){


                    try {

                        SharedHelper.putKey(getApplicationContext(),UserConstant.coin,response.body().getData().get(0).getTotal_token());
                        MainActivity.total_coin_txt.setText(SharedHelper.getKey(ActivityPayAmtSecond.this, UserConstant.coin));

                        SharedHelper.putKey(getApplicationContext(),UserConstant.zokar,response.body().getData().get(0).getTotal_joker());
                        MainActivity.zokar_txt.setText(SharedHelper.getKey(ActivityPayAmtSecond.this, UserConstant.zokar));

                    }catch (Exception e){

                    }


                    Toast.makeText(getApplicationContext(),"Purchased Successful",Toast.LENGTH_LONG).show();

                }
                else {

                    Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_LONG).show();

                }

                if(response.body().getStatus()){
                    Intent intent=new Intent(ActivityPayAmtSecond.this, MainActivity.class);
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


    private void methodIdeal() {
        com.stripe.Stripe.apiKey = "pk_test_51HRYGUDE7aXkljMPwJEDRsbM2kDjzTC5MpzY6PMmJGTU8g7ym9yEsHNwiLhA5YKviK394nfhunNraZqIMvV5mUtp00YyJf1gkc";
        SessionCreateParams params =
                SessionCreateParams.builder()
                        .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                        .addPaymentMethodType(SessionCreateParams.PaymentMethodType.IDEAL)
                        .addLineItem(
                                SessionCreateParams.LineItem.builder()
                                        .setPriceData(
                                                SessionCreateParams.LineItem.PriceData.builder()

                                                        .setCurrency("usd")
                                                        // To accept `ideal`, all line items must have currency: eur
                                                        .setCurrency("eur")
                                                        .setUnitAmount(2000L)
                                                        .setProductData(
                                                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                        .setName("T-shirt")
                                                                        .build())
                                                        .build())
                                        .setQuantity(1L)
                                        .build())
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                      //  .setSuccessUrl("https://example.com/success")
                       // .setCancelUrl("https://example.com/cancel")
                        .build();

        try {
            Session session = Session.create(params);
            fulfillOrder(session);
            Log.i("HereIsSessionsIs","try");
        } catch (StripeException e) {
            Log.i("HereIsSessionsIs","catch"+e.toString());
            e.printStackTrace();
        }


    }

    public void fulfillOrder(Session session) {
        // TODO: fill me in
        System.out.println("Fulfilling order...");

        Log.i("HereIsSessionsIs","SessionsIS");


    }



    // iDEAL>>









}