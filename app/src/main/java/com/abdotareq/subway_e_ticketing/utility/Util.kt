package com.abdotareq.subway_e_ticketing.utility

import android.animation.ObjectAnimator
import android.app.ProgressDialog
import android.content.Context
import android.view.View
import com.abdotareq.subway_e_ticketing.R
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

/**
 * A util class that contains a global static methods used across the App
 */
object Util {
    //TYPES ID
    const val HELP_TYPE_ID = 1
    const val PRIVACY_TYPE_ID = 2
    const val INFO_TYPE_ID = 3
    const val ABOUT_US_TYPE_ID = 4

    // Validate pass is 8 - 12
    // return true when pass isn't valid
    fun isValidPassword(target: String?): Boolean {
        return Pattern.compile("^(?=.*\\d)(?=.*[a-zA-Z])[a-zA-Z0-9]{8,12}$").matcher(target).matches()
    }

    // return true when mail isn't valid
    fun isValidEmail(target: CharSequence?): Boolean {
        val regExpn = ("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$")
        val pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(target)
        return !matcher.matches()
    }

    fun initProgress(context: Context?, message: String?): ProgressDialog {
        val progress = ProgressDialog(context, R.style.ProgressDialogStyle)
        progress.setMessage(message)
        progress.setCancelable(true) // disable dismiss by tapping outside of the dialog
        return progress
    }

    fun formatDate(date: Date?): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy")
        return formatter.format(date)
    }

}