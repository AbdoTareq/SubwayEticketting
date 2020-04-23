package com.abdotareq.subway_e_ticketing.ui.fragment.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.abdotareq.subway_e_ticketing.R
import com.abdotareq.subway_e_ticketing.databinding.FragmentChangePassBinding
import com.abdotareq.subway_e_ticketing.model.ErrorStatus.Codes.NoNetworkException
import com.abdotareq.subway_e_ticketing.model.ErrorStatus.Codes.getErrorMessage
import com.abdotareq.subway_e_ticketing.model.User
import com.abdotareq.subway_e_ticketing.network.UserApiObj
import com.abdotareq.subway_e_ticketing.utility.util
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import timber.log.Timber

class ChangePassFragment : Fragment() {

    private var _binding: FragmentChangePassBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var passEt: EditText
    private lateinit var confirmEt: EditText
    private lateinit var confirmBtn: Button

    private var bearerToken: String = "Bearer "

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentChangePassBinding.inflate(inflater, container, false)
        val view = binding.root


        // take the token to send it with bearer in header to change pass

        val safeArgs: ChangePassFragmentArgs by navArgs()
        val token = safeArgs.token
        val mail = safeArgs.mail

        // take the token to send it with bearer in header to change pass
        bearerToken += token

        Toast.makeText(context, "header: $bearerToken", Toast.LENGTH_SHORT).show()

        passEt = binding.passChangePassEt
        confirmEt = binding.passChangeConfirmPassEt
        confirmBtn = binding.changePassConfirmBtn

        confirmBtn.setOnClickListener {
            confirmBtnClick(mail)
        }

        return view
    }

    /**
     * A method called to handle sign up button clicks
     */
    private fun confirmBtnClick(mail: String?) {
        //check for all inputs from user are correct
        if (!util.isValidPassword(passEt.text.toString())) {
            passEt.setText("")
            passEt.hint = getString(R.string.invalid_pass)
            passEt.setHintTextColor(getColor(context!!, R.color.primaryTextColor))
            return
        } else if (confirmEt.text.toString().isEmpty()
                || confirmEt.text.toString() != passEt.text.toString()) {
            confirmEt.setText("")
            confirmEt.hint = getString(R.string.fix_confirmPassWarning)
            confirmEt.setHintTextColor(getColor(context!!, R.color.primaryTextColor))
            return
        }

        //create MobileUser object and set it's attributes
        val user = User()
        user.email = mail
        user.password = passEt.text.toString()
        // this constructor iz wrong as it's for mail and otp verify
        // val user = User(mail, passEt.text.toString())


        Timber.e(user.toString())

        //sign up method that will call the web service
        changePassCall(user)


    }

    private fun changePassCall(user: User) {

        //initialize and show a progress dialog to the user
        val progressDialog = util.initProgress(context, getString(R.string.loading))
        progressDialog.show()
        //start the call
        UserApiObj.retrofitService.changePassCall(user, bearerToken)?.enqueue(object : retrofit2.Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                val responseCode = response.code()
                if (responseCode in 200..299 && response.body() != null) {
                    //pass changed successfully
                    progressDialog.dismiss()
                    findNavController().navigate(ChangePassFragmentDirections.actionChangePassFragmentToSignInFragment())
                    Toast.makeText(context, "Pass changed", Toast.LENGTH_SHORT).show()
                } else {
                    //user not saved successfully
                    progressDialog.dismiss()
                    Timber.e("response.code:    $responseCode")
                    Toast.makeText(context, getErrorMessage(responseCode, activity!!.application), Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Timber.e("err:    $t")
                progressDialog.dismiss()
                Toast.makeText(context, getErrorMessage(NoNetworkException, activity!!.application), Toast.LENGTH_LONG).show()

            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}