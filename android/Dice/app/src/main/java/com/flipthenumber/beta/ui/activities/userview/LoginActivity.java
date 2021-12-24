package com.flipthenumber.beta.ui.activities.userview;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.flipthenumber.beta.Constants.MessageInterface;
import com.flipthenumber.beta.Constants.UserConstant;
import com.flipthenumber.beta.Helper.CustomToast;
import com.flipthenumber.beta.Helper.SharedHelper;
import com.flipthenumber.beta.MainActivity;
import com.flipthenumber.beta.R;
import com.flipthenumber.beta.ui.activities.userview.presenter.LoginActivityPresenter;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class LoginActivity extends AppCompatActivity implements MessageInterface.View, View.OnClickListener
         {
    private Unbinder unbinder;


    private static final int RC_SIGN_IN = 1;
    private static final String TAG = "LoginActivity";
    String name;
    String email;
    private CallbackManager callbackManager;

    MessageInterface.PresenterLogin presenter;

    @BindView(R.id.email_etxt)
    EditText email_etxt;
    @BindView(R.id.pass_etxt)
    EditText pass_etxt;
    @BindView(R.id.login_btn)
    Button login_btn;
    @BindView(R.id.fb_button)
    LoginButton fb_button;
    @BindView(R.id.google_img)
    ImageView google_img;
    @BindView(R.id.fb_img)
    ImageView fb_img;
    @BindView(R.id.skip_txt)
    TextView skip_txt;
    @BindView(R.id.no_account_txt)
    TextView no_account_txt;
    String userId;
    String firstName = null;
    String lastName = null;
    String userToken;
    String deviceToken;
    private TextView tv_forgot_password;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initilizefb();
        setContentView(R.layout.activity_login);
        getToken();
        init();
        initUI();
    }

    private void init() {
        unbinder = ButterKnife.bind(this);
        deviceToken = SharedHelper.getKey(LoginActivity.this, UserConstant.firbasetoken);
        presenter = new LoginActivityPresenter(LoginActivity.this);
        tv_forgot_password=findViewById(R.id.tv_forgot_password);
        tv_forgot_password.setOnClickListener(this);
    }

    private void initUI() {
        login_btn.setOnClickListener(this::onClick);
        no_account_txt.setOnClickListener(this::onClick);
        skip_txt.setOnClickListener(this::onClick);
        google_img.setOnClickListener(this::onClick);
        fb_img.setOnClickListener(this::onClick);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        fbLogin();
    }


    @Override
    public void onSuccess(String message) {
        CustomToast.displayMessage(LoginActivity.this, message);
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void OnError(String message) {
        CustomToast.displayMessage(LoginActivity.this, message);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                isValidateData();
                break;

            case R.id.no_account_txt:
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                break;

            case R.id.skip_txt:
                 presenter.doGuestLogin();
                break;

            case R.id.google_img:
               // Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
               // startActivityForResult(intent, RC_SIGN_IN);
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);

                break;

            case R.id.fb_img:
                fb_button.performClick();
                break;

            case R.id.tv_forgot_password:
                Intent intent_forgot=new Intent(LoginActivity.this,ActivityForgetPassword.class);
                startActivity(intent_forgot);
                break;

        }
    }

    private void isValidateData() {

        String email = email_etxt.getText().toString();
        String pass = pass_etxt.getText().toString();
        if (TextUtils.isEmpty(email)) {
            OnError("Please Enter your Email Address!");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_etxt.setError("Please enter a Valid Email Address!");
        } else if (TextUtils.isEmpty(pass)) {
            OnError("Please Enter your Password!");
        } else {

            presenter.doLogin(email, pass);

        }
    }


    public Dialog progressDialog() {
        Dialog dialog = new Dialog(LoginActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_progress);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        return dialog;
    }



             @Override
             public void onActivityResult(int requestCode, int resultCode, Intent data) {
                 super.onActivityResult(requestCode, resultCode, data);

                 // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
                 if (requestCode == RC_SIGN_IN) {
                     // The Task returned from this call is always completed, no need to attach
                     // a listener.
                     Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                     handleSignInResult(task);
                 }
             }


             private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
                 try {
                     GoogleSignInAccount account = completedTask.getResult(ApiException.class);
                     Toast.makeText(getApplicationContext(),account.getDisplayName(),Toast.LENGTH_LONG).show();
                     // Signed in successfully, show authenticated UI.
                    // updateUI(account);
                 } catch (ApiException e) {

                     Log.i("ErrorIs",e.toString());
                     // The ApiException status code indicates the detailed failure reason.
                     // Please refer to the GoogleSignInStatusCodes class reference for more information.
                     Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
                     Toast.makeText(getApplicationContext(),e.getStatusCode()+"",Toast.LENGTH_LONG).show();
                     //updateUI(null);
                 }
             }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == RC_SIGN_IN) {
//            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//            handleSignInResult(result);
//        } else {
//            callbackManager.onActivityResult(requestCode, resultCode, data);
//            System.out.println("++++++++fb++" + data.toString());
//        }
//    }
//
//    private void handleSignInResult(GoogleSignInResult result) {
//        if (result.isSuccess()) {
//            GoogleSignInAccount account = result.getSignInAccount();
//            // userId =account.getId();
//            // userToken=account.getId();
//            firstName = account.getFamilyName();
//            lastName = account.getGivenName();
//            name = account.getDisplayName();
//            email = account.getEmail();
//
//            System.out.println("+++++++++++++ " + name);
//            presenter.doSocialLogin(name, email, "Google", "Android",deviceToken );
//            // you can store user data to SharedPreference
//
//        } else {
//            // Google Sign In failed, update UI appropriately
//            Log.e(TAG, "Login Unsuccessful. " + result);
//            Log.e(TAG, "Login Unsuccessful. " + result.getStatus());
//            Toast.makeText(this, "Login Unsuccessful", Toast.LENGTH_SHORT).show();
//        }
//    }


    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>fb>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    private void initilizefb() {
        FacebookSdk.sdkInitialize(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        } catch (Exception e) {
            e.printStackTrace();
        }


        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("com.flipthenumber.beta", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
                Log.i("hash_keyIsHere",something);
                System.out.println(">>>>>>>>>>>>>>>>>>" + something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }

    private void fbLogin() {
        List<String> permissionNeeds = Arrays.asList("user_photos", "email",
                "user_birthday", "public_profile", "AccessToken");
        fb_button.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        System.out.println("onSuccess");

                        String accessToken = loginResult.getAccessToken()
                                .getToken();
                        Log.i("accessToken", accessToken);

                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object,
                                                            GraphResponse response) {

                                        Log.e(TAG, object.toString());
                                        Log.e(TAG, response.toString());

                                        try {
                                            userId = object.getString("id");
                                            userToken = object.getString("id");
                                            //userToken = object.getString("access_token");


                                            if (object.has("first_name"))
                                                firstName = object.getString("first_name");
                                            if (object.has("last_name"))
                                                lastName = object.getString("last_name");
                                            name = firstName + " " + lastName;

                                            if (object.has("email"))
                                                email = object.getString("email");

                                            if (email == null) {
                                                email = firstName + userId + "@gmail.com";
                                            }

                                            System.out.println("<<<<<<<<<<<<<<<" + userId + "  " + name + "   " + email);
                                            presenter.doSocialLogin(name,email,"Facebook","Android",deviceToken);
                                            LoginManager.getInstance().logOut();

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id, first_name, last_name, email, birthday, gender");
                        request.setParameters(parameters);
                        request.executeAsync();

                    }

                    @Override
                    public void onCancel() {
                        System.out.println("onCancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        System.out.println("onError");
                        Log.v("LoginActivity", exception.getMessage());
                    }
                });
  /*  private void facebookLogin() {

        fb_button.setReadPermissions("public_profile");

        fb_button.registerCallback(callbackManager, callback);

    }

    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

            GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    Log.e(TAG, object.toString());
                    Log.e(TAG, response.toString());

                    try {
                        userId = object.getString("id");
                        userToken = object.getString("id");
                        //userToken = object.getString("access_token");


                        if (object.has("first_name"))
                            firstName = object.getString("first_name");
                        if (object.has("last_name"))
                            lastName = object.getString("last_name");
                        name = firstName + " " + lastName;

                        if (object.has("email"))
                            email = object.getString("email");

                        if (email == null) {
                            email = firstName + userId + "@gmail.com";
                        }

                        System.out.println("<<<<<<<<<<<<<<<" + userId + "  " + name + "   " + email);
                        presenter.doSocialLogin(name,email,"Facebook","Android",deviceToken);
                        LoginManager.getInstance().logOut();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            //Here we put the requested fields to be returned from the JSONObject
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id, first_name, last_name, email, birthday, gender");
            request.setParameters(parameters);
            request.executeAsync();



        }

        @Override
        public void onCancel() {
            System.out.println("+++++++++++++cancel+");
        }

        @Override
        public void onError(FacebookException e) {
            System.out.println("++++++FacebookException++++++++"+e.getMessage());
        }
    };*/

    }


    private void getToken(){

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        SharedHelper.putKey(LoginActivity.this, UserConstant.firbasetoken, token);
                        // Log and toast

                        Log.d(TAG, token);
                        // Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}