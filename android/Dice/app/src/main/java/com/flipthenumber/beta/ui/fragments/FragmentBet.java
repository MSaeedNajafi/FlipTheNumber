package com.flipthenumber.beta.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.flipthenumber.beta.MainActivity;
import com.flipthenumber.beta.R;
import com.flipthenumber.beta.ui.activities.ActivityPlayerVsPlayerView;
import com.flipthenumber.beta.ui.activities.ActivityPlayerVsPlayerViewNew;
import com.flipthenumber.beta.ui.activities.Privacy_Policy;

public class FragmentBet  extends Fragment implements View.OnClickListener {
private EditText et_coins;
private ImageView iv_continue;
private RelativeLayout layout_without_bet;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_bet, container, false);
        init(view);
        ClickListener();

        return view;
    }

    private void ClickListener() {
        iv_continue.setOnClickListener(this);
        layout_without_bet.setOnClickListener(this);

    }

    private void init(View view) {
      et_coins=view.findViewById(R.id.et_coins);
      iv_continue=view.findViewById(R.id.iv_continue);
      layout_without_bet=view.findViewById(R.id.layout_without_bet);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.iv_continue:

                if(et_coins.getText().toString().trim().equals("")){
                    Toast.makeText(getActivity(),"Bet Coins empty",Toast.LENGTH_LONG).show();
                }
                else {

                    int total_tokens=Integer.parseInt(MainActivity.total_coin_txt.getText().toString().trim());
                    int bet_tokens=Integer.parseInt(et_coins.getText().toString().trim());

                    if(total_tokens<bet_tokens){
                     Toast.makeText(getActivity(),"You Have Less Coins for Bet!",Toast.LENGTH_LONG).show();
                    }
                    else {
                        Intent intent = new Intent(getActivity(), ActivityPlayerVsPlayerViewNew.class);
                        intent.putExtra("bet_status","true");
                        intent.putExtra("bet_coins",et_coins.getText().toString().trim());
                        startActivity(intent);
                    }

                }

                break;


            case R.id.layout_without_bet:

                Intent intent = new Intent(getActivity(), ActivityPlayerVsPlayerViewNew.class);
                intent.putExtra("bet_status","false");
                intent.putExtra("bet_coins","");
                startActivity(intent);

                break;

        }

    }
}
