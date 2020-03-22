package com.abdotareq.subwaye_ticketting.ui.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.abdotareq.subwaye_ticketting.MainActivity;
import com.abdotareq.subwaye_ticketting.R;
import com.abdotareq.subwaye_ticketting.databinding.ActivitySignUpBinding;
import com.abdotareq.subwaye_ticketting.model.dto.User;
import com.abdotareq.subwaye_ticketting.model.retrofit.UserService;
import com.abdotareq.subwaye_ticketting.utility.util;

import java.util.Calendar;

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

    private String[] gender_array = {"Female", "Male"};

    private Calendar materialCalendar;
    private DatePickerDialog datePicker;

    private Retrofit retrofit;
    private ArrayAdapter<String> genderAdapter;

    int year = 0;
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

        genderAdapter = new ArrayAdapter<String>(this, R.layout.spinner_gender, R.id.gender_tv, gender_array);

        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.signUpGenderSpinner.setAdapter(genderAdapter);


        callListeners();


    }


    // Call listeners on the activity
    private void callListeners() {
        binding.signUpGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                gender = String.valueOf(binding.signUpGenderSpinner.getSelectedItem());

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                binding.signUpGenderSpinner.setSelection(0);
            }
        });

        binding.signUpCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                materialCalendar = Calendar.getInstance();
                int day = materialCalendar.get(Calendar.DAY_OF_MONTH);
                int month = materialCalendar.get(Calendar.MONTH);
                year = materialCalendar.get(Calendar.YEAR);

                datePicker = new DatePickerDialog(SignUpActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
                        Toast.makeText(SignUpActivity.this, "mDay" + mDay + " month:" + (mMonth + 1) + "mYear:" + mYear, Toast.LENGTH_SHORT).show();
                    }
                }, year, month, day);//changed from day,month,year to year,month,day
                datePicker.show();
            }
        });

        //sign up method that will call the web service
        binding.signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpBtnClick();
            }
        });

        binding.signUpSignInTv.setOnClickListener(new View.OnClickListener() {
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
        Call<ResponseBody> call = service.saveUser(user);

        //initialize and show a progress dialog to the user
        final ProgressDialog progressDialog = util.initProgress(this, getString(R.string.progMessage));
        progressDialog.show();

        //start the call
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                int responseCode = response.code();
                if (responseCode >= 200 && responseCode <= 299) {
                    //user saved successfully
                    progressDialog.dismiss();

                    Intent returnIntent = getIntent();
//                    returnIntent.putExtra("MOBILE", phone.getText().toString());
                    setResult(RESULT_OK, returnIntent);
                    Toast.makeText(SignUpActivity.this, "success", Toast.LENGTH_SHORT).show();

                } else {
                    //user not saved successfully
                    progressDialog.dismiss();
                    Toast.makeText(SignUpActivity.this, "else onResponse", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

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
        user.setPassword(binding.signUpPassEt.getText().toString());
        user.setGender(gender);
        user.setAge(year);

        Log.e("SignUpActivity", user.toString());

        //sign up method that will call the web service
        saveUserCall(user);

    }


}
