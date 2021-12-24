package com.flipthenumber.beta.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flipthenumber.beta.Constants.MessageInterface;
import com.flipthenumber.beta.Constants.UserConstant;
import com.flipthenumber.beta.retrofit.ApiInterface;
import com.flipthenumber.beta.retrofit.AppConfig;
import com.flipthenumber.beta.Helper.CustomToast;
import com.flipthenumber.beta.Helper.SharedHelper;
import com.flipthenumber.beta.R;
import com.flipthenumber.beta.model.MyRequestModel;
import com.squareup.picasso.Picasso;

import java.net.SocketTimeoutException;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyRequestAdapter extends RecyclerView.Adapter<MyRequestAdapter.MyViewHolder> implements MessageInterface.View {
    private Context mContext;
    private MyRequestModel model;

    public MyRequestAdapter(Context mContext, MyRequestModel model) {
        this.mContext = mContext;
        this.model = model;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.my_request_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get().load(model.getData().get(position).getImage()).into(holder.profie_image);
        holder.name_txt.setText(model.getData().get(position).getName());
        holder.email_txt.setText(model.getData().get(position).getEmail());

        holder.acceptRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String token = SharedHelper.getKey(mContext, UserConstant.userToken);

                String id =model.getData().get(position).getId().toString();
                methodAccept(holder, token, id, position);

            }
        });



        holder.cancelRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String token = SharedHelper.getKey(mContext, UserConstant.userToken);

                String id =model.getData().get(position).getId().toString();
                unFriend(holder, token, id, position);

            }
        });



    }

    private void methodAccept(MyViewHolder holder, String token, String id, int position) {

        ApiInterface apiInterface = AppConfig.getRetrofit().create(ApiInterface.class);
        Call<MyRequestModel> call = apiInterface.accept_request("Bearer " + token, id);
        call.enqueue(new Callback<MyRequestModel>() {
            @Override
            public void onResponse(Call<MyRequestModel> call, Response<MyRequestModel> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        onSuccess("Request Accepted");
                        deleteItem(holder, position);
                    } else {
                        OnError("Error Occur!");
                    }
                }
            }

            @Override
            public void onFailure(Call<MyRequestModel> call, Throwable t) {
                OnError(t.getMessage());
                if (t instanceof SocketTimeoutException) {
                    methodAccept(holder, token, id, position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return model.getData().size();
    }

    private void unFriend(@NonNull MyRequestAdapter.MyViewHolder holder, String token, String id, int position) {

        ApiInterface apiInterface = AppConfig.getRetrofit().create(ApiInterface.class);
        Call<MyRequestModel> call = apiInterface.unFriend("Bearer " + token, id);
        call.enqueue(new Callback<MyRequestModel>() {
            @Override
            public void onResponse(Call<MyRequestModel> call, Response<MyRequestModel> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        onSuccess("Request Cancelled");
                        deleteItem(holder, position);
                    } else {
                        OnError("Error Occur!");
                    }
                }
            }

            @Override
            public void onFailure(Call<MyRequestModel> call, Throwable t) {
                OnError(t.getMessage());
                if (t instanceof SocketTimeoutException) {
                    unFriend(holder, token, id, position);
                }


            }
        });
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.profie_image)
        CircleImageView profie_image;
        @BindView(R.id.name_txt)
        TextView name_txt;
        @BindView(R.id.email_txt)
        TextView email_txt;
        @BindView(R.id.cancelRequestBtn)
        Button cancelRequestBtn;
        @BindView(R.id.acceptRequestBtn)
        Button acceptRequestBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public void onSuccess(String message) {
        CustomToast.displayMessage(mContext, message);
    }

    @Override
    public void OnError(String message) {
        CustomToast.displayMessage(mContext, message);
    }

    private void deleteItem(@NonNull MyRequestAdapter.MyViewHolder holder, int position) {
        model.getData().remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, model.getData().size());
        holder.itemView.setVisibility(View.GONE);
    }
}
