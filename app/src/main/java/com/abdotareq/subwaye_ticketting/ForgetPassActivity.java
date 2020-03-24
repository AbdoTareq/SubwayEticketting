package com.abdotareq.subwaye_ticketting;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.abdotareq.subwaye_ticketting.databinding.ActivityForgetPassBinding;
import com.abdotareq.subwaye_ticketting.databinding.ActivitySignInBinding;
import com.abdotareq.subwaye_ticketting.model.dto.User;
import com.abdotareq.subwaye_ticketting.model.retrofit.UserService;
import com.abdotareq.subwaye_ticketting.utility.util;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ForgetPassActivity extends AppCompatActivity {

    private ActivityForgetPassBinding binding;
    private EditText mailEt;
    private Button sendCode;

    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);

        // this for view binding to replace findviewbyid
        binding = ActivityForgetPassBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //initialize retrofit object
        retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mailEt = binding.forgetPassMailEt;
        sendCode = binding.forgetPassSendVerificationBtn;

        sendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!util.isValidEmail(mailEt.getText().toString())) {
                    mailEt.setText("");
                    mailEt.setHint(getString(R.string.invalid_mail_warning));
                    return;
                } else
                    sendVerificationCode(mailEt.getText().toString());
            }
        });

    }

    // send send Verification Code to user mail
    private void sendVerificationCode(final String mail) {

        User user = new User(mail);

        //create UserService object
        UserService userService = retrofit.create(UserService.class);

        //initialize the save user call
        Call<ResponseBody> resetPassCall = userService.sendVerificationCode(user);

        //initialize and show a progress dialog to the user
        final ProgressDialog progressDialog = util.initProgress(ForgetPassActivity.this, getString(R.string.loading));
        progressDialog.show();

        //start the call
        resetPassCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                int responseCode = response.code();
                if (responseCode >= 200 && responseCode <= 299 && response.body() != null) {
                    //user saved successfully
                    progressDialog.dismiss();
                    //ToDo: Remove Toast
                    Intent intent = new Intent(ForgetPassActivity.this, VerificationActivity.class);
                    intent.putExtra("mail", mail);
                    startActivity(intent);
                    finish();
                } else {
                    //user not saved successfully
                    progressDialog.dismiss();
                    Toast.makeText(ForgetPassActivity.this, getString(R.string.else_on_repsonse), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                progressDialog.dismiss();
                Toast.makeText(ForgetPassActivity.this, getString(R.string.error_message), Toast.LENGTH_LONG).show();
//                //ToDo: Remove Toast
//                Log.e("FAILED : ", t.getMessage());
            }
        });


    }


}
