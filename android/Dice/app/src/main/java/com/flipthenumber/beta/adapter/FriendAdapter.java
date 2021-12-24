package com.flipthenumber.beta.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
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
import com.flipthenumber.beta.model.FriendsModel;
import com.flipthenumber.beta.model.MyRequestModel;
import com.squareup.picasso.Picasso;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.MyViewHolder> implements MessageInterface.View
 ,Filterable{
    private Context mContext;
    private List<FriendsModel.Datum> model;
    private List<FriendsModel.Datum> model_filter;

    public FriendAdapter(Context mContext, List<FriendsModel.Datum> model) {
        this.mContext = mContext;
        this.model = model;
        this.model_filter=model;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.friend_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get().load(model.get(position).getImage()).into(holder.profie_image);
        holder.name_txt.setText(model.get(position).getName());
        holder.email_txt.setText(model.get(position).getEmail().toString());


        holder.unfriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String token = SharedHelper.getKey(mContext, UserConstant.userToken);
                String id = model.get(position).getId().toString();
                unFriend(holder,token,id,position);

            }
        });




    }

    @Override
    public int getItemCount() {
        return model_filter.size();
    }




    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.profie_image)
        CircleImageView profie_image;
        @BindView(R.id.name_txt)
        TextView name_txt;
        @BindView(R.id.email_txt)
        TextView email_txt;
        @BindView(R.id.unfriendBtn)
        Button unfriendBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void unFriend(@NonNull FriendAdapter.MyViewHolder holder,String token,String id,int position){

        ApiInterface apiInterface = AppConfig.getRetrofit().create(ApiInterface.class);
        Call<MyRequestModel> call = apiInterface.unFriend("Bearer "+token,id);
        call.enqueue(new Callback<MyRequestModel>() {
            @Override
            public void onResponse(Call<MyRequestModel> call, Response<MyRequestModel> response) {
                if (response.isSuccessful()){
                    if (response.code()==200){
                        onSuccess("Unfriend Successfully");
                        deleteItem(holder,position);
                    }else {
                        OnError(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<MyRequestModel> call, Throwable t) {
                OnError(t.getMessage());
                if (t instanceof SocketTimeoutException){
                    unFriend(holder,token, id,position);
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

    private void deleteItem(@NonNull MyViewHolder holder,int position) {
        model.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, model.size());
        holder.itemView.setVisibility(View.GONE);
    }



    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                Log.i("SerachingTextIs",charString);

                if (charString.isEmpty()) {
                    model_filter = model;
                } else {

                    List<FriendsModel.Datum> filteredList = new ArrayList<>();

                    for (FriendsModel.Datum row : model) {

                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {

                            filteredList.add(row);
                            Log.i("rowGetName",row.getName());

                        }

                    }

                    model_filter=filteredList;

                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = model_filter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                Log.i("FiltersResults",filterResults.values.toString());
                //model_filter = (ArrayList<FriendsModel.Datum>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }




}
