package com.flipthenumber.beta.adapter.InviteAdapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flipthenumber.beta.Constants.MessageInterface;
import com.flipthenumber.beta.Constants.UserConstant;
import com.flipthenumber.beta.Helper.CustomToast;
import com.flipthenumber.beta.Helper.SharedHelper;
import com.flipthenumber.beta.R;
import com.flipthenumber.beta.adapter.MyRequestAdapter;
import com.flipthenumber.beta.model.MyRequestModel;
import com.flipthenumber.beta.model.SendRequestModel;
import com.flipthenumber.beta.model.UserListModel;
import com.flipthenumber.beta.retrofit.ApiInterface;
import com.flipthenumber.beta.retrofit.AppConfig;
import com.squareup.picasso.Picasso;

import java.net.SocketTimeoutException;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllinvitesRequestsAdapter extends RecyclerView.Adapter<AllinvitesRequestsAdapter.MyViewHolder> implements MessageInterface.View {
    private Context mContext;
    private MyRequestModel model;

    public AllinvitesRequestsAdapter(Context mContext, MyRequestModel model) {
        this.mContext = mContext;
        this.model = model;
    }

    @NonNull
    @Override
    public AllinvitesRequestsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_all_users, parent, false);
        return new AllinvitesRequestsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllinvitesRequestsAdapter.MyViewHolder holder, int position) {
        Picasso.get().load(model.getData().get(position).getImage()).into(holder.iv_user);
        holder.tv_name.setText(model.getData().get(position).getName());

        if(model.getData().get(position).getFrom().equals("invite")){
            holder.iv_invite_friend.setVisibility(View.GONE);
        }else {
            holder.iv_invite_friend.setVisibility(View.VISIBLE);
        }


        holder.iv_invite_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String token = SharedHelper.getKey(mContext, UserConstant.userToken);
                String id =model.getData().get(position).getId().toString();
                methodAccept(holder, token, id, position);

            }
        });


    }

    @Override
    public int getItemCount() {
        return model.getData().size();
    }

    @Override
    public void onSuccess(String message) {
        Toast.makeText(mContext,message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void OnError(String message) {
        Toast.makeText(mContext,message,Toast.LENGTH_LONG).show();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_user)
        CircleImageView iv_user;
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.iv_invite_friend)
        ImageView iv_invite_friend;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    private void methodAccept(MyViewHolder holder, String token, String id, int position) {
        ProgressDialog progressDialog=new ProgressDialog(mContext);
        progressDialog.setTitle("Loading..");
        progressDialog.show();

        ApiInterface apiInterface = AppConfig.getRetrofit().create(ApiInterface.class);
        Call<MyRequestModel> call = apiInterface.accept_request("Bearer " + token, id);
        call.enqueue(new Callback<MyRequestModel>() {
            @Override
            public void onResponse(Call<MyRequestModel> call, Response<MyRequestModel> response) {
                progressDialog.dismiss();
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
                progressDialog.dismiss();
                OnError(t.getMessage());
                if (t instanceof SocketTimeoutException) {
                    methodAccept(holder, token, id, position);
                }
            }
        });

    }

    private void deleteItem(@NonNull MyViewHolder holder, int position) {
        model.getData().remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, model.getData().size());
        holder.itemView.setVisibility(View.GONE);
    }

}
