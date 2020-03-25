package com.abdotareq.subwaye_ticketting.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.abdotareq.subwaye_ticketting.R;
import com.abdotareq.subwaye_ticketting.databinding.ActivitySignInBinding;
import com.abdotareq.subwaye_ticketting.model.dto.Token;
import com.abdotareq.subwaye_ticketting.model.dto.User;
import com.abdotareq.subwaye_ticketting.model.retrofit.UserService;
import com.abdotareq.subwaye_ticketting.utility.SharedPreferenceUtil;
import com.abdotareq.subwaye_ticketting.utility.util;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignInActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private ActivitySignInBinding binding;

    private Button signInBtn;
    private TextView recoverPassTv, signUpTv;
    private EditText mailEt, passEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //initialize retrofit object
        retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        callListeners();

    }

    // Call listeners on the activity for code readability
    private void callListeners() {

        signInBtn = binding.signInBtn;
        signUpTv = binding.signInSignUpTv;
        recoverPassTv = binding.signInRecoverPassTv;

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInBtnClick();
            }
        });

        signUpTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        recoverPassTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, ForgetPassActivity.class);
                startActivity(intent);
            }
        });


    }

    // Handle sign in Button clicks
    private void signInBtnClick() {

        mailEt = binding.signInMailEt;
        passEt = binding.signInPassEt;

        mailEt.setText("abdo.elbishihi@gmail.com");
//        passEt.setText("abdo1234");

        //check for all inputs from user are not empty
        if (!util.isValidEmail(mailEt.getText().toString())) {
            mailEt.setText("");
            mailEt.setHint(getString(R.string.invalid_mail_warning));
            return;
        } else if (!util.isValidPassword(passEt.getText().toString())) {
            passEt.setText("");
            passEt.setHint(getString(R.string.pass_warning));
            return;
        }

        User user = new User(mailEt.getText().toString());

        user.setPassword(passEt.getText().toString());

        Log.e("SignInActivity: ", user.toString());

        authenticate(user);

    }

    /**
     * A method used to authenticate user (sign in)
     * can't be boolean as it has a thread which method won't wait until it finishes
     */
    private void authenticate(User user) {

        //create UserService object
        UserService service = retrofit.create(UserService.class);

        //initialize the save user call
        Call<Token> call = service.authenticate(user);

        //initialize and show a progress dialog to the user
        final ProgressDialog progressDialog = util.initProgress(this, getString(R.string.progMessage));
        progressDialog.show();

        //start the call
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {

                int responseCode = response.code();

                if (responseCode >= 200 && responseCode <= 299 && response.body() != null) {
                    //user authenticated successfully
                    progressDialog.dismiss();

                    //write token into SharedPreferences
                    SharedPreferenceUtil.setSharedPrefsLoggedIn(SignInActivity.this, true);
                    SharedPreferenceUtil.setSharedPrefsUserId(SignInActivity.this, response.body().getToken());

                    Intent intent =new Intent(SignInActivity.this,HomeLandActivity.class);
                    startActivity(intent);

                    passEt.setHint("token:   " + response.body().getToken());

//                        if (getIntent().hasExtra("LOGGED_OUT")) {
//                            Intent mainIntent = new Intent(SignInActivity.this, MainActivity.class);
//                            startActivity(mainIntent);
//                        }
//                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
//                    startActivity(intent);
//                    finish();
                } else if (responseCode == 436) {
                    //user not authenticated successfully
                    progressDialog.dismiss();
                    Toast.makeText(SignInActivity.this, getText(R.string.wrong_mail_or_pass), Toast.LENGTH_LONG).show();
                } else {
                    //user not authenticated successfully
                    progressDialog.dismiss();
                    Toast.makeText(SignInActivity.this, "else onResponse", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {

                progressDialog.dismiss();
                Toast.makeText(SignInActivity.this, getText(R.string.error_message), Toast.LENGTH_LONG).show();
            }
        });


    }


}
