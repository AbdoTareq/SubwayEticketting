package com.abdotareq.subway_e_ticketing.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.content.ContextCompat.getColor
import com.abdotareq.subway_e_ticketing.R
import com.abdotareq.subway_e_ticketing.databinding.ChangePassDialogeBinding
import com.abdotareq.subway_e_ticketing.model.ErrorStatus
import com.abdotareq.subway_e_ticketing.model.ErrorStatus.Codes.NoNetworkException
import com.abdotareq.subway_e_ticketing.model.ErrorStatus.Codes.getErrorMessage
import com.abdotareq.subway_e_ticketing.model.UserPassword
import com.abdotareq.subway_e_ticketing.network.UserApiObj
import com.abdotareq.subway_e_ticketing.utility.SharedPreferenceUtil
import com.abdotareq.subway_e_ticketing.utility.util
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import timber.log.Timber

class ChangePassDialogFragment(mailToString: String) : AppCompatDialogFragment() {

    private lateinit var binding: ChangePassDialogeBinding

    private var mail: String = mailToString

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ChangePassDialogeBinding.inflate(inflater, container, false)
        val view = binding.root


        binding.confirmBtn.setOnClickListener {
            Timber.i("${binding.oldPass.text}  new: ${binding.newPassword.text}   CONFIRM: ${binding.confirmNewPassword.text}")

            checkPass()


        }

        return view
    }

    private fun checkPass() {

        //check for all inputs from user are correct
        if (!util.isValidPassword(binding.oldPass.text.toString())) {
            binding.first.error = getString(R.string.pass_fix)
            binding.first.requestFocus()
            return
        } else if (!util.isValidPassword(binding.newPassword.text.toString())) {
            binding.second.error = getString(R.string.pass_fix)
            binding.second.requestFocus()
            return
        } else if (binding.confirmNewPassword.text.toString().isEmpty()
                || binding.confirmNewPassword.text.toString() != binding.newPassword.text.toString()) {
            binding.third.error = getString(R.string.fix_confirmPassWarning)
            binding.third.requestFocus()
            return
        }

        changePassCall(binding.oldPass.text.toString(), binding.newPassword.text.toString())

    }

    private fun changePassCall(oldPass: String, newPass: String) {

        var bearerToken = "Bearer "

        bearerToken += SharedPreferenceUtil.getSharedPrefsTokenId(context)

        //initialize and show a progress dialog to the user
        val progressDialog = util.initProgress(context, getString(R.string.loading))
        progressDialog.show()

        val userPass = UserPassword(oldPass, newPass, mail)

        Timber.i("$userPass")

        //start the call
        UserApiObj.retrofitService.updatePass(userPass, bearerToken)?.enqueue(object : retrofit2.Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                val responseCode = response.code()
                if (responseCode in 200..299 && response.body() != null) {
                    //pass changed successfully
                    Toast.makeText(context, "Pass changed", Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()
                    // to close pass dialog
                    dismiss()

                } else {
                    //user not saved successfully
                    progressDialog.dismiss()
                    Timber.i("responseCode: $responseCode ")
                    Toast.makeText(context, getErrorMessage(responseCode, activity!!.application), Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(context, getErrorMessage(NoNetworkException, activity!!.application), Toast.LENGTH_LONG).show()
            }
        })

    }


}