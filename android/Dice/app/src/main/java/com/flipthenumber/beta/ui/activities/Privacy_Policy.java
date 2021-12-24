package com.flipthenumber.beta.ui.activities;

import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.flipthenumber.beta.Constants.UserConstant;
import com.flipthenumber.beta.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Privacy_Policy extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.img_back)
    ImageView back_img;
    @BindView(R.id.policy)
    WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy__policy);

        init();
        initUI();

    }

    private void init(){
        ButterKnife.bind(this);
    }
    private void initUI(){
        back_img.setOnClickListener(this::onClick);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(UserConstant.policy_url);
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress)
            {

                // Return the app name after finish loading
                    /*if (progress==100)
                        progressDialog.dismiss();*/

            }
        });
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                //progressDialog.dismiss();
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed(); // Ignore SSL certificate errors
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_back:
                finish();
                break;
        }
    }
}