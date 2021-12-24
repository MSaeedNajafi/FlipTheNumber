package com.flipthenumber.beta.adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flipthenumber.beta.Constants.UserConstant;
import com.flipthenumber.beta.retrofit.ApiInterface;
import com.flipthenumber.beta.retrofit.AppConfig;
import com.flipthenumber.beta.Helper.CustomToast;
import com.flipthenumber.beta.Helper.SharedHelper;
import com.flipthenumber.beta.MainActivity;
import com.flipthenumber.beta.R;
import com.flipthenumber.beta.model.ModelPayment;
import com.flipthenumber.beta.model.StoreModel;
import com.flipthenumber.beta.ui.activities.ActivityPayAmtSecond;
import com.squareup.picasso.Picasso;

import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.MyViewHolder> {

    private Context mContext;
    private StoreModel model;

    public StoreAdapter(Context mContext, StoreModel model) {
        this.mContext = mContext;
        this.model = model;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        try {
            if(!model.getData().get(position).getStoreImg().trim().equals(""))
            {
                holder.name_txt.setVisibility(View.GONE);
                holder.img_store.setVisibility(View.VISIBLE);
                Picasso.get().load( model.getData().get(position).getStoreImg()).into(holder.img_store);
            }
            else {
                holder.name_txt.setText(model.getData().get(position).getName());
                holder.name_txt.setVisibility(View.VISIBLE);
                holder.img_store.setVisibility(View.GONE);
            }

        }catch (Exception e){

        }


        String data=model.getData().get(position).getNumber();

        Log.i("data_name_token",data);

        holder.number_txt.setText(data);


        if(!model.getData().get(position).getPrice().contains("Tokens")){
            holder.price_txt.setText("$"+model.getData().get(position).getPrice());
        }
        else {
            holder.price_txt.setText(model.getData().get(position).getPrice());
        }





        Log.i("price_here",String.valueOf(position)+">> "+model.getData().get(position).getPrice());


        holder.layout_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("payment_type",model.getData().get(position).getPayment_type());


                if(model.getData().get(position).getPayment_type().equals("0")){
                    String token = SharedHelper.getKey(mContext, UserConstant.userToken);
                    String userId = SharedHelper.getKey(mContext, UserConstant.id);

                    ProgressDialog dialog=new ProgressDialog(mContext);
                    dialog.setTitle("Loading");
                    dialog.setMessage("Please wait...");

                    dialog.show();
                    ApiInterface apiInterface = AppConfig.getRetrofit().create(ApiInterface.class);

                    Log.i("payment_data_is",token+"\n"+userId+"\n"+model.getData().get(position).getType()
                            +"\n"+model.getData().get(position).getTotal()+"\n"+model.getData().get(position).getAdd_type()
                            +"\n"+model.getData().get(position).getTotal_add_type());

                    Call<ModelPayment> call = apiInterface.payment(
                            "Bearer "+token,
                             userId,
                            "0",
                            model.getData().get(position).getType(),
                            model.getData().get(position).getTotal(),
                            model.getData().get(position).getAdd_type(),
                            model.getData().get(position).getTotal_add_type());


                    call.enqueue(new Callback<ModelPayment>() {
                        @Override
                        public void onResponse(Call<ModelPayment> call, Response<ModelPayment> response) {

                            Log.i("response_code_",response.code()+"");
                            Log.i("response_code_",response.message()+"");

                            dialog.dismiss();

                            try {

                                SharedHelper.putKey(mContext,UserConstant.coin,response.body().getData().get(0).getTotal_token());
                                MainActivity.total_coin_txt.setText(SharedHelper.getKey(mContext, UserConstant.coin));

                                SharedHelper.putKey(mContext,UserConstant.zokar,response.body().getData().get(0).getTotal_joker());
                                MainActivity.zokar_txt.setText(SharedHelper.getKey(mContext, UserConstant.zokar));

                            }catch (Exception e){

                            }

                            dialogShow(response.body().getMessage());


                        }
                        @Override
                        public void onFailure(Call<ModelPayment> call, Throwable t) {
                            Log.i("fail_login",t.toString());
                            dialog.dismiss();
                            CustomToast.displayError(mContext,t.getMessage());
                            if (t instanceof SocketTimeoutException){

                            }
                        }
                    });
                }

                else {

                    Intent intent=new Intent(mContext, ActivityPayAmtSecond.class);
                    intent.putExtra("price", model.getData().get(position).getPrice());
                    intent.putExtra("type",  model.getData().get(position).getType());
                    intent.putExtra("total",  model.getData().get(position).getTotal());
                    intent.putExtra("add_type", model.getData().get(position).getAdd_type());
                    intent.putExtra("total_add_type",  model.getData().get(position).getTotal_add_type());
                    intent.putExtra("from",  "adapter");
                    mContext.startActivity(intent);


                }










            }
        });


    }

    private void dialogShow(String message) {

        Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_token_less);
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);


        RelativeLayout layout_ok=dialog.findViewById(R.id.layout_ok);

        TextView tv_message=dialog.findViewById(R.id.tv_message);

        if(message.trim().equals("Purchase successfully")){

            tv_message.setText("Purchased Successful");

        }
        else {
            tv_message.setText(message);

        }


        layout_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }
        });

    }




    @Override
    public int getItemCount() {
        return model.getData().size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        private ImageView img_store;

        private TextView number_txt;

        private TextView name_txt;

        private TextView price_txt;

        private RelativeLayout layout_buy;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img_store=itemView.findViewById(R.id.img_store);
            number_txt=itemView.findViewById(R.id.number_txt);
            name_txt=itemView.findViewById(R.id.name_txt);
            price_txt=itemView.findViewById(R.id.price_txt);
            layout_buy=itemView.findViewById(R.id.layout_buy);
        }
    }
}
