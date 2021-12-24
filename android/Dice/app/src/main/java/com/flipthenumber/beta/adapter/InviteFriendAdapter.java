package com.flipthenumber.beta.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flipthenumber.beta.Constants.MessageInterface;
import com.flipthenumber.beta.retrofit.ApiInterface;
import com.flipthenumber.beta.retrofit.AppConfig;
import com.flipthenumber.beta.Helper.CustomToast;
import com.flipthenumber.beta.R;
import com.flipthenumber.beta.model.InviteModel;
import com.flipthenumber.beta.model.MyRequestModel;
import com.squareup.picasso.Picasso;

import java.net.SocketTimeoutException;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InviteFriendAdapter extends RecyclerView.Adapter<InviteFriendAdapter.MyViewHolder> implements MessageInterface.View {
    private Context mContext;
    private InviteModel model;

    public InviteFriendAdapter(Context mContext, InviteModel model) {
        this.mContext = mContext;
        this.model = model;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.invite_friend_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get().load(model.getData().get(position).getImage()).into(holder.profie_image);
        holder.name_txt.setText(model.getData().get(position).getName());
        holder.email_txt.setText(model.getData().get(position).getEmail());

//        holder.acceptBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                deleteItem(holder, position);
//
//            }
//        });
//
//        holder.cancelBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String token = SharedHelper.getKey(mContext, UserConstant.userToken);
//                String id = model.getData().get(position).getId().toString();
//                unFriend(holder, token, id, position);
//
//            }
//        });


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
//        @BindView(R.id.acceptBtn)
//        Button acceptBtn;
//        @BindView(R.id.cancelBtn)
//        Button cancelBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void unFriend(@NonNull InviteFriendAdapter.MyViewHolder holder, String token, String id, int position) {

        ApiInterface apiInterface = AppConfig.getRetrofit().create(ApiInterface.class);
        Call<MyRequestModel> call = apiInterface.unFriend("Bearer " + token, id);
        call.enqueue(new Callback<MyRequestModel>() {
            @Override
            public void onResponse(Call<MyRequestModel> call, Response<MyRequestModel> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        onSuccess("Request Accepted");
                        deleteItem(holder, position);
                    } else {
                        OnError("Request Cancelled!");
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

    @Override
    public void onSuccess(String message) {
        CustomToast.displayMessage(mContext, message);
    }

    @Override
    public void OnError(String message) {
        CustomToast.displayMessage(mContext, message);
    }

    private void deleteItem(@NonNull InviteFriendAdapter.MyViewHolder holder, int position) {
        model.getData().remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, model.getData().size());
        holder.itemView.setVisibility(View.GONE);
    }

}
