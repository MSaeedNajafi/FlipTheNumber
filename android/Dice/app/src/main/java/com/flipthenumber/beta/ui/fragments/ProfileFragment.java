package com.flipthenumber.beta.ui.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.flipthenumber.beta.Constants.MessageInterface;
import com.flipthenumber.beta.Constants.UserConstant;
import com.flipthenumber.beta.Helper.CustomToast;
import com.flipthenumber.beta.Helper.SharedHelper;
import com.flipthenumber.beta.model.ModelImageSend;
import com.flipthenumber.beta.model.UserModel;
import com.flipthenumber.beta.retrofit.ApiInterface;
import com.flipthenumber.beta.retrofit.AppConfig;
import com.flipthenumber.beta.ui.activities.userview.LoginActivity;
import com.flipthenumber.beta.ui.activities.userview.presenter.LoginActivityPresenter;
import com.flipthenumber.beta.MainActivity;
import com.flipthenumber.beta.R;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment implements View.OnClickListener {

    private Unbinder unbinder;
    @BindView(R.id.edit_btn)
    TextView edit_btn;

    @BindView(R.id.user_name)
    EditText user_name;

    @BindView(R.id.user_email)
    TextView user_email;

    @BindView(R.id.fbShareImgBtn)
    ImageButton fbShareImgBtn;
    @BindView(R.id.back_btn)
    Button back_btn;
    /*@BindView(R.id.login_btn)
    Button login_btn;*/

    TextView logout_btn;
    View view;

    MessageInterface.PresenterLogin presenter;


    @BindView(R.id.editImg)
    CircleImageView editImg;
    @BindView(R.id.proileImg)
    CircleImageView proileImg;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;
    String token;
    String userId;

    private TextView user_password;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        init(view);
        initUI();
        return view;
    }

    private void init(View view) {
        unbinder = ButterKnife.bind(this, view);

        presenter = new LoginActivityPresenter(getActivity());

        if (SharedHelper.getBooleanKey(getActivity(), UserConstant.isLoginGuest)) {
           // login_btn.setVisibility(View.VISIBLE);
            edit_btn.setVisibility(View.GONE);
        }

        token = SharedHelper.getKey(getActivity(), UserConstant.userToken);
        userId = SharedHelper.getKey(getActivity(), UserConstant.id);

    }


    private void initUI() {

        user_password=view.findViewById(R.id.user_password);
        logout_btn=view.findViewById(R.id.logout_btn);

        edit_btn.setOnClickListener(this);
        back_btn.setOnClickListener(this);

        logout_btn.setOnClickListener(this);

       // login_btn.setOnClickListener(this);
        fbShareImgBtn.setOnClickListener(this);
        editImg.setOnClickListener(this);



        user_name.setText(SharedHelper.getKey(getActivity(), UserConstant.name));



        user_email.setText(SharedHelper.getKey(getActivity(), UserConstant.email));

        user_password.setText(SharedHelper.getKey(getActivity(), UserConstant.password));

       // proileImg.getBorderColor()



        String img=SharedHelper.getKey(getActivity(), UserConstant.userImage);
        Log.i("image_user_is","img>> "+img);

        if(!img.trim().equals("")){
            Picasso.get().load(SharedHelper.getKey(getActivity(), UserConstant.userImage)).into(proileImg);
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.editImg:
                Log.i("click","click_img");

                //permissionRequest();

                if (checkAndRequestPermissions()) {
                    selectImage();
                } else {
                    checkAndRequestPermissions();
                }


                break;



            case R.id.edit_btn:

               // startActivity(new Intent(getActivity(), EditProfileActivity.class));

                checkValidationsAndUpdateData();

                break;

            case R.id.fbShareImgBtn:
                shareFacebook(getActivity(), "FlipTheNumber", "FlipTheNumber.com");
                break;
            case R.id.back_btn:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();
                break;
           /* case R.id.login_btn:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();*/


            case R.id.logout_btn:

                SharedHelper.clearSharedPreferences(getActivity());
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();

                break;


        }
    }




    public static void shareFacebook(Activity activity, String text, String url) {
        boolean facebookAppFound = false;
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, url);
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(url));

        PackageManager pm = activity.getPackageManager();
        List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);
        for (final ResolveInfo app : activityList) {
            if ((app.activityInfo.packageName).contains("com.facebook.katana")) {
                final ActivityInfo activityInfo = app.activityInfo;
                final ComponentName name = new ComponentName(activityInfo.applicationInfo.packageName, activityInfo.name);
                shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                shareIntent.setComponent(name);
                facebookAppFound = true;
                break;
            }
        }
        if (!facebookAppFound) {
            String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + url;
            shareIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
        }
        activity.startActivity(shareIntent);
    }



    private void checkValidationsAndUpdateData() {

        if(user_name.getText().toString().trim().equals("")){
            Toast.makeText(getActivity(),"Empty Name",Toast.LENGTH_LONG).show();
        }

        else if(user_password.getText().toString().trim().equals("")){
            Toast.makeText(getActivity(),"Empty Password",Toast.LENGTH_LONG).show();
        }

        else if(user_password.getText().toString().trim().length()<6){
            Toast.makeText(getActivity(),"Short Password",Toast.LENGTH_LONG).show();
        }

        else {
            String userId = SharedHelper.getKey(getActivity(), UserConstant.id);
            String name = user_name.getText().toString().trim();
            String email = SharedHelper.getKey(getActivity(), UserConstant.email);
            String password = user_password.getText().toString().trim();



            methodUpdateProfile(userId,name,email,password);


        }

    }

    private void methodUpdateProfile(String userId, String name, String email, String password) {


        ProgressDialog dialog = ProgressDialog.show(getActivity(), "Loading", "Please wait...", true);
        String userToken = SharedHelper.getKey(getActivity(), UserConstant.userToken);
        ApiInterface apiInterface = AppConfig.getRetrofit().create(ApiInterface.class);

        Log.i("UserIdIs",userId);


        Call<UserModel> call = apiInterface.editProfileNew("Bearer " + userToken, userId, name, email,password);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    System.out.println("+++++++++++++ "+response.body().getData());
                    CustomToast.displayMessage(getActivity(), response.body().getMessage());


                    if(response.body().getStatus()){

                        SharedHelper.putKey(getActivity(), UserConstant.name, response.body().getData().get(0).getName());
                        SharedHelper.putKey(getActivity(), UserConstant.password,password);
                        getActivity().startActivity(new Intent(getContext(), MainActivity.class));
                        ((Activity) getActivity()).finish();

                    }



                } else {

                    CustomToast.displayError(getActivity(), response.message());
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                dialog.dismiss();
                CustomToast.displayError(getActivity(), t.getMessage());

            }
        });


    }




    private boolean checkAndRequestPermissions() {
        int camera = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA);
        int storage = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int storageRead = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
        }
        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (storageRead != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


    private void selectImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    startActivityForResult(intent, 1);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }

    @SuppressLint("LongLogTag")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                proileImg.setImageBitmap(photo);

                Uri tempUri = getImageUri(getActivity(), photo);

                // CALL THIS METHOD TO GET THE ACTUAL PATH
                File finalFile = new File(getRealPathFromURI(tempUri));
                Log.w("path of image from gallery......******************.........", finalFile + "");
                uploadImage(token, userId, finalFile);
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                Log.w("path of image from gallery......******************.........", picturePath + "");
                Uri tempUri = getImageUri(getActivity(), thumbnail);

                // CALL THIS METHOD TO GET THE ACTUAL PATH
                File finalFile = new File(getRealPathFromURI(tempUri));
                proileImg.setImageBitmap(thumbnail);
                uploadImage(token, userId, finalFile);
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getActivity().getContentResolver() != null) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }


    private void uploadImage(String token, String userId, File file) {

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

        ProgressDialog dialog = ProgressDialog.show(getActivity(), "Loading", "Please wait...", true);
        ApiInterface apiInterface = AppConfig.getRetrofit().create(ApiInterface.class);

        Log.i("sending_detail_is",token+"\n"+userId);

        //Call<UserModel> call = apiInterface.updateProfileNew(userId, body);

       // RequestBody token_ = RequestBody.create(MediaType.parse("text/plain"), "Bearer " + token);
        RequestBody id = RequestBody.create(MediaType.parse("text/plain"), userId);



        Call<ModelImageSend> call = apiInterface.updateProfile("Bearer " + token, id, body);

        //Call<UserModel> call = apiInterface.updateProfile(token, userId, body);


        call.enqueue(new Callback<ModelImageSend>() {
            @Override
            public void onResponse(Call<ModelImageSend> call, Response<ModelImageSend> response) {
                dialog.dismiss();

                Log.i("response_code_is",String.valueOf(response.code()));


               // Log.i("status_is",String.valueOf(response.body().getStatus()));

                if(response.isSuccessful()){
                    Toast.makeText(getActivity(),response.body().getMessage(), Toast.LENGTH_SHORT).show();


                    if(response.body().getStatus()){

                        SharedHelper.putKey(getActivity(), UserConstant.userImage, response.body().getData().get(0).getUserImage());

                        MainActivity.iv_profile_img.setVisibility(View.VISIBLE);
                        Picasso.get().load(SharedHelper.getKey(getActivity(), UserConstant.userImage)).into(MainActivity.iv_profile_img);


                    }
                }

            }


            @Override
            public void onFailure(Call<ModelImageSend> call, Throwable t) {
                dialog.dismiss();
                Log.i("response_code_is","Faliure"+t.toString());

                if (t instanceof SocketTimeoutException) {
                    uploadImage(token, userId, file);
                }
            }
        });
    }



}