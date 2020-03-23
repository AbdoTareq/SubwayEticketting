package com.abdotareq.subwaye_ticketting.ui.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.abdotareq.subwaye_ticketting.R;
import com.abdotareq.subwaye_ticketting.databinding.ActivitySignUpBinding;
import com.abdotareq.subwaye_ticketting.model.dto.Token;
import com.abdotareq.subwaye_ticketting.model.dto.User;
import com.abdotareq.subwaye_ticketting.model.retrofit.UserService;
import com.abdotareq.subwaye_ticketting.utility.SharedPreferenceUtil;
import com.abdotareq.subwaye_ticketting.utility.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * The Activity Controller Class that is responsible for handling all UI inputs and outputs
 * for the SignUp Activity
 */
public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;

    private String[] genderList = {"Female", "Male"};

    private Calendar materialCalendar;
    private DatePickerDialog datePicker;

    private Button signUpBtn, calenderBtn, genderBtn;
    private TextView signInTv;

    private Retrofit retrofit;


    int year = 0;
    Date date;
    String formatDate;

    String gender = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //initialize retrofit object
        retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        callListeners();


    }


    // Call listeners on the activity for code readability
    private void callListeners() {

        genderBtn = binding.signUpGenderBtn;
        calenderBtn = binding.signUpCalender;
        signUpBtn = binding.signUpBtn;
        signInTv = binding.signUpSignInTv;

        genderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                builder.setTitle(getString(R.string.select_gender));

                builder.setItems(genderList, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position) {
                        gender = genderList[position];
                        genderBtn.setText(gender);
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        calenderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                materialCalendar = Calendar.getInstance();

                int day = materialCalendar.get(Calendar.DAY_OF_MONTH);
                int month = materialCalendar.get(Calendar.MONTH);
                year = materialCalendar.get(Calendar.YEAR);
                date = materialCalendar.getTime();
                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                formatDate = format1.format(date);


                datePicker = new DatePickerDialog(SignUpActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
                        Toast.makeText(SignUpActivity.this, date.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, year, month, day);//changed from day,month,year to year,month,day
                datePicker.show();
            }
        });

        //sign up method that will call the web service
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpBtnClick();
            }
        });

        signInTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * A method used to save a new user (sign up)
     */
    public void saveUserCall(User user) {

        //create UserService object
        UserService service = retrofit.create(UserService.class);

        //initialize the save user call
        Call<Token> call = service.saveUser(user);

        //initialize and show a progress dialog to the user
        final ProgressDialog progressDialog = util.initProgress(this, getString(R.string.progMessage));
        progressDialog.show();

        //start the call
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {

                int responseCode = response.code();
                if (responseCode >= 200 && responseCode <= 299) {
                    //user saved successfully
                    progressDialog.dismiss();

                    Intent returnIntent = getIntent();
                    //write token into SharedPreferences
                    SharedPreferenceUtil.setSharedPrefsLoggedIn(SignUpActivity.this, true);
                    if (response.body() != null) {
                        SharedPreferenceUtil.setSharedPrefsUserId(SignUpActivity.this, response.body().getToken());
                    }

                    Intent intent =new Intent(SignUpActivity.this,HomeActivity.class);
                    startActivity(intent);

//                    returnIntent.putExtra("MOBILE", phone.getText().toString());
                    setResult(RESULT_OK, returnIntent);
                    Toast.makeText(SignUpActivity.this, "success", Toast.LENGTH_SHORT).show();

                } else if (responseCode == 434) {
                    Toast.makeText(SignUpActivity.this, getText(R.string.pass_war), Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                } else if (responseCode == 435) {
                    Toast.makeText(SignUpActivity.this, getText(R.string.mail_exist), Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                } else {
                    //user not saved successfully
                    progressDialog.dismiss();
                    Toast.makeText(SignUpActivity.this, "else onResponse " + responseCode, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {

                progressDialog.dismiss();
                Toast.makeText(SignUpActivity.this, getString(R.string.error_message), Toast.LENGTH_LONG).show();
            }
        });

    }

    /**
     * A method called to handle sign up button clicks
     */
    private void signUpBtnClick() {
        //check for all inputs from user are correct
        if (TextUtils.isEmpty(binding.signUpFNameEt.getText().toString()) && binding.signUpFNameEt.getText().toString().equals("")) {
            binding.signUpFNameEt.setText(getText(R.string.fix_fist_name));
            return;
        } else if (TextUtils.isEmpty(binding.signUpLNameEt.getText().toString()) && binding.signUpLNameEt.getText().toString().equals("")) {
            binding.signUpLNameEt.setText(getText(R.string.fix_last_name_mess));
            return;
        } else if (!util.isValidEmail(binding.signUpMailEt.getText().toString())) {
            binding.signUpMailEt.setText(getString(R.string.invalid_mail_warning));
            return;
        } else if (!util.isValidPassword(binding.signUpPassEt.getText().toString())) {
            binding.signUpPassEt.setText("");
            binding.signUpPassEt.setHint(getString(R.string.pass_warning));
            return;
        } else if (!(binding.signUpPassEt.getText().toString().equals(binding.signUpConfirmPassEt.getText().toString()))) {
            binding.signUpConfirmPassEt.setText("");
            binding.signUpConfirmPassEt.setHint(getString(R.string.fix_confirmPassWarning));
            return;
        } else if (gender.isEmpty()) {
            Toast.makeText(this, getText(R.string.select_gender), Toast.LENGTH_SHORT).show();
            return;
        } else if (year == 0) {
            Toast.makeText(this, getText(R.string.select_birthday), Toast.LENGTH_SHORT).show();
            return;
        }

        //create MobileUser object and set it's attributes
        User user = new User();
        user.setFirst_name(binding.signUpFNameEt.getText().toString());
        user.setLast_name(binding.signUpLNameEt.getText().toString());
        user.setEmail(binding.signUpMailEt.getText().toString());
        user.setPassword(binding.signUpPassEt.getText().toString());
        user.setGender(gender);
        user.setBirth_date(formatDate);
        user.setAdmin(0);

        Log.e("SignUpActivity", user.toString());

        //sign up method that will call the web service
        saveUserCall(user);

    }


}
