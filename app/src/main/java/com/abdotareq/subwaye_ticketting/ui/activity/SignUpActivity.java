package com.abdotareq.subwaye_ticketting.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.abdotareq.subwaye_ticketting.R;
import com.abdotareq.subwaye_ticketting.databinding.ActivitySignUpBinding;

import java.util.Calendar;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;

    private String[] gender_array = {"Female", "Male"};

    private Calendar materialCalendar;
    private DatePickerDialog datePicker;


    String gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(this, R.layout.gender_spinner_row, R.id.gender_tv, gender_array);

        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.signUpGenderSpinner.setAdapter(genderAdapter);
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
                int year = materialCalendar.get(Calendar.YEAR);

                datePicker = new DatePickerDialog(SignUpActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
                        Toast.makeText(SignUpActivity.this, "mDay" + mDay + " month:" + (mMonth + 1) + "mYear:" + mYear, Toast.LENGTH_SHORT).show();
                    }
                }, year, month, day);//changed from day,month,year to year,month,day
                datePicker.show();
            }
        });


    }
}
