package com.flipthenumber.beta.adapter.InviteAdapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.flipthenumber.beta.Constants.MessageInterface;
import com.flipthenumber.beta.Constants.UserConstant;
import com.flipthenumber.beta.Helper.SharedHelper;
import com.flipthenumber.beta.R;
import com.flipthenumber.beta.model.FriendsModel;
import com.squareup.picasso.Picasso;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class AllFriendsAdapter extends RecyclerView.Adapter<AllFriendsAdapter.MyViewHolder> implements MessageInterface.View {

    private Context mContext;
    private FriendsModel model;

    public AllFriendsAdapter(Context mContext, FriendsModel model) {
        this.mContext = mContext;
        this.model = model;
    }

    @NonNull
    @Override
    public AllFriendsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_all_users, parent, false);
        return new AllFriendsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllFriendsAdapter.MyViewHolder holder, int position) {
        Picasso.get().load(model.getData().get(position).getImage()).into(holder.iv_user);
        holder.tv_name.setText(model.getData().get(position).getName());



    }

    @Override
    public int getItemCount() {
        return model.getData().size();
    }

    @Override
    public void onSuccess(String message) {

    }

    @Override
    public void OnError(String message) {

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_user)
        CircleImageView iv_user;
        @BindView(R.id.tv_name)
        TextView tv_name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
