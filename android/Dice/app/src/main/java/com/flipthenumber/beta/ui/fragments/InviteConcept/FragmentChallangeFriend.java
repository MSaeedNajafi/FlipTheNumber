package com.flipthenumber.beta.ui.fragments.InviteConcept;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.flipthenumber.beta.R;
import com.flipthenumber.beta.ui.fragments.HomeFragment;

public class FragmentChallangeFriend extends Fragment implements View.OnClickListener {
    private TextView tv_users,tv_invites,tv_friends;
    private View view;
    private View view_users,view_invites,view_friends;
    private RelativeLayout layout_one,layout_two,layout_three;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_change_a_friend, container, false);

        Init();
        ClickListener();
        InitUI();

        return view;

    }

    private void ClickListener() {
        tv_users.setOnClickListener(this);
        tv_invites.setOnClickListener(this);
        tv_friends.setOnClickListener(this);
    }

    private void Init() {
        tv_users=view.findViewById(R.id.tv_users);
        tv_invites=view.findViewById(R.id.tv_invites);
        tv_friends=view.findViewById(R.id.tv_friends);
        view_users=view.findViewById(R.id.view_users);
        view_invites=view.findViewById(R.id.view_invites);
        view_friends=view.findViewById(R.id.view_friends);
        layout_one=view.findViewById(R.id.layout_one);
        layout_two=view.findViewById(R.id.layout_two);
        layout_three=view.findViewById(R.id.layout_three);
    }

    private void InitUI() {
        layout_one.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_white_red));
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, new AllUserFragment()).commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.tv_users:
                layout_one.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_white_red));
                layout_two.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_white_view));
                layout_three.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_white_view));
                view_users.setVisibility(View.VISIBLE);
                view_invites.setVisibility(View.GONE);
                view_friends.setVisibility(View.GONE);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, new AllUserFragment()).commit();

                break;

            case R.id.tv_invites:
                layout_one.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_white_view));
                layout_two.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_white_red));
                layout_three.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_white_view));
                view_users.setVisibility(View.GONE);
                view_invites.setVisibility(View.VISIBLE);
                view_friends.setVisibility(View.GONE);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, new AllinvitesRequests()).commit();

                break;

            case R.id.tv_friends:
                layout_one.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_white_view));
                layout_two.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_white_view));
                layout_three.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_white_red));
                view_users.setVisibility(View.GONE);
                view_invites.setVisibility(View.GONE);
                view_friends.setVisibility(View.VISIBLE);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, new FragmentAllFriends()).commit();

                break;
        }
    }
}
