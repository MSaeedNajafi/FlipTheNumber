package com.flipthenumber.beta.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.flipthenumber.beta.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChallengeAdapter extends RecyclerView.Adapter<ChallengeAdapter.MyViewHolder> {
    Context mContext;
    ArrayList dashbordList;

    public ChallengeAdapter(Context mContext, ArrayList dashbordList) {
        this.mContext = mContext;
        this.dashbordList = dashbordList;
    }

    @NonNull
    @Override
    public ChallengeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.challenge_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ChallengeAdapter.MyViewHolder holder, int position) {

        holder.img.setImageResource((Integer) dashbordList.get(position));

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return dashbordList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img)
        public ImageView img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
