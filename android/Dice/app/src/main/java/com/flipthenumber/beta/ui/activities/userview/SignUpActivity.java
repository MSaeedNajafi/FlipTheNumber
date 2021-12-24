package com.flipthenumber.beta.ui.activities.userview;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.flipthenumber.beta.Constants.MessageInterface;
import com.flipthenumber.beta.Helper.CustomToast;
import com.flipthenumber.beta.adapter.AdapterDayMonthYear;
import com.flipthenumber.beta.ui.activities.userview.presenter.SignUpActivityPresenter;
import com.flipthenumber.beta.MainActivity;
import com.flipthenumber.beta.R;
import com.hbb20.CountryCodePicker;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, MessageInterface.View, DatePickerDialog.OnDateSetListener {
    private Unbinder unbinder;
    @BindView(R.id.name_etxt)
    EditText name_etxt;
    @BindView(R.id.email_etxt)
    EditText email_etxt;
    @BindView(R.id.dob_etxt)
    EditText dob_etxt;
    @BindView(R.id.radioGrp)
    RadioGroup radioGrp;
    private RadioButton radioSexButton;
    @BindView(R.id.pass_etxt)
    EditText pass_etxt;
    @BindView(R.id.mobile_etxt)
    EditText mobile_etxt;
    @BindView(R.id.countryCodeHolder)
    CountryCodePicker countryCodeHolder;
    @BindView(R.id.signup_btn)
    Button signup_btn;
    @BindView(R.id.img_back)
    ImageView img_back;
    @BindView(R.id.skip_txt)
    TextView skip_txt;
    @BindView(R.id.already_account_txt)
    TextView already_account_txt;
    String countryCode;
    MessageInterface.SignUpPresenter presenter;
    DatePickerDialog datePickerDialog;
    private ArrayList<String>arrayList_month,arrayList_day,arrayList_year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        init();
        initUI();
    }

    private void init() {

        unbinder = ButterKnife.bind(this);
        presenter = new SignUpActivityPresenter(SignUpActivity.this,this);
       // getSupportActionBar().hide();
    }

    private void initUI() {
        img_back.setOnClickListener(this::onClick);
        signup_btn.setOnClickListener(this::onClick);
        skip_txt.setOnClickListener(this::onClick);
        already_account_txt.setOnClickListener(this::onClick);
        dob_etxt.setOnClickListener(this::onClick);
        countryCodeHolder.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                //Alert.showMessage(RegistrationActivity.this, ccp.getSelectedCountryCodeWithPlus());
                countryCode = countryCodeHolder.getSelectedCountryCodeWithPlus();

            }
        });

         arrayList_day=new ArrayList<>();
         arrayList_month=new ArrayList<>();
         arrayList_year=new ArrayList<>();

         for(int i=1;i<=31;i++){
            arrayList_day.add(String.valueOf(i));
        }
        for(int j=1;j<=12;j++){
            arrayList_month.add(String.valueOf(j));
        }
        for(int k=1970;k<=2021;k++){
            arrayList_year.add(String.valueOf(k));
        }


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.signup_btn:
                isValidateData();
                break;
            case R.id.already_account_txt:
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                break;
            case R.id.skip_txt:
                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                break;
            case R.id.dob_etxt:

                showDatePickerDialog();





//                Calendar c = Calendar.getInstance();
//                c.add(Calendar.YEAR, -19);
//                int day = c.get(Calendar.DAY_OF_MONTH);
//                int month = c.get(Calendar.MONTH);
//                int year = c.get(Calendar.YEAR);
//
//                datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
//                       // String date = dayOfMonth + "/" + monthOfYear + "/" + year;
//                        i1=i1+1;
//                        String date_is= i2 + "/" + i1 + "/" + i;
//                        dob_etxt.setText(date_is);
//                        datePickerDialog.dismiss();
//
//                    }
//                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
//
//                datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                    @Override
//                    public void onCancel(DialogInterface dialogInterface) {
//
//                        //Toast.makeText(getApplicationContext(),"Clicked",Toast.LENGTH_LONG).show();
//                    }
//                });
//                datePickerDialog.show();




