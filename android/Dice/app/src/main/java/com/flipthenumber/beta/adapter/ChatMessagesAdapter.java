package com.flipthenumber.beta.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flipthenumber.beta.R;
import com.flipthenumber.beta.model.ModelGetMsg;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatMessagesAdapter extends RecyclerView.Adapter<ChatMessagesAdapter.MyViewHolder> {

    Context mContext;
    ModelGetMsg model;

    public ChatMessagesAdapter(Context mContext, ModelGetMsg model) {
        this.mContext = mContext;
        this.model = model;
    }

    @NonNull
    @Override
    public ChatMessagesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_chat_messages, parent, false);
        return new ChatMessagesAdapter.MyViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull ChatMessagesAdapter.MyViewHolder holder, int position) {


        holder.tv_msg.setText(model.getData().get(position).getMsg());

        if(model.getData().get(position).getSide().trim().equals("right")){
            holder.tv_msg.setGravity(Gravity.RIGHT);
        }


    }






    @Override
    public int getItemCount() {
        return model.getData().size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_msg)
        public TextView tv_msg;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
