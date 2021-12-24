package com.flipthenumber.beta.ui.activities.userview;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.flipthenumber.beta.Constants.MessageInterface;
import com.flipthenumber.beta.Constants.UserConstant;
import com.flipthenumber.beta.Helper.SharedHelper;
import com.flipthenumber.beta.retrofit.ApiInterface;
import com.flipthenumber.beta.retrofit.AppConfig;
import com.flipthenumber.beta.ui.activities.userview.presenter.LoginActivityPresenter;
import com.flipthenumber.beta.R;
import com.hbb20.CountryCodePicker;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.RequestBody;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    @BindView(R.id.editImg)
    CircleImageView editImg;
    @BindView(R.id.proileImg)
    CircleImageView proileImg;

    @BindView(R.id.name_etxt)
    EditText name_etxt;
    @BindView(R.id.email_etxt)
    EditText email_etxt;
    @BindView(R.id.gender_etxt)
    EditText gender_etxt;
    @BindView(R.id.dob_etxt)
    EditText dob_etxt;
    @BindView(R.id.mobile_etxt)
    EditText mobile_etxt;
    @BindView(R.id.countryCodeHolder)
    CountryCodePicker countryCodeHolder;
    MessageInterface.PresenterLogin presenter;
    @BindView(R.id.logout_btn)
    Button logout_btn;
    @BindView(R.id.savebtn)
    Button savebtn;
    @BindView(R.id.img_back)
    ImageView back;
    String token;
    String userId;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    String countryCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        //getSupportActionBar().hide();
        init();
        initUI();
    }

    private void init() {

        ButterKnife.bind(this);
        presenter = new LoginActivityPresenter(EditProfileActivity.this);
        token = SharedHelper.getKey(EditProfileActivity.this, UserConstant.userToken);
        userId = SharedHelper.getKey(EditProfileActivity.this, UserConstant.id);
    }

    private void initUI() {
        back.setOnClickListener(this);
        editImg.setOnClickListener(this);
        logout_btn.setOnClickListener(this);
        savebtn.setOnClickListener(this);
        dob_etxt.setOnClickListener(this);
        name_etxt.setText(SharedHelper.getKey(EditProfileActivity.this, UserConstant.name));
        email_etxt.setText(SharedHelper.getKey(EditProfileActivity.this, UserConstant.email));

        String dob = SharedHelper.getKey(EditProfileActivity.this, UserConstant.dob);

        if (!TextUtils.isEmpty(dob) && !dob.equalsIgnoreCase("null")) {
            dob_etxt.setText(dob);
        }
        String gender = SharedHelper.getKey(EditProfileActivity.this, UserConstant.gender);
        if (!TextUtils.isEmpty(gender) && !gender.equalsIgnoreCase("null")) {
            gender_etxt.setText(gender);
        }

        countryCode = SharedHelper.getKey(EditProfileActivity.this, UserConstant.countryCode);


        if (!TextUtils.isEmpty(countryCode) && !countryCode.equalsIgnoreCase("null")) {
            countryCodeHolder.setCountryForPhoneCode(Integer.parseInt(countryCode));
        } else {
            countryCode = countryCodeHolder.getDefaultCountryNameCode().toString();
        }

        String mobile = SharedHelper.getKey(EditProfileActivity.this, UserConstant.mobile);
        if (!TextUtils.isEmpty(mobile) && !mobile.equalsIgnoreCase("null")) {
            mobile_etxt.setText(mobile);
        }


        countryCodeHolder.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                //Alert.showMessage(RegistrationActivity.this, ccp.getSelectedCountryCodeWithPlus());
                countryCode = countryCodeHolder.getSelectedCountryCodeWithPlus();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.editImg:
                if (checkAndRequestPermissions()) {
                    selectImage();
                } else {
                    checkAndRequestPermissions();
                }

                break;
            case R.id.dob_etxt:
                Calendar c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);
                String date = day + "-" + (month + 1) + "-" + year;
                System.out.println("++++++++++++++ " + date);

                DatePickerDialog dialog = new DatePickerDialog(EditProfileActivity.this, this, year, month, day);
                dialog.getDatePicker().setMaxDate(c.getTimeInMillis());
                dialog.show();
                break;
            case R.id.savebtn:
                String userId = SharedHelper.getKey(EditProfileActivity.this, UserConstant.id);
                String name = name_etxt.getText().toString();
                String dob = dob_etxt.getText().toString();
                String gender = gender_etxt.getText().toString();
                String mobile = mobile_etxt.getText().toString();
                if (TextUtils.isEmpty(countryCode)) {
                    Toast.makeText(this, "Please Select Country Code!", Toast.LENGTH_SHORT).show();
                } else if (mobile.length() < 10) {
                    Toast.makeText(this, "Please Enter Valid Mobile Number!", Toast.LENGTH_SHORT).show();
                } else {
                   // presenter.doEditProfile(userId, name, gender, dob, countryCode, mobile);
                }
                break;
            case R.id.logout_btn:
                SharedHelper.clearSharedPreferences(EditProfileActivity.this);
                startActivity(new Intent(EditProfileActivity.this, LoginActivity.class));
                finish();
                break;
            case R.id.img_back:
                finish();
                break;
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        dob_etxt.setText(i2 + "-" + i1 + "-" + i);
    }

    private void selectImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                proileImg.setImageBitmap(photo);

                Uri tempUri = getImageUri(getApplicationContext(), photo);

                // CALL THIS METHOD TO GET THE ACTUAL PATH
                File finalFile = new File(getRealPathFromURI(tempUri));
                Log.w("path of image from gallery......******************.........", finalFile + "");
                uploadImage(token, userId, finalFile);
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                Log.w("path of image from gallery......******************.........", picturePath + "");
                Uri tempUri = getImageUri(getApplicationContext(), thumbnail);

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
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

    private boolean checkAndRequestPermissions() {
        int camera = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
        int storage = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int storageRead = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

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
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


    private void uploadImage(String token, String userId, File file) {
//        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//        // MultipartBody.Part is used to send also the actual file name
//        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
//
//        ProgressDialog dialog = ProgressDialog.show(EditProfileActivity.this, "Loading", "Please wait...", true);
//        ApiInterface apiInterface = AppConfig.getRetrofit().create(ApiInterface.class);
//        Call<UserModel> call = apiInterface.updateProfile("Bearer " + token, userId, body);
//        call.enqueue(new Callback<UserModel>() {
//            @Override
//            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
//                dialog.dismiss();
//                Toast.makeText(EditProfileActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
//                SharedHelper.putKey(EditProfileActivity.this, UserConstant.userImage, response.body().getData().get(0).getUserImage());
//            }
//
//            @Override
//            public void onFailure(Call<UserModel> call, Throwable t) {
//                dialog.dismiss();
//                if (t instanceof SocketTimeoutException) {
//                    uploadImage(token, userId, file);
//                }
//            }
//        });
    }

    private void uploadFile(Uri fileUri) {
        // create upload service client

        ApiInterface apiInterface = AppConfig.getRetrofit().create(ApiInterface.class);


        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        //  File file = FileUtils.getFile(this, fileUri);

        // create RequestBody instance from file
      /*  RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(getContentResolver().getType(fileUri)),
                        file
                );*/

        // MultipartBody.Part is used to send also the actual file name
      /*  MultipartBody.Part body =
                MultipartBody.Part.createFormData("picture", file.getName(), requestFile);*/

        // add another part within the multipart request
        String descriptionString = "hello, this is description speaking";
        RequestBody description =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, descriptionString);

        // finally, execute the request
       /* Call<ResponseBody> call = apiInterface.upload(description, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                Log.v("Upload", "success");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });*/
    }

}