//                DatePickerDialog dialog = new DatePickerDialog(SignUpActivity.this, this, year, month, day);
//                dialog.getDatePicker().setMaxDate(c.getTimeInMillis());
//                dialog.show();
//
//                final DatePickerDialog dp = new DatePickerDialog(SignUpActivity.this, new    DatePickerDialog.OnDateSetListener() {
//
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//
//                       // Calendar newDate = Calendar.getInstance();
//                       // newDate.set(year, monthOfYear, dayOfMonth);
//                        String date = dayOfMonth + "/" + monthOfYear + "/" + year;
//                        dob_etxt.setText(date);
//                        dialog.dismiss();
//
//                    }
//                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
//
//                dp.show();






                //dp.getDatePicker().setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);

               break;

        }
    }



    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        //monthOfYear=monthOfYear+1;
      //  dob_etxt.setText(dayOfMonth + "-" + monthOfYear + "-" + year);
    }



    private void isValidateData() {

        String name = name_etxt.getText().toString();
        String email = email_etxt.getText().toString();
        String dob = dob_etxt.getText().toString();
        int selectedId = radioGrp.getCheckedRadioButtonId();
        // find the radiobutton by returned id
        radioSexButton = (RadioButton) findViewById(selectedId);
        String gender = (String) radioSexButton.getText();
        String mobile = mobile_etxt.getText().toString();
        String pass = pass_etxt.getText().toString();

        if (TextUtils.isEmpty(name)) {
            OnError("Please Enter your Name!");
        }

        else if (TextUtils.isEmpty(dob)) {
            OnError("Please select your D.O.B.!");
        }

//        else if (TextUtils.isEmpty(gender)) {
//            OnError("Please select your gender!");
//
//        }

        else if (TextUtils.isEmpty(email)) {
            OnError("Please Enter your Email Address!");
        }

        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_etxt.setError("Please enter a Valid Email Address!");
        }

//        if (TextUtils.isEmpty(countryCode)) {
//            OnError("Please Select Country Code!");
//
//        }  else if (TextUtils.isEmpty(mobile)) {
//            OnError("Please enter your mobile number!");
//        }

        else if (TextUtils.isEmpty(pass)) {
            OnError("Please Enter your Password!");
        } else {
            presenter.doSignUp(name, email, dob, "","+91","", pass);
        }
    }

    @Override
    public void onSuccess(String message) {
        CustomToast.displayMessage(SignUpActivity.this, message);
    }

    @Override
    public void OnError(String message) {
        CustomToast.displayError(SignUpActivity.this, message);
    }


    private Dialog progressDialog(){
        final Dialog dialog = new Dialog(SignUpActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_progress);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        return dialog;
    }



    private void showDatePickerDialog() {

        final Dialog dialog = new Dialog(SignUpActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_custum_date_picker);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView tv_done = dialog.findViewById(R.id.tv_done);
        TextView tv_month = dialog.findViewById(R.id.tv_month);
        TextView tv_day = dialog.findViewById(R.id.tv_day);
        TextView tv_year = dialog.findViewById(R.id.tv_year);
        RelativeLayout layout_month = dialog.findViewById(R.id.layout_month);
        RelativeLayout layout_day = dialog.findViewById(R.id.layout_day);
        RelativeLayout layout_year = dialog.findViewById(R.id.layout_year);

        tv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                        String date_is= tv_day.getText().toString().trim() + "/" + tv_month.getText().toString().trim() + "/" + tv_year.getText().toString().trim();
                        dob_etxt.setText(date_is);
                        dialog.dismiss();
            }
        });

        layout_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                shwoMonthDialog("month",tv_month);

            }
        });

        layout_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shwoMonthDialog("day",tv_day);
            }
        });

        layout_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shwoMonthDialog("year",tv_year);
            }
        });


        dialog.show();

    }

    private void shwoMonthDialog(String find,TextView tv_) {

        final Dialog dialog = new Dialog(SignUpActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_day_month_year);

        ImageView iv_close = dialog.findViewById(R.id.iv_close);
        RecyclerView recylerview = dialog.findViewById(R.id.recylerview);

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recylerview.setLayoutManager(layoutManager);

        if(find.trim().equals("month")){
            recylerview.setAdapter(new AdapterDayMonthYear(getApplicationContext(), arrayList_month,tv_,dialog));
        }
        else if(find.trim().equals("day")){
            recylerview.setAdapter(new AdapterDayMonthYear(getApplicationContext(), arrayList_day,tv_,dialog));
        }
        else if(find.trim().equals("year")){
            recylerview.setAdapter(new AdapterDayMonthYear(getApplicationContext(), arrayList_year,tv_,dialog));
        }

        dialog.show();

    }


}