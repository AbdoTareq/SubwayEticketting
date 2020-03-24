package com.abdotareq.subwaye_ticketting;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.abdotareq.subwaye_ticketting.databinding.ActivityVerificationBinding;
import com.abdotareq.subwaye_ticketting.model.dto.Token;
import com.abdotareq.subwaye_ticketting.model.dto.User;
import com.abdotareq.subwaye_ticketting.model.retrofit.UserService;
import com.abdotareq.subwaye_ticketting.utility.util;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VerificationActivity extends AppCompatActivity {

    private ActivityVerificationBinding binding;

    private TextView mailTv;
    private EditText codeEt;
    private Button continueBtn;

    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        final String mail = getIntent().getStringExtra("mail");

        // this for view binding to replace findviewbyid
        binding = ActivityVerificationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //initialize retrofit object
        retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mailTv = binding.verificationMailTv;
        codeEt = binding.verificationEt;
        continueBtn = binding.verificationContinueBtn;

        mailTv.setText(mail);


        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(codeEt.getText().toString()) && codeEt.getText().toString().equals("")) {
                    codeEt.setText(getText(R.string.enter_verification));
                    return;
                } else {
                    verifyCode(mail, codeEt.getText().toString());
                }
            }
        });


    }

    private void verifyCode(String mail, String code) {
        User user = new User(mail, code);

        //create UserService object
        UserService userService = retrofit.create(UserService.class);

        //initialize the save user call
        Call<Token> call = userService.verifyCode(user);

        //initialize and show a progress dialog to the user
        final ProgressDialog progressDialog = util.initProgress(VerificationActivity.this, getString(R.string.loading));
        progressDialog.show();

        //start the call
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {

                int responseCode = response.code();
                if (responseCode >= 200 && responseCode <= 299 && response.body() != null) {
                    //user saved successfully
                    progressDialog.dismiss();
                    //ToDo: Remove Toast
                    Intent intent = new Intent(VerificationActivity.this, ChangePassActivity.class);
                    intent.putExtra("token", response.body().getToken());
                    Toast.makeText(VerificationActivity.this, "token: " + response.body().getToken(), Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finish();
                } else {
                    //user not saved successfully
                    progressDialog.dismiss();
                    Toast.makeText(VerificationActivity.this, getString(R.string.else_on_repsonse), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {

                progressDialog.dismiss();
                Toast.makeText(VerificationActivity.this, getString(R.string.error_message), Toast.LENGTH_LONG).show();
//                //ToDo: Remove Toast
//                Log.e("FAILED : ", t.getMessage());
            }
        });

    }


}
