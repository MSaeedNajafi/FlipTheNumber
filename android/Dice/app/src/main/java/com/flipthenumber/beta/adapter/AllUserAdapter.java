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
import com.flipthenumber.beta.Helper.CustomToast;
import com.flipthenumber.beta.retrofit.ApiInterface;
import com.flipthenumber.beta.retrofit.AppConfig;
import com.flipthenumber.beta.Helper.SharedHelper;
import com.flipthenumber.beta.R;
import com.flipthenumber.beta.model.SendRequestModel;
import com.flipthenumber.beta.model.UserListModel;
import com.squareup.picasso.Picasso;

import java.net.SocketTimeoutException;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllUserAdapter extends RecyclerView.Adapter<AllUserAdapter.MyViewHolder> implements MessageInterface.View {

    private Context mContext;
    private UserListModel model;

    public AllUserAdapter(Context mContext, UserListModel model) {
        this.mContext = mContext;
        this.model = model;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get().load(model.getData().get(position).getImage()).into(holder.profie_image);
        holder.name_txt.setText(model.getData().get(position).getName());
        holder.email_txt.setText(model.getData().get(position).getEmail());
        holder.sendRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String token = SharedHelper.getKey(mContext, UserConstant.userToken);
                String userId = SharedHelper.getKey(mContext, UserConstant.id);
                String frnId = model.getData().get(position).getUserId().toString();

                sendFriendRequest(holder, token, userId, frnId, position);


               /* if (holder.sendRequestBtn.getText().toString().equalsIgnoreCase("Send Request"))
                {
                    holder.sendRequestBtn.setText("Cancel Request");

                }else {
                    holder.sendRequestBtn.setText("Send Request");


                }*/

            }
        });

    }

    @Override
    public int getItemCount() {
        return model.getData().size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.profie_image)
        CircleImageView profie_image;
        @BindView(R.id.name_txt)
        TextView name_txt;
        @BindView(R.id.email_txt)
        TextView email_txt;
        @BindView(R.id.sendRequestBtn)
        Button sendRequestBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }





    private void sendFriendRequest(@NonNull AllUserAdapter.MyViewHolder holder,String token,String userId,String frndId,int position){

        ApiInterface apiInterface = AppConfig.getRetrofit().create(ApiInterface.class);
        Call<SendRequestModel> call = apiInterface.sendFriendRequest("Bearer "+token,userId,frndId,"invite");
        call.enqueue(new Callback<SendRequestModel>() {
            @Override
            public void onResponse(Call<SendRequestModel> call, Response<SendRequestModel> response) {
                if (response.isSuccessful()){
                    if (response.code()==200){
                        onSuccess("Request sent successfully");
                        deleteItem(holder,position);
                    }else {
                        OnError("Request Not Send!");
                    }
                }
            }

            @Override
            public void onFailure(Call<SendRequestModel> call, Throwable t) {
                OnError(t.getMessage());
                if (t instanceof SocketTimeoutException){
                    sendFriendRequest(holder,token, userId, frndId,position);
                }


            }
        });
    }



    @Override
    public void onSuccess(String message) {
        CustomToast.displayMessage(mContext, message);
    }

    @Override
    public void OnError(String message) {
        CustomToast.displayMessage(mContext, message);
    }

    private void deleteItem(@NonNull AllUserAdapter.MyViewHolder holder, int position) {
        model.getData().remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, model.getData().size());
        holder.itemView.setVisibility(View.GONE);
    }


}
