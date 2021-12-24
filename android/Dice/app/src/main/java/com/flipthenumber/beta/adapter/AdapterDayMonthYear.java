package com.flipthenumber.beta.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flipthenumber.beta.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterDayMonthYear extends RecyclerView.Adapter<AdapterDayMonthYear.MyViewHolder> {

    private Context mContext;
    ArrayList<String> arrayList_month;
    TextView tv_;
    Dialog dialog;

    public AdapterDayMonthYear(Context mContext, ArrayList<String> arrayList_month,TextView tv_,Dialog dialog) {
        this.mContext = mContext;
        this.arrayList_month = arrayList_month;
        this.tv_=tv_;
        this.dialog=dialog;
    }

    @NonNull
    @Override
    public AdapterDayMonthYear.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_day_month_year, parent, false);
        return new AdapterDayMonthYear.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterDayMonthYear.MyViewHolder holder, int position) {

       holder.tv_txt.setText(arrayList_month.get(position));

       holder.layout_view_click.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               tv_.setText(arrayList_month.get(position));
               dialog.dismiss();

           }
       });

    }


    @Override
    public int getItemCount() {
        return arrayList_month.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_txt;
        private RelativeLayout layout_view_click;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_txt=itemView.findViewById(R.id.tv_txt);
            layout_view_click=itemView.findViewById(R.id.layout_view_click);
        }
    }
}
