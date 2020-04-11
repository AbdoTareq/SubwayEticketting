package com.abdotareq.subway_e_ticketing.controller.fragment

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.abdotareq.subway_e_ticketing.R
import com.abdotareq.subway_e_ticketing.controller.fragment.registration.SignInFragment
import com.abdotareq.subway_e_ticketing.databinding.FragmentProfileSettingsBinding
import com.abdotareq.subway_e_ticketing.model.dto.User
import com.abdotareq.subway_e_ticketing.model.retrofit.UserApiObj
import com.abdotareq.subway_e_ticketing.utility.SharedPreferenceUtil
import com.abdotareq.subway_e_ticketing.utility.imageUtil.BitmapConverter
import com.abdotareq.subway_e_ticketing.utility.util
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

/**
 *  [ProfileSettingsFragment] responsible for user profile settings & changes.
 */
// TODO FIX LOGOUT PROBLEM
class ProfileSettingsFragment : Fragment() {

    private var _binding: FragmentProfileSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var user: User? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentProfileSettingsBinding.inflate(inflater, container, false)
        val view = binding.root

        getUserData()

        callListeners()

        return view
    }

    // get user obj from splash screen or get user call
    private fun getUserData() {
        // receive user obj from splash screen
        try {
            user = activity?.intent?.getParcelableExtra("user") as User
            showData()
        } catch (e: Exception) {
            Timber.e(" getUser $e")
            // if failed to get user obj from splash screen get user call
            getUserData(SharedPreferenceUtil.getSharedPrefsTokenId(context))
        }

    }

    private fun callListeners() {
        val genderList = arrayOf("Female", "Male")
        var year = 0
        var date: Date?
        var formatDate: String? = null
        var gender = ""
        var materialCalendar: Calendar
        var datePicker: DatePickerDialog

        binding.changePassBtn.setOnClickListener {
            openDialog()
        }

        binding.logOutBtn.setOnClickListener {
            confirmLogOut()
        }

        binding.updateBtn.setOnClickListener {
            saveBtnClk(gender, year, formatDate)
        }

        binding.genderBtn.setOnClickListener {
            val builder = android.app.AlertDialog.Builder(context)
            builder.setTitle(getString(R.string.select_gender))
            builder.setItems(genderList) { dialogInterface, position ->
                gender = genderList[position]
                binding.genderBtn.text = gender
            }
            val alertDialog = builder.create()
            alertDialog.show()
        }

        binding.calender.setOnClickListener {
            materialCalendar = Calendar.getInstance()
            val day = materialCalendar.get(Calendar.DAY_OF_MONTH)
            val month = materialCalendar.get(Calendar.MONTH)
            year = materialCalendar.get(Calendar.YEAR)
            date = materialCalendar.time
            val format1 = SimpleDateFormat("yyyy-MM-dd")
            formatDate = format1.format(date)
            datePicker = DatePickerDialog(context!!, DatePickerDialog.OnDateSetListener { datePicker, mYear, mMonth, mDay ->
                Toast.makeText(context, date.toString(), Toast.LENGTH_SHORT).show()
            }
                    , year, month, day) //changed from day,month,year to year,month,day
            datePicker.show()
        }


    }

    private fun getUserData(userIdToken: String) {
        var bearerToken = "Bearer "
        bearerToken += userIdToken

        //initialize and show a progress dialog to the user
        val progressDialog = util.initProgress(context, getString(R.string.progMessage))
        progressDialog.show()

        //start the call
        UserApiObj.retrofitService.getUser(bearerToken).enqueue(object : Callback<User?> {
            override fun onResponse(call: Call<User?>, response: Response<User?>) {
                val responseCode = response.code()
                if (responseCode in 200..299 && response.body() != null) {
                    //get user successfully
                    user = response.body()!!
                    progressDialog.dismiss()
                    showData()

                } else if (responseCode == 438) {
                    //pass not saved successfully
                    Toast.makeText(context, getString(R.string.user_not_found), Toast.LENGTH_LONG).show()
                    Timber.e(getString(R.string.user_not_found))
                    progressDialog.dismiss()
                } else {
                    //user not saved successfully
                    Toast.makeText(context, getString(R.string.else_on_repsonse), Toast.LENGTH_LONG).show()
                    Timber.e(getString(R.string.else_on_repsonse))
                    progressDialog.dismiss()

                }
            }

            override fun onFailure(call: Call<User?>, t: Throwable) {
                Toast.makeText(context, getString(R.string.failure_happened), Toast.LENGTH_LONG).show()
                Timber.e("$t")
                progressDialog.dismiss()
            }
        })
    }

    // set profile fields
    private fun showData() {

        try {
            binding.firstNameEt.setText(user?.first_name)
            binding.lastNameEt.setText(user?.last_name)
            binding.mail.text = user?.email
            binding.genderBtn.text = user?.gender
            binding.calender.text = user?.birth_date?.substring(0..9)

            //if image exists
            if (user!!.image != null) {
                val bitMapCon = BitmapConverter(BitmapConverter.AsyncResponse {
                    binding.profileImage.setImageBitmap(it)
                })
                bitMapCon.execute(user?.image)
            }
        } catch (e: Exception) {
            Timber.e(e)
        }

    }

    private fun confirmLogOut() {
        //create a dialog interface to notify user that he is going to log out
        val dialogClickListener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    //reset the saved user data from the shared preferences
                    SharedPreferenceUtil.setSharedPrefsLoggedIn(context, false)
                    SharedPreferenceUtil.setSharedPrefsTokenId(context, "-1")

                    //start the sign in activity
                    SharedPreferenceUtil.setSharedPrefsLoggedIn(context, false)
                    SharedPreferenceUtil.setSharedPrefsTokenId(context, "-1")
                    findNavController().navigate(ProfileSettingsFragmentDirections.actionProfileSettingsFragmentToPassNav())
                }
            }
        }

        //Create AlertDialog Builder and the AlertDialog with the desired message
        val builder = context?.let { AlertDialog.Builder(it) }
        val dialog = builder!!.setMessage(getString(R.string.do_u_want_logout))
                .setPositiveButton(getText(R.string.ok), dialogClickListener)
                .setNegativeButton(getString(R.string.no), dialogClickListener).create()

        //change the color of the buttons
        dialog.setOnShowListener { //set the negative button with the red color
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getColor(context!!, android.R.color.holo_red_dark))
            //set the positive button with the primary color
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getColor(context!!, R.color.colorPrimary))
        }

        //show the dialog
        dialog.show()
    }

    // this for change pass dialog
    private fun openDialog() {
        val passDialog = ChangePassDialogFragment(binding.mail.text.toString())
        fragmentManager?.let { passDialog.show(it, "Pass Dialog") }
    }

    private fun saveBtnClk(gender: String, year: Int, formatDate: String?) {
        //check for all inputs from user are correct
        if (TextUtils.isEmpty(binding.firstNameEt.text.toString()) && binding.firstNameEt.text.toString() == "") {
            binding.firstNameEt.hint = getText(R.string.fix_fist_name)
            return
        } else if (TextUtils.isEmpty(binding.lastNameEt.text.toString()) && binding.lastNameEt.text.toString() == "") {
            binding.lastNameEt.hint = getText(R.string.fix_last_name_mess)
            return
        } else if (gender.isEmpty()) {
            Toast.makeText(context, getText(R.string.select_gender), Toast.LENGTH_SHORT).show()
            return
        } else if (year == 0) {
            Toast.makeText(context, getText(R.string.select_birthday), Toast.LENGTH_SHORT).show()
            return
        }

        // update the original user with only changed data
        // TODO ADD IMAGE TO USER
        user?.first_name = binding.firstNameEt.text.toString()
        user?.last_name = binding.lastNameEt.text.toString()
        user?.gender = gender
        user?.birth_date = formatDate

        Timber.e("$user")

        updateUser()

    }

    private fun updateUser() {
        var bearerToken = "Bearer "
        bearerToken += SharedPreferenceUtil.getSharedPrefsTokenId(context)

        //initialize and show a progress dialog to the user
        val progressDialog = util.initProgress(context, getString(R.string.progMessage))
        progressDialog.show()

        //start the call
        UserApiObj.retrofitService.updateUser(user!!, bearerToken)?.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                val responseCode = response.code()
                if (responseCode in 200..299 && response.body() != null) {
                    //get user successfully
                    progressDialog.dismiss()
                    Toast.makeText(context, getString(R.string.data_saved), Toast.LENGTH_LONG).show()


                } else if (responseCode == 438) {
                    //user not saved successfully
                    Toast.makeText(context, getString(R.string.user_not_found), Toast.LENGTH_LONG).show()
                    Timber.e(getString(R.string.user_not_found))
                    progressDialog.dismiss()
                } else {
                    //user not saved successfully
                    Toast.makeText(context, getString(R.string.else_on_repsonse), Toast.LENGTH_LONG).show()
                    Timber.e(getString(R.string.else_on_repsonse))
                    progressDialog.dismiss()

                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Toast.makeText(context, getString(R.string.failure_happened), Toast.LENGTH_LONG).show()
                Timber.e("$t")
                progressDialog.dismiss()
            }
        })
    }

    override fun onDestroyView() {
        // this to save user data before destroy fragment or replace ir
        // (when select another fragment from bottom navigation view)
        activity!!.intent.putExtra("user", user) // saving user object.

        super.onDestroyView()
        _binding = null
    }

}
