package com.abdotareq.subwaye_ticketting.utility;

import android.app.ProgressDialog;
import android.content.Context;


import com.abdotareq.subwaye_ticketting.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A util class that contains a global static methods used across the App
 */
public class util {

    //TYPES ID
    public static final int HELP_TYPE_ID = 1;
    public static final int PRIVACY_TYPE_ID = 2;
    public static final int INFO_TYPE_ID = 3;
    public static final int ABOUT_US_TYPE_ID = 4;

    // Validate pass is 8 - 12
    public static boolean isValidPassword(String target) {
        return Pattern.compile("^(?=.*\\d)(?=.*[a-zA-Z])[a-zA-Z0-9]{8,12}$").matcher(target).matches();
    }

    public static boolean isValidEmail(CharSequence target) {String regExpn = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
            + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
            + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
            + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        return !matcher.matches();
    }

    public static ProgressDialog initProgress(Context context, String message) {
        ProgressDialog progress = new ProgressDialog(context, R.style.ProgressDialogStyle);
        progress.setMessage(message);
        progress.setCancelable(true); // disable dismiss by tapping outside of the dialog
        return progress;
    }

    public static String formatDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(date);
    }

}
